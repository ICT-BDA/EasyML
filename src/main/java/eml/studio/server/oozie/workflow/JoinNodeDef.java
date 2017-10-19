/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import org.dom4j.Element;

/**
 * The join node for Oozie Join Action
 */
public class JoinNodeDef extends ControlNodeDef {

	public JoinNodeDef(String name) {
		super(name);
	}

	@Override
	public void append2XML(Element root) {
		Element join = root.addElement("join");
		join.addAttribute("name", getName());

		NodeDef toNode = outNodes.iterator().next();
		join.addAttribute("to", toNode.getName());
	}

}
