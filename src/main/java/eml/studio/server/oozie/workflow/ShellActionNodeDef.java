/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import eml.studio.shared.model.Program;
import org.dom4j.Element;

/**
 * The action node for Oozie Action that will use shell command
 */
public class ShellActionNodeDef extends ActionNodeDef {

	public ShellActionNodeDef(Program program, String nodeId, String cmdLine) {
		super(program, nodeId, cmdLine);
	}

	@Override
	protected void appendFileParameter(Element shell) {

		// add all input file arguments to the shell xml element
		int inCnt = inputFiles.size();
		for( int i = 0; i < inCnt; ++ i ){
			generateElement(shell, "argument", "HDFS");
			generateElement(shell, "argument", inputFiles.get(i));
		}

		//add all output file arguments to the shell xml element
		int outCnt = outputFiles.size();
		for( int j = 0; j < outCnt; ++ j ){
			generateElement(shell, "argument", outputFiles.get(j));
		}

	}

}
