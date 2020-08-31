package com.renegade.controller;

import com.renegade.annotations.RenegadeAuth;
import com.renegade.annotations.RenegadeLimit;
import com.renegade.annotations.RenegadeLimit.RenegadeLimitType;
import com.renegade.config.AjaxJson;
import com.renegade.config.JSONUtils;
import com.renegade.constant.RedisNameConstants;
import com.renegade.constant.SysParamEnum;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.HangSellOrderDao;
import com.renegade.dao.M9UserMapper;
import com.renegade.dao.UserAddressDao;
import com.renegade.domain.FrontUserDO;
import com.renegade.domain.UserAddressDO;
import com.renegade.service.impl.FrontUserServiceImpl;
import com.renegade.util.common.HttpRequstDemoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年4月17日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Controller
public class FrontUserController {
	@Autowired
	private FrontUserServiceImpl frontUserServiceImplsss;
	@Autowired
	private UserAddressDao userAddressDao;
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private Environment env;
	@Autowired
	private M9UserMapper m9UserMapper;

	/**
	 * 登录
	 * 
	 * @param req
	 * @param phone
	 * @param pass
	 * @return
	 */

	@RequestMapping("/user/login")
	@RenegadeAuth
	@RenegadeLimit(RenegadeLimitType=RenegadeLimitType.RESUBMIT,count=1,periodTime=3,prefix=RedisNameConstants.t_renegae_limit,key="limitingRengadeUserLogin")
	public @ResponseBody AjaxJson userLogin(HttpServletRequest requests, String phone, String pass) {
		try {
			return frontUserServiceImplsss.login(phone, pass, requests);
		} catch (Exception e) {
			System.out.println(e);
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setSuccess(false);
			ajaxJson.setData("请求失败");
			return ajaxJson;
		}
	}

	/**
	 * 登录成功跳转页面
	 * 
	 * @return
	 */
	@GetMapping("/user/center")
	/*
	 * public String login() { return "/user"; }
	 */


	/**
	 * 忘记登陆密码
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("/frogetLoginPassWord")
	@RenegadeAuth
	public AjaxJson frogetLoginPassWord(@RequestParam Map<String, Object> map) {
		return frontUserServiceImplsss.frogetLoginPassWord(map);
	}

	@ResponseBody
	@PostMapping("/updatePayPass")
	// 修改交易密码
	public AjaxJson updatePayPass(@RequestParam Map<String, Object> map, @SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			ajaxJson.setMsg("登陆失效请重新登录");
			return ajaxJson;
		}
		return frontUserServiceImplsss.updatePayPass(map, userId);
	}

	/**
	 * 地址
	 * 
	 * @return
	 */
	@GetMapping("/address")
	public String address(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "login";
		}
		List<UserAddressDO> userAddressDOs = userAddressDao.findUserAddressListByUserId(userId);
		if (userAddressDOs.size() > 0) {
			model.addAttribute("userAddressDOs", userAddressDOs);
		}
		return "address";
	}

	/**
	 * 添加 地址
	 * 
	 * @return
	 */
	@GetMapping("/addAddressStatic")
	public String addAddres(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "login";
		}
		/*
		 * List<UserAddressDO> userAddressDOs =
		 * userAddressDao.findUserAddressListByUserId(userId);
		 * model.addAttribute("userAddressDOs", userAddressDOs);
		 */
		return "userAddAddress";
	}

	/**
	 * 添加地址
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("/addAdressCity")
	public AjaxJson addAddres(@RequestParam Map<String, Object> map, @SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			ajaxJson.setMsg("登陆失效，请重新登录");
			//ajaxJson.setMsg(PropertyUtil.getProperty("loginTimeOut", getType()));
			return ajaxJson;
		}
		//map.put("type", getType());
		return frontUserServiceImplsss.addUserAddress(map, userId);
	}

	/**
	 * 修改 地址
	 * 
	 * @return
	 */
	@GetMapping("/updateAddressStatic/{addressId}")
	public String updateAddress(Model model, @PathVariable("addressId") Integer addressId,
			@SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "login";
		}
		UserAddressDO userAddressDOs = userAddressDao.get(addressId);
		model.addAttribute("list", userAddressDOs);
		return "edit_address";
	}

	/**
	 * 修改地址
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("/updateAddress")
	public AjaxJson updateAddress(@RequestParam Map<String, Object> map, @SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			ajaxJson.setMsg("登陆失效，请重新登录");
			//ajaxJson.setMsg(PropertyUtil.getProperty("loginTimeOut", getType()));
			return ajaxJson;
		}
		//map.put("type", getType());
		return frontUserServiceImplsss.updateAddress(map, userId);
	}

	/**
	 * 删除地址
	 * 
	 * @param addressId
	 * @param req
	 * @return
	 */
	@RequestMapping("/del_address")
	@ResponseBody
	public AjaxJson delAddress(int addressId, @SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			ajaxJson.setMsg("登陆失效，请重新登录");
			//ajaxJson.setMsg(PropertyUtil.getProperty("loginTimeOut", getType()));
			return ajaxJson;
		}
		if (userAddressDao.deleteAddres(addressId, userId) > 0) {
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("删除成功");
			//ajaxJson.setMsg(PropertyUtil.getProperty("scuccess", getType()));
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		ajaxJson.setMsg("删除失败");
		return ajaxJson;
	}
	/**
	 * 邀请码
	 * @return
	 */
	@GetMapping("/code")
	public String code(){
		return "/Generalization_code";
	}
	/**
	 * 邀请码页面信息加载
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("user/inivation/inivation_user_code")
	public @ResponseBody AjaxJson invitationUserDeta(HttpServletRequest req,
			@SessionAttribute("userId") String userId) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			Map<String, Object> map = m9UserMapper.findUserByuserId(userId);
			//域名：
			String url=StringUtils.join(SysParamEnum.qroderUrl.getName(), map.get("share_rqcode"));
			map.put("url", url);
			Map<String, Object> map1 = m9UserMapper.countNum(userId);
			map.putAll(map1);
			//查看直推人数和已经报单的直推人数
			ajaxJson.setSuccess(true);
			ajaxJson.setData(map);
			return ajaxJson;
		} catch (Exception e) {
			ajaxJson.setMsg("请求失败");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
	}
	@Autowired
	private  HangSellOrderDao hangSellOrderDao;
	//登录成功跳转页面
	// 个人中心
	@GetMapping("/getUserInfo")
	public String getUserInfo(HttpServletRequest requests, Model model) {
		try {
			HttpSession session = requests.getSession();
			if (session.getAttribute("userId") == null) {
				return "/login";
			}
			FrontUserDO frontUserDO = frontUserServiceImplsss
					.get(Integer.parseInt(session.getAttribute("userId").toString()));
			if (frontUserDO == null) {
				return "/login";
			}
			
			model.addAttribute("frontUserDO", frontUserDO);
			Map<String, Object> map=new HashMap<>();
			map.put("hangSellUser",  session.getAttribute("userId"));
			model.addAttribute("countOrder", hangSellOrderDao.count(map));
			//查询伞下团队人数
			int teamSon=frontUserServiceImplsss.underNumAll(frontUserDO.getUserId());
			model.addAttribute("teamSon",teamSon);
			switch (frontUserDO.getUserLevel()) {
			case 0:
				model.addAttribute("level", "游客");
				break;
			case 1:
				model.addAttribute("level", "F1");
				break;
			case 2:
				model.addAttribute("level", "F2");
				break;
			case 3:
				model.addAttribute("level", "F3");
				break;
			case 4:
				model.addAttribute("level", "F4");
				break;
			case 5:
				model.addAttribute("level", "F5");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "/user";
	}


	// 我的钱包地址
	@GetMapping("/wallet")
	public String wallet(Model model, @SessionAttribute("userId") Integer userId) {
		try {
			if (userId == null) {
				return "/login";
			}
			FrontUserDO frontUserDO = frontUserServiceImplsss.get(userId);
			if (frontUserDO != null && frontUserDO.getUserWalletAddress() != null) {
				model.addAttribute("walletAddress", frontUserDO.getUserWalletAddress());
				model.addAttribute("walletAddressUt", frontUserDO.getUserWalletAddressUt());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "/wallet";
	}
	
	@GetMapping("/Invitate")
	public String myTeanView() {
		return "Invitate";
		
	}

	// 我的团队
	@PostMapping("/myTean")
	@ResponseBody
	public AjaxJson myTean(@SessionAttribute("userId") String userId,Integer lastId) {
		AjaxJson ajaxJson=new AjaxJson();
				
		// 个人信息包含直推人数
		// 伞下总人数
//		int count = frontUserDao.underNumAll(userId);
//		model.addAttribute("count", count);
		List<Map<String, Object>> users=m9UserMapper.findDirectUser(userId,lastId);
		ajaxJson.setSuccess(true);
		ajaxJson.setData(users);
		return ajaxJson;
	}

	// 我消费明细(分为usdt明细，ut明细，vs明细）
	@GetMapping("/flowerRecord")
	public String flowerRecord(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "login";
		}
			List<Map<String, Object>> ma = frontUserDao.getFlowerRecordCN(userId);
			if (ma.size() > 0) {
				model.addAttribute("maps", ma);
			}
		return "/myFlowerRecord";
	}

	// 实名验证接口
	@PostMapping("/reaLNameTest")
	@ResponseBody
	public AjaxJson reaLNameTest(@RequestParam Map<String, Object> map) {
		AjaxJson ajaxJson = new AjaxJson();
		String url = env.getProperty("renegade.realNameUrl");
		String key = env.getProperty("renegade.realNameKey");
		map.put("key", key);
		map.put("detail", "1");
		try {
			String reString = HttpRequstDemoUtil.net(url, map, "GET");
//			JSONObject jsonObject=JSONObject.parseObject(reString);
//			JSONObject   result=JSONObject.parseObject(jsonObject.getString("result"));
			Map<String, Object> map2 = JSONUtils.jsonToMap(reString);
			if (((Map<String, Object>) map2.get("result")).get("rescode").toString().equals("11")) {
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
			System.out.println("============="+((Map<String, Object>) map2.get("result")).get("rescode"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		return ajaxJson;
	}


	@ResponseBody
	@PostMapping("/loginOut")
	private AjaxJson loginOut(SessionStatus sessionStatus,HttpSession session) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		session.removeAttribute("userId");
		session.invalidate();
		sessionStatus.setComplete();
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}

}
