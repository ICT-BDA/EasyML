/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import eml.studio.shared.model.Account;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mailService")
public interface MailService extends RemoteService {

	/**   
	 * Verify that the mailbox exists
	 *
	 * @param account
	 * @return boolean, format:true/false
	 * 
	 */
	Account VerifyEmail(Account account);
	
	/**
	 * Verify that the registration link is valid
	 *
	 * @param url
	 * @return boolean, format:true/false
	 */
	String VerifyLink(String url);
	
	/**   
	 * Send email
	 *
	 * @param base_url
	 * @param account
	 * @param flag
	 * @return boolean, format:true/false
	 */
	String SendMail(String base_url, Account account, String flag);

}
