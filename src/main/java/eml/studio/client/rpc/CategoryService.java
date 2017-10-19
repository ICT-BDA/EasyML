/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;

import eml.studio.shared.model.Category;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("categoryservice")
public interface CategoryService extends RemoteService {

	/**
	 * Get the quantity of all valid Accounts
	 * 
	 * @return
	 * @throws Exception
	 */
	int getSize() throws Exception;

	/**
	 * Get the category of Programs from database
	 *
	 * @return category group list
	 * @throws Exception
	 */
	List<Category> getCategory() throws Exception;

	/**
	 * Get the category of id from database
	 * 
	 * @param id category id
	 * @return category object
	 * @throws Exception
	 */
	Category getCategory(String id) throws Exception;

	/**
	 * Get the category list searched by start and size
	 * 
	 * @param start
	 * @param size
	 * @return category list
	 * @throws Exception
	 */
	List<Category> getCategory(int start, int size) throws Exception;

	/**
	 * Get one category's info from database
	 *
	 * @param category category
	 * @param str category string
	 */
	List<Category> getCategory(Category category, String str) throws Exception;

	/**
	 * Insert category to database
	 * 
	 * @param category
	 * @return success or error string
	 */
	String insertCategory(Category category);

	/**
	 * Delete a category
	 *
	 * @param category target category
	 * @return category
	 */
	Category deleteCategory(Category category);

	/**
	 * Search category
	 * 
	 * @param category
	 * @param limitStart
	 * @param limitSize
	 * @return
	 * @throws Exception
	 */
	List<Category> search(Category category, int limitStart, int limitSize) throws Exception;

}
