package com.renegade.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.renegade.config.AjaxJson;
import com.renegade.config.RandomNumber;
import com.renegade.dao.ApplyRecordDao;
import com.renegade.dao.HangSellOrderDao;
import com.renegade.dao.SpotOrderDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.ApplyRecordDO;
import com.renegade.domain.HangSellOrderDO;
import com.renegade.domain.SpotOrder;
import com.renegade.service.ParamService;
import com.renegade.service.StoreManagerService;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月14日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service

public class StoreManagerServiceImpl implements StoreManagerService {
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private WalletManagerDao walletManagerDao;
	@Autowired
	private ApplyRecordDao applyRecordDao;
	@Autowired
	private SpotOrderDao spotOrderDao;
	@Autowired
	private HangSellOrderDao hangSellOrderDao;
	@Autowired
	private shareProfitServiceImpl shareProfitServiceImpl;
	// 申请加盟
	@Override
	@Transactional
	public AjaxJson participateDetail(ApplyRecordDO applyRecordDO) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			/*
			 * if (!RegexUtil.isValidIdCard(applyRecordDO.getIdcard())) {
			 * ajaxJson.setSuccess(true); ajaxJson.setMsg("身份证号码不正确 "); return ajaxJson; }
			 */
			String franchiseFee = paramServiceImpl.findValue("franchiseFee").getParamValue();// 加盟费人命币
			BigDecimal bigDecimal = new BigDecimal(franchiseFee).divide(new BigDecimal("6.8"), BigDecimal.ROUND_HALF_UP)
					.setScale(4, BigDecimal.ROUND_HALF_UP);
			Map<String, Object> map = new HashMap<>();
			String orderId = RandomNumber.generateRandomString3(27, "");
			map.put("userId", applyRecordDO.getUserId());
			map.put("balacne", bigDecimal);
			map.put("orderNo", orderId);
			map.put("benefitType", 5);
			map.put("sourceId", "0");
			int t = walletManagerDao.updateReduceUsdt(map);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setMsg("余额不够");
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			// 插入申请记录
			applyRecordDO.setApplyMoney(bigDecimal);
			t = applyRecordDao.save(applyRecordDO);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setMsg("余额不够");
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("恭喜您申请成功");
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ajaxJson.setMsg("信息不够完善");
			ajaxJson.setSuccess(true);
			return ajaxJson;
		}
	}

	// 确认订单 钱才会到账商家的钱包里，
	@Override
	@Transactional
	public AjaxJson confirmOrder(String orderNo, Integer userId) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		Map<String, Object> map = new HashMap<>();
		try {
			// 确认状态成功
			map.put("orderNo", orderNo);
			map.put("userId", userId);
			int t = spotOrderDao.confirmDeliveryUser(map);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("确认收货失败");
				return ajaxJson;
			}

			// 确认收货成功，把实际支付的数量的钱返给商家
			SpotOrder spotOrder = spotOrderDao.getOrderdetail(orderNo);
			if (spotOrder.getStoreId() == 0) {// 系统商家，那么不结算数据就偶一
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			// 该订单不是挂售单
			// 最后需要纳入计算的商家的利润拨币
			BigDecimal finalMoney = BigDecimal.ZERO;
			// 需要判断写结算收益，然后从利润里拨币，最后才是商家得到的利润收益
			if (spotOrder.getGoodsState() == 0) {// 商家需要发货的订单
				// 查询该订单是否有关联挂售单
				int hangSellOrderDO = hangSellOrderDao.getOrderId(spotOrder.getOrderNo());// 购买的挂售单数量
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
			//拨币计算
			BigDecimal result=shareProfitServiceImpl.wholesale(spotOrder.getOrderUser(), finalMoney, spotOrder.getOrderNo());
			finalMoney=finalMoney.subtract(result).setScale(4, BigDecimal.ROUND_HALF_UP);
//			//该笔确认订单非系统商家获取的利润
			map.put("balacne", finalMoney);
			map.put("benefitType", 8);// 店铺收益
			map.put("sourceId", spotOrder.getOrderUser());
			map.put("orderNo", spotOrder.getOrderNo());
			map.put("userId", spotOrder.getStoreId());
			t = walletManagerDao.updateAddVs(map);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("该用户不存在");
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

}
