package Models;

import java.sql.Date;

public class Compte {

    private int idCompte;
    private int idClient;
    private String numeroCompte;
    private String typeCompte;
    private double soldeActuel;
    private double tauxInteret;
    private Date dateOuverture;
    private Date dateCloture;
    private String statut;
    private int duree;
    private double montantPeriodique;
    private String frequence;

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public double getSoldeActuel() {
        return soldeActuel;
    }

    public void setSoldeActuel(double soldeActuel) {
        this.soldeActuel = soldeActuel;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public double getMontantPeriodique() {
        return montantPeriodique;
    }

    public void setMontantPeriodique(double montantPeriodique) {
        this.montantPeriodique = montantPeriodique;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }
}
