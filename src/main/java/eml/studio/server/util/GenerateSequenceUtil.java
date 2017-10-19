/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class GenerateSequenceUtil {

	/** .log */
	private static final Logger logger = Logger.getLogger(GenerateSequenceUtil.class.getName());

	/** The FieldPosition. */
	private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

	/** This Format for format the data to special format. */
	private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS");

	/** This Format for format the number to special format. */
	private final static NumberFormat numberFormat = new DecimalFormat("0000");

	/** This int is the sequence number ,the default value is 0. */
	private static int seq = 0;

	private static final int MAX = 9999;

	/**
	 * Time format generation sequence
	 * @return String
	 */
	public static synchronized String generateSequenceNo() {

		Calendar rightNow = Calendar.getInstance();

		StringBuffer sb = new StringBuffer();

		dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);

		numberFormat.format(seq, sb, HELPER_POSITION);

		if (seq == MAX) {
			seq = 0;
		} else {
			seq++;
		}

		logger.info("THE SQUENCE IS :" + sb.toString());

		return sb.toString();
	}
}