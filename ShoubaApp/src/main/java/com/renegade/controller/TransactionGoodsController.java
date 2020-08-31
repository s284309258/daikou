package com.renegade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renegade.annotations.RenegadeAuth;
import com.renegade.config.RedissionDSHOrder;
import com.renegade.service.RedissionDelayService;

/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年6月17日 
*   @version  1.0
* @email 291312408@qq.com
*/
@Controller
public class TransactionGoodsController {
	@Autowired
	private RedissionDelayService delayService;
	
	@GetMapping("test")
	@ResponseBody
	@RenegadeAuth
	public void name() {
		RedissionDSHOrder order=new RedissionDSHOrder("AMG1111",15L);
		delayService.add(order);
		RedissionDSHOrder orders=new RedissionDSHOrder("AMG222",30L);
		delayService.add(orders);
		RedissionDSHOrder orderss=new RedissionDSHOrder("AMG333",45L);
		delayService.add(orderss);
		RedissionDSHOrder AMG444=new RedissionDSHOrder("AMG444",60L);
		delayService.add(AMG444);
		return;
	}

}

