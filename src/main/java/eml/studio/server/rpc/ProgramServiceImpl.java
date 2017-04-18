/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import eml.studio.client.rpc.ProgramService;
import eml.studio.server.constant.Constants;
import eml.studio.server.db.SecureDao;
import eml.studio.server.util.DistributedRunShellGenerator;
import eml.studio.server.util.HDFSIO;
import eml.studio.server.util.RunShellGenerator;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.ModuleVersion;
import eml.studio.shared.model.Program;
import eml.studio.shared.util.ProgramUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import java.util.List;

/**
 * Specific methods in Program modules' related RemoteServiceServlet
 */
public class ProgramServiceImpl extends RemoteServiceServlet implements ProgramService {

  private static final Logger logger = Logger.getLogger(ProgramServiceImpl.class.getName());

  /**
   * Get the quantity of all Programs
   *
   * @return size of programs
   */
  @Override
  public int getSize() throws Exception {
	  int size = 0;
	  List<Program> programs;
	  try {
		  Program query = new Program();
	      query.setDeprecated( false );
	      programs = SecureDao.listAll( query, "order by name desc");
		  size = programs.size();
	  } catch (Exception e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	      throw e;
	  }
	  return size;
  }
  
  /**
   * Get part of Programs from database
   *
   * @param start start index
   * @param size window size
   * @return list of part of programs
   */
  @Override
  public List<Program> loadPart(int start, int size) throws Exception{
	  List<Program> programs;
	  try{
		  Program query = new Program();
		  query.setDeprecated( false );
		  logger.info("prog start:"+start);
		  logger.info("prog size:"+size);
		  programs = SecureDao.listAll( query, "order by name desc limit "+ start + ", "+size);
	  } catch (Exception e) {
	      e.printStackTrace();
	      throw e;
	  }
	  return programs;
  }
  
  /**
   * Load Programs from database
   *
   * @return list of programs
   */
  @Override
    public List<Program> load() throws Exception {
        List<Program> programs = null;
        try {
            Program query = new Program();
            query.setDeprecated( false );
            programs = SecureDao.listAll( query, "order by name desc");
            logger.info("the first record of programs:"+programs.get(0).getName());
        } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }

    return programs;
  }

  /**
   * Load a program with given id
   *
   * @param id target program id
   * @return type:Program, target program module
   */
  @Override
    public Program load(String id){
        Program program = new Program();
        program.setId(id);
        try {
            return SecureDao.getObject(program,
            " and id = '" + id + "'");
        } catch (Exception e) {
        e.printStackTrace();
        }
        return null;
  }

    /**
     * Load all programs
     *
     * @param program stochastic program
     * @return type:List<Program>
     */
  @Override
    public List<Program> load(Program program){
	  List<Program> programs = null;
	  try{
		 programs = SecureDao.listAll(program);
	  } catch (Exception e){
		  e.printStackTrace();
	  }
	  return programs;
  }

    /**
     * Upload a new program to mysql
     * @param program new program
     * @param uuid id of new item
     * @return type:Program. new program
     */
  @Override
    public Program upload(Program program, String uuid){
      String path = Constants.MODULE_PATH + "/" + uuid;
      String name = program.getName();
      String category = null;
      try {
	    if(program.getCategory().contains(">")){
	      Category query = new Category();
	      query.setPath(program.getCategory());
          category = SecureDao.getObject(query).getId();
        }else if("my program".equals(program.getCategory().toLowerCase()) || "我的程序".equals(program.getCategory())){
    	  category = "0A0F402F-670F-4696-9D9C-42F0E0D665A2";
        }else if("shared program".equals(program.getCategory().toLowerCase()) || "共享程序".equals(program.getCategory())){
    	  category = "0A0F402F-670F-4696-9D9C-42F0E0D665A1";
        }else if("system program".equals(program.getCategory().toLowerCase()) || "系统程序".equals(program.getCategory())){
    	  category = "0A0F402F-670F-4696-9D9C-42F0E0D665A0";
        }else
    	  category = program.getCategory();
	  
        RunShellGenerator gen = null;
        if (ProgramUtil.isDistributed( program.getType().toLowerCase() )||
                ProgramUtil.isETL( program.getType().toLowerCase() )) {
          gen = new DistributedRunShellGenerator();
        } else {
          gen = new RunShellGenerator();
        }
        String run = gen.generate(program.getCommandline());

        if (HDFSIO.exist(path + "/run.sh"))
          HDFSIO.delete(path + "/run.sh");
        HDFSIO.upload(path + "/run.sh", run);
        program.setId(uuid);
        program.setName(name);
        program.setCategory(category);
        program.setPath(path);
        program.setDeprecated(false);
        program.setScriptversion("2");
        SecureDao.insert(program);

      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return program;
    }

    /**
     * Convert oldcat to new cate
     *
     * @param oldCate
     * @param newCate
     * @return errcode
     * @throws Exception
     */
  @Override
    public String editCategory(String oldCate, String newCate) throws Exception {
	  Program query = new Program();
	  query.setCategory(oldCate);
	  List<Program> programs = null;
	  boolean msg = true;
	  try{
		  programs = SecureDao.listAll(query, "");
		  if ( programs != null ) {
			  for(Program program : programs){
				  Program update = new Program();
				  update.setId(program.getId());
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
			  return "no program";
	  } catch (Exception e){
		  e.printStackTrace();
	  }
	  return null;
  }

    /**
     * Edit program
     * @param program program to be edited
     * @throws Exception
     */
  @Override
    public void edit(Program program) throws Exception {
      String path = program.getPath();
      String name = program.getName();
      String category = null;
      try {
  	    if(program.getCategory().contains(">")){
  	      Category query = new Category();
  	      query.setPath(program.getCategory());
  	      category = SecureDao.getObject(query).getId();
  	    }else if("my program".equals(program.getCategory().toLowerCase()) || "我的程序".equals(program.getCategory())){
      	  category = "0A0F402F-670F-4696-9D9C-42F0E0D665A2";
  	    }else if("shared program".equals(program.getCategory().toLowerCase()) || "共享程序".equals(program.getCategory())){
      	  category = "0A0F402F-670F-4696-9D9C-42F0E0D665A1";
  	    }else if("system program".equals(program.getCategory().toLowerCase()) || "系统程序".equals(program.getCategory())){
      	  category = "0A0F402F-670F-4696-9D9C-42F0E0D665A0";
  	    }else
  		  category = program.getCategory();
    	
        RunShellGenerator gen = null;
        if ( ProgramUtil.isDistributed(program.getType().toLowerCase())||
            ProgramUtil.isETL( program.getType().toLowerCase() )) {
          gen = new DistributedRunShellGenerator();
          System.out.println("woqu ");
        } else {
          gen = new RunShellGenerator();
        }

        if( (program.getProgramable() && !program.isDistributed()) ){
        }else{
          String run = gen.generate(
            program.getCommandline());
        
          if (HDFSIO.exist(path + "/run.sh"))
            HDFSIO.delete(path + "/run.sh");
          HDFSIO.upload(path + "/run.sh", run);
        }
        program.setName(name);
        program.setCategory(category);
        program.setDeprecated(false);
        SecureDao.update(program, "id");
      } catch (Exception e) {
        e.printStackTrace();
        throw new Exception(e.getMessage());
      }
    }

    /**
     * Deprecate a programm
     * @param id target program id
     * @throws Exception
     */
  @Override
    public void deprecate(String id) throws Exception {
      Program program = new Program(id);
      program = SecureDao.getObject(program, "");

      if ( program != null ) {
        program.setDeprecated(true);
        SecureDao.update(program, "id");
        ModuleVersion version = new ModuleVersion();
        version.setNewversionid(null);
        version.setOldversionid(id);
        SecureDao.insert(version);
      } else {
        System.out.println("[ not exist: id] " + id);
      }
  }

  /**
   * Delete a Program from database and HDFS
   *
   * @param id target program id
   */
  @Override
    public void delete(String id){
      try{
        Program program = new Program(id);
        SecureDao.delete(program);
        HDFSIO.delete(program.getPath());
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }

  /** Download a program from HDFS
   *
   * @param id target programm id
   */
  @Override
    public String download(String id){
      Program program = new Program();
      program.setId(id);
      String filepath = null;
      try{
        program = SecureDao.getObject(program, "");
        if ( program != null ) {
          Path[] path = HDFSIO.list(program.getPath());
          for (Path p : path) {
            if (p.toString().endsWith(".zip")) {
              filepath = p.toString().split(id)[1];
              filepath = filepath.substring(1);
              filepath = program.getPath() + "/" + filepath;
            }
          }
        } else {
          logger.error("cannot find the data with id:" + id);
        }
      }catch(Exception ex ){
        ex.printStackTrace();
      }
      logger.info("download program from:" + filepath);
      return filepath;
    }

    /**
     * Upgrade a program
     * @param id old program id
     * @param new_id new program id
     */
  @Override
  public void upgrade(String id, String new_id){
      Program program = new Program();
      program.setId(id);
      try{
        program = SecureDao.getObject(program, "");
        if (program != null) {
            program.setDeprecated(true);
          SecureDao.update(program, "id");
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



}
