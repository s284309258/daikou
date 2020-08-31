package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;



/**
 * 现货商城
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 20:35:13
 */
public class SpotGoodsDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//商品代码
	private Integer goodsId;
	//提货商品名称
	private String goodsName;
	//提货商品起始价
	private BigDecimal goodsOriginPrice;
	//结束价
	private BigDecimal goodsPrice;
	//提货商品类别
	private String goodsCategory;
	//提货商品货号
	private String goodsItemno;
	//提货商品品牌
	private String goodsBrand;
	//提货多图文商品描述（以逗号隔开）
	private String goodsDesc;
	//商品图片
	private String goodsPicture;
	//商品滚动图片介绍
	private String goodsPictureDesc;
	//提货商品库存
	private Integer goodsStock;
	//提货商品销量
	private Integer goodsSales;
	//0未销售，1上架，-1下架
	private Integer goodsStatus;
	private Integer goodsUserId;
	//-1:不优先1优先
	private Integer goodsFirst;
	//创建时间（不用理会，只是用来排序的）
	private Date createTime;
	//不填或者填写0视为包邮
	private BigDecimal goodsPostage;
	//商品秒杀库存量
	private Integer goodsNameEn;
	//
	private String goodsBrandEn;
    
	private Integer goodsState;
	
	private String goodsReason;
	
	
	private  List<SpitkeTicketDO> spitkeTicketDOs;
	

	public List<SpitkeTicketDO> getSpitkeTicketDOs() {
		return spitkeTicketDOs;
	}
	public void setSpitkeTicketDOs(List<SpitkeTicketDO> spitkeTicketDOs) {
		this.spitkeTicketDOs = spitkeTicketDOs;
	}
	public Integer getGoodsUserId() {
		return goodsUserId;
	}
	public void setGoodsUserId(Integer goodsUserId) {
		this.goodsUserId = goodsUserId;
	}
	/**
	 * 设置：商品代码
	 */
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	/**
	 * 获取：商品代码
	 */
	public Integer getGoodsId() {
		return goodsId;
	}
	/**
	 * 设置：提货商品名称
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * 获取：提货商品名称
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * 设置：提货商品起始价
	 */
	public void setGoodsOriginPrice(BigDecimal goodsOriginPrice) {
		this.goodsOriginPrice = goodsOriginPrice;
	}
	/**
	 * 获取：提货商品起始价
	 */
	public BigDecimal getGoodsOriginPrice() {
		return goodsOriginPrice;
	}
	/**
	 * 设置：结束价
	 */
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	/**
	 * 获取：结束价
	 */
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	/**
	 * 设置：提货商品类别
	 */
	public void setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	/**
	 * 获取：提货商品类别
	 */
	public String getGoodsCategory() {
		return goodsCategory;
	}
	/**
	 * 设置：提货商品货号
	 */
	public void setGoodsItemno(String goodsItemno) {
		this.goodsItemno = goodsItemno;
	}
	/**
	 * 获取：提货商品货号
	 */
	public String getGoodsItemno() {
		return goodsItemno;
	}
	/**
	 * 设置：提货商品品牌
	 */
	public void setGoodsBrand(String goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	/**
	 * 获取：提货商品品牌
	 */
	public String getGoodsBrand() {
		return goodsBrand;
	}
	/**
	 * 设置：提货多图文商品描述（以逗号隔开）
	 */
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	/**
	 * 获取：提货多图文商品描述（以逗号隔开）
	 */
	public String getGoodsDesc() {
		return goodsDesc;
	}
	/**
	 * 设置：商品图片
	 */
	public void setGoodsPicture(String goodsPicture) {
		this.goodsPicture = goodsPicture;
	}
	/**
	 * 获取：商品图片
	 */
	public String getGoodsPicture() {
		return goodsPicture;
	}
	/**
	 * 设置：商品滚动图片介绍
	 */
	public void setGoodsPictureDesc(String goodsPictureDesc) {
		this.goodsPictureDesc = goodsPictureDesc;
	}
	/**
	 * 获取：商品滚动图片介绍
	 */
	public String getGoodsPictureDesc() {
		return goodsPictureDesc;
	}
	/**
	 * 设置：提货商品库存
	 */
	public void setGoodsStock(Integer goodsStock) {
		this.goodsStock = goodsStock;
	}
	/**
	 * 获取：提货商品库存
	 */
	public Integer getGoodsStock() {
		return goodsStock;
	}
	/**
	 * 设置：提货商品销量
	 */
	public void setGoodsSales(Integer goodsSales) {
		this.goodsSales = goodsSales;
	}
	/**
	 * 获取：提货商品销量
	 */
	public Integer getGoodsSales() {
		return goodsSales;
	}
	/**
	 * 设置：0未销售，1上架，-1下架
	 */
	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	/**
	 * 获取：0未销售，1上架，-1下架
	 */
	public Integer getGoodsStatus() {
		return goodsStatus;
	}
	/**
	 * 设置：-1:不优先1优先
	 */
	public void setGoodsFirst(Integer goodsFirst) {
		this.goodsFirst = goodsFirst;
	}
	/**
	 * 获取：-1:不优先1优先
	 */
	public Integer getGoodsFirst() {
		return goodsFirst;
	}
	/**
	 * 设置：创建时间（不用理会，只是用来排序的）
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间（不用理会，只是用来排序的）
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：不填或者填写0视为包邮
	 */
	public void setGoodsPostage(BigDecimal goodsPostage) {
		this.goodsPostage = goodsPostage;
	}
	/**
	 * 获取：不填或者填写0视为包邮
	 */
	public BigDecimal getGoodsPostage() {
		return goodsPostage;
	}
	/**
	 * 设置：提货商品名字英文
	 */
	public void setGoodsNameEn(Integer goodsNameEn) {
		this.goodsNameEn = goodsNameEn;
	}
	/**
	 * 获取：提货商品名字英文
	 */
	public Integer getGoodsNameEn() {
		return goodsNameEn;
	}
	/**
	 * 设置：
	 */
	public void setGoodsBrandEn(String goodsBrandEn) {
		this.goodsBrandEn = goodsBrandEn;
	}
	/**
	 * 获取：
	 */
	public String getGoodsBrandEn() {
		return goodsBrandEn;
	}
	public Integer getGoodsState() {
		return goodsState;
	}
	public void setGoodsState(Integer goodsState) {
		this.goodsState = goodsState;
	}
	public String getGoodsReason() {
		return goodsReason;
	}
	public void setGoodsReason(String goodsReason) {
		this.goodsReason = goodsReason;
	}
	
}
