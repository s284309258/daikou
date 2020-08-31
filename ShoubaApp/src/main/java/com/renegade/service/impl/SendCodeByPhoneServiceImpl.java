package com.renegade.service.impl;

import com.renegade.config.AjaxJson;
import com.renegade.config.RegexUtil;
import com.renegade.config.VerificationPhone;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.M9UserMapper;
import com.renegade.domain.FrontUserDO;
import com.renegade.service.SendCodeByPhoneService;
import com.renegade.service.VerifyRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class SendCodeByPhoneServiceImpl implements SendCodeByPhoneService {

	@Autowired
	private FrontUserDao wFrontUserDaoImpl;
	@Autowired
	private VerifyRecordService verifyRecordServiceImpl;
	@Autowired
	private M9UserMapper m9UserMapper;

//注册发送验证码
	@Override
	public AjaxJson regiestSendCodeByPhone(String phone, HttpServletRequest req) {
		AjaxJson ajaxJson = new AjaxJson();
		Map<String, Object> newUser = m9UserMapper.findUserByuPhone(phone);
		if (newUser != null) {
			ajaxJson.setMsg("该邮箱/手机号已注册");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		ajaxJson = verifyRecordServiceImpl.addVerifyRecord(phone, "regiest", "1");
		return ajaxJson;
	}

	@Override
	public AjaxJson updatePhoneSendCode(String oldPhone, String newPhone, int userId) {
		AjaxJson ajaxJson = new AjaxJson();
		FrontUserDO user = wFrontUserDaoImpl.get(userId);
		if (user == null) {
			ajaxJson.setMsg("原手机号号码与当前登录用户手机号不匹配");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		if (!oldPhone.equals(user.getUserTel())) {
			ajaxJson.setMsg("原手机号号码与当前登录用户手机号不匹配");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		FrontUserDO newUser = wFrontUserDaoImpl.findUserByPhone(newPhone);
		if (newUser != null) {
			ajaxJson.setMsg("新手机号已被注册，不能修改成改新手机号");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
//		ajaxJson = verifyRecordServiceImpl.addVerifyRecord(oldPhone, "updatePhone", "1");
		return ajaxJson;
	}

	@Override
	public AjaxJson updateLoginPassSendCode(int userId) {
		AjaxJson ajaxJson = new AjaxJson();
		FrontUserDO user = wFrontUserDaoImpl.get(userId);
		if (user == null) {
			ajaxJson.setMsg("无法获取用户信息，请重新登录");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		if (user.getUserTel() != null) {
			ajaxJson = verifyRecordServiceImpl.addVerifyRecord(user.getUserTel(), "updatePass", "1");
		} else {
//			ajaxJson = verifyRecordServiceImpl.addVerifyRecord(user.getUserEmail(), "updatePass", "2");
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson updatePayPassSendCode(int userId, HttpServletRequest request) {
		AjaxJson ajaxJson = new AjaxJson();
		FrontUserDO user = wFrontUserDaoImpl.get(userId);
		if (user == null) {
			ajaxJson.setMsg("无法获取用户手机号，请重新登录");
			// ajaxJson.setMsg(PropertyUtil.getProperty("getPhoneErroe",
			// request.getSession().getAttribute("type").toString()));
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		boolean flag = VerificationPhone.verificationPhone(user.getUserTel());
		boolean flag1 = RegexUtil.isHKPhoneLegal(user.getUserTel());
		boolean flag2 = RegexUtil.isMCPhoneLegal(user.getUserTel());
		boolean flag3 = RegexUtil.isTWPhoneLegal(user.getUserTel());
		if (flag || flag1 || flag2 || flag3) {// 只要满足一个就走这个
			ajaxJson = verifyRecordServiceImpl.addVerifyRecord(user.getUserTel(), "updatePayPass", "1");
		} else if (RegexUtil.isValidEmail(user.getUserTel())) {// 邮箱正确
			ajaxJson = verifyRecordServiceImpl.addVerifyRecord(user.getUserTel(), "updatePayPass", "2");
		} else {
			ajaxJson.setMsg("账号格式不正确");
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson findPassSendCcde(String phone) {
		AjaxJson ajaxJson = new AjaxJson();
		if (StringUtils.isEmpty(phone)) {
			ajaxJson.setMsg("请填写该账号");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
//		FrontUserDO user = wFrontUserDaoImpl.findUserByEmail(phone);
		Map<String, Object> user = m9UserMapper.findUserByuPhone(phone);
		if (user == null) {
			ajaxJson.setMsg("该账号未注册，不能进行找回密码");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		boolean flag = VerificationPhone.verificationPhone(phone);
		boolean flag1 = RegexUtil.isHKPhoneLegal(phone);
		boolean flag2 = RegexUtil.isMCPhoneLegal(phone);
		boolean flag3 = RegexUtil.isTWPhoneLegal(phone);
		if (flag || flag1 || flag2 || flag3) {// 只要满足一个就走这个
			ajaxJson = verifyRecordServiceImpl.addVerifyRecord(phone, "findPass", "1");
		} else if (RegexUtil.isValidEmail(phone)) {// 邮箱正确
			ajaxJson = verifyRecordServiceImpl.addVerifyRecord(phone, "findPass", "2");
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("账号格式不正确");
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson tranSendCode(int userId) {
		AjaxJson ajaxJson = new AjaxJson();
		Map<String, Object> user = m9UserMapper.findUserByuserId(String.valueOf(userId));
		if (user == null) {
			ajaxJson.setMsg("无法获取用户手机号，请重新登录");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		ajaxJson = verifyRecordServiceImpl.addVerifyRecord(user.get("telphone").toString(), "transfer", "1");
		return ajaxJson;
	}
}
