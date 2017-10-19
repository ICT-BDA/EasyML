/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import eml.studio.server.anotation.TableField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Category of program modules in the project
 */
public class Category implements IsSerializable {

	@TableField
	private String id;
	@TableField
	private String name;
	@TableField
	private String level;
	@TableField
	private String type;
	@TableField
	private String path;
	@TableField
	private String fatherid;
	@TableField
	private Boolean haschild;
	@TableField
	private String createtime;

	public Category(){}

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFatherid() {
		return fatherid;
	}

	public void setFatherid(String fatherid) {
		this.fatherid = fatherid;
	}

	public Boolean getHaschild() {
		return haschild;
	}

	public void setHaschild(Boolean haschild) {
		this.haschild = haschild;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}
