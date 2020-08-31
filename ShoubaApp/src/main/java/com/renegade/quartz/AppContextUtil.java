package com.renegade.quartz;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 这里无法使用 @Autowired 注入 Service，于是定义了 AppContextUtil 工具类用于访问 spring 中相关bean
 * 实现ApplicationContextAware接口，用于获取ApplicationContext对象，
 * 从而获取spring管理的Bean
 * @author Administrator
 *
 */
@Component
public class AppContextUtil implements ApplicationContextAware
{

	private static ApplicationContext ctx = null;

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException
	{
		AppContextUtil.ctx = ctx;
	}

	
	/**
	 * 根据beanName查找bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName)
	{
		return ctx.getBean(beanName);
	}

}