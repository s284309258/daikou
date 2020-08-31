package com.renegade.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.renegade.domain.UserAddressDO;

/**
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 15:45:09
 */
@Mapper
public interface UserAddressDao {

	UserAddressDO get(Integer addressId);
	UserAddressDO findUserDefutlAddress(@Param("addressId")Integer addressId,@Param("userId") Integer userIdd);
	
	List<UserAddressDO> list(Map<String,Object> map);
	List<UserAddressDO> findUserAddressListByUserId(Integer userId);
	
	int count(Map<String,Object> map);
	
	int save(UserAddressDO userAddress);
	
	int update(UserAddressDO userAddress);
	int deleteAddres(@Param("addressId")Integer addressId,@Param("userId") Integer userId);
	
	int remove(Integer address_id);
	
	int batchRemove(Integer[] addressIds);
	
}
