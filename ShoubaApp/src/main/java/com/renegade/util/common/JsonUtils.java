/**
 * 
 */
package com.renegade.util.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

/**
 * json工具类
 * @author huming.wang
 * 2016-5-23 下午07:55:34 
 * com.huifu.util.httpClient.JsonUtils
 */
public class JsonUtils {

    /**
     * 通过字符串转换为MAP(以页面传递的参数为key 例如：{"OperId":"13818930251"}" key为OperId )
     * @param jsonStr
     * @return
     */
    public static Map<String, String> readJSON2MapUnderScore(String jsonStr) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setPropertyNamingStrategy(new UnderScoreCaseNamingStrategy());
        JsonNode node = objectMapper.readTree(jsonStr);
        Map<String, String> map = new HashMap<String, String>();
        Entry<String, JsonNode> jsonNode = null;
        for (Iterator<Entry<String, JsonNode>> iterator = node.getFields(); iterator.hasNext();) {
            jsonNode = iterator.next();
            map.put(jsonNode.getKey(), jsonNode.getValue().asText());
        }
        return map;
    }
    
    
    /**
     * json字符串取值并且解码
     * @param jsonString
     * @param object
     * @return
     */
    public static String getResult(String jsonString,String object){
		//获得返回参数的jsonString
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
        String result1 = jsonObject.getString(object);
        //getbg_ret_url解码
        String result=UrlCodeUtil.getUrlDecode(result1);
        return result;
	}
    
    
    /**
     * json字符串取值
     * @param jsonString
     * @param object
     * @return
     */
    public static String jsonToString(String jsonString,String object) {
    	if(jsonString.contains(object)) {
    		//获得返回参数的jsonString
        	JSONObject jsonObject = JSONObject.fromObject(jsonString);
        	return jsonObject.getString(object);
    	}else {
    		return null;
    	}
    }
    
    
    /**
     * 字符串转成json对象
     * @param jsonString
     * @return
     */
    public static Object getJsonObject(String jsonString) {
    	return JSONArray.parse(jsonString);
    }
    
    
    /**
     * json字符串转集合对象
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
    
    
    /**
     * json字符串转换成List类型，例如：List<Map<String, Object>>
     * @param jsonString
     * @return
     */
    public static List jsonToList(String jsonString) {
    	Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List>() {}.getType());
    }
    
    
    public static String json(String jsonArray,String object) {
    	JSONObject jsonObject = JSONObject.fromObject(jsonArray);
        //取出json中的data数据
        JSONObject result = jsonObject.getJSONObject("result");
        JSONObject jsonresultObject = JSONObject.fromObject(result);
    	return jsonObject.getString(object);//x的值，y类似
    }
    
    
    public static void main(String[] args) {
    	String id="1";
    	String belongMerId="09062512";
    	String extension = "{\"id\":"+id+",\"belongMerId\":\'"+belongMerId+"'}";
    	/*String str="{\"id\":\"289\",\"withHoldFeeAmt\":\"96.00\",\"belongMerId\":09062512}";*/
    	String result=JsonUtils.jsonToString(extension, "belongMerId");
    	System.out.println(result);
	}
    
}
