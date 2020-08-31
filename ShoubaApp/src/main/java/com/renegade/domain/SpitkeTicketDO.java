package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 抵扣券记录
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-18 14:40:30
 */
public class SpitkeTicketDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// id
	private Integer id;
	//商品ID
	private Integer goodsId;
	//用户ID
	private Integer userId;
	//抵扣券价值
	private String spikeTicket;
	//0：未使用，1使用
	private Integer status;
	//0，未排队，1排队
	private Integer state;
	//创建时间
	private Date createTime;

	/**
	 * 设置： id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取： id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：商品ID
	 */
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	/**
	 * 获取：商品ID
	 */
	public Integer getGoodsId() {
		return goodsId;
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
	 * 设置：抵扣券价值
	 */
	public void setSpikeTicket(String spikeTicket) {
		this.spikeTicket = spikeTicket;
	}
	/**
	 * 获取：抵扣券价值
	 */
	public String getSpikeTicket() {
		return spikeTicket;
	}
	/**
	 * 设置：0：未使用，1使用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：0：未使用，1使用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：0，未排队，1排队
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：0，未排队，1排队
	 */
	public Integer getState() {
		return state;
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
