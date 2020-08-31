package com.renegade.dao;

import com.renegade.domain.FrontInformationDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 资讯
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-25 16:36:07
 */
@Mapper
public interface FrontInformationDao {

	FrontInformationDO get(Integer noticeId);
	
	List<FrontInformationDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FrontInformationDO frontInformation);
	
	int update(FrontInformationDO frontInformation);
	
	int remove(Integer notice_id);
	
	int batchRemove(Integer[] noticeIds);
}
