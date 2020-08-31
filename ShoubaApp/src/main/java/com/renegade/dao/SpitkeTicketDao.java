package com.renegade.dao;

import com.renegade.domain.SpitkeTicketDO;
import com.renegade.domain.SpotGoodsDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 抵扣券记录
 * 
 * @author NicRenegade
 * @email 291312408@qq.com
 * @date 2019-06-18 14:40:30
 */
@Mapper
public interface SpitkeTicketDao {

	SpitkeTicketDO get(Integer id);

	List<SpitkeTicketDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(SpitkeTicketDO spitkeTicket);

	int update(SpitkeTicketDO spitkeTicket);

	int updateExpenseTickets(Map<String, Object> map);

	int remove(Integer id);

	int batchRemove(Integer[] ids);

	Map<String, Object> countNum(Integer userId);
    /**
     * 统计某个某个商品的秒杀券
     * @param userId
     * @param goodsId
     * @return
     */
	int countUserNum(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);

	List<SpotGoodsDO> listTicketsDetail(SpitkeTicketDO spitkeTicket);
}
