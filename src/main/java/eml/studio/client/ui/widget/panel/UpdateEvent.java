/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Update module event class
 */
public class UpdateEvent extends GwtEvent<UpdateHandler>{

	public final static Type<UpdateHandler> TYPE = new Type<UpdateHandler>();
	private boolean isInput;
	private boolean isInsert;
	private int row;

	public UpdateEvent(boolean isInput,boolean isInsert, int row){
		this.isInput = isInput;
		this.isInsert = isInsert;
		this.row = row;
	}

	@Override
	public Type<UpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UpdateHandler handler) {
		handler.onUpdate(this);
	}
	public boolean isInput() {
		return isInput;
	}

	public int getRow() {
		return row;
	}

	public boolean isInsert() {
		return isInsert;
	}
}
