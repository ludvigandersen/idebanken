CREATE DATABASE  IF NOT EXISTS `idebanken` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `idebanken`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: idebanken.czb6si4xafah.eu-central-1.rds.amazonaws.com    Database: idebanken
-- ------------------------------------------------------
-- Server version	5.6.39-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DeveloperGroup`
--

DROP TABLE IF EXISTS `DeveloperGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DeveloperGroup` (
  `developer_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`developer_group_id`),
  KEY `person_id_fk` (`person_id`),
  KEY `group_id_fk` (`group_id`),
  CONSTRAINT `group_id_fk` FOREIGN KEY (`group_id`) REFERENCES `Group` (`group_id`) ON DELETE CASCADE,
  CONSTRAINT `person_id_fk` FOREIGN KEY (`person_id`) REFERENCES `Person` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DeveloperGroup`
--

LOCK TABLES `DeveloperGroup` WRITE;
/*!40000 ALTER TABLE `DeveloperGroup` DISABLE KEYS */;
INSERT INTO `DeveloperGroup` VALUES (28,70,55),(46,2,59),(47,2,2);
/*!40000 ALTER TABLE `DeveloperGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Group`
--

DROP TABLE IF EXISTS `Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `Group_group_name_uindex` (`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group`
--

LOCK TABLES `Group` WRITE;
/*!40000 ALTER TABLE `Group` DISABLE KEYS */;
INSERT INTO `Group` VALUES (1,'Team Awesome',0),(2,'test@test.dk',1),(41,'Yah',0),(42,'Memes',0),(43,'memse',0),(44,'asd',0),(46,'dfdf',0),(47,'asdf',0),(48,'test',0),(49,'testttttt',0),(50,'testtttttttttttta',0),(51,'qwe',0),(52,'tyu',0),(53,'teasdf',0),(55,'Nicolailinckandersen@gmail.com',1),(59,'Fuckmylife',0);
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GroupIdea`
--

DROP TABLE IF EXISTS `GroupIdea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GroupIdea` (
  `group_idea_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `idea_id` int(11) DEFAULT NULL,
  `approved` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`group_idea_id`),
  KEY `idea_fk` (`idea_id`),
  KEY `group_fk` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GroupIdea`
--

LOCK TABLES `GroupIdea` WRITE;
/*!40000 ALTER TABLE `GroupIdea` DISABLE KEYS */;
INSERT INTO `GroupIdea` VALUES (1,2,1,0),(2,2,6,1),(10,2,2,0),(11,2,9,0),(12,12,1,0),(13,2,10,0),(14,59,1,0);
/*!40000 ALTER TABLE `GroupIdea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Idea`
--

DROP TABLE IF EXISTS `Idea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Idea` (
  `idea_id` int(11) NOT NULL AUTO_INCREMENT,
  `idea_name` varchar(255) NOT NULL,
  `idea_description` varchar(255) NOT NULL,
  `idea_person` int(11) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`idea_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Idea`
--

LOCK TABLES `Idea` WRITE;
/*!40000 ALTER TABLE `Idea` DISABLE KEYS */;
INSERT INTO `Idea` VALUES (1,'Jahhhh','Yeye Virker det ??? ',9,'2018-05-16'),(2,'JAcob','asdasdasdasd',9,'2018-05-14');
/*!40000 ALTER TABLE `Idea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Person`
--

DROP TABLE IF EXISTS `Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `zip_code` int(11) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `email_notifications` tinyint(1) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `Person_email_uindex` (`email`),
  KEY `Person_PersonRole_role_id_fk` (`role_id`),
  CONSTRAINT `Person_PersonRole_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `PersonRole` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Person`
--

LOCK TABLES `Person` WRITE;
/*!40000 ALTER TABLE `Person` DISABLE KEYS */;
INSERT INTO `Person` VALUES (2,'TestFornavn','TestEfternavn','test@test.dk',2230,'Nørrebro','$2a$10$ZSlHR.NTWl2HneCUB0O/NeVSJds.je0QOYjaPANvhx4dbpahm0jEG',1,0,NULL),(9,'idea','test','idea@test.dk',1234,'Nørrebro','$2a$10$mHNdRqpbv/K8MrNDYWd1QOb4SOHLB7PSat4j4nXHQyaRHowzOPZoK',2,0,'2018-05-18'),(47,'mikkel','nielsen','tryllemikkel@gmail.com',123,'213','$2a$10$vWHjkha.11ukvtjsAKL4zelTOpK2fiHiHJFSqQ9gfIKXPiLVzo5Le',1,0,'2018-05-14'),(56,'Mikkel','Nielsen','mikkel@dalbynielsen.dk',2720,'Vanløse','$2a$10$t6gKifA0eL8PiHRWD9bQ0OxsN89tKdoJPWBosiSxCiFe0m52Nu35e',1,0,'2018-05-15'),(57,'Nicolai','Andersen','Nicolai@osalle4.dk',2990,'Nivå','$2a$10$72YzobzSfAxS8V/cqaq8AOO2QhqOND5CpGv2alrhJbF8lu1cSBxMq',2,0,'2018-05-16'),(70,'Nico','Lundsgaard','Nicolailinckandersen@gmail.com',2990,'Nivå','$2a$10$ku59y2qz6RupFgP9DmcqvOBKeuzqKGLss/djSPwEQBchikBqgNlLK',1,0,'2018-05-17'),(71,'Ludvig','Albert Rossil Andersen','ludvig.andersen@yahoo.dk',3400,'Hillerød','$2a$10$zqeyMqUJsrLGqcpVTRLPFOdGuwsAXPZe70CUWRDKJe0qWsVY5mI8m',1,0,'2018-05-18');
/*!40000 ALTER TABLE `Person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PersonRole`
--

DROP TABLE IF EXISTS `PersonRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PersonRole` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PersonRole`
--

LOCK TABLES `PersonRole` WRITE;
/*!40000 ALTER TABLE `PersonRole` DISABLE KEYS */;
INSERT INTO `PersonRole` VALUES (1,'ROLE_USER'),(2,'ROLE_IDEA'),(3,'ROLE_ADMIN');
/*!40000 ALTER TABLE `PersonRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PhoneNumbers`
--

DROP TABLE IF EXISTS `PhoneNumbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhoneNumbers` (
  `tlf_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) DEFAULT NULL,
  `tlf` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`tlf_id`),
  KEY `PhoneNumbers_Person_person_id_fk` (`person_id`),
  CONSTRAINT `PhoneNumbers_Person_person_id_fk` FOREIGN KEY (`person_id`) REFERENCES `Person` (`person_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PhoneNumbers`
--

LOCK TABLES `PhoneNumbers` WRITE;
/*!40000 ALTER TABLE `PhoneNumbers` DISABLE KEYS */;
INSERT INTO `PhoneNumbers` VALUES (6,2,'22116036'),(7,2,'25381160');
/*!40000 ALTER TABLE `PhoneNumbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'user','$2a$10$mHNdRqpbv/K8MrNDYWd1QOb4SOHLB7PSat4j4nXHQyaRHowzOPZoK','ROLE_USER'),(2,'admin','$2a$10$mHNdRqpbv/K8MrNDYWd1QOb4SOHLB7PSat4j4nXHQyaRHowzOPZoK','ROLE_manager');
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

-- Dump completed on 2018-05-21 12:44:45
