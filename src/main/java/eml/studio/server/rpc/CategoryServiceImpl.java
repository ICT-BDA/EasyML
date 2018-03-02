/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import eml.studio.client.rpc.CategoryService;
import eml.studio.client.util.Constants;
import eml.studio.server.db.SecureDao;
import eml.studio.shared.model.Category;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;

/**
 * Specific methods in Category modules' related RemoteServiceServlet
 */
public class CategoryServiceImpl extends RemoteServiceServlet implements CategoryService {
	private static final long serialVersionUID = 1L;
	/**
	 * Get the quantity of all valid Accounts
	 */
	@Override
	public int getSize() throws Exception {
		int size = 0;
		List<Category> categories;
		try {
			Category query = new Category();
			categories = SecureDao.listAll(query, "and level != 1");

			size = categories.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return size;
	}

	/**
	 * Get the category of Programs from database
	 *
	 * @return category group list
	 */
	@Override
	public List<Category> getCategory() throws Exception {
		List<Category> categories = null;  
		try{
			Category query = new Category();
			query.setLevel("1");
			categories = SecureDao.listAll( query, "order by level, type asc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return categories;
	}

	/**
	 * Get the category of id from database
	 * @param id category id
	 * @return category object
	 * @throws Exception
	 */
	@Override
	public Category getCategory(String id) throws Exception {
		Category category = null;
		try{
			Category query = new Category();
			query.setId(id);
			category = SecureDao.getObject(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return category;
	}

	/**
	 * Get the category list searched by start and size
	 * @param start
	 * @param size
	 * @return category list
	 * @throws Exception
	 */
	@Override
	public List<Category> getCategory(int start, int size) throws Exception{
		List<Category> categories = null;
		try{
			Category query = new Category();
			categories = SecureDao.listAll( query, "and level != 1 order by level, type limit "+ start + ", " + size);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return categories;
	}

	/**
	 * Get one category's info from database
	 *
	 * @param category category
	 * @param str category string
	 */
	@Override
	public List<Category> getCategory(Category category, String str) throws Exception {
		List<Category> categories;
		try{
			categories = SecureDao.listAll(category, str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return categories;
	}

	/**
	 * Insert category to database
	 * @param category
	 * @return success or error string
	 */
	@Override
	public String insertCategory(Category category){
		Category insert_query = null;
		Category update_query = null;
		try{
			Category tmp = new Category();				//Queries whether the directory with the same name exists under the same path
			tmp.setName(category.getName());
			tmp.setPath(category.getPath());
			if(SecureDao.getObject(tmp) != null){
				return  Constants.adminUIMsg.alert6() + tmp.getName() + Constants.adminUIMsg.alert7();
			}else{
				Category update = new Category();			//Update its parent node haschild field to true
				int index = category.getPath().lastIndexOf(">");
				String fathPath = category.getPath().substring(0, index);

				if("my program".equals(fathPath.toLowerCase())||"我的程序".equals(fathPath.toLowerCase())){
					update.setPath("My Program");
				}else if("shared program".equals(fathPath.toLowerCase())||"共享程序".equals(fathPath.toLowerCase())){
					update.setPath("Shared Program");
				}else if("system program".equals(fathPath.toLowerCase())||"系统程序".equals(fathPath.toLowerCase())){
					update.setPath("System Program");
				}else if("my data".equals(fathPath.toLowerCase())||"我的数据".equals(fathPath.toLowerCase())){
					update.setPath("My Data");
				}else if("shared data".equals(fathPath.toLowerCase())||"共享数据".equals(fathPath.toLowerCase())){
					update.setPath("Shared Data");
				}else if("system data".equals(fathPath.toLowerCase())||"系统数据".equals(fathPath.toLowerCase())){
					update.setPath("System Data");
				}else
					update.setPath(fathPath);

				update.setHaschild(true);
				String[] setFields = { "haschild"};
				String[] condFields = { "path" };
				SecureDao.update(update, setFields, condFields);
				update_query = SecureDao.getObject(update);
				//Haschild update success is the implementation of the insert operation will be added to the table insert table
				if(update_query != null){
					int level = Integer.parseInt(update_query.getLevel())+1;
					category.setLevel(Integer.toString(level));
					category.setFatherid(update_query.getId());
					SecureDao.insert(category);
					insert_query = SecureDao.getObject(category);
					if(insert_query != null){
						return "success";
					}else{
						Category back = new Category();
						back.setPath(category.getPath().substring(0, index));
						back.setHaschild(false);
						SecureDao.update(update, setFields, condFields);
						return "insert failed";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Delete a category
	 *
	 * @param category target category
	 * @return category
	 */
	@Override
	public Category deleteCategory(Category category) {
		Category query = new Category();
		query.setId(category.getId());
		try{
			SecureDao.delete(query);
			if(SecureDao.getObject(query) == null){
				return null;
			}else
				return SecureDao.getObject(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Category> search(Category category, int limitStart, int limitSize) throws Exception {
		List<Category> categories = null;
		String sql = "";
		try {
			Category query = new Category();
			if(category.getName() != null ){
				sql = sql + "and name like '%" + category.getName() + "%' ";
			}
			if(category.getPath() != null){
				sql = sql + "and path like '%" + category.getPath() + "%' ";
			}
			sql = sql + "order by level, type";
			if(limitStart != 0 && limitSize != 0){
				sql = sql + " limit " + limitStart + "," + limitSize;
			}
			categories = SecureDao.listAll( query, sql );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return categories;
	}
}
