package com.renegade.config;

/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年3月20日 
*   @version  1.0
* @email 291312408@qq.com
*/
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理的工具类，封装类
 * 
 * @author ThinkPad 线程池的管理 ，通过java 中的api实现管理 采用conncurrent框架： 非常成熟的并发框架
 *         ，特别在匿名线程管理非常优秀的
 *
 */
public class ThreadManager {
	// 通过ThreadPoolExecutor的代理类来对线程池的管理
	private static ThreadPollProxy mThreadPollProxy;

	// 单列对象
	public static ThreadPollProxy getThreadPollProxy() {
		synchronized (ThreadPollProxy.class) {
			if (mThreadPollProxy == null) {
                    int cpuNum = Runtime.getRuntime().availableProcessors();// 获取处理器数量
                    int threadNum = cpuNum * 2 + 1;// 根据cpu数量,计算出合理的线程并发数
				mThreadPollProxy = new ThreadPollProxy(cpuNum+1, threadNum, 300000);
			}
		}
		return mThreadPollProxy;
	}

	// 通过ThreadPoolExecutor的代理类来对线程池的管理
	public static class ThreadPollProxy {
		private ThreadPoolExecutor poolExecutor;// 线程池执行者 ，java内部通过该api实现对线程池管理
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;

		public ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		// 对外提供一个执行任务的方法
		public void execute(Runnable r) {
			if (r == null) {
				return;
			}
			if (poolExecutor == null || poolExecutor.isShutdown()) {
				poolExecutor = new ThreadPoolExecutor(
						// 核心线程数量
						corePoolSize,
						// 最大线程数量
						maximumPoolSize,
						// 当线程空闲时，保持活跃的时间
						keepAliveTime,
						// 时间单元 ，毫秒级
						TimeUnit.MILLISECONDS,
						// 线程任务队列
						new LinkedBlockingQueue<Runnable>(999999),
						// 创建线程的工厂
						Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy() {// 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
							 //拒绝策略
							@Override
							public void rejectedExecution(Runnable re, ThreadPoolExecutor e) {
								super.rejectedExecution(re, e);
							}
						});
			}
			poolExecutor.execute(r);
		}
		 // 从线程队列中移除对象
        public void cancel(Runnable runnable) {
            if (poolExecutor != null) {
            	poolExecutor.getQueue().remove(runnable);
            }
        }
	}
}
