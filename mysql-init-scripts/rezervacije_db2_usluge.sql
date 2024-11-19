CREATE DATABASE IF NOT EXISTS `rezervacije_db2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rezervacije_db2`;

-- ------------------------------------------------------
-- Table structure for table `usluge`
-- ------------------------------------------------------

DROP TABLE IF EXISTS `usluge`;
CREATE TABLE `usluge` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,        -- ID usluge
  `naziv` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL, -- Naziv usluge
  PRIMARY KEY (`id`),                         -- Primarni ključ
  UNIQUE KEY `naziv` (`naziv`)                -- Naziv mora biti jedinstven
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------
-- Insert data into table `usluge`
-- ------------------------------------------------------

LOCK TABLES `usluge` WRITE;
INSERT INTO `usluge` VALUES
(1, 'Sisanje'),       -- Usluga Frizer
(2, 'Manikir'),      -- Usluga Manikir
(3, 'Žensko šišanje'), -- Usluga Žensko šišanje
(4, 'Pedikir'),      -- Dodatna usluga Pedikir
(5, 'Brijanje');     -- Dodatna usluga Brijanje
UNLOCK TABLES;
