package com.renegade.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

public class PhoneSendCode {
	
	private static String Url = "http://v.juhe.cn/sms/send";
	private static String UrlInternational = "http://v.juhe.cn/smsInternational/send.php";
	private static String APPKEY = "9ad92753288415d971814ee0cceb073f";//"2c05ecc506abcd555ab4df35d1dbe7ed";
	private static String APPKEYInternational = "e16a5f3402121d9b239b659d76d70afd";
	public static String mobanId="199237";

	private final static int codeLength = 6;

	/**
	 * @see 产生随机验证码
	 * @return 验证码字符串
	 */
	public static String getCode() {

		Random rand = new Random();
		int a;
		String result = "";
		for (int j = 0; j < codeLength; j++) {
			a = Math.abs(rand.nextInt() % 9);
			result += String.valueOf(a);
		}
		return result;
	}

	/**
	 * 调用短信接口，发送短信
	 * @param phone 手机号
	 * @return 发送状态 success成功   error失败
	 */
	public static String sendCode(String phone,String code) {
		System.out.println("短信消息开始发送：接受手机号："+phone +"，验证码："+code);
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);
		client.getParams().setContentCharset("utf-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
		try {
			String context = URLEncoder.encode("#code#="+code, "utf-8");
			NameValuePair[] data = { // 提交短信参数
					new NameValuePair("mobile", phone), new NameValuePair("tpl_id", "220395"),//短息模板
					new NameValuePair("tpl_value", context), new NameValuePair("key", APPKEY), };
			method.setRequestBody(data);
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			JSONObject json =  JSONObject.parseObject(SubmitResult);
			Map<String,Object> jsonMap = json;
			String error_code = jsonMap.get("error_code").toString();
			String reason = jsonMap.get("reason").toString();
			System.out.println(reason);
			if ("0".equals(error_code)) {
				result = "success";
			} else {
				result = reason;
			}
		} catch (HttpException e) {
			System.out.println("发送短信失败："+e);
			result = "error";
		} catch (IOException e) {
			System.out.println(e);
			result = "error";
		}
		return result;
	}
	/**
	 * 调用短信接口，发送短信
	 * @param phone 手机号
	 * @return 发送状态 success成功   error失败
	 */
	public static String sendCode(String phone,String template,int none) {
		System.out.println("短信消息开始发送");
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);
		client.getParams().setContentCharset("utf-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
		try {
//			String context = URLEncoder.encode("#code#="+code, "utf-8");
			NameValuePair[] data = { // 提交短信参数
					new NameValuePair("mobile", phone), new NameValuePair("tpl_id", template),//短息模板221820
					new NameValuePair("key", APPKEY), };
			method.setRequestBody(data);
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			JSONObject json =  JSONObject.parseObject(SubmitResult);
			Map<String,Object> jsonMap = json;
			String error_code = jsonMap.get("error_code").toString();
			String reason = jsonMap.get("reason").toString();
			System.out.println(reason);
			if ("0".equals(error_code)) {
				result = "success";
			} else {
				result = reason;
			}
		} catch (HttpException e) {
			System.out.println("发送短信失败："+e);
			result = "error";
		} catch (IOException e) {
			System.out.println(e);
			result = "error";
		}
		return result;
	}
	/**
	 * 调用短信接口，发送国际短信短信
	 * @param phone 手机号   areaNum 国际代码
	 * @return 发送状态 success成功   error失败
	 */
	public static String sendCode(String phone,String code,String areaNum) {
		System.out.println("短信消息开始发送：接受手机号："+phone +"，验证码："+code+"，区域代码"+areaNum);
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(UrlInternational);
		client.getParams().setContentCharset("utf-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
		try {
			String context = URLEncoder.encode("#code#="+code, "utf-8");
			NameValuePair[] data = { // 提交短信参数
					new NameValuePair("mobile", phone), new NameValuePair("tplId", "10116"),//短息模板
					new NameValuePair("tplValue", context), new NameValuePair("key", APPKEYInternational),new NameValuePair("areaNum", areaNum) };
			method.setRequestBody(data);
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			JSONObject json =  JSONObject.parseObject(SubmitResult);
			Map<String,Object> jsonMap = json;
			String error_code = jsonMap.get("error_code").toString();
			String reason = jsonMap.get("reason").toString();
			System.out.println(reason);
			if ("0".equals(error_code)) {
				result = "success";
			} else {
				result = reason;
			}
		} catch (HttpException e) {
			System.out.println("发送短信失败："+e);
			result = "error";
		} catch (IOException e) {
			System.out.println(e);
			result = "error";
		}
		return result;
	}
	
	
	/**
	 * 调用短信接口，发送短信
	 * @param phone 手机号
	 * @param mobanId 短信模板Id
	 * @return 发送状态 success成功   error失败
	 */
	public static String sendCodeParams(String phone,String code,String mobanId) {
		System.out.println("短信消息开始发送：接受手机号："+phone +"，验证码："+code);
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);
		client.getParams().setContentCharset("utf-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
		try {
			String context = URLEncoder.encode("#code#="+code, "utf-8");
			NameValuePair[] data = { // 提交短信参数
					new NameValuePair("mobile", phone), new NameValuePair("tpl_id", mobanId),//短息模板
					new NameValuePair("tpl_value", context), new NameValuePair("key", APPKEY), };
			method.setRequestBody(data);
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			JSONObject json =  JSONObject.parseObject(SubmitResult);
			Map<String,Object> jsonMap = json;
			String error_code = jsonMap.get("error_code").toString();
			String reason = jsonMap.get("reason").toString();
			System.out.println(reason);
			if ("0".equals(error_code)) {
				result = "success";
			} else {
				result = reason;
			}
		} catch (HttpException e) {
			System.out.println("发送短信失败："+e);
			result = "error";
		} catch (IOException e) {
			System.out.println(e);
			result = "error";
		}
		return result;
	}
}
