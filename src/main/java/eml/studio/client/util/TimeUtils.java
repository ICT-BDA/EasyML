/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Time format tool
 */
public class TimeUtils {

	private static Logger logger = Logger.getLogger(TimeUtils.class.getName());
	private static DateTimeFormat   dateFormat=DateTimeFormat
			.getFormat("yyyy-MM-dd HH:mm:ss");
	public static String timeDiff(Date date1, Date date2) {
		if( date1 == null || date2 == null ) return "";
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		try {
			long diff = date2.getTime() - date1.getTime();

			// gap of hour
			long hour = diff / nh;
			// gap of min
			long min = diff % nh / nm;
			// gap of sec
			long sec = diff % nh % nm / ns;
			String res = NumberFormat.getFormat("#00").format(hour) + ":"
					+ NumberFormat.getFormat("#00").format(min) + ":"
					+ NumberFormat.getFormat("#00").format(sec);
			return res;
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	/**
	 * Format the time now
	 * @return
	 */
	public static String timeNow(){
		Date dateNow = new Date();
		String dateNowStr=dateFormat.format(dateNow);
		return dateNowStr;
	}

	/**
	 * Convert Date to time string
	 * @param date target date
	 * @return time string
	 */
	public static String format(Date date){
		if( date == null ) return "";
		return dateFormat.format(date);
	}
}
