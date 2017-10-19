/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.db;

import java.sql.*;
import java.util.ArrayList;

/**
 * Hive connection to get tables and columns
 * ++++++++++++++++++++++++++++++++++++++++++++++
 * +url:  jdbc:hive2://hive_ip:10000        ++
 * +user:  hive                                ++
 * +passwd: hive                               ++
 * ++++++++++++++++++++++++++++++++++++++++++++++
 */
public class HiveETL extends ETLDBService {
	// Dependent on two hive jar, hive-jdbc=1.2.1.jar and hadoop-common-2.2.0.2.0.6.0-102.jar.
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	private String errorString = "Database connection failed！";

	/**
	 * Get table names from a url ,user, passwd
	 * @param url jdbc:hive2://host:10000
	 * @param user  user
	 * @param passwd passwd
	 * @return Table lists.
	 * @throws IllegalArgumentException
	 */
	@Override
	public ArrayList<String> getTables(String url, String user, String passwd)
			throws IllegalArgumentException {
		ArrayList<String> tables = new ArrayList<String>();
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, user, passwd);
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet res = dbmd.getTables(null, null, null, new String[] {"TABLE"});
			while (res.next()) {
				tables.add(res.getString(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorString += e.getMessage();
			tables.add(errorString);
		}
		return tables;
	}

	/**
	 * Get table names from a url ,user, passwd
	 * @param url jdbc:hive2://host:10000
	 * @param user  user
	 * @param passwd passwd
	 * @param table  table Name
	 * @return Columns list
	 * @throws IllegalArgumentException
	 */
	@Override
	public ArrayList<String> getColumns(String url, String user, String passwd,
			String table) throws IllegalArgumentException {
		ArrayList<String> columns = new ArrayList<String>();
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, user, passwd);
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet res = dbmd.getColumns(null, null, table, null);
			while (res.next()) {
				columns.add(res.getString(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorString += e.getMessage();
			columns.add(errorString);
		}
		return columns;
	}
}
