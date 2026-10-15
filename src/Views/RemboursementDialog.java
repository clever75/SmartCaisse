/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class RemboursementDialog extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RemboursementDialog.class.getName());

    /**
     * Creates new form RemboursementDialog
     */
    public RemboursementDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        configurerStyle();
        chargerClients();
        configurerListeners();
        datePaiement.setMaxSelectableDate(new java.util.Date());
    }

    /**
     * Pré-sélectionne un prêt spécifique à l'ouverture du dialog. Appelé depuis
     * PretsPanel et DetailPret.
     */
    public void preselectionnerPret(int idPret) {
        DAO.PretDAO pretDao = new DAO.PretDAO();
        Models.Pret pret = pretDao.chercher(idPret);
        if (pret == null) {
            return;
        }

        // Trouver le client via le compte
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        Models.Compte compte = compteDao.chercher(pret.getIdCompte());
        if (compte == null) {
            return;
        }

        DAO.ClientDAO clientDao = new DAO.ClientDAO();
        Models.Client client = clientDao.chercher(compte.getIdClient());
        if (client == null) {
            return;
        }

        // Sélectionner le client dans cmbClient
        for (int i = 0; i < cmbClient.getItemCount(); i++) {
            String item = cmbClient.getItemAt(i);
            if (item.startsWith(client.getIdClient() + " | ")) {
                cmbClient.setSelectedIndex(i);
                break;
            }
        }

        // Attendre que chargerPretsClient() finisse puis sélectionner le prêt
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < cmbPret.getItemCount(); i++) {
                String item = cmbPret.getItemAt(i);
                if (item.startsWith(idPret + " | ")) {
                    cmbPret.setSelectedIndex(i);
                    break;
                }
            }
        });
    }

    private void configurerStyle() {
        // Header
        headerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20)));
        lblTitle.setForeground(new java.awt.Color(15, 23, 42));
        lblSub.setForeground(new java.awt.Color(100, 116, 139));
        lblSub.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        // Footer
        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        jLabel2.setForeground(new java.awt.Color(100, 116, 139));
        jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 11));
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));

        // Bouton Annuler
        btnAnnuler.setBackground(new java.awt.Color(241, 245, 249));
        btnAnnuler.setForeground(new java.awt.Color(71, 85, 105));
        btnAnnuler.setBorderPainted(false);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnnuler.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        // Bouton Enregistrer
        btnEnregistrer.setBackground(new java.awt.Color(14, 165, 233));
        btnEnregistrer.setForeground(java.awt.Color.WHITE);
        btnEnregistrer.setBorderPainted(false);
        btnEnregistrer.setFocusPainted(false);
        btnEnregistrer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnregistrer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        // formPanel padding
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Champs de saisie
        javax.swing.border.Border fieldBorder = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));

        txtMontant.setBorder(fieldBorder);
        txtMontant.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        txtRef.setBorder(fieldBorder);
        txtRef.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        // ComboBox
        cmbClient.setBackground(java.awt.Color.WHITE);
        cmbClient.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        cmbPret.setBackground(java.awt.Color.WHITE);
        cmbPret.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        cmbMoyen.setBackground(java.awt.Color.WHITE);
        cmbMoyen.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    }

    private void configurerListeners() {
        btnAnnuler.addActionListener(e -> dispose());
        btnEnregistrer.addActionListener(e -> enregistrerRemboursement());

        // Quand client change → charger ses prêts
        cmbClient.addActionListener(e -> {
            if (cmbClient.getSelectedIndex() > 0) {
                chargerPretsClient();
            } else {
                cmbPret.removeAllItems();
                cmbPret.addItem("-- Choisir un prêt --");
                reinitialiserDetails();
            }
        });

        // Quand prêt change → afficher détails
        cmbPret.addActionListener(e -> {
            if (cmbPret.getSelectedIndex() > 0) {
                afficherDetailsPret();
            } else {
                reinitialiserDetails();
            }
        });

        // Calcul en temps réel quand montant change
        txtMontant.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculerApres();
            }
        });
    }

    private void chargerClients() {
        DAO.ClientDAO dao = new DAO.ClientDAO();
        java.util.List<Models.Client> clients = dao.listerTous();
        cmbClient.removeAllItems();
        cmbClient.addItem("-- Choisir un client --");
        for (Models.Client c : clients) {
            if ("Actif".equals(c.getStatut())) {
                cmbClient.addItem(c.getIdClient() + " | "
                        + c.getNom() + " " + c.getPrenom());
            }
        }
    }

    private void chargerPretsClient() {
        cmbPret.removeAllItems();
        cmbPret.addItem("-- Choisir un prêt --");
        new DAO.PretDAO().mettreAJourStatuts();

        String selected = cmbClient.getSelectedItem().toString();
        int idClient = Integer.parseInt(selected.split(" \\| ")[0]);

        // Récupérer les comptes du client
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        java.util.List<Models.Compte> comptes = compteDao.listerParClient(idClient);

        DAO.PretDAO pretDao = new DAO.PretDAO();
        for (Models.Compte c : comptes) {
            java.util.List<Models.Pret> prets = pretDao.listerParCompte(c.getIdCompte());
            for (Models.Pret p : prets) {
                if ("En cours".equals(p.getStatut())
                        || "En retard".equals(p.getStatut())) {
                    cmbPret.addItem(p.getIdPret() + " | #"
                            + String.format("%03d", p.getIdPret())
                            + " — " + String.format("%,.0f F CFA",
                                    p.getMontantPrincipal()));
                }
            }
        }

        if (cmbPret.getItemCount() == 1) {
            cmbPret.addItem("Aucun prêt en cours");
        }
    }

    private void afficherDetailsPret() {
        if (cmbPret.getSelectedIndex() <= 0) {
            return;
        }

        String selected = cmbPret.getSelectedItem().toString();
        if (selected.equals("Aucun prêt en cours")) {
            return;
        }

        int idPret = Integer.parseInt(selected.split(" \\| ")[0]);
        DAO.PretDAO pretDao = new DAO.PretDAO();
        Models.Pret pret = pretDao.chercher(idPret);
        if (pret == null) {
            return;
        }

        // Calculs
        double interets = pret.getMontantPrincipal()
                * pret.getTauxInteret()
                * pret.getDureeMois() / 1200.0;
        double total = pret.getMontantPrincipal() + interets;
        double mensualite = total / pret.getDureeMois();
        double reste = Math.max(0, total - pret.getMontantRembourse());
        int echeancesPayees;
if (pret.getMontantRembourse() <= 0) {
    echeancesPayees = 0;
} else if (reste <= 2.0) {
    // Totalement remboursé
    echeancesPayees = pret.getDureeMois();
} else {
    // Arrondi au plus proche avec tolérance de 1% de mensualité
    echeancesPayees = (int) Math.floor(
            (pret.getMontantRembourse() + mensualite * 0.01) / mensualite);
    echeancesPayees = Math.min(echeancesPayees, pret.getDureeMois());
}
int echeancesRestantes = Math.max(0, pret.getDureeMois() - echeancesPayees);
        double pourcentage = (total > 0)
                ? (pret.getMontantRembourse() / total) * 100 : 0;
        double penalite = 0;
        if ("En retard".equals(pret.getStatut())) {
            penalite = pret.getPenalite();
            if (penalite > 0) {
                    penalite = pret.getPenalite();

            }
        }

        // Afficher dans la carte
        lblInfoVal.setText(String.format("%,.0f F CFA", total));
lblRembVal.setText(String.format("%,.0f F CFA", pret.getMontantRembourse()));
lblInfoMensVal.setText(String.format("%,.0f F CFA", mensualite));

// Reste — tenir compte de la pénalité si en retard
if ("En retard".equals(pret.getStatut()) && penalite > 0) {
    lblInfoResteVal.setText(String.format(
            "%,.0f F CFA  +  %,.0f F pénalité", reste, penalite));
    lblInfoResteVal.setForeground(new java.awt.Color(220, 38, 38));
} else {
    lblInfoResteVal.setText(String.format("%,.0f F CFA", reste));
    lblInfoResteVal.setForeground(new java.awt.Color(220, 38, 38));
}
        lblInfoEchVal.setText(echeancesRestantes + " / "
                + pret.getDureeMois());
        lblInfoFinVal.setText(pret.getDateFinPrevue() != null
                ? new java.text.SimpleDateFormat("dd/MM/yyyy")
                        .format(pret.getDateFinPrevue()) : "—");

        // Barre de progression
        int prog = (int) Math.min(100, pourcentage);
        progressrRemb.setValue(prog);
        lblProgressTitle.setText(String.format(
                "Progression du remboursement — %d%%", prog));

        // Pré-remplir montant
        double resteReel = Math.max(0, total - pret.getMontantRembourse());
        double montantSuggere = Math.min(mensualite + penalite, resteReel);
        if (montantSuggere > 0) {
            txtMontant.setText(String.format("%.0f", montantSuggere));
        } else {
            txtMontant.setText("");
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ce prêt est totalement remboursé !",
                    "Information",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        calculerApres();
    }

    private void reinitialiserDetails() {
        lblInfoVal.setText("0 F CFA");
        lblRembVal.setText("0 F CFA");
        lblInfoResteVal.setText("0 F CFA");
        lblInfoMensVal.setText("0 F CFA");
        lblInfoEchVal.setText("0 / 0");
        lblInfoFinVal.setText("--/--/----");
        progressrRemb.setValue(0);
        lblProgressTitle.setText("Progression du remboursement — 0%");
        lblApresRemVal.setText("0 F CFA");
        lblApresResteVal.setText("0 F CFA");
        lblApresEchVal.setText("0 / 0");
        txtMontant.setText("");
    }

    private void calculerApres() {
        if (cmbPret.getSelectedIndex() <= 0) {
            return;
        }
        String selected = cmbPret.getSelectedItem().toString();
        if (selected.equals("Aucun prêt en cours")) {
            return;
        }

        try {
            double montantPaye = Double.parseDouble(
                    txtMontant.getText().trim().replace(" ", "").replace(",", ""));

            int idPret = Integer.parseInt(selected.split(" \\| ")[0]);
            Models.Pret pret = new DAO.PretDAO().chercher(idPret);
            if (pret == null) {
                return;
            }

            double interets = pret.getMontantPrincipal()
                    * pret.getTauxInteret()
                    * pret.getDureeMois() / 1200.0;
            double total = pret.getMontantPrincipal() + interets;
            double mensualite = total / pret.getDureeMois();

            double nouveauRemb = pret.getMontantRembourse() + montantPaye;
            double nouveauReste = Math.max(0, total - nouveauRemb);
          int echeancesPayees;
if (nouveauRemb <= 0) {
    echeancesPayees = 0;
} else if (total - nouveauRemb <= 2.0) {
    echeancesPayees = pret.getDureeMois();
} else {
    echeancesPayees = (int) Math.floor(
            (nouveauRemb + mensualite * 0.01) / mensualite);
    echeancesPayees = Math.min(echeancesPayees, pret.getDureeMois());
}
int echeancesRestantes = Math.max(0, pret.getDureeMois() - echeancesPayees);
// Affichage : échéances restantes / total (pas payées/total)
            lblApresRemVal.setText(String.format("%,.0f F CFA", nouveauRemb));
            lblApresResteVal.setText(String.format("%,.0f F CFA", nouveauReste));
            lblApresEchVal.setText(echeancesRestantes + " / "
                    + pret.getDureeMois());

        } catch (NumberFormatException e) {
            lblApresRemVal.setText("0 F CFA");
            lblApresResteVal.setText("0 F CFA");
            lblApresEchVal.setText("0 / 0");
        }
    }

    private void enregistrerRemboursement() {

        // ── Validations ──
        if (cmbClient.getSelectedIndex() == 0) {
            afficherErreur("Veuillez choisir un client !");
            return;
        }
        if (cmbPret.getSelectedIndex() == 0
                || "Aucun prêt en cours".equals(
                        cmbPret.getSelectedItem().toString())) {
            afficherErreur("Veuillez choisir un prêt en cours !");
            return;
        }
        if (txtMontant.getText().trim().isEmpty()) {
            afficherErreur("Le montant payé est obligatoire !");
            txtMontant.requestFocus();
            return;
        }
        if (datePaiement.getDate() == null) {
            afficherErreur("La date de paiement est obligatoire !");
            return;
        }
        if (datePaiement.getDate().after(new java.util.Date())) {
    afficherErreur("La date de paiement ne peut pas être dans le futur !");
    return;
}
        // Récupérer le prêt pour vérifier la date
String selectedPretVerif = cmbPret.getSelectedItem().toString();
int idPretVerif = Integer.parseInt(selectedPretVerif.split(" \\| ")[0]);
Models.Pret pretVerif = new DAO.PretDAO().chercher(idPretVerif);
if (pretVerif != null && pretVerif.getDateDebut() != null) {
    if (datePaiement.getDate().before(pretVerif.getDateDebut())) {
        afficherErreur(
            "La date de paiement ne peut pas être avant\n"
            + "la date de début du prêt ("
            + new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(pretVerif.getDateDebut()) + ") !");
        return;
    }
}

        try {
            double montantPaye = Double.parseDouble(
                    txtMontant.getText().trim().replace(" ", "").replace(",", ""));

            if (montantPaye <= 0) {
                afficherErreur("Le montant doit être supérieur à 0 !");
                txtMontant.requestFocus();
                return;
            }
            if (montantPaye < 500) {
    afficherErreur("Le montant minimum de remboursement est de 500 F CFA !");
    txtMontant.requestFocus();
    return;
}

            // Récupérer le prêt
            String selectedPret = cmbPret.getSelectedItem().toString();
            int idPret = Integer.parseInt(selectedPret.split(" \\| ")[0]);
            DAO.PretDAO pretDao = new DAO.PretDAO();
            Models.Pret pret = pretDao.chercher(idPret);
            if (pret == null) {
                afficherErreur("Prêt introuvable !");
                return;
            }

            // ── Calculs avec arrondi à 2 décimales ──
            double interets = Math.round(pret.getMontantPrincipal()
                    * pret.getTauxInteret()
                    * pret.getDureeMois() / 1200.0 * 100.0) / 100.0;
            double total = Math.round(
                    (pret.getMontantPrincipal() + interets) * 100.0) / 100.0;
            double reste = Math.round(
                    (total - pret.getMontantRembourse()) * 100.0) / 100.0;

            // Si reste quasi nul — prêt déjà remboursé
            if (reste <= 2.0) {
                pretDao.modifierStatut(idPret, "Remboursé");
                afficherErreur("Ce prêt est déjà totalement remboursé !");
                return;
            }

            // Vérifier que le montant ne dépasse pas le reste
            if (montantPaye > reste + 2.0) {
                afficherErreur(String.format(
                        "Le montant payé (%,.0f F) dépasse le reste à payer (%,.0f F) !",
                        montantPaye, reste));
                txtMontant.requestFocus();
                return;
            }

            // Ajuster si montant légèrement supérieur au reste (arrondi)
            if (montantPaye > reste) {
                montantPaye = reste;
            }

            // Avertissement paiement partiel
            double mensualite = total / pret.getDureeMois();
            if (montantPaye < mensualite - 1) {
                int choix = javax.swing.JOptionPane.showConfirmDialog(this,
                        String.format(
                                "⚠ Le montant saisi (%,.0f F CFA) est inférieur "
                                + "à la mensualité prévue (%,.0f F CFA).\n"
                                + "Ce sera enregistré comme paiement partiel.\n\n"
                                + "Confirmer quand même ?",
                                montantPaye, mensualite),
                        "Paiement partiel",
                        javax.swing.JOptionPane.YES_NO_OPTION,
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                if (choix != javax.swing.JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // ── Enregistrer le remboursement ──
            boolean ok = pretDao.mettreAJourRemboursement(idPret, montantPaye);
            if (!ok) {
                afficherErreur("Erreur lors de la mise à jour du prêt !");
                return;
            }

            // ── Vérifier si prêt totalement remboursé ──
            double nouveauRemb = Math.round(
                    (pret.getMontantRembourse() + montantPaye) * 100.0) / 100.0;
            String nouveauStatut = "En cours";
            if (nouveauRemb >= total - 2.0) {
                pretDao.modifierStatut(idPret, "Remboursé");
                nouveauStatut = "Remboursé ✔";
            }

            // ── Mettre à jour tous les statuts ──
            pretDao.mettreAJourStatuts();

            // ── Enregistrer la transaction ──
            Models.Transaction trans = new Models.Transaction();
            trans.setIdCompte(pret.getIdCompte());
            trans.setType("Remboursement");
            trans.setMontant(montantPaye);
            trans.setDateHeure(new java.sql.Timestamp(
                    datePaiement.getDate().getTime()));
            trans.setMoyenPaiement(cmbMoyen.getSelectedItem().toString());
            trans.setStatut("Validé");
            new DAO.TransactionDAO().ajouter(trans);

// ── Imprimer le reçu ──
            try {
                DAO.CompteDAO compteDao = new DAO.CompteDAO();
                Models.Compte compte = compteDao.chercher(pret.getIdCompte());
                DAO.ClientDAO clientDao = new DAO.ClientDAO();
                Models.Client client = clientDao.chercher(compte.getIdClient());
                double resteApres = Math.max(0, total - nouveauRemb);

                Utils.ImpressionUtil.imprimerRecuRemboursement(
                        pret, compte, client, montantPaye,
                        resteApres,
                        cmbMoyen.getSelectedItem().toString(),
                        false);
            } catch (Exception ex) {
                System.out.println("Impression échouée : " + ex.getMessage());
            }

            javax.swing.JOptionPane.showMessageDialog(this,
                    String.format(
                            "✔ Remboursement enregistré avec succès !\n\n"
                            + "   Montant payé     : %,.0f F CFA\n"
                            + "   Reste à payer    : %,.0f F CFA\n"
                            + "   Statut du prêt   : %s",
                            montantPaye,
                            Math.max(0, total - nouveauRemb),
                            nouveauStatut),
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException e) {
            afficherErreur("Montant invalide ! Entrez uniquement des chiffres.");
            txtMontant.requestFocus();
        }
    }

    private void afficherErreur(String message) {
        javax.swing.JOptionPane.showMessageDialog(
                this, message, "Erreur de saisie",
                javax.swing.JOptionPane.WARNING_MESSAGE);
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
        lblTitle = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        footerBtns = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        panelLigne1 = new javax.swing.JPanel();
        panelClient = new javax.swing.JPanel();
        lblClient = new javax.swing.JLabel();
        cmbClient = new javax.swing.JComboBox<>();
        panelPret = new javax.swing.JPanel();
        lblPret = new javax.swing.JLabel();
        cmbPret = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        lblSecDetails = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        infoCard = new javax.swing.JPanel();
        panelInfoTotal = new javax.swing.JPanel();
        lblInfoTotalTitle = new javax.swing.JLabel();
        lblInfoVal = new javax.swing.JLabel();
        panelInfoRembourse = new javax.swing.JPanel();
        lblInfoRembTitle = new javax.swing.JLabel();
        lblRembVal = new javax.swing.JLabel();
        panelInfoReste = new javax.swing.JPanel();
        lblInfoResteTitle = new javax.swing.JLabel();
        lblInfoResteVal = new javax.swing.JLabel();
        panelInfoMens = new javax.swing.JPanel();
        lblInfoMensTitle = new javax.swing.JLabel();
        lblInfoMensVal = new javax.swing.JLabel();
        panelInfoEch = new javax.swing.JPanel();
        lblInfoEchTitle = new javax.swing.JLabel();
        lblInfoEchVal = new javax.swing.JLabel();
        panelInfoFin = new javax.swing.JPanel();
        lblInfoFinTitle = new javax.swing.JLabel();
        lblInfoFinVal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        progressPanel = new javax.swing.JPanel();
        lblProgressTitle = new javax.swing.JLabel();
        progressrRemb = new javax.swing.JProgressBar();
        jPanel5 = new javax.swing.JPanel();
        lblSecPaiement = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        pnlLigne2 = new javax.swing.JPanel();
        panelMontant = new javax.swing.JPanel();
        lblontant = new javax.swing.JLabel();
        txtMontant = new javax.swing.JTextField();
        panelMoyen = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbMoyen = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        panelLigne3 = new javax.swing.JPanel();
        panelDate = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        datePaiement = new com.toedter.calendar.JDateChooser();
        panelRef = new javax.swing.JPanel();
        lblRf = new javax.swing.JLabel();
        txtRef = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lblSecapres = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        recapCard = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lblApresRembTitle = new javax.swing.JLabel();
        lblApresRemVal = new javax.swing.JLabel();
        panelApresReste = new javax.swing.JPanel();
        lblApresResteTitle = new javax.swing.JLabel();
        lblApresResteVal = new javax.swing.JLabel();
        panelApresEch = new javax.swing.JPanel();
        lblApresEchTitle = new javax.swing.JLabel();
        lblApresEchVal = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enregistrer un remboursement");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(500, 700));
        setResizable(false);
        setSize(new java.awt.Dimension(500, 700));

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 65));
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTitle.setText("Enregistrer un remboursement");
        headerPanel.add(lblTitle);

        lblSub.setText("Saisir le paiment mensuel d'un prêt en cours");
        headerPanel.add(lblSub);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Champs obligatoires *");
        footerPanel.add(jLabel2, java.awt.BorderLayout.WEST);

        footerBtns.setBackground(new java.awt.Color(248, 250, 252));
        footerBtns.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 16));
        footerBtns.setPreferredSize(new java.awt.Dimension(260, 55));
        footerBtns.setLayout(new java.awt.GridLayout(1, 2, 8, 0));

        btnAnnuler.setBackground(new java.awt.Color(241, 245, 249));
        btnAnnuler.setForeground(new java.awt.Color(71, 85, 105));
        btnAnnuler.setText("Annuler");
        btnAnnuler.setBorderPainted(false);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setPreferredSize(new java.awt.Dimension(100, 36));
        footerBtns.add(btnAnnuler);

        btnEnregistrer.setBackground(new java.awt.Color(14, 165, 233));
        btnEnregistrer.setForeground(new java.awt.Color(255, 255, 255));
        btnEnregistrer.setText("Enregistrer");
        footerBtns.add(btnEnregistrer);

        footerPanel.add(footerBtns, java.awt.BorderLayout.EAST);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        jScrollPane1.setBorder(null);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(14, 165, 233));
        jLabel1.setText("SÉLECTION DU PRÊT");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 8, 0));
        jLabel1.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(jLabel1);

        jSeparator1.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator1);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(9999, 12));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 12));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        formPanel.add(jPanel1);

        panelLigne1.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne1.setAlignmentX(0.0F);
        panelLigne1.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne1.setPreferredSize(new java.awt.Dimension(0, 68));
        panelLigne1.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelClient.setBackground(new java.awt.Color(255, 255, 255));
        panelClient.setLayout(new javax.swing.BoxLayout(panelClient, javax.swing.BoxLayout.Y_AXIS));

        lblClient.setForeground(new java.awt.Color(71, 85, 105));
        lblClient.setText("Client *");
        lblClient.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblClient.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelClient.add(lblClient);

        cmbClient.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Choisir un client --" }));
        cmbClient.setAlignmentX(0.0F);
        cmbClient.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelClient.add(cmbClient);

        panelLigne1.add(panelClient);

        panelPret.setBackground(new java.awt.Color(255, 255, 255));
        panelPret.setLayout(new javax.swing.BoxLayout(panelPret, javax.swing.BoxLayout.Y_AXIS));

        lblPret.setForeground(new java.awt.Color(71, 85, 105));
        lblPret.setText("Prêt en cours *");
        lblPret.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblPret.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelPret.add(lblPret);

        cmbPret.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbPret.setAlignmentX(0.0F);
        cmbPret.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelPret.add(cmbPret);

        panelLigne1.add(panelPret);

        formPanel.add(panelLigne1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel2);

        lblSecDetails.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblSecDetails.setForeground(new java.awt.Color(14, 165, 233));
        lblSecDetails.setText("DÉTAILS DU PRÊT SÉLECTIONNÉ");
        lblSecDetails.setToolTipText("");
        lblSecDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        formPanel.add(lblSecDetails);
        formPanel.add(jSeparator2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setMaximumSize(new java.awt.Dimension(9999, 8));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel3);

        infoCard.setBackground(new java.awt.Color(255, 255, 255));
        infoCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        infoCard.setAlignmentX(0.0F);
        infoCard.setMaximumSize(new java.awt.Dimension(9999, 100));
        infoCard.setPreferredSize(new java.awt.Dimension(0, 90));
        infoCard.setLayout(new java.awt.GridLayout(2, 3, 12, 0));

        panelInfoTotal.setBackground(new java.awt.Color(248, 250, 252));
        panelInfoTotal.setLayout(new javax.swing.BoxLayout(panelInfoTotal, javax.swing.BoxLayout.Y_AXIS));

        lblInfoTotalTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInfoTotalTitle.setText("Montant total");
        panelInfoTotal.add(lblInfoTotalTitle);

        lblInfoVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblInfoVal.setForeground(new java.awt.Color(14, 165, 233));
        lblInfoVal.setText("0 FCFA");
        panelInfoTotal.add(lblInfoVal);

        infoCard.add(panelInfoTotal);

        panelInfoRembourse.setBackground(new java.awt.Color(248, 250, 252));
        panelInfoRembourse.setLayout(new javax.swing.BoxLayout(panelInfoRembourse, javax.swing.BoxLayout.Y_AXIS));

        lblInfoRembTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInfoRembTitle.setText("Déjà remboursé");
        panelInfoRembourse.add(lblInfoRembTitle);

        lblRembVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblRembVal.setForeground(new java.awt.Color(21, 128, 61));
        lblRembVal.setText("0 FCFA");
        panelInfoRembourse.add(lblRembVal);

        infoCard.add(panelInfoRembourse);

        panelInfoReste.setBackground(new java.awt.Color(248, 250, 252));
        panelInfoReste.setLayout(new javax.swing.BoxLayout(panelInfoReste, javax.swing.BoxLayout.Y_AXIS));

        lblInfoResteTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInfoResteTitle.setText("Reste à payer");
        panelInfoReste.add(lblInfoResteTitle);

        lblInfoResteVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblInfoResteVal.setForeground(new java.awt.Color(220, 38, 38));
        lblInfoResteVal.setText("0 FCFA");
        panelInfoReste.add(lblInfoResteVal);

        infoCard.add(panelInfoReste);

        panelInfoMens.setBackground(new java.awt.Color(248, 250, 252));
        panelInfoMens.setLayout(new javax.swing.BoxLayout(panelInfoMens, javax.swing.BoxLayout.Y_AXIS));

        lblInfoMensTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInfoMensTitle.setText("Mensualité prévue");
        panelInfoMens.add(lblInfoMensTitle);

        lblInfoMensVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblInfoMensVal.setForeground(new java.awt.Color(15, 23, 42));
        lblInfoMensVal.setText("0 FCFA");
        panelInfoMens.add(lblInfoMensVal);

        infoCard.add(panelInfoMens);

        panelInfoEch.setBackground(new java.awt.Color(248, 250, 252));
        panelInfoEch.setLayout(new javax.swing.BoxLayout(panelInfoEch, javax.swing.BoxLayout.Y_AXIS));

        lblInfoEchTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInfoEchTitle.setText("Échéances  restantes");
        panelInfoEch.add(lblInfoEchTitle);

        lblInfoEchVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblInfoEchVal.setForeground(new java.awt.Color(15, 23, 42));
        lblInfoEchVal.setText("0 / 0");
        panelInfoEch.add(lblInfoEchVal);

        infoCard.add(panelInfoEch);

        panelInfoFin.setBackground(new java.awt.Color(248, 250, 252));
        panelInfoFin.setLayout(new javax.swing.BoxLayout(panelInfoFin, javax.swing.BoxLayout.Y_AXIS));

        lblInfoFinTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInfoFinTitle.setText("Date fin prévue");
        panelInfoFin.add(lblInfoFinTitle);

        lblInfoFinVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblInfoFinVal.setForeground(new java.awt.Color(15, 23, 42));
        lblInfoFinVal.setText(" ---");
        panelInfoFin.add(lblInfoFinVal);

        infoCard.add(panelInfoFin);

        formPanel.add(infoCard);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setAlignmentX(0.0F);
        jPanel4.setMaximumSize(new java.awt.Dimension(9999, 10));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel4);

        progressPanel.setBackground(new java.awt.Color(255, 255, 255));
        progressPanel.setAlignmentX(0.0F);
        progressPanel.setMaximumSize(new java.awt.Dimension(9999, 40));
        progressPanel.setPreferredSize(new java.awt.Dimension(0, 40));
        progressPanel.setLayout(new javax.swing.BoxLayout(progressPanel, javax.swing.BoxLayout.Y_AXIS));

        lblProgressTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblProgressTitle.setText("Progression du remboursement - 0%");
        progressPanel.add(lblProgressTitle);

        progressrRemb.setBackground(new java.awt.Color(226, 232, 240));
        progressrRemb.setForeground(new java.awt.Color(14, 165, 233));
        progressrRemb.setAlignmentX(0.0F);
        progressrRemb.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 0, 0));
        progressrRemb.setMaximumSize(new java.awt.Dimension(9999, 8));
        progressPanel.add(progressrRemb);

        formPanel.add(progressPanel);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setAlignmentX(0.0F);
        jPanel5.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel5);

        lblSecPaiement.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecPaiement.setForeground(new java.awt.Color(14, 165, 233));
        lblSecPaiement.setText("PAIEMENT");
        lblSecPaiement.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecPaiement.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecPaiement);
        formPanel.add(jSeparator3);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setAlignmentX(0.0F);
        jPanel6.setMaximumSize(new java.awt.Dimension(9999, 8));
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel6);

        pnlLigne2.setBackground(new java.awt.Color(255, 255, 255));
        pnlLigne2.setAlignmentX(0.0F);
        pnlLigne2.setMaximumSize(new java.awt.Dimension(9999, 68));
        pnlLigne2.setPreferredSize(new java.awt.Dimension(0, 68));
        pnlLigne2.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelMontant.setBackground(new java.awt.Color(255, 255, 255));
        panelMontant.setLayout(new javax.swing.BoxLayout(panelMontant, javax.swing.BoxLayout.Y_AXIS));

        lblontant.setForeground(new java.awt.Color(71, 85, 105));
        lblontant.setText("Montant payé (F FCFA)");
        lblontant.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblontant.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelMontant.add(lblontant);

        txtMontant.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtMontant.setAlignmentX(0.0F);
        txtMontant.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelMontant.add(txtMontant);

        pnlLigne2.add(panelMontant);

        panelMoyen.setBackground(new java.awt.Color(255, 255, 255));
        panelMoyen.setLayout(new javax.swing.BoxLayout(panelMoyen, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setForeground(new java.awt.Color(71, 85, 105));
        jLabel3.setText("Moyen de paiement");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jLabel3.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelMoyen.add(jLabel3);

        cmbMoyen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Espèces", "Virement", "Mobile Money", "Chèque" }));
        cmbMoyen.setAlignmentX(0.0F);
        cmbMoyen.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelMoyen.add(cmbMoyen);

        pnlLigne2.add(panelMoyen);

        formPanel.add(pnlLigne2);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setAlignmentX(0.0F);
        jPanel7.setMaximumSize(new java.awt.Dimension(9999, 12));
        jPanel7.setPreferredSize(new java.awt.Dimension(0, 12));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        formPanel.add(jPanel7);

        panelLigne3.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne3.setAlignmentX(0.0F);
        panelLigne3.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne3.setPreferredSize(new java.awt.Dimension(0, 68));
        panelLigne3.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelDate.setBackground(new java.awt.Color(255, 255, 255));
        panelDate.setLayout(new javax.swing.BoxLayout(panelDate, javax.swing.BoxLayout.Y_AXIS));

        jLabel4.setForeground(new java.awt.Color(71, 85, 105));
        jLabel4.setText("Date paiement *");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jLabel4.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelDate.add(jLabel4);

        datePaiement.setAlignmentX(0.0F);
        datePaiement.setDateFormatString("dd/MM/yyyy");
        datePaiement.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDate.add(datePaiement);

        panelLigne3.add(panelDate);

        panelRef.setBackground(new java.awt.Color(255, 255, 255));
        panelRef.setLayout(new javax.swing.BoxLayout(panelRef, javax.swing.BoxLayout.Y_AXIS));

        lblRf.setForeground(new java.awt.Color(71, 85, 105));
        lblRf.setText("Référence (optionnel)");
        lblRf.setToolTipText("");
        lblRf.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblRf.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelRef.add(lblRf);

        txtRef.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtRef.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelRef.add(txtRef);

        panelLigne3.add(panelRef);

        formPanel.add(panelLigne3);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setAlignmentX(0.0F);
        jPanel8.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel8.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel8);

        lblSecapres.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecapres.setForeground(new java.awt.Color(14, 165, 233));
        lblSecapres.setText("APRÈS CE PAIMENT");
        lblSecapres.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecapres.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecapres);
        formPanel.add(jSeparator4);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setAlignmentX(0.0F);
        jPanel9.setMaximumSize(new java.awt.Dimension(9999, 8));
        jPanel9.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel9);

        recapCard.setBackground(new java.awt.Color(248, 250, 252));
        recapCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        recapCard.setAlignmentX(0.0F);
        recapCard.setLayout(new java.awt.GridLayout(1, 3));

        jPanel10.setBackground(new java.awt.Color(248, 250, 252));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        lblApresRembTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblApresRembTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblApresRembTitle.setText("Nouveau solde remboursé");
        jPanel10.add(lblApresRembTitle);

        lblApresRemVal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblApresRemVal.setForeground(new java.awt.Color(21, 128, 61));
        lblApresRemVal.setText("0 F CFA");
        jPanel10.add(lblApresRemVal);

        recapCard.add(jPanel10);

        panelApresReste.setBackground(new java.awt.Color(248, 250, 252));
        panelApresReste.setLayout(new javax.swing.BoxLayout(panelApresReste, javax.swing.BoxLayout.Y_AXIS));

        lblApresResteTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblApresResteTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblApresResteTitle.setText("Reste à payer");
        panelApresReste.add(lblApresResteTitle);

        lblApresResteVal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblApresResteVal.setForeground(new java.awt.Color(220, 28, 27));
        lblApresResteVal.setText("0 F CFA");
        panelApresReste.add(lblApresResteVal);

        recapCard.add(panelApresReste);

        panelApresEch.setBackground(new java.awt.Color(248, 250, 252));
        panelApresEch.setLayout(new javax.swing.BoxLayout(panelApresEch, javax.swing.BoxLayout.Y_AXIS));

        lblApresEchTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblApresEchTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblApresEchTitle.setText("Échéances  restantes");
        panelApresEch.add(lblApresEchTitle);

        lblApresEchVal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblApresEchVal.setForeground(new java.awt.Color(15, 23, 42));
        lblApresEchVal.setText("0 / 0");
        panelApresEch.add(lblApresEchVal);

        recapCard.add(panelApresEch);

        formPanel.add(recapCard);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setAlignmentX(0.0F);
        jPanel11.setMaximumSize(new java.awt.Dimension(9999, 20));
        jPanel11.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        formPanel.add(jPanel11);

        jScrollPane1.setViewportView(formPanel);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

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
                RemboursementDialog dialog = new RemboursementDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JComboBox<String> cmbClient;
    private javax.swing.JComboBox<String> cmbMoyen;
    private javax.swing.JComboBox<String> cmbPret;
    private com.toedter.calendar.JDateChooser datePaiement;
    private javax.swing.JPanel footerBtns;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel infoCard;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblApresEchTitle;
    private javax.swing.JLabel lblApresEchVal;
    private javax.swing.JLabel lblApresRemVal;
    private javax.swing.JLabel lblApresRembTitle;
    private javax.swing.JLabel lblApresResteTitle;
    private javax.swing.JLabel lblApresResteVal;
    private javax.swing.JLabel lblClient;
    private javax.swing.JLabel lblInfoEchTitle;
    private javax.swing.JLabel lblInfoEchVal;
    private javax.swing.JLabel lblInfoFinTitle;
    private javax.swing.JLabel lblInfoFinVal;
    private javax.swing.JLabel lblInfoMensTitle;
    private javax.swing.JLabel lblInfoMensVal;
    private javax.swing.JLabel lblInfoRembTitle;
    private javax.swing.JLabel lblInfoResteTitle;
    private javax.swing.JLabel lblInfoResteVal;
    private javax.swing.JLabel lblInfoTotalTitle;
    private javax.swing.JLabel lblInfoVal;
    private javax.swing.JLabel lblPret;
    private javax.swing.JLabel lblProgressTitle;
    private javax.swing.JLabel lblRembVal;
    private javax.swing.JLabel lblRf;
    private javax.swing.JLabel lblSecDetails;
    private javax.swing.JLabel lblSecPaiement;
    private javax.swing.JLabel lblSecapres;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblontant;
    private javax.swing.JPanel panelApresEch;
    private javax.swing.JPanel panelApresReste;
    private javax.swing.JPanel panelClient;
    private javax.swing.JPanel panelDate;
    private javax.swing.JPanel panelInfoEch;
    private javax.swing.JPanel panelInfoFin;
    private javax.swing.JPanel panelInfoMens;
    private javax.swing.JPanel panelInfoRembourse;
    private javax.swing.JPanel panelInfoReste;
    private javax.swing.JPanel panelInfoTotal;
    private javax.swing.JPanel panelLigne1;
    private javax.swing.JPanel panelLigne3;
    private javax.swing.JPanel panelMontant;
    private javax.swing.JPanel panelMoyen;
    private javax.swing.JPanel panelPret;
    private javax.swing.JPanel panelRef;
    private javax.swing.JPanel pnlLigne2;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JProgressBar progressrRemb;
    private javax.swing.JPanel recapCard;
    private javax.swing.JTextField txtMontant;
    private javax.swing.JTextField txtRef;
    // End of variables declaration//GEN-END:variables
}

