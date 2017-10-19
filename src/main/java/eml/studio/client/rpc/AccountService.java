/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;

import eml.studio.shared.model.Account;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("accountservice")
public interface AccountService extends RemoteService {

	/**
	 * Get the quantity of all valid Accounts
	 *
	 * @param account target account
	 * @return size of target account
	 */
	int getSize(Account account) throws Exception;

	/**
	 * Get all valid Accounts from database(except current user & admin)
	 *
	 * @param account target account
	 * @param start start position
	 * @param size number of valid account
	 * @return list of target accounts
	 */
	List<Account> findValid(Account account, int start, int size) throws Exception;

	/**
	 * Get information of an account
	 *
	 * @param account target account
	 * @return account object with information
	 */
	Account getAccountInfo(Account account);

	/**
	 * Register an account
	 *
	 * @param account target account
	 * @return "[success]" + account.getUsername() + account.getEmail() or NULL
	 */
	String register(Account account);

	/**
	 * Verify a token
	 *
	 * @param verifytoken
	 * @return account object
	 */
	Account verify(String token);

	/**
	 * Login an account
	 * 
	 * @param account target account
	 * @return account object
	 */
	Account login(Account account);

	/**
	 * Which account is in the status of login
	 * 
	 * @return login account
	 */
	Account isLogin();

	/**
	 * Auto login service
	 * 
	 * @param account target account
	 * @return account after login
	 */
	Account autoLogin(Account account);

	/**
	 * Log out 
	 */
	void logout();

	/**
	 * Change the information of user
	 * 
	 * @param account
	 * @return status account
	 */
	String modifyInfo(Account account);
	
	/**
	 * Change the password of user
	 * 
	 * @param account format:"email password"
	 * @return status string, format: "[success] username email"
	 */
	String modifyPassword(String account);
	
	/**
	 * Change the power of user
	 * 
	 * @param account format:"email"
	 * @return status account 
	 */
	Account updatePower(Account account);

	/**
	 * Reset the password of user
	 * 
	 * @param account format:"email password"
	 * @return status string format:"[success] username email"
	 */
	Account resetPassword(Account account);

	/**
	 * Delete an account
	 *
	 * @param account target account
	 * @return "success" or "email doesn't exist"
	 */
	String deleteAccount(Account account);

	/**
	 * Search account 
	 * 
	 * @param currentAccount
	 * @param account
	 * @param limitStart
	 * @param limitSize
	 * @return
	 * @throws Exception
	 */
	List<Account> search(Account currentAccount, Account account, int limitStart, int limitSize) throws Exception;

}
