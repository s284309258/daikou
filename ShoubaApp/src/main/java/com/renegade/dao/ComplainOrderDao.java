package com.renegade.dao;

import com.renegade.domain.ComplainOrderDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 订单投诉列表
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-21 10:14:01
 */
@Mapper
public interface ComplainOrderDao {

	ComplainOrderDO get(Integer id);
	
	List<ComplainOrderDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ComplainOrderDO complainOrder);
	
	int update(ComplainOrderDO complainOrder);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
