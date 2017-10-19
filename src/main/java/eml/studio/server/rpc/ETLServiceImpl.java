/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import eml.studio.client.rpc.ETLService;
import eml.studio.server.db.HiveETL;
import eml.studio.server.db.SqlETL;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;

/**
 * Specific methods in ETL modules' related RemoteServiceServlet
 */
public class ETLServiceImpl extends RemoteServiceServlet implements ETLService {

	private SqlETL sqlETL = new SqlETL();

	private HiveETL hiveETL = new HiveETL();

	@Override
	public ArrayList<String> SqlETLGetTables(String url, String user, String passwd){
		return  sqlETL.getTables(url, user, passwd);
	}
	@Override
	public ArrayList<String> SqlETLGetColumns(String url, String user, String passwd, String table) {
		return  sqlETL.getColumns( url, user, passwd, table);
	}
	@Override
	public ArrayList<String> HiveETLGetTables(String url, String user, String passwd){
		return  hiveETL.getTables(url, user, passwd);
	}
	@Override
	public ArrayList<String> HiveETLGetColumns(String url, String user, String passwd, String table){

		return hiveETL.getColumns( url, user, passwd, table);
	}
}
