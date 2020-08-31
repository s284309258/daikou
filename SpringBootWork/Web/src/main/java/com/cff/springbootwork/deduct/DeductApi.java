package com.cff.springbootwork.deduct;

import com.cff.springbootwork.dto.ResultModel;
import com.cff.springbootwork.staff.YlStaffInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("api/deduct")
public class DeductApi {
    @Autowired
    private DeductService deductService;

    @PostMapping("orderQuery")
    @ResponseBody
    public ResultModel orderQuery(HttpServletResponse response, YlStaffInfo ylStaffInfo){
        response.setHeader("Access-Control-Allow-Origin", "*");
        return deductService.orderQuery(ylStaffInfo);
    }
}
