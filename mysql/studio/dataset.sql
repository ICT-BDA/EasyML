/*
Navicat MySQL Data Transfer

Source Server Version : 50717
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-29 10:36:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dataset`
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
INSERT INTO `dataset` VALUES ('A2EEE4A9-EA84-47D2-9EAA-CC8CE701FD49', 'Dataset', '0A0F402F-670F-4696-9D9C-42F0E0D665A00', 'bdaict@hotmail.com', '/EML/Data/A2EEE4A9-EA84-47D2-9EAA-CC8CE701FD49', '0', null, '0.1', '2018-03-28 18:40:08', '', '');
INSERT INTO `dataset` VALUES ('EA15288C-40D5-4E44-884E-0B0C508651BF', 'mnist', '0A0F402F-670F-4696-9D9C-42F0E0D665A00', 'bdaict@hotmail.com', '/EML/Data/EA15288C-40D5-4E44-884E-0B0C508651BF', '0', null, '0.1', '2018-03-28 18:39:27', '', '');
