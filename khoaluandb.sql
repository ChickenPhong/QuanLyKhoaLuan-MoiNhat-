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
  `deTaiKhoaLuan_SinhVien_id` int DEFAULT NULL,
  `giangVienPhanBien_id` int DEFAULT NULL,
  `tieuChi` text,
  `diem` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `giangVienPhanBien_id` (`giangVienPhanBien_id`),
  KEY `fk_bangdiem_dtsv` (`deTaiKhoaLuan_SinhVien_id`),
  CONSTRAINT `bangdiems_ibfk_2` FOREIGN KEY (`giangVienPhanBien_id`) REFERENCES `nguoidungs` (`id`),
  CONSTRAINT `fk_bangdiem_dtsv` FOREIGN KEY (`deTaiKhoaLuan_SinhVien_id`) REFERENCES `detaikhoaluan_sinhvien` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bangdiems`
--

LOCK TABLES `bangdiems` WRITE;
/*!40000 ALTER TABLE `bangdiems` DISABLE KEYS */;
INSERT INTO `bangdiems` VALUES (23,47,67,'Tính thực tế của dự án',7),(24,47,67,'Logic luồng xử lý',4),(25,47,67,'Các chức năng cơ bản',5),(26,47,68,'Tính thực tế của dự án',6),(27,47,68,'Logic luồng xử lý',7),(28,47,68,'Các chức năng cơ bản',8),(29,45,70,'Tính thực tế của dự án',5),(30,45,70,'Logic luồng xử lý',8),(31,45,70,'Các chức năng cơ bản',4),(32,49,70,'Tính thực tế của dự án',3),(33,49,70,'Logic luồng xử lý',8),(34,49,70,'Các chức năng cơ bản',4),(35,44,69,'Tính thực tế của dự án',6),(36,44,69,'Logic luồng xử lý',4),(37,44,69,'Các chức năng cơ bản',5),(38,48,69,'Tính thực tế của dự án',3),(39,48,69,'Logic luồng xử lý',5),(40,48,69,'Các chức năng cơ bản',4),(41,46,69,'Tính thực tế của dự án',8),(42,46,69,'Logic luồng xử lý',5),(43,46,69,'Các chức năng cơ bản',3),(53,50,69,'Tính thực tế của dự án',6),(54,50,69,'Logic luồng xử lý',8),(55,50,69,'Các chức năng cơ bản',5);
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
  `detaikhoaluan_sinhvien_id` int DEFAULT NULL,
  `giangVienHuongDan_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `giangVienHuongDan_id` (`giangVienHuongDan_id`),
  KEY `fk_gvhd_sinhvien` (`detaikhoaluan_sinhvien_id`),
  CONSTRAINT `detaikhoaluan_giangvienhuongdan_ibfk_2` FOREIGN KEY (`giangVienHuongDan_id`) REFERENCES `nguoidungs` (`id`),
  CONSTRAINT `fk_gvhd_sinhvien` FOREIGN KEY (`detaikhoaluan_sinhvien_id`) REFERENCES `detaikhoaluan_sinhvien` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluan_giangvienhuongdan`
--

LOCK TABLES `detaikhoaluan_giangvienhuongdan` WRITE;
/*!40000 ALTER TABLE `detaikhoaluan_giangvienhuongdan` DISABLE KEYS */;
INSERT INTO `detaikhoaluan_giangvienhuongdan` VALUES (51,44,70),(52,45,68),(53,46,70),(54,47,67),(55,48,69),(56,49,69),(57,50,68),(58,45,69);
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
  `detaikhoaluan_sinhvien_id` int DEFAULT NULL,
  `hoiDong_id` int DEFAULT NULL,
  `locked` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `hoiDong_id` (`hoiDong_id`),
  KEY `fk_dkl_sv` (`detaikhoaluan_sinhvien_id`),
  CONSTRAINT `detaikhoaluan_hoidong_ibfk_2` FOREIGN KEY (`hoiDong_id`) REFERENCES `hoidongs` (`id`),
  CONSTRAINT `fk_dkl_sv` FOREIGN KEY (`detaikhoaluan_sinhvien_id`) REFERENCES `detaikhoaluan_sinhvien` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluan_hoidong`
--

LOCK TABLES `detaikhoaluan_hoidong` WRITE;
/*!40000 ALTER TABLE `detaikhoaluan_hoidong` DISABLE KEYS */;
INSERT INTO `detaikhoaluan_hoidong` VALUES (60,44,14,0),(61,45,15,0),(62,46,16,0),(63,47,17,0),(64,48,14,0),(65,49,15,0),(66,50,16,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluan_sinhvien`
--

LOCK TABLES `detaikhoaluan_sinhvien` WRITE;
/*!40000 ALTER TABLE `detaikhoaluan_sinhvien` DISABLE KEYS */;
INSERT INTO `detaikhoaluan_sinhvien` VALUES (44,15,76),(45,16,77),(46,17,78),(47,15,79),(48,16,80),(49,17,81),(50,15,82),(54,18,83),(55,19,84),(56,18,85),(57,19,90),(58,18,91),(59,19,92);
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
  `status` varchar(20) NOT NULL DEFAULT 'active',
  `khoa` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detaikhoaluans`
--

LOCK TABLES `detaikhoaluans` WRITE;
/*!40000 ALTER TABLE `detaikhoaluans` DISABLE KEYS */;
INSERT INTO `detaikhoaluans` VALUES (14,'Quản lý khách sạn','disabled','Công nghệ thông tin'),(15,'Quản lý phòng trọ','active','Công nghệ thông tin'),(16,'Quản lý phòng net','active','Công nghệ thông tin'),(17,'Quản lý rạp chiếu phim','active','Công nghệ thông tin'),(18,'Quản lý tài chính trong đầu tư','active','Quản trị kinh doanh'),(19,'Quản lý rủi ro về thị trường','active','Quản trị kinh doanh');
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoidongs`
--

LOCK TABLES `hoidongs` WRITE;
/*!40000 ALTER TABLE `hoidongs` DISABLE KEYS */;
INSERT INTO `hoidongs` VALUES (14,'Hội đồng 1','active','Công nghệ thông tin'),(15,'Hội đồng 2','active','Công nghệ thông tin'),(16,'Hội đồng 3','active','Công nghệ thông tin'),(17,'test ','active','Công nghệ thông tin'),(18,'Hội đồng Quản trị 1','active','Quản trị kinh doanh');
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
  `nganh` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoidungs`
--

LOCK TABLES `nguoidungs` WRITE;
/*!40000 ALTER TABLE `nguoidungs` DISABLE KEYS */;
INSERT INTO `nguoidungs` VALUES (61,'Nguyễn Văn Sáng','admin1','$2a$10$Af7DHT4moV4hfhQk.Y7DFu6ACjD5mgzXPMgs3Bjf0qKUqtx41yOhO','ROLE_ADMIN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589433/wnpchzaluqvpncqzv546.jpg','admin@gmail.com',NULL,NULL,NULL),(62,'Trần Huỳnh Sang','sang','$2a$10$7EMJ/7JZ.l3IkRc71nPbyOF7d0f2PBtl5NBezv3vO6DMfsTD0MwOS','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589511/ng44ws8ctnxtbb6zqdws.jpg','hsnga@gmail.com','Công nghệ thông tin',NULL,NULL),(63,'Tô Quốc Bình','binh','$2a$10$F3nuhcX2qGQOB8nVN67gSO6cE0uyWQO1rzjlUUuFfOB0WdWNH/Ilm','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589537/vhgcrmnierqpukptwtoa.jpg','binh@gmail.com','Quản trị kinh doanh',NULL,NULL),(64,'Trần Quốc Bảo','bao','$2a$10$5vBxVFKhzUwWcHRjMznBe.k5vNK7CO65CUTyqekcSBwwIkE2iEwFC','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589588/cuyjwlc3ruqfvgz9ekui.jpg','bao@gmail.com','Tài chính - Ngân hàng',NULL,NULL),(65,'Nguyễn Thị Thanh','thanh','$2a$10$YzLqT1wSMbnln1ldcHK0we2FMfpKHwoYCAFt6fDfuKyUb2dmVhwe2','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589616/bu3mdzjavmdbpicujqqu.jpg','thanh@gmail.com','Ngôn ngữ',NULL,NULL),(66,'Tô Oai Hùng','hung','$2a$10$/mafb8acYXtGejzwaWBxEObNGK8ILKeJRSw.ZRHSQOHeLovgoxPL2','ROLE_GIAOVU','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589647/suphnanxpc8n2tieucxp.jpg','hung@gmail.com','Công nghệ sinh học',NULL,NULL),(67,'Trần Thanh Tâm','tam','$2a$10$BP4hbIDn/KvHaEQIoTTgLOWAUZ8O1nQhCXPk96KnA6QvM8gOYX5sa','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589705/arhiplmffh84pz8zy876.jpg','tam@gmail.com','Công nghệ thông tin',NULL,NULL),(68,'Nguyễn Ngọc Ánh','anh','$2a$10$wAvQyy6JN32TqrabOetqbextTvInwfYyuqk49y8NlSaXvFdw07EV.','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748589744/c6lfp8ws8rfk55lju8qk.jpg','an@gmail.com','Công nghệ thông tin',NULL,NULL),(69,'Nguyễn Đăng Khôi','khoi','$2a$10$EUYsTsGXN5hr3FZ1dOxLZO8rR/WOdVdB6RZAoESXcwJTPi0nLYHbC','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590060/kw8g6svtnpmlvburamsc.jpg','2251010052khoi@ou.edu.vn','Công nghệ thông tin',NULL,NULL),(70,'Trần Quốc Phong','phong','$2a$10$k0wTxyVonZGgq2IKIYLC0ed9oOiVRUcrLuuNmNAFovZOi58Mr8Dka','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590109/pgqbsqc8lzm1bqjlyebs.jpg','2251010073phong@ou.edu.vn','Công nghệ thông tin',NULL,NULL),(71,'Nguyễn Văn Mai','mai','$2a$10$ho8FCSQ28wZD4hzItxZyw.8/w6cFaDbvunjLWOIN01F/iGURln1ti','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590227/ruallppqykpynfpqi4sg.jpg','mai@gmail.com','Công nghệ thông tin',NULL,NULL),(72,'Nguyễn Đăng Bách','bach','$2a$10$Ek5/XhCwH4BFOpYjpeBrcOwFLcFlYNGrJFMkzArXT9LMFj6x1X6Ou','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590262/ssiuekvtr0twpg3sabvh.jpg','bach@gmail.com','Quản trị kinh doanh',NULL,NULL),(73,'Nguyễn Văn Ba','ba','$2a$10$VgFMdboTY/XUUzGs69I.EOo36DUh5pixheLu9iHLDof0U6UzqWX1i','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590339/zfxhedss3mmoqng2julf.jpg','ba@gmail.com','Quản trị kinh doanh',NULL,NULL),(74,'Nguyễn Thị Tuyền','tuyen','$2a$10$QpmrHmEKhGSo92AOeJ8Se.QHG49dXux9EyH8MCMDVuDCZ6KOz7KHu','ROLE_GIANGVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590371/y5gpfwsvxtcuwxzukpgh.jpg','tuyen@gmail.com','Quản trị kinh doanh',NULL,NULL),(76,'Trần Thanh Nhàn','nhan','$2a$10$7p7Gwms9L8InANoWtEk9bOfSwMVbvbaBCTSa8LGHnJCq5gQwDbGIy','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590623/ztrqgnx78gzjf0z7nozy.png','nhan@gmail.com','Công nghệ thông tin','2020','Khoa học máy tính'),(77,'Trần Thanh Thảo','thao','$2a$10$e/YJCgQ9QET5UNYZ66L11uKgG7d8VXdqOG0xENImOJKOLxwUeJSX2','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590655/hn5wqjccgaccszus8mms.png','thao@gmail.com','Công nghệ thông tin','2020','Khoa học máy tính'),(78,'Trần Thanh Huyền','huyen','$2a$10$uawvais3hvaMcKUs/wBaTusEKLD96Bcrbax9feoORhs/MCsJjRMb2','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590764/rxmc02ko0cxzmt7hi6mp.png','huyen@gmail.com','Công nghệ thông tin','2020','Hệ thống thông tin quản lý'),(79,'Trần Thanh Gai','gai','$2a$10$Gzstz5VOZyfOgadNHH4mu.inMvVfBPI7TaQPBGqdXN1b/EYKXjVd6','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590815/ln4xsb7lpxxkumuztv3e.jpg','gai@gmail.com','Công nghệ thông tin','2020','Hệ thống thông tin quản lý'),(80,'Trần Như Ý','y','$2a$10$e0bLFMT1PyXvopxuKJOZ8ei3b0L5Erx/CAxnXQF5Q6nWLFTxacf0S','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590848/pobz9r0sewlcpeabvb1h.jpg','y@gmail.com','Công nghệ thông tin','2020','Công nghệ thông tin'),(81,'Trần Như Thắm','tham','$2a$10$tgNqUiwTR.SwLmhdhAvbdeop4mJjyqQ0FRZ0zl0l/IvQg3cpYwEYi','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590884/shd8qbvo1zstk0igbnxf.jpg','tham@gmail.com','Công nghệ thông tin','2020','Công nghệ thông tin'),(82,'Trần Như Quỳnh','quynh','$2a$10$VdmB.9h0xeWE1AHtRtlHEuH04O.pHGVNBKPv965IKDuPBz3skHZNe','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748590947/nbfmhtl6uxutzgoytkfe.jpg','quynh@gmail.com','Công nghệ thông tin','2020','Trí tuệ nhân tạo'),(83,'Nguyễn Ngọc Hân','han','$2a$10$6AumJLiMF1HA1nhEPKR9pee/bv0F.Zozofm91Nmm/K7vjCDMf/C/2','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591030/npvsqxijxwyhyitechcf.jpg','han@gmail.com','Quản trị kinh doanh','2020','Kinh doanh quốc tế'),(84,'Nguyễn Ngọc Thư','thu','$2a$10$GgzidqmVXIJDCzOmCAWmYOU0biLRGssUF7qlaeOnpq4mtxj5X1NLG','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591069/hcp1umozooo2aychwkcx.png','thu@gmail.com','Quản trị kinh doanh','2020','Marketing'),(85,'Nguyễn Ngọc Toàn','toan','$2a$10$0M88VQZrAU1qLshPjJ3vK.6UzKC7oC8cEum6VFQH0cCEIepkcVz4m','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591142/poru4cluo2f9qsctvs2w.jpg','toan@gmail.com','Quản trị kinh doanh','2020','Quản trị kinh doanh'),(86,'Trần Thanh Bảo Khánh','khanh','$2a$10$hjEcHKW4KdLwOuOfe.SVoeF/mmlNPTeAl4WpOKgAxxQI4roB2OXPC','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591218/t6zpygrndj8s8z8sc9vw.jpg','bkhanhtran2010@gmail.com','Công nghệ thông tin','2021','Khoa học máy tính'),(87,'Trần Quốc Vỹ','vy','$2a$10$yDeLsDcREYY7Q94d3ilOs.I5t3TtBlGJwjihhs4ZB2o.fdeor/alO','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591295/vucwlobqyxjrsflzxfnq.jpg','tqphong2004@gmail.com','Công nghệ thông tin','2021','Hệ thống thông tin quản lý'),(88,'Trần Thanh Mẫn','man','$2a$10$647q3./66maZ5SJ1kVSCleGSXwBehlJcU6dTIZmutBTU/L.BFmItq','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591369/plhzuucqgqdhjxcphlat.jpg','asamikiri2@gmail.com','Công nghệ thông tin','2021','Công nghệ thông tin'),(89,'Trần Thị Như Gia','gia','$2a$10$b4jIXK6qr0duG5yKNosQ9OLxHb7PdCeOpyufydPaOtMUIOMSPWlqS','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748591419/zhe74ewdcn4cut5qpnuk.jpg','gia@gmail.com','Công nghệ thông tin','2021','Trí tuệ nhân tạo'),(90,'Nguyễn Ngọc Kem','kem','$2a$10$xGABM596zKXowwdrF0gWg.CfVQOYDrFox4Bg5ta/0Aaaw3lnphsoG','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748649534/nmiurotg9eojhzgunwt8.png','kem@gmail.com','Quản trị kinh doanh','2020','Quản trị nhân lực'),(91,'Nguyễn Ngọc Chiến','chien','$2a$10$QNBm1TJ7XfyYlEqctba2Qel9keGaD86wCDTxnD020g09hAB7urRW.','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748649572/mn0fxopbx3iqhjrcohrd.jpg','chien@gmail.com','Quản trị kinh doanh','2020','Logistics và Quản lý chuỗi cung ứng'),(92,'Nguyễn Ngọc Vi','vi','$2a$10$dMp4qE3b6CdpmEpxokTzdOL.k3kZU9jTigGbhbBmVcYlLHw6GAIKC','ROLE_SINHVIEN','https://res.cloudinary.com/dp4fipzce/image/upload/v1748649620/mkqyqcvbjffxd8f20vzk.jpg','vi@gmail.com','Quản trị kinh doanh','2020','Kinh doanh quốc tế');
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phanconggiangvienphanbiens`
--

LOCK TABLES `phanconggiangvienphanbiens` WRITE;
/*!40000 ALTER TABLE `phanconggiangvienphanbiens` DISABLE KEYS */;
INSERT INTO `phanconggiangvienphanbiens` VALUES (20,69,14),(21,70,15),(22,69,16),(23,67,17),(24,68,17),(25,74,18),(26,73,19);
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
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thanhvienhoidong`
--

LOCK TABLES `thanhvienhoidong` WRITE;
/*!40000 ALTER TABLE `thanhvienhoidong` DISABLE KEYS */;
INSERT INTO `thanhvienhoidong` VALUES (38,14,67,'chu_tich'),(39,14,68,'thu_ky'),(40,14,69,'phan_bien'),(41,15,69,'chu_tich'),(42,15,68,'thu_ky'),(43,15,70,'phan_bien'),(44,16,70,'chu_tich'),(45,16,71,'thu_ky'),(46,16,69,'phan_bien'),(47,17,70,'chu_tich'),(48,17,69,'thu_ky'),(49,17,67,'phan_bien'),(50,17,68,'phan_bien'),(51,18,73,'chu_tich'),(52,18,72,'thu_ky'),(53,18,74,'phan_bien');
/*!40000 ALTER TABLE `thanhvienhoidong` ENABLE KEYS */;
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
  `nguoi_tao` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_nguoi_tao` (`nguoi_tao`),
  CONSTRAINT `fk_nguoi_tao` FOREIGN KEY (`nguoi_tao`) REFERENCES `nguoidungs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tieuchis`
--

LOCK TABLES `tieuchis` WRITE;
/*!40000 ALTER TABLE `tieuchis` DISABLE KEYS */;
INSERT INTO `tieuchis` VALUES (20,'Các chức năng cơ bản','active','Công nghệ thông tin',62),(21,'Tính thực tế của dự án','active','Công nghệ thông tin',62),(22,'Logic luồng xử lý','active','Công nghệ thông tin',62),(23,'Các ý trong tình huống thực tế','active','Quản trị kinh doanh',63),(24,'Tính sáng tạo trong nghiên cứu','active','Quản trị kinh doanh',63);
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

-- Dump completed on 2025-05-31  7:16:58
