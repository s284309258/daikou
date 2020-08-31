package com.renegade.service.impl;

import com.renegade.config.AjaxJson;
import com.renegade.config.MD5;
import com.renegade.config.RegexUtil;
import com.renegade.config.Utils;
import com.renegade.dao.M9OrderMapper;
import com.renegade.dao.M9UserMapper;
import com.renegade.dao.VerifyRecordMapper;
import com.renegade.domain.ParamDO;
import com.renegade.service.M9UserService;
import com.renegade.service.ParamService;
import com.renegade.service.VerifyRecordService;
import com.renegade.util.DateUtil;
import com.renegade.util.common.MapConvertUtil;
import com.renegade.util.common.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class M9UserServiceImpl implements M9UserService {

	@Autowired
	private M9UserMapper m9UserMapper;

	@Autowired
	private M9OrderMapper m9OrderMapper;

	@Autowired
	private VerifyRecordMapper verifyRecordMapper;

	@Autowired
	private VerifyRecordService verifyRecordServiceImpl;

	@Autowired
	private ParamService paramService;

	@Override
	public AjaxJson register(Map<String, Object> map) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtils.isEmpty(StringUtil.getMapValue(map, "telphone"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入账号");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(StringUtil.getMapValue(map, "nickName"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入呢称");
				return ajaxJson;
			}
			if (!RegexUtil.isValidTel(StringUtil.getMapValue(map, "telphone"))
					&& !RegexUtil.isValidEmail(StringUtil.getMapValue(map, "telphone"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("手机号异常");
				return ajaxJson;
			}
			// 核对验证码
			ajaxJson = verifyRecordServiceImpl.compare(null, "regiest", "1", StringUtil.getMapValue(map, "telphone"),
					StringUtil.getMapValue(map, "code"), ajaxJson);
			if (!ajaxJson.isSuccess()) {
				return ajaxJson;
			}
			if (StringUtils.isEmpty(StringUtil.getMapValue(map, "login_pwd"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入登录密码");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(StringUtil.getMapValue(map, "login_pwd1"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入登录密码");
				return ajaxJson;
			}
			if (!StringUtil.getMapValue(map, "login_pwd").equals(StringUtil.getMapValue(map, "login_pwd1"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("两次输入的密码不一致");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(StringUtil.getMapValue(map, "pay_pwd1"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入支付密码");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(StringUtil.getMapValue(map, "pay_pwd"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入支付密码");
				return ajaxJson;
			}
			if (!StringUtil.getMapValue(map, "pay_pwd1").equals(StringUtil.getMapValue(map, "pay_pwd"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("两次输入支付交易的密码不一致");
				return ajaxJson;
			}
			// 通过推荐码判断推荐人是否存在
			Map<String, Object> userMap = m9UserMapper.findUserByCode(StringUtil.getMapValue(map, "user_pid"));
			if (userMap == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入正确的邀请码");
				return ajaxJson;
			}
			// 填写注册信息（定位现在还未开启）
			String invitationCode = Utils.getInvitationCode();
			String newPass = MD5.strToMd5(StringUtil.getMapValue(map, "login_pwd"));
			String newPass1 = MD5.strToMd5(StringUtil.getMapValue(map, "pay_pwd"));
			map.put("login_pwd", newPass);
			map.put("pay_pwd", newPass1);
			map.put("share_rqcode", invitationCode);
			map.put("user_pid", userMap.get("id"));
			// 代数
			map.put("algebra", Integer.parseInt(userMap.get("algebra").toString()) + 1);// 自己的直推父级+1就是代数
			// 更新父级链以后逗号分隔
			String parentChain = "";
			if (StringUtils.isEmpty(String.valueOf(map.get("parent_chain")))
					|| StringUtils.isBlank(String.valueOf(map.get("parent_chain")))) {
				parentChain = String.valueOf(userMap.get("id"));
			} else {
				parentChain = String.valueOf(userMap.get("parent_chain")) + "," + String.valueOf(userMap.get("id"));
			}
			map.put("parent_chain", parentChain);

			int cnt = m9UserMapper.register(map);

			if (cnt < 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("注册失败!");
				return ajaxJson;
			}
			/////注册赠送排单券begin
            ParamDO param = paramService.findValue("sign_prize_lineup_coin");
            String sign_prize_lineup_coin = param.getParamValue();
            m9UserMapper.update_register_user_coin_free(MapConvertUtil.StringArrayToMap(new String[]{
                "telphone:"+StringUtil.getMapValue(map, "telphone"),
                "user_lineup_coin_free:user_lineup_coin_free+"+sign_prize_lineup_coin
            }));
            ////注册赠送排单券end
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("注册成功");
			return ajaxJson;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("注册失败");
			return ajaxJson;
		}
	}

	@Override
	public AjaxJson login(Map<String, Object> map) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("登陆成功");

			if (StringUtils.isEmpty(String.valueOf(map.get("telphone")))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("手机号不能为空");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(String.valueOf(map.get("login_pwd")))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码不能为空");
				return ajaxJson;
			}
			map.put("login_pwd", MD5.strToMd5(StringUtil.getMapValue(map, "login_pwd")));
			List<Map<String, Object>> list = m9UserMapper.selectUserList(map);
			if (list.size() < 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("用户名或密码错误");
				return ajaxJson;
			}
//			if ("2".equals(list.get(0).get("user_state"))) {
//				ajaxJson.setSuccess(false);
//				ajaxJson.setMsg("账号已封号,请联系客服");
//				return ajaxJson;
//			}

			ajaxJson.setData(list.get(0).get("id"));
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
			return ajaxJson;
		}
	}

	@Override
	public List<Map<String, Object>> getUserOrderList(Map<String, Object> params) {
		List<Map<String, Object>> list = m9UserMapper.getUserOrderList(params);
		return list;
	}

	@Override
	public List<Map<String, Object>> selectUserList(Map<String, Object> params) {
		List<Map<String, Object>> list = m9UserMapper.selectUserList(params);
		return list;
	}

	@Override
	public int updateOrder(Map<String, Object> params) {
		return 0;
	}

	@Override
	public int updateUser(Map<String, Object> params) {
		int cnt = m9UserMapper.updateUser(params);
		return cnt;
	}

	@Override
	public int insert_change_log(Map<String, Object> params) {
		int cnt = m9UserMapper.insert_change_log(params);
		return cnt;
	}

	@Override
	public AjaxJson select_flowing_record(Map<String, Object> params) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("查询成功");
		List<Map<String, Object>> list = m9UserMapper.select_flowing_record(params);
		ajaxJson.setData(list);
		return ajaxJson;
	}

	/**
	 * 验证交易密码 params:、 1.id=m9user.id 用户id存在session.userId中 2.pay_pwd 交易密码
	 * 
	 * @param params
	 * @return
	 */
	public AjaxJson checkPayPwd(Map<String, Object> params) {

		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");

		if (StringUtils.isEmpty(String.valueOf(params.get("pay_pwd")))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("交易密码不能为空");
			return ajaxJson;
		}

		HashMap<String, Object> chkPay = new HashMap<>();
		chkPay.put("id", params.get("id"));
		chkPay.put("pay_pwd", MD5.strToMd5(StringUtil.getMapValue(params, "pay_pwd")));
		boolean boo_pay = checkUserInfo(chkPay);
		if (!boo_pay) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("交易密码错误");
			return ajaxJson;
		}

		return ajaxJson;
	}

	/**
	 * params:、 1.id=m9user.id 用户id存在session.userId中 2.pay_pwd 交易密码 3.telphone
	 * 收短信的手机号 4.输入的验证码
	 * 
	 * @param params
	 * @return
	 */
	public AjaxJson checkSmsCodeAndPayPwd(Map<String, Object> params) {

		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");

		if (StringUtils.isEmpty(String.valueOf(params.get("code")))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("验证码不能为空");
			return ajaxJson;
		}

		if (StringUtils.isEmpty(String.valueOf(params.get("pay_pwd")))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("交易密码不能为空");
			return ajaxJson;
		}

		HashMap<String, Object> chkPay = new HashMap<>();
		chkPay.put("id", params.get("id"));
		chkPay.put("pay_pwd", MD5.strToMd5(StringUtil.getMapValue(params, "pay_pwd")));
		boolean boo_pay = checkUserInfo(chkPay);
		if (!boo_pay) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("支付密码错误");
			return ajaxJson;
		}

		Map<String, Object> verify = new HashMap<>();
		verify.put("user_id", params.get("telphone"));
		verify.put("account", params.get("telphone"));
		verify.put("status", "0");
		verify.put("bus_type", params.get("bus_type"));
		Map<String, Object> map = verifyRecordMapper.getInfolast(verify);

		if (map != null) {
			String sysCode = map.get("code").toString();
			if (!sysCode.equals(params.get("code"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码错误");
				return ajaxJson;
			}
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("验证码不存在");
			return ajaxJson;
		}

		Map<String, Object> codeMap = new HashMap<>();
		codeMap.put("id", map.get("id"));
		codeMap.put("status", "1");
		verifyRecordMapper.update(codeMap);

		return ajaxJson;
	}

	@Override
	public AjaxJson updateAddress(Map<String, Object> params) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("地址修改成功");

		if (null==params.get("real_name") || "".equals(params.get("real_name"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("姓名不能为空");
			return ajaxJson;
		}

		if (null==params.get("telphone") || "".equals(params.get("telphone"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("手机号码不能为空");
			return ajaxJson;
		}

		if (null==params.get("eos_address") || "".equals(params.get("eos_address"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("收币地址标签不能为空");
			return ajaxJson;
		}

		if (null==params.get("eos_address_label") || "".equals(params.get("eos_address_label"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("收币地址标签不能为空");
			return ajaxJson;
		}

		if (null==params.get("pay_pwd") || "".equals(params.get("pay_pwd"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("交易密码不能为空");
			return ajaxJson;
		}

		if (null==params.get("code") || "".equals(params.get("code"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("验证码不能为空");
			return ajaxJson;
		}

		HashMap<String, Object> chkPay = new HashMap<>();
		chkPay.put("id", params.get("id"));
		chkPay.put("pay_pwd", MD5.strToMd5(StringUtil.getMapValue(params, "pay_pwd")));
		boolean boo_pay = checkUserInfo(chkPay);
		if (!boo_pay) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("交易密码错误");
			return ajaxJson;
		}

		Map<String,Object> map2 = m9OrderMapper.checkSaleAndBuyOrder(params.get("id").toString());
		if(!"0".equals(map2.get("cnt").toString())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("有待处理的订单,不能修改交易地址");
			return ajaxJson;
		}

		Map<String, Object> verify = new HashMap<>();
		verify.put("user_id", params.get("telphone"));
		verify.put("account", params.get("telphone"));
		verify.put("status", "0");
		verify.put("bus_type", "updatePass");
		Map<String, Object> map = verifyRecordMapper.getInfolast(verify);

		if (map != null) {
			String sysCode = map.get("code").toString();
			if (sysCode.equals(params.get("code"))) {
				Map<String, Object> upd_address = new HashMap<>();
				upd_address.put("id", params.get("id"));
				upd_address.put("eos_address", params.get("eos_address"));
				upd_address.put("real_name",params.get("real_name"));
				upd_address.put("eos_address_label",params.get("eos_address_label"));
				int cnt = m9UserMapper.updateUser(upd_address);
				if (cnt < 1) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("修改失败");
					return ajaxJson;
				}
			}
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("验证码错误");
			return ajaxJson;
		}

		Map<String, Object> codeMap = new HashMap<>();
		codeMap.put("id", map.get("id"));
		codeMap.put("status", "1");
		verifyRecordMapper.update(codeMap);
		return ajaxJson;
	}

	@Override
	public AjaxJson updateUserPwd(Map<String, Object> params) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("修改成功");

        if(StringUtil.EmptyStringMD5.equals(params.get("login_pwd"))){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("密码不能为空");
            return ajaxJson;
        }
        if(StringUtil.EmptyStringMD5.equals(params.get("login_pwd1"))){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("密码不能为空");
            return ajaxJson;
        }
		if (!StringUtil.getMapValue(params, "login_pwd").equals(StringUtil.getMapValue(params, "login_pwd1"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("两次输入的密码不一致");
			return ajaxJson;
		}

        if(com.renegade.util.StringUtils.isEmpty(String.valueOf(params.get("code")))){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("验证码不能为空");
            return ajaxJson;
        }

		Map<String, Object> verify = new HashMap<>();
		verify.put("user_id", params.get("telphone"));
		verify.put("account", params.get("telphone"));
		verify.put("status", "0");
		verify.put("bus_type",params.get("busType"));
		Map<String, Object> map = verifyRecordMapper.getInfolast(verify);
		if (map != null) {
			String sysCode = map.get("code").toString();
			if (sysCode.equals(params.get("code"))) {

				Map<String, Object> pwdmap = new HashMap<>();
				pwdmap.put("id", params.get("id"));
				if ("login".equals(params.get("type_pwd"))) {
					pwdmap.put("login_pwd", MD5.strToMd5(StringUtil.getMapValue(params, "login_pwd")));
				} else if ("pay".equals(params.get("type_pwd"))) {
					pwdmap.put("pay_pwd", MD5.strToMd5(StringUtil.getMapValue(params, "login_pwd")));
				} else {
					return ajaxJson;
				}
				int cnt = m9UserMapper.updateUser(pwdmap);
				if (cnt < 1) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("修改失败");
					return ajaxJson;
				}
			} else {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码错误");
				return ajaxJson;
			}
		}
		return ajaxJson;
	}

	/***
	 * 修改用户信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public AjaxJson updateUserInfo(Map<String, Object> params) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("修改成功");
		int cnt = m9UserMapper.updateUser(params);
		if (cnt < 1) {
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("修改失败");
		}
		return ajaxJson;
	}

	public boolean checkUserInfo(Map<String, Object> map) {
		List<Map<String, Object>> list = m9UserMapper.selectUserList(map);
		if (list.size() < 1) {
			return false;
		}
		return true;
	}

	/***
	 * 兑换绿灯
	 * 
	 * @param quantity
	 * @param userId
	 * @param map
	 * @return
	 */
	@Override
	@Transactional
	public AjaxJson exchange_light(String quantity, String userId, Map<String, Object> map) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("兑换成功");

		try{

			List<Map<String, Object>> list = this
					.selectUserList(MapConvertUtil.StringArrayToMap(new String[] { "id:" + userId }));

			Map<String, Object> user_map = list.get(0);

			if ("2".equals(user_map.get("user_state"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("账号已封号,请联系客服");
				return ajaxJson;
			}

			if (StringUtils.isEmpty(String.valueOf(map.get("num")))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("兑换数量不能为空");
				return ajaxJson;
			}

			Integer num = Integer.parseInt(String.valueOf(map.get("num")));
			if (num < 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("兑换数量不能小于1");
				return ajaxJson;
			}

			if(num%2!=0){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("数量只能为2的倍数");
				return ajaxJson;
			}

			if (StringUtils.isEmpty(String.valueOf(map.get("pay_pwd")))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("交易密码不能为空");
				return ajaxJson;
			}

			AjaxJson chk_ajax = this.checkPayPwd(
					MapConvertUtil.StringArrayToMap(new String[] { "id:" + userId, "pay_pwd:" + map.get("pay_pwd") }));

			// 校验交易密码,如果交易密码不正确直接放回chk
			if (!chk_ajax.isSuccess()) {
				return chk_ajax;
			}

			// 兑换两个绿灯,扣除两个红灯和一个蓝灯
			if ("4".equals(quantity)) {
				Integer light4_red = Integer.parseInt(user_map.get("light4_red").toString());
				Integer light4_blue = Integer.parseInt(user_map.get("light4_blue").toString());
				if (light4_red < 1 * num) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("红灯不足,无法兑换绿灯");
					return ajaxJson;
				}
				if (light4_blue < 1 * (num/2)) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("蓝灯不足,无法兑换绿灯");
					return ajaxJson;
				}

				int cnt = this.updateUser(MapConvertUtil.StringArrayToMap(new String[] {
						"id:" + userId,
						"light4_blue:light4_blue-"+(num/2),
						"order_id:"+user_map.get("telphone"),
						"order_type:6664",
						"source_id:4",
						"upd_date:now()"
				}));
				if (cnt != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("兑换失败！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				cnt = this.updateUser(MapConvertUtil.StringArrayToMap(new String[] {
						"id:" + userId,
						"light4_red:light4_red-"+num,
						"order_id:"+user_map.get("telphone"),
						"order_type:6665",
						"source_id:4",
						"upd_date:now()"
				}));
				if (cnt != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("兑换失败！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}

				cnt = this.updateUser(MapConvertUtil.StringArrayToMap(new String[] {
						"id:" + userId,
						"light4_green:light4_green+"+num,
						"order_id:"+user_map.get("telphone"),
						"order_type:6666",
						"source_id:4",
						"upd_date:now()"
				}));
				if (cnt != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("兑换失败！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}

			} else if ("40".equals(quantity)) {
				Integer light40_red = Integer.parseInt(user_map.get("light40_red").toString());
				Integer light40_blue = Integer.parseInt(user_map.get("light40_blue").toString());
				if (light40_red < 1 * num) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("红灯不足,无法兑换绿灯");
					return ajaxJson;
				}
				if (light40_blue < 1 * (num/2)) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("蓝灯,无法兑换绿灯");
					return ajaxJson;
				}

				int cnt = this.updateUser(MapConvertUtil.StringArrayToMap(new String[] {
						"id:" + userId,
						"light40_blue:light40_blue-"+(num/2),
						"order_id:"+user_map.get("telphone"),
						"order_type:6664",
						"source_id:40",
						"upd_date:now()"
				}));
				if (cnt != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("兑换失败！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}

				cnt = this.updateUser(MapConvertUtil.StringArrayToMap(new String[] {
						"id:" + userId,
						"light40_red:light40_red-"+num,
						"order_id:"+user_map.get("telphone"),
						"order_type:6665",
						"source_id:40",
						"upd_date:now()"
				}));
				if (cnt != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("兑换失败！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}

				cnt = this.updateUser(MapConvertUtil.StringArrayToMap(new String[] {
						"id:" + userId,
						"light40_green:light40_green+"+num,
						"order_id:"+user_map.get("telphone"),
						"order_type:6666",
						"source_id:40",
						"upd_date:now()"
				}));
				if (cnt != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("兑换失败！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			return ajaxJson;

		}catch(Exception e){
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson select_flowing_record_light(Map<String, Object> params) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("查询成功");
		List<Map<String, Object>> list = m9UserMapper.select_flowing_record_light(params);
		ajaxJson.setData(list);
		return ajaxJson;
	}


	@Override
	public List<Map<String,Object>> select_sign_record(String user_id,String sign_date) throws Exception{

		List<Map<String,Object>> list = m9UserMapper.select_sign_record(MapConvertUtil.StringArrayToMap(new String[]{
				"user_id:"+user_id,
				"sign_date:"+sign_date
		}));
		return list;
	}

	@Override
	public AjaxJson select_sign_record(String user_id,int lastId) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("查询成功");
		String sign_date = DateUtil.get_format4_today();

		List<Map<String,Object>> list = m9UserMapper.select_sign_record(MapConvertUtil.StringArrayToMap(new String[]{
				"user_id:"+user_id,
				"sign_date:"+sign_date,
                "lastId:"+lastId
		}));
        ajaxJson.setData(list);
		return ajaxJson;
	}


	@Override
	public AjaxJson insert_sign_record(String user_id){
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("签到成功");
		try
		{
			ParamDO param = paramService.findValue("sign_prize_lineup_coin");

			String sign_prize_lineup_coin = param.getParamValue();

			String sign_date = DateUtil.get_format4_today();

			List<Map<String,Object>> list = m9UserMapper.select_sign_record(MapConvertUtil.StringArrayToMap(new String[]{
					"user_id:"+user_id,
					"sign_date:"+sign_date
			}));

			if(list.size()>0){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("今日已签到");
				return ajaxJson;
			}

			int cnt = m9UserMapper.insert_sign_record(MapConvertUtil.StringArrayToMap(new String[]{
					"prize:"+sign_prize_lineup_coin,
					"user_id:"+user_id
			}));

			if(cnt!=1){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("签到失败");
				return ajaxJson;
			}

			cnt = m9UserMapper.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
					"id:"+user_id,
					"user_lineup_coin_free:user_lineup_coin_free+"+sign_prize_lineup_coin
			}));
			if(cnt!=1){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("签到失败");
				return ajaxJson;
			}
            ajaxJson.setMsg(sign_prize_lineup_coin);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return ajaxJson;
	}
}
