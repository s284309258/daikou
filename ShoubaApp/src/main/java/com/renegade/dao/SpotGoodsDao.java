package com.renegade.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.renegade.domain.SpotGoodsDO;

/**
 * 现货商城
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-05-22 20:35:13
 */
@Mapper
public interface SpotGoodsDao {

	SpotGoodsDO get(Integer goodsId);


	List<SpotGoodsDO> list(Map<String, Object> map);
	List<SpotGoodsDO> listGoodsCategory(Map<String, Object> map);
	
	SpotGoodsDO listTypeGoods(Map<String, Object> map);
	int count(Map<String, Object> map);

	int save(SpotGoodsDO spotGoods);

	int update(SpotGoodsDO spotGoods);
	int updatePutWay(Integer goodsId);
	int updatePullWay(Integer goodsId);

	/**
	 * 增加商品库存
	 * 
	 * @param spotGoods
	 * @return
	 */
	int updateStockNum(SpotGoodsDO spotGoods);

	int reduceStockNum(SpotGoodsDO spotGoods);

	/**
	 * 增加商品销量
	 * 
	 * @param spotGoods
	 * @return
	 */
	int updateSaleNum(SpotGoodsDO spotGoods);

	/**
	 * 增加商品销量，减少商品库存
	 * 
	 * @param spotGoods
	 * @return
	 */
	int updateSaleStockNum(SpotGoodsDO spotGoods);
	int updateSaleSpikeNum(SpotGoodsDO spotGoods);

	int remove(Integer goods_id);

	int batchRemove(Integer[] goodsIds);



}
