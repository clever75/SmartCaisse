package DAO;

import Models.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    
    private Connection conn;
    
    public UtilisateurDAO() {
        conn = Connexion.getConnection();
    }
    
    // Ajouter un utilisateur
    public boolean ajouter(Utilisateur user) {
        String sql = "INSERT INTO Utilisateur (nom, prenom, nomUtilisateur, " +
                     "motDePasse, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getNomUtilisateur());
            ps.setString(4, user.getMotDePasse());
            ps.setString(5, user.getRole());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout utilisateur : " + e.getMessage());
            return false;
        }
    }
    
    // Connexion utilisateur
    public Utilisateur connecter(String nomUtilisateur, String motDePasse) {
        String sql = "SELECT * FROM Utilisateur WHERE nomUtilisateur = ? " +
                     "AND motDePasse = ? AND statut = 'Actif'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nomUtilisateur);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setIdUser(rs.getInt("idUser"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setNomUtilisateur(rs.getString("nomUtilisateur"));
                u.setRole(rs.getString("role"));
                u.setStatut(rs.getString("statut"));
                return u;
            }
        } catch (SQLException e) {
            System.out.println("Erreur connexion : " + e.getMessage());
        }
        return null;
    }
    
    // Lister tous les utilisateurs
    public List<Utilisateur> listerTous() {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur WHERE statut = 'Actif'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setIdUser(rs.getInt("idUser"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setNomUtilisateur(rs.getString("nomUtilisateur"));
                u.setRole(rs.getString("role"));
                u.setStatut(rs.getString("statut"));
                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur liste utilisateurs : " + e.getMessage());
        }
        return users;
    }
    
    // Modifier mot de passe
    public boolean modifierMotDePasse(int idUser, String nouveauMdp) {
        String sql = "UPDATE Utilisateur SET motDePasse = ? WHERE idUser = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nouveauMdp);
            ps.setInt(2, idUser);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification mdp : " + e.getMessage());
            return false;
        }
    }
    
    // Désactiver un utilisateur
    public boolean desactiver(int idUser) {
        String sql = "UPDATE Utilisateur SET statut = 'Inactif' WHERE idUser = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur désactivation : " + e.getMessage());
            return false;
        }
    }
}