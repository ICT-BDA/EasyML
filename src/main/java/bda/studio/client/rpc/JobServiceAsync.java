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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JobServiceAsync {
  void getJobGraphXML(String jobId, AsyncCallback<String> callback);

  void getStdOut(String jobId, String actionName, AsyncCallback<String> callback);

  void getStdErr(String jobId, String actionName, AsyncCallback<String> callback);

  void getJobStatus(String jobId, AsyncCallback<String> callback);

  void killJob(String jobId, AsyncCallback<Void> callback);

  void reRun(String jobId, AsyncCallback<Void> callback);

  void suspendJob(String jobId, AsyncCallback<Void> callback);

  void resumeJob(String jobId, AsyncCallback<Void> callback);

  void startJob(String jobId, AsyncCallback<Void> callback);

  void updateJobStatus(String jobId, AsyncCallback<Void> callback);

  void deleteJob(String jobId, AsyncCallback<Void> callback);

  void getJob(String jobId, AsyncCallback<BdaJob> callback);

  void listRecentExampleJobs(AsyncCallback<List<BdaJob>> callback);

  void listRecentJobs(AsyncCallback<List<BdaJob>> callback);

  void listRecentUserJobs(String email, AsyncCallback<List<BdaJob>> callback);
  
  void getBdaJob(String jobId, AsyncCallback<BdaJob> callback);

  void synCurOozJob(String bdaJobId, AsyncCallback<OozieJob> callback);

  void setExampleJobs(String jobId, AsyncCallback<Void> callback);

  void submit(String jobName, String bdaJobId, OozieGraph graph, String account,
		String desc, AsyncCallback<BdaJob> callback);
}

