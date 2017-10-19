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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("jobservice")
public interface JobService extends RemoteService {

	/**
	 * Get job's graphXML
	 * 
	 * @param jobId target job id
	 * @return job graphXML string
	 */
	String getJobGraphXML(String jobId) ;

	/**
	 * Get a job status
	 * 
	 * @param jobId target job id
	 * @return status string
	 */
	String getJobStatus(String jobId) ;

	/**
	 * Kill a job
	 * 
	 * @param jobId target job id
	 */
	void killJob(String jobId) ;

	/**
	 * Suspend job from oozie by job id
	 * 
	 * @param jobId target job id
	 */
	void suspendJob(String jobId) ;

	/**
	 * Resume job from oozie by job id
	 * 
	 * @param jobId target job id
	 */
	void resumeJob(String jobId) ;

	/**
	 * Re run job from oozie by job id
	 * 
	 * @param jobId target job id
	 */
	void reRun(String jobId) ;

	/**
	 * Start job from oozie by job id
	 * 
	 * @param jobId target job id
	 */
	void startJob(String jobId) ;

	/**
	 * Get the error output of a action (program) in a job
	 * 
	 * @param jobId target job id
	 * @param actionId target action in this job
	 * @return error string
	 */
	String getStdErr(String jobId, String actionName) ;

	/**
	 * Get the std output of a action (program) in a job
	 * 
	 * @param jobId target job id
	 * @param actionId target action in this job
	 * @return stdout string
	 */
	String getStdOut(String jobId, String actionName) ;

	/**
	 * Update the status of a oozie job in database
	 * 
	 * @param jobId
	 */
	void updateJobStatus(String jobId) ;

	/**
	 * Delete bdajob and related ooziejob and actions via job id
	 * 
	 * @param jobId
	 */
	void deleteJob(String jobId) ;

	/**
	 * Get BdaJob(all info) form mysql
	 * 
	 * @param jobId target job id
	 * @return Bdajob object
	 */
	BdaJob getJob(String jobId) ;

	/**
	 * List recent jobs from database
	 * 
	 * @return recent jobs list
	 */
	List<BdaJob> listRecentJobs() ;

	/**
	 * List recent user jobs by user email(account)
	 * 
	 * @param email
	 * @return list of jobs
	 */
	List<BdaJob> listRecentUserJobs(String email) ;

	/**
	 * List recent user example jobs
	 * 
	 * @return the list of recent example jobs
	 */
	List<BdaJob> listRecentExampleJobs() ;

	/**
	 * Get bda job infomation from database by job id
	 * 
	 * @param jobId
	 * @return
	 */
	BdaJob getBdaJob(String jobId);

	/**
	 *  Get oozJob infomation for dynamic update of bda job
	 *  
	 * @param bdaJobId
	 * @return
	 */
	OozieJob synCurOozJob(String bdaJobId);

	/**
	 * Submit a bdajob 
	 * 
	 * @param jobName
	 * @param bdaJobId
	 * @param graph
	 * @param account
	 * @param desc
	 * @return
	 */
	BdaJob submit(String jobName, String bdaJobId, OozieGraph graph,
			String account, String desc);

	/**
	 * Set a job as example by job id
	 * 
	 * @param jobId
	 */
	void setExampleJobs(String jobId);

	/**
	 * Get oozie job by oozie job id
	 * 
	 * @param oozieJobId
	 * @return
	 */
	OozieJob getOozieJob(String oozieJobId);

	/**
	 * Get synchronize oozie job by oozie job id(include oozie actions)
	 * 
	 * @param oozieJobId
	 * @return
	 */
	OozieJob getSynOozieJob(String oozieJobId);

	/**
	 * Sychronize cyrrebt oozie job by oozie job id
	 * 
	 * @param oozieJobId  
	 * @return
	 */
	OozieJob synCurOozJobByOozId(String oozieJobId);

	/**
	 * Get oozie action by oozie job id and oozie action name
	 * 
	 * @param oozieJobId  oozie job id
	 * @param actionName oozie action name
	 * @return
	 */
	OozieAction getOozieAction(String oozieJobId, String actionName);

	/**
	 * Get bdajob associate ooziejob record size
	 * 
	 * @param bdaJobId  bdajob id 
	 * @param startTime   start time
	 * @param endTme  end time
	 * @param firstLoad  if is first load the page should sys the oozie database
	 * @return
	 */
	Integer getRefOozieJobNum(String bdaJobId,Date startTime,Date endTime,boolean firstLoad);

	/**
	 * Get bdajob associate ooziejob page size
	 * 
	 * @param bdaJobId
	 * @param start  startIndex
	 * @param size pageSize
	 * @return
	 */
	List<OozieJob> getRefOozieJobPage(String bdaJobId,int start, int size,Date startTime, Date endTime);
	
	/**
	 * Delete oozieJob by oozie job id
	 * 
	 * @param oozJobId
	 */
	public void deleteOozieJob(String oozJobId);

	/**
	 * Delete batch oozie jobs by job ids
	 * 
	 * @param oozJobIds
	 */
	public void deleteBatchOozieJob(Set<String> oozJobIds);

	/**
	 * Update oozie job action status 
	 * 
	 * @param jobId  oozie job id
	 */
	public void updateJobActionStatus(String jobId) ;
}
