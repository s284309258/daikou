package com.renegade.service;

import com.renegade.config.AjaxJson;
import com.renegade.domain.M9UserAndOrderDo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface M9OrderService {
    /***
     * 查询入场订单列表实体类的方式存储
     * @param params
     * @return
     */
    List<M9UserAndOrderDo> getUserOrderListObj(Map<String, Object> params);

    /***
     * 查询入场订单列表
     * @param params
     * @return
     */
    List<Map<String,Object>> getUserOrderList(Map<String, Object> params);

    /***
     * 更新M9订单信息
     * @param params
     * @return
     */
    int updateOrder(Map<String, Object> params);


    /***
     * 查询交易大厅订单信息
     * @param params
     * @return
     */
    List<Map<String,Object>> selectOrderAll(Map<String,Object> params);

    /***
     *查询订单不分页
     * @param parms
     * @return
     */
    List<Map<String,Object>> selectOrder(Map<String,Object> parms);

    /***
     * 新增入场订单
     * @param parms
     * @return
     */
    int insertOrder(Map<String,Object> parms);

    /***
     * 确认交易(交易成功)
     * @param userId
     * @param params
     * @return
     */
    AjaxJson transfer_confirm(String userId,Map<String, Object> params);

    /***
     * 撤销订单
     * @param userId
     * @param params
     * @return
     */
    AjaxJson transfer_undo(String userId,Map<String, Object> params);


    /**
     * 排单入场
     * @param userId
     * @param map
     * @return
     */
    AjaxJson transfer_buy(String userId,Map<String,Object> map);


    /***
     * 卖给ta，交易大厅中卖给ta操作
     * @param userId
     * @param map
     * @return
     */
    AjaxJson transfer(String userId,Map<String,Object> map);

    /***
     * 上传打款凭证
     * @param userId
     * @param browerfile
     * @param map
     * @return
     */
    AjaxJson uploadFile(String userId, MultipartFile browerfile, Map<String,Object> map);


    /***
     * 得到系统超时和警告次数
     * 0.cancelTime
     * 1.warn_count_saletota
     * 2.warn_count_confirm
     * @return cancelTime,warn_count_saletota,warn_count_confirm
     */
    String[] getWarnAndEndTimeParam();


}
