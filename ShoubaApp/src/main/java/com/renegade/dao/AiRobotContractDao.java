package com.renegade.dao;

import com.renegade.domain.AiRobotContractDO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 智能合约定期管理
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-18 21:30:58
 */
@Mapper
public interface AiRobotContractDao {

	AiRobotContractDO get(Integer id);

	BigDecimal sumMoney(Integer userId);

	List<AiRobotContractDO> getMoneyAll(Integer userId);

	List<AiRobotContractDO> list(Map<String, Object> map);

	int updateProfit(Map<String, Object> map);

	int updateAgin(Map<String, Object> map);

	/**
	 * 未超过规定的期限
	 * 
	 * @param map
	 * @return
	 */
	List<AiRobotContractDO> listTaskDetailNo(Map<String, Object> map);

	/**
	 * 超过规定的期限
	 * 
	 * @param map
	 * @return
	 */
	List<AiRobotContractDO> listTaskDetailSuper(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(AiRobotContractDO aiRobotContract);

	int update(AiRobotContractDO aiRobotContract);
	int updateMoreContract(AiRobotContractDO aiRobotContract);

	int uodateContractStatus(Map<String, Object> map);

	int remove(Integer id);

	int batchRemove(Integer[] ids);
}
