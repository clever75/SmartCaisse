/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

/**
 *
 * @author Admin
 */
public class TableauAmortissement extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TableauAmortissement.class.getName());

    /**
     * Creates new form TableauAmortissement
     */
    public TableauAmortissement(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(600, 500);
        setLocationRelativeTo(parent);
        configurerStyle();
        configurerTableau();
        btnFermer.addActionListener(e -> dispose());
        btnImprimer.addActionListener(e -> imprimerTableau());
    }

    public void chargerPret(int idPret) {
        DAO.PretDAO pretDao = new DAO.PretDAO();
        Models.Pret pret = pretDao.chercher(idPret);
        if (pret == null) {
            return;
        }

        // Récupérer infos client
        DAO.CompteDAO compteDao = new DAO.CompteDAO();
        Models.Compte compte = compteDao.chercher(pret.getIdCompte());
        String nomClient = "—";
        String numCompte = "—";
        if (compte != null) {
            numCompte = compte.getNumeroCompte();
            DAO.ClientDAO clientDao = new DAO.ClientDAO();
            Models.Client client = clientDao.chercher(compte.getIdClient());
            if (client != null) {
                nomClient = client.getNom() + " " + client.getPrenom();
            }
        }

        // ── Calculs ──
        double capital = pret.getMontantPrincipal();
        double taux = pret.getTauxInteret();
        int duree = pret.getDureeMois();
        double capitalMois = capital / duree;

// Calculer le vrai total avec intérêts dégressifs
        double totalInteretsReel = 0;
        double capTemp = capital;
        for (int k = 1; k <= duree; k++) {
            totalInteretsReel += Math.round(capTemp * taux / 1200.0 * 100.0) / 100.0;
            capTemp = Math.round((capTemp - capitalMois) * 100.0) / 100.0;
            if (capTemp < 0.01) {
                capTemp = 0;
            }
        }
        double total = capital + totalInteretsReel;
        double mensualite = capitalMois + (capital * taux / 1200.0); // 1ère mensualité

        // ── Header ──
        lblTitre.setText("Tableau d'amortissement");
        lblSousTitre.setText(nomClient + "  —  Prêt #"
                + String.format("%03d", pret.getIdPret())
                + "  —  " + numCompte);

        // ── Cartes infos ──
        lblMontantVal.setText(formater(capital));
lblTotalVal.setText(formater(total));
        lblDureeVal.setText(duree + " mois");
        lblTauxVal.setText(String.format("%.0f %%", taux));
        // ── Date génération ──
        // lblDate.setText("Généré le : "
        // + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
        //     .format(new java.util.Date()));
        // ── Progression ──
        double remb = pret.getMontantRembourse();
        int echeancesPayees;
        if (remb <= 0) {
            echeancesPayees = 0;
        } else if (total - remb <= 1.0) {
            echeancesPayees = duree;
        } else {
            echeancesPayees = (int) Math.min(
                    Math.round(remb / mensualite), duree);
        }
        int pct = duree > 0 ? (int) ((double) echeancesPayees / duree * 100) : 0;
        progressBar.setValue(Math.min(pct, 100));
        lblProgressTitle.setText(String.format(
                "Progression — %d / %d échéances payées",
                echeancesPayees, duree));
        if (pct >= 100) {
            progressBar.setForeground(new java.awt.Color(21, 128, 61));
        } else if (pct >= 50) {
            progressBar.setForeground(new java.awt.Color(14, 165, 233));
        } else {
            progressBar.setForeground(new java.awt.Color(245, 158, 11));
        }

        // ── Remplir le tableau ──
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) tableAmort.getModel();
        model.setRowCount(0);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        if (pret.getDateDebut() != null) {
            cal.setTime(pret.getDateDebut());
        } else {
            cal.setTime(new java.util.Date());
        }
// Première échéance = 1 mois après la date de début
        cal.add(java.util.Calendar.MONTH, 1);
// Fixer le jour au 1er pour éviter les décalages
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd/MM/yyyy");
        double capitalRestant = capital;
        double totalMensualites = 0;
        double totalCapitalPaye = 0;
        double totalInteretsPayes = 0;

        for (int i = 1; i <= duree; i++) {
            double capitalEcheance = Math.round(capitalMois * 100.0) / 100.0;
            if (i == duree) {
                capitalEcheance = capitalRestant; // ajustement dernière ligne
            }
            // Intérêts calculés sur le capital RESTANT (dégressif)
            double interetsEcheance = Math.round(
                    capitalRestant * taux / 1200.0 * 100.0) / 100.0;
            double mensualiteEcheance = capitalEcheance + interetsEcheance;
            capitalRestant = Math.round((capitalRestant - capitalEcheance) * 100.0) / 100.0;
            if (capitalRestant < 0.01) {
                capitalRestant = 0;
            }

            totalMensualites += mensualiteEcheance;
            totalCapitalPaye += capitalEcheance;
            totalInteretsPayes += interetsEcheance;

            String dateEch = sdf.format(cal.getTime());
            String statut;
            java.util.Date dateEchDate = cal.getTime();
            java.util.Date aujourdhui = new java.util.Date();

            if (i <= echeancesPayees) {
                statut = "Payé";
            } else if (dateEchDate.before(aujourdhui)) {
                statut = "En retard";  // Date dépassée et non payée
            } else if (i == echeancesPayees + 1) {
                statut = "En attente";
            } else {
                statut = "À venir";
            }

            model.addRow(new Object[]{
                i,
                dateEch,
                formater(mensualiteEcheance),
                formater(capitalEcheance),
                formater(interetsEcheance),
                formater(capitalRestant),
                statut
            });

            cal.add(java.util.Calendar.MONTH, 1); // ← incrémenter À LA FIN

        }
        // Ligne total
        model.addRow(new Object[]{
            "—",
            "TOTAL",
            formater(totalMensualites),
            formater(totalCapitalPaye),
            formater(totalInteretsPayes),
            "—",
            "—"
        });
        configurerRenderers(echeancesPayees, duree);

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
        return sb.toString() + " F";
    }

    private void configurerTableau() {
        tableAmort.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"N°", "Date", "Mensualité",
                    "Capital", "Intérêts", "Capital restant", "Statut"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
        tableAmort.setRowHeight(36);
        tableAmort.getTableHeader().setReorderingAllowed(false);
        tableAmort.getColumnModel().getColumn(0).setPreferredWidth(35);
        tableAmort.getColumnModel().getColumn(1).setPreferredWidth(90);
        tableAmort.getColumnModel().getColumn(2).setPreferredWidth(90);
        tableAmort.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableAmort.getColumnModel().getColumn(4).setPreferredWidth(70);
        tableAmort.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableAmort.getColumnModel().getColumn(6).setPreferredWidth(80);
    }

    private void configurerStyle() {
        // Header
        tableAmort.getTableHeader().setBackground(
                new java.awt.Color(241, 245, 249));
        tableAmort.getTableHeader().setForeground(
                new java.awt.Color(71, 85, 105));
        tableAmort.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tableAmort.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 38));
        tableAmort.getTableHeader().setReorderingAllowed(false);

        tableAmort.setBackground(java.awt.Color.WHITE);
        tableAmort.setGridColor(new java.awt.Color(241, 245, 249));
        tableAmort.setShowVerticalLines(false);
        tableAmort.setShowHorizontalLines(true);
        tableAmort.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableAmort.setSelectionBackground(new java.awt.Color(239, 246, 255));

        // Centrer en-têtes
        ((javax.swing.table.DefaultTableCellRenderer) tableAmort.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Boutons
        btnFermer.setBackground(new java.awt.Color(241, 245, 249));
        btnFermer.setForeground(new java.awt.Color(71, 85, 105));
        btnFermer.setBorderPainted(false);
        btnFermer.setFocusPainted(false);
        btnFermer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFermer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        btnImprimer.setBackground(new java.awt.Color(14, 165, 233));
        btnImprimer.setForeground(java.awt.Color.WHITE);
        btnImprimer.setBorderPainted(false);
        btnImprimer.setFocusPainted(false);
        btnImprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    }

    private void configurerRenderers(int echeancesPayees, int duree) {

        // Renderer centrage + couleurs lignes
        tableAmort.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                boolean isTotal = row == duree;

                if (isTotal) {
                    // Ligne total
                    setBackground(new java.awt.Color(241, 245, 249));
                    setForeground(new java.awt.Color(15, 23, 42));
                    setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.BOLD, 12));
                } else if (!isSelected) {
                    if (row < echeancesPayees) {
                        // Lignes payées — grisées
                        setBackground(new java.awt.Color(250, 250, 250));
                        setForeground(new java.awt.Color(150, 160, 175));
                        setFont(new java.awt.Font("Segoe UI",
                                java.awt.Font.PLAIN, 12));
                    } else if (row == echeancesPayees) {
                        // Ligne courante — surlignée
                        setBackground(new java.awt.Color(239, 246, 255));
                        setForeground(new java.awt.Color(15, 23, 42));
                        setFont(new java.awt.Font("Segoe UI",
                                java.awt.Font.BOLD, 12));
                    } else {
                        // Lignes à venir
                        setBackground(row % 2 == 0
                                ? java.awt.Color.WHITE
                                : new java.awt.Color(248, 250, 252));
                        setForeground(new java.awt.Color(15, 23, 42));
                        setFont(new java.awt.Font("Segoe UI",
                                java.awt.Font.PLAIN, 12));
                    }
                }
                return this;
            }
        });

        // Badge Statut — colonne 6
        tableAmort.getColumnModel().getColumn(6).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    boolean isTotal = row == duree;
                    if (isTotal) {
                        javax.swing.JLabel lbl = new javax.swing.JLabel("—");
                        lbl.setHorizontalAlignment(
                                javax.swing.SwingConstants.CENTER);
                        lbl.setBackground(new java.awt.Color(241, 245, 249));
                        lbl.setOpaque(true);
                        return lbl;
                    }

                    javax.swing.JPanel cell = new javax.swing.JPanel(
                            new java.awt.FlowLayout(
                                    java.awt.FlowLayout.CENTER, 0, 6));
                    String text = value != null ? value.toString() : "";

                    java.awt.Color bg, fg, cellBg;
                    if (row < echeancesPayees) {
                        bg = new java.awt.Color(220, 252, 231);
                        fg = new java.awt.Color(21, 128, 61);
                        cellBg = new java.awt.Color(250, 250, 250);
                    } else if (row == echeancesPayees) {
                        bg = new java.awt.Color(219, 234, 254);
                        fg = new java.awt.Color(14, 165, 233);
                        cellBg = new java.awt.Color(239, 246, 255);
                    } else {
                        String statutCell = value != null ? value.toString() : "";
                        if ("En retard".equals(statutCell)) {
                            bg = new java.awt.Color(254, 226, 226);
                            fg = new java.awt.Color(220, 38, 38);
                            cellBg = new java.awt.Color(255, 245, 245);
                        } else {
                            bg = new java.awt.Color(241, 245, 249);
                            fg = new java.awt.Color(100, 116, 139);
                            cellBg = row % 2 == 0
                                    ? java.awt.Color.WHITE
                                    : new java.awt.Color(248, 250, 252);
                        }
                    }

                    cell.setBackground(cellBg);

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

    private void imprimerTableau() {
        try {
            java.io.File fichier = java.io.File.createTempFile(
                    "tableau_amortissement_", ".pdf");
            fichier.deleteOnExit();

            com.lowagie.text.Document doc = new com.lowagie.text.Document(
                    com.lowagie.text.PageSize.A4);
            com.lowagie.text.pdf.PdfWriter.getInstance(doc,
                    new java.io.FileOutputStream(fichier));
            doc.open();

            // ── En-tête ──
            Utils.ImpressionUtil.imprimerEntetePublic(doc,
                    "SmartCaisse", "Microfinance — Lomé, Togo");

            // ── Titre ──
            com.lowagie.text.Paragraph titre
                    = new com.lowagie.text.Paragraph(
                            "TABLEAU D'AMORTISSEMENT",
                            new com.lowagie.text.Font(
                                    com.lowagie.text.Font.HELVETICA,
                                    15, com.lowagie.text.Font.BOLD,
                                    new java.awt.Color(15, 23, 42)));
            titre.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            doc.add(titre);

            com.lowagie.text.Paragraph sousTitre
                    = new com.lowagie.text.Paragraph(
                            lblSousTitre.getText(),
                            new com.lowagie.text.Font(
                                    com.lowagie.text.Font.HELVETICA,
                                    10, com.lowagie.text.Font.NORMAL,
                                    new java.awt.Color(100, 116, 139)));
            sousTitre.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            sousTitre.setSpacingAfter(12);
            doc.add(sousTitre);

            // ── Cartes infos ──
            com.lowagie.text.pdf.PdfPTable tableInfos
                    = new com.lowagie.text.pdf.PdfPTable(4);
            tableInfos.setWidthPercentage(100);
            tableInfos.setSpacingBefore(8);
            tableInfos.setSpacingAfter(12);

            ajouterCardInfo(tableInfos, "Montant accordé",
                    lblMontantVal.getText(),
                    new java.awt.Color(14, 165, 233));
            ajouterCardInfo(tableInfos, "Taux d'intérêt",
                    lblTauxVal.getText(),
                    new java.awt.Color(15, 23, 42));
            ajouterCardInfo(tableInfos, "Durée",
                    lblDureeVal.getText(),
                    new java.awt.Color(15, 23, 42));
            ajouterCardInfo(tableInfos, "Total à rembourser",
                    lblTotalVal.getText(),
                    new java.awt.Color(21, 128, 61));
            doc.add(tableInfos);

            // ── Barre progression ──
            String progression = lblProgressTitle.getText();
            com.lowagie.text.Paragraph pProg
                    = new com.lowagie.text.Paragraph(
                            progression,
                            new com.lowagie.text.Font(
                                    com.lowagie.text.Font.HELVETICA,
                                    9, com.lowagie.text.Font.NORMAL,
                                    new java.awt.Color(100, 116, 139)));
            pProg.setSpacingAfter(8);
            doc.add(pProg);

            // ── Séparateur section ──
            com.lowagie.text.Paragraph lblSection
                    = new com.lowagie.text.Paragraph(
                            "ÉCHÉANCIER DE REMBOURSEMENT",
                            new com.lowagie.text.Font(
                                    com.lowagie.text.Font.HELVETICA,
                                    10, com.lowagie.text.Font.BOLD,
                                    new java.awt.Color(14, 165, 233)));
            lblSection.setSpacingAfter(4);
            doc.add(lblSection);

            com.lowagie.text.pdf.draw.LineSeparator sep
                    = new com.lowagie.text.pdf.draw.LineSeparator(
                            1, 100,
                            new java.awt.Color(226, 232, 240),
                            com.lowagie.text.Element.ALIGN_LEFT, -2);
            doc.add(sep);

            // ── Tableau échéancier ──
            com.lowagie.text.pdf.PdfPTable tableEch
                    = new com.lowagie.text.pdf.PdfPTable(7);
            tableEch.setWidthPercentage(100);
            tableEch.setWidths(new float[]{
                0.6f, 1.4f, 1.4f, 1.2f, 1.2f, 1.4f, 1.2f});
            tableEch.setSpacingBefore(8);

            // En-têtes colonnes
            String[] entetes = {"N°", "Date", "Mensualité",
                "Capital", "Intérêts", "Cap. restant", "Statut"};
            for (String e : entetes) {
                com.lowagie.text.pdf.PdfPCell cell
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(e,
                                        new com.lowagie.text.Font(
                                                com.lowagie.text.Font.HELVETICA,
                                                9, com.lowagie.text.Font.BOLD,
                                                java.awt.Color.WHITE)));
                cell.setBackgroundColor(
                        new java.awt.Color(15, 23, 42));
                cell.setPadding(6);
                cell.setBorder(
                        com.lowagie.text.Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(
                        com.lowagie.text.Element.ALIGN_CENTER);
                tableEch.addCell(cell);
            }

            // Lignes du tableau
            javax.swing.table.DefaultTableModel model
                    = (javax.swing.table.DefaultTableModel) tableAmort.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                boolean isTotal = i == model.getRowCount() - 1;
                boolean isPaye = !isTotal
                        && "Payé".equals(
                                model.getValueAt(i, 6));
                boolean isEnRetard = !isTotal
                        && "En retard".equals(
                                model.getValueAt(i, 6));
                boolean isCourant = !isTotal
                        && "En attente".equals(
                                model.getValueAt(i, 6));

                java.awt.Color bgLigne;
                if (isTotal) {
                    bgLigne = new java.awt.Color(241, 245, 249);
                } else if (isPaye) {
                    bgLigne = new java.awt.Color(250, 250, 250);
                } else if (isCourant) {
                    bgLigne = new java.awt.Color(239, 246, 255);
                } else if (isEnRetard) {
                    bgLigne = new java.awt.Color(255, 245, 245);
                } else {
                    bgLigne = i % 2 == 0
                            ? java.awt.Color.WHITE
                            : new java.awt.Color(248, 250, 252);
                }

                for (int j = 0; j < 7; j++) {
                    String val = model.getValueAt(i, j) != null
                            ? model.getValueAt(i, j).toString() : "—";

                    // Couleur texte selon colonne et statut
                    java.awt.Color fgTexte;
                    int fontStyle = com.lowagie.text.Font.NORMAL;

                    if (isTotal) {
                        fgTexte = new java.awt.Color(15, 23, 42);
                        fontStyle = com.lowagie.text.Font.BOLD;
                    } else if (isPaye) {
                        fgTexte = new java.awt.Color(150, 160, 175);
                    } else if (j == 6) {
                        // Colonne statut — couleur spéciale
                        if (isEnRetard) {
                            fgTexte = new java.awt.Color(220, 38, 38);
                        } else if (isCourant) {
                            fgTexte = new java.awt.Color(14, 165, 233);
                            fontStyle = com.lowagie.text.Font.BOLD;
                        } else {
                            fgTexte = new java.awt.Color(100, 116, 139);
                        }
                    } else {
                        fgTexte = new java.awt.Color(15, 23, 42);
                    }

                    com.lowagie.text.pdf.PdfPCell cell
                            = new com.lowagie.text.pdf.PdfPCell(
                                    new com.lowagie.text.Phrase(val,
                                            new com.lowagie.text.Font(
                                                    com.lowagie.text.Font.HELVETICA,
                                                    8, fontStyle, fgTexte)));
                    cell.setBackgroundColor(bgLigne);
                    cell.setPadding(5);
                    cell.setBorderColor(
                            new java.awt.Color(241, 245, 249));
                    cell.setHorizontalAlignment(
                            com.lowagie.text.Element.ALIGN_CENTER);
                    tableEch.addCell(cell);
                }
            }
            doc.add(tableEch);

            // ── Pied de page ──
            Utils.ImpressionUtil.imprimerPiedDePagePublic(doc);

            doc.close();

            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(fichier);
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Erreur impression :\n" + e.getMessage(),
                    "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterCardInfo(
            com.lowagie.text.pdf.PdfPTable table,
            String titre, String valeur,
            java.awt.Color couleurVal) {

        com.lowagie.text.pdf.PdfPCell cell
                = new com.lowagie.text.pdf.PdfPCell();
        cell.setBorder(com.lowagie.text.Rectangle.BOX);
        cell.setBorderColor(new java.awt.Color(226, 232, 240));
        cell.setPadding(8);
        cell.setBackgroundColor(java.awt.Color.WHITE);

        com.lowagie.text.Paragraph pTitre
                = new com.lowagie.text.Paragraph(titre,
                        new com.lowagie.text.Font(
                                com.lowagie.text.Font.HELVETICA,
                                9, com.lowagie.text.Font.NORMAL,
                                new java.awt.Color(100, 116, 139)));
        cell.addElement(pTitre);

        com.lowagie.text.Paragraph pVal
                = new com.lowagie.text.Paragraph(valeur,
                        new com.lowagie.text.Font(
                                com.lowagie.text.Font.HELVETICA,
                                11, com.lowagie.text.Font.BOLD,
                                couleurVal));
        cell.addElement(pVal);
        table.addCell(cell);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitre = new javax.swing.JLabel();
        lblSousTitre = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        btnFermer = new javax.swing.JButton();
        btnImprimer = new javax.swing.JButton();
        centerPanel = new javax.swing.JPanel();
        infoGrid = new javax.swing.JPanel();
        panelInfoMontant = new javax.swing.JPanel();
        lblMontantTitle = new javax.swing.JLabel();
        lblMontantVal = new javax.swing.JLabel();
        panelInfoTaux = new javax.swing.JPanel();
        lblTauxTitle = new javax.swing.JLabel();
        lblTauxVal = new javax.swing.JLabel();
        panelInfoDuree = new javax.swing.JPanel();
        lblDureeTitle = new javax.swing.JLabel();
        lblDureeVal = new javax.swing.JLabel();
        panelInfoTotal = new javax.swing.JPanel();
        lblTotalTitle = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        progressPanel = new javax.swing.JPanel();
        lblProgressTitle = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        scrollTable = new javax.swing.JScrollPane();
        tableAmort = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tableau d'amortissement");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(700, 580));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20)));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 80));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        lblTitre.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTitre.setText("Tableau d'amortissement");
        jPanel1.add(lblTitre);

        lblSousTitre.setForeground(new java.awt.Color(100, 116, 139));
        lblSousTitre.setText("-");
        jPanel1.add(lblSousTitre);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(248, 250, 252));
        footerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        footerPanel.setPreferredSize(new java.awt.Dimension(0, 55));
        footerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        btnFermer.setText("Fermer");
        footerPanel.add(btnFermer);

        btnImprimer.setForeground(new java.awt.Color(14, 165, 233));
        btnImprimer.setText("Imprimer");
        footerPanel.add(btnImprimer);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        centerPanel.setBackground(new java.awt.Color(255, 255, 255));
        centerPanel.setLayout(new javax.swing.BoxLayout(centerPanel, javax.swing.BoxLayout.Y_AXIS));

        infoGrid.setBackground(new java.awt.Color(248, 250, 252));
        infoGrid.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        infoGrid.setAlignmentX(0.0F);
        infoGrid.setMaximumSize(new java.awt.Dimension(9999, 70));
        infoGrid.setPreferredSize(new java.awt.Dimension(0, 70));
        infoGrid.setLayout(new java.awt.GridLayout(1, 4, 10, 0));

        panelInfoMontant.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoMontant.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelInfoMontant.setLayout(new javax.swing.BoxLayout(panelInfoMontant, javax.swing.BoxLayout.Y_AXIS));

        lblMontantTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblMontantTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblMontantTitle.setText("Montant accordé");
        panelInfoMontant.add(lblMontantTitle);

        lblMontantVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblMontantVal.setForeground(new java.awt.Color(14, 165, 233));
        lblMontantVal.setText("-");
        panelInfoMontant.add(lblMontantVal);

        infoGrid.add(panelInfoMontant);

        panelInfoTaux.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoTaux.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelInfoTaux.setLayout(new javax.swing.BoxLayout(panelInfoTaux, javax.swing.BoxLayout.Y_AXIS));

        lblTauxTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTauxTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblTauxTitle.setText("Taux d'intérêt");
        panelInfoTaux.add(lblTauxTitle);

        lblTauxVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTauxVal.setForeground(new java.awt.Color(15, 23, 42));
        lblTauxVal.setText("-");
        panelInfoTaux.add(lblTauxVal);

        infoGrid.add(panelInfoTaux);

        panelInfoDuree.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoDuree.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelInfoDuree.setLayout(new javax.swing.BoxLayout(panelInfoDuree, javax.swing.BoxLayout.Y_AXIS));

        lblDureeTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDureeTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblDureeTitle.setText("Durée");
        panelInfoDuree.add(lblDureeTitle);

        lblDureeVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDureeVal.setForeground(new java.awt.Color(15, 23, 42));
        lblDureeVal.setText("-");
        panelInfoDuree.add(lblDureeVal);

        infoGrid.add(panelInfoDuree);

        panelInfoTotal.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelInfoTotal.setLayout(new javax.swing.BoxLayout(panelInfoTotal, javax.swing.BoxLayout.Y_AXIS));

        lblTotalTitle.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTotalTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblTotalTitle.setText("Total à rembourser");
        panelInfoTotal.add(lblTotalTitle);

        lblTotalVal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTotalVal.setForeground(new java.awt.Color(21, 128, 61));
        lblTotalVal.setText("-");
        panelInfoTotal.add(lblTotalVal);

        infoGrid.add(panelInfoTotal);

        centerPanel.add(infoGrid);

        progressPanel.setBackground(new java.awt.Color(255, 255, 255));
        progressPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        progressPanel.setAlignmentX(0.0F);
        progressPanel.setMaximumSize(new java.awt.Dimension(9999, 50));
        progressPanel.setPreferredSize(new java.awt.Dimension(0, 50));
        progressPanel.setLayout(new javax.swing.BoxLayout(progressPanel, javax.swing.BoxLayout.Y_AXIS));

        lblProgressTitle.setForeground(new java.awt.Color(100, 116, 139));
        lblProgressTitle.setText("Progession - 0 / 0 échéances payées");
        lblProgressTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        progressPanel.add(lblProgressTitle);

        progressBar.setBackground(new java.awt.Color(226, 232, 240));
        progressBar.setForeground(new java.awt.Color(14, 165, 233));
        progressBar.setAlignmentX(0.0F);
        progressBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        progressBar.setMaximumSize(new java.awt.Dimension(9999, 6));
        progressPanel.add(progressBar);

        centerPanel.add(progressPanel);

        scrollTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));
        scrollTable.setAlignmentX(0.0F);

        tableAmort.setModel(new javax.swing.table.DefaultTableModel(
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
        tableAmort.setGridColor(new java.awt.Color(241, 245, 249));
        tableAmort.setRowHeight(36);
        tableAmort.setSelectionBackground(new java.awt.Color(239, 246, 255));
        tableAmort.setSelectionForeground(new java.awt.Color(15, 23, 42));
        tableAmort.setShowHorizontalLines(true);
        scrollTable.setViewportView(tableAmort);

        centerPanel.add(scrollTable);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

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
                TableauAmortissement dialog = new TableauAmortissement(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnImprimer;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel infoGrid;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDureeTitle;
    private javax.swing.JLabel lblDureeVal;
    private javax.swing.JLabel lblMontantTitle;
    private javax.swing.JLabel lblMontantVal;
    private javax.swing.JLabel lblProgressTitle;
    private javax.swing.JLabel lblSousTitre;
    private javax.swing.JLabel lblTauxTitle;
    private javax.swing.JLabel lblTauxVal;
    private javax.swing.JLabel lblTitre;
    private javax.swing.JLabel lblTotalTitle;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JPanel panelInfoDuree;
    private javax.swing.JPanel panelInfoMontant;
    private javax.swing.JPanel panelInfoTaux;
    private javax.swing.JPanel panelInfoTotal;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTable tableAmort;
    // End of variables declaration//GEN-END:variables
}
