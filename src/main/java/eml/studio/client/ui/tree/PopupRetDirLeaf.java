/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
/**
 * Preview the results in the window of the results in the list of each leaf
 */
public class PopupRetDirLeaf extends TreeItem{
	protected static Logger logger = Logger.getLogger(PopupRetDirLeaf.class.getName());
	private String path;
	private String name;
	protected Label label = null;

	public PopupRetDirLeaf(String path)
	{
		this.path = path;
		setName(path);
		label = new Label(getName());
		this.setWidget(label);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 *  Obtain the file name from the path of the current file
	 * @param path Path of current file
	 */
	private void setName(String path) {
		String[] splitStrs = path.split("/");
		this.name = splitStrs[splitStrs.length-1];
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}
}
