/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.rpc.DatasetService;
import eml.studio.server.db.SecureDao;
import eml.studio.shared.model.Category;
import org.apache.hadoop.fs.Path;
import eml.studio.client.util.UUID;
import eml.studio.server.constant.Constants;
import eml.studio.server.util.HDFSIO;
import eml.studio.server.util.ModuleBuilder;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.ModuleVersion;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Specific methods in Dataset modules' related RemoteServiceServlet
 */
public class DatasetServiceImpl extends RemoteServiceServlet implements
DatasetService {

	private static final long serialVersionUID = 1L;
	private static final Logger logger =
			Logger.getLogger(DatasetServiceImpl.class.getName());

	/**
	 * Get the quantity of all Datasets
	 */
	@Override
	public int getSize() throws Exception {
		int size = 0;
		List<Dataset> datasets;
		try {
			Dataset query = new Dataset();
			query.setDeprecated( false );
			datasets = SecureDao.listAll(query, "order by category desc,name desc");
			size = datasets.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return size;
	}

	/**
	 * Get part of Datasets from database
	 *
	 * @param start start index
	 * @param size size of list
	 * @return list of dataset
	 */
	@Override
	public List<Dataset> loadPart(int start, int size) throws Exception{
		List<Dataset> datasets;
		try{
			Dataset query = new Dataset();
			query.setDeprecated( false );
			datasets = SecureDao.listAll( query, "order by category desc,name desc limit "+ start + ", " + size);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return datasets;
	}

	/**
	 * Load Dataset from database
	 *
	 * @return list of dataset
	 */
	@Override
	public List<Dataset> load() throws Exception {
		List<Dataset> datasets;
		try {
			Dataset query = new Dataset();
			query.setDeprecated( false );
			datasets = SecureDao.listAll(query,
					"order by category desc,name desc");
			logger.info("n(dataset)=" + datasets.size());
		} catch (Exception e) {
			logger.warning("load dataset failed!");
			e.printStackTrace();
			throw e;
		}

		return datasets;
	}

	/**
	 * Load a Dataset with given id
	 *
	 * @param id of target dataset
	 * @return target dataset
	 */
	@Override
	public Dataset load(String id) {
		Dataset dataset = new Dataset();
		dataset.setId(id);
		try {
			return SecureDao.getObject(dataset,
					" and id = '" + id + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Load a file from hdfs
	 *
	 * @param path path of target dataset in HDFS
	 * @return target dataset
	 */
	@Override
	public Dataset loadFile(String path) throws Exception {
		String uuid = path.replaceAll(".*/", "");
		logger.info("Dataset path:"+path+";uuid: " + uuid);
		Dataset data = ModuleBuilder.buildDataset(path, uuid);
		if (data == null)
		{
			logger.info("Default dataset path:"+Constants.DATASET_PATH);
			data = ModuleBuilder.buildDataset(Constants.DATASET_PATH , uuid);
		}
		return data;
	}

	/**
	 * Save the intermediate generated data into the dataset
	 *
	 * @param dataset dataset to save
	 * @param src_uri the uri of target dataset in HDFS
	 */
	@Override
	public void save(Dataset dataset, String src_uri) throws Exception {
		String uid = UUID.randomUUID();
		String path = Constants.DATASET_PATH + "/" + uid;
		String old_uid = src_uri.substring(src_uri.lastIndexOf("/") + 1);
		logger.info("[old_uid]" + old_uid);
		logger.info("[new_uid]" + uid);
		try {
			HDFSIO.copy(src_uri, path);
			HDFSIO.rename(path + "/" + old_uid, path + "/" + uid);
			;

			dataset.setId(uid);
			Dataset temp= SecureDao.getObject(dataset, "");
			if ( temp != null ) {
				System.out.println("[id exist]");
			} else {
				dataset.setPath(path);
				dataset.setDeprecated(false);
				SecureDao.insert(dataset);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Upload a new dataset
	 *
	 * @param  dataset target dataset
	 * @param  src_uri uri of target dataset in HDFS
	 * @return new dataset
	 */
	@Override
	public Dataset upload(Dataset dataset, String src_uri)
			throws Exception {

		String uuid = src_uri.replaceAll(".*/", "");
		String path = Constants.DATASET_PATH + "/" + uuid;
		String category = null;
		try {
			if(dataset.getCategory().contains(">")){
				Category query = new Category();
				query.setPath(dataset.getCategory());
				category = SecureDao.getObject(query).getId();
			}else if("my data".equals(dataset.getCategory().toLowerCase()) || "我的数据".equals(dataset.getCategory())){
				category = "0A0F402F-670F-4696-9D9C-42F0E0D665A22"; 
			}else if("shared data".equals(dataset.getCategory().toLowerCase()) || "共享数据".equals(dataset.getCategory())){
				category = "0A0F402F-670F-4696-9D9C-42F0E0D665A11"; 
			}else if("system data".equals(dataset.getCategory().toLowerCase()) || "系统数据".equals(dataset.getCategory())){
				category = "0A0F402F-670F-4696-9D9C-42F0E0D665A00"; 
			}else
				category = dataset.getCategory();

			dataset.setId(uuid);
			dataset.setPath(path);
			dataset.setCategory(category);
			dataset.setDeprecated(false);
			SecureDao.insert(dataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return dataset;
	}

	/**
	 * Convert oldCate to newCate
	 * @param oldCate
	 * @param newCate
	 * @return error code
	 * @throws Exception
	 */
	@Override
	public String editCategory(String oldCate, String newCate) throws Exception {
		Dataset query = new Dataset();
		query.setCategory(oldCate);
		List<Dataset> datasets = null;
		boolean msg = true;
		try{
			datasets = SecureDao.listAll(query, "");
			if ( datasets != null ) {
				for(Dataset dataset : datasets){
					Dataset update = new Dataset();
					update.setId(dataset.getId());
					update.setCategory(newCate);
					SecureDao.update(update, "id");
					if(SecureDao.getObject(update) != null){
						msg = msg & true;
					}else
						msg = msg & false;
				}
				if(msg){
					return "success";
				}else
					return "some update failed";
			}else
				return "no dataset";
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Edit dataset
	 * @param dataset target dataset after edited
	 * @throws Exception
	 */
	@Override
	public void edit(Dataset dataset) throws Exception {
	    if(  dataset.getId() == null || dataset.getId().trim().equals(""))
	    {
	      logger.info("Dataset id is null or empty,deprecate failed!");
	      return;
	    }
		String category = null;
		try {
			if(dataset.getCategory().contains(">")){
				Category query = new Category();
				query.setPath(dataset.getCategory());
				category = SecureDao.getObject(query).getId();
			}else if("my data".equals(dataset.getCategory().toLowerCase()) || "我的数据".equals(dataset.getCategory())){
				category = "0A0F402F-670F-4696-9D9C-42F0E0D665A22"; 
			}else if("shared data".equals(dataset.getCategory().toLowerCase()) || "共享数据".equals(dataset.getCategory())){
				category = "0A0F402F-670F-4696-9D9C-42F0E0D665A11"; 
			}else if("system data".equals(dataset.getCategory().toLowerCase()) || "系统数据".equals(dataset.getCategory())){
				category = "0A0F402F-670F-4696-9D9C-42F0E0D665A00"; 
			}else
				category = dataset.getCategory();

			dataset.setCategory(category);
			dataset.setDeprecated(false);
			SecureDao.update(dataset, "id");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Preview dataset
	 * @param src_uri the location uri of target dataset
	 * @param head start index
	 * @return string content of dataset
	 * @throws Exception
	 */
	@Override
	public String previewFile(String src_uri, int head) throws Exception {
		/**
		 * When the src_uri exists, the direct read file does not exist,
		 * in fact, access to the corresponding DuASET_PATH directory uuid file,
		 * which is the default uuid saved
		 */
		try {
			if (HDFSIO.exist(src_uri))
				return HDFSIO.head(src_uri, head);
			String uuid = src_uri.replaceAll(".*/", "");
			return HDFSIO
					.head(Constants.DATASET_PATH + "/" + uuid + "/" + uuid, head);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Deprecate a dataset module
	 * @param id id of target dataset module
	 */
	@Override
	public void deprecate(String id){
	    if(  id == null || id.trim().equals(""))
	    {
	      logger.info("Dataset id is null or empty,deprecate failed!");
	      return;
	    }
		Dataset dataset = new Dataset();
		dataset.setId(id);
		try{
			dataset = SecureDao.getObject(dataset, "");
			if ( dataset != null ) {
				dataset.setDeprecated(true);
				SecureDao.update(dataset, "id");
			} else {
				System.out.println("[ Not exist id] " + id);
			}
		}catch( Exception ex){
			ex.printStackTrace();
		}
	}

	/** Delete a Dataset from database and HDFS */
	@Override
	public void delete(String id){
		Dataset dataset = new Dataset();
		dataset.setId(id);
		try{
			SecureDao.delete(dataset);
			HDFSIO.delete(dataset.getPath());
		}catch( Exception ex){
			ex.printStackTrace();
		}
	}

	/** Download a dataset from HDFS */
	@Override
	public String download(String id) {
		Dataset dataset = new Dataset();
		dataset.setId(id);
		String filepath = null;
		try{
			dataset = SecureDao.getObject(dataset, "");

			if ( dataset != null ) {
				filepath = dataset.getPath();
			} else {
				logger.info("cannot find the data with id:" + id);
			}

			logger.info("download dataset from:" + filepath);

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return filepath;

	}

	/**
	 * Upgrade a the old dataset
	 *
	 * @param id
	 * @param new_id
	 */
	@Override
	public void upgrade(String id, String new_id) {
		Dataset dataset = new Dataset();
		dataset.setId(id);
		try{
			dataset = SecureDao.getObject(dataset, "");
			if ( dataset != null ) {

				dataset.setDeprecated(true);
				SecureDao.update(dataset, "id");
				ModuleVersion version = new ModuleVersion();
				version.setNewversionid(new_id);
				version.setOldversionid(id);
				SecureDao.insert(version);
			} else {
				System.out.println("[ Not exist id] " + id);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Get all files under a directory
	 *
	 * @param path  path of directory
	 * @return list of names of all files
	 * @throws Exception
	 */
	@Override
	public List<String> getDirFilesPath(String path) throws Exception {
		// TODO Auto-generated method stub
		Path[]  filePaths = HDFSIO.list(path);
		List<String> fileNames = new ArrayList<String>();
		for(Path p : filePaths)
		{
			fileNames.add(p.getName());
		}
		return  fileNames;
	}

	/**
	 * If a path is a directory
	 * @param path path
	 * @return true of fals
	 * @throws Exception
	 */
	@Override
	public boolean isDirectory(String path) throws Exception {
		// TODO Auto-generated method stub
		Path p = new Path(path);
		return HDFSIO.isDirectory(p);
	}

	/**
	 * If a file is exist in HDFS
	 * @param path  file path
	 * @return true or false
	 * @throws Exception
	 */
	@Override
	public boolean isFileExist(String path) throws Exception {
		// TODO Auto-generated method stub
		return  HDFSIO.exist(path);
	}

	/**
	 * Get file path in HDFS
	 * @param path file path in HDFS
	 * @return size of the file
	 * @throws Exception
	 */
	@Override
	public double getFileSize(String path) throws Exception {
		// TODO Auto-generated method stub
		return HDFSIO.getFileSize(path);
	}

	public List<Dataset> search(Dataset dataset, String startDate, String endDate, int limitStart, int limitSize) throws Exception {
		List<Dataset> datasets = null;
		String sql = "";
		try {
			Dataset query = new Dataset();
			query.setDeprecated( false );
			query.setCategory(dataset.getCategory());
			if(dataset.getName() != null ){
				sql = sql + "and name like '%" + dataset.getName() + "%' ";
			}
			if(dataset.getOwner() != null){
				sql = sql + "and owner like '%" + dataset.getOwner() + "%' ";
			}
			if(startDate == null){
				if(endDate == null){
					sql = sql + "order by name desc";
				}else{
					sql = sql + "and createdate < '" + endDate + "' order by name desc";
				}
			}else{
				if(endDate == null){
					sql = sql + "and createdate > '" + startDate + "' order by name desc";
				}else{
					sql = sql + "and createdate > '" + startDate + "' and createdate < '" + endDate + "' order by name desc";
				}
			}
			if(limitStart != 0 && limitSize != 0){
				sql = sql + " limit " + limitStart + "," + limitSize;
			}
			datasets = SecureDao.listAll( query, sql );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return datasets;
	}
}
