package com.renegade.dao;

import com.renegade.domain.GoodsCategoryDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 商品类别
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-14 18:23:51
 */
@Mapper
public interface GoodsCategoryDao {

	GoodsCategoryDO get(Integer id);
	
	List<GoodsCategoryDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(GoodsCategoryDO goodsCategory);
	
	int update(GoodsCategoryDO goodsCategory);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	String findNameById(int id);
}
