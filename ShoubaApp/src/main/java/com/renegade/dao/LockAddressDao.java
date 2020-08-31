package com.renegade.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.renegade.domain.LockAddressDO;

/**
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 15:20:58
 */
@Mapper
public interface LockAddressDao {

	LockAddressDO get(Integer id);
	
	List<LockAddressDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(LockAddressDO lockAddress);
	
	int update(LockAddressDO lockAddress);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
