package com.renegade.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUtil {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入缓存设置时效时间
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 设置缓存对象
	 * 
	 * @param key 缓存key
	 * @param obj 缓存value
	 */
	public <T> void setObject(String key, T obj, int expireTime) {
		set(key, JSON.toJSONString(obj), (long) expireTime);
	}

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void delete(final String key) {
		if (hasKey(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 获取指定key的缓存(泛型
	 * 
	 * @param <obj>
	 * 
	 * @param key---JSON.parseObject(value, User.class);
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> T gettGenericity(String key, Class<T> obj) {
		String object = (String) get(key);
		if (object == null)
			return null;

		return JSON.parseObject(object, obj);

	}

	/**
	 * 哈希 添加
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void hmSet(String key, Object hashKey, Object value) {
		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
		hash.put(key, hashKey, value);
	}

	/**
	 * 哈希获取数据
	 * 
	 * @param key
	 * @param hashKey
	 * @return
	 */
	public Object hmGet(String key, Object hashKey) {
		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
		return hash.get(key, hashKey);
	}

	/**
	 * 列表添加
	 * 
	 * @param k
	 * @param v
	 */
	public void lPush(String k, Object v) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		list.rightPush(k, v);
	}

	/**
	 * 列表获取
	 * 
	 * @param k
	 * @param l
	 * @param l1
	 * @return
	 */
	public List<Object> lRange(String k, long l, long l1) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		return list.range(k, l, l1);
	}

	/**
	 * 集合添加
	 * 
	 * @param key
	 * @param value
	 */
	public void add(String key, Object value) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		set.add(key, value);
	}

	/**
	 * 集合获取
	 * 
	 * @param key
	 * @return
	 */
	public Set<Object> setMembers(String key) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		return set.members(key);
	}

	/**
	 * 有序集合添加
	 * 
	 * @param key
	 * @param value
	 * @param scoure
	 */
	public void zAdd(String key, Object value, double scoure) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		zset.add(key, value, scoure);
	}

	/**
	 * 有序集合获取
	 * 
	 * @param key
	 * @param scoure
	 * @param scoure1
	 * @return
	 */
	public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.rangeByScore(key, scoure, scoure1);
	}

	/**
	 * 获取长度
	 * 
	 * @param key
	 * @return
	 */
	public Long llen(String key) {
		return redisTemplate.opsForList().size(key);
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			System.out.println("========="+jedis);
//			long ints = jedis.llen(key);
//			return ints;
//		} finally {
//			if (jedis != null) {
//				jedis.close();
//			}
//		}

	}

	/**
	 * 从队列头部插入，最早的属于属于队列尾部，就是最后一个元素
	 * 
	 * @param key
	 * @return
	 */
	public void lpush(String key, String time) {
		redisTemplate.opsForList().leftPush(key, time);
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			jedis.lpush(key, time);
//		} finally {
//			if (jedis != null) {
//				jedis.close();
//			}
//		}
	}

	/**
	 * 根据队列下标取出队列的元素
	 * 
	 * @param key
	 * @return
	 */
	public Object lindex(String key, Long ints) {
		return redisTemplate.opsForList().index(key, ints);
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			return jedis.lindex(key, ints);
//		} finally {
//			if (jedis != null) {
//				jedis.close();
//			}
//		}
	}

	/**
	 * 列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
	 * 
	 * @param key
	 * @return
	 */
	public void ltrim(String key, int ints, Long limitCount) {
		redisTemplate.opsForList().trim(key, ints, limitCount);
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			jedis.ltrim(key, ints, limitCount);
//		} finally {
//			if (jedis != null) {
//				jedis.close();
//			}
//		}
	}
}