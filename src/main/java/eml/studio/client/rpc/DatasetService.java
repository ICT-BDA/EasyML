/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import eml.studio.shared.model.Dataset;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("datasetservice")
public interface DatasetService extends RemoteService {

	/**
	 * Get the quantity of all Datasets
	 * 
	 * @return
	 * @throws Exception
	 */
	int getSize() throws Exception;

	/**
	 * Get part of Datasets from database
	 *
	 * @param start start index
	 * @param size size of list
	 * @return list of dataset
	 * @throws Exception
	 */
	List<Dataset> loadPart(int start, int size) throws Exception;

	/**
	 * Load Dataset from database
	 *
	 * @return list of dataset
	 * @throws Exception
	 */
	List<Dataset> load() throws Exception;

	/**
	 * Load a Dataset with given id
	 *
	 * @param id of target dataset
	 * @return target dataset
	 * @throws Exception
	 */
	Dataset load(String id) throws Exception;

	/**
	 * Load a file from hdfs
	 *
	 * @param path path of target dataset in HDFS
	 * @return target dataset
	 * @throws Exception
	 */
	Dataset loadFile(String path) throws Exception;

	/**
	 * Preview the front line of the file
	 * 
	 * @param src_uri  source url
	 * @param head  head line size
	 * @return
	 * @throws Exception
	 */
	String previewFile(String src_uri, int head) throws Exception;

	/**
	 * Save the intermediate generated data into the dataset
	 *
	 * @param dataset dataset to save
	 * @param src_uri the uri of target dataset in HDFS
	 * @throws Exception
	 */
	void save(Dataset dataset, String src_uri) throws Exception;

	/**
	 * Upload a new dataset
	 *
	 * @param  dataset target dataset
	 * @param  src_uri uri of target dataset in HDFS
	 * @return new dataset
	 * @throws Exception
	 */
	Dataset upload(Dataset dataset, String src_uri) throws Exception;

	/**
	 * Convert oldCate to newCate
	 * 
	 * @param oldCate
	 * @param newCate
	 * @return
	 * @throws Exception
	 */
	String editCategory(String oldCate, String newCate) throws Exception;

	/**
	 * Edit dataset
	 * 
	 * @param dataset target dataset after edited
	 * @throws Exception
	 */
	void edit(Dataset dataset) throws Exception;

	/**
	 * Delete a Dataset from database and HDFS
	 * 
	 * @param id
	 * @throws Exception
	 */
	void delete(String id) throws Exception;

	/**
	 * Deprecate a dataset module
	 * 
	 * @param id id of target dataset module
	 * @throws Exception
	 */
	void deprecate(String id) throws Exception;

	/**
	 * Download dataset from HDFS by id 
	 * 
	 * @param id
	 * @return dataset path on hdfs
	 * @throws Exception
	 */
	String download(String id) throws Exception;

	/**
	 * Upgrade a the old dataset
	 * 
	 * @param id
	 * @param newID
	 * @throws Exception
	 */
	void upgrade(String id, String newID) throws Exception;

	/**
	 * To determine whether it is a catalog
	 * 
	 * @param path file path
	 * @return
	 * @throws Exception
	 */
	boolean isDirectory(String path) throws Exception;

	/**
	 * Get a list of file paths in the directory
	 * 
	 * @param path  Directory of the path
	 * @return   File path list
	 * @throws Exception
	 */
	List<String> getDirFilesPath(String path) throws Exception;

	/**
	 * Determine if the file or directory exists on HDFS
	 * 
	 * @param path  File or directory address
	 * @return does it exist
	 * @throws Exception
	 */
	boolean isFileExist(String path) throws Exception;
	/**
	 * Get the size of the file or directory file
	 * 
	 * @param path File or directory address
	 * @return File size（kb）
	 * @throws Exception
	 */
	double getFileSize(String path) throws Exception;

	/**
	 * Search dataset
	 * 
	 * @param dataset 
	 * @param startDate 
	 * @param endDate
	 * @param limitStart  
	 * @param limitSize
	 * @return
	 * @throws Exception
	 */
	List<Dataset> search(Dataset dataset, String startDate, String endDate, int limitStart, int limitSize) throws Exception;

}
