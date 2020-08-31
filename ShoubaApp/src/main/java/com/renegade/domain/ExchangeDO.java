package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 兑换表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-27 22:24:30
 */
public class ExchangeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//兑换订单号
	private String id;
	//用户ID
	private Integer userId;
	//兑换量
	private BigDecimal money;
	//
	private Date createTime;
	//1 usdt兑换vfs 2 ut兑换vfs
	private Integer type;

	/**
	 * 设置：兑换订单号
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：兑换订单号
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：兑换量
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：兑换量
	 */
	public BigDecimal getMoney() {
		return money;
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
	/**
	 * 设置：1 usdt兑换vfs 2 ut兑换vfs
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：1 usdt兑换vfs 2 ut兑换vfs
	 */
	public Integer getType() {
		return type;
	}
}
