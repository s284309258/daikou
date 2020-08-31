package com.renegade.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** 
*  @Copyright    2018 
*        @author Renegade丶無歡  
*                    All right reserved
*      @Created   2018年12月9日 
*   @version  1.0
* @email 291312408@qq.com
*/
@Component
public class ApplicationContextRegister implements ApplicationContextAware{

	 private static ApplicationContext APPLICATION_CONTEXT;
	    /**
	     * 设置spring上下文
	     * @param applicationContext spring上下文
	     * @throws BeansException
	     * */
	    @Override  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	        APPLICATION_CONTEXT = applicationContext;
	    }

	    /**
	     * 获取容器
	     * @return
	     */
	    public static ApplicationContext getApplicationContext() {
	        return APPLICATION_CONTEXT;
	    }

	    /**
	     * 获取容器对象
	     * @param type
	     * @param <T>
	     * @return
	     */
	    public static <T> T getBean(Class<T> type) {
	        return APPLICATION_CONTEXT.getBean(type);
	    }

}

