package com.renegade.service;

import javax.servlet.http.HttpServletRequest;

import com.renegade.config.AjaxJson;

public interface SendCodeByPhoneService  {
	
	/**
	 * 注册发送验证码
	 * @param phone
	 * @return
	 */
	public AjaxJson regiestSendCodeByPhone(String phone,HttpServletRequest req);
	
	/**
	 * 修改手机号发送验证码
	 * @param oldPhone
	 * @param newPhone
	 * @param userId
	 * @return
	 */
	public AjaxJson updatePhoneSendCode(String oldPhone,String newPhone,int userId);
	
	/**
	 * 修改登录密码发送验证码
	 * @param userId
	 * @return
	 */
	public AjaxJson updateLoginPassSendCode(int userId);
	
	/**
	 * 修改支付密码发送验证码
	 * @param userId
	 * @return
	 */
	public AjaxJson updatePayPassSendCode(int userId,HttpServletRequest req);
	
	/**
	 * 修改密码发送验证码
	 * @param phone
	 * @return
	 */
	public AjaxJson findPassSendCcde(String phone);
	
	/**
	 * 转账发送验证码
	 * @param userId
	 * @return
	 */
	public AjaxJson tranSendCode(int userId);

	
	
}
