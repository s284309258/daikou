package com.renegade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME) //注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target(ElementType.METHOD)//注解作用于方法上
public @interface EnableCacheService {

	 /**
     * key前缀 
     */
	String keyPrefix();
    /**
     * key主体，spel表示，例：#id（取形参中id的值）
     */
    String fieldKey() default "";
    /**
     * 过期时间 (默认不设置) 默认一天
     */
    int expireTime() default 86400;
    
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    CacheOperation cacheOperation();
    
    /**
    * 缓存操作类型
    */
    enum CacheOperation {
        QUERY, // 查询
        UPDATE, // 修改
        DELETE;  // 删除
    }
}
