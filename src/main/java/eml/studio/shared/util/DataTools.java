/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.util;

/**
 * Provide data process tool method
 *
 */
public class DataTools {
	/**
	 * Identify a string data is a real number
	 * 
	 * @param str  String data
	 * @return
	 */
	public static  boolean isNumeric(String str){ 
		try {
		    Double.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Identify a string data is a positive integer
	 * 
	 * @param str  String data
	 * @return
	 */
	public static   boolean isPosInteger(String str){ 
		try {
		    int data = Integer.valueOf(str);
		    if(data > 0)
			   return true;
		    else
		       return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Remove quotation marks for a string data( used for GWT JSONObject)
	 * @param value
	 */
	public static String getJSONValue(String value)
	{
		return value.replaceAll("\"", "");
	}
}
