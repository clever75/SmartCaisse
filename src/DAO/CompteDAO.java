package DAO;

import Models.Compte;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteDAO {

    private Connection conn;

    public CompteDAO() {
        conn = Connexion.getConnection();
    }

    // ── Ajouter un compte ──
    public boolean ajouter(Compte compte) {
        String sql = "INSERT INTO Compte (idClient, numeroCompte, typeCompte, "
                + "soldeActuel, tauxInteret, dateOuverture, duree, "
                + "montantPeriodique, frequence, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Actif')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, compte.getIdClient());
            ps.setString(2, compte.getNumeroCompte());
            ps.setString(3, compte.getTypeCompte());
            ps.setDouble(4, compte.getSoldeActuel());
            ps.setDouble(5, compte.getTauxInteret());
            ps.setDate(6, compte.getDateOuverture());
            ps.setInt(7, compte.getDuree());
            ps.setDouble(8, compte.getMontantPeriodique());
            ps.setString(9, compte.getFrequence());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout compte : " + e.getMessage());
            return false;
        }
    }

    // ── Modifier solde ──
    public boolean modifierSolde(int idCompte, double nouveauSolde) {
        String sql = "UPDATE Compte SET soldeActuel = ? WHERE idCompte = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, nouveauSolde);
            ps.setInt(2, idCompte);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification solde : " + e.getMessage());
            return false;
        }
    }

    // ── Clôturer un compte ──
    public boolean cloturer(int idCompte) {
        // 1. Vérifier prêts actifs
        if (new PretDAO().existePretsActifsParCompte(idCompte)) {
            System.out.println("Clôture refusée : prêts actifs");
            return false;
        }
        // 2. Vérifier solde
        Compte c = chercher(idCompte);
        if (c != null && c.getSoldeActuel() > 0) {
            System.out.println("Clôture refusée : solde non nul");
            return false;
        }
        // 3. Clôturer
        String sql = "UPDATE Compte SET statut = 'Clôturé', "
                + "dateCloture = CURDATE() WHERE idCompte = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCompte);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur clôture : " + e.getMessage());
            return false;
        }
    }

    // ── Chercher par ID ──
    public Compte chercher(int idCompte) {
        String sql = "SELECT * FROM Compte WHERE idCompte = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCompte);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            System.out.println("Erreur recherche compte : " + e.getMessage());
        }
        return null;
    }

    // ── Chercher par numéro ──
    public Compte chercherParNumero(String numeroCompte) {
        String sql = "SELECT * FROM Compte WHERE numeroCompte = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numeroCompte);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) {
            System.out.println("Erreur recherche compte : " + e.getMessage());
        }
        return null;
    }

    // ── Vérifier si numéro de compte existe ──
    public boolean numeroCompteExiste(String numeroCompte) {
        String sql = "SELECT COUNT(*) FROM Compte WHERE numeroCompte = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numeroCompte);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Erreur vérif numéro : " + e.getMessage());
        }
        return false;
    }

    // ── Lister par client ──
    public List<Compte> listerParClient(int idClient) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM Compte WHERE idClient = ? "
                + "AND statut = 'Actif'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) comptes.add(mapper(rs));
        } catch (SQLException e) {
            System.out.println("Erreur liste comptes : " + e.getMessage());
        }
        return comptes;
    }

    // ── Lister actifs ──
    public List<Compte> listerActifs() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM Compte WHERE statut = 'Actif' "
                + "ORDER BY dateOuverture DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) comptes.add(mapper(rs));
        } catch (SQLException e) {
            System.out.println("Erreur liste actifs : " + e.getMessage());
        }
        return comptes;
    }

    // ── Lister clôturés ──
    public List<Compte> listerCloturer() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM Compte WHERE statut = 'Clôturé' "
                + "ORDER BY dateCloture DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) comptes.add(mapper(rs));
        } catch (SQLException e) {
            System.out.println("Erreur liste clôturés : " + e.getMessage());
        }
        return comptes;
    }

    public boolean clientADejaCompteDeType(int idClient, String type) {
    String sql = "SELECT COUNT(*) FROM compte WHERE idClient = ? "
               + "AND typeCompte = ? AND statut = 'Actif'";
    try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClient);
        ps.setString(2, type);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    // ── Mapper ResultSet → Compte ──
    private Compte mapper(ResultSet rs) throws SQLException {
        Compte c = new Compte();
        c.setIdCompte(rs.getInt("idCompte"));
        c.setIdClient(rs.getInt("idClient"));
        c.setNumeroCompte(rs.getString("numeroCompte"));
        c.setTypeCompte(rs.getString("typeCompte"));
        c.setSoldeActuel(rs.getDouble("soldeActuel"));
        c.setTauxInteret(rs.getDouble("tauxInteret"));
        c.setDateOuverture(rs.getDate("dateOuverture"));
        c.setDateCloture(rs.getDate("dateCloture"));
        c.setStatut(rs.getString("statut"));
        c.setDuree(rs.getInt("duree"));
        c.setMontantPeriodique(rs.getDouble("montantPeriodique"));
        c.setFrequence(rs.getString("frequence"));
        return c;
    }
}