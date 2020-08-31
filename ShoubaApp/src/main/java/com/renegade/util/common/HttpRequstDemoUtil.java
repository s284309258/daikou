package com.renegade.util.common;

import com.renegade.config.AjaxJson;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
 
/**
*短信API服务调用示例代码 �? 聚合数据
*在线接口文档：http://www.juhe.cn/docs/54
**/
 
public class HttpRequstDemoUtil {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    
 
    //配置您申请的KEY
    public static final String APPKEY ="1c289f73c944e04e826ea580a6c05a55";
    
    
    //2.随机验证码
  	public static String smsCode(){
  		//6位数随机数
  		String randow = new Random().nextInt(1000000)+"";
  		if(randow.length()==6){
  			return randow;
  		}else {
  			return smsCode();  //递归
  		}		
  	} 
 

  	
    /**
    *
    * @param strUrl 请求地址
    * @param params 请求参数
    * @param method 请求方法
    * @return  网络请求字符�?
    * @throws Exception
    */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                        out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
 
    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * 给账户发送短信验证码
     * @param account
     * @param code
     * @param ModelId 
     * @return
     */
	public static AjaxJson sendMesg(String account, String code, String ModelId) {
		AjaxJson ajaxJson=new AjaxJson();
        String result =null;
        String url ="http://v.juhe.cn/sms/send";
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("mobile",account);
        params.put("tpl_id",ModelId);
        params.put("tpl_value","#code#="+code);
        params.put("key",APPKEY);
        params.put("dtype","json");
        try {
            result =net(url, params, "GET");
            System.out.println(result);
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
            	ajaxJson.setSuccess(true);
//            	ajaxJson.setMsg(code);
            }else{
            	ajaxJson.setSuccess(false);
            }
            ajaxJson.setData(object.get("reason"));
        } catch (Exception e) {
        	ajaxJson.setData("发送异常");
            e.printStackTrace();
        }
        return ajaxJson;
	}
	
	/**
     * 给账户发送短信通知
     * @param account
     * @param code
     * @param ModelId 
     * @return
     */
	public static AjaxJson sendMesg(String account, String ModelId) {
		AjaxJson ajaxJson=new AjaxJson();
        String result =null;
        String url ="http://v.juhe.cn/sms/send";
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("mobile",account);
        params.put("tpl_id",ModelId);
        params.put("key",APPKEY);
        params.put("dtype","json");
        try {
            result =net(url, params, "GET");
            System.out.println(result);
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
            	ajaxJson.setSuccess(true);
//            	ajaxJson.setMsg(code);
            }else{
            	ajaxJson.setSuccess(false);
            }
            ajaxJson.setData(object.get("reason"));
        } catch (Exception e) {
        	ajaxJson.setData("发送异常");
            e.printStackTrace();
        }
        return ajaxJson;
	}
	
	/**
	 * 给系统管理员发送短信验证码
	 * @param uTel
	 * @param ModelId
	 * @return
	 */
	public static AjaxJson sendMsg(String uTel,String ModelId){
    	AjaxJson ajaxJson=new AjaxJson();
    	String randow = smsCode();
        String result =null;
        String url ="http://v.juhe.cn/sms/send";
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("mobile",uTel);
        params.put("tpl_id",ModelId);
        params.put("tpl_value","#code#="+randow);
        params.put("key",APPKEY);
        params.put("dtype","json");
        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
            	ajaxJson.setSuccess(true);
//            	ajaxJson.setMsg(randow);
            }else{
            	ajaxJson.setSuccess(false);
            }
            ajaxJson.setData(object.get("reason"));
        } catch (Exception e) {
        	ajaxJson.setData("发送异常");
            e.printStackTrace();
        }
        return ajaxJson;
    }
	
	
	public static void main(String[] args) {

	    sendMesg("18124242084", smsCode(), MesgTemplate.TRANS_BUY);
	}
	
}