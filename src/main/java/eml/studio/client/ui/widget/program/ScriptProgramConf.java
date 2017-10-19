/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import java.util.List;

import eml.studio.client.ui.widget.command.FileDescription;
import eml.studio.client.ui.widget.command.ScriptCommand;
import eml.studio.client.ui.widget.command.ScriptFileDescription;

/**
 * The conference of Script Program Widget
 */
public class ScriptProgramConf implements ProgramConf{

	private ScriptCommand command;

	public ScriptProgramConf(String type){
		command = new ScriptCommand(type);
	}

	@Override
	public String getCommandLine() {
		return command.toCommandLine();
	}

	@Override
	public int getInputFileCount() {
		return command.getInFileDescriptions().size();
	}

	@Override
	public int getOutputFileCount() {
		return command.getOutFileDescriptions().size();
	}

	@Override
	public FileDescription getInputFile(int index) {
		return command.getInFileDescriptions().get(index);
	}

	@Override
	public FileDescription getOutputFile(int index) {
		return command.getOutFileDescriptions().get(index);
	}

	@Override
	public void setInputFilePath(int index, String path, String filename) {
		int i = command.getInFileDescriptions().size();
		while( i <= index ){ 
			command.getInFileDescriptions().add( new ScriptFileDescription());
			i ++;
		}

		FileDescription fd = command.getInFileDescriptions().get(index);
		fd.setPath(path);
		fd.setFileName(filename);
	}

	@Override
	public void setOutputFilePath(int index, String path, String filename) {
		int i = command.getOutFileDescriptions().size();
		while( i <= index ){ 
			command.getOutFileDescriptions().add( new ScriptFileDescription());
			i ++;
		}
		FileDescription fd = command.getOutFileDescriptions().get(index);
		fd.setPath(path);
		fd.setFileName(filename);
	}

	public void setScriptPath(String path, String name){
		command.setScriptPath(path);
		command.setScriptName( name );
	}

	public void setScriptContent(String content){
		command.setContent(content);
	}

	public String getScriptContent(){
		return command.getContent();
	}


	public List<FileDescription> getOutFileDescriptions(){
		return command.getOutFileDescriptions();
	}
	public List<FileDescription> getInFileDescriptions(){
		return command.getInFileDescriptions();
	}
}
