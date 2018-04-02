/*
Navicat MySQL Data Transfer

Source Server Version : 50717
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-29 10:36:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `jobcron`
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
