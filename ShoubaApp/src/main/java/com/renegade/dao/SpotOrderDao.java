package com.renegade.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.renegade.domain.SpotOrder;

@Mapper
public interface SpotOrderDao {

	/**
	 * 统计提货订单数量
	 * 
	 * @param userId
	 * @return
	 */
	int findDeliverNumByUserId(@Param("userId") int userId);

	// 通过订单ID
	SpotOrder getOrderdetail(String orderId);

	// 通过订单编号
	SpotOrder getOrderNo(String orderNo);

	int updateDeliveryOrder(SpotOrder spotOrder);

	int updateComplainStatus(Map<String, Object> map);

	List<Map<String, Object>> findeliverByUserIdOrder(Integer userId, Integer storeId);

	List<SpotOrder> listOrderDetail();

	int reduceDeliverOrder(Map<String, Object> map);

	int saveDeliverOrder(SpotOrder zkDeliverOrder);

	int confirmDelivery(Map<String, Object> map);

	int confirmDeliveryUser(Map<String, Object> map);

	int updateGsStatus(Map<String, Object> map);

	int updateDeliverGoods(Map<String, Object> map);

	// 查询待发货订单
	int findWaitOrderById(Integer userId);

	// 查询待发货订单
	List<Map<String, Object>> findeliverBydeliveryOrderId(@Param("deliveryOrderId") String deliveryOrderId,
			@Param("userId") Integer userId, Integer storeId);

	int countGoodsNumByUserId(@Param("userId") Integer userId, @Param("storeId") Integer storeId,
			@Param("goodsId") Integer goodsId);

	BigDecimal sumGoodsPriceByUserId(@Param("userId") Integer userId, @Param("storeId") Integer storeId,
			@Param("goodsId") Integer goodsId);

}
