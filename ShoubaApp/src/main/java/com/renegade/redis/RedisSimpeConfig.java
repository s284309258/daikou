package com.renegade.redis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月13日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Configuration
@EnableCaching // 开启注解
public class RedisSimpeConfig {
	@Autowired
	private RedisProperties redisProperties;
	/**
	 * log
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedisSimpeConfig.class);

	// 以下两种redisTemplate自由根据场景选择
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);

		template.setValueSerializer(serializer);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		System.out.println("redis======redis单机生效1===================");
		return template;
	}

	// @Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(factory);
		System.out.println("redis======redis单机生效2===================");
		return stringRedisTemplate;
	}

//	@Bean
	public RedisTemplate<String, Serializable> limitRedisTemplate(LettuceConnectionFactory factory) {
		RedisTemplate<String, Serializable> template = new RedisTemplate<String, Serializable>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(factory);
		System.out.println("redis======redis单机生效3===================");
		return template;
	}
	/**
     * 获取jedispool（不能兼容集群模式的redis配置单机版的注入）
     *
     * @return
     */
   // @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = getJedisPoolConfig();
        JedisPool jedisPool = null;
        if (redisProperties.getPassword().isEmpty()) {
            jedisPool = new JedisPool(
                    config,
                    redisProperties.getHost(),
                    redisProperties.getPort(),
                    redisProperties.getTimeout());
        } else {
            jedisPool = new JedisPool(
                    config,
                    redisProperties.getHost(),
                    redisProperties.getPort(),
                    redisProperties.getTimeout(),
                    redisProperties.getPassword());
        }
        System.out.println("jedis======jedis客户端单机生效3===================");
        return jedisPool;
    }
    /**
     * jedis配置
     *
     * @return
     */
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getMaxIdle());// 最大空闲数
        config.setMaxWaitMillis(redisProperties.getMaxWaitMillis()); //最大等待时间
        config.setMaxTotal(redisProperties.getMaxActive()); //最大连接数
        return config;
    }
}
