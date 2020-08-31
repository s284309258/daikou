package com.renegade.config;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtils {
	private static final	Logger logger=LoggerFactory.getLogger(JSONUtils.class);
	/**
	 * json字符串转换list集合map类型。增强版
	 * 
	 * @param <clazz>
	 * 
	 */
	public static List<Map<String, Object>> parseList(Object object) {
		Type type = new TypeReference<List<Map<String, Object>>>() {
		}.getType();// 阿里的反射机制
		return JSON.parseObject((String) object, type);
	}

	/**
	 * json字符串转换为复杂类型，如实体类的集合等等这些
	 * 
	 * @param <clazz>
	 * 
	 */
	public class result<T> {
		public T data;
	}

	public class T<K> {
		public K data;
	}

	/**
	 * 自定义json字符串转换指定的复杂类型
	 * 
	 * @param string
	 * @param t      json转换的返回类型 比如集合，bean类
	 * @param K      返回类型的指定的泛型
	 * @return * @author NIC丶四叶草^Renegade
	 * @date 2019年5月10日
	 */
	public static <T, K> T parseListGenericity(String string, Class<T> t, Class<K> k) {
		if (StringUtils.isEmpty(string)) {
			return null;
		}
		Type type = new TypeReference<T>() {
		}.getType();// 阿里的反射机制
		return JSON.parseObject((String) string, type);
	}

	/**
	 * 单个参数实体类转换 通用泛型
	 * <p>
	 * Title: Response
	 * </p>
	 * 
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @author NIC丶四叶草^Renegade
	 * 
	 * @date 2019年5月10日
	 */
	public class Response<T> {
		public T data;
	}

	/**
	 * 通过反射转换为对应的实体类 这个不好使
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> Response<T> parseToMap(String json, Class<T> type) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return  JSON.parseObject(json, new TypeReference<Response<T>>() {
		});
	}
	/**
	 * 自创的好使
	 * @param json
	 * @param claszz
	 * @return
	 */
	public static  <T> T parseToMap11(String json,  Class<T> claszz) {
		 return JSON.parseObject(json,  new TypeReference<T>() {}.getType());
		}

public static void main(String[] args) throws ClassNotFoundException {
/*	ZKUser zkuser= new ZKUser();
	zkuser.setUserId(110);
	zkuser.setUserPhone("13599888899");
	zkuser.setUserName("西装暴徒");
	String json=JSONObject.toJSONString(zkuser,SerializerFeature.WriteClassName);//可以在序列化的文本中加入@type信息，WriteClassName如果不加，
	logger.info(json);
	//如果顶层类型中存在泛型，就会出现不知道确切类型的状况，因为Class是无法携带泛型信息的，Java中不存在一个类型为List<String>.class，如果这样使用，Fastjson会将对象解析成JSONObject。
	ZKUser zkUser2=JSON.parseObject(json, new TypeReference<ZKUser>() {}.getType());//直接调用。适合多层类型，再已知泛型的基础上
	ZKUser  zkUser3=JSON.parseObject(json, ZKUser.class);//不通过TypeReference 反射，适合单层类型
	String path="com.zhongkehui.pojo.ZKUser";
	Class clazz = Class.forName(path);  
	String string1=((ZKUser) parseToMap11(json, clazz)).getUserName();
	logger.debug("===TypeReference=>"+parseToMap11(json, clazz).toString());//在未知泛型的基础上！
	logger.debug("===TypeReference=>"+string1);*/
}
	/**
	 * Bean对象转JSON
	 * 
	 * @param object
	 * @param dataFormatString
	 * @return
	 */
	public static String beanToJson(Object object, String dataFormatString) {
		if (object != null) {
			if (StringUtils.isEmpty(dataFormatString)) {
				return JSONObject.toJSONString(object);
			}
			return JSON.toJSONStringWithDateFormat(object, dataFormatString);
		} else {
			return null;
		}
	}

	/**
	 * Bean对象转JSON
	 * 
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object) {
		if (object != null) {
			return JSON.toJSONString(object);
		} else {
			return null;
		}
	}

	/**
	 * String转JSON字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String stringToJsonByFastjson(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(key, value);
		return beanToJson(map, null);
	}

	/**
	 * 将json字符串转换成对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object jsonToBean(String json, Object clazz) {
		if (StringUtils.isEmpty(json) || clazz == null) {
			return null;
		}
		return JSON.parseObject(json, clazz.getClass());
	}

	/**
	 * json字符串转map
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, Map.class);
	}
}
