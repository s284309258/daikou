package com.renegade.dao;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.renegade.domain.FrontFlowingRecordDO;

/**
 * 用户流水表
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 18:22:13
 */
@Mapper
public interface FrontFlowingRecordDao {

	FrontFlowingRecordDO get(Long flowingId);
	
	List<FrontFlowingRecordDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FrontFlowingRecordDO frontFlowingRecord);
	
	int update(FrontFlowingRecordDO frontFlowingRecord);
	
	int remove(Long flowing_id);
	
	int batchRemove(Long[] flowingIds);

	FrontFlowingRecordDO findKindBalance(String sendNo);
    //计算该用户直推有效用户的usdt收益
	BigDecimal getUsdtProfit(Map<String,Object> map);
	  //计算该用户usdt收益
	BigDecimal getUsdtProfitTeam(Integer userId);

}
