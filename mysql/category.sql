/*
Navicat MySQL Data Transfer

Source Server         : BDA(118)
Source Server Version : 50553
Source Host           : 10.61.1.118:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2017-06-21 09:37:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` varchar(100) NOT NULL,
  `name` varchar(60) NOT NULL,
  `level` varchar(60) NOT NULL,
  `type` varchar(10) NOT NULL,
  `path` longtext,
  `fatherid` varchar(100) DEFAULT NULL,
  `haschild` tinyint(1) DEFAULT NULL,
  `createtime` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
