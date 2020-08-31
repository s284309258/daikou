package com.renegade.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Future;

import com.renegade.domain.HangSellOrderDO;
import com.renegade.domain.ResonancePoolDO;

import jodd.mutable.ValueProvider;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月19日
 * @version 1.0
 * @email 291312408@qq.com
 */
public interface TaskService {
	void regularBenefit();

	void regularOrderCancel();

	void regularOrderCancelSSS();

	void autoConfirmOrder();

	// 信仰者
	Future<Integer> benefitLevelOne(ResonancePoolDO id, String ordNo);

	// 幸运者
	Future<Integer> benefitLevelTwo(ResonancePoolDO id, String ordNo);

	// 裂变者
	Future<Integer> benefitLevelThress(ResonancePoolDO id, String ordNo);
	//支付订单的是匹配委托出售单返款
	void asynsRefund(Map<String, Object> map);
	//注册异步插入钱包
	 void insertWanlletManger(Integer userId);
	 //变为活跃用户
	 void becomeActive(Integer userId);

	void nameasynsRefund(HangSellOrderDO hangSellOrderDO, String orderNo, Integer userId) throws Exception;

}
