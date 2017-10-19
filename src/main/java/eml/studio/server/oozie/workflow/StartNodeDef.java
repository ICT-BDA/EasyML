/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import org.dom4j.Element;

/**
 * The start node of Oozie start node
 */
public class StartNodeDef extends NodeDef {

	public StartNodeDef() {
		super("start");
	}

	@Override
	public void append2XML(Element root) {
		Element start = root.addElement("start");

		// assure that start node has only one out-path
		NodeDef nextNode = outNodes.iterator().next();
		start.addAttribute("to", nextNode.getName());
	}

}
