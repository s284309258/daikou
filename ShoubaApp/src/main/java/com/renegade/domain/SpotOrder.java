package com.renegade.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提货订单
 * @author Administrator
 *
 */
public class SpotOrder {
	
	private int deliveryOrderId;
	private String orderNo;
	private String goodsName;
	private BigDecimal goodsPrice;
	private int goodsNum;
	private String goodsImg;
	private int goodsId;
	private String orderStatus;
	private Date createTime;
	private Date payTime;
	private Date diliverTime;
	private Date singTime;
	private BigDecimal orderYuPay;
	private BigDecimal orderSjPay;
	private String receivingAddress;
	private String receivingName;
	private String receivedPhone;
	private String courierNo;
	private String courierName;
	private String userMark;
	private Integer orderUser;
	private Integer storeId;
	private Integer goodsState;
	
	public Integer getGoodsState() {
		return goodsState;
	}
	public void setGoodsState(Integer goodsState) {
		this.goodsState = goodsState;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getOrderUser() {
		return orderUser;
	}
	public void setOrderUser(Integer orderUser) {
		this.orderUser = orderUser;
	}
	public int getDeliveryOrderId() {
		return deliveryOrderId;
	}
	public void setDeliveryOrderId(int deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getDiliverTime() {
		return diliverTime;
	}
	public void setDiliverTime(Date diliverTime) {
		this.diliverTime = diliverTime;
	}
	public Date getSingTime() {
		return singTime;
	}
	public void setSingTime(Date singTime) {
		this.singTime = singTime;
	}
	public BigDecimal getOrderYuPay() {
		return orderYuPay;
	}
	public void setOrderYuPay(BigDecimal orderYuPay) {
		this.orderYuPay = orderYuPay;
	}
	public BigDecimal getOrderSjPay() {
		return orderSjPay;
	}
	public void setOrderSjPay(BigDecimal orderSjPay) {
		this.orderSjPay = orderSjPay;
	}
	public String getReceivingAddress() {
		return receivingAddress;
	}
	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}
	public String getReceivingName() {
		return receivingName;
	}
	public void setReceivingName(String receivingName) {
		this.receivingName = receivingName;
	}
	public String getReceivedPhone() {
		return receivedPhone;
	}
	public void setReceivedPhone(String receivedPhone) {
		this.receivedPhone = receivedPhone;
	}
	public String getCourierNo() {
		return courierNo;
	}
	public void setCourierNo(String courierNo) {
		this.courierNo = courierNo;
	}
	public String getCourierName() {
		return courierName;
	}
	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}
	public String getUserMark() {
		return userMark;
	}
	public void setUserMark(String userMark) {
		this.userMark = userMark;
	}
	
	
	
}
