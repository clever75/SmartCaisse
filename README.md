# SmartCaisse

Application de bureau (Windows) de gestion d'une structure de microfinance : clients, comptes, prêts, remboursements, transactions, rapports.

![Aperçu de SmartCaisse](docs/apercu.jpg)

## Le problème

Les petites structures de microfinance doivent suivre prêts et remboursements au quotidien, mais les logiciels existants sont chers et peu adaptés au contexte local (montants en F CFA, épargne, tontine).

## Fonctionnalités

- **Clients** : fiche complète, modification, détail.
- **Comptes** : plusieurs types (courant, à terme, tontine), solde, clôture, numéro de compte unique.
- **Prêts** : création avec taux et durée, intérêts simples (`capital × taux × durée / 1200`, taux annuel en %), suivi des remboursements.
- **Règles d'éligibilité** appliquées automatiquement avant tout prêt : aucun prêt en cours sur le compte, compte ouvert depuis au moins 3 mois, solde minimum de 5 000 F CFA.
- **Pénalités de retard** : 5 % du reste dû par mois de retard.
- **Transactions** : dépôt, retrait, virement, remboursement.
- **Tableau de bord** avec alerte sur les **prêts critiques** et les échéances.
- **Documents** : reçu de remboursement, contrat de prêt, rapport journalier (PDF) ; états de gestion et export CSV lisible dans Excel.
- Connexion avec comptes utilisateurs actifs / inactifs.

## Stack

Java (projet NetBeans configuré pour Java 25) · Swing · FlatLaf · MySQL / MariaDB via JDBC · iText (PDF) · JFreeChart · installeur Inno Setup.

## Architecture

```
src/Models/   Client, Compte, Pret, Transaction, Utilisateur
src/DAO/      accès à la base + règles métier des prêts (PretDAO)
src/Views/    interfaces Swing
src/Utils/    impression PDF et états
lib/          bibliothèques .jar
```

## Installation pour le développement

Prérequis : JDK (la version configurée dans NetBeans est la 25, adapter *Project Properties → Sources → Source/Binary Format* si votre JDK est plus ancien), NetBeans, MySQL ou MariaDB.

1. Créer la base `smartcaisse` puis importer `database/smartcaisse.sql`.
2. Connexion dans `src/DAO/Connexion.java` (par défaut : `localhost:3306`, `root`, mot de passe vide).
3. Ouvrir le dossier dans NetBeans, lancer le projet (classe principale `Views.LoginFrame`).
4. Créer un premier utilisateur actif dans la table `Utilisateur`.

## Utilisation sans développement

Télécharger `SmartCaisse_Setup_v1.0.exe` dans la page [Releases](https://github.com/clever75/SmartCaisse/releases). MySQL doit tourner sur la machine.

## Ce que j'ai appris

- Traduire une logique métier financière (intérêts, pénalités, échéances, éligibilité) en code, puis en interface claire.
- Garder la cohérence des données quand plusieurs opérations dépendent les unes des autres (solde, remboursement, statut du prêt).
- Générer des documents PDF (reçus, contrats) avec iText.

## Limites connues et pistes d'amélioration

- Mots de passe stockés en clair : remplacement par BCrypt prévu.
- Identifiants de base codés en dur dans `Connexion.java` : à externaliser.
- Pas de tests automatisés sur les calculs financiers, qui en auraient pourtant le plus besoin.

## Contexte

Projet réalisé en 2026 dans le cadre du BTS en développement d'applications à DEFITECH (Lomé).

**Auteur** : ASSAN Ablavi Clever, [GitHub](https://github.com/clever75)
