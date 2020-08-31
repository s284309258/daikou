package com.merchant.demo.client;

import com.merchant.demo.base.Constants;
import com.merchant.demo.base.ConstantsClient;
import com.merchant.util.Base64;
import com.merchant.util.Log;
import com.merchant.util.Tools;
import com.merchant.util.Xml;
import com.merchant.util.http.HttpClient;
import com.merchant.util.rsa.Signatory;


/**
 * 描述:商户对接通用接口封装
 * 1、商户订单查询接口:             适用于所有对接方式
 * 2、商户订单冲正接口:             除【互联网金融】行业外的所有接入方式
 * 3、商户订单退款申请接口 :          除【互联网金融】行业外的所有接入方式
 * 4、商户订单退款结果查询接口 : 		 除【互联网金融】行业外的所有接入方式
 * 5、验证订单结果通知签名:          适用于所有对接方式
 * 6、互联网金融行业银行卡解除绑定接口: 仅适合于【互联网金融】行业的商户
 */
public class TransactionClientCommon {
	/**
	 * 描述:商户订单查询接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId:     商户订单号
	 * @param tradeTime:        商户订单提交时间
	 * @param payecoUrl:        易联服务器URL地址，只需要填写域名部分
	 * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return:                 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception:       E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderQuery(String merchantId, String merchOrderId, 
			String tradeTime, Xml retXml) 
			throws Exception{
		// 订单查询接口交易码
		String tradeCode = Constants.PAYECO_QUERY_ORDER;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
	    String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
	    // 易联服务器地址
	    String payecoUrl = Constants.PAYECO_URL;
	    
	    // 1.进行数据签名
	    String srcData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	             + "&TradeTime=" + tradeTime;
	    
	    // 2.私钥签名
		Log.println(" 私钥 =" + priKey);
		Log.println(" 数据 =" + srcData);
	    String sign = Signatory.sign(priKey, srcData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名出错，退出
			throw new Exception("E103");
		}
		Log.println(" 签名 =" + sign);

		// 3.组装通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    srcData = srcData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    Log.println(" url="+url+"&"+srcData);
		String retStr = httpClient.send(url, srcData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println(" 易联返回数据 ="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败，退出
			throw new Exception("E101");
		}

		// 4.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			// 响应失败，退出
			return retCode;
		}
		
		// 5.获取返回数据
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
		
		// 6.设置返回数据
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
		  
		// 7.验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
		  + "&Amount=" + retAmount + "&ExtData=" + retExtData + "&OrderId=" + retOrderId
		  + "&Status=" + retStatus + "&PayTime=" + retPayTime + "&SettleDate=" + retSettleDate;

		// 8.验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验证签名的字符串 =" + backSign);
		Log.println(" 易联返回签名 =" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 验签失败，退出
			throw new Exception("E102");
		}
		return retCode;
	}
	
	
	/**
	 * 商户订单冲正接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param amount        :   订单金额
	 * @param tradeTime		:	商户订单提交时间
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderReverse(String merchantId, String merchOrderId, 
			String amount, String tradeTime, Xml retXml) throws Exception{
		// 订单查询接口交易码
		String tradeCode = Constants.PAYECO_QUASH_ORDER;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
	    String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
	    // 易联服务器地址
	    String payecoUrl = Constants.PAYECO_URL;
		
	    // 1.进行数据签名
	    String srcData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	             + "&Amount=" + amount + "&TradeTime=" + tradeTime;
	    
	    // 2.私钥签名
		Log.println(" 私钥 =" + priKey);
		Log.println(" 原数据 =" + srcData);
	    String sign = Signatory.sign(priKey, srcData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名出错，退出
			throw new Exception("E103");
		}
		Log.println(" 签名 =" + sign);

		// 3.组装通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    srcData = srcData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    Log.println(" url="+url+"&"+srcData);
		String retStr = httpClient.send(url, srcData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println(" 易联返回数据 ="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败，退出
			throw new Exception("E101");
		}

		// 4.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			// 响应失败，退出
			return retCode;
		}
		
		// 5.获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 6.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setStatus(retStatus);
		retXml.setSign(retSign);
  
		// 7.验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
				+ "&Amount=" + retAmount + "&Status=" + retStatus + "&TradeTime=" + retTradeTime;
		
		// 8.验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验证签名的字符串 =" + backSign);
		Log.println(" 易联返回签名=" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 验签失败，退出
			throw new Exception("E102");
		}
		return retCode;
	}

	/**
	 * 描述:商户订单退款申请接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId:     商户订单号
	 * @param merchRefundId:    商户退款申请号
	 * @param amount:           商户退款金额
	 * @param tradeTime:        商户订单提交时间
	 * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return:                 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception:       E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderRefundReq(String merchantId, String merchOrderId, 
			String merchRefundId, String amount, String tradeTime, Xml retXml) throws Exception{
		// 订单查询接口交易码
		String tradeCode = Constants.PAYECO_REFUND_ORDER;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
	    String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
	    // 易联服务器地址
	    String payecoUrl = Constants.PAYECO_URL;
		
	    // 1.进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	    		+ "&MerchRefundId=" + merchRefundId + "&Amount=" + amount + "&TradeTime=" + tradeTime;
	    
	    // 2.私钥签名
		Log.println(" 私钥 =" + priKey);
		Log.println(" 原数据 =" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名失败，退出
			throw new Exception("E103");
		}
		Log.println(" 签名 =" + sign);

		// 3.组装通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode;
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    Log.println(" url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败，退出
			throw new Exception("E101");
		}

		// 4.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			// 响应失败，退出
			return retCode;
		}
		
		// 5.获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retMerchRefundId = Tools.getXMLValue(retStr, "MerchRefundId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retTsNo = Tools.getXMLValue(retStr, "TsNo");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 6.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setMerchRefundId(retMerchRefundId);
		retXml.setAmount(retAmount);
		retXml.setTsNo(retTsNo);
		retXml.setTradeTime(retTradeTime);
		retXml.setSign(retSign);
  
		// 7.验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId 
				+ "&MerchOrderId=" + retMerchOrderId + "&MerchRefundId=" + retMerchRefundId  
				+ "&Amount=" + retAmount + "&TsNo=" + retTsNo + "&TradeTime=" + retTradeTime;
		
		// 8.验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥=" + pubKey);
		Log.println(" 验证签名的字符串 =" + backSign);
		Log.println(" 易联返回签名 =" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 验签失败，退出
			throw new Exception("E102");
		}
		return retCode;
	}	
	
	
	/**
	 * 描述:商户订单退款结果查询接口
	 * @param merchantId:       商户代码
	 * @param merchOrderId:     商户订单号
	 * @param merchRefundId:    商户退款申请号
	 * @param tradeTime:        商户订单提交时间
	 * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return:                 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception:       E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderRefundQuery(String merchantId, String merchOrderId, String merchRefundId, 
			String tradeTime, Xml retXml) 
			throws Exception{
		// 订单查询接口交易码
		String tradeCode = Constants.PAYECO_REFUND_ORDER;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
	    String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
	    // 易联服务器地址
	    String payecoUrl = Constants.PAYECO_URL;
		
	    // 1.进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	    		+ "&MerchRefundId=" + merchRefundId + "&TradeTime=" + tradeTime;
	    
	    // 2.私钥签名
		Log.println("PrivateKey=" + priKey);
		Log.println("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名失败
			throw new Exception("E103");
		}
		Log.println(" 签名 =" + sign);

		// 3.组装通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //请求URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    Log.println("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println(" 易联返回数据 ="+retStr);
		if(Tools.isStrEmpty(retStr)){
			// 通讯失败，退出
			throw new Exception("E101");
		}

		// 4.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			// 响应失败，退出
			return retCode;
		}
		
		// 5.获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retMerchRefundId = Tools.getXMLValue(retStr, "MerchRefundId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retTsNo = Tools.getXMLValue(retStr, "TsNo");	
		String retStatus = Tools.getXMLValue(retStr, "Status");	
		String retRefundTime = Tools.getXMLValue(retStr, "RefundTime");	
		String retSettleDate = Tools.getXMLValue(retStr, "SettleDate");	
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 6.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setMerchRefundId(retMerchRefundId);
		retXml.setAmount(retAmount);
		retXml.setTsNo(retTsNo);
		retXml.setStatus(retStatus);
		retXml.setRefundTime(retRefundTime);
		retXml.setSettleDate(retSettleDate);
		retXml.setSign(retSign);
  
		// 7.验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
				 + "&MerchRefundId=" + retMerchRefundId + "&Amount=" + retAmount+ "&TsNo=" + retTsNo
				 + "&Status=" + retStatus + "&RefundTime=" + retRefundTime + "&SettleDate=" + retSettleDate;
		
		// 8.验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验证签名的字符串 =" + backSign);
		Log.println(" 易联返回签名 =" + retSign);
		Log.println(" 验证结果 =" + b);
		if(!b){
			// 验签失败，退出
			throw new Exception("E102");
		}
		return retCode;
	}	
		
	
	/**
	 * 描述: 验证订单结果通知签名
	 * @param version:        通讯版本号
	 * @param merchantId:     商户代码
	 * @param merchOrderId:   商户订单号
	 * @param amount:         商户订单金额
	 * @param extData:        商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码 ； 提交参数：采用UTF-8的base64格式编码
	 * @param orderId:        易联订单号
	 * @param status:         订单状态
	 * @param payTime:        订单支付时间
	 * @param settleDate:     订单结算日期
	 * @param sign:           签名数据
	 * @param pubKey:         易联签名验证公钥
	 * @return:               true：验证通过； false：验证不通过
	 * @throws Exception
	 */
	public static boolean backCheckNotifySign(String version, String merchantId, 
			String merchOrderId, String amount, String extData, String orderId, 
			String status, String payTime, String settleDate, String sign,
			String pubKey) 
			throws Exception{
		// 1.对extData进行转码处理: base64转码
		if (extData != null) {
			extData = extData.replaceAll(" ", "+");
			extData = new String(Base64.decode(extData), ConstantsClient.PAYECO_DATA_ENCODE);
			Log.println("extData=" + extData); // 日志输出，检查转码是否正确
		}
		 
		// 2.进行数据签名
		String data = "Version=" + version + "&MerchantId=" + merchantId
				+ "&MerchOrderId=" + merchOrderId + "&Amount=" + amount
				+ "&ExtData=" + extData + "&OrderId=" + orderId + "&Status="
				+ status + "&PayTime=" + payTime + "&SettleDate=" + settleDate;

		// 3.验证签名
		sign = sign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, data, sign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验证签名的字符串 =" + data);
		Log.println(" 易联返回签名=" + sign);
		Log.println(" 验证结果=" + b);
		return b;
	}
	
	/**
	 * 描述:互联网金融行业银行卡解除绑定接口
	 * @param merchantId:		商户代码
	 * @param bankAccNo:        解除绑定的银行卡账号
	 * @param tradeTime:        商户提交时间
	 * @param priKey:           商户签名的私钥
	 * @param pubKey:           易联签名验证公钥
	 * @param payecoUrl:        易联服务器URL地址，只需要填写域名部分
	 * @param retXml:           通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return:                 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception:       E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String UnboundBankCard(String merchantId, String bankAccNo, String tradeTime, 
			Xml retXml) 
			throws Exception{
		// 订单查询接口交易码
		String tradeCode = Constants.PAYECO_UNBOUND_BANK_CARD;
		// 通讯协议版本号
		String version = ConstantsClient.COMM_INTF_VERSION;
		// 私钥
		String priKey = Constants.MERCHANT_RSA_PRIVATE_KEY;
		// 易联公钥
	    String pubKey = Constants.PAYECO_RSA_PUBLIC_KEY;
	    // 易联服务器地址
	    String payecoUrl = Constants.PAYECO_URL;
		
	    // 1.进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&BankAccNo=" + bankAccNo 
	             + "&TradeTime=" + tradeTime;
	    
	    // 2.私钥签名
		Log.println("PrivateKey=" + priKey);
		Log.println("data=" + signData);
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			// 签名失败，退出
			throw new Exception("E103");
		}
		Log.println("sign=" + sign);

		// 3.组装通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //解除绑定URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();
	    Log.println("url="+url+"&"+signData);
		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);
		Log.println("retStr="+retStr);
		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		// 4.返回数据的返回码判断
		retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			// 响应失败，退出
			return retCode;
		}
		
		// 5.获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retBankAccNo = Tools.getXMLValue(retStr, "BankAccNo");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		
		// 6.设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setBankAccNo(retBankAccNo);
		retXml.setStatus(retStatus);
		retXml.setTradeTime(retTradeTime);
		retXml.setSign(retSign);
		  
		// 7.验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&BankAccNo=" + retBankAccNo 
		  + "&TradeTime=" + retTradeTime + "&Status=" + retStatus;

		// 8.验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);
		Log.println(" 易联公钥 =" + pubKey);
		Log.println(" 验证签名的字符串 =" + backSign);
		Log.println(" 易联返回签名=" + retSign);
		Log.println(" 验证结果=" + b);
		if(!b){
			// 验签失败，退出
			throw new Exception("E102");
		}
		return retCode;
	}	
	
}
