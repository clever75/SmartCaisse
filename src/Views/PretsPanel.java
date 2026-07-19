/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

import DAO.PretDAO;

/**
 *
 * @author Admin
 */
public class PretsPanel extends javax.swing.JPanel {

    /**
     * Creates new form PretsPanel
     */
    public PretsPanel() {
        initComponents();
        configurerTableau();
        configurerStyle();
        configurerColonneActions();
        configurerRenderers();
        configurerListeners();
        chargerPrets();
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                chargerPrets();
            }
        });
        // ── Boutons états prêts ──
javax.swing.JButton btnEtatPrets = new javax.swing.JButton("📄 En cours");
btnEtatPrets.setBackground(new java.awt.Color(245, 240, 232));
btnEtatPrets.setForeground(new java.awt.Color(15, 23, 42));
btnEtatPrets.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnEtatPrets.setBorder(javax.swing.BorderFactory.createLineBorder(
        new java.awt.Color(14, 165, 233)));
btnEtatPrets.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnEtatPrets.setFocusPainted(false);
btnEtatPrets.setPreferredSize(new java.awt.Dimension(110, 36));
btnEtatPrets.addActionListener(e -> utils.EtatsHelperSmartCaisse.etatPretsEnCours());

javax.swing.JButton btnEtatRetards = new javax.swing.JButton("⚠ Retards");
btnEtatRetards.setBackground(new java.awt.Color(255, 235, 235));
btnEtatRetards.setForeground(new java.awt.Color(180, 30, 30));
btnEtatRetards.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
btnEtatRetards.setBorder(javax.swing.BorderFactory.createLineBorder(
        new java.awt.Color(220, 38, 38)));
btnEtatRetards.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnEtatRetards.setFocusPainted(false);
btnEtatRetards.setPreferredSize(new java.awt.Dimension(110, 36));
btnEtatRetards.addActionListener(e -> utils.EtatsHelperSmartCaisse.etatRetards());

javax.swing.JButton btnExcelPrets = new javax.swing.JButton("📊 Excel");
btnExcelPrets.setBackground(new java.awt.Color(33, 115, 70));
btnExcelPrets.setForeground(java.awt.Color.WHITE);
btnExcelPrets.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnExcelPrets.setBorderPainted(false);
btnExcelPrets.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnExcelPrets.setFocusPainted(false);
btnExcelPrets.setPreferredSize(new java.awt.Dimension(90, 36));
btnExcelPrets.addActionListener(e -> utils.EtatsHelperSmartCaisse.exportExcelPrets());

headerRight.add(btnEtatPrets, 0);
headerRight.add(btnEtatRetards, 1);
headerRight.add(btnExcelPrets, 2);
headerRight.revalidate();
    }

    private void configurerTableau() {
        tablePrets.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"N°", "Client", "Montant accordé",
                    "Mensualité", "Échéances", "Garantie", "Statut", "Actions"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 7;
            }
        });
        tablePrets.setRowHeight(44);
        tablePrets.getTableHeader().setReorderingAllowed(false);
        tablePrets.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablePrets.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablePrets.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablePrets.getColumnModel().getColumn(3).setPreferredWidth(110);
        tablePrets.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablePrets.getColumnModel().getColumn(5).setPreferredWidth(140);
        tablePrets.getColumnModel().getColumn(6).setPreferredWidth(90);
        tablePrets.getColumnModel().getColumn(7).setPreferredWidth(120);
    }

    private void configurerStyle() {
        tablePrets.getTableHeader().setBackground(new java.awt.Color(241, 245, 249));
        tablePrets.getTableHeader().setForeground(new java.awt.Color(71, 85, 105));
        tablePrets.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tablePrets.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));
        tablePrets.getTableHeader().setBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(226, 232, 240)));

        tablePrets.setBackground(java.awt.Color.WHITE);
        tablePrets.setSelectionBackground(new java.awt.Color(239, 246, 255));
        tablePrets.setSelectionForeground(new java.awt.Color(15, 23, 42));
        tablePrets.setGridColor(new java.awt.Color(241, 245, 249));
        tablePrets.setShowVerticalLines(false);
        tablePrets.setShowHorizontalLines(true);
        tablePrets.setIntercellSpacing(new java.awt.Dimension(0, 0));

        scrollPrets.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16));

        headerPanel.setBackground(java.awt.Color.WHITE);
        headerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        searchPanel.setBackground(new java.awt.Color(248, 250, 252));
        searchPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        txtSearch.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtSearch.setBackground(java.awt.Color.WHITE);

        cmbFiltre.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Tous", "En cours", "En retard", "Remboursé"}));
        cmbFiltre.setBackground(java.awt.Color.WHITE);
// Bouton nouveau prêt
        btnNouveauPret.setBorderPainted(false);
        btnNouveauPret.setFocusPainted(false);

        tablePrets.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
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
        cmbFiltre.addActionListener(e -> chargerPrets());
        btnSearch.addActionListener(e -> chargerPrets());
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                chargerPrets();
            }
        });
    }

    private void chargerPrets() {
        new DAO.PretDAO().mettreAJourStatuts();

        DAO.PretDAO dao = new DAO.PretDAO();
        java.util.List<Models.Pret> tousPrets = dao.listerTous();

        // ── Stats ──
        int nbEnCours = 0, nbRetard = 0, nbRembourse = 0, nbTotal = 0;
        double totalDecaisse = 0, totalEnCours = 0;

        for (Models.Pret p : tousPrets) {
            if ("Remboursé".equals(p.getStatut())) {
                continue;
            }
            String statutAuto = calculerStatutAutomatique(p);
            if (!p.getStatut().equals(statutAuto)) {
                dao.modifierStatut(p.getIdPret(), statutAuto);
                p.setStatut(statutAuto);
            }
            nbTotal++;
            totalDecaisse += p.getMontantPrincipal();
            switch (p.getStatut()) {
                case "En cours" -> {
                    nbEnCours++;
                    totalEnCours
                            += p.getMontantPrincipal();
                }
                case "En retard" ->
                    nbRetard++;
                case "Remboursé" ->
                    nbRembourse++;
            }
        }

        lblEnCoursVal.setText(String.valueOf(nbEnCours));
        lblEnCoursSub.setText(String.format("%,.0f F CFA", totalEnCours));
        lblDecaisseVal.setText(String.format("%,.0f F CFA", totalDecaisse));
        lblDecaisseSub.setText(nbTotal + " prêt(s) au total");
        lblRetardVal.setText(String.valueOf(nbRetard));
        lblRetardSub.setText(nbRetard > 0 ? "Attention requise !" : "Aucun retard");
        lblRetardVal.setForeground(nbRetard > 0
                ? new java.awt.Color(220, 38, 38)
                : new java.awt.Color(21, 128, 61));

        // ── Filtre ──
        String filtre = cmbFiltre.getSelectedItem().toString();
        String recherche = txtSearch.getText().trim().toLowerCase();

        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) tablePrets.getModel();
        model.setRowCount(0);

        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        DAO.ClientDAO clientDao = new DAO.ClientDAO();

        for (Models.Pret p : tousPrets) {
            if (!"Tous".equals(filtre) && !p.getStatut().equals(filtre)) {
                continue;
            }

            Models.Compte compte = compteDao.chercher(p.getIdCompte());
            String nomClient = "—";
            if (compte != null) {
                Models.Client client = clientDao.chercher(compte.getIdClient());
                if (client != null) {
                    nomClient = client.getNom() + " " + client.getPrenom();
                }
            }

            if (!recherche.isEmpty()) {
                if (!nomClient.toLowerCase().contains(recherche)
                        && !String.valueOf(p.getIdPret()).contains(recherche)) {
                    continue;
                }
            }

            double interets = p.getMontantPrincipal()
                    * p.getTauxInteret() * p.getDureeMois() / 1200.0;
            double total = p.getMontantPrincipal() + interets;
            double mensualite = total / p.getDureeMois();

            int echeancesPayees;
            if (p.getMontantRembourse() <= 0) {
                echeancesPayees = 0;
            } else if (total - p.getMontantRembourse() <= 1.0) {
                echeancesPayees = p.getDureeMois();
            } else {
                echeancesPayees = (int) Math.round(
                        p.getMontantRembourse() / mensualite);
                echeancesPayees = Math.min(echeancesPayees, p.getDureeMois());
            }

            String garantie = p.getGarantie() != null
                    && !p.getGarantie().isEmpty()
                    ? p.getGarantie() : "—";

            model.addRow(new Object[]{
                "#" + String.format("%03d", p.getIdPret()),
                nomClient,
                String.format("%,.0f F CFA", p.getMontantPrincipal()),
                String.format("%,.0f F CFA", mensualite),
                echeancesPayees + " / " + p.getDureeMois(),
                garantie,
                p.getStatut(),
                ""
            });
        }
    }

    private String calculerStatutAutomatique(Models.Pret p) {
        if ("Remboursé".equals(p.getStatut())) {
            return "Remboursé";
        }
        double interets = p.getMontantPrincipal()
                * p.getTauxInteret()
                * p.getDureeMois() / 1200.0;
        double totalAvecInterets = p.getMontantPrincipal() + interets;
        double reste = totalAvecInterets - p.getMontantRembourse();

        if (reste <= 2.0) {      // ← était 1.0
            return "Remboursé";
        }

        if (p.getDateFinPrevue() != null) {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate fin = p.getDateFinPrevue().toLocalDate();
            if (fin.isBefore(today)) {
                return "En retard";
            }
        }

        return "En cours";
    }

    private void configurerColonneActions() {
        // Renderer — 4 boutons
        tablePrets.getColumn("Actions").setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel p = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 3, 6));
                    p.setOpaque(true);
                    p.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    String statut = table.getValueAt(row, 6) != null
                    ? table.getValueAt(row, 6).toString() : "";
                    p.add(creerBoutonAction("/icons/eye.png",
                            new java.awt.Color(219, 234, 254)));
                    if (!"Remboursé".equals(statut)) {
                        p.add(creerBoutonAction("/icons/deposit.png",
                                new java.awt.Color(220, 252, 231)));
                        p.add(creerBoutonAction("/icons/cells.png",
                                new java.awt.Color(254, 243, 199)));
                        p.add(creerBoutonAction("/icons/fast.png",
                                new java.awt.Color(254, 226, 226)));
                    } else {
                        p.add(creerBoutonAction("/icons/cells.png",
                                new java.awt.Color(254, 243, 199)));
                    }
                    return p;
                });

        tablePrets.getColumn("Actions").setCellEditor(
                new javax.swing.DefaultCellEditor(
                        new javax.swing.JCheckBox()) {
            private javax.swing.JPanel panel;
            private int currentRow;
            private javax.swing.JTable tableRef;

            {
                panel = new javax.swing.JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.CENTER, 3, 6));
                panel.setOpaque(true);
                panel.setBackground(new java.awt.Color(239, 246, 255));

                javax.swing.JButton bVoir = creerBoutonAction(
                        "/icons/eye.png",
                        new java.awt.Color(219, 234, 254));
                javax.swing.JButton bRembourser = creerBoutonAction(
                        "/icons/deposit.png",
                        new java.awt.Color(220, 252, 231));
                javax.swing.JButton bTableau = creerBoutonAction(
                        "/icons/cells.png",
                        new java.awt.Color(254, 243, 199));
                javax.swing.JButton bAnticipe = creerBoutonAction(
                        "/icons/fast.png",
                        new java.awt.Color(254, 226, 226));

                // 👁 Voir
                bVoir.addActionListener(e -> {
                    fireEditingStopped();
                    String idStr = tableRef.getValueAt(currentRow, 0)
                            .toString().replace("#", "").trim();
                    int idPret = Integer.parseInt(idStr);
                    DetailPret dialog = new DetailPret(
                            (java.awt.Frame) javax.swing.SwingUtilities
                                    .getWindowAncestor(PretsPanel.this), true);
                    dialog.chargerPret(idPret);
                    dialog.setVisible(true);
                    chargerPrets();
                });

                // 💰 Rembourser
                bRembourser.addActionListener(e -> {
                    fireEditingStopped();
                    String statut = tableRef.getValueAt(currentRow, 6)
                            .toString();
                    if ("Remboursé".equals(statut)) {
                        javax.swing.JOptionPane.showMessageDialog(
                                PretsPanel.this,
                                "Ce prêt est déjà entièrement remboursé !",
                                "Information",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    String idStr = tableRef.getValueAt(currentRow, 0)
                            .toString().replace("#", "").trim();
                    int idPret = Integer.parseInt(idStr);
                    RemboursementDialog dialog = new RemboursementDialog(
                            (java.awt.Frame) javax.swing.SwingUtilities
                                    .getWindowAncestor(PretsPanel.this), true);
                    dialog.preselectionnerPret(idPret);
                    dialog.setVisible(true);
                    chargerPrets();
                });

                // 📋 Tableau amortissement
                bTableau.addActionListener(e -> {
                    fireEditingStopped();
                    String idStr = tableRef.getValueAt(currentRow, 0)
                            .toString().replace("#", "").trim();
                    int idPret = Integer.parseInt(idStr);
                    TableauAmortissement dialog = new TableauAmortissement(
                            (java.awt.Frame) javax.swing.SwingUtilities
                                    .getWindowAncestor(PretsPanel.this), true);
                    dialog.chargerPret(idPret);
                    dialog.setVisible(true);
                });

                // ⚡ Remboursement anticipé
                bAnticipe.addActionListener(e -> {
                    fireEditingStopped();
                    String statut = tableRef.getValueAt(currentRow, 6)
                            .toString();
                    if ("Remboursé".equals(statut)) {
                        javax.swing.JOptionPane.showMessageDialog(
                                PretsPanel.this,
                                "Ce prêt est déjà remboursé !",
                                "Information",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    String idStr = tableRef.getValueAt(currentRow, 0)
                            .toString().replace("#", "").trim();
                    int idPret = Integer.parseInt(idStr);
                    rembourserAnticipe(idPret);
                });

                panel.add(bVoir);
                panel.add(bRembourser);
                panel.add(bTableau);
                panel.add(bAnticipe);
            }

            @Override
            public java.awt.Component getTableCellEditorComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, int row, int col) {
                currentRow = row;
                tableRef = table;
                String statut = table.getValueAt(row, 6) != null
                        ? table.getValueAt(row, 6).toString() : "";
                // Cacher rembourser et anticipé si déjà remboursé
                panel.getComponent(1).setVisible(!"Remboursé".equals(statut));
                panel.getComponent(3).setVisible(!"Remboursé".equals(statut));
                return panel;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });

        tablePrets.getColumn("Actions").setPreferredWidth(130);
    }

    private void rembourserAnticipe(int idPret) {
        DAO.PretDAO pretDao = new DAO.PretDAO();
        Models.Pret pret = pretDao.chercher(idPret);
        if (pret == null) {
            return;
        }

        double interets = pret.getMontantPrincipal()
                * pret.getTauxInteret() * pret.getDureeMois() / 1200.0;
        double total = pret.getMontantPrincipal() + interets;
        double reste = Math.max(0, total - pret.getMontantRembourse());
        // Ajouter ceci :
        if ("En retard".equals(pret.getStatut())) {
            double penalite = new DAO.PretDAO().calculerPenalite(idPret);
            reste += penalite;
        }
        if (reste <= 0.5) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ce prêt est déjà totalement remboursé !",
                    "Information",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Calcul remise 50% sur intérêts restants non courus
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
                        + "Confirmer le remboursement anticipé ?",
                        reste, remise, montantAnticipe),
                "Remboursement anticipé",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        // Mettre à jour remboursement
        pretDao.mettreAJourRemboursement(idPret, montantAnticipe);
        pretDao.modifierStatut(idPret, "Remboursé");

        // Transaction
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
                        + "   Remise       : %,.0f F CFA\n"
                        + "   Statut       : Remboursé",
                        montantAnticipe, remise),
                "Succès",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        chargerPrets();
    }

    private void configurerRenderers() {
        ((javax.swing.table.DefaultTableCellRenderer) tablePrets.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.table.DefaultTableCellRenderer centreur
                = new javax.swing.table.DefaultTableCellRenderer();
        centreur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tablePrets.getColumnModel().getColumn(0).setCellRenderer(centreur);
        tablePrets.getColumnModel().getColumn(1).setCellRenderer(centreur);
        tablePrets.getColumnModel().getColumn(2).setCellRenderer(centreur);
        tablePrets.getColumnModel().getColumn(3).setCellRenderer(centreur);

        // Garantie — colonne 5
        tablePrets.getColumnModel().getColumn(5).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JLabel lbl = new javax.swing.JLabel(
                            value != null ? value.toString() : "—");
                    lbl.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.PLAIN, 11));
                    lbl.setForeground(new java.awt.Color(71, 85, 105));
                    lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    lbl.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    lbl.setOpaque(true);
                    return lbl;
                });

        // Échéances — colonne 4
        tablePrets.getColumnModel().getColumn(4).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    String text = value != null ? value.toString() : "0 / 0";
                    int payees = 0, total = 1;
                    try {
                        String[] parts = text.split(" / ");
                        payees = Integer.parseInt(parts[0].trim());
                        total = Integer.parseInt(parts[1].trim());
                    } catch (Exception ex) {
                    }
                    int pct = (int) ((double) payees / total * 100);

                    javax.swing.JPanel cell = new javax.swing.JPanel();
                    cell.setLayout(new java.awt.BorderLayout(0, 2));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    cell.setBorder(javax.swing.BorderFactory
                            .createEmptyBorder(8, 12, 8, 12));

                    javax.swing.JLabel lblEch = new javax.swing.JLabel(text);
                    lblEch.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.BOLD, 12));
                    lblEch.setForeground(new java.awt.Color(15, 23, 42));
                    lblEch.setHorizontalAlignment(
                            javax.swing.SwingConstants.CENTER);

                    javax.swing.JProgressBar bar
                    = new javax.swing.JProgressBar(0, 100);
                    bar.setValue(pct);
                    bar.setStringPainted(false);
                    bar.setPreferredSize(new java.awt.Dimension(0, 5));
                    bar.setBorderPainted(false);
                    if (pct >= 100) {
                        bar.setForeground(
                                new java.awt.Color(21, 128, 61));
                    } else if (pct >= 50) {
                        bar.setForeground(
                                new java.awt.Color(14, 165, 233));
                    } else {
                        bar.setForeground(new java.awt.Color(245, 158, 11));
                    }
                    bar.setBackground(new java.awt.Color(226, 232, 240));

                    cell.add(lblEch, java.awt.BorderLayout.CENTER);
                    cell.add(bar, java.awt.BorderLayout.SOUTH);
                    return cell;
                });

        // Badge Statut — colonne 6
        tablePrets.getColumnModel().getColumn(6).setCellRenderer(
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
                        case "En cours" -> {
                            bg = new java.awt.Color(219, 234, 254);
                            fg = new java.awt.Color(14, 165, 233);
                        }
                        case "En retard" -> {
                            bg = new java.awt.Color(254, 226, 226);
                            fg = new java.awt.Color(220, 38, 38);
                        }
                        case "Remboursé" -> {
                            bg = new java.awt.Color(220, 252, 231);
                            fg = new java.awt.Color(21, 128, 61);
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

    private javax.swing.JButton creerBoutonAction(String iconPath, java.awt.Color bg) {
        javax.swing.JButton btn = new javax.swing.JButton();
        try {
            java.net.URL url = getClass().getResource(iconPath);
            if (url != null) {
                btn.setIcon(new javax.swing.ImageIcon(url));
            } else {
                if (iconPath.contains("eye")) {
                    btn.setText("👁");
                } else if (iconPath.contains("deposit")) {
                    btn.setText("+");
                } else {
                    btn.setText("⚡");
                }
            }
        } catch (Exception e) {
            btn.setText("?");
        }
        btn.setBackground(bg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new java.awt.Dimension(30, 30));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPrets = new javax.swing.JScrollPane();
        tablePrets = new javax.swing.JTable();
        topPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerRight = new javax.swing.JPanel();
        btnNouveauPret = new javax.swing.JButton();
        headerLeft = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        cardEnCours = new javax.swing.JPanel();
        lblEnCoursTitle = new javax.swing.JLabel();
        lblEnCoursVal = new javax.swing.JLabel();
        lblEnCoursSub = new javax.swing.JLabel();
        cardDeCaisse = new javax.swing.JPanel();
        lblDeCaisseTitle = new javax.swing.JLabel();
        lblDecaisseVal = new javax.swing.JLabel();
        lblDecaisseSub = new javax.swing.JLabel();
        cardRetard = new javax.swing.JPanel();
        lblRetardTitle = new javax.swing.JLabel();
        lblRetardVal = new javax.swing.JLabel();
        lblRetardSub = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cmbFiltre = new javax.swing.JComboBox<>();

        setLayout(new java.awt.BorderLayout());

        scrollPrets.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16));

        tablePrets.setBackground(new java.awt.Color(252, 252, 253));
        tablePrets.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tablePrets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N° Prêt", "Client", "Montant", "Durée ( mois )", "Date début", "Date fin prévue", "Statut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablePrets.setGridColor(new java.awt.Color(214, 245, 249));
        tablePrets.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tablePrets.setRowHeight(44);
        tablePrets.setSelectionBackground(new java.awt.Color(219, 234, 254));
        tablePrets.setSelectionForeground(new java.awt.Color(14, 165, 233));
        tablePrets.setShowGrid(true);
        scrollPrets.setViewportView(tablePrets);

        add(scrollPrets, java.awt.BorderLayout.CENTER);

        topPanel.setBackground(new java.awt.Color(248, 250, 252));
        topPanel.setMaximumSize(new java.awt.Dimension(9999, 180));
        topPanel.setPreferredSize(new java.awt.Dimension(0, 200));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBackground(new java.awt.Color(248, 250, 252));
        headerPanel.setAlignmentX(0.0F);
        headerPanel.setMaximumSize(new java.awt.Dimension(9999, 60));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 80));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerRight.setBackground(new java.awt.Color(248, 250, 252));
        headerRight.setAlignmentX(0.0F);
        headerRight.setPreferredSize(new java.awt.Dimension(510, 60));
        headerRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 12));

        btnNouveauPret.setBackground(new java.awt.Color(14, 165, 233));
        btnNouveauPret.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnNouveauPret.setForeground(new java.awt.Color(255, 255, 255));
        btnNouveauPret.setText("+ Nouveau prêt");
        btnNouveauPret.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNouveauPret.setFocusPainted(false);
        btnNouveauPret.setPreferredSize(new java.awt.Dimension(140, 36));
        btnNouveauPret.addActionListener(this::btnNouveauPretActionPerformed);
        headerRight.add(btnNouveauPret);

        headerPanel.add(headerRight, java.awt.BorderLayout.EAST);

        headerLeft.setBackground(new java.awt.Color(248, 250, 252));
        headerLeft.setPreferredSize(new java.awt.Dimension(300, 60));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(15, 23, 42));
        lblTitle.setText("Gestion des prêts");
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        headerLeft.add(lblTitle);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        topPanel.add(headerPanel);

        statsPanel.setBackground(new java.awt.Color(248, 250, 252));
        statsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 8, 16));
        statsPanel.setAlignmentX(0.0F);
        statsPanel.setMaximumSize(new java.awt.Dimension(9999, 80));
        statsPanel.setPreferredSize(new java.awt.Dimension(0, 75));
        statsPanel.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        cardEnCours.setBackground(new java.awt.Color(255, 255, 255));
        cardEnCours.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(59, 130, 246))));
        cardEnCours.setLayout(new javax.swing.BoxLayout(cardEnCours, javax.swing.BoxLayout.Y_AXIS));

        lblEnCoursTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblEnCoursTitle.setText("Prêts en cours");
        lblEnCoursTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 0));
        cardEnCours.add(lblEnCoursTitle);

        lblEnCoursVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEnCoursVal.setForeground(new java.awt.Color(15, 23, 42));
        lblEnCoursVal.setText("0 ");
        lblEnCoursVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardEnCours.add(lblEnCoursVal);

        lblEnCoursSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblEnCoursSub.setForeground(new java.awt.Color(100, 116, 139));
        lblEnCoursSub.setText("0 FCFA");
        lblEnCoursSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardEnCours.add(lblEnCoursSub);

        statsPanel.add(cardEnCours);

        cardDeCaisse.setBackground(new java.awt.Color(255, 255, 255));
        cardDeCaisse.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(245, 158, 11))));
        cardDeCaisse.setLayout(new javax.swing.BoxLayout(cardDeCaisse, javax.swing.BoxLayout.Y_AXIS));

        lblDeCaisseTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblDeCaisseTitle.setText("Total Décaissé");
        lblDeCaisseTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 0));
        cardDeCaisse.add(lblDeCaisseTitle);

        lblDecaisseVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDecaisseVal.setForeground(new java.awt.Color(15, 23, 42));
        lblDecaisseVal.setText("0 FCFA");
        lblDecaisseVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardDeCaisse.add(lblDecaisseVal);

        lblDecaisseSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDecaisseSub.setForeground(new java.awt.Color(100, 116, 139));
        lblDecaisseSub.setText("0 prêts total");
        lblDecaisseSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardDeCaisse.add(lblDecaisseSub);

        statsPanel.add(cardDeCaisse);

        cardRetard.setBackground(new java.awt.Color(255, 255, 255));
        cardRetard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(200, 38, 38))));
        cardRetard.setLayout(new javax.swing.BoxLayout(cardRetard, javax.swing.BoxLayout.Y_AXIS));

        lblRetardTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblRetardTitle.setText("Prêts en retard");
        lblRetardTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 0));
        cardRetard.add(lblRetardTitle);

        lblRetardVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblRetardVal.setForeground(new java.awt.Color(15, 23, 42));
        lblRetardVal.setText("0");
        lblRetardVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardRetard.add(lblRetardVal);

        lblRetardSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblRetardSub.setForeground(new java.awt.Color(100, 116, 139));
        lblRetardSub.setText("Attention requise");
        lblRetardSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardRetard.add(lblRetardSub);

        statsPanel.add(cardRetard);

        topPanel.add(statsPanel);

        searchPanel.setBackground(new java.awt.Color(248, 250, 252));
        searchPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 9, 0, 0));
        searchPanel.setAlignmentX(0.0F);
        searchPanel.setMaximumSize(new java.awt.Dimension(9999, 45));
        searchPanel.setPreferredSize(new java.awt.Dimension(0, 80));
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 5));

        txtSearch.setPreferredSize(new java.awt.Dimension(300, 34));
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        searchPanel.add(txtSearch);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        btnSearch.setText("Rechercher");
        btnSearch.setPreferredSize(new java.awt.Dimension(120, 34));
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        searchPanel.add(btnSearch);

        cmbFiltre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous", "En cours", "Remboursé", "En retard", "Rejeté" }));
        cmbFiltre.setPreferredSize(new java.awt.Dimension(120, 34));
        searchPanel.add(cmbFiltre);

        topPanel.add(searchPanel);

        add(topPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        chargerPrets();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnNouveauPretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauPretActionPerformed
        // TODO add your handling code here:
        AjouterPret dialog = new AjouterPret(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true
        );
        dialog.setVisible(true);
        chargerPrets();
    }//GEN-LAST:event_btnNouveauPretActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        chargerPrets();
    }//GEN-LAST:event_txtSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNouveauPret;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel cardDeCaisse;
    private javax.swing.JPanel cardEnCours;
    private javax.swing.JPanel cardRetard;
    private javax.swing.JComboBox<String> cmbFiltre;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerRight;
    private javax.swing.JLabel lblDeCaisseTitle;
    private javax.swing.JLabel lblDecaisseSub;
    private javax.swing.JLabel lblDecaisseVal;
    private javax.swing.JLabel lblEnCoursSub;
    private javax.swing.JLabel lblEnCoursTitle;
    private javax.swing.JLabel lblEnCoursVal;
    private javax.swing.JLabel lblRetardSub;
    private javax.swing.JLabel lblRetardTitle;
    private javax.swing.JLabel lblRetardVal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollPrets;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTable tablePrets;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
