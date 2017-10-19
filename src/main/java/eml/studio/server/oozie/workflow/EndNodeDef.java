/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import org.dom4j.Element;
/**
 * The end action for oozie end node 
 */
public class EndNodeDef extends NodeDef {

	public EndNodeDef() {
		super("end");
	}

	@Override
	public void append2XML(Element root) {
		Element kill = root.addElement("kill");
		Element msg = kill.addElement("message");
		msg.addText("Map/Reduce failed, error message[${wf:errorMessage(wf:lastErrorNode())}]");
		kill.addAttribute("name", "fail");

		Element end = root.addElement("end");
		end.addAttribute("name", getName());
	}

}
