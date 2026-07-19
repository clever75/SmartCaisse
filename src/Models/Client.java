package Models;

import java.sql.Date;

public class Client {

    private int idClient;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String sexe;
    private String situationMat;
    private String telephone;
    private String email;
    private String adresse;
    private String numCarteIdentite;
    private String typePiece;
    private String profession;
    private double revenuMensuel;
    private String numeroCompte;
    private String statut;
    private java.sql.Date dateInscription;

    

    

    // Getters et Setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getSituationMat() {
        return situationMat;
    }

    public void setSituationMat(String situationMat) {
        this.situationMat = situationMat;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumCarteIdentite() {
        return numCarteIdentite;
    }

    public void setNumCarteIdentite(String numCarteIdentite) {
        this.numCarteIdentite = numCarteIdentite;
    }

    public String getTypePiece() {
        return typePiece;
    }

    public void setTypePiece(String typePiece) {
        this.typePiece = typePiece;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public double getRevenuMensuel() {
        return revenuMensuel;
    }

    public void setRevenuMensuel(double revenuMensuel) {
        this.revenuMensuel = revenuMensuel;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    public java.sql.Date getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(java.sql.Date dateInscription) {
        this.dateInscription = dateInscription;
    }
}
