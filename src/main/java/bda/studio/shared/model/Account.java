/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.shared.model;

import java.util.Date;
import bda.studio.server.anotation.Password;
import bda.studio.server.anotation.TableField;
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
   * 登录序列：一个被MD5散列过的随机数， 用户每次修改密码会重新设置
   * 为了方式cookie被盗用。
   * Note: 目前暂时不用
   */
  @TableField
  private String serial;

  /**
   * 登录token：一个被MD5散列过的随机数，仅一个登录session内有效，
   * 用户每次登陆会重新设置。
   * 对应cooke字段"bdatoken"。
   */
  @TableField
  private String token;
  
  /**
   * 有效时间：用户通过发送邮件方式注册/修改密码，邮件中链接的有效时间
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
