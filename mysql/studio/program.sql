/*
Navicat MySQL Data Transfer

Source Server Version : 50717
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-29 10:36:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `program`
-- ----------------------------
DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
  `id` varchar(100) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `deprecated` tinyint(1) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `isdeterministic` tinyint(1) DEFAULT '0',
  `description` varchar(5000) DEFAULT NULL,
  `version` varchar(30) DEFAULT NULL,
  `createdate` varchar(100) DEFAULT NULL,
  `commandline` varchar(5000) DEFAULT NULL,
  `describXML` varchar(5000) DEFAULT NULL,
  `scriptversion` varchar(30) DEFAULT NULL,
  `programable` tinyint(1) DEFAULT '0' COMMENT 'ÃƒÆ’Ã‚Â¦Ãƒâ€¹Ã…â€œÃƒâ€šÃ‚Â¯ÃƒÆ’Ã‚Â¥Ãƒâ€šÃ‚ÂÃƒâ€šÃ‚Â¦ÃƒÆ’Ã‚Â¤Ãƒâ€šÃ‚Â¸Ãƒâ€šÃ‚ÂºÃƒÆ’Ã‚Â¥Ãƒâ€šÃ‚ÂÃƒâ€šÃ‚Â¯ÃƒÆ’Ã‚Â§Ãƒâ€šÃ‚Â¼ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Å“ÃƒÆ’Ã‚Â§Ãƒâ€šÃ‚Â¨ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¹ÃƒÆ’Ã‚Â§Ãƒâ€šÃ‚Â®ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬ÂÃƒÆ’Ã‚Â¥Ãƒâ€šÃ‚Â­Ãƒâ€šÃ‚Â',
  `sourcelink` varchar(500) DEFAULT NULL,
  `tensorflow_mode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of program
-- ----------------------------
INSERT INTO `program` VALUES ('183F3557-6A63-4465-AA94-CDAE0164DE36', 'GBRT_Predict', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/183F3557-6A63-4465-AA94-CDAE0164DE36', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:31:01', 'spark-submit --class bda.spark.runnable.tree.gbrt.Predict spark.jar --model_pt {in:ObjectFile:\"Model_file\"}  --test_pt {in:LabeledPoint:\"test_file\"}  --predict_pt {out:TextFile:\"Predict_file\"}  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('2585825A-D47B-4147-ACC7-4D453AE2A9AB', 'FormatTransform', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/2585825A-D47B-4147-ACC7-4D453AE2A9AB', '0', 'distributed', '0', '', '0.1', '2018-03-28 06:56:21 PM', 'spark-submit  --class sparksql.reader.FormatTransform spark-sql.jar --input_format [\"(Input Format)\":String:default,\"json\"] --output_format [\"(Output Format)\":String:default,\"csv\"] --input_pt {in:(Custom):\"(Output File)\"} --output_pt {out:(Custom):\"(Output File)\"}  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('2827D732-6741-40CE-940E-E74E58B6BE36', 'cnn_model_parallel', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/2827D732-6741-40CE-940E-E74E58B6BE36', '0', 'tensorflow', '0', '', '0.1', '2018-03-28 18:46:50', 'python cnn.py {in:General:\"input\"} {out:Directory:\"output\"}    ', null, '2', '0', null, 'model distributed');
INSERT INTO `program` VALUES ('35538B3C-727C-403A-B667-CE1BF98B22BB', 'Shell', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/35538B3C-727C-403A-B667-CE1BF98B22BB', '0', 'standalone', '0', '', '0.1', '2018-03-28 06:38:02 PM', 'sh ', null, '2', '1', null, null);
INSERT INTO `program` VALUES ('396A769E-A1B5-4A9D-B4E1-27AE5DB5303B', 'RMSE', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/396A769E-A1B5-4A9D-B4E1-27AE5DB5303B', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:03:03', 'spark-submit --class bda.spark.runnable.evaluate.RMSERunner spark.jar --predict_pt {in:Prediction:\"predict_file\"}  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('4B26643D-EFF3-4E4F-8E24-7EEDA7FF077E', 'spark_sql', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/4B26643D-EFF3-4E4F-8E24-7EEDA7FF077E', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:00:30', 'spark-submit --class bda.spark.runnable.preprocess.DataTransformRunner spark.jar --script_fp [\"script path\":string:default,\" \"] --input_kv [\"input table_name,file_path pair\":string:default,\" \"] --output_kv [\"output table_name,file_path pair\":string:default,\" \"]  ', null, '2', '1', null, null);
INSERT INTO `program` VALUES ('681D9137-BD17-467F-ABBD-8053BC0B08F6', 'GBRT_Train', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/681D9137-BD17-467F-ABBD-8053BC0B08F6', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:29:59', 'spark-submit --class bda.spark.runnable.tree.gbrt.Train spark.jar --train_pt {in:LabeledPoint:\"Train_file\"} --model_pt {out:ObjectFile,HFile:\"Model_file\"} --cp_pt /EML/tmp --impurity [\"impurity\":String:default,\"Variance\"] --max_depth [\"Max_depth\":Int:default,10] --max_bins [\"Max_bins\":Int:default,32] --bin_samples [\"Bin_sample\":Int:default,10000] --min_node_size [\"Min_node_size\":Int:default,15] --min_info_gain [\"Min_info_gain\":Double:default,1e-6] --num_round [\"Round\":Int:default,10] --learn_rate [\"Learning_rate\":Double:default,0.02]  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('C95E3E3A-03F2-4D43-B5E0-25F926CA71A2', 'cnn_data_parallel', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/C95E3E3A-03F2-4D43-B5E0-25F926CA71A2', '0', 'tensorflow', '0', '', '0.1', '2018-03-28 18:47:42', 'python dist.py {in:General:\"input\"} {out:Directory:\"output\"}   ', null, '2', '0', null, 'data distributed');
INSERT INTO `program` VALUES ('CEF4304E-13A5-4AEB-9E27-9A55A44D4657', 'SqlETL', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/CEF4304E-13A5-4AEB-9E27-9A55A44D4657', '0', 'etl', '0', '', '0.1', '2018-03-28 17:49:10', 'spark-submit --executor-memory \'5G\' --class \'sparksql.reader.DatabaseReader\' --conf \'spark.cores.max=10\' reader.jar --url [\"SqlURL\":String:default,\"jdbc:mysql://mysql:3306/test\"] --output_pt {out:Custom:\"output\"} --user [\"user\":String:default,\"root\"] --passwd [\"passwd\":String:default,\"111111\"] --table [\"table\":String:default,\"\"] --columns [\"columns\":String:default,\"\"] --format [\"format\":String:default,\"csv\"]  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('D3C8BB0F-0421-4F74-8F59-B8F4CD0D5A91', 'cnn_single_predict', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/D3C8BB0F-0421-4F74-8F59-B8F4CD0D5A91', '0', 'tensorflow', '0', '', '0.1', '2018-03-28 18:45:35', 'python cnn.py {in:General:\"data\"} {in:General:\"model\"} {out:Directory:\"output\"}   ', null, '2', '0', null, 'standalone');
INSERT INTO `program` VALUES ('D536F4DD-57EF-4BA7-A3C0-D6C45BD5FCD0', 'File_Split', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/D536F4DD-57EF-4BA7-A3C0-D6C45BD5FCD0', '0', 'distributed', '0', '', '0.1', '2018-03-28 06:37:39 PM', 'spark-submit --class bda.spark.runnable.preprocess.FileSplitRunner spark.jar --input_pt {in:Data:\"Input_file\"} --ratio [\"Cut_ratio\":Double:default,0.5] --output_pt1 {out:Data,HFile:\"Output_file_1(ratio)\"} --output_pt2 {out:Data,HFile:\"Output_fie(1-ratio)\"}  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('E0CFBCFC-2FE8-40F1-B147-8A238DB3B576', 'LogisticRegression_Predict', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/E0CFBCFC-2FE8-40F1-B147-8A238DB3B576', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:20:41', 'spark-submit --class bda.spark.runnable.logisticRegression.Predict spark.jar --model_pt {in:LogisticRegressionModel:\"model\"} --test_pt {in:LabeledPoint:\"test_file\"} --predict_pt {out:Prediction,HFile:\"predict_file\"}  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('E9CDC384-D2FA-45F6-8FE4-7A2BDBFFED15', 'BinaryClassification_Evaluate', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/E9CDC384-D2FA-45F6-8FE4-7A2BDBFFED15', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:35:40', 'spark-submit --class bda.spark.runnable.evaluate.BinaryClassificationRunner spark.jar --predict_pt {in:Prediction:\"Prediction File\"} ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('EA7078B1-57AF-4DB0-9CB9-F97DF8C8FDE3', 'JsonToLabelPoint', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/EA7078B1-57AF-4DB0-9CB9-F97DF8C8FDE3', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:22:42', 'spark-submit  --class \"bda.spark.labelpoint.JsonToLabelPoint\"  --conf \"spark.cores.max=10\" bda-spark.jar --output_pt {out:Custom:\"output\"} --label_col [\"label_col\":String:default,\"label\"] --is_class [\"is_class\":String:default,\"true\"] --input_pt {in:Custom:\"input\"}  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('ED1539FF-F132-4E97-849E-A75D0DE09166', 'File_Split', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/ED1539FF-F132-4E97-849E-A75D0DE09166', '0', 'standalone', '0', '', '0.1', '2018-03-28 18:41:33', 'java -cp local.jar bda.local.runnable.preprocess.FileSplitRunner --input_pt {in:Data:\"Input File\"} --ratio [\"Cut Ratio\":Double:default,0.5] --output_pt1 {out:Data,HFile:\"Output File1(ratio)\"} --output_pt2 {out:Data,HFile:\"Output File2(1-ratio)\"} ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('F109E2AD-934B-4A68-8E4F-A4BD351E65D2', 'LogisticRegression_Train', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/F109E2AD-934B-4A68-8E4F-A4BD351E65D2', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:19:37', 'spark-submit --class bda.spark.runnable.logisticRegression.Train spark.jar --train_pt {in:LabeledPoint:\"Train_file\"} --validate_pt {in:LabeledPoint:\"validate_file\"} --model_pt {out:LogisticRegressionModel,HFile:\"Model\"} --graphx [\"If_GraphX\":Boolean:default,false] --max_iter [\"max_iter\":Int:default,20] --reg [\"reg\":Double:default,0.01] --learn_rate [\"learn_rate\":Double:default,0.1]  ', null, '2', '0', null, null);
INSERT INTO `program` VALUES ('FD5248DA-7979-42F5-A959-CEC2F6C1F9FC', 'LibSVM2LabeledPoint', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/FD5248DA-7979-42F5-A959-CEC2F6C1F9FC', '0', 'distributed', '0', '', '0.1', '2018-03-28 18:21:47', 'spark-submit --class bda.spark.runnable.formatTransform.LibSVM2LabeledPoint spark.jar --input_pt {in:LibSVM:\"LibSVM_data_file\"} --output_pt {out:LabeledPoint,HFile:\"LabeledPoint_data_file\"} --is_class [\"Is_classfication\":Boolean:default,true]  ', null, '2', '0', null, null);
