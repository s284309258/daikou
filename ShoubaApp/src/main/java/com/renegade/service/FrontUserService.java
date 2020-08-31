package com.renegade.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.renegade.config.AjaxJson;
import com.renegade.domain.FrontUserDO;

/**
 * app用户表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-04-17 17:01:29
 */

public interface FrontUserService {
	
	FrontUserDO get(Integer userId);
	
	List<FrontUserDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FrontUserDO frontUser);
	
	
	int remove(Integer userId);
	
	int batchRemove(Integer[] userIds);
	/**
	 * 登陆
	 * @param phone
	 * @param pass
	 * @param req
	 * @return
	 */
	AjaxJson login(String phone,String pass,HttpServletRequest req);

	/**
	 * 注册
	 * @param phone
	 * @param pass1
	 * @param pass2
	 * @param code
	 * @param province
	 * @param city
	 * @param area
	 * @param pid
	 * @return
	 */
	AjaxJson regiest(String phone,String pass1,String pass2,String code,String userName,String   userNickname,String pid,String accType,String identityNo,HttpServletRequest req,String type);
	/**
	 * 忘记登陆密码。
	 * 
	 * @param map
	 * @return
	 */
	AjaxJson frogetLoginPassWord(Map<String, Object> map);
	/**
	 * 修改交易密码
	 * @param map
	 * @return
	 */
	AjaxJson updatePayPass(Map<String, Object> map,Integer userId);
	/**
	 * 添加地址
	 * @param map
	 * @param userId
	 * @return
	 */
	AjaxJson addUserAddress(Map<String, Object> map,Integer userId);
	AjaxJson updateAddress(Map<String, Object> map,Integer userId);
     //查找活跃用户
	List<FrontUserDO> findIsActive();
    //查询该用户下面有效直推人数
	int getZhituiNum(Integer userId);
   //升级
	void updateLevel(Map<String, Object> map);
     //查询所有会员信息
	List<FrontUserDO> listProfit();
	int findTeamLevel(Map<String, Object> mapLev);
}
