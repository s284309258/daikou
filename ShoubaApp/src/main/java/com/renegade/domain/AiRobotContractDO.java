package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 智能合约定期管理
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-18 21:30:58
 */
public class AiRobotContractDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Integer id;
	//用户ID
	private Integer userId;
	// 金额
	private BigDecimal money;
	//创建时间
	private Date createTime;
	//修改时间（用来判断是否到期，活期是小时精确度，定期是自然日）
	private Date updateTime;
	//是否已经被取出：0 否 1是
	private Integer status;
	//类型：1，30天，2，90天3，180天 4，360天
	private Integer type;
	private Integer oldType;
	//0：usdt，1： ut 2 ：vs
	private Integer kind;
	//预期收益
	private BigDecimal robotProfit;
	private BigDecimal totolProfit;
	//到期天数
	private Integer days;
	//剩余多少天复投
	private Integer days1;

	public Integer getDays1() {
		return days1;
	}
	public void setDays1(Integer days1) {
		this.days1 = days1;
	}
	public Integer getOldType() {
		return oldType;
	}
	public void setOldType(Integer oldType) {
		this.oldType = oldType;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public BigDecimal getTotolProfit() {
		return totolProfit;
	}
	public void setTotolProfit(BigDecimal totolProfit) {
		this.totolProfit = totolProfit;
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
	 * 设置： 金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取： 金额
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
	 * 设置：类型：1，30天，2，90天3，180天 4，360天
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型：1，30天，2，90天3，180天 4，360天
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：0：usdt，1： ut 2 ：vs
	 */
	public void setKind(Integer kind) {
		this.kind = kind;
	}
	/**
	 * 获取：0：usdt，1： ut 2 ：vs
	 */
	public Integer getKind() {
		return kind;
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
