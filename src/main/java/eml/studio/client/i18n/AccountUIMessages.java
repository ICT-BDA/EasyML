/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface AccountUIMessages extends Messages{

	//modify account information
	String information();
	String account();
	String username();
	String company();
	String position();
	String oldpwd();
	String newpwd();
	String verpwd();
	String infoSuccess();
	String infoExist();
	String infoFail();
	String usrEmpty();
	String cmpEmpty();
	String pstEmpty();
	//modify account password
	String pwdSuccess();
	String pwdFail();
	String pwdWrong();
	String pwdEmpty();
	String pwdLength();
	String pwdMatch1();
	String pwdMatch2();
	String confirm();
}
