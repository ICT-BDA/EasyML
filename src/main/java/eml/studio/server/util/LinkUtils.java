/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import eml.studio.server.db.SecureDao;
import eml.studio.shared.model.Account;

/**
 * Email link generation tool
 * -- The mailbox verification link at the time of registration
 * -- The modification of password link
 */
public class LinkUtils {
	private static final String CHECK_CODE = "checkcode";  

	/**
	 * Generate a registered link
	 * @param base_url Link prefix
	 * @param account Account information
	 * @return url register url
	 */
	public static String activateLink(String base_url, Account account){
		String url = base_url + "/EMLStudio.html#verifyr?email="
				+ account.getEmail() + "&" + CHECK_CODE + "="
				+ checkCode(account);
		return url;
	}

	/**
	 * Generate the url of change Password
	 * @param base_url Link prefix
	 * @param account Account information
	 * @return url result url
	 */
	public static String resetpwdLink(String base_url, Account account){
		String url = base_url + "/EMLStudio.html#verifym?email="
				+ account.getEmail() + "&" + CHECK_CODE + "="
				+ checkCode(account);

		return url;
	}

	/**
	 * Generate check code（serial+token）
	 * @param account
	 * @return Encrypted check code
	 */
	public static String checkCode(Account account) {  
		return md5(account.getSerial() + ":" + account.getToken());  
	} 

	/**
	 * Link validity verification
	 * verify that the check code in the link is consistent with the check code at the time of transmission
	 * @param account
	 * @param checkcode
	 * @return boolean, true/false
	 */
	public static String verifyCheckcode(Account account, String checkcode) {  
		try {
			Account temp = SecureDao.getObject(account);
			if( temp != null ){
				Date begin_time = temp.getActivetime();
				Date end_time = TimeUtils.getTime();
				long between = end_time.getTime() - begin_time.getTime();
				long day = between/(24*60*60*1000);
				long hour = (between/(60*60*1000) - day*24);
				long minute = ((between/(60*1000)) - day*24*60 - hour*60);
				long second = (between/1000 - day*24*60*60 - hour*60*60 - minute*60);
				//The account.verifylink is verify link flag，ok：verification passed，other：first verification
				if (temp.getVerifylink()==null || !temp.getVerifylink().equals("ok")){				//Not ok: first verification
					if(second > 1800){									//Over valid date
						return "timed_out" + " " + temp.getEmail();	
					}else{												//Not over valid date
						account.setSerial(temp.getSerial());
						account.setToken(temp.getToken());
						if(checkCode(account).equals(checkcode)){		//Vertification code is correct, verification passed
							account.setVerifylink("ok");				//Flag is set ok，verification passed
							return "success" + " " + account.getUsername() + " " + account.getEmail() 
									+ " " + account.getSerial() + " " + account.getToken()
									+ " " + account.getVerifylink();
						}else											//Vertification code error,  verification failed
							return "useless_checkcode" + " " + temp.getEmail();
					}
				}else													//Not the first vertification, the link have been verified, invalid
					return "useless_link";
			}else														//The email in the verification link does not exist in the database, link is invalid
				return "useless_email";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	} 

	/**
	 * Use md5 method to encryption
	 * @param string input string
	 */
	private static String md5(String string) {  
		MessageDigest md = null;  
		try {  
			md = MessageDigest.getInstance("md5");  
			md.update(string.getBytes());  
			byte[] md5Bytes = md.digest();  
			return bytes2Hex(md5Bytes);  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		}  

		return null;  
	}

	/**
	 * Convert byteArray to String
	 * @param byteArray
	 * @return string of input byteArray
	 */
	private static String bytes2Hex(byte[] byteArray) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++)  {  
			if(byteArray[i] >= 0 && byteArray[i] < 16) {  
				strBuf.append("0");  
			}  
			strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));  
		}  
		return strBuf.toString();  
	}
}
