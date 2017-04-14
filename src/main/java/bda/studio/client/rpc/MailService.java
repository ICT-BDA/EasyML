/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.rpc;

import bda.studio.shared.model.Account;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mailService")
public interface MailService extends RemoteService {

	Account VerifyEmail(Account account);
	String VerifyLink(String url);
	String SendMail(String base_url, Account account, String flag);

}
