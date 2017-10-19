/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.component;

import com.google.gwt.user.client.ui.ListBox;

import java.util.LinkedList;

/**
 * List Box in DescCmdPanel
 */
public class DescListBox extends ListBox implements DescWidget {

	public DescListBox (boolean multichocie){
		super(multichocie);
	}
	public DescListBox (){
		super();
	}

	public LinkedList<Integer> getSelectedItems() {
		LinkedList<Integer> selectedItems = new LinkedList<Integer>();
		for (int i = 0; i < getItemCount(); i++) {
			if (isItemSelected(i)) {
				selectedItems.add(i);
			}
		}
		return selectedItems;
	}
	@Override
	public void setValue(String val) {
		this.setSelectedIndex(-1);
		for (int i = 0; i < this.getItemCount(); i++) {
			String string = this.getItemText(i);
			if (string.equals(val)) {
				this.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public String getValue() {
		return this.getItemText(this.getSelectedIndex());
	}

}
