CREATE TABLE `working_hours` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,        -- Jedinstveni ID za radno vreme
  `company_id` BIGINT NOT NULL,               -- ID kompanije
  `day_of_week` ENUM('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN') NOT NULL, -- Dan u nedelji
  `start_time` TIME NOT NULL,                 -- Početak radnog vremena
  `end_time` TIME NOT NULL,                   -- Kraj radnog vremena
  PRIMARY KEY (`id`),
  KEY `fk_working_hours_company` (`company_id`),
  CONSTRAINT `fk_working_hours_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
LOCK TABLES `working_hours` WRITE;
INSERT INTO `working_hours` (`company_id`, `day_of_week`, `start_time`, `end_time`) VALUES
(1, 'MON', '08:00:00', '16:00:00'),
(1, 'TUE', '08:00:00', '16:00:00'),
(1, 'WED', '08:00:00', '16:00:00'),
(1, 'THU', '08:00:00', '16:00:00'),
(1, 'FRI', '08:00:00', '16:00:00'),
(2, 'MON', '09:00:00', '17:00:00'),
(2, 'TUE', '09:00:00', '17:00:00'),
(2, 'WED', '09:00:00', '17:00:00'),
(2, 'THU', '09:00:00', '17:00:00'),
(2, 'FRI', '09:00:00', '17:00:00');
UNLOCK TABLES;
