package DAO;

import Models.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private Connection conn;

    public TransactionDAO() {
        conn = Connexion.getConnection();
    }

    // ── Ajouter une transaction ──
    public boolean ajouter(Transaction transaction) {
        String sql = "INSERT INTO Transaction (idCompte, type, "
                + "montant, dateHeure, moyenPaiement, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, transaction.getIdCompte());
            ps.setString(2, transaction.getType());
            ps.setDouble(3, transaction.getMontant());
            ps.setTimestamp(4, transaction.getDateHeure() != null
                    ? transaction.getDateHeure()
                    : new java.sql.Timestamp(System.currentTimeMillis()));
            if (transaction.getMoyenPaiement() != null) {
                ps.setString(5, transaction.getMoyenPaiement());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }
            ps.setString(6, transaction.getStatut());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout transaction : "
                    + e.getMessage());
            return false;
        }
    }

    // ── Lister toutes les transactions ──
    public List<Transaction> listerTous() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transaction ORDER BY dateHeure DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) transactions.add(mapper(rs));
        } catch (SQLException e) {
            System.out.println("Erreur liste transactions : "
                    + e.getMessage());
        }
        return transactions;
    }

    // ── Lister transactions d'un compte ──
    public List<Transaction> listerParCompte(int idCompte) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transaction WHERE idCompte = ? "
                + "ORDER BY dateHeure DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCompte);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) transactions.add(mapper(rs));
        } catch (SQLException e) {
            System.out.println("Erreur transactions compte : "
                    + e.getMessage());
        }
        return transactions;
    }

    // ── Lister 5 dernières transactions ──
    public List<Transaction> listerDernieres() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transaction "
                + "ORDER BY dateHeure DESC LIMIT 5";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) transactions.add(mapper(rs));
        } catch (SQLException e) {
            System.out.println("Erreur dernières transactions : "
                    + e.getMessage());
        }
        return transactions;
    }

    // ── Stats du mois courant ──
    public double[] statsduMois() {
        double[] stats = {0, 0, 0, 0};
        String sql = "SELECT type, SUM(montant) as total "
                + "FROM Transaction "
                + "WHERE MONTH(dateHeure) = MONTH(CURDATE()) "
                + "AND YEAR(dateHeure) = YEAR(CURDATE()) "
                + "AND statut = 'Validé' "
                + "GROUP BY type";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String type = rs.getString("type");
                double total = rs.getDouble("total");
                switch (type) {
                    case "Dépôt épargne",
                         "Dépôt initial" -> stats[0] += total;
                    case "Retrait épargne" -> stats[1] += total;
                    case "Décaissement" -> stats[2] += total;
                    case "Remboursement",
                         "Remboursement anticipé" -> stats[3] += total;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur stats mois : " + e.getMessage());
        }
        return stats;
    }

    // ── Modifier statut ──
    public boolean modifierStatut(int idTransaction, String statut) {
        String sql = "UPDATE Transaction SET statut = ? "
                + "WHERE idTransaction = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            ps.setInt(2, idTransaction);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification statut : "
                    + e.getMessage());
            return false;
        }
    }

    // ── Mapper ResultSet → Transaction ──
    private Transaction mapper(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setIdTransaction(rs.getInt("idTransaction"));
        t.setIdCompte(rs.getInt("idCompte"));
        t.setType(rs.getString("type"));
        t.setMontant(rs.getDouble("montant"));
        t.setDateHeure(rs.getTimestamp("dateHeure"));
        t.setMoyenPaiement(rs.getString("moyenPaiement"));
        t.setStatut(rs.getString("statut"));
        return t;
    }
}