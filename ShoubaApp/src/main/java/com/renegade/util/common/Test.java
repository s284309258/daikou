package com.renegade.util.common;

import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test {
	

	static Jedis jedis = new Jedis("192.168.0.144");
	static void saveUser(){
		String id = new Random().nextInt(10000) + "";
		Map<String, String> map = new HashMap<String, String>();
			
		map.put("id", id);
		map.put("age", new Random().nextInt(70) + "");
		map.put("name", "张三"+new Random().nextInt(10000) + "");
			
		jedis.rpush("personid", id);		// 保存用户id
			
		jedis.hmset("person:" + id, map);	// 保存用户信息
	}
	
	
	static Map<String, String> getUserInfo(String id){
	      return jedis.hgetAll("person:" + id);
	}
	
	static void getUserList(){
		Long count = jedis.llen("personid");		// 用户总数
		
		List<String> idList = jedis.lrange("personid", 5, 10);
		for(String id : idList){
			System.out.println(jedis.hgetAll("person:" + id));
		}
	}
	
	
	public static void main(String[] args) {
		String shouxufei = "0.006";
		String amount = "666.74";
		BigDecimal bamount = new BigDecimal(amount).setScale(2);
		BigDecimal bshouxufei = new BigDecimal(shouxufei).multiply(bamount).setScale(2, RoundingMode.DOWN);
		BigDecimal ddd = bamount.subtract(bshouxufei);
		System.out.println(ddd.doubleValue());

		//saveUser();
	}


}
