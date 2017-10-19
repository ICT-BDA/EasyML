/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.instance;

import java.util.UUID;

import eml.studio.server.constant.Constants;
import eml.studio.server.db.SecureDao;
import eml.studio.server.graph.OozieGraphXMLParser;
import eml.studio.server.oozie.workflow.WFBuilder;
import eml.studio.server.util.HDFSIO;
import eml.studio.server.util.OozieUtil;
import eml.studio.server.util.ProgramAbleRunShellGenerator;
import eml.studio.server.util.TimeUtils;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.model.BdaJob;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;
import eml.studio.shared.script.Script;

/**
 * BdaJob Oozie Job
 */
public class OozieInstance {

	public OozieInstance(BdaJob bdaJob){
		this.bdaJob = bdaJob;
	}

	public OozieInstance(String bdaJobId) throws Exception{
		BdaJob query = new BdaJob();
		query.setJobId(bdaJobId);
		bdaJob = SecureDao.getObject(query);
		bdaJob.setOozieGraph(OozieGraphXMLParser.parse(bdaJob.getGraphxml()));
	}
	public BdaJob exec() throws Exception {
		if( bdaJob == null ) return null;

		OozieGraph graph = bdaJob.getOozieGraph();
		WFBuilder wfBuilder = new WFBuilder();
		wfBuilder.buildFromOozieGraph(graph);
		String workflow = wfBuilder.asWFGraph().toWorkflow(bdaJob.getJobName());

		// Generate and create oozie job application path
		String app_path = Constants.APP_WORKSPACE + "/APP-PATH-"
				+ UUID.randomUUID().toString();

		HDFSIO.mkdirs(app_path);
		HDFSIO.upload(app_path + "/workflow.xml", workflow);

		// If is script or sql script program , it should upload the script to hdfs
		for (Script entry : graph.getScriptList()) {
			String path = entry.getPath().replace("${appPath}", app_path);
			ProgramAbleRunShellGenerator generator = new ProgramAbleRunShellGenerator();
			if (entry.getStartShellPath() != null) {
				String start_script = generator.generate(entry.getInputCount(),
						entry.getOutputCount());
				HDFSIO.upload(
						entry.getStartShellPath().replace("${appPath}",
								app_path), start_script);
			}
			HDFSIO.upload(path, entry.getValue());
		}

		// Submit the oozie job
		String oozJobId = OozieUtil.submit(app_path);

		// Set the current oozie job id
		bdaJob.setCurOozJobId(oozJobId);
		bdaJob.setLastSubmitTime(TimeUtils.getTime());

		String cond[] = {"job_id"};
		String setFields[] = {"oozie_job", "job_name", "account",
		"last_submit_time"};
		SecureDao.update(bdaJob, setFields, cond);

		// Every oozie job corresponds to multiple actions,  all the job actions information saved in bda database
		for (String actionName : graph.getActiveList()) {
			OozieAction action = new OozieAction();
			action.setBdaJobId(bdaJob.getJobId());
			action.setJobId(oozJobId);
			action.setName(actionName);
			action.setStatus("new");
			action.setAppPath(app_path);
			SecureDao.insert(action);
		}

		// All oozie jobs information are also saved in bda database 
		OozieJob oozJob = new OozieJob();
		oozJob.setAppName(bdaJob.getJobName());
		oozJob.setJobid(bdaJob.getJobId());
		oozJob.setId(oozJobId);
		oozJob.setAccount(bdaJob.getAccount());
		oozJob.setDescription(bdaJob.getDesc());
		oozJob.setAppPath(app_path);
		oozJob.setStatus("RUNNING");
		oozJob.setGraphxml(graph.toXML());
		SecureDao.insert(oozJob);

		bdaJob.setCurOozJob(oozJob);
		return bdaJob;
	}

	private BdaJob bdaJob;
}
