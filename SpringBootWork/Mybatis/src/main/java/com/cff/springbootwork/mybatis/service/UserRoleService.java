package com.cff.springbootwork.mybatis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cff.springbootwork.mybatis.dao.UserRoleMapper;
import com.cff.springbootwork.mybatis.domain.UserRole;

@Service
public class UserRoleService {
	@Autowired
	UserRoleMapper userRoleMapper;

	// @Autowired
	// UserRoleMapper userRoleMapper;

	public List<UserRole> selectAll() {
		return userRoleMapper.selectAll();
	}

	public int saveTest(UserRole userRole) {
		return userRoleMapper.saveTest(userRole);
	}

	public int update(Integer id, String phone) {
		return userRoleMapper.update(id, phone);
	}

	public int delete(Integer id) {
		return userRoleMapper.delete(id);
	}
}
