/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class RapportsPanel extends javax.swing.JPanel {

    /**
     * Creates new form RapportsPanel
     */
    private String modeActuel = "journalier";
    private double lastDepots, lastRetraits, lastRemb, lastDecaisse, lastSolde;
    private java.util.List<String[]> lastLignes = new java.util.ArrayList<>();

    public RapportsPanel() {
        initComponents();

        configurerStyles();
        configurerModeles();
        configurerTableau();
        configurerListeners();
        chargerRapport();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                chargerRapport();
            }
        });
    }

    private void configurerStyles() {
        // Bouton journalier actif par défaut
        btnJournalier.setBackground(new java.awt.Color(14, 165, 233));
        btnJournalier.setForeground(java.awt.Color.WHITE);
        btnJournalier.setBorderPainted(false);
        btnJournalier.setFocusPainted(false);
        btnJournalier.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        // Bouton mensuel inactif
        btnMensuel.setBackground(java.awt.Color.WHITE);
        btnMensuel.setForeground(new java.awt.Color(71, 85, 105));
        btnMensuel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        btnMensuel.setFocusPainted(false);
        btnMensuel.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        // Bouton actualiser
        btnActualiser.setBackground(new java.awt.Color(14, 165, 233));
        btnActualiser.setForeground(java.awt.Color.WHITE);
        btnActualiser.setBorderPainted(false);
        btnActualiser.setFocusPainted(false);
        btnActualiser.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        // Bouton imprimer
        btnImprimer.setBackground(new java.awt.Color(15, 23, 42));
        btnImprimer.setForeground(java.awt.Color.WHITE);
        btnImprimer.setBorderPainted(false);
        btnImprimer.setFocusPainted(false);
        btnImprimer.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR));

        // Masquer mois/année par défaut
        cmbMois.setVisible(false);
        cmbAnnee.setVisible(false);
    }

    private void configurerModeles() {
        // Modèle cmbMois
        cmbMois.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{
                    "Janvier", "Février", "Mars", "Avril",
                    "Mai", "Juin", "Juillet", "Août",
                    "Septembre", "Octobre", "Novembre", "Décembre"
                }));
        cmbMois.setSelectedIndex(
                java.time.LocalDate.now().getMonthValue() - 1);

        // Modèle cmbAnnee
        int annee = java.time.LocalDate.now().getYear();
        String[] annees = new String[5];
        for (int i = 0; i < 5; i++) {
            annees[i] = String.valueOf(annee - i);
        }
        cmbAnnee.setModel(
                new javax.swing.DefaultComboBoxModel<>(annees));

        // Date du jour par défaut
        dateChooser.setDate(new java.util.Date());
    }

    private void configurerTableau() {
        tableRapport.setModel(
                new javax.swing.table.DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                            "Client", "Compte", "Type",
                            "Montant", "Moyen", "Date"
                        }
                ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
        tableRapport.setRowHeight(40);
        tableRapport.getTableHeader().setBackground(
                new java.awt.Color(241, 245, 249));
        tableRapport.getTableHeader().setForeground(
                new java.awt.Color(71, 85, 105));
        tableRapport.getTableHeader().setFont(
                new java.awt.Font("Segoe UI",
                        java.awt.Font.BOLD, 12));
        tableRapport.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 40));
        tableRapport.setBackground(java.awt.Color.WHITE);
        tableRapport.setGridColor(
                new java.awt.Color(241, 245, 249));
        tableRapport.setShowVerticalLines(false);
        tableRapport.setShowHorizontalLines(true);
        tableRapport.setSelectionBackground(
                new java.awt.Color(239, 246, 255));
        tableRapport.getTableHeader()
                .setReorderingAllowed(false);

        // Renderer montant coloré — colonne 3
        tableRapport.getColumnModel().getColumn(3)
                .setCellRenderer(
                        (table, value, isSelected,
                                hasFocus, row, col) -> {
                            String text = value != null
                                    ? value.toString() : "";
                            javax.swing.JLabel lbl
                            = new javax.swing.JLabel(text);
                            lbl.setHorizontalAlignment(
                                    javax.swing.SwingConstants.CENTER);
                            lbl.setFont(new java.awt.Font(
                                    "Segoe UI",
                                    java.awt.Font.BOLD, 12));
                            lbl.setForeground(
                                    text.startsWith("+")
                                    ? new java.awt.Color(21, 128, 61)
                                    : new java.awt.Color(220, 38, 38));
                            lbl.setOpaque(true);
                            lbl.setBackground(isSelected
                                    ? new java.awt.Color(239, 246, 255)
                                    : java.awt.Color.WHITE);
                            return lbl;
                        });
    }

    private void configurerListeners() {
        btnJournalier.addActionListener(e -> activerJournalier());
        btnMensuel.addActionListener(e -> activerMensuel());
        btnActualiser.addActionListener(e -> chargerRapport());
        btnImprimer.addActionListener(e -> imprimerRapport());
    }

    private void activerJournalier() {
        modeActuel = "journalier";
        btnJournalier.setBackground(new java.awt.Color(14, 165, 233));
        btnJournalier.setForeground(java.awt.Color.WHITE);
        btnJournalier.setBorderPainted(false);
        btnMensuel.setBackground(java.awt.Color.WHITE);
        btnMensuel.setForeground(new java.awt.Color(71, 85, 105));
        btnMensuel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        dateChooser.setVisible(true);
        cmbMois.setVisible(false);
        cmbAnnee.setVisible(false);
        chargerRapport();
    }

    private void activerMensuel() {
        modeActuel = "mensuel";
        btnMensuel.setBackground(new java.awt.Color(14, 165, 233));
        btnMensuel.setForeground(java.awt.Color.WHITE);
        btnMensuel.setBorderPainted(false);
        btnJournalier.setBackground(java.awt.Color.WHITE);
        btnJournalier.setForeground(new java.awt.Color(71, 85, 105));
        btnJournalier.setBorder(
                javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(
                                new java.awt.Color(226, 232, 240)),
                        javax.swing.BorderFactory
                                .createEmptyBorder(4, 12, 4, 12)));
        dateChooser.setVisible(false);
        cmbMois.setVisible(true);
        cmbAnnee.setVisible(true);
        chargerRapport();
    }

    private void chargerRapport() {
        DAO.TransactionDAO dao = new DAO.TransactionDAO();
        java.util.List<Models.Transaction> toutes = dao.listerTous();
        java.util.List<Models.Transaction> filtrees
                = new java.util.ArrayList<>();

        if ("journalier".equals(modeActuel)) {
            java.util.Date dateChoisie = dateChooser.getDate();
            if (dateChoisie == null) {
                dateChoisie = new java.util.Date();
            }
            java.time.LocalDate jourChoisi = dateChoisie.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            for (Models.Transaction t : toutes) {
                if (t.getDateHeure() != null) {
                    java.time.LocalDate dateTrans = t.getDateHeure()
                            .toLocalDateTime().toLocalDate();
                    if (dateTrans.equals(jourChoisi)) {
                        filtrees.add(t);
                    }
                }
            }
        } else {
            int moisChoisi = cmbMois.getSelectedIndex() + 1;
            int anneeChoisie = Integer.parseInt(
                    cmbAnnee.getSelectedItem().toString());
            for (Models.Transaction t : toutes) {
                if (t.getDateHeure() != null) {
                    java.time.LocalDate dateTrans = t.getDateHeure()
                            .toLocalDateTime().toLocalDate();
                    if (dateTrans.getMonthValue() == moisChoisi
                            && dateTrans.getYear() == anneeChoisie) {
                        filtrees.add(t);
                    }
                }
            }
        }

        // ── Calculer stats ──
        double depots = 0, retraits = 0, remb = 0, decaisse = 0;
        for (Models.Transaction t : filtrees) {
            switch (t.getType()) {
                case "Dépôt épargne", "Dépôt initial", "Intérêts épargne" ->
                    depots += t.getMontant();
                case "Retrait épargne" ->
                    retraits += t.getMontant();
                case "Remboursement", "Remboursement anticipé" ->
                    remb += t.getMontant();
                case "Décaissement" ->
                    decaisse += t.getMontant();
            }
        }
        double solde = (depots + remb) - (retraits + decaisse);
        // Sauvegarder pour impression
        lastDepots = depots;
        lastRetraits = retraits;
        lastRemb = remb;
        lastDecaisse = decaisse;
        lastSolde = solde;

        // ── Mettre à jour les cards ──
        lblTotalVal.setText(String.valueOf(filtrees.size()));
        lblDepotsVal.setText(formater(depots));
        lblRetraitsVal.setText(formater(retraits));
        lblRembVal.setText(formater(remb));
        lblDecaisseVal.setText(formater(decaisse));
        lblSoldeVal.setText((solde >= 0 ? "+" : "") + formater(solde));
        lblSoldeVal.setForeground(solde >= 0
                ? new java.awt.Color(21, 128, 61)
                : new java.awt.Color(220, 38, 38));

        // ── Remplir tableau ──
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) tableRapport.getModel();
        model.setRowCount(0);

        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        DAO.ClientDAO clientDao = new DAO.ClientDAO();

        lastLignes.clear();
        for (Models.Transaction t : filtrees) {
            Models.Compte compte
                    = compteDao.chercher(t.getIdCompte());
            String nomClient = "—", numCompte = "—";
            if (compte != null) {
                numCompte = compte.getNumeroCompte();
                Models.Client client
                        = clientDao.chercher(compte.getIdClient());
                if (client != null) {
                    nomClient = client.getNom()
                            + " " + client.getPrenom();
                }

            }

            String montantStr;
            switch (t.getType()) {
                case "Dépôt épargne", "Dépôt initial", "Intérêts épargne", "Remboursement", "Remboursement anticipé" ->
                    montantStr = "+ " + formater(t.getMontant());
                case "Retrait épargne", "Décaissement" ->
                    montantStr = "- " + formater(t.getMontant());
                default ->
                    montantStr = formater(t.getMontant());
            }

            String dateStr = t.getDateHeure() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(t.getDateHeure()) : "—";

            String[] ligne = new String[]{
                nomClient, numCompte, t.getType(),
                montantStr,
                t.getMoyenPaiement() != null
                && !t.getMoyenPaiement().isEmpty()
                ? t.getMoyenPaiement() : "—",
                dateStr
            };
            lastLignes.add(ligne);
            model.addRow(ligne);
        }
    }

 private void imprimerRapport() {

    // ── Vérifier qu'il y a des données ──
    if (lastLignes.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Aucune transaction à imprimer\n"
                + "pour cette période.",
                "Aucune donnée",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    if ("journalier".equals(modeActuel)) {
        java.util.Date date = dateChooser.getDate();
        if (date == null) date = new java.util.Date();
        Utils.ImpressionUtil.imprimerRapportJournalier(
                date, lastDepots, lastRetraits,
                lastRemb, lastDecaisse, lastSolde, lastLignes);
    } else {
        int mois = cmbMois.getSelectedIndex() + 1;
        int annee = Integer.parseInt(
                cmbAnnee.getSelectedItem().toString());
        Utils.ImpressionUtil.imprimerRapportMensuel(
                mois, annee, lastDepots, lastRetraits,
                lastRemb, lastDecaisse, lastSolde, lastLignes);
    }
}

   private String formater(double montant) {
    long valeur = Math.round(montant);
    String s = String.valueOf(valeur);
    StringBuilder sb = new StringBuilder();
    int count = 0;
    for (int i = s.length() - 1; i >= 0; i--) {
        if (count > 0 && count % 3 == 0) {
            sb.insert(0, ' ');
        }
        sb.insert(0, s.charAt(i));
        count++;
    }
    return sb.toString() + " F CFA";
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
        lblTitre = new javax.swing.JLabel();
        filtrePanel = new javax.swing.JPanel();
        btnJournalier = new javax.swing.JButton();
        btnMensuel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        dateChooser = new com.toedter.calendar.JDateChooser();
        cmbMois = new javax.swing.JComboBox<>();
        cmbAnnee = new javax.swing.JComboBox<>();
        btnActualiser = new javax.swing.JButton();
        cardsPanel = new javax.swing.JPanel();
        cardTotal = new javax.swing.JPanel();
        lblTotalTitre = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        lblTotalSub = new javax.swing.JLabel();
        cardDepots = new javax.swing.JPanel();
        lblDepotsTitre = new javax.swing.JLabel();
        lblDepotsVal = new javax.swing.JLabel();
        lblDepotsSub = new javax.swing.JLabel();
        cardRetraits = new javax.swing.JPanel();
        lblRetraitsTitre = new javax.swing.JLabel();
        lblRetraitsVal = new javax.swing.JLabel();
        lblRetraitsSub = new javax.swing.JLabel();
        cardRemb = new javax.swing.JPanel();
        lblRemVal = new javax.swing.JLabel();
        lblRembVal = new javax.swing.JLabel();
        lblRembSub = new javax.swing.JLabel();
        cardDecaisse = new javax.swing.JPanel();
        lblDecaisseTitre = new javax.swing.JLabel();
        lblDecaisseVal = new javax.swing.JLabel();
        lblDecaisseSub = new javax.swing.JLabel();
        cardSolde = new javax.swing.JPanel();
        lblSoldeTitre = new javax.swing.JLabel();
        lblSoldeVal = new javax.swing.JLabel();
        lblSoldeSub = new javax.swing.JLabel();
        scrollRapport = new javax.swing.JScrollPane();
        tableRapport = new javax.swing.JTable();
        footerPanel = new javax.swing.JPanel();
        btnImprimer = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        topPanel.setBackground(new java.awt.Color(248, 250, 252));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBackground(new java.awt.Color(248, 250, 252));
        headerPanel.setMaximumSize(new java.awt.Dimension(9999, 60));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        headerPanel.setLayout(new java.awt.BorderLayout());

        lblTitre.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitre.setForeground(new java.awt.Color(15, 23, 42));
        lblTitre.setText("Rappports & Statistiques");
        lblTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        headerPanel.add(lblTitre, java.awt.BorderLayout.WEST);

        topPanel.add(headerPanel);

        filtrePanel.setBackground(new java.awt.Color(248, 250, 252));
        filtrePanel.setMaximumSize(new java.awt.Dimension(9999, 55));
        filtrePanel.setPreferredSize(new java.awt.Dimension(0, 55));
        filtrePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 8));

        btnJournalier.setBackground(new java.awt.Color(14, 165, 233));
        btnJournalier.setForeground(new java.awt.Color(255, 255, 255));
        btnJournalier.setText("Rapport Journalier");
        btnJournalier.setBorderPainted(false);
        btnJournalier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnJournalier.setFocusPainted(false);
        btnJournalier.setPreferredSize(new java.awt.Dimension(180, 34));
        filtrePanel.add(btnJournalier);

        btnMensuel.setForeground(new java.awt.Color(71, 85, 105));
        btnMensuel.setText("Rapport Mensuel");
        btnMensuel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMensuel.setFocusPainted(false);
        btnMensuel.setPreferredSize(new java.awt.Dimension(160, 34));
        filtrePanel.add(btnMensuel);

        jSeparator1.setPreferredSize(new java.awt.Dimension(1, 30));
        filtrePanel.add(jSeparator1);

        dateChooser.setPreferredSize(new java.awt.Dimension(140, 34));
        filtrePanel.add(dateChooser);

        cmbMois.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbMois.setPreferredSize(new java.awt.Dimension(120, 34));
        filtrePanel.add(cmbMois);

        cmbAnnee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnnee.setPreferredSize(new java.awt.Dimension(90, 34));
        filtrePanel.add(cmbAnnee);

        btnActualiser.setBackground(new java.awt.Color(14, 165, 233));
        btnActualiser.setForeground(new java.awt.Color(255, 255, 255));
        btnActualiser.setText("Actualiser");
        btnActualiser.setBorderPainted(false);
        btnActualiser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualiser.setFocusPainted(false);
        btnActualiser.setPreferredSize(new java.awt.Dimension(120, 34));
        filtrePanel.add(btnActualiser);

        topPanel.add(filtrePanel);

        cardsPanel.setBackground(new java.awt.Color(248, 250, 252));
        cardsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 8, 12));
        cardsPanel.setMaximumSize(new java.awt.Dimension(9999, 90));
        cardsPanel.setPreferredSize(new java.awt.Dimension(0, 90));
        cardsPanel.setLayout(new java.awt.GridLayout(1, 6, 8, 0));

        cardTotal.setBackground(new java.awt.Color(255, 255, 255));
        cardTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(59, 130, 246))));
        cardTotal.setLayout(new javax.swing.BoxLayout(cardTotal, javax.swing.BoxLayout.Y_AXIS));

        lblTotalTitre.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblTotalTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblTotalTitre.setText("Transactions");
        cardTotal.add(lblTotalTitre);

        lblTotalVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTotalVal.setForeground(new java.awt.Color(15, 23, 42));
        lblTotalVal.setText("0");
        cardTotal.add(lblTotalVal);

        lblTotalSub.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        lblTotalSub.setForeground(new java.awt.Color(148, 163, 184));
        lblTotalSub.setText("Opérations");
        cardTotal.add(lblTotalSub);

        cardsPanel.add(cardTotal);

        cardDepots.setBackground(new java.awt.Color(255, 255, 255));
        cardDepots.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(34, 197, 94))));
        cardDepots.setLayout(new javax.swing.BoxLayout(cardDepots, javax.swing.BoxLayout.Y_AXIS));

        lblDepotsTitre.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblDepotsTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblDepotsTitre.setText("Dépôts");
        cardDepots.add(lblDepotsTitre);

        lblDepotsVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDepotsVal.setForeground(new java.awt.Color(15, 23, 42));
        lblDepotsVal.setText("0");
        cardDepots.add(lblDepotsVal);

        lblDepotsSub.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        lblDepotsSub.setForeground(new java.awt.Color(148, 163, 184));
        lblDepotsSub.setText("entrées");
        cardDepots.add(lblDepotsSub);

        cardsPanel.add(cardDepots);

        cardRetraits.setBackground(new java.awt.Color(255, 255, 255));
        cardRetraits.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(220, 38, 38))));
        cardRetraits.setLayout(new javax.swing.BoxLayout(cardRetraits, javax.swing.BoxLayout.Y_AXIS));

        lblRetraitsTitre.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblRetraitsTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblRetraitsTitre.setText("Retraits");
        cardRetraits.add(lblRetraitsTitre);

        lblRetraitsVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblRetraitsVal.setForeground(new java.awt.Color(15, 23, 42));
        lblRetraitsVal.setText("0 F CFA");
        cardRetraits.add(lblRetraitsVal);

        lblRetraitsSub.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        lblRetraitsSub.setForeground(new java.awt.Color(148, 163, 184));
        lblRetraitsSub.setText("sorties");
        cardRetraits.add(lblRetraitsSub);

        cardsPanel.add(cardRetraits);

        cardRemb.setBackground(new java.awt.Color(255, 255, 255));
        cardRemb.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(245, 158, 11))));
        cardRemb.setLayout(new javax.swing.BoxLayout(cardRemb, javax.swing.BoxLayout.Y_AXIS));

        lblRemVal.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblRemVal.setForeground(new java.awt.Color(100, 116, 139));
        lblRemVal.setText("Remboursements");
        cardRemb.add(lblRemVal);

        lblRembVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblRembVal.setForeground(new java.awt.Color(15, 23, 42));
        lblRembVal.setText("0 F CFA");
        cardRemb.add(lblRembVal);

        lblRembSub.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        lblRembSub.setForeground(new java.awt.Color(148, 163, 184));
        lblRembSub.setText("recouvrements");
        cardRemb.add(lblRembSub);

        cardsPanel.add(cardRemb);

        cardDecaisse.setBackground(new java.awt.Color(255, 255, 255));
        cardDecaisse.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(168, 85, 247))));
        cardDecaisse.setLayout(new javax.swing.BoxLayout(cardDecaisse, javax.swing.BoxLayout.Y_AXIS));

        lblDecaisseTitre.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblDecaisseTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblDecaisseTitre.setText("Décaissements");
        cardDecaisse.add(lblDecaisseTitre);

        lblDecaisseVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDecaisseVal.setForeground(new java.awt.Color(15, 23, 42));
        lblDecaisseVal.setText("0 F CFA");
        cardDecaisse.add(lblDecaisseVal);

        lblDecaisseSub.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        lblDecaisseSub.setForeground(new java.awt.Color(148, 163, 184));
        lblDecaisseSub.setText("prêts accordés");
        cardDecaisse.add(lblDecaisseSub);

        cardsPanel.add(cardDecaisse);

        cardSolde.setBackground(new java.awt.Color(255, 255, 255));
        cardSolde.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(20, 184, 166))));
        cardSolde.setLayout(new javax.swing.BoxLayout(cardSolde, javax.swing.BoxLayout.Y_AXIS));

        lblSoldeTitre.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSoldeTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblSoldeTitre.setText("Solde net");
        cardSolde.add(lblSoldeTitre);

        lblSoldeVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblSoldeVal.setForeground(new java.awt.Color(15, 23, 42));
        lblSoldeVal.setText("0 F CFA");
        cardSolde.add(lblSoldeVal);

        lblSoldeSub.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        lblSoldeSub.setForeground(new java.awt.Color(148, 163, 184));
        lblSoldeSub.setText("flux net");
        cardSolde.add(lblSoldeSub);

        cardsPanel.add(cardSolde);

        topPanel.add(cardsPanel);

        add(topPanel, java.awt.BorderLayout.NORTH);

        scrollRapport.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 12));

        tableRapport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Client", "Compte", "Type", "Montant", "Moyen", "Date"
            }
        ));
        tableRapport.setGridColor(new java.awt.Color(241, 245, 249));
        tableRapport.setRowHeight(40);
        tableRapport.setSelectionBackground(new java.awt.Color(239, 246, 255));
        scrollRapport.setViewportView(tableRapport);

        add(scrollRapport, java.awt.BorderLayout.CENTER);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)));
        footerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 10));

        btnImprimer.setBackground(new java.awt.Color(15, 23, 42));
        btnImprimer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnImprimer.setForeground(new java.awt.Color(255, 255, 255));
        btnImprimer.setText("Imprimer");
        btnImprimer.setBorderPainted(false);
        btnImprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimer.setFocusPainted(false);
        btnImprimer.setPreferredSize(new java.awt.Dimension(220, 40));
        footerPanel.add(btnImprimer);

        add(footerPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualiser;
    private javax.swing.JButton btnImprimer;
    private javax.swing.JButton btnJournalier;
    private javax.swing.JButton btnMensuel;
    private javax.swing.JPanel cardDecaisse;
    private javax.swing.JPanel cardDepots;
    private javax.swing.JPanel cardRemb;
    private javax.swing.JPanel cardRetraits;
    private javax.swing.JPanel cardSolde;
    private javax.swing.JPanel cardTotal;
    private javax.swing.JPanel cardsPanel;
    private javax.swing.JComboBox<String> cmbAnnee;
    private javax.swing.JComboBox<String> cmbMois;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JPanel filtrePanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDecaisseSub;
    private javax.swing.JLabel lblDecaisseTitre;
    private javax.swing.JLabel lblDecaisseVal;
    private javax.swing.JLabel lblDepotsSub;
    private javax.swing.JLabel lblDepotsTitre;
    private javax.swing.JLabel lblDepotsVal;
    private javax.swing.JLabel lblRemVal;
    private javax.swing.JLabel lblRembSub;
    private javax.swing.JLabel lblRembVal;
    private javax.swing.JLabel lblRetraitsSub;
    private javax.swing.JLabel lblRetraitsTitre;
    private javax.swing.JLabel lblRetraitsVal;
    private javax.swing.JLabel lblSoldeSub;
    private javax.swing.JLabel lblSoldeTitre;
    private javax.swing.JLabel lblSoldeVal;
    private javax.swing.JLabel lblTitre;
    private javax.swing.JLabel lblTotalSub;
    private javax.swing.JLabel lblTotalTitre;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JScrollPane scrollRapport;
    private javax.swing.JTable tableRapport;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
