/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.ui.widget.command.CommandParser;
import eml.studio.client.ui.widget.command.Commander;
import eml.studio.shared.model.Program;
import com.google.gwt.user.client.Window;

/**
 * Class of ETL program widget
 */
public class ETLProgramWidget extends ProgramWidget {

	public ETLProgramWidget(Program program, String widget_uuid) {
		super(program, widget_uuid);
	}

	@Override
	protected ProgramConf createProgramConf() {
		Commander cmd = null;
		try {
			cmd = CommandParser.parse(program.getCommandline());
		} catch (CommandParseException e) {
			Window.alert( e.getMessage() );
		}
		ETLProgramConf conf = new ETLProgramConf(cmd, !program.isDistributed());

		return conf;
	}

	/**
	 * As the parameters can be changed, the parameter panel should
	 * be created manually.
	 */
	@Override
	public void genParamPanel(boolean editable) {

	}

	@Override
	public ETLProgramConf getProgramConf(){
		return (ETLProgramConf)programConf;
	}

}