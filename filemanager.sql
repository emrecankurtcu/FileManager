-- --------------------------------------------------------
-- Sunucu:                       127.0.0.1
-- Sunucu sürümü:                8.0.29 - MySQL Community Server - GPL
-- Sunucu İşletim Sistemi:       Win64
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE DATABASE IF NOT EXISTS `FileManagerDatabase`;
USE `FileManagerDatabase`;

CREATE TABLE IF NOT EXISTS `fileInformation` (
  `fileInformationId` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `path` varchar(512) DEFAULT NULL,
  `size` bigint DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `extension` varchar(5) DEFAULT NULL,
  `createdDatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`fileInformationId`) USING BTREE,
  KEY `FK_file_user` (`userId`),
  CONSTRAINT `FK_file_user` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `user` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `email` varchar(128) DEFAULT NULL,
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `firstName` varchar(128) DEFAULT NULL,
  `lastName` varchar(128) DEFAULT NULL,
  `token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user` (`userId`, `email`, `password`, `firstName`, `lastName`, `token`) VALUES
	(1, 'test@test.test', '$2a$10$dbnxMkZUkDAHDPLJ8Y3.d.xFk6EzhXGQo2bMadZDzoax4c1sc4THC', 'Test', 'User', 'bnaht0yd2xy8dmd40c5n3es5k3yf761logrh736zdhvl3xnxk1j7nmkf06048cy4');
