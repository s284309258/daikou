package com.cff.springbootwork.mybatis.domain;

import java.io.Serializable;

public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String role;

	private String userName;

	private String phone;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserRole() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", role=" + role + ", userName=" + userName + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
