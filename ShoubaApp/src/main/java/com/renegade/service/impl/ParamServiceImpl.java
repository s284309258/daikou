package com.renegade.service.impl;

import static org.mockito.Mockito.verifyZeroInteractions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renegade.annotations.EnableCacheService;
import com.renegade.annotations.EnableCacheService.CacheOperation;
import com.renegade.config.SysParamConstant;
import com.renegade.dao.ParamDao;
import com.renegade.dao.ResonancePoolDao;
import com.renegade.dao.ResonancePoolRecordDao;
import com.renegade.domain.ParamDO;
import com.renegade.domain.ResonancePoolDO;
import com.renegade.domain.ResonancePoolRecordDO;
import com.renegade.service.ParamService;

@Service
public class ParamServiceImpl implements ParamService {
	@Autowired
	private ParamDao paramDao;
	@Autowired
	private ResonancePoolRecordDao resonancePoolRecordDao;
	@Autowired
	private ResonancePoolDao resonancePoolDao;
	@Autowired
	private ParamService paramServiceImpl;

	@Override
	public ParamDO get(Integer id) {
		return paramDao.getsss(id);
	}

	@Override
	public List<ParamDO> list(Map<String, Object> map) {
		return paramDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return paramDao.count(map);
	}

	@Override
	public int save(ParamDO param) {
		return paramDao.save(param);
	}

	@Override
	public int update(ParamDO param) {
		return paramDao.update(param);
	}

	@Override
	public int remove(Integer id) {
		return paramDao.remove(id);
	}

	@Override
	public int batchRemove(Integer[] ids) {
		return paramDao.batchRemove(ids);
	}

	@Override
	@EnableCacheService(keyPrefix = SysParamConstant.T_SYS_PARAM, fieldKey = "#paramCode", cacheOperation = CacheOperation.QUERY)
	public ParamDO findValue(String paramCode) {
		// TODO Auto-generated method stub
		return paramDao.findValue(paramCode);
	}

	@Override
	 @EnableCacheService(keyPrefix = SysParamConstant.T_SYS_RANK, fieldKey
	 ="#userId", cacheOperation = CacheOperation.QUERY)
	public List<ResonancePoolRecordDO> getUserHistoryRecord(Integer userId) {
		// TODO Auto-generated method stub
		List<ResonancePoolRecordDO> recordDOs = resonancePoolRecordDao.getUserHistoryRecord(userId);
		return recordDOs;
	}

	/**
	 * 查看历史开奖记录
	 */
	@Override
	@EnableCacheService(keyPrefix = SysParamConstant.T_SYS_HISTORY, fieldKey = "#userId", cacheOperation = CacheOperation.QUERY)
	public List<Map<String, Object>>  historyList(Integer userId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMap = new ArrayList<>();
		List<ResonancePoolDO> resonancePoolDO = resonancePoolDao.getNewRecord();
		//由于msql里使用聚合函数，会导致集合为空，但是会有长度,过滤空元素
		resonancePoolDO.removeAll(Collections.singleton(null));
		int a=resonancePoolDO.size();
		if (resonancePoolDO.size() > 0) {
			int t = resonancePoolDO.size();
			for (ResonancePoolDO resonancePoolDO2 : resonancePoolDO) {
				Map<String, Object> map = new HashMap<>();
				map.put("daishu", t);
				Map<String, Object> map2 = resonancePoolRecordDao.getHistoryCount(resonancePoolDO2.getId());
				if (map2 != null) {
					map.put("map2", map2);
					map.put("type2", 1);
				} else {
					map.put("type2", 0);
				}
			
				BigDecimal money3 = resonancePoolDO2.getResonanceTotal()
						.multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
				BigDecimal money2 = resonancePoolDO2.getResonanceTotal()
						.multiply(new BigDecimal(paramServiceImpl.findValue("level2").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
				BigDecimal money1 =resonancePoolDO2.getResonanceTotal()
						.multiply(new BigDecimal(paramServiceImpl.findValue("level1").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
				// 查看是否是裂变者
				map.put("userId", userId);
				map.put("money3", money3);
				map.put("money2", money2);
				map.put("money1", money1);
				map.put("status", "1");
				map.put("resonanceId", resonancePoolDO2.getId());
				// 当前个人排名
//				Map<String, Object> map3 = resonancePoolRecordDao.getRankingResonance(map);
				ResonancePoolRecordDO rDo=resonancePoolRecordDao.getByUserId(map);
				if (rDo != null) {
//					获取的奖励
					BigDecimal bigDecimal=BigDecimal.ZERO;
					System.out.println("l============"+resonancePoolDO2.getResonanceTotal());
					if (rDo.getLevel3().equals(1)) {
						 bigDecimal=resonancePoolDO2.getResonanceTotal().multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue())).multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit1").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
					}else if (rDo.getLevel3().equals(2)) {
						 bigDecimal=resonancePoolDO2.getResonanceTotal().multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue())).multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit2").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
					}else if (rDo.getLevel3().equals(3)) {
						 bigDecimal=resonancePoolDO2.getResonanceTotal().multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue())).multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit3").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
					}else if (rDo.getLevel3().equals(4)) {
						 bigDecimal=resonancePoolDO2.getResonanceTotal().multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue())).multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit4").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
					}else if (rDo.getLevel3().equals(5)) {
						 bigDecimal=resonancePoolDO2.getResonanceTotal().multiply(new BigDecimal(paramServiceImpl.findValue("level3").getParamValue())).multiply(new BigDecimal(paramServiceImpl.findValue("fissionBenefit5").getParamValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
					}
					map.put("map3", rDo);
					map.put("bigDecimal", bigDecimal);
					map.put("type3", 1);
				} else {
					map.put("type3", 0);
				}
				listMap.add(map);
				t--;
			}
		}
		return listMap;
	}

}
