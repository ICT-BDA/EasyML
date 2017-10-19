/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import java.util.List;
import java.util.UUID;
import eml.studio.client.rpc.AccountService;
import eml.studio.server.db.BaseDao;
import eml.studio.server.db.SecureDao;
import eml.studio.server.util.TimeUtils;
import eml.studio.shared.model.Account;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.util.tools.shared.Md5Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Specific methods in Account and login related RemoteServiceServlet
 */
public class AccountServiceImpl extends RemoteServiceServlet implements AccountService {
	private static final long serialVersionUID = 1L;

	/**
	 * Get the quantity of all valid Accounts
	 *
	 * @param account target account
	 * @return size of target account
	 */
	@Override
	public int getSize(Account account) throws Exception {
		int size = 0;
		List<Account> accounts;
		try {
			Account query = new Account();
			accounts = SecureDao.listAll(query,
					"and email != '" + account.getEmail() + "' and email != 'admin' and email != 'guest' "
							+ "and password != '' order by createtime desc");

			size = accounts.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return size;
	}

	/**
	 * Get all valid Accounts from database(except current user & admin)
	 *
	 * @param account target account
	 * @param start start position
	 * @param size number of valid account
	 * @return list of target accounts
	 */
	@Override
	public List<Account> findValid(Account account, int start, int size) throws Exception{
		List<Account> accounts = null;
		try{
			Account query = new Account();
			accounts = SecureDao.listAll(query, "and email != '" + account.getEmail() + "' "
					+ "and email != 'admin' and email != 'guest' and password != '' "
					+ "order by createtime desc limit " + start + ", " + size);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return accounts;
	}

	/**
	 * Get information of an account
	 *
	 * @param account target account
	 * @return account object with information
	 */
	@Override
	public Account getAccountInfo(Account account) {
		Account tmp = new Account();
		tmp.setEmail(account.getEmail());
		try {
			Account query = tmp;
			tmp = SecureDao.getObject(query);
			if (tmp != null) {
				return tmp;
			} else
				return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Register an account
	 *
	 * @param account target account
	 * @return "[success]" + account.getUsername() + account.getEmail() or NULL
	 */
	@Override
	public String register(Account account) {
		try {
			Account un_tmp = new Account();
			un_tmp.setUsername(account.getUsername());
			if (SecureDao.getObject(un_tmp) != null) {
				return "username error";
			} else {
				account.setPassword(BaseDao.password(account.getPassword()));
				account.setCreatetime(TimeUtils.getTime());
				String serialmd5 = Md5Utils.getMd5Digest(UUID.randomUUID().toString().getBytes()).toString();
				account.setSerial(serialmd5);
				String[] setFields = { "username", "password", "createtime", "serial", "company",
						"position", "verifylink", "power"};
				String[] condFields = { "email" };
				SecureDao.update(account, setFields, condFields);
				return "[success]" + " " + " " + account.getUsername()
						+ account.getEmail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Delete an account
	 *
	 * @param account target account
	 * @return "success" or "email doesn't exist"
	 */
	@Override
	public String deleteAccount(Account account) {
		Account tmp = new Account();
		tmp.setEmail(account.getEmail());
		Account deleteAccount;
		try {
			deleteAccount = SecureDao.getObject(tmp);
			if (deleteAccount != null) {
				SecureDao.delete(tmp);
				return "success";
			} else
				return "email doesn't exist";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "delete failed";
	}
	/**
	 * Verify a token
	 *
	 * @param verifytoken
	 * @return account object
	 */
	@Override
	public Account verify(String verifytoken) {
		Account account = new Account();
		account.setVerified(verifytoken);
		try {
			account = SecureDao.getObject(account);
			if (account != null) {
				account.setVerified("verified");
				String[] setFields = { "verified" };
				String[] condFields = { "email" };
				SecureDao.update(account, setFields, condFields);
				return account;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Which account is in the status of login
	 * @return login account
	 */
	@Override
	public Account isLogin() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		Object email = session.getAttribute("email");
		Object serial = session.getAttribute("serial");			//Assign a serial when user modify password each time
		if (email == null || serial == null) {
			return null;
		}

		Account tmp = new Account();
		tmp.setEmail(email.toString());
		tmp.setSerial(serial.toString());
		try {
			Account query = tmp;
			tmp = SecureDao.getObject(query);
			if (tmp != null) {
				return tmp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Set session time in HttpServletRequest
	 */
	public void setSessionExpireTime() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60 * 60 * 12);  			// expired after 12 days
	}

	/**
	 * Login an account
	 * @param account target account
	 * @return account object
	 */
	@Override
	public Account login(Account account) {
		setSessionExpireTime();

		if (account.getEmail() == null) {
			account.setEmail("");
		}

		if (account.getPassword() == null) {
			account.setPassword(BaseDao.password(""));
		} else {
			account.setPassword(BaseDao.password(account.getPassword()));
		}

		try {
			Account query = account ;
			account = SecureDao.getObject( query );
			if ( account  != null) {

				if (account.getSerial() == null || account.getSerial().length() == 0){
					String serialmd5 = Md5Utils.getMd5Digest(
							UUID.randomUUID().toString().getBytes()).toString();
					account.setSerial(serialmd5);
					String[] setFields = { "serial"};
					String[] condFields = { "email" };
					SecureDao.update(account, setFields, condFields);
				}

				HttpServletRequest request = this.getThreadLocalRequest();
				HttpSession session = request.getSession();
				session.setAttribute("username", account.getUsername());
				session.setAttribute("email", account.getEmail());
				session.setAttribute("serial", account.getSerial());
				return account;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Auto login service
	 * @param account target account
	 * @return account after login
	 */
	@Override
	public Account autoLogin(Account account) {
		setSessionExpireTime();

		if (account.getEmail() == null) {
			account.setEmail("");
		}
		if (account.getSerial() == null) {
			account.setSerial("");
		}

		try {
			Account query = account;
			account = SecureDao.getObject( query );
			if( account != null){
				HttpServletRequest request = this.getThreadLocalRequest();
				HttpSession session = request.getSession();
				session.setAttribute("username", account.getUsername());
				session.setAttribute("email", account.getEmail());
				session.setAttribute("serial", account.getSerial());
				System.out.println("login success, email=" + account.getEmail()
						+ ", serial=" + account.getSerial());
				return account;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Log out
	 */
	@Override
	public void logout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		session.removeAttribute("email");
		session.removeAttribute("serial");
	}

	/**
	 * Reset the password of user
	 * 
	 * @param account format:"email password"
	 * @return status string format:"[success] username email"
	 */
	@Override
	public Account resetPassword(Account account) {
		Account tmp = new Account();
		tmp.setEmail(account.getEmail());
		try {
			Account query = tmp;
			tmp = SecureDao.getObject(query);
			if (tmp != null) {
				Account new_account = new Account();
				new_account.setEmail(tmp.getEmail());
				new_account.setPassword(account.getPassword());
				String serialmd5 = Md5Utils.getMd5Digest(UUID.randomUUID().toString().getBytes()).toString();
				new_account.setSerial(serialmd5);
				new_account.setVerifylink(account.getVerifylink());
				String[] setFields = { "password", "serial", "verifylink" };
				String[] condFields = { "email" };
				SecureDao.update(new_account, setFields, condFields);

				return new_account;

			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Change the information of user
	 * 
	 * @param account
	 * @return status account
	 */
	@Override
	public String modifyInfo(Account account) {
		Account un = new Account();
		un.setUsername(account.getUsername());
		try {
			List<Account> list = SecureDao.listAll(un, "and email != '"+ account.getEmail() +"'");
			if(list.size() > 0){
				return "username existed";
			} else {
				Account tmp = new Account();
				tmp.setEmail(account.getEmail());
				if (SecureDao.getObject(tmp) != null) {
					String[] setFields = { "username", "company", "position" };
					String[] condFields = { "email" };
					SecureDao.update(account, setFields, condFields);
					return "success" + " " + account.getUsername();
				} else
					return "email doesn't exist";
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * Change the password of user
	 * 
	 * @param account format:"email password"
	 * @return status string, format: "[success] username email"
	 */
	@Override
	public String modifyPassword(String account) {
		String arr[] = account.split(" ");
		Account tmp = new Account();
		tmp.setEmail(arr[0]);
		tmp.setPassword(arr[1]);

		try {
			Account query = tmp;
			tmp = SecureDao.getObject(query);
			if (tmp != null) {
				Account new_account = new Account();
				new_account.setEmail(tmp.getEmail());
				new_account.setPassword(arr[2]);
				String serialmd5 = Md5Utils.getMd5Digest(UUID.randomUUID().toString().getBytes()).toString();
				new_account.setSerial(serialmd5);
				String[] setFields = { "password", "serial" };
				String[] condFields = { "email" };
				SecureDao.update(new_account, setFields, condFields);

				return "[success]" + " " + new_account.getUsername() + " " + new_account.getEmail();

			} else {
				return "wrong old password"; 						// Wrong email
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "new password failed"; 							// Password modification failed
	}

	/**
	 * Change the power of user
	 * 
	 * @param account format:"email"
	 * @return status account 
	 */
	@Override
	public Account updatePower(Account account) {
		Account tmp = new Account();
		tmp.setEmail(account.getEmail());
		try {
			if(SecureDao.getObject(tmp) != null){
				String[] setFields = { "power"};
				String[] condFields = { "email" };
				SecureDao.update(account, setFields, condFields);
				Account query = account;
				account = SecureDao.getObject(query);
				if(account != null){
					return account;
				}else
					return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public List<Account> search(Account currentAccount, Account account, int limitStart, int limitSize) throws Exception {
		List<Account> accounts = null;
		String sql = "";
		try {
			Account query = new Account();
			if(account.getEmail() != null ){
				sql = sql + "and email like '%" + account.getEmail() + "%' ";
			}
			if(account.getUsername() != null){
				sql = sql + "and username like '%" + account.getUsername() + "%' ";
			}
			if(account.getCompany() != null){
				sql = sql + "and company like '%" + account.getCompany() + "%' ";
			}
			if(account.getPosition() != null){
				sql = sql + "and position like '%" + account.getPosition() + "%' ";
			}
			if(account.getPower() != null){
				switch(Integer.parseInt(account.getPower())){
				case 1:
					sql = sql + "and power like '1%' ";
					break;
				case 2:
					sql = sql + "and power like '%1%' ";
					break;
				case 3:
					sql = sql + "and power like '%1' ";
					break;
				}
			}
			sql = sql + "and email != '" + currentAccount.getEmail() + "' "
					+ "and email != 'admin' and email != 'guest' and password != '' order by createtime desc";
			if(limitStart != 0 && limitSize != 0){
				sql = sql + " limit " + limitStart + "," + limitSize;
			}
			accounts = SecureDao.listAll( query, sql );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return accounts;
	}
}
