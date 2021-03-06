package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 资讯
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-25 16:36:07
 */
public class FrontInformationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//资讯编号
	private Integer noticeId;
	//资讯标题
	private String boticeTip;
	//资讯内容
	private String noticeDesc;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//搜索的开始时间
	private String startTime;
	//搜索的结束时间
	private String endTime;
	//浏览次数
	private Integer lookNum;
	//
	private String noticeInt;

	/**
	 * 设置：资讯编号
	 */
	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}
	/**
	 * 获取：资讯编号
	 */
	public Integer getNoticeId() {
		return noticeId;
	}
	/**
	 * 设置：资讯标题
	 */
	public void setBoticeTip(String boticeTip) {
		this.boticeTip = boticeTip;
	}
	/**
	 * 获取：资讯标题
	 */
	public String getBoticeTip() {
		return boticeTip;
	}
	/**
	 * 设置：资讯内容
	 */
	public void setNoticeDesc(String noticeDesc) {
		this.noticeDesc = noticeDesc;
	}
	/**
	 * 获取：资讯内容
	 */
	public String getNoticeDesc() {
		return noticeDesc;
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
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：搜索的开始时间
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：搜索的开始时间
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * 设置：搜索的结束时间
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：搜索的结束时间
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * 设置：浏览次数
	 */
	public void setLookNum(Integer lookNum) {
		this.lookNum = lookNum;
	}
	/**
	 * 获取：浏览次数
	 */
	public Integer getLookNum() {
		return lookNum;
	}
	/**
	 * 设置：
	 */
	public void setNoticeInt(String noticeInt) {
		this.noticeInt = noticeInt;
	}
	/**
	 * 获取：
	 */
	public String getNoticeInt() {
		return noticeInt;
	}
}
