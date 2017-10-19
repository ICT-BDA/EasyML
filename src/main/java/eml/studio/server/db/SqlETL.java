/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.db;

import java.sql.*;
import java.util.ArrayList;

/**
 * Mysql connection to get tables and columns
 * ++++++++++++++++++++++++++++++++++++++++++++++
 * +url:  jdbc:mysql://mysql_ip:mysql_port/database_name ++
 * +user:  user name                              
 * +passwd:  user password                         
 * ++++++++++++++++++++++++++++++++++++++++++++++
 */
public class SqlETL extends ETLDBService {

	/**
	 * construct JDBC connection.
	 */
	String errorMessage = "";
	private Connection getConnection(String url, String user, String passwd) {
		errorMessage = "Database connection failed !";
		Connection con = null;
		try {
			// Dependent on a mysql jar mysql-connector-java-5.1.37-bin.jar
			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println("can not found class com.mysql.jdbc.Driver!");
			e.printStackTrace();
			errorMessage += e.getMessage();
			return null;
		}
		try {
			con = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException se) {
			System.out.println("Database connection failed！"+se.getMessage());
			errorMessage += se.getMessage();
			se.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage += e.getMessage();
			return null;
		}
		return con;
	}

	/**
	 * Close connection.
	 * @param con
	 */
	private void closeConnect(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	/**
	 * Get table names from a url ,user, passwd
	 * @param url jdbc:mysql://host:3306/database
	 * @param user  user
	 * @param passwd passwd
	 * @return Table lists.
	 * @throws IllegalArgumentException
	 */
	@Override
	public ArrayList<String> getTables(String url, String user, String passwd) throws IllegalArgumentException {
		Connection con = getConnection(url, user, passwd);
		DatabaseMetaData dbmd = null;
		ArrayList<String> tables = new ArrayList<String>();
		if(con == null){
			tables.add(errorMessage);
			return  tables;
		}
		try {
			dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, null, new String[] {"TABLE"});
			while (rs.next()) {
				tables.add(rs.getString(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnect(con);
		}
		return tables;
	}

	/**
	 * Get column names from a url ,user, passwd and table.
	 * @param url jdbc:mysql://host:3306/database
	 * @param user  user
	 * @param passwd passwd
	 * @param table  table Name
	 * @return Columns list
	 * @throws IllegalArgumentException
	 */
	@Override
	public ArrayList<String> getColumns(String url, String user, String passwd, String table)
			throws IllegalArgumentException {
		Connection con = getConnection(url, user, passwd);
		ArrayList<String> columns = new ArrayList<String>();
		try {
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getColumns(null, null, table, null);
			while (rs.next()) {
				columns.add(rs.getString(4));
			}
			return columns;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnect(con);
		}
		return columns;
	}
}
