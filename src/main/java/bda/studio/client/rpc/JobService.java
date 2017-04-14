/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.rpc;

import java.util.List;

import bda.studio.shared.graph.OozieGraph;
import bda.studio.shared.model.BdaJob;
import bda.studio.shared.oozie.OozieJob;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("jobservice")
public interface JobService extends RemoteService {

  String getJobGraphXML(String jobId) ;

  String getJobStatus(String jobId) ;

  void killJob(String jobId) ;

  void suspendJob(String jobId) ;

  void resumeJob(String jobId) ;

  void reRun(String jobId) ;

  void startJob(String jobId) ;

  /** Get the error output of a action (program) in a job */
  String getStdErr(String jobId, String actionName) ;

  /** Get the std output of a action (program) in a job */
  String getStdOut(String jobId, String actionName) ;

  void updateJobStatus(String jobId) ;

  void deleteJob(String jobId) ;

  BdaJob getJob(String jobId) ;

  List<BdaJob> listRecentJobs() ;

  List<BdaJob> listRecentUserJobs(String email) ;

  List<BdaJob> listRecentExampleJobs() ;

  /** Get bda job infomation*/
  BdaJob getBdaJob(String jobId);
  
  /** Get oozJob infomation for dynamic update of bda job*/
  OozieJob synCurOozJob(String bdaJobId);
  
  BdaJob submit(String jobName, String bdaJobId, OozieGraph graph,
			String account, String desc);

  void setExampleJobs(String jobId);
}
