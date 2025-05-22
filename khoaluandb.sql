-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: khoaluandb
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `bangdiems`
--

DROP TABLE IF EXISTS `bangdiems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bangdiems` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deTaiKhoaLuan_id` int DEFAULT NULL,
  `giangVienPhanBien_id` int DEFAULT NULL,
  `tieuChi` text,
  `diem` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `deTaiKhoaLuan_id` (`deTaiKhoaLuan_id`),
  KEY `giangVienPhanBien_id` (`giangVienPhanBien_id`),
  CONSTRAINT `bangdiems_ibfk_1` FOREIGN KEY (`deTaiKhoaLuan_id`) REFERENCES `detaikhoaluans` (`id`),
  CONSTRAINT `bangdiems_ibfk_2` FOREIGN KEY (`giangVienPhanBien_id`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bangdiems`
--

LOCK TABLES `bangdiems` WRITE;
/*!40000 ALTER TABLE `bangdiems` DISABLE KEYS */;
INSERT INTO `bangdiems` VALUES (5,2,17,'Độ chính xác của nghiên cứu',6.5),(6,2,17,'Tính khả dụng của nghiên cứu',8),(7,1,16,'Độ chính xác của nghiên cứu',7),(8,1,16,'Tính khả dụng của nghiên cứu',6);
/*!40000 ALTER TABLE `bangdiems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detaikhoaluan_giangvienhuongdan`
--

DROP TABLE IF EXISTS `detaikhoaluan_giangvienhuongdan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detaikhoaluan_giangvienhuongdan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deTaiKhoaLuan_id` int DEFAULT NULL,
  `giangVienHuongDan_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `deTaiKhoaLuan_id` (`deTaiKhoaLuan_id`),
  KEY `giangVienHuongDan_id` (`giangVienHuongDan_id`),
  CONSTRAINT `detaikhoaluan_giangvienhuongdan_ibfk_1` FOREIGN KEY (`deTaiKhoaLuan_id`) REFERENCES `detaikhoaluans` (`id`),
  CONSTRAINT `detaikhoaluan_giangvienhuongdan_ibfk_2` FOREIGN KEY (`giangVienHuongDan_id`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluan_giangvienhuongdan`
--

LOCK TABLES `detaikhoaluan_giangvienhuongdan` WRITE;
/*!40000 ALTER TABLE `detaikhoaluan_giangvienhuongdan` DISABLE KEYS */;
INSERT INTO `detaikhoaluan_giangvienhuongdan` VALUES (1,1,7),(2,2,8),(5,1,8);
/*!40000 ALTER TABLE `detaikhoaluan_giangvienhuongdan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detaikhoaluan_hoidong`
--

DROP TABLE IF EXISTS `detaikhoaluan_hoidong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detaikhoaluan_hoidong` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deTaiKhoaLuan_id` int DEFAULT NULL,
  `hoiDong_id` int DEFAULT NULL,
  `locked` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `deTaiKhoaLuan_id` (`deTaiKhoaLuan_id`),
  KEY `hoiDong_id` (`hoiDong_id`),
  CONSTRAINT `detaikhoaluan_hoidong_ibfk_1` FOREIGN KEY (`deTaiKhoaLuan_id`) REFERENCES `detaikhoaluans` (`id`),
  CONSTRAINT `detaikhoaluan_hoidong_ibfk_2` FOREIGN KEY (`hoiDong_id`) REFERENCES `hoidongs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluan_hoidong`
--

LOCK TABLES `detaikhoaluan_hoidong` WRITE;
/*!40000 ALTER TABLE `detaikhoaluan_hoidong` DISABLE KEYS */;
INSERT INTO `detaikhoaluan_hoidong` VALUES (13,1,3,1),(14,2,4,1);
/*!40000 ALTER TABLE `detaikhoaluan_hoidong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detaikhoaluan_sinhvien`
--

DROP TABLE IF EXISTS `detaikhoaluan_sinhvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detaikhoaluan_sinhvien` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deTaiKhoaLuan_id` int DEFAULT NULL,
  `sinhVien_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `deTaiKhoaLuan_id` (`deTaiKhoaLuan_id`),
  KEY `sinhVien_id` (`sinhVien_id`),
  CONSTRAINT `detaikhoaluan_sinhvien_ibfk_1` FOREIGN KEY (`deTaiKhoaLuan_id`) REFERENCES `detaikhoaluans` (`id`),
  CONSTRAINT `detaikhoaluan_sinhvien_ibfk_2` FOREIGN KEY (`sinhVien_id`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluan_sinhvien`
--

LOCK TABLES `detaikhoaluan_sinhvien` WRITE;
/*!40000 ALTER TABLE `detaikhoaluan_sinhvien` DISABLE KEYS */;
INSERT INTO `detaikhoaluan_sinhvien` VALUES (1,1,9),(2,2,14);
/*!40000 ALTER TABLE `detaikhoaluan_sinhvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detaikhoaluans`
--

DROP TABLE IF EXISTS `detaikhoaluans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detaikhoaluans` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `khoa` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluans`
--

LOCK TABLES `detaikhoaluans` WRITE;
/*!40000 ALTER TABLE `detaikhoaluans` DISABLE KEYS */;
INSERT INTO `detaikhoaluans` VALUES (1,'Quản lý khách sạn','Công nghệ thông tin'),(2,'Quản lý bãi đỗ xe','Công nghệ thông tin'),(3,'Quản lý thư viện','Công nghệ thông tin'),(4,'Quản lý cửa hàng tiện lợi','Công nghệ thông tin');
/*!40000 ALTER TABLE `detaikhoaluans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoidongs`
--

DROP TABLE IF EXISTS `hoidongs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoidongs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `status` enum('active','closed') DEFAULT 'active',
  `khoa` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoidongs`
--

LOCK TABLES `hoidongs` WRITE;
/*!40000 ALTER TABLE `hoidongs` DISABLE KEYS */;
INSERT INTO `hoidongs` VALUES (3,'Hội đồng 1','active','Công nghệ thông tin'),(4,'Hội đồng 2','active','Công nghệ thông tin'),(6,'test','active','Công nghệ thông tin');
/*!40000 ALTER TABLE `hoidongs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nguoidungs`
--

DROP TABLE IF EXISTS `nguoidungs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nguoidungs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fullname` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `khoa` varchar(100) DEFAULT NULL,
  `khoaHoc` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoidungs`
--

LOCK TABLES `nguoidungs` WRITE;
/*!40000 ALTER TABLE `nguoidungs` DISABLE KEYS */;
INSERT INTO `nguoidungs` VALUES (2,'Nguyễn Văn A','admin','$2a$10$fdEqk74SrGthQ4g9GTb6XOjWlQRjlZmlcorWNXOQCO8W00pTUarzO','ROLE_ADMIN','admin.png','admin1@example.com',NULL,NULL),(5,'Trần Huỳnh Sang','sang','$2a$10$uHhxwGZi.5RIvWwxaUqNLOUQkrhP.n3/vJkSiw0Xls3fttESGVzAW','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1746964288/rbudpadcmrgbw0o0a52l.jpg','sang12@gmail.com','Công nghệ thông tin',NULL),(7,'Tô Quốc Bình','binh','$2a$10$GAkTj5yjHg0XbWpF5zPgLuJ7OGKGBGo.qPTxKaIFoD0F0ezdBbiZO','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1746964633/mzoody395lo41z3oodz8.jpg','binh@gmail.com','Công nghệ thông tin',NULL),(8,'Trần Quốc Phong','phong','$2a$10$/gyHfcy9195gsLrE9y8GJu7UDScJ1rQWeE2RGnO4z2f4K9wL3Y9AC','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1746967846/rbfcnahhp55ebq7ulkiz.jpg','asamikiri2004@gmail.com','Công nghệ thông tin',NULL),(9,'Nguyễn Đăng Khôi','khoi','$2a$10$qyFEvDXFwv7mp1U1YhVtOuwmw8wIOQzXJv4W3JOjRC.7AJLYWoBnW','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1746967972/gf6jgp03jtks6xkyjrfw.jpg','tqphong2004@gmail.com','Công nghệ thông tin','2022'),(11,'Nguyễn Huỳnh Đan','giaovu1','$2a$10$gbqyucHnmBQoS/FNXIADlOgnMbWAXUnylVxQfxDp0mZs7fKR3Oh0m','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1747018459/cne69oxhvyy1iftwcyqz.jpg','ffg@gmail.com','Quản trị kinh doanh',NULL),(14,'Trần Văn Ben','ben','$2a$10$.RfH6fZkMacqKPJ.5N3x1emMwWdV77bFIPNEkr8Dz12pOcXme4GQ2','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1747042537/nudodqrgdfxdxnp7oeap.jpg','ben@gmail.com','Công nghệ thông tin','2022'),(15,'Nguyễn Như Quỳnh','quynh','$2a$10$Kvt4L5hOubUuo3tvrQxnnuwxrR57wahnOj0OHo7DhswJqmM11FO7S','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1747042876/koodiu9bun3dhcdbgmpw.jpg','quynh@gmail.com','Công nghệ thông tin','2023'),(16,'Bùi Tấn Phát','phat','$2a$10$oIaQ.lkF7HNtX1dd6QFLK.Iubd6.zY.FJ2oZpbdNoPedWdYyyiXue','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1747051536/kzrzxsiwpzr4cggldqfy.jpg','phat@gmail.com','Công nghệ thông tin',NULL),(17,'Trần Phong','phong tran','$2a$10$dqoJfW8rMsjBDJtQKHd/luqdYz0qs0WJSFNFqtIet5fOAEncZ/jNC','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1747275032/ifndemkwsxp5nhe3sf0n.jpg','2251010073phong@ou.edu.vn','Công nghệ thông tin',NULL),(19,'Bùi Xuân Đức','duc','$2a$10$bRfPuNj1X7Afp/bDdJdAeulJ2y6/9vghZ981/vCKqrVXJS5r0f7cu','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1747653210/vgr2yk9jth0sx1kmhqwk.jpg','vggg2004@gmail.com','Công nghệ thông tin',NULL),(46,'Trần Thanh Tâm','tam','$2a$10$HIlBAmQcQ.VpxW9RXqXd5.GRTL1Hpqw5.p4g49yDGI.jtug0SWsya','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1747904193/uxvwquukszgop74vjos3.jpg','13ng2004@gmail.com','Tài chính - Ngân hàng',NULL);
/*!40000 ALTER TABLE `nguoidungs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phanconggiangvienphanbiens`
--

DROP TABLE IF EXISTS `phanconggiangvienphanbiens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phanconggiangvienphanbiens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `giangVienPhanBien_id` int DEFAULT NULL,
  `hoiDong_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `giangVienPhanBien_id` (`giangVienPhanBien_id`),
  CONSTRAINT `phanconggiangvienphanbiens_ibfk_2` FOREIGN KEY (`giangVienPhanBien_id`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phanconggiangvienphanbiens`
--

LOCK TABLES `phanconggiangvienphanbiens` WRITE;
/*!40000 ALTER TABLE `phanconggiangvienphanbiens` DISABLE KEYS */;
INSERT INTO `phanconggiangvienphanbiens` VALUES (1,16,3),(2,17,4);
/*!40000 ALTER TABLE `phanconggiangvienphanbiens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thanhvienhoidong`
--

DROP TABLE IF EXISTS `thanhvienhoidong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thanhvienhoidong` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hoiDong_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `role` enum('chu_tich','thu_ky','phan_bien','thanh_vien') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `hoiDong_id` (`hoiDong_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `thanhvienhoidong_ibfk_1` FOREIGN KEY (`hoiDong_id`) REFERENCES `hoidongs` (`id`),
  CONSTRAINT `thanhvienhoidong_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thanhvienhoidong`
--

LOCK TABLES `thanhvienhoidong` WRITE;
/*!40000 ALTER TABLE `thanhvienhoidong` DISABLE KEYS */;
INSERT INTO `thanhvienhoidong` VALUES (5,3,7,'chu_tich'),(6,3,8,'thu_ky'),(7,3,16,'phan_bien'),(8,4,8,'chu_tich'),(9,4,16,'thu_ky'),(10,4,17,'phan_bien');
/*!40000 ALTER TABLE `thanhvienhoidong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thongbaos`
--

DROP TABLE IF EXISTS `thongbaos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thongbaos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nguoiDung_id` int DEFAULT NULL,
  `tinNhan` text,
  `thoiGianGui` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `nguoiDung_id` (`nguoiDung_id`),
  CONSTRAINT `thongbaos_ibfk_1` FOREIGN KEY (`nguoiDung_id`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thongbaos`
--

LOCK TABLES `thongbaos` WRITE;
/*!40000 ALTER TABLE `thongbaos` DISABLE KEYS */;
/*!40000 ALTER TABLE `thongbaos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tieuchis`
--

DROP TABLE IF EXISTS `tieuchis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tieuchis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_tieuchi` varchar(255) NOT NULL,
  `status` enum('active','closed') DEFAULT 'active',
  `khoa` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tieuchis`
--

LOCK TABLES `tieuchis` WRITE;
/*!40000 ALTER TABLE `tieuchis` DISABLE KEYS */;
INSERT INTO `tieuchis` VALUES (1,'Độ chính xác của nghiên cứu','active','Công nghệ thông tin'),(2,'Tính khả dụng của nghiên cứu','active','Công nghệ thông tin'),(10,'sd','active','Công nghệ thông tin'),(11,'xc','active','Công nghệ thông tin'),(12,'rt','active','Quản trị kinh doanh');
/*!40000 ALTER TABLE `tieuchis` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-22 16:44:13
