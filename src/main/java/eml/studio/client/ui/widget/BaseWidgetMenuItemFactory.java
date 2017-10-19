/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.controller.MonitorController;
import eml.studio.client.ui.menu.HasRightMouseUpMenu;
import eml.studio.client.ui.menu.MenuItemCommand;
import eml.studio.client.ui.widget.dataset.DatasetWidget;
import eml.studio.client.ui.widget.program.ProgramWidget;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Generate base widget's menu item
 */
public class BaseWidgetMenuItemFactory {
	/**
	 * Create DeleteItem
	 * @param com
	 * @return delete item
	 */
	public static MenuItem createDeleteItem(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {

			@Override
			public void execute() {
				BaseWidget widget = (BaseWidget) this.component;
				DiagramController controller = widget.getController();
				controller.deleteWidget(widget);
				widget.getContextMenu().hide();
			}

		};

		MenuItem item = new MenuItem("Delete", command);
		return item;
	}
	/**
	 * Create PremeterItem
	 * @param com
	 * @return PremeterItem
	 */
	public static MenuItem createPremeterItem(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {

			@Override
			public void execute() {
				ProgramWidget widget = (ProgramWidget) this.component;
				widget.getParameterPanel();
				widget.setETLPanel();
				widget.getContextMenu().hide();
			}

		};

		MenuItem item = new MenuItem("Configure parameters", command);
		return item;
	}

	/**
	 * Create DownloadDataItem
	 * @param com
	 * @return DownloadDataItem
	 */
	public static MenuItem createDownloadData(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {

			@Override
			public void execute() {

				DatasetWidget widget = (DatasetWidget) this.component;
				widget.getContextMenu().hide();
				OutNodeShape shape = widget.getOutNodeShapes().get(0);
				String filename = shape.getAbsolutePath() + "/" + shape.getFileId();
				String url = GWT.getModuleBaseURL().split("EMLStudio")[0]
						+ "EMLStudioMonitor/filedownload?filename=" + filename;
				Window.open(url, "_blank", "status=0,toolbar=0,menubar=0,location=0");

			}

		};

		MenuItem item = new MenuItem("Download", command);
		return item;
	}


	/**
	 * Create StderrItem
	 * @param com
	 * @return StderrItem
	 */
	public static MenuItem createStderrItem(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {

			@Override
			public void execute() {
				ProgramWidget widget = (ProgramWidget) this.component;
				MonitorController controller = (MonitorController) widget
						.getController();
				String oozJobId = widget.getAction().getJobId();
				String actionid = widget.getId();
				controller.getStdErr(oozJobId, actionid);
				widget.getContextMenu().hide();
			}

		};

		MenuItem item = new MenuItem("Show Stderr", command);
		return item;
	}
	/**
	 * Create StdoutItem
	 * @param com
	 * @return StdoutItem
	 */
	public static MenuItem createStdoutItem(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {

			@Override
			public void execute() {
				ProgramWidget widget = (ProgramWidget) this.component;
				MonitorController controller = (MonitorController) widget
						.getController();
				String oozJobId = widget.getAction().getJobId();
				String actionid = widget.getId();
				controller.getStdOut(oozJobId, actionid);
				widget.getContextMenu().hide();
			}

		};

		MenuItem item = new MenuItem("Show Stdout", command);
		return item;
	}
}
