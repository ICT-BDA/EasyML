/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.util;

import eml.studio.shared.oozie.OozieAction;

/**
 * ProgramUtil methods in order to distinguish the elements in program modules
 */
public class ProgramUtil {

	/**
	 * @return if is distributed
	 */
	public static boolean isDistributed(String type){
		if( "分布式".equals(type) || "distributed".equals(type)
				|| "spark".equals(type)
				|| "mapreduce".equals(type) ) return true;
		return false;
	}
	public static boolean isETL(String type){
		if( "ETL".equals(type)||"etl".equals(type)) return true;
		return false;
	}
	public static boolean isStandalone(String type){
		if( "单机".equals(type)
				|| "standalone".equals(type)
				|| "Standalone".equals(type)
				|| "STANDALONE".equals(type) ) return true;
		return false;
	}

	public static boolean isTensorflow(String type){
		if("Tensorflow".equals(type) || "tensorflow".equals(type)) return true;
		return false;
	}

	public static boolean isSuccess(OozieAction action){
		if( action == null ||
				action.getAppPath() == null ||
				"".equals(action.getAppPath() )) return false;
		return "OK".equals(action.getStatus()) || "SUCCESS".equals(action.getStatus());
	}



	public static boolean isOkState(OozieAction action){
		if( "OK".equals(action.getStatus()) ) return true;
		return false;
	}

	public static boolean isErrorState(OozieAction action){
		if( "ERROR".equals(action.getStatus())
				|| "FAILED".equals(action.getStatus())
				|| "KILLED".equals(action.getStatus()) )
			return true;
		return false;
	}

	public static boolean isNewState(OozieAction action){
		if( "new".equals(action.getStatus()) ) return true;
		return false;
	}
}
