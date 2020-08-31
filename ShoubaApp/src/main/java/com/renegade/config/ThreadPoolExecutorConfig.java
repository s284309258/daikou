package com.renegade.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月21日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Configuration
public class ThreadPoolExecutorConfig extends AsyncConfigurerSupport {
	/**
	 * 线程池配置，不影响主线程的情况，把异步任务丢到这里面处理
	 *
	 * @return
	 */
	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(1000);
		executor.setKeepAliveSeconds(300);
		executor.setThreadNamePrefix("asyncTread四叶草-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务都完成再继续销毁其他的Bean
		executor.setWaitForTasksToCompleteOnShutdown(true);
		// 线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}

}
