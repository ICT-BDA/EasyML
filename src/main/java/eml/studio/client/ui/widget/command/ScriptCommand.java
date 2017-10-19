/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Cached the detail of the script program command format,
 * which can support python or shell command script
 *
 */
public class ScriptCommand {

	//python,shell
	private String type;
	/*the script file path*/
	private String scriptPath;
	/*the script file name*/
	private String scriptName;

	/*the script content*/
	private String content = "";
	private List<FileDescription> inFileDescriptions = new ArrayList<FileDescription>();
	private List<FileDescription> outFileDescriptions = new ArrayList<FileDescription>();

	public ScriptCommand(String type){
		this.type =  type;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String toCommandLine(){
		StringBuilder sb = new StringBuilder(type);
		sb.append(" ");
		sb.append(scriptName);

		for( FileDescription fd: inFileDescriptions ){
			sb.append(" ");
			sb.append( fd.getFileName() );
		}

		for( FileDescription fd: outFileDescriptions ){
			sb.append(" ");
			sb.append( fd.getFileName() );
		}
		return sb.toString();
	}


	public String getScriptPath() {
		return scriptPath;
	}
	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public List<FileDescription> getInFileDescriptions() {
		return inFileDescriptions;
	}

	public List<FileDescription> getOutFileDescriptions() {
		return outFileDescriptions;
	}

}
