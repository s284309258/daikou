package com.renegade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renegade.dao.FrontFlowingRecordFreetrbDao;
import com.renegade.domain.FrontFlowingRecordFreetrbDO;
import com.renegade.service.FrontFlowingRecordFreetrbService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;





@Service
public class FrontFlowingRecordFreetrbServiceImpl implements FrontFlowingRecordFreetrbService {
	@Autowired
	private FrontFlowingRecordFreetrbDao frontFlowingRecordFreetrbDao;
	
	@Override
	public FrontFlowingRecordFreetrbDO get(Long flowingId){
		return frontFlowingRecordFreetrbDao.get(flowingId);
	}
	
	@Override
	public List<FrontFlowingRecordFreetrbDO> list(Map<String, Object> map){
		return frontFlowingRecordFreetrbDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return frontFlowingRecordFreetrbDao.count(map);
	}
	
	@Override
	public int save(FrontFlowingRecordFreetrbDO frontFlowingRecordFreetrb){
		return frontFlowingRecordFreetrbDao.save(frontFlowingRecordFreetrb);
	}
	
	@Override
	public int update(FrontFlowingRecordFreetrbDO frontFlowingRecordFreetrb){
		return frontFlowingRecordFreetrbDao.update(frontFlowingRecordFreetrb);
	}
	
	@Override
	public int remove(Long flowingId){
		return frontFlowingRecordFreetrbDao.remove(flowingId);
	}
	
	@Override
	public int batchRemove(Long[] flowingIds){
		return frontFlowingRecordFreetrbDao.batchRemove(flowingIds);
	}

	@Override
	public BigDecimal getUtProfit(Map<String, Object> map1) {
		// TODO Auto-generated method stub
		return frontFlowingRecordFreetrbDao.getUtProfit(map1);
	}

	@Override
	public BigDecimal getUtProfitTeamUt(Integer userId) {
		// TODO Auto-generated method stub
		return frontFlowingRecordFreetrbDao.getUtProfitTeamUt(userId);
	}
	
}
