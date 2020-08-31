package com.renegade.dao.yilian;

import com.renegade.domain.yilian.YlStaffInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface YilianMapper {

    @Select("select * from yl_staff_info where login_name=#{map.login_name} and login_pwd=#{map.login_pwd}")
    @ResultType(YlStaffInfo.class)
    YlStaffInfo login(@Param("map") Map<String,Object> map);


    @Select("select * from yl_user_order where approve1='1' and approve2='2' order by apply_datetime desc")
    List<Map<String,Object>> orderQuery();

    @Select("select * from yl_user_order where approve1='2' and org_no=#{orgNo} order by apply_datetime desc")
    List<Map<String,Object>> orderQueryOrgNo(@Param("orgNo") Long orgNo);

    @Insert("insert into yl_user_info(real_name,user_card,user_tel,bank_account,bank_name,amount,stage,stage_amount,staff_no,staff_name,staff_tel,org_no,org_name,org_agent,org_agent_name,card_poto1,card_poto2,acc_poto1,acc_poto2,person_poto,state) " +
            " values(#{map.real_name},#{map.user_card},#{map.user_tel},#{map.bank_account},#{map.bank_name},#{map.amount},#{map.stage},#{map.stage_amount},#{map.staff_no}," +
            " #{map.staff_name},#{map.staff_tel},#{map.org_no},#{map.org_name},#{map.org_agent},#{map.org_agent_name},#{map.card_poto1},#{map.card_poto2},#{map.acc_poto1},#{map.acc_poto2},#{map.person_poto},1)")
    @Options(useGeneratedKeys=true,keyColumn="id",keyProperty = "map.id")
    int insertUser(@Param("map") Map<String,Object> map);

    int insertUser12(@Param("map") Map<String,Object> map);

    @Select("select code from t_verify_record where user_id=#{user_id} and bus_type='checkInUser' and account=#{user_tel} and create_time>DATE_ADD(now(),interval -1 HOUR)")
    List<String> checkSmsCode(@Param("user_id") Long user_id,@Param("user_tel") String user_tel);

    @Select("select code from t_verify_record where bus_type='forgetPwd' and account=#{user_tel} and create_time>DATE_ADD(now(),interval -1 HOUR)")
    List<String> checkForgetCode(@Param("user_tel") String user_tel);

    @Update("update yl_user_info set sms_flag=#{map.sms_flag},sms_id=#{map.sms_id},merch_order_id=#{map.merch_order_id} where id=#{map.user_id}")
    int updateUser(@Param("map") Map<String,Object> map);

    @Update("update yl_staff_info set login_pwd=#{login_pwd} where login_name=#{login_name} and user_tel=#{user_tel}")
    int updateStaff(@Param("login_name") String login_name,@Param("login_pwd") String login_pwd,@Param("user_tel") String user_tel);

    @Insert("insert into yl_user_order(real_name,user_tel,user_card,verfiy_code,bank_account,bank_name,amount,merch_order_id," +
            " staff_no,staff_name,staff_tel,org_no,org_name,org_agent,org_agent_name,sms_flag,sms_id,user_id,create_time) " +
            " values(#{map.real_name},#{map.user_tel},#{map.user_card},#{map.verfiy_code},#{map.bank_account},#{map.bank_name},#{map.amount},#{map.merch_order_id}," +
            " #{map.staff_no},#{map.staff_name},#{map.staff_tel},#{map.org_no},#{map.org_name},#{map.org_agent},#{map.org_agent_name}," +
            " #{map.sms_flag},#{map.sms_id},#{map.id},#{map.create_time})")
    @Options(useGeneratedKeys=true,keyColumn="id",keyProperty = "map.id")
    int insertOrder(@Param("map") Map<String,Object> map);

    @Select("select * from yl_user_info where id=#{user_id}")
    Map<String,Object> userDetail(@Param("user_id") String user_id);

    @Select("<script>" +
            "select * from yl_user_info where 1=1" +
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
            "</script>")
    List<Map<String,Object>> openRecordAdmin(@Param("real_name") Object real_name);

    @Select("<script>" +
            "select * from yl_user_info where staff_no=#{staff_no}"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
            "</script>")
    List<Map<String,Object>> openRecordStaff(@Param("staff_no") Long staff_no,@Param("real_name") Object real_name);

    @Select("<script>" +
            "select * from yl_user_info where org_no=#{org_no}"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
            "</script>")
    List<Map<String,Object>> openRecordOrg(@Param("org_no") Long org_no,@Param("real_name") Object real_name);



    @Select("<script>" +
            "select * from yl_user_order where 1=1"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
            " order by apply_datetime desc"+
            "</script>")
    List<Map<String,Object>> deductRecordAdmin(@Param("real_name") Object real_name);

    @Select("<script>" +
            "select * from yl_user_order where staff_no=#{staff_no}"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('%',#{real_name},'%')"+
            "</if>"+
            " order by apply_datetime desc"+
            "</script>")
    List<Map<String,Object>> deductRecordStaff(@Param("staff_no") Long staff_no,@Param("real_name") Object real_name);

    @Select("<script>" +
            "select * from yl_user_order where org_no=#{org_no}"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('%',#{real_name},'%')"+
            "</if>"+
            " order by apply_datetime desc"+
            "</script>")
    List<Map<String,Object>> deductRecordOrg(@Param("org_no") Long org_no,@Param("real_name") Object real_name);
    
    
    @Select("<script>" +
            "select * from yl_user_order where org_agent=#{org_no}"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('%',#{real_name},'%')"+
            "</if>"+
            " order by apply_datetime desc"+
            "</script>")
    List<Map<String,Object>> deductRecordAgent(@Param("org_no") Long org_no,@Param("real_name") Object real_name);


    @Select("select * from yl_user_info where staff_no=#{staff_id}")
    List<Map<String,Object>> stafTeam(@Param("staff_id") String staff_id);
    
    
    /////////////////////////////////////////////////
    
    @Select("<script>" +
    		"select * from yl_user_info where 1=1"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
    		"</script>")
    List<Map<String,Object>> withholdUserInfoAdmin(@Param("real_name") Object real_name);
    
    @Select("<script>"+
    		"select * from yl_user_info where staff_no=#{staff_no}"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
    		"</script>")
    List<Map<String,Object>> withholdUserInfoStaff(@Param("staff_no") Long staff_no,@Param("real_name") Object real_name);
    
    @Select("<script>"+
    		"select * from yl_user_info where org_no=#{org_no}"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
    		"</script>")
    List<Map<String,Object>> withholdUserInfoOrg(@Param("org_no") Long org_no,@Param("real_name") Object real_name);

    
    
    
    @Select("<script>" +
    		"select * from yl_user_order where 1=1"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('%',#{real_name},'%')"+
            "</if>"+
            " order by apply_datetime desc"+
    		"</script>")
    List<Map<String,Object>> deductOrderRecordAdmin(@Param("real_name") Object real_name);
    
    
    @Select("<script>"+
    		"select * from yl_user_order where staff_no=#{staff_no}"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('%',#{real_name},'%')"+
            "</if>"+
            " order by apply_datetime desc"+
    		"</script>")
    List<Map<String,Object>> deductOrderRecordStaff(@Param("staff_no") Long staff_no,@Param("real_name") Object real_name);
    
    
    @Select("<script>"+
    		"select * from yl_user_order where org_no=#{org_no}"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('',#{real_name},'')"+
            "</if>"+
            " order by apply_datetime desc"+
    		"</script>")
    List<Map<String,Object>> deductOrderRecordOrg(@Param("org_no") Long org_no,@Param("real_name") Object real_name);


    @Select("<script>"+
            "select * from yl_user_order where agent_no=#{agent_no}"+
            "<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name like concat('',#{real_name},'')"+
            "</if>"+
            " order by apply_datetime desc"+
            "</script>")
    List<Map<String,Object>> deductOrderRecordAgent(@Param("agent_no") Long agent_no,@Param("real_name") Object real_name);

    
    
    @Select("<script>" +
    		"select * from yl_staff_info where 1=1"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
    		"</script>")
    List<Map<String,Object>> staffInfoAdmin(@Param("real_name") Object real_name);
    
    @Select("<script>"+
    		"select * from yl_staff_info where org_no=#{org_no}"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
            " and real_name=#{real_name}"+
            "</if>"+
    		"</script>")
    List<Map<String,Object>> staffInfoOrg(@Param("org_no") Long org_no,@Param("real_name") Object real_name);
    
    @Select("select * from yl_user_info where id=#{user_id}")
    Map<String,Object> withholdUserDetail(@Param("user_id") String user_id);
    
    @Select("select * from yl_user_order where id=#{order_id}")
    Map<String,Object> orderDatil(@Param("order_id") String order_id);

    @Select("select * from yl_user_order where merch_order_id=#{merch_order_id}")
    Map<String,Object> orderDetailMerchNo(@Param("merch_order_id") String merch_order_id);

    @Update("update yl_user_info set sign_poto=#{imageBase64} where id=#{user_id}")
    int signSave(@Param("user_id") String user_id,@Param("imageBase64") String imageBase64);

    @Update("update yl_user_order set approve1=1 where id=#{order_id}")
    int agreeDeductOne(@Param("order_id") String order_id);

    @Update("update yl_user_order set approve1=0,approve1_mark=#{remark},status='90' where id=#{order_id}")
    int refuseDeductOne(@Param("order_id") String order_id,@Param("remark") String remark);

    @Update("update yl_user_order set approve2=0,approve1_mark=#{remark},status='90' where id=#{order_id}")
    int refuseDeductTwo(@Param("order_id") String order_id,@Param("remark") String remark);

    @Update("update yl_user_order set approve2=1 where id=#{order_id}")
    int agreeDeductTwo(@Param("order_id") String order_id);

    @Update("update yl_user_order set paytime=#{map.paytime},status=#{map.status},settledate=#{map.settledate} where id=#{map.order_id}")
    int updateOrder(@Param("map") Map<String,Object> map);


    @Select("<script>" +
            "select * from yl_user_order where approve1=1 " +
            " and approve2=2"+
            "</script>")
    List<Map<String,Object>> withholdAuditAdmin();


    @Select("<script>" +
            "select * from yl_user_order where approve1=2"+
            " and org_no=#{org_no}"+
            "</script>")
    List<Map<String,Object>> withholdAuditOrg(@Param("org_no") Long org_no);


    @Select("select * from yl_companies_account where org_no=#{org_no}")
    Map<String,Object> bankAccDetail(@Param("org_no") Object org_no);

    @Select("select config_value from sys_config where config_key=#{config_key}")
    @ResultType(String.class)
    String getSysConfig(@Param("config_key") String config_key);
    
    
    @Select("<script>" +
    		"select * from yl_staff_info where role='agent'"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
    		" and org_name like concat('%',#{real_name},'%')"+
    		"</if>"+
    		"</script>")
    List<Map<String,Object>> orgQuery(@Param("real_name") String real_name);
    
    
    @Select("select * from yl_staff_info where id=#{org_id}")
    Map<String,Object> orgDetail(@Param("org_id") String org_id);
    
    
    @Select("<script>" +
    		"select * from yl_staff_info where role='companies' and create_by=#{create_by}"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
    		" and org_name like concat('%',#{real_name},'%')"+
    		"</if>"+
    		"</script>")
    List<Map<String,Object>> companies(@Param("org_no") String org_no,@Param("create_by") Long create_by,@Param("real_name") String real_name);


    @Select("<script>" +
    		"select * from yl_staff_info where role='companies'"+
    		"<if test='real_name!=null and real_name!=\"\"'>" +
    		" and (org_name like concat('%',#{real_name},'%') or org_agent_name like concat('%',#{real_name},'%'))"+
    		"</if>"+
    		"</script>")
    List<Map<String,Object>> allCompanies(@Param("real_name") String real_name);


    @Insert("insert into yl_staff_info(real_name,user_card,user_tel,login_name,login_pwd,org_no,org_name,org_agent,org_agent_name,role,org_address,create_by)" +
            " values(#{map.real_name},#{map.user_card},#{map.user_tel},#{map.login_name},#{map.login_pwd},#{map.org_no},#{map.org_name},#{map.org_agent},#{map.org_agent_name},#{map.role},#{map.org_address},#{map.create_by})")
    int openStaff(@Param("map") Map<String,Object> map);
    
    
    @Select("select * from yl_staff_info where org_no=#{comp_id}")
    List<Map<String,Object>> salerQuery(@Param("comp_id") String comp_id);
    
    
    @Select("select * from yl_staff_info where org_no=#{comp_id} and real_name like concat('%',#{real_name},'%')")
    List<Map<String,Object>> salerQueryName(@Param("comp_id") String comp_id,@Param("real_name") String real_name);
    
    
    @Select("select * from yl_staff_info where id=#{comp_id}")
    Map<String,Object> companyDetail(@Param("comp_id") String comp_id);
    
    
    @Update("update yl_user_order set orderId=#{OrderId},status=#{Status},paytime=DATE_FORMAT(#{PayTime},'%Y-%m-%d %T'),settledate=#{SettleDate},notify_state=#{notify_state} "
    		+ " where merch_order_id=#{MerchOrderId}")
    int payNotify(@Param("MerchOrderId") String MerchOrderId,@Param("OrderId") String OrderId,@Param("Status") String Status,
    		@Param("PayTime") String PayTime,@Param("SettleDate") String SettleDate,@Param("notify_state") int notify_state);
    
    
    @Insert("insert into yl_staff_info(real_name,user_tel,login_name,login_pwd,org_no,org_name,org_address,org_agent,org_agent_name,create_by,role) "
    		+ " values(#{map.real_name},#{map.user_tel},#{map.login_name},#{map.login_pwd},#{map.org_no},#{map.org_name},#{map.org_address},#{map.org_agent},#{map.org_agent_name},#{map.create_by},'agent')")
    int openAgentSubmit(@Param("map") Map<String,Object> map);
    
    
    @Insert("insert into yl_staff_info(real_name,user_tel,login_name,login_pwd,org_no,org_name,org_address,create_by,role,org_agent,org_agent_name,photo) "
    		+ " values(#{map.real_name},#{map.user_tel},#{map.login_name},#{map.login_pwd},#{map.org_no},#{map.org_name},#{map.org_address},#{map.create_by},'companies',#{map.org_agent},#{map.org_agent_name},#{map.photo})")
    int openCompanySubmit(@Param("map") Map<String,Object> map);

    @Insert("insert into yl_pay_order(batch_no,user_name,sn,acc_no,acc_name,acc_province,acc_city,amount,acc_prop,id_no,mer_order_no,mer_seq_no," +
            " pay_state,sett_date,success_date,staff_no,staff_name,staff_tel,org_no,org_name,org_agent,org_agent_name,remark) " +
            " values (#{batch_no},#{user_name},#{sn},#{acc_no},#{acc_name},#{acc_province},#{acc_city},#{amount},#{acc_prop}," +
            " #{id_no},#{mer_order_no},#{mer_seq_no},#{pay_state},#{sett_date},#{success_date},#{staff_no},#{staff_name},#{staff_tel},#{org_no},#{org_name},#{org_agent},#{org_agent_name},#{remark})")
    int insertPayOrder(@Param("batch_no") String batch_no,@Param("user_name") String user_name,@Param("sn") String sn,@Param("acc_no") String acc_no,@Param("acc_name") String acc_name,
                       @Param("acc_province") String acc_province,@Param("acc_city") String acc_city,@Param("amount") String amount,@Param("acc_prop") String acc_prop,@Param("id_no") Object id_no,
                       @Param("mer_order_no") String mer_order_no,@Param("mer_seq_no") String mer_seq_no,@Param("pay_state") String pay_state,@Param("sett_date") String sett_date,@Param("success_date") String success_date,
                       @Param("staff_no") Object staff_no,@Param("staff_name") Object staff_name,@Param("staff_tel") Object staff_tel,@Param("org_no") Object org_no,@Param("org_name") Object org_name,
                       @Param("org_agent") Object org_agent,@Param("org_agent_name") Object org_agent_name,@Param("remark") String remark);


    @Insert("insert into yl_companies_account(acc_name,acc_no,acc_province,acc_city,org_no) values(#{acc_name},#{acc_no},#{acc_province},#{acc_city},#{org_no})")
    int insertCompanyAccount(@Param("acc_name") String acc_name,@Param("acc_no") String acc_no,@Param("acc_province") String acc_province,@Param("acc_city") String acc_city,@Param("org_no") int org_no);
}
