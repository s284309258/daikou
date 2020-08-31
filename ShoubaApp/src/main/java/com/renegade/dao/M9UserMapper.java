package com.renegade.dao;

import com.renegade.domain.M9UserAndOrderDo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface M9UserMapper {

	int register(@Param("map") Map<String, Object> map);

	List<Map<String, Object>> getUserOrderList(@Param("map") Map<String, Object> map);

	List<M9UserAndOrderDo> getUserOrderListObj(@Param("map") Map<String, Object> map);

	List<Map<String, Object>> selectUserList(@Param("map") Map<String, Object> map);

	int updateUser(@Param("map") Map<String, Object> map);

	@Select("select * from m9_user where share_rqcode = #{userPid}")
	Map<String, Object> findUserByCode(String userPid);

	@Select("select * from m9_user where id = #{userId}")
	Map<String, Object> findUserByuserId(String userId);

	int insert_change_log(@Param("map") Map<String, Object> params);

	List<Map<String, Object>> select_flowing_record(@Param("map") Map<String, Object> map);

	@Select("select * from m9_user where telphone = #{phone}")
	Map<String, Object> findUserByuPhone(String phone);

	@Update("update m9_user set login_pwd = #{newPass} where telphone = #{phone}")
	int updateFrogetPass(Map<String, Object> map);

	@Select("SELECT COUNT(user_limit =1 or null) as num, COUNT(user_limit =0  or null)as num1 FROM m9_user WHERE user_pid = #{userId}")
	Map<String, Object> countNum(String userId);

	int updateAddCoin(Map<String, Object> map);

	int updateReduceCoin(Map<String, Object> map);

	List<Map<String, Object>> select_flowing_record_light(@Param("map") Map<String, Object> map);

	@Select("<script>" 
	        + " select * from m9_user where user_pid = #{userId} "
			+ "<if test='lastId!=null and lastId != 0 '>" 
			+ "and id &lt; #{lastId} " 
			+ "</if>"
			+ " order by id desc  limit 10 "
			+ " </script>")
	List<Map<String, Object>> findDirectUser(@Param("userId") String userId, @Param("lastId") Integer lastId);

	@Update("insert into t_sign_record(sign_date,prize,user_id) values(CURRENT_DATE,#{prize},#{user_id})")
	int insert_sign_record(Map<String, Object> map);

	@Update("update m9_user set user_lineup_coin_free=user_lineup_coin_free+${user_lineup_coin_free} where telphone=#{telphone}")
	int update_register_user_coin_free(Map<String,Object> map);


	List<Map<String,Object>> select_sign_record(@Param("map") Map<String, Object> map);

}
