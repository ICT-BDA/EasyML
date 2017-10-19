/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import eml.studio.server.anotation.TableField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This object is used to record module version
 */
public class ModuleVersion implements IsSerializable {
	@TableField
	private String oldversionid;
	@TableField
	private String newversionid;

	public String getOldversionid() {
		return oldversionid;
	}

	public void setOldversionid(String Oldversionid) {
		this.oldversionid = Oldversionid;
	}

	public String getNewversionid() {
		return newversionid;
	}

	public void setNewversionid(String Newversionid) {
		this.newversionid = Newversionid;
	}
}
