/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.util;

/**
 * Dataset type enumeration
 */
public enum DatasetType {
	JSON(0,"JSON"),
	CSV(1,"CSV"),
	TSV(2,"TSV"),
	GENERAL(3,"General"),
	DIRECTORY(4,"Directory");

	private int typeCode;  
	private String desc;

	private DatasetType(int typeCode, String desc)
	{
		this.setTypeCode(typeCode);
		this.setDesc(desc);
	}

	public int getTypeCode() {
		return typeCode;
	}

	private void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public String getDesc() {
		return desc;
	}

	private void setDesc(String desc) {
		this.desc = desc;
	}
}
