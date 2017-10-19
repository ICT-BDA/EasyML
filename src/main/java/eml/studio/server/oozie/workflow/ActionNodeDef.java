/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import eml.studio.shared.model.Program;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * Node definition for action node of oozie
 */
public abstract class ActionNodeDef extends NodeDef {
	/**
	 * the command line configuration of the action
	 */
	private String commandLine;
	/**
	 * the widget id this node respected to
	 */
	private String widgetId;
	/**
	 * infomation of program that will be run in this action
	 */
	private Program program;
	public ActionNodeDef(Program program, String widgetId, String cmdLine) {
		super(widgetId);
		this.widgetId = widgetId;
		this.program = program;
		this.commandLine = cmdLine;
	}

	/**
	 * This function will be called somewhere when generate oozie workflow. <br/>
	 * It append current Oozie Action workflow configuration to the workflow xml tree.
	 */
	@Override
	public void append2XML(Element root) {
		Element action = root.addElement("action");

		action.addAttribute("name", getName());

		generateActionXML(action);

		Element ok = action.addElement("ok");
		Element error = action.addElement("error");

		NodeDef toNode = outNodes.iterator().next();

		//when success, the next runnable action is configured by toNode
		ok.addAttribute("to", toNode.getName());
		error.addAttribute("to", "fail");
	}

	private void createProperty(Element root, String name, String value) {
		Element property = root.addElement("property");
		generateElement(property, "name", name);
		generateElement(property, "value", value);
	}

	/**
	 * the detail xml configurations goes here, this will be called in append2XML
	 * @param action
	 */
	public void generateActionXML(Element action) {
		Namespace xmlns = new Namespace("", "uri:oozie:shell-action:0.2"); // root namespace uri
		QName qName = QName.get("shell", xmlns); // your root element's name
		Element shell = action.addElement(qName);
		// action.appendChild( shell );

		generateElement(shell, "job-tracker", "${jobTracker}");
		generateElement(shell, "name-node", "${nameNode}");

		Element configuration = shell.addElement("configuration");
		createProperty(configuration, "mapred.job.queue.name", "${queueName}");
		createProperty(configuration, "mapreduce.map.memeory.mb", "10240");

		if( program.isScriptProgram() ){
			//单机脚本类型程序
			generateElement(shell, "exec", "./" + widgetId + ".startup");
		} else {
			generateElement(shell, "exec", "./run.sh");
		}

		command2XMLforShell(shell);

		if( program.isScriptProgram() ){
			generateElement(shell, "file", "${appPath}/" + widgetId
					+ ".startup");
			generateElement(shell, "file", "${appPath}/" + widgetId
					+ ".script");
		} else
			generateElement(shell, "file", "${nameNode}/" + program.getPath()
					+ "/run.sh");

		shell.addElement("capture-output");
	}

	/**
	 * generate command line and program arguments configuration
	 * <br/>
	 * <p>
	 * <argument>java -jar filemerge.jar ...</argument>
       <argument>path/to/lib/</argument>
       <argument>parameter 1</argument>
       <argument>parameter 2</argument>
       <argument>parameter 3</argument>
       <p/>
	 * @param shell
	 */
	protected void command2XMLforShell(Element shell) {
		generateElement(shell, "argument", commandLine);
		generateElement(shell, "argument", "${nameNode}/" + program.getPath()
				+ "/lib/");
		generateElement(shell, "argument", "${appPath}/" + widgetId);
		appendFileParameter(shell);
	}

	protected void generateElement(Element root, String tag, String content) {
		if(content == null) content = "";
		Element ele = root.addElement(tag);
		ele.addText(content);
	}

	protected abstract void appendFileParameter(Element shell);

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

}
