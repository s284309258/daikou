/**
 *@author: chenyoulong 
 *@Title:JsonUtil2.java
 *@date 2017-2-22 ????12:10:44 
 *@Description:TODO
 */
package service.util;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *@ClassName:JsonUtil2
 *@author: chenyoulong  Email: chen.youlong@payeco.com
 *@date :2017-2-22 ????12:10:44
 *@Description:TODO  json??????????bean??map??list???????bean
 */
public class JsontoBeanUtil {
	private static Log logger = LogFactory.getLog(JsontoBeanUtil.class);
	/**
	 * ??????????????
	 */
	static {
		//?????
		MorpherRegistry mr = JSONUtils.getMorpherRegistry();

		//?????????????????Json???п???????????????????????
		DateMorpher dm = new DateMorpher(new String[] { "yyyy-MM-dd",
				"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS","yyyyMMdd","MMdd","HH:mm:ss", 
				"yyyyMMddHHmmss", "HHmmss"});
		mr.registerMorpher(dm);
	}

	/**
	* ??json?????????????
	* @param jsonObjStr e.g. {'name':'get','dateAttr':'2009-11-12'}
	* @param clazz Person.class
	* @return
	*/
	public static Object getDtoFromJsonObjStr(String jsonObjStr, Class clazz) {
		return JSONObject.toBean(JSONObject.fromObject(jsonObjStr), clazz);
	}
	
	/**
	* ??json?????????????
	* @param jsonObjStr e.g. {'name':'get','dateAttr':'2009-11-12'}
	* @param clazz Person.class
	* @return
	*/
	public static Object getDtoFromJsonObjStr(String jsonObjStr, Class clazz,String[] excludes) {
		JsonConfig jsonConfig = new JsonConfig(); 
		jsonConfig.setExcludes(excludes);
		
		return JSONObject.toBean(JSONObject.fromObject(jsonObjStr,jsonConfig), clazz);
	}

	/**
	* ??json??????????????????弯????????????????Bean
	* @param jsonObjStr e.g. {'data':[{'name':'get'},{'name':'set'}]}
	* @param clazz e.g. MyBean.class
	* @param classMap e.g. classMap.put("data", Person.class)
	* @return Object
	*/
	public static Object getDtoFromJsonObjStr(String jsonObjStr, Class clazz, Map classMap) {
		
		return JSONObject.toBean(JSONObject.fromObject(jsonObjStr), clazz, classMap);
	}

	
	/**
	 * ??JSONObject?е????????entity??.
	 * 
	 * @param <T>
	 *            Object
	 * @param jsonObject
	 *            json????
	 * @param entity
	 *            ???????????node
	 * @param excludes
	 *            ??????????????????
	 * @param datePattern
	 *            ?????????
	 * @return T
	 * @throws Exception
	 *             java.lang.InstantiationException,
	 *             java.beans.IntrospectionException,
	 *             java.lang.IllegalAccessException
	 */
	public static <T extends Object> T json2Bean(JSONObject jsonObject,
			T entity, String[] excludes, String datePattern,Map classMap) throws Exception {
		// JsonUtils.configJson(excludes, datePattern);
		Set<String> excludeSet = new HashSet<String>();

		for (String exclude : excludes) {
			excludeSet.add(exclude);
		}

		for (Object object : jsonObject.entrySet()) {
			Map.Entry entry = (Map.Entry) object;
			String propertyName = entry.getKey().toString();

			if (excludeSet.contains(propertyName)) {
				continue;
			}

			String propertyValue = entry.getValue().toString();
			if(classMap.get(propertyName)!=null){
				
			}
			
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
						propertyName, entity.getClass());
				Class propertyType = propertyDescriptor.getPropertyType();

				Method writeMethod = propertyDescriptor.getWriteMethod();
				
				if (propertyType == String.class) {
					writeMethod.invoke(entity, propertyValue);
				} else if ((propertyType == Byte.class)
						|| (propertyType == byte.class)) {
					writeMethod.invoke(entity, Byte.parseByte(propertyValue));
				} else if ((propertyType == Short.class)
						|| (propertyType == short.class)) {
					writeMethod.invoke(entity, Short.parseShort(propertyValue));
				} else if ((propertyType == Integer.class)
						|| (propertyType == int.class)) {
					writeMethod.invoke(entity, Integer.parseInt(propertyValue));
				} else if ((propertyType == Long.class)
						|| (propertyType == long.class)) {
					writeMethod.invoke(entity, Long.parseLong(propertyValue));
				} else if ((propertyType == Float.class)
						|| (propertyType == float.class)) {
					writeMethod.invoke(entity, Float.parseFloat(propertyValue));
				} else if ((propertyType == Double.class)
						|| (propertyType == double.class)) {
					writeMethod.invoke(entity, Double
							.parseDouble(propertyValue));
				} else if ((propertyType == Boolean.class)
						|| (propertyType == boolean.class)) {
					writeMethod.invoke(entity, Boolean
							.parseBoolean(propertyValue));
				} else if ((propertyType == Character.class)
						|| (propertyType == char.class)) {
					writeMethod.invoke(entity, propertyValue.charAt(0));
				} else if (propertyType == Date.class) {
					if(propertyValue ==null || "".equals(propertyValue) || "null".equals(propertyValue.trim())){
						continue;
					}
					
					if(datePattern==null || "".equals(datePattern)){
						datePattern = "yyyy-MM-dd HH:mm:ss";
						if(propertyValue.length()==10)
							datePattern = "yyyy-MM-dd";
						else if(propertyValue.length() == 8){
							datePattern = propertyValue.contains(":")?"HH:mm:ss":"yyyyMMdd";						
						}else if(propertyValue.length() == 14){
							datePattern = "yyyyMMddHHmmss";
						}
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							datePattern);
					writeMethod.invoke(entity, dateFormat.parse(propertyValue));
				}
			} catch (IntrospectionException ex) {
				logger.info(ex);

				continue;
			}
		}

		return entity;
	}
	/**
	* ?????json???鴮????????????
	* @param jsonArrStr  e.g. ['get',1,true,null]
	* @return Object[]
	*/
	public static Object[] getArrFromJsonArrStr(String jsonArrStr) {
		return JSONArray.fromObject(jsonArrStr).toArray();
	}

	/**
	* ?????json???鴮????????????
	* @param jsonArrStr e.g. [{'name':'get'},{'name':'set'}]
	* @param clazz e.g. Person.class
	* @return Object[]
	*/
	public static Object[] getDtoArrFromJsonArrStr(String jsonArrStr, Class clazz) {
		JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);
		Object[] objArr = new Object[jsonArr.size()];
		for (int i = 0; i < jsonArr.size(); i++) {
			objArr[i] = JSONObject.toBean(jsonArr.getJSONObject(i), clazz);
		}
		return objArr;
	}

	/**
	* ?????json???鴮???????????飬????????????????????????Bean
	* @param jsonArrStr e.g. [{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]
	* @param clazz e.g. MyBean.class
	* @param classMap e.g. classMap.put("data", Person.class)
	* @return Object[]
	*/
	public static Object[] getDtoArrFromJsonArrStr(String jsonArrStr, Class clazz,
			Map classMap) {
		JSONArray array = JSONArray.fromObject(jsonArrStr);
		Object[] obj = new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, clazz, classMap);
		}
		return obj;
	}

	/**
	* ?????json???鴮?????????????????????
	* @param jsonArrStr  e.g. ['get',1,true,null]
	* @return List
	*/
	public static List getListFromJsonArrStr(String jsonArrStr) {
		JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);
		List list = new ArrayList();
		for (int i = 0; i < jsonArr.size(); i++) {
			list.add(jsonArr.get(i));
		}
		return list;
	}

	/**
	* ?????json???鴮???????????????????????Bean
	* @param jsonArrStr e.g. [{'name':'get'},{'name':'set'}]
	* @param clazz
	* @return List
	*/
	public static List getListFromJsonArrStr(String jsonArrStr, Class clazz) {
		JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);
		List list = new ArrayList();
		for (int i = 0; i < jsonArr.size(); i++) {
			list.add(JSONObject.toBean(jsonArr.getJSONObject(i), clazz));
		}
		return list;
	}

	/**
	* ?????json???鴮???????????????????????????????????Bean
	* @param jsonArrStr e.g. [{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]
	* @param clazz e.g. MyBean.class
	* @param classMap e.g. classMap.put("data", Person.class)
	* @return List
	*/
	public static List getListFromJsonArrStr(String jsonArrStr, Class clazz, Map classMap) {
		JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);
		List list = new ArrayList();
		for (int i = 0; i < jsonArr.size(); i++) {
			list.add(JSONObject.toBean(jsonArr.getJSONObject(i), clazz, classMap));
		}
		return list;
	}

	/**
	* ??json?????????map????
	* @param jsonObjStr e.g. {'name':'get','int':1,'double',1.1,'null':null}
	* @return Map
	*/
	public static Map getMapFromJsonObjStr(String jsonObjStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);

		Map map = new HashMap();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

	/**
	* ??json?????????map??????map?????????????????Bean
	* @param jsonObjStr e.g. {'data1':{'name':'get'},'data2':{'name':'set'}}
	* @param clazz e.g. Person.class
	* @return Map
	*/
	public static Map getMapFromJsonObjStr(String jsonObjStr, Class clazz) {
		JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);

		Map map = new HashMap();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, JSONObject.toBean(jsonObject.getJSONObject(key), clazz));
		}
		return map;
	}

	/**
	 * ??json?????????map??????map????????????????Bean?????????????Bean
	 * @param jsonObjStr e.g. {'mybean':{'data':[{'name':'get'}]}}
	 * @param clazz e.g. MyBean.class
	 * @param classMap	e.g. classMap.put("data", Person.class)
	 * @return Map
	 */
	public static Map getMapFromJsonObjStr(String jsonObjStr, Class clazz, Map classMap) {
		JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);

		Map map = new HashMap();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, JSONObject
					.toBean(jsonObject.getJSONObject(key), clazz, classMap));
		}
		return map;
	}

}
