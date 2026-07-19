/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class DetailClient extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DetailClient.class.getName());

    private int idClientCourant;
    private Runnable onModified;

    public void setOnModified(Runnable callback) {
        onModified = callback;
    }

    public DetailClient(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(520,620);
        setLocationRelativeTo(parent);
        configurerStyle();

        btnModifier.addActionListener(e -> {
            dispose();
            ModifierClient dialog = new ModifierClient(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(this),
                    true, idClientCourant);
            dialog.setVisible(true);
            if (onModified != null) {
                onModified.run();
            }
        });

        btnFermer.addActionListener(e -> dispose());
    }

    private void configurerStyle() {
        // Header
        headerPanel.setBackground(new java.awt.Color(219, 234, 254));
        headerCenter.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(16, 24, 16, 24));

        // Boutons footer
        btnFermer.setBackground(new java.awt.Color(241, 245, 249));
        btnFermer.setForeground(new java.awt.Color(71, 85, 105));
        btnFermer.setBorderPainted(false);
        btnFermer.setFocusPainted(false);
        btnFermer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFermer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        btnModifier.setBackground(new java.awt.Color(220, 252, 231));
        btnModifier.setForeground(new java.awt.Color(21, 128, 61));
        btnModifier.setBorderPainted(false);
        btnModifier.setFocusPainted(false);
        btnModifier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModifier.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        // Sections en bleu
        lblSec1.setForeground(new java.awt.Color(14, 165, 233));
        lblSec2.setForeground(new java.awt.Color(14, 165, 233));
        lblSec3.setForeground(new java.awt.Color(14, 165, 233));

        // Labels titres section 2 — ils n'ont pas de labels dans ton Design
        // On les ajoute dynamiquement via les panels
        configurerLabelsSection2();
        configurerLabelsSection3();
    }

    private void configurerLabelsSection2() {
        // Type de pièce
        javax.swing.JLabel lblTypePiece = new javax.swing.JLabel("Type de pièce");
        lblTypePiece.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblTypePiece.setForeground(new java.awt.Color(100, 116, 139));
        valTypePiece.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valTypePiece.setForeground(new java.awt.Color(15, 23, 42));
        panelTypePiece.add(lblTypePiece, 0);

        // Numéro de pièce
        javax.swing.JLabel lblNumeroPiece = new javax.swing.JLabel("Numéro de pièce");
        lblNumeroPiece.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblNumeroPiece.setForeground(new java.awt.Color(100, 116, 139));
        valNumeroPiece.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valNumeroPiece.setForeground(new java.awt.Color(15, 23, 42));
        panelNumeroPiece.add(lblNumeroPiece, 0);

        // Profession
        javax.swing.JLabel lblProfession = new javax.swing.JLabel("Profession");
        lblProfession.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblProfession.setForeground(new java.awt.Color(100, 116, 139));
        valProfession.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valProfession.setForeground(new java.awt.Color(15, 23, 42));
        panelProfession.add(lblProfession, 0);
    }

    private void configurerLabelsSection3() {
        // Email
        javax.swing.JLabel lblEmail = new javax.swing.JLabel("Email");
        lblEmail.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblEmail.setForeground(new java.awt.Color(100, 116, 139));
        valEmail.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valEmail.setForeground(new java.awt.Color(15, 23, 42));
        panelEmail.add(lblEmail, 0);

        // Adresse
        javax.swing.JLabel lblAdresse = new javax.swing.JLabel("Adresse");
        lblAdresse.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblAdresse.setForeground(new java.awt.Color(100, 116, 139));
        valAdresse.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valAdresse.setForeground(new java.awt.Color(15, 23, 42));
        panelAdresse.add(lblAdresse, 0);

        // Revenu
        javax.swing.JLabel lblRevenu = new javax.swing.JLabel("Revenu mensuel");
        lblRevenu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblRevenu.setForeground(new java.awt.Color(100, 116, 139));
        valRevenu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valRevenu.setForeground(new java.awt.Color(21, 128, 61));
        panelRevenu.add(lblRevenu, 0);

        // Date inscription
        javax.swing.JLabel lblDateInscription = new javax.swing.JLabel("Date d'inscription");
        lblDateInscription.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblDateInscription.setForeground(new java.awt.Color(100, 116, 139));
        valDateInscription.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        valDateInscription.setForeground(new java.awt.Color(15, 23, 42));
        panelDateInscription.add(lblDateInscription, 0);
    }

    public void chargerClient(int idClient) {
        idClientCourant = idClient;

        DAO.ClientDAO dao = new DAO.ClientDAO();
        Models.Client c = dao.chercher(idClient);
        if (c == null) {
            return;
        }

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Header
        lblNomComplet.setText(c.getNom() + " " + c.getPrenom());
        lblStatutClient.setText(c.getStatut());
        lblStatutClient.setForeground("Actif".equals(c.getStatut())
                ? new java.awt.Color(21, 128, 61)
                : new java.awt.Color(220, 38, 38));

        // Section 1 — Infos personnelles
        valNom.setText(c.getNom());
        valPrenom.setText(c.getPrenom());
        valDateNaissance.setText(c.getDateNaissance() != null
                ? sdf.format(c.getDateNaissance()) : "—");
        valSexe.setText(c.getSexe() != null ? c.getSexe() : "—");
        valSituationMat.setText(c.getSituationMat() != null
                ? c.getSituationMat() : "—");
        valTelephone.setText(c.getTelephone() != null
                ? c.getTelephone() : "—");

        // Section 2 — Identification
        valTypePiece.setText(c.getTypePiece() != null
                ? c.getTypePiece() : "—");
        valNumeroPiece.setText(c.getNumCarteIdentite() != null
                && !c.getNumCarteIdentite().isEmpty()
                ? c.getNumCarteIdentite() : "—");
        valProfession.setText(c.getProfession() != null
                && !c.getProfession().isEmpty()
                ? c.getProfession() : "—");

        // Section 3 — Coordonnées
        valEmail.setText(c.getEmail() != null
                && !c.getEmail().isEmpty()
                ? c.getEmail() : "—");
        valAdresse.setText(c.getAdresse() != null
                && !c.getAdresse().isEmpty()
                ? c.getAdresse() : "—");
        valRevenu.setText(c.getRevenuMensuel() > 0
                ? String.format("%,.0f F CFA", c.getRevenuMensuel()) : "—");
        valDateInscription.setText(c.getDateInscription() != null
                ? sdf.format(c.getDateInscription()) : "—");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        headerCenter = new javax.swing.JPanel();
        lblNomComplet = new javax.swing.JLabel();
        lblStatutClient = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        btnFermer = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        scrollInfo = new javax.swing.JScrollPane();
        infoPanel = new javax.swing.JPanel();
        lblSec1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        gridInfo1 = new javax.swing.JPanel();
        panelNom = new javax.swing.JPanel();
        lblNom = new javax.swing.JLabel();
        valNom = new javax.swing.JLabel();
        panelPrenom = new javax.swing.JPanel();
        lblPrenom = new javax.swing.JLabel();
        valPrenom = new javax.swing.JLabel();
        panelDateNaissance = new javax.swing.JPanel();
        lblDateNaissance = new javax.swing.JLabel();
        valDateNaissance = new javax.swing.JLabel();
        panelSexe = new javax.swing.JPanel();
        lblSexe = new javax.swing.JLabel();
        valSexe = new javax.swing.JLabel();
        panelSituationMat = new javax.swing.JPanel();
        lblSituationMat = new javax.swing.JLabel();
        valSituationMat = new javax.swing.JLabel();
        panelTelephone = new javax.swing.JPanel();
        lblTel = new javax.swing.JLabel();
        valTelephone = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblSec2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        gridInfo2 = new javax.swing.JPanel();
        panelTypePiece = new javax.swing.JPanel();
        valTypePiece = new javax.swing.JLabel();
        panelNumeroPiece = new javax.swing.JPanel();
        valNumeroPiece = new javax.swing.JLabel();
        panelProfession = new javax.swing.JPanel();
        valProfession = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblSec3 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        gridInfo3 = new javax.swing.JPanel();
        panelEmail = new javax.swing.JPanel();
        valEmail = new javax.swing.JLabel();
        panelAdresse = new javax.swing.JPanel();
        valAdresse = new javax.swing.JLabel();
        panelRevenu = new javax.swing.JPanel();
        valRevenu = new javax.swing.JLabel();
        panelDateInscription = new javax.swing.JPanel();
        valDateInscription = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Détail client");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(400, 700));
        setResizable(false);
        setSize(new java.awt.Dimension(400, 700));

        headerPanel.setBackground(new java.awt.Color(219, 234, 254));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 80));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerCenter.setBackground(new java.awt.Color(219, 234, 254));
        headerCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 24, 16, 24));
        headerCenter.setLayout(new javax.swing.BoxLayout(headerCenter, javax.swing.BoxLayout.Y_AXIS));

        lblNomComplet.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblNomComplet.setForeground(new java.awt.Color(15, 23, 42));
        lblNomComplet.setText("Nom Prénom");
        headerCenter.add(lblNomComplet);

        lblStatutClient.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblStatutClient.setForeground(new java.awt.Color(21, 128, 61));
        lblStatutClient.setText("Actif");
        headerCenter.add(lblStatutClient);

        headerPanel.add(headerCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnFermer.setText("Fermer");
        btnFermer.setPreferredSize(new java.awt.Dimension(100, 36));
        btnFermer.addActionListener(this::btnFermerActionPerformed);
        footerPanel.add(btnFermer);

        btnModifier.setBackground(new java.awt.Color(220, 252, 231));
        btnModifier.setForeground(new java.awt.Color(21, 128, 61));
        btnModifier.setText("Modifier");
        btnModifier.setPreferredSize(new java.awt.Dimension(120, 36));
        footerPanel.add(btnModifier);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));

        lblSec1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSec1.setForeground(new java.awt.Color(14, 165, 233));
        lblSec1.setText("INFORMATIONS PERSONNELLES");
        lblSec1.setMaximumSize(new java.awt.Dimension(9999, 25));
        infoPanel.add(lblSec1);

        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator1);

        gridInfo1.setBackground(new java.awt.Color(255, 255, 255));
        gridInfo1.setAlignmentX(0.0F);
        gridInfo1.setMaximumSize(new java.awt.Dimension(9999, 110));
        gridInfo1.setPreferredSize(new java.awt.Dimension(0, 110));
        gridInfo1.setLayout(new java.awt.GridLayout(2, 3, 12, 12));

        panelNom.setBackground(new java.awt.Color(255, 255, 255));
        panelNom.setLayout(new javax.swing.BoxLayout(panelNom, javax.swing.BoxLayout.Y_AXIS));

        lblNom.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblNom.setForeground(new java.awt.Color(100, 116, 139));
        lblNom.setText("Nom");
        panelNom.add(lblNom);

        valNom.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valNom.setForeground(new java.awt.Color(15, 23, 42));
        valNom.setText("jLabel4");
        panelNom.add(valNom);

        gridInfo1.add(panelNom);

        panelPrenom.setBackground(new java.awt.Color(255, 255, 255));
        panelPrenom.setLayout(new javax.swing.BoxLayout(panelPrenom, javax.swing.BoxLayout.Y_AXIS));

        lblPrenom.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblPrenom.setForeground(new java.awt.Color(100, 116, 139));
        lblPrenom.setText("Prénom");
        panelPrenom.add(lblPrenom);

        valPrenom.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valPrenom.setForeground(new java.awt.Color(15, 23, 42));
        panelPrenom.add(valPrenom);

        gridInfo1.add(panelPrenom);

        panelDateNaissance.setBackground(new java.awt.Color(255, 255, 255));
        panelDateNaissance.setLayout(new javax.swing.BoxLayout(panelDateNaissance, javax.swing.BoxLayout.Y_AXIS));

        lblDateNaissance.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDateNaissance.setForeground(new java.awt.Color(100, 116, 139));
        lblDateNaissance.setText("Date de naissance");
        panelDateNaissance.add(lblDateNaissance);

        valDateNaissance.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valDateNaissance.setForeground(new java.awt.Color(15, 23, 42));
        valDateNaissance.setText("jLabel4");
        panelDateNaissance.add(valDateNaissance);

        gridInfo1.add(panelDateNaissance);

        panelSexe.setBackground(new java.awt.Color(255, 255, 255));
        panelSexe.setLayout(new javax.swing.BoxLayout(panelSexe, javax.swing.BoxLayout.Y_AXIS));

        lblSexe.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblSexe.setForeground(new java.awt.Color(100, 116, 139));
        lblSexe.setText("Sexe");
        panelSexe.add(lblSexe);

        valSexe.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valSexe.setForeground(new java.awt.Color(15, 23, 42));
        valSexe.setText("jLabel4");
        panelSexe.add(valSexe);

        gridInfo1.add(panelSexe);

        panelSituationMat.setBackground(new java.awt.Color(255, 255, 255));
        panelSituationMat.setLayout(new javax.swing.BoxLayout(panelSituationMat, javax.swing.BoxLayout.Y_AXIS));

        lblSituationMat.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblSituationMat.setForeground(new java.awt.Color(100, 116, 139));
        lblSituationMat.setText("Situation matrimoniale");
        panelSituationMat.add(lblSituationMat);

        valSituationMat.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valSituationMat.setForeground(new java.awt.Color(15, 23, 42));
        valSituationMat.setText("jLabel4");
        panelSituationMat.add(valSituationMat);

        gridInfo1.add(panelSituationMat);

        panelTelephone.setBackground(new java.awt.Color(255, 255, 255));
        panelTelephone.setLayout(new javax.swing.BoxLayout(panelTelephone, javax.swing.BoxLayout.Y_AXIS));

        lblTel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTel.setForeground(new java.awt.Color(100, 116, 139));
        lblTel.setText("Téléphone");
        panelTelephone.add(lblTel);

        valTelephone.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        valTelephone.setForeground(new java.awt.Color(15, 23, 42));
        valTelephone.setText("jLabel4");
        panelTelephone.add(valTelephone);

        gridInfo1.add(panelTelephone);

        infoPanel.add(gridInfo1);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 16));
        infoPanel.add(jPanel1);

        lblSec2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSec2.setForeground(new java.awt.Color(14, 165, 233));
        lblSec2.setText(" IDENTIFICATION");
        lblSec2.setMaximumSize(new java.awt.Dimension(9999, 25));
        infoPanel.add(lblSec2);

        jSeparator2.setAlignmentX(0.0F);
        jSeparator2.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator2);

        gridInfo2.setBackground(new java.awt.Color(255, 255, 255));
        gridInfo2.setAlignmentX(0.0F);
        gridInfo2.setMaximumSize(new java.awt.Dimension(9999, 55));
        gridInfo2.setPreferredSize(new java.awt.Dimension(0, 55));
        gridInfo2.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        panelTypePiece.setBackground(new java.awt.Color(255, 255, 255));
        panelTypePiece.setLayout(new javax.swing.BoxLayout(panelTypePiece, javax.swing.BoxLayout.Y_AXIS));

        valTypePiece.setText("jLabel1");
        panelTypePiece.add(valTypePiece);

        gridInfo2.add(panelTypePiece);

        panelNumeroPiece.setBackground(new java.awt.Color(255, 255, 255));
        panelNumeroPiece.setLayout(new javax.swing.BoxLayout(panelNumeroPiece, javax.swing.BoxLayout.Y_AXIS));

        valNumeroPiece.setText("jLabel2");
        panelNumeroPiece.add(valNumeroPiece);

        gridInfo2.add(panelNumeroPiece);

        panelProfession.setBackground(new java.awt.Color(255, 255, 255));
        panelProfession.setLayout(new javax.swing.BoxLayout(panelProfession, javax.swing.BoxLayout.Y_AXIS));

        valProfession.setText("jLabel3");
        panelProfession.add(valProfession);

        gridInfo2.add(panelProfession);

        infoPanel.add(gridInfo2);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setMaximumSize(new java.awt.Dimension(9999, 16));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 16));
        infoPanel.add(jPanel2);

        lblSec3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSec3.setForeground(new java.awt.Color(14, 165, 233));
        lblSec3.setText("COORDONNÉES");
        lblSec3.setMaximumSize(new java.awt.Dimension(9999, 25));
        infoPanel.add(lblSec3);

        jSeparator3.setAlignmentX(0.0F);
        jSeparator3.setMaximumSize(new java.awt.Dimension(9999, 2));
        infoPanel.add(jSeparator3);

        gridInfo3.setBackground(new java.awt.Color(255, 255, 255));
        gridInfo3.setAlignmentX(0.0F);
        gridInfo3.setMaximumSize(new java.awt.Dimension(9999, 120));
        gridInfo3.setPreferredSize(new java.awt.Dimension(0, 120));
        gridInfo3.setLayout(new java.awt.GridLayout(2, 2, 12, 12));

        panelEmail.setBackground(new java.awt.Color(255, 255, 255));
        panelEmail.setLayout(new javax.swing.BoxLayout(panelEmail, javax.swing.BoxLayout.Y_AXIS));

        valEmail.setText("jLabel1");
        panelEmail.add(valEmail);

        gridInfo3.add(panelEmail);

        panelAdresse.setBackground(new java.awt.Color(255, 255, 255));
        panelAdresse.setLayout(new javax.swing.BoxLayout(panelAdresse, javax.swing.BoxLayout.Y_AXIS));

        valAdresse.setText("jLabel2");
        panelAdresse.add(valAdresse);

        gridInfo3.add(panelAdresse);

        panelRevenu.setBackground(new java.awt.Color(255, 255, 255));
        panelRevenu.setLayout(new javax.swing.BoxLayout(panelRevenu, javax.swing.BoxLayout.Y_AXIS));

        valRevenu.setText("jLabel3");
        panelRevenu.add(valRevenu);

        gridInfo3.add(panelRevenu);

        panelDateInscription.setBackground(new java.awt.Color(255, 255, 255));
        panelDateInscription.setLayout(new javax.swing.BoxLayout(panelDateInscription, javax.swing.BoxLayout.Y_AXIS));

        valDateInscription.setText("jLabel4");
        panelDateInscription.add(valDateInscription);

        gridInfo3.add(panelDateInscription);

        infoPanel.add(gridInfo3);

        scrollInfo.setViewportView(infoPanel);

        getContentPane().add(scrollInfo, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFermerActionPerformed

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
                DetailClient dialog = new DetailClient(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnFermer;
    private javax.swing.JButton btnModifier;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel gridInfo1;
    private javax.swing.JPanel gridInfo2;
    private javax.swing.JPanel gridInfo3;
    private javax.swing.JPanel headerCenter;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblDateNaissance;
    private javax.swing.JLabel lblNom;
    private javax.swing.JLabel lblNomComplet;
    private javax.swing.JLabel lblPrenom;
    private javax.swing.JLabel lblSec1;
    private javax.swing.JLabel lblSec2;
    private javax.swing.JLabel lblSec3;
    private javax.swing.JLabel lblSexe;
    private javax.swing.JLabel lblSituationMat;
    private javax.swing.JLabel lblStatutClient;
    private javax.swing.JLabel lblTel;
    private javax.swing.JPanel panelAdresse;
    private javax.swing.JPanel panelDateInscription;
    private javax.swing.JPanel panelDateNaissance;
    private javax.swing.JPanel panelEmail;
    private javax.swing.JPanel panelNom;
    private javax.swing.JPanel panelNumeroPiece;
    private javax.swing.JPanel panelPrenom;
    private javax.swing.JPanel panelProfession;
    private javax.swing.JPanel panelRevenu;
    private javax.swing.JPanel panelSexe;
    private javax.swing.JPanel panelSituationMat;
    private javax.swing.JPanel panelTelephone;
    private javax.swing.JPanel panelTypePiece;
    private javax.swing.JScrollPane scrollInfo;
    private javax.swing.JLabel valAdresse;
    private javax.swing.JLabel valDateInscription;
    private javax.swing.JLabel valDateNaissance;
    private javax.swing.JLabel valEmail;
    private javax.swing.JLabel valNom;
    private javax.swing.JLabel valNumeroPiece;
    private javax.swing.JLabel valPrenom;
    private javax.swing.JLabel valProfession;
    private javax.swing.JLabel valRevenu;
    private javax.swing.JLabel valSexe;
    private javax.swing.JLabel valSituationMat;
    private javax.swing.JLabel valTelephone;
    private javax.swing.JLabel valTypePiece;
    // End of variables declaration//GEN-END:variables
}
