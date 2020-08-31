package com.renegade.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.renegade.config.JSONUtils;
import com.renegade.dao.FrontInformationDao;
import com.renegade.domain.FrontInformationDO;
import com.renegade.redis.RedisUtil;
import com.renegade.util.common.HttpRequstDemoUtil;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月25日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Controller
public class QuotationController {
	@Autowired
	private FrontInformationDao frontInformationDao;
	@Autowired
	private Environment env;
	@Autowired
	private RedisUtil redisUtil;
	private final static Logger logger=LoggerFactory.getLogger(QuotationController.class);
	private final static String key="renegadeCoin";

	// 行情(调取浪啊接口)
	@GetMapping("/quotation")
	public String quotation(@SessionAttribute("userId") Integer userId, Model model) throws Exception {
		if (userId == null) {
			return "/login";
		}
		String redisRult=(String) redisUtil.get(key);
		logger.debug("=======redis========={}", redisRult);
		if (redisRult==null) {
			 redisRult=HttpRequstDemoUtil.net(env.getProperty("renegade.informationUrl"), null, "POST");
			 redisUtil.set(key, redisRult, (long) 180);//
		}
		List<Map<String, Object>> json=JSONUtils.parseListGenericity(redisRult, List.class, Map.class);
		model.addAttribute("maps", json);
		return "/quotation";
	}

	// 资讯
	@GetMapping("/informationView")
	public String informationView(@SessionAttribute("userId") Integer userId, Model model) {
		if (userId == null) {
			return "/login";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("sort", "create_time");
		map.put("order", "DESC");
		List<FrontInformationDO> fInformationDOs = frontInformationDao.list(map);
		model.addAttribute("fInformationDOs", fInformationDOs);
		return "/quotation_news";
	}

	// 资讯详情
	@GetMapping("/quotationNewsDetail/{noticeId}")
	public String quotationNewsDetail(@SessionAttribute("userId") Integer userId, Model model,
			@PathVariable("noticeId") Integer noticeId) {
		if (userId == null) {
			return "/login";
		}
		// 请求之后次数据加1
		FrontInformationDO fInformationDO = frontInformationDao.get(noticeId);
		fInformationDO.setLookNum(fInformationDO.getLookNum() + 1);
		// 修改
		frontInformationDao.update(fInformationDO);
		model.addAttribute("fInformationDO", fInformationDO);
		return "quotation_news_detail";

	}
}
