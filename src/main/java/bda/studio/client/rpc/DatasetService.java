/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.rpc;

import bda.studio.shared.model.Dataset;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("datasetservice")
public interface DatasetService extends RemoteService {

  int getSize() throws Exception;
		
  List<Dataset> loadPart(int start, int size) throws Exception;
	
  List<Dataset> load() throws Exception;

  Dataset load(String id) throws Exception;

  /** Load the data module */
  Dataset loadFile(String path) throws Exception;

  /** Preview the front line of the file */
  String previewFile(String src_uri, int head) throws Exception;

  /** Save the temporarily generated file copy to the data module database */
  void save(Dataset dataset, String src_uri) throws Exception;

  Dataset upload(Dataset dataset, String src_uri) throws Exception;

  String editCategory(String oldCate, String newCate) throws Exception;
  
  void edit(Dataset dataset) throws Exception;

  void delete(String id) throws Exception;

  void deprecate(String id) throws Exception;

  String download(String id) throws Exception;

  void upgrade(String id, String newID) throws Exception;
  
  /**
   * To determine whether it is a catalog
   * @param path file path
   * @return
   * @throws Exception
   */
  boolean isDirectory(String path) throws Exception;
  
  /**
   * Get a list of file paths in the directory
   * @param path  Directory of the path
   * @return   File path list
   * @throws Exception
   */
  List<String> getDirFilesPath(String path) throws Exception;
  
  /**
   * Determine if the file or directory exists on HDFS
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

}
