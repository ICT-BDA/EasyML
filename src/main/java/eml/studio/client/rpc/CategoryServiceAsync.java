/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;
import eml.studio.shared.model.Category;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CategoryServiceAsync {

  void getSize(AsyncCallback<Integer> callback);

  void getCategory(String id, AsyncCallback<Category> callback);
	
  void getCategory(AsyncCallback<List<Category>> callback);

  void getCategory(int start, int size, AsyncCallback<List<Category>> callback);
  
  void getCategory(Category category, String str, AsyncCallback<List<Category>> callback);

  void insertCategory(Category category, AsyncCallback<String> callback);
  
  void deleteCategory(Category category, AsyncCallback<Category> callback);
  
  void search(Category category, int limitStart, int limitSize, AsyncCallback<List<Category>> callback);
}
