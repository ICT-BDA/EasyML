/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.rpc;

import java.util.List;
import bda.studio.shared.model.Program;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProgramServiceAsync {

  void getSize(AsyncCallback<Integer> callback);
  
  void loadPart(int start, int size, AsyncCallback<List<Program>> callback);
  
  /** Load all available programs */
  void load(AsyncCallback<List<Program>> callback);

  /** Load a program given its id */
  void load(String id, AsyncCallback<Program> callback);
  
  void load(Program program, AsyncCallback<List<Program>> callback);

  void upload(Program program, String src_uri, AsyncCallback<Program> callback);

  void editCategory(String oldCate, String newCate, AsyncCallback<String> callback);
  
  void edit(Program program, AsyncCallback<Void> callback);

  void delete(String id, AsyncCallback<Void> callback);

  void deprecate(String id, AsyncCallback<Void> callback);

  void download(String id, AsyncCallback<String> callback);

  void upgrade(String id, String newID, AsyncCallback<Void> callback);

}
