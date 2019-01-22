-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 20, 2018 at 04:56 PM
-- Server version: 5.6.38
-- PHP Version: 7.2.1

SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `client_cible`
--
CREATE DATABASE IF NOT EXISTS `client_cible` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `client_cible`;

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `client_adresse_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `client_adresse_procedure` (IN `idclient` INT, IN `adresse` VARCHAR(255))  BEGIN
			insert into client_adresse  (idclient,adresse) values (idclient,adresse);
END$$

DROP PROCEDURE IF EXISTS `client_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `client_procedure` (IN `idclient` INT, IN `nom` VARCHAR(45), `idclient_type` INT, IN `website` VARCHAR(45))  BEGIN
				insert into client (idclient,nom,idclient_type,website) values (idclient,nom,idclient_type,website)
                on duplicate key update nom =values (nom),idclient_type= values (idclient_type),website = values (website);
END$$

DROP PROCEDURE IF EXISTS `client_social_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `client_social_procedure` (IN `idclient` INT, IN `idsocial_network` INT, IN `id` VARCHAR(45))  BEGIN
				insert into client_social_network (idclient,idsocial_network,id) values (idclient,idsocial_network,id);
END$$

DROP PROCEDURE IF EXISTS `client_telephone_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `client_telephone_procedure` (IN `idclient` INT, IN `numero` VARCHAR(25))  BEGIN
				insert into client_telephone (idclient,numero) values (idclient,numero);
END$$

DROP PROCEDURE IF EXISTS `client_type_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `client_type_procedure` (IN `desc_type` VARCHAR(45))  BEGIN
				insert into client_type (desc_type) values (desc_type) on duplicate key update desc_type = values (desc_type);
END$$

DROP PROCEDURE IF EXISTS `client_vs_email_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `client_vs_email_procedure` (IN `idclient` INT, IN `courrier` VARCHAR(45))  BEGIN
			insert into client_vs_email (idclient,courrier) values (idclient,courrier);
END$$

DROP PROCEDURE IF EXISTS `social_network_procedure`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `social_network_procedure` (IN `name` VARCHAR(45))  BEGIN
			insert into social_network (name) values (name);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `idclient` int(11) UNSIGNED ZEROFILL NOT NULL,
  `clientcol` varchar(45) NOT NULL DEFAULT 'Clie#',
  `nom` varchar(45) DEFAULT NULL,
  `idclient_type` int(11) NOT NULL,
  `registration_dat` datetime DEFAULT CURRENT_TIMESTAMP,
  `website` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`idclient`, `clientcol`, `nom`, `idclient_type`, `registration_dat`, `website`) VALUES
(00000000001, 'Clie#', 'A & B HARDWARE', 1, '2018-03-17 14:41:30', 'www.abhardware.com'),
(00000000002, 'Clie#', 'CK HARDWARE', 1, '2018-03-18 13:57:16', 'wwww.ckhardware.net'),
(00000000003, 'Clie#', 'A & L QUINCAILLERIE', 1, '2018-03-18 14:20:32', ''),
(00000000004, 'Clie#', 'A & P HARDWARE', 1, '2018-03-18 14:24:24', 'aphardware20@gmail.com'),
(00000000005, 'Clie#', 'ALLINACE DISTRIBUTIONS S.A', 1, '2018-03-18 14:35:43', 'www.allianced.com'),
(00000000006, 'Clie#', 'ANTOINE KAWAS-ANKA', 1, '2018-03-18 14:39:09', 'www.anka.ht'),
(00000000007, 'Clie#', 'BENCO HARDWARE', 1, '2018-03-18 14:41:33', ''),
(00000000008, 'Clie#', 'DEPOT NATIONAL DE CONSTRUCTION S.A', 1, '2018-03-18 14:45:51', ''),
(00000000009, 'Clie#', 'MSC PLUS', 1, '2018-03-18 14:51:30', ''),
(00000000010, 'Clie#', 'MSC TRAIDING', 1, '2018-03-18 14:55:27', ''),
(00000000011, 'Clie#', 'BERACA HARDWARE', 1, '2018-03-18 14:56:53', '');

-- --------------------------------------------------------

--
-- Table structure for table `client_adresse`
--

DROP TABLE IF EXISTS `client_adresse`;
CREATE TABLE `client_adresse` (
  `idclient` int(11) UNSIGNED ZEROFILL NOT NULL,
  `clientcol` varchar(45) NOT NULL DEFAULT 'Clie#',
  `adresse` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client_adresse`
--

INSERT INTO `client_adresse` (`idclient`, `clientcol`, `adresse`) VALUES
(00000000001, 'Clie#', '3 A, Rue Derenoncourt, (Place Ti St Pierre), Petion-Ville'),
(00000000001, 'Clie#', '53, Route de l\'Aeroport, Delmas'),
(00000000004, 'Clie#', '20, Route de Freres (en face Imp St-Marc)'),
(00000000004, 'Clie#', 'Petion-Ville'),
(00000000005, 'Clie#', '27, Route du Canape-Vert, Juvenat, Petion-Ville'),
(00000000005, 'Clie#', '30, Blvd, Toussaint Louverture, Route de l\'Aeroport Delmas'),
(00000000006, 'Clie#', 'Bldg, 20, Blvd Toussaint Louverture Prolonge'),
(00000000006, 'Clie#', 'Carrefour Fleuriot (Airport Industrial Park)'),
(00000000006, 'Clie#', 'Tabarre'),
(00000000003, 'Clie#', '5, Route de Tabarre, Cazeau, Tabarre'),
(00000000007, 'Clie#', '25, Angle Delmas 19 & Rue C. Brouard, P>O Box 13278'),
(00000000007, 'Clie#', 'Delmas'),
(00000000008, 'Clie#', 'Chancerelles, Routes Nationale #1'),
(00000000008, 'Clie#', 'SHODECOSA'),
(00000000009, 'Clie#', '44, Boulevard 15 Octobre,Tabarre'),
(00000000002, 'Clie#', 'Tabarre Rue Ferrand de Beaudiere'),
(00000000002, 'Clie#', 'Bldg. 42, Airport Industrial Park,Fleuriot'),
(00000000002, 'Clie#', 'Entree de Peguy-Ville,Petion-Ville'),
(00000000010, 'Clie#', 'Drouillard, Sarthe, Route Nationale #1, Port-au-prince'),
(00000000011, 'Clie#', 'La Tremblay 1, Route de Malpasse, Croix-des-Bouquets');

-- --------------------------------------------------------

--
-- Table structure for table `client_social_network`
--

DROP TABLE IF EXISTS `client_social_network`;
CREATE TABLE `client_social_network` (
  `idclient` int(11) UNSIGNED ZEROFILL NOT NULL,
  `clientcol` varchar(45) NOT NULL DEFAULT 'Clie#',
  `idsocial_network` int(11) NOT NULL,
  `id` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client_social_network`
--

INSERT INTO `client_social_network` (`idclient`, `clientcol`, `idsocial_network`, `id`) VALUES
(00000000001, 'Clie#', 1, 'ABhardware'),
(00000000001, 'Clie#', 2, 'abhardware'),
(00000000002, 'Clie#', 1, 'Ck Hardware'),
(00000000002, 'Clie#', 2, 'ckhardware');

-- --------------------------------------------------------

--
-- Table structure for table `client_telephone`
--

DROP TABLE IF EXISTS `client_telephone`;
CREATE TABLE `client_telephone` (
  `idclient` int(11) UNSIGNED ZEROFILL NOT NULL,
  `clientcol` varchar(45) NOT NULL DEFAULT 'Clie#',
  `numero` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client_telephone`
--

INSERT INTO `client_telephone` (`idclient`, `clientcol`, `numero`) VALUES
(00000000001, 'Clie#', '+(509) 2813-1193'),
(00000000001, 'Clie#', '+(509) 2813-1194'),
(00000000001, 'Clie#', '+(509) 2813-1195'),
(00000000001, 'Clie#', '+(509) 2813-1196'),
(00000000004, 'Clie#', '+(509) 2813-1127'),
(00000000004, 'Clie#', '+(509) 3721-7561'),
(00000000005, 'Clie#', '+(509) 2813-0374'),
(00000000005, 'Clie#', '+(509) 3170-6330'),
(00000000005, 'Clie#', '+(509) 2816-0374'),
(00000000005, 'Clie#', '+(509) 2811-1961'),
(00000000006, 'Clie#', '+(509) 2816-2000'),
(00000000006, 'Clie#', '+(509) 2816-2001'),
(00000000006, 'Clie#', '+(509) 2816-2004'),
(00000000006, 'Clie#', '+(509) 2816-2002'),
(00000000006, 'Clie#', '+(509) 2816-2006'),
(00000000003, 'Clie#', '+(509) 4310-1828'),
(00000000007, 'Clie#', '+(509) 3734-7833'),
(00000000007, 'Clie#', '+(509) 3458-1499'),
(00000000007, 'Clie#', '+(509) 3376-4624'),
(00000000008, 'Clie#', '+(509) 2813-1020'),
(00000000009, 'Clie#', '+(509) 2945-7777'),
(00000000009, 'Clie#', '+(509) 3656-7777'),
(00000000009, 'Clie#', '+(509) 3330-7777'),
(00000000002, 'Clie#', '+(509) 3657-3333'),
(00000000002, 'Clie#', '+(509) 3671-3333'),
(00000000002, 'Clie#', '+(509) 3713-1313'),
(00000000002, 'Clie#', '+(509) 2947-3333'),
(00000000002, 'Clie#', '+(509) 2945-3333'),
(00000000010, 'Clie#', '+(509) 3499-1616'),
(00000000010, 'Clie#', '+(509) 3462-1111'),
(00000000011, 'Clie#', '+(509) 2817-6018');

-- --------------------------------------------------------

--
-- Table structure for table `client_type`
--

DROP TABLE IF EXISTS `client_type`;
CREATE TABLE `client_type` (
  `idclient_type` int(11) NOT NULL,
  `desc_type` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client_type`
--

INSERT INTO `client_type` (`idclient_type`, `desc_type`) VALUES
(1, 'Quincaillerie'),
(2, 'Depot Alimentaire'),
(3, 'Pharmacie');

-- --------------------------------------------------------

--
-- Table structure for table `client_vs_email`
--

DROP TABLE IF EXISTS `client_vs_email`;
CREATE TABLE `client_vs_email` (
  `idclient` int(11) UNSIGNED ZEROFILL NOT NULL,
  `clientcol` varchar(45) NOT NULL DEFAULT 'Clie#',
  `courrier` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client_vs_email`
--

INSERT INTO `client_vs_email` (`idclient`, `clientcol`, `courrier`) VALUES
(00000000001, 'Clie#', 'info@abhardware.com'),
(00000000005, 'Clie#', 'info@allianced.com'),
(00000000005, 'Clie#', 'sales@allainced.com'),
(00000000006, 'Clie#', 'info@anka.ht'),
(00000000008, 'Clie#', 'depotnational@gmail.com'),
(00000000009, 'Clie#', 'mscplushaiti@gmail.com'),
(00000000009, 'Clie#', 'msctraiding@aol.com'),
(00000000002, 'Clie#', 'ckhardware@ckhardware.net'),
(00000000010, 'Clie#', 'msctraidinghaiti@gmail.com'),
(00000000010, 'Clie#', 'msctraiding_depot@yahoo.com');

-- --------------------------------------------------------

--
-- Table structure for table `social_network`
--

DROP TABLE IF EXISTS `social_network`;
CREATE TABLE `social_network` (
  `idsocial_network` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `social_network`
--

INSERT INTO `social_network` (`idsocial_network`, `name`) VALUES
(1, 'Facebook'),
(2, 'Instagram'),
(3, 'Twitter'),
(4, 'Whatsapp');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`idclient`,`clientcol`),
  ADD KEY `fk_client_client_type_idx` (`idclient_type`);

--
-- Indexes for table `client_adresse`
--
ALTER TABLE `client_adresse`
  ADD KEY `fk_client_adresse_client1_idx` (`idclient`,`clientcol`);

--
-- Indexes for table `client_social_network`
--
ALTER TABLE `client_social_network`
  ADD KEY `fk_client_social_network_client1_idx` (`idclient`,`clientcol`),
  ADD KEY `fk_client_social_network_social_network1_idx` (`idsocial_network`);

--
-- Indexes for table `client_telephone`
--
ALTER TABLE `client_telephone`
  ADD KEY `fk_client_telephone_client1_idx` (`idclient`,`clientcol`);

--
-- Indexes for table `client_type`
--
ALTER TABLE `client_type`
  ADD PRIMARY KEY (`idclient_type`);

--
-- Indexes for table `client_vs_email`
--
ALTER TABLE `client_vs_email`
  ADD KEY `fk_client_vs_email_client1_idx` (`idclient`,`clientcol`);

--
-- Indexes for table `social_network`
--
ALTER TABLE `social_network`
  ADD PRIMARY KEY (`idsocial_network`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `client`
--
ALTER TABLE `client`
  MODIFY `idclient` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `client_type`
--
ALTER TABLE `client_type`
  MODIFY `idclient_type` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `social_network`
--
ALTER TABLE `social_network`
  MODIFY `idsocial_network` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `client`
--
ALTER TABLE `client`
  ADD CONSTRAINT `fk_client_client_type` FOREIGN KEY (`idclient_type`) REFERENCES `client_type` (`idclient_type`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `client_adresse`
--
ALTER TABLE `client_adresse`
  ADD CONSTRAINT `fk_client_adresse_client1` FOREIGN KEY (`idclient`,`clientcol`) REFERENCES `client` (`idclient`, `clientcol`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `client_social_network`
--
ALTER TABLE `client_social_network`
  ADD CONSTRAINT `fk_client_social_network_client1` FOREIGN KEY (`idclient`,`clientcol`) REFERENCES `client` (`idclient`, `clientcol`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_client_social_network_social_network1` FOREIGN KEY (`idsocial_network`) REFERENCES `social_network` (`idsocial_network`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `client_telephone`
--
ALTER TABLE `client_telephone`
  ADD CONSTRAINT `fk_client_telephone_client1` FOREIGN KEY (`idclient`,`clientcol`) REFERENCES `client` (`idclient`, `clientcol`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `client_vs_email`
--
ALTER TABLE `client_vs_email`
  ADD CONSTRAINT `fk_client_vs_email_client1` FOREIGN KEY (`idclient`,`clientcol`) REFERENCES `client` (`idclient`, `clientcol`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
