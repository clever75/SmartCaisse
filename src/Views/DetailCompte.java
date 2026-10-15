/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import Models.Transaction;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class DetailCompte extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DetailCompte.class.getName());

    /**
     * Creates new form DetailCompte
     */
    private int idCompteCourant;

    public DetailCompte(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(560,600);
        setLocationRelativeTo(parent);
        configurerStyle();

        btnFermer.addActionListener(e -> dispose());
        btnDepot.addActionListener(e -> faireDepot());
        btnRetrait.addActionListener(e -> faireRetrait());
        btnCloturer.addActionListener(e -> cloturerCompte());
    }

    private void configurerStyle() {
        // Boutons footer
        btnFermer.setBackground(new java.awt.Color(241, 245, 249));
        btnFermer.setForeground(new java.awt.Color(71, 85, 105));
        btnFermer.setBorderPainted(false);
        btnFermer.setFocusPainted(false);
        btnFermer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFermer.setFont(new java.awt.Font("Segoe UI",
                java.awt.Font.PLAIN, 13));

        btnDepot.setBackground(new java.awt.Color(220, 252, 231));
        btnDepot.setForeground(new java.awt.Color(21, 128, 61));
        btnDepot.setBorderPainted(false);
        btnDepot.setFocusPainted(false);
        btnDepot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDepot.setFont(new java.awt.Font("Segoe UI",
                java.awt.Font.BOLD, 13));

        btnRetrait.setBackground(new java.awt.Color(254, 243, 199));
        btnRetrait.setForeground(new java.awt.Color(146, 64, 14));
        btnRetrait.setBorderPainted(false);
        btnRetrait.setFocusPainted(false);
        btnRetrait.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRetrait.setFont(new java.awt.Font("Segoe UI",
                java.awt.Font.PLAIN, 13));

        btnCloturer.setBackground(new java.awt.Color(254, 226, 226));
        btnCloturer.setForeground(new java.awt.Color(220, 38, 38));
        btnCloturer.setBorderPainted(false);
        btnCloturer.setFocusPainted(false);
        btnCloturer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCloturer.setFont(new java.awt.Font("Segoe UI",
                java.awt.Font.PLAIN, 13));
    }

    public void chargerCompte(int idCompte) {
        idCompteCourant = idCompte;

        DAO.CompteDAO dao = new DAO.CompteDAO();
        Models.Compte compte = dao.chercher(idCompte);
        if (compte == null) {
            return;
        }

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Récupérer nom client
        DAO.ClientDAO clientDao = new DAO.ClientDAO();
        Models.Client client = clientDao.chercher(compte.getIdClient());
        String nomClient = client != null
                ? client.getNom() + " " + client.getPrenom() : "—";

        // Header
        lblNomClient.setText(nomClient);
        lblNumCompte.setText(compte.getNumeroCompte());
        lblBadgeType.setText(compte.getTypeCompte());

        // Badge statut
        if ("Actif".equals(compte.getStatut())) {
            lblBadgeStatut.setText("Actif");
            lblBadgeStatut.setBackground(new java.awt.Color(220, 252, 231));
            lblBadgeStatut.setForeground(new java.awt.Color(21, 128, 61));
        } else {
            lblBadgeStatut.setText("Clôturé");
            lblBadgeStatut.setBackground(new java.awt.Color(254, 226, 226));
            lblBadgeStatut.setForeground(new java.awt.Color(220, 38, 38));
        }

        // Solde
        lblSoldeVal.setText(String.format("%,.0f F CFA",
                compte.getSoldeActuel()));

        // Réinitialiser boutons
        btnDepot.setEnabled(true);
        btnRetrait.setEnabled(true);
        btnCloturer.setEnabled(true);
        btnDepot.setToolTipText(null);
        btnRetrait.setToolTipText(null);

        // Infos selon type
        switch (compte.getTypeCompte()) {
            case "Courant" -> {
                lblSoldeTitle.setText("Solde actuel");
                lblTauxTitle.setText("Taux d'intérêt");
                lblTauxVal.setText("—");
                valDateOuv.setText(compte.getDateOuverture() != null
                        ? sdf.format(compte.getDateOuverture()) : "—");
                lblInfo2Title.setText("Type");
                valInfo2.setText("Courant");
                lblInfo3Title.setText("Solde minimum");
                valInfo3.setText("5 000 F CFA");
            }

            case "À terme" -> {
                lblSoldeTitle.setText("Solde bloqué");
                lblTauxTitle.setText("Taux d'intérêt");
                lblTauxVal.setText(compte.getTauxInteret() + " %");
                valDateOuv.setText(compte.getDateOuverture() != null
                        ? sdf.format(compte.getDateOuverture()) : "—");
                lblInfo2Title.setText("Durée");
                valInfo2.setText(compte.getDuree() + " mois");

                // Date échéance
                if (compte.getDateOuverture() != null
                        && compte.getDuree() > 0) {
                    java.util.Calendar cal
                            = java.util.Calendar.getInstance();
                    cal.setTime(compte.getDateOuverture());
                    cal.add(java.util.Calendar.MONTH, compte.getDuree());
                    lblInfo3Title.setText("Date échéance");
                    valInfo3.setText(sdf.format(cal.getTime()));

                    // Vérifier si échéance atteinte
                    boolean echeanceAtteinte
                            = cal.getTime().before(new java.util.Date());
                    if (!echeanceAtteinte) {
                        // Pas encore à échéance — retrait bloqué
                        btnRetrait.setEnabled(false);
                        btnRetrait.setToolTipText(
                                "Retrait bloqué jusqu'au "
                                + sdf.format(cal.getTime()));
                        btnCloturer.setEnabled(false);
                        btnCloturer.setToolTipText(
                                "Clôture possible à partir du "
                                + sdf.format(cal.getTime()));
                    }
                } else {
                    lblInfo3Title.setText("Date échéance");
                    valInfo3.setText("—");
                }
            }

            case "Tontine" -> {
                lblSoldeTitle.setText("Cagnotte actuelle");
                lblTauxTitle.setText("Fréquence");
                lblTauxVal.setText(compte.getFrequence() != null
                        ? compte.getFrequence() : "—");
                valDateOuv.setText(compte.getDateOuverture() != null
                        ? sdf.format(compte.getDateOuverture()) : "—");
                lblInfo2Title.setText("Montant de référence");
                valInfo2.setText(compte.getMontantPeriodique() > 0
                        ? String.format("%,.0f F CFA",
                                compte.getMontantPeriodique()) : "—");
                lblInfo3Title.setText("Durée");
                valInfo3.setText(compte.getDuree() > 0
                        ? compte.getDuree() + " mois" : "—");

                // Pas de retrait sur Tontine
                btnRetrait.setEnabled(false);
                btnRetrait.setToolTipText(
                        "Retrait non autorisé sur une Tontine");
            }
        }

        // Tout désactiver si clôturé
        if ("Clôturé".equals(compte.getStatut())) {
            btnDepot.setEnabled(false);
            btnRetrait.setEnabled(false);
            btnCloturer.setEnabled(false);
            btnDepot.setToolTipText("Compte clôturé");
            btnCloturer.setToolTipText("Compte déjà clôturé");
        }

        chargerTransactions(idCompte);
    }

    private void chargerTransactions(int idCompte) {
        DAO.TransactionDAO dao = new DAO.TransactionDAO();
        java.util.List<Models.Transaction> transactions
                = dao.listerParCompte(idCompte);
        transPanel.removeAll();

        if (transactions.isEmpty()) {
            javax.swing.JLabel lblVide = new javax.swing.JLabel(
                    "Aucune transaction enregistrée");
            lblVide.setForeground(new java.awt.Color(100, 116, 139));
            lblVide.setFont(new java.awt.Font("Segoe UI",
                    java.awt.Font.ITALIC, 12));
            lblVide.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(12, 0, 0, 0));
            transPanel.add(lblVide);
        } else {
            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (Models.Transaction t : transactions) {
                javax.swing.JPanel row = new javax.swing.JPanel(
                        new java.awt.BorderLayout());
                row.setBackground(java.awt.Color.WHITE);
                row.setBorder(javax.swing.BorderFactory
                        .createMatteBorder(0, 0, 1, 0,
                                new java.awt.Color(243, 244, 246)));
                row.setMaximumSize(new java.awt.Dimension(9999, 52));
                row.setPreferredSize(new java.awt.Dimension(0, 52));

                // Gauche — type + date
                javax.swing.JPanel left = new javax.swing.JPanel();
                left.setLayout(new javax.swing.BoxLayout(left,
                        javax.swing.BoxLayout.Y_AXIS));
                left.setBackground(java.awt.Color.WHITE);
                left.setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(8, 0, 8, 0));

                javax.swing.JLabel lblType
                        = new javax.swing.JLabel(t.getType());
                lblType.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.BOLD, 13));
                lblType.setForeground(new java.awt.Color(15, 23, 42));

                javax.swing.JLabel lblDate = new javax.swing.JLabel(
                        t.getDateHeure() != null
                        ? sdf.format(t.getDateHeure()) : "—");
                lblDate.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.PLAIN, 11));
                lblDate.setForeground(new java.awt.Color(100, 116, 139));

                left.add(lblType);
                left.add(lblDate);

                // Droite — montant coloré
                String typeStr = t.getType().toLowerCase();
                java.awt.Color couleur;
                String signe;
                if (typeStr.contains("retrait")
                        || typeStr.contains("décaissement")) {
                    couleur = new java.awt.Color(220, 38, 38);
                    signe = "- ";
                } else {
                    couleur = new java.awt.Color(21, 128, 61);
                    signe = "+ ";
                }

                javax.swing.JLabel lblMontant = new javax.swing.JLabel(
                        signe + String.format("%,.0f F CFA",
                                t.getMontant()));
                lblMontant.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.BOLD, 13));
                lblMontant.setForeground(couleur);
                lblMontant.setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(0, 0, 0, 4));
                lblMontant.setHorizontalAlignment(
                        javax.swing.SwingConstants.RIGHT);

                row.add(left, java.awt.BorderLayout.WEST);
                row.add(lblMontant, java.awt.BorderLayout.EAST);
                transPanel.add(row);
            }
        }

        transPanel.revalidate();
        transPanel.repaint();
    }

    private void faireDepot() {
        DAO.CompteDAO dao = new DAO.CompteDAO();
        Models.Compte compte = dao.chercher(idCompteCourant);
        if (compte == null) {
            return;
        }

        // Message de référence pour Tontine
        String message = "Montant du dépôt (F CFA) :";
        if ("Tontine".equals(compte.getTypeCompte())
                && compte.getMontantPeriodique() > 0) {
            message = String.format(
                    "Montant du dépôt (F CFA) :\n"
                    + "(Montant de référence : %,.0f F CFA — "
                    + "vous pouvez mettre un montant différent)",
                    compte.getMontantPeriodique());
        }

        String montantStr = javax.swing.JOptionPane.showInputDialog(
                this, message, "Dépôt",
                javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (montantStr == null || montantStr.trim().isEmpty()) {
            return;
        }

        try {
            double montant = Double.parseDouble(
                    montantStr.trim().replace(" ", ""));

            if (montant <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Le montant doit être supérieur à 0 !",
                        "Erreur",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            double nouveauSolde = compte.getSoldeActuel() + montant;
            dao.modifierSolde(idCompteCourant, nouveauSolde);

            // Transaction
            Models.Transaction t = new Models.Transaction();
            t.setIdCompte(idCompteCourant);
            t.setType("Dépôt épargne");
            t.setMontant(montant);
            t.setDateHeure(new java.sql.Timestamp(
                    System.currentTimeMillis()));
            t.setStatut("Validé");
            t.setMoyenPaiement("Espèces");
            new DAO.TransactionDAO().ajouter(t);

            javax.swing.JOptionPane.showMessageDialog(this,
                    String.format(
                            "✔ Dépôt effectué avec succès !\n\n"
                            + "   Montant déposé  : %,.0f F CFA\n"
                            + "   Nouveau solde   : %,.0f F CFA",
                            montant, nouveauSolde),
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            chargerCompte(idCompteCourant);

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Montant invalide ! Entrez uniquement des chiffres.",
                    "Erreur",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void faireRetrait() {
        DAO.CompteDAO dao = new DAO.CompteDAO();
        Models.Compte compte = dao.chercher(idCompteCourant);
        if (compte == null) {
            return;
        }

        String montantStr = javax.swing.JOptionPane.showInputDialog(
                this,
                String.format("Montant du retrait (F CFA) :\n"
                        + "(Solde disponible : %,.0f F CFA — "
                        + "Minimum à maintenir : 5 000 F CFA)",
                        compte.getSoldeActuel()),
                "Retrait",
                javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (montantStr == null || montantStr.trim().isEmpty()) {
            return;
        }

        try {
            double montant = Double.parseDouble(
                    montantStr.trim().replace(" ", ""));

            if (montant <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Le montant doit être supérieur à 0 !",
                        "Erreur",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Vérifier solde suffisant
            if (montant > compte.getSoldeActuel()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        String.format(
                                "Solde insuffisant !\n"
                                + "Solde disponible : %,.0f F CFA",
                                compte.getSoldeActuel()),
                        "Erreur",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Vérifier solde minimum 5000 F après retrait
            double soldeApres = compte.getSoldeActuel() - montant;
            if (soldeApres < 5000) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        String.format(
                                "Retrait refusé !\n"
                                + "Le solde minimum à maintenir "
                                + "est de 5 000 F CFA.\n"
                                + "Montant maximum que vous pouvez "
                                + "retirer : %,.0f F CFA",
                                compte.getSoldeActuel() - 5000),
                        "Solde minimum non respecté",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.modifierSolde(idCompteCourant, soldeApres);

            // Transaction
            Models.Transaction t = new Models.Transaction();
            t.setIdCompte(idCompteCourant);
            t.setType("Retrait épargne");
            t.setMontant(montant);
            t.setDateHeure(new java.sql.Timestamp(
                    System.currentTimeMillis()));
            t.setStatut("Validé");
            t.setMoyenPaiement("Espèces");
            new DAO.TransactionDAO().ajouter(t);

            javax.swing.JOptionPane.showMessageDialog(this,
                    String.format(
                            "✔ Retrait effectué avec succès !\n\n"
                            + "   Montant retiré  : %,.0f F CFA\n"
                            + "   Nouveau solde   : %,.0f F CFA",
                            montant, soldeApres),
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            chargerCompte(idCompteCourant);

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Montant invalide ! Entrez uniquement des chiffres.",
                    "Erreur",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cloturerCompte() {
        DAO.CompteDAO dao = new DAO.CompteDAO();
        Models.Compte compte = dao.chercher(idCompteCourant);
        if (compte == null) {
            return;
        }

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Vérifier prêts actifs
        if (new DAO.PretDAO().existePretsActifsParCompte(
                idCompteCourant)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Impossible de clôturer !\n"
                    + "Ce compte a un prêt en cours.",
                    "Erreur",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Règles spécifiques À terme
        if ("À terme".equals(compte.getTypeCompte())) {
            if (compte.getDateOuverture() != null
                    && compte.getDuree() > 0) {
                java.util.Calendar cal
                        = java.util.Calendar.getInstance();
                cal.setTime(compte.getDateOuverture());
                cal.add(java.util.Calendar.MONTH, compte.getDuree());

                if (cal.getTime().after(new java.util.Date())) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Clôture impossible avant l'échéance !\n"
                            + "Date d'échéance : "
                            + sdf.format(cal.getTime()),
                            "Erreur",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Calculer et ajouter les intérêts
                double interets = compte.getSoldeActuel()
                        * compte.getTauxInteret()
                        * compte.getDuree() / 1200.0;

                if (interets > 0) {
                    int choix = javax.swing.JOptionPane
                            .showConfirmDialog(this,
                                    String.format(
                                            "Échéance atteinte !\n\n"
                                            + "   Solde bloqué    : %,.0f F CFA\n"
                                            + "   Intérêts gagnés : %,.0f F CFA\n"
                                            + "   Total à verser  : %,.0f F CFA\n\n"
                                            + "Ajouter les intérêts et clôturer ?",
                                            compte.getSoldeActuel(),
                                            interets,
                                            compte.getSoldeActuel() + interets),
                                    "Clôture avec intérêts",
                                    javax.swing.JOptionPane.YES_NO_OPTION,
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    if (choix != javax.swing.JOptionPane.YES_OPTION) {
                        return;
                    }

                    // Ajouter les intérêts d'abord
                    double nouveauSolde
                            = compte.getSoldeActuel() + interets;
                    dao.modifierSolde(idCompteCourant, nouveauSolde);

                    // Transaction intérêts
                    Models.Transaction t = new Models.Transaction();
                    t.setIdCompte(idCompteCourant);
                    t.setType("Intérêts épargne");
                    t.setMontant(interets);
                    t.setDateHeure(new java.sql.Timestamp(
                            System.currentTimeMillis()));
                    t.setStatut("Validé");
                    t.setMoyenPaiement("Virement");
                    new DAO.TransactionDAO().ajouter(t);

                    // Remettre solde à 0 pour permettre la clôture
                    dao.modifierSolde(idCompteCourant, 0);
                }
            }
        } else {
            // Courant et Tontine — vérifier solde = 0
            if (compte.getSoldeActuel() > 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        String.format(
                                "Impossible de clôturer !\n"
                                + "Le solde doit être à 0 avant clôture.\n"
                                + "Solde actuel : %,.0f F CFA",
                                compte.getSoldeActuel()),
                        "Erreur",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Confirmer la clôture du compte "
                + compte.getNumeroCompte() + " ?",
                "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.cloturer(idCompteCourant)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "✔ Compte clôturé avec succès !",
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Erreur lors de la clôture !",
                    "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        headerCenter = new javax.swing.JPanel();
        headerLeft = new javax.swing.JPanel();
        lblSousTitre = new javax.swing.JLabel();
        lblNomClient = new javax.swing.JLabel();
        lblNumCompte = new javax.swing.JLabel();
        headerBadges = new javax.swing.JPanel();
        lblBadgeStatut = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblBadgeType = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        btnFermer = new javax.swing.JButton();
        btnDepot = new javax.swing.JButton();
        btnRetrait = new javax.swing.JButton();
        btnCloturer = new javax.swing.JButton();
        scrollInfo = new javax.swing.JScrollPane();
        infoPanel = new javax.swing.JPanel();
        soldePanel = new javax.swing.JPanel();
        soldePanelLeft = new javax.swing.JPanel();
        lblSoldeTitle = new javax.swing.JLabel();
        lblSoldeVal = new javax.swing.JLabel();
        soldePanelRight = new javax.swing.JPanel();
        lblTauxTitle = new javax.swing.JLabel();
        lblTauxVal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblSec1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        gridInfo1 = new javax.swing.JPanel();
        panelDateOuv = new javax.swing.JPanel();
        lblDateOuvTitle = new javax.swing.JLabel();
        valDateOuv = new javax.swing.JLabel();
        panelInfo2 = new javax.swing.JPanel();
        lblInfo2Title = new javax.swing.JLabel();
        valInfo2 = new javax.swing.JLabel();
        panelInfo3 = new javax.swing.JPanel();
        lblInfo3Title = new javax.swing.JLabel();
        valInfo3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblSec2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        transPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Détail du compte");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(560, 600));
        setSize(new java.awt.Dimension(600, 580));

        headerPanel.setBackground(new java.awt.Color(219, 234, 254));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 90));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerCenter.setBackground(new java.awt.Color(219, 234, 254));
        headerCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 24, 16, 24));
        headerCenter.setLayout(new java.awt.BorderLayout());

        headerLeft.setBackground(new java.awt.Color(219, 234, 254));
        headerLeft.setPreferredSize(new java.awt.Dimension(350, 58));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblSousTitre.setForeground(new java.awt.Color(59, 130, 246));
        lblSousTitre.setText("Compte épargne");
        headerLeft.add(lblSousTitre);

        lblNomClient.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNomClient.setForeground(new java.awt.Color(15, 23, 42));
        lblNomClient.setText("Nom client");
        headerLeft.add(lblNomClient);

        lblNumCompte.setForeground(new java.awt.Color(100, 116, 139));
        lblNumCompte.setText("CPT-0000");
        headerLeft.add(lblNumCompte);

        headerCenter.add(headerLeft, java.awt.BorderLayout.WEST);

        headerBadges.setBackground(new java.awt.Color(219, 234, 254));
        headerBadges.setPreferredSize(new java.awt.Dimension(130, 58));
        headerBadges.setLayout(new javax.swing.BoxLayout(headerBadges, javax.swing.BoxLayout.Y_AXIS));

        lblBadgeStatut.setBackground(new java.awt.Color(220, 252, 231));
        lblBadgeStatut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBadgeStatut.setForeground(new java.awt.Color(21, 128, 61));
        lblBadgeStatut.setText("Actif");
        lblBadgeStatut.setAlignmentX(1.0F);
        lblBadgeStatut.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));
        headerBadges.add(lblBadgeStatut);

        jPanel1.setBackground(new java.awt.Color(219, 234, 254));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(9999, 6));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 6));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        headerBadges.add(jPanel1);

        lblBadgeType.setBackground(new java.awt.Color(255, 255, 255));
        lblBadgeType.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBadgeType.setForeground(new java.awt.Color(14, 165, 233));
        lblBadgeType.setText("Courant");
        lblBadgeType.setAlignmentX(1.0F);
        lblBadgeType.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));
        headerBadges.add(lblBadgeType);

        headerCenter.add(headerBadges, java.awt.BorderLayout.EAST);

        headerPanel.add(headerCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 10));

        btnFermer.setText("Fermer");
        btnFermer.setPreferredSize(new java.awt.Dimension(90, 36));
        footerPanel.add(btnFermer);

        btnDepot.setBackground(new java.awt.Color(220, 252, 231));
        btnDepot.setForeground(new java.awt.Color(21, 128, 61));
        btnDepot.setText("+ Dépôt");
        btnDepot.setPreferredSize(new java.awt.Dimension(100, 36));
        footerPanel.add(btnDepot);

        btnRetrait.setBackground(new java.awt.Color(254, 243, 199));
        btnRetrait.setForeground(new java.awt.Color(146, 64, 14));
        btnRetrait.setText("- Retrait");
        btnRetrait.setPreferredSize(new java.awt.Dimension(90, 36));
        footerPanel.add(btnRetrait);

        btnCloturer.setBackground(new java.awt.Color(254, 226, 226));
        btnCloturer.setForeground(new java.awt.Color(220, 38, 38));
        btnCloturer.setText("Clôturer");
        btnCloturer.setPreferredSize(new java.awt.Dimension(100, 36));
        footerPanel.add(btnCloturer);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));

        soldePanel.setBackground(new java.awt.Color(248, 250, 252));
        soldePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));
        soldePanel.setAlignmentX(0.0F);
        soldePanel.setMaximumSize(new java.awt.Dimension(9999, 72));
        soldePanel.setPreferredSize(new java.awt.Dimension(0, 72));
        soldePanel.setLayout(new java.awt.BorderLayout());

        soldePanelLeft.setBackground(new java.awt.Color(248, 250, 252));
        soldePanelLeft.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 0));
        soldePanelLeft.setLayout(new javax.swing.BoxLayout(soldePanelLeft, javax.swing.BoxLayout.Y_AXIS));

        lblSoldeTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblSoldeTitle.setText("Solde actuel");
        soldePanelLeft.add(lblSoldeTitle);

        lblSoldeVal.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblSoldeVal.setForeground(new java.awt.Color(15, 23, 42));
        lblSoldeVal.setText("0 F CFA");
        soldePanelLeft.add(lblSoldeVal);

        soldePanel.add(soldePanelLeft, java.awt.BorderLayout.WEST);

        soldePanelRight.setBackground(new java.awt.Color(248, 250, 252));
        soldePanelRight.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 12, 16));
        soldePanelRight.setPreferredSize(new java.awt.Dimension(150, 72));
        soldePanelRight.setLayout(new javax.swing.BoxLayout(soldePanelRight, javax.swing.BoxLayout.Y_AXIS));

        lblTauxTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblTauxTitle.setText("Taux d'intérêt");
        lblTauxTitle.setAlignmentX(1.0F);
        soldePanelRight.add(lblTauxTitle);

        lblTauxVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTauxVal.setForeground(new java.awt.Color(14, 165, 233));
        lblTauxVal.setText("0%");
        lblTauxVal.setAlignmentX(1.0F);
        soldePanelRight.add(lblTauxVal);

        soldePanel.add(soldePanelRight, java.awt.BorderLayout.EAST);

        infoPanel.add(soldePanel);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        infoPanel.add(jPanel2);

        lblSec1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSec1.setForeground(new java.awt.Color(14, 165, 233));
        lblSec1.setText("INFORMATIONS DU COMPTE");
        infoPanel.add(lblSec1);

        jSeparator1.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator1);

        gridInfo1.setBackground(new java.awt.Color(255, 255, 255));
        gridInfo1.setAlignmentX(0.0F);
        gridInfo1.setMaximumSize(new java.awt.Dimension(9999, 65));
        gridInfo1.setPreferredSize(new java.awt.Dimension(0, 65));
        gridInfo1.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelDateOuv.setBackground(new java.awt.Color(255, 255, 255));
        panelDateOuv.setLayout(new javax.swing.BoxLayout(panelDateOuv, javax.swing.BoxLayout.Y_AXIS));

        lblDateOuvTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDateOuvTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblDateOuvTitle.setText("Date d'ouverture");
        lblDateOuvTitle.setMaximumSize(new java.awt.Dimension(9999, 18));
        panelDateOuv.add(lblDateOuvTitle);

        valDateOuv.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valDateOuv.setForeground(new java.awt.Color(15, 23, 42));
        valDateOuv.setText("jLabel2");
        panelDateOuv.add(valDateOuv);

        gridInfo1.add(panelDateOuv);

        panelInfo2.setBackground(new java.awt.Color(255, 255, 255));
        panelInfo2.setLayout(new javax.swing.BoxLayout(panelInfo2, javax.swing.BoxLayout.Y_AXIS));

        lblInfo2Title.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblInfo2Title.setForeground(new java.awt.Color(100, 116, 139));
        lblInfo2Title.setText("Type");
        lblInfo2Title.setMaximumSize(new java.awt.Dimension(9999, 18));
        panelInfo2.add(lblInfo2Title);

        valInfo2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valInfo2.setForeground(new java.awt.Color(15, 23, 42));
        valInfo2.setText("jLabel2");
        panelInfo2.add(valInfo2);

        gridInfo1.add(panelInfo2);

        panelInfo3.setBackground(new java.awt.Color(255, 255, 255));
        panelInfo3.setLayout(new javax.swing.BoxLayout(panelInfo3, javax.swing.BoxLayout.Y_AXIS));

        lblInfo3Title.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblInfo3Title.setForeground(new java.awt.Color(100, 116, 139));
        lblInfo3Title.setText("Durée");
        lblInfo3Title.setMaximumSize(new java.awt.Dimension(9999, 18));
        panelInfo3.add(lblInfo3Title);

        valInfo3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valInfo3.setForeground(new java.awt.Color(15, 23, 42));
        valInfo3.setText("jLabel2");
        panelInfo3.add(valInfo3);

        gridInfo1.add(panelInfo3);

        infoPanel.add(gridInfo1);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setAlignmentX(0.0F);
        jPanel4.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        infoPanel.add(jPanel4);

        lblSec2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSec2.setForeground(new java.awt.Color(14, 165, 233));
        lblSec2.setText("DERNIÈRES TRANSACTIONS");
        infoPanel.add(lblSec2);

        jSeparator2.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator2.setAlignmentX(0.0F);
        jSeparator2.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator2);

        transPanel.setBackground(new java.awt.Color(255, 255, 255));
        transPanel.setAlignmentX(0.0F);
        transPanel.setMaximumSize(new java.awt.Dimension(9999, 165));
        transPanel.setPreferredSize(new java.awt.Dimension(0, 165));
        transPanel.setLayout(new javax.swing.BoxLayout(transPanel, javax.swing.BoxLayout.Y_AXIS));
        infoPanel.add(transPanel);

        scrollInfo.setViewportView(infoPanel);

        getContentPane().add(scrollInfo, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DetailCompte dialog = new DetailCompte(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloturer;
    private javax.swing.JButton btnDepot;
    private javax.swing.JButton btnFermer;
    private javax.swing.JButton btnRetrait;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel gridInfo1;
    private javax.swing.JPanel headerBadges;
    private javax.swing.JPanel headerCenter;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblBadgeStatut;
    private javax.swing.JLabel lblBadgeType;
    private javax.swing.JLabel lblDateOuvTitle;
    private javax.swing.JLabel lblInfo2Title;
    private javax.swing.JLabel lblInfo3Title;
    private javax.swing.JLabel lblNomClient;
    private javax.swing.JLabel lblNumCompte;
    private javax.swing.JLabel lblSec1;
    private javax.swing.JLabel lblSec2;
    private javax.swing.JLabel lblSoldeTitle;
    private javax.swing.JLabel lblSoldeVal;
    private javax.swing.JLabel lblSousTitre;
    private javax.swing.JLabel lblTauxTitle;
    private javax.swing.JLabel lblTauxVal;
    private javax.swing.JPanel panelDateOuv;
    private javax.swing.JPanel panelInfo2;
    private javax.swing.JPanel panelInfo3;
    private javax.swing.JScrollPane scrollInfo;
    private javax.swing.JPanel soldePanel;
    private javax.swing.JPanel soldePanelLeft;
    private javax.swing.JPanel soldePanelRight;
    private javax.swing.JPanel transPanel;
    private javax.swing.JLabel valDateOuv;
    private javax.swing.JLabel valInfo2;
    private javax.swing.JLabel valInfo3;
    // End of variables declaration//GEN-END:variables
}
