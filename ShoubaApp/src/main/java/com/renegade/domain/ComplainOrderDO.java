package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 订单投诉列表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-21 10:14:01
 */
public class ComplainOrderDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Integer id;
	//投诉商家ID
	private Integer complainUser;
	//投诉内容
	private String complainContent;
	//投诉订单ID
	private String complainOrder;
	//投诉人ID
	private Integer userId;
	//创建时间
	private Date createTime;
	//投诉状态：0，未处理，1处理成功，2处理中
	private Integer complainStatus;

	/**
	 * 设置：ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：投诉商家ID
	 */
	public void setComplainUser(Integer complainUser) {
		this.complainUser = complainUser;
	}
	/**
	 * 获取：投诉商家ID
	 */
	public Integer getComplainUser() {
		return complainUser;
	}
	/**
	 * 设置：投诉内容
	 */
	public void setComplainContent(String complainContent) {
		this.complainContent = complainContent;
	}
	/**
	 * 获取：投诉内容
	 */
	public String getComplainContent() {
		return complainContent;
	}
	/**
	 * 设置：投诉订单ID
	 */
	public void setComplainOrder(String complainOrder) {
		this.complainOrder = complainOrder;
	}
	/**
	 * 获取：投诉订单ID
	 */
	public String getComplainOrder() {
		return complainOrder;
	}
	/**
	 * 设置：投诉人ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：投诉人ID
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：投诉状态：0，未处理，1处理成功，2处理中
	 */
	public void setComplainStatus(Integer complainStatus) {
		this.complainStatus = complainStatus;
	}
	/**
	 * 获取：投诉状态：0，未处理，1处理成功，2处理中
	 */
	public Integer getComplainStatus() {
		return complainStatus;
	}
}
