package com.renegade.dao.yilian;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户管理
 * @author Administrator
 *
 */
public interface UserInfoMapper {

	/**
	 * 根据id查询账户信息
	 * @param string
	 * @return
	 */
	Map<String, Object> getUserInfoById(@Param("user_id") String user_id);
	
	/**
	 * 根据user_account查询账户信息
	 * @param user_account
	 * @return
	 */
	Map<String, Object> getUserInfoByUserAccount(@Param("sys_user_account") String sys_user_account);

	/**
	 * 更新用户登录信息
	 * @param dataMap
	 * @return
	 */
	int updateUserLoginInfo(@Param("map") Map<String, Object> dataMap);

	/**
	 * 更新所有父级伞下人数
	 * @param dataMap
	 * @return
	 */
	int updateParentUnderNum(@Param("map") Map<String, Object> dataMap);

	/**
	 * 更新父级直推人数
	 * @param dataMap
	 * @return
	 */
	int updateUserReferNum(@Param("map") Map<String, Object> dataMap);

	/**
	 * 保存用户信息
	 * @param dataMap
	 * @return
	 */
	int saveUserInfo(@Param("map") Map<String, Object> dataMap);

	/**
	 * 更新用户登录密码
	 * @param dataMap
	 * @return
	 */
	int updateUserLoginPass(@Param("map") Map<String, Object> dataMap);

	/***
	 *add byqh202003
	 * @param dataMap
	 * @return
	 */
	@Update("update t_user_info set device_token=#{map.device_token} where id=#{map.user_id}")
	int updateUserDeviceToken(@Param("map") Map<String, Object> dataMap);


	/**
	 * 更新用户交易密码
	 * @param dataMap
	 * @return
	 */
	int updateUserPayPass(@Param("map") Map<String, Object> dataMap);
	
	/**
	 * 通过用户ID查询用户基础信息放入缓存
	 * @param map
	 * @return
	 */
	Map<String, Object> getUserInfoCacheById(@Param("user_id") String user_id);
	

	/**
	 * 更新用户资料（头像）
	 * @param dataMap
	 * @return
	 */
	int updateUserInfo(@Param("map") Map<String, Object> dataMap);

	/**
	 * 查询冻结用户信息
	 * @param user_id
	 * @return
	 */
	Map<String, Object> getUserFreezeCacheById(@Param("user_id") String user_id);

	/**
	 * 查询用户实名认证状态信息
	 * @param user_id
	 * @return
	 */
	Map<String, Object> getUserAuthStatus(@Param("user_id") String user_id);

	/**
	 * 更新用户实名认证状态信息
	 * @param map
	 * @return
	 */
	int updateUserAuthStatus(@Param("map") Map<String, Object> map);

	
	/**
	 * 查询用户代理信息
	 * @param user_id
	 * @return
	 */
	Map<String, Object> getUserAgentInfo(@Param("user_id") String user_id);

	
	/**
	 * 查询用户实时钱包信息
	 * @param string
	 * @return
	 */
	Map<String, Object> getUserPurseById(@Param("user_id") String user_id);

	
	/**
	 * 更新用户信息=======》用户申请提现
	 * @param dataMap
	 * @return
	 */
	int updateUserByApplyCash(@Param("map") Map<String, Object> dataMap);

	
	/**
	 * 根据用户id查询代理信息
	 * @param string
	 * @return
	 */
	Map<String, Object> getAgentUserById(@Param("user_id") String user_id);

	
	/**
	 * 修改用户手机号
	 * @param map
	 * @return
	 */
	int updateUserTel(@Param("map") Map<String, Object> map);

	
	/**
	 * 根据登录名称查询代理信息
	 * @param string
	 * @return
	 */
	Map<String, Object> getAgentUserByLoginName(@Param("login_name") String login_name);


	/***
	 * 查询当日总交易金额 add byqh 201912
	 * @param user_id
	 * @return
	 */
	@Select("select IFNULL(cast(sum(amount) as char),'0') as amount from (" +
			" select sum(performance) as amount from t_summary_traditional_pos_trans_everyday where user_id=#{user_id} and cre_date=CURRENT_DATE" +
			" union" +
			" select sum(performance) as amount from t_summary_mpos_trans_everyday where user_id=#{user_id} and cre_date=CURRENT_DATE" +
			" ) t")
	@ResultType(String.class)
	String selectTransAmountByDay(@Param("user_id") String user_id);

	/***
	 * 查询累计总交易金额 add byqh 201912
	 * @param user_id
	 * @return
	 */
	@Select("select IFNULL(cast(sum(amount) as char),'0') as amount from (" +
			" select sum(performance) as amount from t_summary_traditional_pos_trans_all where user_id=#{user_id}" +
			" union" +
			" select sum(performance) as amount from t_summary_mpos_trans_all where user_id=#{user_id}" +
			" ) t")
	String selectTransAmountByAll(@Param("user_id") String user_id);

}
