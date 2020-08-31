package com.cff.springbootwork.swagger.web;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cff.springbootwork.swagger.entity.TestEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"测试模块"}, produces = "application/json")
@RestController
@RequestMapping("/swagger")
public class SwaggerRest {
	
	@ApiOperation(value = "测试swagger的功能")
	@RequestMapping(value = "/test", method = { RequestMethod.GET })
	public TestEntity test(@RequestParam String reqType) {
		String uuid = UUID.randomUUID().toString();
		String welMsg = "welcome 程序猿";
		if (reqType != null && "1000".equals(reqType)) {
			welMsg = "welcome 程序媛";
		}
		TestEntity welEntity = new TestEntity();
		welEntity.setUuid(uuid);
		welEntity.setWelMsg(welMsg);
		return welEntity;
	}
	
	@RequestMapping(value = "/test2", method = { RequestMethod.GET })
	public TestEntity test2(@RequestParam String reqType) {
		String uuid = UUID.randomUUID().toString();
		String welMsg = "welcome 程序猿";
		if (reqType != null && "1000".equals(reqType)) {
			welMsg = "welcome 程序媛";
		}
		TestEntity welEntity = new TestEntity();
		welEntity.setUuid(uuid);
		welEntity.setWelMsg(welMsg);
		return welEntity;
	}
}
