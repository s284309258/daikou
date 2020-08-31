package com.renegade.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.renegade.dao.ResonancePoolDao;
import com.renegade.domain.ResonancePoolDO;
import com.renegade.service.ParamService;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月20日
 * @version 1.0 系统启动监听初始化奖金池
 * @email 291312408@qq.com
 */
//@Configuration
public class InitializeListener implements ApplicationListener<ContextRefreshedEvent> {
	private final static Logger logger = LoggerFactory.getLogger(InitializeListener.class);

	// 初始化查询是否存在奖金池
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		logger.debug("=================系统监听启动");
	}
}
