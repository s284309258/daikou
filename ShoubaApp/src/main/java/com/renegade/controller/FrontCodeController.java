package com.renegade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.renegade.annotations.RenegadeAuth;
import com.renegade.annotations.RenegadeAuthSign;
import com.renegade.config.AjaxJson;
import com.renegade.config.MailSendUtil;
import com.renegade.config.PropertyUtil;
import com.renegade.config.StringUtil;
import com.renegade.redis.RedisUtilCluster;
import com.renegade.service.SendCodeByPhoneService;
import com.sun.mail.handlers.message_rfc822;

@Controller
public class FrontCodeController  {
	@Autowired 
	private SendCodeByPhoneService sendCodeByPhoneServiceImpl;

	/**
	 * 注册发送验证码
	 * @param phone
	 * @param req
	 * @return
	 */
	@RequestMapping("/sendCode/getCode")
	@RenegadeAuth
	public @ResponseBody AjaxJson regiestSendCode(String phone,HttpServletRequest req){
		try{
			return sendCodeByPhoneServiceImpl.regiestSendCodeByPhone(phone, req);
		}catch(Exception e){
			AjaxJson ajaxJson = new AjaxJson();
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请求失败");
			return ajaxJson;
		}
	}
	
	/**
	 * 修改手机号发送验证码
	 * @param oldPhone
	 * @param newPhone
	 * @param req
	 * @return
	 */
	@RequestMapping("sendCode/updatePhoneSendCode")
	public @ResponseBody AjaxJson updatePhoneSendCode(String oldPhone,String newPhone,HttpServletRequest req){
		AjaxJson ajaxJson = new AjaxJson();
		try{
			HttpSession  session = req.getSession();
			if(session.getAttribute("userId")==null){
				ajaxJson.setMsg("loginfailure");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			int userId = (Integer) session.getAttribute("userId");
			return sendCodeByPhoneServiceImpl.updatePhoneSendCode(oldPhone, newPhone, userId);
		}catch(Exception e){
			System.out.println(e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请求失败");
			return ajaxJson;
		}
		
	}
	/**
	 * 修改登录密码发送验证码
	 * @param req
	 * @return
	 */
	@RequestMapping("sendCode/updateLoginPassSendCode")
	public @ResponseBody AjaxJson updateLoginPassSendCode(HttpServletRequest req){
		AjaxJson ajaxJson = new AjaxJson();
		try{
			HttpSession  session = req.getSession();
			if(session.getAttribute("userId")==null){
				ajaxJson.setMsg("loginfailure");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			int userId = (Integer) session.getAttribute("userId");
			return sendCodeByPhoneServiceImpl.updateLoginPassSendCode(userId);
		}catch(Exception e){
			System.out.println(e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请求失败");
			return ajaxJson;
		}
	}
		

	/**
	 * 发送修改支付密码验证码        
	 * @param req
	 * @return
	 */
	@RequestMapping("sendCode/updatePayPassSendCode")
	public @ResponseBody AjaxJson updatePayPassSendCode(HttpServletRequest req){
		AjaxJson ajaxJson = new AjaxJson();
		try{
			HttpSession  session = req.getSession();
			if(session.getAttribute("userId")==null){
				ajaxJson.setMsg("loginfailure");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			int userId = (Integer) session.getAttribute("userId");
			return sendCodeByPhoneServiceImpl.updatePayPassSendCode(userId,req);
		}catch(Exception e){
			System.out.println(e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请求失败");
			return ajaxJson;
		}
	}
	
	/**
	 * 发送忘记密码验证码        
	 * @param req
	 * @return
	 */
	@RequestMapping("sendCode/findPassSendCode")
	@RenegadeAuthSign
	@RenegadeAuth
	public @ResponseBody AjaxJson findPassSendCode(String phone){
		AjaxJson ajaxJson = new AjaxJson();
		//String type=getType();
		//System.out.println("type=========="+type);
		try{
			return sendCodeByPhoneServiceImpl.findPassSendCcde(phone);
		}catch(Exception e){
			System.out.println(e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请求失败");
			return ajaxJson;
		}
	}
	
	/**
	 * 转账发送验证码
	 * @param req
	 * @return
	 */
	@RequestMapping("sendCode/transferSendCode")
	public @ResponseBody AjaxJson transferSendCode(HttpServletRequest req){
		AjaxJson ajaxJson = new AjaxJson();
		try{
			HttpSession  session = req.getSession();
			if(session.getAttribute("userId")==null){
				ajaxJson.setMsg("loginfailure");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			int userId = Integer.parseInt(session.getAttribute("userId").toString());
			return sendCodeByPhoneServiceImpl.tranSendCode(userId);
		}catch(Exception e){
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请求失败");
			return ajaxJson;
		}
	}
}
