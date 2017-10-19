/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.graph;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import eml.studio.client.ui.widget.program.ProgramWidget;

/**
 * Program node in Oozie workflow graph
 */
public class OozieProgramNode extends OozieNode implements IsSerializable{

	/** Output files of the program */
	protected List<String> files = new LinkedList<String>();

	/** Nickname of the input files */
	protected List<String> input_aliases = new LinkedList<String>();

	/** Nickname of the output files */
	protected List<String> output_aliases = new LinkedList<String>();

	/** Input Parameters of the program */
	protected List<String> params = new LinkedList<String>();

	protected boolean distributed=false;
	private boolean standaloneScript=false;

	private String workPath = null;
	private String cmdLine = null;
	private String script = null;
	private int inputFileCount = 0;
	private int outputFileCount = 0;

	private String oozJobId = ProgramWidget.Model.LATEST_OOZIE_JOBID;

	public OozieProgramNode() {
	}

	public void init(String widgetId, String moduleId, String workPath, int x,int y,
			String oozJobId,int inCnt, int outCnt,boolean isDistributed){
		init(widgetId, moduleId, x, y);
		setInputFileCount(inCnt);
		setOutputFileCount(outCnt);
		setOozJobId(oozJobId);
		this.setWorkPath(workPath);
		this.distributed = isDistributed;
	}

	public void initAsScriptNode(List<String> outFileIdList,
			String script){
		for(String fileId : outFileIdList) files.add( fileId );
		this.script = script;
		this.standaloneScript=true;
	}

	public void initAsCommonNode(List<String> outFileIdList, List<String> paramList){
		for(String fileId : outFileIdList) files.add( fileId );
		for(String param : paramList) params.add( param );
	}

	public void initAsSqlNode(List<String> outFileIdList, 
			List<String> dyOutFileIdList, 
			List<String> paramList,
			List<String> input_aliases,
			List<String> output_aliases,
			List<String> dy_input_aliases,
			List<String> dy_output_aliases,
			String script){
		for(String fileId : outFileIdList) files.add( fileId );
		for(String param : input_aliases) this.input_aliases.add( param );
		for(String param : output_aliases) this.output_aliases.add( param );
		setScript(script);
	}

	public List<String> getFiles() {
		return files;
	}

	public List<String> getParams() {
		return params;
	}


	public String getOozJobId() {
		return oozJobId;
	}

	public void setOozJobId(String oozJobId) {
		this.oozJobId = oozJobId;
	}

	public void addFile(String file) {
		files.add(file);
	}

	public void addParam(String param) {
		params.add(param);
	}

	public void addInputAliases(String aliases){
		input_aliases.add( aliases);
	}

	public void addOutputAliases(String aliases){
		output_aliases.add( aliases);
	}

	/**
	 * Generate a XML String for the node
	 */
	@Override
	public String toXML() {
		StringBuffer sb = new StringBuffer(500);
		sb.append("<widget type='program'>\n");
		genXML(sb);
		return sb.toString();
	}
	public void genXML(StringBuffer sb){
		sb.append("  <id>" + id + "</id>\n");
		sb.append("  <moduleId>" + moduleId + "</moduleId>\n");
		sb.append("  <oozJob>" + getOozJobId() + "</oozJob>\n");
		sb.append("  <x>" + x + "</x>\n");
		sb.append("  <y>" + y + "</y>\n");
		sb.append("  <work_path>"+String.valueOf(getWorkPath())+"</work_path>\n");
		for (String file : files)
			sb.append("  <file>" + file + "</file>\n");
		for (String param : params)
			sb.append("  <param>" + param + "</param>\n");
		for(String aliases: input_aliases)
			sb.append("  <input_aliases>" + aliases + "</input_aliases>\n");
		for(String aliases: output_aliases)
			sb.append("  <output_aliases>" + aliases + "</output_aliases>\n");

		if( getScript() != null ){
			sb.append("  <incount>" + getInputFileCount() + "</incount>\n");
			sb.append("  <outcount>" + getOutputFileCount() + "</outcount>\n");

			String script_trans = getScript().replace(">", "&gt;").replace("<", "&lt;");
			sb.append("  <script>" + script_trans +"</script>\n");
		}
		sb.append("  <cmd_line>"+String.valueOf(getCmdLine())+"</cmd_line>\n");
		sb.append("  <is_distributed>"+String.valueOf(isDistributed())+"</is_distributed>\n");
		sb.append("  <is_standalone_script>"+String.valueOf(standaloneScript)+"</is_standalone_script>\n");		sb.append("</widget>\n");
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getOutputFileCount() {
		return outputFileCount;
	}

	public void setOutputFileCount(int outputFileCount) {
		this.outputFileCount = outputFileCount;
	}

	public int getInputFileCount() {
		return inputFileCount;
	}

	public void setInputFileCount(int inputFileCount) {
		this.inputFileCount = inputFileCount;
	}

	public List<String> getOutputAliases(){
		return output_aliases;
	}

	public List<String> getInputAliases(){
		return input_aliases;
	}

	public boolean isDistributed(){
		return this.distributed;
	}

	public void setDistributed(boolean distributed){
		this.distributed = distributed;
	}

	public String getCmdLine() {
		return cmdLine;
	}

	public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}

	public void setStandaloneScript(boolean standaloneScript) {
		this.standaloneScript = standaloneScript;
	}

	public String getWorkPath() {
		return workPath;
	}

	public void setWorkPath(String workPath) {
		this.workPath = workPath;
	}

}
