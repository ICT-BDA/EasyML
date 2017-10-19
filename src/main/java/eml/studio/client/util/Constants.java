/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.util;

import eml.studio.client.i18n.AccountUIMessages;
import eml.studio.client.i18n.AdminUIMessages;
import eml.studio.client.i18n.HeaderUIMessages;
import eml.studio.client.i18n.LogUIMessages;
import eml.studio.client.i18n.RegisterUIMessages;
import eml.studio.client.i18n.StudioUIMessages;
import eml.studio.client.i18n.ResetpwdUIMessages;

import com.google.gwt.core.shared.GWT;

public class Constants {

	/**********************************************
	 *********** CLIENT *****************
	 **********************************************/

	public static int CONTROLLER_WIDTH = 2400;
	public static int CONTROLLER_HEIGHT = 1200;
	public static StudioUIMessages studioUIMsg = (StudioUIMessages)GWT.create(StudioUIMessages.class);
	public static HeaderUIMessages headerUIMsg = (HeaderUIMessages)GWT.create(HeaderUIMessages.class);
	public static LogUIMessages logUIMsg = (LogUIMessages)GWT.create(LogUIMessages.class);
	public static RegisterUIMessages registerUIMsg = (RegisterUIMessages)GWT.create(RegisterUIMessages.class);
	public static ResetpwdUIMessages resetpwdUIMsg = (ResetpwdUIMessages)GWT.create(ResetpwdUIMessages.class);
	public static AdminUIMessages adminUIMsg = (AdminUIMessages)GWT.create(AdminUIMessages.class);
	public static AccountUIMessages accountUIMsg = (AccountUIMessages)GWT.create(AccountUIMessages.class);

}
