package DAO;

import Models.Utilisateur;

public class Session {
    
    private static Utilisateur utilisateurConnecte = null;
    
    public static Utilisateur getUtilisateur() { 
        return utilisateurConnecte; 
    }
    
    public static void setUtilisateur(Utilisateur u) { 
        utilisateurConnecte = u; 
    }
    
    public static void deconnecter() { 
        utilisateurConnecte = null; 
    }
    
    public static boolean estConnecte() { 
        return utilisateurConnecte != null; 
    }
}