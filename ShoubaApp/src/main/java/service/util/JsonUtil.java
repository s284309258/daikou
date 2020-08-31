package service.util;

import net.sf.json.*;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


public class JsonUtil {
	/** * logger. */
	private static Log logger = LogFactory.getLog(JsonUtil.class);
	public final static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static SimpleDateFormat SDF = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
	
	/** * ????????????????????. */
	protected JsonUtil() {
	}

	
	
	/**
	 * write.
	 * 
	 * @param bean
	 *            obj
	 * @param writer
	 *            ?????
	 * @param excludes
	 *            ???????????????
	 * @param datePattern
	 *            date??string???????
	 * @throws Exception
	 *             д??????????????
	 */
	public static void write(Object bean, Writer writer, String[] excludes,
			String datePattern) throws Exception {
		JsonConfig jsonConfig = configJson(excludes, datePattern);

		JSON json = JSONSerializer.toJSON(bean, jsonConfig);

		json.write(writer);
	}

	/**
	 * ????json-lib?????excludes??datePattern.
	 * 
	 * @param excludes
	 *            ??????????????????
	 * @param datePattern
	 *            ?????????
	 * @return JsonConfig ????excludes??dataPattern?????jsonConfig??????write
	 */
	public static JsonConfig configJson(String[] excludes, String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor(datePattern));

		return jsonConfig;
	}
	
	/**
	 * ???????????????json????????????β????
	 * ????json-lib?????excludes??datePattern.
	 * 
	 * @param excludes
	 *            ??????????????????
	 * @param datePattern
	 *            ?????????
	 * @return JsonConfig ????excludes??dataPattern?????jsonConfig??????write
	 */
	public static JsonConfig configJson2(String[] excludes, String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor(datePattern));
		//????json????????????β????
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			
			public boolean apply(Object source, String name, Object value) {
				if (value == null || "".equals(value)) {
                    return true;
                }
                return false;
			}
		});
		return jsonConfig;
	}

	/**
	 * data={"id":"1"}??json??????????????pojo.
	 * ???????????key-value???????????bean?е??????
	 * @param <T>
	 *            Object
	 * @param data
	 *            json?????
	 * @param clazz
	 *            ????????bean?????????
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
	public static <T extends Object> T json2Bean(String data, Class<T> clazz,
			String[] excludes, String datePattern) throws Exception {
		// JsonUtils.configJson(excludes, datePattern);
		T entity = clazz.newInstance();

		return json2Bean(data, entity, excludes, datePattern);
	}
	
	
	/**
	 * data={"id":"1"}??json????????????????pojo.
	 * ???????????key-value???????????bean?е??????
	 * @param <T>
	 *            Object
	 * @param data
	 *            json?????
	 * @param entity
	 *            ???????????bean
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
	public static <T extends Object> T json2Bean(String data, T entity,
			String[] excludes, String datePattern) throws Exception {
		// JsonUtils.configJson(excludes, datePattern);
		JSONObject jsonObject = JSONObject.fromObject(data);

		return json2Bean(jsonObject, entity, excludes, datePattern);
	}

	
	/**
	 * ????Class????entity?????JSONObject?е??????????.
	 * 
	 * @param <T>
	 *            Object
	 * @param jsonObject
	 *            json????
	 * @param clazz
	 *            ????????bean?????????
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
			Class<T> clazz, String[] excludes, String datePattern)
			throws Exception {
		// JsonUtils.configJson(excludes, datePattern);
		T entity = clazz.newInstance();

		return json2Bean(jsonObject, entity, excludes, datePattern);
	}

	/**
	 * ??JSONObject?е????????entity??. 
	 * ???????????key-value???????????bean?е??????
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
			T entity, String[] excludes, String datePattern) throws Exception {
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
	 * data=[{"id":"1"},{"id":2}]??json????????????pojo????.
	 * 
	 * @param <T>
	 *            Object
	 * @param data
	 *            json?????
	 * @param clazz
	 *            ????????node?????????
	 * @param excludes
	 *            ??????????????????
	 * @param datePattern
	 *            ?????????
	 * @return List
	 * @throws Exception
	 *             java.lang.InstantiationException,
	 *             java.beans.IntrospectionException,
	 *             java.lang.IllegalAccessException
	 */
	public static <T extends Object> List<T> json2List(String data,
			Class<T> clazz, String[] excludes, String datePattern)
			throws Exception {
		JSONArray jsonArray = JSONArray.fromObject(data);

		return json2List(jsonArray, clazz, excludes, datePattern);
	}

	/**
	 * data=[{"id":"1"},{"id":2}]??json????????????pojo????.
	 * 
	 * @param <T>
	 *            Object
	 * @param jsonArray
	 *            JSONArray
	 * @param clazz
	 *            ????????node?????????
	 * @param excludes
	 *            ??????????????????
	 * @param datePattern
	 *            ?????????
	 * @return List
	 * @throws Exception
	 *             java.lang.InstantiationException,
	 *             java.beans.IntrospectionException,
	 *             java.lang.IllegalAccessException
	 */
	public static <T extends Object> List<T> json2List(JSONArray jsonArray,
			Class<T> clazz, String[] excludes, String datePattern)
			throws Exception {
		List<T> list = new ArrayList<T>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			T node = json2Bean(jsonObject, clazz, excludes, datePattern);
			list.add(node);
		}

		return list;
	}
	
	public static String listToJson(List list){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		//???json-lib??????????????
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		JSONArray json = JSONArray.fromObject(list, jsonConfig);

		return json.toString();
		
	}

	/**
	 * ???????????????ζ????????????????
	 * @Title: toStrJson 
	 * @Description: TODO 
	 * @param obj
	 * @param unMethods
	 * @return 
	 * @return String
	 */
	@Deprecated
	public static String toStrJson(Object obj, String unMethods){
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if(null != obj){
			Class cl = obj.getClass();
			Field[] fields = cl.getDeclaredFields();
			Method[] methods = cl.getDeclaredMethods();
			StringBuffer con = new StringBuffer();
			for(int i=0; i<fields.length; i++){
				Field fd = fields[i];
				try {
					String fieldName = fd.getName();
					String fieldGetName = parGetName(fd.getName());
					if (!checkGetMet(methods, fieldGetName)) {
						continue;
					}
					Method fieldGetMet = cl.getMethod(fieldGetName, new Class[] {});
					if(!unMethods.contains(fieldGetMet.getName())){
						Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
						con.append(",");
						if(null != fieldVal){
							if(fieldGetMet.getReturnType().equals(java.util.Date.class)){
								con.append("\""+fieldName+"\":\"" + SDF.format((Date) fieldVal) +"\"");
							} else if(fieldGetMet.getReturnType().equals(java.lang.String.class)){
								con.append("\""+fieldName+"\":\"" + stringToJson(fieldVal.toString()) +"\"");
							} else
								con.append("\""+fieldName+"\":" + fieldVal);
						} else {
							con.append("\""+fieldName+"\":" + null);
						}
					}
				} catch (Exception e) {
					
				}
			}
			if(!Strings.isNullOrEmpty(con.toString())){
				buf.append(con.toString().substring(1));
			}
		}
		buf.append("}");
		return buf.toString();
	}
	
	
	private static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}
	
	    private static boolean checkGetMet(Method[] methods, String fieldGetMet) {
			for (Method met : methods) {
				if (fieldGetMet.equals(met.getName())) {
					return true;
				}
			}
			return false;
		}
	    
	    
	    private static String stringToJson(String s) {
	        StringBuffer sb = new StringBuffer();
	        for (int i=0; i<s.length(); i++) {
	            char c = s.charAt(i);
	            switch (c) {
	            case '\"':
	                sb.append("\\\"");
	                break; 
	            case '\\':   
	                sb.append("\\\\");
	                break;
	            case '/':
	                sb.append("\\/");
	                break;
	            case '\b':      
	                sb.append("\\b");
	                break;
	            case '\f':      
	                sb.append("\\f");
	                break;
	            case '\n':
	                sb.append("\\n"); 
	                break;
	            case '\r':     
	                sb.append("\\r");
	                break;
	            case '\t':      
	                sb.append("\\t");
	                break;
	            default:
	                sb.append(c);
	            }
	        }
	        return sb.toString();
	     }
	    
	    /**
	     * json?????bean??????????е????????
	     * @Title: toBean 
	     * @Description: TODO 
	     * @param json
	     * @param beanClass
	     * @return
	     * @throws Exception 
	     * @return Object
	     */
	    public static Object toBean(String json, Class beanClass) throws Exception {
			Object obj = null;
			try {
				JSONObject jsonObject = JSONObject.fromObject(json);
				obj = JSONObject.toBean(jsonObject, beanClass);
				
				
			} catch (Exception e) {			
				throw e;
			}
			
			return obj;
		}
	    
	    public static String toJson(Object obj) {
			String json = JSONObject.fromObject(obj,
					configJson2(new String[] {}, DEFAULT_DATETIME_FORMAT))
					.toString();
			return json;
		}
	    /**
	     * 
	     * @Title: toJson 
	     * @Description: TODO 
	     * @param obj
	     * @param excludes ????????get?????????????roperty 'fail' of class test.TestBean has no read method. SKIPPED???? ???磺getFail(), ????д??Fail??
	     * @return String
	     */
	    public static String toJson(Object obj,String[] excludes ) {
			String json = JSONObject.fromObject(obj,
					configJson2(excludes, DEFAULT_DATETIME_FORMAT))
					.toString();
			return json;
		}
	    
	    public static String toJson(Object obj,String[] excludes,Map classMap ) {
			String json = JSONObject.fromObject(obj,
					configJson2(excludes, DEFAULT_DATETIME_FORMAT))
					.toString();
			return json;
		}
	    
	    public static String toJson(Object obj,String dateFormat) {
	    	String[] excludes = {};
	    	JsonConfig config = JsonUtil.configJson(excludes, dateFormat);
	    	String json = JSONObject.fromObject(obj,config).toString();
	    	return json;
	    }
    	
	    
}
