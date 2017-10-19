/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.util.Constants;
import eml.studio.client.ui.widget.program.ProgramWidget;

/**
 * Program Tree Menu
 */
public class ProgramTree extends ProgramModuleTree {

	/** Record the latest popup widget */
	private ProgramWidget popupWidget = null;

	public ProgramTree() {
		super(Constants.studioUIMsg.systemProgram(),
				Constants.studioUIMsg.myProgram(),
				Constants.studioUIMsg.sharedProgram());
	}

	public ProgramWidget getPopupWidget() {
		return popupWidget;
	}

	public void setPopupWidget(ProgramWidget popupWidget) {
		this.popupWidget = popupWidget;
	}

}
