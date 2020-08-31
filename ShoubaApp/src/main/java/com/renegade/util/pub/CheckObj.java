package com.renegade.util.pub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对象验证
 * 
 * required 是否必填字段 默认 true
 * 
 * validType 字段类型验证 默认 "String"
 * 
 * formatter 时间字段格式 默认 "yyyyMMdd"
 * 
 * @author Dracula
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckObj {

	/**
	 * 字段标识
	 * 
	 * @return
	 */
	public String fieldName();
	

	/**
	 * 是否必填字段
	 * 
	 * @return
	 */
	public boolean required() default true;

}