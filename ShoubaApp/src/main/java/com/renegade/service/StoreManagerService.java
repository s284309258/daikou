package com.renegade.service;
/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年6月14日 
*   @version  1.0
* @email 291312408@qq.com
*/

import com.renegade.config.AjaxJson;
import com.renegade.domain.ApplyRecordDO;

public interface StoreManagerService {
	AjaxJson participateDetail(ApplyRecordDO applyRecordDO);
	AjaxJson confirmOrder(String orderNo,Integer userId);

}
