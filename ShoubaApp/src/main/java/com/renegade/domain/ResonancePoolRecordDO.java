package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 共振池用户投放记录
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-19 21:47:57
 */
public class ResonancePoolRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	private Integer id;
	// 共振池ID
	private Integer resonanceId;
	// 参与者ID
	private Integer userId;
	// 参与者投放的钱
	private BigDecimal money;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 是否结算：0 否，1是
	private Integer status;
	private Integer level1;
	private Integer level2;
	private Integer level3;

	public Integer getLevel1() {
		return level1;
	}

	public void setLevel1(Integer level1) {
		this.level1 = level1;
	}

	public Integer getLevel2() {
		return level2;
	}

	public void setLevel2(Integer level2) {
		this.level2 = level2;
	}

	public Integer getLevel3() {
		return level3;
	}

	public void setLevel3(Integer level3) {
		this.level3 = level3;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



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
	 * 设置：共振池ID
	 */
	public void setResonanceId(Integer resonanceId) {
		this.resonanceId = resonanceId;
	}

	/**
	 * 获取：共振池ID
	 */
	public Integer getResonanceId() {
		return resonanceId;
	}

	/**
	 * 设置：参与者ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * 获取：参与者ID
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 设置：参与者投放的钱
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	/**
	 * 获取：参与者投放的钱
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

}
