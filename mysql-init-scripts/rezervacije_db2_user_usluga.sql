CREATE DATABASE IF NOT EXISTS `rezervacije_db2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rezervacije_db2`;

-- ------------------------------------------------------
-- Table structure for table `user_usluga`
-- ------------------------------------------------------

DROP TABLE IF EXISTS `user_usluga`;
CREATE TABLE `user_usluga` (
  `user_id` INT NOT NULL,               -- ID korisnika
  `usluga_id` BIGINT NOT NULL,          -- ID usluge
  `cena` DOUBLE NOT NULL,               -- Cena usluge
  `trajanje` INT NOT NULL DEFAULT 30,   -- Trajanje termina (min), podrazumevano 30
  PRIMARY KEY (`user_id`, `usluga_id`, `cena`),
  KEY `idx_usluga_id` (`usluga_id`),
  CONSTRAINT `user_usluga_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_usluga_ibfk_2` FOREIGN KEY (`usluga_id`) REFERENCES `usluge` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------
-- Insert data into table `user_usluga`
-- ------------------------------------------------------

LOCK TABLES `user_usluga` WRITE;
INSERT INTO `user_usluga` (`user_id`, `usluga_id`, `cena`, `trajanje`) VALUES
(1, 1, 2, 15),       -- Korisnik 1, Usluga 1, Cena 2, Trajanje 15 min
(5, 1, 2645, 45),    -- Korisnik 5, Usluga 1, Cena 2645, Trajanje 45 min
(5, 1, 10085, 60),   -- Korisnik 5, Usluga 1, Cena 10085, Trajanje 60 min
(6, 1, 26, 30),      -- Korisnik 6, Usluga 1, Cena 26, Trajanje 30 min
(1, 2, 100, 20),     -- Korisnik 1, Usluga 2, Cena 100, Trajanje 20 min
(6, 2, 1008, 25),    -- Korisnik 6, Usluga 2, Cena 1008, Trajanje 25 min
(5, 3, 10085, 30),   -- Korisnik 5, Usluga 3, Cena 10085, Trajanje 30 min
(5, 10, 1500, 40),   -- Korisnik 5, Usluga 10, Cena 1500, Trajanje 40 min
(5, 11, 2000, 50);   -- Korisnik 5, Usluga 11, Cena 2000, Trajanje 50 min
UNLOCK TABLES;
