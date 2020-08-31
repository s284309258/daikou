package com.renegade.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;

/**
 * 分布式锁接口
 * 
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月3日
 * @version 1.0
 * @email 291312408@qq.com
 */
public interface RediissionLockerService {
	RLock lock(String lockKey);

	RLock lock(String lockKey, long timeout);

	RLock lock(String lockKey, TimeUnit unit, long timeout);

	boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

	void unlock(String lockKey);

	void unlock(RLock lock);
	
	 /** 
     * 使用分布式锁，使用锁默认超时时间。 
     * 
     * @param callback 
     * @return 
     */  
    public <T> T lock(RedissonLockCallback<T> callback);
    
    
    /** 
     * 使用分布式锁。自定义锁的超时时间 
     * 
     * @param callback 
     * @param leaseTime 锁超时时间。超时后自动释放锁。 
     * @param timeUnit 
     * @return 
     */  
    public <T> T lock(RedissonLockCallback<T> callback, long leaseTime, TimeUnit timeUnit);  
    
}
