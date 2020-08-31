package com.renegade.util.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class RequestForPHP {
	
	/**
	 * php查询交易结果的请求
	 * @param url
	 * @param map
	 * @return
	 */
	 public static Map<String, Object> KuaYu(String url,Map<String , Object> map){  
		 Map<String, Object> reMap = new HashMap<String, Object>();  
		 try{  
			 if(map.size() == 0){  
				 reMap.put("status", "fail");    
				 reMap.put("mesg", "无数据需要发送");    
				 return reMap;  
			 }  
			 HttpClient client = new HttpClient();    
			 //返回结果集    
			 net.sf.json.JSONObject resJson = new net.sf.json.JSONObject();    
			 PostMethod postMethod = new PostMethod(url);  
			 client.getParams().setSoTimeout(15000); 
			 Part[] parts = new Part[map.size()]; 
			 Map<String , Object> mapParas = map;
			 int i =0;
			 for(Map.Entry<String, Object> entry : mapParas.entrySet()){     
				 parts[i] = new StringPart(entry.getKey().trim(), entry.getValue().toString());
				 i++;
			 }       
	         //对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装    
	         MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());    
	         postMethod.setRequestEntity(mre);    
	         //执行请求，返回状态码      
	         int status = client.executeMethod(postMethod); 
	         System.out.println("请求返回状态码："+status);
	         if (status == HttpStatus.SC_OK) {    
	        	 String result = postMethod.getResponseBodyAsString();   
	        	//汉字需要编码成UTF-8     
	             result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
	             resJson = net.sf.json.JSONObject.fromObject(result);  
	             Iterator iterator = resJson.keys();
	             String key = null;
	             String value = null;
	             while(iterator.hasNext()){
	            	 key = (String) iterator.next();
	            	 value = resJson.getString(key);
	            	 reMap.put(key,value);
	             }
	         } else { 
	        	 //接口请求失败。
	             reMap.put("status", "fail");    
	             reMap.put("mesg", "请求失败。");    
	         }    
		 }catch(Exception e){  
			 e.printStackTrace();  
	         reMap.put("status", "fail");    
	         reMap.put("mesg", "请求超时。");    
		 }  
		 return reMap;  
	 } 
	 
	 
	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	 public static JSONObject doPost(String url, JSONObject json) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = JSONObject.fromObject(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}
	
	 
}
