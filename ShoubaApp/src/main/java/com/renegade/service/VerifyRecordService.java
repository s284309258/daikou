package com.renegade.service;

import com.renegade.config.AjaxJson;

public interface VerifyRecordService {
	/**
	 * 发送验证码
	 * @param phone 手机号/邮箱
	 * @param busType 业务类型
	 * @param accType 1手机 2邮箱
	 * @return
	 */
	AjaxJson addVerifyRecord(String phone,String busType,String accType);
	
	/**
	 * 对比验证码
	 * @param ajaxJson 
	 * @param user：用户
	 * @param busType：业务类型
	 * @param accType：获取类型（1：手机:2：邮箱）
	 * @param account：获取账号
	 * @return
	 */
	public AjaxJson compare(String id,String busType,String accType,String account,String code, AjaxJson ajaxJson);
		
	}

	

