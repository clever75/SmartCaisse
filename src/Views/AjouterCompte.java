/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class AjouterCompte extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AjouterCompte.class.getName());

    public AjouterCompte(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        configurerStyle();
        chargerClients();
        genererNumeroCompte();
        btnAnnuler.addActionListener(e -> dispose());

        cardTypeCourant.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectionnerType("Courant");
            }
        });
        cardTypeATerme.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectionnerType("À terme");
            }
        });
        cardTypeTontine.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectionnerType("Tontine");
            }
        });

        panelTaux.setVisible(false);
        panelTontine.setVisible(false);
        selectionnerType("Courant");
        dateOuverture.setMaxSelectableDate(new java.util.Date());
    }

    private void configurerStyle() {
        javax.swing.border.Border fieldBorder
                = javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(
                                new java.awt.Color(226, 232, 240)),
                        javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));

        txtSolde.setBorder(fieldBorder);
        txtTaux.setBorder(fieldBorder);
        txtDuree.setBorder(fieldBorder);
        txtMontantPeriodique.setBorder(fieldBorder);
        txtDureeTontine.setBorder(fieldBorder);

        cmbClient.setBackground(java.awt.Color.WHITE);
        cmbClient.setFont(new java.awt.Font("Segoe UI",
                java.awt.Font.PLAIN, 13));

        // Supprimer item vide fréquence
        cmbFrequence.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Mensuelle", "Hebdomadaire"}));
        cmbFrequence.setBackground(java.awt.Color.WHITE);

        // Boutons
        btnAnnuler.setBackground(new java.awt.Color(241, 245, 249));
        btnAnnuler.setForeground(new java.awt.Color(71, 85, 105));
        btnAnnuler.setBorderPainted(false);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        btnCreer.setBackground(new java.awt.Color(14, 165, 233));
        btnCreer.setForeground(java.awt.Color.WHITE);
        btnCreer.setBorderPainted(false);
        btnCreer.setFocusPainted(false);
        btnCreer.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        // Sections
        lblSecType.setForeground(new java.awt.Color(14, 165, 233));
        lblSecInfo.setForeground(new java.awt.Color(14, 165, 233));
    }

    private String typeSelectionne = "Courant";

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

    private void genererNumeroCompte() {
        // Génération unique basée sur timestamp
        String annee = new java.text.SimpleDateFormat("yyyy")
                .format(new java.util.Date());
        String num;
        DAO.CompteDAO dao = new DAO.CompteDAO();
        do {
            int rand = (int) (System.currentTimeMillis() % 9000) + 1000;
            num = String.format("CPT-%s-%04d", annee, rand);
        } while (dao.numeroCompteExiste(num));
        txtNumeroCompte.setText(num);
    }

    private void selectionnerType(String type) {
        typeSelectionne = type;

        // Reset tous les cards
        java.awt.Color bgNormal = new java.awt.Color(255, 255, 255);
        java.awt.Color borderNormal = new java.awt.Color(226, 232, 240);

        for (javax.swing.JPanel card : new javax.swing.JPanel[]{
            cardTypeCourant, cardTypeATerme, cardTypeTontine}) {
            card.setBackground(bgNormal);
            card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(
                            borderNormal, 1),
                    javax.swing.BorderFactory.createEmptyBorder(
                            10, 12, 10, 12)));
        }
        for (javax.swing.JLabel lbl : new javax.swing.JLabel[]{
            lblTypeCourant, lblTypeATerme, lblTypeTontine}) {
            lbl.setForeground(new java.awt.Color(15, 23, 42));
        }

        // Activer le card sélectionné
        javax.swing.JPanel cardActif = "Courant".equals(type)
                ? cardTypeCourant
                : "À terme".equals(type) ? cardTypeATerme
                : cardTypeTontine;
        javax.swing.JLabel lblActif = "Courant".equals(type)
                ? lblTypeCourant
                : "À terme".equals(type) ? lblTypeATerme
                : lblTypeTontine;

        cardActif.setBackground(new java.awt.Color(219, 234, 254));
        cardActif.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(14, 165, 233), 2),
                javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        lblActif.setForeground(new java.awt.Color(14, 165, 233));

        mettreAJourChamps(type);
    }

    private void mettreAJourChamps(String type) {
        switch (type) {
            case "Courant" -> {
                panelTaux.setVisible(false);
                panelTontine.setVisible(false);
            }
            case "À terme" -> {
                panelTaux.setVisible(true);
                panelTontine.setVisible(false);
            }
            case "Tontine" -> {
                panelTaux.setVisible(false);
                panelTontine.setVisible(true);
            }
        }
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void afficherErreur(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message,
                "Erreur de saisie",
                javax.swing.JOptionPane.WARNING_MESSAGE);
    }

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
        btnCreer = new javax.swing.JButton();
        scrollForm = new javax.swing.JScrollPane();
        formPanel = new javax.swing.JPanel();
        lblSecType = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        typePanel = new javax.swing.JPanel();
        cardTypeCourant = new javax.swing.JPanel();
        lblTypeCourant = new javax.swing.JLabel();
        lblTypeCourantDesc = new javax.swing.JLabel();
        cardTypeATerme = new javax.swing.JPanel();
        lblTypeATerme = new javax.swing.JLabel();
        lblTypeATermeDesc = new javax.swing.JLabel();
        cardTypeTontine = new javax.swing.JPanel();
        lblTypeTontine = new javax.swing.JLabel();
        lblTypeTontineDesc = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblSecInfo = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        panelLigne1 = new javax.swing.JPanel();
        panelClient = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbClient = new javax.swing.JComboBox<>();
        panelNumeroCompte = new javax.swing.JPanel();
        lblNumeroCompte = new javax.swing.JLabel();
        txtNumeroCompte = new javax.swing.JTextField();
        panelLigne2 = new javax.swing.JPanel();
        panelSolde = new javax.swing.JPanel();
        lblSolde = new javax.swing.JLabel();
        txtSolde = new javax.swing.JTextField();
        panelDateOuverture = new javax.swing.JPanel();
        lblDateOuverture = new javax.swing.JLabel();
        dateOuverture = new com.toedter.calendar.JDateChooser();
        panelTaux = new javax.swing.JPanel();
        panelTauxField = new javax.swing.JPanel();
        lblTaux = new javax.swing.JLabel();
        txtTaux = new javax.swing.JTextField();
        panelDureeField = new javax.swing.JPanel();
        lblDuree = new javax.swing.JLabel();
        txtDuree = new javax.swing.JTextField();
        panelTontine = new javax.swing.JPanel();
        panelMontantPeriodique = new javax.swing.JPanel();
        lblMontantPeriodique = new javax.swing.JLabel();
        txtMontantPeriodique = new javax.swing.JTextField();
        panelFrequence = new javax.swing.JPanel();
        lblFrequence = new javax.swing.JLabel();
        cmbFrequence = new javax.swing.JComboBox<>();
        panelDureeeTontine = new javax.swing.JPanel();
        lblDureeTontine = new javax.swing.JLabel();
        txtDureeTontine = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nouveau compte épargne");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(520, 550));
        setSize(new java.awt.Dimension(400, 700));

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 65));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerLeft.setBackground(new java.awt.Color(255, 255, 255));
        headerLeft.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerLeft.setPreferredSize(new java.awt.Dimension(400, 65));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTitle.setText("Nouveau compte épargne");
        headerLeft.add(lblTitle);

        lblSub.setForeground(new java.awt.Color(100, 116, 139));
        lblSub.setText("Créer un compte pour un client existant");
        headerLeft.add(lblSub);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.BorderLayout());

        lblRequired.setForeground(new java.awt.Color(100, 116, 139));
        lblRequired.setText("* Champs obligatoires");
        lblRequired.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        footerPanel.add(lblRequired, java.awt.BorderLayout.WEST);

        footerBtns.setBackground(new java.awt.Color(248, 250, 252));
        footerBtns.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 20));
        footerBtns.setPreferredSize(new java.awt.Dimension(240, 55));
        footerBtns.setLayout(new java.awt.GridLayout(1, 2, 8, 0));

        btnAnnuler.setText("Annuler");
        btnAnnuler.setPreferredSize(new java.awt.Dimension(100, 36));
        footerBtns.add(btnAnnuler);

        btnCreer.setBackground(new java.awt.Color(14, 165, 233));
        btnCreer.setForeground(new java.awt.Color(255, 255, 255));
        btnCreer.setText("Créer le compte");
        btnCreer.setPreferredSize(new java.awt.Dimension(120, 36));
        btnCreer.addActionListener(this::btnCreerActionPerformed);
        footerBtns.add(btnCreer);

        footerPanel.add(footerBtns, java.awt.BorderLayout.CENTER);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        scrollForm.setBorder(null);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        lblSecType.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecType.setForeground(new java.awt.Color(100, 116, 39));
        lblSecType.setText("TYPE DE COMPTE");
        lblSecType.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecType.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecType);

        jSeparator1.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator1);

        typePanel.setBackground(new java.awt.Color(255, 255, 255));
        typePanel.setAlignmentX(0.0F);
        typePanel.setMaximumSize(new java.awt.Dimension(9999, 80));
        typePanel.setPreferredSize(new java.awt.Dimension(0, 80));
        typePanel.setLayout(new java.awt.GridLayout(1, 3, 10, 0));

        cardTypeCourant.setBackground(new java.awt.Color(219, 234, 254));
        cardTypeCourant.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(14, 165, 233), 2), javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        cardTypeCourant.setLayout(new javax.swing.BoxLayout(cardTypeCourant, javax.swing.BoxLayout.Y_AXIS));

        lblTypeCourant.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTypeCourant.setForeground(new java.awt.Color(14, 165, 233));
        lblTypeCourant.setText("Courant");
        cardTypeCourant.add(lblTypeCourant);

        lblTypeCourantDesc.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTypeCourantDesc.setForeground(new java.awt.Color(71, 85, 105));
        lblTypeCourantDesc.setText("Dépôts et retraits libres");
        cardTypeCourant.add(lblTypeCourantDesc);

        typePanel.add(cardTypeCourant);

        cardTypeATerme.setBackground(new java.awt.Color(255, 255, 255));
        cardTypeATerme.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        cardTypeATerme.setLayout(new javax.swing.BoxLayout(cardTypeATerme, javax.swing.BoxLayout.Y_AXIS));

        lblTypeATerme.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTypeATerme.setForeground(new java.awt.Color(15, 23, 42));
        lblTypeATerme.setText("À terme");
        cardTypeATerme.add(lblTypeATerme);

        lblTypeATermeDesc.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTypeATermeDesc.setForeground(new java.awt.Color(100, 116, 139));
        lblTypeATermeDesc.setText("Épargne  bloquée sur durée");
        cardTypeATerme.add(lblTypeATermeDesc);

        typePanel.add(cardTypeATerme);

        cardTypeTontine.setBackground(new java.awt.Color(255, 255, 255));
        cardTypeTontine.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        cardTypeTontine.setLayout(new javax.swing.BoxLayout(cardTypeTontine, javax.swing.BoxLayout.Y_AXIS));

        lblTypeTontine.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTypeTontine.setForeground(new java.awt.Color(15, 23, 42));
        lblTypeTontine.setText("Tontine");
        cardTypeTontine.add(lblTypeTontine);

        lblTypeTontineDesc.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTypeTontineDesc.setForeground(new java.awt.Color(100, 116, 139));
        lblTypeTontineDesc.setText("Épargne collective");
        cardTypeTontine.add(lblTypeTontineDesc);

        typePanel.add(cardTypeTontine);

        formPanel.add(typePanel);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 574, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        formPanel.add(jPanel1);

        lblSecInfo.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSecInfo.setForeground(new java.awt.Color(100, 116, 139));
        lblSecInfo.setText("INFORMATIONS COMPTE");
        lblSecInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSecInfo.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSecInfo);

        jSeparator2.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator2.setAlignmentX(0.0F);
        jSeparator2.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator2);

        panelLigne1.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne1.setAlignmentX(0.0F);
        panelLigne1.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne1.setPreferredSize(new java.awt.Dimension(0, 688));
        panelLigne1.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelClient.setBackground(new java.awt.Color(255, 255, 255));
        panelClient.setLayout(new javax.swing.BoxLayout(panelClient, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setForeground(new java.awt.Color(71, 85, 105));
        jLabel1.setText("Client *");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jLabel1.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelClient.add(jLabel1);

        cmbClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbClient.setAlignmentX(0.0F);
        cmbClient.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelClient.add(cmbClient);

        panelLigne1.add(panelClient);

        panelNumeroCompte.setBackground(new java.awt.Color(255, 255, 255));
        panelNumeroCompte.setLayout(new javax.swing.BoxLayout(panelNumeroCompte, javax.swing.BoxLayout.Y_AXIS));

        lblNumeroCompte.setForeground(new java.awt.Color(71, 85, 105));
        lblNumeroCompte.setText("Numéro de compte");
        lblNumeroCompte.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblNumeroCompte.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelNumeroCompte.add(lblNumeroCompte);

        txtNumeroCompte.setBackground(new java.awt.Color(248, 250, 252));
        txtNumeroCompte.setAlignmentX(0.0F);
        txtNumeroCompte.setEnabled(false);
        txtNumeroCompte.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelNumeroCompte.add(txtNumeroCompte);

        panelLigne1.add(panelNumeroCompte);

        formPanel.add(panelLigne1);

        panelLigne2.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne2.setAlignmentX(0.0F);
        panelLigne2.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelLigne2.setPreferredSize(new java.awt.Dimension(0, 68));
        panelLigne2.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelSolde.setBackground(new java.awt.Color(255, 255, 255));
        panelSolde.setLayout(new javax.swing.BoxLayout(panelSolde, javax.swing.BoxLayout.Y_AXIS));

        lblSolde.setForeground(new java.awt.Color(71, 58, 105));
        lblSolde.setText("Solde initial (F CFA)");
        lblSolde.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblSolde.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelSolde.add(lblSolde);

        txtSolde.setAlignmentX(0.0F);
        txtSolde.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelSolde.add(txtSolde);

        panelLigne2.add(panelSolde);

        panelDateOuverture.setBackground(new java.awt.Color(255, 255, 255));
        panelDateOuverture.setLayout(new javax.swing.BoxLayout(panelDateOuverture, javax.swing.BoxLayout.Y_AXIS));

        lblDateOuverture.setForeground(new java.awt.Color(71, 58, 105));
        lblDateOuverture.setText("Date d'ouverture *");
        lblDateOuverture.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblDateOuverture.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelDateOuverture.add(lblDateOuverture);

        dateOuverture.setAlignmentX(0.0F);
        dateOuverture.setDateFormatString("dd/MM/yyyy");
        dateOuverture.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDateOuverture.add(dateOuverture);

        panelLigne2.add(panelDateOuverture);

        formPanel.add(panelLigne2);

        panelTaux.setBackground(new java.awt.Color(255, 255, 255));
        panelTaux.setAlignmentX(0.0F);
        panelTaux.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelTaux.setPreferredSize(new java.awt.Dimension(0, 68));
        panelTaux.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelTauxField.setBackground(new java.awt.Color(255, 255, 255));
        panelTauxField.setLayout(new javax.swing.BoxLayout(panelTauxField, javax.swing.BoxLayout.Y_AXIS));

        lblTaux.setForeground(new java.awt.Color(71, 85, 105));
        lblTaux.setText("Taux d'interêt(%)");
        lblTaux.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblTaux.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelTauxField.add(lblTaux);

        txtTaux.setAlignmentX(0.0F);
        txtTaux.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelTauxField.add(txtTaux);

        panelTaux.add(panelTauxField);

        panelDureeField.setBackground(new java.awt.Color(255, 255, 255));
        panelDureeField.setLayout(new javax.swing.BoxLayout(panelDureeField, javax.swing.BoxLayout.Y_AXIS));

        lblDuree.setForeground(new java.awt.Color(71, 85, 105));
        lblDuree.setText("Durée (mois)");
        lblDuree.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblDuree.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelDureeField.add(lblDuree);

        txtDuree.setAlignmentX(0.0F);
        txtDuree.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDureeField.add(txtDuree);

        panelTaux.add(panelDureeField);

        formPanel.add(panelTaux);

        panelTontine.setBackground(new java.awt.Color(255, 255, 255));
        panelTontine.setAlignmentX(0.0F);
        panelTontine.setMaximumSize(new java.awt.Dimension(9999, 68));
        panelTontine.setPreferredSize(new java.awt.Dimension(0, 68));
        panelTontine.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelMontantPeriodique.setBackground(new java.awt.Color(255, 255, 255));
        panelMontantPeriodique.setLayout(new javax.swing.BoxLayout(panelMontantPeriodique, javax.swing.BoxLayout.Y_AXIS));

        lblMontantPeriodique.setForeground(new java.awt.Color(71, 85, 105));
        lblMontantPeriodique.setText("montant périodique (F CFA)");
        lblMontantPeriodique.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblMontantPeriodique.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelMontantPeriodique.add(lblMontantPeriodique);

        txtMontantPeriodique.setAlignmentX(0.0F);
        txtMontantPeriodique.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelMontantPeriodique.add(txtMontantPeriodique);

        panelTontine.add(panelMontantPeriodique);

        panelFrequence.setBackground(new java.awt.Color(255, 255, 255));
        panelFrequence.setLayout(new javax.swing.BoxLayout(panelFrequence, javax.swing.BoxLayout.Y_AXIS));

        lblFrequence.setForeground(new java.awt.Color(71, 85, 105));
        lblFrequence.setText("Fréquence");
        lblFrequence.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblFrequence.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelFrequence.add(lblFrequence);

        cmbFrequence.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mensuelle", "Hebdomadaire", " " }));
        cmbFrequence.setAlignmentX(0.0F);
        cmbFrequence.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelFrequence.add(cmbFrequence);

        panelTontine.add(panelFrequence);

        panelDureeeTontine.setBackground(new java.awt.Color(255, 255, 255));
        panelDureeeTontine.setLayout(new javax.swing.BoxLayout(panelDureeeTontine, javax.swing.BoxLayout.Y_AXIS));

        lblDureeTontine.setForeground(new java.awt.Color(71, 85, 105));
        lblDureeTontine.setText("Durée (mois)");
        lblDureeTontine.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblDureeTontine.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelDureeeTontine.add(lblDureeTontine);

        txtDureeTontine.setAlignmentX(0.0F);
        txtDureeTontine.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelDureeeTontine.add(txtDureeTontine);

        panelTontine.add(panelDureeeTontine);

        formPanel.add(panelTontine);

        scrollForm.setViewportView(formPanel);

        getContentPane().add(scrollForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreerActionPerformed

        // 1. Client
        if (cmbClient.getSelectedIndex() == 0) {
            afficherErreur("Veuillez choisir un client !");
            return;
        }

        // 2. Date ouverture
        if (dateOuverture.getDate() == null) {
            afficherErreur("La date d'ouverture est obligatoire !");
            return;
        }
        java.util.Date aujourd_hui = new java.util.Date();
        if (dateOuverture.getDate().after(aujourd_hui)) {
            afficherErreur("La date d'ouverture ne peut pas être dans le futur !");
            return;
        }

        // 3. Solde initial
        double solde = 0;
        if (txtSolde.getText().trim().isEmpty()) {
            afficherErreur("Le solde initial est obligatoire !");
            txtSolde.requestFocus();
            return;
        }
        try {
            solde = Double.parseDouble(
                    txtSolde.getText().trim().replace(" ", "").replace(",", ""));
            if (solde < 0) {
                afficherErreur("Le solde ne peut pas être négatif !");
                txtSolde.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            afficherErreur("Le solde doit être un nombre !\nExemple : 50000");
            txtSolde.requestFocus();
            return;
        }

        // ── Règles spécifiques par type ──
        double taux = 0;
        int duree = 0;
        double montantPeriodique = 0;
        String frequence = null;

        switch (typeSelectionne) {

            case "Courant" -> {
                // Solde minimum 5000 F
                if (solde < 5000) {
                    afficherErreur(
                            "Le solde minimum pour un compte courant "
                            + "est de 5 000 F CFA !");
                    txtSolde.requestFocus();
                    return;
                }
            }

            case "À terme" -> {
                // Solde minimum 5000 F
                if (solde < 5000) {
                    afficherErreur(
                            "Le solde minimum pour un compte à terme "
                            + "est de 5 000 F CFA !");
                    txtSolde.requestFocus();
                    return;
                }
                // Taux obligatoire
                if (txtTaux.getText().trim().isEmpty()) {
                    afficherErreur("Le taux d'intérêt est obligatoire !");
                    txtTaux.requestFocus();
                    return;
                }
                try {
                    taux = Double.parseDouble(
                            txtTaux.getText().trim());
                    if (taux <= 0 || taux > 100) {
                        afficherErreur(
                                "Le taux doit être compris entre 0 et 100% !");
                        txtTaux.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    afficherErreur("Le taux doit être un nombre !\n"
                            + "Exemple : 5");
                    txtTaux.requestFocus();
                    return;
                }
                // Durée obligatoire
                if (txtDuree.getText().trim().isEmpty()) {
                    afficherErreur("La durée est obligatoire !");
                    txtDuree.requestFocus();
                    return;
                }
                try {
                    duree = Integer.parseInt(txtDuree.getText().trim());
                    if (duree < 3 || duree > 60) {
                        afficherErreur(
                                "La durée doit être entre 3 et 60 mois !");
                        txtDuree.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    afficherErreur("La durée doit être un nombre entier !\n"
                            + "Exemple : 12");
                    txtDuree.requestFocus();
                    return;
                }

                // Calculer et afficher les intérêts attendus
                double interetsAttendus = solde * taux * duree / 1200.0;
                double totalAttendu = solde + interetsAttendus;
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                        String.format(
                                "Récapitulatif du compte à terme :\n\n"
                                + "   Montant bloqué  : %,.0f F CFA\n"
                                + "   Taux            : %.0f %%\n"
                                + "   Durée           : %d mois\n"
                                + "   Intérêts        : %,.0f F CFA\n"
                                + "   Total à terme   : %,.0f F CFA\n\n"
                                + "Confirmer la création ?",
                                solde, taux, duree,
                                interetsAttendus, totalAttendu),
                        "Confirmation",
                        javax.swing.JOptionPane.YES_NO_OPTION,
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                    return;
                }
            }

            case "Tontine" -> {
                // Montant périodique — référence seulement
                if (txtMontantPeriodique.getText().trim().isEmpty()) {
                    afficherErreur("Le montant périodique est obligatoire pour une tontine !");
                    txtMontantPeriodique.requestFocus();
                    return;
                }
                try {
                    montantPeriodique = Double.parseDouble(
                            txtMontantPeriodique.getText().trim()
                                    .replace(" ", "").replace(",", ""));
                    if (montantPeriodique <= 0) {
                        afficherErreur("Le montant périodique doit être supérieur à 0 !");
                        txtMontantPeriodique.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    afficherErreur("Le montant périodique doit être un nombre !");
                    txtMontantPeriodique.requestFocus();
                    return;
                }
                // Durée obligatoire
                if (txtDureeTontine.getText().trim().isEmpty()) {
                    afficherErreur("La durée de la tontine est obligatoire !");
                    txtDureeTontine.requestFocus();
                    return;
                }
                try {
                    duree = Integer.parseInt(
                            txtDureeTontine.getText().trim());
                    if (duree <= 0) {
                        afficherErreur("La durée doit être positive !");
                        txtDureeTontine.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    afficherErreur(
                            "La durée doit être un nombre entier !");
                    txtDureeTontine.requestFocus();
                    return;
                }
                frequence = cmbFrequence.getSelectedItem().toString();

                // Solde initial optionnel pour tontine
                // (pas de minimum obligatoire)
            }
        }

        // 4. Vérifier unicité numéro compte
        // 4. Vérifier unicité numéro compte
        DAO.CompteDAO dao = new DAO.CompteDAO();
        if (dao.numeroCompteExiste(txtNumeroCompte.getText().trim())) {
            genererNumeroCompte();
        }

// 5. Créer le compte
        String selected = cmbClient.getSelectedItem().toString();
        int idClient = Integer.parseInt(selected.split(" \\| ")[0]);

// ── Vérifier doublon type de compte ──
        if (dao.clientADejaCompteDeType(idClient, typeSelectionne)) {
            afficherErreur(
                    "Ce client a déjà un compte de type « " + typeSelectionne + " » !\n"
                    + "Un client ne peut pas avoir deux comptes du même type.");
            return;
        }

        Models.Compte compte = new Models.Compte();
        compte.setIdClient(idClient);
        compte.setNumeroCompte(txtNumeroCompte.getText().trim());
        compte.setTypeCompte(typeSelectionne);
        compte.setSoldeActuel(solde);
        compte.setTauxInteret(taux);
        compte.setDateOuverture(new java.sql.Date(
                dateOuverture.getDate().getTime()));
        compte.setDuree(duree);
        compte.setMontantPeriodique(montantPeriodique);
        compte.setFrequence(frequence);

        if (dao.ajouter(compte)) {
            // Enregistrer transaction si solde initial > 0
            if (solde > 0) {
                Models.Compte compteCree = dao.chercherParNumero(
                        compte.getNumeroCompte());
                if (compteCree != null) {
                    Models.Transaction t = new Models.Transaction();
                    t.setIdCompte(compteCree.getIdCompte());
                    t.setIdUser(DAO.Session.getUtilisateur().getIdUser());
                    t.setType("Dépôt initial");
                    t.setMontant(solde);
                    t.setDateHeure(new java.sql.Timestamp(
                            System.currentTimeMillis()));
                    t.setStatut("Validé");
                    t.setMoyenPaiement("Espèces");
                    new DAO.TransactionDAO().ajouter(t);
                }
            }

            javax.swing.JOptionPane.showMessageDialog(this,
                    "✔ Compte créé avec succès !\n\n"
                    + "   Numéro    : " + compte.getNumeroCompte() + "\n"
                    + "   Type      : " + typeSelectionne + "\n"
                    + "   Solde     : "
                    + String.format("%,.0f F CFA", solde),
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            afficherErreur("Erreur lors de la création du compte !\n"
                    + "Vérifiez la connexion à la base de données.");
        }

    }//GEN-LAST:event_btnCreerActionPerformed

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
                AjouterCompte dialog = new AjouterCompte(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCreer;
    private javax.swing.JPanel cardTypeATerme;
    private javax.swing.JPanel cardTypeCourant;
    private javax.swing.JPanel cardTypeTontine;
    private javax.swing.JComboBox<String> cmbClient;
    private javax.swing.JComboBox<String> cmbFrequence;
    private com.toedter.calendar.JDateChooser dateOuverture;
    private javax.swing.JPanel footerBtns;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDateOuverture;
    private javax.swing.JLabel lblDuree;
    private javax.swing.JLabel lblDureeTontine;
    private javax.swing.JLabel lblFrequence;
    private javax.swing.JLabel lblMontantPeriodique;
    private javax.swing.JLabel lblNumeroCompte;
    private javax.swing.JLabel lblRequired;
    private javax.swing.JLabel lblSecInfo;
    private javax.swing.JLabel lblSecType;
    private javax.swing.JLabel lblSolde;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTaux;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTypeATerme;
    private javax.swing.JLabel lblTypeATermeDesc;
    private javax.swing.JLabel lblTypeCourant;
    private javax.swing.JLabel lblTypeCourantDesc;
    private javax.swing.JLabel lblTypeTontine;
    private javax.swing.JLabel lblTypeTontineDesc;
    private javax.swing.JPanel panelClient;
    private javax.swing.JPanel panelDateOuverture;
    private javax.swing.JPanel panelDureeField;
    private javax.swing.JPanel panelDureeeTontine;
    private javax.swing.JPanel panelFrequence;
    private javax.swing.JPanel panelLigne1;
    private javax.swing.JPanel panelLigne2;
    private javax.swing.JPanel panelMontantPeriodique;
    private javax.swing.JPanel panelNumeroCompte;
    private javax.swing.JPanel panelSolde;
    private javax.swing.JPanel panelTaux;
    private javax.swing.JPanel panelTauxField;
    private javax.swing.JPanel panelTontine;
    private javax.swing.JScrollPane scrollForm;
    private javax.swing.JTextField txtDuree;
    private javax.swing.JTextField txtDureeTontine;
    private javax.swing.JTextField txtMontantPeriodique;
    private javax.swing.JTextField txtNumeroCompte;
    private javax.swing.JTextField txtSolde;
    private javax.swing.JTextField txtTaux;
    private javax.swing.JPanel typePanel;
    // End of variables declaration//GEN-END:variables
}
