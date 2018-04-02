/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import eml.studio.server.anotation.TableField;
import eml.studio.shared.util.ProgramUtil;

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 * Program object class in project
 */
public class Program extends Module implements IsSerializable {
	/** For the convenience of using Java reflection mechanism,
      all the name of the attribute is lowercase*/
	@TableField
	private String type;
	@TableField
	private Boolean isdeterministic;
	@TableField
	private String commandline;
	@TableField
	private String scriptversion;
	@TableField
	private Boolean programable;
	
	@TableField(name="tensorflow_mode")
	private String tensorflowMode;

	public Program() {
	}

	public Program(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.toLowerCase();
	}

	public Boolean getIsdeterministic() {
		return isdeterministic;
	}

	public void setIsdeterministic(Boolean deterministic) {
		this.isdeterministic = deterministic;
	}

	/** is a distributed program or not */
	@Override
	public Boolean isDistributed() {
		return ProgramUtil.isDistributed(type);
	}
	public Boolean isStandalone() {
		return ProgramUtil.isStandalone(type);
	}
	public Boolean isETL() {
		return ProgramUtil.isETL(type);
	}
	public Boolean isTensorflow(){
		return ProgramUtil.isTensorflow(type);
	}
	
	public String getCommandline() {
		return commandline;
	}

	public void setCommandline(String cmd) {
		commandline = cmd;
	}

	public String getScriptversion() {
		return scriptversion;
	}

	public void setScriptversion(String sv) {
		scriptversion = sv;
	}
	
	/**
	 * @return the tensorflowMode
	 */
	public String getTensorflowMode() {
		return tensorflowMode;
	}

	/**
	 * @param tensorflowMode the tensorflowMode to set
	 */
	public void setTensorflowMode(String tensorflowMode) {
		if(tensorflowMode !=null)
		    this.tensorflowMode = tensorflowMode.toLowerCase();
		else
			this.tensorflowMode = tensorflowMode;
	}

	@Override
	public String toString() {
		return "Module [" + "type=" + type + ", deterministic=" + isdeterministic
				+ ", description=" + description + ", version=" + version
				+ ", createdate=" + createdate + ", owner=" + owner + ", commandline="
				+ commandline + "]";
	}

	/** Clone a new model Module */
	public Module clone() {
		Program m = new Program();
		m.setId(this.getId());
		m.setPath(this.getPath());
		m.setCreatedate(this.getCreatedate());
		m.setCommandline(this.getCommandline());
		m.setDeprecated(this.getDeprecated());
		m.setDescription(this.getDescription());
		m.setIsdeterministic(this.getIsdeterministic());
		m.setName(this.getName());
		m.setCategory(this.getCategory());
		m.setOwner(this.getOwner());
		m.setType(this.getType());
		m.setVersion(this.getVersion());
		m.setCategory(this.getCategory());
		m.setScriptversion(this.getScriptversion());
		m.setTensorflowMode(this.getTensorflowMode());
		return m;
	}

	public Boolean getProgramable() {
		return programable;
	}

	public void setProgramable(Boolean progamable) {
		this.programable = progamable;
	}

	public boolean isScriptProgram(){
		return getProgramable() && !isDistributed();
	}
}
