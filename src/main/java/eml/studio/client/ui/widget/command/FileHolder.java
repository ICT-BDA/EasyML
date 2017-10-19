/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

public class FileHolder implements FileDescription{
	/**
	 * File type: identity the file is input or output file 
	 * InputFile :  workflow input file
	 * OutputFile : workflow output file
	 */
	public enum FileType {
		InputFile, OutputFile;
	}
	/**
	 * File content type, like "RawPoint", "LabeledPoint", ...
	 */
	private String contentType;
	private StoreType storeType;
	private String paraName;

	/**
	 * description of the file
	 */
	private String description;

	private FileType fileType;

	private boolean inputfile;

	private int index;

	private String path = null;
	private String filename = null;

	public FileHolder() {
		contentType = "General";
		setParaName("");
		setDescription("");
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void setContentType(String type) {
		this.contentType = type;
	}

	public String getParaName() {
		return paraName;
	}

	public void setDescription(String des) {
		this.description = des;
	}

	public void addDescription(String des) {
		this.description = this.description + " " + des;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(contentType);
		sb.append(":");
		sb.append(paraName);
		return sb.toString();

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public StoreType getStoreType() {
		return storeType;
	}

	@Override
	public void setStoreType(StoreType storeType) {
		this.storeType = storeType;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getFileName() {
		return filename;
	}

	@Override
	public void setFileName(String name) {
		filename = name;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public boolean isInputfile() {
		return inputfile;
	}

	public void setInputfile(boolean inputfile) {
		this.inputfile = inputfile;
	}
}