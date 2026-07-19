/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class AjouterClient extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AjouterClient.class.getName());

    /**
     * Creates new form AjouterClient
     */
    public AjouterClient(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        configurerStyle();
        btnAnnuler.addActionListener(e -> dispose());
    ajouterListenersFormatage();

    }

    /**
     * Listeners de formatage en temps réel
     */
    private void ajouterListenersFormatage() {
    txtNom.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent e) {
            int pos = txtNom.getCaretPosition();
            String v = txtNom.getText().toUpperCase().replaceAll("[0-9]","");
            if (!v.equals(txtNom.getText())) {
                txtNom.setText(v);
                txtNom.setCaretPosition(Math.min(pos, v.length()));
            }
        }
    });
    txtPrenom.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent e) {
            String v = txtPrenom.getText().replaceAll("[0-9]","");
            if (!v.isEmpty())
                v = Character.toUpperCase(v.charAt(0)) + v.substring(1);
            int pos = txtPrenom.getCaretPosition();
            if (!v.equals(txtPrenom.getText())) {
                txtPrenom.setText(v);
                txtPrenom.setCaretPosition(Math.min(pos, v.length()));
            }
        }
    });
    txtTelephone.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent e) {
            int kc = e.getKeyCode();
            if (kc == java.awt.event.KeyEvent.VK_BACK_SPACE
                    || kc == java.awt.event.KeyEvent.VK_DELETE) return;
            String raw = txtTelephone.getText().replaceAll("[^0-9+]","");
            String pfx = raw.startsWith("+") ? "+" : "";
            String dig = raw.startsWith("+") ? raw.substring(1) : raw;
            StringBuilder sb = new StringBuilder(pfx);
            for (int i = 0; i < dig.length(); i++) {
                if (i > 0 && i % 2 == 0) sb.append(' ');
                sb.append(dig.charAt(i));
            }
            String fmt = sb.toString();
            if (!fmt.equals(txtTelephone.getText())) {
                txtTelephone.setText(fmt);
                txtTelephone.setCaretPosition(fmt.length());
            }
        }
    });
}


    private void configurerStyle() {
        javax.swing.border.Border fieldBorder = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10));

        txtNom.setBorder(fieldBorder);
        txtPrenom.setBorder(fieldBorder);
        txtTelephone.setBorder(fieldBorder);
        txtEmail.setBorder(fieldBorder);
        txtAdresse.setBorder(fieldBorder);
        txtRevenu.setBorder(fieldBorder);
        txtNumeroPiece.setBorder(fieldBorder);
        txtProfession.setBorder(fieldBorder);

        // Combobox
        cmbSexe.setBackground(java.awt.Color.WHITE);
        cmbSituationMat.setBackground(java.awt.Color.WHITE);
        cmbTypePiece.setBackground(java.awt.Color.WHITE);

        // Supprimer items vides
        cmbSexe.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Masculin", "Féminin"}));
        cmbSituationMat.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Célibataire", "Marié(e)", "Divorcé(e)", "Veuf(ve)"}));
        cmbTypePiece.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"CNI", "Passeport", "Permis de conduire", "Carte d'électeur"}));

        // Boutons
        btnAnnuler.setBackground(new java.awt.Color(241, 245, 249));
        btnAnnuler.setForeground(new java.awt.Color(71, 85, 105));
        btnAnnuler.setBorderPainted(false);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEnregistrer.setBackground(new java.awt.Color(14, 165, 233));
        btnEnregistrer.setForeground(java.awt.Color.WHITE);
        btnEnregistrer.setBorderPainted(false);
        btnEnregistrer.setFocusPainted(false);
        btnEnregistrer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Couleurs sections
        lblSection1.setForeground(new java.awt.Color(14, 165, 233));
        lblSection2.setForeground(new java.awt.Color(14, 165, 233));
        lblSection3.setForeground(new java.awt.Color(14, 165, 233));
    }

    private boolean validerChamps() {
    if (txtNom.getText().trim().isEmpty()) {
        afficherErreur("Le nom est obligatoire !"); txtNom.requestFocus(); return false;
    }
    if (txtPrenom.getText().trim().isEmpty()) {
        afficherErreur("Le prénom est obligatoire !"); txtPrenom.requestFocus(); return false;
    }
    if (txtTelephone.getText().trim().isEmpty()) {
        afficherErreur("Le téléphone est obligatoire !"); txtTelephone.requestFocus(); return false;
    }
    if (!txtTelephone.getText().trim().matches("[0-9 +]{8,15}")) {
        afficherErreur("Téléphone invalide !\nExemple : 90 00 00 00"); txtTelephone.requestFocus(); return false;
    }
    if (txtNumeroPiece.getText().trim().isEmpty()) {
        afficherErreur("Le numéro de pièce est obligatoire !"); txtNumeroPiece.requestFocus(); return false;
    }
    if (txtAdresse.getText().trim().isEmpty()) {
        afficherErreur("L'adresse est obligatoire !"); txtAdresse.requestFocus(); return false;
    }
    if (!txtEmail.getText().trim().isEmpty()) {
        if (!txtEmail.getText().trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            afficherErreur("Email invalide !\nExemple : nom@email.com"); txtEmail.requestFocus(); return false;
        }
    }
    if (cmbTypePiece.getSelectedItem().toString().trim().isEmpty()) {
    afficherErreur("Veuillez choisir un type de pièce !");
    cmbTypePiece.requestFocus();
    return false;
}
    if (txtDateNaissance.getDate() == null) {
    afficherErreur("La date de naissance est obligatoire !");
    return false;
}
    if (txtDateNaissance.getDate() != null) {
        long age = (new java.util.Date().getTime() - txtDateNaissance.getDate().getTime())
                / (1000L * 60 * 60 * 24 * 365);
        if (age < 18) { afficherErreur("Le client doit avoir au moins 18 ans !"); return false; }
        if (age > 100) { afficherErreur("La date de naissance semble incorrecte !"); return false; }
    }
    if (txtDateNaissance.getDate() == null) {
    afficherErreur("La date de naissance est obligatoire !");
    return false;
}
    return true;
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
        lblDialogTitle = new javax.swing.JLabel();
        lblDialogSub = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        lblRequired = new javax.swing.JLabel();
        footerBtns = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();
        scrollFrame = new javax.swing.JScrollPane();
        formPanel = new javax.swing.JPanel();
        lblSection1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        panelLigne1 = new javax.swing.JPanel();
        panelNom = new javax.swing.JPanel();
        lblNom = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        panelPrenom = new javax.swing.JPanel();
        lblPrenom = new javax.swing.JLabel();
        txtPrenom = new javax.swing.JTextField();
        panelDateNaissance = new javax.swing.JPanel();
        lblDateNaissance = new javax.swing.JLabel();
        txtDateNaissance = new com.toedter.calendar.JDateChooser();
        panelLigne2 = new javax.swing.JPanel();
        panelSexe = new javax.swing.JPanel();
        lblSexe = new javax.swing.JLabel();
        cmbSexe = new javax.swing.JComboBox<>();
        panelSituationMat = new javax.swing.JPanel();
        lblSituationMat = new javax.swing.JLabel();
        cmbSituationMat = new javax.swing.JComboBox<>();
        lblSection2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        panelLigne5 = new javax.swing.JPanel();
        panelTypePiece = new javax.swing.JPanel();
        lblTypePiece = new javax.swing.JLabel();
        cmbTypePiece = new javax.swing.JComboBox<>();
        panelNumeroPiece = new javax.swing.JPanel();
        lblNumeroPiece = new javax.swing.JLabel();
        txtNumeroPiece = new javax.swing.JTextField();
        panelProfession = new javax.swing.JPanel();
        lblProfession = new javax.swing.JLabel();
        txtProfession = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblSection3 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        panelLigne6 = new javax.swing.JPanel();
        panelTelephone = new javax.swing.JPanel();
        lblTelephone = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        panelEmail = new javax.swing.JPanel();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        panelLigne7 = new javax.swing.JPanel();
        lblAdresse = new javax.swing.JLabel();
        txtAdresse = new javax.swing.JTextField();
        panelLigne8 = new javax.swing.JPanel();
        panelRevenu = new javax.swing.JPanel();
        lblRevenu = new javax.swing.JLabel();
        txtRevenu = new javax.swing.JTextField();
        panelNumeroCompte = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nouveau client");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(450, 700));
        setSize(new java.awt.Dimension(450, 700));

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 65));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerLeft.setBackground(new java.awt.Color(255, 255, 255));
        headerLeft.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerLeft.setPreferredSize(new java.awt.Dimension(400, 65));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblDialogTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDialogTitle.setForeground(new java.awt.Color(15, 23, 42));
        lblDialogTitle.setText("Nouveau client");
        headerLeft.add(lblDialogTitle);

        lblDialogSub.setForeground(new java.awt.Color(100, 116, 139));
        lblDialogSub.setText("Remplissez les informations du client");
        headerLeft.add(lblDialogSub);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.BorderLayout());

        lblRequired.setForeground(new java.awt.Color(100, 116, 139));
        lblRequired.setText("* Les champs obligatoires");
        lblRequired.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        footerPanel.add(lblRequired, java.awt.BorderLayout.WEST);

        footerBtns.setBackground(new java.awt.Color(248, 250, 252));
        footerBtns.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 20));
        footerBtns.setPreferredSize(new java.awt.Dimension(240, 55));
        footerBtns.setLayout(new java.awt.GridLayout(1, 2, 8, 0));

        btnAnnuler.setBackground(new java.awt.Color(241, 245, 249));
        btnAnnuler.setForeground(new java.awt.Color(71, 85, 105));
        btnAnnuler.setText("Annuler");
        btnAnnuler.setMaximumSize(new java.awt.Dimension(90, 20));
        btnAnnuler.setPreferredSize(new java.awt.Dimension(100, 36));
        footerBtns.add(btnAnnuler);

        btnEnregistrer.setBackground(new java.awt.Color(14, 165, 233));
        btnEnregistrer.setForeground(new java.awt.Color(255, 255, 255));
        btnEnregistrer.setText("Enregistrer");
        btnEnregistrer.setPreferredSize(new java.awt.Dimension(130, 36));
        btnEnregistrer.addActionListener(this::btnEnregistrerActionPerformed);
        footerBtns.add(btnEnregistrer);

        footerPanel.add(footerBtns, java.awt.BorderLayout.EAST);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        lblSection1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSection1.setForeground(new java.awt.Color(100, 116, 139));
        lblSection1.setText("INFORMATIONS PERSONNELLES");
        lblSection1.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSection1);

        jSeparator1.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator1);

        panelLigne1.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne1.setAlignmentX(0.0F);
        panelLigne1.setMaximumSize(new java.awt.Dimension(9999, 65));
        panelLigne1.setPreferredSize(new java.awt.Dimension(0, 65));
        panelLigne1.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelNom.setBackground(new java.awt.Color(255, 255, 255));
        panelNom.setLayout(new javax.swing.BoxLayout(panelNom, javax.swing.BoxLayout.Y_AXIS));

        lblNom.setForeground(new java.awt.Color(71, 85, 105));
        lblNom.setText("Nom *");
        lblNom.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblNom.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelNom.add(lblNom);

        txtNom.setAlignmentX(0.0F);
        txtNom.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelNom.add(txtNom);

        panelLigne1.add(panelNom);

        panelPrenom.setBackground(new java.awt.Color(255, 255, 255));
        panelPrenom.setLayout(new javax.swing.BoxLayout(panelPrenom, javax.swing.BoxLayout.Y_AXIS));

        lblPrenom.setForeground(new java.awt.Color(71, 85, 105));
        lblPrenom.setText("Prénom *");
        lblPrenom.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblPrenom.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelPrenom.add(lblPrenom);

        txtPrenom.setAlignmentX(0.0F);
        txtPrenom.setMaximumSize(new java.awt.Dimension(9999, 36));
        txtPrenom.setPreferredSize(new java.awt.Dimension(0, 36));
        panelPrenom.add(txtPrenom);

        panelLigne1.add(panelPrenom);

        panelDateNaissance.setBackground(new java.awt.Color(255, 255, 255));
        panelDateNaissance.setLayout(new javax.swing.BoxLayout(panelDateNaissance, javax.swing.BoxLayout.Y_AXIS));

        lblDateNaissance.setForeground(new java.awt.Color(71, 85, 105));
        lblDateNaissance.setText("Date de naissance");
        lblDateNaissance.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblDateNaissance.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelDateNaissance.add(lblDateNaissance);

        txtDateNaissance.setAlignmentX(0.0F);
        txtDateNaissance.setDateFormatString("dd/MM/YYYY");
        txtDateNaissance.setMaximumSize(new java.awt.Dimension(9999, 36));
        txtDateNaissance.setPreferredSize(new java.awt.Dimension(0, 36));
        panelDateNaissance.add(txtDateNaissance);

        panelLigne1.add(panelDateNaissance);

        formPanel.add(panelLigne1);

        panelLigne2.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne2.setAlignmentX(0.0F);
        panelLigne2.setMaximumSize(new java.awt.Dimension(9999, 65));
        panelLigne2.setPreferredSize(new java.awt.Dimension(0, 65));
        panelLigne2.setLayout(new java.awt.GridLayout(1, 0, 12, 0));

        panelSexe.setBackground(new java.awt.Color(255, 255, 255));
        panelSexe.setLayout(new javax.swing.BoxLayout(panelSexe, javax.swing.BoxLayout.Y_AXIS));

        lblSexe.setForeground(new java.awt.Color(71, 85, 105));
        lblSexe.setText("Sexe *");
        panelSexe.add(lblSexe);

        cmbSexe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculin", "Féminin", " ", " " }));
        cmbSexe.setAlignmentX(0.0F);
        cmbSexe.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelSexe.add(cmbSexe);

        panelLigne2.add(panelSexe);

        panelSituationMat.setBackground(new java.awt.Color(255, 255, 255));
        panelSituationMat.setLayout(new javax.swing.BoxLayout(panelSituationMat, javax.swing.BoxLayout.Y_AXIS));

        lblSituationMat.setText("Situation matrimoniale");
        panelSituationMat.add(lblSituationMat);

        cmbSituationMat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Célibataire", "Marié(e)", "Divorcé(e)", "Veuf", "Veuve", " " }));
        cmbSituationMat.setAlignmentX(0.0F);
        cmbSituationMat.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelSituationMat.add(cmbSituationMat);

        panelLigne2.add(panelSituationMat);

        formPanel.add(panelLigne2);

        lblSection2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSection2.setForeground(new java.awt.Color(100, 116, 139));
        lblSection2.setText("IDENTIFICATION");
        lblSection2.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 4, 0));
        lblSection2.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSection2);
        formPanel.add(jSeparator2);

        panelLigne5.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne5.setAlignmentX(0.0F);
        panelLigne5.setMaximumSize(new java.awt.Dimension(9999, 65));
        panelLigne5.setPreferredSize(new java.awt.Dimension(0, 65));
        panelLigne5.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelTypePiece.setBackground(new java.awt.Color(255, 255, 255));
        panelTypePiece.setLayout(new javax.swing.BoxLayout(panelTypePiece, javax.swing.BoxLayout.Y_AXIS));

        lblTypePiece.setForeground(new java.awt.Color(71, 85, 105));
        lblTypePiece.setText("Type de pièce *");
        lblTypePiece.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblTypePiece.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelTypePiece.add(lblTypePiece);

        cmbTypePiece.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CNI", "Passeport", "Permis de conduire", " " }));
        cmbTypePiece.setAlignmentX(0.0F);
        cmbTypePiece.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelTypePiece.add(cmbTypePiece);

        panelLigne5.add(panelTypePiece);

        panelNumeroPiece.setBackground(new java.awt.Color(255, 255, 255));
        panelNumeroPiece.setLayout(new javax.swing.BoxLayout(panelNumeroPiece, javax.swing.BoxLayout.Y_AXIS));

        lblNumeroPiece.setForeground(new java.awt.Color(71, 85, 105));
        lblNumeroPiece.setText("Numéro de pièce");
        lblNumeroPiece.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblNumeroPiece.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelNumeroPiece.add(lblNumeroPiece);

        txtNumeroPiece.setAlignmentX(0.0F);
        txtNumeroPiece.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelNumeroPiece.add(txtNumeroPiece);

        panelLigne5.add(panelNumeroPiece);

        panelProfession.setBackground(new java.awt.Color(255, 255, 255));
        panelProfession.setLayout(new javax.swing.BoxLayout(panelProfession, javax.swing.BoxLayout.Y_AXIS));

        lblProfession.setForeground(new java.awt.Color(105, 85, 105));
        lblProfession.setText("Profession");
        lblProfession.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblProfession.setMaximumSize(new java.awt.Dimension(99, 20));
        panelProfession.add(lblProfession);

        txtProfession.setAlignmentX(0.0F);
        txtProfession.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelProfession.add(txtProfession);

        panelLigne5.add(panelProfession);

        formPanel.add(panelLigne5);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(9999, 10));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 10));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        formPanel.add(jPanel1);

        lblSection3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSection3.setForeground(new java.awt.Color(100, 116, 39));
        lblSection3.setText("COORDONNÉES");
        lblSection3.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 4, 0));
        lblSection3.setMaximumSize(new java.awt.Dimension(9999, 25));
        formPanel.add(lblSection3);

        jSeparator3.setForeground(new java.awt.Color(226, 232, 240));
        jSeparator3.setAlignmentX(0.0F);
        jSeparator3.setMaximumSize(new java.awt.Dimension(9999, 2));
        formPanel.add(jSeparator3);

        panelLigne6.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne6.setAlignmentX(0.0F);
        panelLigne6.setMaximumSize(new java.awt.Dimension(9999, 65));
        panelLigne6.setPreferredSize(new java.awt.Dimension(0, 65));
        panelLigne6.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelTelephone.setBackground(new java.awt.Color(255, 255, 255));
        panelTelephone.setLayout(new javax.swing.BoxLayout(panelTelephone, javax.swing.BoxLayout.Y_AXIS));

        lblTelephone.setForeground(new java.awt.Color(71, 85, 105));
        lblTelephone.setText("Téléphone *");
        lblTelephone.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 4));
        lblTelephone.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelTelephone.add(lblTelephone);

        txtTelephone.setAlignmentX(0.0F);
        txtTelephone.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelTelephone.add(txtTelephone);

        panelLigne6.add(panelTelephone);

        panelEmail.setBackground(new java.awt.Color(255, 255, 255));
        panelEmail.setLayout(new javax.swing.BoxLayout(panelEmail, javax.swing.BoxLayout.Y_AXIS));

        lblEmail.setForeground(new java.awt.Color(71, 85, 105));
        lblEmail.setText("Email");
        lblEmail.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblEmail.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelEmail.add(lblEmail);

        txtEmail.setAlignmentX(0.0F);
        txtEmail.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelEmail.add(txtEmail);

        panelLigne6.add(panelEmail);

        formPanel.add(panelLigne6);

        panelLigne7.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne7.setMaximumSize(new java.awt.Dimension(9999, 65));
        panelLigne7.setPreferredSize(new java.awt.Dimension(0, 65));
        panelLigne7.setLayout(new javax.swing.BoxLayout(panelLigne7, javax.swing.BoxLayout.Y_AXIS));

        lblAdresse.setForeground(new java.awt.Color(71, 85, 105));
        lblAdresse.setText("Adresse");
        lblAdresse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblAdresse.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelLigne7.add(lblAdresse);

        txtAdresse.setAlignmentX(0.0F);
        txtAdresse.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelLigne7.add(txtAdresse);

        formPanel.add(panelLigne7);

        panelLigne8.setBackground(new java.awt.Color(255, 255, 255));
        panelLigne8.setAlignmentX(0.0F);
        panelLigne8.setMaximumSize(new java.awt.Dimension(9999, 65));
        panelLigne8.setPreferredSize(new java.awt.Dimension(0, 65));
        panelLigne8.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelRevenu.setBackground(new java.awt.Color(255, 255, 255));
        panelRevenu.setLayout(new javax.swing.BoxLayout(panelRevenu, javax.swing.BoxLayout.Y_AXIS));

        lblRevenu.setForeground(new java.awt.Color(71, 85, 105));
        lblRevenu.setText("Revenu mensuel (F CFA)");
        lblRevenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        lblRevenu.setMaximumSize(new java.awt.Dimension(9999, 20));
        panelRevenu.add(lblRevenu);

        txtRevenu.setAlignmentX(0.0F);
        txtRevenu.setMaximumSize(new java.awt.Dimension(9999, 36));
        panelRevenu.add(txtRevenu);

        panelLigne8.add(panelRevenu);

        panelNumeroCompte.setBackground(new java.awt.Color(255, 255, 255));
        panelNumeroCompte.setLayout(new javax.swing.BoxLayout(panelNumeroCompte, javax.swing.BoxLayout.Y_AXIS));
        panelLigne8.add(panelNumeroCompte);

        formPanel.add(panelLigne8);

        scrollFrame.setViewportView(formPanel);

        getContentPane().add(scrollFrame, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnregistrerActionPerformed
        // 1. Validation champs
            if (!validerChamps()) return;
    DAO.ClientDAO dao = new DAO.ClientDAO();

    // Doublon nom+prénom (+ date si renseignée)
    java.sql.Date dn = txtDateNaissance.getDate() != null
            ? new java.sql.Date(txtDateNaissance.getDate().getTime()) : null;
    if (dao.clientDoublonExiste(txtNom.getText().trim(),
            txtPrenom.getText().trim(), dn, 0)) {
        afficherErreur("Un client avec ce nom et prénom existe déjà !");
        return;
    }
    if (dao.telephoneExiste(txtTelephone.getText().trim(), 0)) {
        afficherErreur("Ce téléphone est déjà utilisé !"); txtTelephone.requestFocus(); return;
    }
    if (dao.numeroPieceExiste(txtNumeroPiece.getText().trim(), 0)) {
        afficherErreur("Ce numéro de pièce est déjà enregistré !"); txtNumeroPiece.requestFocus(); return;
    }

    Models.Client client = new Models.Client();
    client.setNom(txtNom.getText().trim().toUpperCase());
    String prn = txtPrenom.getText().trim();
    client.setPrenom(prn.isEmpty() ? prn :
            Character.toUpperCase(prn.charAt(0)) + prn.substring(1).toLowerCase());
    client.setSexe(cmbSexe.getSelectedItem().toString());
    client.setSituationMat(cmbSituationMat.getSelectedItem().toString());
    client.setTelephone(txtTelephone.getText().trim());
    client.setEmail(txtEmail.getText().trim());
    client.setAdresse(txtAdresse.getText().trim());
    client.setNumCarteIdentite(txtNumeroPiece.getText().trim());
    client.setTypePiece(cmbTypePiece.getSelectedItem().toString());
    client.setProfession(txtProfession.getText().trim());
    if (dn != null) client.setDateNaissance(dn);
    if (!txtRevenu.getText().trim().isEmpty())
        client.setRevenuMensuel(Double.parseDouble(txtRevenu.getText().trim().replace(" ","")));

    if (dao.ajouter(client)) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "✔ Client enregistré avec succès !\n\n"
                + "   Nom    : " + client.getNom() + " " + client.getPrenom() + "\n"
                + "   Tél    : " + client.getTelephone() + "\n"
                + "   Statut : Actif",
                "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        dispose();
    } else {
        afficherErreur("Erreur lors de l'enregistrement !\nVérifiez la connexion.");
    }

    }

    private void afficherErreur(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message,
                "Erreur de saisie", javax.swing.JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btnEnregistrerActionPerformed

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
                AjouterClient dialog = new AjouterClient(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cmbSexe;
    private javax.swing.JComboBox<String> cmbSituationMat;
    private javax.swing.JComboBox<String> cmbTypePiece;
    private javax.swing.JPanel footerBtns;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblAdresse;
    private javax.swing.JLabel lblDateNaissance;
    private javax.swing.JLabel lblDialogSub;
    private javax.swing.JLabel lblDialogTitle;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNom;
    private javax.swing.JLabel lblNumeroPiece;
    private javax.swing.JLabel lblPrenom;
    private javax.swing.JLabel lblProfession;
    private javax.swing.JLabel lblRequired;
    private javax.swing.JLabel lblRevenu;
    private javax.swing.JLabel lblSection1;
    private javax.swing.JLabel lblSection2;
    private javax.swing.JLabel lblSection3;
    private javax.swing.JLabel lblSexe;
    private javax.swing.JLabel lblSituationMat;
    private javax.swing.JLabel lblTelephone;
    private javax.swing.JLabel lblTypePiece;
    private javax.swing.JPanel panelDateNaissance;
    private javax.swing.JPanel panelEmail;
    private javax.swing.JPanel panelLigne1;
    private javax.swing.JPanel panelLigne2;
    private javax.swing.JPanel panelLigne5;
    private javax.swing.JPanel panelLigne6;
    private javax.swing.JPanel panelLigne7;
    private javax.swing.JPanel panelLigne8;
    private javax.swing.JPanel panelNom;
    private javax.swing.JPanel panelNumeroCompte;
    private javax.swing.JPanel panelNumeroPiece;
    private javax.swing.JPanel panelPrenom;
    private javax.swing.JPanel panelProfession;
    private javax.swing.JPanel panelRevenu;
    private javax.swing.JPanel panelSexe;
    private javax.swing.JPanel panelSituationMat;
    private javax.swing.JPanel panelTelephone;
    private javax.swing.JPanel panelTypePiece;
    private javax.swing.JScrollPane scrollFrame;
    private javax.swing.JTextField txtAdresse;
    private com.toedter.calendar.JDateChooser txtDateNaissance;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtNumeroPiece;
    private javax.swing.JTextField txtPrenom;
    private javax.swing.JTextField txtProfession;
    private javax.swing.JTextField txtRevenu;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables
}
