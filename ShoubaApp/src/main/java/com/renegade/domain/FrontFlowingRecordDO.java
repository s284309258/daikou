package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用户流水表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 18:22:13
 */
public class FrontFlowingRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long flowingId;
	//流水号
	private String flowingNo;
	//资产变化前
	private BigDecimal assetsBefrom;
	//资产变化后
	private BigDecimal assetsAfter;
	//变化资产
	private BigDecimal assetsChange;
	//订单id
	private String orderNo;
	//1.提货积分收益，2，批发仓单收益，3积分提货消费
	private Integer type;
	//创建时间
	private Date createTime;
	//用户Id
	private Integer userId;
    //用户余额
	private BigDecimal user_banlance;
	//用户可交易余额
	private BigDecimal is_transaction_balance;
	//用户冻结交易余额
	private BigDecimal is_freetransaction_balance;
	//用户可提现余额
	private BigDecimal is_withdraw_balance;
	//用户冻结交易余额
	private BigDecimal is_freewithdraw_balance;
	
	
	/**
	 * 设置：
	 */
	public void setFlowingId(Long flowingId) {
		this.flowingId = flowingId;
	}
	/**
	 * 获取：
	 */
	public Long getFlowingId() {
		return flowingId;
	}
	/**
	 * 设置：流水号
	 */
	public void setFlowingNo(String flowingNo) {
		this.flowingNo = flowingNo;
	}
	/**
	 * 获取：流水号
	 */
	public String getFlowingNo() {
		return flowingNo;
	}
	/**
	 * 设置：资产变化前
	 */
	public void setAssetsBefrom(BigDecimal assetsBefrom) {
		this.assetsBefrom = assetsBefrom;
	}
	/**
	 * 获取：资产变化前
	 */
	public BigDecimal getAssetsBefrom() {
		return assetsBefrom;
	}
	/**
	 * 设置：资产变化后
	 */
	public void setAssetsAfter(BigDecimal assetsAfter) {
		this.assetsAfter = assetsAfter;
	}
	/**
	 * 获取：资产变化后
	 */
	public BigDecimal getAssetsAfter() {
		return assetsAfter;
	}
	/**
	 * 设置：变化资产
	 */
	public void setAssetsChange(BigDecimal assetsChange) {
		this.assetsChange = assetsChange;
	}
	/**
	 * 获取：变化资产
	 */
	public BigDecimal getAssetsChange() {
		return assetsChange;
	}
	/**
	 * 设置：订单id
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：订单id
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：1.提货积分收益，2，批发仓单收益，3积分提货消费
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：1.提货积分收益，2，批发仓单收益，3积分提货消费
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
	/**
	 * 设置：用户Id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户Id
	 */
	public Integer getUserId() {
		return userId;
	}
	public BigDecimal getUser_banlance() {
		return user_banlance;
	}
	public void setUser_banlance(BigDecimal user_banlance) {
		this.user_banlance = user_banlance;
	}
	public BigDecimal getIs_transaction_balance() {
		return is_transaction_balance;
	}
	public void setIs_transaction_balance(BigDecimal is_transaction_balance) {
		this.is_transaction_balance = is_transaction_balance;
	}
	public BigDecimal getIs_freetransaction_balance() {
		return is_freetransaction_balance;
	}
	public void setIs_freetransaction_balance(BigDecimal is_freetransaction_balance) {
		this.is_freetransaction_balance = is_freetransaction_balance;
	}
	public BigDecimal getIs_withdraw_balance() {
		return is_withdraw_balance;
	}
	public void setIs_withdraw_balance(BigDecimal is_withdraw_balance) {
		this.is_withdraw_balance = is_withdraw_balance;
	}
	public BigDecimal getIs_freewithdraw_balance() {
		return is_freewithdraw_balance;
	}
	public void setIs_freewithdraw_balance(BigDecimal is_freewithdraw_balance) {
		this.is_freewithdraw_balance = is_freewithdraw_balance;
	}
	
}
