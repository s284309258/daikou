package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 17:23:06
 */
public class ApplyRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Integer id;
	//用户ID
	private Integer userId;
	//申请扣除对应的usdt
	private BigDecimal applyMoney;
	//扣除对应的ut数量
	private BigDecimal applyMoneyUt;
	//身份证号码
	private String idcard;
	//手持身份证正面
	private String idcardPicture1;
	//手持身份证反面
	private String idcardPicture2;
	//状态：0待审核，1审核通过，2拒绝申请
	private Integer status;
	//审核不通过，拒接原因
	private String refuseReason;
	//创建时间
	private Date createTime;
    //店铺名称
	private String storeName;
	//店铺业务介绍
	private String storeMajor;
	//审核通过时间
	private Date checkTime;
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
	 * 设置：申请扣除对应的usdt
	 */
	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	/**
	 * 获取：申请扣除对应的usdt
	 */
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}
	/**
	 * 设置：扣除对应的ut数量
	 */
	public void setApplyMoneyUt(BigDecimal applyMoneyUt) {
		this.applyMoneyUt = applyMoneyUt;
	}
	/**
	 * 获取：扣除对应的ut数量
	 */
	public BigDecimal getApplyMoneyUt() {
		return applyMoneyUt;
	}
	/**
	 * 设置：身份证号码
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	/**
	 * 获取：身份证号码
	 */
	public String getIdcard() {
		return idcard;
	}
	/**
	 * 设置：手持身份证正面
	 */
	public void setIdcardPicture1(String idcardPicture1) {
		this.idcardPicture1 = idcardPicture1;
	}
	/**
	 * 获取：手持身份证正面
	 */
	public String getIdcardPicture1() {
		return idcardPicture1;
	}
	/**
	 * 设置：手持身份证反面
	 */
	public void setIdcardPicture2(String idcardPicture2) {
		this.idcardPicture2 = idcardPicture2;
	}
	/**
	 * 获取：手持身份证反面
	 */
	public String getIdcardPicture2() {
		return idcardPicture2;
	}
	/**
	 * 设置：状态：0待审核，1审核通过，2拒绝申请
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态：0待审核，1审核通过，2拒绝申请
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：审核不通过，拒接原因
	 */
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	/**
	 * 获取：审核不通过，拒接原因
	 */
	public String getRefuseReason() {
		return refuseReason;
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
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreMajor() {
		return storeMajor;
	}
	public void setStoreMajor(String storeMajor) {
		this.storeMajor = storeMajor;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
}
