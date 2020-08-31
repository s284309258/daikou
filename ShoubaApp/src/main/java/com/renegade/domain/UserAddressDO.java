package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 15:45:09
 */
public class UserAddressDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer addressId;
	//收货省份
	private String addressProvince;
	//收货城市
	private String addressCity;
	//详细地址
	private String addressDetailed;
	//用户id
	private Integer userId;
	//区域
	private String addressArea;
	//地址电话
	private String addressPhone;
	//收货人姓名
	private String addressUserName;
	//0不是默认 1默认地址
	private Integer addressState;
	//0未删除  1已删除
	private Integer addressDorp;
	//省编码
	private String provinceCode;
	//城市编码
	private String cityCode;
	//区编码
	private String areaCode;
	//
	private String addressUserNameEn;

	/**
	 * 设置：
	 */
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	/**
	 * 获取：
	 */
	public Integer getAddressId() {
		return addressId;
	}
	/**
	 * 设置：收货省份
	 */
	public void setAddressProvince(String addressProvince) {
		this.addressProvince = addressProvince;
	}
	/**
	 * 获取：收货省份
	 */
	public String getAddressProvince() {
		return addressProvince;
	}
	/**
	 * 设置：收货城市
	 */
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	/**
	 * 获取：收货城市
	 */
	public String getAddressCity() {
		return addressCity;
	}
	/**
	 * 设置：详细地址
	 */
	public void setAddressDetailed(String addressDetailed) {
		this.addressDetailed = addressDetailed;
	}
	/**
	 * 获取：详细地址
	 */
	public String getAddressDetailed() {
		return addressDetailed;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：区域
	 */
	public void setAddressArea(String addressArea) {
		this.addressArea = addressArea;
	}
	/**
	 * 获取：区域
	 */
	public String getAddressArea() {
		return addressArea;
	}
	/**
	 * 设置：地址电话
	 */
	public void setAddressPhone(String addressPhone) {
		this.addressPhone = addressPhone;
	}
	/**
	 * 获取：地址电话
	 */
	public String getAddressPhone() {
		return addressPhone;
	}
	/**
	 * 设置：收货人姓名
	 */
	public void setAddressUserName(String addressUserName) {
		this.addressUserName = addressUserName;
	}
	/**
	 * 获取：收货人姓名
	 */
	public String getAddressUserName() {
		return addressUserName;
	}
	/**
	 * 设置：0不是默认 1默认地址
	 */
	public void setAddressState(Integer addressState) {
		this.addressState = addressState;
	}
	/**
	 * 获取：0不是默认 1默认地址
	 */
	public Integer getAddressState() {
		return addressState;
	}
	/**
	 * 设置：0未删除  1已删除
	 */
	public void setAddressDorp(Integer addressDorp) {
		this.addressDorp = addressDorp;
	}
	/**
	 * 获取：0未删除  1已删除
	 */
	public Integer getAddressDorp() {
		return addressDorp;
	}
	/**
	 * 设置：省编码
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	/**
	 * 获取：省编码
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	/**
	 * 设置：城市编码
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * 获取：城市编码
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * 设置：区编码
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	/**
	 * 获取：区编码
	 */
	public String getAreaCode() {
		return areaCode;
	}
	/**
	 * 设置：
	 */
	public void setAddressUserNameEn(String addressUserNameEn) {
		this.addressUserNameEn = addressUserNameEn;
	}
	/**
	 * 获取：
	 */
	public String getAddressUserNameEn() {
		return addressUserNameEn;
	}
}
