package com.renegade.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

//@Configuration
public class JedisClusterConfig {

	@Autowired
    private RedisProperties redisProperties;
 
    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     * @return
     */
//    @Bean
  public JedisCluster getJedisCluster() {
        String[] serverArray = redisProperties.getClusterNodes().split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        Set<HostAndPort> nodes = new HashSet<>();
 
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        
        JedisPoolConfig pool = new JedisPoolConfig ();
        pool.setMaxTotal(100);
        pool.setMinIdle(10);
        pool.setMaxIdle(100);
        pool.setMaxWaitMillis(60000);
        System.out.println("redis======redis配置生效===================");
        return new JedisCluster(nodes,redisProperties.getCommandTimeout(),60000,3,redisProperties.getPassword() ,pool);//需要密码连接的创建对象方式
    }


}
