/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import javax.swing.Box;

/**
 *
 * @author Admin
 */
public class DetailPret extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DetailPret.class.getName());

    /**
     * Creates new form DetailPret
     */
    private int idPretCourant;

    public DetailPret(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(520, 580);
        setLocationRelativeTo(parent);
        configurerStyle();

        btnFermer.addActionListener(e -> dispose());

        btnRembourser.addActionListener(e -> {
            RemboursementDialog dialog = new RemboursementDialog(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(DetailPret.this), true);
            dialog.preselectionnerPret(idPretCourant);
            dialog.setVisible(true);
            chargerPret(idPretCourant);
        });

        // Remplacer btnRejeter par Remboursement anticipé
        btnRejeter.setText("Anticipé ⚡");
        btnRejeter.setBackground(new java.awt.Color(254, 226, 226));
        btnRejeter.setForeground(new java.awt.Color(220, 38, 38));
        btnRejeter.addActionListener(e -> {
            DAO.PretDAO dao = new DAO.PretDAO();
            Models.Pret pret = dao.chercher(idPretCourant);
            if (pret == null) {
                return;
            }

            if ("Remboursé".equals(pret.getStatut())) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Ce prêt est déjà remboursé !",
                        "Information",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            double interets = pret.getMontantPrincipal()
                    * pret.getTauxInteret() * pret.getDureeMois() / 1200.0;
            double total = pret.getMontantPrincipal() + interets;
            double reste = Math.max(0, total - pret.getMontantRembourse());

            double mensualite = total / pret.getDureeMois();
            int echeancesPayees = mensualite > 0
                    ? (int) (pret.getMontantRembourse() / mensualite) : 0;
            int echeancesRestantes = Math.max(0,
                    pret.getDureeMois() - echeancesPayees);
            double interetsRestants = pret.getMontantPrincipal()
                    * pret.getTauxInteret() * echeancesRestantes / 1200.0;
            double remise = interetsRestants * 0.5;
            double montantAnticipe = Math.max(0, reste - remise);

            int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                    String.format(
                            "Remboursement anticipé :\n\n"
                            + "   Reste à payer          : %,.0f F CFA\n"
                            + "   Remise (50%% intérêts)  : %,.0f F CFA\n"
                            + "   Montant à payer        : %,.0f F CFA\n\n"
                            + "Confirmer ?",
                            reste, remise, montantAnticipe),
                    "Remboursement anticipé",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE);

            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }

            dao.mettreAJourRemboursement(idPretCourant, montantAnticipe);
            dao.modifierStatut(idPretCourant, "Remboursé");

            Models.Transaction trans = new Models.Transaction();
            trans.setIdCompte(pret.getIdCompte());
            trans.setType("Remboursement anticipé");
            trans.setMontant(montantAnticipe);
            trans.setDateHeure(new java.sql.Timestamp(System.currentTimeMillis()));
            trans.setStatut("Validé");
            trans.setMoyenPaiement("Espèces");
            new DAO.TransactionDAO().ajouter(trans);

            javax.swing.JOptionPane.showMessageDialog(this,
                    String.format(
                            "✔ Remboursement anticipé effectué !\n\n"
                            + "   Montant payé : %,.0f F CFA\n"
                            + "   Remise       : %,.0f F CFA",
                            montantAnticipe, remise),
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            chargerPret(idPretCourant);
        });

        btnImprimer.addActionListener(e -> {
            TableauAmortissement dialog = new TableauAmortissement(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(DetailPret.this), true);
            dialog.chargerPret(idPretCourant);
            dialog.setVisible(true);
        });
    }

    private void configurerStyle() {
        btnFermer.setBackground(new java.awt.Color(241, 245, 249));
        btnFermer.setForeground(new java.awt.Color(71, 85, 105));
        btnFermer.setBorderPainted(false);
        btnFermer.setFocusPainted(false);
        btnFermer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnRembourser.setBorderPainted(false);
        btnRembourser.setFocusPainted(false);
        btnRembourser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnImprimer.setBackground(new java.awt.Color(14, 165, 233));
        btnImprimer.setForeground(java.awt.Color.WHITE);
        btnImprimer.setBorderPainted(false);
        btnImprimer.setFocusPainted(false);
        btnImprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    public void chargerPret(int idPret) {
        idPretCourant = idPret;
        new DAO.PretDAO().mettreAJourStatuts();

        DAO.PretDAO dao = new DAO.PretDAO();
        Models.Pret pret = dao.chercher(idPret);
        if (pret == null) {
            return;
        }

        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        Models.Compte compte = compteDao.chercher(pret.getIdCompte());
        String nomClient = "—";
        String numCompte = "—";
        if (compte != null) {
            numCompte = compte.getNumeroCompte();
            DAO.ClientDAO clientDao = new DAO.ClientDAO();
            Models.Client client = clientDao.chercher(compte.getIdClient());
            if (client != null) {
                nomClient = client.getNom() + " " + client.getPrenom();
            }
        }

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Header
        lblNomClient.setText(nomClient);
        lblStatutPret.setText(pret.getStatut());
        switch (pret.getStatut()) {
            case "En cours" ->
                lblStatutPret.setForeground(
                        new java.awt.Color(14, 165, 233));
            case "Remboursé" ->
                lblStatutPret.setForeground(
                        new java.awt.Color(21, 128, 61));
            case "En retard" ->
                lblStatutPret.setForeground(
                        new java.awt.Color(220, 38, 38));
            default ->
                lblStatutPret.setForeground(
                        new java.awt.Color(100, 116, 139));
        }

        // Section 1 — Infos prêt
        valMontant.setText(String.format("%,.0f F CFA",
                pret.getMontantPrincipal()));
        valTaux.setText(pret.getTauxInteret() + " %");
        valDuree.setText(pret.getDureeMois() + " mois");
        valDateDebut.setText(pret.getDateDebut() != null
                ? sdf.format(pret.getDateDebut()) : "—");
        valDateFin.setText(pret.getDateFinPrevue() != null
                ? sdf.format(pret.getDateFinPrevue()) : "—");
        valCompte.setText(numCompte);

        // Garantie — afficher dans le label compte si pas de champ dédié
        if (pret.getGarantie() != null && !pret.getGarantie().isEmpty()) {
            lblCompte.setText("Garantie");
            valCompte.setText(pret.getGarantie());
        }

        // Section 2 — Remboursement (calcul correct)
        double interets = pret.getMontantPrincipal()
                * pret.getTauxInteret() * pret.getDureeMois() / 1200.0;
        double total = pret.getMontantPrincipal() + interets;
        double rembourse = pret.getMontantRembourse();
        double reste = Math.max(0, total - rembourse);
        double mensualite = total / pret.getDureeMois();

        valRembourse.setText(String.format("%,.0f F CFA", rembourse));
        valReste.setText(reste <= 2.0 ? "0 F CFA"
                : String.format("%,.0f F CFA", reste));
        valMensualite.setText(String.format("%,.0f F CFA", mensualite));

        // Pénalité si en retard
        if ("En retard".equals(pret.getStatut())) {
            double penalite = new DAO.PretDAO().calculerPenalite(idPretCourant);
            if (penalite > 0) {
                valReste.setText(String.format(
                        "%,.0f F CFA  (+ %,.0f F pénalité)",
                        reste, penalite));
                valReste.setForeground(new java.awt.Color(220, 38, 38));
            }
        }

        // Progression
        int pct;
        if (reste <= 2.0) {
            pct = 100;
        } else {
            pct = (int) ((rembourse / total) * 100);
            pct = Math.min(Math.max(pct, 0), 100);
        }
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(pct);
        progressBar.setString(pct + "% remboursé");
        progressBar.setStringPainted(true);
        if (pct >= 100) {
            progressBar.setForeground(
                    new java.awt.Color(21, 128, 61));
        } else if (pct >= 50) {
            progressBar.setForeground(
                    new java.awt.Color(14, 165, 233));
        } else {
            progressBar.setForeground(new java.awt.Color(245, 158, 11));
        }
        progressBar.repaint();

        // Désactiver boutons si remboursé
        boolean actif = !"Remboursé".equals(pret.getStatut());
        btnRembourser.setEnabled(actif);
        btnRejeter.setEnabled(actif);
    }

    private void imprimerPret() {
        DAO.PretDAO dao = new DAO.PretDAO();
        Models.Pret pret = dao.chercher(idPretCourant);
        if (pret == null) {
            return;
        }

        // Récupérer infos client
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        Models.Compte compte = compteDao.chercher(pret.getIdCompte());
        String nomClient = "—";
        if (compte != null) {
            DAO.ClientDAO clientDao = new DAO.ClientDAO();
            Models.Client client = clientDao.chercher(compte.getIdClient());
            if (client != null) {
                nomClient = client.getNom() + " " + client.getPrenom();
            }
        }

        // Créer le dialog d'impression
        final Models.Pret p = pret;
        final String nom = nomClient;

        javax.swing.JDialog printDialog = new javax.swing.JDialog(this, "Reçu de prêt", true);
        printDialog.setSize(400, 500);
        printDialog.setLocationRelativeTo(this);

        javax.swing.JPanel printPanel = new javax.swing.JPanel();
        printPanel.setLayout(new javax.swing.BoxLayout(printPanel, javax.swing.BoxLayout.Y_AXIS));
        printPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 32, 24, 32));
        printPanel.setBackground(java.awt.Color.WHITE);

        // Titre
        javax.swing.JLabel titre = new javax.swing.JLabel("REÇU DE PRÊT");
        titre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        titre.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        titre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));

        javax.swing.JLabel appName = new javax.swing.JLabel("SmartCaisse — Microfinance");
        appName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        appName.setForeground(new java.awt.Color(100, 116, 139));
        appName.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        appName.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JSeparatorLine sep1 = new JSeparatorLine();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Contenu
        String[][] infos = {
            {"Client", nom},
            {"Prêt N°", String.format("%03d", p.getIdPret())},
            {"Montant", String.format("%,.0f F CFA", p.getMontantPrincipal())},
            {"Taux", p.getTauxInteret() + "%"},
            {"Date début", p.getDateDebut() != null ? sdf.format(p.getDateDebut()) : "—"},
            {"Date fin", p.getDateFinPrevue() != null ? p.getDateFinPrevue().toString() : "—"},
            {"Remboursé", String.format("%,.0f F CFA", p.getMontantRembourse())},
            {"Statut", p.getStatut()}
        };

        javax.swing.JPanel infosPanel = new javax.swing.JPanel(
                new java.awt.GridLayout(infos.length, 2, 8, 8));
        infosPanel.setBackground(java.awt.Color.WHITE);
        infosPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        for (String[] info : infos) {
            javax.swing.JLabel lbl = new javax.swing.JLabel(info[0]);
            lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            lbl.setForeground(new java.awt.Color(100, 116, 139));

            javax.swing.JLabel val = new javax.swing.JLabel(info[1]);
            val.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            val.setForeground(new java.awt.Color(15, 23, 42));

            infosPanel.add(lbl);
            infosPanel.add(val);
        }

        // Date impression
        String dateImpression = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(new java.util.Date());
        javax.swing.JLabel dateLabel = new javax.swing.JLabel(
                "Imprimé le : " + dateImpression);
        dateLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        dateLabel.setForeground(new java.awt.Color(100, 116, 139));
        dateLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        dateLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));

        // Bouton imprimer
        javax.swing.JButton btnPrint = new javax.swing.JButton("🖨 Imprimer");
        btnPrint.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnPrint.setBackground(new java.awt.Color(14, 165, 233));
        btnPrint.setForeground(java.awt.Color.WHITE);
        btnPrint.setBorderPainted(false);
        btnPrint.setFocusPainted(false);
        btnPrint.setPreferredSize(new java.awt.Dimension(150, 36));
        btnPrint.setMaximumSize(new java.awt.Dimension(150, 36));
        btnPrint.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));

        btnPrint.addActionListener(ev -> {
            java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) {
                    return java.awt.print.Printable.NO_SUCH_PAGE;
                }
                printPanel.print(graphics);
                return java.awt.print.Printable.PAGE_EXISTS;
            });
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (java.awt.print.PrinterException ex) {
                    javax.swing.JOptionPane.showMessageDialog(printDialog,
                            "Erreur d'impression : " + ex.getMessage(),
                            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        printPanel.add(titre);
        printPanel.add(appName);
        printPanel.add(sep1);
        printPanel.add(Box.createVerticalStrut(12));
        printPanel.add(infosPanel);
        printPanel.add(dateLabel);
        printPanel.add(Box.createVerticalStrut(16));
        printPanel.add(btnPrint);

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(printPanel);
        scroll.setBorder(null);
        printDialog.add(scroll);
        printDialog.setVisible(true);
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
        lblNomClient = new javax.swing.JLabel();
        lblStatutPret = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        btnFermer = new javax.swing.JButton();
        btnRembourser = new javax.swing.JButton();
        btnRejeter = new javax.swing.JButton();
        btnImprimer = new javax.swing.JButton();
        scrollInfo = new javax.swing.JScrollPane();
        infoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        gridInfo1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblMontant = new javax.swing.JLabel();
        valMontant = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblTaux = new javax.swing.JLabel();
        valTaux = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblDuree = new javax.swing.JLabel();
        valDuree = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblDateDebut = new javax.swing.JLabel();
        valDateDebut = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblDateFin = new javax.swing.JLabel();
        valDateFin = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblCompte = new javax.swing.JLabel();
        valCompte = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        gridInfo2 = new javax.swing.JPanel();
        panelRemboursement = new javax.swing.JPanel();
        lblRembursement = new javax.swing.JLabel();
        valRembourse = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lblReste = new javax.swing.JLabel();
        valReste = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lblCompte3 = new javax.swing.JLabel();
        valMensualite = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        progressPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Détail du prêt");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(520, 580));
        setResizable(false);

        headerPanel.setBackground(new java.awt.Color(219, 234, 254));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 90));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerCenter.setBackground(new java.awt.Color(219, 234, 254));
        headerCenter.setLayout(new javax.swing.BoxLayout(headerCenter, javax.swing.BoxLayout.Y_AXIS));

        lblNomClient.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblNomClient.setForeground(new java.awt.Color(15, 23, 42));
        lblNomClient.setText("Nom Client");
        lblNomClient.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerCenter.add(lblNomClient);

        lblStatutPret.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblStatutPret.setForeground(new java.awt.Color(14, 165, 233));
        lblStatutPret.setText("En cours");
        lblStatutPret.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerCenter.add(lblStatutPret);

        headerPanel.add(headerCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(226, 232, 240));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        btnFermer.setText("Fermer");
        btnFermer.setPreferredSize(new java.awt.Dimension(100, 36));
        footerPanel.add(btnFermer);

        btnRembourser.setBackground(new java.awt.Color(220, 252, 231));
        btnRembourser.setForeground(new java.awt.Color(21, 128, 61));
        btnRembourser.setText("Rembourser");
        btnRembourser.setPreferredSize(new java.awt.Dimension(130, 36));
        footerPanel.add(btnRembourser);

        btnRejeter.setBackground(new java.awt.Color(254, 226, 226));
        btnRejeter.setForeground(new java.awt.Color(220, 38, 38));
        btnRejeter.setText("Rejeter");
        btnRejeter.setPreferredSize(new java.awt.Dimension(100, 36));
        footerPanel.add(btnRejeter);

        btnImprimer.setText("Imprimer");
        btnImprimer.setPreferredSize(new java.awt.Dimension(100, 36));
        footerPanel.add(btnImprimer);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(14, 165, 233));
        jLabel1.setText("INFORMATIONS DU PRÊT");
        jLabel1.setMaximumSize(new java.awt.Dimension(9999, 25));
        infoPanel.add(jLabel1);

        jSeparator1.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator1);

        gridInfo1.setBackground(new java.awt.Color(255, 255, 255));
        gridInfo1.setAlignmentX(0.0F);
        gridInfo1.setMaximumSize(new java.awt.Dimension(9999, 120));
        gridInfo1.setPreferredSize(new java.awt.Dimension(0, 120));
        gridInfo1.setLayout(new java.awt.GridLayout(2, 3, 12, 12));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        lblMontant.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblMontant.setForeground(new java.awt.Color(100, 116, 139));
        lblMontant.setText("Montant principal");
        jPanel1.add(lblMontant);

        valMontant.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valMontant.setForeground(new java.awt.Color(15, 23, 42));
        valMontant.setText("-");
        jPanel1.add(valMontant);

        gridInfo1.add(jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        lblTaux.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTaux.setForeground(new java.awt.Color(100, 116, 139));
        lblTaux.setText("Taux d'intérêt");
        jPanel2.add(lblTaux);

        valTaux.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valTaux.setForeground(new java.awt.Color(15, 23, 42));
        valTaux.setText("-");
        jPanel2.add(valTaux);

        gridInfo1.add(jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        lblDuree.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDuree.setForeground(new java.awt.Color(100, 116, 139));
        lblDuree.setText("Durée");
        jPanel3.add(lblDuree);

        valDuree.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valDuree.setForeground(new java.awt.Color(15, 23, 42));
        valDuree.setText("-");
        jPanel3.add(valDuree);

        gridInfo1.add(jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        lblDateDebut.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDateDebut.setForeground(new java.awt.Color(100, 116, 139));
        lblDateDebut.setText("Date début");
        jPanel4.add(lblDateDebut);

        valDateDebut.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valDateDebut.setForeground(new java.awt.Color(15, 23, 42));
        valDateDebut.setText("-");
        jPanel4.add(valDateDebut);

        gridInfo1.add(jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        lblDateFin.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDateFin.setForeground(new java.awt.Color(100, 116, 139));
        lblDateFin.setText("Date fin prévue");
        jPanel5.add(lblDateFin);

        valDateFin.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valDateFin.setForeground(new java.awt.Color(15, 23, 42));
        valDateFin.setText("-");
        jPanel5.add(valDateFin);

        gridInfo1.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        lblCompte.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblCompte.setForeground(new java.awt.Color(100, 116, 139));
        lblCompte.setText("Compte associé");
        jPanel6.add(lblCompte);

        valCompte.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valCompte.setForeground(new java.awt.Color(15, 23, 42));
        valCompte.setText("-");
        jPanel6.add(valCompte);

        gridInfo1.add(jPanel6);

        infoPanel.add(gridInfo1);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel7.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        infoPanel.add(jPanel7);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(14, 165, 233));
        jLabel2.setText("ÉTAT DU REMBOURSEMENT");
        jLabel2.setMaximumSize(new java.awt.Dimension(9999, 25));
        infoPanel.add(jLabel2);

        jSeparator2.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator2.setAlignmentX(0.0F);
        jSeparator2.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator2);

        gridInfo2.setBackground(new java.awt.Color(255, 255, 255));
        gridInfo2.setAlignmentX(0.0F);
        gridInfo2.setMaximumSize(new java.awt.Dimension(9999, 65));
        gridInfo2.setPreferredSize(new java.awt.Dimension(0, 65));
        gridInfo2.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelRemboursement.setBackground(new java.awt.Color(255, 255, 255));
        panelRemboursement.setLayout(new javax.swing.BoxLayout(panelRemboursement, javax.swing.BoxLayout.Y_AXIS));

        lblRembursement.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblRembursement.setForeground(new java.awt.Color(100, 116, 139));
        lblRembursement.setText("Montant remboursé");
        panelRemboursement.add(lblRembursement);

        valRembourse.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valRembourse.setForeground(new java.awt.Color(15, 23, 42));
        valRembourse.setText("-");
        panelRemboursement.add(valRembourse);

        gridInfo2.add(panelRemboursement);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        lblReste.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblReste.setForeground(new java.awt.Color(100, 116, 139));
        lblReste.setText("Reste à payer");
        jPanel9.add(lblReste);

        valReste.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valReste.setForeground(new java.awt.Color(220, 38, 38));
        valReste.setText("-");
        jPanel9.add(valReste);

        gridInfo2.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        lblCompte3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblCompte3.setForeground(new java.awt.Color(100, 116, 139));
        lblCompte3.setText("Mensualité estimée");
        jPanel10.add(lblCompte3);

        valMensualite.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valMensualite.setForeground(new java.awt.Color(15, 23, 42));
        valMensualite.setText("-");
        jPanel10.add(valMensualite);

        gridInfo2.add(jPanel10);

        infoPanel.add(gridInfo2);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel8.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        infoPanel.add(jPanel8);

        progressPanel.setBackground(new java.awt.Color(248, 250, 252));
        progressPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));
        progressPanel.setAlignmentX(0.0F);
        progressPanel.setMaximumSize(new java.awt.Dimension(9999, 60));
        progressPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        progressPanel.setLayout(new javax.swing.BoxLayout(progressPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setForeground(new java.awt.Color(100, 116, 139));
        jLabel3.setText("Progression du remmboursement");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 4, 12));
        progressPanel.add(jLabel3);

        progressBar.setAlignmentX(0.0F);
        progressBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 12, 8));
        progressBar.setMaximumSize(new java.awt.Dimension(9999, 16));
        progressBar.setPreferredSize(new java.awt.Dimension(50, 50));
        progressBar.setStringPainted(true);
        progressPanel.add(progressBar);

        infoPanel.add(progressPanel);

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
                DetailPret dialog = new DetailPret(new javax.swing.JFrame(), true);
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

    class JSeparatorLine extends javax.swing.JPanel {

        public JSeparatorLine() {
            setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 1));
            setPreferredSize(new java.awt.Dimension(0, 1));
            setBackground(new java.awt.Color(226, 232, 240));
            setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFermer;
    private javax.swing.JButton btnImprimer;
    private javax.swing.JButton btnRejeter;
    private javax.swing.JButton btnRembourser;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel gridInfo1;
    private javax.swing.JPanel gridInfo2;
    private javax.swing.JPanel headerCenter;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblCompte;
    private javax.swing.JLabel lblCompte3;
    private javax.swing.JLabel lblDateDebut;
    private javax.swing.JLabel lblDateFin;
    private javax.swing.JLabel lblDuree;
    private javax.swing.JLabel lblMontant;
    private javax.swing.JLabel lblNomClient;
    private javax.swing.JLabel lblRembursement;
    private javax.swing.JLabel lblReste;
    private javax.swing.JLabel lblStatutPret;
    private javax.swing.JLabel lblTaux;
    private javax.swing.JPanel panelRemboursement;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JScrollPane scrollInfo;
    private javax.swing.JLabel valCompte;
    private javax.swing.JLabel valDateDebut;
    private javax.swing.JLabel valDateFin;
    private javax.swing.JLabel valDuree;
    private javax.swing.JLabel valMensualite;
    private javax.swing.JLabel valMontant;
    private javax.swing.JLabel valRembourse;
    private javax.swing.JLabel valReste;
    private javax.swing.JLabel valTaux;
    // End of variables declaration//GEN-END:variables
}
