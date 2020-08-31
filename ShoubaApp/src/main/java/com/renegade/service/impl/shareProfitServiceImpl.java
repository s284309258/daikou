package com.renegade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.renegade.dao.FrontUserDao;
import com.renegade.dao.ParamDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.FrontUserDO;
import com.renegade.service.FrontFlowingRecordFreetrbService;
import com.renegade.service.FrontFlowingRecordFreewdbService;
import com.renegade.service.FrontFlowingRecordService;
import com.renegade.service.FrontUserService;
import com.renegade.service.shareProfitService;

@Service
public class shareProfitServiceImpl implements shareProfitService {
	// 定义日志文件操作类对象
	private static Logger logger = (Logger) LoggerFactory.getLogger(shareProfitServiceImpl.class);
	@Autowired
	private FrontUserService memberUserService;
	@Autowired
	private FrontFlowingRecordService frontFlowingRecordService;
	@Autowired
	private FrontFlowingRecordFreetrbService frontFlowingRecordFreetrbService;
	@Autowired
	private FrontFlowingRecordFreewdbService frontFlowingRecordFreewdbService;
	@Autowired
	private ParamDao paramServiceImpl;
	@Autowired
	private WalletManagerDao walletManagerService;
	@Autowired
	private FrontUserDao memberUserDao;


	// 商城制度
	// 零售委托找3代
	public BigDecimal retailCommission(int userId, BigDecimal profit,String orderNo) {
		logger.debug("開始執行商城制度");
		// 查询当前代
		FrontUserDO frontUser = memberUserService.get(userId);
		// 统计总共分出多少的钱
		BigDecimal totalRetail = BigDecimal.ZERO;
		;
		int len = 1;// 循环的基数，继续++后继续循环
		int puserId = frontUser.getUserPid();
		if (puserId > 0) {
			for (int i = 0; i < len; i++) {
				// 循环父级查询用户
				FrontUserDO parentUser = memberUserService.get(puserId);
				// 根据直推人数计算分配等级奖励
				if(parentUser!=null) {
					if (frontUser.getAlgebra() - parentUser.getAlgebra() == 1) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("這裡是委託開始執行收益扣款一代");
						if (parentUser.getIsActive() == 1) {
							logger.debug("這裡是委託開始執行收益活躍用戶開始扣款一代");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("retailReward1").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 99);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("這裡是委託開始執行收益扣款一代修改語句結束");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("這裡是委託開始執行收益扣款一代修改語句結束返款金額為"+totalRetail);
						}
					}  if (frontUser.getAlgebra() - parentUser.getAlgebra() == 2) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("這裡是委託開始執行收益扣款二代修改語句結束");
						if (parentUser.getIsActive() == 1) {
							logger.debug("這裡是委託開始執行收益活躍用戶開始扣款二代");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("retailReward2").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 99);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("這裡是委託開始執行收益扣款二代修改語句結束");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("這裡是委託開始執行收益扣款二代修改語句結束返款金額為"+totalRetail);
						}
						
					}  if (frontUser.getAlgebra() - parentUser.getAlgebra()==3) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("這裡是委託開始執行收益扣款三代修改語句開始");
						if (parentUser.getIsActive() == 1) {
							logger.debug("這裡是委託開始執行收益扣款三代活躍用戶修改語句開始");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("retailReward3").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 99);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("這裡是委託開始執行收益扣款三代活躍用戶修改語句開始");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("這裡是委託開始執行收益扣款三代活躍用戶修改語句開始返款金額為"+totalRetail);
						}
					
					}
			  }
			}
		}
		return totalRetail;
	}

	// 批发市场找9代
	public BigDecimal wholesale(int userId, BigDecimal profit,String orderNo) {
		logger.debug("開始執行批發市場制度");
		// 查询当前代
		FrontUserDO frontUser = memberUserService.get(userId);
		// 统计总共分出多少的钱
		BigDecimal totalRetail = BigDecimal.ZERO;
		;
		int len = 1;// 循环的基数，继续++后继续循环
		int puserId = frontUser.getUserPid();
		if (puserId > 0) {
			for (int i = 0; i < len; i++) {
				// 循环父级查询用户
				FrontUserDO parentUser = memberUserService.get(puserId);
				if(parentUser!=null) {
				// 根据直推人数计算分配等级奖励
                 if (parentUser.getIsActive() == 1) {
                	 logger.debug("活躍用戶一代");
						if (frontUser.getAlgebra()-parentUser.getAlgebra()== 1) {
							len++;
							puserId = parentUser.getUserPid();
							 logger.debug("活躍用戶一代開始計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward1").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							//缺少该笔收益来源于哪个订单，哪个下级
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							 logger.debug("活躍用戶一代修改方法急速計算");
							totalRetail = pUserProfit.add(totalRetail);
							 logger.debug("返款金額為");
						}

					} if (frontUser.getAlgebra()-parentUser.getAlgebra() == 2) {
						len++;
						puserId = parentUser.getUserPid();
						 logger.debug("用戶二代開始計算");
						if (parentUser.getIsActive() == 1) {
							 logger.debug("活躍用戶二代開始計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward2").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							 logger.debug("活躍用戶二代開始修改語句自信計算");
							totalRetail = pUserProfit.add(totalRetail);
							 logger.debug("活躍用戶二代開始返款為"+totalRetail);
						}
						
					} if (frontUser.getAlgebra()-parentUser.getAlgebra() == 3) {
						len++;
						puserId = parentUser.getUserPid();
						 logger.debug("用戶三代開始計算");
						if (parentUser.getIsActive() == 1) {
							 logger.debug("用戶三代開始活躍用戶計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward3").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							 logger.debug("用戶三代開始活躍用戶修改語句計算");
							totalRetail = pUserProfit.add(totalRetail);
							 logger.debug("用戶三代開始活躍用戶計算返款"+totalRetail);
							
						}
					}  if (frontUser.getAlgebra()-parentUser.getAlgebra() == 4) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("用戶四代開始用戶計算");
						if (parentUser.getIsActive() == 1) {
							logger.debug("用戶四代開始活躍用戶計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward4").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("用戶四代開始活躍用戶修改語句計算");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("用戶四代開始活躍用戶開始返款計算"+totalRetail);
						}
					}  if (frontUser.getAlgebra()-parentUser.getAlgebra() == 5) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("用戶五代開始用戶計算");
						if (parentUser.getIsActive() == 1) {
							logger.debug("用戶五代活躍開始用戶計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward5").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("用戶五代開始用戶修改語句計算");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("用戶五代開始活躍用戶開始返款計算"+totalRetail);
						}
						
					} if (frontUser.getAlgebra()-parentUser.getAlgebra() == 6) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("用戶六代開始用戶計算"+totalRetail);
						if (parentUser.getIsActive() == 1) {
							logger.debug("用戶六代開始活躍用戶計算"+totalRetail);
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward6").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("用戶六代開始用戶修改語句計算");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("用戶六代開始活躍用戶開始返款計算"+totalRetail);
						}
						
					}  if (frontUser.getAlgebra()-parentUser.getAlgebra() == 7) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("用戶七代開始用戶計算");
						if (parentUser.getIsActive() == 1) {
							logger.debug("用戶七代開始活躍用戶計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward7").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("用戶七代開始活躍用戶修改欲絕計算");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("用戶七代開始活躍用戶收益返款計算"+totalRetail);
						}
						
					} if (frontUser.getAlgebra()-parentUser.getAlgebra()== 8) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("用戶八代開始計算"+totalRetail);
						if (parentUser.getIsActive() == 1) {
							logger.debug("用戶八代活躍開始計算"+totalRetail);
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward8").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("用戶八代活躍開始修改語句計算"+totalRetail);
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("用戶八代開始活躍用戶收益返款計算"+totalRetail);
						}
						
					}  if (frontUser.getAlgebra()-parentUser.getAlgebra()== 9) {
						len++;
						puserId = parentUser.getUserPid();
						logger.debug("用戶九代計算");
						if (parentUser.getIsActive() == 1) {
							logger.debug("用戶九代活躍計算");
							BigDecimal F1TeamGrade = new BigDecimal(
									paramServiceImpl.findValue("wholesaleReward9").getParamValue());
							// 父一代获取的收益为
							BigDecimal pUserProfit = profit.multiply(F1TeamGrade);
							Map<String, Object> mapUpdateUt = new HashMap<String, Object>();
							mapUpdateUt.put("vfsBalance", pUserProfit);
							mapUpdateUt.put("benefitType", 999);
							mapUpdateUt.put("userId", parentUser.getUserId());
							mapUpdateUt.put("sourceId",userId);
							mapUpdateUt.put("orderNo",orderNo);
							walletManagerService.shareProfitByUtMap(mapUpdateUt);
							logger.debug("用戶就代活躍開始修改語句計算");
							totalRetail = pUserProfit.add(totalRetail);
							logger.debug("用戶就代活躍開始返款計算"+totalRetail);
						}
						
					}
				} 

			}
		}
		return totalRetail;
	}
}
