package com.renegade.service.impl;

import com.renegade.config.*;
import com.renegade.config.time.TimeUtil;
import com.renegade.dao.*;
import com.renegade.domain.ExchangeDO;
import com.renegade.domain.FrontTiBiLogDo;
import com.renegade.domain.FrontUserDO;
import com.renegade.domain.TranferRecordDO;
import com.renegade.service.ParamService;
import com.renegade.service.UsdtChargeService;
import com.renegade.service.VerifyRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月24日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service
public class UsdtChargeServiceImpl implements UsdtChargeService {
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private FrontTiBiLogDao frontTiBiLogDao;
	@Autowired
	private WalletManagerDao walletManagerDao;
	@Autowired
	private TranferRecordDao tranferRecordDao;
	@Autowired
	private ExchangeDao exchangeDao;
	@Autowired
	private VerifyRecordService verifyRecordServiceImpl;
	@Autowired
	private M9UserMapper m9UserMapper;

	@Override
	@Transactional
	public AjaxJson inserSendLog(BigDecimal num, String sendAddress, int userId, String passWord, String type) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		// 未认证的用户不允许提币
		try {
			Long nowTIme = Long.valueOf(TimeUtil.getJ8TimeString());
			if (nowTIme >= 2000 && nowTIme <= 4000) {
				ajaxJson.setMsg("结算时间段，请稍候在操作");
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			BigDecimal minTranNum = new BigDecimal(paramServiceImpl.findValue("minTranNum").getParamValue());
			if (num.compareTo(minTranNum) == -1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("最低提币数量为" + minTranNum);
				return ajaxJson;
			}
			FrontUserDO user = frontUserDao.getLock(userId);
			// TODO Auto-generated method stub
			if (StringUtils.isEmpty(num.toString()) || StringUtils.isBlank(sendAddress)
					|| StringUtils.isEmpty(passWord)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("信息请填写完整");
				return ajaxJson;
			}
			String pass = MD5.strToMd5(passWord);
			if (StringUtils.isBlank(user.getPayPassword())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请设置交易密码");
				return ajaxJson;
			}
			if (!user.getPayPassword().equals(pass)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码错误");
				return ajaxJson;
			}
			FrontTiBiLogDo wFrontTiBiLog = new FrontTiBiLogDo();
			wFrontTiBiLog.setSendAddress(sendAddress);// 转出地址
			wFrontTiBiLog.setUserId(userId);// 提币id
			String orderId = RandomNumber.generateRandomString3(27, "");// 订单id
			wFrontTiBiLog.setSendNo(orderId);// t提币订单
			wFrontTiBiLog.setSendNum(num);// 提币数量
			wFrontTiBiLog.setSendCheck(0);// 默认带审核，提交到后台审核
			// 计算手续费。默认为0
			String string = paramServiceImpl.findValue("tibiCharge").getParamValue();

			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("balacne", num);
			map.put("sourceId", "0");
			map.put("orderNo", orderId);
			map.put("benefitType", 4);
			int m = walletManagerDao.updateReduceVs(map);
			if (m != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("余额不够");
				return ajaxJson;
			}
			BigDecimal num1 = num.multiply(new BigDecimal(string)).setScale(2, BigDecimal.ROUND_HALF_UP);
			map.put("benefitType", 500);
			map.put("balacne", num1);
			m = walletManagerDao.updateReduceVs(map);// 提币手续费扣减
			if (m != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("余额不够");
				return ajaxJson;
			}
			wFrontTiBiLog.setCharge(num1);
			wFrontTiBiLog.setSendStatus(0);
			int i = frontTiBiLogDao.addTiBiLog(wFrontTiBiLog);
			if (i != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("系统异常");
				return ajaxJson;
			}
			// 变为非信仰者
			frontUserDao.updateLimitssssUser(userId);
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
			return ajaxJson;
		}
	}

	// 配票结算

	/**
	 * 会员升级
	 */
	@Override
	public void membershipUpgrade(Integer userId) {
		// TODO Auto-generated method stub
		FrontUserDO frontUserDO = frontUserDao.get(userId);
		if (frontUserDO.getIsActive() == 0) {// 判定是否为活跃用户
			return;
		}
		if (frontUserDO.getUserLevel() == 0) {
			int t = frontUserDao.getZhituiNum(userId);
			if (t < Integer.parseInt(paramServiceImpl.findValue("updateJobber").getParamValue())) {// 升级为批发商直推人数
				return;
			}
//			Map<String, Object> map=frontUserDao.getunderNumAllUnderNUm(userId, frontUserDO.getUserLevel(),Integer.parseInt(paramServiceImpl.findValue("updateJobber").getParamValue()));
			t = frontUserDao.underNumAll(userId);
			System.out.println("=========" + paramServiceImpl.findValue("jobber").getParamValue());
			if (t >= Integer.parseInt(paramServiceImpl.findValue("jobber").getParamValue())) {
				// 升级
				Map<String, Object> map = new HashMap<>();
				map.put("userId", frontUserDO.getUserId());
				map.put("oldLevel", frontUserDO.getUserLevel());
				map.put("newLevel", "1");
				frontUserDao.updateLevel(map);
			}
		} else if (frontUserDO.getUserLevel() == 1) {
			Map<String, Object> map = frontUserDao.getUnderNUm(userId, frontUserDO.getUserLevel(),
					Integer.parseInt(paramServiceImpl.findValue("updateCountry").getParamValue()));
			if (map != null) {
				if (Integer.parseInt(String.valueOf(map.get("allTotal"))) >= Integer
						.parseInt(paramServiceImpl.findValue("countyAgent").getParamValue())) {
					map.put("userId", frontUserDO.getUserId());
					map.put("oldLevel", frontUserDO.getUserLevel());
					map.put("newLevel", "2");
					frontUserDao.updateLevel(map);
				}
			}
		} else if (frontUserDO.getUserLevel() == 2) {
			Map<String, Object> map = frontUserDao.getUnderNUm(userId, frontUserDO.getUserLevel(),
					Integer.parseInt(paramServiceImpl.findValue("updateCity").getParamValue()));
			if (map != null) {
				if (Integer.parseInt(String.valueOf(map.get("allTotal"))) >= Integer
						.parseInt(paramServiceImpl.findValue("cityAgent").getParamValue())) {
					map.put("userId", frontUserDO.getUserId());
					map.put("oldLevel", frontUserDO.getUserLevel());
					map.put("newLevel", "3");
					frontUserDao.updateLevel(map);
				}
			}
		} else if (frontUserDO.getUserLevel() == 3) {
			Map<String, Object> map = frontUserDao.getUnderNUm(userId, frontUserDO.getUserLevel(),
					Integer.parseInt(paramServiceImpl.findValue("updateProvince").getParamValue()));
			if (map != null) {
				if (Integer.parseInt(String.valueOf(map.get("allTotal"))) >= Integer
						.parseInt(paramServiceImpl.findValue("provinceAgent").getParamValue())) {
					map.put("userId", frontUserDO.getUserId());
					map.put("oldLevel", frontUserDO.getUserLevel());
					map.put("newLevel", "4");
					frontUserDao.updateLevel(map);
				}
			}
		} else if (frontUserDO.getUserLevel() == 4) {
			Map<String, Object> map = frontUserDao.getUnderNUm(userId, frontUserDO.getUserLevel(),
					Integer.parseInt(paramServiceImpl.findValue("updateCenter").getParamValue()));
			if (map != null) {
				if (Integer.parseInt(String.valueOf(map.get("allTotal"))) >= Integer
						.parseInt(paramServiceImpl.findValue("operationCenter").getParamValue())) {
					map.put("userId", frontUserDO.getUserId());
					map.put("oldLevel", frontUserDO.getUserLevel());
					map.put("newLevel", "5");
					frontUserDao.updateLevel(map);
				}
			}
		}

	}

	@Override
	@Transactional
	public AjaxJson transferAccountsUser(Map<String, Object> map, Integer userId) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			Map<String, Object> user = m9UserMapper.findUserByuserId(String.valueOf(userId));
			// 对比验证码是否正确
			ajaxJson = verifyRecordServiceImpl.compare(null, "transfer", "1",
					user.get("telphone").toString(), map.get("code").toString(), ajaxJson);
//			Long nowTIme = Long.valueOf(TimeUtil.getJ8TimeString());
//			if (nowTIme >= 2000 && nowTIme <= 4000) {
//				ajaxJson.setMsg("结算时间段，请稍候在操作");
//				ajaxJson.setSuccess(true);
//				return ajaxJson;
//			}
			if (!ajaxJson.isSuccess()) {
				return ajaxJson;
			}
			// 校验密码
			String pass = MD5.strToMd5(StringUtil.getMapValue(map, "passWord"));
			if (StringUtils.isBlank(user.get("pay_pwd").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请设置交易密码");
				return ajaxJson;
			}
			if (!user.get("pay_pwd").toString().equals(pass)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码错误");
				return ajaxJson;
			}
			if (!RegexUtil.isValidNumFloat4S(map.get("balacne").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("转账金额有误");
				return ajaxJson;
			}
			if (map.get("tansferAcount").toString().equals(user.get("telphone").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("对不起！自己不能与自己转账");
				return ajaxJson;
			}
			//转账金额
            String num=String.valueOf(map.get("balacne"));
			// 转入用户信息
			Map<String, Object> toUserDO = m9UserMapper.findUserByuPhone(StringUtil.getMapValue(map, "tansferAcount"));
			if (toUserDO==null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("该用户不存在！请重新输入");
				return ajaxJson;
			}
			if ("2".equals(toUserDO.get("user_state").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("该用户已经被封号");
				return ajaxJson;
			}
			TranferRecordDO fTranferRecordDO = new TranferRecordDO();
			String orderNo = RandomNumber.generateRandomString3(27, "");
			fTranferRecordDO.setMoney(new BigDecimal(map.get("balacne").toString()));
			fTranferRecordDO.setId(orderNo);
			// 根据类型去判断支付哪种币
			if (StringUtil.getMapValue(map, "type").equals("1")) {// usdt
				// 转出者扣钱
				map.put("userId", userId);
				map.put("sourceId", "0");
				map.put("orderNo", orderNo);
				map.put("benefitType", "6");// 转出usdtt
				map.put("telphone",user.get("telphone").toString());
				int t = m9UserMapper.updateReduceCoin(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				// 扣除手续费
				map.put("benefitType", "5");// 手续费
				BigDecimal charge = new BigDecimal(paramServiceImpl.findValue("transferCharge").getParamValue())
						.multiply(new BigDecimal(num)).setScale(4, BigDecimal.ROUND_HALF_UP);
				map.put("balacne", charge);// 手续费
				t = m9UserMapper.updateReduceCoin(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("手续费不够");
					return ajaxJson;
				}
				map.put("userId", toUserDO.get("id"));
				map.put("sourceId", userId);
				map.put("benefitType", "4");// 转入usdtt
				map.put("balacne", num);// 手续费
				t = m9UserMapper.updateAddCoin(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("该用户还不存在钱包哟！");
					return ajaxJson;
				}

				fTranferRecordDO.setType(1);
			} else if (StringUtil.getMapValue(map, "type").equals("2")) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("系统异常");
				return ajaxJson;
			} else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("系统异常");
				return ajaxJson;
			}
			fTranferRecordDO.setUserId(userId);
			fTranferRecordDO.setToUserId(Integer.parseInt(toUserDO.get("id").toString()));
			int t = tranferRecordDao.save(fTranferRecordDO);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("转账失败！");
				return ajaxJson;
			}
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
			return ajaxJson;
		}
	}

	/**
	 * 校验支付密码,map参数
	 * 
	 * @param map
	 * @param frontUserDO
	 * @param strings
	 * @return
	 */
	public AjaxJson selfCheckTrasferPass(Map<String, Object> map, FrontUserDO frontUserDO, String[] strings) {
		AjaxJson ajaxJson = new AjaxJson();
		if (strings != null && strings.length > 0) {
			for (String string : strings) {
				if (StringUtils.isBlank(StringUtil.getMapValue(map, string))) {
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("信息请填写完整");
					return ajaxJson;
				}
			}
		}
		String pass = MD5.strToMd5(StringUtil.getMapValue(map, "passWord"));
		if (StringUtils.isBlank(frontUserDO.getPayPassword())) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请设置交易密码");
			return ajaxJson;
		}
		if (!frontUserDO.getPayPassword().equals(pass)) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("密码错误");
			return ajaxJson;
		}
		ajaxJson.setSuccess(true);
		return ajaxJson;

	}

	/**
	 * 兑换 type=1 usdt兑换 type=2 ut兑换
	 */
	@Override
	@Transactional
	public AjaxJson exchange(Integer userId, String num, String type, String passWord) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			Map<String, Object> map = new HashMap<>();
			FrontUserDO frontUserDO = frontUserDao.get(userId);
			ajaxJson = selfCheckTrasferPass(map, frontUserDO, null);
			if (!RegexUtil.isValidNumFloat(num)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("输入的数量有误");
				return ajaxJson;
			}
			String orderNo = RandomNumber.generateRandomString3(27, "");
			map.put("userId", userId);
			map.put("orderNo", orderNo);
			map.put("balacne", num);
			map.put("orderNo", orderNo);
			map.put("sourceId", "0");
			ExchangeDO exchangeDO = new ExchangeDO();
			exchangeDO.setId(orderNo);
			exchangeDO.setCreateTime(new Date());
			exchangeDO.setUserId(userId);
			exchangeDO.setMoney(new BigDecimal(num));
			if (type.equals("USDT")) {// usdt转换vs
				// 转换之后的金额
				String string = paramServiceImpl.findValue("usdtTovs").getParamValue();
				BigDecimal bigDecimal = new BigDecimal(string).multiply(new BigDecimal(num)).setScale(4,
						BigDecimal.ROUND_HALF_UP);
				map.put("vsbalacne", bigDecimal);
				map.put("benefitType", 4);// usdt兑换
				int t = walletManagerDao.updateUsdtToVs(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				exchangeDO.setType(1);
			} else {// ut转vs
				String string = paramServiceImpl.findValue("utToVs").getParamValue();
				BigDecimal bigDecimal = new BigDecimal(string).multiply(new BigDecimal(num)).setScale(4,
						BigDecimal.ROUND_HALF_UP);
				map.put("vsbalacne", bigDecimal);
				map.put("benefitType", 4);// ut兑换
				int t = walletManagerDao.updateUtToVs(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				exchangeDO.setType(2);
			}
			if (exchangeDao.save(exchangeDO) != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("兑换失败");
				return ajaxJson;
			}
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
			return ajaxJson;

		}
	}
}
