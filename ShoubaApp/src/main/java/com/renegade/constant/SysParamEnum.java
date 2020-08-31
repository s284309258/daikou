package com.renegade.constant;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年9月19日
 * @version 1.0
 * @email 291312408@qq.com
 */
public enum SysParamEnum {
	/**
	 * 二维码邀请链接
	 */
//	qroderUrl(1, "http://m9.wgzvip.com//register?inviteCode=");
	qroderUrl(1, "https://www.etzcoin8.com//register?inviteCode=");
	
	String name;
	Integer key;

	SysParamEnum(Integer key, String name) {
		this.name = name;
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public static SysParamEnum getStatus(int key) {
		for (SysParamEnum sysParamEnums : values()) {
			if (key == sysParamEnums.key) {
				return sysParamEnums;
			}
		}
		return null;

	}
}
