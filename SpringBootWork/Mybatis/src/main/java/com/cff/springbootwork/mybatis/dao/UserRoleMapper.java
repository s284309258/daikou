package com.cff.springbootwork.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cff.springbootwork.mybatis.domain.UserRole;

@Mapper
public interface UserRoleMapper {
    @Insert({"<script> ",
        "INSERT INTO user_role",
        "( phone,",
        "userName ,",
        "role",
         ") ",
        " values ",
         "( #{phone},",
         "#{userName},",
         "#{role}",
        " ) ",
         "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int saveTest(UserRole userRole);

    @Select({
        "<script>",
            "SELECT ",
            "id as id,",
            "userName as userName,",
            "role as role",
            "FROM user_role",
       "</script>"})
    List<UserRole> selectAll();

    @Update({
        "<script>",
        " update user_role set",
        " phone = #{phone, jdbcType=VARCHAR}",
        " where id=#{id}",
        "</script>"
    })
    int update(@Param("id") Integer id, @Param("phone") String phone);

    @Delete({
        "<script>",
        " delete from user_role",
        " where id=#{id}",
        "</script>"
    })
    int delete(@Param("id") Integer id);
}
