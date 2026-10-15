package DAO;

import Models.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class ClientDAO {

    private Connection conn;

     public ClientDAO() {
        conn = Connexion.getConnection();
    }

    // ── Ajouter un client ──
    public boolean ajouter(Client client) {
        String sql = "INSERT INTO Client (nom, prenom, dateNaissance, sexe, "
                + "situationMat, telephone, email, adresse, numCarteIdentite, "
                + "typePiece, profession, revenuMensuel, statut, dateInscription) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Actif', CURDATE())";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setDate(3, client.getDateNaissance());
            ps.setString(4, client.getSexe());
            ps.setString(5, client.getSituationMat());
            ps.setString(6, client.getTelephone());
            ps.setString(7, client.getEmail());
            ps.setString(8, client.getAdresse());
            ps.setString(9, client.getNumCarteIdentite());
            ps.setString(10, client.getTypePiece());
            ps.setString(11, client.getProfession());
            ps.setDouble(12, client.getRevenuMensuel());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout client : " + e.getMessage());
            return false;
        }
    }

    // ── Vérifier doublon client (nom + prénom + date naissance) ──
public boolean clientDoublonExiste(String nom, String prenom,
        java.sql.Date dateNaissance, int idClientExclu) {
    String sql = "SELECT COUNT(*) FROM Client "
            + "WHERE UPPER(TRIM(nom))=UPPER(TRIM(?)) "
            + "AND UPPER(TRIM(prenom))=UPPER(TRIM(?)) "
            + "AND idClient != ?"
            + (dateNaissance != null ? " AND dateNaissance=?" : "");
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nom.trim());
        ps.setString(2, prenom.trim());
        ps.setInt(3, idClientExclu);
        if (dateNaissance != null) ps.setDate(4, dateNaissance);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (SQLException e) {
        System.out.println("Erreur doublon : " + e.getMessage());
    }
    return false;
}
    // ── Modifier un client ──
    public boolean modifier(Client client) {
        String sql = "UPDATE Client SET nom=?, prenom=?, dateNaissance=?, sexe=?, "
                + "situationMat=?, telephone=?, email=?, adresse=?, "
                + "numCarteIdentite=?, typePiece=?, profession=?, revenuMensuel=? "
                + "WHERE idClient=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setDate(3, client.getDateNaissance());
            ps.setString(4, client.getSexe());
            ps.setString(5, client.getSituationMat());
            ps.setString(6, client.getTelephone());
            ps.setString(7, client.getEmail());
            ps.setString(8, client.getAdresse());
            ps.setString(9, client.getNumCarteIdentite());
            ps.setString(10, client.getTypePiece());
            ps.setString(11, client.getProfession());
            ps.setDouble(12, client.getRevenuMensuel());
            ps.setInt(13, client.getIdClient());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification client : " + e.getMessage());
            return false;
        }
    }

    // ── Désactiver un client (pas de vraie suppression) ──
   public boolean supprimer(int idClient) {
    String sqlVerifPrets = "SELECT COUNT(*) FROM pret "
            + "WHERE idCompte IN (SELECT idCompte FROM compte WHERE idClient=?) "
            + "AND statut IN ('En cours','En attente','En retard')";
    String sqlVerifTrans = "SELECT COUNT(*) FROM transaction "
            + "WHERE idCompte IN (SELECT idCompte FROM compte WHERE idClient=?)";
    String sqlDeleteComptes = "DELETE FROM compte WHERE idClient=?";
    String sqlDeleteClient  = "DELETE FROM client WHERE idClient=?";
    String sqlDisableClient = "UPDATE client SET statut='Inactif' WHERE idClient=?";
    String sqlCloseComptes  = "UPDATE compte SET statut='Clôturé' "
            + "WHERE idClient=? AND statut='Actif'";
    try (java.sql.Connection c = DAO.Connexion.getConnection()) {
        // 1. Prêts actifs → bloquer
        try (PreparedStatement ps = c.prepareStatement(sqlVerifPrets)) {
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return false;
        }
        // 2. Transactions → choisir entre suppression ou désactivation
        int nbTrans = 0;
        try (PreparedStatement ps = c.prepareStatement(sqlVerifTrans)) {
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) nbTrans = rs.getInt(1);
        }
        if (nbTrans == 0) {
            // Aucune opération → suppression physique
            try (PreparedStatement ps = c.prepareStatement(sqlDeleteComptes)) {
                ps.setInt(1, idClient); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(sqlDeleteClient)) {
                ps.setInt(1, idClient); ps.executeUpdate();
            }
        } else {
            // A des opérations → désactivation logique
            try (PreparedStatement ps = c.prepareStatement(sqlDisableClient)) {
                ps.setInt(1, idClient); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(sqlCloseComptes)) {
                ps.setInt(1, idClient); ps.executeUpdate();
            }
        }
        return true;
    } catch (Exception e) {
        System.out.println("Erreur supprimer client : " + e.getMessage());
        return false;
    }
}

// Méthode utilitaire utilisée par ClientsPanel
public boolean aDesOperations(int idClient) {
    String sql = "SELECT COUNT(*) FROM transaction "
            + "WHERE idCompte IN (SELECT idCompte FROM compte WHERE idClient=?)";
    try (java.sql.Connection c = DAO.Connexion.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, idClient);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        System.out.println("Erreur aDesOperations : " + e.getMessage());
    }
    return false;
}

    // ── Lister tous les clients (actifs ET inactifs) ──
    public List<Client> listerTous() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client ORDER BY dateInscription DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                clients.add(mapper(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur liste clients : " + e.getMessage());
        }
        return clients;
    }

    // ── Chercher un client par ID ──
    public Client chercher(int idClient) {
        String sql = "SELECT * FROM Client WHERE idClient = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapper(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur recherche client : " + e.getMessage());
        }
        return null;
    }

    // ── Vérifier si un téléphone existe déjà ──
    public boolean telephoneExiste(String telephone, int idClientExclu) {
        String sql = "SELECT COUNT(*) FROM Client WHERE telephone = ? AND idClient != ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, telephone);
            ps.setInt(2, idClientExclu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur vérif téléphone : " + e.getMessage());
        }
        return false;
    }

    // ── Vérifier si un numéro de pièce existe déjà ──
    public boolean numeroPieceExiste(String numeroPiece, int idClientExclu) {
        if (numeroPiece == null || numeroPiece.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM Client "
                + "WHERE numCarteIdentite = ? AND idClient != ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numeroPiece.trim());
            ps.setInt(2, idClientExclu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur vérif pièce : " + e.getMessage());
        }
        return false;
    }
    // ── Réactiver un client ──

    public boolean reactiver(int idClient) {
        String sql = "UPDATE Client SET statut = 'Actif' WHERE idClient = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idClient);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur réactivation : " + e.getMessage());
            return false;
        }
    }

    // ── Mapper ResultSet → Client ──
    private Client mapper(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setIdClient(rs.getInt("idClient"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setDateNaissance(rs.getDate("dateNaissance"));
        c.setSexe(rs.getString("sexe"));
        c.setSituationMat(rs.getString("situationMat"));
        c.setTelephone(rs.getString("telephone"));
        c.setEmail(rs.getString("email"));
        c.setAdresse(rs.getString("adresse"));
        c.setNumCarteIdentite(rs.getString("numCarteIdentite"));
        c.setTypePiece(rs.getString("typePiece"));
        c.setProfession(rs.getString("profession"));
        c.setRevenuMensuel(rs.getDouble("revenuMensuel"));
        c.setDateInscription(rs.getDate("dateInscription"));
        c.setStatut(rs.getString("statut"));
        return c;
    }
    public int compterPretsActifs(int idClient) {
    String sql = "SELECT COUNT(*) FROM pret "
            + "WHERE idCompte IN ("
            + "  SELECT idCompte FROM compte "
            + "  WHERE idClient = ?"
            + ") AND statut IN ('En cours', 'En attente')";
    try (java.sql.Connection conn = DAO.Connexion.getConnection();
            java.sql.PreparedStatement ps =
                    conn.prepareStatement(sql)) {
        ps.setInt(1, idClient);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Erreur vérif prêts : "
                + e.getMessage());
    }
    return 0;
}
}
