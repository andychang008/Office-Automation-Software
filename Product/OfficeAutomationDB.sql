-- MySQL dump 10.13  Distrib 5.7.20, for macos10.12 (x86_64)
--
-- Host: localhost    Database: OfficeAutomationDB
-- ------------------------------------------------------
-- Server version	5.7.20

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
-- Table structure for table `attendanceTable`
--

DROP TABLE IF EXISTS `attendanceTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attendanceTable` (
  `employeeID` int(11) NOT NULL,
  `attendanceYear` varchar(10) NOT NULL,
  `presentCount` int(11) DEFAULT '0',
  `absentCount` int(11) DEFAULT '0',
  PRIMARY KEY (`employeeID`,`attendanceYear`),
  CONSTRAINT `attendancetable_ibfk_1` FOREIGN KEY (`employeeID`) REFERENCES `employeesTable` (`employeeID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendanceTable`
--

LOCK TABLES `attendanceTable` WRITE;
/*!40000 ALTER TABLE `attendanceTable` DISABLE KEYS */;
INSERT INTO `attendanceTable` VALUES (1,'2025',2,0),(2,'2025',3,1);
/*!40000 ALTER TABLE `attendanceTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budgetTable`
--

DROP TABLE IF EXISTS `budgetTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budgetTable` (
  `budgetID` int(11) NOT NULL AUTO_INCREMENT,
  `departmentID` int(11) NOT NULL,
  `budgetCategory` varchar(255) DEFAULT NULL,
  `allocatedAmount` double DEFAULT NULL,
  `amountSpent` double DEFAULT NULL,
  PRIMARY KEY (`budgetID`),
  KEY `budgettable_ibfk_1` (`departmentID`),
  CONSTRAINT `budgettable_ibfk_1` FOREIGN KEY (`departmentID`) REFERENCES `departmentsTable` (`departmentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budgetTable`
--

LOCK TABLES `budgetTable` WRITE;
/*!40000 ALTER TABLE `budgetTable` DISABLE KEYS */;
INSERT INTO `budgetTable` VALUES (1,1,'Salary',200000,0),(2,1,'Equipments',2000000,1000),(3,1,'Internet',130000,100000),(4,2,'Promotion',100000,56000.75),(5,2,'Salary',100000,70000),(6,2,'Video Production',50000,7500.95),(7,1,'QA',10000,100);
/*!40000 ALTER TABLE `budgetTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departmentsTable`
--

DROP TABLE IF EXISTS `departmentsTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departmentsTable` (
  `departmentID` int(11) NOT NULL AUTO_INCREMENT,
  `departmentName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`departmentID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departmentsTable`
--

LOCK TABLES `departmentsTable` WRITE;
/*!40000 ALTER TABLE `departmentsTable` DISABLE KEYS */;
INSERT INTO `departmentsTable` VALUES (1,'IT'),(2,'Marketing');
/*!40000 ALTER TABLE `departmentsTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeesTable`
--

DROP TABLE IF EXISTS `employeesTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employeesTable` (
  `employeeID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) DEFAULT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `departmentID` int(11) DEFAULT NULL,
  `DOB` varchar(15) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  PRIMARY KEY (`employeeID`),
  KEY `userID` (`userID`),
  KEY `employeestable_ibfk_2` (`departmentID`),
  CONSTRAINT `employeestable_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `usersTable` (`userID`),
  CONSTRAINT `employeestable_ibfk_2` FOREIGN KEY (`departmentID`) REFERENCES `departmentsTable` (`departmentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeesTable`
--

LOCK TABLES `employeesTable` WRITE;
/*!40000 ALTER TABLE `employeesTable` DISABLE KEYS */;
INSERT INTO `employeesTable` VALUES (1,2,'Manager','Account',1,'2000-02-02',200000),(2,3,'Employee','Account',1,'2000-02-02',50000),(3,4,'John','Doe',1,'2000-02-02',100000),(4,7,'Jane','Doe',2,'2020-03-20',10),(5,8,'Johnny','Doey',1,'2024-02-02',0);
/*!40000 ALTER TABLE `employeesTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `knowledgebaseTable`
--

DROP TABLE IF EXISTS `knowledgebaseTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `knowledgebaseTable` (
  `articleID` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `kbTitle` varchar(255) DEFAULT NULL,
  `kbContent` text,
  `publishedDate` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`articleID`),
  KEY `username` (`username`),
  CONSTRAINT `knowledgebasetable_ibfk_1` FOREIGN KEY (`username`) REFERENCES `usersTable` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `knowledgebaseTable`
--

LOCK TABLES `knowledgebaseTable` WRITE;
/*!40000 ALTER TABLE `knowledgebaseTable` DISABLE KEYS */;
INSERT INTO `knowledgebaseTable` VALUES (1,'admin','Post on 03/04','This is a knowledge base post.','2025-03-04'),(2,'admin','Post on 03/05','This is a knowledge base post.','2025-03-05'),(3,'admin','Post on 03/06','This is a knowledge base post.','2025-03-06'),(4,'admin','Post on 05/14','This is a knowledge base post.','2025-05-14');
/*!40000 ALTER TABLE `knowledgebaseTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagesTable`
--

DROP TABLE IF EXISTS `messagesTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagesTable` (
  `messageID` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `messageTitle` varchar(255) DEFAULT NULL,
  `messageContent` text,
  `sentDate` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`messageID`),
  KEY `username` (`username`),
  CONSTRAINT `messagestable_ibfk_1` FOREIGN KEY (`username`) REFERENCES `usersTable` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagesTable`
--

LOCK TABLES `messagesTable` WRITE;
/*!40000 ALTER TABLE `messagesTable` DISABLE KEYS */;
INSERT INTO `messagesTable` VALUES (1,'manager','Manager Message Post','For testing purposes only.','2025-03-05'),(2,'admin','Admin Message Post','For testing','2025-03-05'),(3,'admin','Admin Test','Hello everybody','2025-03-06'),(4,'admin','ihiihwi','hahihiahi','2025-05-14');
/*!40000 ALTER TABLE `messagesTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `peTable`
--

DROP TABLE IF EXISTS `peTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peTable` (
  `evaluationID` int(11) NOT NULL AUTO_INCREMENT,
  `employeeID` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `departmentID` int(11) NOT NULL,
  `evaluationDate` varchar(15) DEFAULT NULL,
  `evaluationContent` text,
  PRIMARY KEY (`evaluationID`),
  KEY `employeeID` (`employeeID`),
  KEY `username` (`username`),
  KEY `petable_ibfk_3` (`departmentID`),
  CONSTRAINT `petable_ibfk_1` FOREIGN KEY (`employeeID`) REFERENCES `employeesTable` (`employeeID`) ON DELETE CASCADE,
  CONSTRAINT `petable_ibfk_2` FOREIGN KEY (`username`) REFERENCES `usersTable` (`username`) ON DELETE CASCADE,
  CONSTRAINT `petable_ibfk_3` FOREIGN KEY (`departmentID`) REFERENCES `departmentsTable` (`departmentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `peTable`
--

LOCK TABLES `peTable` WRITE;
/*!40000 ALTER TABLE `peTable` DISABLE KEYS */;
INSERT INTO `peTable` VALUES (3,2,'admin',1,'2025-03-05','Employee has been satisfactory'),(4,4,'manager',1,'2025-03-05','Employee has been satisfactory'),(5,3,'admin',1,'2025-03-06','Employee has been satisfactory'),(6,1,'Admin',1,'2025-04-25','Employee has been satisfactory'),(7,4,'Admin',1,'2025-04-25','Employee has been satisfactory'),(8,4,'Admin',2,'2025-04-25','Employee has been satisfactory'),(9,1,'admin',1,'2025-05-14','Employee has been satisfactory');
/*!40000 ALTER TABLE `peTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usersTable`
--

DROP TABLE IF EXISTS `usersTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usersTable` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usersTable`
--

LOCK TABLES `usersTable` WRITE;
/*!40000 ALTER TABLE `usersTable` DISABLE KEYS */;
INSERT INTO `usersTable` VALUES (1,'admin','password','Admin'),(2,'manager','0000','Manager'),(3,'employee','0000','Employee'),(4,'JohnDoe','0000','Employee'),(5,'JohnDoe2','0000','Employee'),(6,'JaneDoe','0000','Employee'),(7,'JaneDoe2','0000','Employee'),(8,'JohnDoe3','0000','Employee'),(9,'JaneDoe3','0000','Employee');
/*!40000 ALTER TABLE `usersTable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-03 13:38:02
