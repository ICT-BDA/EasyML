/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;
import eml.studio.shared.model.Program;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("programservice")
public interface ProgramService extends RemoteService {

	/**
	 * Get the quantity of all Programs
	 *
	 * @return size of programs
	 * @throws Exception
	 */
	int getSize() throws Exception;

	/**
	 * Get part of Programs from database
	 *
	 * @param start start index
	 * @param size window size
	 * @return list of part of programs
	 * @throws Exception
	 */
	List<Program> loadPart(int start, int size) throws Exception;

	/**
	 * Load Programs from database
	 *
	 * @return list of programs
	 * @throws Exception
	 */
	List<Program> load() throws Exception;

	/**
	 * Load a program with given id
	 *
	 * @param id target program id
	 * @return type:Program, target program module
	 * @throws Exception
	 */
	Program load(String id) throws Exception;

	/**
	 * Load all programs
	 *
	 * @param program stochastic program
	 * @return type:List<Program>
	 * @throws Exception
	 */
	List<Program> load(Program program) throws Exception;

	/**
	 * Upload a new program to mysql
	 * 
	 * @param program new program
	 * @param uuid id of new item
	 * @return type:Program. new program
	 * @throws Exception
	 */
	Program upload(Program program, String src_uri) throws Exception;

	/**
	 * Convert oldcat to new cate
	 * 
	 * @param oldCate
	 * @param newCate
	 * @return
	 * @throws Exception
	 */
	String editCategory(String oldCate, String newCate) throws Exception;

	/**
	 * Edit program
	 * 
	 * @param program program to be edited
	 * @throws Exception
	 */
	void edit(Program program) throws Exception;

	/**
	 * Upgrade a program
	 * 
	 * @param id old program id
	 * @param new_id new program id
	 * @throws Exception
	 */
	void upgrade(String id, String newID) throws Exception;

	/**
	 * Deprecate a program
	 * 
	 * @param id target program id
	 * @throws Exception
	 */
	void deprecate(String id) throws Exception;

	/**
	 * Delete a Program from database and HDFS
	 * 
	 * @param id
	 * @throws Exception
	 */
	void delete(String id) throws Exception;

	/**
	 * Download a program from HDFS
	 * 
	 * @param id
	 * @return  program path on hdfs
	 * @throws Exception
	 */
	String download(String id) throws Exception;

	/**
	 * Search program 
	 * 
	 * @param program
	 * @param startDate
	 * @param endDate
	 * @param limitStart
	 * @param limitSize
	 * @return
	 * @throws Exception
	 */
	List<Program> search(Program program, String startDate, String endDate, int limitStart, int limitSize) throws Exception;

}
