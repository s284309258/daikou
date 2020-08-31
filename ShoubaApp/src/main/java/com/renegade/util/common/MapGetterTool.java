package com.renegade.util.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Map取值方法,其中取得多种值,避免null值转换
 * </p>
 * @author hailan
 *
 */
public class MapGetterTool {
	
	
	/**
	 * <p>
	 * 根据Key返回一个Double型
	 * </p>
	 * @param key
	 * @return Double
	 */
	public static Double getDouble(String key,Map<String,Object> map){
		if(map.get(key)!=null){
			if(map.get(key) instanceof Double){
				return (Double)map.get(key);
			}else{
				return 0.0;
			}
		}else{
			return 0.00;
		}
	}
	
	/**
	 * <p>
	 * 根据Key返回一个String
	 * </p>
	 * @param key
	 * @return String
	 */
	public static String getString(String key,Map<String,Object> map){
		if(map.get(key)!=null){
			if(map.get(key) instanceof String){
				return (String)map.get(key);
			}else{
				return null;
			}
		}else{
			return "";
		}
	}
	
	/**
	 * <p>
	 * 根据Key返回一个Date
	 * </p>
	 * @param key
	 * @return Date
	 */
	public static Date getDate(String key,Map<String,Object> map){
		if(map.get(key)!=null){
			return TimeUtil.strToDateLong(map.get(key).toString());
		}else{
			return null;
		}
	}
	
	/**
	 * <p>
	 * 根据Key返回一个Integer
	 * </p>
	 * @param key
	 * @return Integer
	 */
	public static Integer getInteger(String key,Map<String,Object> map){
		if(map.get(key)!=null){
			if(map.get(key) instanceof Integer){
				return (Integer)map.get(key);
			}else{
				return null;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * <p>
	 * 根据一个Key返回一个Map<String,String>
	 * </p>
	 * @param key
	 * @return Map<String,String>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getMap(String key,Map<String,Object> map){
		if(map.get(key)!=null){
			if(map.get(key) instanceof Map){
				return (Map<String,String>)map.get(key);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * <p>
	 * 根据key返回BigDecimal
	 * 如果为null,则返回 new BigDecimal(0)
	 * </p>
	 * @param key
	 * @return BigDecimal
	 */
	public static BigDecimal getBigDecimal(String key,Map<String,Object> map){
		if(map.containsKey(key)){
			if(map.get(key) instanceof BigDecimal){
				return (BigDecimal)map.get(key);
			}else{
				return new BigDecimal(0);
			}
		}else{
			return new BigDecimal(0);
		}
	}

	
	/**
	 * 从starRankList列表中取出str2=str1的map
	 * @param starRankList
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static Map<String, Object> getOneMap(List<Map<String, Object>> starRankList, String str1, String str2 ) {
		Map<String, Object> oneMap = new HashMap<>();
		for(int i=0;i<starRankList.size();i++) {
			if(str1.equals(starRankList.get(i).get(str2).toString())) {
				oneMap=starRankList.get(i);
				return oneMap;
			}
		}
		return oneMap;
	}
 
}
