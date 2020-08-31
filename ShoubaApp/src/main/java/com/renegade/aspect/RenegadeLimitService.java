/**
 *  @author 、妙じ公子
 *  @version 2.9T
 *    Copyright © 2019 四叶草 Info. Rengade Nic. All rights reserved.
 *   
 *       @date: 2019年7月14日 下午4:43:50
 *
 */
package com.renegade.aspect;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renegade.annotations.RenegadeLimit;
import com.renegade.annotations.RenegadeLimit.RenegadeLimitType;
import com.renegade.exception.BDException;
import com.renegade.redis.RedisUtil;
import com.renegade.service.impl.RediissionLockerServiceImpl;
import com.renegade.util.common.IpUtil;

/**
 * @author 、妙じ公子
 * @Description: 利用redis限流削峰逻辑
 * @version 2.9T Copyright © 2019 四叶草 Info. Rengade Nic. All rights reserved.
 *
 * @date: 2019年7月14日 下午4:43:50
 */
@Component
@Aspect
public class RenegadeLimitService {
	private static final Logger logger = LoggerFactory.getLogger(RenegadeLimitService.class);
//	private final String REDIS_LUA = buildLuaRedis();
	private final String REDIS_LUA = buildLuaRedis2();
//	@Autowired
//	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedisUtil redisTemplate;
	@Autowired
	private RediissionLockerServiceImpl rediissionLockerServiceImpl;

	/**
	 * 标识呗注解的进行额外的逻辑处理
	 * 
	 * @param point
	 * @param renegadeLimit
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(renegadeLimit)")
	public Object limitTimeObject(ProceedingJoinPoint point, RenegadeLimit renegadeLimit) throws Throwable {
		RenegadeLimitType limitType = renegadeLimit.RenegadeLimitType();
		String name = renegadeLimit.name();
		String key = null;
		String msg = null;
		long limitPeriod = renegadeLimit.periodTime() * 1000;// 某个时间段毫秒级(用那个计数器脚本这里就是秒级)
		int limitCount = renegadeLimit.count();// 限流次数
		switch (limitType) {
		case IP:
			key = IpUtil.getIpAddress(); // 根据IP去限流
			msg = "对不起，当前人数爆满！请稍候再试！";
			break;
		case CUSTOMER:
			// TODO 根据自己定义的key 去限流
			key = renegadeLimit.key();
			msg = "对不起，当前人数爆满！请稍候再试！";
			break;
		case RESUBMIT:
			// 根据IP去特殊重复提交
			msg = "对不起，请不要频繁重复操作！";
			key = name + IpUtil.getIpAddress();
		default:
			break;
		}
		// 计数器限流
//		ImmutableList<String> keys = ImmutableList.of(StringUtils.join(renegadeLimit.prefix(), key));
//		try {
//			RedisScript<Number> redisScript = new DefaultRedisScript<Number>(REDIS_LUA, Number.class);
//			Number count = redisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
//			logger.info("Access try count is {} for name={} and key = {}", count, name, key);
//			if (count != null && count.intValue() <= limitCount) { // 计数器限流
//				return point.proceed();
//			} else {
//				throw new RuntimeException("对不起，秒杀失败");
//			}
//		} catch (Throwable e) {
//			if (e instanceof RuntimeException) {
//				e.printStackTrace();
//				throw new RuntimeException("服务器错误");
//			}
//			throw new RuntimeException("服务器请求繁忙");
//		}
		// 队列实现，处理高并发临界点，进一步优化，可把锁带到这里，
		try {
			Long llen = redisTemplate.llen(StringUtils.join(renegadeLimit.prefix(), key));
			logger.debug("Queue length{}", llen);
			if (llen < limitCount) {
				redisTemplate.lpush(StringUtils.join(renegadeLimit.prefix(), key),
						String.valueOf(new Date().getTime()));
				return point.proceed();
//				boolean getLock = rediissionLockerServiceImpl.tryLock(name, TimeUnit.SECONDS, 1L, 1L);
//				if (getLock) {
//					return point.proceed();
//				}
//				throw new RuntimeException("对不起，秒杀失败");
			} else {
				// 取出最后一个元素和当前时间戳对比
				Long lastTime = Long.parseLong(
						redisTemplate.lindex(StringUtils.join(renegadeLimit.prefix(), key), -1L).toString().trim());
				// 最早的元素时间也就是队列的最后一个元素与当前时间对比，如果大于限定的时间段，那么放行
				if ((new Date().getTime() - lastTime) >= limitPeriod) {
					redisTemplate.lpush(StringUtils.join(renegadeLimit.prefix(), key),
							String.valueOf(new Date().getTime()));
					// 只保留该区间的队列数量
					redisTemplate.ltrim(StringUtils.join(renegadeLimit.prefix(), key), 0, (long) limitCount - 1);
					// 拿到锁 ,这个项目不需要！ 直接屏蔽简单粗暴
//					boolean getLock = rediissionLockerServiceImpl.tryLock(name, TimeUnit.SECONDS, 1L, 1L);
//					if (getLock) {
//						return point.proceed();
//					}
					return point.proceed();
					// 否则，拦截
				} else {
					throw new BDException(msg);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			if (e instanceof BDException) {
				throw new BDException(((BDException) e).getMsg());
			}

		}
		return limitCount;

	}

	/**
	 * 限流 脚本 基于计数器的限流脚本，无法处理高并发临界点的脚本，这里是复习lua脚本，其实是可以用incr（）代替
	 *
	 * @return lua脚本
	 */
	private String buildLuaRedis() {
		StringBuilder lua = new StringBuilder();
		lua.append("local c").append("\nc = redis.call('get', KEYS[1])")
				// 调用不超过最大值，则直接返回
				.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then").append("\nreturn c;").append("\nend")
				// 执行计算器自加
				.append("\nc = redis.call('incr', KEYS[1])").append("\nif tonumber(c) == 1 then")
				// 从第一次调用开始限流，设置对应键值的过期
				.append("\nredis.call('expire', KEYS[1], ARGV[2])").append("\nend").append("\nreturn c;");
		return lua.toString();
	}

	/**
	 * 限流 脚本（处理临界时间大量请求的情况）
	 * 记录当前redis的key中记录最近m次访问的时间，一旦key中的队列元素超过m个，判断当前时间减去最早存入的时间是否小于h秒。如果是，则表示h秒的访问次数超过m次，
	 * 如果不是就将现在的时间加入列表中同时把最早的元素删除（可以通过脚本功能避免竞态条件）。由于需要记录每次访问的时间，所以当要限制“h时间最多访问m次”时，
	 * 如果“m”的数值较大，此方法可能占用一定的内存空间，可以用队列实现
	 * 
	 * @return lua脚本
	 */
	private String buildLuaRedis2() {
		StringBuilder lua = new StringBuilder();
		lua.append("local listLen, time").append("\nlistLen = redis.call('LLEN', KEYS[1])")
				// 不超过最大值，则直接写入时间
				.append("\nif listLen and tonumber(listLen) < tonumber(ARGV[1]) then")
				.append("\nlocal a = redis.call('TIME');").append("\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])")
				.append("\nelse")
				// 取出现存的最早的那个时间，和当前时间比较，看是小于时间间隔
				.append("\ntime = redis.call('LINDEX', KEYS[1], -1)").append("\nlocal a = redis.call('TIME');")
				.append("\nif a[1]*1000000+a[2] - time < tonumber(ARGV[2])*1000000 then")
				// 访问频率超过了限制，返回0表示失败
				.append("\nreturn 0;").append("\nelse").append("\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])")
				.append("\nredis.call('LTRIM', KEYS[1], 0, tonumber(ARGV[1])-1)").append("\nend").append("\nend")
				.append("\nreturn 1;");
		return lua.toString();
	}
}
