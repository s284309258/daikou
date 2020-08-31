package com.renegade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月20日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Component
public class spikeTimeBetween {
	@Autowired
	private RenegadeConfig renegadeConfig;
	@Autowired
	private Environment env;

	/**
	 * 比较是否在时间段内
	 */
	public Boolean cBooleanTime(Long nowTIme, Long week) {
		// TODO Auto-generated method stub
		if (week < Long.valueOf(env.getProperty("renegade.startWeek").trim())
				|| (week > Long.valueOf(env.getProperty("renegade.endWeek").trim()))) {
			return false;
		}
		String string = renegadeConfig.getStartTimeAm().trim();
		String string2 = renegadeConfig.getEndTimeAm().trim();
		if ((nowTIme >= Long.valueOf(renegadeConfig.getStartTimeAm().trim())
				&& nowTIme <= Long.valueOf(renegadeConfig.getEndTimeAm().trim()))
				|| (nowTIme >= Long.valueOf(renegadeConfig.getStartTimePm().trim())
						&& nowTIme <= Long.valueOf(renegadeConfig.getEndTimePm().trim()))) {
			return true;

		}
		return false;
	}
}
