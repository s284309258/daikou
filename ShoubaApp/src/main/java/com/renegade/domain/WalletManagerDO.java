package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用户的钱包管理
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 15:04:47
 */
public class WalletManagerDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//用户ID
	private Integer userId;
	//usdt总余额
	private BigDecimal usdtBalance;
	//usdt活期累积总收益
	private BigDecimal usdtDueBalance;
	//usdt定期累积总收益
	private BigDecimal usdtPeriodicBalance;
	//usdt动态累积总收益
	private BigDecimal usdtActiveBalance;
	//usdt冻结金额
	private BigDecimal usdtFreezeBalance;
	//ut总余额
	private BigDecimal utBalance;
	//ut活期累积总收益
	private BigDecimal utDueBalance;
	//ut定期累积总收益
	private BigDecimal utPeriodicBalance;
	//ut动态累积总收益
	private BigDecimal utActiveBalance;
	//ut冻结金额
	private BigDecimal utFreezeBalance;
	//vfs总余额（包括ut奖励和usdt换算之后的奖励）
	private BigDecimal vfsBalance;
	//vfs活期累积总收益
	private BigDecimal vfsDueBalance;
	//vfs定期累积总收益
	private BigDecimal vfsPeriodicBalance;
	//vfs动态累积总收益
	private BigDecimal vfsActiveBalance;
	//vfs冻结金额
	private BigDecimal vfsFreezeBalance;
	//修改时间
	private Date updateTime;
	//类型
	private String benefitType;
	//订单号
	private String orderNo;
	//资产变化的来源用户ID默认为0代表是系统
	private Integer sourceId;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
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
	 * 设置：usdt总余额
	 */
	public void setUsdtBalance(BigDecimal usdtBalance) {
		this.usdtBalance = usdtBalance;
	}
	/**
	 * 获取：usdt总余额
	 */
	public BigDecimal getUsdtBalance() {
		return usdtBalance;
	}
	/**
	 * 设置：usdt活期累积总收益
	 */
	public void setUsdtDueBalance(BigDecimal usdtDueBalance) {
		this.usdtDueBalance = usdtDueBalance;
	}
	/**
	 * 获取：usdt活期累积总收益
	 */
	public BigDecimal getUsdtDueBalance() {
		return usdtDueBalance;
	}
	/**
	 * 设置：usdt定期累积总收益
	 */
	public void setUsdtPeriodicBalance(BigDecimal usdtPeriodicBalance) {
		this.usdtPeriodicBalance = usdtPeriodicBalance;
	}
	/**
	 * 获取：usdt定期累积总收益
	 */
	public BigDecimal getUsdtPeriodicBalance() {
		return usdtPeriodicBalance;
	}
	/**
	 * 设置：usdt动态累积总收益
	 */
	public void setUsdtActiveBalance(BigDecimal usdtActiveBalance) {
		this.usdtActiveBalance = usdtActiveBalance;
	}
	/**
	 * 获取：usdt动态累积总收益
	 */
	public BigDecimal getUsdtActiveBalance() {
		return usdtActiveBalance;
	}
	/**
	 * 设置：usdt冻结金额
	 */
	public void setUsdtFreezeBalance(BigDecimal usdtFreezeBalance) {
		this.usdtFreezeBalance = usdtFreezeBalance;
	}
	/**
	 * 获取：usdt冻结金额
	 */
	public BigDecimal getUsdtFreezeBalance() {
		return usdtFreezeBalance;
	}
	/**
	 * 设置：ut总余额
	 */
	public void setUtBalance(BigDecimal utBalance) {
		this.utBalance = utBalance;
	}
	/**
	 * 获取：ut总余额
	 */
	public BigDecimal getUtBalance() {
		return utBalance;
	}
	/**
	 * 设置：ut活期累积总收益
	 */
	public void setUtDueBalance(BigDecimal utDueBalance) {
		this.utDueBalance = utDueBalance;
	}
	/**
	 * 获取：ut活期累积总收益
	 */
	public BigDecimal getUtDueBalance() {
		return utDueBalance;
	}
	/**
	 * 设置：ut定期累积总收益
	 */
	public void setUtPeriodicBalance(BigDecimal utPeriodicBalance) {
		this.utPeriodicBalance = utPeriodicBalance;
	}
	/**
	 * 获取：ut定期累积总收益
	 */
	public BigDecimal getUtPeriodicBalance() {
		return utPeriodicBalance;
	}
	/**
	 * 设置：ut动态累积总收益
	 */
	public void setUtActiveBalance(BigDecimal utActiveBalance) {
		this.utActiveBalance = utActiveBalance;
	}
	/**
	 * 获取：ut动态累积总收益
	 */
	public BigDecimal getUtActiveBalance() {
		return utActiveBalance;
	}
	/**
	 * 设置：ut冻结金额
	 */
	public void setUtFreezeBalance(BigDecimal utFreezeBalance) {
		this.utFreezeBalance = utFreezeBalance;
	}
	/**
	 * 获取：ut冻结金额
	 */
	public BigDecimal getUtFreezeBalance() {
		return utFreezeBalance;
	}
	/**
	 * 设置：vfs总余额（包括ut奖励和usdt换算之后的奖励）
	 */
	public void setVfsBalance(BigDecimal vfsBalance) {
		this.vfsBalance = vfsBalance;
	}
	/**
	 * 获取：vfs总余额（包括ut奖励和usdt换算之后的奖励）
	 */
	public BigDecimal getVfsBalance() {
		return vfsBalance;
	}
	/**
	 * 设置：vfs活期累积总收益
	 */
	public void setVfsDueBalance(BigDecimal vfsDueBalance) {
		this.vfsDueBalance = vfsDueBalance;
	}
	/**
	 * 获取：vfs活期累积总收益
	 */
	public BigDecimal getVfsDueBalance() {
		return vfsDueBalance;
	}
	/**
	 * 设置：vfs定期累积总收益
	 */
	public void setVfsPeriodicBalance(BigDecimal vfsPeriodicBalance) {
		this.vfsPeriodicBalance = vfsPeriodicBalance;
	}
	/**
	 * 获取：vfs定期累积总收益
	 */
	public BigDecimal getVfsPeriodicBalance() {
		return vfsPeriodicBalance;
	}
	/**
	 * 设置：vfs动态累积总收益
	 */
	public void setVfsActiveBalance(BigDecimal vfsActiveBalance) {
		this.vfsActiveBalance = vfsActiveBalance;
	}
	/**
	 * 获取：vfs动态累积总收益
	 */
	public BigDecimal getVfsActiveBalance() {
		return vfsActiveBalance;
	}
	/**
	 * 设置：vfs冻结金额
	 */
	public void setVfsFreezeBalance(BigDecimal vfsFreezeBalance) {
		this.vfsFreezeBalance = vfsFreezeBalance;
	}
	/**
	 * 获取：vfs冻结金额
	 */
	public BigDecimal getVfsFreezeBalance() {
		return vfsFreezeBalance;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：类型
	 */
	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}
	/**
	 * 获取：类型
	 */
	public String getBenefitType() {
		return benefitType;
	}
	/**
	 * 设置：订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：资产变化的来源用户ID默认为0代表是系统
	 */
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	/**
	 * 获取：资产变化的来源用户ID默认为0代表是系统
	 */
	public Integer getSourceId() {
		return sourceId;
	}
}
