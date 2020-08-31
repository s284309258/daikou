package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-04-17 17:25:49
 */
public class ParamDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String paramCode;
	//
	private String paramName;
	//
	private String paramValue;
	//
	private Date craeteTime;
	//
	private Date updateTime;
	//
	private String autorUser;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	/**
	 * 获取：
	 */
	public String getParamCode() {
		return paramCode;
	}
	/**
	 * 设置：
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	/**
	 * 获取：
	 */
	public String getParamName() {
		return paramName;
	}
	/**
	 * 设置：
	 */
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	/**
	 * 获取：
	 */
	public String getParamValue() {
		return paramValue;
	}
	/**
	 * 设置：
	 */
	public void setCraeteTime(Date craeteTime) {
		this.craeteTime = craeteTime;
	}
	/**
	 * 获取：
	 */
	public Date getCraeteTime() {
		return craeteTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setAutorUser(String autorUser) {
		this.autorUser = autorUser;
	}
	/**
	 * 获取：
	 */
	public String getAutorUser() {
		return autorUser;
	}
}
