/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

import javax.swing.JButton;

/**
 *
 * @author Admin
 */
public class ClientsPanel extends javax.swing.JPanel {

    /**
     * Creates new form Clientspanel
     */
    public ClientsPanel() {
        initComponents();
        configurerTableau();
        configurerStyle();
        configurerColonneActions();
        configurerRenderers();
        configurerListeners();
        chargerClients();
        addComponentListener(new java.awt.event.ComponentAdapter() {
    @Override
    public void componentShown(java.awt.event.ComponentEvent e) {
        chargerClients();
        
    }
    
});
        // ── Boutons états clients ──
javax.swing.JButton btnEtatClients = new javax.swing.JButton("📄 État");
btnEtatClients.setBackground(new java.awt.Color(245, 240, 232));
btnEtatClients.setForeground(new java.awt.Color(15, 23, 42));
btnEtatClients.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnEtatClients.setBorder(javax.swing.BorderFactory.createLineBorder(
        new java.awt.Color(14, 165, 233)));
btnEtatClients.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnEtatClients.setFocusPainted(false);
btnEtatClients.setPreferredSize(new java.awt.Dimension(90, 36));
btnEtatClients.addActionListener(e -> utils.EtatsHelperSmartCaisse.etatClients());

javax.swing.JButton btnExcelClients = new javax.swing.JButton("📊 Excel");
btnExcelClients.setBackground(new java.awt.Color(33, 115, 70));
btnExcelClients.setForeground(java.awt.Color.WHITE);
btnExcelClients.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnExcelClients.setBorderPainted(false);
btnExcelClients.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnExcelClients.setFocusPainted(false);
btnExcelClients.setPreferredSize(new java.awt.Dimension(90, 36));
btnExcelClients.addActionListener(e -> utils.EtatsHelperSmartCaisse.exportExcelClients());

headerRight.add(btnEtatClients, 0);
headerRight.add(btnExcelClients, 1);
headerRight.revalidate();
    }

    // Appeler dans le constructeur après chargerClients()
    private void configurerColonneActions() {

        // Renderer
tableClients.getColumn("Actions").setCellRenderer(
        (table, value, isSelected, hasFocus, row, col) -> {
            javax.swing.JPanel p = new javax.swing.JPanel(
                    new java.awt.FlowLayout(
                            java.awt.FlowLayout.CENTER, 4, 4));
            p.setBackground(isSelected
                    ? new java.awt.Color(239, 246, 255)
                    : java.awt.Color.WHITE);

            // Récupérer le statut de la ligne
            String statut = table.getValueAt(row, 5) != null
                    ? table.getValueAt(row, 5).toString() : "Actif";

            p.add(creerBoutonAction("/icons/eye.png",
                    new java.awt.Color(219, 234, 254)));
            p.add(creerBoutonAction("/icons/edit.png",
                    new java.awt.Color(220, 252, 231)));

            // Bouton Désactiver ou Réactiver selon statut
            if ("Actif".equals(statut)) {
                p.add(creerBoutonAction("/icons/delete.png",
                        new java.awt.Color(254, 226, 226)));
            } else {
                p.add(creerBoutonAction("/icons/success.png",
                        new java.awt.Color(220, 252, 231)));
            }

            return p;
        });

        // Editor
        // --- TROUVEZ CE BLOC DANS VOTRE MÉTHODE configurerColonneActions() ---
// Editor
        tableClients.getColumn("Actions").setCellEditor(
                new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()) {

            private javax.swing.JPanel panel;
            private int currentRow;
            private javax.swing.JTable tableRef;

            {
                panel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 4, 4));
                panel.setBackground(new java.awt.Color(239, 246, 255));

                // On recrée les boutons pour l'éditeur (interactifs)
                javax.swing.JButton bVoir = creerBoutonAction("/icons/eye.png", new java.awt.Color(219, 234, 254));
                javax.swing.JButton bModif = creerBoutonAction("/icons/edit.png", new java.awt.Color(220, 252, 231));
                javax.swing.JButton bSuppr = creerBoutonAction("/icons/delete.png", new java.awt.Color(254, 226, 226));

                // Action Voir
                bVoir.addActionListener(e -> {
                    fireEditingStopped(); // Arrête l'édition pour libérer la cellule
                    int rowModel = tableRef.convertRowIndexToModel(currentRow); // Sécurité si trié
                    int idClient = Integer.parseInt(tableRef.getModel().getValueAt(rowModel, 0).toString().replace("#", "").trim());

                    DetailClient dialog = new DetailClient((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(ClientsPanel.this), true);
                    dialog.chargerClient(idClient);
                    dialog.setLocationRelativeTo(ClientsPanel.this);
                    dialog.setVisible(true);
                });

                // Action Modifier
                bModif.addActionListener(e -> {
                    fireEditingStopped();
                    int rowModel = tableRef.convertRowIndexToModel(currentRow);
                    int idClient = Integer.parseInt(tableRef.getModel().getValueAt(rowModel, 0).toString().replace("#", "").trim());

                    ModifierClient dialog = new ModifierClient((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(ClientsPanel.this), true, idClient);
                    dialog.setLocationRelativeTo(ClientsPanel.this);
                    dialog.setVisible(true);
                    chargerClients();
                });

                // Action Supprimer
             bSuppr.addActionListener(e -> {
    fireEditingStopped();
    int rowModel = tableRef.convertRowIndexToModel(currentRow);
    int idClient = Integer.parseInt(tableRef.getModel()
            .getValueAt(rowModel, 0).toString()
            .replace("#", "").trim());
    String statut = tableRef.getModel()
            .getValueAt(rowModel, 5).toString();

if ("Actif".equals(statut)) {
    int nbPrets = new DAO.ClientDAO().compterPretsActifs(idClient);
    if (nbPrets > 0) {
        javax.swing.JOptionPane.showMessageDialog(ClientsPanel.this,
                "Impossible ! Ce client a " + nbPrets + " prêt(s) en cours.\n"
                + "Soldez tous les prêts d'abord.",
                "Suppression impossible",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    boolean aOps = new DAO.ClientDAO().aDesOperations(idClient);
    String msg = aOps
            ? "Ce client a des opérations enregistrées.\nIl sera désactivé (pas supprimé définitivement).\nSes comptes actifs seront clôturés.\n\nConfirmer ?"
            : "Ce client n'a aucune opération.\nIl sera supprimé définitivement.\n\nConfirmer la suppression ?";
    int confirm = javax.swing.JOptionPane.showConfirmDialog(
            ClientsPanel.this, msg,
            aOps ? "Désactiver le client" : "Supprimer le client",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE);
    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
        if (new DAO.ClientDAO().supprimer(idClient)) {
            javax.swing.JOptionPane.showMessageDialog(ClientsPanel.this,
                    aOps ? "Client désactivé. Comptes clôturés."
                         : "Client supprimé définitivement.",
                    "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            chargerClients();
        }
    }
} else {
        // Réactiver
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                ClientsPanel.this,
                "Voulez-vous réactiver ce client ?",
                "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            if (new DAO.ClientDAO().reactiver(idClient)) {
                javax.swing.JOptionPane.showMessageDialog(
                        ClientsPanel.this,
                        "Client réactivé avec succès !",
                        "Succès",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                chargerClients();
            }
        }
    }
});

                panel.add(bVoir);
                panel.add(bModif);
                panel.add(bSuppr);
            }

            @Override
            public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int col) {
                this.currentRow = row;
                this.tableRef = table;
                return panel;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });

// --- LA SUITE DU CODE RESTE INCHANGÉE ---
        tableClients.getColumn("Actions").setPreferredWidth(120);
        tableClients.setRowHeight(44);
    }

    private void configurerStyle() {
        // Tableau
        tableClients.getTableHeader().setBackground(new java.awt.Color(241, 245, 249));
        tableClients.getTableHeader().setForeground(new java.awt.Color(71, 85, 105));
        tableClients.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tableClients.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));
        tableClients.setBackground(java.awt.Color.WHITE);
        tableClients.setSelectionBackground(new java.awt.Color(239, 246, 255));
        tableClients.setSelectionForeground(new java.awt.Color(15, 23, 42));
        tableClients.setGridColor(new java.awt.Color(241, 245, 249));
        tableClients.setShowVerticalLines(false);
        tableClients.setShowHorizontalLines(true);
        tableClients.setIntercellSpacing(new java.awt.Dimension(0, 0));

        // Panels
        searchPanel.setBackground(new java.awt.Color(248, 250, 252));
        searchPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        statsPanel.setBackground(new java.awt.Color(248, 250, 252));
        statsPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        headerPanel.setBackground(java.awt.Color.WHITE);
        headerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        // Champs
        txtSearch.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtSearch.setBackground(java.awt.Color.WHITE);
        cmbStatut.setBackground(java.awt.Color.WHITE);
        cmbStatut.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        // Bouton
        btnNewClient.setBackground(new java.awt.Color(14, 165, 233));
        btnNewClient.setForeground(java.awt.Color.WHITE);
        btnNewClient.setBorderPainted(false);
        btnNewClient.setFocusPainted(false);
        btnNewClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewClient.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        // Lignes alternées
        tableClients.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(248, 250, 252));
                }
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    private void configurerTableau() {
        tableClients.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"N°", "Nom complet", "Téléphone",
                    "Profession", "Date inscription", "Statut", "Actions"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        });
        tableClients.setRowHeight(44);
        tableClients.getTableHeader().setReorderingAllowed(false);
        tableClients.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableClients.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableClients.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableClients.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableClients.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableClients.getColumnModel().getColumn(5).setPreferredWidth(90);
        tableClients.getColumnModel().getColumn(6).setPreferredWidth(120);
    }

    private void configurerListeners() {
    btnNewClient.addActionListener(e -> {
        AjouterClient dialog = new AjouterClient(
                (java.awt.Frame) javax.swing.SwingUtilities
                        .getWindowAncestor(this), true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        chargerClients();
    });

    btnSearch.addActionListener(e -> chargerClients());

    txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            chargerClients();
        }
    });

    cmbStatut.addActionListener(e -> chargerClients());
}

    private void configurerRenderers() {
        // Centrer les en-têtes
        ((javax.swing.table.DefaultTableCellRenderer) tableClients.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Renderer par défaut — texte centré pour toutes les colonnes
        javax.swing.table.DefaultTableCellRenderer centreur
                = new javax.swing.table.DefaultTableCellRenderer();
        centreur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Appliquer centrage sur toutes les colonnes sauf Actions
        for (int i = 0; i < tableClients.getColumnCount() - 1; i++) {
            tableClients.getColumnModel().getColumn(i).setCellRenderer(centreur);
        }

        // Badge statut — colonne 5 (centré + coloré)
        tableClients.getColumnModel().getColumn(5).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255) : java.awt.Color.WHITE);

                    javax.swing.JLabel badge = new javax.swing.JLabel(
                            value != null ? value.toString() : "");
                    badge.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
                    badge.setOpaque(true);
                    badge.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 10, 3, 10));

                    switch (value != null ? value.toString() : "") {
                        case "Actif" -> {
                            badge.setBackground(new java.awt.Color(220, 252, 231));
                            badge.setForeground(new java.awt.Color(21, 128, 61));
                        }
                        case "Inactif" -> {
                            badge.setBackground(new java.awt.Color(254, 226, 226));
                            badge.setForeground(new java.awt.Color(220, 38, 38));
                        }
                        default -> {
                            badge.setBackground(new java.awt.Color(241, 245, 249));
                            badge.setForeground(new java.awt.Color(100, 116, 139));
                        }
                    }
                    cell.add(badge);
                    return cell;
                });
    }

// Méthode utilitaire pour créer un bouton action
    private javax.swing.JButton creerBoutonAction(String iconPath, java.awt.Color bg) {
        javax.swing.JButton btn = new javax.swing.JButton();
        try {
            btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            btn.setText("?");
        }
        btn.setBackground(bg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new java.awt.Dimension(34, 30));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    // Charger les clients depuis la base
private void chargerClients() {
    DAO.ClientDAO dao = new DAO.ClientDAO();
    java.util.List<Models.Client> clients = dao.listerTous();

    // Stats calculées dans la même boucle
    int total = 0, actifs = 0, inactifs = 0, nouveaux = 0;
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int moisCourant = cal.get(java.util.Calendar.MONTH);
    int annéeCourante = cal.get(java.util.Calendar.YEAR);

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) tableClients.getModel();
    model.setRowCount(0);

    String statutFiltre = cmbStatut.getSelectedItem().toString();
    String recherche = txtSearch.getText().trim().toLowerCase();

    for (Models.Client c : clients) {
        total++;
        if ("Actif".equals(c.getStatut())) actifs++;
        else inactifs++;

        // Nouveaux ce mois
        if (c.getDateInscription() != null) {
            java.util.Calendar calClient = java.util.Calendar.getInstance();
            calClient.setTime(c.getDateInscription());
            if (calClient.get(java.util.Calendar.MONTH) == moisCourant
                    && calClient.get(java.util.Calendar.YEAR) == annéeCourante) {
                nouveaux++;
            }
        }

        // Filtre statut
        if (!"Tous".equals(statutFiltre)
                && !statutFiltre.equals(c.getStatut())) continue;

        // Filtre recherche
        if (!recherche.isEmpty()) {
            String nomComplet = (c.getNom() + " " + c.getPrenom()).toLowerCase();
            String profession = c.getProfession() != null
                    ? c.getProfession().toLowerCase() : "";
            if (!nomComplet.contains(recherche)
                    && !c.getTelephone().contains(recherche)
                    && !profession.contains(recherche)) continue;
        }

        model.addRow(new Object[]{
            "#" + String.format("%03d", c.getIdClient()),
            c.getNom() + " " + c.getPrenom(),
            c.getTelephone(),
            c.getProfession() != null && !c.getProfession().isEmpty()
                    ? c.getProfession() : "—",
            c.getDateInscription() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy")
                            .format(c.getDateInscription()) : "—",
            c.getStatut(),
            ""
        });
    }

    // Mettre à jour les stats
    lblStatTotal.setText("Total : " + total);
    lblStatActifs.setText("Actifs : " + actifs);
    lblStatInactifs.setText("Inactifs : " + inactifs);
    lblStatNouveaux.setText("Nouveaux : " + nouveaux);
}

    private void chargerStats() {
        DAO.ClientDAO dao = new DAO.ClientDAO();
        java.util.List<Models.Client> clients = dao.listerTous();
        int total = clients.size();
        long actifs = clients.stream()
                .filter(c -> "Actif".equals(c.getStatut())).count();
        long inactifs = total - actifs;
        lblStatTotal.setText("Total : " + total);
        lblStatActifs.setText("Actifs : " + actifs);
        lblStatInactifs.setText("Inactifs : " + inactifs);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollClients = new javax.swing.JScrollPane();
        tableClients = new javax.swing.JTable();
        topPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerRight = new javax.swing.JPanel();
        btnNewClient = new javax.swing.JButton();
        headerLeft = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cmbStatut = new javax.swing.JComboBox<>();
        statsPanel = new javax.swing.JPanel();
        lblStatTotal = new javax.swing.JLabel();
        lblStatActifs = new javax.swing.JLabel();
        lblStatInactifs = new javax.swing.JLabel();
        lblStatNouveaux = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        scrollClients.setBackground(new java.awt.Color(252, 252, 253));
        scrollClients.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 0, 16));

        tableClients.setBackground(new java.awt.Color(252, 252, 253));
        tableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N° Client", "Nom", "Prénom", "Téléphone", "Date inscription", "Statut", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableClients.setGridColor(new java.awt.Color(214, 245, 249));
        tableClients.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tableClients.setRowHeight(44);
        tableClients.setSelectionBackground(new java.awt.Color(219, 234, 254));
        tableClients.setSelectionForeground(new java.awt.Color(14, 165, 233));
        tableClients.setShowGrid(true);
        scrollClients.setViewportView(tableClients);

        add(scrollClients, java.awt.BorderLayout.CENTER);

        topPanel.setBackground(new java.awt.Color(247, 247, 250));
        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 9, 0, 0));
        topPanel.setMaximumSize(new java.awt.Dimension(9999, 190));
        topPanel.setPreferredSize(new java.awt.Dimension(0, 190));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBackground(new java.awt.Color(248, 250, 252));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerRight.setBackground(new java.awt.Color(248, 250, 252));
        headerRight.setPreferredSize(new java.awt.Dimension(165, 64));

        btnNewClient.setBackground(new java.awt.Color(14, 165, 233));
        btnNewClient.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnNewClient.setForeground(new java.awt.Color(255, 255, 255));
        btnNewClient.setText(" + Nouveau client");
        btnNewClient.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNewClient.setIconTextGap(8);
        btnNewClient.setPreferredSize(new java.awt.Dimension(160, 36));
        btnNewClient.addActionListener(this::btnNewClientActionPerformed);
        headerRight.add(btnNewClient);

        headerPanel.add(headerRight, java.awt.BorderLayout.CENTER);

        headerLeft.setBackground(new java.awt.Color(248, 250, 252));
        headerLeft.setPreferredSize(new java.awt.Dimension(300, 70));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(15, 23, 42));
        lblTitle.setText("Gestion des clients");
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        headerLeft.add(lblTitle);

        jLabel1.setForeground(new java.awt.Color(100, 116, 139));
        jLabel1.setText("Liste de tous les clients enregistrés");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        headerLeft.add(jLabel1);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        topPanel.add(headerPanel);

        searchPanel.setBackground(new java.awt.Color(248, 250, 252));
        searchPanel.setMaximumSize(new java.awt.Dimension(9999, 45));
        searchPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        txtSearch.setToolTipText("");
        txtSearch.setPreferredSize(new java.awt.Dimension(300, 34));
        searchPanel.add(txtSearch);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        btnSearch.setText("Rechercher");
        btnSearch.setToolTipText("Rechercher un client");
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSearch.setIconTextGap(8);
        searchPanel.add(btnSearch);

        cmbStatut.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous", "Actif", "Inactif" }));
        cmbStatut.setPreferredSize(new java.awt.Dimension(200, 30));
        searchPanel.add(cmbStatut);

        topPanel.add(searchPanel);

        statsPanel.setBackground(new java.awt.Color(248, 250, 252));
        statsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));
        statsPanel.setMaximumSize(new java.awt.Dimension(9999, 44));
        statsPanel.setPreferredSize(new java.awt.Dimension(0, 44));
        statsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 8));

        lblStatTotal.setBackground(new java.awt.Color(255, 255, 255));
        lblStatTotal.setForeground(new java.awt.Color(71, 85, 105));
        lblStatTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user2.png"))); // NOI18N
        lblStatTotal.setText("Total : 0");
        lblStatTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        lblStatTotal.setOpaque(true);
        statsPanel.add(lblStatTotal);

        lblStatActifs.setBackground(new java.awt.Color(240, 253, 244));
        lblStatActifs.setForeground(new java.awt.Color(21, 128, 61));
        lblStatActifs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/success.png"))); // NOI18N
        lblStatActifs.setText("Actifs : 0");
        lblStatActifs.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        lblStatActifs.setOpaque(true);
        statsPanel.add(lblStatActifs);

        lblStatInactifs.setBackground(new java.awt.Color(254, 226, 226));
        lblStatInactifs.setForeground(new java.awt.Color(220, 38, 38));
        lblStatInactifs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/circle.png"))); // NOI18N
        lblStatInactifs.setText("Inactifs : 0");
        lblStatInactifs.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        lblStatInactifs.setOpaque(true);
        statsPanel.add(lblStatInactifs);

        lblStatNouveaux.setBackground(new java.awt.Color(219, 234, 254));
        lblStatNouveaux.setForeground(new java.awt.Color(14, 165, 233));
        lblStatNouveaux.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/circle.png"))); // NOI18N
        lblStatNouveaux.setText("Inactifs : 0");
        lblStatNouveaux.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        lblStatNouveaux.setOpaque(true);
        statsPanel.add(lblStatNouveaux);

        topPanel.add(statsPanel);

        add(topPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewClientActionPerformed
        // TODO add your handling code here:
       

    }//GEN-LAST:event_btnNewClientActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewClient;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbStatut;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerRight;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblStatActifs;
    private javax.swing.JLabel lblStatInactifs;
    private javax.swing.JLabel lblStatNouveaux;
    private javax.swing.JLabel lblStatTotal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollClients;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTable tableClients;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
