package com.cff.springbootwork.web;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cff.springbootwork.dto.ResultModel;
import com.cff.springbootwork.web.entity.WelEntity;


@RestController
@RequestMapping("/pub")
public class WebRest {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/welCome")
	public ResultModel welCome(@RequestParam String reqType) {
		log.info("测试请求数据为：{}",reqType);
		String uuid = UUID.randomUUID().toString();
		String welMsg = "welcome 程序猿";
		if(reqType != null && "1000".equals(reqType)){
			welMsg = "welcome 程序媛";
		}
		WelEntity welEntity = new WelEntity();
		welEntity.setUuid(uuid);
		welEntity.setWelMsg(welMsg);
		return ResultModel.ok(welEntity);
	}
}
