package com.renegade.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renegade.config.RedissionDSHOrder;
import com.renegade.config.ThreadManager;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年3月20日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service
public class RedissionDelayService {
	private final static Logger logger=LoggerFactory.getLogger(RedissionDelayService.class);
	@Autowired
	private ParamService tSysParamServiceImpl;
	private boolean start;
	@Autowired
	private RedissonClient redissonClient;// 配置已经自动生成装配
	// 内部接口
	private ROnDelayedListener listener;

	public static interface ROnDelayedListener {
		public void onDelayedArrived(RedissionDSHOrder order);
	}

	public void start(ROnDelayedListener listener) {
		if (start) {
			return;
		}
		logger.debug("======>>>>>>>>>>>>>RedissionDSHOrder启动OK<<<<<<<<<<<<<<<<================");
		start = true;
		this.listener = listener;
		RBlockingQueue<RedissionDSHOrder> blockingDeque = redissonClient.getBlockingQueue("anyQuque");// 先获取阻塞队列这里可以用RQeque这个！获取延迟队列
		// 当延迟队列的到期后会自动发到blockingDeque里，如果不写，存在的后果就是当服务器重启后，如果没有获取该队列，那么该队列的元素可能有一些不能被消费！
		RDelayedQueue<RedissionDSHOrder> delayQueue = redissonClient.getDelayedQueue(blockingDeque);// 在放入延迟队列，作用的目标队列就是blockingDeque

		ThreadManager.getThreadPollProxy().execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						// 延迟队列的元素到期后把元素转到目标队列blockingDeque中的头部，并从其中取出！当服务器宕机等服务器重启时会重新获取，当redis重启会重新自动自动订阅原先的队列数据
						RedissionDSHOrder order = blockingDeque.take();
						logger.debug("======到期元素在哪里================>>>>>>>>>>>>{}",order);
//						RedissionDSHOrder order =((BlockingDeque<RedissionDSHOrder>) delayQueue).take();// 这种方法必须强转
						if (RedissionDelayService.this.listener != null) {
							RedissionDelayService.this.listener.onDelayedArrived(order);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 添加延时队列
	public void add(RedissionDSHOrder order) {
		RBlockingQueue<RedissionDSHOrder> blockingDeque = redissonClient.getBlockingQueue("anyQuque");// 先获取阻塞队列这里可以用RQeque这个！获取延迟队列，该队列没有就自动定义，有获取队列
		RDelayedQueue<RedissionDSHOrder> delayQueue = redissonClient.getDelayedQueue(blockingDeque);// 在放入延迟队列，作用的目标队列就是blockingDeque
		logger.debug("=====RedissionDSHOrde队列的信息================>>>>>>>>>>>>{}",order.toString());
		delayQueue.offer(order, order.getStartTime(), TimeUnit.SECONDS);
		delayQueue.destroy();
	}

	public void add(String orderId) {// 添加要延时队列
		// spikeTimes分钟以后将消息发送到指定队列
		// 相当于spikeTimes分钟后取消订单
//		         延迟队列包含RedissionDSHOrder spikeTimes分钟，然后将其传输到blockingDeque中
		// 在spikeTimes分钟后就可以在blockingDeque 中获取RedissionDSHOrder了
		RBlockingQueue<RedissionDSHOrder> blockingDeque = redissonClient.getBlockingQueue("anyQuque");// 先获取阻塞队列这里可以用RQeque这个！获取延迟队列，该队列没有就自动定义，有获取队列
		RDelayedQueue<RedissionDSHOrder> delayQueue = redissonClient.getDelayedQueue(blockingDeque);// 在放入延迟队列，作用的目标队列就是blockingDeque
		delayQueue.offer(new RedissionDSHOrder(orderId, new Date().getTime()),
				Integer.parseInt(tSysParamServiceImpl.findValue("spikeTimes").getParamValue()), TimeUnit.SECONDS);
		// 当队列对象不再使用的时候应该主动关闭，要么就随着redission的关闭而关闭
		delayQueue.destroy();
	}
}
