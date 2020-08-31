package com.renegade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renegade.dao.FrontFlowingRecordDao;
import com.renegade.domain.FrontFlowingRecordDO;
import com.renegade.service.FrontFlowingRecordService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;





@Service
public class FrontFlowingRecordServiceImpl implements FrontFlowingRecordService {
	@Autowired
	private FrontFlowingRecordDao frontFlowingRecordDao;
	
	@Override
	public FrontFlowingRecordDO get(Long flowingId){
		return frontFlowingRecordDao.get(flowingId);
	}
	
	@Override
	public List<FrontFlowingRecordDO> list(Map<String, Object> map){
		return frontFlowingRecordDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return frontFlowingRecordDao.count(map);
	}
	
	@Override
	public int save(FrontFlowingRecordDO frontFlowingRecord){
		return frontFlowingRecordDao.save(frontFlowingRecord);
	}
	
	@Override
	public int update(FrontFlowingRecordDO frontFlowingRecord){
		return frontFlowingRecordDao.update(frontFlowingRecord);
	}
	
	@Override
	public int remove(Long flowingId){
		return frontFlowingRecordDao.remove(flowingId);
	}
	
	@Override
	public int batchRemove(Long[] flowingIds){
		return frontFlowingRecordDao.batchRemove(flowingIds);
	}

	@Override
	public BigDecimal getUsdtProfit(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return frontFlowingRecordDao.getUsdtProfit(map);
	}

	@Override
	public BigDecimal getUsdtProfitTeam(Integer userId) {
		// TODO Auto-generated method stub
		return frontFlowingRecordDao.getUsdtProfitTeam(userId);
	}

	

	
}
