package com.merchant.demo.client;

import com.merchant.demo.base.Constants;
import com.merchant.demo.base.ConstantsClient;
import com.merchant.util.Base64;
import com.merchant.util.Log;
import com.merchant.util.Tools;
import com.merchant.util.Xml;
import com.merchant.util.http.HttpClient;
import com.merchant.util.rsa.Signatory;

import java.net.URLEncoder;

/**
 * 【短信+API接口版本】商户对接下单接口封装
 * 易联服务器交易接口调用API封装，分别对以下接口调用进行了封装；
 * 接口封装了参数的转码（中文base64转码）、签名和验证签名、通讯和通讯报文处理
 * 1、【短信+API接口版本】的短信验证发送接口
 * 2、【短信+API接口版本】的无磁无密交易接口
 */
public class TransactionClientSmsApi {




	/**
	 * 描述：短信码检查通讯方法
	 * @param merchOrderId:	         商户订单号
	 * @param tradeTime:	         商户订单提交时间
	 * @param mobileNo:	                  手机号码
	 * @param smParam:	                  短信参数
	 * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return:                 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception:       E101:通讯失败； E102：签名验证失败；  E103：签名失败。
	 */
	public static String apiSmsCodeCheck(String merchOrderId, 
			String tradeTime, String mobileNo, String smParam, Xml retXml) 
			throws Exception{
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 交易码
		String tradeCode = Constants.PAYECO_API_SMS_CODE_CHECK;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 验证短信的交易码
		String verifyTradeCode = Constants.PAYECO_API_PAY;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
		String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
		
	    // 1.进行数据拼接
	    String srcData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId="
                + merchOrderId  + "&MobileNo=" + mobileNo + "&TradeTime=" + tradeTime  
                + "&VerifyTradeCode=" + verifyTradeCode + "&SmParam=" + smParam;
	    
	    // 2.私钥签名
		Log.println(" 私钥 =" + priKey);
		Log.println(" 原数据 =" + srcData);
	    String sign = Signatory.sign(priKey, srcData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名失败退出
			throw new Exception("E103");
		}
		Log.println(" 签名 =" + sign);

		// 3.提交参数包含中文的需要做base64转码
		String smParam64 = Base64.encodeBytes(smParam.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
	    String srcData64 = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId="
                + merchOrderId  + "&MobileNo=" + mobileNo + "&TradeTime=" + tradeTime  
                + "&VerifyTradeCode=" + verifyTradeCode + "&SmParam=" + smParam64;

	    // 4.组装通讯数据
		String url= Constants.PAYECO_URL + "/ppi/merchant/itf.do"; 
		srcData64 = "TradeCode=" + tradeCode + "&" + srcData64 + "&Sign=" + sign;
		HttpClient httpClient = new HttpClient();
		Log.println("url = " + url + "?" + srcData64);
		
		// 5.发往易联
		String retStr = httpClient.send(url, srcData64, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println(" 易联返回数据 ="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败退出
			throw new Exception("E101");
		}

		// 6.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		
		// 7.响应异常，退出
		if(!ConstantsClient.PAYECO_PLAT_CODE_SUCCESS_VAL.equals(retCode)){
			return retCode;
		}
		
		// 8.获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
	    String retSmsFlag = Tools.getXMLValue(retStr, "SmsFlag");
		String retSmId = Tools.getXMLValue(retStr, "SmId");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retNeedPwd= Tools.getXMLValue(retStr, "NeedPwd");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 9.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setSmsFlag(retSmsFlag);
		retXml.setSmId(retSmId);
		retXml.setNeedPwd(retNeedPwd);
		retXml.setSign(retSign);
		
		// 10.验证签名的字符串
        String backData = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" 
                + retMerchOrderId + "&SmsFlag="+ retSmsFlag + "&SmId=" + retSmId + "&TradeTime="+retTradeTime+"&NeedPwd="+retNeedPwd;
        Log.println(" 验签数据 ="+backData);
        // 替换空格为+号
        retSign = retSign.replaceAll(" ", "+");
        
		// 11.验证签名
		boolean b = Signatory.verify(pubKey, backData, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验证数据 =" + backData);
		Log.println(" 易联返回签名 =" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 验签失败退出
			throw new Exception("E102");
		}
		return retCode;
	}
	
	/**
	 * 描述：重发短信通讯方法
	 * @param merchOrderId:	         商户订单号
	 * @param smId:             短信凭证码
	 * @param tradeTime:	         商户订单提交时间
	 * @param mobileNo:	                  手机号码
	 * @param smParam:	                  短信参数
	 * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return:                 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception:       E101:通讯失败； E102：签名验证失败；  E103：签名失败。
	 */
	public static String apiSmsAgain(String merchOrderId, 
			String smId, String tradeTime, Xml retXml) 
					throws Exception{
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 交易码
		String tradeCode = Constants.PAYECO_API_SMS_AGAIN;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
		String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
		
		// 1.进行数据拼接
		String srcData = "Version="+ version + "&MerchantId=" + merchantId + "&MerchOrderId="
	            + merchOrderId + "&SmId=" + smId + "&TradeTime=" + tradeTime ;
		
		// 2.私钥签名
		Log.println(" 私钥 =" + priKey);
		Log.println(" 原数据 =" + srcData);
		String sign = Signatory.sign(priKey, srcData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名失败退出
			throw new Exception("E103");
		}
		Log.println(" 私钥签名 =" + sign);
		
		// 4.组装通讯数据
		String url= Constants.PAYECO_URL + "/ppi/merchant/itf.do"; 
		srcData = "TradeCode=" + tradeCode + "&" + srcData + "&Sign=" + sign;
		HttpClient httpClient = new HttpClient();
		Log.println("url = " + url + "?" + srcData);
		
		// 5.发往易联
		String retStr = httpClient.send(url, srcData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println(" 易联返回结果 ="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败退出
			throw new Exception("E101");
		}
		
		// 6.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		
		// 7.响应异常，退出
		if(!ConstantsClient.PAYECO_PLAT_CODE_SUCCESS_VAL.equals(retCode)){
			return retCode;
		}
		
		// 8.获取返回数据
		String retVersion = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retSmId = Tools.getXMLValue(retStr, "SmId");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retComplated = Tools.getXMLValue(retStr, "Complated");
		String retRemain = Tools.getXMLValue(retStr, "Remain");
		String retExpTime = Tools.getXMLValue(retStr, "ExpTime");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 9.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVersion);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setSmId(retSmId);
		retXml.setTradeTime(retTradeTime);
		retXml.setComplated(retComplated);
		retXml.setRemain(retRemain);
		retXml.setExpTime(retExpTime);
		retXml.setSign(retSign);
		
		// 10.验证签名的字符串
        String backData = "Version="+retVersion+"&MerchantId=" + retMerchantId +"&SmId=" + retSmId 
                + "&MerchOrderId=" + retMerchOrderId + "&TradeTime="+retTradeTime 
                + "&Complated=" + retComplated + "&Remain=" + retRemain + "&ExpTime=" + retExpTime;
		Log.println(" 验签数据 ="+backData);
		// 替换空格为+号
		retSign = retSign.replaceAll(" ", "+");
		
		// 11.验证签名
		boolean b = Signatory.verify(pubKey, backData, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验签数据 =" + backData);
		Log.println(" 易联返回签名 =" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 验签失败退出
			throw new Exception("E102");
		}
		return retCode;
	}
		
    /**
     * 描述：支付接口
     * @param industryId:       商户行业编号; 未上送此字段时，系统将使用商户配置中对应的行业
     * @param merchOrderId:     商户订单号
     * @param amount:           商户订单金额，单位为元，格式： nnnnnn.nn
     * @param orderDesc:        商户订单描述    字符最大128，中文最多40个；参与签名：采用UTF-8编码
     * @param tradeTime:        商户订单提交时间，格式：yyyyMMddHHmmss，超过订单超时时间未支付，订单作废；不提交该参数，采用系统的默认时间（从接收订单后超时时间为30分钟）
     * @param expTime:          交易超时时间，格式：yyyyMMddHHmmss， 超过订单超时时间未支付，订单作废；不提交该参数，采用系统的默认时间（从接收订单后超时时间为30分钟）
     * @param extData:          商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码
     * @param miscData:         订单扩展信息   根据不同的行业，传送的信息不一样；参与签名：采用UTF-8编码
     * @param notifyFlag:       订单通知标志    0：成功才通知，1：全部通知（成功或失败）  不填默认为“1：全部通知”
     * @param smId:             短信凭证号
     * @param smCode:           短信验证码
     * @param pwd               密码(请根据文档《易联支付插件产品_商户接口报文_短信+API接口新版_V2.0.docx》的附录"密码加密说明"进行加密)
     * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
     * @return 处理状态码:        0000 : 处理成功， 其他： 处理失败
     * @throws Exception        E101:通讯失败； E102：签名验证失败；  E103：签名失败；
     */
    public static String apiPay(String industryId, String merchOrderId, String amount, 
    		String orderDesc, String tradeTime, String expTime, String extData, 
    		String miscData, String notifyFlag, String smId, String smCode, String pwd,
    		Xml retXml) throws Exception {
    	// 商户号
    	String merchantId = Constants.MERCHANT_ID;
    	// 交易参数
		String tradeCode = Constants.PAYECO_API_PAY;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
	    String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
	    // 异步通知地址
	    String notifyUrl = Constants.MERCHANT_NOTIFY_URL; 
	    // 易联服务器地址
	    String payecoUrl = Constants.PAYECO_URL;
		
		// 1.进行数据签名  
		String srcData = "Version="+version+"&MerchantId=" + merchantId + "&IndustryId=" + industryId 
				+ "&MerchOrderId=" + merchOrderId + "&Amount=" + amount + "&OrderDesc=" + orderDesc 
				+ "&TradeTime=" + tradeTime + "&ExpTime=" + expTime + "&NotifyUrl=" + notifyUrl 
				+ "&ExtData=" + extData + "&MiscData=" + miscData + "&NotifyFlag=" + notifyFlag
				+ "&SmId=" + smId + "&SmCode=" + smCode;
		
		if(pwd!=null && !"".equals(pwd)){
			srcData+="&pwd="+pwd;
		}else {
			srcData+="&pwd=";
		}
		
		// 2.私钥签名
		Log.println(" 私钥 = " + priKey);
		Log.println(" 原数据 = " + srcData);
		String sign = Signatory.sign(priKey, srcData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名失败退出
			throw new Exception("E103");
		}
		Log.println(" 签名 =" + sign);

		// 3.提交参数包含中文的需要做base64转码
		String orderDesc64 = Base64.encodeBytes(orderDesc.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
		String extData64 = Base64.encodeBytes(extData.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
		String miscData64 = Base64.encodeBytes(miscData.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
		
		// 4.通知地址做URLEncoder处理
		String notifyUrlEn = URLEncoder.encode(notifyUrl, ConstantsClient.PAYECO_DATA_ENCODE);
		
		String srcData64 = "Version="+version+"&MerchantId=" + merchantId + "&IndustryId=" + industryId 
				    + "&MerchOrderId=" + merchOrderId + "&Amount=" + amount + "&OrderDesc=" + orderDesc64 
				    + "&TradeTime=" + tradeTime + "&ExpTime=" + expTime + "&NotifyUrl=" + notifyUrlEn 
				    + "&ExtData=" + extData64 + "&MiscData=" + miscData64 + "&NotifyFlag=" + notifyFlag
   				 	+ "&SmId=" + smId + "&SmCode=" + smCode;
		
		if(pwd!=null && !"".equals(pwd)){
			srcData64+="&pwd="+pwd;
		}else {
			srcData64+="&pwd=";
		}

		// 5.通讯报文
		String url= payecoUrl + "/ppi/merchant/itf.do";
		srcData64 = "TradeCode=" + tradeCode + "&" + srcData64 + "&Sign=" + sign;
		HttpClient httpClient = new HttpClient();
		Log.println(" url = " + url + "?" + srcData64);
		String retStr = httpClient.send(url, srcData64, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println(" 易联返回结果 ="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败
			throw new Exception("E101");
		}

		// 6.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			// 响应异常，退出
			return retCode;
		}
		
		// 7.获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retExtData = Tools.getXMLValue(retStr, "ExtData");
		if (retExtData != null){
			retExtData = retExtData.replaceAll(" ", "+");
			retExtData = new String (Base64.decode(retExtData), ConstantsClient.PAYECO_DATA_ENCODE);
		}
		String retOrderId = Tools.getXMLValue(retStr, "OrderId");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retPayTime = Tools.getXMLValue(retStr, "PayTime");
		String retSettleDate = Tools.getXMLValue(retStr, "SettleDate");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 8.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setExtData(retExtData);
		retXml.setOrderId(retOrderId);
		retXml.setStatus(retStatus);
		retXml.setPayTime(retPayTime);
		retXml.setSettleDate(retSettleDate);
		retXml.setSign(retSign);
		  
		// 9.验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
		  + "&Amount=" + retAmount + "&ExtData=" + retExtData + "&OrderId=" + retOrderId
		  + "&Status=" + retStatus + "&PayTime=" + retPayTime + "&SettleDate=" + retSettleDate;

		// 10.验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验签数据 =" + backSign);
		Log.println(" 易联返回签名 =" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 公钥验签失败退出
			throw new Exception("E102");
		}
		return retCode;
	}

    


}
