/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client;

import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.ui.panel.LoginPanel;
import eml.studio.client.ui.panel.RegisterPanel;
import eml.studio.client.ui.panel.ResetpwdPanel;
import eml.studio.client.util.Constants;
import eml.studio.client.util.Util;
import eml.studio.shared.model.Account;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The index page module of the project
 * 
 */
public class IndexPage implements EntryPoint {
	protected LoginPanel loginPanel = new LoginPanel();
	protected RegisterPanel registerPanel = new RegisterPanel();
	protected ResetpwdPanel forgetPwdPanel = new ResetpwdPanel();
	protected AccountServiceAsync accountSrv = GWT.create(AccountService.class);
	protected Anchor loginAnchor = null;

	/**
	 * IndexPage module load method
	 */
	@Override
	public void onModuleLoad() {
		init();
		loginPanel.addGuestLoginHandler(new GuestLoginHandler());
		loginPanel.addSignInHandler(new SignInHandler());
		loginPanel.addSignUpHandler(new SignUpHandler());
		loginPanel.addForgetPwdHandler(new ForgetPwdHandler());

		loginPanel.addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()== 13){
					enterLogin();
				}
			}
		}, KeyUpEvent.getType());

		Element elem = DOM.getElementById("bda-login-a");
		loginAnchor = Anchor.wrap(elem);
		loginAnchor.setText( Constants.logUIMsg.login());
		DOM.sinkEvents(elem, Event.ONCLICK);
		DOM.setEventListener(elem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					registerPanel.hide();
					forgetPwdPanel.hide();

					if (loginPanel.isShowing())
						loginPanel.hide();
					else {
						loginPanel.showRelativeTo(loginAnchor);
					}
				}
			}

		});

		elem = DOM.getElementById("bda-logout-a");
		elem.setInnerText( Constants.logUIMsg.logout());
		DOM.sinkEvents(elem, Event.ONCLICK);
		DOM.setEventListener(elem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					accountSrv.logout(new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.Location.reload();
						}

						@Override
						public void onSuccess(Void result) {
							Window.Location.reload();
						}

					});
				}
			}

		});

		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				if (loginPanel.isShowing())
					loginPanel.showRelativeTo(loginAnchor);
				if (registerPanel.isShowing())
					registerPanel.showRelativeTo(loginAnchor);
			}

		});

	}

	/**
	 * Init account roles 
	 */
	private void init() {
		accountSrv.isLogin(new AsyncCallback<Account>() {

			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get("bda-login").getElement().getStyle().setDisplay(Display.INLINE);
			}

			@Override
			public void onSuccess(Account result) {
				if (result != null) {
					if(result.getEmail() != "guest"){
						RootPanel.get("bda-logout").getElement().getStyle().setDisplay(Display.INLINE);
						RootPanel.get("bda-workstage").getElement().getStyle().setDisplay(Display.INLINE);
						if(result.getUsername() != null){
							RootPanel.get("bda-account").getElement().setInnerText(result.getUsername());
						}else{
							RootPanel.get("bda-account").getElement().setInnerText(result.getEmail());
						}
						if(result.getEmail().equals("admin") || result.getEmail().equals("guest")){
							RootPanel.get("bda-account").getElement().removeAttribute("href");
						}else{
							RootPanel.get("bda-account").getElement().setAttribute("href", "EMLStudio.html#account");
						}
					}else{
						RootPanel.get("bda-login").getElement().getStyle().setDisplay(Display.INLINE);
					}
				} else {
					RootPanel.get("bda-login").getElement().getStyle().setDisplay(Display.INLINE);
				}
			}
		});
	}

	/**
	 * Enter login action 
	 */
	public void enterLogin(){
		Account account = new Account();
		if (loginPanel.getAutoLogin()) {
			account.setEmail(loginPanel.getEmail());
			account.setSerial(Cookies.getCookie("bdaserial"));
			accountSrv.autoLogin(account, new AsyncCallback<Account>() {

				@Override
				public void onFailure(Throwable caught) {
					loginPanel.errorLabel.setText(Constants.logUIMsg.loginErrorMsg());
				}

				@Override
				public void onSuccess(Account account) {
					if (account != null) {
						loginPanel.hide();

						String refer = Window.Location.getParameter("url");
						if (refer == null)
							AppController.redirect("EMLStudio.html");
						else
							Window.Location.replace(refer);
					} else {
						loginPanel.errorLabel.setText(Constants.logUIMsg.loginWrongMsg());
					}
				}

			});

		} else {
			account.setEmail(loginPanel.getEmail());
			account.setPassword(loginPanel.getPassword());
			accountSrv.login(account, new AsyncCallback<Account>() {

				@Override
				public void onFailure(Throwable caught) {
					loginPanel.errorLabel.setText(Constants.logUIMsg.loginErrorMsg());
				}

				@Override
				public void onSuccess(Account account) {
					if (account != null) {
						Cookies.setCookie("bdaemail", account.getEmail(), Util.getCookieExpireDate());
						if (loginPanel.getRemmenber()) {
							Cookies.setCookie("bdaserial", account.getSerial(), Util.getCookieExpireDate());
						}
						loginPanel.hide();

						String refer = Window.Location.getParameter("url");
						if (refer == null)
							AppController.redirect("EMLStudio.html");
						else
							Window.Location.replace(refer);
					} else {
						loginPanel.errorLabel.setText(Constants.logUIMsg.loginWrongMsg());
					}
				}

			});
		}
	}

	class GuestLoginHandler implements ClickHandler {
		/**
		 * Guest login action
		 */
		public void login() {
			Account account = new Account();
			account.setEmail("guest");
			account.setPassword("123456");
			accountSrv.login(account, new AsyncCallback<Account>() {

				@Override
				public void onFailure(Throwable caught) {
					loginPanel.errorLabel.setText(Constants.logUIMsg.loginErrorMsg());
				}

				@Override
				public void onSuccess(Account account) {
					if (account != null) {
						loginPanel.hide();

						String refer = Window.Location.getParameter("url");
						if (refer == null)
							AppController.redirect("EMLStudio.html");
						else
							Window.Location.replace(refer);
					} else {
						loginPanel.errorLabel.setText(Constants.logUIMsg.loginWrongMsg());
					}
				}

			});
		}

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			login();
		}
	}

	class SignInHandler implements ClickHandler {
		/**
		 * Sign in login action
		 */
		public void login() {
			Account account = new Account();
			if (loginPanel.getAutoLogin()) {
				account.setEmail(loginPanel.getEmail());
				account.setSerial(Cookies.getCookie("bdaserial"));

				accountSrv.autoLogin(account, new AsyncCallback<Account>() {

					@Override
					public void onFailure(Throwable caught) {
						loginPanel.errorLabel.setText(Constants.logUIMsg.loginErrorMsg());
					}

					@Override
					public void onSuccess(Account account) {
						if (account != null) {
							loginPanel.hide();

							String refer = Window.Location.getParameter("url");
							if (refer == null)
								AppController.redirect("EMLStudio.html");
							else
								Window.Location.replace(refer);
						} else {
							loginPanel.errorLabel.setText(Constants.logUIMsg.loginWrongMsg());
						}
					}

				});

			} else {
				account.setEmail(loginPanel.getEmail());
				account.setPassword(loginPanel.getPassword());
				accountSrv.login(account, new AsyncCallback<Account>() {

					@Override
					public void onFailure(Throwable caught) {
						loginPanel.errorLabel.setText(Constants.logUIMsg.loginErrorMsg());
					}

					@Override
					public void onSuccess(Account account) {
						if (account != null) {
							Cookies.setCookie("bdaemail", account.getEmail(),Util.getCookieExpireDate());
							if (loginPanel.getRemmenber()) {
								Cookies.setCookie("bdaserial", account.getSerial(),
										Util.getCookieExpireDate());
							}
							loginPanel.hide();

							String refer = Window.Location.getParameter("url");
							if (refer == null)
								AppController.redirect("EMLStudio.html");
							else
								Window.Location.replace(refer);
						} else {
							loginPanel.errorLabel.setText(Constants.logUIMsg.loginWrongMsg());
						}
					}

				});
			}

		}

		/**
		 * Fired when the user clicks on the sendButton.
		 */
		@Override
		public void onClick(ClickEvent event) {
			login();
		}

	}

	class SignUpHandler implements ClickHandler {

		/**
		 * Fired when user clicks on sign up button
		 */
		@Override
		public void onClick(ClickEvent event) {
			loginPanel.hide();
			forgetPwdPanel.hide();

			if (registerPanel.isShowing())
				registerPanel.hide();
			else
				registerPanel.showRelativeTo(loginAnchor);
		}
	}

	class ForgetPwdHandler implements ClickHandler {  
		/**
		 * Fired when user click on forget  button
		 */
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			loginPanel.hide();
			registerPanel.hide();

			if (forgetPwdPanel.isShowing())
				forgetPwdPanel.hide();
			else
				forgetPwdPanel.showRelativeTo(loginAnchor);
		}
	}
}
