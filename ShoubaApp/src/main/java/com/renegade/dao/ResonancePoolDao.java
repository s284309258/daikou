package com.renegade.dao;

import com.renegade.domain.ResonancePoolDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 共振奖池
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-19 21:47:25
 */
@Mapper
public interface ResonancePoolDao {

	ResonancePoolDO get(Integer id);

	Map<String, Object> getMoneyCount();

	int updateStatus(Integer id);

	List<ResonancePoolDO> getNewRecord();

	List<ResonancePoolDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int updateRemaingNum(Map<String, Object> map);

	int save(ResonancePoolDO resonancePool);

	int update(ResonancePoolDO resonancePool);

	int remove(Integer id);

	int batchRemove(Integer[] ids);
}
