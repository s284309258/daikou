package com.renegade.dao;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.renegade.domain.FrontFlowingRecordFreetrbDO;

/**
 * 交易余额流水表
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-23 14:41:09
 */
@Mapper
public interface FrontFlowingRecordFreetrbDao {

	FrontFlowingRecordFreetrbDO get(Long flowingId);
	
	List<FrontFlowingRecordFreetrbDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FrontFlowingRecordFreetrbDO frontFlowingRecordFreetrb);
	
	int update(FrontFlowingRecordFreetrbDO frontFlowingRecordFreetrb);
	
	int remove(Long flowing_id);
	
	int batchRemove(Long[] flowingIds);
	  //计算该用户直推有效用户的ut收益
	BigDecimal getUtProfit(Map<String, Object> map1);
	 //计算该用户的ut收益
	BigDecimal getUtProfitTeamUt(Integer userId);
}
