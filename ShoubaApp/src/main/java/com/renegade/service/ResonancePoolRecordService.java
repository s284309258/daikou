package com.renegade.service;

import com.renegade.config.AjaxJson;
import com.renegade.domain.ResonancePoolRecordDO;

import java.util.List;
import java.util.Map;

/**
 * 共振池用户投放记录
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-19 21:47:57
 */
public interface ResonancePoolRecordService {
	
	AjaxJson saveSelefPutInto(Map<String, Object> map);
}
