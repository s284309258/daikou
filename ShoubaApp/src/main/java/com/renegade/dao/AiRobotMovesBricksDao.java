package com.renegade.dao;

import com.renegade.domain.AiRobotMovesBricksDO;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 搬砖活期表
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-18 21:32:13
 */
@Mapper
public interface AiRobotMovesBricksDao {

	AiRobotMovesBricksDO get(Integer id);

	List<AiRobotMovesBricksDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(AiRobotMovesBricksDO aiRobotMovesBricks);

	int update(AiRobotMovesBricksDO aiRobotMovesBricks);

	int remove(Integer id);

	int updateKeyStart(Integer userId);

	int updateStastAble(Map<String, Object> map);

	int updateKeyStartGet(Map<String, Object> map);

	int batchRemove(Integer[] ids);
}
