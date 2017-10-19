/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

import eml.studio.client.controller.DBController;
import eml.studio.client.ui.menu.HasRightMouseUpMenu;
import eml.studio.client.ui.menu.MenuItemCommand;
import eml.studio.client.ui.widget.shape.OutNodeShape;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 *  MenuItem factory for action outnode shape
 */
public class BaseShapeMenuItemFactory {

	/**
	 * Create result preview popup menuitem
	 * 
	 * @param com
	 * @return
	 */
	public static MenuItem createShowResultItem(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {
			@Override
			public void execute() {
				NodeShape nodeShape = (NodeShape) this.component;
				//If the shape node is output node shape
				if(nodeShape instanceof OutNodeShape) {
					OutNodeShape outShape  = (OutNodeShape) nodeShape;
					String fileId = outShape.getFileId();
					String path = outShape.getAbsolutePath();
					DBController.showPreviewPopup(path,fileId);
				}
				nodeShape.getContextMenu().hide();
			}
		};

		MenuItem item = new MenuItem("Results preview", command);
		return item;
	}

	/**
	 * Create data visualization popup menuitem
	 * 
	 * @param com
	 * @return
	 */
	public static MenuItem createVisuallizeItem(HasRightMouseUpMenu com) {
		Command command = new MenuItemCommand(com) {
			@Override
			public void execute() {
				NodeShape nodeShape = (NodeShape) this.component;
				// If the node shape is output node shape
				if(nodeShape instanceof OutNodeShape) {
					OutNodeShape outShape  = (OutNodeShape) nodeShape;
					String fileId = outShape.getFileId();
					String path = outShape.getAbsolutePath();
					String fPath = path +"/"+fileId;
					DBController.showDataVisualPopup(fPath,fileId);
				}
				nodeShape.getContextMenu().hide();
			}
		};
		MenuItem item = new MenuItem("Visualization", command);
		return item;
	}
}
