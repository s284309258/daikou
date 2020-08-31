package com.renegade.config;

import java.math.BigDecimal;

public class SysParamConstant {

	/*
	 * token过期时间
	 */	
	public static int past_seconds = 2592000;
	
	
	/**
	 * 缓存参数前缀
	 */
	public static final String T_SYS_PARAM ="t_sys_param_";
    /**
         * 历史排行榜	
     */
	public static final String T_SYS_RANK ="T_SYS_RANK";
	public static final String T_SYS_HISTORY ="T_SYS_HISTORY";
	/**
	 * 账号验证码登录
	 */
	public static String login_type_account = "account";
	
	/**
	 * 账号密码登录
	 */
	public static String login_type_account_pass = "account_pass";
	
	
	/**
	 * token登录
	 */
	public static String login_type_token = "token";
	
	/**
	 * md5密钥 前后台约定密钥
	 */
	public static String md5_key = "QCVFUW0VXH9UFYMLBQ8PEB2DVRBJF3TOL6IDOVNN5606UBXBOBDUEQAENY81BB03TNRPMHV6";
	
	/**
	 * 页面数量（分页）
	 */
	public static int page_size = 10;
	
	/**
	 * 星星排行榜页面加载数量
	 */
	public static int star_rank_page_size = 30;
	
	/**
	 * 缓存中分页类型：下一页
	 */
	public static String page_type1 = "1";
	
	/**
	 * 缓存中分页类型：上一页
	 */
	public static String page_type2 = "2";
	
	/**
	 * 兑换方式：星星兑换YKC
	 */
	public static String exchange_StarToYkc = "StarToYkc";
	
	/**
	 * 兑换方式：YKC兑换星星
	 */
	public static String exchange_YkcToStar = "YkcToStar";
	
	/**
	 * 兑换方式：所有
	 */
	public static String exchange_All = "All";

	/**
	 * 取反参数
	 */
	public static final BigDecimal lose = new BigDecimal(-1);
	
	
	/**
	 * 七牛公钥
	 */
	public static final String qiniu_accessKey = "OwlrZ-TAZ8kTdDYP7ssYS2Dbmlc_WV2nHdeTqMcY";

	/**
	 * 七牛私钥
	 */
	public static final String qiniu_secretKey = "L5gEXOGiqgHXG8BrsmXwnFGVMPwm0zEDY2CjSpYm";
	
	/**
	 * 七牛域名
	 */
	public static final String qiniu_domain = "http://cdn.rxhwl.com";
	
	/**
	 * 七牛对象名
	 */
	public static final String qiniu_bucket = "renegade";
	
	/**
	 * 系统域名
	 */
	public static final String sys_domain = "http://www.lichangwanglai.com";
	
	/**
	 * 小数精度
	 */
	public static final int decimal_precision = 4;
	
	
	
	
	
	/**
	 * 用户头像默认值
	 */
	public static final String head_photo = "lcwlAvatarlogo.png";
	
	
	/**
	 * 请求加好友发送验证消息次数限制
	 */
	public static final Integer friend_notice_send_time = 3;
	
	
	/**
	 * 用户好友最多数量
	 */
	public static final Integer user_friend_max_num = 2000;
	
	
	/**
	 * 图形验证码过期时间
	 */
	public static final int verification_code_seconds = 3600;
}
