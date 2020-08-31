package com.renegade.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisProperties {
	@Value("${spring.redis.expire-seconds}")
    private int expireSeconds;
//	@Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
	@Value("${spring.redis.password}")
    private String password;
	@Value("${spring.redis.timeout}")
    private int commandTimeout;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;
    
    
    public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public int getExpireSeconds() {
        return expireSeconds;
    }
 
    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
 
    public String getClusterNodes() {
        return clusterNodes;
    }
 
    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public int getCommandTimeout() {
        return commandTimeout;
    }
 
    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }
}