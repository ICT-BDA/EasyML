/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import eml.studio.shared.model.Account;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailServiceAsync {

	void VerifyEmail(Account account, AsyncCallback<Account> callback);
	
	void VerifyLink(String url, AsyncCallback<String> callback);
	
	void SendMail(String base_url, Account account, String flag, AsyncCallback<String> asyncCallback);

}
