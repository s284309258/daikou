package com.renegade.quartz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.renegade.domain.ScheduleJobEntity;

public class QuartzJobUtils
{
	public final static Logger log = LoggerFactory.getLogger(QuartzJobUtils.class);

	
	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	public static void invokMethod(ScheduleJobEntity scheduleJob)
	{
		//查找出的bean
		Object object = null;
		Class<?> clazz = null;
		
		/**
		 * 如果springId不为空则先按照springId查找bean
		 */
		if (StringUtils.isNotBlank(scheduleJob.getSpringId())){	
			//按照springId查找bean
			object = AppContextUtil.getBean(scheduleJob.getSpringId());
		} else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())){
			//如果spingId为空，beanClass不为空，则按beanClass查找
			try{
				clazz = Class.forName(scheduleJob.getBeanClass());
				object = clazz.newInstance();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		//如果bean查找不到，即任务未启动成功
		if (object == null){
			log.error("[任务名称] - [{}] [未启动成功,请检查是否配置正确!!!]",scheduleJob.getJobName());
			return;
		}
		
		//bean找到了，不为空，得到其方法类
		clazz = object.getClass();
		//方法
		Method method = null;
		//找到类中相应方法名对应的方法
		try{
			method = clazz.getDeclaredMethod(scheduleJob.getMethodName(),String.class);
		} catch (NoSuchMethodException e){
			log.error("[任务名称] - [{}] [未启动成功,方法名设置错误!!!]",scheduleJob.getJobName());
		} catch (SecurityException e){
			e.printStackTrace();
		}
		
		//方法找到了，不为空
		if (method != null)
		{
			try
			{
				method.invoke(object,scheduleJob.getParamValue());
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		log.info("[任务名称] - [{}] [启动成功!!!]",scheduleJob.getJobName());
	}
	
	
	
	/**
	 * 判断cron时间表达式正确性
	 * @param cronExpression
	 * @return
	 */
	public static boolean isValidExpression(final String cronExpression) {
		 CronTriggerImpl trigger = new CronTriggerImpl();
		 try {
			trigger.setCronExpression(cronExpression);
			Date date = trigger.computeFirstFireTime(null);
			return date != null &&date.after(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return false;
	}
	
	
	/**
	 * 任务运行状态
	 * @author Administrator
	 *
	 */
	public enum TASK_STATE{     
        NONE("NONE","未知"),     
        NORMAL("NORMAL", "正常运行"),     
        PAUSED("PAUSED", "暂停状态"),      
        COMPLETE("COMPLETE",""),     
        ERROR("ERROR","错误状态"),     
        BLOCKED("BLOCKED","锁定状态"); 

        private String index;       
        private String name;       

        private TASK_STATE(String index, String name) {
            this.name = name;        
            this.index = index; 
        }

        public String getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
    }

	
}
