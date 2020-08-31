package com.renegade.domain;

import java.math.BigDecimal;

/**
 * 提币记录
 * @author Administrator
 *
 */
public class FrontTiBiLogDo {
	
	private int sendId;
	private String sendNo;
	private String sendTime;
	private BigDecimal sendNum;
	private int sendStatus;
	private int userId;
	private String sendAddress;
	private int sendCheck;
	private BigDecimal charge;
	
	
	
	public BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}
	public int getSendId() {
		return sendId;
	}
	public void setSendId(int sendId) {
		this.sendId = sendId;
	}
	public String getSendNo() {
		return sendNo;
	}
	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public BigDecimal getSendNum() {
		return sendNum;
	}
	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}
	public int getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public int getSendCheck() {
		return sendCheck;
	}
	public void setSendCheck(int sendCheck) {
		this.sendCheck = sendCheck;
	}
	
	
}
