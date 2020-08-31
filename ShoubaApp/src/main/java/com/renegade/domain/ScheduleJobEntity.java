package com.renegade.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @Description: 计划任务信息
 * @author Levin
 * @date 2014年6月6日 下午10:49:43
 */
@Table(name = "t_sys_schedule_job")
public class ScheduleJobEntity
{
	//任务状态：运行
	public static final Integer STATUS_RUNNING = 1;
	//任务状态：未运行
	public static final Integer STATUS_NOT_RUNNING = 0;
	//任务状态：删除
	public static final Integer STATUS_DELETED = 2;
	
	public static final Integer CONCURRENT_IS = 1;
	public static final Integer CONCURRENT_NOT = 0;
	
	/**
	 * 任务ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	
	private String createTime;
	private String updateTime;
	
	/**
	 * 任务名称
	 */
	@Column(name = "jobName")
	private String jobName;
	
	/**
	 * 任务分组，任务名称+组名称应该是唯一的
	 */
	@Column(name = "jobGroup")
	private String jobGroup;
	
	/**
	 * 任务状态 是否启动任务（0：禁用，1：启用，2：删除） 
	 */
	@Column(name = "jobStatus")
	private Integer jobStatus;
	
	
	/**
	 * cron表达式（任务运行时间表达式）
	 */
	@Column(name = "cronExpression")
	private String cronExpression;
	
	/**
	 * 描述
	 */
	@Column(name = "description")
	private String description;
	
	/**
	 * 任务执行时调用哪个类的方法 包名+类名
	 * 任务调用类名，包名+类名，通过类反射调用 ，如果spingId为空，则按beanClass查找
	 */
	@Column(name = "beanClass")
	private String beanClass;
	
	/**
	 * 任务是否有状态（并发与否：0）
	 */
	@Column(name = "isConcurrent")
	private Integer isConcurrent;
	
	/**
	 * spring bean：任务调用类在spring中注册的bean id，如果spingId不为空，则按springId查找
	 */
	@Column(name = "springId")
	private String springId;
	
	/**
	 * 任务调用的方法名
	 */
	@Column(name = "methodName")
	private String methodName;
	
	/**
	 * 参数值
	 */
	@Column(name = "paramValue")
	private String paramValue;
	
	/**
	 * 延迟任务执行与否（0：未执行，1：已执行）
	 */
	@Column(name = "carryStatus")
	private String carryStatus;
	
	/**
	 * 延迟任务类型（01：创建星星，02：夺宝开奖）
	 */
	@Column(name = "type")
	private String type;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public String getJobGroup()
	{
		return jobGroup;
	}

	public void setJobGroup(String jobGroup)
	{
		this.jobGroup = jobGroup;
	}

	public Integer getJobStatus()
	{
		return jobStatus;
	}

	public void setJobStatus(Integer jobStatus)
	{
		this.jobStatus = jobStatus;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public void setCronExpression(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getBeanClass()
	{
		return beanClass;
	}

	public void setBeanClass(String beanClass)
	{
		this.beanClass = beanClass;
	}

	public Integer getIsConcurrent()
	{
		return isConcurrent;
	}

	public void setIsConcurrent(Integer isConcurrent)
	{
		this.isConcurrent = isConcurrent;
	}

	public String getSpringId()
	{
		return springId;
	}

	public void setSpringId(String springId)
	{
		this.springId = springId;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getCarryStatus() {
		return carryStatus;
	}

	public void setCarryStatus(String carryStatus) {
		this.carryStatus = carryStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ScheduleJobEntity [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobStatus=" + jobStatus + ", cronExpression="
				+ cronExpression + ", description=" + description + ", beanClass=" + beanClass + ", isConcurrent="
				+ isConcurrent + ", springId=" + springId + ", methodName=" + methodName + ", paramValue=" + paramValue
				+ ", carryStatus=" + carryStatus + ", type=" + type + "]";
	}
	
}