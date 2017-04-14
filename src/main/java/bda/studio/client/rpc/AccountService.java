/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.rpc;

import java.util.List;

import bda.studio.shared.model.Account;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("accountservice")
public interface AccountService extends RemoteService {

  int getSize(Account account) throws Exception;

  // --------get all accounts ---------
  List<Account> findValid(Account account, int start, int size) throws Exception;
	
  // ------- get account informations ---------
  Account getAccountInfo(Account account);
	
  // ------ register --------------
  String register(Account account);

  Account verify(String token);

  // ------- login -------------
  Account login(Account account);

  Account isLogin();

  Account autoLogin(Account account);

  void logout();

  // ------- modify account -------------
  String modifyInfo(Account account);
  String modifyPassword(String account);
  Account updatePower(Account account);
  
  // ------- reset account password -----
  Account resetPassword(Account account);
	
  // --------- delete account ---------
  String deleteAccount(Account account);

}
