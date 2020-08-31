package com.renegade.dao;

import com.renegade.domain.WalletManagerDO;

import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户的钱包管理
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 15:04:47
 */
@Mapper
public interface WalletManagerDao {

	WalletManagerDO get(Integer id);

	WalletManagerDO getUserIdWallet(Integer userId);

	List<WalletManagerDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(Integer userId);

	int update(WalletManagerDO walletManager);

	int remove(Integer id);

	int batchRemove(Integer[] ids);

	int updateReduceUsdt(Map<String, Object> map);

	int updateAddUsdt(Map<String, Object> map);

	int updateReduceUt(Map<String, Object> map);

	int updateAddUt(Map<String, Object> map);

	int updateReduceVs(Map<String, Object> map);

	int updateAddVs(Map<String, Object> map);

	int updateAddVsHuoqi(Map<String, Object> map);

	int updateAddVsHuoqi2(Map<String, Object> map);

	int updateAddVsHuoqi3(Map<String, Object> map);

	int updateAddVsDingqi(Map<String, Object> map);

	int updateAddVsDingqi2(Map<String, Object> map);

	int updateAddVsDingqi3(Map<String, Object> map);

	int updateUsdtToVs(Map<String, Object> map);

	int updateUsdtFreeze(Map<String, Object> map);

	int updateVsFreeze(Map<String, Object> map);

	int updateUtFreeze(Map<String, Object> map);

	int updateUsdtFreezeReduce(Map<String, Object> map);

	int updateVsFreezeReduce(Map<String, Object> map);

	int updateUtFreezeReduce(Map<String, Object> map);

	int updateUtToVs(Map<String, Object> map);

	Map<String, Object> getDogsDetail(@Param("userId") Integer userId);
		 //分享收益奖励ut
	void shareProfitByUtMap(Map<String, Object> mapUpdateUt);
	
	   //计算该用户伞下团队业绩
	BigDecimal getTeamUsdtById(Integer userId);
	
	
}
