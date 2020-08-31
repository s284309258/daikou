package com.renegade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.renegade.config.AjaxJson;
import com.renegade.config.MD5;
import com.renegade.config.PropertyUtil;
import com.renegade.config.RegexUtil;
import com.renegade.dao.ComplainOrderDao;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.HangSellOrderDao;
import com.renegade.dao.SpitkeTicketDao;
import com.renegade.dao.SpotGoodsDao;
import com.renegade.dao.SpotOrderDao;
import com.renegade.dao.UserAddressDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.ComplainOrderDO;
import com.renegade.domain.FrontUserDO;
import com.renegade.domain.HangSellOrderDO;
import com.renegade.domain.SpitkeTicketDO;
import com.renegade.domain.SpotGoodsDO;
import com.renegade.domain.SpotOrder;
import com.renegade.domain.UserAddressDO;
import com.renegade.filter.XssHttpServletRequestWrapper;
import com.renegade.service.ParamService;
import com.renegade.service.SpotOrderSerivce;
import com.renegade.service.TaskService;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月24日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Service
public class SpotOrderSerivceImpl implements SpotOrderSerivce {
	@Autowired
	private UserAddressDao userAddressDao;
	@Autowired
	private SpotOrderDao spotOrderDao;
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private SpotGoodsDao spotGoodsDao;
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private WalletManagerDao walletManagerDao;
	@Autowired
	private HangSellOrderDao hangSellOrderDao;
	@Autowired
	private SpitkeTicketDao spitkeTicketDao;
	@Autowired
	private TaskService taskServiceImpl;
	@Autowired
	private ComplainOrderDao complainOrderDao;
	@Autowired
	private shareProfitServiceImpl shareProfitServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(SpotOrderSerivceImpl.class);

	@Override
	@Transactional
	public AjaxJson registDeliveryScoreGoods(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtils.isEmpty((CharSequence) map.get("address"))) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("地址有误");// 地址有误
				return ajaxJson;
			}
			// 地址拼接
			UserAddressDO zkUserAddress = userAddressDao.findUserDefutlAddress(
					Integer.parseInt(map.get("address").toString()), Integer.parseInt(map.get("userId").toString()));
			if (zkUserAddress == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("地址有误");// 地址有误
				return ajaxJson;
			}
			if (StringUtils.isEmpty(map.get("passWord").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码错误");// 请输入交易密码
				return ajaxJson;
			}
			// 购买数量
			if (!RegexUtil.isValidNum(map.get("num").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("数量有误");// 数量有误
				return ajaxJson;
			}
			int num = Integer.parseInt(map.get("num").toString());
			// 库存判定
			// 判断密码是否设置以及是否为空
			FrontUserDO zkUser = frontUserDao.get(Integer.parseInt(map.get("userId").toString()));
			if (StringUtils.isEmpty(zkUser.getPayPassword())) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请设置交易密码");// 请设置交易密码
				return ajaxJson;
			}
			String md5Pass = MD5.strToMd5(map.get("passWord").toString());
			if (!md5Pass.equals(zkUser.getPayPassword())) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setMsg("密码错误");// 密码错误
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		
			// 减少库存
			SpotGoodsDO zkDeliveryGoods = spotGoodsDao.get(Integer.parseInt(map.get("goodsId").toString()));
			// 判断该店铺是否被封
			FrontUserDO frontUserDO = frontUserDao.get(zkDeliveryGoods.getGoodsUserId());
			if (frontUserDO!=null&&frontUserDO.getUserLimitss() == 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setMsg("该店铺已经被封");// 密码错误
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			zkDeliveryGoods.setGoodsStock(num);
			int t = spotGoodsDao.updateSaleStockNum(zkDeliveryGoods);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setMsg("库存不够");// 密码错误
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			// 总价
			BigDecimal allBigdecimal = zkDeliveryGoods.getGoodsPrice().multiply(new BigDecimal(num)).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			SpotOrder zkDeliverOrder = new SpotOrder();
			// 扣钱付款
			// 是否选择抵扣券 type=1选择。type=0不选择
			if (map.get("type").toString().equals("1") && zkUser.getUserTicket().compareTo(new BigDecimal(num)) >= 0) {
				// 判断秒杀商品库存是否足够
				zkDeliveryGoods.setGoodsNameEn(num);
				t = spotGoodsDao.updateSaleSpikeNum(zkDeliveryGoods);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("对不起，秒杀失败！");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				// 判断秒杀券是否抵扣当前购买的数量
				map.put("num", num);
				t = spitkeTicketDao.updateExpenseTickets(map);
				if (t != num) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("抵扣券不够");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				// 使用抵扣券，就不会再得到抵扣券
				t = frontUserDao.updateReduceTickets(zkUser.getUserId(), num);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("抵扣券有误");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				// 扣钱
				BigDecimal balacne = (zkDeliveryGoods.getGoodsPrice()
						.subtract(new BigDecimal(paramServiceImpl.findValue("voucher").getParamValue())))
								.multiply(new BigDecimal(num)).setScale(4, BigDecimal.ROUND_HALF_UP);
				logger.debug("抵扣券抵扣之后的余额{},抵扣的数量{}", balacne, num);
				map.put("benefitType", 6);
				map.put("sourceId", "0");
				map.put("balacne", balacne);
				t = walletManagerDao.updateReduceVs(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("余额不足");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				zkDeliverOrder.setOrderSjPay(balacne);// 实际支付的钱
				zkDeliverOrder.setOrderStatus("XD");// 需要挂售的订单
				zkDeliverOrder.setGoodsState(1);// 商家代理挂售的订单
			} else {// 不使用抵扣券，会获得抵扣券
				map.put("benefitType", 6);
				map.put("sourceId", "0");
				map.put("balacne", allBigdecimal);
				t = walletManagerDao.updateReduceVs(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("余额不足");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
//				获得抵扣券
				t = frontUserDao.updateAddTickets(zkUser.getUserId(), num*2);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("赠送抵扣券失败");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				// 循环添加抵扣券
				SpitkeTicketDO spitkeTicketDO = new SpitkeTicketDO();
				spitkeTicketDO.setUserId(Integer.parseInt(map.get("userId").toString()));
				spitkeTicketDO.setGoodsId(zkDeliveryGoods.getGoodsId());
				spitkeTicketDO.setSpikeTicket(paramServiceImpl.findValue("voucher").getParamValue());
				for (int i = 0; i < num*2; i++) {
					spitkeTicketDao.save(spitkeTicketDO);
				}
				zkDeliverOrder.setOrderSjPay(allBigdecimal);// 实际支付的钱
				zkDeliverOrder.setOrderStatus("JD");// 需要发货的订单
				zkDeliverOrder.setGoodsState(0);// 商家发货的订单
			}
			// 判定是否存在该商品有挂售中的商品
			// 付款成功插入订单
			zkDeliverOrder.setStoreId(zkDeliveryGoods.getGoodsUserId());// 店铺ID
			zkDeliverOrder.setOrderNo(map.get("orderNo").toString());
			zkDeliverOrder.setGoodsId(zkDeliveryGoods.getGoodsId());
			zkDeliverOrder.setGoodsPrice(zkDeliveryGoods.getGoodsPrice());
			zkDeliverOrder.setGoodsNum(num);
			zkDeliverOrder.setOrderUser(Integer.parseInt(map.get("userId").toString()));
			zkDeliverOrder.setPayTime(new Date());
			zkDeliverOrder.setOrderYuPay(allBigdecimal);
			String receivingAddress = zkUserAddress.getAddressProvince() + zkUserAddress.getAddressCity()
					+ zkUserAddress.getAddressDetailed();
			zkDeliverOrder.setReceivingAddress(receivingAddress);
			zkDeliverOrder.setReceivedPhone(zkUserAddress.getAddressPhone());
			zkDeliverOrder.setReceivingName(zkUserAddress.getAddressUserName());
			t = spotOrderDao.saveDeliverOrder(zkDeliverOrder);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("系统异常");
				return ajaxJson;
			}
			// 变为非信仰者
			frontUserDao.updateLimitssssUser(Integer.parseInt(map.get("userId").toString()));
			ajaxJson.setSuccess(true);
			if (ajaxJson.isSuccess()) {// 卖买完成以后，异步处理排序挂售订单的用户的利润
				map.put("hangSelllGoodsId", zkDeliveryGoods.getGoodsId());
//				map.put("buyOrderId", zkDeliveryGoods.getGoodsUserId());
//				map.put("sourceId",map.get("userId").toString());
//				map.put("benefitType", 7);// 委托挂售收益
				map.put("allBigdecimal", allBigdecimal);
				map.put("sotoreId", zkDeliveryGoods.getGoodsUserId());
				map.put("limit", num);
				map.put("offset", 0);
				map.put("hangSellStatus", "0");
				map.put("sort", "create_time");// 时间排序
				map.put("order", "ASC");// 升序
				taskServiceImpl.asynsRefund(map);

			}
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

	// 异步处理优化版
	@Async
	public void asynsRefundS(Map<String, Object> map) throws Exception {
		int t = hangSellOrderDao.updateStatusRfoud(map);
		if (t == 1) {
			logger.debug("==============返款成功" + map);
		} else {
			logger.debug("==============返款失败" + map);
		}
	}

	// 异步处理(粗糙版)
	@Async
	public void asynsRefund(Map<String, Object> map) {
		List<HangSellOrderDO> hangSellOrderDOs = hangSellOrderDao.list(map);
		for (HangSellOrderDO hangSellOrderDO : hangSellOrderDOs) {
			try {
				((SpotOrderSerivceImpl) AopContext.currentProxy()).name(hangSellOrderDO, map.get("orderNo").toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 返款逻辑
	@Transactional(propagation = Propagation.NESTED, readOnly = false, rollbackFor = Exception.class)
	public void name(HangSellOrderDO hangSellOrderDO, String orderNo) throws Exception {
		// 通过订单查询单价
//		SpotOrder spotOrder = spotOrderDao.getOrderdetail(hangSellOrderDO.getHangSellId());
//		// 返款
//		int t = hangSellOrderDao.updateStatus(hangSellOrderDO.getId());
//		if (t != 1) {
//			throw new Exception("返款状态失败");
//		}
		Map<String, Object> map = new HashMap<>();
		map.put("benefitType", 7);
		map.put("orderNo", orderNo);
		map.put("id", hangSellOrderDO.getId());
		int t = hangSellOrderDao.updateStatusRfoudCode(map);
		if (t != 1) {
			throw new Exception("返款失败");
		}
//		给买这单的用户的上级分钱委托的钱，往上找三级.期待着制度逻辑植入。

//		map.put("sourceId", spotOrder.getOrderUser());
//		map.put("balacne", spotOrder.getGoodsPrice());
//		map.put("userId", hangSellOrderDO.getHangSellUser());

//		logger.debug("==============返款成功" + spotOrder.getGoodsPrice());
	}

	// 添加店铺商品
	@Override
	public AjaxJson addSocreGoods(SpotGoodsDO spotGoodsDO) {
		AjaxJson ajaxJson = new AjaxJson();
		// TODO Auto-generated method stub
		// 判断是否够有开店资格
		FrontUserDO frontUserDO = frontUserDao.get(spotGoodsDO.getGoodsUserId());
		if (frontUserDO.getUserLimit() == 0 || frontUserDO.getUserLimitss() == 1) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("对不起，您的店铺还未开通或者该店铺存在违规操作！");
			return ajaxJson;
		}
		// xss过滤
//		判断有没挂售中的商品
		Map<String, Object> map = new HashMap<>();
		map.put("goodsState", "1");
		map.put("goodsUserId", spotGoodsDO.getGoodsUserId());
		List<SpotGoodsDO> spotGoodsDOs = spotGoodsDao.list(map);
		if (!spotGoodsDOs.isEmpty()) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("对不起，您的店铺已经存在审核通过可挂售的商品！");
			return ajaxJson;
		}
		String goodsName = XssHttpServletRequestWrapper.replaceXSS(spotGoodsDO.getGoodsName());
		spotGoodsDO.setGoodsPictureDesc(XssHttpServletRequestWrapper.replaceXSS(spotGoodsDO.getGoodsPictureDesc()));
		spotGoodsDO.setGoodsPicture(XssHttpServletRequestWrapper.replaceXSS(spotGoodsDO.getGoodsPicture()));
		spotGoodsDO.setGoodsName(goodsName);
		spotGoodsDO.setCreateTime(new Date());
		if (spotGoodsDao.save(spotGoodsDO) > 0) {
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("添加商品成功");
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		ajaxJson.setMsg("系统异常");
		return ajaxJson;
	}

	/**
	 * 确认 委托管理
	 */
	@Override
	@Transactional
	public AjaxJson consignmentOrder(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			int t = spotOrderDao.updateGsStatus(map);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("委托出售状态修改失败");
				return ajaxJson;
			}
			// 插入挂售记录表
			SpotOrder wSpotOrder = spotOrderDao.getOrderdetail(map.get("orderNo").toString());
			FrontUserDO frontUserDO = frontUserDao.get(wSpotOrder.getStoreId());
			if (frontUserDO!=null) {
				// 判断该店铺是否被封
				if (frontUserDO.getUserLimitss() == 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("该店铺已经被封,不可以进行委托出售");// 密码错误
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			int diliveryNum = wSpotOrder.getGoodsNum();
			if (diliveryNum <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("委托出售记录失败");
				return ajaxJson;
			}
			for (int i = 0; i < diliveryNum; i++) {
				HangSellOrderDO hangSellOrderDO = new HangSellOrderDO();
				hangSellOrderDO.setHangSellUser(wSpotOrder.getOrderUser());// 挂售人
				hangSellOrderDO.setHangSellId(wSpotOrder.getOrderNo());
				hangSellOrderDO.setHangSelllGoodsId(wSpotOrder.getGoodsId());
				hangSellOrderDO.setCreateTime(new Date());
				hangSellOrderDO.setRefoudPrice(wSpotOrder.getGoodsPrice());// 一单为一价格
				t = hangSellOrderDao.save(hangSellOrderDO);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("委托出售记录失败");
					return ajaxJson;
				}
			}
			if (wSpotOrder.getStoreId() != 0) {
				// 委托出售的时候，返钱给商家非官方的
//				涉及到拨币计算
				BigDecimal balacne = shareProfitServiceImpl.wholesale(wSpotOrder.getOrderUser(),
						wSpotOrder.getOrderSjPay(), wSpotOrder.getOrderNo());
				balacne = wSpotOrder.getOrderSjPay().subtract(balacne).setScale(4, BigDecimal.ROUND_HALF_UP);
				map.put("balacne", balacne);
				map.put("benefitType", 8);// 店铺收益
				map.put("sourceId", wSpotOrder.getOrderUser());
				map.put("userId", wSpotOrder.getStoreId());
				map.put("orderNo", wSpotOrder.getOrderNo());
				t = walletManagerDao.updateAddVs(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("委托返款失败");
				}
			}
			// 增加库存
			SpotGoodsDO spotGoods = new SpotGoodsDO();
			spotGoods.setGoodsStock(diliveryNum);
			spotGoods.setGoodsId(wSpotOrder.getGoodsId());
			t = spotGoodsDao.updateStockNum(spotGoods);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("委托出售数量失败");
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

	@Override
	public SpotOrder getOrderNo(String orderId) {
		// TODO Auto-generated method stub
		return spotOrderDao.getOrderNo(orderId);
	}

	/**
	 * 委托提货
	 */
	@Override
	public AjaxJson confirmDeliveryActiopn(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			SpotOrder spotOrder = new SpotOrder();
			spotOrder.setOrderStatus("JD");
			spotOrder.setOrderNo(map.get("orderNo").toString());
			if (spotOrderDao.updateDeliveryOrder(spotOrder) > 0) {
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("提货失败");
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
			return ajaxJson;
		}

	}

	/**
	 * 申请投诉
	 */
	@Override
	@Transactional
	public AjaxJson applyComplainOrder(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			// 修改订单状态未发货或者发货的进行的投诉
			int t = spotOrderDao.updateComplainStatus(map);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("订单投诉失败");
				return ajaxJson;
			}
			// 插入投诉信息
			ComplainOrderDO complainOrderDO = new ComplainOrderDO();
			complainOrderDO.setComplainOrder(map.get("orderNo").toString());
			// xss
			String content = XssHttpServletRequestWrapper.replaceXSS((map.get("content").toString()));
			complainOrderDO.setComplainContent(content);
			complainOrderDO.setUserId(Integer.parseInt(map.get("userId").toString()));
			complainOrderDO.setCreateTime(new Date());
			complainOrderDO.setComplainUser(spotOrderDao.getOrderNo(map.get("orderNo").toString()).getStoreId());
			t = complainOrderDao.save(complainOrderDO);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("申请投诉失败");
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

	@Override
	public int findWaitOrderById(Integer userId) {
		// TODO Auto-generated method stub
		return spotOrderDao.findWaitOrderById(userId);
	}

}
