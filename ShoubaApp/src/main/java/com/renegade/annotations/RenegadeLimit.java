package com.renegade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单位时间请求时间限制次数
 * 
 * @author 、妙じ公子
 * @Description: TODO
 * @version 2.9T Copyright © 2019 四叶草 Info. Rengade Nic. All rights reserved.
 *
 * @date: 2019年7月14日 下午4:34:25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RenegadeLimit {
	/**
	 * 拦截请求名字
	 * 
	 * @return
	 */
	String name() default "renegadeLimit";

	/**
	 * 拦截请求资源的key
	 *
	 * @return
	 */
	String key() default "";

	/**
	 * Key的前缀
	 *
	 * @return
	 */
	String prefix() default "";

	/**
	 * 给定的时间段 单位秒
	 *
	 * @return
	 */
	int periodTime();

	/**
	 * 最多的访问限制次数
	 *
	 * @return
	 */
	int count();

	/**
	 * 类型
	 *
	 * @return
	 */
	RenegadeLimitType RenegadeLimitType() default RenegadeLimitType.CUSTOMER;

	// 限制的类型
	public enum RenegadeLimitType {

		/**
		 * 自定义key
		 */
		CUSTOMER,
		/**
		 * 根据请求者IP
		 */
		IP,
		/**
		 * 自定义限制
		 */
		RESUBMIT;

	}
}
