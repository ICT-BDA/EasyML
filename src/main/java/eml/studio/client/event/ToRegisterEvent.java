/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The event that will be triggered when navigating to register page
 *
 */
public class ToRegisterEvent extends GwtEvent<ToRegisterEvent.ToRegisterEventHandler> {
	public final static Type<ToRegisterEventHandler> TYPE = new Type<ToRegisterEventHandler>();

	private String message;

	public ToRegisterEvent(String msg) {
		message = msg;
	}

	public interface ToRegisterEventHandler extends EventHandler {
		void onToRegisterEvent(ToRegisterEvent event);
	}

	@Override
	public Type<ToRegisterEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ToRegisterEventHandler handler) {
		handler.onToRegisterEvent(this);
	}

	public String getMessage() {
		return message;
	}

}
