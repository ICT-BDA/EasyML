/*
Navicat MySQL Data Transfer

Source Server         : 10.20.13.7
Source Server Version : 50717
Source Host           : 10.20.13.7:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-05-01 22:47:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for supernode
-- ----------------------------
DROP TABLE IF EXISTS `supernode`;
CREATE TABLE `supernode` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `bdajob` varchar(255) DEFAULT NULL,
  `graphxml` text,
  `status` varchar(255) DEFAULT NULL,
  `describ` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of supernode
-- ----------------------------
