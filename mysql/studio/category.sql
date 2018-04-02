/*
Navicat MySQL Data Transfer

Source Server Version : 50717
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-29 10:36:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `category`
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

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'System Program', '1', 'prog', 'System Program', null, '0', '2016-06-17 14:32:48');
INSERT INTO `category` VALUES ('0A0F402F-670F-4696-9D9C-42F0E0D665A00', 'System Data', '1', 'data', 'System Data', '', '0', '2016-06-17 14:32:48');
INSERT INTO `category` VALUES ('0A0F402F-670F-4696-9D9C-42F0E0D665A1', 'Shared Program', '1', 'prog', 'Shared Program', null, '0', '2016-06-17 14:32:48');
INSERT INTO `category` VALUES ('0A0F402F-670F-4696-9D9C-42F0E0D665A11', 'Shared Data', '1', 'data', 'Shared Data', '', '0', '2016-06-17 14:32:48');
INSERT INTO `category` VALUES ('0A0F402F-670F-4696-9D9C-42F0E0D665A2', 'My Program', '1', 'prog', 'My Program', null, '0', '2016-06-17 14:32:48');
INSERT INTO `category` VALUES ('0A0F402F-670F-4696-9D9C-42F0E0D665A22', 'My Data', '1', 'data', 'My Data', '', '0', '2016-06-17 14:32:48');
