/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.util;

import java.util.Date;

/**
 * Generate UUID class
 */
public class UUID {
	private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
			.toCharArray();

	/**
	 * Generate random UUID
	 * @return ID String
	 */
	public static String randomUUID() {
		char[] uuid = new char[36];
		int r;
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';
		for (int i = 0; i < 36; i++)
			if (uuid[i] == 0) {
				r = (int) (Math.random() * 16);
				uuid[i] = CHARS[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
			}
		return new String(uuid);
	}
	/**
	 * Generate random ID
	 * @return ID String
	 */
	public static String randomID() {
		Date date = new Date();
		String id = Long.toHexString(date.getTime());
		int rand = (int) (Math.random() * 65536);

		String rand_str = Integer.toHexString(rand);
		while (rand_str.length() < 4) {
			rand_str = "0" + rand_str;
		}
		id = id + "-" + rand_str;
		return id;
	}
}
