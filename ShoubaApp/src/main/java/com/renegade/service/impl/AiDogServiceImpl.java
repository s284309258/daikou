package com.renegade.service.impl;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.renegade.config.AjaxJson;
import com.renegade.config.RegexUtil;
import com.renegade.config.StringUtil;
import com.renegade.config.time.TimeUtil;
import com.renegade.dao.AiRobotContractDao;
import com.renegade.dao.AiRobotMovesBricksDao;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.AiRobotContractDO;
import com.renegade.domain.AiRobotMovesBricksDO;
import com.renegade.domain.FrontUserDO;
import com.renegade.exception.BDException;
import com.renegade.service.AiDogSerivice;
import com.renegade.service.ParamService;
import com.renegade.service.TaskService;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月18日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service
public class AiDogServiceImpl implements AiDogSerivice {
	@Autowired
	private WalletManagerDao walletManagerDao;
	@Autowired
	private AiRobotMovesBricksDao aiRobotMovesBricksDao;
	@Autowired
	private AiRobotContractDao aiRobotContractDao;
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private UsdtChargeServiceImpl usdtChargeServiceImpl;
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private TaskService taskServiceImpl;

	private final static Logger logger = LoggerFactory.getLogger(AiDogServiceImpl.class);

	// 启动活期
	@Override
	@Transactional
	public AjaxJson aiDogMoveBrickStart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtil.getMapValue(map, "type") == "") {
				ajaxJson.setMsg("请选择类型");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			if (!RegexUtil.isValidNumFloat4S(StringUtil.getMapValue(map, "money"))) {
				ajaxJson.setMsg("您输入的数量有误");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			// 根绝类型扣钱冻结
			FrontUserDO frontUserDO = frontUserDao.get(Integer.parseInt(map.get("userId").toString()));
			AjaxJson result = usdtChargeServiceImpl.selfCheckTrasferPass(map, frontUserDO, null);
			if (!result.isSuccess()) {
				return result;
			}
			// 扣钱变为冻结
			String orderNo = StringUtil.getDateTimeAndRandomForID();
			map.put("sourceId", "0");
			map.put("benefitType", 13);
			map.put("orderNo", orderNo);
			map.put("balacne", map.get("money").toString());
			AiRobotMovesBricksDO aiRobotMovesBricksDO = new AiRobotMovesBricksDO();
			aiRobotMovesBricksDO.setUserId(Integer.parseInt(map.get("userId").toString()));
			aiRobotMovesBricksDO.setMoney(new BigDecimal(map.get("money").toString()));
			// 预期收益
			BigDecimal profit = aiRobotMovesBricksDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("activeMoney").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
			if (StringUtil.getMapValue(map, "type").equals("0")) {// USDT
				if (walletManagerDao.updateUsdtFreeze(map) != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				// 插入订单记录
				aiRobotMovesBricksDO.setType(0);
				profit = profit.multiply(new BigDecimal(paramServiceImpl.findValue("usdtTovs").getParamValue()))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				aiRobotMovesBricksDO.setRobotProfit(profit);

			} else if (StringUtil.getMapValue(map, "type").equals("1")) {// UT
				if (walletManagerDao.updateUtFreeze(map) != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				aiRobotMovesBricksDO.setType(1);
				profit = profit.multiply(new BigDecimal(paramServiceImpl.findValue("utToVs").getParamValue()))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				aiRobotMovesBricksDO.setRobotProfit(profit);
			} else {// VFS
				if (walletManagerDao.updateVsFreeze(map) != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				aiRobotMovesBricksDO.setType(2);
				aiRobotMovesBricksDO.setRobotProfit(profit);
			}
			if (aiRobotMovesBricksDao.save(aiRobotMovesBricksDO) < 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("存入失败");
				return ajaxJson;
			}
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

	// 领取收益
	@Override
	@Transactional
	public AjaxJson aKeyToStart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			int t = aiRobotMovesBricksDao.updateKeyStartGet(map);
			if (t < 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("领取失败");
				return ajaxJson;
			}
			AiRobotMovesBricksDO aiRobotMovesBricksDO = aiRobotMovesBricksDao
					.get(Integer.parseInt(map.get("id").toString()));
			String orderNo = StringUtil.getDateTimeAndRandomForID();
			map.put("sourceId", "0");
			map.put("benefitType", 14);
			map.put("orderNo", orderNo);
			map.put("balacne", aiRobotMovesBricksDO.getMoney());
			BigDecimal profit = aiRobotMovesBricksDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("activeMoney").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
			// 钱解冻
			if (aiRobotMovesBricksDO.getType() == 0) {
				t = walletManagerDao.updateUsdtFreezeReduce(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("解冻失败");
					return ajaxJson;
				}
				// 钱到账
				profit=profit
				.multiply(new BigDecimal(paramServiceImpl.findValue("usdtTovs").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
				map.put("balacne", profit);
				map.put("benefitType", 150);
				t = walletManagerDao.updateAddVsHuoqi(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("到账失败");
					return ajaxJson;
				}
			} else if (aiRobotMovesBricksDO.getType() == 1) {
				t = walletManagerDao.updateUtFreezeReduce(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("解冻失败");
					return ajaxJson;
				}
				// 钱到账
				profit=profit
						.multiply(new BigDecimal(paramServiceImpl.findValue("utToVs").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
				map.put("balacne", profit);
				map.put("benefitType", 151);
				t = walletManagerDao.updateAddVsHuoqi2(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("到账失败");
					return ajaxJson;
				}
			} else {
				t = walletManagerDao.updateVsFreezeReduce(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("解冻失败");
					return ajaxJson;
				}
				// 钱到账
				map.put("balacne", profit);
				map.put("benefitType", 152);
				t = walletManagerDao.updateAddVsHuoqi3(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("到账失败");
					return ajaxJson;
				}
			}
			ajaxJson.setSuccess(true);
			if (ajaxJson.isSuccess()) {
				taskServiceImpl.becomeActive(Integer.parseInt(map.get("userId").toString()));
			}
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

	@Override
	public AjaxJson myActiceProfitOrder(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		// 先修改该用户可领取的收益
		AjaxJson ajaxJson = new AjaxJson();
		int t = aiRobotMovesBricksDao.updateStastAble(map);
		List<AiRobotMovesBricksDO> aiRobotMovesBricksDOs = aiRobotMovesBricksDao.list(map);
		if (!aiRobotMovesBricksDOs.isEmpty()) {
			map.put("aiRobotMovesBricksDOs", aiRobotMovesBricksDOs);
			for (AiRobotMovesBricksDO aiRobotMovesBricksDO : aiRobotMovesBricksDOs) {
				if (aiRobotMovesBricksDO.getStatus() == 2) {
					Long date = new Date().getTime();
					date = (date - aiRobotMovesBricksDO.getCreateTime().getTime()) / 1000;
					date = 86400 - date;
					long days = date / (60 * 60 * 24);
					long hours = (date - ((60 * 60 * 24) * days)) / (60 * 60);
					long minutes = (date - (60 * 60 * 24 * days) - hours * 60 * 60) / 60;
					long second = date - (60 * 60 * 24 * days) - (hours * 60 * 60) - (minutes * 60);
					aiRobotMovesBricksDO.setTimeCount(days + "天" + hours + "小时" + minutes + "分" + second + "秒");
				}
			}
			logger.debug("=========aiRobotMovesBricksDOs===>{}", aiRobotMovesBricksDOs);
			ajaxJson.setSuccess(true);
			ajaxJson.setData(aiRobotMovesBricksDOs);
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		return ajaxJson;
	}

	// 智能合约购买
	@Override
	@Transactional
	public AjaxJson smartContractToBuy(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtil.getMapValue(map, "kind") == "") {
				ajaxJson.setMsg("请选择币种类型");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			if (!RegexUtil.isValidNumFloat4S(StringUtil.getMapValue(map, "money"))) {
				ajaxJson.setMsg("您输入的数量有误");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			if (new BigDecimal(paramServiceImpl.findValue("theMinimumPurchasing").getParamValue())
					.compareTo(new BigDecimal(map.get("money").toString())) > 0) {
				ajaxJson.setMsg("您输入的数量小于起购价");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			// 根绝类型扣钱冻结
			FrontUserDO frontUserDO = frontUserDao.get(Integer.parseInt(map.get("userId").toString()));
			AjaxJson result = usdtChargeServiceImpl.selfCheckTrasferPass(map, frontUserDO, null);
			if (!result.isSuccess()) {
				return result;
			}
			String orderNo = StringUtil.getDateTimeAndRandomForID();
			map.put("sourceId", "0");
			map.put("benefitType", 11);
			map.put("orderNo", orderNo);
			map.put("balacne", map.get("money").toString());
			AiRobotContractDO aiRobotContractDO = new AiRobotContractDO();
			aiRobotContractDO.setUserId(Integer.parseInt(map.get("userId").toString()));
			aiRobotContractDO.setMoney(new BigDecimal(map.get("money").toString()));
			aiRobotContractDO.setType(Integer.parseInt(map.get("type").toString()));// 合约类型
			// 预期收益 分类型
			BigDecimal profit = BigDecimal.ZERO;

			if (StringUtil.getMapValue(map, "kind").equals("0")) {// USDT
				if (walletManagerDao.updateUsdtFreeze(map) != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				// 插入订单记录
				aiRobotContractDO.setKind(0);// 合约的币种类型
				profit = aiRobotContractDO.getMoney()
						.multiply(new BigDecimal(paramServiceImpl.findValue("usdtTovs").getParamValue()))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				profit = name(Integer.parseInt(map.get("type").toString()), profit, profit);

			} else if (StringUtil.getMapValue(map, "kind").equals("1")) {// UT
				if (walletManagerDao.updateUtFreeze(map) != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				aiRobotContractDO.setKind(1);
				profit = aiRobotContractDO.getMoney()
						.multiply(new BigDecimal(paramServiceImpl.findValue("utToVs").getParamValue()))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				profit = name(Integer.parseInt(map.get("type").toString()), profit, profit);

			} else {// VFS
				if (walletManagerDao.updateVsFreeze(map) != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("余额不够");
					return ajaxJson;
				}
				aiRobotContractDO.setKind(2);
				profit = aiRobotContractDO.getMoney();
				profit = name(Integer.parseInt(map.get("type").toString()), profit, profit);
			}
			aiRobotContractDO.setRobotProfit(profit);
			if (aiRobotContractDao.save(aiRobotContractDO) < 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("买入失败");
				return ajaxJson;
			}
			ajaxJson.setSuccess(true);
			//异步处理更新活跃有效用户
			if (ajaxJson.isSuccess()) {
				taskServiceImpl.becomeActive(Integer.parseInt(map.get("userId").toString()));
			}
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

	// 合约每天利息预估
	public BigDecimal name(Integer type, BigDecimal profit, BigDecimal money) {
		if (type == 1) {
			profit = money.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate1").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);

		} else if (type == 2) {
			profit = money.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate2").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);

		} else if (type == 3) {
			profit = money.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate3").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
		} else if (type == 4) {
			profit = money.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate4").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
		}
		return profit;

	}

	// 我的智能合约
	@Override
	public AjaxJson myContract(Integer userId) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		// 昨日收益
		try {
			BigDecimal sumMoney = aiRobotContractDao.sumMoney(userId);
			List<AiRobotContractDO> moneyGroup = aiRobotContractDao.getMoneyAll(userId);
			Map<String, Object> map = new HashMap<>();
			map.put("moneyGroup1", 0);
			map.put("moneyGroup2", 0);
			map.put("moneyGroup3", 0);
			if (moneyGroup.size() > 0) {
				for (AiRobotContractDO aiRobotContractDO : moneyGroup) {
					if (aiRobotContractDO.getKind() == 0) {
						map.put("moneyGroup1", aiRobotContractDO.getMoney());
					} else if (aiRobotContractDO.getKind() == 1) {
						map.put("moneyGroup2", aiRobotContractDO.getMoney());
					} else {
						map.put("moneyGroup3", aiRobotContractDO.getMoney());
					}
				}
			}
			map.put("sumMoney", sumMoney);
			map.put("userId", userId);
			map.put("moneyGroup", moneyGroup);
			List<AiRobotContractDO> aiRobotContractDOs = aiRobotContractDao.list(map);
			int day1 = Integer.parseInt(paramServiceImpl.findValue("regularMoney1").getParamValue());
			int day2 = Integer.parseInt(paramServiceImpl.findValue("regularMoney2").getParamValue());
			int day3 = Integer.parseInt(paramServiceImpl.findValue("regularMoney3").getParamValue());
			int day4 = Integer.parseInt(paramServiceImpl.findValue("regularMoney4").getParamValue());
			// 遍历处理还剩下多少天
			if (aiRobotContractDOs.size() > 0) {
				for (AiRobotContractDO aiRobotContractDO : aiRobotContractDOs) {
					if (aiRobotContractDO.getStatus() == 0) {
						// 多少天到期
						int t = TimeUtil.getDiffDays(aiRobotContractDO.getCreateTime(),new Date());
						logger.info("===days=={}", t);
						switch (aiRobotContractDO.getType()) {
						case 1:
							t = day1 - t;
							break;
						case 2:
							t = day2 - t;
							break;
						case 3:
							t = day3 - t;
							break;
						default:
							t = day4 - t;
							break;
						}
						if (t<=0) {
							aiRobotContractDO.setDays1(t+3);
							// 到期时间小于三天，会提示推荐购买更高合约.提示购买更到合约
							aiRobotContractDO.setDays(0);
						}else {
							aiRobotContractDO.setDays1(10);
							aiRobotContractDO.setDays(t);
						}
					}
				}
			}
			// 提示购买更高合约的收益
			map.put("aiRobotContractDOs", aiRobotContractDOs);
			map.put("regularMoney1", day1);
			map.put("regularMoney2", day2);
			map.put("regularMoney3", day3);
			map.put("regularMoney4", day4);
			map.put("regularRate2",
					new BigDecimal(Double.parseDouble(paramServiceImpl.findValue("regularRate2").getParamValue()) * 100)
							.setScale(4, BigDecimal.ROUND_HALF_UP));
			map.put("regularRate3",
					new BigDecimal(Double.parseDouble(paramServiceImpl.findValue("regularRate3").getParamValue()) * 100)
							.setScale(4, BigDecimal.ROUND_HALF_UP));
			map.put("regularRate4",
					new BigDecimal(Double.parseDouble(paramServiceImpl.findValue("regularRate4").getParamValue()) * 100)
							.setScale(4, BigDecimal.ROUND_HALF_UP));
			ajaxJson.setData(map);
			logger.debug("=============contact======{}", ajaxJson.getData());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ajaxJson;
	}

	@Override
	@Transactional
	public AjaxJson immediatelyRedemptive(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			FrontUserDO frontUserDO = frontUserDao.get(Integer.parseInt(map.get("userId").toString()));
			AjaxJson result = usdtChargeServiceImpl.selfCheckTrasferPass(map, frontUserDO, null);
			if (!result.isSuccess()) {
				return result;
			}
			ajaxJson = immediatelyRedemptivePublic(map);
			if (ajaxJson.isSuccess()) {
				return ajaxJson;
			}
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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

	// 定期赎回
	public AjaxJson immediatelyRedemptivePublic(Map<String, Object> map) {
		AjaxJson ajaxJson = new AjaxJson();
		// 修改状态
		int t = aiRobotContractDao.uodateContractStatus(map);
		if (t != 1) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("赎回失败");
			return ajaxJson;
		}
		// 钱解冻返回
		AiRobotContractDO aiRobotContractDO = aiRobotContractDao.get(Integer.parseInt(map.get("id").toString()));
		String orderNo = StringUtil.getDateTimeAndRandomForID();
		map.put("sourceId", "0");
		map.put("benefitType", 12);
		map.put("orderNo", orderNo);
		map.put("balacne", aiRobotContractDO.getMoney());// 需要赎回的钱
		if (aiRobotContractDO.getKind() == 0) {// usdt
			t = walletManagerDao.updateUsdtFreezeReduce(map);
			if (t != 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("解冻失败");
				return ajaxJson;
			}
		} else if (aiRobotContractDO.getKind() == 1) {// ut
			t = walletManagerDao.updateUtFreezeReduce(map);
			if (t != 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("解冻失败");
				return ajaxJson;
			}
		} else if (aiRobotContractDO.getKind() == 2) {// vs
			t = walletManagerDao.updateVsFreezeReduce(map);
			if (t != 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("解冻失败");
				return ajaxJson;
			}
		}
		ajaxJson.setSuccess(true);
		return ajaxJson;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AjaxJson confirmUpgrade(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		// 验证密码
		FrontUserDO frontUserDO = frontUserDao.get(Integer.parseInt(map.get("userId").toString()));
		AjaxJson ajaxJson = usdtChargeServiceImpl.selfCheckTrasferPass(map, frontUserDO, null);
		if (!ajaxJson.isSuccess()) {
			return ajaxJson;
		}
		// 升级到等级高的合约，收益会发生变化
		BigDecimal profit = BigDecimal.ZERO;
		AiRobotContractDO aiRobotContractDO = aiRobotContractDao.get(Integer.parseInt(map.get("id").toString()));
		if (aiRobotContractDO.getKind() == 0) {
			profit = aiRobotContractDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("usdtTovs").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
		} else if (aiRobotContractDO.getKind() == 1) {
			profit = aiRobotContractDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("utTovs").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
		} else {
			profit = aiRobotContractDO.getMoney();
		}
		if (aiRobotContractDO.getType() == 1) {
			aiRobotContractDO.setType(2);
			aiRobotContractDO.setOldType(1);
			profit = name(aiRobotContractDO.getType(), profit, profit);
		} else if (aiRobotContractDO.getType() == 2) {
			aiRobotContractDO.setType(3);
			aiRobotContractDO.setOldType(2);
			profit = name(aiRobotContractDO.getType(), profit, profit);
		} else if (aiRobotContractDO.getType() == 3) {
			aiRobotContractDO.setType(4);
			aiRobotContractDO.setOldType(3);
			profit = name(aiRobotContractDO.getType(), profit, profit);
		} else {
			aiRobotContractDO.setOldType(4);
			profit = name(aiRobotContractDO.getType(), profit, profit);
		}

		aiRobotContractDO.setRobotProfit(profit);
		if (aiRobotContractDao.updateMoreContract(aiRobotContractDO) != 1) {
			throw new BDException("修状态失败");
		}
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}
}
