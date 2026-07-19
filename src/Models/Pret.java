package Models;

import java.sql.Date;

public class Pret {

    private int idPret;
    private int idCompte;
    private double montantPrincipal;
    private double tauxInteret;
    private int dureeMois;
    private Date dateDebut;
    private Date dateFinPrevue;
    private double montantRembourse;
    private String statut;
    private String garantie;      // ← AJOUT : type de garantie
    private String motifRejet;    // ← AJOUT : si prêt rejeté

    // ── Getters / Setters ──
    public int getIdPret() { return idPret; }
    public void setIdPret(int idPret) { this.idPret = idPret; }

    public int getIdCompte() { return idCompte; }
    public void setIdCompte(int idCompte) { this.idCompte = idCompte; }

    public double getMontantPrincipal() { return montantPrincipal; }
    public void setMontantPrincipal(double montantPrincipal) {
        this.montantPrincipal = montantPrincipal;
    }

    public double getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public int getDureeMois() { return dureeMois; }
    public void setDureeMois(int dureeMois) { this.dureeMois = dureeMois; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFinPrevue() { return dateFinPrevue; }
    public void setDateFinPrevue(Date dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }

    public double getMontantRembourse() { return montantRembourse; }
    public void setMontantRembourse(double montantRembourse) {
        this.montantRembourse = montantRembourse;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getGarantie() { return garantie; }
    public void setGarantie(String garantie) { this.garantie = garantie; }

    public String getMotifRejet() { return motifRejet; }
    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    // ── Méthodes calculées ──

    /** Montant total à rembourser (capital + intérêts) */
    public double getMontantTotal() {
        double interets = montantPrincipal * tauxInteret
                * dureeMois / 1200.0;
        return montantPrincipal + interets;
    }

    /** Mensualité fixe */
    public double getMontantEcheance() {
        if (dureeMois <= 0) return 0;
        return getMontantTotal() / dureeMois;
    }

    /** Montant restant à rembourser */
    public double getMontantRestant() {
        return Math.max(0, getMontantTotal() - montantRembourse);
    }

    /** Nombre d'échéances payées */
    public int getNombreEcheancesPayees() {
        double mensualite = getMontantEcheance();
        if (mensualite <= 0) return 0;
        return (int) Math.min(
                Math.round(montantRembourse / mensualite), dureeMois);
    }

    /** Nombre d'échéances restantes */
    public int getNombreEcheancesRestantes() {
        return Math.max(0,
                dureeMois - getNombreEcheancesPayees());
    }

    /** Pourcentage remboursé */
    public double getPourcentageRembourse() {
        double total = getMontantTotal();
        if (total <= 0) return 0;
        return Math.min(100, (montantRembourse / total) * 100);
    }

    /** Calcul pénalité de retard — 5% du restant par mois de retard */
    public double getPenalite() {
        if (!"En retard".equals(statut)) return 0;
        if (dateFinPrevue == null) return 0;

        long diffMillis = System.currentTimeMillis()
                - dateFinPrevue.getTime();
        long joursRetard = diffMillis / (1000L * 60 * 60 * 24);
        int moisRetard = (int) Math.max(1, joursRetard / 30);

        return getMontantRestant() * 0.05 * moisRetard;
    }

    /** Montant anticipé avec remise 50% sur intérêts restants */
    public double getMontantAnticipe() {
        double mensualite = getMontantEcheance();
        int echeancesPayees = getNombreEcheancesPayees();
        int echeancesRestantes = Math.max(0,
                dureeMois - echeancesPayees);

        double interetsRestants = montantPrincipal * tauxInteret
                * echeancesRestantes / 1200.0;
        double remise = interetsRestants * 0.5;

        return Math.max(0, getMontantRestant() - remise);
    }
}