/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.property;

import eml.studio.client.ui.widget.panel.ParameterPanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Program;

/**
 * The property table of a program widget, shown in the
 * right of the main panel.
 */
public class ProgramPropertyTable extends PropertyTable {

	public ProgramPropertyTable(Program program) {
		super(7, 2);
		addProperties(program);
	}

	public void addParameterPanel(ParameterPanel parameterPanel) {
		vp.add(parameterPanel);
	}

	private void addProperties(Program m) {
		int i = 0;
		Property p = new Property(Constants.studioUIMsg.moduleName(), m.getName());
		addProperty(p, i++);
		p = new Property(Constants.studioUIMsg.moduleDesription(), m.getDescription());
		addProperty(p, i++);
		p = new Property(Constants.studioUIMsg.moduleDeterminacy(), m.getIsdeterministic().toString());
		addProperty(p, i++);
		p = new Property(Constants.studioUIMsg.moduleVersion(), m.getVersion());
		addProperty(p, i++);
		p = new Property(Constants.studioUIMsg.moduleCreateTime(), m.getCreatedate());
		addProperty(p, i++);
		p = new Property(Constants.studioUIMsg.moduleOwner(), m.getOwner());
		addProperty(p, i++);
		if (!m.getDeprecated())
			p = new Property(Constants.studioUIMsg.moduleDeprecated(), Constants.studioUIMsg.no());
		else
			p = new Property(Constants.studioUIMsg.moduleDeprecated(), Constants.studioUIMsg.yes());
		addProperty(p, i++);
	}

}
