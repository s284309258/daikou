package com.cff.springbootwork.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StaffMapper {

    @Select("select * from yl_staff_info where login_name=#{login_name} and login_pwd=#{login_pwd}")
    Map<String,Object> login(@Param("login_name") String userName, @Param("login_pwd") String password);

    @Select("select * from yl_user_info")
    List<Map<String,Object>> withholdUserInfo();

    @Select("select * from yl_user_order")
    List<Map<String,Object>> deductOrderRecord();

    @Select("select * from yl_staff_info")
    List<Map<String,Object>> staffInfo();

    @Select("select * from yl_user_info where id=#{user_id}")
    Map<String,Object> withholdUserDetail(@Param("user_id") String user_id);
}
