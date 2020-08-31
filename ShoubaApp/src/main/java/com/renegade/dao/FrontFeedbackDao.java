package com.renegade.dao;

import java.util.List;
import java.util.Map;

import com.renegade.domain.FrontFeedbackDO;

/**
 * 
 * @author RenegadeNic
 * @email 291312408@qq.com
 * @date 2019-04-19 21:57:27
 */
public interface FrontFeedbackDao {

	FrontFeedbackDO get(Integer id);
	
	List<FrontFeedbackDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FrontFeedbackDO frontFeedback);
	
	int update(FrontFeedbackDO frontFeedback);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
