-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: smartcaisse
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `smartcaisse`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `smartcaisse` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `smartcaisse`;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `idClient` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `dateNaissance` date DEFAULT NULL,
  `sexe` enum('Masculin','Féminin') NOT NULL,
  `situationMat` enum('Célibataire','Marié(e)','Divorcé(e)','Veuf/Veuve') DEFAULT NULL,
  `telephone` varchar(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `adresse` varchar(200) DEFAULT NULL,
  `numCarteIdentite` varchar(50) DEFAULT NULL,
  `typePiece` enum('CNI','Passeport','Permis de conduire') DEFAULT NULL,
  `profession` varchar(100) DEFAULT NULL,
  `revenuMensuel` decimal(15,2) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `dateInscription` date DEFAULT curdate(),
  `statut` enum('Actif','Inactif') DEFAULT 'Actif',
  PRIMARY KEY (`idClient`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `compte`
--

DROP TABLE IF EXISTS `compte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `compte` (
  `idCompte` int(11) NOT NULL AUTO_INCREMENT,
  `idClient` int(11) NOT NULL,
  `numeroCompte` varchar(20) NOT NULL,
  `typeCompte` enum('Courant','À terme','Tontine') NOT NULL,
  `soldeActuel` decimal(15,2) DEFAULT NULL,
  `tauxInteret` decimal(5,2) DEFAULT 0.00,
  `dateOuverture` date NOT NULL,
  `dateCloture` date DEFAULT NULL,
  `statut` enum('Actif','Clôturé') DEFAULT 'Actif',
  `duree` int(11) DEFAULT NULL,
  `montantPeriodique` decimal(15,2) DEFAULT NULL,
  `frequence` enum('Hebdomadaire','Mensuelle') DEFAULT NULL,
  PRIMARY KEY (`idCompte`),
  UNIQUE KEY `numeroCompte` (`numeroCompte`),
  KEY `idClient` (`idClient`),
  CONSTRAINT `compte_ibfk_1` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lier`
--

DROP TABLE IF EXISTS `lier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lier` (
  `idPret` int(11) NOT NULL,
  `idTransaction` int(11) NOT NULL,
  `montantAffecter` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`idPret`,`idTransaction`),
  KEY `idTransaction` (`idTransaction`),
  CONSTRAINT `lier_ibfk_1` FOREIGN KEY (`idPret`) REFERENCES `pret` (`idPret`),
  CONSTRAINT `lier_ibfk_2` FOREIGN KEY (`idTransaction`) REFERENCES `transaction` (`idTransaction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pret`
--

DROP TABLE IF EXISTS `pret`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pret` (
  `idPret` int(11) NOT NULL AUTO_INCREMENT,
  `idCompte` int(11) NOT NULL,
  `montantPrincipal` decimal(15,2) NOT NULL,
  `tauxInteret` decimal(5,2) NOT NULL,
  `dureeMois` int(11) NOT NULL,
  `dateDebut` date NOT NULL,
  `dateFinPrevue` date NOT NULL,
  `montantRembourse` decimal(15,2) DEFAULT 0.00,
  `statut` enum('En cours','Remboursé','En retard','Rejeté') DEFAULT 'En cours',
  `garantie` varchar(100) DEFAULT NULL,
  `motifRejet` text DEFAULT NULL,
  PRIMARY KEY (`idPret`),
  KEY `idCompte` (`idCompte`),
  CONSTRAINT `pret_ibfk_1` FOREIGN KEY (`idCompte`) REFERENCES `compte` (`idCompte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `idTransaction` int(11) NOT NULL AUTO_INCREMENT,
  `idCompte` int(11) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `montant` decimal(15,2) DEFAULT NULL,
  `dateHeure` datetime DEFAULT current_timestamp(),
  `moyenPaiement` varchar(50) DEFAULT NULL,
  `statut` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idTransaction`),
  KEY `idCompte` (`idCompte`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`idCompte`) REFERENCES `compte` (`idCompte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utilisateur` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `nomUtilisateur` varchar(50) NOT NULL,
  `motDePasse` varchar(255) NOT NULL,
  `role` enum('Admin','Agent','Caissier') DEFAULT 'Agent',
  `statut` enum('Actif','Inactif') DEFAULT 'Actif',
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `nomUtilisateur` (`nomUtilisateur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'smartcaisse'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed
