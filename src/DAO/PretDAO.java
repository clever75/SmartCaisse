package DAO;

import Models.Pret;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PretDAO {

    private Connection conn;

    public PretDAO() {
        conn = Connexion.getConnection();
    }

    // ── Ajouter un prêt ──
    public boolean ajouter(Pret pret) {
        String sql = "INSERT INTO Pret (idCompte, montantPrincipal, "
                + "tauxInteret, dureeMois, dateDebut, dateFinPrevue, "
                + "montantRembourse, statut, garantie) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pret.getIdCompte());
            ps.setDouble(2, pret.getMontantPrincipal());
            ps.setDouble(3, pret.getTauxInteret());
            ps.setInt(4, pret.getDureeMois());
            ps.setDate(5, pret.getDateDebut());
            ps.setDate(6, pret.getDateFinPrevue());
            ps.setDouble(7, pret.getMontantRembourse());
            ps.setString(8, pret.getStatut());
            ps.setString(9, pret.getGarantie());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout prêt : " + e.getMessage());
            return false;
        }
    }

    // ── Chercher un prêt par ID ──
    public Pret chercher(int idPret) {
        String sql = "SELECT * FROM Pret WHERE idPret = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPret);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapper(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur chercher prêt : " + e.getMessage());
        }
        return null;
    }

    // ── Lister tous les prêts ──
    public List<Pret> listerTous() {
        List<Pret> prets = new ArrayList<>();
        String sql = "SELECT * FROM Pret ORDER BY dateDebut DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                prets.add(mapper(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur liste prêts : " + e.getMessage());
        }
        return prets;
    }

    // ── Lister prêts d'un compte ──
    public List<Pret> listerParCompte(int idCompte) {
        List<Pret> prets = new ArrayList<>();
        String sql = "SELECT * FROM Pret WHERE idCompte = ? "
                + "ORDER BY dateDebut DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCompte);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prets.add(mapper(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur liste prêts compte : " + e.getMessage());
        }
        return prets;
    }

    // ── Lister prêts en retard ──
    public List<Pret> listerEnRetard() {
        List<Pret> prets = new ArrayList<>();
        String sql = "SELECT * FROM Pret WHERE statut = 'En retard'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                prets.add(mapper(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur prêts en retard : " + e.getMessage());
        }
        return prets;
    }

    // ── Modifier statut ──
    public boolean modifierStatut(int idPret, String statut) {
        String sql = "UPDATE Pret SET statut = ? WHERE idPret = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            ps.setInt(2, idPret);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification statut : " + e.getMessage());
            return false;
        }
    }

    // ── Mettre à jour remboursement ──
    public boolean mettreAJourRemboursement(int idPret, double montant) {
        String sql = "UPDATE Pret SET montantRembourse = "
                + "montantRembourse + ? WHERE idPret = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, montant);
            ps.setInt(2, idPret);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur remboursement : " + e.getMessage());
            return false;
        }
    }

    

    // ── Calculer pénalité de retard ──
    // Règle : 5% du montant restant par mois de retard
    public double calculerPenalite(int idPret) {
        Pret pret = chercher(idPret);
        if (pret == null) {
            return 0;
        }
        if (!"En retard".equals(pret.getStatut())) {
            return 0;
        }
        if (pret.getDateFinPrevue() == null) {
            return 0;
        }

        // Nombre de mois de retard
        long diffMillis = System.currentTimeMillis()
                - pret.getDateFinPrevue().getTime();
        long joursRetard = diffMillis / (1000L * 60 * 60 * 24);
        int moisRetard = (int) Math.max(1, joursRetard / 30);

        double interets = pret.getMontantPrincipal()
                * pret.getTauxInteret()
                * pret.getDureeMois() / 1200.0;
        double total = pret.getMontantPrincipal() + interets;
        double resteTotal = Math.max(0,
                total - pret.getMontantRembourse());

        // 5% par mois de retard sur le montant restant
        return resteTotal * 0.05 * moisRetard;
    }

    // ── Vérifier éligibilité au prêt ──
    public String verifierEligibilite(int idCompte) {
        // Retourne null si éligible, sinon le motif de refus

        // 1. Prêt déjà en cours
        if (existePretsActifsParCompte(idCompte)) {
            return "Ce compte a déjà un prêt en cours.";
        }

        // 2. Ancienneté 3 mois
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        Models.Compte compte = compteDao.chercher(idCompte);
        if (compte != null && compte.getDateOuverture() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(compte.getDateOuverture());
            cal.add(java.util.Calendar.MONTH, 3);
            if (cal.getTime().after(new java.util.Date())) {
                java.text.SimpleDateFormat sdf
                        = new java.text.SimpleDateFormat("dd/MM/yyyy");
                return "Compte pas encore éligible.\n"
                        + "Date d'éligibilité : "
                        + sdf.format(cal.getTime());
            }
        }

        // 3. Solde minimum pour emprunter
        if (compte != null && compte.getSoldeActuel() < 5000) {
            return "Solde épargne insuffisant pour obtenir un prêt.\n"
                    + "Solde minimum requis : 5 000 F CFA";
        }

        return null; // Éligible
    }

    // ── Vérifier si prêts actifs sur un compte ──
    public boolean existePretsActifsParCompte(int idCompte) {
        String sql = "SELECT COUNT(*) FROM Pret WHERE idCompte = ? "
                + "AND statut IN ('En cours', 'En retard')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCompte);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur vérif prêts actifs : "
                    + e.getMessage());
        }
        return false;
    }

    // ── Alias ──
    public boolean aDejaUnPretEnCours(int idCompte) {
        return existePretsActifsParCompte(idCompte);
    }

    // ── Mettre à jour statuts automatiquement ──
    public void mettreAJourStatuts() {
        String sqlRetard = "UPDATE Pret SET statut = 'En retard' "
                + "WHERE statut = 'En cours' "
                + "AND dateFinPrevue < CURDATE()";
        String sqlRembourse = "UPDATE Pret SET statut = 'Remboursé' "
                + "WHERE statut IN ('En cours', 'En retard') "
                + "AND montantRembourse >= "
                + "((montantPrincipal + (montantPrincipal "
                + "* tauxInteret * dureeMois / 1200)) - 2.0)";
        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sqlRetard);
            st.executeUpdate(sqlRembourse);
        } catch (SQLException e) {
            System.out.println("Erreur mise à jour statuts : "
                    + e.getMessage());
        }
    }

    // ── Mapper ResultSet → Pret ──
    private Pret mapper(ResultSet rs) throws SQLException {
        Pret p = new Pret();
        p.setIdPret(rs.getInt("idPret"));
        p.setIdCompte(rs.getInt("idCompte"));
        p.setMontantPrincipal(rs.getDouble("montantPrincipal"));
        p.setTauxInteret(rs.getDouble("tauxInteret"));
        p.setDureeMois(rs.getInt("dureeMois"));
        p.setDateDebut(rs.getDate("dateDebut"));
        p.setDateFinPrevue(rs.getDate("dateFinPrevue"));
        p.setMontantRembourse(rs.getDouble("montantRembourse"));
        p.setStatut(rs.getString("statut"));
   
        try {
            p.setGarantie(rs.getString("garantie"));
        } catch (SQLException e) {
        }
        return p;
    }
}
