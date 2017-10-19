/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import java.util.UUID;
import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.MailService;
import eml.studio.server.db.SecureDao;
import eml.studio.server.util.JavaMail;
import eml.studio.server.util.LinkUtils;
import eml.studio.server.util.TimeUtils;
import eml.studio.shared.model.Account;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.util.tools.shared.Md5Utils;

/**   
 * Specific methods in Mail service' related RemoteServiceServlet
 */
public class MailServiceImpl extends RemoteServiceServlet implements MailService {
	private static final long serialVersionUID = 1L;

	/**   
	 * Verify that the mailbox exists
	 *
	 * @param account
	 * @return boolean, format:true/false
	 * */
	@Override
	public Account VerifyEmail(Account account) {
		// TODO Auto-generated method stub
		try {
			return SecureDao.getObject( account );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Verify that the registration link is valid
	 *
	 * @param url
	 * @return boolean, format:true/false
	 */
	@Override
	public String VerifyLink(String url) {
		// TODO Auto-generated method stubx
		Account account = new Account();
		
		String arr[] = url.split("&");
		String tmp[] = arr[0].split("=");
		String url_email = tmp[1];
		
		account.setEmail(url_email);
			
		tmp = arr[1].split("=");
		String checkcode = tmp[1];
		
		return LinkUtils.verifyCheckcode(account, checkcode);
	}

	/**   
	 * Send email
	 *
	 * @param base_url
	 * @param account
	 * @param flag
	 * @return boolean, format:true/false
	 */
	@Override
	public String SendMail(String base_url, Account account, String flag){
		if(flag.equals("r")){
			try {
				Account new_account = new Account();
				String serialmd5 =  Md5Utils.getMd5Digest( UUID.randomUUID().toString().getBytes()).toString();
				String token = UUID.randomUUID().toString();
				
				new_account.setEmail(account.getEmail());
				new_account.setSerial( serialmd5 );
				new_account.setToken( token );
				new_account.setActivetime(TimeUtils.getTime());
				new_account.setCreatetime(TimeUtils.getTime());

				JavaMail mail = new JavaMail();
				String title = "Register-mail verification";
				String content = new_account.getEmail() + " Hello, <br>you are using this mailbox to register your EMLStudio account, "
						+ "Please click on the link for email verification and set the password and personal information after the verification.<br/>"
						+ "<a href='" + LinkUtils.activateLink(base_url, new_account) + "' onClick='" + "'>" + LinkUtils.activateLink(base_url, new_account) + "</a>";
				
				Account tmp = SecureDao.getObject(account);
				if(tmp != null){
					if(!tmp.getPassword().isEmpty()){
						return "email existed";
					}else{
						String[] setFields = { "serial", "token", "activetime", "createtime"};
					    String[] condFields = { "email" };
						boolean result = mail.sendMsg(new_account.getEmail(), title, content);
						if(result){
							SecureDao.update(new_account, setFields, condFields);
							return "success";
						}else{
							return "send email failed";
						}
					}
				}else{
					boolean result = mail.sendMsg(new_account.getEmail(), title, content);
					if(result){
						SecureDao.insert(new_account);
						return "success";
					}else{
						return "send email failed";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(flag.equals("m")){
			try {
				Account temp= SecureDao.getObject( account );
				if( temp != null && !temp.getPassword().isEmpty()){
					Account verify_account = new Account();
					String token = UUID.randomUUID().toString();
					verify_account.setEmail(temp.getEmail());
					verify_account.setSerial( temp.getSerial() );
					verify_account.setToken( token );
					
					JavaMail mail = new JavaMail();
					String title = "Reset password-mail verification";
					String content = verify_account.getEmail() + "Hello, <br>you are using this mailbox to reset your account password,"
							+ "Please click on the link for email verification and set your new password after after the verification.<br/>"
							+ "<a href='" + LinkUtils.resetpwdLink(base_url, verify_account) + "' onClick='" + "'>" + LinkUtils.resetpwdLink(base_url, verify_account) + "</a>";

					Account update_account = new Account();
					update_account.setEmail(temp.getEmail());
					update_account.setToken(token);
					update_account.setActivetime(TimeUtils.getTime());
					update_account.setVerifylink(" ");
					String[] setFields = {"token", "activetime", "verifylink"};
			        String[] condFields = {"email"};
			        SecureDao.update(update_account, setFields, condFields);
			        AppController.verifylink = null;
			        boolean result = mail.sendMsg(verify_account.getEmail(), title, content);
			        if(result){
			        	return "success";
			        }else
			        	return "send email failed";
        
				}else
					return "email doesn't exist";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
