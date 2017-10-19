/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface RegisterUIMessages extends Messages {

	String register();
	String registerUsr();
	String registerPwd();
	String registerVer();
	String registerCmp();
	String registerPst();

	String emailFormatErrorMsg();
	String emailHasRegisteredMsg();
	String verifyEmailErrMsg();
	String verifyEmailSuccessMsg();
	String registerSuccessMsg();
	String usernameExistMsg();
	String usernameEmptyMsg();
	String companyEmptyMsg();
	String positionEmptyMsg();
	String passwordEmptyMsg();
	String passwordLengthMsg();
	String passwordMismatchMsg();
	String otherErrMsg();

	String confirm();
}
