package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 转账记录
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 14:59:38
 */
public class TranferRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private String id;
	//转出用户
	private Integer userId;
	//转入用户
	private Integer toUserId;
	//金额
	private BigDecimal money;
	//类型：1 usdt 2 ut 3 vs
	private Integer type;
	//创建时间
	private Date createTime;

	/**
	 * 设置：主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：转出用户
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：转出用户
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：转入用户
	 */
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}
	/**
	 * 获取：转入用户
	 */
	public Integer getToUserId() {
		return toUserId;
	}
	/**
	 * 设置：金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：金额
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * 设置：类型：1 usdt 2 ut 3 vs
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型：1 usdt 2 ut 3 vs
	 */
	public Integer getType() {
		return type;
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
}
