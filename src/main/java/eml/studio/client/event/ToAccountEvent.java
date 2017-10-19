/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The event that will be triggered when navigating to account page
 */
public class ToAccountEvent extends GwtEvent<ToAccountEvent.ToAccountEventHandler> {
	public final static Type<ToAccountEventHandler> TYPE = new Type<ToAccountEventHandler>();

	private String message;

	public ToAccountEvent(String msg) {
		message = msg;
	}

	public interface ToAccountEventHandler extends EventHandler {
		void onToAccountEvent(ToAccountEvent event);
	}

	@Override
	public Type<ToAccountEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ToAccountEventHandler handler) {
		handler.onToAccountEvent(this);
	}

	public String getMessage() {
		return message;
	}

}
