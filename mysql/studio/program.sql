/*
Navicat MySQL Data Transfer

Source Server         : BDA(烟台)
Source Server Version : 50717
Source Host           : 10.20.13.7:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-10-18 16:28:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for program
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
  `programable` tinyint(1) DEFAULT '0' COMMENT 'Ã¦ËœÂ¯Ã¥ÂÂ¦Ã¤Â¸ÂºÃ¥ÂÂ¯Ã§Â¼â€“Ã§Â¨â€¹Ã§Â®â€”Ã¥Â­Â',
  `sourcelink` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of program
-- ----------------------------
INSERT INTO `program` VALUES ('spark_sql', 'spark_sql', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/spark_sql', '0', 'distributed', '0', '', '1.1', '2017-03-31 10:57:00 AM', 'spark-submit --class bda.spark.runnable.preprocess.DataTransformRunner spark.jar --script_fp [\"script path\":string:default,\" \"] --input_kv [\"input table_name,file_path pair\":string:default,\" \"] --output_kv [\"output table_name,file_path pair\":string:default,\" \"] ', null, '2', '1', null);
INSERT INTO `program` VALUES ('BinaryClassification_Evaluate', 'BinaryClassification_Evaluate', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'peng_come_on@126.com', '/EML/Modules/BinaryClassification_Evaluate', '0', 'spark', '1', 'Spark版本的BinaryClassification Evaluation。', '0.4', '2016-01-25 04:48:39 PM', 'spark-submit --class bda.spark.runnable.evaluate.BinaryClassificationRunner spark.jar --predict_pt {in:Prediction:\"预测结果文件\"}', '<Model>\n	<Name>BinaryClassification_Evaluate</Name>\n	<Category>结果评价>分布式</Category>\n	<Type>Spark</Type>\n	<Deterministic>是</Deterministic>\n	<Version>0.4</Version>\n	<CreateDate>2016-01-25 04:48:39 PM</CreateDate>\n	<Owner>peng_come_on@126.com</Owner>\n	<Description>Spark版本的BinaryClassification Evaluation。</Description>\n	<CommandLine>spark-submit --class bda.spark.runnable.evaluate.BinaryClassificationRunner spark.jar --predict_pt {in:Prediction:预测结果文件}</CommandLine>\n</Model>', '2', '0', null);
INSERT INTO `program` VALUES ('Shell', 'Shell', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'fortianyou@qq.com', '/EML/Modules/Shell', '0', 'standalone', '0', '', '0.4', '2016-11-29 03:15:45 PM', 'sh', null, '2', '1', null);
INSERT INTO `program` VALUES ('SqlETL', 'SqlETL', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/SqlETL', '0', 'etl', '0', '', '1.1', '2017-08-09 02:11:46 PM', 'spark-submit --executor-memory \'5G\' --class \'sparksql.reader.DatabaseReader\' --conf \'spark.cores.max=10\' reader.jar --url [\"SqlURL\":String:default,\"jdbc:mysql://mysql:3306/test\"] --output_pt {out:Custom:\"output\"} --user [\"user\":String:default,\"root\"] --passwd [\"passwd\":String:default,\"111111\"] --table [\"table\":String:default,\"\"] --columns [\"columns\":String:default,\"\"] --format [\"format\":String:default,\"csv\"] ', null, '2', '0', '');
INSERT INTO `program` VALUES ('JsonToLabelPoint', 'JsonToLabelPoint', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/JsonToLabelPoint', '0', 'distributed', '0', 'JsonToLabelPoint', '0.2', '2017-08-09 02:12:59 PM', 'spark-submit  --class \"bda.spark.labelpoint.JsonToLabelPoint\"  --conf \"spark.cores.max=10\" bda-spark.jar --output_pt {out:Custom:\"output\"} --label_col [\"label_col\":String:default,\"label\"] --is_class [\"is_class\":String:default,\"true\"] --input_pt {in:Custom:\"input\"} ', null, '2', '0', '');
INSERT INTO `program` VALUES ('E8BB2E5C-D834-4749-B7F2-FCB72C85C27E', 'FormatTransform', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/FormatTransform', '0', 'distributed', '0', 'tranform a csv,tsv, parquet, json file into any of them.', '0.1', '2017-08-09 02:12:42 PM', 'spark-submit  --class sparksql.reader.FormatTransform spark-sql.jar --input_format [\"(输入格式)\":String:default,\"csv\"] --output_format [\"(输出格式)\":String:default,\"json\"] --input_pt {in:(Custom):\"(输出文件)\"} --output_pt {out:(Custom):\"(输出文件)\"} ', null, '2', '0', '');
INSERT INTO `program` VALUES ('File_split', 'File_split', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/File_split', '0', 'distributed', '0', '', '0.1', '2017-04-28 17:08:20', 'spark-submit --class bda.spark.runnable.preprocess.FileSplitRunner spark.jar --input_pt {in:Data:\"Input_file\"} --ratio [\"Cut_ratio\":Double:default,0.5] --output_pt1 {out:Data,HFile:\"Output_file_1(ratio)\"} --output_pt2 {out:Data,HFile:\"Output_fie(1-ratio)\"} ', null, '2', '0', null);
INSERT INTO `program` VALUES ('GBRT_Predict', 'GBRT_Predict', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/GBRT_Predict', '0', 'distributed', '0', '', '0.1', '2017-04-29 16:25:55', 'spark-submit --class bda.spark.runnable.tree.gbrt.Predict spark.jar --model_pt {in:ObjectFile:\"Model_file\"}  --test_pt {in:LabeledPoint:\"test_file\"}  --predict_pt {out:TextFile:\"Predict_file\"} ', null, '2', '0', null);
INSERT INTO `program` VALUES ('GBRT_Train', 'GBRT_Train', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/GBRT_Train', '0', 'distributed', '0', '', '0.1', '2017-04-29 16:27:42', 'spark-submit --class bda.spark.runnable.tree.gbrt.Train spark.jar --train_pt {in:LabeledPoint:\"Train_file\"} --model_pt {out:ObjectFile,HFile:\"Model_file\"} --cp_pt /EML/tmp --impurity [\"impurity\":String:default,\"Variance\"] --max_depth [\"Max_depth\":Int:default,10] --max_bins [\"Max_bins\":Int:default,32] --bin_samples [\"Bin_sample\":Int:default,10000] --min_node_size [\"Min_node_size\":Int:default,15] --min_info_gain [\"Min_info_gain\":Double:default,1e-6] --num_round [\"Round\":Int:default,10] --learn_rate [\"Learning_rate\":Double:default,0.02] ', null, '2', '0', null);
INSERT INTO `program` VALUES ('LibSVM2LabeledPoint', 'LibSVM2LabeledPoint', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/LibSVM2LabeledPoint', '0', 'distributed', '0', '', '0.1', '2017-04-29 16:28:19', 'spark-submit --class bda.spark.runnable.formatTransform.LibSVM2LabeledPoint spark.jar --input_pt {in:LibSVM:\"LibSVM_data_file\"} --output_pt {out:LabeledPoint,HFile:\"LabeledPoint_data_file\"} --is_class [\"Is_classfication\":Boolean:default,true] ', null, '2', '0', null);
INSERT INTO `program` VALUES ('LogisticRegression_Predict', 'LogisticRegression_Predict', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/LogisticRegression_Predict', '0', 'distributed', '0', '', '0.1', '2017-04-28 17:10:45', 'spark-submit --class bda.spark.runnable.logisticRegression.Predict spark.jar --model_pt {in:LogisticRegressionModel:\"model\"} --test_pt {in:LabeledPoint:\"test_file\"} --predict_pt {out:Prediction,HFile:\"predict_file\"} ', null, '2', '0', null);
INSERT INTO `program` VALUES ('LogisticRegression_Train', 'LogisticRegression_Train', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/LogisticRegression_Train', '0', 'distributed', '0', '', '0.1', '2017-04-28 17:15:13', 'spark-submit --class bda.spark.runnable.logisticRegression.Train spark.jar --train_pt {in:LabeledPoint:\"Train_file\"} --validate_pt {in:LabeledPoint:\"validate_file\"} --model_pt {out:LogisticRegressionModel,HFile:\"Model\"} --graphx [\"If_GraphX\":Boolean:default,false] --max_iter [\"max_iter\":Int:default,20] --reg [\"reg\":Double:default,0.01] --learn_rate [\"learn_rate\":Double:default,0.1] ', null, '2', '0', null);
INSERT INTO `program` VALUES ('RMSE', 'RMSE', '0A0F402F-670F-4696-9D9C-42F0E0D665A0', 'bdaict@hotmail.com', '/EML/Modules/RMSE', '0', 'distributed', '0', '', '0.1', '2017-04-29 16:28:55', 'spark-submit --class bda.spark.runnable.evaluate.RMSERunner spark.jar --predict_pt {in:Prediction:\"predict_file\"} ', null, '2', '0', null);
