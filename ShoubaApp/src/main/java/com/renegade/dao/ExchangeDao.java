package com.renegade.dao;

import com.renegade.domain.ExchangeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 兑换表
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-27 22:24:30
 */
@Mapper
public interface ExchangeDao {

	ExchangeDO get(String id);
	
	List<ExchangeDO> list(Map<String,Object> map);
	List<Map<String, Object>> listRecord(Integer userId);
	
	int count(Map<String,Object> map);
	
	int save(ExchangeDO exchange);
	
	int update(ExchangeDO exchange);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
