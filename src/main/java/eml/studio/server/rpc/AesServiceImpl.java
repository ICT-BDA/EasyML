/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.rpc.AesService;
import eml.studio.shared.util.Aes;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * String Encryption and Decryption service implemention use aes algorithom
 *
 */
public class AesServiceImpl extends RemoteServiceServlet implements AesService {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AesServiceImpl.class.getName());
	
	@Override
	public List<String> aesEncrypt(List<String> contents) {
		// TODO Auto-generated method stub
		List<String> encryptConts = new ArrayList<String>();
		for(String content : contents)
		{
			String encryptCont = Aes.aesEncrypt(content);
			encryptConts.add(encryptCont);
		}
		return encryptConts;
	}


}
