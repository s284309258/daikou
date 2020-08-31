package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 搬砖活期表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-18 21:32:13
 */
public class AiRobotMovesBricksDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Integer id;
	//用户ID
	private Integer userId;
	//金额
	private BigDecimal money;
	//创建时间
	private Date createTime;
	//修改时间（用来判断是否到期，活期是小时精确度，定期是自然日）
	private Date updateTime;
	//是否已经被取出：0 否 1是
	private Integer status;
	//是否启动计算利息：0否，1是
	private Integer state;
	//0：usdt 1： ut 2 vs
	private Integer type;
	//预期收益
	private BigDecimal robotProfit;
	private String timeCount;
	

	public String getTimeCount() {
		return timeCount;
	}
	public void setTimeCount(String timeCount) {
		this.timeCount = timeCount;
	}
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
	 * 设置：修改时间（用来判断是否到期，活期是小时精确度，定期是自然日）
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间（用来判断是否到期，活期是小时精确度，定期是自然日）
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：是否已经被取出：0 否 1是
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：是否已经被取出：0 否 1是
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：是否启动计算利息：0否，1是
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：是否启动计算利息：0否，1是
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：0：usdt 1： ut 2 vs
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：0：usdt 1： ut 2 vs
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：预期收益
	 */
	public void setRobotProfit(BigDecimal robotProfit) {
		this.robotProfit = robotProfit;
	}
	/**
	 * 获取：预期收益
	 */
	public BigDecimal getRobotProfit() {
		return robotProfit;
	}
}
