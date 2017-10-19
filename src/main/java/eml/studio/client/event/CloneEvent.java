/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The event that will be triggered when clone
 */
public class CloneEvent extends GwtEvent<CloneEvent.CloneEventHandler> {
	public final static Type<CloneEventHandler> TYPE = new Type<CloneEventHandler>();

	private String message;

	public CloneEvent(String msg) {
		message = msg;
	}

	public interface CloneEventHandler extends EventHandler {
		void onCloneEvent(CloneEvent event);
	}

	@Override
	public Type<CloneEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CloneEventHandler handler) {
		handler.onCloneEvent(this);
	}

	public String getMessage() {
		return message;
	}

}
