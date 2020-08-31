package com.merchant.demo.test;

import com.merchant.demo.base.Constants;
import com.merchant.demo.service.CommonService;
import com.merchant.demo.service.SmsApiService;
/**
 * 描述：执行口
 * @author Payeco
 *
 */
public class SmsApiTest {

	public static void main(String[] args) {
		SmsApiService smsApiService = new SmsApiService();
		//--------------数据都需要自己补充-------------
		
		// 1. 短信码检查接口
		//smsApiService.apiSmsCodeCheckTest();
		
		// 2.重发短信码接口
//		smsApiService.apiSmsAgainTest();
		
		// 3.支付接口
		smsApiService.apiPayTest(Constants.PAYECO_TEST_SMSCODE);
		
		
		// 4.订单查询接口测试
		//填写已经存在成功下单的商户订单号
		String merchOrderId = ""; 
		CommonService.OrderQueryTest(merchOrderId);
		
		// 5.订单冲正接口测试
		//填写已经存在成功下单的商户订单号，只有当天支付成功才能冲正
		merchOrderId = "";
		//冲正额金额需要和订单金额一样
		String amount = "";	   
//		CommonService.OrderReverseTest(orderId, amount);

		// 6.订单退款申请接口测试
		//填写已经存在成功下单的商户订单号，只有支付成功才能退款申请
		merchOrderId = "";
		//退款的金额不能大于订单金额
		amount = "";	   
//		CommonService.OrderRefundReqTest(merchOrderId, amount);
		
		// 7.订单退款结果查询接口测试
		//填写已经存在成功下单的商户订单号，只有支付成功才能退款申请
		merchOrderId = ""; 
		//退款申请号，需要填写退款申请时提交的申请号，退款结果需要在申请的隔天后才能查询到结果
		String refundId = "";      
//		CommonService.OrderRefundQueryTest(merchOrderId, refundId);
		
		
		
		// 8.互联网金融行业银行卡解除绑定接口测试
		String bankAccNo="";
//		CommonService.UnboundBankCardTest(bankAccNo);
	}
}
