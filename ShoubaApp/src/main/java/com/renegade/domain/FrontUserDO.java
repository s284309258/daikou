package com.renegade.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * app用户表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 15:25:32
 */
public class FrontUserDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户id
	private Integer userId;
	//账户ID
	private String accountId;
	//用户名
	private String userName;
	//注册手机号/邮箱
	private String userTel;
	//支付密码
	private String payPassword;
	//登录密码
	private String loginPassword;
	//用户余额
	private BigDecimal userBalance;
	//用户图像
	private String headPhoto;
	//推荐人ID
	private Integer userPid;
	//用户状态（激活，0 冻结，1 ）
	private Integer userStatus;
	//资料完整度（百分比）
	private String completeRate;
	//职业
	private String occupation;
	//教育经历
	private String education;
	//个人简介
	private String profile;
	//年龄
	private Integer age;
	//1男2女
	private Integer sex;
	//微信ID
	private String wxId;
	//区域标识ID
	private Integer areaId;
	//用户注册时间(时间戳)
	private String userRegtime;
	//订单号
	private String orderNo;
	//订单类型1充币 2提币驳回 
	private Integer benefitType;
	//流水修改时间(时间戳)
	private Date updateTime;
	//邀请码
	private String invitationCode;
	//注册时间
	private Date userRegtimes;
	//经度
	private String lng;
	//纬度
	private String lat;
	//是否实名验证 0 否 1是
	private Integer realVerification;
	//每天秒杀限制：0.未限制，1限制
	private Integer userLimit;
	//限购次数0 未限制，1限制
	private Integer userLimitss;
	//A网是否限购：0 未 1 限购
	private Integer userLimitsss;
	//父级链（以逗号分隔！）
	private String parentChain;
	//代数
	private Integer algebra;
	//是否为活跃用户：0 不是 1 是
	private Integer isActive;
	//抵扣券张数
	private BigDecimal userTicket;
	//直推人数
	private Integer directNum;
	//伞下人数
	private Integer underNum;
	//1批发商2县代理3市代理4省代理5运营中心
	private Integer userLevel;
	//可用交易余额
	private BigDecimal isTransactionBalance;
	//冻结交易余额
	private BigDecimal freezeTransactionBalance;
	//可用提现余额
	private BigDecimal isWithdrawBalance;
	//冻结提现余额
	private BigDecimal freezeWithdrawBalance;
	//用户钱包地址
	private String userWalletAddress;
	private String userWalletAddressUt;
	//提货积分
	private BigDecimal userAssests;
	//用户邮箱
	private String userEmail;
	//新状态
	private String newStatus;

	public String getNewStatus() {
		return newStatus;
	}
	
	public String getUserWalletAddressUt() {
		return userWalletAddressUt;
	}

	public void setUserWalletAddressUt(String userWalletAddressUt) {
		this.userWalletAddressUt = userWalletAddressUt;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
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
	 * 设置：账户ID
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * 获取：账户ID
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * 设置：用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：注册手机号/邮箱
	 */
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	/**
	 * 获取：注册手机号/邮箱
	 */
	public String getUserTel() {
		return userTel;
	}
	/**
	 * 设置：支付密码
	 */
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	/**
	 * 获取：支付密码
	 */
	public String getPayPassword() {
		return payPassword;
	}
	/**
	 * 设置：登录密码
	 */
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	/**
	 * 获取：登录密码
	 */
	public String getLoginPassword() {
		return loginPassword;
	}
	/**
	 * 设置：用户余额
	 */
	public void setUserBalance(BigDecimal userBalance) {
		this.userBalance = userBalance;
	}
	/**
	 * 获取：用户余额
	 */
	public BigDecimal getUserBalance() {
		return userBalance;
	}
	/**
	 * 设置：用户图像
	 */
	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}
	/**
	 * 获取：用户图像
	 */
	public String getHeadPhoto() {
		return headPhoto;
	}
	/**
	 * 设置：推荐人ID
	 */
	public void setUserPid(Integer userPid) {
		this.userPid = userPid;
	}
	/**
	 * 获取：推荐人ID
	 */
	public Integer getUserPid() {
		return userPid;
	}
	/**
	 * 设置：用户状态（激活，0 冻结，1 ）
	 */
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	/**
	 * 获取：用户状态（激活，0 冻结，1 ）
	 */
	public Integer getUserStatus() {
		return userStatus;
	}
	/**
	 * 设置：资料完整度（百分比）
	 */
	public void setCompleteRate(String completeRate) {
		this.completeRate = completeRate;
	}
	/**
	 * 获取：资料完整度（百分比）
	 */
	public String getCompleteRate() {
		return completeRate;
	}
	/**
	 * 设置：职业
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	/**
	 * 获取：职业
	 */
	public String getOccupation() {
		return occupation;
	}
	/**
	 * 设置：教育经历
	 */
	public void setEducation(String education) {
		this.education = education;
	}
	/**
	 * 获取：教育经历
	 */
	public String getEducation() {
		return education;
	}
	/**
	 * 设置：个人简介
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}
	/**
	 * 获取：个人简介
	 */
	public String getProfile() {
		return profile;
	}
	/**
	 * 设置：年龄
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * 获取：年龄
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * 设置：1男2女
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 获取：1男2女
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * 设置：微信ID
	 */
	public void setWxId(String wxId) {
		this.wxId = wxId;
	}
	/**
	 * 获取：微信ID
	 */
	public String getWxId() {
		return wxId;
	}
	/**
	 * 设置：区域标识ID
	 */
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：区域标识ID
	 */
	public Integer getAreaId() {
		return areaId;
	}
	/**
	 * 设置：用户注册时间(时间戳)
	 */
	public void setUserRegtime(String userRegtime) {
		this.userRegtime = userRegtime;
	}
	/**
	 * 获取：用户注册时间(时间戳)
	 */
	public String getUserRegtime() {
		return userRegtime;
	}
	/**
	 * 设置：订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：订单类型1充币 2提币驳回 
	 */
	public void setBenefitType(Integer benefitType) {
		this.benefitType = benefitType;
	}
	/**
	 * 获取：订单类型1充币 2提币驳回 
	 */
	public Integer getBenefitType() {
		return benefitType;
	}
	/**
	 * 设置：流水修改时间(时间戳)
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：流水修改时间(时间戳)
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：邀请码
	 */
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	/**
	 * 获取：邀请码
	 */
	public String getInvitationCode() {
		return invitationCode;
	}
	/**
	 * 设置：注册时间
	 */
	public void setUserRegtimes(Date userRegtimes) {
		this.userRegtimes = userRegtimes;
	}
	/**
	 * 获取：注册时间
	 */
	public Date getUserRegtimes() {
		return userRegtimes;
	}
	/**
	 * 设置：经度
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}
	/**
	 * 获取：经度
	 */
	public String getLng() {
		return lng;
	}
	/**
	 * 设置：纬度
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}
	/**
	 * 获取：纬度
	 */
	public String getLat() {
		return lat;
	}
	/**
	 * 设置：是否实名验证 0 否 1是
	 */
	public void setRealVerification(Integer realVerification) {
		this.realVerification = realVerification;
	}
	/**
	 * 获取：是否实名验证 0 否 1是
	 */
	public Integer getRealVerification() {
		return realVerification;
	}
	/**
	 * 设置：每天秒杀限制：0.未限制，1限制
	 */
	public void setUserLimit(Integer userLimit) {
		this.userLimit = userLimit;
	}
	/**
	 * 获取：每天秒杀限制：0.未限制，1限制
	 */
	public Integer getUserLimit() {
		return userLimit;
	}
	/**
	 * 设置：限购次数0 未限制，1限制
	 */
	public void setUserLimitss(Integer userLimitss) {
		this.userLimitss = userLimitss;
	}
	/**
	 * 获取：限购次数0 未限制，1限制
	 */
	public Integer getUserLimitss() {
		return userLimitss;
	}
	/**
	 * 设置：A网是否限购：0 未 1 限购
	 */
	public void setUserLimitsss(Integer userLimitsss) {
		this.userLimitsss = userLimitsss;
	}
	/**
	 * 获取：A网是否限购：0 未 1 限购
	 */
	public Integer getUserLimitsss() {
		return userLimitsss;
	}
	/**
	 * 设置：父级链（以逗号分隔！）
	 */
	public void setParentChain(String parentChain) {
		this.parentChain = parentChain;
	}
	/**
	 * 获取：父级链（以逗号分隔！）
	 */
	public String getParentChain() {
		return parentChain;
	}
	/**
	 * 设置：代数
	 */
	public void setAlgebra(Integer algebra) {
		this.algebra = algebra;
	}
	/**
	 * 获取：代数
	 */
	public Integer getAlgebra() {
		return algebra;
	}
	/**
	 * 设置：是否为活跃用户：0 不是 1 是
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	/**
	 * 获取：是否为活跃用户：0 不是 1 是
	 */
	public Integer getIsActive() {
		return isActive;
	}
	/**
	 * 设置：特价票张数
	 */
	public void setUserTicket(BigDecimal userTicket) {
		this.userTicket = userTicket;
	}
	/**
	 * 获取：特价票张数
	 */
	public BigDecimal getUserTicket() {
		return userTicket;
	}
	/**
	 * 设置：直推人数
	 */
	public void setDirectNum(Integer directNum) {
		this.directNum = directNum;
	}
	/**
	 * 获取：直推人数
	 */
	public Integer getDirectNum() {
		return directNum;
	}
	/**
	 * 设置：伞下人数
	 */
	public void setUnderNum(Integer underNum) {
		this.underNum = underNum;
	}
	/**
	 * 获取：伞下人数
	 */
	public Integer getUnderNum() {
		return underNum;
	}
	/**
	 * 设置：1批发商2县代理3市代理4省代理5运营中心
	 */
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	/**
	 * 获取：1批发商2县代理3市代理4省代理5运营中心
	 */
	public Integer getUserLevel() {
		return userLevel;
	}
	/**
	 * 设置：可用交易余额
	 */
	public void setIsTransactionBalance(BigDecimal isTransactionBalance) {
		this.isTransactionBalance = isTransactionBalance;
	}
	/**
	 * 获取：可用交易余额
	 */
	public BigDecimal getIsTransactionBalance() {
		return isTransactionBalance;
	}
	/**
	 * 设置：冻结交易余额
	 */
	public void setFreezeTransactionBalance(BigDecimal freezeTransactionBalance) {
		this.freezeTransactionBalance = freezeTransactionBalance;
	}
	/**
	 * 获取：冻结交易余额
	 */
	public BigDecimal getFreezeTransactionBalance() {
		return freezeTransactionBalance;
	}
	/**
	 * 设置：可用提现余额
	 */
	public void setIsWithdrawBalance(BigDecimal isWithdrawBalance) {
		this.isWithdrawBalance = isWithdrawBalance;
	}
	/**
	 * 获取：可用提现余额
	 */
	public BigDecimal getIsWithdrawBalance() {
		return isWithdrawBalance;
	}
	/**
	 * 设置：冻结提现余额
	 */
	public void setFreezeWithdrawBalance(BigDecimal freezeWithdrawBalance) {
		this.freezeWithdrawBalance = freezeWithdrawBalance;
	}
	/**
	 * 获取：冻结提现余额
	 */
	public BigDecimal getFreezeWithdrawBalance() {
		return freezeWithdrawBalance;
	}
	/**
	 * 设置：用户钱包地址
	 */
	public void setUserWalletAddress(String userWalletAddress) {
		this.userWalletAddress = userWalletAddress;
	}
	/**
	 * 获取：用户钱包地址
	 */
	public String getUserWalletAddress() {
		return userWalletAddress;
	}
	/**
	 * 设置：提货积分
	 */
	public void setUserAssests(BigDecimal userAssests) {
		this.userAssests = userAssests;
	}
	/**
	 * 获取：提货积分
	 */
	public BigDecimal getUserAssests() {
		return userAssests;
	}
	/**
	 * 设置：用户邮箱
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/**
	 * 获取：用户邮箱
	 */
	public String getUserEmail() {
		return userEmail;
	}
	@Override
	public String toString() {
		return "FrontUserDO [userId=" + userId + ", accountId=" + accountId + ", userName=" + userName + ", userTel="
				+ userTel + ", payPassword=" + payPassword + ", loginPassword=" + loginPassword + ", userBalance="
				+ userBalance + ", headPhoto=" + headPhoto + ", userPid=" + userPid + ", userStatus=" + userStatus
				+ ", completeRate=" + completeRate + ", occupation=" + occupation + ", education=" + education
				+ ", profile=" + profile + ", age=" + age + ", sex=" + sex + ", wxId=" + wxId + ", areaId=" + areaId
				+ ", userRegtime=" + userRegtime + ", orderNo=" + orderNo + ", benefitType=" + benefitType
				+ ", updateTime=" + updateTime + ", invitationCode=" + invitationCode + ", userRegtimes=" + userRegtimes
				+ ", lng=" + lng + ", lat=" + lat + ", realVerification=" + realVerification + ", userLimit="
				+ userLimit + ", userLimitss=" + userLimitss + ", userLimitsss=" + userLimitsss + ", parentChain="
				+ parentChain + ", algebra=" + algebra + ", isActive=" + isActive + ", userTicket=" + userTicket
				+ ", directNum=" + directNum + ", underNum=" + underNum + ", userLevel=" + userLevel
				+ ", isTransactionBalance=" + isTransactionBalance + ", freezeTransactionBalance="
				+ freezeTransactionBalance + ", isWithdrawBalance=" + isWithdrawBalance + ", freezeWithdrawBalance="
				+ freezeWithdrawBalance + ", userWalletAddress=" + userWalletAddress + ", userAssests=" + userAssests
				+ ", userEmail=" + userEmail + "]";
	}
	
}
