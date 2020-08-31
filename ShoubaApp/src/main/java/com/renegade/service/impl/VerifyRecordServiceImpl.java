package com.renegade.service.impl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.renegade.config.AjaxJson;
import com.renegade.config.PhoneSendCode;
import com.renegade.config.PropertyUtil;
import com.renegade.config.RegexUtil;
import com.renegade.config.SendEmail;
import com.renegade.config.VerificationPhone;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.M9UserMapper;
import com.renegade.dao.VerifyRecordMapper;
import com.renegade.domain.FrontUserDO;
import com.renegade.service.ParamService;
import com.renegade.service.VerifyRecordService;


@Service
public class VerifyRecordServiceImpl implements VerifyRecordService {

	@Autowired
	private ParamService paramServiceImpl;

	@Autowired
	private FrontUserDao wFrontUserDaoImpl;
	@Autowired
	private VerifyRecordMapper verifyRecordDaoImpl;
	@Autowired
	private M9UserMapper m9UserMapper;

	/**
	 * 发送验证码
	 */
	@Override
	public AjaxJson addVerifyRecord(String phone, String busType, String accType) {
		AjaxJson ajaxJson = new AjaxJson();
		 int verifySendHourLimit =
		 Integer.parseInt(paramServiceImpl.findValue("verifySendHourLimit").getParamValue());//每小时6次
		 int verifySendDayLimit =
		 Integer.parseInt(paramServiceImpl.findValue("verifySendDayLimit").getParamValue());//每天30次
		 int verifySendDuration =
		 Integer.parseInt(paramServiceImpl.findValue("verifySendDuration").getParamValue());//发送验证码间隔时间1分钟
		 int verifyInvalidDuration =
		 Integer.parseInt(paramServiceImpl.findValue("verifyInvalidDuration").getParamValue());//验证码有效时长5分钟
		//int verifySendHourLimit = 10;// Integer.parseInt(tSysParamServiceImpl.findValue("verifySendHourLimit").getParamValue());//每小时6次
		//int verifySendDayLimit = 30;// Integer.parseInt(tSysParamServiceImpl.findValue("verifySendDayLimit").getParamValue());//每天30次
		//int verifySendDuration = 1;// Integer.parseInt(tSysParamServiceImpl.findValue("verifySendDuration").getParamValue());//发送验证码间隔时间1分钟
		//int verifyInvalidDuration = 5;
		Map<String, Object> lastSendMap = getInfoLastForSendCheck(phone, accType, busType);
		if(lastSendMap!=null) {
			if(!contrastTime(getDate("send_time", lastSendMap), verifySendDuration)) {//如果两个时间差了十分钟以上
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("同一个手机号一分钟内不能多次获取验证码");
				return ajaxJson;
			}
		}

		//拿到1个小时的发送次数
				int hourSendNum=hourSendNum(phone);
				if(hourSendNum>verifySendHourLimit) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("每个手机号每小时发送次数只能为"+verifySendHourLimit+"次");
					return ajaxJson;
				}
				//拿到24个小时的发送次数
				int daySendNum=daySendNum(phone);
				if(daySendNum>verifySendDayLimit) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("每个手机号每天发送次数只能为"+verifySendDayLimit+"次");
					return ajaxJson;
				}
				String code = PhoneSendCode.getCode();
				if("1".equals(accType)){//手机号
					
					String result= PhoneSendCode.sendCode(phone,code);
					if(result.equals("success")){
						ajaxJson.setSuccess(true);
					} else {
						ajaxJson.setSuccess(false);
					}
				} else {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("目前只支持手机发送短信验证码");
				}
				Map<String,Object>map = new HashMap<>();
				if(ajaxJson.isSuccess() == true){
					if(busType.equals("regiest") || busType.equals("addCard")){//注册
					} else {
//						FrontUserDO user = wFrontUserDaoImpl.findUserByPhone(phone);
						Map<String, Object> user=m9UserMapper.findUserByuPhone(phone);
						map.put("user_id", user.get("telphone"));
					}
					map.put("code", code);
					map.put("bus_type", busType);
					map.put("account", phone);
					map.put("status", 0);
					map.put("acc_type", accType);
					map.put("create_time", get_format5(new Date()));//发送时间
					map.put("send_time", get_format5(new Date()));//发送时间
					map.put("invalid_time", getInvalidTime(verifyInvalidDuration));//失效时间
					int rows = verifyRecordDaoImpl.addVerifyRecord(map);
					if(rows != 1){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("记录短信验证码发送记录失败");
						return ajaxJson;
					}
					ajaxJson.setMsg("验证码发送");
					return ajaxJson;
				} else {
					ajaxJson.setMsg("发送验证码失败");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}

	}

	@Override
	public AjaxJson compare(String id, String busType, String accType, String account, String code, AjaxJson ajaxJson) {
//		if (code.equals("999999")) {
//			ajaxJson.setSuccess(true);
//			return ajaxJson;
//		}
		int i = 0;
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("user_id", id);// 用户ID
		condition.put("system_type", null);// 系统类型
		condition.put("bus_type", busType);// 业务类型
		condition.put("acc_type", accType);// 账号类型 1手机 2邮箱
		condition.put("account", account);// 账号
		try {
			// 拿到发送账号在当前系统 最后发送的验证码
			Map<String, Object> lastSendMap = verifyRecordDaoImpl.getInfolast(condition);
			// 请发送验证码
			if (lastSendMap == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请发送验证码");
				return ajaxJson;
			}

			// 验证码不正确
			if (!code.equals(lastSendMap.get("code").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码不正确");
				return ajaxJson;
			}
			// 如果已经被验证
			if ("1".equals(lastSendMap.get("status").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("该验证码已被验证");
				return ajaxJson;
			}
			// 验证码已经失效
			if (contrastTime(getDate("invalid_time", lastSendMap), 0)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("该验证码已失效");
				return ajaxJson;
			}
			// 如果用户输入的验证码是一样的 改变该验证码的状态
			lastSendMap.put("status", "1");// 验证码状态：已被验证
			lastSendMap.put("verify_time", get_format5(new Date()));// 验证时间
			i = verifyRecordDaoImpl.update(lastSendMap);// 标记为已被验证
			if (i != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("标记验证码异常");
				return ajaxJson;
			}
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("验证码对比异常");
			return ajaxJson;
		}
	}

	/**
	 * <用于发送验证> 拿到发送账号在当前系统 最后发送的验证
	 * 
	 * @param account
	 * @return
	 */
	public Map<String, Object> getInfoLastForSendCheck(String account, String accType, String busType) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("system_type", null);
		condition.put("account", account);
		condition.put("bus_type", busType);
		condition.put("acc_type", accType);
		return verifyRecordDaoImpl.getInfolast(condition);
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 拿到1个小时的发送次数
	 * 
	 * @param account
	 * @return
	 */
	public int hourSendNum(String account) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("system_type", null);
		condition.put("account", account);
		condition.put("nowTime", sdf.format(new Date()));
		condition.put("hour", 1);
		return verifyRecordDaoImpl.getPeriodCount(condition);
	}

	/**
	 * 拿到24个小时的发送次数
	 * 
	 * @param account
	 * @return
	 */
	public int daySendNum(String account) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("system_type", null);
		condition.put("account", account);
		condition.put("nowTime", sdf.format(new Date()));
		condition.put("hour", 24);
		return verifyRecordDaoImpl.getPeriodCount(condition);
	}

	/**
	 * <p>
	 * 根据Key返回一个Date
	 * </p>
	 * 
	 * @param key
	 * @return Date
	 */
	public static Date getDate(String key, Map<String, Object> map) {
		if (map.get(key) != null) {
			return strToDateLong(map.get(key).toString());
		} else {
			return null;
		}
	}

	private static SimpleDateFormat format5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 把字符串时间转换为date类型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = format5.parse(strDate, pos);
		System.out.println(strtodate);
		return strtodate;
	}

	/**
	 * 验证某个时间是否与当前时间差了某分钟 比如 验证2018-06-15 16:18:45与当前时间是否差了十分钟
	 */
	public static boolean contrastTime(Date contrastTime, int minute) {
		Calendar nowCal = Calendar.getInstance();
		long now = nowCal.getTimeInMillis();// 现在时间
		nowCal.setTime(contrastTime);
		long lastly = nowCal.getTimeInMillis();// 最后一条验证的时间
		if ((now - lastly) > (minute * 60 * 1000)) {// 如果两个时间差了十分钟以上
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 获取某一天时间,格式例如：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String get_format5(Date date) {
		String firstDay = format5.format(date);
		return firstDay;
	}

	/**
	 * 得到失效时间
	 * 
	 * @param verifyInvalidDuration
	 * @return
	 */
	private static String getInvalidTime(int verifyInvalidDuration) {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, verifyInvalidDuration);
		return get_format5(nowTime.getTime());
	}

}
