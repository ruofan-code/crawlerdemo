/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : movie_v2

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2020-01-11 13:50:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mid_movie_performer
-- ----------------------------
DROP TABLE IF EXISTS `mid_movie_performer`;
CREATE TABLE `mid_movie_performer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `movie_id` bigint(20) NOT NULL,
  `performer_id` bigint(20) NOT NULL,
  `type` bigint(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `movie_id` (`movie_id`,`performer_id`,`type`),
  KEY `performer_id` (`performer_id`),
  CONSTRAINT `mid_movie_performer_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mid_movie_performer_ibfk_2` FOREIGN KEY (`performer_id`) REFERENCES `performer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5455 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for mid_movie_type
-- ----------------------------
DROP TABLE IF EXISTS `mid_movie_type`;
CREATE TABLE `mid_movie_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `movie_id` bigint(20) NOT NULL,
  `movie_type_id` bigint(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `movie_id` (`movie_id`,`movie_type_id`),
  KEY `movie_type_id` (`movie_type_id`),
  CONSTRAINT `mid_movie_type_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mid_movie_type_ibfk_2` FOREIGN KEY (`movie_type_id`) REFERENCES `movie_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1793 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `plot` varchar(256) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `release_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for movie_type
-- ----------------------------
DROP TABLE IF EXISTS `movie_type`;
CREATE TABLE `movie_type` (
  `id` bigint(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for performer
-- ----------------------------
DROP TABLE IF EXISTS `performer`;
CREATE TABLE `performer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3819 DEFAULT CHARSET=utf8mb4;
