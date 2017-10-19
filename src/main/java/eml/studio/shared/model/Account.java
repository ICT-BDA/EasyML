/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.model;

import java.util.Date;
import eml.studio.server.anotation.Password;
import eml.studio.server.anotation.TableField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Account object class in project
 */
public class Account implements IsSerializable {

	@TableField
	private String email;
	@TableField
	private String username;
	@TableField@Password
	private String password;
	@TableField
	private String verified;
	@TableField
	private Date createtime;
	@TableField
	private String power;
	/**
	 * Login sequence：a random number than has been overdone by MD5, and when the user modify password it will be reset each time
	 * In order for cookie stolen
	 * Note: not used at present time
	 */
	@TableField
	private String serial;

	/**
	 * Login token：a random number that has been hash by MD5，only be valid in one login session
	 * It will be reset when user login each time
	 * Corresponding cookie field "bdatoken"。
	 */
	@TableField
	private String token;

	/**
	 * Valid time：the valid time in the email link when user send email to register or modify password
	 */
	@TableField
	private Date activetime;
	@TableField
	private String company;
	@TableField
	private String position;
	@TableField
	private String verifylink;

	/** For deserialize */
	public Account(){}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getActivetime() {
		return activetime;
	}

	public void setActivetime(Date date) {
		this.activetime = date;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}  

	public String getVerifylink(){
		return verifylink;
	}

	public void setVerifylink(String verifylink){
		this.verifylink = verifylink;
	}

	public String getPower(){
		return power;
	}

	public void setPower(String power){
		this.power = power;
	}
}
