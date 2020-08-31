package com.renegade.service;

import com.renegade.config.AjaxJson;

import java.util.List;
import java.util.Map;

public interface M9UserService {


    /***
     * 用户注册接口
     * @param map
     * @return
     */
    AjaxJson register(Map<String,Object> map);

    /***
     * 用户登陆接口
     * @param map
     * @return
     */
    AjaxJson login(Map<String,Object> map);

    /***
     * 查询M9会员求购的订单列表
     * @param params
     * @return
     */
    List<Map<String,Object>> getUserOrderList(Map<String, Object> params);

    /***
     * 查询M9会员列表,包括为入场的会员
     * @param params
     * @return
     */
    List<Map<String,Object>> selectUserList(Map<String,Object> params);

    /***
     * 更新M9求购订单
     * @param params
     * @return
     */
    int updateOrder(Map<String,Object> params);

    /***
     * 更新M9用户信息
     * @param params
     * @return
     */
    int updateUser(Map<String,Object> params);

    /***
     * 记录充值记录
     * @param params
     * @return
     */
    int insert_change_log(Map<String,Object> params);

    /***
     * 查看排单币流水
     * @param params
     * @return
     */
    AjaxJson select_flowing_record(Map<String,Object> params);

    /***
     * 修改交易地址
     * @param params
     * @return
     */
    AjaxJson updateAddress(Map<String,Object> params);

    /***
     * 修改登陆密码
     * @param params
     * @return
     */
    AjaxJson updateUserPwd(Map<String,Object> params);

    /***
     * 修改用户头像和昵称
     * @param params
     * @return
     */
    AjaxJson updateUserInfo(Map<String, Object> params);

    /**
     * 验证交易密码和短信验证码
     * params:、
     * 1.id=m9user.id 用户id存在session.userId中
     * 2.pay_pwd 交易密码
     * 3.telphone 收短信的手机号
     * 4.code 输入的验证码
     * 5.bus_type 验证码需要校验的类型
     * @param params
     * @return
     */
    AjaxJson checkSmsCodeAndPayPwd(Map<String,Object> params);

    /**
     * 验证交易密码
     * params:、
     * 1.id=m9user.id 用户id存在session.userId中
     * 2.pay_pwd 交易密码
     * @param params
     * @return
     */
    AjaxJson checkPayPwd(Map<String,Object> params);


    /***
     * 兑换绿灯
     * @param quantity
     * @param userId
     * @param map
     * @return
     */
    AjaxJson exchange_light(String quantity,String userId,Map<String,Object> map);

    /***
     * 灯火详情-明细
     * 灯火明细，红灯+1，绿-1，蓝灯+1 等
     * @param params
     * @return
     */
    AjaxJson select_flowing_record_light(Map<String,Object> params);


    /***
     * 签到
     * @param user_id
     * @return
     */
    AjaxJson insert_sign_record(String user_id);

    /***
     * 查看用户签到列表
     * @param user_id
     * @return
     */
    AjaxJson select_sign_record(String user_id,int lastId);

    /***
     * 查看用户是否已经签到
     * @param user_id
     * @return
     */
    List<Map<String,Object>> select_sign_record(String user_id,String sign_date) throws Exception;

}
