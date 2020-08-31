package com.cff.springbootwork.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cff.springbootwork.mybatis.domain.UserInfo;

@Mapper
public interface UserInfoDao {
	@Select({
		"<script>",
	        "SELECT ",
	        "user_name as userName,passwd,name,mobile,valid, user_type as userType",
	        "FROM user_info",
	        "WHERE user_name = #{userName,jdbcType=VARCHAR}",
	   "</script>"})
	UserInfo findByUserName(@Param("userName") String userName);
	
	@Select({
		"<script>",
	        "SELECT ",
	        "user_name as userName,passwd,name,mobile,valid, user_type as userType",
	        "FROM user_info",
	        "WHERE mobile = #{mobile,jdbcType=VARCHAR}",
            "<if test='userType != null and userType != \"\" '> and user_type = #{userType, jdbcType=VARCHAR} </if>",
	   "</script>"})
	List<UserInfo> testIfSql(@Param("mobile") String mobile,@Param("userType") String userType);
	
	@Select({
		"<script>",
			"SELECT ",
	        "user_name as userName,passwd,name,mobile,valid, user_type as userType",
	         "     FROM user_info ",
	         "    WHERE mobile IN (",
	         "   <foreach collection = 'mobileList' item='mobileItem' index='index' separator=',' >",
	         "      #{mobileItem}",
	         "   </foreach>",
	         "   )",
	    "</script>"})
	List<UserInfo> testForeachSql(@Param("mobileList") List<String> mobile);
	
	@Update({
		"<script>",
			"	UPDATE user_info",
	        "   SET ",
			"	<choose>",
			"	<when test='userType!=null'> user_type=#{user_type, jdbcType=VARCHAR} </when>",
			"	<otherwise> user_type='0000' </otherwise>",
			"	</choose>",
	        "  	WHERE user_name = #{userName, jdbcType=VARCHAR}",
	  	"</script>" })
	int testUpdateWhenSql(@Param("userName") String userName,@Param("userType") String userType);
	
	@Select({
		"<script>",
	 		"<bind name=\"tableName\" value=\"item.getIdentifyTable()\" />",
	 		"SELECT ",
	        "user_name as userName,passwd,name,mobile,valid, user_type as userType",
	        "FROM ${tableName}",
	        "WHERE mobile = #{item.mobile,jdbcType=VARCHAR}",
	   "</script>"})
	public List<UserInfo> testBindSql(@Param("item") UserInfo userInfo);
	
	@Select({
		"<script>",
	 		"<bind name=\"startNum\" value=\"page*pageSize\" />",
	 		"SELECT ",
	        "user_name as userName,passwd,name,mobile,valid, user_type as userType",
	        "FROM user_info",
	        "ORDER BY mobile ASC",
	        "LIMIT #{pageSize} OFFSET #{startNum}",
	   "</script>"})
	public List<UserInfo> testPageSql(@Param("page") int page, @Param("pageSize") int size);
	
	@Select({
		"<script>",
	 		"SELECT ",
	        "user_name as userName,passwd,name,mobile,valid, user_type as userType",
	        "FROM user_info",
	        "<trim prefix=\" where \" prefixOverrides=\"AND\">",
            	"<if test='item.userType != null and item.userType != \"\" '> and user_type = #{item.userType, jdbcType=VARCHAR} </if>",
            	"<if test='item.mobile != null and item.mobile != \"\" '> and mobile = #{item.mobile, jdbcType=VARCHAR} </if>",
	        "</trim>",
	   "</script>"})
	public List<UserInfo> testTrimSql(@Param("item") UserInfo userInfo);
	
	@Update({
		"<script>",
			"	UPDATE user_info",
	        "   <set> ",
	        "<if test='item.userType != null and item.userType != \"\" '>user_type = #{item.userType, jdbcType=VARCHAR}, </if>",
        	"<if test='item.mobile != null and item.mobile != \"\" '> mobile = #{item.mobile, jdbcType=VARCHAR} </if>",
			" 	</set>",
	        "  	WHERE user_name = #{item.userName, jdbcType=VARCHAR}",
	  	"</script>" })
	public int testSetSql(@Param("item") UserInfo userInfo);
}
