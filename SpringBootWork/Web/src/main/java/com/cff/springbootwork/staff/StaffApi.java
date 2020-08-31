package com.cff.springbootwork.staff;

import com.cff.springbootwork.dto.ResultModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/yilian/api")
@Api(tags = "登陆接口")
public class StaffApi {

    @Autowired
    private StaffService staffService;

    @PostMapping("/login")
    @ResponseBody
    public ResultModel login(HttpServletResponse response, YlStaffInfo ylStaffInfo){
        response.setHeader("Access-Control-Allow-Origin", "*");
        return staffService.login(ylStaffInfo);
    }

    @PostMapping("/withholdUserInfo")
    @ResponseBody
    public ResultModel withholdUserInfo(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        return staffService.withholdUserInfo();
    }

    @PostMapping("/deductOrder")
    @ResponseBody
    public ResultModel deductOrder(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        return staffService.deductOrderRecord();
    }

    @PostMapping("/staffInfo")
    @ResponseBody
    public ResultModel staffInfo(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        return staffService.staffInfo();
    }

    @GetMapping("/withholdUserDetail/{user_id}")
    public String withholdUserDetail(HttpServletResponse response, Model model, @PathVariable("user_id") String user_id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> mm = staffService.withholdUserDetail(user_id);
        model.addAttribute("user",mm);
        return "/companies/sale-team";
    }
}
