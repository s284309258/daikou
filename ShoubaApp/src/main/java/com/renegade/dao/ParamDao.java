package com.renegade.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.renegade.domain.ParamDO;

/**
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-04-17 17:25:49
 */
@Mapper
public interface ParamDao {
	ParamDO getsss(Integer id);
	/**
	 * 获得参数值
	 * 
	 * @param paramCode
	 * @return
	 */
	ParamDO findValue(String paramCode);

	List<ParamDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ParamDO param);

	int update(ParamDO param);

	int remove(Integer id);

	int batchRemove(Integer[] ids);
}
