/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.i18n;

import eml.studio.client.util.Constants;

public class Globalization {

	public static String getI18NString(String cateName){

		if( cateName.equals("预处理") ){
			return Constants.studioUIMsg.preprocess();
		}else if(cateName.equals("单机") ){
			return Constants.studioUIMsg.standalone();
		}else if(cateName.equals("分布式") ){
			return Constants.studioUIMsg.distributed();
		}else if(cateName.equals("结果评价") ){
			return Constants.studioUIMsg.evaluation();
		}else if(cateName.equals("格式转换") ){
			return Constants.studioUIMsg.transformation();
		}else if(cateName.equals("机器学习算法") ){
			return Constants.studioUIMsg.machineLearning();
		}else if(cateName.equals("深度学习") ){
			return Constants.studioUIMsg.deepLearning();
		}else if(cateName.equals("文本分析") ){
			return Constants.studioUIMsg.textAnalysis();
		}else if(cateName.equals("推荐算法") ){
			return Constants.studioUIMsg.recommendation();
		}else if(cateName.equals("图分析") ){
			return Constants.studioUIMsg.graphAnalysis();
		}else if(cateName.equals("分类回归") ){
			return Constants.studioUIMsg.classification();
		}else if(cateName.equals("聚类算法") ){
			return Constants.studioUIMsg.clusterAnalysis();
		}else if(cateName.equals("结构化分析") ){
			return Constants.studioUIMsg.structuralAnalysis();
		}
		return cateName;
	}
}
