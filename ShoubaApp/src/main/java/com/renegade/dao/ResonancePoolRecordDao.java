package com.renegade.dao;

import com.renegade.domain.ResonancePoolRecordDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 共振池用户投放记录
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-19 21:47:57
 */
@Mapper
public interface ResonancePoolRecordDao {

	ResonancePoolRecordDO get(Integer id);

	ResonancePoolRecordDO getUserRecord(Map<String, Object> map);

	ResonancePoolRecordDO getByUserId(Map<String, Object> map);

	// 查看个人历史投放记录
	List<ResonancePoolRecordDO> getUserHistoryRecord(Integer userId);

	List<ResonancePoolRecordDO> list(Map<String, Object> map);

	List<ResonancePoolRecordDO> getUserLevel(Integer level, Integer resonanceId);

	int getUserLevelNum(Integer level, Integer resonanceId);

	int count(Map<String, Object> map);

	int save(ResonancePoolRecordDO resonancePoolRecord);

	int saveSelefPutInto(ResonancePoolRecordDO resonancePoolRecord);

	int update(ResonancePoolRecordDO resonancePoolRecord);

	int updateStatus(ResonancePoolRecordDO resonancePoolRecord);

	int remove(Integer id);

	int batchRemove(Integer[] ids);

	Map<String, Object> getRankingResonance(Map<String, Object> map);

	Map<String, Object> getHistoryCount(Integer resonanceId);

	List<Map<String, Object>> listRankingResonance(Map<String, Object> map);
}
