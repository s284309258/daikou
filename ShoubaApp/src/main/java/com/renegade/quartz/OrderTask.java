package com.renegade.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.renegade.service.TaskService;
import com.renegade.service.impl.shareProfitServiceImpl;

/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年6月19日 
*   @version  1.0
* @email 291312408@qq.com
*/
@Component
public class OrderTask {
	@Autowired
	private TaskService taskServiceImpl;
	@Autowired
	private shareProfitServiceImpl shareProfitServiceImpl;
   //超过七天未收货的订单需要确认收货
	@Scheduled(cron = "0 15 0 * * ?")
//	@Scheduled(cron = "0/5 * * * * ? ")
	public void autoConfirmOrder() {// 超过七天未确认收货的，自动确认收货，并且返钱给商家
		taskServiceImpl.autoConfirmOrder();
	}
}

