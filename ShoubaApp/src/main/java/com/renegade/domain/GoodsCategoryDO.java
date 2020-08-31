package com.renegade.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 商品类别
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 18:23:51
 */
public class GoodsCategoryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//类别名称
	private String categoryName;

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
	 * 设置：类别名称
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * 获取：类别名称
	 */
	public String getCategoryName() {
		return categoryName;
	}
}
