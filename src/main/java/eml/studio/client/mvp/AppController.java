/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp;

import java.util.Random;
import java.util.logging.Logger;

import eml.studio.client.controller.LoginController;
import eml.studio.client.event.LoginEvent;
import eml.studio.client.event.LogoutEvent;
import eml.studio.client.event.ToMonitorEvent;
import eml.studio.client.mvp.presenter.AccountPresenter;
import eml.studio.client.mvp.presenter.AdminPresenter;
import eml.studio.client.mvp.presenter.MonitorPresenter;
import eml.studio.client.mvp.presenter.Presenter;
import eml.studio.client.mvp.presenter.RegisterPresenter;
import eml.studio.client.mvp.presenter.ResetpwdPresenter;
import eml.studio.client.mvp.view.AccountView;
import eml.studio.client.mvp.view.AdminView;
import eml.studio.client.mvp.view.MonitorView;
import eml.studio.client.mvp.view.RegisterView;
import eml.studio.client.mvp.view.ResetpwdView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * This Controller is used to control webpage's operations
 */
public class AppController implements ValueChangeHandler<String> {
	private static Logger logger = Logger.getLogger(AppController.class.getName());

	public static String email = null;
	public static String username = null;
	public static String verifylink = null;
	public static String power = null;
	private LoginController loginController;
	private HandlerManager eventBus;
	private HasWidgets container;

	public AppController(HandlerManager eventBus, LoginController loginController) {
		this.eventBus = eventBus;
		this.loginController = loginController;
		bind();
	}

	/**
	 * Event binding
	 */
	private void bind() {
		//Browser browser page token history (token is url # token)
		History.addValueChangeHandler(this);

		eventBus.addHandler(ToMonitorEvent.TYPE,
				new ToMonitorEvent.ToMonitorEventHandler() {

			@Override
			public void onToMonitorEvent(ToMonitorEvent event) {
				String msg = event.getMessage();
				if (msg != null && !"".equals(msg))
					History.newItem("monitor=" + msg);
				else
					History.newItem("monitor");
			}
		});

		eventBus.addHandler(LogoutEvent.TYPE,
				new LogoutEvent.LogoutEventHandler() {

			@Override
			public void onLogoutEvent(LogoutEvent event) {
				loginController.logout();
			}

		});

		eventBus.addHandler(LoginEvent.TYPE,
				new LoginEvent.LoginEventHandler() {

			@Override
			public void onLoginEvent(LoginEvent event) {
				handleToken(event.getMessage());
			}
		});
	}

	public void go(final HasWidgets container) {
		logger.info("app view going...");
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("monitor");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String token = event.getValue();
		logger.info("value change:" + token);

		loginController.go(token);
	}

	/** token is the url postfix after bdastudio, like designer or desigeer=jobid */
	private void handleToken(String token) {
		if (token != null) {
			Presenter presenter = null;

			if (token.startsWith("monitor")) {
				if(token.contains("&instance="))
				{
					String jobId = null;
					String oozieJobId = null;
					if(token.startsWith("monitor="))
					{
						logger.info("token = " + token);
						String[] ids = token.replaceFirst("monitor=", "").split("&instance=");
						jobId = ids[0];
						oozieJobId = ids[1];
						logger.info("jobId = "+jobId+"; oozieJobId = "+oozieJobId);
					}
					presenter = new MonitorPresenter(eventBus,new MonitorView(),jobId,oozieJobId);
				}
				else
				{
					String jobid = null;
					if (token.startsWith("monitor=")) {
						logger.info("token = " + token);
						jobid = token.replaceFirst("monitor=", "");
						logger.info("jobid = " + jobid);
					}
					presenter = new MonitorPresenter(eventBus, new MonitorView(), jobid);
				}
			}else if (token.startsWith("account")){
				presenter = new AccountPresenter(eventBus, new AccountView());
			} else if (token.startsWith("register")){
				presenter = new RegisterPresenter(eventBus, new RegisterView());
			} else if (token.startsWith("resetpwd")){
				presenter = new ResetpwdPresenter(eventBus, new ResetpwdView());
			} else if (token.startsWith("admin")){
				presenter = new AdminPresenter(eventBus, new AdminView());
			}

			if (presenter != null) {
				presenter.go(container);
			}
		}
	}

	/**
	 * Redirect to url
	 * @param url target url
	 */
	public static void redirect(String url) {
		String href = Window.Location.getHref();
		logger.info("[href]" + href);
		int splitIdx = href.lastIndexOf('/');
		String base_url = href.substring(0, splitIdx);
		logger.info("[base_url]" + base_url);
		Window.Location.replace(base_url + "/" + url);
	}

	/**
	 * Get now Url
	 * @return url string
	 */
	public static native String getUrl()/*-{
      return $wnd.location;
  }-*/;
	public static int random(int min, int max){
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
}
