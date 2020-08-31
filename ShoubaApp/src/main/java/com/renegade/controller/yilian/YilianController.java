package com.renegade.controller.yilian;

import com.merchant.demo.base.Constants;
import com.merchant.demo.client.TransactionClientCommon;
import com.merchant.util.Log;
import com.renegade.annotations.RenegadeAuth;
import com.renegade.dao.yilian.YilianMapper;
import com.renegade.domain.yilian.YlStaffInfo;
import com.renegade.domain.yilian.dto.ResultModel;
import com.renegade.service.yilian.YilianService;
import com.renegade.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.version2.MsgBean;
import service.version2.Service2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/yilian/api")
public class YilianController {

    @Autowired
    private YilianService yilianService;

    @Autowired
    private YilianMapper yilianMapper;

    @PostMapping("/login")
    @RenegadeAuth
    @ResponseBody
    public ResultModel login(HttpSession session,HttpServletResponse response, @RequestParam Map<String, Object> map){
        response.setHeader("Access-Control-Allow-Origin", "*");
        ResultModel model = yilianService.login(map);
        if("00000".equals(model.getErrorCode())){
            session.setAttribute("user", model.getData());
            return ResultModel.ok("登陆成功");
        }
        return ResultModel.error("用户名或密码错误");
    }
    
    @PostMapping("/withholdUserInfo")
    @ResponseBody
    @RenegadeAuth
    public ResultModel withholdUserInfo(HttpServletRequest request,HttpServletResponse response,String real_name){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return yilianService.withholdUserInfo(ylStaffInfo,real_name);
    }

    @PostMapping("/deductOrder")
    @ResponseBody
    @RenegadeAuth
    public ResultModel deductOrder(HttpServletRequest request,HttpServletResponse response,String real_name){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return yilianService.deductOrderRecord(ylStaffInfo,real_name);
    }

    @PostMapping("/staffInfo")
    @ResponseBody
    @RenegadeAuth
    public ResultModel staffInfo(HttpServletRequest request,HttpServletResponse response,String real_name){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return yilianService.staffInfo(ylStaffInfo,real_name);
    }

    @PostMapping("/signSave")
    @ResponseBody
    @RenegadeAuth
    public ResultModel signSave(String user_id,String imageBase64){
        int cnt =yilianService.signSave(user_id,imageBase64);
        if(cnt>0){
            return ResultModel.ok();
        }else{
            return ResultModel.error("电子签名保存失败,请重试");
        }
    }
    
    
    @GetMapping("/sign/{user_id}")
    @RenegadeAuth
    public String sign(HttpServletRequest request, HttpServletResponse response, Model model,@PathVariable("user_id") String user_id) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        Map<String,Object> map = yilianService.userDetail(null,user_id);
        model.addAttribute("data",map);
    	return "/staff/signShow";
    }

    @GetMapping("/withholdUserDetail/{user_id}")
    @RenegadeAuth
    public String withholdUserDetail(HttpServletRequest request,HttpServletResponse response, Model model, @PathVariable("user_id") String user_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        Map<String, Object> mm = yilianService.withholdUserDetail(user_id);
        model.addAttribute("user",mm);
        return "/companies/withholdUserDetail";
    }
    
    @GetMapping("/openAgent")
    @RenegadeAuth
    public String openAgent(HttpServletRequest request,HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
    	return "/fyhadmin/openAgent";
    }
    
    @GetMapping("/agentOpenCompany")
    @RenegadeAuth
    public String agentOpenCompany(HttpServletRequest request,HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        return "/agent/agentOpenCompany";
    }
    
    @GetMapping("/agentDeductList")
    @RenegadeAuth
    public String agentDeductList(HttpServletRequest request,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("real_name", real_name);
        List<Map<String,Object>> list = yilianService.deductRecord(ylStaffInfo, map);
        model.addAttribute("record", list);
        return "/agent/agentDeductList";
    }
    
    @GetMapping("/orgQuery")
    @RenegadeAuth
    public String orgQuery(HttpServletRequest request,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        List<Map<String,Object>> list = yilianService.orgQuery(ylStaffInfo,real_name);
        model.addAttribute("record", list);
    	return "/fyhadmin/orgQuery";
    }
    
    @GetMapping("/agentCompanyDetail")
    @RenegadeAuth
    public String agentCompanyDetail(HttpServletRequest request,HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return "/agent/agentCompanyDetail";
    }
    
    @PostMapping("/agentCompanies")
    @RenegadeAuth
    @ResponseBody
    public ResultModel agentCompanies(HttpServletRequest request,HttpServletResponse response,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	List<Map<String,Object>> list = yilianService.companies("",ylStaffInfo,real_name);
    	return ResultModel.ok(list);
    }
    
    @GetMapping("/agentCompanies")
    @RenegadeAuth
    public String agentCompanies(HttpServletRequest request,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
//        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
//    	List<Map<String,Object>> list = yilianService.companies(ylStaffInfo,real_name);
//    	model.addAttribute("record", list);
    	return "/agent/agentCompanies";
    }

    @PostMapping("/forgetPwd")
    @RenegadeAuth
    @ResponseBody
    public ResultModel forgetPwd(String login_name,String login_pwd,String user_tel,String verify_code){
        return yilianService.forgetPwd(login_name,login_pwd,user_tel,verify_code);
    }

    @GetMapping("/forgetPwd")
    @RenegadeAuth
    public String forgetPwd(){
        return "/forgetPwd";
    }

    @GetMapping("/openStaff")
    @RenegadeAuth
    public String openStaff(HttpServletRequest request,HttpServletResponse response){
        return "/companies/openStaff";
    }

    @PostMapping("/openStaff")
    @RenegadeAuth
    @ResponseBody
    public ResultModel openStaff(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> map){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            return ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        int cnt = yilianService.openStaff(ylStaffInfo,map);
        if(cnt==2){
            return ResultModel.error("登陆用户名已被占用");
        }
        if(cnt == 15){
            return ResultModel.error("超过业务员开户数"+cnt);
        }
        return ResultModel.ok("开户成功");
    }
    
    @GetMapping("/companies/{org_no}")
    @RenegadeAuth
    public String companies(HttpServletRequest request,HttpServletResponse response,Model model,@PathVariable("org_no") String org_no,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	List<Map<String,Object>> list = yilianService.companies(org_no,ylStaffInfo,real_name);
    	model.addAttribute("record", list);
        Map<String,Object> mm = yilianService.companyDetail(org_no);
        model.addAttribute("org_name",mm.get("org_name"));
        model.addAttribute("org_no",org_no);
    	return "/fyhadmin/companies";
    }
    
    @GetMapping("/allCompanies")
    @RenegadeAuth
    public String allCompanies(HttpServletRequest request,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	List<Map<String,Object>> list = yilianService.allCompanies(ylStaffInfo,real_name);
    	model.addAttribute("record", list);
    	return "/fyhadmin/allCompanies";
    }
    
    @GetMapping("/staffQuery/{comp_id}/{id}")
    @RenegadeAuth
    public String staffQuery(HttpServletRequest request,HttpServletResponse response,Model model,@PathVariable("comp_id") String comp_id,@PathVariable("id") String id) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        List<Map<String,Object>> list = yilianService.salerQuery(comp_id);
        model.addAttribute("record", list);
        Map<String,Object> map = yilianService.companyDetail(id);
        model.addAttribute("title",map.get("org_name"));
        model.addAttribute("comp_id",comp_id);
        model.addAttribute("id",id);
        return "/fyhadmin/staffQuery";
    }
    
    @GetMapping("/deductList")
    @RenegadeAuth
    public String deductList(HttpServletRequest request,HttpServletResponse response,Model model) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return "/fyhadmin/deductList";
    }
    
    @GetMapping("/staffQueryName")
    @RenegadeAuth
    public String staffQueryName(HttpServletRequest request,HttpServletResponse response,Model model,String comp_id,String id,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        List<Map<String,Object>> list = yilianService.salerQuery(comp_id,real_name);
        model.addAttribute("record", list);
        Map<String,Object> map = yilianService.companyDetail(id);
        model.addAttribute("title",map.get("org_name"));
        model.addAttribute("comp_id",comp_id);
        model.addAttribute("id",id);
        return "/fyhadmin/staffQuery";
    }
    
    @GetMapping("/companyDetail/{comp_id}")
    @RenegadeAuth
    public String companyDetail(HttpServletRequest request,HttpServletResponse response,Model model,@PathVariable("comp_id") String comp_id) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	Map<String,Object> map = yilianService.companyDetail(comp_id);
    	model.addAttribute("comp", map);
    	Map<String,Object> titleMap = yilianService.companyDetail(comp_id);
        model.addAttribute("title",titleMap.get("org_name"));
    	return "/fyhadmin/companyDetail";
    }
    
    @GetMapping("/agentCompanyDetail/{comp_id}")
    @RenegadeAuth
    public String agentCompanyDetail(HttpServletRequest request,HttpServletResponse response,Model model,@PathVariable("comp_id") String comp_id) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	Map<String,Object> map = yilianService.companyDetail(comp_id);
    	model.addAttribute("comp", map);
    	Map<String,Object> titleMap = yilianService.companyDetail(comp_id);
        model.addAttribute("title",titleMap.get("org_name"));
    	return "/agent/agentCompanyDetail";
    }
    
    @PostMapping("/openAgentSubmit")
    @RenegadeAuth
    @ResponseBody
    public ResultModel openAgentSubmit(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> map) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	if (request.getSession().getAttribute("user") == null) {
            return ResultModel.error("登陆超时请重新登陆");
        }
    	YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	return yilianService.openAgentSubmit(ylStaffInfo,map);
    }
    
    @PostMapping("/openCompanySubmit")
    @RenegadeAuth
    @ResponseBody
    public ResultModel openCompanySubmit(HttpServletRequest request,MultipartFile card_poto1,HttpServletResponse response,@RequestParam Map<String,Object> map) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	if (request.getSession().getAttribute("user") == null) {
            return ResultModel.error("登陆超时请重新登陆");
        }
    	YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
    	return yilianService.openCompanySubmit(ylStaffInfo,map,card_poto1);
    }

    @GetMapping("/index")
    @RenegadeAuth
    public String index(HttpServletRequest requests,HttpServletResponse response, Model model) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = requests.getSession();
        if (session.getAttribute("user") == null) {
            return "/login";
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        Long orgNo = ylStaffInfo.getOrgNo();
        String role = ylStaffInfo.getRole();
        if("companies".equals(role)){
            model.addAttribute("auditNum",yilianService.withholdAudit(ylStaffInfo).size());
            model.addAttribute("real_name",ylStaffInfo.getOrgName());
            return "/companies/index";
        }else if("agent".equals(role)){
            model.addAttribute("real_name",ylStaffInfo.getOrgName());
            return "/agent/index";
        }else if("fyhadmin".equals(role)){
            model.addAttribute("auditNum",yilianService.withholdAudit(ylStaffInfo).size());
            model.addAttribute("real_name",ylStaffInfo.getRealName());
            return "/fyhadmin/index";
        }else{
            model.addAttribute("real_name",ylStaffInfo.getRealName());
            return "/staff/index";
        }
    }

    @PostMapping("/agreeDeductOne/{order_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel agreeDeductOne(HttpSession session,HttpServletResponse response,@PathVariable("order_id") String order_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        yilianService.agreeDeductOne(ylStaffInfo,order_id);
        return ResultModel.ok("已提交审批,等待终审");
    }

    @PostMapping("/refuseDeductOne/{order_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel refuseDeductOne(HttpSession session,HttpServletResponse response,@PathVariable("order_id") String order_id,String remark){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        yilianService.refuseDeductOne(ylStaffInfo,order_id,remark);
        return ResultModel.ok("已拒绝,需重新提交申请");
    }


    @PostMapping("/agreeDeductTwo/{order_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel agreeDeductTwo(HttpSession session,HttpServletResponse response,@PathVariable("order_id") String order_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        return yilianService.agreeDeductTwo(order_id);
    }

    @PostMapping("/refuseDeductTwo/{order_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel refuseDeductTwo(HttpSession session,HttpServletResponse response,@PathVariable("order_id") String order_id,String remark){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        yilianService.refuseDeductTwo(ylStaffInfo,order_id,remark);
        return ResultModel.ok("已拒绝,需重新提交审批");
    }
    
    @GetMapping("/withholdUserInfo")
    @RenegadeAuth
    public String withholdUserInfo(HttpServletRequest request,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return "/companies/deductRecord";
    }
    
    @GetMapping("/userInfoList")
    @RenegadeAuth
    public String userInfoList(HttpSession session,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return "/fyhadmin/userInfoList";
    }
    
    @GetMapping("/openCompany")
    @RenegadeAuth
    public String checkinCompany(HttpSession session,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return "/fyhadmin/openCompany";
//        return "/agent/agentOpenCompany";
    }
    
    
    @GetMapping("/staffRecord")
    @RenegadeAuth
    public String staffRecord(HttpServletRequest request,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        return "/companies/staffRecord";
    }

    @GetMapping("/staffTeam/{staff_id}")
    @RenegadeAuth
    public String staffTeam(HttpServletRequest request,HttpServletResponse response,Model model,@PathVariable("staff_id") String staff_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getSession().getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)request.getSession().getAttribute("user");
        List<Map<String,Object>> list = yilianService.staffTeam(ylStaffInfo,staff_id);
        model.addAttribute("record",list);
        model.addAttribute("staff_id",staff_id);
        Map<String, Object> mm =yilianService.companyDetail(staff_id);
        model.addAttribute("real_name",mm.get("real_name"));
        return "/companies/staffTeam";
    }
    
    @GetMapping("/deductRecordComp")
    @RenegadeAuth
    public String deductRecordComp(HttpSession session,HttpServletResponse response,Model model,String real_name) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return "/companies/deductRecord";
    }
    
    

    @GetMapping("/deductRecord")
    @RenegadeAuth
    public String deductRecord(HttpSession session,HttpServletResponse response,Model model,String real_name){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        Map<String,Object> map = new HashMap<>();
        map.put("real_name",real_name);
        List<Map<String,Object>> list = yilianService.deductRecord(ylStaffInfo,map);
        model.addAttribute("record",list);
        return "/staff/deductRecord";
    }


    @GetMapping("/withholdAudit")
    @RenegadeAuth
    public String withholdAudit(HttpSession session,HttpServletResponse response,Model model){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        List<Map<String, Object>> list = yilianService.withholdAudit(ylStaffInfo);
        model.addAttribute("record",list);
        return "/companies/withholdAudit";
    }

    @GetMapping("/withholdAudit2")
    @RenegadeAuth
    public String withholdAudit2(HttpSession session,HttpServletResponse response,Model model){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        List<Map<String, Object>> list = yilianService.withholdAudit(ylStaffInfo);
        model.addAttribute("record",list);
        return "/fyhadmin/withholdAudit";
    }

    @GetMapping("/staffCheckInUser")
    @RenegadeAuth
    public String staffCheckInUser(){
        return "/staff/staffCheckInUser";
    }

    @PostMapping("/withhold/{user_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel withhold(HttpSession session,HttpServletResponse response,@PathVariable("user_id") String user_id,String verfiy_code){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return yilianService.withhold(ylStaffInfo,user_id,verfiy_code);
    }

    @PostMapping("/withholdTest/{user_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel withholdTest(HttpSession session,HttpServletResponse response,@PathVariable("user_id") String user_id,String verfiy_code){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return yilianService.withholdTest(ylStaffInfo,user_id,verfiy_code);
    }

    @PostMapping("/sendCodeWithHold/{user_id}")
    @RenegadeAuth
    @ResponseBody
    public ResultModel sendCodeWithHold(HttpSession session,HttpServletResponse response,@PathVariable("user_id") String user_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return yilianService.sendCodeWithHold(ylStaffInfo,user_id);
    }

    @GetMapping("/userDetail/{user_id}")
    @RenegadeAuth
    public String userDetail(HttpSession session,HttpServletResponse response,Model model,@PathVariable("user_id") String user_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        Map<String,Object> user = yilianService.userDetail(ylStaffInfo,user_id);
        model.addAttribute("user",user);
        return "/staff/userDetail";
    }

    @GetMapping("/openRecord")
    @RenegadeAuth
    public String openUserRecord(HttpSession session,HttpServletResponse response,Model model,String real_name){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        Map<String,Object> map = new HashMap<>();
        map.put("real_name",real_name);
        List<Map<String,Object>> list = yilianService.openRecord(ylStaffInfo,map);
        model.addAttribute("record",list);
        return "/staff/openRecord";
    }

    @GetMapping("/withholdUser")
    @RenegadeAuth
    public String withholdUser(HttpSession session,HttpServletResponse response,Model model,String real_name){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        Map<String,Object> map = new HashMap<>();
        map.put("real_name",real_name);
        List<Map<String,Object>> list = yilianService.openRecord(ylStaffInfo,map);
        model.addAttribute("record",list);
        return "/staff/withhold";
    }

    @GetMapping("/uploadPhoto")
    @RenegadeAuth
    public String uploadPhoto(@RequestParam Map<String,Object> dataMap, ModelMap modelMap){
        modelMap.put("data",dataMap);
        return "/staff/uploadPhoto";
    }

    @GetMapping("/logout")
    @RenegadeAuth
    public void logout(HttpServletResponse response){
        try{
            response.sendRedirect("/login");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/sendCode")
    @ResponseBody
    @RenegadeAuth
    public ResultModel sendCode(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String,Object> map){
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();
//        if (session.getAttribute("user") == null) {
//            return ResultModel.error("登陆超时,请重新登陆");
//        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        if(map.get("user_tel")==null){
            return ResultModel.error("请输入手机号码");
        }
        yilianService.sendCode(ylStaffInfo,map.get("user_tel").toString(),map.get("code_type").toString());
        return ResultModel.ok("短信发送成功");
    }


    @PostMapping("/orderQuery")
    @ResponseBody
    @RenegadeAuth
    public ResultModel orderQuery(HttpServletResponse response, YlStaffInfo ylStaffInfo){
        response.setHeader("Access-Control-Allow-Origin", "*");
        return yilianService.orderQuery(ylStaffInfo);
    }

    @PostMapping("/openRecordQuery")
    @ResponseBody
    @RenegadeAuth
    public ResultModel openRecordQuery(HttpSession session,HttpServletResponse response,@RequestParam Map<String,Object> map,Model model){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时,请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        List<Map<String,Object>> list = yilianService.openRecord(ylStaffInfo,map);
        model.addAttribute("record",list);
        return ResultModel.ok();
    }

    @PostMapping("/openUser")
    @RenegadeAuth
    public String openUser(HttpSession session,HttpServletResponse response,Model model,MultipartFile card_poto1,MultipartFile card_poto2,
                                MultipartFile acc_poto1,MultipartFile acc_poto2,MultipartFile person_poto,@RequestParam Map<String,Object> map){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        model.addAttribute("data",map);
        return yilianService.insertUser(map,card_poto1,card_poto2,acc_poto1,acc_poto2,person_poto,ylStaffInfo);
    }

    @PostMapping("/checkSmsCode")
    @RenegadeAuth
    @ResponseBody
    public ResultModel checkSmsCode(HttpSession session,HttpServletResponse response,String user_tel,String verCode){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (session.getAttribute("user") == null) {
            return ResultModel.error("登陆超时请重新登陆");
        }
        YlStaffInfo ylStaffInfo = (YlStaffInfo)session.getAttribute("user");
        return yilianService.checkSmsCode(ylStaffInfo.getId(),user_tel,verCode);
    }
    
    
    @RequestMapping("/notify")
    @RenegadeAuth
    @ResponseBody
    public String notify(HttpServletRequest request,HttpServletResponse response) {
        try{
            // 1.设置编码
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");

            // 2.结果通知参数，易联异步通知默认POST提交
            String version = request.getParameter("Version");
            String merchantId = request.getParameter("MerchantId");
            String merchOrderId = request.getParameter("MerchOrderId");
            String amount = request.getParameter("Amount");
            String extData = request.getParameter("ExtData");
            String orderId = request.getParameter("OrderId");
            String status = request.getParameter("Status");
            String payTime = request.getParameter("PayTime");
            String settleDate = request.getParameter("SettleDate");
            String sign = request.getParameter("Sign");

            // 3.需要对必要输入的参数进行检查[业务处理]

            String retMsgJson = "";
            try {
                Log.setLogFlag(true);
                Log.println("------------交易:订单结果异步通知-------------------");

                // 4.验证订单结果通知的验签
                boolean b = TransactionClientCommon.backCheckNotifySign(version, merchantId, merchOrderId,
                        amount, extData, orderId, status, payTime, settleDate, sign,
                        Constants.PAYECO_RSA_PUBLIC_KEY);
                if (!b) {
                    retMsgJson = "{\"RetCode\":\"E104\",\"RetMsg\":\"订单结果异步通知验证签名失败!\"}";
                    Log.println("验证签名失败!");
                }else{

                    // 5.签名验证成功后，需要对订单进行后续处理[对自己的业务进行处理]
                    // 订单已支付
                    if ("02".equals(status)) {
                        // 若是互联金融行业, 订单已支付的状态为【0000】
                        //if ("0000".equals(status)) {
                        // 1、检查Amount和商户系统的订单金额是否一致
                        Map<String,Object> map = yilianService.orderDetailMerchNo(merchOrderId);
                        // 2、订单支付成功的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理）；
                        String notify_state = map.get("notify_state").toString();
                        if("0".equals(notify_state)){
                            yilianService.payNotify(merchOrderId, orderId, "94", payTime, settleDate,1);
                        }
                        // 3、返回响应内容
                        retMsgJson = "{\"RetCode\":\"0000\",\"RetMsg\":\"订单已支付\"}";
                        Log.println("订单已支付!");
                    } else {
                        // 1、订单支付失败的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理，避免成功后又修改为失败）；
                        yilianService.payNotify(merchOrderId, orderId, "91", payTime, settleDate,1);
                        // 2、返回响应内容
                        retMsgJson = "{\"RetCode\":\"E105\",\"RetMsg\":\"订单支付失败+"+status+"\"}";
                        Log.println(" 订单支付失败!status="+status);
                    }
                }
            } catch (Exception e) {
                retMsgJson = "{\"RetCode\":\"E106\",\"RetMsg\":\"订单结果异步通知处理结果异常\"}";
                Log.println(" 处理通知结果异常!e="+e.getMessage());
            }
            Log.println("----------处理完成-----------");
            // 返回数据
            PrintWriter out = response.getWriter();
            out.println(retMsgJson);
            // for HTTP1.1
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    	return "0000";
    }

    @RequestMapping("/orderPayMerchant")
    @RenegadeAuth
    @ResponseBody
    public String orderPayMerchant(@RequestBody Map<String,Object> params){
        try{
            String merchOrderId = params.get("merchOrderId").toString();
            // 若是互联金融行业, 订单已支付的状态为【0000】
            //if ("0000".equals(status)) {
            // 1、检查Amount和商户系统的订单金额是否一致
            Map<String,Object> map = yilianService.orderDetailMerchNo(params.get("merchOrderId").toString());
            // 2、订单支付成功的业务逻辑处理请在本处增加（订单通知可能存在多次通知的情况，需要做多次通知的兼容处理）；
            String status1 = map.get("status").toString();
            if("94".equals(status1)){

                //批量代付****************************
                Map<String,Object> bankAcc = yilianMapper.bankAccDetail(map.get("org_no"));
                String user_name = map.get("user_name").toString();
                String sn = StringUtil.generateOrderId("FYHDFSN");
                String acc_no = bankAcc.get("acc_no").toString();
                System.out.println("bankAcc(acc_no)===="+acc_no);
                String acc_name = bankAcc.get("acc_name").toString();
                System.out.println("bankAcc(acc_name)===="+acc_name);
                String shouxufei = yilianMapper.getSysConfig("shouxufei");
                System.out.println("map.get(shouxufei)===="+shouxufei);
                System.out.println("map.get(amount)===="+map.get("amount"));
                String amount1 = map.get("amount").toString();
                System.out.println("orderId====="+map.get("orderId"));
                String orderId = map.get("orderId").toString();
                System.out.println("status====="+map.get("status"));
                String status =  map.get("status").toString();
                System.out.println("paytime======"+map.get("paytime"));
                String payTime = map.get("paytime").toString();
                System.out.println("settledate====="+map.get("settledate"));
                String settleDate = map.get("settledate").toString();


                BigDecimal bamount = new BigDecimal(amount1).setScale(2);
                BigDecimal bshouxufei = new BigDecimal(shouxufei).multiply(bamount).setScale(2, RoundingMode.DOWN);
                amount1 = bamount.subtract(bshouxufei).toString();


                String acc_province = bankAcc.get("acc_province").toString();
                String acc_city = bankAcc.get("acc_city").toString();
                String acc_prop = bankAcc.get("acc_prop").toString();
                String mer_order_no = StringUtil.generateOrderId("FYHDFMERONO");
                Log.println("Service2.pay代付接口请求参数user_name="+user_name+"&sn="+sn+"&acc_no="+acc_no+"&acc_name="+acc_name+"&amount="+amount1+
                        "&acc_province="+acc_province+"&acc_city="+acc_city+"&acc_prop="+acc_prop+"&mer_order_no="+mer_order_no);
                MsgBean msgBean = Service2.pay(user_name,sn,acc_no,acc_name,amount1,acc_province,acc_city,acc_prop,mer_order_no);
                String batch_no = msgBean.getBATCH_NO();
//                批量代付****************************
                if("0000".equals(msgBean.getBODYS().get(0).getPAY_STATE())){
                    yilianService.payNotify(merchOrderId, orderId, "96", payTime, settleDate,1);
                    yilianMapper.insertPayOrder(batch_no,user_name,sn,acc_no,acc_name,acc_province,acc_city,amount1,acc_prop,map.get("user_card"),mer_order_no,null,"96",settleDate,settleDate,map.get("staff_no"),
                            map.get("staff_name"),map.get("staff_tel"),map.get("org_no"),map.get("org_name"),map.get("org_agent"),map.get("org_agent_name"),msgBean.getBODYS().get(0).getREMARK());
                }else{
                    yilianService.payNotify(merchOrderId, orderId, "99", payTime, settleDate,1);
                    yilianMapper.insertPayOrder(batch_no,user_name,sn,acc_no,acc_name,acc_province,acc_city,amount1,acc_prop,map.get("user_card"),mer_order_no,null,"99",settleDate,settleDate,map.get("staff_no"),
                            map.get("staff_name"),map.get("staff_tel"),map.get("org_no"),map.get("org_name"),map.get("org_agent"),map.get("org_agent_name"),msgBean.getBODYS().get(0).getREMARK());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }
}
