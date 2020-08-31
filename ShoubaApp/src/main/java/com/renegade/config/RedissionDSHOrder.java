package com.renegade.config;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 实现延时队列 All right reserved
 * @Created 2019年3月20日
 * @version 1.0
 * @email 291312408@qq.com
 */
public class RedissionDSHOrder {
	private String orderId;
	private long startTime;

	public RedissionDSHOrder() {

	}

	/**
	 * 订单ID
	 * 
	 * @param orderId
	 * @param timeout 订单超时多少秒
	 */
	public RedissionDSHOrder(String orderId, long timeout) {
		super();
		this.orderId = orderId;
//		this.startTime = System.currentTimeMillis()+timeout*1000L;//当前时间
		this.startTime = timeout;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "DSHOrder [orderId=" + orderId + ", startTime=" + startTime + "]";
	}

}
