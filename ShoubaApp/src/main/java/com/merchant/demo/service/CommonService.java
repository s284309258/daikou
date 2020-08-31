package com.merchant.demo.service;

import com.merchant.demo.base.Constants;
import com.merchant.demo.client.TransactionClientCommon;
import com.merchant.util.Log;
import com.merchant.util.Tools;
import com.merchant.util.Xml;

/**
 * 描述:各版本通用接口测试demo
 * 1、订单查询接口（OrderQueryTest）
 * 2、订单冲正接口（OrderReverseTest）
 * 3、订单退款申请接口（OrderRefundReqTest）
 * 4、订单退款查询接口（OrderRefundQueryTest）
 * 5、互联网金融行业银行卡解除绑定接口（UnboundBankCardTest）
 */
public class CommonService {
	/**
	 * 描述:订单查询接口测试
	 */
	public static void OrderQueryTest(String orderId) {
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 易联平台订单号
		String merchOrderId = orderId;  
		// 交易提交时间
		String tradeTime = Tools.getSysTime();

		// 调用查询接口
		Log.println("-------订单查询接口测试-------------------------");
		try {
			Xml retXml = new Xml();
			//接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientCommon.OrderQuery(merchantId, merchOrderId, tradeTime,
					 retXml);
			if(!Constants.PAYECO_CODE_SUCCESS_VAL.equals(ret)){
				Log.println("订单查询接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println("订单查询接口测试失败！");
			e.printStackTrace();
			return;
		}
		Log.println("订单查询接口测试----ok");
		Log.println("------------------------------------------------");
	}
	
	/**
	 * 描述:订单冲正接口测试
	 */
	public static void OrderReverseTest(String orderId, String amt) {
		// 金额
		String amount = amt;
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 需要填写已经存在的商户订单号
		String merchOrderId = orderId;  
		// 交易时间
		String tradeTime = Tools.getSysTime();

		// 调用冲正接口
		Log.println("-------订单冲正接口测试-------------------------");
		try {
			Xml retXml = new Xml();
			//接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientCommon.OrderReverse(merchantId, merchOrderId, amount, tradeTime, 
					 retXml);
			if(!Constants.PAYECO_CODE_SUCCESS_VAL.equals(ret)){
				Log.println("订单冲正接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println("订单冲正接口测试失败！");
			e.printStackTrace();
			return;
		}
		Log.println("订单冲正接口测试----ok");
		Log.println("------------------------------------------------");
	}

	/**
	 * 描述:订单退款申请接口测试
	 */
	public static void OrderRefundReqTest(String orderId, String amt) {
		// 金额
		String amount = amt;
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 需要填写已经存在的商户订单号
		String merchOrderId = orderId; 
		// 退款订单号
		String merchRefundId = "" + System.currentTimeMillis(); // 退款申请号
		// 交易时间
		String tradeTime = Tools.getSysTime();

		// 调用冲正接口
		Log.println("-------订单退款申请接口测试-------------------------");
		try {
			Xml retXml = new Xml();
			//接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientCommon.OrderRefundReq(merchantId, merchOrderId, merchRefundId, amount, tradeTime, 
					 retXml);
			if(!Constants.PAYECO_CODE_SUCCESS_VAL.equals(ret)){
				Log.println("订单退款申请接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println("订单退款申请接口测试失败！");
			e.printStackTrace();
			return;
		}
		Log.println("订单退款申请接口测试----ok");
		Log.println("------------------------------------------------");
	}	

	/**
	 * 描述:订单退款结果查询接口测试
	 */
	public static void OrderRefundQueryTest(String orderId, String refundId) {
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 需要填写已经存在的商户订单号
		String merchOrderId = orderId;  
		// 退款申请号
		String merchRefundId = refundId; 
		// 交易提交时间
		String tradeTime = Tools.getSysTime();

		// 调用冲正接口
		Log.println("-------订单退款结果查询接口测试-------------------------");
		try {
			Xml retXml = new Xml();
			//接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientCommon.OrderRefundQuery(merchantId, merchOrderId, merchRefundId, tradeTime, 
					retXml);
			if(!Constants.PAYECO_CODE_SUCCESS_VAL.equals(ret)){
				Log.println("订单退款结果查询接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
		} catch (Exception e) {
			Log.println("订单退款结果查询接口测试失败！");
			e.printStackTrace();
			return;
		}
		Log.println("订单退款结果查询接口测试----ok");
		Log.println("------------------------------------------------");
	}	
	
	////==================以下接口为特殊行业或定制化的接口========================
	/**
	 * 描述: 互联网金融行业银行卡解除绑定接口测试
	 */
	public static void UnboundBankCardTest(String bankAccNo) {
		// 商户号
		String merchantId = Constants.MERCHANT_ID;
		// 交易提交时间
		String tradeTime = Tools.getSysTime();

		// 调用查询接口
		Log.println("-------互联网金融行业银行卡解除绑定接口测试-------------------------");
		try {
			Xml retXml = new Xml();
			//接口参数请参考TransactionClient的参数说明
			String ret = TransactionClientCommon.UnboundBankCard(merchantId, bankAccNo, tradeTime,
					retXml);
			if(!Constants.PAYECO_CODE_SUCCESS_VAL.equals(ret)){
				Log.println("互联网金融行业银行卡解除绑定接口测试失败！：retCode="+ret+"; msg="+retXml.getRetMsg());
				return;
			}
			Log.println("Status="+retXml.getStatus());
		} catch (Exception e) {
			Log.println("互联网金融行业银行卡解除绑定接口测试失败！：");
			e.printStackTrace();
			return;
		}
		Log.println("互联网金融行业银行卡解除绑定接口测试----ok;");
		Log.println("------------------------------------------------");
	}
	
}
