/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface LogUIMessages extends Messages{

	String login();
	String logout();
	String register();
	String remember();
	String forget();

	String guest();
	String email();
	String password();

	String loginErrorMsg();
	String loginWrongMsg();

	String resetEmail();
	String resetSend();
	String resetCancel();

	String registerEmail();
	String registerVerify();
	String registerConfirm();
	String registerCancel();

}
