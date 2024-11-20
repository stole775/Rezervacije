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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `blocked` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role_id` int DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `ime` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prezime` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sms_naslov` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
INSERT INTO `users` VALUES (1,'admin','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@example.com',0,'2024-10-02 12:55:41',4,2,'mladn','prezime','aaa',NULL),(5,'Mladen','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@examplne.com',0,'2024-10-02 12:55:41',5,1,'ime','prezime 2','qwcqwcq',NULL),(6,'test','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','test@test.com',0,'2024-10-02 12:55:41',3,1,'ime','prezime3','/',NULL),(7,'mladenmaki','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','john.doe@example.com',0,'2024-10-03 09:24:28',5,1,'John','Doez','Appointment Reminder',NULL),(9,'sadmin','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@examples.com',0,'2024-10-02 12:55:41',4,2,'mladn','prezime','aaa',NULL),(10,'cadmin','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','admin@exampslne.com',0,'2024-10-02 12:55:41',5,3,'ime','prezime 2','bbb',NULL),(11,'customer','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','test@xtest.com',0,'2024-10-02 12:55:41',3,1,'ime','prezime222','/',NULL),(13,'user','$2a$10$kWqTWfxtGDrO.bYjDakZaOi1ndDqbqhuCZ/pH2gRw1dAYYEneQE2O','john.doe@examples.com',1,'2024-10-03 09:24:28',5,1,'John','Doez','Appointment Reminder',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-06 14:23:17
