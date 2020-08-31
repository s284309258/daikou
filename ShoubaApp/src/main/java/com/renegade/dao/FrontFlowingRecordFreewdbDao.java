package com.renegade.dao;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.renegade.domain.FrontFlowingRecordFreewdbDO;

/**
 * 提现余额流水表
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-23 14:45:01
 */
@Mapper
public interface FrontFlowingRecordFreewdbDao {

	FrontFlowingRecordFreewdbDO get(Long flowingId);
	
	List<FrontFlowingRecordFreewdbDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FrontFlowingRecordFreewdbDO frontFlowingRecordFreewdb);
	
	int update(FrontFlowingRecordFreewdbDO frontFlowingRecordFreewdb);
	
	int remove(Long flowing_id);
	
	int batchRemove(Long[] flowingIds);

	BigDecimal getUtProfit(Map<String, Object> map1);

	BigDecimal getUtProfitTeamVfs(Integer userId);

	BigDecimal getUtAllProfit();
}
