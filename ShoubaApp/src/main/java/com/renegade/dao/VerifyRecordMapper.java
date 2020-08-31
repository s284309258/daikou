package com.renegade.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface VerifyRecordMapper {

	/**
	 * 拿到发送账号在当前系统  最后发送的验证码
	 * @param map
	 * @return
	 */
	Map<String, Object> getInfolast(@Param("map")Map<String, Object> map); 
	
	
	/**
	 * 将验证码标记为已被验证
	 * @param entity
	 * @return
	 */
	int update(@Param("map")Map<String, Object> map);
	
	
	/**
	 * 拿到一个小时的发送数量
	 * @param map
	 * @return
	 */
	int getPeriodCount(@Param("map")Map<String, Object> map);


	/**
	 * 短信验证码发送
	 * @param verifyRecordMap
	 * @return
	 */
//	@Insert("insert into t_verify_record(user_id,user_name,bus_type,acc_type,account,code,`status`,create_time,send_time) " +
//			"values(#{map.user_id},#{map.user_name},#{map.bus_type},#{map.acc_type},#{map.account},#{map.status},#{map.create_time},#{map.send_time})")
	int addVerifyRecord(@Param("map")Map<String, Object> verifyRecordMap);

}
