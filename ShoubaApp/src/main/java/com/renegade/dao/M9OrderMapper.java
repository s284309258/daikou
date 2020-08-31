package com.renegade.dao;

import com.renegade.domain.M9UserAndOrderDo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface M9OrderMapper {
    /***
     * 查询入场订单列表实体类的方式存储
     * @param params
     * @return
     */
    List<M9UserAndOrderDo> getUserOrderListObj(@Param("map") Map<String, Object> params);

    /***
     * 查询入场订单列表
     * @param params
     * @return
     */
    List<Map<String,Object>> getUserOrderList(@Param("map") Map<String, Object> params);

    /***
     * 更新M9订单信息
     * @param params
     * @return
     */
    int updateOrder(@Param("map") Map<String, Object> params);


    List<Map<String,Object>> selectOrderAll(@Param("map") Map<String,Object> parms);


    List<Map<String,Object>> selectOrder(@Param("map") Map<String,Object> parms);

    int insertOrder(@Param("map") Map<String,Object> parms);


    int updateOrderTask(@Param("map") Map<String, Object> params);

    @Select("select count(1) as cnt from m9_order where (user_id=#{user_id} and state in(1,4,5)) or (buyer=#{user_id} and state in(1,4,5))")
    Map<String,Object> checkSaleAndBuyOrder(String user_id);
}
