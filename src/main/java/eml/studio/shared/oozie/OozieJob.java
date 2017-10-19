/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.oozie;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import eml.studio.server.anotation.TableField;
import eml.studio.shared.graph.OozieGraph;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A OozieJob object contains the detail information, such as actions and url, of a ooize job.
 */
public class OozieJob implements IsSerializable {

	private List<OozieAction> actions;

	@TableField
	private String account;
	@TableField
	private String description;
	@TableField
	private Boolean isexample;
	@TableField
	private String graphxml;

	@TableField(name="name")
	private String AppName;
	@TableField(name="path")
	private String AppPath;

	@TableField(name="createtime")
	private Date CreatedTime;
	@TableField(name="endtime")
	private Date EndTime;

	@TableField(name="id")
	private String Id;
	@TableField(name="status")
	private String Status;

	@TableField(name="jobid")
	private String jobid;

	private OozieGraph graph;
	private String User;
	private String ExternalId;
	private String Group;
	private Date LastModifiedTime;
	private String ParentId;
	private Integer Run;
	private Date StartTime;
	private String Acl;
	private String Conf;
	private String ConsoleUrl;


	public void addAction(OozieAction oozieAction) {
		if( this.actions == null ){
			this.actions = new LinkedList<OozieAction>();
		}
		this.actions.add(oozieAction);
	}

	public String getAcl() {
		return Acl;
	}

	public void setAcl(String Acl) {
		this.Acl = Acl;
	}

	public List<OozieAction> getActions() {
		return actions;
	}

	public void setActions(List<OozieAction> Action) {
		this.actions = Action;
	}

	public String getAppName() {
		return AppName;
	}

	public void setAppName(String AppName) {
		this.AppName = AppName;
	}

	public String getAppPath() {
		return AppPath;
	}

	public void setAppPath(String AppPath) {
		this.AppPath = AppPath;
	}

	public String getConf() {
		return Conf;
	}

	public void setConf(String Conf) {
		this.Conf = Conf;
	}

	public String getConsoleUrl() {
		return ConsoleUrl;
	}

	public void setConsoleUrl(String ConsoleUrl) {
		this.ConsoleUrl = ConsoleUrl;
	}

	public Date getCreatedTime() {
		return CreatedTime;
	}

	public void setCreatedTime(Date CreatedTime) {
		this.CreatedTime = CreatedTime;
	}

	public Date getEndTime() {
		return EndTime;
	}

	public void setEndTime(Date EndTime) {
		this.EndTime = EndTime;
	}

	public String getExternalId() {
		return ExternalId;
	}

	public void setExternalId(String ExternalId) {
		this.ExternalId = ExternalId;
	}

	public String getGroup() {
		return Group;
	}

	public void setGroup(String Group) {
		this.Group = Group;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public Date getLastModifiedTime() {
		return LastModifiedTime;
	}

	public void setLastModifiedTime(Date LastModifiedTime) {
		this.LastModifiedTime = LastModifiedTime;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String ParentId) {
		this.ParentId = ParentId;
	}

	public Integer getRun() {
		return Run;
	}

	public void setRun(Integer Run) {
		this.Run = Run;
	}

	public Date getStartTime() {
		return StartTime;
	}

	public void setStartTime(Date StartTime) {
		this.StartTime = StartTime;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		this.Status = status;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String User) {
		this.User = User;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsexample() {
		return isexample;
	}

	public void setIsexample(Boolean isexample) {
		this.isexample = isexample;
	}

	public String getGraphxml() {
		return graphxml;
	}

	public void setGraphxml(String graphxml) {
		this.graphxml = graphxml;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public OozieGraph getGraph() {
		return graph;
	}

	public void setGraph(OozieGraph graph) {
		this.graph = graph;
	}

}