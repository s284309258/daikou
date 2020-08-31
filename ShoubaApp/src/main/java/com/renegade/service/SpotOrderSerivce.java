package com.renegade.service;

import java.util.Map;

import com.renegade.config.AjaxJson;
import com.renegade.domain.SpotGoodsDO;
import com.renegade.domain.SpotOrder;

/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年5月24日 
*   @version  1.0
* @email 291312408@qq.com
*/
public interface SpotOrderSerivce {
	AjaxJson registDeliveryScoreGoods(Map<String, Object> map);
	AjaxJson addSocreGoods(SpotGoodsDO spotGoodsDO);
	AjaxJson consignmentOrder(Map<String, Object> map);
	AjaxJson confirmDeliveryActiopn(Map<String, Object> map);
	//通过订单ID
	SpotOrder getOrderNo(String orderId);
	AjaxJson applyComplainOrder(Map<String, Object> map);
	//查询待发货订单
	int findWaitOrderById(Integer userId);
}

