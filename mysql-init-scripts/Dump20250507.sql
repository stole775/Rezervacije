CREATE DATABASE  IF NOT EXISTS `rezervacije_db2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rezervacije_db2`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: rezervacije_db2
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
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `industry_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `industry_id` (`industry_id`),
  CONSTRAINT `fk_companies_industries` FOREIGN KEY (`industry_id`) REFERENCES `industries` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (1,'Salon Lux',1),(2,'Salon Brica',5),(3,'Telefoni018',7);
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,9,'tesst','2024-10-10 13:54:43');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industries`
--

DROP TABLE IF EXISTS `industries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `industries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industries`
--

LOCK TABLES `industries` WRITE;
/*!40000 ALTER TABLE `industries` DISABLE KEYS */;
INSERT INTO `industries` VALUES (1,'Beauty'),(8,'Education'),(3,'Food'),(5,'Hair'),(2,'Health'),(6,'Mechanic'),(4,'Nail'),(7,'Technology');
/*!40000 ALTER TABLE `industries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_logs`
--

DROP TABLE IF EXISTS `message_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sent_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('sent','failed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `message_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_logs`
--

LOCK TABLES `message_logs` WRITE;
/*!40000 ALTER TABLE `message_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (3,'C_ADMIN_FULL_ACCESS'),(5,'CREATE_RESERVATION'),(4,'CUSTOMER_FULL_ACCESS'),(7,'DELETE_RESERVATION'),(6,'EDIT_RESERVATION'),(2,'FULL_ACCESS'),(9,'MANAGE_USERS'),(1,'READ_ONLY'),(8,'VIEW_SETTINGS');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `usluga_id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `appointment_date` datetime NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `first_msg_sent` tinyint(1) DEFAULT '0',
  `second_msg_sent` tinyint(1) DEFAULT '0',
  `vreme_trajanja` int NOT NULL DEFAULT '30',
  `vreme_zavrsetka` datetime GENERATED ALWAYS AS ((`appointment_date` + interval `vreme_trajanja` minute)) STORED,
  `confirmed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_usluga_id` (`usluga_id`),
  CONSTRAINT `fk_reservations_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reservations_usluge` FOREIGN KEY (`usluga_id`) REFERENCES `usluge` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` (`id`, `user_id`, `usluga_id`, `name`, `phone`, `appointment_date`, `created_at`, `first_msg_sent`, `second_msg_sent`, `vreme_trajanja`, `confirmed`) VALUES (3,1,1,'Mladen stolic','+381 65 4903 526','2024-10-10 13:01:00','2024-10-01 10:14:14',0,0,20,1),(4,1,2,'Anastasija','+381628095545','2024-10-02 10:47:00','2024-10-01 10:45:03',0,1,30,0),(5,5,1,'test 5','+381659365685','2024-10-03 13:37:00','2024-10-03 11:34:26',1,0,300,0),(6,7,2,'twesta 7','+381659365685','2024-10-10 13:34:00','2024-10-03 11:35:03',0,0,30,1),(7,5,1,'Rim 5','+381659365685','2024-10-10 13:57:00','2024-10-03 11:57:36',0,0,30,0),(8,6,1,'Rim 6','+38165936555','2024-10-24 14:10:00','2024-10-03 12:10:21',0,0,300,1),(9,6,1,'milica 6','+38165848443','2024-10-23 11:34:00','2024-10-07 09:34:32',0,0,30,0),(11,6,2,'Mikw 6','+381658525802','2024-10-17 12:24:00','2024-10-07 10:24:31',0,0,30,0);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES (1,2),(2,2),(3,5),(4,5),(5,5),(3,6),(4,6),(5,6),(4,7),(5,7),(4,8),(5,8),(4,9);
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(5,'CADMIN'),(3,'CUSTOMER'),(4,'SADMIN'),(2,'USER'),(6,'WORKER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

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
  `message_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `days_to_keep` int NOT NULL,
  `cenovnik` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id` bigint DEFAULT NULL,
  `image_url_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url_background` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `button_shape` enum('PILL','ROUNDED','RECTANGLE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'PILL',
  `theme` enum('LIGHT','DARK') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'LIGHT',
  `image_url_10` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `timezone` enum('UTC_MINUS_12','UTC_MINUS_11','UTC_MINUS_10','UTC_MINUS_9','UTC_MINUS_8','UTC_MINUS_7','UTC_MINUS_6','UTC_MINUS_5','UTC_MINUS_4','UTC_MINUS_3','UTC_MINUS_2','UTC_MINUS_1','UTC_0','UTC_PLUS_1','UTC_PLUS_2','UTC_PLUS_3','UTC_PLUS_4','UTC_PLUS_5','UTC_PLUS_5_30','UTC_PLUS_6','UTC_PLUS_7','UTC_PLUS_8','UTC_PLUS_9','UTC_PLUS_10','UTC_PLUS_11','UTC_PLUS_12') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'UTC_0',
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
INSERT INTO `settings` VALUES (1,25,2,2,'Poštovana {name}, podsetnik za vaš termin {appointment_time}.',3000,0,'2024-10-01 10:14:14',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1),(2,30,2,2,'Poštovana {name}, podsetnik za vaš termin {appointment_time}.',30,0,'2024-10-01 10:14:14',1,NULL,NULL,'PILL','LIGHT',NULL,'email1@g.c','+381659365685','c','nis','19000','UTC_MINUS_10',1),(3,30,2,2,'Poštovana {name}, podsetnik za vaš termin {appointment_time}.',5,0,'2024-10-01 10:14:14',3,'company_3_832191f3-4f77-4676-874f-f4efe0d3e6a3.jpeg','company_3_9ab61c41-7150-4b98-be33-eebed61a79d2.png','ROUNDED','DARK',NULL,'email1@g.c','+381659365685',NULL,'nis','19000','UTC_PLUS_3',1);
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `teams_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_usluga`
--

DROP TABLE IF EXISTS `user_usluga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_usluga` (
  `user_id` int NOT NULL,
  `usluga_id` bigint NOT NULL,
  `cena` double NOT NULL,
  `trajanje` int NOT NULL DEFAULT '30',
  PRIMARY KEY (`user_id`,`usluga_id`,`cena`),
  KEY `idx_usluga_id` (`usluga_id`),
  CONSTRAINT `user_usluga_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_usluga_ibfk_2` FOREIGN KEY (`usluga_id`) REFERENCES `usluge` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_usluga`
--

LOCK TABLES `user_usluga` WRITE;
/*!40000 ALTER TABLE `user_usluga` DISABLE KEYS */;
INSERT INTO `user_usluga` VALUES (1,1,2,15),(1,2,100,20),(5,1,2645,45),(5,1,10085,60),(5,3,10085,30),(6,1,26,30),(6,2,1008,25);
/*!40000 ALTER TABLE `user_usluga` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `blocked` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role_id` int DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `ime` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prezime` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sms_naslov` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `team_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_users_roles` (`role_id`),
  KEY `fk_users_companies` (`company_id`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `fk_users_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_users_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL,
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@example.com',0,'2024-10-02 12:55:41',4,2,'mladn','prezime','aaa',NULL),(5,'Mladen','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@examplne.com',0,'2024-10-02 12:55:41',5,3,'ime','prezime 2','qwcqwcq',NULL),(6,'test','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','test@test.com',0,'2024-10-02 12:55:41',3,1,'ime','prezime3','/',NULL),(7,'mladenmaki','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','john.doe@example.com',0,'2024-10-03 09:24:28',5,1,'John','Doez','Appointment Reminder',NULL),(9,'sadmin','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@examples.com',0,'2024-10-02 12:55:41',4,2,'mladn','prezime','aaa',NULL),(10,'cadmin','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@exampslne.com',0,'2024-10-02 12:55:41',5,3,'ime','prezime 2','bbb',NULL),(11,'customer','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','test@xtest.com',0,'2024-10-02 12:55:41',3,1,'ime','prezime222','/',NULL),(13,'user','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','john.doe@examples.com',1,'2024-10-03 09:24:28',5,1,'John','Doez','Appointment Reminder',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usluge`
--

DROP TABLE IF EXISTS `usluge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usluge` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `naziv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `naziv` (`naziv`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usluge`
--

LOCK TABLES `usluge` WRITE;
/*!40000 ALTER TABLE `usluge` DISABLE KEYS */;
INSERT INTO `usluge` VALUES (5,'Brijanje'),(2,'Manikir'),(4,'Pedikir'),(1,'Sisanje'),(3,'Žensko šišanje');
/*!40000 ALTER TABLE `usluge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_hours`
--

DROP TABLE IF EXISTS `working_hours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_hours` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `day_of_week` enum('MON','TUE','WED','THU','FRI','SAT','SUN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_working_hours_company` (`company_id`),
  CONSTRAINT `fk_working_hours_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_hours`
--

LOCK TABLES `working_hours` WRITE;
/*!40000 ALTER TABLE `working_hours` DISABLE KEYS */;
INSERT INTO `working_hours` VALUES (1,1,'MON','08:00:00','16:00:00'),(2,1,'TUE','08:00:00','16:00:00'),(3,1,'WED','08:00:00','16:00:00'),(4,1,'THU','08:00:00','16:00:00'),(5,1,'FRI','08:00:00','16:00:00'),(6,2,'MON','09:00:00','17:00:00'),(7,2,'TUE','09:00:00','17:00:00'),(8,2,'WED','09:00:00','17:00:00'),(9,2,'THU','09:00:00','17:00:00'),(10,2,'FRI','09:00:00','17:00:00');
/*!40000 ALTER TABLE `working_hours` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-07 17:47:15
