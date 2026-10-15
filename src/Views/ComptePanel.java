/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author Admin
 */
public class ComptePanel extends javax.swing.JPanel {

    /**
     * Creates new form EpargnePanel
     */
    public ComptePanel() {
        initComponents();
        configurerTableau();
        configurerStyle();
        configurerColonneActions();
        configurerRenderers();
        chargerComptes();

        cmbFiltre.addActionListener(e -> chargerComptes());
        btnSearch.addActionListener(e -> chargerComptes());
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                chargerComptes();
            }
        });

        btnNouveauCompte.addActionListener(e -> {
            AjouterCompte dialog = new AjouterCompte(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(ComptePanel.this), true);
            dialog.setVisible(true);
            chargerComptes();
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                chargerComptes();
            }
        });
        // ── Boutons états comptes ──
javax.swing.JButton btnEtatComptes = new javax.swing.JButton("📄 État");
btnEtatComptes.setBackground(new java.awt.Color(245, 240, 232));
btnEtatComptes.setForeground(new java.awt.Color(15, 23, 42));
btnEtatComptes.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnEtatComptes.setBorder(javax.swing.BorderFactory.createLineBorder(
        new java.awt.Color(14, 165, 233)));
btnEtatComptes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnEtatComptes.setFocusPainted(false);
btnEtatComptes.setPreferredSize(new java.awt.Dimension(90, 36));
btnEtatComptes.addActionListener(e -> utils.EtatsHelperSmartCaisse.etatComptes());

javax.swing.JButton btnExcelComptes = new javax.swing.JButton("📊 Excel");
btnExcelComptes.setBackground(new java.awt.Color(33, 115, 70));
btnExcelComptes.setForeground(java.awt.Color.WHITE);
btnExcelComptes.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
btnExcelComptes.setBorderPainted(false);
btnExcelComptes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnExcelComptes.setFocusPainted(false);
btnExcelComptes.setPreferredSize(new java.awt.Dimension(90, 36));
btnExcelComptes.addActionListener(e -> utils.EtatsHelperSmartCaisse.exportExcelComptes());

headerRight.add(btnEtatComptes, 0);
headerRight.add(btnExcelComptes, 1);
headerRight.revalidate();
    }

    private void configurerTableau() {
        tableComptes.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"N° Compte", "Client", "Type",
                    "Solde", "Ouverture", "Statut", "Actions"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        });
        tableComptes.setRowHeight(44);
        tableComptes.getTableHeader().setReorderingAllowed(false);
        tableComptes.getColumnModel().getColumn(0).setPreferredWidth(130);
        tableComptes.getColumnModel().getColumn(1).setPreferredWidth(160);
        tableComptes.getColumnModel().getColumn(2).setPreferredWidth(90);
        tableComptes.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableComptes.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableComptes.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableComptes.getColumnModel().getColumn(6).setPreferredWidth(120);
    }

    private void configurerStyle() {
        // Header tableau
        tableComptes.getTableHeader().setBackground(
                new java.awt.Color(241, 245, 249));
        tableComptes.getTableHeader().setForeground(
                new java.awt.Color(71, 85, 105));
        tableComptes.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tableComptes.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 40));
        tableComptes.setBackground(java.awt.Color.WHITE);
        tableComptes.setSelectionBackground(
                new java.awt.Color(239, 246, 255));
        tableComptes.setGridColor(new java.awt.Color(241, 245, 249));
        tableComptes.setShowVerticalLines(false);
        tableComptes.setShowHorizontalLines(true);

        // Supprimer item vide dans filtre
        cmbFiltre.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Tous", "Courant", "À terme",
                    "Tontine", "Clôturés"}));
        cmbFiltre.setBackground(java.awt.Color.WHITE);

        // Champ recherche
        txtSearch.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)));

        // Bouton nouveau compte
        btnNouveauCompte.setBorderPainted(false);
        btnNouveauCompte.setFocusPainted(false);
    }

    private void chargerComptes() {
        DAO.CompteDAO dao = new DAO.CompteDAO();
        DAO.ClientDAO clientDao = new DAO.ClientDAO();

        // Stats — toujours sur tous les comptes actifs
        java.util.List<Models.Compte> tousActifs = dao.listerActifs();

        double totalCourant = 0;
        int nbCourant = 0;
        double totalTerme = 0;
        int nbTerme = 0;
        double totalTontine = 0;
        int nbTontine = 0;

        for (Models.Compte c : tousActifs) {
            switch (c.getTypeCompte()) {
                case "Courant" -> {
                    totalCourant += c.getSoldeActuel();
                    nbCourant++;
                }
                case "À terme" -> {
                    totalTerme += c.getSoldeActuel();
                    nbTerme++;
                }
                case "Tontine" -> {
                    totalTontine += c.getSoldeActuel();
                    nbTontine++;
                }
            }
        }

        lblCourantVal.setText(String.format("%,.0f F CFA", totalCourant));
        lblCourantSub.setText(nbCourant + " compte(s)");
        lblATermeVal.setText(String.format("%,.0f F CFA", totalTerme));
        lblATermeSub.setText(nbTerme + " compte(s)");
        lblTontineVal.setText(String.format("%,.0f F CFA", totalTontine));
        lblTontineSub.setText(nbTontine + " compte(s)");

        // Filtre et recherche
        String filtre = cmbFiltre.getSelectedItem().toString();
        String recherche = txtSearch.getText().trim().toLowerCase();

        java.util.List<Models.Compte> comptes
                = "Clôturés".equals(filtre)
                ? dao.listerCloturer()
                : dao.listerActifs();

        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) tableComptes.getModel();
        model.setRowCount(0);

        for (Models.Compte c : comptes) {
            // Filtre type
            if (!filtre.equals("Tous") && !filtre.equals("Clôturés")
                    && !c.getTypeCompte().equals(filtre)) {
                continue;
            }

            // Nom client — clientDao créé UNE SEULE FOIS avant la boucle
            Models.Client client = clientDao.chercher(c.getIdClient());
            String nomClient = client != null
                    ? client.getNom() + " " + client.getPrenom() : "—";

            // Filtre recherche
            if (!recherche.isEmpty()) {
                if (!nomClient.toLowerCase().contains(recherche)
                        && !c.getNumeroCompte().toLowerCase()
                                .contains(recherche)
                        && !c.getTypeCompte().toLowerCase()
                                .contains(recherche)) {
                    continue;
                }
            }

            model.addRow(new Object[]{
                c.getNumeroCompte(),
                nomClient,
                c.getTypeCompte(),
                String.format("%,.0f F CFA", c.getSoldeActuel()),
                c.getDateOuverture() != null
                ? new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(c.getDateOuverture()) : "—",
                c.getStatut(),
                ""
            });
        }
    }

    private void configurerColonneActions() {

        // Renderer — 3 boutons selon statut
        tableComptes.getColumn("Actions").setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel p = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 4, 6));
                    p.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);

                    String statut = table.getValueAt(row, 5).toString();
                    String type = table.getValueAt(row, 2).toString();

                    p.add(creerBoutonAction("/icons/eye.png",
                            new java.awt.Color(219, 234, 254)));

                    if (!"Clôturé".equals(statut)) {
                        p.add(creerBoutonAction("/icons/deposit.png",
                                new java.awt.Color(220, 252, 231)));
                        if (!"Tontine".equals(type)) {
                            p.add(creerBoutonAction("/icons/delete.png",
                                    new java.awt.Color(254, 226, 226)));
                        }
                    }
                    return p;
                });

        // Editor
        tableComptes.getColumn("Actions").setCellEditor(
                new javax.swing.DefaultCellEditor(
                        new javax.swing.JCheckBox()) {

            private javax.swing.JPanel panel;
            private int currentRow;
            private javax.swing.JTable tableRef;

            {
                panel = new javax.swing.JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.CENTER, 4, 6));
                panel.setBackground(new java.awt.Color(239, 246, 255));

                javax.swing.JButton bVoir = creerBoutonAction(
                        "/icons/eye.png",
                        new java.awt.Color(219, 234, 254));
                javax.swing.JButton bDepot = creerBoutonAction(
                        "/icons/deposit.png",
                        new java.awt.Color(220, 252, 231));
                javax.swing.JButton bCloturer = creerBoutonAction(
                        "/icons/delete.png",
                        new java.awt.Color(254, 226, 226));

                // 👁 Voir
                bVoir.addActionListener(e -> {
                    fireEditingStopped();
                    String numCompte = tableRef
                            .getValueAt(currentRow, 0).toString();
                    DAO.CompteDAO dao = new DAO.CompteDAO();
                    Models.Compte compte
                            = dao.chercherParNumero(numCompte);
                    if (compte != null) {
                        DetailCompte dialog = new DetailCompte(
                                (java.awt.Frame) javax.swing.SwingUtilities
                                        .getWindowAncestor(
                                                ComptePanel.this), true);
                        dialog.chargerCompte(compte.getIdCompte());
                        dialog.setVisible(true);
                        chargerComptes();
                    }
                });

                // + Dépôt rapide
                bDepot.addActionListener(e -> {
                    fireEditingStopped();
                    String numCompte = tableRef
                            .getValueAt(currentRow, 0).toString();
                    DAO.CompteDAO dao = new DAO.CompteDAO();
                    Models.Compte compte
                            = dao.chercherParNumero(numCompte);
                    if (compte == null) {
                        return;
                    }

                    String msg = "Montant du dépôt (F CFA) :";
                    if ("Tontine".equals(compte.getTypeCompte())
                            && compte.getMontantPeriodique() > 0) {
                        msg = String.format(
                                "Montant du dépôt (F CFA) :\n"
                                + "(Référence : %,.0f F CFA)",
                                compte.getMontantPeriodique());
                    }

                    String montantStr
                            = javax.swing.JOptionPane.showInputDialog(
                                    ComptePanel.this, msg, "Dépôt",
                                    javax.swing.JOptionPane.PLAIN_MESSAGE);

                    if (montantStr == null
                            || montantStr.trim().isEmpty()) {
                        return;
                    }

                    try {
                        double montant = Double.parseDouble(
                                montantStr.trim().replace(" ", ""));
                        if (montant <= 0) {
                            javax.swing.JOptionPane.showMessageDialog(
                                    ComptePanel.this,
                                    "Le montant doit être positif !",
                                    "Erreur",
                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if (montant > 10_000_000) {
                            javax.swing.JOptionPane.showMessageDialog(
                                    ComptePanel.this,
                                    "Le montant d'un dépôt ne peut pas dépasser 10 000 000 F CFA !",
                                    "Erreur",
                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        double nouveauSolde
                                = compte.getSoldeActuel() + montant;
                        int confirm = javax.swing.JOptionPane.showConfirmDialog(
    ComptePanel.this,
    String.format(
        "Confirmer le dépôt de %,.0f F CFA\nsur le compte %s ?",
        montant, compte.getNumeroCompte()),
    "Confirmation",
    javax.swing.JOptionPane.YES_NO_OPTION,
    javax.swing.JOptionPane.QUESTION_MESSAGE);
if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
                        dao.modifierSolde(compte.getIdCompte(),
                                nouveauSolde);

                        Models.Transaction t = new Models.Transaction();
                        t.setIdCompte(compte.getIdCompte());
                        t.setIdUser(DAO.Session.getUtilisateur()
                                .getIdUser());
                        t.setType("Dépôt épargne");
                        t.setMontant(montant);
                        t.setDateHeure(new java.sql.Timestamp(
                                System.currentTimeMillis()));
                        t.setStatut("Validé");
                        t.setMoyenPaiement("Espèces");
                        new DAO.TransactionDAO().ajouter(t);

                        javax.swing.JOptionPane.showMessageDialog(
                                ComptePanel.this,
                                String.format(
                                        "✔ Dépôt effectué !\n\n"
                                        + "   Montant  : %,.0f F CFA\n"
                                        + "   Nouveau solde : %,.0f F CFA",
                                        montant, nouveauSolde),
                                "Succès",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        chargerComptes();

                    } catch (NumberFormatException ex) {
                        javax.swing.JOptionPane.showMessageDialog(
                                ComptePanel.this,
                                "Montant invalide !",
                                "Erreur",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                });

                // 🔒 Clôturer
                bCloturer.addActionListener(e -> {
                    fireEditingStopped();
                    String numCompte = tableRef
                            .getValueAt(currentRow, 0).toString();
                    DAO.CompteDAO dao = new DAO.CompteDAO();
                    Models.Compte compte
                            = dao.chercherParNumero(numCompte);
                    if (compte == null) {
                        return;
                    }

                    // ── Vérifier prêt en cours ──
                    DAO.PretDAO pretDao = new DAO.PretDAO();
                    if (pretDao.aDejaUnPretEnCours(compte.getIdCompte())) {
                        javax.swing.JOptionPane.showMessageDialog(
                                ComptePanel.this,
                                "Impossible de clôturer ce compte !\n"
                                + "Il existe un prêt en cours lié à ce compte.",
                                "Clôture impossible",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // ── Vérifier solde restant ──
                    if (compte.getSoldeActuel() > 0) {
                        int choix = javax.swing.JOptionPane.showConfirmDialog(
                                ComptePanel.this,
                                String.format(
                                        "Ce compte a encore un solde de %,.0f F CFA.\n"
                                        + "Le client doit retirer son solde avant la clôture.\n\n"
                                        + "Continuer quand même ?",
                                        compte.getSoldeActuel()),
                                "Solde restant",
                                javax.swing.JOptionPane.YES_NO_OPTION,
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                        if (choix != javax.swing.JOptionPane.YES_OPTION) {
                            return;
                        }
                    }

                    // Ouvrir DetailCompte sur clôture directement
                    DetailCompte dialog = new DetailCompte(
                            (java.awt.Frame) javax.swing.SwingUtilities
                                    .getWindowAncestor(
                                            ComptePanel.this), true);
                    dialog.chargerCompte(compte.getIdCompte());
                    dialog.setVisible(true);
                    chargerComptes();
                });

                panel.add(bVoir);
                panel.add(bDepot);
                panel.add(bCloturer);
            }

            @Override
            public java.awt.Component getTableCellEditorComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, int row, int col) {
                currentRow = row;
                tableRef = table;

                String statut = table.getValueAt(row, 5).toString();
                String type = table.getValueAt(row, 2).toString();

                // Afficher/cacher selon statut et type
                panel.getComponent(1).setVisible(
                        !"Clôturé".equals(statut));
                panel.getComponent(2).setVisible(
                        !"Clôturé".equals(statut)
                        && !"Tontine".equals(type));

                return panel;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });

        tableComptes.getColumn("Actions").setPreferredWidth(120);
    }

    private void configurerRenderers() {
        // Centrer en-têtes
        ((javax.swing.table.DefaultTableCellRenderer) tableComptes.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Centrage colonnes sauf Actions
        javax.swing.table.DefaultTableCellRenderer centreur
                = new javax.swing.table.DefaultTableCellRenderer();
        centreur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        for (int i = 0; i < tableComptes.getColumnCount() - 1; i++) {
            tableComptes.getColumnModel().getColumn(i)
                    .setCellRenderer(centreur);
        }

        // Badge type — colonne 2
        tableComptes.getColumnModel().getColumn(2).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);

                    javax.swing.JLabel badge = new javax.swing.JLabel(
                            value != null ? value.toString() : "");
                    badge.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.PLAIN, 11));
                    badge.setOpaque(true);
                    badge.setBorder(javax.swing.BorderFactory
                            .createEmptyBorder(3, 10, 3, 10));

                    switch (value != null ? value.toString() : "") {
                        case "Courant" -> {
                            badge.setBackground(
                                    new java.awt.Color(219, 234, 254));
                            badge.setForeground(
                                    new java.awt.Color(14, 165, 233));
                        }
                        case "À terme" -> {
                            badge.setBackground(
                                    new java.awt.Color(220, 252, 231));
                            badge.setForeground(
                                    new java.awt.Color(21, 128, 61));
                        }
                        case "Tontine" -> {
                            badge.setBackground(
                                    new java.awt.Color(254, 243, 199));
                            badge.setForeground(
                                    new java.awt.Color(146, 64, 14));
                        }
                        default -> {
                            badge.setBackground(
                                    new java.awt.Color(241, 245, 249));
                            badge.setForeground(
                                    new java.awt.Color(100, 116, 139));
                        }
                    }
                    cell.add(badge);
                    return cell;
                });

        // Badge statut — colonne 5
        tableComptes.getColumnModel().getColumn(5).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);

                    javax.swing.JLabel badge = new javax.swing.JLabel(
                            value != null ? value.toString() : "");
                    badge.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.PLAIN, 11));
                    badge.setOpaque(true);
                    badge.setBorder(javax.swing.BorderFactory
                            .createEmptyBorder(3, 10, 3, 10));

                    switch (value != null ? value.toString() : "") {
                        case "Actif" -> {
                            badge.setBackground(
                                    new java.awt.Color(220, 252, 231));
                            badge.setForeground(
                                    new java.awt.Color(21, 128, 61));
                        }
                        case "Clôturé" -> {
                            badge.setBackground(
                                    new java.awt.Color(254, 226, 226));
                            badge.setForeground(
                                    new java.awt.Color(220, 38, 38));
                        }
                        default -> {
                            badge.setBackground(
                                    new java.awt.Color(241, 245, 249));
                            badge.setForeground(
                                    new java.awt.Color(100, 116, 139));
                        }
                    }
                    cell.add(badge);
                    return cell;
                });
    }

    private javax.swing.JButton creerBoutonAction(
            String iconPath, java.awt.Color bg) {
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
                    btn.setText("x");
                }
            }
        } catch (Exception e) {
            btn.setText("?");
        }
        btn.setBackground(bg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new java.awt.Dimension(32, 30));
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

        topPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerLeft = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        headerRight = new javax.swing.JPanel();
        btnNouveauCompte = new javax.swing.JButton();
        statsPanel = new javax.swing.JPanel();
        cardCourant = new javax.swing.JPanel();
        lblCourantTitle = new javax.swing.JLabel();
        lblCourantVal = new javax.swing.JLabel();
        lblCourantSub = new javax.swing.JLabel();
        cardTerme = new javax.swing.JPanel();
        lblATermeTitle = new javax.swing.JLabel();
        lblATermeVal = new javax.swing.JLabel();
        lblATermeSub = new javax.swing.JLabel();
        cardTontine = new javax.swing.JPanel();
        lblTontineTitle = new javax.swing.JLabel();
        lblTontineVal = new javax.swing.JLabel();
        lblTontineSub = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cmbFiltre = new javax.swing.JComboBox<>();
        scrollComptes = new javax.swing.JScrollPane();
        tableComptes = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        topPanel.setBackground(new java.awt.Color(248, 250, 252));
        topPanel.setPreferredSize(new java.awt.Dimension(0, 180));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBackground(new java.awt.Color(248, 250, 252));
        headerPanel.setAlignmentX(0.0F);
        headerPanel.setMaximumSize(new java.awt.Dimension(9999, 60));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerLeft.setBackground(new java.awt.Color(248, 250, 252));
        headerLeft.setLayout(new javax.swing.BoxLayout(headerLeft, javax.swing.BoxLayout.Y_AXIS));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(15, 23, 42));
        lblTitle.setText("Gestion de compte");
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        headerLeft.add(lblTitle);

        headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);

        headerRight.setBackground(new java.awt.Color(248, 250, 252));
        headerRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 14));

        btnNouveauCompte.setBackground(new java.awt.Color(14, 165, 233));
        btnNouveauCompte.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnNouveauCompte.setForeground(new java.awt.Color(255, 255, 255));
        btnNouveauCompte.setText("+ Nouveau compte");
        btnNouveauCompte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNouveauCompte.setFocusPainted(false);
        btnNouveauCompte.setPreferredSize(new java.awt.Dimension(155, 36));
        btnNouveauCompte.addActionListener(this::btnNouveauCompteActionPerformed);
        headerRight.add(btnNouveauCompte);

        headerPanel.add(headerRight, java.awt.BorderLayout.EAST);

        topPanel.add(headerPanel);

        statsPanel.setBackground(new java.awt.Color(248, 250, 252));
        statsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 8, 16));
        statsPanel.setAlignmentX(0.0F);
        statsPanel.setMaximumSize(new java.awt.Dimension(9999, 80));
        statsPanel.setPreferredSize(new java.awt.Dimension(0, 80));
        statsPanel.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        cardCourant.setBackground(new java.awt.Color(255, 255, 255));
        cardCourant.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(59, 130, 246))));
        cardCourant.setLayout(new javax.swing.BoxLayout(cardCourant, javax.swing.BoxLayout.Y_AXIS));

        lblCourantTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblCourantTitle.setText("Comptes courants");
        lblCourantTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 0));
        cardCourant.add(lblCourantTitle);

        lblCourantVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblCourantVal.setForeground(new java.awt.Color(15, 23, 42));
        lblCourantVal.setText("0 FCFA");
        lblCourantVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardCourant.add(lblCourantVal);

        lblCourantSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblCourantSub.setForeground(new java.awt.Color(100, 116, 139));
        lblCourantSub.setText("0 compte");
        lblCourantSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardCourant.add(lblCourantSub);

        statsPanel.add(cardCourant);

        cardTerme.setBackground(new java.awt.Color(255, 255, 255));
        cardTerme.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(34, 197, 94))));
        cardTerme.setLayout(new javax.swing.BoxLayout(cardTerme, javax.swing.BoxLayout.Y_AXIS));

        lblATermeTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblATermeTitle.setText("Comptes à terme");
        lblATermeTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 0));
        cardTerme.add(lblATermeTitle);

        lblATermeVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblATermeVal.setForeground(new java.awt.Color(15, 23, 42));
        lblATermeVal.setText("0 FCFA");
        lblATermeVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTerme.add(lblATermeVal);

        lblATermeSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblATermeSub.setForeground(new java.awt.Color(100, 116, 139));
        lblATermeSub.setText("0 compte");
        lblATermeSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTerme.add(lblATermeSub);

        statsPanel.add(cardTerme);

        cardTontine.setBackground(new java.awt.Color(255, 255, 255));
        cardTontine.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(245, 158, 11))));
        cardTontine.setLayout(new javax.swing.BoxLayout(cardTontine, javax.swing.BoxLayout.Y_AXIS));

        lblTontineTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblTontineTitle.setText("Tontines");
        lblTontineTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 0));
        cardTontine.add(lblTontineTitle);

        lblTontineVal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTontineVal.setForeground(new java.awt.Color(15, 23, 42));
        lblTontineVal.setText("0 FCFA");
        lblTontineVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTontine.add(lblTontineVal);

        lblTontineSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTontineSub.setForeground(new java.awt.Color(100, 116, 139));
        lblTontineSub.setText("0 compte");
        lblTontineSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        cardTontine.add(lblTontineSub);

        statsPanel.add(cardTontine);

        topPanel.add(statsPanel);

        searchPanel.setBackground(new java.awt.Color(248, 250, 252));
        searchPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 9, 0, 0));
        searchPanel.setAlignmentX(0.0F);
        searchPanel.setMaximumSize(new java.awt.Dimension(9999, 45));
        searchPanel.setPreferredSize(new java.awt.Dimension(0, 45));
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 5));

        txtSearch.setPreferredSize(new java.awt.Dimension(300, 34));
        searchPanel.add(txtSearch);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        btnSearch.setText("Rechercher");
        btnSearch.setPreferredSize(new java.awt.Dimension(120, 34));
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        searchPanel.add(btnSearch);

        cmbFiltre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous", "Courant", "À terme", "Tontine", "Clôturés", " " }));
        cmbFiltre.setPreferredSize(new java.awt.Dimension(120, 34));
        searchPanel.add(cmbFiltre);

        topPanel.add(searchPanel);

        add(topPanel, java.awt.BorderLayout.NORTH);

        scrollComptes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16));

        tableComptes.setBackground(new java.awt.Color(252, 252, 253));
        tableComptes.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tableComptes.setModel(new javax.swing.table.DefaultTableModel(
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
        tableComptes.setGridColor(new java.awt.Color(214, 245, 249));
        tableComptes.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tableComptes.setRowHeight(44);
        tableComptes.setSelectionBackground(new java.awt.Color(219, 234, 254));
        tableComptes.setSelectionForeground(new java.awt.Color(14, 165, 233));
        tableComptes.setShowGrid(true);
        scrollComptes.setViewportView(tableComptes);

        add(scrollComptes, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNouveauCompteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauCompteActionPerformed

    }//GEN-LAST:event_btnNouveauCompteActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNouveauCompte;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel cardCourant;
    private javax.swing.JPanel cardTerme;
    private javax.swing.JPanel cardTontine;
    private javax.swing.JComboBox<String> cmbFiltre;
    private javax.swing.JPanel headerLeft;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerRight;
    private javax.swing.JLabel lblATermeSub;
    private javax.swing.JLabel lblATermeTitle;
    private javax.swing.JLabel lblATermeVal;
    private javax.swing.JLabel lblCourantSub;
    private javax.swing.JLabel lblCourantTitle;
    private javax.swing.JLabel lblCourantVal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTontineSub;
    private javax.swing.JLabel lblTontineTitle;
    private javax.swing.JLabel lblTontineVal;
    private javax.swing.JScrollPane scrollComptes;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTable tableComptes;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
