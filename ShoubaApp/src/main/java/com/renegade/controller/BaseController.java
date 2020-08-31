package com.renegade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.renegade.config.AjaxJson;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年4月17日
 * @version 1.0
 * @email 291312408@qq.com
 */
public class BaseController {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	String type;

	/** ModelAttribute是初始化 
	 * 代表共享的session全局变量
	 * */ 
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		if (session.getAttribute("type") == null) {
			type = "CN";
		} else {
			type = session.getAttribute("type").toString();
		}
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
