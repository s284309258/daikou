package com.renegade.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.scheduling.config.TaskManagementConfigUtils;
import org.springframework.stereotype.Component;

/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年6月27日 
*   @version  1.0
* @email 291312408@qq.com
*/
@Component
public class MyBeanFactoryPostProcessor  implements BeanFactoryPostProcessor{

	//异步调用时候，会保证提前自动创建代理
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		  BeanDefinition beanDefinition = beanFactory.getBeanDefinition(TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME);
	        beanDefinition.getPropertyValues().add("exposeProxy", true);
	        System.out.println("增加显示依赖");
	        //解决异步调用，无法调用调用本类的代理对象
	    //    ((AbstractAutowireCapableBeanFactory) beanFactory).setAllowRawInjectionDespiteWrapping(true);
	}

}

