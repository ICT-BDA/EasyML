/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;


public interface FileDescription {
	/**
	 * File store type:
	 * - SFile: local file
	 * - HFile: HDFS file
	 * - Directory: local/HDFS directory
	 */
	public enum StoreType {
		SFile("SFile"), HFile("HFile"), Directory("Directory"),None("None");

		private String name;

		StoreType(String name) {
			this.name = name;
		}

		public static StoreType get(String name) {
			if ("SFile".equals(name)) {
				return SFile;
			} else if ("HFile".equals(name)) {
				return HFile;
			} else if ("Directory".equals(name)) {
				return Directory;
			} else {
				return HFile;
			}
		}
		@Override
		public String toString() {
			return name;
		}
	}

	public String getContentType();
	public void setContentType(String contentType);

	public StoreType getStoreType();
	public void setStoreType(StoreType storeType);

	public String getPath();
	public void setPath(String path);

	public String getFileName();
	public void setFileName(String name);

	public String getDescription();
}

