/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import eml.studio.server.anotation.TableField;

/**
 * DataSet object class in project
 */
public class Dataset extends Module {
	@TableField
	private String storetype; // "hdfs" or "local"
	private String contenttype; // "general" or "csv" or "tsv" or "json"

	public Dataset(){
	}

	public Dataset(String id) {
		this.id = id;
	}

	/** 
	 * Is a distributed dataset or not 
	 */
	@Override
	public Boolean isDistributed() {return storetype.equals("hdfs");}

	/**
	 * Clone a new Dataset Module
	 */
	public Dataset clone() {
		Dataset m = new Dataset();
		m.setId(this.getId());
		m.setPath(this.getPath());
		m.setName(this.getName());
		m.setContenttype(this.getContenttype());
		m.setStoretype(this.getStoretype());
		m.setCreatedate(this.getCreatedate());
		m.setDeprecated(this.getDeprecated());
		m.setDescription(this.getDescription());
		m.setOwner(this.getOwner());
		m.setVersion(this.getVersion());
		m.setCategory(this.getCategory());
		return m;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public String getStoretype() {
		return storetype;
	}

	public void setStoretype(String storetype) {
		this.storetype = storetype;
	}
}
