/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;

import eml.studio.shared.model.Account;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AccountServiceAsync {
	void getSize(Account account, AsyncCallback<Integer> callback);
	
	void findValid(Account account, int start, int size, AsyncCallback<List<Account>> callback);
	
	void getAccountInfo(Account account, AsyncCallback<Account> callback);
	
	void register(Account account, AsyncCallback<String> callback);

	void verify(String token, AsyncCallback<Account> callback);

	void login(Account account, AsyncCallback<Account> callback);

	void isLogin(AsyncCallback<Account> callback);

	void autoLogin(Account account, AsyncCallback<Account> asyncCallback);

	void logout(AsyncCallback<Void> callback);

	void modifyInfo(Account account, AsyncCallback<String> callback);
	
	void modifyPassword(String account, AsyncCallback<String> callback);
	
	void updatePower(Account account, AsyncCallback<Account> callback);
	
	void resetPassword(Account account, AsyncCallback<Account> callback);

	void deleteAccount(Account account, AsyncCallback<String> callback);
	
	void search(Account currentAccount, Account account, int limitStart, int limitSize, AsyncCallback<List<Account>> callback);
}
