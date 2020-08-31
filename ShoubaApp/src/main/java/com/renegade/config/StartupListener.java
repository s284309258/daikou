package com.renegade.config;

import com.renegade.service.RedissionDelayService;
import com.renegade.service.RedissionDelayService.ROnDelayedListener;
import com.renegade.service.impl.M9OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 //延时队列自动修改状态 All right reserved
 * @Created 2019年5月3日
 * @version 1.0
 * @email 291312408@qq.com
 */
//系统监听启动
@Configuration
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	private final static Logger logger=LoggerFactory.getLogger(StartupListener.class);
	@Autowired
	private RedissionDelayService delayService;

	@Autowired
	private M9OrderServiceImpl m9OrderServiceImpl;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {
		// TODO Auto-generated method stub
	  //可能存在2个容器！比如root为空的时候，这个容器才可以走
		if (evt.getApplicationContext().getParent() == null) {
			delayService.start(new ROnDelayedListener() {
				@Override
				public void onDelayedArrived(RedissionDSHOrder order) {
					// TODO Auto-generated method stub
					ThreadManager.getThreadPollProxy().execute(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							logger.debug("===>>>>>>>>>>>>延时队列到期处理开始");
							String orderId = order.getOrderId();
							m9OrderServiceImpl.taskHandle(orderId);
							logger.debug("===>>>>>>>>>>>>延时队列到期处理结束");
						}
					});

				}
			});
		}
	}

}
