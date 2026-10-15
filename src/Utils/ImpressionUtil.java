package Utils;


import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
public class ImpressionUtil {

    // ── Polices ──
    private static final Font FONT_TITRE = new Font(
            Font.HELVETICA, 16, Font.BOLD,
            new java.awt.Color(15, 23, 42));
    private static final Font FONT_SOUS_TITRE = new Font(
            Font.HELVETICA, 11, Font.NORMAL,
            new java.awt.Color(100, 116, 139));
    private static final Font FONT_SECTION = new Font(
            Font.HELVETICA, 10, Font.BOLD,
            new java.awt.Color(14, 165, 233));
    private static final Font FONT_LABEL = new Font(
            Font.HELVETICA, 9, Font.NORMAL,
            new java.awt.Color(100, 116, 139));
    private static final Font FONT_VALEUR = new Font(
            Font.HELVETICA, 10, Font.BOLD,
            new java.awt.Color(15, 23, 42));
    private static final Font FONT_MONTANT = new Font(
            Font.HELVETICA, 13, Font.BOLD,
            new java.awt.Color(14, 165, 233));
    private static final Font FONT_SMALL = new Font(
            Font.HELVETICA, 8, Font.NORMAL,
            new java.awt.Color(100, 116, 139));

    // ══════════════════════════════════════════════
    // REÇU DE REMBOURSEMENT
    // ══════════════════════════════════════════════
    public static void imprimerRecuRemboursement(
            Models.Pret pret,
            Models.Compte compte,
            Models.Client client,
            double montantPaye,
            double resteApres,
            String moyenPaiement,
            boolean estAnticipe) {

        try {
            // Fichier temporaire
            File fichier = File.createTempFile(
                    "recu_remboursement_", ".pdf");
            fichier.deleteOnExit();

            Document doc = new Document(PageSize.A5);
            PdfWriter.getInstance(doc,
                    new FileOutputStream(fichier));
            doc.open();

            // ── En-tête microfinance ──
            ajouterEntete(doc, "SmartCaisse",
                    "Microfinance — Lomé, Togo");

            // ── Titre reçu ──
            doc.add(new Paragraph(estAnticipe
                    ? "REÇU DE REMBOURSEMENT ANTICIPÉ"
                    : "REÇU DE REMBOURSEMENT",
                    new Font(Font.HELVETICA, 14, Font.BOLD,
                            new java.awt.Color(15, 23, 42))));
            doc.add(new Paragraph(
                    "N° Reçu : RMB-"
                    + String.format("%04d", pret.getIdPret())
                    + "-" + System.currentTimeMillis() % 10000,
                    FONT_SOUS_TITRE));
            doc.add(new Paragraph(
                    "Date : " + new java.text.SimpleDateFormat(
                            "dd/MM/yyyy HH:mm")
                            .format(new java.util.Date()),
                    FONT_SOUS_TITRE));

            doc.add(new Paragraph(" "));

            // ── Infos client ──
            ajouterSeparateur(doc, "INFORMATIONS CLIENT");
            PdfPTable tableClient = new PdfPTable(2);
            tableClient.setWidthPercentage(100);
            tableClient.setSpacingBefore(6);
            tableClient.setSpacingAfter(6);

            ajouterLigne2Col(tableClient, "Client",
                    client.getNom() + " " + client.getPrenom());
            ajouterLigne2Col(tableClient, "Téléphone",
                    client.getTelephone());
            ajouterLigne2Col(tableClient, "N° Compte",
                    compte.getNumeroCompte());
            ajouterLigne2Col(tableClient, "N° Prêt",
                    "#" + String.format("%03d", pret.getIdPret()));
            doc.add(tableClient);

            // ── Détails remboursement ──
            ajouterSeparateur(doc, "DÉTAILS DU REMBOURSEMENT");
            PdfPTable tableRemb = new PdfPTable(2);
            tableRemb.setWidthPercentage(100);
            tableRemb.setSpacingBefore(6);
            tableRemb.setSpacingAfter(6);

            double interets = pret.getMontantPrincipal()
                    * pret.getTauxInteret()
                    * pret.getDureeMois() / 1200.0;
            double totalPret = pret.getMontantPrincipal() + interets;

           ajouterLigne2Col(tableRemb, "Montant accordé",
        formaterMontant(pret.getMontantPrincipal()));
ajouterLigne2Col(tableRemb, "Total à rembourser",
        formaterMontant(totalPret));
ajouterLigne2Col(tableRemb, "Déjà remboursé",
        formaterMontant(pret.getMontantRembourse()));
            ajouterLigne2Col(tableRemb, "Moyen de paiement",
                    moyenPaiement);
            doc.add(tableRemb);

            // ── Montant payé — mis en valeur ──
            PdfPTable tableMontant = new PdfPTable(1);
            tableMontant.setWidthPercentage(100);
            tableMontant.setSpacingBefore(8);
            tableMontant.setSpacingAfter(8);

            PdfPCell cellMontant = new PdfPCell();
            cellMontant.setBackgroundColor(
                    new java.awt.Color(219, 234, 254));
            cellMontant.setBorder(Rectangle.NO_BORDER);
            cellMontant.setPadding(12);

            Paragraph pMontant = new Paragraph(
                    "MONTANT PAYÉ : " + formaterMontant(montantPaye),

                    new Font(Font.HELVETICA, 14, Font.BOLD,
                            new java.awt.Color(14, 165, 233)));
            pMontant.setAlignment(Element.ALIGN_CENTER);
            cellMontant.addElement(pMontant);

            Paragraph pReste = new Paragraph(
                    resteApres <= 2.0
                    ? "✔ Prêt totalement remboursé !"
                    : "Reste à payer : " + formaterMontant(resteApres),
                    new Font(Font.HELVETICA, 10,
                            Font.BOLD,
                            resteApres <= 2.0
                            ? new java.awt.Color(21, 128, 61)
                            : new java.awt.Color(220, 38, 38)));
            pReste.setAlignment(Element.ALIGN_CENTER);
            cellMontant.addElement(pReste);

            tableMontant.addCell(cellMontant);
            doc.add(tableMontant);

            // ── Pied de page ──
            ajouterPiedDePage(doc);

            doc.close();

            // Ouvrir le PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fichier);
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Erreur lors de la génération du reçu :\n"
                    + e.getMessage(),
                    "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // ══════════════════════════════════════════════
    // MÉTHODES UTILITAIRES PRIVÉES
    // ══════════════════════════════════════════════
    private static void ajouterEntete(Document doc,
            String nom, String sousTitre) throws Exception {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new java.awt.Color(15, 23, 42));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(14);

        Paragraph pNom = new Paragraph(nom,
                new Font(Font.HELVETICA, 18, Font.BOLD,
                        java.awt.Color.WHITE));
        pNom.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pNom);

        Paragraph pSub = new Paragraph(sousTitre,
                new Font(Font.HELVETICA, 10, Font.NORMAL,
                        new java.awt.Color(148, 163, 184)));
        pSub.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pSub);

        table.addCell(cell);
        doc.add(table);
    }

    private static void ajouterSeparateur(Document doc,
            String titre) throws Exception {
        Paragraph p = new Paragraph(titre, FONT_SECTION);
        p.setSpacingBefore(8);
        p.setSpacingAfter(4);
        doc.add(p);

        LineSeparator line = new LineSeparator(1, 100,
                new java.awt.Color(226, 232, 240),
                Element.ALIGN_LEFT, -2);
        doc.add(line);
    }

    private static void ajouterLigne2Col(PdfPTable table,
            String label, String valeur) {
        PdfPCell cellLabel = new PdfPCell(
                new Phrase(label, FONT_LABEL));
        cellLabel.setBorder(Rectangle.BOTTOM);
        cellLabel.setBorderColor(new java.awt.Color(241, 245, 249));
        cellLabel.setPadding(6);
        cellLabel.setBackgroundColor(
                new java.awt.Color(248, 250, 252));
        table.addCell(cellLabel);

        PdfPCell cellVal = new PdfPCell(
                new Phrase(valeur != null ? valeur : "—",
                        FONT_VALEUR));
        cellVal.setBorder(Rectangle.BOTTOM);
        cellVal.setBorderColor(new java.awt.Color(241, 245, 249));
        cellVal.setPadding(6);
        table.addCell(cellVal);
    }

    private static void ajouterPiedDePage(Document doc)
            throws Exception {
        doc.add(new Paragraph(" "));
        LineSeparator line = new LineSeparator(1, 100,
                new java.awt.Color(226, 232, 240),
                Element.ALIGN_LEFT, -2);
        doc.add(line);

        Paragraph pied = new Paragraph(
                "Document généré par SmartCaisse — "
                + new java.text.SimpleDateFormat("dd/MM/yyyy")
                        .format(new java.util.Date())
                + "\nCe document tient lieu de reçu officiel.",
                FONT_SMALL);
        pied.setAlignment(Element.ALIGN_CENTER);
        pied.setSpacingBefore(6);
        doc.add(pied);
    }
    public static String formaterMontant(double montant) {
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
    //Méthode pour imprimer un prêt
    public static void imprimerContratPret(
        Models.Pret pret,
        Models.Compte compte,
        Models.Client client) {

    try {
        File fichier = File.createTempFile("contrat_pret_", ".pdf");
        fichier.deleteOnExit();

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(fichier));
        doc.open();

        // ── En-tête ──
        ajouterEntete(doc, "SmartCaisse", "Microfinance — Lomé, Togo");

        // ── Titre ──
        Paragraph titre = new Paragraph("CONTRAT DE PRÊT",
                new Font(Font.HELVETICA, 16, Font.BOLD,
                        new java.awt.Color(15, 23, 42)));
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingAfter(4);
        doc.add(titre);

        Paragraph refContrat = new Paragraph(
                "Contrat N° : CPT-"
                + String.format("%04d", pret.getIdPret())
                + "  —  Signé le : "
                + new java.text.SimpleDateFormat("dd/MM/yyyy")
                        .format(new java.util.Date()),
                FONT_SOUS_TITRE);
        refContrat.setAlignment(Element.ALIGN_CENTER);
        refContrat.setSpacingAfter(16);
        doc.add(refContrat);

        // ── Parties du contrat ──
        ajouterSeparateur(doc, "PARTIES DU CONTRAT");

        PdfPTable tableParties = new PdfPTable(2);
        tableParties.setWidthPercentage(100);
        tableParties.setSpacingBefore(8);
        tableParties.setSpacingAfter(8);

        // En-têtes colonnes
        PdfPCell entetePreneur = new PdfPCell(new Phrase(
                "LA MICROFINANCE", new Font(Font.HELVETICA,
                        10, Font.BOLD, java.awt.Color.WHITE)));
        entetePreneur.setBackgroundColor(new java.awt.Color(15, 23, 42));
        entetePreneur.setPadding(8);
        entetePreneur.setBorder(Rectangle.NO_BORDER);
        tableParties.addCell(entetePreneur);

        PdfPCell enteteEmprunteur = new PdfPCell(new Phrase(
                "L'EMPRUNTEUR", new Font(Font.HELVETICA,
                        10, Font.BOLD, java.awt.Color.WHITE)));
        enteteEmprunteur.setBackgroundColor(
                new java.awt.Color(14, 165, 233));
        enteteEmprunteur.setPadding(8);
        enteteEmprunteur.setBorder(Rectangle.NO_BORDER);
        tableParties.addCell(enteteEmprunteur);

        // Infos microfinance
        PdfPCell cellMicro = new PdfPCell();
        cellMicro.setBorder(Rectangle.BOX);
        cellMicro.setBorderColor(new java.awt.Color(226, 232, 240));
        cellMicro.setPadding(10);
        Paragraph pMicro = new Paragraph();
        pMicro.add(new Phrase("SmartCaisse\n", new Font(
                Font.HELVETICA, 11, Font.BOLD,
                new java.awt.Color(15, 23, 42))));
        pMicro.add(new Phrase("Lomé, Togo\n", FONT_LABEL));
        pMicro.add(new Phrase("Agréée BCEAO", FONT_LABEL));
        cellMicro.addElement(pMicro);
        tableParties.addCell(cellMicro);

        // Infos client
        PdfPCell cellClient = new PdfPCell();
        cellClient.setBorder(Rectangle.BOX);
        cellClient.setBorderColor(new java.awt.Color(226, 232, 240));
        cellClient.setPadding(10);
        Paragraph pClient = new Paragraph();
        pClient.add(new Phrase(
                client.getNom() + " " + client.getPrenom() + "\n",
                new Font(Font.HELVETICA, 11, Font.BOLD,
                        new java.awt.Color(15, 23, 42))));
        pClient.add(new Phrase(
                "Tél : " + client.getTelephone() + "\n", FONT_LABEL));
        if (client.getAdresse() != null
                && !client.getAdresse().isEmpty()) {
            pClient.add(new Phrase(
                    "Adresse : " + client.getAdresse() + "\n",
                    FONT_LABEL));
        }
        pClient.add(new Phrase(
                "Pièce : " + (client.getTypePiece() != null
                        ? client.getTypePiece() : "—")
                + " N° " + (client.getNumCarteIdentite() != null
                        ? client.getNumCarteIdentite() : "—"),
                FONT_LABEL));
        cellClient.addElement(pClient);
        tableParties.addCell(cellClient);

        doc.add(tableParties);

        // ── Conditions du prêt ──
        ajouterSeparateur(doc, "CONDITIONS DU PRÊT");

        double interets = pret.getMontantPrincipal()
                * pret.getTauxInteret()
                * pret.getDureeMois() / 1200.0;
        double total = pret.getMontantPrincipal() + interets;
        double mensualite = total / pret.getDureeMois();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("dd/MM/yyyy");

        PdfPTable tableConditions = new PdfPTable(2);
        tableConditions.setWidthPercentage(100);
        tableConditions.setSpacingBefore(8);
        tableConditions.setSpacingAfter(8);

        ajouterLigne2Col(tableConditions, "Montant accordé",
                formaterMontant(pret.getMontantPrincipal()));
        ajouterLigne2Col(tableConditions, "Taux d'intérêt mensuel",
                pret.getTauxInteret() + " %");
        ajouterLigne2Col(tableConditions, "Durée",
                pret.getDureeMois() + " mois");
        ajouterLigne2Col(tableConditions, "Intérêts totaux",
                formaterMontant(interets));
        ajouterLigne2Col(tableConditions, "Total à rembourser",
                formaterMontant(total));
        ajouterLigne2Col(tableConditions, "Mensualité",
                formaterMontant(mensualite));
        ajouterLigne2Col(tableConditions, "Date de début",
                pret.getDateDebut() != null
                        ? sdf.format(pret.getDateDebut()) : "—");
        ajouterLigne2Col(tableConditions, "Date de fin prévue",
                pret.getDateFinPrevue() != null
                        ? sdf.format(pret.getDateFinPrevue()) : "—");
        ajouterLigne2Col(tableConditions, "Garantie",
                pret.getGarantie() != null
                        && !pret.getGarantie().isEmpty()
                        ? pret.getGarantie() : "—");
        ajouterLigne2Col(tableConditions, "N° Compte épargne",
                compte.getNumeroCompte());
        doc.add(tableConditions);

        // ── Montant total mis en valeur ──
        PdfPTable tableMontant = new PdfPTable(1);
        tableMontant.setWidthPercentage(100);
        tableMontant.setSpacingBefore(8);
        tableMontant.setSpacingAfter(12);

        PdfPCell cellTotal = new PdfPCell();
        cellTotal.setBackgroundColor(new java.awt.Color(220, 252, 231));
        cellTotal.setBorder(Rectangle.NO_BORDER);
        cellTotal.setPadding(12);

        Paragraph pTotal = new Paragraph(
                "TOTAL À REMBOURSER : " + formaterMontant(total),
                new Font(Font.HELVETICA, 13, Font.BOLD,
                        new java.awt.Color(21, 128, 61)));
        pTotal.setAlignment(Element.ALIGN_CENTER);
        cellTotal.addElement(pTotal);

        Paragraph pMens = new Paragraph(
                "Mensualité : " + formaterMontant(mensualite)
                + "  ×  " + pret.getDureeMois() + " mois",
                new Font(Font.HELVETICA, 10, Font.NORMAL,
                        new java.awt.Color(21, 128, 61)));
        pMens.setAlignment(Element.ALIGN_CENTER);
        cellTotal.addElement(pMens);

        tableMontant.addCell(cellTotal);
        doc.add(tableMontant);

        // ── Clauses ──
        ajouterSeparateur(doc, "CLAUSES ET CONDITIONS");

        String[] clauses = {
            "1. L'emprunteur s'engage à rembourser le prêt selon "
            + "l'échéancier convenu.",
            "2. Tout retard de paiement entraîne des pénalités "
            + "conformément à la réglementation BCEAO.",
            "3. En cas de non-remboursement, la garantie fournie "
            + "pourra être saisie.",
            "4. Le taux d'intérêt appliqué respecte le plafond "
            + "légal fixé par la BCEAO (27% max).",
            "5. L'emprunteur peut procéder à un remboursement "
            + "anticipé avec remise de 50% sur les intérêts restants.",
            "6. Tout litige sera soumis aux juridictions "
            + "compétentes de Lomé, Togo."
        };

        for (String clause : clauses) {
            Paragraph pClause = new Paragraph(clause, FONT_LABEL);
            pClause.setSpacingAfter(4);
            doc.add(pClause);
        }

        // ── Signatures ──
        doc.add(new Paragraph(" "));
        ajouterSeparateur(doc, "SIGNATURES");
        doc.add(new Paragraph(" "));

        PdfPTable tableSign = new PdfPTable(2);
        tableSign.setWidthPercentage(100);
        tableSign.setSpacingBefore(8);

        // Signature microfinance
        PdfPCell cellSignMicro = new PdfPCell();
        cellSignMicro.setBorder(Rectangle.NO_BORDER);
        cellSignMicro.setPadding(8);
        Paragraph pSignMicro = new Paragraph();
        pSignMicro.add(new Phrase("Pour SmartCaisse\n\n\n\n",
                FONT_LABEL));
        pSignMicro.add(new Phrase("_______________________\n",
                FONT_LABEL));
        pSignMicro.add(new Phrase("Signature & Cachet",
                FONT_SMALL));
        cellSignMicro.addElement(pSignMicro);
        tableSign.addCell(cellSignMicro);

        // Signature client
        PdfPCell cellSignClient = new PdfPCell();
        cellSignClient.setBorder(Rectangle.NO_BORDER);
        cellSignClient.setPadding(8);
        Paragraph pSignClient = new Paragraph();
        pSignClient.add(new Phrase(
                "L'emprunteur : "
                + client.getNom() + " " + client.getPrenom()
                + "\n\n\n\n", FONT_LABEL));
        pSignClient.add(new Phrase("_______________________\n",
                FONT_LABEL));
        pSignClient.add(new Phrase("Signature précédée de\n"
                + "\"Lu et approuvé\"", FONT_SMALL));
        cellSignClient.addElement(pSignClient);
        tableSign.addCell(cellSignClient);

        doc.add(tableSign);

        // ── Pied de page ──
        ajouterPiedDePage(doc);

        doc.close();

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(fichier);
        }

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null,
                "Erreur génération contrat :\n" + e.getMessage(),
                "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    public static void imprimerRapportJournalier(
        java.util.Date date,
        double depots, double retraits,
        double remb, double decaisse, double solde,
        java.util.List<String[]> lignes) {

    try {
        File fichier = File.createTempFile(
                "rapport_journalier_", ".pdf");
        fichier.deleteOnExit();

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc,
                new FileOutputStream(fichier));
        doc.open();

        ajouterEntete(doc, "SmartCaisse",
                "Microfinance — Lomé, Togo");

        // Titre
        String dateStr = new java.text.SimpleDateFormat(
                "EEEE dd MMMM yyyy",
                new java.util.Locale("fr", "FR"))
                .format(date);
        Paragraph titre = new Paragraph(
                "RAPPORT JOURNALIER", new Font(
                        Font.HELVETICA, 15, Font.BOLD,
                        new java.awt.Color(15, 23, 42)));
        titre.setAlignment(Element.ALIGN_CENTER);
        doc.add(titre);

        Paragraph pDate = new Paragraph(dateStr,
                FONT_SOUS_TITRE);
        pDate.setAlignment(Element.ALIGN_CENTER);
        pDate.setSpacingAfter(12);
        doc.add(pDate);

        // Résumé
        ajouterSeparateur(doc, "RÉSUMÉ DE LA JOURNÉE");
        ajouterTableauResume(doc, depots, retraits,
                remb, decaisse, solde);

        // Détail
        ajouterSeparateur(doc, "DÉTAIL DES TRANSACTIONS");
        ajouterTableauTransactions(doc, lignes);

        ajouterPiedDePage(doc);
        doc.close();

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(fichier);
        }

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null,
                "Erreur rapport journalier :\n"
                + e.getMessage(), "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

public static void imprimerRapportMensuel(
        int mois, int annee,
        double depots, double retraits,
        double remb, double decaisse, double solde,
        java.util.List<String[]> lignes) {

    try {
        File fichier = File.createTempFile(
                "rapport_mensuel_", ".pdf");
        fichier.deleteOnExit();

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc,
                new FileOutputStream(fichier));
        doc.open();

        ajouterEntete(doc, "SmartCaisse",
                "Microfinance — Lomé, Togo");

        String[] moisNoms = {
            "Janvier", "Février", "Mars", "Avril",
            "Mai", "Juin", "Juillet", "Août",
            "Septembre", "Octobre", "Novembre", "Décembre"
        };

        Paragraph titre = new Paragraph(
                "RAPPORT MENSUEL", new Font(
                        Font.HELVETICA, 15, Font.BOLD,
                        new java.awt.Color(15, 23, 42)));
        titre.setAlignment(Element.ALIGN_CENTER);
        doc.add(titre);

        Paragraph pDate = new Paragraph(
                moisNoms[mois - 1] + " " + annee,
                FONT_SOUS_TITRE);
        pDate.setAlignment(Element.ALIGN_CENTER);
        pDate.setSpacingAfter(12);
        doc.add(pDate);

        ajouterSeparateur(doc, "RÉSUMÉ DU MOIS");
        ajouterTableauResume(doc, depots, retraits,
                remb, decaisse, solde);

        ajouterSeparateur(doc, "DÉTAIL DES TRANSACTIONS");
        ajouterTableauTransactions(doc, lignes);

        ajouterPiedDePage(doc);
        doc.close();

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(fichier);
        }

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null,
                "Erreur rapport mensuel :\n"
                + e.getMessage(), "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

// ── Méthodes privées partagées ──
private static void ajouterTableauResume(
        Document doc,
        double depots, double retraits,
        double remb, double decaisse,
        double solde) throws Exception {

    PdfPTable table = new PdfPTable(2);
    table.setWidthPercentage(60);
    table.setHorizontalAlignment(Element.ALIGN_LEFT);
    table.setSpacingBefore(8);
    table.setSpacingAfter(12);

    ajouterLigne2Col(table, "Total dépôts",
            formaterMontant(depots));
    ajouterLigne2Col(table, "Total retraits",
            formaterMontant(retraits));
    ajouterLigne2Col(table, "Total remboursements",
            formaterMontant(remb));
    ajouterLigne2Col(table, "Total décaissements",
            formaterMontant(decaisse));
    doc.add(table);

    // Solde net mis en valeur
    PdfPTable tableSolde = new PdfPTable(1);
    tableSolde.setWidthPercentage(60);
    tableSolde.setHorizontalAlignment(Element.ALIGN_LEFT);
    tableSolde.setSpacingAfter(12);

    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(solde >= 0
            ? new java.awt.Color(220, 252, 231)
            : new java.awt.Color(254, 226, 226));
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setPadding(10);

    Paragraph p = new Paragraph(
            "SOLDE NET : "
            + (solde >= 0 ? "+" : "")
            + formaterMontant(solde),
            new Font(Font.HELVETICA, 12, Font.BOLD,
                    solde >= 0
                    ? new java.awt.Color(21, 128, 61)
                    : new java.awt.Color(220, 38, 38)));
    p.setAlignment(Element.ALIGN_CENTER);
    cell.addElement(p);
    tableSolde.addCell(cell);
    doc.add(tableSolde);
}

private static void ajouterTableauTransactions(
        Document doc,
        java.util.List<String[]> lignes) throws Exception {

    if (lignes.isEmpty()) {
        Paragraph p = new Paragraph(
                "Aucune transaction pour cette période.",
                FONT_LABEL);
        p.setSpacingBefore(8);
        doc.add(p);
        return;
    }

    PdfPTable table = new PdfPTable(6);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{2.5f, 1.8f, 2f, 1.8f, 1.5f, 1.8f});
    table.setSpacingBefore(8);

    // En-tête tableau
    String[] entetes = {"Client", "Compte",
        "Type", "Montant", "Moyen", "Date"};
    for (String e : entetes) {
        PdfPCell cell = new PdfPCell(new Phrase(e,
                new Font(Font.HELVETICA, 9,
                        Font.BOLD, java.awt.Color.WHITE)));
        cell.setBackgroundColor(
                new java.awt.Color(15, 23, 42));
        cell.setPadding(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    // Lignes
    boolean pair = true;
    for (String[] ligne : lignes) {
        java.awt.Color bg = pair
                ? java.awt.Color.WHITE
                : new java.awt.Color(248, 250, 252);
        for (int i = 0; i < ligne.length; i++) {
            Font f = (i == 3)
                    ? new Font(Font.HELVETICA, 9,
                            Font.BOLD,
                            ligne[i].startsWith("+")
                            ? new java.awt.Color(21, 128, 61)
                            : new java.awt.Color(220, 38, 38))
                    : FONT_LABEL;
            PdfPCell cell = new PdfPCell(
                    new Phrase(ligne[i], f));
            cell.setBackgroundColor(bg);
            cell.setPadding(5);
            cell.setBorderColor(
                    new java.awt.Color(241, 245, 249));
        table.addCell(cell);
        }
        pair = !pair;
    }
    doc.add(table);
}
public static void imprimerEntetePublic(
        com.lowagie.text.Document doc,
        String nom, String sousTitre) throws Exception {
    ajouterEntete(doc, nom, sousTitre);
}

public static void imprimerPiedDePagePublic(
        com.lowagie.text.Document doc) throws Exception {
    ajouterPiedDePage(doc);
}
}