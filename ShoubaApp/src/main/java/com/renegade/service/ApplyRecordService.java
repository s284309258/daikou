package com.renegade.service;



import java.util.List;
import java.util.Map;

import com.renegade.domain.ApplyRecordDO;
import com.renegade.util.R;

/**
 * 
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 11:54:59
 */
public interface ApplyRecordService {
	
     //根据用户id查询该用户商铺信息
	ApplyRecordDO getObjectById(Integer userId);
}
