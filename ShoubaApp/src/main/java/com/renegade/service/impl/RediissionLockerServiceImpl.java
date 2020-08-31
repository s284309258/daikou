package com.renegade.service.impl;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renegade.service.RediissionLockerService;
import com.renegade.service.RedissonLockCallback;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月3日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service
public class RediissionLockerServiceImpl implements RediissionLockerService {
	private static final long DEFAULT_TIMEOUT = 5;
	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
	@Autowired
	private RedissonClient redissonClient;// 配置已经自动生成装配
//lock(), 拿不到lock就不罢休，不然线程就一直block

	@Override
	public RLock lock(String lockKey) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock();
		return lock;
	}

	// leaseTime为加锁时间，单位为秒
	@Override
	public RLock lock(String lockKey, long leaseTime) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock(leaseTime, TimeUnit.SECONDS);// 代表秒
		return lock;

	}

	// timeout为加锁时间，时间单位由unit确定
	@Override
	public RLock lock(String lockKey, TimeUnit unit, long timeout) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock(timeout, unit);
		return null;
	}

	// tryLock()，马上返回，拿到lock就返回true，不然返回false。
	// 带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false.
	// waitTime 等待尝试获锁时间
	// leaseTime 加锁时间，即超时时间
	@Override
	public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(lockKey);// 获取锁
		try {
			return lock.tryLock(waitTime, leaseTime, unit);
		} catch (InterruptedException e) {
			return false;
		}
	}

//解锁
	@Override
	public void unlock(String lockKey) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(lockKey);
		lock.unlock();

	}

//解锁
	@Override
	public void unlock(RLock lock) {
		// TODO Auto-generated method stub
		lock.unlock();
	}

	@Override
	public <T> T lock(RedissonLockCallback<T> callback) {
		// TODO Auto-generated method stub
		return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
	}

//单位为秒
	@Override
	public <T> T lock(RedissonLockCallback<T> callback, long leaseTime, TimeUnit timeUnit) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(callback.getLockName());// 获取锁
		lock.lock(leaseTime, timeUnit);
		try {
			return callback.process();
		} finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}
}
