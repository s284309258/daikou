package com.cff.springbootwork.staff;

import com.cff.springbootwork.dto.ResultModel;
import com.cff.springbootwork.mybatis.dao.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StaffService {

    @Autowired
    public StaffMapper staffMapper;

    public ResultModel login(YlStaffInfo ylStaffInfo){
        Map<String,Object> map = staffMapper.login(ylStaffInfo.getLoginName(),ylStaffInfo.getLoginPwd());
        if(map==null){
            return ResultModel.error("用户名或密码错误");
        }
        return ResultModel.ok(map);
    }

    public ResultModel withholdUserInfo(){
        List<Map<String,Object>> list = staffMapper.withholdUserInfo();
        return ResultModel.ok(list);
    }

    public ResultModel deductOrderRecord(){
        List<Map<String,Object>> list = staffMapper.deductOrderRecord();
        return ResultModel.ok(list);
    }

    public ResultModel staffInfo(){
        List<Map<String,Object>> list = staffMapper.staffInfo();
        return ResultModel.ok(list);
    }

    public Map<String,Object> withholdUserDetail(String user_id){
        Map<String,Object> mm = staffMapper.withholdUserDetail(user_id);
        return mm;
    }
}
