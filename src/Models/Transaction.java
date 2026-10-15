package Models;

import java.sql.Timestamp;

public class Transaction {
    
    private int idTransaction;
    private int idCompte;
    private int idUser;
    private String type;
    private double montant;
    private Timestamp dateHeure;
    private String moyenPaiement;
    private String statut;
    
    public int getIdTransaction() { return idTransaction; }
    public void setIdTransaction(int idTransaction) { this.idTransaction = idTransaction; }
    
    public int getIdCompte() { return idCompte; }
    public void setIdCompte(int idCompte) { this.idCompte = idCompte; }
    
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    
    public Timestamp getDateHeure() { return dateHeure; }
    public void setDateHeure(Timestamp dateHeure) { this.dateHeure = dateHeure; }
    
    public String getMoyenPaiement() { return moyenPaiement; }
    public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement = moyenPaiement; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}