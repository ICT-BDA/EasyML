/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import bda.studio.server.db.SecureDao;
import bda.studio.shared.model.Account;

/**
 * 链接生成工具（注册时的邮箱验证链接、修改密码链接）
 * 
 * @author madongjing
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
		String url = base_url + "/BDAStudio.html#verifyr?email="
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
		String url = base_url + "/BDAStudio.html#verifym?email="
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
				//account.verifylink是验证链接标志位，ok：验证通过，其他：第一次验证
				if (!temp.getVerifylink().equals("ok")){				//非ok，即第一次验证
					if(second > 1800){									//超过过有效期
						return "timed_out" + " " + temp.getEmail();	
					}else{												//未过有效期
						account.setSerial(temp.getSerial());
						account.setToken(temp.getToken());
						if(checkCode(account).equals(checkcode)){		//验证码正确 即验证成功
							account.setVerifylink("ok");				//标志位设为ok，即验证通过
							return "success" + " " + account.getUsername() + " " + account.getEmail() 
									+ " " + account.getSerial() + " " + account.getToken()
									+ " " + account.getVerifylink();
						}else											//验证码错误 及验证失败
							return "useless_checkcode" + " " + temp.getEmail();
					}
				}else													//非第一次验证 即链接已经验证过 无效
					return "useless_link";
			}else														//验证链接中的邮箱不存在数据库中 即链接失效
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
