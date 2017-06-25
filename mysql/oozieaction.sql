/*
Navicat MySQL Data Transfer

Source Server         : 10.20.13.7
Source Server Version : 50717
Source Host           : 10.20.13.7:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-05-01 22:47:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oozieaction
-- ----------------------------
DROP TABLE IF EXISTS `oozieaction`;
CREATE TABLE `oozieaction` (
  `jobid` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `cred` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `retries` int(11) DEFAULT '0',
  `userretrycount` int(11) DEFAULT '0',
  `userretrymax` int(11) DEFAULT '0',
  `userretryinterval` int(11) DEFAULT '0',
  `starttime` varchar(255) DEFAULT NULL,
  `endtime` varchar(255) DEFAULT NULL,
  `transition` varchar(255) DEFAULT NULL,
  `stats` varchar(255) DEFAULT NULL,
  `externalchildids` varchar(255) DEFAULT NULL,
  `externalid` varchar(255) DEFAULT NULL,
  `externalstatus` varchar(255) DEFAULT NULL,
  `trackeruri` varchar(255) DEFAULT NULL,
  `consoleurl` varchar(255) DEFAULT NULL,
  `errorcode` varchar(255) DEFAULT NULL,
  `errormessage` varchar(255) DEFAULT NULL,
  `apppath` varchar(255) DEFAULT NULL,
  `ooziejobid` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oozieaction
-- ----------------------------
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'RMSE-15bc2b909a8-346a', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:11:26', '2017-05-01 08:12:04', 'end', null, null, 'job_1493624196048_0009', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0009/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'File_split-15bc2b97594-306b', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:08:48', '2017-05-01 08:09:24', 'File_split-15bc2b9d556-f370', null, null, 'job_1493624196048_0004', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0004/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'GBRT_Train-15bc2b99d06-b3a7', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:10:00', '2017-05-01 08:10:45', 'fork-E3EC0509-121A-4290-8851-DC01695DFBE2', null, null, 'job_1493624196048_0006', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0006/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'GBRT_Predict-15bc2b9c369-f641', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:10:46', '2017-05-01 08:11:26', 'join-E3EC0509-121A-4290-8851-DC01695DFBE2', null, null, 'job_1493624196048_0008', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0008/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'GBRT_Predict-15bc2b9b546-d790', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:10:45', '2017-05-01 08:11:20', 'join-E3EC0509-121A-4290-8851-DC01695DFBE2', null, null, 'job_1493624196048_0007', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0007/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'File_split-15bc2b9d556-f370', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:09:24', '2017-05-01 08:10:00', 'GBRT_Train-15bc2b99d06-b3a7', null, null, 'job_1493624196048_0005', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0005/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
INSERT INTO `oozieaction` VALUES ('05010808093800000-bda', 'LibSVM2LabeledPoint-15bc2b9f8ca-3180', null, 'shell', 'OK', '0', '0', '0', '0', '2017-05-01 08:08:11', '2017-05-01 08:08:48', 'File_split-15bc2b97594-306b', null, null, 'job_1493624196048_0003', 'SUCCEEDED', 'hadoop-master:8032', 'http://hadoop-master:8088/proxy/application_1493624196048_0003/', null, null, '/EML/oozie/APP-PATH-bd13e472-381b-4fd1-9e29-42a36dc11a45', '0000001-170501074411311-oozie-root-W');
