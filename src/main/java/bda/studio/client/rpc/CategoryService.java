/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.rpc;

import java.util.List;
import bda.studio.shared.model.Category;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("categoryservice")
public interface CategoryService extends RemoteService {

  int getSize() throws Exception;
	
  List<Category> getCategory() throws Exception;

  Category getCategory(String id) throws Exception;

  List<Category> getCategory(int start, int size) throws Exception;

  List<Category> getCategory(Category category, String str) throws Exception;
  
  String insertCategory(Category category);
  
  Category deleteCategory(Category category);

}
