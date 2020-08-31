package com.renegade.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.alibaba.fastjson.JSONObject;
import com.renegade.annotations.RenegadeAuth;
import com.renegade.config.AjaxJson;
import com.renegade.dao.AiRobotMovesBricksDao;
import com.renegade.dao.ExchangeDao;
import com.renegade.dao.FrontNoticeDao;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.SpitkeTicketDao;
import com.renegade.dao.SpotGoodsDao;
import com.renegade.dao.WalletManagerDao;
import com.renegade.domain.AiRobotMovesBricksDO;
import com.renegade.domain.FrontNoticeDO;
import com.renegade.domain.SpitkeTicketDO;
import com.renegade.domain.SpotGoodsDO;
import com.renegade.domain.WalletManagerDO;
import com.renegade.exception.BDException;
import com.renegade.filter.XssHttpServletRequestWrapper;
import com.renegade.service.AiDogSerivice;
import com.renegade.service.ParamService;
import com.renegade.service.UsdtChargeService;
import com.renegade.service.impl.ParamServiceImpl;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月18日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Controller
public class IndexController {
	@Autowired
	private FrontNoticeDao frontNoticeDao;
	@Autowired
	private ParamService paramServiceImpl;


	/**
	 * 公告所有查询
	 * 
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/myNoticeCenter")
	public String myNoticeCenter(@SessionAttribute("userId") Integer userId, Model model) {
		if (userId == null) {
			return "/login";
		}
		Map<String, Object> map = new HashMap<>();
		// 公告
		List<FrontNoticeDO> frontNoticeDOs = frontNoticeDao.listAll();
		model.addAttribute("frontNoticeDOs", frontNoticeDOs);
		return "/Notice_list";
	}

	/**
	 * 公告某条查询
	 * 
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/editNotice/{noticeId}")
	String edit(@PathVariable("noticeId") Integer noticeId, Model model) {
		FrontNoticeDO frontNotice = frontNoticeDao.get(noticeId);
		String notice = frontNotice.getNoticeDesc().replaceAll("</?[^>]+>", "");
		notice=notice.replace("Document","");
		notice=notice.replace("img","");
		notice=notice.replace("}","");
		notice=notice.replace("max-width: 100%;","");
		notice=notice.replace("{","");
		System.out.println("==="+notice);
		frontNotice.setNoticeDesc(notice);
		model.addAttribute("frontNotice", frontNotice);
		return "/Notice";
	}

	@Autowired
	private UsdtChargeService usdtChargeServiceImpl;

	// usdt和ut兑换成vs
	@PostMapping("/exchange")
	@ResponseBody
	public AjaxJson exchange(@SessionAttribute("userId") Integer userId, String num, String type, String passWord) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(true);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		return usdtChargeServiceImpl.exchange(userId, num, type, passWord);

	}
	//客服细腻
	@PostMapping("/kefuInfo")
	@ResponseBody
	public AjaxJson kefuInfo(@SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		String wx=paramServiceImpl.findValue("wx1").getParamValue();
		String email=paramServiceImpl.findValue("email").getParamValue();
		String emailGf=paramServiceImpl.findValue("emailGf").getParamValue();
		String workTimeKe=paramServiceImpl.findValue("workTimeKe").getParamValue();
		Map<String, Object> map=new HashMap<>();
		map.put("wx", wx);
		map.put("email", email);
		map.put("emailGf", emailGf);
		map.put("workTimeKe", workTimeKe);
		ajaxJson.setSuccess(true);
		ajaxJson.setData(map);
		return ajaxJson;
		
	}
}
