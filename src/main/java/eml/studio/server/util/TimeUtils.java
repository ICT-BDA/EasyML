/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Time model, use to generate time or convert time format
 */
public class TimeUtils {

	private static SimpleDateFormat ft = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Date getTime() {
		Date date = new Date();
		return date;
	}

	/**
	 * Calculate the diff between input time and now time
	 * @param time input time
	 * @return long int time diff
	 */
	public static long timeDiff(String time) {
		try {
			Date old = ft.parse(time);
			Date now = new Date();

			return now.getTime() - old.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Convert a date type to string type
	 * @param date date
	 * @return string of date
	 */
	public static String format(Date date) {
		if( date == null ) return null;
		return ft.format(date);
	}

	/**
	 * Convert a string to date format
	 * @param string data string
	 * @return date
	 */
	public static Date parse(String string){

		if( string == null ) return null;
		string = string.trim();
		if( string.equals("unknown") || string.equals("")) return null;

		try {
			if( !"".equals( string )){
				SimpleDateFormat ft = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				return ft.parse(string);
			}else{
				System.out.println( string );
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			System.out.println( "Exception string:" + string );
			e.printStackTrace();
		}
		return null;
	}

}
