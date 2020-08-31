package com.renegade.dao;

import com.renegade.domain.HangSellOrderDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 挂售订单列表
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-17 21:51:50
 */
@Mapper
public interface HangSellOrderDao {

	HangSellOrderDO get(Integer id);
	List<HangSellOrderDO> getOrderIdLockDetail(String id);
	int getOrderId(String id);
	int getOrderIdLock(String id);
	
	List<HangSellOrderDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(HangSellOrderDO hangSellOrder);
	
	int update(HangSellOrderDO hangSellOrder);
	int updateStatus(Integer id);
	
	int remove(String hang_sell_id);
	
	int batchRemove(String[] hangSellIds);
	int updateStatusRfoud(Map<String, Object> map);
	int updateStatusRfoudCode(Map<String, Object> map);
	
	List<Map<String, Object>> consignmentOrder(Integer hangSellUser);
	List<Map<String, Object>> listHangSellOrder(Map<String, Object> map);
}
