/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link Commander} cached the detail of the program command format,
 * which would be used to generate a real command line at real time. <br/>
 *<br/>
 * For example: <br/>
 * `program.exe <br/>
   -d {in:TextFile:"input_tfidf_file"}<br/> 
   -u {out:TextFile:"output_UMatrix"}<br/>
   -v {out:TextFile:"output_VMatrix"} <br/>
   -outdir {out:Directory_Of_GenericTSV:"output_directory"}<br/> 
   -t [NumberOfTopics:int,1,1000:default,100] <br/>
   -category [ODPCategory:int,0,218:default,0] <br/>
   -l1 [L1Value:float,0,10:default,0.1] <br/>
   -l2 [L2Value:float,0,10:default,0.1]` <br/>
 * In the upper command format, appears following fields: <br/>
 * Input file field holder：{in:FileType:"File description"} <br/>
 * Output file field holder: {out:FileType,StoreType:"File description" } <br/>
 * Numerical field holder：["Parameter Name":ParameterType,Minimum,Maximum:default,DefaultValue]<br/>
 *     The numerical value must be: int、double, and the Minimum and Maximum could be neglected. <br/>
 *     Without Minimum and Maximum: ["Parameter Name":ParameterType:default,DefaultValue]<br/>
 *     Only Minimum is configured: ["Parameter Name":ParameterType,Minimum:default,DefaultValue]<br/>
 *     Only Maximum is configured: ["Parameter Name":ParameterType,Maximum:default,DefaultValue]<br/>
 * Boolean field holder：["Parameter Name":bool:default,true/false]<br/>
 * String field holder：["Parameter Name":string:default,"DefaultValue"]<br/>
 */
public class Commander {

	/**
	 * The field holder for input file 
	 */
	private List<FileHolder> inFileHolders = new ArrayList<FileHolder>();
	private List<FileHolder> outFileHolders = new ArrayList<FileHolder>();
	private List<FileHolder> dyInFileHolders = new ArrayList<FileHolder>();
	private List<FileHolder> dyOutFileHolders = new ArrayList<FileHolder>();
	/**
	 * The field holder for parameters
	 */
	private List<Parameter> parameters = new ArrayList<Parameter>();
	private ScriptHolder scriptHolder = null;
	private List<String> splitList = new LinkedList<String>();

	public List<FileHolder> getInFileHolders() {
		return inFileHolders;
	}
	public List<FileHolder> getOutFileHolders() {
		return outFileHolders;
	}
	public List<FileHolder> getDyInFileHolders() {
		return dyInFileHolders;
	}
	public List<FileHolder> getDyOutFileHolders() {
		return dyOutFileHolders;
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public List<String> getSplitList() {
		return splitList;
	}

	public ScriptHolder getScriptHolder(){
		return scriptHolder;
	}

	public void setScriptHolder(ScriptHolder scriptHolder) {
		this.scriptHolder = scriptHolder;
	}

	/**
	 * Transfer commander to real command line
	 * @param standalone is standalone program or not
	 * @return the command line
	 */
	public String toCommandLine(boolean standalone){
		for (Parameter p : this.getParameters()) {
			int idx = p.getIndex();
			String value = p.getParamValue();
			if( "string".equals( p.getParamType().toLowerCase() ) ){
				value = value.replace("'", "'\\''").replace("*", "'\\*'");
				value = "'"+ value +"'";
			}
			splitList.set(idx, value );
		}

		for( FileHolder fh : this.getInFileHolders() ){
			int idx = fh.getIndex();
			String value = null;
			if( standalone ) value = fh.getFileName();
			else value = fh.getPath();
			splitList.set(idx, value );
		}

		for( FileHolder fh : this.getOutFileHolders() ){
			int idx = fh.getIndex();
			String value = null;
			if( standalone ) value = fh.getFileName();
			else value = fh.getPath();
			splitList.set(idx, value );
		}

		for( FileHolder fh : this.getDyInFileHolders() ){
			int idx = fh.getIndex();
			String value = null;
			if( standalone ) value = fh.getFileName();
			else value = fh.getPath();
			splitList.set(idx, "" );
		}

		for( FileHolder fh : this.getDyOutFileHolders() ){
			int idx = fh.getIndex();
			String value = null;
			if( standalone ) value = fh.getFileName();
			else value = fh.getPath();
			splitList.set(idx,value);
		}

		if( scriptHolder != null  ){
			splitList.set( scriptHolder.getIndex(), scriptHolder.getScriptName() );
		}

		StringBuilder line = new StringBuilder("");
		for(String cmd: splitList ){
			if( cmd == null ) cmd = "''";
			line.append( cmd );
			line.append( " " );
		}

		if( scriptHolder != null  ){
			for( String path: scriptHolder.getInFilePaths() ){
				line.append( path );
				line.append( " ");
			}

			for( String path: scriptHolder.getOutFilePaths() ){
				line.append( path );
				line.append( " ");
			}
		}

		return line.toString().trim();
	}
}
