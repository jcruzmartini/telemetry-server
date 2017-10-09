-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.10


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema smsbd
--

CREATE DATABASE IF NOT EXISTS smsbd;
USE smsbd;

--
-- Definition of table `alert`
--

DROP TABLE IF EXISTS `alert`;
CREATE TABLE `alert` (
  `ID_ALERT` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_VARIABLE` int(10) unsigned DEFAULT NULL,
  `ALERT_DESCRIPTION` varchar(45) DEFAULT NULL,
  `CODE` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID_ALERT`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `alert`
--

/*!40000 ALTER TABLE `alert` DISABLE KEYS */;
INSERT INTO `alert` (`ID_ALERT`,`ID_VARIABLE`,`ALERT_DESCRIPTION`,`CODE`) VALUES 
 (1,1,'Alerta Temperatura Máxima','T'),
 (2,2,'Alerta Lluvia','P'),
 (3,3,'Alerta humedad Máxima','H'),
 (4,5,'Alerta Nivel bateria de la EMA muy bajo','BAT'),
 (5,6,'Alerta Temperatura interna de la EMA muy alta','TEMP');
/*!40000 ALTER TABLE `alert` ENABLE KEYS */;


--
-- Definition of table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `ID_CUSTOMER` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `LAST_NAME` varchar(45) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `ADDRESS` varchar(45) DEFAULT NULL,
  `CITY` varchar(45) DEFAULT NULL,
  `PROVINCE` varchar(45) DEFAULT NULL,
  `STATUS` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID_CUSTOMER`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`ID_CUSTOMER`,`NAME`,`LAST_NAME`,`EMAIL`,`ADDRESS`,`CITY`,`PROVINCE`,`STATUS`) VALUES 
 (1,'Juan Cruz','Martini','jcruzmartini@gmail.com','Testing 1','Marcos Juarez','Cordoba','E'),
 (2,'Probando','Avances',NULL,'jcruzmartini@gmail.com','Rosario','Santa Fe','E');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;


--
-- Definition of table `gsmmodem`
--

DROP TABLE IF EXISTS `gsmmodem`;
CREATE TABLE `gsmmodem` (
  `ID_GSMMODEM` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `BRANCH` varchar(45) DEFAULT NULL,
  `MODEL` varchar(45) DEFAULT NULL,
  `PORT` varchar(6) DEFAULT NULL,
  `BAUDIOS` int(10) unsigned DEFAULT NULL,
  `PIN_SIM` varchar(45) DEFAULT NULL,
  `IS_SENDER` tinyint(1) DEFAULT NULL,
  `IS_RECEIVER` tinyint(1) DEFAULT NULL,
  `ID_SIMCARD` int(10) unsigned DEFAULT NULL,
  `ENABLE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_GSMMODEM`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `gsmmodem`
--

/*!40000 ALTER TABLE `gsmmodem` DISABLE KEYS */;
INSERT INTO `gsmmodem` (`ID_GSMMODEM`,`BRANCH`,`MODEL`,`PORT`,`BAUDIOS`,`PIN_SIM`,`IS_SENDER`,`IS_RECEIVER`,`ID_SIMCARD`,`ENABLE`) VALUES 
 (1,'WAVECOME','2DEE','COM1',115200,'0000',1,1,1,1);
/*!40000 ALTER TABLE `gsmmodem` ENABLE KEYS */;


--
-- Definition of table `measures`
--

DROP TABLE IF EXISTS `measures`;
CREATE TABLE `measures` (
  `ID_MEASURE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_STATION` int(10) unsigned DEFAULT NULL,
  `ID_VARIABLE` varchar(45) DEFAULT NULL,
  `VALUE` double DEFAULT NULL,
  `DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_MEASURE`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `measures`
--

/*!40000 ALTER TABLE `measures` DISABLE KEYS */;
INSERT INTO `measures` (`ID_MEASURE`,`ID_STATION`,`ID_VARIABLE`,`VALUE`,`DATE`) VALUES 
 (43,1,'2',25,'2011-03-21 06:00:45'),
 (44,1,'1',25.2,'2011-03-21 06:00:45'),
 (45,1,'3',25,'2011-03-21 06:00:45'),
 (46,1,'2',25,'2011-03-21 03:00:45'),
 (47,1,'1',25.2,'2011-03-21 03:00:45'),
 (48,1,'3',25,'2011-03-21 03:00:45'),
 (49,1,'2',25,'2011-03-21 05:00:45'),
 (50,1,'1',25.2,'2011-03-21 05:00:45'),
 (51,1,'3',25,'2011-03-21 05:00:45'),
 (52,1,'3',25,'2011-03-21 02:00:45'),
 (53,1,'1',25.2,'2011-03-21 02:00:45'),
 (54,1,'2',25,'2011-03-21 02:00:45'),
 (55,1,'1',25.2,'2011-03-21 01:00:45'),
 (56,1,'2',25,'2011-03-21 01:00:45'),
 (57,1,'3',25,'2011-03-21 01:00:45'),
 (58,1,'2',25,'2011-03-21 04:00:45'),
 (59,1,'1',25.2,'2011-03-21 04:00:45'),
 (60,1,'3',25,'2011-03-21 04:00:45'),
 (61,1,'2',25,'2011-03-21 06:00:54'),
 (62,1,'3',25,'2011-03-21 06:00:54'),
 (63,1,'1',25.2,'2011-03-21 06:00:54'),
 (64,1,'3',25,'2011-03-21 03:00:54'),
 (65,1,'1',25.2,'2011-03-21 03:00:54'),
 (66,1,'2',25,'2011-03-21 03:00:54'),
 (67,1,'2',25,'2011-03-21 05:00:54'),
 (68,1,'3',25,'2011-03-21 05:00:54'),
 (69,1,'1',25.2,'2011-03-21 05:00:54'),
 (70,1,'2',25,'2011-03-21 02:00:54'),
 (71,1,'1',25.2,'2011-03-21 02:00:54'),
 (72,1,'3',25,'2011-03-21 02:00:54'),
 (73,1,'2',25,'2011-03-21 01:00:54'),
 (74,1,'1',25.2,'2011-03-21 01:00:54'),
 (75,1,'3',25,'2011-03-21 01:00:54'),
 (76,1,'1',25.2,'2011-03-21 04:00:54'),
 (77,1,'2',25,'2011-03-21 04:00:54'),
 (78,1,'3',25,'2011-03-21 04:00:54'),
 (79,1,'1',25.2,'2011-03-22 06:00:16'),
 (80,1,'3',25,'2011-03-22 06:00:16'),
 (81,1,'2',25,'2011-03-22 06:00:16'),
 (82,1,'2',25,'2011-03-22 03:00:16'),
 (83,1,'3',25,'2011-03-22 03:00:16'),
 (84,1,'1',25.2,'2011-03-22 03:00:16'),
 (85,1,'3',25,'2011-03-22 05:00:16'),
 (86,1,'1',25.2,'2011-03-22 05:00:16'),
 (87,1,'2',25,'2011-03-22 05:00:16'),
 (88,1,'3',25,'2011-03-22 02:00:16'),
 (89,1,'2',25,'2011-03-22 02:00:16'),
 (90,1,'1',25.2,'2011-03-22 02:00:16'),
 (91,1,'3',25,'2011-03-22 01:00:16'),
 (92,1,'1',25.2,'2011-03-22 01:00:16'),
 (93,1,'2',25,'2011-03-22 01:00:16'),
 (94,1,'2',25,'2011-03-22 04:00:16'),
 (95,1,'1',25.2,'2011-03-22 04:00:16'),
 (96,1,'3',25,'2011-03-22 04:00:16'),
 (97,1,'1',25.2,'2011-03-29 06:00:00'),
 (98,1,'2',25,'2011-03-29 06:00:00'),
 (99,1,'3',25,'2011-03-29 06:00:00'),
 (100,1,'1',25.2,'2011-03-29 03:00:00'),
 (101,1,'3',25,'2011-03-29 03:00:00'),
 (102,1,'2',25,'2011-03-29 03:00:00'),
 (103,1,'1',25.2,'2011-03-29 05:00:00'),
 (104,1,'2',25,'2011-03-29 05:00:00'),
 (105,1,'3',25,'2011-03-29 05:00:00'),
 (106,1,'1',25.2,'2011-03-29 02:00:00'),
 (107,1,'3',25,'2011-03-29 02:00:00'),
 (108,1,'2',25,'2011-03-29 02:00:00'),
 (109,1,'1',25.2,'2011-03-29 01:00:00'),
 (110,1,'2',25,'2011-03-29 01:00:00'),
 (111,1,'3',25,'2011-03-29 01:00:00'),
 (112,1,'1',25.2,'2011-03-29 04:00:00'),
 (113,1,'2',25,'2011-03-29 04:00:00'),
 (114,1,'3',25,'2011-03-29 04:00:00'),
 (115,1,'2',180.1,'2016-10-12 11:16:00'),
 (116,1,'3',99,'2016-10-12 11:16:00'),
 (117,1,'4',1002,'2016-10-12 11:16:00'),
 (118,1,'1',45,'2016-10-12 11:16:00'),
 (119,1,'4',1002,'2016-10-12 11:16:00'),
 (120,1,'1',45,'2016-10-12 11:16:00'),
 (121,1,'3',99,'2016-10-12 11:16:00'),
 (122,1,'2',180.1,'2016-10-12 11:16:00');
/*!40000 ALTER TABLE `measures` ENABLE KEYS */;


--
-- Definition of table `rulesxcustomer`
--

DROP TABLE IF EXISTS `rulesxcustomer`;
CREATE TABLE `rulesxcustomer` (
  `ID_RULE` int(10) unsigned NOT NULL DEFAULT '0',
  `ID_CUSTOMER` int(10) unsigned NOT NULL DEFAULT '0',
  `ORDER` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_RULE`,`ID_CUSTOMER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rulesxcustomer`
--

/*!40000 ALTER TABLE `rulesxcustomer` DISABLE KEYS */;
INSERT INTO `rulesxcustomer` (`ID_RULE`,`ID_CUSTOMER`,`ORDER`) VALUES 
 (1,1,1),
 (2,1,2);
/*!40000 ALTER TABLE `rulesxcustomer` ENABLE KEYS */;


--
-- Definition of table `simcard`
--

DROP TABLE IF EXISTS `simcard`;
CREATE TABLE `simcard` (
  `ID_SIMCARD` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NUMBER` varchar(45) DEFAULT NULL,
  `COMPANY` varchar(45) DEFAULT NULL,
  `ID_CUSTOMER` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_SIMCARD`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `simcard`
--

/*!40000 ALTER TABLE `simcard` DISABLE KEYS */;
INSERT INTO `simcard` (`ID_SIMCARD`,`NUMBER`,`COMPANY`,`ID_CUSTOMER`) VALUES 
 (1,'5493415460866','CLARO',NULL),
 (2,'5493472436172','CLARO',1),
 (3,'5493415775431','CLARO',2);
/*!40000 ALTER TABLE `simcard` ENABLE KEYS */;


--
-- Definition of table `sms`
--

DROP TABLE IF EXISTS `sms`;
CREATE TABLE `sms` (
  `ID_SMS` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DATE` datetime DEFAULT NULL,
  `FROME` varchar(45) DEFAULT NULL,
  `STATE` varchar(10) DEFAULT NULL,
  `TEXT` varchar(165) DEFAULT NULL,
  `CONFIRMATION_ID` int(11) DEFAULT '-1',
  `TOKEN_NUMBER` int(11) DEFAULT '-1',
  `ERROR_CODE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID_SMS`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms`
--

/*!40000 ALTER TABLE `sms` DISABLE KEYS */;
INSERT INTO `sms` (`ID_SMS`,`DATE`,`FROME`,`STATE`,`TEXT`,`CONFIRMATION_ID`,`TOKEN_NUMBER`,`ERROR_CODE`) VALUES 
 (10,'2011-03-18 00:00:00','5493415460866','S','A|T|22.2',-1,-1,NULL),
 (11,'2011-03-19 00:00:00','5493415460866','S','A|T|22.2',-1,-1,NULL),
 (12,'2011-03-19 00:00:00','5493415460866','S','A|T|22.2',-1,-1,NULL),
 (13,'2011-03-19 00:00:00','5493415460866','S','A|T|22.2',-1,-1,NULL),
 (14,'2011-03-19 00:00:00','5493415460866','C','A|T|22.2',-1,-1,NULL),
 (15,'2011-03-19 00:00:00','5493415460866','C','A|T|22.2',-1,-1,NULL),
 (16,'2011-03-19 00:00:00','5493472436171','C','Tiempo',-1,-1,NULL),
 (17,'2011-03-19 00:00:00','5493472436171','C','Tiempo',-1,-1,NULL),
 (18,'2011-03-19 00:00:00','5493415460866','C','R|T:22*P:25*H:32',-1,-1,NULL),
 (19,'2011-03-19 00:00:00','5493472436171','C','Tiempo',-1,-1,NULL),
 (20,'2011-03-19 00:00:00','5493415460866','C','R|T:22*P:25*H:32',-1,-1,NULL),
 (21,'2011-03-19 13:43:21','5493472436171','C','Tiempo',-1,-1,NULL),
 (22,'2011-03-19 13:45:10','5493415460866','C','R|T:22*P:25*H:32',-1,-1,NULL),
 (23,'2011-03-21 21:29:02','5493415460866','C','D|00:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (24,'2011-03-21 21:59:33','5493415460866','C','D|00:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (25,'2011-03-21 22:06:25','5493415460866','F','D|0#:25.2*H:25*P:25',-1,-1,NULL),
 (26,'2011-03-21 22:16:32','5493415460866','F','R|00:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (27,'2011-03-21 22:36:07','5493415460866','F','A|X|2',-1,-1,NULL),
 (28,'2011-03-21 22:49:02','5493415460866','C','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (29,'2011-03-21 23:07:06','5493415460866','C','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (30,'2011-03-21 23:28:45','5493415460866','C','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (31,'2011-03-21 23:41:54','5493415460866','S','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (32,'2011-03-22 00:18:16','5493415460866','C','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (33,'2011-03-22 00:18:16','5493415460866','C','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (34,'2011-03-29 20:01:23','5493415460866','F','A|T|22.2',-1,-1,NULL),
 (35,'2011-03-29 20:15:33','5493415460866','C','A|T|22.2',-1,-1,NULL),
 (36,'2011-03-29 23:37:16','5493415460866','C','D|01:00#T:25.2*H:25*P:25|02:00#T:25.2*H:25*P:25|03:00#T:25.2*H:25*P:25|04:00#T:25.2*H:25*P:25|05:00#T:25.2*H:25*P:25|06:00#T:25.2*H:25*P:25',-1,-1,NULL),
 (37,'2011-03-29 23:40:11','5493415460866','F','R|V+',-1,-1,NULL),
 (38,'2011-03-29 23:52:46','5493415460866','BF','R|V+',-1,-1,NULL),
 (39,'2011-03-30 00:12:18','5493415460866','C','A|T|22.2',-1,-1,NULL),
 (40,'2011-03-30 00:13:43','5493415460866','C','A|T|22.2',-1,-1,NULL),
 (41,'2011-04-30 16:52:51','5493415460866','C','A|T|22.2',-1,-1,NULL),
 (42,'2012-01-03 19:51:09','5493415460866','F','A|T|22.6',-1,-1,NULL),
 (43,'2012-01-03 19:51:09','5493415460866','F','A|T|22.6',-1,-1,NULL),
 (44,'2012-01-03 21:11:45','5493415460866','F','A|T|22.6',-1,-1,NULL),
 (45,'2012-01-03 21:11:45','5493415460866','C','A|T|22.6',-1,-1,NULL),
 (46,'2012-01-03 22:40:00','5493415460866','BF','D|DDMMYY|hh:mm#T:0000*L:00000* H:00*P:0000|hh:mm#T:0000*L: 00000*H:00*P:0000|hh:mm#T: 0000*L:00000*H:00*P:0000|hh: mm#T:0000*L:00000*H:00*P:0000',-1,-1,NULL),
 (47,'2012-01-03 22:59:59','5493415460866','BF','D|161012|11:16#T:0000*L:00000* H:00*P:0000|11:16#T:0000*L: 00000*H:00*P:0000|11:16#T: 0000*L:00000*H:00*P:0000|11: 16#T:0000*L:00000*H:00*P:0000',-1,-1,NULL),
 (48,'2012-01-03 23:00:41','5493472448259','BF','Tiene heladera el quincho?',-1,-1,NULL),
 (49,'2012-01-03 23:24:53','5493415460866','C','D|161012|11:16#T:45*L:180.1*H:99*P:1002|11:16#T:45*L:180.1*H:99*P:1002|11:16#T:45*L:180.1*H:99*P:1002|11:16#T:45*L:180.1*H:99*P:1002',-1,-1,NULL),
 (50,'2012-01-07 10:11:12','SERVER','C',NULL,-1,-1,NULL),
 (51,'2012-01-09 23:48:45','5493415460866','WCS','A|T|45|126',126,-1,NULL),
 (52,'2012-01-09 23:56:15','SERVER','S','OK',NULL,-1,NULL),
 (53,'2012-01-10 00:06:57','5493415460866','X','A|T|45|126',126,-1,NULL),
 (54,'2012-01-10 00:18:12','SERVER','X','OK[|]126',-1,-1,NULL),
 (55,'2012-01-10 00:22:50','5493415460866','F','R|T:23.6*L:124*H:56*P:1234|125',-1,-1,NULL),
 (56,'2012-01-10 00:22:50','5493415460866','BF','R|T:23.6*L:124*H:56*p:1234',-1,-1,NULL),
 (57,'2012-01-10 00:29:03','5493415460866','F','R|T:23.6*L:124*H:56*P:1234|55',55,-1,NULL),
 (58,'2012-01-10 00:40:39','SERVER','X','FAIL|55',-1,-1,NULL),
 (59,'2012-01-10 00:52:56','5493415775431','C','TIEMPO',-1,-1,NULL),
 (60,'2012-01-10 00:58:49','5493415460866','BF','Vgjkyvcaf',-1,-1,NULL),
 (61,'2012-01-10 01:28:13','5493415460866','C','R|T:23.6*L:124*H:56*P:1234|55',55,-1,NULL),
 (62,'2012-01-10 01:51:05','SERVER','S','OK|55',-1,-1,NULL),
 (63,'2012-01-10 01:54:24','5493415775431','BF','Gracias ema!',-1,-1,NULL),
 (64,'2012-01-10 01:49:33','5493415460866','P','R|T:23.6*L:124*H:56*P:1234|55',55,-1,NULL),
 (65,'2012-01-10 02:02:14','SERVER','P','OK|55',-1,-1,NULL);
/*!40000 ALTER TABLE `sms` ENABLE KEYS */;


--
-- Definition of table `sms_confirmation`
--

DROP TABLE IF EXISTS `sms_confirmation`;
CREATE TABLE `sms_confirmation` (
  `ID_SMS_CONFIRMATION` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  `SENT_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ID_SMS_REQUEST` int(10) unsigned DEFAULT NULL,
  `ID_STATION` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_SMS_CONFIRMATION`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_confirmation`
--

/*!40000 ALTER TABLE `sms_confirmation` DISABLE KEYS */;
INSERT INTO `sms_confirmation` (`ID_SMS_CONFIRMATION`,`ID_SMS`,`SENT_ON`,`ID_SMS_REQUEST`,`ID_STATION`) VALUES 
 (1,50,'2012-01-07 10:13:30',49,1),
 (2,52,'2012-01-09 23:56:16',51,1),
 (3,54,'2012-01-10 00:18:14',53,1),
 (4,58,'2012-01-10 00:40:55',57,1),
 (5,62,'2012-01-10 01:51:10',61,3),
 (6,65,'2012-01-10 02:02:15',64,3);
/*!40000 ALTER TABLE `sms_confirmation` ENABLE KEYS */;


--
-- Definition of table `sms_customer_request`
--

DROP TABLE IF EXISTS `sms_customer_request`;
CREATE TABLE `sms_customer_request` (
  `ID_SMS_CUSTOMER_REQUEST` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  `ID_STATION` int(10) unsigned DEFAULT NULL,
  `ID_CUSTOMER` int(10) unsigned DEFAULT NULL,
  `ID_SMS_RESPONSE` int(10) unsigned DEFAULT NULL,
  `SENT_ON` datetime DEFAULT NULL,
  `TOKEN_NUMBER` int(11) DEFAULT '-1',
  PRIMARY KEY (`ID_SMS_CUSTOMER_REQUEST`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_customer_request`
--

/*!40000 ALTER TABLE `sms_customer_request` DISABLE KEYS */;
INSERT INTO `sms_customer_request` (`ID_SMS_CUSTOMER_REQUEST`,`ID_SMS`,`ID_STATION`,`ID_CUSTOMER`,`ID_SMS_RESPONSE`,`SENT_ON`,`TOKEN_NUMBER`) VALUES 
 (1,16,1,1,NULL,'2011-03-19 00:00:00',-1),
 (2,17,1,1,18,'2011-03-19 00:00:00',-1),
 (3,19,1,1,20,'2011-03-19 00:00:00',-1),
 (4,21,1,1,22,'2011-03-19 13:40:28',-1),
 (5,59,3,2,64,'2012-01-10 02:01:10',-1);
/*!40000 ALTER TABLE `sms_customer_request` ENABLE KEYS */;


--
-- Definition of table `sms_daily_summary`
--

DROP TABLE IF EXISTS `sms_daily_summary`;
CREATE TABLE `sms_daily_summary` (
  `ID_SMS_DAILY_SUMMARY` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TEST` varchar(45) DEFAULT NULL,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_SMS_DAILY_SUMMARY`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_daily_summary`
--

/*!40000 ALTER TABLE `sms_daily_summary` DISABLE KEYS */;
INSERT INTO `sms_daily_summary` (`ID_SMS_DAILY_SUMMARY`,`TEST`,`ID_SMS`) VALUES 
 (1,NULL,23),
 (2,NULL,24),
 (3,NULL,25),
 (4,NULL,28),
 (5,NULL,29),
 (6,NULL,30),
 (7,NULL,31),
 (8,NULL,32),
 (9,NULL,33),
 (10,NULL,36),
 (11,NULL,49);
/*!40000 ALTER TABLE `sms_daily_summary` ENABLE KEYS */;


--
-- Definition of table `sms_operation`
--

DROP TABLE IF EXISTS `sms_operation`;
CREATE TABLE `sms_operation` (
  `ID_SMS_OPERATION` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  `ID_STATION` int(10) unsigned DEFAULT NULL,
  `SENT_ON` datetime DEFAULT NULL,
  `SUCESS` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_SMS_OPERATION`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_operation`
--

/*!40000 ALTER TABLE `sms_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_operation` ENABLE KEYS */;


--
-- Definition of table `sms_response`
--

DROP TABLE IF EXISTS `sms_response`;
CREATE TABLE `sms_response` (
  `ID_SMS_RESPONSE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `SENT_ON` datetime DEFAULT NULL,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  `TO_` varchar(45) DEFAULT NULL,
  `FRIENDLYTEXT` varchar(160) DEFAULT NULL,
  PRIMARY KEY (`ID_SMS_RESPONSE`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_response`
--

/*!40000 ALTER TABLE `sms_response` DISABLE KEYS */;
INSERT INTO `sms_response` (`ID_SMS_RESPONSE`,`SENT_ON`,`ID_SMS`,`TO_`,`FRIENDLYTEXT`) VALUES 
 (1,'2011-03-19 00:00:00',18,'5493472436171','Lluvia : 25 mmHumedad : 32 %Temperatura : 22 °C'),
 (2,'2011-03-19 00:00:00',20,'5493472436171','Temperatura : 22 °C Humedad : 32 % Lluvia : 25 mm'),
 (3,'2011-03-19 13:41:47',22,'5493472436171','Humedad : 32 % Temperatura : 22 °C Lluvia : 25 mm'),
 (4,NULL,26,NULL,NULL),
 (5,NULL,55,NULL,NULL),
 (6,NULL,57,NULL,NULL),
 (7,'2012-01-10 01:52:15',61,'5493415775431','Humedad:56 % ,Lluvia:124 mm ,Presion:1234 hPas ,Temperatura:23.6 °C'),
 (8,NULL,64,'5493415775431','Presion:1234 hPas ,Lluvia:124 mm ,Humedad:56 % ,Temperatura:23.6 °C');
/*!40000 ALTER TABLE `sms_response` ENABLE KEYS */;


--
-- Definition of table `sms_station_event`
--

DROP TABLE IF EXISTS `sms_station_event`;
CREATE TABLE `sms_station_event` (
  `ID_SMS_STATION_EVENT` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  `ID_STATION` int(10) unsigned DEFAULT NULL,
  `ID_CUSTOMER` int(10) unsigned DEFAULT NULL,
  `SENT_ON` datetime DEFAULT NULL,
  `ALERT_CODE` varchar(5) DEFAULT NULL,
  `VALUE` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID_SMS_STATION_EVENT`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_station_event`
--

/*!40000 ALTER TABLE `sms_station_event` DISABLE KEYS */;
INSERT INTO `sms_station_event` (`ID_SMS_STATION_EVENT`,`ID_SMS`,`ID_STATION`,`ID_CUSTOMER`,`SENT_ON`,`ALERT_CODE`,`VALUE`) VALUES 
 (10,11,1,1,NULL,'T','22.2'),
 (11,12,1,1,NULL,'T','22.2'),
 (12,13,1,1,NULL,'T','22.2'),
 (13,14,1,1,'2011-03-19 00:00:00','T','22.2'),
 (14,15,1,1,'2011-03-19 00:00:00','T','22.2'),
 (15,27,1,1,NULL,'X','2'),
 (16,34,NULL,NULL,NULL,NULL,NULL),
 (17,35,1,1,'2011-03-29 23:27:50','T','22.2'),
 (18,39,1,1,'2011-03-30 00:08:59','T','22.2'),
 (19,40,1,1,'2011-03-30 00:28:12','T','22.2'),
 (20,41,1,1,'2011-04-30 16:53:21','T','22.2'),
 (21,42,NULL,NULL,NULL,NULL,NULL),
 (22,43,NULL,NULL,NULL,NULL,NULL),
 (23,44,NULL,NULL,NULL,NULL,NULL),
 (24,45,1,1,'2012-01-03 22:10:32','T','22.6'),
 (25,51,1,1,NULL,'T','45'),
 (26,53,1,1,NULL,'T','45');
/*!40000 ALTER TABLE `sms_station_event` ENABLE KEYS */;


--
-- Definition of table `sms_status`
--

DROP TABLE IF EXISTS `sms_status`;
CREATE TABLE `sms_status` (
  `ID_SMS_STATUS` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_SMS` int(10) unsigned DEFAULT NULL,
  `ID_STATION` int(10) unsigned DEFAULT NULL,
  `ALERT_VARIABLE` varchar(5) DEFAULT NULL,
  `VALUE` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID_SMS_STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sms_status`
--

/*!40000 ALTER TABLE `sms_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_status` ENABLE KEYS */;


--
-- Definition of table `station`
--

DROP TABLE IF EXISTS `station`;
CREATE TABLE `station` (
  `ID_STATION` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `STATUS` varchar(45) DEFAULT NULL,
  `ID_SIMCARD` int(10) unsigned DEFAULT NULL,
  `ID_CUSTOMER` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_STATION`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `station`
--

/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` (`ID_STATION`,`STATUS`,`ID_SIMCARD`,`ID_CUSTOMER`) VALUES 
 (1,'1',4,1),
 (2,'1',2,1),
 (3,'1',1,2);
/*!40000 ALTER TABLE `station` ENABLE KEYS */;


--
-- Definition of table `station_error`
--

DROP TABLE IF EXISTS `station_error`;
CREATE TABLE `station_error` (
  `ID_ERROR` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  `COD_ERROR` varchar(45) DEFAULT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ID_STATION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_ERROR`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `station_error`
--

/*!40000 ALTER TABLE `station_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `station_error` ENABLE KEYS */;


--
-- Definition of table `thread`
--

DROP TABLE IF EXISTS `thread`;
CREATE TABLE `thread` (
  `ID_THREAD` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(5) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `ENABLE` tinyint(1) DEFAULT NULL,
  `STARTED` tinyint(1) DEFAULT NULL,
  `ID_GSMMODEM` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_THREAD`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `thread`
--

/*!40000 ALTER TABLE `thread` DISABLE KEYS */;
INSERT INTO `thread` (`ID_THREAD`,`TYPE`,`NAME`,`ENABLE`,`STARTED`,`ID_GSMMODEM`) VALUES 
 (1,'R','RECEPTOR-1',1,1,1),
 (2,'S','SENDER-1',1,1,1);
/*!40000 ALTER TABLE `thread` ENABLE KEYS */;


--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID_USER` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `LAST_NAME` varchar(45) NOT NULL,
  `EMAIL` varchar(45) NOT NULL,
  `ID_SIMCARD` int(10) unsigned NOT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_USER`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`ID_USER`,`NAME`,`LAST_NAME`,`EMAIL`,`ID_SIMCARD`,`IS_ACTIVE`) VALUES 
 (1,'Carlos','Fontana','cfontana0@gmail.com',3,1),
 (2,'Juan','Martini','jcruzmartini@gmail.com',1,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;


--
-- Definition of table `validation_rule`
--

DROP TABLE IF EXISTS `validation_rule`;
CREATE TABLE `validation_rule` (
  `ID_RULE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  `RULE_CODE` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID_RULE`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `validation_rule`
--

/*!40000 ALTER TABLE `validation_rule` DISABLE KEYS */;
INSERT INTO `validation_rule` (`ID_RULE`,`NAME`,`DESCRIPTION`,`RULE_CODE`) VALUES 
 (1,'CustomerStatusRule','CustomerStatusRule','CSR'),
 (2,'MaximumRequestCustomerRule','MaximumRequestCustomerRule','MRCR');
/*!40000 ALTER TABLE `validation_rule` ENABLE KEYS */;


--
-- Definition of table `variable`
--

DROP TABLE IF EXISTS `variable`;
CREATE TABLE `variable` (
  `ID_VARIABLE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  `UNIT` varchar(45) DEFAULT NULL,
  `CODE` varchar(45) DEFAULT NULL,
  `IS_METEOROLOGICAL` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID_VARIABLE`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `variable`
--

/*!40000 ALTER TABLE `variable` DISABLE KEYS */;
INSERT INTO `variable` (`ID_VARIABLE`,`DESCRIPTION`,`UNIT`,`CODE`,`IS_METEOROLOGICAL`) VALUES 
 (1,'Temperatura','°C','T',1),
 (2,'Lluvia','mm','L',1),
 (3,'Humedad','%','H',1),
 (4,'Presion','hPas','P',1),
 (5,'Nivel Bateria','%','BAT',0),
 (6,'Temperatura Interna','°C','TEMP',0);
/*!40000 ALTER TABLE `variable` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
