/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.Date;
import java.util.List;
import java.util.Set;

import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.model.BdaJob;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;

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

	void getOozieJob(String oozieJobId,AsyncCallback<OozieJob> callback);

	void  getSynOozieJob(String oozieJobId,AsyncCallback<OozieJob> callback);
	
    void  synCurOozJobByOozId(String oozieJobId,AsyncCallback<OozieJob> callback);
    
    void  getOozieAction(String oozieJobId, String actionName, AsyncCallback<OozieAction> callback);

	void getRefOozieJobNum(String bdaJobId,Date startTime,Date endTime, boolean firstLoad , AsyncCallback<Integer> callback);

	void getRefOozieJobPage(String bdaJobId,int start, int size, Date startTime, Date endTime, AsyncCallback<List<OozieJob>> callback);

	void deleteOozieJob(String oozJobId,AsyncCallback<Void> callback);

	void deleteBatchOozieJob(Set<String> oozJobIds,AsyncCallback<Void> callback);

	void updateJobActionStatus(String jobId, AsyncCallback<Void> callback);
}

