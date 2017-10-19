/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The event that will be triggered when navigating to monitor page
 */
public class ToMonitorEvent extends GwtEvent<ToMonitorEvent.ToMonitorEventHandler> {
	public final static Type<ToMonitorEventHandler> TYPE = new Type<ToMonitorEventHandler>();

	private String message;

	public ToMonitorEvent(String msg) {
		message = msg;
	}

	public interface ToMonitorEventHandler extends EventHandler {
		void onToMonitorEvent(ToMonitorEvent event);
	}

	@Override
	public Type<ToMonitorEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ToMonitorEventHandler handler) {
		handler.onToMonitorEvent(this);
	}

	public String getMessage() {
		return message;
	}

}
