/*
Navicat MySQL Data Transfer

Source Server Version : 50717
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-29 10:37:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `moduleversion`
-- ----------------------------
DROP TABLE IF EXISTS `moduleversion`;
CREATE TABLE `moduleversion` (
  `oldversionid` varchar(100) CHARACTER SET utf8 NOT NULL,
  `newversionid` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`oldversionid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of moduleversion
-- ----------------------------
