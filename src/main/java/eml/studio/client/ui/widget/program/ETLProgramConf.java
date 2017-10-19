/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.widget.command.Commander;
import eml.studio.client.ui.widget.command.FileDescription;
import eml.studio.client.ui.widget.command.FileHolder;
import eml.studio.client.ui.widget.command.Parameter;

import java.util.List;

/**
 * Conf of ETL Program
 */
public class ETLProgramConf implements ProgramConf{

	private Commander commander = null;
	private boolean standalone;
	public ETLProgramConf(Commander cmd, boolean standalone){
		commander = cmd;
		this.standalone = standalone;
	}

	public int getParameterCount() {
		return commander.getParameters().size();
	}

	@Override
	public int getInputFileCount() {
		return commander.getInFileHolders().size();
	}

	@Override
	public int getOutputFileCount() {
		return commander.getOutFileHolders().size();
	}

	public boolean setParameter(int id, String value) {
		for (Parameter p : commander.getParameters()) {
			if (id == p.getIndex()) {
				p.setParamValue(value);
				return true;
			}
		}
		return false;
	}

	@Override
	public FileHolder getInputFile(int index) {
		List<FileHolder> list = commander.getInFileHolders();
		if( list == null || list.size() < index ) return null;
		return list.get(index);
	}

	@Override
	public FileHolder getOutputFile(int index) {
		List<FileHolder> list = commander.getOutFileHolders();
		if( list == null || list.size() < index ) return null;
		return list.get(index);
	}

	public List<Parameter> getParameters(){
		return commander.getParameters();
	}

	@Override
	public String getCommandLine() {
		return commander.toCommandLine(standalone);
	}

	@Override
	public void setInputFilePath(int index,String path, String filename) {
		FileDescription fd = commander.getInFileHolders().get(index);
		fd.setPath( path );
		fd.setFileName(filename);
	}

	@Override
	public void setOutputFilePath(int index, String path, String filename) {
		FileDescription fd = commander.getOutFileHolders().get(index);
		fd.setPath( path );
		fd.setFileName(filename);
	}
}
