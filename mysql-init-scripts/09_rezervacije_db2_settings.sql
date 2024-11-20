CREATE DATABASE  IF NOT EXISTS `rezervacije_db2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rezervacije_db2`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: rezervacije_db2
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hours_before_first_msg` int NOT NULL,
  `hours_before_second_msg` int DEFAULT NULL,
  `number_of_messages` int NOT NULL,
  `message_template` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `days_to_keep` int NOT NULL,
  `cenovnik` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id` bigint DEFAULT NULL,
  `image_url_logo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url_background` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `button_shape` enum('PILL','ROUNDED','RECTANGLE') COLLATE utf8mb4_unicode_ci DEFAULT 'PILL',
  `theme` enum('LIGHT','DARK') COLLATE utf8mb4_unicode_ci DEFAULT 'LIGHT',
  `image_url_10` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `timezone` enum('UTC_MINUS_12','UTC_MINUS_11','UTC_MINUS_10','UTC_MINUS_9','UTC_MINUS_8','UTC_MINUS_7','UTC_MINUS_6','UTC_MINUS_5','UTC_MINUS_4','UTC_MINUS_3','UTC_MINUS_2','UTC_MINUS_1','UTC_0','UTC_PLUS_1','UTC_PLUS_2','UTC_PLUS_3','UTC_PLUS_4','UTC_PLUS_5','UTC_PLUS_5_30','UTC_PLUS_6','UTC_PLUS_7','UTC_PLUS_8','UTC_PLUS_9','UTC_PLUS_10','UTC_PLUS_11','UTC_PLUS_12') COLLATE utf8mb4_unicode_ci DEFAULT 'UTC_0',
  `prikazi_cene` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_settings_companies` (`company_id`),
  CONSTRAINT `fk_settings_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `settings_chk_1` CHECK ((`number_of_messages` in (1,2))),
  CONSTRAINT `settings_chk_2` CHECK (json_valid(`image_url_10`))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,25,2,2,'Poštovana {name}, podsetnik za vaš termin {appointment_time}.',3000,0,'2024-10-01 10:14:14',2,NULL,NULL,NULL,NULL,'[\"company_2_84170c53-5407-4d50-a850-e04645aef6dd.png\",\"company_2_407dd7f5-b06a-4e0f-9eec-9c2ded46fb2c.png\",\"company_2_e6b92762-b005-46c6-bded-5423c8c1d894.png\",\"company_2_805ee3b7-4e61-4e26-aa95-026c7c2eb859.png\",\"company_2_669a441b-4ddb-4312-93bb-618e64cb8937.png\"]',NULL,NULL,NULL,NULL,NULL,NULL,1),(2,30,2,2,'Poštovana {name}, podsetnik za vaš termin {appointment_time}.',30,0,'2024-10-01 10:14:14',1,'company_1_1dfd2081-161a-460a-ba8f-9184ee59c2d7.png','company_1_1c49fbd2-edf4-4bba-a4c0-059f36903270.jpeg','PILL','LIGHT','[\"company_1_2be38543-103b-4173-b127-ff9a283788e7.png\",\"company_1_9b4d28e6-ef78-4cc1-b8bb-e9e15492d0b3.png\"]','email1@g.c','+381659365685','c','nis','19000','UTC_MINUS_10',1),(3,30,2,2,'Poštovana {name}, podsetnik za vaš termin {appointment_time}.',5,0,'2024-10-01 10:14:14',3,NULL,NULL,'ROUNDED','DARK',NULL,'email1@g.c','+381659365685',NULL,'nis','19000','UTC_PLUS_3',1);
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-06 14:23:18
