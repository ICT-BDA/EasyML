/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The event that will be triggered when logout
 */
public class LogoutEvent extends GwtEvent<LogoutEvent.LogoutEventHandler>{

	public interface LogoutEventHandler extends EventHandler{
		void onLogoutEvent(LogoutEvent event);
	}

	public final static Type<LogoutEventHandler> TYPE = new Type<LogoutEventHandler>();

	@Override
	public Type<LogoutEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogoutEventHandler handler) {
		handler.onLogoutEvent(this);
	}


}
