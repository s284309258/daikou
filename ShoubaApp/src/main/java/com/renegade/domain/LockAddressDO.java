package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 15:20:58
 */
public class LockAddressDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//地址
	private String starAddress;
	//星空来客人
	private Integer value;
	//0:未处理1；处理
	private String status;
	//星星密码
	private String starPass;
	//
	private Date createTime;

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：地址
	 */
	public void setStarAddress(String starAddress) {
		this.starAddress = starAddress;
	}
	/**
	 * 获取：地址
	 */
	public String getStarAddress() {
		return starAddress;
	}
	/**
	 * 设置：星空来客人
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	/**
	 * 获取：星空来客人
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * 设置：0:未处理1；处理
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：0:未处理1；处理
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：星星密码
	 */
	public void setStarPass(String starPass) {
		this.starPass = starPass;
	}
	/**
	 * 获取：星星密码
	 */
	public String getStarPass() {
		return starPass;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
