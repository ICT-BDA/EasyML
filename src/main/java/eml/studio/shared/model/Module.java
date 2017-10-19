/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import eml.studio.server.anotation.TableField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Abstract class of program and dataset module
 */
public abstract class Module implements IsSerializable {
	@TableField
	protected String id;
	@TableField
	protected String name;
	@TableField
	protected String category;
	@TableField
	protected String path;
	@TableField
	protected Boolean deprecated;
	@TableField
	protected String owner;
	@TableField
	protected String createdate;
	@TableField
	protected String version;
	@TableField
	protected String description;

	public Module(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createDate) {
		this.createdate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public abstract Boolean isDistributed();
}
