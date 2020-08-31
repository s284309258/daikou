package com.cff.springbootwork.mybatis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cff.springbootwork.mybatis.dao.UserInfoDao;
import com.cff.springbootwork.mybatis.domain.UserInfo;

@Service
public class UserInfoService {
	@Autowired
	UserInfoDao userInfoDao;
	public UserInfo getUserInfoByUserName(String userName){
		return userInfoDao.findByUserName(userName);
	}
	
	public List<UserInfo> page(int page, int size){
		return userInfoDao.testPageSql(page, size);
	}
	
	public List<UserInfo> testTrimSql(UserInfo userInfo){
		return userInfoDao.testTrimSql(userInfo);
	}
}
