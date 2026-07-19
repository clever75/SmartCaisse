package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
        /*private static Connection conn=null;
         public static Connection getConnexion() throws Exception{
        try{
            String url="jdbc:mysql://localhost:3306/smartcaisse";
            String user="root";
            String password="";
            
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,user,password);
            
            System.out.println("Bien réussie ");
        }catch(Exception e){
            System.out.println("Erreur connexion :"+ e.getMessage());
            
        }
        return conn;
    }
         public static void main(String[] args) throws Exception{
        
      Connexion.getConnexion();
      
    }*/
    private static final String URL =
            "jdbc:mysql://localhost:3306/smartcaisse"
            + "?useSSL=false"
            + "&serverTimezone=UTC"
            + "&autoReconnect=true"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Chaque appel crée une NOUVELLE connexion
   public static Connection getConnexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL introuvable : "
                    + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Erreur connexion : "
                    + e.getMessage());
            return null;
        }
    }

    // Garder getConnection() pour compatibilité
    public static Connection getConnection() {
        return getConnexion();
    }
}