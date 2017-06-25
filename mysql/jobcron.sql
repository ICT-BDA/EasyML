/*
Navicat MySQL Data Transfer

Source Server         : 10.20.13.7
Source Server Version : 50717
Source Host           : 10.20.13.7:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-05-01 22:45:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jobcron
-- ----------------------------
DROP TABLE IF EXISTS `jobcron`;
CREATE TABLE `jobcron` (
  `bda_job_id` varchar(45) NOT NULL,
  `cron_pattern` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bda_job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobcron
-- ----------------------------
