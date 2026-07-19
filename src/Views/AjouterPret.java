/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class AjouterPret extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AjouterPret.class.getName());

    /**
     * Creates new form AjouterPret
     */
    public AjouterPret(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        // Bloquer la saisie manuelle dans date fin
        txtDateFin.setEnabled(false);
        txtDateFin.getDateEditor().setEnabled(false);
        txtDateFin.setBackground(new java.awt.Color(248, 250, 252));
        chargerComptes();

        // Calculer automatiquement date fin et récap
        txtMontant.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculerRecap();
            }
        });
        txtTaux.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculerRecap();
            }
        });
        txtDuree.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculerDateFin();
                calculerRecap();
            }
        });
        dateDebut.addPropertyChangeListener("date", e -> {
            calculerDateFin();
        });

        // Afficher solde quand compte sélectionné
        cmbCompte.addActionListener(e -> afficherSolde());

        btnAnnuler.addActionListener(e -> dispose());
        btnAccorder.addActionListener(e -> btnAccorderActionPerformed(null));
        dateDebut.setMaxSelectableDate(new java.util.Date());
    }

    private void chargerComptes() {
        DAO.CompteDAO dao = new DAO.CompteDAO();
        java.util.List<Models.Compte> comptes = dao.listerActifs();
        cmbCompte.removeAllItems();
        cmbCompte.addItem("-- Choisir un compte --");
        for (Models.Compte c : comptes) {
            DAO.ClientDAO clientDao = new DAO.ClientDAO();
            Models.Client client = clientDao.chercher(c.getIdClient());
            String nomClient = client != null
                    ? client.getNom() + " " + client.getPrenom() : "—";
            cmbCompte.addItem(c.getIdCompte() + " | "
                    + c.getNumeroCompte() + " — " + nomClient);
        }
    }

    private void afficherSolde() {
        if (cmbCompte.getSelectedIndex() == 0) {
            txtSoldeDisp.setText("");
            return;
        }
        String selected = cmbCompte.getSelectedItem().toString();
        int idCompte = Integer.parseInt(selected.split(" \\| ")[0]);
        DAO.CompteDAO dao = new DAO.CompteDAO();
        Models.Compte compte = dao.chercher(idCompte);
        if (compte != null) {
            txtSoldeDisp.setText(String.format("%,.0f F CFA",
                    compte.getSoldeActuel()));
        }
    }

    private void calculerDateFin() {
        if (dateDebut.getDate() == null || txtDuree.getText().trim().isEmpty()) {
            return;
        }
        try {
            int duree = Integer.parseInt(txtDuree.getText().trim());
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(dateDebut.getDate());
            cal.add(java.util.Calendar.MONTH, duree);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            txtDateFin.setDate(cal.getTime());
        } catch (NumberFormatException e) {
            txtDateFin.setDate(null);
        }
    }

    private void calculerRecap() {
        try {
            double montant = Double.parseDouble(
                    txtMontant.getText().trim().replace(" ", "").replace(",", ""));
            double taux = Double.parseDouble(txtTaux.getText().trim());
            int duree = Integer.parseInt(txtDuree.getText().trim());

            if (montant <= 0 || taux <= 0 || duree <= 0) {
                reinitialiserRecap();
                return;
            }

            double interets = montant * taux * duree / 1200.0;
            double total = montant + interets;
            double mensualite = total / duree;

            lblRecapTotalVal.setText(String.format("%,.0f F CFA", total));
            lblMensualiteVal.setText(String.format("%,.0f F CFA", mensualite));
            lblInteretsVal.setText(String.format("%,.0f F CFA", interets));

        } catch (NumberFormatException e) {
            reinitialiserRecap();
        }
    }

    private void reinitialiserRecap() {
        lblRecapTotalVal.setText("0 F CFA");
        lblMensualiteVal.setText("0 F CFA");
        lblInteretsVal.setText("0 F CFA");
    }

    private void btnAccorderActionPerformed(java.awt.event.ActionEvent evt) {

        // ── 1. Validation compte ──
        if (cmbCompte.getSelectedIndex() == 0) {
            afficherErreur("Veuillez choisir un compte !");
            cmbCompte.requestFocus();
            return;
        }

        // ── 2. Validation montant ──
        if (txtMontant.getText().trim().isEmpty()) {
            afficherErreur("Le montant est obligatoire !");
            txtMontant.requestFocus();
            return;
        }

        // ── 3. Validation taux ──
        if (txtTaux.getText().trim().isEmpty()) {
            afficherErreur("Le taux d'intérêt est obligatoire !");
            txtTaux.requestFocus();
            return;
        }

        // ── 4. Validation durée ──
        if (txtDuree.getText().trim().isEmpty()) {
            afficherErreur("La durée est obligatoire !");
            txtDuree.requestFocus();
            return;
        }

        // ── 5. Validation date ──
        if (dateDebut.getDate() == null) {
            afficherErreur("La date de début est obligatoire !");
            dateDebut.requestFocus();
            return;
        }
        // ── NOUVEAU : bloquer date future ──
        java.util.Date aujourd_hui = new java.util.Date();
        if (dateDebut.getDate().after(aujourd_hui)) {
            afficherErreur("La date de début du prêt ne peut pas être dans le futur !");
            dateDebut.requestFocus();
            return;
        }

        try {
            double montant = Double.parseDouble(
                    txtMontant.getText().trim().replace(" ", "").replace(",", ""));
            double taux = Double.parseDouble(txtTaux.getText().trim());
            int duree = Integer.parseInt(txtDuree.getText().trim());

            // ── 6. Contrôles de cohérence ──
            if (montant <= 0) {
                afficherErreur("Le montant doit être supérieur à 0 !");
                txtMontant.requestFocus();
                return;
            }
// ── NOUVEAU : montant minimum ──
            if (montant < 10000) {
                afficherErreur("Le montant minimum d'un prêt est de 10 000 F CFA !");
                txtMontant.requestFocus();
                return;
            }
            if (montant > 100_000_000) {
                afficherErreur("Le montant ne peut pas dépasser 100 000 000 F CFA !");
                txtMontant.requestFocus();
                return;
            }
            if (taux <= 0 || taux > 100) {
                afficherErreur("Le taux doit être compris entre 0 et 100 % !");
                txtTaux.requestFocus();
                return;
            }
            if (duree < 1 || duree > 60) {
                afficherErreur("La durée doit être comprise entre 1 et 60 mois !");
                txtDuree.requestFocus();
                return;
            }
// ── NOUVEAU : vérifier garantie ──
            String garantie = cmbGarantie.getSelectedItem().toString().trim();
            if (garantie.isEmpty()) {
                afficherErreur("Veuillez choisir un type de garantie !");
                cmbGarantie.requestFocus();
                return;
            }


            // ── 7. Récupérer le compte ──
            String selected = cmbCompte.getSelectedItem().toString();
            int idCompte = Integer.parseInt(selected.split(" \\| ")[0]);
            DAO.CompteDAO compteDao = new DAO.CompteDAO();
            Models.Compte compte = compteDao.chercher(idCompte);

            if (compte == null) {
                afficherErreur("Compte introuvable. Veuillez réessayer.");
                return;
            }

            // ── 8. Vérifier qu'il n'a pas déjà un prêt en cours ──
            DAO.PretDAO pretDao = new DAO.PretDAO();
            if (pretDao.aDejaUnPretEnCours(idCompte)) {
                afficherErreur(
                        "Ce compte a déjà un prêt en cours !\n"
                        + "Un client ne peut pas avoir deux prêts simultanés.");
                return;
            }

            // ── 9. Vérification capacité de remboursement ──
            double interets = montant * taux * duree / 1200.0;
            double total = montant + interets;
            double mensualite = total / duree;

            DAO.ClientDAO clientDao = new DAO.ClientDAO();
            Models.Client client = clientDao.chercher(compte.getIdClient());
            if (client != null && client.getRevenuMensuel() > 0) {
                double ratioEndettement = (mensualite / client.getRevenuMensuel()) * 100;
                if (ratioEndettement > 33) {
                    int choix = javax.swing.JOptionPane.showConfirmDialog(this,
                            String.format(
                                    "⚠ Attention : la mensualité de %,.0f F CFA représente %.1f%% "
                                    + "du revenu mensuel du client (%,.0f F CFA).\n"
                                    + "Le taux d'endettement recommandé est de 33%% maximum.\n\n"
                                    + "Voulez-vous quand même accorder ce prêt ?",
                                    mensualite, ratioEndettement, client.getRevenuMensuel()),
                            "Capacité de remboursement dépassée",
                            javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    if (choix != javax.swing.JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            // ── 10. Calculer date de fin ──
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(dateDebut.getDate());
            cal.add(java.util.Calendar.MONTH, duree);
            java.sql.Date dateFinPrevue = new java.sql.Date(cal.getTimeInMillis());

            // ── 11. Créer le prêt ──
            // ── 11. Créer le prêt ──
            Models.Pret pret = new Models.Pret();
            pret.setIdCompte(idCompte);
            pret.setMontantPrincipal(montant);
            pret.setTauxInteret(taux);
            pret.setDureeMois(duree);
            pret.setDateDebut(new java.sql.Date(dateDebut.getDate().getTime()));
            pret.setDateFinPrevue(dateFinPrevue);
            pret.setMontantRembourse(0);
            pret.setStatut("En cours");
            pret.setGarantie(cmbGarantie != null
                    ? cmbGarantie.getSelectedItem().toString().trim() : "");

            if (pretDao.ajouter(pret)) {

                // ── 12. Transaction décaissement ──
                Models.Transaction trans = new Models.Transaction();
                trans.setIdCompte(idCompte);
                trans.setType("Décaissement");
                trans.setMontant(montant);
                trans.setDateHeure(new java.sql.Timestamp(System.currentTimeMillis()));
                trans.setStatut("Validé");
                trans.setMoyenPaiement("Virement");
                new DAO.TransactionDAO().ajouter(trans);
                // ── Imprimer le contrat ──
                try {
                    Utils.ImpressionUtil.imprimerContratPret(
                            pret,
                            compteDao.chercher(idCompte),
                            clientDao.chercher(compte.getIdClient()));
                } catch (Exception ex) {
                    System.out.println("Contrat non imprimé : " + ex.getMessage());
                }
                javax.swing.JOptionPane.showMessageDialog(this,
                        String.format(
                                "✔ Prêt accordé avec succès !\n\n"
                                + "   Montant accordé  : %,.0f F CFA\n"
                                + "   Intérêts totaux  : %,.0f F CFA\n"
                                + "   Montant total    : %,.0f F CFA\n"
                                + "   Mensualité       : %,.0f F CFA\n"
                                + "   Durée            : %d mois\n"
                                + "   Date de fin      : %s",
                                montant, interets, total, mensualite, duree,
                                new java.text.SimpleDateFormat("dd/MM/yyyy")
                                        .format(dateFinPrevue)),
                        "Prêt accordé",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                afficherErreur("Erreur lors de l'enregistrement du prêt.\nVérifiez la connexion à la base de données.");
            }

        } catch (NumberFormatException e) {
            afficherErreur(
                    "Valeurs incorrectes !\n"
                    + "• Montant : chiffres uniquement (ex: 500000)\n"
                    + "• Taux : chiffres uniquement (ex: 10)\n"
                    + "• Durée : nombre entier de mois (ex: 12)");
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
        headerLeft = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        lblRequired = new javax.swing.JLabel();
        footerBtns = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnAccorder = new javax.swing.JButton();
        scrollForm = new javax.swing.JScrollPane();
        formPanel = new javax.swing.JPanel();
        lblSecCompte = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        panelLigne1 = new javax.swing.JPanel();
        panelCompte = new javax.swing.JPanel();
        lblCompte = new javax.swing.JLabel();
        cmbCompte = new javax.swing.JComboBox<>();
        panelSolde = new javax.swing.JPanel();
        lblCompte1 = new javax.swing.JLabel();
        txtSoldeDisp = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblSecConditions = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        panelLigne2 = new javax.swing.JPanel();
        panelMontant = new javax.swing.JPanel();
        lblMontant = new javax.swing.JLabel();
        txtMontant = new javax.swing.JTextField();
        panelTaux = new javax.swing.JPanel();
        lblTaux = new javax.swing.JLabel();
        txtTaux = new javax.swing.JTextField();
        panelDuree = new javax.swing.JPanel();
        lblDuree = new javax.swing.JLabel();
        txtDuree = new javax.swing.JTextField();
        panelLigne3 = new javax.swing.JPanel();
        panelDateDebut = new javax.swing.JPanel();
        lblDateDebut = new javax.swing.JLabel();
        dateDebut = new com.toedter.calendar.JDateChooser();
        panelDateFin = new javax.swing.JPanel();
        lblDateFin = new javax.swing.JLabel();
        txtDateFin = new com.toedter.calendar.JDateChooser();
        panelGarantie = new javax.swing.JPanel();
        lblGarantie = new javax.swing.JLabel();
        cmbGarantie = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        lblSecRecap = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        panelRecap = new javax.swing.JPanel();
        panelRecapTotal = new javax.swing.JPanel();
        lblRecapTotalTitle = new javax.swing.JLabel();
        lblRecapTotalVal = new javax.swing.JLabel();
        panelRecapMensualite = new javax.swing.JPanel();
        lblMensualiteTitle = new javax.swing.JLabel();
        lblMensualiteVal = new javax.swing.JLabel();
        panelRecapInteretsTitle = new javax.swing.JPanel();
        lblInteretsTitle = new javax.swing.JLabel();
        lblInteretsVal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nouveau prêt");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(400, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(400, 600));

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 65));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerLeft.setBackground(new java.awt.Color(255, 255, 255));
        headerLeft.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerLeft.setPreferredSize(new java.awt.Dimension(400, 65));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(25, 23, 42));
        lblTitle.setText("Nouveau prêt");
        headerLeft.add(lblTitle);

        lblSub.setBackground(new java.awt.Color(100, 116, 139));
        lblSub.setText("Accorder un prêt à partir d'un compte épargne");
        headerLeft.add(lblSub);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.BorderLayout());

        lblRequired.setForeground(new java.awt.Color(100, 116, 139));
        lblRequired.setText("Champs obligatoires *");
        footerPanel.add(lblRequired, java.awt.BorderLayout.WEST);

        footerBtns.setBackground(new java.awt.Color(248, 250, 252));
        footerBtns.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 20));
        footerBtns.setForeground(new java.awt.Color(248, 250, 252));
        footerBtns.setPreferredSize(new java.awt.Dimension(260, 55));
        footerBtns.setLayout(new java.awt.GridLayout(1, 2, 8, 0));

        btnAnnuler.setBackground(new java.awt.Color(204, 255, 255));
        btnAnnuler.setText("Annuler");
        btnAnnuler.setPreferredSize(new java.awt.Dimension(110, 36));
        footerBtns.add(btnAnnuler);

        btnAccorder.setBackground(new java.awt.Color(14, 165, 233));
        btnAccorder.setForeground(new java.awt.Color(255, 255, 255));
        btnAccorder.setText("Accorder le prêt");
        btnAccorder.setPreferredSize(new java.awt.Dimension(130, 36));
        footerBtns.add(btnAccorder);

        footerPanel.add(footerBtns, java.awt.BorderLayout.CENTER);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        scrollForm.setBorder(null);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        lblSecCompte.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecCompte.setText("COMPTE ÉPARGNE");
        lblSecCompte.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecCompte.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecCompte);

        jSeparator1.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator1);

        panelLigne1.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne1.setAlignmentX(0.0F);
        panelLigne1.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne1.setPreferredSize(new java.awt.Dimension(0, 68));
        panelLigne1.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelCompte.setBackground(new java.awt.Color(255, 255, 255));
        panelCompte.setLayout(new javax.swing.BoxLayout(panelCompte, javax.swing.BoxLayout.Y_AXIS));

        lblCompte.setForeground(new java.awt.Color(71, 85, 105));
        lblCompte.setText("Compte *");
        lblCompte.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblCompte.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelCompte.add(lblCompte);

        cmbCompte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCompte.setAlignmentX(0.0F);
        cmbCompte.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelCompte.add(cmbCompte);

        panelLigne1.add(panelCompte);

        panelSolde.setBackground(new java.awt.Color(255, 255, 255));
        panelSolde.setLayout(new javax.swing.BoxLayout(panelSolde, javax.swing.BoxLayout.Y_AXIS));

        lblCompte1.setForeground(new java.awt.Color(71, 85, 105));
        lblCompte1.setText("Solde disponible");
        lblCompte1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblCompte1.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelSolde.add(lblCompte1);

        txtSoldeDisp.setEditable(false);
        txtSoldeDisp.setBackground(new java.awt.Color(248, 250, 252));
        txtSoldeDisp.setAlignmentX(0.0F);
        txtSoldeDisp.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelSolde.add(txtSoldeDisp);

        panelLigne1.add(panelSolde);

        formPanel.add(panelLigne1);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(9999, 12));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 12));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        formPanel.add(jPanel1);

        lblSecConditions.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecConditions.setForeground(new java.awt.Color(100, 116, 139));
        lblSecConditions.setText("CONDITIONS DU PRÊT");
        lblSecConditions.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecConditions.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecConditions);

        jSeparator2.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator2.setAlignmentX(0.0F);
        jSeparator2.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator2);

        panelLigne2.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne2.setAlignmentX(0.0F);
        panelLigne2.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne2.setPreferredSize(new java.awt.Dimension(0, 68));
        panelLigne2.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelMontant.setBackground(new java.awt.Color(255, 255, 255));
        panelMontant.setLayout(new javax.swing.BoxLayout(panelMontant, javax.swing.BoxLayout.Y_AXIS));

        lblMontant.setForeground(new java.awt.Color(71, 85, 105));
        lblMontant.setText("Montant principal *");
        lblMontant.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panelMontant.add(lblMontant);

        txtMontant.setAlignmentX(0.0F);
        txtMontant.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelMontant.add(txtMontant);

        panelLigne2.add(panelMontant);

        panelTaux.setBackground(new java.awt.Color(255, 255, 255));
        panelTaux.setLayout(new javax.swing.BoxLayout(panelTaux, javax.swing.BoxLayout.Y_AXIS));

        lblTaux.setForeground(new java.awt.Color(71, 85, 105));
        lblTaux.setText("Taux interêt (%)");
        lblTaux.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panelTaux.add(lblTaux);

        txtTaux.setAlignmentX(0.0F);
        txtTaux.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelTaux.add(txtTaux);

        panelLigne2.add(panelTaux);

        panelDuree.setBackground(new java.awt.Color(255, 255, 255));
        panelDuree.setForeground(new java.awt.Color(255, 255, 255));
        panelDuree.setLayout(new javax.swing.BoxLayout(panelDuree, javax.swing.BoxLayout.Y_AXIS));

        lblDuree.setForeground(new java.awt.Color(71, 85, 105));
        lblDuree.setText("Durée (mois) *");
        lblDuree.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panelDuree.add(lblDuree);

        txtDuree.setAlignmentX(0.0F);
        txtDuree.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDuree.add(txtDuree);

        panelLigne2.add(panelDuree);

        formPanel.add(panelLigne2);

        panelLigne3.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne3.setAlignmentX(0.0F);
        panelLigne3.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne3.setPreferredSize(new java.awt.Dimension(0, 68));
        panelLigne3.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelDateDebut.setBackground(new java.awt.Color(255, 255, 255));
        panelDateDebut.setLayout(new javax.swing.BoxLayout(panelDateDebut, javax.swing.BoxLayout.Y_AXIS));

        lblDateDebut.setForeground(new java.awt.Color(71, 85, 105));
        lblDateDebut.setText("Date de début *");
        lblDateDebut.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panelDateDebut.add(lblDateDebut);

        dateDebut.setAlignmentX(0.0F);
        dateDebut.setDateFormatString("dd/MM/yyyy");
        dateDebut.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDateDebut.add(dateDebut);

        panelLigne3.add(panelDateDebut);

        panelDateFin.setBackground(new java.awt.Color(255, 255, 255));
        panelDateFin.setLayout(new javax.swing.BoxLayout(panelDateFin, javax.swing.BoxLayout.Y_AXIS));

        lblDateFin.setForeground(new java.awt.Color(71, 85, 105));
        lblDateFin.setText("Date de fin prévue");
        lblDateFin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panelDateFin.add(lblDateFin);

        txtDateFin.setBackground(new java.awt.Color(248, 250, 252));
        txtDateFin.setAlignmentX(0.0F);
        txtDateFin.setDateFormatString("dd/MM/yyyy");
        txtDateFin.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDateFin.add(txtDateFin);

        panelLigne3.add(panelDateFin);

        formPanel.add(panelLigne3);

        panelGarantie.setBackground(new java.awt.Color(255, 255, 255));
        panelGarantie.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelGarantie.setLayout(new javax.swing.BoxLayout(panelGarantie, javax.swing.BoxLayout.Y_AXIS));

        lblGarantie.setForeground(new java.awt.Color(71, 85, 105));
        lblGarantie.setText("Type Garantie");
        panelGarantie.add(lblGarantie);

        cmbGarantie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nantissement de l'épargne", "Caution solidaire", "Domiciliation de salaire", "Garantie sur titre foncier", "Warrant agricole", "Garantie matérielle (bien meuble)", " " }));
        cmbGarantie.setAlignmentX(0.0F);
        cmbGarantie.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelGarantie.add(cmbGarantie);

        formPanel.add(panelGarantie);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setMaximumSize(new java.awt.Dimension(9999, 12));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 12));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        formPanel.add(jPanel2);

        lblSecRecap.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecRecap.setForeground(new java.awt.Color(100, 116, 139));
        lblSecRecap.setText("RÉCAPITULATIF");
        lblSecRecap.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecRecap.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecRecap);

        jSeparator3.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator3.setAlignmentX(0.0F);
        jSeparator3.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator3);

        panelRecap.setBackground(new java.awt.Color(248, 250, 252));
        panelRecap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));
        panelRecap.setAlignmentX(0.0F);
        panelRecap.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelRecap.setPreferredSize(new java.awt.Dimension(0, 68));
        panelRecap.setLayout(new java.awt.GridLayout(1, 3));

        panelRecapTotal.setBackground(new java.awt.Color(248, 250, 252));
        panelRecapTotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        panelRecapTotal.setLayout(new javax.swing.BoxLayout(panelRecapTotal, javax.swing.BoxLayout.Y_AXIS));

        lblRecapTotalTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblRecapTotalTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblRecapTotalTitle.setText("Montant à rembourser");
        panelRecapTotal.add(lblRecapTotalTitle);

        lblRecapTotalVal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRecapTotalVal.setForeground(new java.awt.Color(14, 165, 233));
        lblRecapTotalVal.setText("0 FCFA");
        panelRecapTotal.add(lblRecapTotalVal);

        panelRecap.add(panelRecapTotal);

        panelRecapMensualite.setBackground(new java.awt.Color(248, 250, 252));
        panelRecapMensualite.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        panelRecapMensualite.setLayout(new javax.swing.BoxLayout(panelRecapMensualite, javax.swing.BoxLayout.Y_AXIS));

        lblMensualiteTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblMensualiteTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblMensualiteTitle.setText("Mensualité estimée");
        panelRecapMensualite.add(lblMensualiteTitle);

        lblMensualiteVal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMensualiteVal.setForeground(new java.awt.Color(15, 23, 42));
        lblMensualiteVal.setText("0 FCFA");
        panelRecapMensualite.add(lblMensualiteVal);

        panelRecap.add(panelRecapMensualite);

        panelRecapInteretsTitle.setBackground(new java.awt.Color(248, 250, 252));
        panelRecapInteretsTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        panelRecapInteretsTitle.setLayout(new javax.swing.BoxLayout(panelRecapInteretsTitle, javax.swing.BoxLayout.Y_AXIS));

        lblInteretsTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblInteretsTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblInteretsTitle.setText("Interêts totaux");
        panelRecapInteretsTitle.add(lblInteretsTitle);

        lblInteretsVal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblInteretsVal.setForeground(new java.awt.Color(15, 23, 42));
        lblInteretsVal.setText("0 FCFA");
        panelRecapInteretsTitle.add(lblInteretsVal);

        panelRecap.add(panelRecapInteretsTitle);

        formPanel.add(panelRecap);

        scrollForm.setViewportView(formPanel);

        getContentPane().add(scrollForm, java.awt.BorderLayout.CENTER);

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
                AjouterPret dialog = new AjouterPret(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAccorder;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JComboBox<String> cmbCompte;
    private javax.swing.JComboBox<String> cmbGarantie;
    private com.toedter.calendar.JDateChooser dateDebut;
    private javax.swing.JPanel footerBtns;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblCompte;
    private javax.swing.JLabel lblCompte1;
    private javax.swing.JLabel lblDateDebut;
    private javax.swing.JLabel lblDateFin;
    private javax.swing.JLabel lblDuree;
    private javax.swing.JLabel lblGarantie;
    private javax.swing.JLabel lblInteretsTitle;
    private javax.swing.JLabel lblInteretsVal;
    private javax.swing.JLabel lblMensualiteTitle;
    private javax.swing.JLabel lblMensualiteVal;
    private javax.swing.JLabel lblMontant;
    private javax.swing.JLabel lblRecapTotalTitle;
    private javax.swing.JLabel lblRecapTotalVal;
    private javax.swing.JLabel lblRequired;
    private javax.swing.JLabel lblSecCompte;
    private javax.swing.JLabel lblSecConditions;
    private javax.swing.JLabel lblSecRecap;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTaux;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelCompte;
    private javax.swing.JPanel panelDateDebut;
    private javax.swing.JPanel panelDateFin;
    private javax.swing.JPanel panelDuree;
    private javax.swing.JPanel panelGarantie;
    private javax.swing.JPanel panelLigne1;
    private javax.swing.JPanel panelLigne2;
    private javax.swing.JPanel panelLigne3;
    private javax.swing.JPanel panelMontant;
    private javax.swing.JPanel panelRecap;
    private javax.swing.JPanel panelRecapInteretsTitle;
    private javax.swing.JPanel panelRecapMensualite;
    private javax.swing.JPanel panelRecapTotal;
    private javax.swing.JPanel panelSolde;
    private javax.swing.JPanel panelTaux;
    private javax.swing.JScrollPane scrollForm;
    private com.toedter.calendar.JDateChooser txtDateFin;
    private javax.swing.JTextField txtDuree;
    private javax.swing.JTextField txtMontant;
    private javax.swing.JTextField txtSoldeDisp;
    private javax.swing.JTextField txtTaux;
    // End of variables declaration//GEN-END:variables
}
