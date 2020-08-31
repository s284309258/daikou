package com.merchant.demo.service;

import com.merchant.demo.base.Constants;
import com.merchant.demo.client.TransactionClientSmsApi;
import com.merchant.util.Log;
import com.merchant.util.Tools;
import com.merchant.util.Xml;



/**
 * 描述：【短信+API2.0接口版本】的接口测试demo，分别对以下接口进行测试
 * 1、 短信码检查接口（ApiSmsCodeCheckTest）
 * 2、重发短信码接口接口（ApiSmsAgainTest）
 * 3、支付接口接口（ApiPayTest）
 */
public class SmsApiService {
	/**
	 * 描述：短信码检查接口
	 */
	public void apiSmsCodeCheckTest() {
		//商户订单号(保持唯一性)
		String merchOrderId = "3232323232300000";
		// 手机号
		String mobileNo = "13552535506";
		// 交易提交时间
		String tradeTime = Tools.getSysTime();
		// 短信参数，格式：姓名|证件类型|证件号码|银行卡号|行业编号|交易金额  （正确数据且必填）
		String smParam = "全渠道||341126197709218366|6216261000000000018||";
		
		Log.println("----------短信码检查接口测试---------");
		Xml retXml = new Xml();
		try {
			// 接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientSmsApi.apiSmsCodeCheck(merchOrderId, 
					tradeTime, mobileNo, smParam, retXml);
			if(!Constants.PAYECO_CODE_SUCCESS_VAL.equals(ret)){
				Log.println(" 短信码检查接口调用失败：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println("短信码检查接口调用异常");
			e.printStackTrace();
			return;
		}
		Log.println(" 短信码检查接口调用测试--------ok");
	}

	/**
	 * 描述：重发短信码接口
	 */
	public void apiSmsAgainTest() {
		// 商户订单号(与短信码检查接口保持一致)
		String merchOrderId = "";
		// 短信凭证码(不需要发短信则不要)
		String smId = "";
		// 交易提交时间
		String tradeTime = Tools.getSysTime();

		Log.println("-------重发短信码接口测试-------------------");
		Xml retXml = new Xml();
		try {
			// 接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientSmsApi.apiSmsAgain(merchOrderId,smId,tradeTime,retXml);
			if(!"0000".equals(ret)){
				Log.println(" 重发短信码接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println(" 重发短信码接口测试失败");
			e.printStackTrace();
			return;
		}
		Log.println(" 重发短信码接口测试--------ok");
	}
	
	
	/**
	 * 描述：支付接口
	 */
	public void apiPayTest(String smCodePay) {
		// 行业
		String industryId = "";
		// 商户订单号(与短信码检查接口保持一致)
		String merchOrderId = "32323232323"; 
		// 金钱
		String amount = "1.00";
		// 交易提交时间
		String orderDesc = "通用商户充值";
		// 交易提交时间
		String tradeTime = Tools.getSysTime();
		// 采用系统默认的订单有效时间，为空默认30分钟
		String expTime = ""; 
		// 商户保留信息
		String extData = "充值测试";
		// 以下扩展参数是按通用行业填写的；其他行业请参考接口文件说明进行填写
		String miscData = "13552535506|||全渠道|341126197709218366|6216261000000000018|||||||||";//"||||||||";
		// 订单通知标志
		String notifyFlag = "0";
		// 短信凭证码(不需要发短信则不要)
		String smId = "384f66ece3f347fbb1830ef9ec64c6af";
		// 短信码(不需要发短信则不要)
		String smCode = "000000";
		// 密码(不需要密码则不要)
		String pwd="";

		// 调用下单接口
		Log.println("-----------支付接口测试-------------------------");
		try {
			Xml retXml = new Xml();
			// 接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientSmsApi.apiPay(industryId, merchOrderId, amount, orderDesc, 
					tradeTime, expTime, extData, miscData, notifyFlag, smId, smCode, pwd, retXml);
			if(!"0000".equals(ret)){
				Log.println("支付接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println(" 支付接口测试失败！");
			e.printStackTrace();
			return;
		}
		Log.println(" 支付接口测试--------ok");
	}
}
