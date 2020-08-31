package com.renegade.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.renegade.domain.ScheduleJobEntity;


/**
 * Job有状态实现类，不允许并发执行
 * @Description: 若一个方法一次执行不完下次轮转时则等待改方法执行完后才执行下一次操作
 * 主要是通过注解：@DisallowConcurrentExecution (quartz 2.3.0 版本中 StatefulJob 已经被弃用，改用注解的方式)
 * @author 唐亚峰
 * @date 2016-11-18
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job
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