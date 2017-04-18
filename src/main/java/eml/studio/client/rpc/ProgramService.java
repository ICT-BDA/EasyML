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

  int getSize() throws Exception;
  
  List<Program> loadPart(int start, int size) throws Exception;
  
  /** Load all available programs */
  List<Program> load() throws Exception;

  /** Load a program given its id */  
  Program load(String id) throws Exception;

  List<Program> load(Program program) throws Exception;
  
  Program upload(Program program, String src_uri) throws Exception;

  String editCategory(String oldCate, String newCate) throws Exception;
  
  void edit(Program program) throws Exception;

  void upgrade(String id, String newID) throws Exception;

  void deprecate(String id) throws Exception;

  void delete(String id) throws Exception;

  String download(String id) throws Exception;

}
