package com.renegade.service;

import java.util.List;
import java.util.Map;

import com.renegade.domain.ParamDO;
import com.renegade.domain.ResonancePoolRecordDO;

/**
 * 
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-04-17 17:25:49
 */
public interface ParamService {

	ParamDO get(Integer id);

	List<ParamDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ParamDO param);

	int update(ParamDO param);

	int remove(Integer id);

	int batchRemove(Integer[] ids);

	/**
	 * 获得参数值
	 * 
	 * @param paramCode
	 * @return
	 */
	ParamDO findValue(String paramCode);

	// 查看个人历史投放记录以及历史开奖记录
	List<ResonancePoolRecordDO>getUserHistoryRecord(Integer userId);
	//查看开奖记录

	List<Map<String, Object>> historyList(Integer userId);
}
