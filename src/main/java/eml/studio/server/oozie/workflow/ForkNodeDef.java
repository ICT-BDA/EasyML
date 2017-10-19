/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import org.dom4j.Element;

/**
 * The fork node for Oozie fork action
 * @author Roger
 *
 */
public class ForkNodeDef extends ControlNodeDef {

	public ForkNodeDef(String name) {
		super(name);
	}

	@Override
	public void append2XML(Element root) {
		Element fork = root.addElement("fork");
		fork.addAttribute("name", getName());

		for (NodeDef node : outNodes) {
			Element path = fork.addElement("path");
			path.addAttribute("start", node.getName());
		}

	}

}
