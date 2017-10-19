/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.oozie;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

import eml.studio.server.anotation.TableField;

/**
 * A OozieAction object contains the detail information, such as id, name, status.
 */
public class OozieAction implements IsSerializable {

	public static String NEW_STATUS = "new";
	@TableField(name="jobid")
	private String bdaJobId;
	@TableField(name="ooziejobid")
	private String JobId;
	@TableField(name="apppath")
	private String AppPath;
	@TableField(name="name")
	private String Name;
	@TableField(name="cred")
	private String Cred;
	@TableField(name="type")
	private String Type;
	private String Conf;
	@TableField(name="status")
	private String Status;
	@TableField(name="retries")
	private Integer Retries;
	@TableField(name="userretrycount")
	private Integer UserRetryCount;
	@TableField(name="userretrymax")
	private Integer UserRetryMax;
	@TableField(name="userretryinterval")
	private Integer UserRetryInterval;
	@TableField(name="starttime")
	private Date StartTime;
	@TableField(name="endtime")
	private Date EndTime;
	@TableField(name="transition")
	private String Transition;

	private String Data;
	@TableField(name="stats")
	private String Stats;
	@TableField(name="externalchildids")
	private String ExternalChildIDs;
	@TableField(name="externalid")
	private String ExternalId;
	@TableField(name="externalstatus")
	private String ExternalStatus;
	@TableField(name="trackeruri")
	private String TrackerUri;
	@TableField(name="consoleurl")
	private String ConsoleUrl;
	@TableField(name="errorcode")
	private String ErrorCode;
	@TableField(name="errormessage")
	private String ErrorMessage;

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

	public String getCred() {
		return Cred;
	}

	public void setCred(String Cred) {
		this.Cred = Cred;
	}

	public String getData() {
		return Data;
	}

	public void setData(String Data) {
		this.Data = Data;
	}

	public Date getEndTime() {
		return EndTime;
	}

	public void setEndTime(Date EndTime) {
		this.EndTime = EndTime;
	}

	public String getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(String ErrorCode) {
		this.ErrorCode = ErrorCode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String ErrorMessage) {
		this.ErrorMessage = ErrorMessage;
	}

	public String getExternalChildIDs() {
		return ExternalChildIDs;
	}

	public void setExternalChildIDs(String ExternalChildIDs) {
		this.ExternalChildIDs = ExternalChildIDs;
	}

	public String getExternalId() {
		return ExternalId;
	}

	public void setExternalId(String ExternalId) {
		this.ExternalId = ExternalId;
	}

	public String getExternalStatus() {
		return ExternalStatus;
	}

	public void setExternalStatus(String ExternalStatus) {
		this.ExternalStatus = ExternalStatus;
	}

	public String getJobId() {
		return JobId;
	}

	public void setJobId(String jobId) {
		this.JobId = jobId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Integer getRetries() {
		return Retries;
	}

	public void setRetries(Integer Retries) {
		this.Retries = Retries;
	}

	public Date getStartTime() {
		return StartTime;
	}

	public void setStartTime(Date StartTime) {
		this.StartTime = StartTime;
	}

	public String getStats() {
		return Stats;
	}

	public void setStats(String Stats) {
		this.Stats = Stats;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		this.Status = status;
	}

	public String getTrackerUri() {
		return TrackerUri;
	}

	public void setTrackerUri(String TrackerUri) {
		this.TrackerUri = TrackerUri;
	}

	public String getTransition() {
		return Transition;
	}

	public void setTransition(String Transition) {
		this.Transition = Transition;
	}

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public Integer getUserRetryCount() {
		return UserRetryCount;
	}

	public void setUserRetryCount(Integer UserRetryCount) {
		this.UserRetryCount = UserRetryCount;
	}

	public Integer getUserRetryInterval() {
		return UserRetryInterval;
	}

	public void setUserRetryInterval(Integer UserRetryInterval) {
		this.UserRetryInterval = UserRetryInterval;
	}

	public Integer getUserRetryMax() {
		return UserRetryMax;
	}

	public void setUserRetryMax(Integer UserRetryMax) {
		this.UserRetryMax = UserRetryMax;
	}

	public String getAppPath() {
		return AppPath;
	}

	public void setAppPath(String appPath) {
		AppPath = appPath;
	}

	public String getBdaJobId() {
		return bdaJobId;
	}

	public void setBdaJobId(String bdaJobId) {
		this.bdaJobId = bdaJobId;
	}
}
