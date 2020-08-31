package com.cff.springbootwork.deduct;

import com.cff.springbootwork.dto.ResultModel;
import com.cff.springbootwork.mybatis.dao.DeductMapper;
import com.cff.springbootwork.staff.YlStaffInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DeductService {
    @Autowired
    private DeductMapper deductMapper;


    public ResultModel orderQuery(YlStaffInfo ylStaffInfo){
        List<Map<String,Object>> list = new ArrayList<>();
        if("companies".equals(ylStaffInfo.getRole())){
            list = deductMapper.orderQuery();
        }else if("fyhadmin".equals(ylStaffInfo.getRole())){
            list = deductMapper.orderQueryOrgNo(ylStaffInfo.getOrgNo());
        }
        return ResultModel.ok(list);
    }
}
