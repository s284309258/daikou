package com.cff.springbootwork.mybatis.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cff.springbootwork.mybatis.domain.UserInfo;
import com.cff.springbootwork.mybatis.domain.UserRole;
import com.cff.springbootwork.mybatis.service.UserInfoService;
import com.cff.springbootwork.mybatis.service.UserRoleService;

@RestController
@RequestMapping("/mybatis")
public class MybatisRest {

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	UserRoleService userRoleService;

	@RequestMapping(value = "/query")
	public List<UserRole> query() {
		return userRoleService.selectAll();
	}

	@RequestMapping(value = "/save")
	public Object save() {
		UserRole userRole = new UserRole();
		userRole.setRole("TEST");
		userRole.setUserName("TEST");
		userRole.setPhone("3424234");
		return userRoleService.saveTest(userRole);
	}

	@RequestMapping(value = "/update")
	public Object update() {
		return userRoleService.update(4, "454");
	}

	@RequestMapping(value = "/delete")
	public Object delete() {
		return userRoleService.delete(4);
	}

	@RequestMapping(value = "/mybatis/{name}", method = { RequestMethod.GET })
	public UserInfo testMybatis(@PathVariable("name") String name) {
		return userInfoService.getUserInfoByUserName(name);
	}

	@RequestMapping(value = "/page", method = { RequestMethod.GET })
	public List<UserInfo> page() {
		return userInfoService.page(1, 10);
	}
	
	@RequestMapping(value = "/trim", method = { RequestMethod.GET })
	public List<UserInfo> trim() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserType("0000");
		userInfo.setMobile("123");
		return userInfoService.testTrimSql(userInfo );
	}
}
