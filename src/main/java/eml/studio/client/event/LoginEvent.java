/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The event that will be triggered when login
 */
public class LoginEvent extends GwtEvent<LoginEvent.LoginEventHandler>{

	public interface LoginEventHandler extends EventHandler{
		void onLoginEvent(LoginEvent event);
	}

	public final static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();

	private String message;

	public LoginEvent(String msg){
		this.message = msg;
	}

	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLoginEvent(this);
	}

	public String getMessage(){
		return message;
	}

}
