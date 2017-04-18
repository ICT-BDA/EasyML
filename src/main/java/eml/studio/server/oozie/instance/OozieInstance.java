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
import eml.studio.server.util.TimeUtils;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.model.BdaJob;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;

/**
 * BdaJob Oozie Job
 * @author Roger
 *
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
		// 生成和创建oozie任务对应app-path
		String app_path = Constants.APP_WORKSPACE + "/APP-PATH-"
				+ UUID.randomUUID().toString();

		HDFSIO.mkdirs(app_path);
		HDFSIO.upload(app_path + "/workflow.xml", workflow);
		// 上传脚本
		// 提交oozie任务
		String oozJobId = OozieUtil.submit(app_path);

		// 设置当前对应的oozie任务id
		bdaJob.setCurOozJobId(oozJobId);
		bdaJob.setLastSubmitTime(TimeUtils.getTime());

		String cond[] = {"job_id"};
		String setFields[] = {"oozie_job", "job_name", "account",
				"last_submit_time"};
		SecureDao.update(bdaJob, setFields, cond);

		// 每个oozie任务会对应多个action，bda数据库中会保存所有与任务相关的action信息
		for (String actionName : graph.getActiveList()) {
			OozieAction action = new OozieAction();
			action.setBdaJobId(bdaJob.getJobId());
			action.setJobId(oozJobId);
			action.setName(actionName);
			action.setStatus("new");
			action.setAppPath(app_path);
			SecureDao.insert(action);
		}

		// 同时bda数据库还会保留oozie数据库的任务副本
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
