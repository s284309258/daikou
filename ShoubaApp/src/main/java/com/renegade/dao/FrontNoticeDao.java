package com.renegade.dao;

import com.renegade.domain.FrontNoticeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-17 20:02:29
 */
@Mapper
public interface FrontNoticeDao {

	FrontNoticeDO get(Integer noticeId);
	
	List<FrontNoticeDO> list(Map<String,Object> map);
	//轮播图
	List<Map<String, Object>> getRocationView();
	
	int count(Map<String,Object> map);
	
	int save(FrontNoticeDO frontNotice);
	
	int update(FrontNoticeDO frontNotice);
	
	int remove(Integer notice_id);
	
	int batchRemove(Integer[] noticeIds);

	List<FrontNoticeDO> listAll();
}
