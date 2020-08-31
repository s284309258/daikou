package com.renegade.dao;

import com.renegade.domain.ApplyRecordDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 17:23:06
 */
@Mapper
public interface ApplyRecordDao {

	ApplyRecordDO get(Integer id);
	
	List<ApplyRecordDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ApplyRecordDO applyRecord);
	
	int update(ApplyRecordDO applyRecord);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
     //根据用户查询对象
	ApplyRecordDO getObjectById(Integer userId);
}
