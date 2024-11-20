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
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `usluga_id` bigint NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-06 14:23:18
