/*
Navicat MySQL Data Transfer

Source Server         : BDA(烟台)
Source Server Version : 50717
Source Host           : 10.20.13.7:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-10-18 16:28:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for moduleversion
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
