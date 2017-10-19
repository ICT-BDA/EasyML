/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import java.util.Date;
import java.util.List;

import eml.studio.server.anotation.TableField;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * BdaJob object class in project
 */
public class BdaJob implements IsSerializable {
	@TableField(name = "job_id")
	private String jobId;
	@TableField(name = "job_name")
	private String jobName;
	@TableField(name = "oozie_job")
	private String curOozJobId;
	@TableField(name = "account")
	private String account;
	@TableField(name = "is_example")
	private Boolean isExample;
	@TableField(name = "last_submit_time")
	private Date lastSubmitTime;
	@TableField(name = "description")
	private String desc;
	@TableField(name="graphxml")
	private String graphxml;

	private OozieJob curOozJob;
	private OozieGraph graph;

	private List<OozieAction> actions;
	public String getCurOozJobId() {
		return curOozJobId;
	}

	public void setCurOozJobId(String lastOozieJobId) {
		this.curOozJobId = lastOozieJobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobid) {
		this.jobId = jobid;
	}

	public OozieJob getCurOozJob() {
		return curOozJob;
	}

	public void setCurOozJob(OozieJob lastjob) {
		this.curOozJob = lastjob;
		if( this.curOozJob != null ) {
			this.curOozJobId = this.curOozJob.getId();
		}
		else {
			this.curOozJobId = null;
		}
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Boolean getIsExample() {
		return isExample;
	}

	public void setIsExample(Boolean isExample) {
		this.isExample = isExample;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<OozieAction> getActions() {
		return actions;
	}

	public void setActions(List<OozieAction> actions) {
		this.actions = actions;
	}

	public Date getLastSubmitTime() {
		return lastSubmitTime;
	}

	public void setLastSubmitTime(Date lastSubmitTime) {
		this.lastSubmitTime = lastSubmitTime;
	}

	public void setOozieGraph(OozieGraph graph) {
		this.graph = graph;
	}

	public OozieGraph getOozieGraph() {
		return this.graph;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getGraphxml() {
		return graphxml;
	}

	public void setGraphxml(String graphxml) {
		this.graphxml = graphxml;
	}

}
