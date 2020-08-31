package com.renegade.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.renegade.config.AjaxJson;
import com.renegade.config.RegexUtil;
import com.renegade.config.StringUtil;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.ResonancePoolDao;
import com.renegade.dao.ResonancePoolRecordDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.FrontUserDO;
import com.renegade.domain.ResonancePoolDO;
import com.renegade.domain.ResonancePoolRecordDO;
import com.renegade.service.ParamService;
import com.renegade.service.ResonancePoolRecordService;

@Service
public class ResonancePoolRecordServiceImpl implements ResonancePoolRecordService {
	@Autowired
	private ResonancePoolRecordDao resonancePoolRecordDao;
	@Autowired
	private UsdtChargeServiceImpl usdtChargeServiceImpl;
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private ResonancePoolDao resonancePoolDao;
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private WalletManagerDao walletManagerDao;

	@Override
	@Transactional
	public AjaxJson saveSelefPutInto(Map<String, Object> map) {
		// TODO Auto-generated method stub
		// 验证密码是否正确
		FrontUserDO frontUserDO = frontUserDao.get(Integer.parseInt(map.get("userId").toString()));
		AjaxJson ajaxJson = usdtChargeServiceImpl.selfCheckTrasferPass(map, frontUserDO, null);
		if (!ajaxJson.isSuccess()) {
			return ajaxJson;
		}
		if (!RegexUtil.isValidNumFloat( map.get("money").toString())) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("输入的数量有误");
			return ajaxJson;
		}
		try {
			//没有满的共振池
			map.put("resonanceStatus", "0");
			List<ResonancePoolDO> resonancePoolDO = resonancePoolDao.list(map);
			if (resonancePoolDO.isEmpty()) {// 没有共振池，初始化那么创建要一个共振池
				ajaxJson.setMsg("共振池维护中");
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			// 用户扣钱
			BigDecimal balance = BigDecimal.ZERO;
			String orderNo = StringUtil.getDateTimeAndRandomForID();
			if (map.get("type").equals("0")) {// usdt
				// 折算成vfs
				balance = new BigDecimal(map.get("money").toString())
						.multiply(new BigDecimal(paramServiceImpl.findValue("usdtTovs").getParamValue()))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				map.put("balacne", map.get("money").toString());
				map.put("orderNo", orderNo);
				map.put("benefitType", 20);
				map.put("sourceId", "0");
				int t = walletManagerDao.updateReduceUsdt(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("共振的usdt余额不够！");
					return ajaxJson;
				}
			} else {// ut
				balance = new BigDecimal(map.get("money").toString())
						.multiply(new BigDecimal(paramServiceImpl.findValue("utToVs").getParamValue()))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				map.put("balacne", map.get("money").toString());
				map.put("orderNo", orderNo);
				map.put("benefitType", 20);
				map.put("sourceId", "0");
				int t = walletManagerDao.updateReduceUt(map);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("共振的ut余额不够！");
					return ajaxJson;
				}
			}
			// 投放记录没投的出入，投的那么就更新
			ResonancePoolRecordDO recordDO = new ResonancePoolRecordDO();
			recordDO.setResonanceId(resonancePoolDO.get(0).getId());
			recordDO.setUserId(Integer.parseInt(map.get("userId").toString()));
			recordDO.setMoney(balance);
			// 先更新
			int t = resonancePoolRecordDao.update(recordDO);
			if (t == 0) {
				t = resonancePoolRecordDao.save(recordDO);
				if (t != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setMsg("投放失败");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			// 更新该奖池的剩余数量
			map.put("id", resonancePoolDO.get(0).getId());
			map.put("balacne", balance);
			t = resonancePoolDao.updateRemaingNum(map);
			if (t != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setMsg("共振池中的余量不够");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception0
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ajaxJson.setMsg("系统池异常");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}

	}

}
