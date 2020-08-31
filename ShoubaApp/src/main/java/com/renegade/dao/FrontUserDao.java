package com.renegade.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.renegade.domain.FrontUserDO;

/**
 * app用户表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-04-17 17:01:29
 */
@Mapper
public interface FrontUserDao {

	FrontUserDO get(Integer userId);

	FrontUserDO getLock(Integer userId);

	List<Map<String, Object>> getFlowerRecordCN(Integer userId);

	List<Map<String, Object>> getFlowerRecordUSDT(Integer userId);

	List<Map<String, Object>> getFlowerRecordVFS(Integer userId);

	List<Map<String, Object>> getFlowerRecordUT(Integer userId);

	/**
	 * 通过手机号查询用户信息
	 * 
	 * @param phone
	 * @return
	 */
	FrontUserDO findUserByPhone(String phone);

	// 修改登陆密码
	int updateFrogetPass(Map<String, Object> map);

	int updatePayPass(Map<String, Object> map);

	Map<String, Object> getUnderNUm(@Param("userId") Integer userId, @Param("userLevel") Integer userLevel,
			@Param("num") Integer num);

	int getZhituiNum(@Param("userId") Integer userId);

	int underNumAll(@Param("userId") Integer userId);

	int updateReduceCapValue(Map<String, Object> map);

	/**
	 * 升级,必须交割完成以后才进行升级
	 * 
	 * @param map
	 * @return
	 */
	int updateLevel(Map<String, Object> map);

	int updateActiveUser(Integer userId);

	int updateLimitssssUser(Integer userId);

	/**
	 * 排行榜
	 * 
	 * @return
	 */
	List<Map<String, Object>> getRowsYestoday();

	int updateLimitStatus(FrontUserDO frontUser);

	/**
	 * 根据邮箱查询用户信息
	 * 
	 * @param phone
	 * @return
	 */
	FrontUserDO findUserByEmail(String phone);

	/**
	 * 根据邀请码查询信息
	 * 
	 * @param phone
	 * @return
	 */
	FrontUserDO findUserByCode(String phone);

	List<FrontUserDO> list(Map<String, Object> map);

	List<FrontUserDO> listSameLevel(Map<String, Object> map);

	List<Map<String, Object>> getTeamDetaulUser(Integer userId);

	int count(Map<String, Object> map);

	int save(FrontUserDO frontUser);

	int remove(Integer user_id);

	int updateAddTickets(Integer userId, Integer num);

	int updateReduceTickets(Integer userId, Integer num);

	int batchRemove(Integer[] userIds);

	// 更新直推人数
	int updateParentDirectNum(Integer userId);

	// 保存注册用户
	int insertUser(FrontUserDO user);

	int updateWalletAddress(String userTel);

	int updateWalletAddressVfsUt(String userTel);

	String findStoreNameById(Integer integer);

	// 查找活跃用户
	List<FrontUserDO> findIsActive();

	int findTeamLevel(Map<String, Object> mapLev);

	List<FrontUserDO> listProfit();

	void updateActiveUser(Integer userId, BigDecimal bigDecimal);
}
