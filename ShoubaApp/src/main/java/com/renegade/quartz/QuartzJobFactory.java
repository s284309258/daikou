package com.renegade.quartz;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.renegade.domain.ScheduleJobEntity;


/**
 * 
 * @Description: 计划任务执行处 无状态
 * @author 唐亚峰
 * @date 2016-11-18
 */
public class QuartzJobFactory implements Job
{

	/**
	 * 当一个job触发时，execute方法就会被调用，执行里面的操作。
	 * 这里具体取出MergedJobDataMap中存放的Job实例，
	 * 并通过调用QuartzJobUtils.invokMethod方法来通过反射获取对象并执行对象的方法，
	 * 记录执行结果并写入数据库。
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap().get("scheduleJob");
		QuartzJobUtils.invokMethod(scheduleJob);
	}
}