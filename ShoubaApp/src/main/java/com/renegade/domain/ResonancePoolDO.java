package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 共振奖池
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-19 21:47:25
 */
public class ResonancePoolDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Integer id;
	//创建时间
	private Date createTime;
	//共振状态：0，未满，1满
	private Integer resonanceStatus;
	//共振池中的总量
	private BigDecimal resonanceTotal;
	//共振池中的剩余可参与总量
	private BigDecimal resonanceRemainingTotal;
	

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
	 * 设置：共振状态：0，未满，1满
	 */
	public void setResonanceStatus(Integer resonanceStatus) {
		this.resonanceStatus = resonanceStatus;
	}
	/**
	 * 获取：共振状态：0，未满，1满
	 */
	public Integer getResonanceStatus() {
		return resonanceStatus;
	}
	/**
	 * 设置：共振池中的总量
	 */
	public void setResonanceTotal(BigDecimal resonanceTotal) {
		this.resonanceTotal = resonanceTotal;
	}
	/**
	 * 获取：共振池中的总量
	 */
	public BigDecimal getResonanceTotal() {
		return resonanceTotal;
	}
	/**
	 * 设置：共振池中的剩余可参与总量
	 */
	public void setResonanceRemainingTotal(BigDecimal resonanceRemainingTotal) {
		this.resonanceRemainingTotal = resonanceRemainingTotal;
	}
	/**
	 * 获取：共振池中的剩余可参与总量
	 */
	public BigDecimal getResonanceRemainingTotal() {
		return resonanceRemainingTotal;
	}
}
