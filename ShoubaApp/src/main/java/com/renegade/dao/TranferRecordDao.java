package com.renegade.dao;

import com.renegade.domain.TranferRecordDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 转账记录
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 14:59:38
 */
@Mapper
public interface TranferRecordDao {

	TranferRecordDO get(String id);
	
	List<TranferRecordDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(TranferRecordDO tranferRecord);
	
	int update(TranferRecordDO tranferRecord);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
