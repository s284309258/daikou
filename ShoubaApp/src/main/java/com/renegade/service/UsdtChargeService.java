package com.renegade.service;

import java.math.BigDecimal;
import java.util.Map;

import com.renegade.config.AjaxJson;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月24日
 * @version 1.0
 * @email 291312408@qq.com
 */
public interface UsdtChargeService {

	// 插入提币日志
	AjaxJson inserSendLog(BigDecimal num, String sendAddress, int userId, String passWord, String type);

	// 会员升级
	void membershipUpgrade(Integer userId);

	AjaxJson transferAccountsUser(Map<String, Object> map, Integer userId);


	AjaxJson exchange(Integer userId, String num, String type, String passWord);

}
