package com.renegade.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.renegade.domain.FrontTiBiLogDo;
@Mapper
public interface FrontTiBiLogDao {
	
	/**
	 * 添加提币记录
	 * @param tiBiLog
	 * @return
	 */
	public int addTiBiLog(FrontTiBiLogDo tiBiLog);
	
	/**
	 * 获取提币记录
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> findTiBiLogByUserId(@Param("userId")int userId);
	
	/**
	 * 修改提币状态
	 * @param sendId
	 * @param status
	 * @return
	 */
	public int updateTiBiLogStatusBySendId(@Param("sendId")int sendId,@Param("status") int status);
	
	
}
