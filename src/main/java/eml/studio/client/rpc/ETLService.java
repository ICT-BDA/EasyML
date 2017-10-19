/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("etlservice")
public interface ETLService extends RemoteService{
	/**
	 * Get  tables from  database
	 * 
	 * @param url  database url
	 * @param user  database account
	 * @param passwd   database password
	 * @return
	 * @throws Exception
	 */
	ArrayList<String> SqlETLGetTables(String url, String user,
			String passwd) throws Exception;
	
	/**
	 * Get table columns from some database
	 * 
	 * @param url  database url 
	 * @param user  database account
	 * @param passwd  database password
	 * @param table  database table
	 * @return
	 * @throws Exception
	 */
	ArrayList<String> SqlETLGetColumns(String url, String user,
			String passwd, String table) throws Exception;
	
	/**
	 * Get tables from hive
	 * 
	 * @param url   hive url 
	 * @param user  hive account
	 * @param passwd  hive password
	 * @return
	 * @throws Exception
	 */
	ArrayList<String> HiveETLGetTables(String url, String user,
			String passwd) throws Exception;
	
	/**
	 * Get table columns from hive
	 * 
	 * @param url  hive url
	 * @param user  hive account
	 * @param passwd  hive password
	 * @param table  hive table
	 * @return
	 * @throws Exception
	 */
	ArrayList<String> HiveETLGetColumns(String url, String user,
			String passwd, String table) throws Exception;
}

