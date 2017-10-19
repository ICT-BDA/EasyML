/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import eml.studio.client.rpc.JobService;
import eml.studio.shared.model.BdaJob;

import org.apache.oozie.client.OozieClientException;

import eml.studio.server.db.SecureDao;
import eml.studio.server.graph.OozieGraphXMLParser;
import eml.studio.server.oozie.instance.OozieInstance;
import eml.studio.server.util.GenerateSequenceUtil;
import eml.studio.server.util.HDFSIO;
import eml.studio.server.util.OozieUtil;
import eml.studio.server.util.TimeUtils;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieProgramNode;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Specific methods in Oozie Jobs' related RemoteServiceServlet
 */
public class JobServiceImpl extends RemoteServiceServlet implements JobService {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(JobServiceImpl.class
			.getName());

	/**
	 * Get job's graphXML
	 * @param jobId target job id
	 * @return job graphXML string
	 */
	@Override
	public String getJobGraphXML(String jobId) {
		OozieJob job = new OozieJob();
		job.setId(jobId);
		try {
			OozieJob res = SecureDao.getObject(job);
			if (res != null) {
				return res.getGraphxml();
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Get BdaJob(all info) form mysql
	 * @param jobId target job id
	 * @return Bdajob object
	 */
	@Override
	public BdaJob getJob(String jobId) {

		try {
			BdaJob job = new BdaJob();
			job.setJobId(jobId);
			job = SecureDao.getObject(job);

			OozieJob entity = new OozieJob();
			entity.setId(job.getCurOozJobId());
			entity = SecureDao.getObject(entity);
			job.setCurOozJob(entity);

			logger.info("************* Parse Graph *****************");
			OozieGraph graph = OozieGraphXMLParser.parse(entity.getGraphxml());
			job.setOozieGraph(graph);
			logger.info(graph.toXML());
			logger.info("************* Parse Graph *****************");
			return job;
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
		}
		return null;
	}

	/**
	 * Get a job status
	 * @param jobId target job id
	 * @return status string
	 */
	@Override
	public String getJobStatus(String jobId) {
		try {
			OozieJob job = OozieUtil.getJob(jobId);
			return job.getStatus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * Kill a job
	 * @param jobId target job id
	 */
	@Override
	public void killJob(String jobId) {

		try {
			OozieUtil.kill(jobId);
		} catch (OozieClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Suspend a job
	 * @param jobId target job id
	 */
	@Override
	public void suspendJob(String jobId) {
		try {
			OozieUtil.suspend(jobId);
		} catch (OozieClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Resume a job
	 * @param jobId target job id
	 */
	@Override
	public void resumeJob(String jobId) {
		try {
			OozieUtil.resume(jobId);
		} catch (OozieClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Start a job
	 * @param jobId target job id
	 */
	@Override
	public void startJob(String jobId) {
		try {
			OozieUtil.start(jobId);
		} catch (OozieClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rerun a job
	 * @param jobId target job id
	 */
	@Override
	public void reRun(String jobId) {
		try {
			OozieUtil.reRun(jobId);
		} catch (OozieClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Return the stdout of a action node
	 *
	 * @param jobId target job id
	 * @param actionId target action in this job
	 * @return stdout string
	 */
	@Override
	public String getStdOut(String jobId, String actionId) {

		String url;
		try {
			url = OozieUtil.getUrl(jobId) + actionId;
			String url_out = url + "/stdout";
			return HDFSIO.cat(url_out);

		} catch (OozieClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return the stderr of a action node
	 *
	 * @param jobId target job id
	 * @param actionId target action in this job
	 * @return error string
	 */
	@Override
	public String getStdErr(String jobId, String actionId) {
		try {
			String url = OozieUtil.getUrl(jobId) + actionId;
			String url_err = url + "/stderr";
			return HDFSIO.cat(url_err);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * List recent jobs belong to an user
	 * @param email target user
	 * @return list of jobs
	 */
	@Override
	public List<BdaJob> listRecentUserJobs(String email) {
		BdaJob entity = new BdaJob();
		entity.setAccount(email);

		try {
			List<BdaJob> list = SecureDao.listAll(entity,
					"order by last_submit_time desc limit 100");

			for (BdaJob temp : list) {
				syncDatabase(temp);
			}
			logger.info("recent user job size: " + list.size());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * List recent exampleJobs
	 * @return the list of recent example jobs
	 */
	@Override
	public List<BdaJob> listRecentExampleJobs() {
		BdaJob entity = new BdaJob();
		entity.setIsExample(true);
		try {
			List<BdaJob> list = SecureDao.listAll(entity, "order by job_name");
			for (BdaJob temp : list) {
				syncDatabase(temp);
			}
			logger.info("recent example job size: " + list.size());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Add a job to Example job list
	 * @param jobId target jobid
	 */
	@Override
	public void setExampleJobs(String jobId) {
		BdaJob example = new BdaJob();
		example.setJobId(jobId);
		example.setIsExample(true);
		String[] setFields = {"is_example"};
		String[] condFields = {"job_id"};
		try {
			SecureDao.update(example, setFields, condFields);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * List recent jobs
	 * @return recent jobs list
	 */
	@Override
	public List<BdaJob> listRecentJobs() {

		try {
			List<BdaJob> jobs = SecureDao.listAll(new BdaJob(),
					"order by last_submit_time desc limit 100");

			for (BdaJob temp : jobs) {
				syncDatabase(temp);
			}
			logger.info("recent job size: " + jobs.size());
			return jobs;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Synchronize the status of the BdaJob in the EML and Oozie databases
	 * If the task status is non-SUCCEEDED, KILLED, FAILED,
	 * the state is obtained from the oozie database and the bda database
	 * will be updated and the job object information also will be updated
	 * 
	 * @param job
	 *            The incoming job object will be synchronized with
	 *            the oozie database after the method is executed successfully
	 */
	private void syncDatabase(BdaJob job) {

		try {
			OozieJob ooziejob = new OozieJob();
			ooziejob.setId(job.getCurOozJobId());
			ooziejob = SecureDao.getObject(ooziejob);

			job.setCurOozJob(ooziejob);

			if (ooziejob != null && ooziejob.getEndTime() == null) {
				OozieJob temp = OozieUtil.getJob(ooziejob.getId());
				ooziejob.setStatus(temp.getStatus());
				ooziejob.setEndTime(temp.getEndTime());
				ooziejob.setCreatedTime(temp.getCreatedTime());
				ooziejob.setActions(temp.getActions());
				String[] setFields = {"status", "endtime", "createtime"};
				String[] condFields = {"id"};
				SecureDao.update(ooziejob, setFields, condFields);

				// syc Action
				for (OozieAction action : temp.getActions()) {
					action.setJobId(ooziejob.getId());
					SecureDao.update(action, new String[]{"jobid",
							"ooziejobid", "name"});
				}

				for (OozieAction action : ooziejob.getActions()) {
					action.setAppPath(ooziejob.getAppPath());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Synchronize the status of the OozieJob in the EML and Oozie databases
	 * If the task status is non-SUCCEEDED, KILLED, FAILED,
	 * the state is obtained from the oozie database and the bda database
	 * will be updated and the job object information also will be updated
	 * 
	 * @param job
	 *            The incoming job object will be synchronized with
	 *            the oozie database after the method is executed successfully
	 */
	private void syncDatabase(OozieJob oozieJob) {

		try {
			// If not for the end state
			if (oozieJob != null && oozieJob.getEndTime() == null) {
				// Get status from oozie database
				OozieJob temp = OozieUtil.getJob(oozieJob.getId());
				// Synchronize object information
				oozieJob.setStatus(temp.getStatus());
				oozieJob.setEndTime(temp.getEndTime());
				oozieJob.setCreatedTime(temp.getCreatedTime());
				oozieJob.setActions(temp.getActions());
				String[] setFields = {"status", "endtime", "createtime"};
				String[] condFields = {"id"};
				// Synchronize database information
				SecureDao.update(oozieJob, setFields, condFields);

				// Synchronize action information
				for (OozieAction action : temp.getActions()) {
					action.setJobId(oozieJob.getId());
					SecureDao.update(action, new String[]{"jobid",
							"ooziejobid", "name"});
				}

				for (OozieAction action : oozieJob.getActions()) {
					action.setAppPath(oozieJob.getAppPath());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Update the status of a oozie job in database
	 *
	 * @param jobId jobid
	 */
	@Override
	public void updateJobStatus(String jobId) {
		try {
			OozieJob job = OozieUtil.getJob(jobId);
			String[] setFields = {"status", "endtime", "createtime"};
			String[] condFields = {"id"};
			SecureDao.update(job, setFields, condFields);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Delete bdajob and related ooziejob and actions via job id
	 * @param bdaJobId
	 */
	@Override
	public void deleteJob(String bdaJobId) {
		BdaJob bdaJob = new BdaJob();
		bdaJob.setJobId(bdaJobId);
		try {
			SecureDao.delete(bdaJob);
			OozieJob query = new OozieJob();
			query.setJobid(bdaJobId);
			List<OozieJob> oozJobs = SecureDao.listAll(query);
			for (OozieJob oozJob : oozJobs) {
				deleteOozieJob(oozJob.getId());
			}

			// delete related OozieAction records
			OozieAction actionQuery = new OozieAction();
			actionQuery.setBdaJobId(bdaJobId);
			SecureDao.delete(actionQuery);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Submit a bda job to Oozie
	 * @param bdaJobName
	 * @param bdaJobId
	 * @param graph
	 * @param account
	 * @param desc
	 * @return new EMLjob
	 */
	@Override
	public BdaJob submit(String bdaJobName, String bdaJobId, OozieGraph graph,
			String account, String desc) {
		try {
			BdaJob job = null;
			if (bdaJobId == null) {
				job = this.createBdaJob(bdaJobName, bdaJobId, graph, account,
						desc);
				bdaJobId = job.getJobId();
			} else {
				job = this.updateBdaJob(bdaJobName, bdaJobId, graph, account,
						desc);
			}

			OozieInstance instant = new OozieInstance(job);
			job = instant.exec();
			return job;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Inquire OozieJob info via bdajobid，include related OozieActions
	 * 
	 * @param bdaJobId target job id
	 * @return newest oozieJob
	 */
	@Override
	public BdaJob getBdaJob(String bdaJobId) {
		try {
			BdaJob job = new BdaJob();
			job.setJobId(bdaJobId);
			job = SecureDao.getObject(job);
			syncDatabase(job);

			logger.info("query actions from bda database");
			OozieAction queryAction = new OozieAction();
			queryAction.setBdaJobId(bdaJobId);
			List<OozieAction> actions = SecureDao.listAll(queryAction);
			job.setActions(actions);

			return job;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Synchronize a EML job with Oozie Job
	 *
	 * @param bdaJobId
	 * @return ooziejob object
	 */
	@Override
	public OozieJob synCurOozJob(String bdaJobId) {
		try {
			BdaJob job = new BdaJob();
			job.setJobId(bdaJobId);
			job = SecureDao.getObject(job);
			syncDatabase(job);

			return job.getCurOozJob();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public OozieAction getOozieAction(String oozieJobId, String actionName) {
		// TODO Auto-generated method stub
		try {
			OozieAction queryAction = new OozieAction();
			queryAction.setJobId(oozieJobId);
			queryAction.setName(actionName);
			List<OozieAction> actions = SecureDao.listAll(queryAction);
			return actions.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public OozieJob synCurOozJobByOozId(String oozieJobId) {
		try {
			OozieJob job = new OozieJob();
			job.setId(oozieJobId);
			job = SecureDao.getObject(job);
			// Synchronize with oozie database
			syncDatabase(job);

			return job;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a EML job in database
	 * @param bdaJobName
	 * @param bdaJobId
	 * @param graph
	 * @param account
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	private BdaJob createBdaJob(String bdaJobName, String bdaJobId,
			OozieGraph graph, String account, String desc) throws Exception {

		if (bdaJobName == null)
			bdaJobName = "Not Specified";
		if (bdaJobId == null)
			bdaJobId = GenerateSequenceUtil.generateSequenceNo() + "-bda";

		// Create the corresponding bda job in the database
		BdaJob job = new BdaJob();
		job.setJobName(bdaJobName);
		job.setJobId(bdaJobId);
		job.setGraphxml(graph.toXML());
		job.setAccount(account);
		job.setDesc(desc);
		job.setLastSubmitTime(TimeUtils.getTime());

		//Modify the programs in the graph xml of the bda job to latest
		OozieGraph tmpGraph = OozieGraphXMLParser.parse(graph.toXML());
		LinkedList<OozieProgramNode> proNodes =  tmpGraph.getProgramNodes();
		for(int i=0 ; i < proNodes.size() ; i++)
		{
			OozieProgramNode proNode = proNodes.get(i);
			proNode.setOozJobId("latest");
			proNode.setWorkPath("${appPath}/" + proNode.getId() + "/");
			proNodes.set(i, proNode	);
		}
		job.setGraphxml(tmpGraph.toXML());
		SecureDao.insert(job);

		job.setOozieGraph(graph);
		return job;
	}

	/**
	 * Update a EML job in the database
	 * @param bdaJobName
	 * @param bdaJobId
	 * @param graph
	 * @param account
	 * @param desc
	 * @return Bda job object
	 * @throws Exception
	 */
	private BdaJob updateBdaJob(String bdaJobName, String bdaJobId,
			OozieGraph graph, String account, String desc) throws Exception {

		if (bdaJobName == null)
			bdaJobName = "Not Specified";
		BdaJob job = new BdaJob();
		job.setJobName(bdaJobName);
		job.setJobId(bdaJobId);
		job.setGraphxml(graph.toXML());
		job.setAccount(account);
		job.setDesc(desc);
		job.setLastSubmitTime(TimeUtils.getTime());

		String[] cond = {"job_id"};
		String[] sets = {"job_name", "graphxml", "account", "description",
		"last_submit_time"};
		
		//Modify the programs in the graph xml of the bda job to latest
	    OozieGraph tmpGraph = OozieGraphXMLParser.parse(graph.toXML());
	    LinkedList<OozieProgramNode> proNodes =  tmpGraph.getProgramNodes();
	    for(int i=0 ; i < proNodes.size() ; i++)
	    {
	      OozieProgramNode proNode = proNodes.get(i);
	      proNode.setOozJobId("latest");
	      proNodes.set(i, proNode  );
	    }
	    job.setGraphxml(tmpGraph.toXML());
	    
		SecureDao.update(job, sets, cond);
		job.setOozieGraph(graph);
		return job;
	}

	@Override
	public OozieJob getOozieJob(String oozieJobId) {
		// TODO Auto-generated method stub
		try {
			OozieJob oozieJob = new OozieJob();
			oozieJob.setId(oozieJobId);
			oozieJob = SecureDao.getObject(oozieJob);

			logger.info("************* Parse Graph Begin *****************");
			OozieGraph graph = OozieGraphXMLParser.parse(oozieJob.getGraphxml());
			oozieJob.setGraph(graph);
			logger.info(graph.toXML());
			logger.info("************* Parse Graph End*****************");
			return oozieJob;
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
		}
		return null;
	}

	@Override
	public OozieJob getSynOozieJob(String oozieJobId) {
		// TODO Auto-generated method stub
		try {
			OozieJob oozieJob = new OozieJob();
			oozieJob.setId(oozieJobId);
			oozieJob = SecureDao.getObject(oozieJob);
			// Synchronize with oozie database
			syncDatabase(oozieJob);

			logger.info("query actions from bda database");
			OozieAction queryAction = new OozieAction();
			queryAction.setJobId(oozieJobId);
			List<OozieAction> actions = SecureDao.listAll(queryAction);
			oozieJob.setActions(actions);

			return oozieJob;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer getRefOozieJobNum(String bdaJobId, Date startTime, Date endTime,boolean firstLoad) {
		// TODO Auto-generated method stub
		OozieJob oozieJob = new OozieJob();
		oozieJob.setJobid(bdaJobId);
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(firstLoad)//If is the first paging statistics, synchronize status for all the job
			{
				List<OozieJob> totalJobs = SecureDao.listAll(oozieJob); 
				for(OozieJob job : totalJobs) //Only synchronize status for running job
				{
					if(job.getStatus().toLowerCase().equals("running"))
						updateJobStatus(job.getId());
				}
			}
			List<OozieJob> totalJobs = SecureDao.listAll(oozieJob,"and createtime > \""+formatter.format(startTime)+"\""+"  and endtime < \""+formatter.format(endTime)+"\"");
			Integer oozieJobNum =totalJobs.size();
			return oozieJobNum;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteOozieJob(String oozJobId) {
		OozieJob oozJob = new OozieJob();
		// Set the query condition to oozJobId
		oozJob.setId(oozJobId);

		try {
			// Delete HDFS directory
			OozieJob oozJobs_d = SecureDao.getObject(oozJob);
			String bdaJobId = oozJobs_d.getJobid();	
			HDFSIO.delete(oozJobs_d.getAppPath());
			// Delete by id
			SecureDao.delete(oozJob);

			//If the deleted ooziejob is the bdajob's current ooziejob, it should use current latest ooziejob to corresponding with the bdajob for replacement
			BdaJob bdaJob = new BdaJob();
			bdaJob.setJobId(bdaJobId);
			bdaJob = SecureDao.getObject(bdaJob);
			if(bdaJob!=null && bdaJob.getCurOozJobId().equals(oozJobId))
			{
				logger.info("Need to update bdajob: "+bdaJobId+" current ooziejob");
				oozJob = new OozieJob();
				oozJob.setJobid(bdaJobId);
				List<OozieJob> oozJobList = SecureDao.listAll(oozJob, "order by createtime desc");
				if(oozJobList!=null && oozJobList.size()>0)
				{
					bdaJob.setCurOozJobId(oozJobList.get(0).getId());
					SecureDao.update(bdaJob, "job_id");
					logger.info("Update bdajob: "+bdaJobId+" current ooziejob to "+oozJobList.get(0).getId());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public List<OozieJob> getRefOozieJobPage(String bdaJobId, int start,
			int size, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		OozieJob oozieJob = new OozieJob();
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		oozieJob.setJobid(bdaJobId);
		List<OozieJob> result = null;
		try {
			result =SecureDao.listAll(oozieJob, "and createtime > \""+formatter.format(startTime)+"\""+"  and endtime < \""+formatter.format(endTime)+"\""+" order by createtime desc limit " + start + ", " + size);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteBatchOozieJob(Set<String> oozJobIds) {
		// TODO Auto-generated method stub
		for(String jobId : oozJobIds)
		{
			deleteOozieJob(jobId);
		}
	}

	@Override
	public void updateJobActionStatus(String jobId) {
		// TODO Auto-generated method stub
		OozieJob job;
		try {
			job = OozieUtil.getJob(jobId);
			// Synchronize action information
			for (OozieAction action : job.getActions()) {
				action.setJobId(job.getId());
				SecureDao.update(action, new String[]{"jobid","ooziejobid", "name"});
			}
			for (OozieAction action : job.getActions()) {
				action.setAppPath(job.getAppPath());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
