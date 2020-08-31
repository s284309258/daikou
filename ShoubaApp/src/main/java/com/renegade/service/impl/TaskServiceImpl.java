package com.renegade.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.renegade.config.ApplicationContextRegister;
import com.renegade.config.StringUtil;
import com.renegade.dao.AiRobotContractDao;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.HangSellOrderDao;
import com.renegade.dao.ResonancePoolRecordDao;
import com.renegade.dao.SpotOrderDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.AiRobotContractDO;
import com.renegade.domain.HangSellOrderDO;
import com.renegade.domain.ResonancePoolDO;
import com.renegade.domain.ResonancePoolRecordDO;
import com.renegade.domain.SpotOrder;
import com.renegade.redis.RedisUtil;
import com.renegade.service.ParamService;
import com.renegade.service.TaskService;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月19日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service
public class TaskServiceImpl implements TaskService {
	@Autowired
	private AiRobotContractDao aiRobotContractDao;
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private WalletManagerDao walletManagerDao;
	@Autowired
	private SpotOrderDao spotOrderDao;
	@Autowired
	private HangSellOrderDao hangSellOrderDao;
	@Autowired
	private ResonancePoolRecordDao resonancePoolRecordDao;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private shareProfitServiceImpl shareProfitServiceImpl;
	@Lazy // 延迟加载循环依赖
	@Autowired
	private TaskService taskServiceImpl;
	@Autowired
	private FrontUserDao frontUserDao;
	private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

	/**
	 * 定时获取未到期的定期利息
	 */
	@Override
	public void regularBenefit() {
		// TODO Auto-generated method stub
		Map<String, Object> map = getCache();
//		Map<String, Object> map = new HashMap<>();
//		map.put("days1", 1);
//		map.put("days2", 1);
//		map.put("days3", 1);
//		map.put("days4", 1);
		List<AiRobotContractDO> aiRobotContractDOs = aiRobotContractDao.listTaskDetailNo(map);
		if (aiRobotContractDOs.isEmpty()) {
			return;
		}
		for (AiRobotContractDO aiRobotContractDO : aiRobotContractDOs) {
			try {
				((TaskServiceImpl) AopContext.currentProxy()).name(aiRobotContractDO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Transactional(propagation = Propagation.NESTED, readOnly = false, rollbackFor = Exception.class)
	public void name(AiRobotContractDO aiRobotContractDO) throws Exception {
		// 修改每日收益
		BigDecimal profit = BigDecimal.ZERO;
		if (aiRobotContractDO.getType() == 1) { // 根据合约类型得到未转换usdt的收益
			profit = aiRobotContractDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate1").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
		} else if (aiRobotContractDO.getType() == 2) {
			profit = aiRobotContractDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate2").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);

		} else if (aiRobotContractDO.getType() == 3) {
			profit = aiRobotContractDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate3").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);

		} else if (aiRobotContractDO.getType() == 4) {
			profit = aiRobotContractDO.getMoney()
					.multiply(new BigDecimal(paramServiceImpl.findValue("regularRate4").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);

		}
		// 根据币种类型，转换成最终收益
		Map<String, Object> map = new HashMap<>();
		String orderNo = StringUtil.getDateTimeAndRandomForID();
		map.put("userId", aiRobotContractDO.getUserId());
		map.put("id", aiRobotContractDO.getId());
		map.put("orderNo", orderNo);
		map.put("sourceId", "0");// 来源订单
		if (aiRobotContractDO.getKind() == 0) {// usdt
			profit = profit.multiply(new BigDecimal(paramServiceImpl.findValue("usdtTovs").getParamValue())).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			map.put("balacne", profit);
			map.put("benefitType", 160);// 定期收益
			if (walletManagerDao.updateAddVsDingqi(map) != 1) {
				throw new Exception("利息添加失败");
			}
		} else if (aiRobotContractDO.getKind() == 1) {// ut
			profit = profit.multiply(new BigDecimal(paramServiceImpl.findValue("utToVs").getParamValue())).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			map.put("balacne", profit);
			map.put("benefitType", 161);// 定期收益
			if (walletManagerDao.updateAddVsDingqi2(map) != 1) {
				throw new Exception("利息添加失败");
			}
		} else {// vs
			map.put("balacne", profit);
			map.put("benefitType", 162);// 定期收益
			if (walletManagerDao.updateAddVsDingqi3(map) != 1) {
				throw new Exception("利息添加失败");
			}
		}
		map.put("profit", profit);
		if (aiRobotContractDao.updateProfit(map) != 1) {
			throw new Exception("利息记录失败");
		}
	}

	/**
	 * 定期到期后三天未手动赎回的自动复购相应的合约(无用)
	 */
	@Override
	public void regularOrderCancel() {
		// TODO Auto-generated method stub
		Map<String, Object> map = getCache();
		List<AiRobotContractDO> aiRobotContractDOs = aiRobotContractDao.listTaskDetailSuper(map);
		for (AiRobotContractDO aiRobotContractDO : aiRobotContractDOs) {
			try {
				// ((TaskServiceImpl) AopContext.currentProxy()).name2(aiRobotContractDO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 对应的合约参数
	public Map<String, Object> getCache() {
		Map<String, Object> map = new HashMap<>();
		map.put("days1", paramServiceImpl.findValue("regularMoney1").getParamValue());
		map.put("days2", paramServiceImpl.findValue("regularMoney2").getParamValue());
		map.put("days3", paramServiceImpl.findValue("regularMoney3").getParamValue());
		map.put("days4", paramServiceImpl.findValue("regularMoney4").getParamValue());
		return map;
	}

	// ss版本
	@Override
	public void regularOrderCancelSSS() {
		// TODO Auto-generated method stub
		Map<String, Object> map = getCache();
//		Map<String, Object> map = new HashMap<>();
//		map.put("days1", 1);
//		map.put("days2", 1);
//		map.put("days3", 1);
//		map.put("days4", 1);
		aiRobotContractDao.updateAgin(map);
	}

	// 超过七天未确认收货的，自动确认收货，并且返钱给商家
	@Override
	public void autoConfirmOrder() {
		// TODO Auto-generated method stub
		List<SpotOrder> spotOrders = spotOrderDao.listOrderDetail();
		if (!spotOrders.isEmpty()) {
			for (SpotOrder spotOrder : spotOrders) {
				try {
					((TaskServiceImpl) AopContext.currentProxy()).name2(spotOrder);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Transactional(propagation = Propagation.NESTED, readOnly = false, rollbackFor = Exception.class)
	public void name2(SpotOrder spotOrder) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 确认状态成功
		map.put("orderNo", spotOrder.getDeliveryOrderId());
		map.put("userId", spotOrder.getOrderUser());
		int t = spotOrderDao.confirmDelivery(map);
		if (t != 1) {
			throw new Exception("确认收货失败");
		}
		if (spotOrder.getStoreId() == 0) {// 系统商家，那么不结算数据就偶一
			return;
		}
		// 确认收货成功，把实际支付的数量的钱返给商家
		// 该订单不是挂售单
		// 最后需要纳入计算的商家的利润拨币
		BigDecimal finalMoney = BigDecimal.ZERO;
		// 需要判断写结算收益，然后从利润里拨币，最后才是商家得到的利润收益
		if (spotOrder.getGoodsState() == 0) {// 商家需要发货的订单
			// 查询该订单是否有关联挂售单
			int hangSellOrderDO = hangSellOrderDao.getOrderId(spotOrder.getOrderNo());
			if (hangSellOrderDO == 0) {// 直接结算收益
				// 非系统的商家需要返钱 逻辑待处理
				finalMoney = spotOrder.getOrderSjPay();
			} else {
				finalMoney = (spotOrder.getGoodsPrice())
						.multiply(new BigDecimal(spotOrder.getGoodsNum() - hangSellOrderDO))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				System.out.println("单单从该笔订单实际上商家得到的利润==" + finalMoney);
			}
		} else {// 可挂售的订单选择提货的订单
			finalMoney = spotOrder.getOrderSjPay();
		}
//		//该笔确认订单非系统商家获取的利润，需要进行一个拨币计算
		BigDecimal result = shareProfitServiceImpl.wholesale(spotOrder.getOrderUser(), finalMoney,
				spotOrder.getOrderNo());
		finalMoney = finalMoney.subtract(result).setScale(4, BigDecimal.ROUND_HALF_UP);
		map.put("balacne", finalMoney);
		map.put("benefitType", 8);// 店铺收益
		map.put("sourceId", spotOrder.getOrderUser());
		map.put("orderNo", spotOrder.getOrderNo());
		map.put("userId", spotOrder.getStoreId());
		t = walletManagerDao.updateAddVs(map);
		if (t != 1) {
			throw new Exception("该用户不存在");
		}
	}

	@Override
	@Async("asyncExecutor")
	public Future<Integer> benefitLevelOne(ResonancePoolDO esonancePoolDO, String ordNo) {
		// TODO Auto-generated method stub
		// 查询是否存在信仰者
	/*	int num = resonancePoolRecordDao.getUserLevelNum(0, esonancePoolDO.getId());
		if (num == 0) {
			return new AsyncResult<Integer>(1);
		}*/
		List<ResonancePoolRecordDO> recordDOs = resonancePoolRecordDao.getUserLevel(1, esonancePoolDO.getId());
		if (recordDOs.isEmpty()) {
			return new AsyncResult<Integer>(1);
		}
		// 钱平分
		BigDecimal balance = esonancePoolDO.getResonanceTotal()
				.multiply(new BigDecimal(paramServiceImpl.findValue("level1").getParamValue()))
				.divide(new BigDecimal(recordDOs.size()), 4, BigDecimal.ROUND_HALF_UP).setScale(4, BigDecimal.ROUND_HALF_UP);
		System.out.println("=====剩余量======="+esonancePoolDO.getResonanceTotal()+"==参数=="+paramServiceImpl.findValue("level1").getParamValue()+"===除数=="+recordDOs.size()+"========="+balance);
		Map<String, Object> map = new HashMap<>();
		map.put("balacne", balance);
		map.put("sourceId", "0");
		map.put("orderNo", ordNo);
		map.put("benefitType", 20);// 信仰者收益
		for (ResonancePoolRecordDO resonancePoolRecordDO : recordDOs) {
			ResonancePoolRecordDO recordDO = new ResonancePoolRecordDO();
			recordDO.setUserId(resonancePoolRecordDO.getUserId());
			recordDO.setId(resonancePoolRecordDO.getId());
			recordDO.setLevel1(1);
			resonancePoolRecordDao.updateStatus(recordDO);
			// 返钱
			map.put("userId", resonancePoolRecordDO.getUserId());// 信仰者收益
			walletManagerDao.updateAddVs(map);
			String key = "T_SYS_RANK" + resonancePoolRecordDO.getUserId();
			redisUtil.delete(key);
			key = "T_SYS_HISTORY" + resonancePoolRecordDO.getUserId();
			redisUtil.delete(key);
		}
		return new AsyncResult<Integer>(1);
	}

	@Override
	@Async("asyncExecutor")
	public Future<Integer> benefitLevelTwo(ResonancePoolDO esonancePoolDO, String ordNo) {
		// TODO Auto-generated method stub
	/*	int num = resonancePoolRecordDao.getUserLevelNum(1, esonancePoolDO.getId());
		if (num == 0) {
			return new AsyncResult<Integer>(2);
		}*/
		List<ResonancePoolRecordDO> recordDOs = resonancePoolRecordDao.getUserLevel(0, esonancePoolDO.getId());
		if (recordDOs.isEmpty()) {
			return new AsyncResult<Integer>(2);
		}
		// 钱平分
		BigDecimal balance = esonancePoolDO.getResonanceTotal()
				.multiply(new BigDecimal(paramServiceImpl.findValue("level2").getParamValue()))
				.divide(new BigDecimal(recordDOs.size()), 4, BigDecimal.ROUND_HALF_UP).setScale(4, BigDecimal.ROUND_HALF_UP);
		System.out.println("=====剩余量======="+esonancePoolDO.getResonanceTotal()+"==参数=="+paramServiceImpl.findValue("level2").getParamValue()+"===除数=="+recordDOs.size()+"========="+balance);
		Map<String, Object> map = new HashMap<>();
		map.put("balacne", balance);
		map.put("orderNo", ordNo);
		map.put("sourceId", "0");
		map.put("benefitType", 21);// 幸运者收益
		for (ResonancePoolRecordDO resonancePoolRecordDO : recordDOs) {
			ResonancePoolRecordDO recordDO = new ResonancePoolRecordDO();
			recordDO.setUserId(resonancePoolRecordDO.getUserId());
			recordDO.setId(resonancePoolRecordDO.getId());
			recordDO.setLevel2(1);
			resonancePoolRecordDao.updateStatus(recordDO);
			// 返钱
			map.put("userId", resonancePoolRecordDO.getUserId());// 信仰者收益
			walletManagerDao.updateAddVs(map);
			String key = "T_SYS_RANK" + resonancePoolRecordDO.getUserId();
			redisUtil.delete(key);
			key = "T_SYS_HISTORY" + resonancePoolRecordDO.getUserId();
			redisUtil.delete(key);
		}
		return new AsyncResult<Integer>(2);
	}

	@Override
	@Async("asyncExecutor")
	public Future<Integer> benefitLevelThress(ResonancePoolDO esonancePoolDO, String ordNo) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("resonanceId", esonancePoolDO.getId());
		map.put("offset", 0);
		map.put("limit", 10);// 前10名
		// map.put("status", "0");
		List<Map<String, Object>> resonancePoolRecordDO = resonancePoolRecordDao.listRankingResonance(map);
		int t = 0;
		if (resonancePoolRecordDO.size() > 0) {
			// 钱平分
			BigDecimal balance1 = esonancePoolDO.getResonanceTotal()
					.multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue()))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
			map.put("sourceId", "0");
			map.put("orderNo", ordNo);
			map.put("benefitType", 22);// 裂变者者收益
			for (Map<String, Object> map2 : resonancePoolRecordDO) {
				t++;
				ResonancePoolRecordDO recordDO = new ResonancePoolRecordDO();
				recordDO.setUserId(Integer.parseInt(map2.get("user_id").toString()));
				recordDO.setId(Integer.parseInt(map2.get("id").toString()));
				BigDecimal balance=BigDecimal.ZERO;
				if (t == 1) {// 第一名
					balance = balance1
							.multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit1").getParamValue()))
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					recordDO.setLevel3(t);
				} else if (t == 2) {
					balance = balance1
							.multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit2").getParamValue()))
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					recordDO.setLevel3(t);
				} else if (t == 3) {
					balance = balance1
							.multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit3").getParamValue()))
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					recordDO.setLevel3(t);
				} else if (t == 4) {
					balance = balance1
							.multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit4").getParamValue()))
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					recordDO.setLevel3(t);
				} else {
					balance = balance1
							.multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit5").getParamValue()))
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					recordDO.setLevel3(t);
				}
				resonancePoolRecordDao.updateStatus(recordDO);
				map.put("balacne", balance);
				// 返钱
				map.put("userId", map2.get("user_id").toString());// 信仰者收益
				walletManagerDao.updateAddVs(map);
				String key = "T_SYS_RANK" + Integer.parseInt(map2.get("user_id").toString());
				redisUtil.delete(key);
				key = "T_SYS_HISTORY" + Integer.parseInt(map2.get("user_id").toString());
				redisUtil.delete(key);
			}
		}
		return new AsyncResult<Integer>(3);
	}

	@Override
	@Async("asyncExecutor")
	public void asynsRefund(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<HangSellOrderDO> hangSellOrderDOs = hangSellOrderDao.list(map);
		if (!hangSellOrderDOs.isEmpty()) {
			for (HangSellOrderDO hangSellOrderDO : hangSellOrderDOs) {
				try {
					// ApplicationContextRegister.getBean(TaskServiceImpl.class).nameasynsRefund(hangSellOrderDO,
					taskServiceImpl.nameasynsRefund(hangSellOrderDO, map.get("orderNo").toString(),
							Integer.parseInt(map.get("userId").toString()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
			
	}

	// 返款逻辑
	@Override
	@Transactional(propagation = Propagation.NESTED, readOnly = false, rollbackFor = Exception.class) // 依赖父级的事务
	public void nameasynsRefund(HangSellOrderDO hangSellOrderDO, String orderNo, Integer userId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("benefitType", 7);
		map.put("buyOrderId", orderNo);
		map.put("id", hangSellOrderDO.getId());
		map.put("userId", userId);// 收益来源那个用户，那么奖金就要拨到哪个要用户
//		给买这单的用户的上级分钱委托的钱，往上找三级.期待着制度逻辑植入。vfsBalance 等待奖金结算后返款
		BigDecimal balacne = shareProfitServiceImpl.retailCommission(userId, hangSellOrderDO.getRefoudPrice(),
				hangSellOrderDO.getHangSellId());
		logger.debug("===========balacne111=={}", balacne);
		logger.debug("===========balacne11123123123123=={}",  hangSellOrderDO.getRefoudPrice());
		balacne = hangSellOrderDO.getRefoudPrice().subtract(balacne).setScale(4, BigDecimal.ROUND_HALF_UP);
		logger.debug("===========balacne=={}", balacne);
		map.put("vfsBalance", balacne);
		int t = hangSellOrderDao.updateStatusRfoudCode(map);
		if (t != 2) {
			throw new Exception("秒返款失败");
		}

	}

	@Override
	@Async("asyncExecutor")
	public void insertWanlletManger(Integer userId) {
		// TODO Auto-generated method stub
		walletManagerDao.save(userId);
	}

	// 购买智能合约和搬砖的时候判定是否够500usdt
	@Override
	@Async("asyncExecutor")
	public void becomeActive(Integer userId) {
		// TODO Auto-generated method stub
		frontUserDao.updateActiveUser(userId, new BigDecimal(paramServiceImpl.findValue("aciveNum").getParamValue()));
	}

}
