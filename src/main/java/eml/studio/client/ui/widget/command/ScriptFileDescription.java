/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

/**
 * Script file type description
 * - Script file default store type is local file
 *
 */
public class ScriptFileDescription implements FileDescription{

	private String contentType = "General";
	private StoreType storeType = StoreType.SFile;
	private String path;
	private String filename;
	private String otherName;

	@Override
	public String getContentType(){
		return contentType;
	}
	
	@Override
	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	@Override
	public StoreType getStoreType(){
		return this.storeType;
	}
	@Override
	public void setStoreType(StoreType storeType){
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
		this.filename = name;
	}

	@Override
	public String getDescription(){
		if( otherName != null ) return otherName;
		return "...";
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
}

