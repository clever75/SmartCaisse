/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class TransactionsPanel extends javax.swing.JPanel {

    /**
     * Creates new form TransactionsPanel
     */
    public TransactionsPanel() {
        initComponents();
        configurerTableau();
        configurerStyle();
        configurerRenderers();
        configurerListeners();
        chargerTransactions();
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                chargerTransactions();
            }
        });
        // ── Boutons états transactions ──
javax.swing.JButton btnEtatTrans = new javax.swing.JButton("📄 État");
btnEtatTrans.setBackground(new java.awt.Color(245, 240, 232));
btnEtatTrans.setForeground(new java.awt.Color(15, 23, 42));
btnEtatTrans.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnEtatTrans.setBorder(javax.swing.BorderFactory.createLineBorder(
        new java.awt.Color(14, 165, 233)));
btnEtatTrans.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnEtatTrans.setFocusPainted(false);
btnEtatTrans.setPreferredSize(new java.awt.Dimension(90, 36));
btnEtatTrans.addActionListener(e -> utils.EtatsHelperSmartCaisse.etatTransactions());

javax.swing.JButton btnExcelTrans = new javax.swing.JButton("📊 Excel");
btnExcelTrans.setBackground(new java.awt.Color(33, 115, 70));
btnExcelTrans.setForeground(java.awt.Color.WHITE);
btnExcelTrans.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnExcelTrans.setBorderPainted(false);
btnExcelTrans.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnExcelTrans.setFocusPainted(false);
btnExcelTrans.setPreferredSize(new java.awt.Dimension(90, 36));
btnExcelTrans.addActionListener(e -> utils.EtatsHelperSmartCaisse.exportExcelTransactions());

headerRight.add(btnEtatTrans);
headerRight.add(btnExcelTrans);
headerRight.revalidate();
    }

    private void configurerTableau() {
        tableTransactions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"N°", "Client", "Compte", "Type",
                    "Montant", "Moyen", "Date", "Statut"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
        tableTransactions.setRowHeight(44);
        tableTransactions.getTableHeader().setReorderingAllowed(false);
        tableTransactions.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableTransactions.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableTransactions.getColumnModel().getColumn(2).setPreferredWidth(110);
        tableTransactions.getColumnModel().getColumn(3).setPreferredWidth(140);
        tableTransactions.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableTransactions.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableTransactions.getColumnModel().getColumn(6).setPreferredWidth(120);
        tableTransactions.getColumnModel().getColumn(7).setPreferredWidth(80);
    }

    private void configurerStyle() {
        tableTransactions.getTableHeader().setBackground(
                new java.awt.Color(241, 245, 249));
        tableTransactions.getTableHeader().setForeground(
                new java.awt.Color(71, 85, 105));
        tableTransactions.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tableTransactions.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 40));
        tableTransactions.setBackground(java.awt.Color.WHITE);
        tableTransactions.setSelectionBackground(
                new java.awt.Color(239, 246, 255));
        tableTransactions.setSelectionForeground(
                new java.awt.Color(15, 23, 42));
        tableTransactions.setGridColor(new java.awt.Color(241, 245, 249));
        tableTransactions.setShowVerticalLines(false);
        tableTransactions.setShowHorizontalLines(true);
        tableTransactions.setIntercellSpacing(new java.awt.Dimension(0, 0));

        scrollTrans.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(0, 16, 0, 16));

        txtSearch.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtSearch.setBackground(java.awt.Color.WHITE);

        // Corriger les types dans cmbType
        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Tous les types",
            "Dépôt épargne",
            "Dépôt initial",
            "Retrait épargne",
            "Remboursement",
            "Remboursement anticipé",
            "Décaissement",
            "Intérêts épargne"
        }));
        cmbType.setBackground(java.awt.Color.WHITE);
        cmbPeriode.setBackground(java.awt.Color.WHITE);

        tableTransactions.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int col) {
                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    setBackground(row % 2 == 0
                            ? java.awt.Color.WHITE
                            : new java.awt.Color(248, 250, 252));
                }
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return this;
            }
        });
    }

    private void configurerListeners() {
        cmbType.addActionListener(e -> chargerTransactions());
        cmbPeriode.addActionListener(e -> chargerTransactions());
        btnSearch.addActionListener(e -> chargerTransactions());
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                chargerTransactions();
            }
        });
    }

    private void chargerTransactions() {
        DAO.TransactionDAO dao = new DAO.TransactionDAO();
        java.util.List<Models.Transaction> toutes = dao.listerTous();

        // ── Stats globales — calculées sur TOUTES les transactions ──
        int nbTotal = 0;
        double totalDepots = 0, totalRetraits = 0,
                totalDecaisse = 0, totalRemb = 0;

        for (Models.Transaction t : toutes) {
            nbTotal++;
            switch (t.getType()) {
                case "Dépôt épargne", "Dépôt initial", "Intérêts épargne" ->
                    totalDepots += t.getMontant();
                case "Retrait épargne" ->
                    totalRetraits += t.getMontant();
                case "Décaissement" ->
                    totalDecaisse += t.getMontant();
                case "Remboursement", "Remboursement anticipé" ->
                    totalRemb += t.getMontant();
            }
        }

        lblTotalVal.setText(String.valueOf(nbTotal));
        lblTotalSub.setText("Toutes périodes");
        lblDepotsVal.setText(String.format("%,.0f F CFA", totalDepots));
        lblRetraitsVal.setText(String.format("%,.0f F CFA", totalRetraits));
        lblDecaisseVal.setText(String.format("%,.0f F CFA", totalDecaisse));

        // ── Filtres ──
        String typeFiltre = cmbType.getSelectedItem().toString();
        String periodeFiltre = cmbPeriode.getSelectedItem().toString();
        String recherche = txtSearch.getText().trim().toLowerCase();

        java.time.LocalDate dateLimite = null;
        switch (periodeFiltre) {
            case "Aujourd'hui" ->
                dateLimite = java.time.LocalDate.now();
            case "Cette semaine" ->
                dateLimite = java.time.LocalDate.now().minusDays(7);
            case "Ce mois" ->
                dateLimite = java.time.LocalDate.now().withDayOfMonth(1);
        }

        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) tableTransactions.getModel();
        model.setRowCount(0);

        // ── DAO créés UNE SEULE FOIS avant la boucle ──
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        DAO.ClientDAO clientDao = new DAO.ClientDAO();

        for (Models.Transaction t : toutes) {

            // Filtre type
            if (!"Tous les types".equals(typeFiltre)
                    && !t.getType().equals(typeFiltre)) {
                continue;
            }

            // Filtre période
            if (dateLimite != null && t.getDateHeure() != null) {
                java.time.LocalDate dateTrans = t.getDateHeure()
                        .toLocalDateTime().toLocalDate();
                if (dateTrans.isBefore(dateLimite)) {
                    continue;
                }
            }

            // Infos compte + client
            Models.Compte compte = compteDao.chercher(t.getIdCompte());
            String nomClient = "—";
            String numCompte = "—";
            if (compte != null) {
                numCompte = compte.getNumeroCompte();
                Models.Client client = clientDao.chercher(compte.getIdClient());
                if (client != null) {
                    nomClient = client.getNom() + " " + client.getPrenom();
                }
            }

            // Filtre recherche
            if (!recherche.isEmpty()) {
                if (!nomClient.toLowerCase().contains(recherche)
                        && !numCompte.toLowerCase().contains(recherche)
                        && !t.getType().toLowerCase().contains(recherche)) {
                    continue;
                }
            }

            // Formater date
            String dateStr = t.getDateHeure() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(t.getDateHeure()) : "—";

            // Signe montant selon type
            String montantStr;
            switch (t.getType()) {
                case "Dépôt épargne", "Dépôt initial", "Intérêts épargne", "Remboursement", "Remboursement anticipé" ->
                    montantStr = "+ " + String.format(
                            "%,.0f F CFA", t.getMontant());
                case "Retrait épargne", "Décaissement" ->
                    montantStr = "- " + String.format(
                            "%,.0f F CFA", t.getMontant());
                default ->
                    montantStr = String.format(
                            "%,.0f F CFA", t.getMontant());
            }

            // Moyen de paiement
            String moyen = t.getMoyenPaiement() != null
                    && !t.getMoyenPaiement().isEmpty()
                    ? t.getMoyenPaiement() : "—";

            model.addRow(new Object[]{
                "#" + String.format("%03d", t.getIdTransaction()),
                nomClient,
                numCompte,
                t.getType(),
                montantStr,
                moyen,
                dateStr,
                t.getStatut()
            });
        }
    }

    private void configurerRenderers() {
        ((javax.swing.table.DefaultTableCellRenderer) tableTransactions.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.table.DefaultTableCellRenderer centreur
                = new javax.swing.table.DefaultTableCellRenderer();
        centreur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableTransactions.getColumnModel().getColumn(0).setCellRenderer(centreur);
        tableTransactions.getColumnModel().getColumn(1).setCellRenderer(centreur);
        tableTransactions.getColumnModel().getColumn(2).setCellRenderer(centreur);
        tableTransactions.getColumnModel().getColumn(5).setCellRenderer(centreur);
        tableTransactions.getColumnModel().getColumn(6).setCellRenderer(centreur);

        // Badge Type — colonne 3
        tableTransactions.getColumnModel().getColumn(3).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    String text = value != null ? value.toString() : "";
                    java.awt.Color bg, fg;
                    switch (text) {
                        case "Dépôt épargne", "Dépôt initial", "Intérêts épargne" -> {
                            bg = new java.awt.Color(220, 252, 231);
                            fg = new java.awt.Color(21, 128, 61);
                        }
                        case "Retrait épargne" -> {
                            bg = new java.awt.Color(254, 226, 226);
                            fg = new java.awt.Color(220, 38, 38);
                        }
                        case "Remboursement", "Remboursement anticipé" -> {
                            bg = new java.awt.Color(254, 243, 199);
                            fg = new java.awt.Color(146, 64, 14);
                        }
                        case "Décaissement" -> {
                            bg = new java.awt.Color(219, 234, 254);
                            fg = new java.awt.Color(14, 165, 233);
                        }
                        default -> {
                            bg = new java.awt.Color(241, 245, 249);
                            fg = new java.awt.Color(100, 116, 139);
                        }
                    }
                    javax.swing.JLabel badge = new javax.swing.JLabel(text) {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    java.awt.Graphics2D g2
                            = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(
                            java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(),
                            getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
                    badge.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.PLAIN, 11));
                    badge.setOpaque(false);
                    badge.setBackground(bg);
                    badge.setForeground(fg);
                    badge.setBorder(javax.swing.BorderFactory
                            .createEmptyBorder(3, 10, 3, 10));
                    cell.add(badge);
                    return cell;
                });

        // Montant coloré — colonne 4
        tableTransactions.getColumnModel().getColumn(4).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    String text = value != null ? value.toString() : "";
                    java.awt.Color fg, bg;
                    if (text.startsWith("+")) {
                        fg = new java.awt.Color(21, 128, 61);
                        bg = new java.awt.Color(220, 252, 231);
                    } else if (text.startsWith("-")) {
                        fg = new java.awt.Color(220, 38, 38);
                        bg = new java.awt.Color(254, 226, 226);
                    } else {
                        fg = new java.awt.Color(146, 64, 14);
                        bg = new java.awt.Color(254, 243, 199);
                    }
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    javax.swing.JLabel lbl = new javax.swing.JLabel(text) {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    java.awt.Graphics2D g2
                            = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(
                            java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(),
                            getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
                    lbl.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.BOLD, 12));
                    lbl.setOpaque(false);
                    lbl.setBackground(bg);
                    lbl.setForeground(fg);
                    lbl.setBorder(javax.swing.BorderFactory
                            .createEmptyBorder(3, 10, 3, 10));
                    cell.add(lbl);
                    return cell;
                });

        // Badge Statut — colonne 7
        tableTransactions.getColumnModel().getColumn(7).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    String text = value != null ? value.toString() : "";
                    java.awt.Color bg, fg;
                    switch (text) {
                        case "Validé" -> {
                            bg = new java.awt.Color(220, 252, 231);
                            fg = new java.awt.Color(21, 128, 61);
                        }
                        case "En cours" -> {
                            bg = new java.awt.Color(219, 234, 254);
                            fg = new java.awt.Color(14, 165, 233);
                        }
                        case "Annulé" -> {
                            bg = new java.awt.Color(254, 226, 226);
                            fg = new java.awt.Color(220, 38, 38);
                        }
                        default -> {
                            bg = new java.awt.Color(241, 245, 249);
                            fg = new java.awt.Color(100, 116, 139);
                        }
                    }
                    javax.swing.JLabel badge = new javax.swing.JLabel(text) {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    java.awt.Graphics2D g2
                            = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(
                            java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(),
                            getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
                    badge.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.PLAIN, 11));
                    badge.setOpaque(false);
                    badge.setBackground(bg);
                    badge.setForeground(fg);
                    badge.setBorder(javax.swing.BorderFactory
                            .createEmptyBorder(3, 10, 3, 10));
                    cell.add(badge);
                    return cell;
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerLeft = new javax.swing.JPanel();
        lblTitre = new javax.swing.JLabel();
        headerRight = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        cardTotal = new javax.swing.JPanel();
        lblTotalTitre = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        lblTotalSub = new javax.swing.JLabel();
        cardDepots = new javax.swing.JPanel();
        lblDepotsTitle = new javax.swing.JLabel();
        lblDepotsVal = new javax.swing.JLabel();
        lblDepotsSub = new javax.swing.JLabel();
        cardRetraits = new javax.swing.JPanel();
        lblRetraitsTitle = new javax.swing.JLabel();
        lblRetraitsVal = new javax.swing.JLabel();
        lblRetraitsSub = new javax.swing.JLabel();
        cardTotal3 = new javax.swing.JPanel();
        lblRDecaisseTitle = new javax.swing.JLabel();
        lblDecaisseVal = new javax.swing.JLabel();
        lblDecaisseSub = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cmbType = new javax.swing.JComboBox<>();
        cmbPeriode = new javax.swing.JComboBox<>();
        scrollTrans = new javax.swing.JScrollPane();
        tableTransactions = new javax.swing.JTable();

        setBackground(new java.awt.Color(247, 247, 242));
        setLayout(new java.awt.BorderLayout());

        topPanel.setBackground(new java.awt.Color(247, 247, 250));
        topPanel.setMaximumSize(new java.awt.Dimension(9999, 180));
        topPanel.setPreferredSize(new java.awt.Dimension(0, 180));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBackground(new java.awt.Color(248, 250, 252));
        headerPanel.setAlignmentX(0.0F);
        headerPanel.setMaximumSize(new java.awt.Dimension(9999, 60));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerLeft.setBackground(new java.awt.Color(248, 250, 252));
        headerLeft.setMaximumSize(new java.awt.Dimension(400, 30));
        headerLeft.setPreferredSize(new java.awt.Dimension(400, 60));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblTitre.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitre.setForeground(new java.awt.Color(15, 23, 42));
        lblTitre.setText("Historique des transactions");
        lblTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        lblTitre.setMaximumSize(new java.awt.Dimension(400, 30));
        lblTitre.setPreferredSize(new java.awt.Dimension(400, 30));
        headerLeft.add(lblTitre);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        headerRight.setBackground(new java.awt.Color(248, 250, 252));
        headerRight.setPreferredSize(new java.awt.Dimension(180, 60));
        headerRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 5));
        headerPanel.add(headerRight, java.awt.BorderLayout.EAST);

        topPanel.add(headerPanel);

        statsPanel.setBackground(new java.awt.Color(248, 250, 252));
        statsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 16, 8));
        statsPanel.setAlignmentX(0.0F);
        statsPanel.setMaximumSize(new java.awt.Dimension(9999, 80));
        statsPanel.setPreferredSize(new java.awt.Dimension(0, 100));
        statsPanel.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        cardTotal.setBackground(new java.awt.Color(255, 255, 255));
        cardTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(59, 130, 246))));
        cardTotal.setLayout(new javax.swing.BoxLayout(cardTotal, javax.swing.BoxLayout.Y_AXIS));

        lblTotalTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblTotalTitre.setText("Total transactions");
        lblTotalTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 4, 0));
        cardTotal.add(lblTotalTitre);

        lblTotalVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalVal.setForeground(new java.awt.Color(15, 23, 42));
        lblTotalVal.setText("0");
        lblTotalVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTotal.add(lblTotalVal);

        lblTotalSub.setForeground(new java.awt.Color(100, 116, 139));
        lblTotalSub.setText("Toutes périodes");
        lblTotalSub.setToolTipText("");
        lblTotalSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTotal.add(lblTotalSub);

        statsPanel.add(cardTotal);

        cardDepots.setBackground(new java.awt.Color(255, 255, 255));
        cardDepots.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(34, 197, 94))));
        cardDepots.setLayout(new javax.swing.BoxLayout(cardDepots, javax.swing.BoxLayout.Y_AXIS));

        lblDepotsTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblDepotsTitle.setText("Total dépôts");
        lblDepotsTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 4, 0));
        cardDepots.add(lblDepotsTitle);

        lblDepotsVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDepotsVal.setForeground(new java.awt.Color(21, 128, 61));
        lblDepotsVal.setText("0 F CFA");
        lblDepotsVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardDepots.add(lblDepotsVal);

        lblDepotsSub.setForeground(new java.awt.Color(100, 116, 139));
        lblDepotsSub.setText("Entrées d'argent");
        lblDepotsSub.setToolTipText("");
        lblDepotsSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardDepots.add(lblDepotsSub);

        statsPanel.add(cardDepots);

        cardRetraits.setBackground(new java.awt.Color(255, 255, 255));
        cardRetraits.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(220, 38, 38))));
        cardRetraits.setLayout(new javax.swing.BoxLayout(cardRetraits, javax.swing.BoxLayout.Y_AXIS));

        lblRetraitsTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblRetraitsTitle.setText("Total retraits");
        lblRetraitsTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 4, 0));
        cardRetraits.add(lblRetraitsTitle);

        lblRetraitsVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblRetraitsVal.setForeground(new java.awt.Color(200, 38, 38));
        lblRetraitsVal.setText("0 F CFA");
        lblRetraitsVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardRetraits.add(lblRetraitsVal);

        lblRetraitsSub.setForeground(new java.awt.Color(100, 116, 139));
        lblRetraitsSub.setText("Sorties d'argent");
        lblRetraitsSub.setToolTipText("");
        lblRetraitsSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardRetraits.add(lblRetraitsSub);

        statsPanel.add(cardRetraits);

        cardTotal3.setBackground(new java.awt.Color(255, 255, 255));
        cardTotal3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(245, 158, 11))));
        cardTotal3.setLayout(new javax.swing.BoxLayout(cardTotal3, javax.swing.BoxLayout.Y_AXIS));

        lblRDecaisseTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblRDecaisseTitle.setText("Total décaissé");
        lblRDecaisseTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 4, 0));
        cardTotal3.add(lblRDecaisseTitle);

        lblDecaisseVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDecaisseVal.setForeground(new java.awt.Color(146, 64, 14));
        lblDecaisseVal.setText(" 0 F CFA");
        lblDecaisseVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTotal3.add(lblDecaisseVal);

        lblDecaisseSub.setForeground(new java.awt.Color(100, 116, 139));
        lblDecaisseSub.setText("Prêts accordés");
        lblDecaisseSub.setToolTipText("");
        lblDecaisseSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTotal3.add(lblDecaisseSub);

        statsPanel.add(cardTotal3);

        topPanel.add(statsPanel);

        searchPanel.setBackground(new java.awt.Color(248, 250, 252));
        searchPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 9, 0, 0));
        searchPanel.setAlignmentX(0.0F);
        searchPanel.setMaximumSize(new java.awt.Dimension(9999, 40));
        searchPanel.setPreferredSize(new java.awt.Dimension(0, 50));
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 3));

        txtSearch.setToolTipText("Text");
        txtSearch.setPreferredSize(new java.awt.Dimension(280, 34));
        searchPanel.add(txtSearch);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        btnSearch.setText("Rechercher");
        btnSearch.setPreferredSize(new java.awt.Dimension(120, 34));
        searchPanel.add(btnSearch);

        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous les types", "Dépôt épargne", "Retrait épargne", "Remboursement", "Décaissement" }));
        cmbType.setPreferredSize(new java.awt.Dimension(150, 34));
        searchPanel.add(cmbType);

        cmbPeriode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toutes les dates", "Aujourd'hui", "Cette semaine", "Ce mois" }));
        cmbPeriode.setPreferredSize(new java.awt.Dimension(130, 34));
        searchPanel.add(cmbPeriode);

        topPanel.add(searchPanel);

        add(topPanel, java.awt.BorderLayout.NORTH);

        scrollTrans.setBackground(new java.awt.Color(247, 247, 242));
        scrollTrans.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16));

        tableTransactions.setBackground(new java.awt.Color(252, 252, 253));
        tableTransactions.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tableTransactions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableTransactions.setGridColor(new java.awt.Color(214, 245, 249));
        tableTransactions.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tableTransactions.setRowHeight(44);
        tableTransactions.setSelectionBackground(new java.awt.Color(219, 234, 254));
        tableTransactions.setSelectionForeground(new java.awt.Color(14, 165, 233));
        tableTransactions.setShowGrid(true);
        scrollTrans.setViewportView(tableTransactions);

        add(scrollTrans, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel cardDepots;
    private javax.swing.JPanel cardRetraits;
    private javax.swing.JPanel cardTotal;
    private javax.swing.JPanel cardTotal3;
    private javax.swing.JComboBox<String> cmbPeriode;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerRight;
    private javax.swing.JLabel lblDecaisseSub;
    private javax.swing.JLabel lblDecaisseVal;
    private javax.swing.JLabel lblDepotsSub;
    private javax.swing.JLabel lblDepotsTitle;
    private javax.swing.JLabel lblDepotsVal;
    private javax.swing.JLabel lblRDecaisseTitle;
    private javax.swing.JLabel lblRetraitsSub;
    private javax.swing.JLabel lblRetraitsTitle;
    private javax.swing.JLabel lblRetraitsVal;
    private javax.swing.JLabel lblTitre;
    private javax.swing.JLabel lblTotalSub;
    private javax.swing.JLabel lblTotalTitre;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JScrollPane scrollTrans;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTable tableTransactions;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
