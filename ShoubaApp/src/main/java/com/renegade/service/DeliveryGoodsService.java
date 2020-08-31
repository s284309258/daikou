package com.renegade.service;
/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年5月22日 
*   @version  1.0
* @email 291312408@qq.com
*/

import java.util.Map;

import com.renegade.config.AjaxJson;

public interface DeliveryGoodsService {
//注册提交单
	AjaxJson registDeliveryScoreGoods(Map<String, Object> map);
	AjaxJson confirmDelivery(Map<String, Object> map);
	AjaxJson confirmBuyOrderss(Map<String, Object> map);
}
