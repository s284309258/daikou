package com.cff.springbootwork.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeductMapper {
    @Select("select * from yl_user_order where approve1='1' and approve2='2'")
    List<Map<String,Object>> orderQuery();

    @Select("select * from yl_user_order where approve1='2' and org_no=#{orgNo}")
    List<Map<String,Object>> orderQueryOrgNo(@Param("orgNo") Long orgNo);
}
