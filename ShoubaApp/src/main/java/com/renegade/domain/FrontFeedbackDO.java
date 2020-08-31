package com.renegade.domain;

import java.io.Serializable;



/**
 * 
 * 
 * @author RenegadeNic
 * @email 291312408@qq.com
 * @date 2019-04-19 21:57:27
 */
public class FrontFeedbackDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//意见ID
	private Integer id;
	//反馈内容
	private String coontent;
	//反馈图片1
	private String photo1;
	//图片2
	private String photo2;
	//反馈图片3
	private String photo3;
	//用户ID
	private Integer userId;
	//用户电话
	private String userTel;
	//类型
	private String type;

	/**
	 * 设置：意见ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：意见ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：反馈内容
	 */
	public void setCoontent(String coontent) {
		this.coontent = coontent;
	}
	/**
	 * 获取：反馈内容
	 */
	public String getCoontent() {
		return coontent;
	}
	/**
	 * 设置：反馈图片1
	 */
	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}
	/**
	 * 获取：反馈图片1
	 */
	public String getPhoto1() {
		return photo1;
	}
	/**
	 * 设置：图片2
	 */
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	/**
	 * 获取：图片2
	 */
	public String getPhoto2() {
		return photo2;
	}
	/**
	 * 设置：反馈图片3
	 */
	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}
	/**
	 * 获取：反馈图片3
	 */
	public String getPhoto3() {
		return photo3;
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
	 * 设置：用户电话
	 */
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	/**
	 * 获取：用户电话
	 */
	public String getUserTel() {
		return userTel;
	}
	/**
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型
	 */
	public String getType() {
		return type;
	}
}
