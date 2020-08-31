package com.renegade.redis;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.renegade.util.MySSLSocketFactory;

import redis.clients.jedis.HostAndPort;

/**
 * redisson集群
 * 
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月3日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Configuration
public class RedissionClusterConfig {
	@Autowired
	private RedisProperties redisProperties;
	private String masterName = "110";
	private int connectionPoolSize = 64;
	private int slaveConnectionPoolSize = 250;

	private int masterConnectionPoolSize = 250;
	/**
	 * 注意： 这里返回的RedissionCluster是单例的，并且可以直接注入到其他类中去使用
	 * 
	 */
	  /**
     * 单机模式自动装配
     * @return
     */
   @Bean
    RedissonClient redissonSingle() {
    	RedissonClient redisson = null;
        Config config = new Config();
        String string="redis://"+redisProperties.getHost().trim() + ":"+String.valueOf(redisProperties.getPort());
        System.out.println("string====="+string);
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(string.trim())
                .setTimeout(redisProperties.getTimeout())
                .setConnectionPoolSize(redisProperties.getMaxIdle())///设置对于master节点的连接池中连接数最大为500
                .setConnectionMinimumIdleSize(redisProperties.getMinIdle());
        
        if(StringUtils.isNotBlank(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        redisson=Redisson.create(config);
        try {
			System.out.println("============确认redisson单机配置成功===" + redisson.getConfig().toJSON().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return redisson;
    }
	// 集群模式自动装备
//	@Bean
	public RedissonClient getRedission() {
		String[] nodes = redisProperties.getClusterNodes().split(",");// 获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
		// redisson版本是3.9，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = "redis://" + nodes[i].trim();
		}
		RedissonClient redisson = null;
		Config config = new Config();
		config.useClusterServers() // 这是用的集群server
//        .setScanInterval(60000000) //设置集群状态扫描时间 毫秒级
				.setScanInterval(6000) // 设置集群状态扫描时间 毫秒级
				.addNodeAddress(nodes).setPassword(redisProperties.getPassword());
		redisson = Redisson.create(config);
		// 可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
		try {
			System.out.println("============确认redisson集群配置成功===" + redisson.getConfig().toJSON().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return redisson;
	}

	/**
	 * 哨兵模式自动装配
	 * 
	 * @return
	 */
//	@Bean
//	RedissonClient redissonSentinel() {
//		Config config = new Config();
//		SentinelServersConfig serverConfig = config.useSentinelServers()
//				.addSentinelAddress("redis://" + redisProperties.getHost().trim() + redisProperties.getPort())
//				.setMasterName(masterName).setTimeout(redisProperties.getTimeout())
//				.setMasterConnectionPoolSize(slaveConnectionPoolSize)
//				.setSlaveConnectionPoolSize(masterConnectionPoolSize);
//
//		if (StringUtils.isNotBlank(redisProperties.getPassword())) {
//			serverConfig.setPassword(redisProperties.getPassword());
//		}
//		return Redisson.create(config);
//	}

}
