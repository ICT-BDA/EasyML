/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;


public interface ETLServiceAsync {

	void SqlETLGetTables(String url, String user, String passwd,
			AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
	
	void SqlETLGetColumns(String url, String user, String passwd,
			String table,AsyncCallback<ArrayList<String>> callback)throws IllegalArgumentException;
	
	void HiveETLGetTables(String url, String user, String passwd,
			AsyncCallback<ArrayList<String>> callback)throws IllegalArgumentException;
	
	void HiveETLGetColumns(String url, String user, String passwd,
			String table,AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
}
