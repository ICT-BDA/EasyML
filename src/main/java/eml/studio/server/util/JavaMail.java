/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import eml.studio.server.constant.Constants;

/**
 * Use Java method to operate MailBox
 */
public class JavaMail {

	private Properties props;

	public JavaMail() {
		props = new Properties();
		props.setProperty("mail.debug", "true");					// Enable debug debugging
		props.setProperty("mail.smtp.auth", "true");				// The sending server requires authentication
		props.setProperty("mail.host", Constants.MAIL_HOST);	 	// Set the mail server host name
		props.setProperty("mail.transport.protocol", "smtp");		// Send mail protocol name
		props.setProperty("mail.smtp.auth.mechanisms", "login");

	}

	public boolean sendMsg(String recipient, String subject, String content)
			throws MessagingException {
		// Create a mail object
		Session session = Session.getInstance(props, new Authenticator() {
			// Set the account information session，transport will send mail
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD);
			}
		});
		session.setDebug(true);
		Message msg = new MimeMessage(session);
		try {
			msg.setSubject(subject);			//Set the mail subject
			msg.setContent(content,"text/html;charset=utf-8");
			msg.setFrom(new InternetAddress(Constants.MAIL_USERNAME));			//Set the sender
			msg.setRecipient(RecipientType.TO, new InternetAddress(recipient));	//Set the recipient
			Transport.send(msg);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			return false;
		}

	}
}
