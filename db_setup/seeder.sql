USE `vims`;

--
-- Dumping data for table `investment_product`
--

LOCK TABLES `investment_product` WRITE;
/*!40000 ALTER TABLE `investment_product` DISABLE KEYS */;
INSERT INTO `investment_product` VALUES (1,'Something Fund','real estate',NULL,NULL,19.65),(2,'Chance Fund','bond',NULL,NULL,54.42),(3,'Assume Fund','real estate',NULL,NULL,26.49),(4,'Hold Fund','bond',NULL,NULL,87.45),(5,'Himself Fund','stock',NULL,NULL,93.15),(6,'try 1','stock','tinggi','luar biasa   ',100.90),(7,'newprod2','stock','low','buat liat ada deskripsi atau ngga',190.10);
/*!40000 ALTER TABLE `investment_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `investor`
--

LOCK TABLES `investor` WRITE;
/*!40000 ALTER TABLE `investor` DISABLE KEYS */;
INSERT INTO `investor` VALUES (1,'Maria Johnson','admin@vims.com','adminpassword','active','ADMIN',57863.54),(2,'Ashley Gill','manager@vims.com','managerpassword','active','MANAGER',74086.80),(3,'Kevin Tucker','juankoch@example.com','investorpassword','active','INVESTOR',31764.30),(4,'Arthur Johnson','shirleyflores@example.org','defaultpassword','active','INVESTOR',94227.80),(5,'James Burns','cannonjason@example.com','defaultpassword','active','INVESTOR',81262.30),(6,'James Mayo','coffeysamantha@example.org','defaultpassword','active','INVESTOR',31236.71),(7,'Brittany Allen','tracipatton@example.net','defaultpassword','banned','INVESTOR',75865.88),(8,'James Bishop','qromero@example.com','defaultpassword','active','INVESTOR',14486.84),(9,'Nicole Macdonald','oclarke@example.com','defaultpassword','banned','INVESTOR',45687.87),(10,'Kimberly Armstrong DDS','angela08@example.net','defaultpassword','active','INVESTOR',85104.25);
/*!40000 ALTER TABLE `investor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `investor_investment`
--

LOCK TABLES `investor_investment` WRITE;
/*!40000 ALTER TABLE `investor_investment` DISABLE KEYS */;
INSERT INTO `investor_investment` VALUES (1,1,16),(1,2,2),(1,3,20),(1,5,7),(2,1,17),(2,2,4),(2,3,8),(2,5,6),(3,1,1),(3,3,13),(3,4,14),(3,5,19),(4,1,11),(4,3,5),(4,4,12),(4,5,4),(5,1,14),(5,3,10),(5,4,18),(5,5,11),(6,1,17),(6,4,14),(7,3,7),(8,1,1),(8,3,1),(8,4,7),(9,1,14),(9,3,6),(9,4,14),(10,3,11);
/*!40000 ALTER TABLE `investor_investment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `product_requests`
--

LOCK TABLES `product_requests` WRITE;
/*!40000 ALTER TABLE `product_requests` DISABLE KEYS */;
INSERT INTO `product_requests` VALUES ('REQ-135B4795',3,'damn','Mutual Fund','try','REJECTED'),('REQ-1748625267724',1,'Saham Teknologi Masa Depan',NULL,NULL,'APPROVED'),('REQ-717490E6',3,'plsplspls','Stock','karna cuannn','REJECTED'),('REQ-ECE1DB11',1,'Obligasi Korporasi Stabil','Obligasi','Pendapatan tetap jangka menengah','PENDING'),('REQ001',1,'Saham XYZ Super',NULL,NULL,'PENDING');
/*!40000 ALTER TABLE `product_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,'2025-03-18 17:50:52',2,'buy',5,1),(2,'2025-04-08 03:23:06',10,'sell',9,5),(3,'2025-01-04 12:37:56',6,'buy',8,2),(4,'2025-02-04 04:42:08',8,'sell',5,1),(5,'2024-07-14 08:10:14',3,'buy',4,3),(6,'2024-09-08 16:19:34',9,'sell',9,3),(7,'2024-11-18 10:20:56',4,'sell',5,2),(8,'2024-08-29 08:05:21',9,'sell',5,2),(9,'2024-12-21 01:30:24',9,'buy',8,3),(10,'2024-06-04 18:47:02',9,'sell',10,3),(11,'2025-04-12 01:02:10',3,'buy',3,5),(12,'2024-11-17 12:03:09',8,'sell',5,3),(13,'2025-05-24 10:10:05',1,'buy',2,2),(14,'2024-09-21 03:00:40',10,'sell',3,5),(15,'2024-06-04 09:26:10',2,'buy',4,3),(16,'2024-09-05 12:23:37',5,'buy',8,1),(17,'2024-08-14 17:11:34',2,'sell',10,4),(18,'2025-04-19 17:04:41',10,'buy',1,2),(19,'2024-08-17 09:08:28',6,'buy',3,1),(20,'2024-11-10 10:18:09',3,'buy',10,1),(21,'2024-06-19 21:24:52',4,'buy',8,4),(22,'2024-11-30 01:18:11',8,'buy',3,1),(23,'2025-04-01 17:59:19',9,'sell',5,1),(24,'2025-03-13 12:28:04',9,'buy',6,1),(25,'2025-04-18 14:55:26',1,'sell',1,4),(26,'2024-07-31 14:41:00',1,'sell',6,5),(27,'2024-09-06 17:05:39',10,'sell',4,3),(28,'2024-11-17 17:44:50',9,'sell',3,3),(29,'2025-01-17 05:31:40',2,'buy',6,3),(30,'2024-05-30 08:46:37',4,'sell',10,2),(31,'2025-03-20 05:40:43',4,'sell',5,5),(32,'2024-07-31 06:01:57',7,'buy',1,3),(33,'2024-12-24 03:55:47',6,'sell',7,2),(34,'2025-02-02 07:20:42',6,'sell',1,4),(35,'2025-05-22 07:50:26',3,'sell',5,1),(36,'2024-07-17 01:24:45',10,'buy',7,2),(37,'2025-05-08 14:18:45',10,'sell',8,1),(38,'2024-11-26 06:48:27',8,'sell',2,2),(39,'2024-08-18 08:12:32',10,'sell',4,1),(40,'2025-04-28 04:56:44',9,'buy',1,5),(41,'2025-03-16 12:14:39',9,'sell',1,1),(42,'2024-09-13 07:04:56',4,'buy',6,5),(43,'2025-02-19 07:27:31',9,'sell',1,2),(44,'2024-08-04 05:28:18',1,'buy',6,4),(45,'2025-04-16 08:56:14',3,'sell',6,5),(46,'2025-02-08 17:38:45',10,'sell',6,4),(47,'2024-12-25 16:44:42',9,'sell',10,4),(48,'2024-12-30 08:18:09',9,'buy',4,4),(49,'2024-09-14 19:19:23',6,'sell',10,2),(50,'2025-01-05 20:04:10',10,'sell',6,3),(51,'2025-05-30 16:25:16',10,'buy',1,1),(52,'2025-05-30 16:26:09',10,'buy',1,1),(53,'2025-05-30 17:28:41',5,'buy',1,1),(54,'2025-05-30 17:28:41',2,'sell',1,1),(55,'2025-06-02 05:03:36',1,'buy',1,1),(56,'2025-06-02 05:05:13',1,'buy',1,4),(57,'2025-06-02 05:06:50',1,'buy',1,1),(58,'2025-06-02 05:27:15',1,'buy',1,1),(59,'2025-06-02 05:49:12',1,'sell',1,1),(60,'2025-06-02 06:39:02',1,'buy',1,1),(61,'2025-06-02 06:39:12',1,'sell',1,1),(62,'2025-06-02 06:41:00',1,'sell',1,4),(63,'2025-06-03 09:23:00',2,'buy',1,6),(64,'2025-06-03 09:23:14',2,'sell',1,6),(65,'2025-06-03 11:52:09',3,'buy',1,6),(66,'2025-06-03 11:52:36',3,'sell',1,6),(67,'2025-06-03 12:08:16',4,'buy',1,6),(68,'2025-06-03 12:08:37',4,'sell',1,6),(69,'2025-06-03 12:12:03',2,'buy',3,6),(70,'2025-06-03 12:20:03',2,'sell',3,6),(71,'2025-06-03 12:52:47',1,'buy',3,6),(72,'2025-06-03 14:13:56',1,'sell',3,6),(73,'2025-06-03 14:47:23',1,'buy',3,7),(74,'2025-06-03 14:47:31',1,'sell',3,7);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;