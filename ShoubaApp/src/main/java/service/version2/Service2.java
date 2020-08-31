package service.version2;

import service.encrypt.RSA;
import service.encrypt.TripleDes;
import service.util.*;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Service2 {

	private static Logger logger = Logger.getLogger(Service2.class.getName());


//	private static String dna_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWSfUW3fSyoOYzOG8joy3xldpBanLVg8gEDcvm9KxVjqvA/qJI7y0Rmkc1I7l9vAfWtNzphMC+wlulpaAsa/4PbfVj+WhoNQyhG+m4sP27BA8xuevNT9/W7/2ZVk4324NSowwWkaqo1yuZe1wQMcVhROz2h+g7j/uZD0fiCokWwIDAQAB";//商户公钥
//	private static String mer_pfx_key = "/data/wwwroot/shouba.wgzvip.com/yilian.pfx";//商户私钥
//	private static String mer_pfx_pass = "11111111";//商户私钥密码

	private static String dna_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDc+L2JGUKlGtsFm2f/wuF2T6/8mc6yrN8tLPgsx7sxAatvMvunHLXKC8xjkChHqVfJgohV4OIWe8zCw7jPsJMiPvrNnFHJ2Mumg/zQ8eZOnzMA0LDqBNFvZnOpy2XtagQn4yxxzG9+9h4P5eNojC3vD2t3H/6q5V3Cd022/egIZQIDAQAB";
	private static String mer_pfx_key = "/data/wwwroot/shouba.szfyhgs.com/104000000253042-Signature.pfx";//商户私钥
	private static String mer_pfx_pass = "28128181";//商户私钥密码

	//	private static String url = "https://58.248.38.253:9444/service";//测试环境下单地址
//	private static String url = "http://10.123.1.26:9081/service";
//	private static String url = "http://10.123.1.26:9081/service";
//	private static String url = "http://localhost:8080/service";
//	private static String url = "https://testdsf.payeco.com:9445/service";
//	private static String url = "http://10.123.74.90:9080/service";
//	private static String url = "https://testagent.payeco.com:9444/service";
//	private static String url = "https://10.123.65.17:8443/service";
//	private static String url = "https://127.0.0.1:8443/service";
	private static String url = "https://agent.payeco.com/service";//生产环境下单地z址

	public static void main(String[] args) throws Exception {
//user_name=302020000055&sn=20200719181951990&acc_no=6216261321581015888&acc_name=张三&amount=1&acc_province=上海市&acc_city=上海市&acc_prop=1&mer_order_no=20200719181951990
		pay("15901837011","20200719181951990","6216261321581015888","张三","1","上海市","上海市","1","20200719181951990");
//		payTest();//代付
//		pay_query();//查询代付结果

//		gather();//代收
//		gather_query();//查询代收结果

//		dsBatch();// 批量代收
//		dsBatchQuery(); //批量代收查询

//		verify_query(); //签约查询
//		preSign(); //预签约
//		sign();//签约

//		queryAccountInfo();//查询银行卡所属银行名称
	}

	public static void gather() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.1");
		req_bean.setMSG_TYPE("200001");
		req_bean.setBATCH_NO(new String(Base64.decode(Util.generateKey(99999,14))));
//		req_bean.setUSER_NAME("15901837011");//商户号：302020000001
		req_bean.setUSER_NAME("13760136514");//商户号：00000001
//		req_bean.setUSER_NAME("02022132719");//商户号：00000001

		MsgBody body = new MsgBody();
		body.setSN("1010000");
//		body.setACC_NO("9559980570466651413");//T432
		body.setACC_NO("6226090000000048");//必填  622908376336850613  黑名单：9559980570466651413
		body.setACC_NAME("张三");//必填
		body.setMOBILE_NO("18100000000");//支付手机号，必填
		body.setID_NO("510265790128303");//必填
		body.setID_TYPE("0");
		body.setAMOUNT("0.1");//
//		body.setAMOUNT("5");//
		body.setCNY("CNY");
		body.setREMARK("代收测试");
		body.setRETURN_URL("");
		body.setMER_ORDER_NO("");
		body.setMER_SEQ_NO("");
		body.setTRANS_DESC("单笔代收测试");
		body.setRETURN_URL("");

		req_bean.getBODYS().add(body);
		String res = sendAndRead(signANDencrypt(req_bean));
		System.out.println("res="+res);
		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		System.out.println(""+res_bean.getBODYS().get(0).getPAY_STATE()+"-"+res_bean.getBODYS().get(0).getREMARK());
		logger.info(res_bean.toXml());
	}

	//代收
	public static void gather2() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.0");
		req_bean.setMSG_TYPE("200001");//代收
//		req_bean.setMSG_TYPE("200005");//批量代收
		req_bean.setBATCH_NO(new String(Base64.decode(Util.generateKey(99999,28))));//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
//		req_bean.setBATCH_NO("1C30CA021ACFE5ER");//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
		req_bean.setUSER_NAME("13760136514");//首笔外呼流程测试
//		req_bean.setUSER_NAME("15901837011");//系统后台登录名
//		req_bean.setUSER_NAME("13728096873");//SMS快捷支付流程测试
//		req_bean.setUSER_NAME("02022132719");//T0实时代收付流程测试
//		req_bean.setUSER_NAME("13580506035");//T0实时代收付流程测试
//		req_bean.setUSER_NAME("13423124211");//首笔无密鉴权扣款流程 010020000038
//		req_bean.setUSER_NAME("15996963258");//首笔无密鉴权扣款流程 010020000038

		MsgBody body = new MsgBody();
		body.setSN("101000001");//流水号，同一批次不重复即可
//		body.setACC_NO("6222023602076000006");//工行  6222023602076096870 张三  320721198203082423  13728096879
//		body.setACC_NO("6225882008106548");//招行  6225882008106541 王五 320721198203082423 13728096879  调风控2.0
//		body.setACC_NO("6227003324120038769");//建行卡：6227003324120038769  朱芳 320721198203082423 13450204808
//		body.setACC_NO("6222023602076096870");//必填  622908376336850613
//		body.setACC_NO("6222020401878200510");//必填
//		body.setACC_NO("6222020401878200000");//必填
		body.setACC_NO("9559980570466651413");//必填
		body.setACC_NAME("朱芳");//必填
		body.setID_NO("320721198203082423");//开户证件号
		body.setID_TYPE("0");//证件类型
		body.setAMOUNT("1");//必填
		body.setCNY("CNY");
		body.setREMARK("测试2");
		body.setMOBILE_NO("");//支付手机号
		body.setRETURN_URL("");//异步通知地址
		body.setMER_ORDER_NO("mer12432423423");
		body.setMER_SEQ_NO("");
		body.setMOBILE_NO("13728096879");
		body.setTRANS_DESC("批量接口 交易描述");//首笔外呼播报语音内容
		req_bean.getBODYS().add(body);





		/*MsgBody body2 = new MsgBody();
		body2.setSN("101000002");//流水号，同一批次不重复即可
//		body.setACC_NO("6222023602076055599");//必填
		body2.setACC_NO("6222023602076096877");//必填
		body2.setACC_NAME("陈佑陇");//必填
		body2.setID_NO("420984198301080413");//开户证件号
		body2.setID_TYPE("1");//证件类型
		body2.setAMOUNT("1000000");//必填
		body2.setCNY("CNY");
		body2.setREMARK("dna测试2");
		body2.setMOBILE_NO("");//支付手机号
		body2.setRETURN_URL("");//异步通知地址
		body2.setMER_ORDER_NO("");
		body2.setMER_SEQ_NO("");
		body2.setMOBILE_NO("13728096879");
		body2.setTRANS_DESC("批量接口 交易描述dna");//首笔外呼播报语音内容
		req_bean.getBODYS().add(body2);*/

	/*	MsgBody body1 = new MsgBody();
		body1.setSN("101000003");
		body1.setACC_NO("6222023602076090051");
		body1.setACC_NAME("李是");
		body1.setAMOUNT("2");
		req_bean.getBODYS().add(body1);

		MsgBody body3 = new MsgBody();
		body3.setSN("0000000000000003");
		body3.setACC_NO("6225887800100101");
		body3.setACC_NAME("王午");
		body3.setAMOUNT("2.2");
		req_bean.getBODYS().add(body3);
		*/
		/*MsgBody body4 = new MsgBody();
		body4.setSN("0000000000000003");
		body4.setACC_NO("6221550001820517");
		body4.setACC_NAME("梁海泉");
		body4.setAMOUNT("2.5");
		body4.setID_NO("");
		body4.setREMARK("信用卡测试");
		req_bean.getBODYS().add(body4);*/

		String res = sendAndRead(signANDencrypt(req_bean));

		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
	}

	//代收查询
	public static void gather_query() {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.0");
		req_bean.setMSG_TYPE("200002");
		req_bean.setBATCH_NO("6A05EF0A667701"); //同代收交易请求的批次号
		req_bean.setUSER_NAME("13760136514");//系统后台登录名
//		req_bean.setUSER_NAME("15901837011");//系统后台登录名
		/*MsgBody body1 = new MsgBody();
		body1.setQUERY_NO_FLAG("1");
		body1.setMER_ORDER_NO("");
		body1.setMER_SEQ_NO("");
		body1.setRETURN_URL("http://10.123.18.44:8080/notifyasyn?beanName=PayEcoNotifyHome&amp;ENCODING=utf-8");
		req_bean.getBODYS().add(body1);*/

//		MsgBody body2 = new MsgBody();
//		body2.setQUERY_NO_FLAG("1");
//		body2.setMER_ORDER_NO("MONBE932A83421E6C");//KK78965421354
//		req_bean.getBODYS().add(body2);

		String res = sendAndRead(signANDencrypt(req_bean));

		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
	}

	/**
	 * 批量代收
	 * @Title: dsBatch
	 * @Description: TODO
	 * @throws Exception
	 * @return void
	 */
	public static void dsBatch() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.1");
		req_bean.setMSG_TYPE("200005");
		req_bean.setBATCH_NO(new String(Base64.decode(Util.generateKey(99999,14))));
		req_bean.setUSER_NAME("15901837011");//商户号：302020000001
//		req_bean.setUSER_NAME("13760136514");//商户号：00000001
//		req_bean.setUSER_NAME("02022132719");//商户号：00000001

		MsgBody body = new MsgBody();
		body.setSN("1010000");

		//交易明细
		List<BatchDsDetailReqDTO> detailList = new ArrayList<BatchDsDetailReqDTO>();
		BatchDsDetailReqDTO reqDTO1 = new BatchDsDetailReqDTO();
		reqDTO1.setMerOrderNo("10001");
		reqDTO1.setAccNo("6226090000000048");
		reqDTO1.setAccName("张三");
		reqDTO1.setIdCardNo("510265790128303");
		reqDTO1.setMobileNo("18100000000");
		reqDTO1.setAmount("1");
		reqDTO1.setTransDesc("批量代收");
		detailList.add(reqDTO1);

		BatchDsDetailReqDTO reqDTO2 = new BatchDsDetailReqDTO();
		reqDTO2.setMerOrderNo("10002");
		reqDTO2.setAccNo("6222800601167325");
		reqDTO2.setAccName("黄思");
		reqDTO2.setIdCardNo("440105198812072727");
		reqDTO2.setMobileNo("13728096879");
		reqDTO2.setAmount("0.5");
		reqDTO2.setTransDesc("批量代收");
		detailList.add(reqDTO2);

		BatchDsDetailReqDTO reqDTO3 = new BatchDsDetailReqDTO();
		reqDTO3.setMerOrderNo("10003");
		reqDTO3.setAccNo("621626100000000013");
		reqDTO3.setAccName("黎明");
		reqDTO3.setIdCardNo("110101199003078670");
		reqDTO3.setMobileNo("13800138002");
		reqDTO3.setAmount("1.5");
		reqDTO3.setTransDesc("批量代收");
		detailList.add(reqDTO3);

		String jsonStr = JsonUtil.listToJson(detailList);
		System.out.println("批量代收，交易明细json串："+jsonStr);

		String flaterStr = FlaterUtil.deflaterFromString(jsonStr, "GBK");
		System.out.println("批量代收，压缩后交易明细："+flaterStr);

		body.setDETAIL_CONTENT(flaterStr);

		req_bean.getBODYS().add(body);
		String res = sendAndRead(signANDencrypt(req_bean));
		System.out.println("res="+res);
		MsgBean res_bean = decryptANDverify(res);
		System.out.println(""+res_bean.getBODYS().get(0).getPAY_STATE()+"-"+res_bean.getBODYS().get(0).getREMARK());
		logger.info(res_bean.toXml());

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
			MsgBody resBody = res_bean.getBODYS().get(0);
			String resFlaterStr = resBody.getDETAIL_CONTENT();
			String resFileContent = FlaterUtil.inFlaterFromBase64(resFlaterStr, "GBK");
			System.out.println("批量代收，解压响应明细："+resFileContent);
			List<BatchDsDetailResDTO> resDetailList = JsontoBeanUtil.getListFromJsonArrStr(resFileContent, BatchDsDetailResDTO.class);
			for (BatchDsDetailResDTO resDTO : resDetailList) {
				System.out.println(String.format("批量代收明细，响应结果,订单号：%s，返回码：%s，返回码描述：%s",
						resDTO.getMerOrderNo(),resDTO.getPayState(),resDTO.getResMsg()));
			}
		}

	}


	/**
	 * 批量代收查询
	 * @Title: dsBatch
	 * @Description: TODO
	 * @throws Exception
	 * @return void
	 */
	public static void dsBatchQuery() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.1");
		req_bean.setMSG_TYPE("200006");
		req_bean.setBATCH_NO("5AFB7FA143FB2D");
		req_bean.setUSER_NAME("15901837011");//商户号：302020000001
//		req_bean.setUSER_NAME("13760136514");//商户号：00000001
//		req_bean.setUSER_NAME("02022132719");//商户号：00000001

		String res = sendAndRead(signANDencrypt(req_bean));
		System.out.println("payeco res="+res);
		MsgBean res_bean = decryptANDverify(res);
		System.out.println(""+res_bean.getBODYS().get(0).getPAY_STATE()+"-"+res_bean.getBODYS().get(0).getREMARK());
		logger.info(res_bean.toXml());

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");

			MsgBody resBody = res_bean.getBODYS().get(0);
			if(!"00A4".equals(resBody.getPAY_STATE())){
				logger.info("订单处理完成");
				String resFlaterStr = resBody.getDETAIL_CONTENT();
				if(resFlaterStr !=null && !resFlaterStr.equals("")){
					logger.info("批量代收查询，解压文件");
					String resFileContent = FlaterUtil.inFlaterFromBase64(resFlaterStr, "GBK");
					System.out.println("批量代收查询，解压响应明细："+resFileContent);
					List<BatchDsDetailResDTO> resDetailList = JsontoBeanUtil.getListFromJsonArrStr(resFileContent, BatchDsDetailResDTO.class);
					for (BatchDsDetailResDTO resDTO : resDetailList) {
						System.out.println(String.format("批量代收明细，响应结果,订单号：%s，返回码：%s，返回码描述：%s",
								resDTO.getMerOrderNo(),resDTO.getPayState(),resDTO.getResMsg()));
					}
				}

			}

		}

	}

	//预签约，发送短信
	public static void preSign() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.0");
		req_bean.setMSG_TYPE("500001");
		String batch_no = new String(Base64.decode(Util.generateKey(99999,14)));//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
		req_bean.setBATCH_NO(batch_no);
		req_bean.setUSER_NAME("15901837011");//系统后台登录名
//			req_bean.setUSER_NAME("13760136514");//系统后台登录名
//			req_bean.setUSER_NAME("13423124211");//首笔无密鉴权扣款流程
//			req_bean.setUSER_NAME("02022132719");//T0实时代收付流程测试

		MsgBody body = new MsgBody();
		body.setSN("101000001");//流水号，同一批次不重复即可
//			body.setACC_NO("6222980601160021");//0021结尾的卡号，  户名不符
		body.setACC_NO("6226090000000048");//必填  622908376336850613  黑名单：9559980570466651413
		body.setACC_NAME("张三");//必填
		body.setMOBILE_NO("18100000000");//支付手机号，必填
		body.setID_NO("510265790128303");//必填
		body.setID_TYPE("0");//必填
		req_bean.getBODYS().add(body);

		String res = sendAndRead(signANDencrypt(req_bean));

		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
	}

	//签约
	public static void sign() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.0");
		req_bean.setMSG_TYPE("300001");
		String batch_no = new String(Base64.decode(Util.generateKey(99999,14)));//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
		req_bean.setBATCH_NO(batch_no);
		req_bean.setUSER_NAME("15901837011");//系统后台登录名
//		req_bean.setUSER_NAME("13760136514");//系统后台登录名
//		req_bean.setUSER_NAME("13423124211");//首笔无密鉴权扣款流程
//		req_bean.setUSER_NAME("02022132719");//T0实时代收付流程测试

		MsgBody body = new MsgBody();
		body.setSN("101000001");//流水号，同一批次不重复即可
//		body.setACC_NO("6222980601167325");//必填  622908376336850613  黑名单：9559980570466651413
		body.setACC_NO("6226090000000048");//必填  622908376336850613  黑名单：9559980570466651413
		body.setACC_NAME("张三");//必填
		body.setMOBILE_NO("18100000000");//支付手机号，必填
		body.setID_NO("510265790128303");//必填
		body.setID_TYPE("0");//必填
		body.setSMS_CODE("111111"); //测试环境默认123456  ,
		req_bean.getBODYS().add(body);

		String res = sendAndRead(signANDencrypt(req_bean));

		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
	}



	//认证查询
	public static void verify_query() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.0");
		req_bean.setMSG_TYPE("300002");
		String batch_no = new String(Base64.decode(Util.generateKey(99999,14)));
		req_bean.setBATCH_NO(batch_no);
		req_bean.setUSER_NAME("15901837011");//系统后台登录名
//		req_bean.setUSER_NAME("13760136514");//系统后台登录名   00000001

		MsgBody body = new MsgBody();
		body.setSN("101000001");
		body.setACC_NO("622214000000000018");
		body.setACC_NAME("张三");
//		body.setACC_PROVINCE("广东");
//		body.setACC_CITY("广州");

		body.setID_NO("");
		body.setID_TYPE("0");
		body.setRESERVE("Y");
		req_bean.getBODYS().add(body);

		/*	MsgBody body2 = new MsgBody();
		body2.setSN("101000002");
		body2.setACC_NO("6222023602076096878");
		body2.setACC_NAME("李是");
		body2.setAMOUNT("2");
		req_bean.getBODYS().add(body2);

		MsgBody body3 = new MsgBody();
		body3.setSN("0000000000000003");
		body3.setACC_NO("6225887800100101");
		body3.setACC_NAME("王午");
		body3.setAMOUNT("2.2");
		req_bean.getBODYS().add(body3);*/

		String res = sendAndRead(signANDencrypt(req_bean));

		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
	}

	//代付
	public static MsgBean pay(String user_name,String sn,String acc_no,String acc_name,String amount,String acc_province,String acc_city,String acc_prop,String mer_order_no) throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.1");
		req_bean.setMSG_TYPE("100001");
		req_bean.setBATCH_NO(new String(Base64.decode(Util.generateKey(999999,8))));//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
//		req_bean.setBATCH_NO("YHHL2015040922BF9CD47A6564");//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
		req_bean.setUSER_NAME(user_name);//直接代扣  //302020000001
//		req_bean.setUSER_NAME("13728096873");//SMS快捷支付流程测试
//		req_bean.setUSER_NAME("13728096874");//短信码扣款流程
//		req_bean.setUSER_NAME("02022132719");//民生代付 002020000007
//		req_bean.setUSER_NAME("100220000001");//跨境
//		req_bean.setUSER_NAME("13760136514");//首笔外呼代扣  00000001
//		req_bean.setUSER_NAME("02022132719");//T0实时代收付流程测试  、、002020000007
//		req_bean.setUSER_NAME("02022132749");//上海银联代付流程测试 002020000005
//		req_bean.setUSER_NAME("13423124211");//首笔无密鉴权扣款流程 010020000038

		MsgBody body = new MsgBody();
		body.setSN(sn);//流水号，同一批次不重复即可
//		body.setACC_NO("36225882008106541");  // 招商
//		body.setACC_NO("6226090000000048"); //建行
		body.setACC_NO(acc_no); //邮储
//		body.setACC_NO("6222022003004150044"); //工行 走光大
//		body.setACC_NO("6222022003004151102"); //工行 走光大
//		body.setACC_NO("6228232003004151103"); //农行 走UPI
//		body.setACC_NO("6226922003004151105"); //中信 走CMBC
//		body.setACC_NO("6226922003004150044"); //中信 走CMBC

		body.setACC_NAME(acc_name);
//		body.setID_NO("341126197709218366");
//		body.setACC_NO("32001881536059666888");
//		body.setACC_NO("2000000000066");
//		body.setACC_NAME("万金所金融信息服务有限公司");
//		body.setID_NO("440306196009150000");
		body.setAMOUNT(amount);
		body.setACC_PROVINCE(acc_province);
		body.setACC_CITY(acc_city);
//		body.setBANK_NAME("工商银行股份有限公司成都开发西区支行");
		body.setACC_PROP(acc_prop); //0-对私； 1-对公
		body.setMER_ORDER_NO(mer_order_no);

		req_bean.getBODYS().add(body);

		/*MsgBody body2 = new MsgBody();
		body2.setSN("0000000000000002");
		body2.setACC_NO("6216261003004150044");
		body2.setACC_NAME("陈氏阿凤有限公司");
		body2.setAMOUNT("101");
		body2.setACC_PROVINCE("广东");
		body2.setACC_CITY("广州");
		body2.setACC_PROP("1"); //0-对私； 1-对公
		body2.setBANK_NAME("中国银行股份有限公司广州天文苑支行");
		req_bean.getBODYS().add(body2);

		MsgBody body3 = new MsgBody();
		body3.setSN("0000000000000003");
		body3.setACC_NO("6222142003004150043"); //工行信用卡
		body3.setACC_NAME("王午3");
		body3.setAMOUNT("2000");
		body3.setBANK_NAME("工商银行同福东路支行");
		req_bean.getBODYS().add(body3);*/

		String res = sendAndRead(signANDencrypt(req_bean));
		logger.info("响应报文："+res);
		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
		return res_bean;
	}

	//代付
	public static void payTest() throws Exception {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.1");
		req_bean.setMSG_TYPE("100001");
		req_bean.setBATCH_NO(new String(Base64.decode(Util.generateKey(999999,8))));//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
//			req_bean.setBATCH_NO("YHHL2015040922BF9CD47A6564");//每笔订单不可重复，建议：公司简称缩写+yymmdd+流水号
		req_bean.setUSER_NAME("15901837011");//直接代扣  //302020000001
//			req_bean.setUSER_NAME("13728096873");//SMS快捷支付流程测试
//			req_bean.setUSER_NAME("13728096874");//短信码扣款流程
//			req_bean.setUSER_NAME("02022132719");//民生代付 002020000007
//			req_bean.setUSER_NAME("100220000001");//跨境
//			req_bean.setUSER_NAME("13760136514");//首笔外呼代扣  00000001
//			req_bean.setUSER_NAME("02022132719");//T0实时代收付流程测试  、、002020000007
//			req_bean.setUSER_NAME("02022132749");//上海银联代付流程测试 002020000005
//			req_bean.setUSER_NAME("13423124211");//首笔无密鉴权扣款流程 010020000038

		MsgBody body = new MsgBody();
		body.setSN("0000000001");//流水号，同一批次不重复即可
//			body.setACC_NO("36225882008106541");  // 招商
//			body.setACC_NO("6226090000000048"); //建行
		body.setACC_NO("6216261321581015888"); //邮储
//			body.setACC_NO("6222022003004150044"); //工行 走光大
//			body.setACC_NO("6222022003004151102"); //工行 走光大
//			body.setACC_NO("6228232003004151103"); //农行 走UPI
//			body.setACC_NO("6226922003004151105"); //中信 走CMBC
//			body.setACC_NO("6226922003004150044"); //中信 走CMBC

		body.setACC_NAME("张三");
//			body.setID_NO("341126197709218366");
//			body.setACC_NO("32001881536059666888");
//			body.setACC_NO("2000000000066");
//			body.setACC_NAME("万金所金融信息服务有限公司");
//			body.setID_NO("440306196009150000");
		body.setAMOUNT("1");
		body.setACC_PROVINCE("上海市");
		body.setACC_CITY("上海市");
//			body.setBANK_NAME("工商银行股份有限公司成都开发西区支行");
//			body.setACC_PROP("1"); //0-对私； 1-对公
		body.setMER_ORDER_NO("DF1234567811");

		req_bean.getBODYS().add(body);

			/*MsgBody body2 = new MsgBody();
			body2.setSN("0000000000000002");
			body2.setACC_NO("6216261003004150044");
			body2.setACC_NAME("陈氏阿凤有限公司");
			body2.setAMOUNT("101");
			body2.setACC_PROVINCE("广东");
			body2.setACC_CITY("广州");
			body2.setACC_PROP("1"); //0-对私； 1-对公
			body2.setBANK_NAME("中国银行股份有限公司广州天文苑支行");
			req_bean.getBODYS().add(body2);

			MsgBody body3 = new MsgBody();
			body3.setSN("0000000000000003");
			body3.setACC_NO("6222142003004150043"); //工行信用卡
			body3.setACC_NAME("王午3");
			body3.setAMOUNT("2000");
			body3.setBANK_NAME("工商银行同福东路支行");
			req_bean.getBODYS().add(body3);*/

		String res = sendAndRead(signANDencrypt(req_bean));
		logger.info("响应报文："+res);
		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());
	}

	//代付查询
	public static void pay_query() {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.1");
		req_bean.setMSG_TYPE("100002");
		req_bean.setBATCH_NO("6FD2E4D4");//同代付交易请求批次号
//		req_bean.setUSER_NAME("13760136514");//系统后台登录名 00000001
		req_bean.setUSER_NAME("15901837011");//系统后台登录名 302020000001

//		MsgBody body = new MsgBody();
//		body.setQUERY_NO_FLAG("0");
//		body.setMER_ORDER_NO("DF123456789");
//		req_bean.getBODYS().add(body);
		String res = sendAndRead(signANDencrypt(req_bean));
		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());

	}

	//查询银行卡所属银行
	public static void queryAccountInfo() {

		MsgBean req_bean = new MsgBean();
		req_bean.setVERSION("2.0");
		req_bean.setMSG_TYPE("400001");
		req_bean.setBATCH_NO("99EE936559D864");
		req_bean.setUSER_NAME("13760136514");//系统后台登录名

		MsgBody body = new MsgBody();
		body.setSN("101000004");
		body.setACC_NO("6225380048403812");
		req_bean.getBODYS().add(body);

		String res = sendAndRead(signANDencrypt(req_bean));

		MsgBean res_bean = decryptANDverify(res);

		if("0000".equals(res_bean.getTRANS_STATE())) {
			logger.info("请求成功");
		}
		logger.info(res_bean.toXml());

	}



	private static MsgBean decryptANDverify(String res) {

		String msg_sign_enc = res.split("\\|")[0];
		String key_3des_enc = res.split("\\|")[1];

		//解密密钥
		String key_3des = RSA.decrypt(key_3des_enc,mer_pfx_key,mer_pfx_pass);

		//解密报文
		String msg_sign = TripleDes.decrypt(key_3des, msg_sign_enc);
		MsgBean res_bean = new MsgBean();
		res_bean.toBean(msg_sign);
		logger.info("res:" + res_bean.toXml());

		//验签
		String dna_sign_msg = res_bean.getMSG_SIGN();
		res_bean.setMSG_SIGN("");
		String verify = Strings.isNullOrEmpty(res_bean.getVERSION())? res_bean.toXml(): res_bean.toSign() ;
		logger.info("verify:" + verify);
		if(!RSA.verify(dna_sign_msg, dna_pub_key, verify)) {
			logger.info("验签失败");
			res_bean.setTRANS_STATE("00A0");
		}
		return res_bean;
	}

	private static String signANDencrypt(MsgBean req_bean) {

		//商户签名

		System.out.println("before sign xml =="+ req_bean.toSign());
		System.out.println("msg sign = "+RSA.sign(req_bean.toSign(),mer_pfx_key,mer_pfx_pass));
		req_bean.setMSG_SIGN(RSA.sign(req_bean.toSign(),mer_pfx_key,mer_pfx_pass));
		logger.info("req:" + req_bean.toXml());

		//加密报文
		String key = Util.generateKey(9999,24);
		logger.info("key:" + key);
		String req_body_enc = TripleDes.encrypt(key, req_bean.toXml());
		logger.info("req_body_enc:" + req_body_enc);
		//加密密钥
		String req_key_enc = RSA.encrypt(key, dna_pub_key);
		logger.info("req_key_enc:" + req_key_enc);
		logger.info("signANDencrypt:" + req_body_enc+"|"+req_key_enc);
		return req_body_enc+"|"+req_key_enc;

	}

	public static String sendAndRead(String req) {

		try {
			HttpURLConnection connect = new SslConnection().openConnection(url);

			connect.setReadTimeout(30000);
			connect.setConnectTimeout(10000);

			connect.setRequestMethod("POST");
			connect.setDoInput(true);
			connect.setDoOutput(true);
			connect.connect();

			byte[] put = req.getBytes("UTF-8");
			connect.getOutputStream().write(put);

			connect.getOutputStream().flush();
			connect.getOutputStream().close();
			String res = SslConnection.read(connect);

			connect.getInputStream().close();
			connect.disconnect();

//			String res = new SslConnection().connect(url);

			return res;
		} catch(Exception e) {
			logger.info(Strings.getStackTrace(e));
		}
		return "";
	}
}
