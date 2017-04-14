/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.server.rpc;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.oozie.client.OozieClientException;

import bda.studio.client.rpc.JobService;
import bda.studio.server.db.SecureDao;
import bda.studio.server.graph.OozieGraphXMLParser;
import bda.studio.server.oozie.instance.OozieInstance;
import bda.studio.server.util.GenerateSequenceUtil;
import bda.studio.server.util.HDFSIO;
import bda.studio.server.util.OozieUtil;
import bda.studio.server.util.TimeUtils;
import bda.studio.shared.graph.OozieGraph;
import bda.studio.shared.model.BdaJob;
import bda.studio.shared.oozie.OozieAction;
import bda.studio.shared.oozie.OozieJob;

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
	 * Synchronize the status of the OozieJob in the BDA and Oozie databases
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
     * Delete an oozie job
     * @param oozJobId
     */
	private void deleteOozieJob(String oozJobId) {
		OozieJob oozJob = new OozieJob();
		oozJob.setJobid(oozJobId);
		try {
			SecureDao.delete(oozJob);
			HDFSIO.delete(oozJob.getAppPath());
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
     * @return new BDAjob
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
	 * Synchronize a BDA job with Oozie Job
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

	/**
	 * Create a BDA job in database
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

		BdaJob job = new BdaJob();
		job.setJobName(bdaJobName);
		job.setJobId(bdaJobId);
		job.setGraphxml(graph.toXML());
		job.setAccount(account);
		job.setDesc(desc);
		job.setLastSubmitTime(TimeUtils.getTime());
		SecureDao.insert(job);

		job.setOozieGraph(graph);
		return job;
	}

	/**
	 * Update a BDA job in the database
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
		SecureDao.update(job, sets, cond);
		job.setOozieGraph(graph);
		return job;
	}


}
