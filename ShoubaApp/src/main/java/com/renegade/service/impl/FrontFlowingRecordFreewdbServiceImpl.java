package com.renegade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renegade.dao.FrontFlowingRecordFreewdbDao;
import com.renegade.domain.FrontFlowingRecordFreewdbDO;
import com.renegade.service.FrontFlowingRecordFreewdbService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;





@Service
public class FrontFlowingRecordFreewdbServiceImpl implements FrontFlowingRecordFreewdbService {
	@Autowired
	private FrontFlowingRecordFreewdbDao frontFlowingRecordFreewdbDao;
	
	@Override
	public FrontFlowingRecordFreewdbDO get(Long flowingId){
		return frontFlowingRecordFreewdbDao.get(flowingId);
	}
	
	@Override
	public List<FrontFlowingRecordFreewdbDO> list(Map<String, Object> map){
		return frontFlowingRecordFreewdbDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return frontFlowingRecordFreewdbDao.count(map);
	}
	
	@Override
	public int save(FrontFlowingRecordFreewdbDO frontFlowingRecordFreewdb){
		return frontFlowingRecordFreewdbDao.save(frontFlowingRecordFreewdb);
	}
	
	@Override
	public int update(FrontFlowingRecordFreewdbDO frontFlowingRecordFreewdb){
		return frontFlowingRecordFreewdbDao.update(frontFlowingRecordFreewdb);
	}
	
	@Override
	public int remove(Long flowingId){
		return frontFlowingRecordFreewdbDao.remove(flowingId);
	}
	
	@Override
	public int batchRemove(Long[] flowingIds){
		return frontFlowingRecordFreewdbDao.batchRemove(flowingIds);
	}

	@Override
	public BigDecimal getUtProfit(Map<String, Object> map1) {
		// TODO Auto-generated method stub
		return frontFlowingRecordFreewdbDao.getUtProfit(map1);
	}

	@Override
	public BigDecimal getUtProfitTeamVfs(Integer userId) {
		// TODO Auto-generated method stub
		return frontFlowingRecordFreewdbDao.getUtProfitTeamVfs(userId);
	}

	@Override
	public BigDecimal getUtAllProfit() {
		// TODO Auto-generated method stub
		return frontFlowingRecordFreewdbDao.getUtAllProfit();
	}
	
}
