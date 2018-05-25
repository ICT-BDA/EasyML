/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("aesservice")
public interface AesService extends RemoteService {
	
  /**
   * Encryption  use aes algorithom
   * 
   * @param contents  data list to encrypt
   * @return
   */
  List<String> aesEncrypt(List<String> contents);

}
