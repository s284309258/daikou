package com.renegade.service.yilian;

import com.merchant.demo.client.TransactionClientSmsApi;
import com.merchant.util.Xml;
import com.renegade.config.AjaxJson;
import com.renegade.config.MD5;
import com.renegade.config.PhoneSendCode;
import com.renegade.dao.VerifyRecordMapper;
import com.renegade.dao.yilian.YilianMapper;
import com.renegade.domain.yilian.YlStaffInfo;
import com.renegade.domain.yilian.dto.ResultModel;
import com.renegade.domain.yilian.dto.domain.User;
import com.renegade.util.common.FileUpload;
import com.renegade.util.common.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class YilianService {

    private static Logger logger = Logger.getLogger(YilianService.class.getName());

    @Autowired
    private YilianMapper yilianMapper;

    @Autowired
    private VerifyRecordMapper verifyRecordMapper;
    
    @Autowired
	UserServiceImpl userService;

    public ResultModel login(Map<String, Object> map){
        try {
            if (StringUtils.isEmpty(String.valueOf(map.get("login_name")))) {
                return ResultModel.error("用户名不能为空");
            }
            if (StringUtils.isEmpty(String.valueOf(map.get("login_pwd")))) {
                return ResultModel.error("密码不能为空");
            }
            YlStaffInfo ylStaffInfo = yilianMapper.login(map);
            if (ylStaffInfo==null) {
                return ResultModel.error("用户名或密码错误");
            }
            return ResultModel.ok(ylStaffInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("系统异常");
        }
    }

    public ResultModel orderQuery(YlStaffInfo ylStaffInfo){
        List<Map<String,Object>> list = new ArrayList<>();
        if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.orderQueryOrgNo(ylStaffInfo.getOrgNo());
        }else if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.orderQuery();
        }
        return ResultModel.ok(list);
    }

    public ResultModel checkSmsCode(Long user_id,String user_tel,String verCode){
        List<String> code = yilianMapper.checkSmsCode(user_id,user_tel);
        if(code==null){
            return ResultModel.error("验证码已失效(有效期一小时)");
        }
        for(String codeItem: code){
            if(verCode.equals(codeItem)){
                return ResultModel.ok();
            }
        }
        return ResultModel.error("验证码不正确");
    }

    public String insertUser(Map<String,Object> userMap,MultipartFile card_poto1,MultipartFile card_poto2,
                                  MultipartFile acc_poto1,MultipartFile acc_poto2,MultipartFile person_poto,YlStaffInfo ylStaffInfo){
        AjaxJson card1 = FileUpload.uploadImg(card_poto1);
        AjaxJson card2 = FileUpload.uploadImg(card_poto2);
        AjaxJson acc1 = FileUpload.uploadImg(acc_poto1);
        AjaxJson acc2 = FileUpload.uploadImg(acc_poto2);
        AjaxJson person = FileUpload.uploadImg(person_poto);
        if(!card1.isSuccess()) return "/error";
        if(!card2.isSuccess()) return "/error";
        if(!acc1.isSuccess()) return "/error";
        if(!acc2.isSuccess()) return "/error";
        if(!person.isSuccess()) return "/error";
        if("".equals(userMap.get("stage"))){
            userMap.put("stage","0");
        }
        if("".equals(userMap.get("stage_amount"))){
            userMap.put("stage_amount","0");
        }
        if("".equals(userMap.get("amount"))){
            userMap.put("amount","0");
        }
        userMap.put("card_poto1",card1.getData().toString());
        userMap.put("card_poto2",card2.getData().toString());
        userMap.put("acc_poto1",acc1.getData().toString());
        userMap.put("acc_poto2",acc2.getData().toString());
        userMap.put("person_poto",person.getData().toString());
        userMap.put("staff_no",ylStaffInfo.getId());
        userMap.put("staff_name",ylStaffInfo.getRealName());
        userMap.put("staff_tel",ylStaffInfo.getUserTel());
        userMap.put("org_no",ylStaffInfo.getOrgNo());
        userMap.put("org_name",ylStaffInfo.getOrgName());
        userMap.put("org_agent",ylStaffInfo.getOrgAgent());
        userMap.put("org_agent_name",ylStaffInfo.getOrgAgentName());
        userMap.put("id",0);
        int cnt = yilianMapper.insertUser(userMap);
        System.out.println(userMap.get("id"));
        return "/staff/sign";
    }

    public Map<String,Object> userDetail(YlStaffInfo ylStaffInfo,String user_id){
        return yilianMapper.userDetail(user_id);
    }

    public ResultModel sendCodeWithHold(YlStaffInfo ylStaffInfo,String user_id){
        try{
            Map<String,Object> map = yilianMapper.userDetail(user_id);
            String merchOrderId = StringUtil.generateOrderId("FYHDKMERONO");
            String tradeTime = StringUtil.getSysDateTimeYMD();
            String mobileNo = map.get("user_tel").toString();

            // 短信参数，格式：姓名|证件类型|证件号码|银行卡号|行业编号|交易金额  （正确数据且必填）
            String real_name = map.get("real_name").toString();
            String user_card = map.get("user_card").toString();
            String bank_account = map.get("bank_account").toString();
            String smParam = real_name+"||"+user_card+"|"+bank_account+"||";
            //测试测试测试测试
//            smParam = "全渠道||341126197709218366|6216261000000000018||";
            //测试测试测试测试
            Xml retXml = new Xml();
            TransactionClientSmsApi.apiSmsCodeCheck(merchOrderId,
                    tradeTime, mobileNo, smParam, retXml);
            if("0000".equals(retXml.getRetCode())){

                String smId = retXml.getSmId();
                String smsFlag =retXml.getSmsFlag();
                merchOrderId = retXml.getMerchOrderId();
                Map<String,Object> params = new HashMap<>();
                params.put("sms_flag",smsFlag);
                params.put("sms_id",smId);
                params.put("user_id",user_id);
                params.put("merch_order_id",merchOrderId);
                yilianMapper.updateUser(params);
            }else{
                return ResultModel.error(retXml.getRetMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultModel.ok("短信验证码发送成功");
    }

    public List<Map<String,Object>> withholdAudit(YlStaffInfo ylStaffInfo){

        List<Map<String,Object>> list = new ArrayList<>();
        if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.withholdAuditAdmin();
        }else if("staff".equals(ylStaffInfo.getRole())){
//            list = yilianMapper.withholdAuditStaff(ylStaffInfo.getId());
        }else if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.withholdAuditOrg(ylStaffInfo.getOrgNo());
        }
        return list;
    }

    public ResultModel withhold(YlStaffInfo ylStaffInfo,String user_id,String verfiy_code){
        Map<String,Object> map = yilianMapper.userDetail(user_id);
        map.put("verfiy_code",verfiy_code);
        yilianMapper.insertOrder(map);
        sendNoticeSmsUp(ylStaffInfo,"221820");
        return ResultModel.ok("已发起扣款,等待审核");
    }

    public ResultModel withholdTest(YlStaffInfo ylStaffInfo,String user_id,String verfiy_code){
        Map<String,Object> map = yilianMapper.userDetail(user_id);
        map.put("verfiy_code",verfiy_code);
        map.put("amount","1");
        map.put("id","0");
        yilianMapper.insertOrder(map);
        return agreeDeductTwo(String.valueOf(map.get("id")));
    }

    public List<Map<String,Object>> openRecord(YlStaffInfo ylStaffInfo,Map<String,Object> map){
        List<Map<String,Object>> list = new ArrayList<>();
        if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.openRecordAdmin(map.get("real_name"));
        }else if("staff".equals(ylStaffInfo.getRole())){
            list = yilianMapper.openRecordStaff(ylStaffInfo.getId(),map.get("real_name"));
        }else if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.openRecordOrg(ylStaffInfo.getOrgNo(),map.get("real_name"));
        }
        return list;
    }

    public List<Map<String,Object>> deductRecord(YlStaffInfo ylStaffInfo,Map<String,Object> map){
        List<Map<String,Object>> list = new ArrayList<>();
        if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductRecordAdmin(map.get("real_name"));
        }else if("staff".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductRecordStaff(ylStaffInfo.getId(),map.get("real_name"));
        }else if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductRecordOrg(ylStaffInfo.getOrgNo(),map.get("real_name"));
        }else if("agent".contentEquals(ylStaffInfo.getRole())) {
        	list = yilianMapper.deductRecordAgent(ylStaffInfo.getOrgNo(), map.get("real_name"));
        }
        return list;
    }

    public List<Map<String,Object>> staffTeam(YlStaffInfo ylStaffInfo,String staff_id){
        return yilianMapper.stafTeam(staff_id);
    }

    public int agreeDeductOne(YlStaffInfo ylStaffInfo, String order_id){
        try{
            Map<String, Object> map = yilianMapper.companyDetail("11");
            PhoneSendCode.sendCode(String.valueOf(map.get("user_tel")),"221820",1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return yilianMapper.agreeDeductOne(order_id);
    }

    public int refuseDeductOne(YlStaffInfo ylStaffInfo, String order_id,String remark){
        try{
            Map<String,Object> map = yilianMapper.orderDatil(order_id);
            String staff_tel = String.valueOf(map.get("staff_tel"));
            sendNoticeSmsUnder(ylStaffInfo,"221896",staff_tel);
        }catch (Exception e){
            e.printStackTrace();
        }
        return yilianMapper.refuseDeductOne(order_id,remark);
    }

    public int refuseDeductTwo(YlStaffInfo ylStaffInfo, String order_id,String remark){
        try{
            Map<String,Object> map = yilianMapper.orderDatil(order_id);
            String org_name = map.get("org_name").toString();
            List<Map<String,Object>> list = yilianMapper.allCompanies(org_name);
            Map<String,Object> map1 = list.get(0);
            String user_tel = map1.get("user_tel").toString();
            sendNoticeSmsUnder(ylStaffInfo,"221896",user_tel);
        }catch (Exception e){
            e.printStackTrace();
        }
        return yilianMapper.refuseDeductTwo(order_id,remark);
    }

    public void sendNoticeSmsUp(YlStaffInfo ylStaffInfo,String template){
        try{
            Map<String,Object> map1 = yilianMapper.companyDetail(String.valueOf(ylStaffInfo.getId()));
            String create_by = String.valueOf(map1.get("create_by"));
            Map<String,Object> map2 =  yilianMapper.companyDetail(String.valueOf(create_by));
            String user_tel = String.valueOf(map2.get("user_tel"));
            PhoneSendCode.sendCode(user_tel,template,1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sendNoticeSmsUnder(YlStaffInfo ylStaffInfo,String template,String user_tel){
        try{
            PhoneSendCode.sendCode(user_tel,template,1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultModel agreeDeductTwo(String order_id){
        Map<String,Object> mm = yilianMapper.orderDatil(order_id);
        // 行业
        String industryId = "";
        String merchOrderId = mm.get("merch_order_id").toString();
        String amount = mm.get("amount").toString();
        String user_tel = mm.get("user_tel").toString();
        String real_name = mm.get("real_name").toString();
        String user_card = mm.get("user_card").toString();
        String bank_account = mm.get("bank_account").toString();
        String orderDesc = "通用商户充值";
        String tradeTime = StringUtil.getSysDateTimeYMD();
        String expTime="";
        String extData="商户充值";
        String miscData=user_tel+"|||"+real_name+"|"+user_card+"|"+bank_account+"|||||||||";
        //测试测试测试测试
//        miscData = "13552535506|||全渠道|341126197709218366|6216261000000000018|||||||||";//"||||||||";
        //测试测试测试测试
        String notifyFlag="0";
        String smId="";
        String smCode="";
        String pwd="";
        Xml retXml = new Xml();
        if("1".equals(mm.get("sms_flag").toString())){
            smId = mm.get("sms_id").toString();
            smCode = mm.get("verfiy_code").toString();
        }
        try{
            //代扣,商户充值**********************************
            TransactionClientSmsApi.apiPay(industryId,merchOrderId,amount,orderDesc,tradeTime,expTime,extData,miscData,notifyFlag,smId,smCode,pwd,retXml);
            if(!"0000".equals(retXml.getRetCode())){
                return ResultModel.error(retXml.getRetMsg());
            }
//            String paytime = retXml.getPayTime();
//            String status = retXml.getStatus();
//            String settledate = retXml.getSettleDate();
//            Map<String,Object> map = new HashMap<>();
//            map.put("paytime",paytime);
//            map.put("status",status);
//            map.put("settledate",settledate);
//            map.put("order_id",order_id);
//            yilianMapper.updateOrder(map);
            //代扣,商户充值**********************************


            //批量代付****************************
//            Map<String,Object> bankAcc = yilianMapper.bankAccDetail(mm.get("org_no"));
//            String user_name = mm.get("user_name").toString();
//            String sn = StringUtil.generateOrderId("FYHDFSN");
//            String acc_no = bankAcc.get("acc_no").toString();
//            String acc_name = bankAcc.get("acc_name").toString();
//            String amount1 = mm.get("amount").toString();
//            String acc_province = bankAcc.get("acc_province").toString();
//            String acc_city = bankAcc.get("acc_city").toString();
//            String acc_prop = bankAcc.get("acc_prop").toString();
//            String mer_order_no = StringUtil.generateOrderId("FYHDFMERONO");
//            logger.info("Service2.pay代付接口请求参数user_name="+user_name+"&sn="+sn+"&acc_no="+acc_no+"&acc_name="+acc_name+"&amount="+amount1+
//                    "&acc_province="+acc_province+"&acc_city="+acc_city+"&acc_prop="+acc_prop+"&mer_order_no="+mer_order_no);
//            Service2.pay(user_name,sn,acc_no,acc_name,amount1,acc_province,acc_city,acc_prop,mer_order_no);
            //批量代付****************************

            //更新扣款状态*************************
            yilianMapper.agreeDeductTwo(order_id);
            //更新扣款状态*************************
        }catch (Exception e){
            e.printStackTrace();
            return ResultModel.error("系统异常");
        }
        return ResultModel.ok("已发起扣款,请查看扣款记录");
    }


    public ResultModel sendCode(YlStaffInfo ylStaffInfo,String phone,String bus_type){
        String code = PhoneSendCode.getCode();
        String result= PhoneSendCode.sendCode(phone,code);
        if("success".equals(result)){
            Map<String,Object>map = new HashMap<>();
            if(ylStaffInfo!=null){
                map.put("user_name",ylStaffInfo.getRealName());
                map.put("user_id",ylStaffInfo.getId());
            }
            map.put("code", code);
            map.put("bus_type", bus_type);
            map.put("account", phone);
            map.put("status", 0);
            map.put("acc_type", "1");
            map.put("create_time", StringUtil.getSysDateTimeY_M_D_h_m_s());//发送时间
            map.put("send_time", StringUtil.getSysDateTimeY_M_D_h_m_s());//发送时间
            verifyRecordMapper.addVerifyRecord(map);
            return ResultModel.ok();
        }
        return ResultModel.error("短信发送失败");
    }
    
    
    public ResultModel withholdUserInfo(YlStaffInfo ylStaffInfo,String real_name){
    	List<Map<String,Object>> list = new ArrayList<>();
        if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.withholdUserInfoAdmin(real_name);
        }else if("staff".equals(ylStaffInfo.getRole())){
            list = yilianMapper.withholdUserInfoStaff(ylStaffInfo.getId(),real_name);
        }else if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.withholdUserInfoOrg(ylStaffInfo.getOrgNo(),real_name);
        }
        return ResultModel.ok(list);
    }

    public ResultModel deductOrderRecord(YlStaffInfo ylStaffInfo,String real_name){
    	List<Map<String,Object>> list = new ArrayList<>();
        if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductOrderRecordAdmin(real_name);
        }else if("staff".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductOrderRecordStaff(ylStaffInfo.getId(),real_name);
        }else if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductOrderRecordOrg(ylStaffInfo.getOrgNo(),real_name);
        }else if("agent".equals(ylStaffInfo.getRole())){
            list = yilianMapper.deductOrderRecordAgent(ylStaffInfo.getOrgNo(),real_name);
        }
        return ResultModel.ok(list);
    }

    public ResultModel staffInfo(YlStaffInfo ylStaffInfo,String real_name){
    	List<Map<String,Object>> list = new ArrayList<>();
        if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = yilianMapper.staffInfoAdmin(real_name);
        }else if("staff".equals(ylStaffInfo.getRole())){
        	
        }else if("companies".equals(ylStaffInfo.getRole())){
            list = yilianMapper.staffInfoOrg(ylStaffInfo.getOrgNo(),real_name);
        }
        return ResultModel.ok(list);
    }

    public Map<String,Object> withholdUserDetail(String user_id){
        Map<String,Object> mm = yilianMapper.withholdUserDetail(user_id);
        return mm;
    }

    public Map<String,Object> orderDetailMerchNo(String MerchOrderId){
        return yilianMapper.orderDetailMerchNo(MerchOrderId);
    }

    public int payNotify(String MerchOrderId,String OrderId,String Status,String PayTime,String SettleDate,int notify_state) {
    	return yilianMapper.payNotify(MerchOrderId, OrderId, Status, PayTime, SettleDate,notify_state);
    }

    public int signSave(String user_id,String imageBase64){
        return yilianMapper.signSave(user_id,imageBase64);
    }
    
    
    public ResultModel openAgentSubmit(YlStaffInfo ylStaffInfo,Map<String,Object> map) {
    	Long id = ylStaffInfo.getId();
    	//更新客户后台系统登陆用户信息202006  begin
		User user = new User();
		user.setUserName(map.get("real_name").toString());
		user.setLoginName(map.get("login_name").toString());
		user.setEmail("123456@163.com");
		user.setPhonenumber(map.get("user_tel").toString());
		user.setSex("0");
		user.setPassword("123456");
		user.setDeptId(102L);
		user.setCreateBy("admin");
		user.setRemark("app创建");
		user.setRoleId(101L);
		user.setRoleIds(new Long[]{101L});
		user.setPostIds(new Long[]{4L});
		int org_no = userService.insertUser(user);
		//更新客户后台系统登陆用户信息202006 end
    	map.put("create_by", id);
        map.put("org_no",org_no);
        map.put("org_name",map.get("org_name").toString());
        map.put("login_pwd",MD5.strToMd5(MD5.strToMd5(map.get("login_pwd").toString())));
    	try{
            yilianMapper.openAgentSubmit(map);
        }catch (DuplicateKeyException e){
    	    e.printStackTrace();
    	    return ResultModel.error("登陆用户名已存在");
        }
    	return ResultModel.ok();
    }

    @Transactional
    public ResultModel openCompanySubmit(YlStaffInfo ylStaffInfo,Map<String,Object> map,MultipartFile card_poto1) {
    	Long id = ylStaffInfo.getId();
    	//更新客户后台系统登陆用户信息202006  begin
		User user = new User();
		user.setUserName(map.get("real_name").toString());
		user.setLoginName(map.get("login_name").toString());
		user.setEmail("123456@163.com");
		user.setPhonenumber(map.get("user_tel").toString());
		user.setSex("0");
		user.setPassword(map.get("login_pwd").toString());
		user.setDeptId(103L);
		user.setCreateBy("admin");
		user.setRemark("app创建");
		user.setRoleId(101L);
		user.setRoleIds(new Long[]{101L});
		user.setPostIds(new Long[]{4L});
		int org_no = userService.insertUser(user);
    	//更新客户后台系统登陆用户信息202006 end
		map.put("org_no", org_no);
		map.put("create_by", id);
        map.put("agent_no","0");
        map.put("login_pwd",MD5.strToMd5(MD5.strToMd5(map.get("login_pwd").toString())));
		if("agent".equals(ylStaffInfo.getRole())){
            AjaxJson file = FileUpload.uploadImg(card_poto1);
            if(!file.isSuccess()){
                return ResultModel.error("上传营业执照失败");
            }
            map.put("photo",file.getData().toString());
            map.put("org_agent",ylStaffInfo.getOrgNo());
            map.put("org_agent_name",ylStaffInfo.getOrgName());
        }
		try{
            yilianMapper.openCompanySubmit(map);
            String acc_name = map.get("acc_name").toString();
            String acc_no = map.get("acc_no").toString();
            String acc_province = map.get("acc_province").toString();
            String acc_city = map.get("acc_city").toString();
            yilianMapper.insertCompanyAccount(acc_name,acc_no,acc_province,acc_city,org_no);
        }catch (DuplicateKeyException e){
		    e.printStackTrace();
		    return ResultModel.error("登陆用户名已存在");
        }
    	return ResultModel.ok();
    }

    public ResultModel forgetPwd(String login_name,String pwd,String user_tel,String verify_code){
        List<String> code = yilianMapper.checkForgetCode(user_tel);

        for(String codeItem: code){
            if(verify_code.equals(codeItem)){
                int cnt =yilianMapper.updateStaff(login_name,pwd,user_tel);
                if(cnt<1){
                    return ResultModel.error("用户不存在或手机号码不一致或密码和上次相同");
                }
                return ResultModel.ok("密码已重置,请重新登陆");
            }
        }

        return ResultModel.error("验证码不正确");
    }
    
    public List<Map<String,Object>> orgQuery(YlStaffInfo ylStaffInfo,String real_name){
    	List<Map<String,Object>> list = yilianMapper.orgQuery(real_name);
    	return list;
    }
    
    public List<Map<String,Object>> companies(String org_no,YlStaffInfo ylStaffInfo,String real_name){
    	return yilianMapper.companies(org_no,ylStaffInfo.getId(),real_name);
    }

    public int openStaff(YlStaffInfo ylStaffInfo,Map<String,Object> map){
        map.put("org_no",ylStaffInfo.getOrgNo());
        map.put("role","staff");
        map.put("org_address",ylStaffInfo.getOrgAddress());
        map.put("org_name",ylStaffInfo.getOrgName());
        map.put("create_by",ylStaffInfo.getId());
        map.put("org_agent",ylStaffInfo.getOrgAgent());
        map.put("org_agent_name",ylStaffInfo.getOrgAgentName());
        map.put("login_pwd",MD5.strToMd5(MD5.strToMd5(map.get("login_pwd").toString())));
        try{
            List<Map<String,Object>> list = yilianMapper.salerQuery(String.valueOf(ylStaffInfo.getOrgNo()));
            if(list!=null){
                String zuidayewuyuan = yilianMapper.getSysConfig("zuidayewuyuan");
                int zuidashu = Integer.parseInt(zuidayewuyuan);
                if(list.size()>zuidashu){
                    return 15;
                }
            }
            yilianMapper.openStaff(map);
        }catch (Exception e){
            e.printStackTrace();
            return 2;
        }
        return 1;
    }
    
    public List<Map<String,Object>> allCompanies(YlStaffInfo ylStaffInfo,String real_name){
    	return yilianMapper.allCompanies(real_name);
    }
    
    public List<Map<String,Object>> salerQuery(String comp_id){
    	return yilianMapper.salerQuery(comp_id);
    }
    
    public List<Map<String,Object>> salerQuery(String comp_id,String real_name){
    	return yilianMapper.salerQueryName(comp_id,real_name);
    }
    
    public Map<String,Object> companyDetail(String comp_id){
    	return yilianMapper.companyDetail(comp_id);
    }
    
    public Map<String,Object> orgDetail(String org_id){
    	Map<String,Object> mm = yilianMapper.orgDetail(org_id);
    	return mm;
    }
}
