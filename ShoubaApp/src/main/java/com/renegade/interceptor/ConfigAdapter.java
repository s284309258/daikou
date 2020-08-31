package com.renegade.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 继承WebMvcConfigurerAdapter，通过重写父类的方法进行扩展从而新增一个拦截器
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年7月5日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Configuration
public class ConfigAdapter implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/406","/405","/407","/nav","/error","/css/**","/js/**","/images/**","/img/**","/layer/**","/i18n/**");
	}
}
