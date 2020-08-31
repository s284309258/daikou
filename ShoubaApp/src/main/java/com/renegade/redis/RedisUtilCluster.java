package com.renegade.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
//@Component
public class RedisUtilCluster {
	
	//缓存时间
	public static final Integer SECONDS=1*60*60*24;
	
	private static final Logger logger = LoggerFactory.getLogger(RedisUtilCluster.class);

	@Autowired
	private JedisCluster jedisCluster;
	 
	/**
	 * 设置缓存
	 * @param key    缓存key
	 * @param value  缓存value
	 */
	public void set(String key, String value) {
	        jedisCluster.set(key, value);
	    }
	 
	    /**
	     * 设置缓存对象
	     * @param key    缓存key
	     * @param obj  缓存value
	     */
	    public <T> void setObject(String key, T obj , int expireTime) {
	        jedisCluster.setex(key, expireTime, JSON.toJSONString(obj));
	    }
	    /**
		 * 获取指定key的缓存(泛型
		 * @param <obj>
		 * 
		 * @param key---JSON.parseObject(value, User.class);
		 * @return
		 * @throws ClassNotFoundException 
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 */
		public  <T> T gettGenericity(String key,  Class<T> obj ) {
			String object = jedisCluster.get(key);
			if (object == null)
				return null;
//			return JSON.parseObject(object, 
//						new TypeReference<Response<T>>(obj) {});
					return  JSON.parseObject(object, obj);
//			try {
//	            ObjectMapper objectMapper = new ObjectMapper();
//	            t = objectMapper.readValue(object,
//	                    obj);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//			return t;
		}
	    /**
	     * 获取指定key的缓存
	     * @param key---JSON.parseObject(value, User.class);
	     */
	    public Object getObject(String key) {
	    	String object = jedisCluster.get(key);
	    	if(object == null) return null;
	        return JSONObject.parse(object);
	    }
	 
	    /**
	     * 判断当前key值 是否存在
	     *
	     * @param key
	     */
	    public boolean hasKey(String key) {
	        return jedisCluster.exists(key);
	    }
	 
	 
	    /**
	     * 设置缓存，并且自己指定过期时间
	     * @param key
	     * @param value
	     * @param expireTime 过期时间
	     */
	    public void setWithExpireTime( String key, String value, int expireTime) {
	        jedisCluster.setex(key, expireTime, value);
	 
	    }
	 
	 
	    /**
	     * 获取指定key的缓存
	     * @param key
	     */
	    public String get(String key) {
	        String value = jedisCluster.get(key);
	        return value;
	    }
	 
	    /**
	     * 删除指定key的缓存
	     * @param key
	     */
	    public void delete(String key) {
	        jedisCluster.del(key);
	    }
	    
	    /**
		 * 实现redis keys 模糊查询
		 * @author hq
		 * @param pattern
		 * @return
		 */
		public List<String> keys(String pattern){  
	        logger.info("开始模糊查询【{}】keys", pattern);  
	        List<String> keys = new ArrayList<>();  
	        //获取所有连接池节点
	        Map<String, JedisPool> nodes = jedisCluster.getClusterNodes();  
	        //遍历所有连接池，逐个进行模糊查询
	        for(String k : nodes.keySet()){  
	            logger.info("从【{}】获取keys", k);  
	            JedisPool pool = nodes.get(k);  
	            //获取Jedis对象，Jedis对象支持keys模糊查询
	            Jedis connection = pool.getResource();  
	            try {  
	                keys.addAll(connection.keys(pattern));  
	            } catch(Exception e){  
	                logger.error("获取key异常", e);  
	            } finally{  
	                logger.info("关闭连接");
	                //一定要关闭连接！
	                connection.close();
	            }  
	        }  
	        logger.info("已获取所有keys");
	        return keys;  
	    }
	    
	    /**
	     * 批量删除Key值
	     * @param keys
	     */
	    public void delete(List<String> keys){
	    	if(keys==null || keys.isEmpty()){
	    		return;
	    	}
	    	for(String key : keys){
	    		jedisCluster.del(key);
	    	}
	    }
	    
	    
	    /**
	     * 将排行榜集合数据存储到redis缓存中
	     * @param rankList：排行榜的集合数据
	     * @param prefixKey：redis缓存key前缀
	     * @param majorIndex：map的主键
	     */
	    public void addRankList(List<Map<String, Object>> rankList,String prefixKey,String majorIndex) {
	    	//删除记录的索引
	    	delete(prefixKey);
			jedisCluster.sort(prefixKey, JSONObject.toJSONString(rankList).toString());
			
	    }
	    

	    
	    /**
	     * 分页查询redis缓存
	     * @param prefixKey：redis缓存key
	     * @param pageNum：当前页码数量
	     * @param pageSize：每页大小
	     * @param type：分页类型（1：下一页，2：上一页）
	     * @return
	     */
	    public List<Map<String, Object>> getRankLis(String prefixKey,String pageNum,int pageSize,String type){
	    	System.out.println("分页查询==================");
	    	int endCount = 0;
	    	List<Map<String, Object>> rankList = (List<Map<String, Object>>) JSONObject.parse(jedisCluster.get(prefixKey));
	    	if(rankList==null) {
	    		return null;
	    	}
	    	//如果pageNum为空，则默认查询第一页
	    	if(StringUtils.isEmpty(pageNum)||"0".equals(pageNum)) {
	    		pageNum="1";
	    	}
	    	int startCount=(Integer.parseInt(pageNum)-1)*pageSize;
	    	if(startCount > rankList.size()){
	    		return null;
	    	}
	    	if(startCount+pageSize > rankList.size()){
	    		endCount = rankList.size();
	    	}else{
	    		endCount = startCount+pageSize;
	    	}
	    	return rankList.subList(startCount, endCount);
	    	
//	    	Set<String> list = null;
//	    	
//	    	//元素总个数
//	    	Long totalNum=jedisCluster.zcard(prefixKey);
//	    	//如果pageNum为空，则默认查询第一页
//	    	if(StringUtils.isEmpty(pageNum)||"0".equals(pageNum)) {
//	    		pageNum="1";
//	    	}
//	    	//否则按照传入的页码数进行查询，计算开始记录数
//	    	int startCount=(Integer.parseInt(pageNum)-1)*pageSize;
//	    	//redis中的记录是从0开始的
//	    	pageSize=pageSize-1;
//	    	//这个id是翻页时的索引，不传时从第一个开始
//	    	if(startCount==0) {
//	    		//向下取值
//	    		list=jedisCluster.zrange(prefixKey, 0, pageSize);
//	    	}else {
//	    		//元素下脚标超标
//	    		System.out.println(totalNum);
//	    		System.out.println(startCount+1);
//	    		if(startCount+1>totalNum) {
//	    			return null;
//	    		}
//	    		//这里加1是因为总元素是从0开始计算的，获取的坐标就会小1
//	    		long round = Math.round(jedisCluster.zscore(prefixKey, String.valueOf(startCount))) + 1;
//	    		//type 1：下一页，2：上一页
//	    		if(!StringUtils.isEmpty(type) && "1".equals(type)) {
//	    			//下一页：zrange() 是向下取值 获取下一页数据
//	    			list=jedisCluster.zrange(prefixKey, round, pageSize+round);
//	    		}else {
//	    			//上一页：//zrevrange() 是向上取值 获取上一页数据
//	    			list=jedisCluster.zrevrange(prefixKey, round, pageSize+round);
//	    		}
//	    	}
//	    	
//	    	List<Map<String, Object>> rankList = new ArrayList<>();
//	    	for(String backId : list) {
//	    		Map<String, Object> parseObject=JSON.parseObject(jedisCluster.get(prefixKey + backId).toString(), new TypeReference<Map<String, Object>>() {
//	    		});
//	    		rankList.add(parseObject);
//	    	}
//	    	System.out.println("元素个数======"+jedisCluster.zcard(prefixKey));
//	    	return rankList;
	    }
	    
	    /**
	     * 计数器
	     * @param key
	     * @return
	     */
	    public long incr(String key){
	    	return jedisCluster.incr(key);
	    }
	    
}
