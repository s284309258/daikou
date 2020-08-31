package com.renegade.config;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里云发送短信操作
 * 
 * @author Administrator
 *
 */
public class AliyunSendCode {

	private static final String REGION_ID = "cn-hangzhou";
	private static final String ACCESS_KEY = "LTAIH8v3EJJysuy8";
	private static final String ACCESS_KEY_SECRET = "ZZyBP7hsiljkMJwuAPFczmbcGfmZJH";
	private static final String ENDPOINT_NAME = "cn-hangzhou";
	private static final String PRODUCT = "Dysmsapi";
	private static final String DOMAIN = "dysmsapi.aliyuncs.com";

	/**
	 * 阿里云发送验证码接口
	 * 
	 * @param phone
	 * @param code
	 * @return
	 * @throws ClientException
	 */
	private static String sendMessage(String phone, String code) throws ClientException {
		// 初始化ascClient,暂时不支持多region（请勿修改
		DefaultProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY, ACCESS_KEY_SECRET);
		DefaultProfile.addEndpoint(REGION_ID, ENDPOINT_NAME, PRODUCT, DOMAIN);
		IAcsClient client = new DefaultAcsClient(profile);

		// 组装请求对象
		CommonRequest request = new CommonRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，
		// 批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
	/*	request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("官酝新零售");
		// 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
		request.setTemplateCode("SMS_161598094");
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\"" + code + "\"}");*/
		   request.setMethod(MethodType.POST);
	        request.setDomain("dysmsapi.aliyuncs.com");
	        request.setVersion("2017-05-25");
	        request.setAction("SendSms");
	        request.putQueryParameter("PhoneNumbers", "13545132758");
	        request.putQueryParameter("SignName", "官酝新零售");
	        request.putQueryParameter("TemplateCode", "SMS_161598094");
	        request.putQueryParameter("TemplateParam", "{\"code\": code}");
		  CommonResponse sendSmsResponse = client.getCommonResponse(request);
		if (sendSmsResponse.getData() != null && sendSmsResponse.getData().equals("OK")) {
			// 请求成功
			 System.out.println(sendSmsResponse.getData());
			return "success";
		} else {
			return "error";
		}

	}

	/**
	 * 发送验证码
	 * 
	 * @param phone 手机号
	 * @param code  验证码
	 * @return
	 */
	public static boolean sendCode(String phone, String code) {
		try {
			String result = sendMessage(phone, code);
			if ("success".equals(result)) {
				return true;
			} else {
				return false;
			}
		} catch (ClientException e) {
			e.printStackTrace();
			System.out.println("===================发送信息出现异常啦======================");
			return false;
		}
	}

	public static void main(String[] args) throws ClientException {
		String s = sendMessage("18268683982", "654321");
		System.out.println(s);
	}

}
