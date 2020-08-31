package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 挂售订单列表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-17 21:51:50
 */
public class HangSellOrderDO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 挂售订单ID
	private String hangSellId;
	// 挂售用户ID
	private Integer hangSellUser;
	private Integer id;

	// 挂售商品ID
	private Integer hangSelllGoodsId;
	// 0：挂售中，1：挂售成功
	private Integer hangSellStatus;
	// 创建时间
	private Date createTime;
	private BigDecimal refoudPrice;

	public BigDecimal getRefoudPrice() {
		return refoudPrice;
	}

	public void setRefoudPrice(BigDecimal refoudPrice) {
		this.refoudPrice = refoudPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 设置：挂售订单ID
	 */
	public void setHangSellId(String hangSellId) {
		this.hangSellId = hangSellId;
	}

	/**
	 * 获取：挂售订单ID
	 */
	public String getHangSellId() {
		return hangSellId;
	}

	/**
	 * 设置：挂售用户ID
	 */
	public void setHangSellUser(Integer hangSellUser) {
		this.hangSellUser = hangSellUser;
	}

	/**
	 * 获取：挂售用户ID
	 */
	public Integer getHangSellUser() {
		return hangSellUser;
	}

	/**
	 * 设置：挂售商品ID
	 */
	public void setHangSelllGoodsId(Integer hangSelllGoodsId) {
		this.hangSelllGoodsId = hangSelllGoodsId;
	}

	/**
	 * 获取：挂售商品ID
	 */
	public Integer getHangSelllGoodsId() {
		return hangSelllGoodsId;
	}

	/**
	 * 设置：0：挂售中，1：挂售成功
	 */
	public void setHangSellStatus(Integer hangSellStatus) {
		this.hangSellStatus = hangSellStatus;
	}

	/**
	 * 获取：0：挂售中，1：挂售成功
	 */
	public Integer getHangSellStatus() {
		return hangSellStatus;
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
