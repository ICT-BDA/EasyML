/*
Navicat MySQL Data Transfer

Source Server Version : 50717
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-29 10:36:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `email` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varbinary(41) NOT NULL DEFAULT '',
  `verified` varchar(255) DEFAULT NULL,
  `createtime` varchar(255) NOT NULL,
  `serial` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `activetime` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `verifylink` varchar(255) DEFAULT NULL,
  `power` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('zhaohuilee@yeah.net', 'bdaict', 0x2A46443537313230333937344241394146453237304645363231353141453936374543413545304141, '', '2016-08-09 17:09:56', '8aa5e494-da77-43b4-8613-27a0ec42f945', 'd6929604-452f-4364-8455-e4d57863cf79', '2016-08-10 09:20:49', 'ICT_UCAS', 'Company', 'ok', '111');
INSERT INTO `account` VALUES ('houjp1992@gmail.com', 'bdaict', 0x2A39423339354639443736313846313434413344313630363145393733463242444633443837424236, '', '2016-08-09 17:09:56', '[B@2dfde1e8', '8ef93c4d-5493-4354-b7e7-1e1801c95da8', '', 'ICT_UCAS', 'Company', 'ok', '011');
INSERT INTO `account` VALUES ('junxu@ict.ac.cn', 'bdaict', 0x2A33374444444444323331373542444435374445344446384541413130323846393544323543453242, null, '2016-08-09 17:09:56', '6ea64477-1066-495a-bb2e-455bc71788e3', 'db7b8832-8f73-4fcf-ae6e-02351d1d1458', '2016-08-26 10:21:11', 'ICT_UCAS', 'Company', 'ok', '011');
INSERT INTO `account` VALUES ('admin', 'bdaict', 0x2A36424234383337454237343332393130354545343536384444413744433637454432434132414439, null, '2016-08-09 17:09:56', '[B@2dfde1e8', '8ef93c4d-5493-4354-b7e7-1e1801c95da8', '', 'ICT_UCAS', 'Company', 'ok', '011');
INSERT INTO `account` VALUES ('bdaict@hotmail.com', 'bdaict', 0x2A39423339354639443736313846313434413344313630363145393733463242444633443837424236, null, '2016-08-09 17:09:56', '3d65c11f-b7ea-492e-8318-c52115a27ebd', '8ef93c4d-5493-4354-b7e7-1e1801c95da8', '2016-05-29 23:20:29', 'ICT_UCAS', 'Company', 'ok', '111');
INSERT INTO `account` VALUES ('guest', 'bdaict', 0x2A36424234383337454237343332393130354545343536384444413744433637454432434132414439, null, '2016-08-09 17:09:56', '[B@2dfde1e8', '8ef93c4d-5493-4354-b7e7-1e1801c95da8', null, 'ICT_UCAS', 'Company', null, '011');
INSERT INTO `account` VALUES ('guotianyou@software.ict.ac.cn', 'bdaict', 0x2A39423339354639443736313846313434413344313630363145393733463242444633443837424236, null, '2016-08-09 17:09:56', '[B@2dfde1e8', '0e6aa472-ba0a-4920-9f8e-1e71556a4372', '2016-08-09 17:04:09', 'ICT_UCAS', 'Company', null, '011');
