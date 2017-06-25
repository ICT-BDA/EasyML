/*
Navicat MySQL Data Transfer

Source Server         : 10.20.13.7
Source Server Version : 50717
Source Host           : 10.20.13.7:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-05-01 22:45:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dataset
-- ----------------------------
DROP TABLE IF EXISTS `dataset`;
CREATE TABLE `dataset` (
  `id` varchar(100) NOT NULL,
  `name` varchar(60) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  `deprecated` tinyint(1) DEFAULT NULL,
  `contenttype` varchar(30) DEFAULT NULL,
  `version` varchar(30) DEFAULT NULL,
  `createdate` varchar(100) DEFAULT NULL,
  `description` mediumtext,
  `storetype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dataset
-- ----------------------------
INSERT INTO `dataset` VALUES ('Dataset', 'DataSet', 'System Data', 'bdaict@hotmail.com', '/EML/Data/Dataset', '0', null, '0.1', '2017-04-29 16:34:01', '', 'General');
