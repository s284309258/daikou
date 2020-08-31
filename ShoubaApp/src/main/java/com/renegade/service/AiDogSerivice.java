package com.renegade.service;
/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年6月18日 
*   @version  1.0
* @email 291312408@qq.com
*/

import java.util.Map;

import com.renegade.config.AjaxJson;

public interface AiDogSerivice {
	AjaxJson aiDogMoveBrickStart(Map<String, Object> map);

	AjaxJson aKeyToStart(Map<String, Object> map);

	AjaxJson myActiceProfitOrder(Map<String, Object> map) throws Exception;

	AjaxJson smartContractToBuy(Map<String, Object> map);

	AjaxJson myContract(Integer userId) throws Exception;

	AjaxJson immediatelyRedemptive(Map<String, Object> map);

	AjaxJson confirmUpgrade(Map<String, Object> map) throws Exception;
}
