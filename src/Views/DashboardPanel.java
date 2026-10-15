/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class DashboardPanel extends javax.swing.JPanel {

    /**
     * Creates new form DashboardPanel
     */
    public DashboardPanel() {
    initComponents();

    configurerAlerte();
    configurerDate();
    configurerBoutonActualiser();
    configurerTableauTransactions();
    configurerChartsPanel();
    configurerRaccourcis();
    configurerRendererTableau();
    configurerAutoRefresh();

    chargerDonnees();
    // ── Bouton tableau de bord imprimable ──
javax.swing.JButton btnEtatDash = new javax.swing.JButton("🖨 Imprimer tableau de bord");
btnEtatDash.setBackground(new java.awt.Color(219, 234, 254));
btnEtatDash.setForeground(new java.awt.Color(14, 165, 233));
btnEtatDash.setBorderPainted(false);
btnEtatDash.setFocusPainted(false);
btnEtatDash.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btnEtatDash.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
btnEtatDash.setPreferredSize(new java.awt.Dimension(220, 34));
btnEtatDash.addActionListener(e -> utils.EtatsHelperSmartCaisse.etatTableauDeBord());
raccourcisPanel.add(btnEtatDash);
raccourcisPanel.revalidate();
}

    private void configurerAlerte() {
    alertPanel.setVisible(false);
}

private void configurerDate() {
    lblDate.setText(new java.text.SimpleDateFormat(
            "EEEE dd MMMM yyyy",
            new java.util.Locale("fr", "FR"))
            .format(new java.util.Date()));
}

private void configurerBoutonActualiser() {
    btnRefresh.setBackground(new java.awt.Color(14, 165, 233));
    btnRefresh.setForeground(java.awt.Color.WHITE);
    btnRefresh.setBorderPainted(false);
    btnRefresh.setFocusPainted(false);
    btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnRefresh.addActionListener(e -> chargerDonnees());
    lblVoirTout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
}

private void configurerTableauTransactions() {
    tableTransactions.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Client", "Type", "Montant", "Date", "Statut"}
    ) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    });
    tableTransactions.setRowHeight(40);
    tableTransactions.getTableHeader().setReorderingAllowed(false);
    tableTransactions.setShowVerticalLines(false);
    tableTransactions.setShowHorizontalLines(true);
    tableTransactions.setGridColor(new java.awt.Color(243, 244, 246));
    tableTransactions.setBackground(java.awt.Color.WHITE);
    tableTransactions.setSelectionBackground(new java.awt.Color(239, 246, 255));
}

private void configurerChartsPanel() {
    chartsPanel.setLayout(new java.awt.GridLayout(1, 3, 12, 0));
    chartsPanel.setBorder(javax.swing.BorderFactory
            .createEmptyBorder(8, 16, 8, 16));
}

private void configurerAutoRefresh() {
    addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentShown(java.awt.event.ComponentEvent e) {
            chargerDonnees();
        }
    });
}
    private void configurerRaccourcis() {
        raccourcisPanel.setBackground(new java.awt.Color(248, 250, 252));
        raccourcisPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12));

        // Style boutons raccourcis
        Object[][] btns = {
            {btnNouveauClient, "➕ Nouveau client",
                new java.awt.Color(220, 252, 231), new java.awt.Color(21, 128, 61)},
            {btnNouveauCompte, "🏦 Nouveau compte",
                new java.awt.Color(219, 234, 254), new java.awt.Color(14, 165, 233)},
            {btnNouveauPret, "💰 Nouveau prêt",
                new java.awt.Color(254, 243, 199), new java.awt.Color(146, 64, 14)},
            {btnRemboursement, "💵 Remboursement",
                new java.awt.Color(254, 226, 226), new java.awt.Color(220, 38, 38)}
        };

        for (Object[] b : btns) {
            javax.swing.JButton btn = (javax.swing.JButton) b[0];
            btn.setText((String) b[1]);
            btn.setBackground((java.awt.Color) b[2]);
            btn.setForeground((java.awt.Color) b[3]);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btn.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
            btn.setPreferredSize(new java.awt.Dimension(160, 34));
        }

        // Actions raccourcis — ouvrir les dialogs directement
        btnNouveauClient.addActionListener(e -> {
            AjouterClient dialog = new AjouterClient(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(DashboardPanel.this), true);
            dialog.setVisible(true);
            chargerDonnees();
        });

        btnNouveauCompte.addActionListener(e -> {
            AjouterCompte dialog = new AjouterCompte(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(DashboardPanel.this), true);
            dialog.setVisible(true);
            chargerDonnees();
        });

        btnNouveauPret.addActionListener(e -> {
            AjouterPret dialog = new AjouterPret(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(DashboardPanel.this), true);
            dialog.setVisible(true);
            chargerDonnees();
        });

        btnRemboursement.addActionListener(e -> {
            RemboursementDialog dialog = new RemboursementDialog(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(DashboardPanel.this), true);
            dialog.setVisible(true);
            chargerDonnees();
        });
    }

    public void chargerDonnees() {
        lblDate.setText(new java.text.SimpleDateFormat("EEEE dd MMMM yyyy",
                new java.util.Locale("fr", "FR")).format(new java.util.Date()));

        // ── Clients actifs ──
        DAO.ClientDAO clientDao = new DAO.ClientDAO();
        java.util.List<Models.Client> clients = clientDao.listerTous();
        long nbActifs = clients.stream()
                .filter(c -> "Actif".equals(c.getStatut())).count();

        // Nouveaux ce mois
        java.util.Calendar calMois = java.util.Calendar.getInstance();
        int moisCourant = calMois.get(java.util.Calendar.MONTH);
        int anneeCourante = calMois.get(java.util.Calendar.YEAR);
        long nouveauxMois = clients.stream().filter(c -> {
            if (c.getDateInscription() == null) {
                return false;
            }
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(c.getDateInscription());
            return cal.get(java.util.Calendar.MONTH) == moisCourant
                    && cal.get(java.util.Calendar.YEAR) == anneeCourante;
        }).count();

        lblClientsVal.setText(String.valueOf(nbActifs));
        lblClientsSub.setForeground(new java.awt.Color(34, 197, 94));
        lblClientsSub.setText(nouveauxMois > 0
                ? "+" + nouveauxMois + " ce mois" : "Aucun nouveau ce mois");

        // ── Épargne ──
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        java.util.List<Models.Compte> comptes = compteDao.listerActifs();
        double totalEpargne = comptes.stream()
                .mapToDouble(Models.Compte::getSoldeActuel).sum();
        lblEpargneVal.setText(String.format("%,.0f F CFA", totalEpargne));
        lblEpargneSub.setText(comptes.size() + " compte(s) actif(s)");

        // ── Prêts ──
        new DAO.PretDAO().mettreAJourStatuts();
        DAO.PretDAO pretDao = new DAO.PretDAO();
        java.util.List<Models.Pret> tousPrets = pretDao.listerTous();

        int nbEnCours = 0, nbRetard = 0;
        double totalPrets = 0, montantRetard = 0;

        for (Models.Pret p : tousPrets) {
            if ("En cours".equals(p.getStatut())) {
                nbEnCours++;
                totalPrets += p.getMontantPrincipal();
            }
            if ("En retard".equals(p.getStatut())) {
                nbRetard++;
                double interets = p.getMontantPrincipal()
                        * p.getTauxInteret() * p.getDureeMois() / 1200.0;
                montantRetard += (p.getMontantPrincipal() + interets)
                        - p.getMontantRembourse();
            }
        }

        lblPretsVal.setText(String.valueOf(nbEnCours));
        lblPrestSub.setText(String.format("%,.0f F CFA", totalPrets));

        lblRetardVal.setText(String.valueOf(nbRetard));
        lblRetardVal.setForeground(nbRetard > 0
                ? new java.awt.Color(220, 38, 38)
                : new java.awt.Color(21, 128, 61));
        lblReatrdSub.setText(nbRetard > 0
                ? String.format("%,.0f F à récupérer", montantRetard)
                : "Aucun retard ✔");
        lblReatrdSub.setForeground(nbRetard > 0
                ? new java.awt.Color(220, 38, 38)
                : new java.awt.Color(21, 128, 61));

        // ── Capital total ──
        double capital = totalEpargne + totalPrets;
        lblCapitalVal.setText(String.format("%,.0f F CFA", capital));
        lblCapitalSub.setText("Épargne + Prêts en cours");

        // ── Alerte prêts en retard ──
        if (nbRetard > 0) {
            alertPanel.setVisible(true);
            lblAlerte.setText("  ⚠ " + nbRetard
                    + " prêt(s) en retard — Action requise !");
            lblAlerte.setFont(new java.awt.Font("Segoe UI Emoji",
                    java.awt.Font.BOLD, 12));
            lblAlerte.setForeground(new java.awt.Color(153, 27, 27));
            alertPanel.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(0, 16, 0, 0));
        } else {
            alertPanel.setVisible(false);
        }

        chargerTopEpargnants();
        chargerPretsEnRetard();
        chargerProchainesEcheances();
        chargerDernieresTransactions();
    }

    private void chargerDernieresTransactions() {
        DAO.TransactionDAO dao = new DAO.TransactionDAO();
        java.util.List<Models.Transaction> transactions = dao.listerDernieres();

        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) tableTransactions.getModel();
        model.setRowCount(0);

        // DAO créés UNE SEULE FOIS avant la boucle
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        DAO.ClientDAO clientDao = new DAO.ClientDAO();

        for (Models.Transaction t : transactions) {
            Models.Compte compte = compteDao.chercher(t.getIdCompte());
            String nomClient = "—";
            if (compte != null) {
                Models.Client client = clientDao.chercher(compte.getIdClient());
                if (client != null) {
                    nomClient = client.getNom() + " " + client.getPrenom();
                }
            }

            String typeLow = t.getType().toLowerCase();
            boolean estEntree = typeLow.contains("dépôt")
                    || typeLow.contains("depot")
                    || typeLow.contains("remboursement")
                    || typeLow.contains("intérêt");

            String montantStr = (estEntree ? "+ " : "- ")
                    + String.format("%,.0f F CFA", t.getMontant());

            String dateStr = t.getDateHeure() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(t.getDateHeure()) : "—";

            model.addRow(new Object[]{
                nomClient, t.getType(), montantStr, dateStr, t.getStatut()
            });
        }
    }

    private void chargerTopEpargnants() {
        fluxPanel.removeAll();
        fluxPanel.setLayout(new java.awt.BorderLayout());
        fluxPanel.setBackground(java.awt.Color.WHITE);
        fluxPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        javax.swing.JLabel titre = new javax.swing.JLabel("🏆 Top Épargnants");
        titre.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
        titre.setForeground(new java.awt.Color(180, 120, 0));
        titre.setPreferredSize(new java.awt.Dimension(0, 32));
        titre.setBorder(javax.swing.BorderFactory.createMatteBorder(
                0, 0, 1, 0, new java.awt.Color(243, 244, 246)));
        fluxPanel.add(titre, java.awt.BorderLayout.NORTH);

        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        java.util.List<Models.Compte> comptes = compteDao.listerActifs();

        java.util.Map<Integer, Double> soldesParClient = new java.util.HashMap<>();
        for (Models.Compte c : comptes) {
            soldesParClient.merge(c.getIdClient(), c.getSoldeActuel(), Double::sum);
        }

        java.util.List<java.util.Map.Entry<Integer, Double>> top3
                = soldesParClient.entrySet().stream()
                        .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                        .limit(3)
                        .collect(java.util.stream.Collectors.toList());

        javax.swing.JPanel listPanel = new javax.swing.JPanel();
        listPanel.setLayout(new javax.swing.BoxLayout(
                listPanel, javax.swing.BoxLayout.Y_AXIS));
        listPanel.setBackground(java.awt.Color.WHITE);

        if (top3.isEmpty()) {
            javax.swing.JLabel vide = new javax.swing.JLabel(
                    "Aucun compte épargne");
            vide.setFont(new java.awt.Font("Segoe UI Emoji",
                    java.awt.Font.ITALIC, 12));
            vide.setForeground(new java.awt.Color(203, 213, 225));
            vide.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            vide.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(20, 0, 0, 0));
            listPanel.add(vide);
        } else {
            String[] rangs = {"🥇 #1", "🥈 #2", "🥉 #3"};
            java.awt.Color[] rangColors = {
                new java.awt.Color(217, 119, 6),
                new java.awt.Color(100, 116, 139),
                new java.awt.Color(120, 53, 15)
            };
            DAO.ClientDAO clientDao = new DAO.ClientDAO();

            for (int i = 0; i < top3.size(); i++) {
                java.util.Map.Entry<Integer, Double> entry = top3.get(i);
                Models.Client client = clientDao.chercher(entry.getKey());
                String nomClient = client != null
                        ? client.getNom() + " " + client.getPrenom() : "—";

                javax.swing.JPanel row = new javax.swing.JPanel(
                        new java.awt.BorderLayout());
                row.setBackground(i % 2 == 0
                        ? new java.awt.Color(250, 250, 250)
                        : java.awt.Color.WHITE);
                row.setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(8, 6, 8, 6));
                row.setMaximumSize(new java.awt.Dimension(9999, 42));

                javax.swing.JPanel leftPart = new javax.swing.JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.LEFT, 6, 0));
                leftPart.setBackground(row.getBackground());

                javax.swing.JLabel lblRang = new javax.swing.JLabel(rangs[i]);
                lblRang.setFont(new java.awt.Font("Segoe UI Emoji",
                        java.awt.Font.BOLD, 11));
                lblRang.setForeground(rangColors[i]);

                javax.swing.JLabel lblNom = new javax.swing.JLabel(nomClient);
                lblNom.setFont(new java.awt.Font("Segoe UI Emoji",
                        java.awt.Font.PLAIN, 12));
                lblNom.setForeground(new java.awt.Color(15, 23, 42));

                leftPart.add(lblRang);
                leftPart.add(lblNom);

                javax.swing.JLabel lblSolde = new javax.swing.JLabel(
                        String.format("%,.0f F CFA", entry.getValue()));
                lblSolde.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.BOLD, 12));
                lblSolde.setForeground(new java.awt.Color(21, 128, 61));
                lblSolde.setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(0, 0, 0, 4));

                row.add(leftPart, java.awt.BorderLayout.WEST);
                row.add(lblSolde, java.awt.BorderLayout.EAST);
                listPanel.add(row);
            }
        }

        fluxPanel.add(listPanel, java.awt.BorderLayout.CENTER);
        fluxPanel.revalidate();
        fluxPanel.repaint();
    }

    private void chargerPretsEnRetard() {
        piePanel.removeAll();
        piePanel.setLayout(new java.awt.BorderLayout());
        piePanel.setBackground(java.awt.Color.WHITE);
        piePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        javax.swing.JLabel titre = new javax.swing.JLabel(
                "🚨 Prêts critiques");
        titre.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
        titre.setForeground(new java.awt.Color(220, 38, 38));
        titre.setPreferredSize(new java.awt.Dimension(0, 32));
        titre.setBorder(javax.swing.BorderFactory.createMatteBorder(
                0, 0, 1, 0, new java.awt.Color(243, 244, 246)));
        piePanel.add(titre, java.awt.BorderLayout.NORTH);

        DAO.PretDAO pretDao = new DAO.PretDAO();
        java.util.List<Models.Pret> tousPrets = pretDao.listerTous();

        java.util.List<Models.Pret> enRetard = tousPrets.stream()
                .filter(p -> "En retard".equals(p.getStatut()))
                .sorted((a, b) -> {
                    double resteA = a.getMontantPrincipal()
                            + a.getMontantPrincipal()
                            * a.getTauxInteret() * a.getDureeMois() / 1200.0
                            - a.getMontantRembourse();
                    double resteB = b.getMontantPrincipal()
                            + b.getMontantPrincipal()
                            * b.getTauxInteret() * b.getDureeMois() / 1200.0
                            - b.getMontantRembourse();
                    return Double.compare(resteB, resteA);
                })
                .limit(3)
                .collect(java.util.stream.Collectors.toList());

        javax.swing.JPanel listPanel = new javax.swing.JPanel();
        listPanel.setLayout(new javax.swing.BoxLayout(
                listPanel, javax.swing.BoxLayout.Y_AXIS));
        listPanel.setBackground(java.awt.Color.WHITE);

        if (enRetard.isEmpty()) {
            javax.swing.JPanel okPanel = new javax.swing.JPanel(
                    new java.awt.BorderLayout());
            okPanel.setBackground(new java.awt.Color(240, 253, 244));
            okPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(
                            new java.awt.Color(187, 247, 208)),
                    javax.swing.BorderFactory.createEmptyBorder(
                            14, 12, 14, 12)));
            okPanel.setMaximumSize(new java.awt.Dimension(9999, 60));

            javax.swing.JLabel lblOk = new javax.swing.JLabel(
                    "✔ Aucun prêt en retard !");
            lblOk.setFont(new java.awt.Font("Segoe UI Emoji",
                    java.awt.Font.BOLD, 12));
            lblOk.setForeground(new java.awt.Color(21, 128, 61));
            lblOk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            okPanel.add(lblOk, java.awt.BorderLayout.CENTER);
            listPanel.add(okPanel);
        } else {
            DAO.CompteDAO compteDao = new DAO.CompteDAO();
            DAO.ClientDAO clientDao = new DAO.ClientDAO();

            for (int i = 0; i < enRetard.size(); i++) {
                Models.Pret p = enRetard.get(i);
                Models.Compte compte = compteDao.chercher(p.getIdCompte());
                String nomClient = "—";
                if (compte != null) {
                    Models.Client client
                            = clientDao.chercher(compte.getIdClient());
                    if (client != null) {
                        nomClient = client.getNom() + " "
                                + client.getPrenom();
                    }
                }

                // Calcul correct des intérêts
                double interets = p.getMontantPrincipal()
                        * p.getTauxInteret() * p.getDureeMois() / 1200.0;
                double reste = (p.getMontantPrincipal() + interets)
                        - p.getMontantRembourse();
                double penalite = p.getPenalite();

                javax.swing.JPanel row = new javax.swing.JPanel(
                        new java.awt.BorderLayout());
                row.setBackground(java.awt.Color.WHITE);
                row.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createMatteBorder(
                                0, 3, 0, 0, new java.awt.Color(220, 38, 38)),
                        javax.swing.BorderFactory.createEmptyBorder(
                                6, 8, 6, 6)));
                row.setMaximumSize(new java.awt.Dimension(9999, 52));

                javax.swing.JPanel left = new javax.swing.JPanel();
                left.setLayout(new javax.swing.BoxLayout(
                        left, javax.swing.BoxLayout.Y_AXIS));
                left.setBackground(java.awt.Color.WHITE);

                javax.swing.JLabel lblNom = new javax.swing.JLabel(nomClient);
                lblNom.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.PLAIN, 12));
                lblNom.setForeground(new java.awt.Color(15, 23, 42));

                javax.swing.JLabel lblPenalite = new javax.swing.JLabel(
                        penalite > 0
                                ? String.format("Pénalité : %,.0f F", penalite)
                                : "En retard");
                lblPenalite.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.PLAIN, 10));
                lblPenalite.setForeground(new java.awt.Color(220, 38, 38));

                left.add(lblNom);
                left.add(lblPenalite);

                javax.swing.JLabel lblReste = new javax.swing.JLabel(
                        String.format("%,.0f F", reste));
                lblReste.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.BOLD, 12));
                lblReste.setForeground(new java.awt.Color(220, 38, 38));

                row.add(left, java.awt.BorderLayout.WEST);
                row.add(lblReste, java.awt.BorderLayout.EAST);
                listPanel.add(row);

                if (i < enRetard.size() - 1) {
                    javax.swing.JSeparator sep = new javax.swing.JSeparator();
                    sep.setForeground(new java.awt.Color(243, 244, 246));
                    sep.setMaximumSize(new java.awt.Dimension(9999, 1));
                    listPanel.add(sep);
                }
            }
        }

        piePanel.add(listPanel, java.awt.BorderLayout.CENTER);
        piePanel.revalidate();
        piePanel.repaint();
    }

    private void chargerProchainesEcheances() {
        javax.swing.JPanel echeancesPanel;
        if (chartsPanel.getComponentCount() >= 3) {
            echeancesPanel = (javax.swing.JPanel) chartsPanel.getComponent(2);
            echeancesPanel.removeAll();
        } else {
            echeancesPanel = new javax.swing.JPanel();
            chartsPanel.add(echeancesPanel);
        }

        echeancesPanel.setLayout(new java.awt.BorderLayout());
        echeancesPanel.setBackground(java.awt.Color.WHITE);
        echeancesPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(226, 232, 240)),
                javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        javax.swing.JLabel titre = new javax.swing.JLabel(
                "📅 Prochaines échéances");
        titre.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
        titre.setForeground(new java.awt.Color(14, 165, 233));
        titre.setPreferredSize(new java.awt.Dimension(0, 32));
        titre.setBorder(javax.swing.BorderFactory.createMatteBorder(
                0, 0, 1, 0, new java.awt.Color(243, 244, 246)));
        echeancesPanel.add(titre, java.awt.BorderLayout.NORTH);

        DAO.PretDAO pretDao = new DAO.PretDAO();
        java.util.List<Models.Pret> tousPrets = pretDao.listerTous();

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate dans30Jours = today.plusDays(30);

        java.util.List<Models.Pret> echeances = tousPrets.stream()
                .filter(p -> {
                    if (!"En cours".equals(p.getStatut())) {
                        return false;
                    }
                    if (p.getDateFinPrevue() == null) {
                        return false;
                    }
                    java.time.LocalDate fin
                            = p.getDateFinPrevue().toLocalDate();
                    return !fin.isBefore(today)
                            && !fin.isAfter(dans30Jours);
                })
                .sorted((a, b) -> a.getDateFinPrevue()
                .compareTo(b.getDateFinPrevue()))
                .limit(3)
                .collect(java.util.stream.Collectors.toList());

        javax.swing.JPanel listPanel = new javax.swing.JPanel();
        listPanel.setLayout(new javax.swing.BoxLayout(
                listPanel, javax.swing.BoxLayout.Y_AXIS));
        listPanel.setBackground(java.awt.Color.WHITE);

        if (echeances.isEmpty()) {
            javax.swing.JLabel vide = new javax.swing.JLabel(
                    "Aucune échéance dans 30 jours");
            vide.setFont(new java.awt.Font("Segoe UI Emoji",
                    java.awt.Font.ITALIC, 12));
            vide.setForeground(new java.awt.Color(203, 213, 225));
            vide.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            vide.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(20, 0, 0, 0));
            listPanel.add(vide);
        } else {
            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("dd/MM/yyyy");
            DAO.CompteDAO compteDao = new DAO.CompteDAO();
            DAO.ClientDAO clientDao = new DAO.ClientDAO();

            for (int i = 0; i < echeances.size(); i++) {
                Models.Pret p = echeances.get(i);
                Models.Compte compte = compteDao.chercher(p.getIdCompte());
                String nomClient = "—";
                if (compte != null) {
                    Models.Client client
                            = clientDao.chercher(compte.getIdClient());
                    if (client != null) {
                        nomClient = client.getNom() + " "
                                + client.getPrenom();
                    }
                }

                long joursRestants = today.until(
                        p.getDateFinPrevue().toLocalDate(),
                        java.time.temporal.ChronoUnit.DAYS);

                javax.swing.JPanel row = new javax.swing.JPanel(
                        new java.awt.BorderLayout());
                row.setBackground(i % 2 == 0
                        ? new java.awt.Color(240, 249, 255)
                        : java.awt.Color.WHITE);
                row.setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(7, 6, 7, 6));
                row.setMaximumSize(new java.awt.Dimension(9999, 50));

                javax.swing.JPanel leftPart = new javax.swing.JPanel();
                leftPart.setLayout(new javax.swing.BoxLayout(
                        leftPart, javax.swing.BoxLayout.Y_AXIS));
                leftPart.setBackground(row.getBackground());

                javax.swing.JLabel lblNom
                        = new javax.swing.JLabel(nomClient);
                lblNom.setFont(new java.awt.Font("Segoe UI Emoji",
                        java.awt.Font.PLAIN, 12));
                lblNom.setForeground(new java.awt.Color(15, 23, 42));

                javax.swing.JLabel lblJours = new javax.swing.JLabel(
                        "Dans " + joursRestants + " jour(s)");
                lblJours.setFont(new java.awt.Font("Segoe UI Emoji",
                        java.awt.Font.PLAIN, 11));
                lblJours.setForeground(joursRestants <= 7
                        ? new java.awt.Color(220, 38, 38)
                        : new java.awt.Color(100, 116, 139));

                leftPart.add(lblNom);
                leftPart.add(lblJours);

                javax.swing.JLabel lblDateEch = new javax.swing.JLabel(
                        sdf.format(p.getDateFinPrevue()));
                lblDateEch.setFont(new java.awt.Font("Segoe UI Emoji",
                        java.awt.Font.BOLD, 11));
                lblDateEch.setForeground(new java.awt.Color(146, 64, 14));
                lblDateEch.setOpaque(true);
                lblDateEch.setBackground(new java.awt.Color(254, 243, 199));
                lblDateEch.setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(4, 8, 4, 8));

                row.add(leftPart, java.awt.BorderLayout.WEST);
                row.add(lblDateEch, java.awt.BorderLayout.EAST);
                listPanel.add(row);

                if (i < echeances.size() - 1) {
                    javax.swing.JSeparator sep = new javax.swing.JSeparator();
                    sep.setForeground(new java.awt.Color(243, 244, 246));
                    sep.setMaximumSize(new java.awt.Dimension(9999, 1));
                    listPanel.add(sep);
                }
            }
        }

        echeancesPanel.add(listPanel, java.awt.BorderLayout.CENTER);
        echeancesPanel.revalidate();
        echeancesPanel.repaint();
        chartsPanel.revalidate();
        chartsPanel.repaint();
    }

    private void configurerRendererTableau() {
        tableTransactions.getTableHeader().setBackground(
                new java.awt.Color(241, 245, 249));
        tableTransactions.getTableHeader().setForeground(
                new java.awt.Color(71, 85, 105));
        tableTransactions.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tableTransactions.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 38));

        // Badge Type — colonne 1
        tableTransactions.getColumnModel().getColumn(1).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 8, 8));
                    cell.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    if (value != null) {
                        javax.swing.JLabel badge
                        = new javax.swing.JLabel(value.toString());
                        badge.setOpaque(true);
                        badge.setFont(new java.awt.Font("Segoe UI",
                                java.awt.Font.PLAIN, 11));
                        badge.setBorder(javax.swing.BorderFactory
                                .createEmptyBorder(3, 10, 3, 10));
                        String v = value.toString().toLowerCase();
                        if (v.contains("dépôt") || v.contains("depot")) {
                            badge.setBackground(
                                    new java.awt.Color(220, 252, 231));
                            badge.setForeground(
                                    new java.awt.Color(21, 128, 61));
                        } else if (v.contains("retrait")) {
                            badge.setBackground(
                                    new java.awt.Color(254, 226, 226));
                            badge.setForeground(
                                    new java.awt.Color(220, 38, 38));
                        } else if (v.contains("remboursement")) {
                            badge.setBackground(
                                    new java.awt.Color(254, 243, 199));
                            badge.setForeground(
                                    new java.awt.Color(146, 64, 14));
                        } else if (v.contains("décaissement")
                        || v.contains("decaissement")) {
                            badge.setBackground(
                                    new java.awt.Color(219, 234, 254));
                            badge.setForeground(
                                    new java.awt.Color(14, 165, 233));
                        } else {
                            badge.setBackground(
                                    new java.awt.Color(241, 245, 249));
                            badge.setForeground(
                                    new java.awt.Color(100, 116, 139));
                        }
                        cell.add(badge);
                    }
                    return cell;
                });

        // Montant coloré — colonne 2
        tableTransactions.getColumnModel().getColumn(2).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JLabel lbl = new javax.swing.JLabel(
                            value != null ? value.toString() : "");
                    lbl.setOpaque(true);
                    lbl.setHorizontalAlignment(
                            javax.swing.SwingConstants.CENTER);
                    lbl.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.BOLD, 12));
                    lbl.setBackground(isSelected
                            ? new java.awt.Color(239, 246, 255)
                            : java.awt.Color.WHITE);
                    if (lbl.getText().startsWith("+")) {
                        lbl.setForeground(new java.awt.Color(21, 128, 61));
                    } else if (lbl.getText().startsWith("-")) {
                        lbl.setForeground(new java.awt.Color(220, 38, 38));
                    }
                    return lbl;
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

        headerPanel = new javax.swing.JPanel();
        headerRight = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        dashContent = new javax.swing.JPanel();
        titleGroup = new javax.swing.JPanel();
        lblTitre = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        alertPanel = new javax.swing.JPanel();
        lblAlerte = new javax.swing.JLabel();
        kpiPanel = new javax.swing.JPanel();
        cardCapital = new javax.swing.JPanel();
        lblCapitalVal = new javax.swing.JLabel();
        lblCapitalSub = new javax.swing.JLabel();
        cardCapitalTitle = new javax.swing.JPanel();
        lblCapitalTitle = new javax.swing.JLabel();
        cardCapitalIconBg = new javax.swing.JPanel();
        lblCapitalIcon = new javax.swing.JLabel();
        cardClients = new javax.swing.JPanel();
        lblClientsVal = new javax.swing.JLabel();
        lblClientsSub = new javax.swing.JLabel();
        cardClientsTitle = new javax.swing.JPanel();
        lblClientsTitle = new javax.swing.JLabel();
        cardClientsIconBg = new javax.swing.JPanel();
        lblClientsIcon = new javax.swing.JLabel();
        cardPrets = new javax.swing.JPanel();
        lblPretsVal = new javax.swing.JLabel();
        lblPrestSub = new javax.swing.JLabel();
        cardPretsTitle = new javax.swing.JPanel();
        lblPretsTitle = new javax.swing.JLabel();
        cardPretsIconBg = new javax.swing.JPanel();
        lblPretsIcon = new javax.swing.JLabel();
        cardRetard = new javax.swing.JPanel();
        lblRetardVal = new javax.swing.JLabel();
        lblReatrdSub = new javax.swing.JLabel();
        cardRetardTitle = new javax.swing.JPanel();
        lblRetardTitle = new javax.swing.JLabel();
        cardRetardIconBg = new javax.swing.JPanel();
        lblRetardIcon = new javax.swing.JLabel();
        cardEpargne = new javax.swing.JPanel();
        lblEpargneVal = new javax.swing.JLabel();
        lblEpargneSub = new javax.swing.JLabel();
        cardEpargneTitle = new javax.swing.JPanel();
        lblEpargneTitle = new javax.swing.JLabel();
        cardEpargneIconBg = new javax.swing.JPanel();
        lblEpargneIcon = new javax.swing.JLabel();
        raccourcisPanel = new javax.swing.JPanel();
        btnNouveauClient = new javax.swing.JButton();
        btnNouveauCompte = new javax.swing.JButton();
        btnNouveauPret = new javax.swing.JButton();
        btnRemboursement = new javax.swing.JButton();
        chartsPanel = new javax.swing.JPanel();
        fluxPanel = new javax.swing.JPanel();
        piePanel = new javax.swing.JPanel();
        tablePanel = new javax.swing.JPanel();
        tableHeader = new javax.swing.JPanel();
        lblTableTitle = new javax.swing.JLabel();
        lblVoirTout = new javax.swing.JLabel();
        scrollTransactions = new javax.swing.JScrollPane();
        tableTransactions = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(248, 250, 252));
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 50));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerRight.setBackground(new java.awt.Color(255, 255, 255));
        headerRight.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 16));
        headerRight.setPreferredSize(new java.awt.Dimension(160, 36));
        headerRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDate.setForeground(new java.awt.Color(100, 100, 100));
        lblDate.setText("18/04/2026");
        headerRight.add(lblDate);

        btnRefresh.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnRefresh.setText("Actualiser");
        headerRight.add(btnRefresh);

        headerPanel.add(headerRight, java.awt.BorderLayout.EAST);

        scrollPane.setBackground(new java.awt.Color(248, 249, 250));

        dashContent.setBackground(new java.awt.Color(248, 250, 252));
        dashContent.setLayout(new javax.swing.BoxLayout(dashContent, javax.swing.BoxLayout.Y_AXIS));

        titleGroup.setBackground(new java.awt.Color(247, 247, 242));
        titleGroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 12, 0));
        titleGroup.setMaximumSize(new java.awt.Dimension(9999, 80));
        titleGroup.setPreferredSize(new java.awt.Dimension(0, 80));
        titleGroup.setLayout(new javax.swing.BoxLayout(titleGroup, javax.swing.BoxLayout.Y_AXIS));

        lblTitre.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitre.setForeground(new java.awt.Color(15, 23, 42));
        lblTitre.setText("Tableau de bord");
        titleGroup.add(lblTitre);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(100, 116, 139));
        jLabel2.setText("Vue d'ensemble de l'activité");
        titleGroup.add(jLabel2);

        dashContent.add(titleGroup);

        alertPanel.setBackground(new java.awt.Color(254, 226, 226));
        alertPanel.setAlignmentX(0.0F);
        alertPanel.setMaximumSize(new java.awt.Dimension(9999, 40));
        alertPanel.setLayout(new java.awt.BorderLayout());

        lblAlerte.setText("label");
        alertPanel.add(lblAlerte, java.awt.BorderLayout.CENTER);

        dashContent.add(alertPanel);

        kpiPanel.setBackground(new java.awt.Color(247, 247, 242));
        kpiPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 8, 16));
        kpiPanel.setAlignmentX(0.0F);
        kpiPanel.setMaximumSize(new java.awt.Dimension(9999, 130));
        kpiPanel.setMinimumSize(new java.awt.Dimension(750, 110));
        kpiPanel.setPreferredSize(new java.awt.Dimension(0, 130));
        kpiPanel.setLayout(new java.awt.GridLayout(1, 5, 12, 0));

        cardCapital.setBackground(new java.awt.Color(255, 255, 255));
        cardCapital.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(59, 130, 246))));
        cardCapital.setMinimumSize(new java.awt.Dimension(150, 110));
        cardCapital.setLayout(new java.awt.BorderLayout());

        lblCapitalVal.setBackground(new java.awt.Color(15, 23, 42));
        lblCapitalVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCapitalVal.setForeground(new java.awt.Color(15, 23, 42));
        lblCapitalVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCapitalVal.setText("5 575 000 F");
        lblCapitalVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblCapitalVal.setMaximumSize(new java.awt.Dimension(200, 33));
        cardCapital.add(lblCapitalVal, java.awt.BorderLayout.CENTER);

        lblCapitalSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblCapitalSub.setForeground(new java.awt.Color(100, 116, 139));
        lblCapitalSub.setText("Prêts + Épargne");
        lblCapitalSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblCapitalSub.setPreferredSize(new java.awt.Dimension(0, 24));
        cardCapital.add(lblCapitalSub, java.awt.BorderLayout.SOUTH);

        cardCapitalTitle.setBackground(new java.awt.Color(255, 255, 255));
        cardCapitalTitle.setPreferredSize(new java.awt.Dimension(0, 50));
        cardCapitalTitle.setLayout(new java.awt.BorderLayout());

        lblCapitalTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblCapitalTitle.setText("Capital total");
        lblCapitalTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblCapitalTitle.setPreferredSize(new java.awt.Dimension(120, 30));
        cardCapitalTitle.add(lblCapitalTitle, java.awt.BorderLayout.CENTER);

        cardCapitalIconBg.setBackground(new java.awt.Color(219, 234, 254));
        cardCapitalIconBg.setPreferredSize(new java.awt.Dimension(36, 36));

        lblCapitalIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/money-bag.png"))); // NOI18N
        lblCapitalIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
        cardCapitalIconBg.add(lblCapitalIcon);

        cardCapitalTitle.add(cardCapitalIconBg, java.awt.BorderLayout.WEST);

        cardCapital.add(cardCapitalTitle, java.awt.BorderLayout.NORTH);

        kpiPanel.add(cardCapital);

        cardClients.setBackground(new java.awt.Color(255, 255, 255));
        cardClients.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(34, 197, 94))));
        cardClients.setMinimumSize(new java.awt.Dimension(150, 110));
        cardClients.setLayout(new java.awt.BorderLayout());

        lblClientsVal.setBackground(new java.awt.Color(15, 23, 42));
        lblClientsVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblClientsVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblClientsVal.setText("124");
        lblClientsVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardClients.add(lblClientsVal, java.awt.BorderLayout.CENTER);

        lblClientsSub.setBackground(new java.awt.Color(34, 197, 94));
        lblClientsSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblClientsSub.setForeground(new java.awt.Color(34, 197, 94));
        lblClientsSub.setText("+3 ce mois");
        lblClientsSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblClientsSub.setPreferredSize(new java.awt.Dimension(0, 24));
        cardClients.add(lblClientsSub, java.awt.BorderLayout.SOUTH);

        cardClientsTitle.setBackground(new java.awt.Color(255, 255, 255));
        cardClientsTitle.setPreferredSize(new java.awt.Dimension(0, 50));
        cardClientsTitle.setLayout(new java.awt.BorderLayout());

        lblClientsTitle.setBackground(new java.awt.Color(100, 116, 139));
        lblClientsTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblClientsTitle.setText("Clients actifs");
        lblClientsTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardClientsTitle.add(lblClientsTitle, java.awt.BorderLayout.CENTER);

        cardClientsIconBg.setBackground(new java.awt.Color(220, 252, 231));
        cardClientsIconBg.setPreferredSize(new java.awt.Dimension(36, 36));

        lblClientsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/group.png"))); // NOI18N
        lblClientsIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
        cardClientsIconBg.add(lblClientsIcon);

        cardClientsTitle.add(cardClientsIconBg, java.awt.BorderLayout.WEST);

        cardClients.add(cardClientsTitle, java.awt.BorderLayout.NORTH);

        kpiPanel.add(cardClients);

        cardPrets.setBackground(new java.awt.Color(255, 255, 255));
        cardPrets.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(59, 130, 246))));
        cardPrets.setMinimumSize(new java.awt.Dimension(150, 110));
        cardPrets.setLayout(new java.awt.BorderLayout());

        lblPretsVal.setBackground(new java.awt.Color(15, 23, 42));
        lblPretsVal.setForeground(new java.awt.Color(15, 23, 42));
        lblPretsVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPretsVal.setText("38");
        lblPretsVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardPrets.add(lblPretsVal, java.awt.BorderLayout.CENTER);

        lblPrestSub.setBackground(new java.awt.Color(100, 116, 139));
        lblPrestSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblPrestSub.setForeground(new java.awt.Color(100, 116, 139));
        lblPrestSub.setText("2 350 000");
        lblPrestSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblPrestSub.setPreferredSize(new java.awt.Dimension(0, 24));
        cardPrets.add(lblPrestSub, java.awt.BorderLayout.SOUTH);

        cardPretsTitle.setBackground(new java.awt.Color(255, 255, 255));
        cardPretsTitle.setPreferredSize(new java.awt.Dimension(0, 50));
        cardPretsTitle.setLayout(new java.awt.BorderLayout());

        lblPretsTitle.setBackground(new java.awt.Color(100, 116, 139));
        lblPretsTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblPretsTitle.setText("Prêts en cours");
        lblPretsTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardPretsTitle.add(lblPretsTitle, java.awt.BorderLayout.CENTER);

        cardPretsIconBg.setBackground(new java.awt.Color(219, 234, 254));
        cardPretsIconBg.setPreferredSize(new java.awt.Dimension(36, 36));

        lblPretsIcon.setBackground(new java.awt.Color(219, 234, 254));
        lblPretsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/credit-card1.png"))); // NOI18N
        lblPretsIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
        cardPretsIconBg.add(lblPretsIcon);

        cardPretsTitle.add(cardPretsIconBg, java.awt.BorderLayout.WEST);

        cardPrets.add(cardPretsTitle, java.awt.BorderLayout.NORTH);

        kpiPanel.add(cardPrets);

        cardRetard.setBackground(new java.awt.Color(255, 255, 255));
        cardRetard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(220, 38, 38))));
        cardRetard.setMinimumSize(new java.awt.Dimension(150, 110));
        cardRetard.setLayout(new java.awt.BorderLayout());

        lblRetardVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblRetardVal.setForeground(new java.awt.Color(220, 38, 38));
        lblRetardVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRetardVal.setText("5");
        lblRetardVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardRetard.add(lblRetardVal, java.awt.BorderLayout.CENTER);

        lblReatrdSub.setFont(new java.awt.Font("Segoe UI Emoji", 0, 11)); // NOI18N
        lblReatrdSub.setForeground(new java.awt.Color(220, 38, 38));
        lblReatrdSub.setText("187 500");
        lblReatrdSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        lblReatrdSub.setPreferredSize(new java.awt.Dimension(0, 24));
        cardRetard.add(lblReatrdSub, java.awt.BorderLayout.SOUTH);

        cardRetardTitle.setBackground(new java.awt.Color(255, 255, 255));
        cardRetardTitle.setPreferredSize(new java.awt.Dimension(0, 50));
        cardRetardTitle.setLayout(new java.awt.BorderLayout());

        lblRetardTitle.setBackground(new java.awt.Color(100, 116, 139));
        lblRetardTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblRetardTitle.setText("Prêts en retards");
        lblRetardTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardRetardTitle.add(lblRetardTitle, java.awt.BorderLayout.CENTER);

        cardRetardIconBg.setBackground(new java.awt.Color(254, 226, 226));
        cardRetardIconBg.setPreferredSize(new java.awt.Dimension(36, 36));

        lblRetardIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/alert.png"))); // NOI18N
        lblRetardIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
        cardRetardIconBg.add(lblRetardIcon);

        cardRetardTitle.add(cardRetardIconBg, java.awt.BorderLayout.WEST);

        cardRetard.add(cardRetardTitle, java.awt.BorderLayout.NORTH);

        kpiPanel.add(cardRetard);

        cardEpargne.setBackground(new java.awt.Color(255, 255, 255));
        cardEpargne.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(34, 197, 94))));
        cardEpargne.setMinimumSize(new java.awt.Dimension(150, 110));
        cardEpargne.setLayout(new java.awt.BorderLayout());

        lblEpargneVal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblEpargneVal.setForeground(new java.awt.Color(15, 23, 42));
        lblEpargneVal.setText("1 250 000 F");
        lblEpargneVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardEpargne.add(lblEpargneVal, java.awt.BorderLayout.CENTER);

        lblEpargneSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblEpargneSub.setForeground(new java.awt.Color(100, 116, 39));
        lblEpargneSub.setText("47 comptes");
        lblEpargneSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardEpargne.add(lblEpargneSub, java.awt.BorderLayout.SOUTH);

        cardEpargneTitle.setBackground(new java.awt.Color(255, 255, 255));
        cardEpargneTitle.setPreferredSize(new java.awt.Dimension(0, 50));
        cardEpargneTitle.setLayout(new java.awt.BorderLayout());

        lblEpargneTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblEpargneTitle.setText("Total épargne");
        lblEpargneTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        cardEpargneTitle.add(lblEpargneTitle, java.awt.BorderLayout.CENTER);

        cardEpargneIconBg.setBackground(new java.awt.Color(220, 252, 231));
        cardEpargneIconBg.setPreferredSize(new java.awt.Dimension(36, 36));

        lblEpargneIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/piggy-bank1.png"))); // NOI18N
        lblEpargneIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
        cardEpargneIconBg.add(lblEpargneIcon);

        cardEpargneTitle.add(cardEpargneIconBg, java.awt.BorderLayout.WEST);

        cardEpargne.add(cardEpargneTitle, java.awt.BorderLayout.NORTH);

        kpiPanel.add(cardEpargne);

        dashContent.add(kpiPanel);

        raccourcisPanel.setAlignmentX(0.0F);
        raccourcisPanel.setMaximumSize(new java.awt.Dimension(9999, 50));
        raccourcisPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnNouveauClient.setText("Nouveau client");
        raccourcisPanel.add(btnNouveauClient);

        btnNouveauCompte.setText("Nouveau compte");
        raccourcisPanel.add(btnNouveauCompte);

        btnNouveauPret.setText("Nouveau prêt");
        raccourcisPanel.add(btnNouveauPret);

        btnRemboursement.setText("Remboursement");
        raccourcisPanel.add(btnRemboursement);

        dashContent.add(raccourcisPanel);

        chartsPanel.setBackground(new java.awt.Color(248, 250, 252));
        chartsPanel.setAlignmentX(0.0F);
        chartsPanel.setMaximumSize(new java.awt.Dimension(32767, 280));
        chartsPanel.setPreferredSize(new java.awt.Dimension(0, 280));
        chartsPanel.setLayout(new java.awt.BorderLayout());

        fluxPanel.setBackground(new java.awt.Color(255, 255, 255));
        fluxPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        fluxPanel.setLayout(new java.awt.BorderLayout());
        chartsPanel.add(fluxPanel, java.awt.BorderLayout.CENTER);

        piePanel.setBackground(new java.awt.Color(255, 255, 255));
        piePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        piePanel.setPreferredSize(new java.awt.Dimension(260, 0));
        piePanel.setLayout(new java.awt.BorderLayout());
        chartsPanel.add(piePanel, java.awt.BorderLayout.EAST);

        dashContent.add(chartsPanel);

        tablePanel.setBackground(new java.awt.Color(255, 255, 255));
        tablePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        tablePanel.setAlignmentX(0.0F);
        tablePanel.setMaximumSize(new java.awt.Dimension(2147483647, 250));
        tablePanel.setPreferredSize(new java.awt.Dimension(0, 250));
        tablePanel.setLayout(new java.awt.BorderLayout());

        tableHeader.setBackground(new java.awt.Color(247, 247, 242));
        tableHeader.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)));
        tableHeader.setPreferredSize(new java.awt.Dimension(0, 44));
        tableHeader.setLayout(new java.awt.BorderLayout());

        lblTableTitle.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTableTitle.setForeground(new java.awt.Color(15, 23, 42));
        lblTableTitle.setText("Dernières transactions");
        lblTableTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        tableHeader.add(lblTableTitle, java.awt.BorderLayout.WEST);

        lblVoirTout.setForeground(new java.awt.Color(14, 165, 233));
        lblVoirTout.setText("Voir tout +");
        lblVoirTout.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 16));
        tableHeader.add(lblVoirTout, java.awt.BorderLayout.EAST);

        tablePanel.add(tableHeader, java.awt.BorderLayout.NORTH);

        tableTransactions.setAutoCreateRowSorter(true);
        tableTransactions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Client", "Type", "Montant", "Date", "Statut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableTransactions.setGridColor(new java.awt.Color(243, 244, 246));
        tableTransactions.setRowHeight(40);
        scrollTransactions.setViewportView(tableTransactions);

        tablePanel.add(scrollTransactions, java.awt.BorderLayout.CENTER);

        dashContent.add(tablePanel);

        scrollPane.setViewportView(dashContent);

        headerPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel alertPanel;
    private javax.swing.JButton btnNouveauClient;
    private javax.swing.JButton btnNouveauCompte;
    private javax.swing.JButton btnNouveauPret;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRemboursement;
    private javax.swing.JPanel cardCapital;
    private javax.swing.JPanel cardCapitalIconBg;
    private javax.swing.JPanel cardCapitalTitle;
    private javax.swing.JPanel cardClients;
    private javax.swing.JPanel cardClientsIconBg;
    private javax.swing.JPanel cardClientsTitle;
    private javax.swing.JPanel cardEpargne;
    private javax.swing.JPanel cardEpargneIconBg;
    private javax.swing.JPanel cardEpargneTitle;
    private javax.swing.JPanel cardPrets;
    private javax.swing.JPanel cardPretsIconBg;
    private javax.swing.JPanel cardPretsTitle;
    private javax.swing.JPanel cardRetard;
    private javax.swing.JPanel cardRetardIconBg;
    private javax.swing.JPanel cardRetardTitle;
    private javax.swing.JPanel chartsPanel;
    private javax.swing.JPanel dashContent;
    private javax.swing.JPanel fluxPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerRight;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel kpiPanel;
    private javax.swing.JLabel lblAlerte;
    private javax.swing.JLabel lblCapitalIcon;
    private javax.swing.JLabel lblCapitalSub;
    private javax.swing.JLabel lblCapitalTitle;
    private javax.swing.JLabel lblCapitalVal;
    private javax.swing.JLabel lblClientsIcon;
    private javax.swing.JLabel lblClientsSub;
    private javax.swing.JLabel lblClientsTitle;
    private javax.swing.JLabel lblClientsVal;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEpargneIcon;
    private javax.swing.JLabel lblEpargneSub;
    private javax.swing.JLabel lblEpargneTitle;
    private javax.swing.JLabel lblEpargneVal;
    private javax.swing.JLabel lblPrestSub;
    private javax.swing.JLabel lblPretsIcon;
    private javax.swing.JLabel lblPretsTitle;
    private javax.swing.JLabel lblPretsVal;
    private javax.swing.JLabel lblReatrdSub;
    private javax.swing.JLabel lblRetardIcon;
    private javax.swing.JLabel lblRetardTitle;
    private javax.swing.JLabel lblRetardVal;
    private javax.swing.JLabel lblTableTitle;
    private javax.swing.JLabel lblTitre;
    private javax.swing.JLabel lblVoirTout;
    private javax.swing.JPanel piePanel;
    private javax.swing.JPanel raccourcisPanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JScrollPane scrollTransactions;
    private javax.swing.JPanel tableHeader;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTable tableTransactions;
    private javax.swing.JPanel titleGroup;
    // End of variables declaration//GEN-END:variables
}
