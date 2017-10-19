/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import java.util.List;

import eml.studio.client.ui.widget.command.Commander;
import eml.studio.client.ui.widget.command.FileDescription;
import eml.studio.client.ui.widget.command.Parameter;
import eml.studio.client.ui.widget.command.ScriptCommand;
import eml.studio.client.ui.widget.command.ScriptFileDescription;

/**
 * The conference of SQL Script Program Widget
 */
public class SqlScriptProgramConf implements ProgramConf{

	private ScriptCommand scripCommand;
	private Commander commander;
	public SqlScriptProgramConf(Commander cmd){
		scripCommand = new ScriptCommand("sql");
		commander = cmd;
	}

	@Override
	public String getCommandLine() {

		String scriptPath = scripCommand.getScriptPath();
		String inputPaths = "";
		String outputPaths = "";
		for( FileDescription fd: scripCommand.getInFileDescriptions()){
			ScriptFileDescription sfd = (ScriptFileDescription)fd;
			inputPaths += sfd.getOtherName() +":";
			inputPaths += sfd.getPath() +";";
		}

		for( FileDescription fd: scripCommand.getOutFileDescriptions()){
			ScriptFileDescription sfd = (ScriptFileDescription)fd;
			outputPaths += sfd.getOtherName() +":";
			outputPaths += sfd.getPath() +";";
		}

		List<Parameter> list = commander.getParameters();
		list.get(0).setParamValue( scriptPath );
		list.get(1).setParamValue( inputPaths );
		list.get(2).setParamValue( outputPaths );

		return commander.toCommandLine(false);
	}

	@Override
	public int getInputFileCount() {
		return scripCommand.getInFileDescriptions().size();
	}

	@Override
	public int getOutputFileCount() {
		return scripCommand.getOutFileDescriptions().size();
	}

	@Override
	public FileDescription getInputFile(int index) {
		return scripCommand.getInFileDescriptions().get(index);
	}

	@Override
	public FileDescription getOutputFile(int index) {
		return scripCommand.getOutFileDescriptions().get(index);
	}

	@Override
	public void setInputFilePath(int index, String path, String filename) {
		int i = scripCommand.getInFileDescriptions().size();
		while( i <= index ){ 
			scripCommand.getInFileDescriptions().add( new ScriptFileDescription());
			i ++;
		}

		FileDescription fd = scripCommand.getInFileDescriptions().get(index);
		fd.setPath(path);
		fd.setFileName(filename);
	}

	@Override
	public void setOutputFilePath(int index, String path, String filename) {
		int i = scripCommand.getOutFileDescriptions().size();
		while( i <= index ){ 
			scripCommand.getOutFileDescriptions().add( new ScriptFileDescription());
			i ++;
		}
		FileDescription fd = scripCommand.getOutFileDescriptions().get(index);
		fd.setPath(path);
		fd.setFileName(filename);
	}

	public void setScriptPath(String path, String name){
		scripCommand.setScriptPath(path);
		scripCommand.setScriptName( name );
	}
	public void setScriptContent(String content){
		scripCommand.setContent(content);
	}
	public String getScriptContent(){
		return scripCommand.getContent();
	}
	public List<FileDescription> getOutFileDescriptions(){
		return scripCommand.getOutFileDescriptions();
	}
	public List<FileDescription> getInFileDescriptions(){
		return scripCommand.getInFileDescriptions();
	}
	public List<Parameter> getParameters(){
		return commander.getParameters();
	}
}
