package utils;

import DAO.ClientDAO;
import DAO.CompteDAO;
import DAO.PretDAO;
import DAO.TransactionDAO;
import Models.Client;
import Models.Compte;
import Models.Pret;
import Models.Transaction;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * États imprimables et exports Excel pour SmartCaisse.
 * Appel depuis n'importe quel panel :
 *   utils.EtatsHelperSmartCaisse.etatClients();
 *   utils.EtatsHelperSmartCaisse.exportExcelClients();
 *   etc.
 *
 * @author ASSAN Ablavi Clever
 */
public class EtatsHelperSmartCaisse {

    // ── Formatage monétaire FCFA ──────────────────────────────────────────────
    private static final NumberFormat NF = NumberFormat.getInstance(Locale.FRANCE);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat SDF_DATETIME =
            new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static String fcfa(double montant) {
        return NF.format((long) montant) + " F CFA";
    }

    private static String date(java.sql.Date d) {
        if (d == null) return "—";
        return SDF.format(d);
    }

    private static String dateHeure(java.sql.Timestamp ts) {
        if (ts == null) return "—";
        return SDF_DATETIME.format(ts);
    }

    // ── Badge statut HTML ─────────────────────────────────────────────────────
    private static String badgeClient(String statut) {
        if (statut == null) return "<span class='badge badge-gris'>—</span>";
        return switch (statut) {
            case "Actif"   -> "<span class='badge badge-vert'>Actif</span>";
            case "Inactif" -> "<span class='badge badge-rouge'>Inactif</span>";
            default        -> "<span class='badge badge-gris'>" + statut + "</span>";
        };
    }

    private static String badgePret(String statut) {
        if (statut == null) return "<span class='badge badge-gris'>—</span>";
        return switch (statut) {
            case "En cours"   -> "<span class='badge badge-bleu'>En cours</span>";
            case "En retard"  -> "<span class='badge badge-rouge'>En retard</span>";
            case "Remboursé"  -> "<span class='badge badge-vert'>Remboursé</span>";
            default           -> "<span class='badge badge-gris'>" + statut + "</span>";
        };
    }

    private static String badgeCompte(String statut) {
        if (statut == null) return "<span class='badge badge-gris'>—</span>";
        return switch (statut) {
            case "Actif"    -> "<span class='badge badge-vert'>Actif</span>";
            case "Clôturé"  -> "<span class='badge badge-gris'>Clôturé</span>";
            default         -> "<span class='badge badge-gris'>" + statut + "</span>";
        };
    }

    private static String badgeTrans(String type) {
        if (type == null) return "<span class='badge badge-gris'>—</span>";
        boolean depot = type.toLowerCase().contains("dépôt")
                     || type.equals("Décaissement");
        return depot
            ? "<span class='badge badge-vert'>" + type + "</span>"
            : "<span class='badge badge-rouge'>" + type + "</span>";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // STYLES HTML communs
    // ═══════════════════════════════════════════════════════════════════════════
    private static String css() {
        return """
            <style>
              @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap');
              * { box-sizing: border-box; margin: 0; padding: 0; }
              body { font-family: 'Inter', Arial, sans-serif; font-size: 13px;
                     color: #1a1a2e; background: #f4f6f9; padding: 24px; }
              .page { background: white; max-width: 1100px; margin: 0 auto;
                      border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,.1);
                      overflow: hidden; }
              .header { background: linear-gradient(135deg,#1a2035,#2d3561);
                        color: white; padding: 28px 36px; }
              .header h1 { font-size: 22px; font-weight: 700; letter-spacing: .5px; }
              .header .subtitle { font-size: 13px; opacity: .75; margin-top: 4px; }
              .header .date-print { font-size: 12px; opacity: .65; margin-top: 8px; }
              .kpis { display: flex; gap: 16px; padding: 20px 36px;
                      background: #f8f9fc; border-bottom: 1px solid #eee; flex-wrap: wrap; }
              .kpi { background: white; border-radius: 8px; padding: 14px 20px;
                     flex: 1; min-width: 160px;
                     box-shadow: 0 1px 4px rgba(0,0,0,.08); }
              .kpi .val { font-size: 22px; font-weight: 700; color: #1a2035; }
              .kpi .lbl { font-size: 11px; color: #888; margin-top: 3px; }
              .kpi.accent .val { color: #d4a843; }
              .kpi.danger .val { color: #e05252; }
              .kpi.success .val { color: #3bad72; }
              .content { padding: 24px 36px 36px; }
              table { width: 100%; border-collapse: collapse; margin-top: 16px; }
              th { background: #1a2035; color: white; padding: 10px 12px;
                   text-align: left; font-size: 12px; font-weight: 600;
                   letter-spacing: .3px; }
              td { padding: 9px 12px; border-bottom: 1px solid #f0f0f0;
                   font-size: 12px; vertical-align: middle; }
              tr:nth-child(even) td { background: #fafbff; }
              tr:hover td { background: #f0f4ff; }
              .badge { display: inline-block; padding: 2px 10px; border-radius: 12px;
                       font-size: 11px; font-weight: 600; }
              .badge-vert  { background: #e6f9ef; color: #1e7e4a; }
              .badge-rouge { background: #fdeaea; color: #c0392b; }
              .badge-bleu  { background: #e8f0fe; color: #1a56db; }
              .badge-gris  { background: #f0f0f0; color: #666; }
              .badge-or    { background: #fff8e1; color: #b8860b; }
              .footer { background: #f8f9fc; border-top: 1px solid #eee;
                        padding: 14px 36px; font-size: 11px; color: #aaa;
                        display: flex; justify-content: space-between; }
              .alerte { background: #fdeaea; border-left: 4px solid #e05252;
                        padding: 12px 16px; margin-bottom: 16px; border-radius: 4px;
                        font-size: 12px; color: #c0392b; }
              @media print {
                body { background: white; padding: 0; }
                .page { box-shadow: none; border-radius: 0; }
              }
            </style>
            """;
    }

    private static String header(String titre, String sousTitre) {
        String dateStr = new SimpleDateFormat("dd/MM/yyyy à HH:mm")
                .format(new Date());
        return "<div class='header'>"
             + "<h1>🏦 SmartCaisse — " + titre + "</h1>"
             + "<div class='subtitle'>" + sousTitre + "</div>"
             + "<div class='date-print'>Imprimé le " + dateStr + "</div>"
             + "</div>";
    }

    private static String footer() {
        return "<div class='footer'>"
             + "<span>SmartCaisse — Système de gestion microfinance</span>"
             + "<span>ASSAN Ablavi Clever &nbsp;|&nbsp; BTS Développement d'applications</span>"
             + "</div>";
    }

    // ── Ouvrir HTML dans le navigateur ────────────────────────────────────────
    private static void ouvrir(String html, String nomFichier) {
        try {
            java.io.File f = java.io.File.createTempFile(nomFichier, ".html");
            java.io.FileWriter fw = new java.io.FileWriter(
                    f, java.nio.charset.StandardCharsets.UTF_8);
            fw.write("<!DOCTYPE html><html lang='fr'><head>"
                   + "<meta charset='UTF-8'>"
                   + "<title>SmartCaisse — " + nomFichier + "</title>"
                   + css()
                   + "</head><body><div class='page'>"
                   + html
                   + "</div></body></html>");
            fw.close();
            java.awt.Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Erreur génération état : " + e.getMessage(),
                "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ÉTAT 1 — Liste des clients
    // ═══════════════════════════════════════════════════════════════════════════
    public static void etatClients() {
        ClientDAO dao = new ClientDAO();
        List<Client> clients = dao.listerTous();

        long nbActifs   = clients.stream()
                .filter(c -> "Actif".equals(c.getStatut())).count();
        long nbInactifs = clients.size() - nbActifs;

        StringBuilder html = new StringBuilder();
        html.append(header("Liste des clients",
                "Tous les membres enregistrés"));

        html.append("<div class='kpis'>")
            .append("<div class='kpi'><div class='val'>").append(clients.size())
            .append("</div><div class='lbl'>Total clients</div></div>")
            .append("<div class='kpi success'><div class='val'>").append(nbActifs)
            .append("</div><div class='lbl'>Actifs</div></div>")
            .append("<div class='kpi danger'><div class='val'>").append(nbInactifs)
            .append("</div><div class='lbl'>Inactifs</div></div>")
            .append("</div>");

        html.append("<div class='content'>")
            .append("<table>")
            .append("<tr><th>#</th><th>Nom & Prénom</th><th>Téléphone</th>")
            .append("<th>Profession</th><th>Revenu mensuel</th>")
            .append("<th>Date inscription</th><th>Statut</th></tr>");

        int i = 1;
        for (Client c : clients) {
            html.append("<tr>")
                .append("<td>").append(i++).append("</td>")
                .append("<td><strong>").append(c.getNom()).append("</strong> ")
                .append(c.getPrenom()).append("</td>")
                .append("<td>").append(nvl(c.getTelephone())).append("</td>")
                .append("<td>").append(nvl(c.getProfession())).append("</td>")
                .append("<td>").append(fcfa(c.getRevenuMensuel())).append("</td>")
                .append("<td>").append(date(c.getDateInscription())).append("</td>")
                .append("<td>").append(badgeClient(c.getStatut())).append("</td>")
                .append("</tr>");
        }
        html.append("</table></div>");
        html.append(footer());

        ouvrir(html.toString(), "SmartCaisse_Clients");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ÉTAT 2 — Liste des comptes
    // ═══════════════════════════════════════════════════════════════════════════
    public static void etatComptes() {
        CompteDAO dao = new CompteDAO();
        ClientDAO clientDao = new ClientDAO();
        List<Compte> actifs   = dao.listerActifs();
        List<Compte> cloturer = dao.listerCloturer();

        double totalEpargne = actifs.stream()
                .mapToDouble(Compte::getSoldeActuel).sum();

        StringBuilder html = new StringBuilder();
        html.append(header("Liste des comptes",
                "Comptes actifs et clôturés"));

        html.append("<div class='kpis'>")
            .append("<div class='kpi success'><div class='val'>").append(actifs.size())
            .append("</div><div class='lbl'>Comptes actifs</div></div>")
            .append("<div class='kpi'><div class='val'>").append(cloturer.size())
            .append("</div><div class='lbl'>Comptes clôturés</div></div>")
            .append("<div class='kpi accent'><div class='val'>").append(fcfa(totalEpargne))
            .append("</div><div class='lbl'>Total épargne</div></div>")
            .append("</div>");

        html.append("<div class='content'>")
            .append("<table>")
            .append("<tr><th>N° Compte</th><th>Client</th><th>Type</th>")
            .append("<th>Solde actuel</th><th>Taux</th>")
            .append("<th>Date ouverture</th><th>Statut</th></tr>");

        // Actifs d'abord, clôturés ensuite
        for (Compte c : actifs)   appendLigneCompte(html, c, clientDao);
        for (Compte c : cloturer) appendLigneCompte(html, c, clientDao);

        html.append("</table></div>");
        html.append(footer());

        ouvrir(html.toString(), "SmartCaisse_Comptes");
    }

    private static void appendLigneCompte(StringBuilder html, Compte c,
            ClientDAO clientDao) {
        Client client = clientDao.chercher(c.getIdClient());
        String nomClient = client != null
                ? client.getNom() + " " + client.getPrenom() : "—";
        html.append("<tr>")
            .append("<td><strong>").append(nvl(c.getNumeroCompte()))
            .append("</strong></td>")
            .append("<td>").append(nomClient).append("</td>")
            .append("<td>").append(nvl(c.getTypeCompte())).append("</td>")
            .append("<td>").append(fcfa(c.getSoldeActuel())).append("</td>")
            .append("<td>").append(c.getTauxInteret()).append("%</td>")
            .append("<td>").append(date(c.getDateOuverture())).append("</td>")
            .append("<td>").append(badgeCompte(c.getStatut())).append("</td>")
            .append("</tr>");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ÉTAT 3 — Prêts en cours
    // ═══════════════════════════════════════════════════════════════════════════
    public static void etatPretsEnCours() {
        PretDAO pretDao = new PretDAO();
        CompteDAO compteDao = new CompteDAO();
        ClientDAO clientDao = new ClientDAO();
        pretDao.mettreAJourStatuts(); // Mise à jour automatique avant affichage

        List<Pret> tous = pretDao.listerTous();
        List<Pret> encours = tous.stream()
                .filter(p -> "En cours".equals(p.getStatut())
                          || "En retard".equals(p.getStatut()))
                .toList();

        double totalDecaisse  = encours.stream()
                .mapToDouble(Pret::getMontantPrincipal).sum();
        double totalRestant   = encours.stream()
                .mapToDouble(Pret::getMontantRestant).sum();
        long   nbRetard       = encours.stream()
                .filter(p -> "En retard".equals(p.getStatut())).count();

        StringBuilder html = new StringBuilder();
        html.append(header("Prêts en cours",
                "Prêts actifs et en retard"));

        html.append("<div class='kpis'>")
            .append("<div class='kpi'><div class='val'>").append(encours.size())
            .append("</div><div class='lbl'>Prêts actifs</div></div>")
            .append("<div class='kpi accent'><div class='val'>")
            .append(fcfa(totalDecaisse))
            .append("</div><div class='lbl'>Total décaissé</div></div>")
            .append("<div class='kpi'><div class='val'>").append(fcfa(totalRestant))
            .append("</div><div class='lbl'>Reste à rembourser</div></div>")
            .append("<div class='kpi danger'><div class='val'>").append(nbRetard)
            .append("</div><div class='lbl'>En retard</div></div>")
            .append("</div>");

        if (nbRetard > 0) {
            html.append("<div class='content'>")
                .append("<div class='alerte'>⚠ ").append(nbRetard)
                .append(" prêt(s) en retard — pénalités en cours de calcul.</div>");
        } else {
            html.append("<div class='content'>");
        }

        html.append("<table>")
            .append("<tr><th>Client</th><th>N° Compte</th><th>Capital</th>")
            .append("<th>Taux</th><th>Durée</th><th>Mensualité</th>")
            .append("<th>Remboursé</th><th>Restant</th>")
            .append("<th>Date fin prévue</th><th>Statut</th></tr>");

        for (Pret p : encours) {
            Compte  compte = compteDao.chercher(p.getIdCompte());
            Client  client = compte != null
                    ? clientDao.chercher(compte.getIdClient()) : null;
            String  nomClient = client != null
                    ? client.getNom() + " " + client.getPrenom() : "—";
            String  numCompte = compte != null
                    ? compte.getNumeroCompte() : "—";

            String trClass = "En retard".equals(p.getStatut())
                    ? " style='background:#fff5f5;'" : "";

            html.append("<tr").append(trClass).append(">")
                .append("<td><strong>").append(nomClient).append("</strong></td>")
                .append("<td>").append(numCompte).append("</td>")
                .append("<td>").append(fcfa(p.getMontantPrincipal())).append("</td>")
                .append("<td>").append(p.getTauxInteret()).append("%</td>")
                .append("<td>").append(p.getDureeMois()).append(" mois</td>")
                .append("<td>").append(fcfa(p.getMontantEcheance())).append("</td>")
                .append("<td>").append(fcfa(p.getMontantRembourse())).append("</td>")
                .append("<td><strong>").append(fcfa(p.getMontantRestant()))
                .append("</strong></td>")
                .append("<td>").append(date(p.getDateFinPrevue())).append("</td>")
                .append("<td>").append(badgePret(p.getStatut())).append("</td>")
                .append("</tr>");
        }
        html.append("</table></div>");
        html.append(footer());

        ouvrir(html.toString(), "SmartCaisse_PretsEnCours");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ÉTAT 4 — Prêts en retard avec pénalités
    // ═══════════════════════════════════════════════════════════════════════════
    public static void etatRetards() {
        PretDAO pretDao = new PretDAO();
        CompteDAO compteDao = new CompteDAO();
        ClientDAO clientDao = new ClientDAO();
        pretDao.mettreAJourStatuts();

        List<Pret> retards = pretDao.listerEnRetard();

        double totalPenalites = 0;
        for (Pret p : retards) totalPenalites += p.getPenalite();

        StringBuilder html = new StringBuilder();
        html.append(header("Rapport des retards",
                "Prêts dont l'échéance est dépassée — pénalités 5% par mois"));

        html.append("<div class='kpis'>")
            .append("<div class='kpi danger'><div class='val'>").append(retards.size())
            .append("</div><div class='lbl'>Prêts en retard</div></div>")
            .append("<div class='kpi danger'><div class='val'>")
            .append(fcfa(totalPenalites))
            .append("</div><div class='lbl'>Total pénalités dues</div></div>")
            .append("</div>");

        html.append("<div class='content'>");

        if (retards.isEmpty()) {
            html.append("<p style='color:#3bad72;font-weight:600;padding:20px 0;'>"
                      + "✅ Aucun prêt en retard. Tous les remboursements sont à jour !</p>");
        } else {
            html.append("<table>")
                .append("<tr><th>Client</th><th>Téléphone</th><th>N° Compte</th>")
                .append("<th>Capital</th><th>Restant</th>")
                .append("<th>Date fin prévue</th><th>Jours retard</th>")
                .append("<th>Mois retard</th><th>Pénalité (5%/mois)</th></tr>");

            for (Pret p : retards) {
                Compte compte = compteDao.chercher(p.getIdCompte());
                Client client = compte != null
                        ? clientDao.chercher(compte.getIdClient()) : null;
                String nomClient = client != null
                        ? client.getNom() + " " + client.getPrenom() : "—";
                String tel = client != null ? nvl(client.getTelephone()) : "—";
                String numCompte = compte != null
                        ? compte.getNumeroCompte() : "—";

                long joursRetard = 0;
                int  moisRetard  = 0;
                if (p.getDateFinPrevue() != null) {
                    long diffMillis = System.currentTimeMillis()
                            - p.getDateFinPrevue().getTime();
                    joursRetard = Math.max(0, diffMillis / (1000L * 60 * 60 * 24));
                    moisRetard  = (int) Math.max(1, joursRetard / 30);
                }

                html.append("<tr style='background:#fff5f5;'>")
                    .append("<td><strong>").append(nomClient).append("</strong></td>")
                    .append("<td>").append(tel).append("</td>")
                    .append("<td>").append(numCompte).append("</td>")
                    .append("<td>").append(fcfa(p.getMontantPrincipal())).append("</td>")
                    .append("<td>").append(fcfa(p.getMontantRestant())).append("</td>")
                    .append("<td>").append(date(p.getDateFinPrevue())).append("</td>")
                    .append("<td style='color:#e05252;font-weight:700;'>")
                    .append(joursRetard).append(" j").append("</td>")
                    .append("<td>").append(moisRetard).append(" mois</td>")
                    .append("<td style='color:#e05252;font-weight:700;'>")
                    .append(fcfa(p.getPenalite())).append("</td>")
                    .append("</tr>");
            }

            // Ligne total
            html.append("<tr style='background:#fdeaea;font-weight:700;'>")
                .append("<td colspan='8' style='text-align:right;'>")
                .append("TOTAL PÉNALITÉS :</td>")
                .append("<td style='color:#c0392b;'>")
                .append(fcfa(totalPenalites)).append("</td>")
                .append("</tr>");

            html.append("</table>");
        }

        html.append("</div>");
        html.append(footer());

        ouvrir(html.toString(), "SmartCaisse_Retards");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ÉTAT 5 — Historique des transactions
    // ═══════════════════════════════════════════════════════════════════════════
    public static void etatTransactions() {
        TransactionDAO dao = new TransactionDAO();
        CompteDAO compteDao = new CompteDAO();
        List<Transaction> transactions = dao.listerTous();

        double totalDepots      = transactions.stream()
                .filter(t -> "Validé".equals(t.getStatut())
                    && (t.getType().contains("Dépôt")
                     || t.getType().equals("Décaissement")))
                .mapToDouble(Transaction::getMontant).sum();
        double totalRetraits    = transactions.stream()
                .filter(t -> "Validé".equals(t.getStatut())
                    && t.getType().contains("Retrait"))
                .mapToDouble(Transaction::getMontant).sum();
        double totalRembours    = transactions.stream()
                .filter(t -> "Validé".equals(t.getStatut())
                    && t.getType().contains("Remboursement"))
                .mapToDouble(Transaction::getMontant).sum();

        StringBuilder html = new StringBuilder();
        html.append(header("Historique des transactions",
                "Toutes les opérations enregistrées"));

        html.append("<div class='kpis'>")
            .append("<div class='kpi'><div class='val'>").append(transactions.size())
            .append("</div><div class='lbl'>Total opérations</div></div>")
            .append("<div class='kpi success'><div class='val'>")
            .append(fcfa(totalDepots))
            .append("</div><div class='lbl'>Dépôts & décaissements</div></div>")
            .append("<div class='kpi danger'><div class='val'>")
            .append(fcfa(totalRetraits))
            .append("</div><div class='lbl'>Retraits</div></div>")
            .append("<div class='kpi accent'><div class='val'>")
            .append(fcfa(totalRembours))
            .append("</div><div class='lbl'>Remboursements</div></div>")
            .append("</div>");

        html.append("<div class='content'>")
            .append("<table>")
            .append("<tr><th>Date & heure</th><th>N° Compte</th>")
            .append("<th>Type d'opération</th><th>Montant</th>")
            .append("<th>Moyen de paiement</th><th>Statut</th></tr>");

        for (Transaction t : transactions) {
            Compte compte = compteDao.chercher(t.getIdCompte());
            String numCompte = compte != null ? compte.getNumeroCompte() : "—";

            html.append("<tr>")
                .append("<td>").append(dateHeure(t.getDateHeure())).append("</td>")
                .append("<td>").append(numCompte).append("</td>")
                .append("<td>").append(badgeTrans(t.getType())).append("</td>")
                .append("<td><strong>").append(fcfa(t.getMontant()))
                .append("</strong></td>")
                .append("<td>").append(nvl(t.getMoyenPaiement())).append("</td>")
                .append("<td>")
                .append("Validé".equals(t.getStatut())
                    ? "<span class='badge badge-vert'>Validé</span>"
                    : "<span class='badge badge-gris'>" + t.getStatut() + "</span>")
                .append("</td>")
                .append("</tr>");
        }

        html.append("</table></div>");
        html.append(footer());

        ouvrir(html.toString(), "SmartCaisse_Transactions");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ÉTAT 6 — Tableau de bord imprimable
    // ═══════════════════════════════════════════════════════════════════════════
    public static void etatTableauDeBord() {
        ClientDAO clientDao  = new ClientDAO();
        CompteDAO compteDao  = new CompteDAO();
        PretDAO   pretDao    = new PretDAO();
        TransactionDAO tDao  = new TransactionDAO();

        pretDao.mettreAJourStatuts();

        List<Client>      clients      = clientDao.listerTous();
        List<Compte>      comptesActifs= compteDao.listerActifs();
        List<Pret>        tousPretsL   = pretDao.listerTous();
        List<Pret>        retards      = pretDao.listerEnRetard();
        double[]          statsMois    = tDao.statsduMois();

        long nbClientsActifs = clients.stream()
                .filter(c -> "Actif".equals(c.getStatut())).count();
        long nbPretsEnCours  = tousPretsL.stream()
                .filter(p -> "En cours".equals(p.getStatut())
                          || "En retard".equals(p.getStatut())).count();
        double totalEpargne  = comptesActifs.stream()
                .mapToDouble(Compte::getSoldeActuel).sum();
        double totalCapital  = tousPretsL.stream()
                .filter(p -> "En cours".equals(p.getStatut())
                          || "En retard".equals(p.getStatut()))
                .mapToDouble(Pret::getMontantPrincipal).sum();
        double totalPenalite = retards.stream()
                .mapToDouble(Pret::getPenalite).sum();

        StringBuilder html = new StringBuilder();
        html.append(header("Tableau de bord",
                "Vue d'ensemble de l'activité"));

        html.append("<div class='kpis'>")
            .append("<div class='kpi'><div class='val'>").append(clients.size())
            .append("</div><div class='lbl'>Total clients</div></div>")
            .append("<div class='kpi success'><div class='val'>").append(nbClientsActifs)
            .append("</div><div class='lbl'>Clients actifs</div></div>")
            .append("<div class='kpi'><div class='val'>").append(comptesActifs.size())
            .append("</div><div class='lbl'>Comptes actifs</div></div>")
            .append("<div class='kpi accent'><div class='val'>").append(fcfa(totalEpargne))
            .append("</div><div class='lbl'>Total épargne</div></div>")
            .append("</div>");

        html.append("<div class='kpis' style='border-top:none;padding-top:0;'>")
            .append("<div class='kpi'><div class='val'>").append(nbPretsEnCours)
            .append("</div><div class='lbl'>Prêts en cours</div></div>")
            .append("<div class='kpi accent'><div class='val'>").append(fcfa(totalCapital))
            .append("</div><div class='lbl'>Capital prêté (en cours)</div></div>")
            .append("<div class='kpi danger'><div class='val'>").append(retards.size())
            .append("</div><div class='lbl'>Prêts en retard</div></div>")
            .append("<div class='kpi danger'><div class='val'>").append(fcfa(totalPenalite))
            .append("</div><div class='lbl'>Pénalités à recouvrer</div></div>")
            .append("</div>");

        // Stats du mois
        html.append("<div class='content'>")
            .append("<h3 style='font-size:14px;margin-bottom:12px;color:#1a2035;'>")
            .append("📊 Activité du mois en cours</h3>")
            .append("<table style='max-width:500px;'>")
            .append("<tr><th>Type</th><th>Montant</th></tr>")
            .append("<tr><td>Dépôts épargne</td><td>").append(fcfa(statsMois[0]))
            .append("</td></tr>")
            .append("<tr><td>Retraits épargne</td><td>").append(fcfa(statsMois[1]))
            .append("</td></tr>")
            .append("<tr><td>Décaissements (prêts)</td><td>").append(fcfa(statsMois[2]))
            .append("</td></tr>")
            .append("<tr><td>Remboursements reçus</td><td>").append(fcfa(statsMois[3]))
            .append("</td></tr>")
            .append("</table>");

        html.append("</div>");
        html.append(footer());

        ouvrir(html.toString(), "SmartCaisse_TableauDeBord");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EXPORTS EXCEL (CSV UTF-8)
    // ═══════════════════════════════════════════════════════════════════════════

    public static void exportExcelClients() {
        ClientDAO dao = new ClientDAO();
        List<Client> clients = dao.listerTous();

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF'); // BOM UTF-8
        csv.append("Nom;Prénom;Téléphone;Email;Adresse;Profession;"
                 + "Revenu mensuel (FCFA);Date inscription;Statut\n");

        for (Client c : clients) {
            csv.append(esc(c.getNom())).append(";")
               .append(esc(c.getPrenom())).append(";")
               .append(esc(c.getTelephone())).append(";")
               .append(esc(c.getEmail())).append(";")
               .append(esc(c.getAdresse())).append(";")
               .append(esc(c.getProfession())).append(";")
               .append((long) c.getRevenuMensuel()).append(";")
               .append(date(c.getDateInscription())).append(";")
               .append(esc(c.getStatut())).append("\n");
        }
        sauvegarderCSV(csv.toString(), "SmartCaisse_Clients");
    }

    public static void exportExcelComptes() {
        CompteDAO dao = new CompteDAO();
        ClientDAO clientDao = new ClientDAO();
        List<Compte> actifs   = dao.listerActifs();
        List<Compte> cloturer = dao.listerCloturer();

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("N° Compte;Client;Type;Solde (FCFA);Taux (%);"
                 + "Date ouverture;Date clôture;Statut\n");

        for (Compte c : actifs) {
            appendLigneCompteCSV(csv, c, clientDao);
        }
        for (Compte c : cloturer) {
            appendLigneCompteCSV(csv, c, clientDao);
        }
        sauvegarderCSV(csv.toString(), "SmartCaisse_Comptes");
    }

    private static void appendLigneCompteCSV(StringBuilder csv, Compte c,
            ClientDAO clientDao) {
        Client client = clientDao.chercher(c.getIdClient());
        String nomClient = client != null
                ? client.getNom() + " " + client.getPrenom() : "—";
        csv.append(esc(c.getNumeroCompte())).append(";")
           .append(esc(nomClient)).append(";")
           .append(esc(c.getTypeCompte())).append(";")
           .append((long) c.getSoldeActuel()).append(";")
           .append(c.getTauxInteret()).append(";")
           .append(date(c.getDateOuverture())).append(";")
           .append(date(c.getDateCloture())).append(";")
           .append(esc(c.getStatut())).append("\n");
    }

    public static void exportExcelPrets() {
        PretDAO pretDao = new PretDAO();
        CompteDAO compteDao = new CompteDAO();
        ClientDAO clientDao = new ClientDAO();
        pretDao.mettreAJourStatuts();

        List<Pret> prets = pretDao.listerTous();

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("Client;N° Compte;Capital (FCFA);Taux (%);Durée (mois);"
                 + "Mensualité (FCFA);Remboursé (FCFA);Restant (FCFA);"
                 + "Date début;Date fin prévue;Pénalité (FCFA);Statut\n");

        for (Pret p : prets) {
            Compte compte = compteDao.chercher(p.getIdCompte());
            Client client = compte != null
                    ? clientDao.chercher(compte.getIdClient()) : null;
            String nomClient = client != null
                    ? client.getNom() + " " + client.getPrenom() : "—";
            String numCompte = compte != null ? compte.getNumeroCompte() : "—";

            csv.append(esc(nomClient)).append(";")
               .append(esc(numCompte)).append(";")
               .append((long) p.getMontantPrincipal()).append(";")
               .append(p.getTauxInteret()).append(";")
               .append(p.getDureeMois()).append(";")
               .append((long) p.getMontantEcheance()).append(";")
               .append((long) p.getMontantRembourse()).append(";")
               .append((long) p.getMontantRestant()).append(";")
               .append(date(p.getDateDebut())).append(";")
               .append(date(p.getDateFinPrevue())).append(";")
               .append((long) p.getPenalite()).append(";")
               .append(esc(p.getStatut())).append("\n");
        }
        sauvegarderCSV(csv.toString(), "SmartCaisse_Prets");
    }

    public static void exportExcelTransactions() {
        TransactionDAO dao = new TransactionDAO();
        CompteDAO compteDao = new CompteDAO();
        List<Transaction> transactions = dao.listerTous();

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("Date & heure;N° Compte;Type;Montant (FCFA);"
                 + "Moyen de paiement;Statut\n");

        for (Transaction t : transactions) {
            Compte compte = compteDao.chercher(t.getIdCompte());
            String numCompte = compte != null ? compte.getNumeroCompte() : "—";

            csv.append(dateHeure(t.getDateHeure())).append(";")
               .append(esc(numCompte)).append(";")
               .append(esc(t.getType())).append(";")
               .append((long) t.getMontant()).append(";")
               .append(nvl(t.getMoyenPaiement())).append(";")
               .append(esc(t.getStatut())).append("\n");
        }
        sauvegarderCSV(csv.toString(), "SmartCaisse_Transactions");
    }

    // ── Utilitaires ───────────────────────────────────────────────────────────
    private static String nvl(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }

    private static String esc(String s) {
        if (s == null) return "";
        // Encadrer par guillemets si virgule ou point-virgule
        if (s.contains(";") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static void sauvegarderCSV(String contenu, String nomDefaut) {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle("Enregistrer le fichier Excel");
        fc.setSelectedFile(new java.io.File(nomDefaut + "_"
            + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Fichier CSV (Excel)", "csv"));

        int rep = fc.showSaveDialog(null);
        if (rep != javax.swing.JFileChooser.APPROVE_OPTION) return;

        java.io.File fichier = fc.getSelectedFile();
        if (!fichier.getName().endsWith(".csv")) {
            fichier = new java.io.File(fichier.getAbsolutePath() + ".csv");
        }

        try {
            java.io.FileWriter fw = new java.io.FileWriter(
                fichier, java.nio.charset.StandardCharsets.UTF_8);
            fw.write(contenu);
            fw.close();
            java.awt.Desktop.getDesktop().open(fichier);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Erreur export : " + e.getMessage(),
                "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}