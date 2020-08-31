package com.renegade.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.renegade.annotations.RenegadeAuthSign;
import com.renegade.config.AjaxJson;
import com.renegade.dao.*;
import com.renegade.domain.FrontFeedbackDO;
import com.renegade.domain.FrontUserDO;
import com.renegade.domain.WalletManagerDO;
import com.renegade.filter.XssHttpServletRequestWrapper;
import com.renegade.service.ParamService;
import com.renegade.service.UsdtChargeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年5月24日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Controller
public class UserFallBackConroller extends BaseController {
	@Autowired
	private FrontFeedbackDao frontFeedbackDao;
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private UsdtChargeService usdtChargeServiceImpl;
	@Autowired
	private FrontTiBiLogDao frontTiBiLogDao;
	@Autowired
	private WalletManagerDao walletManagerDao;
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private M9UserMapper m9UserMapper;
	
	// 图片上传秘钥
	public static final String qinduyun_public_key = "OwlrZ-TAZ8kTdDYP7ssYS2Dbmlc_WV2nHdeTqMcY";
	// 图片上传公钥
	public static final String qinduyun_private_key = "L5gEXOGiqgHXG8BrsmXwnFGVMPwm0zEDY2CjSpYm";
	// 图片创建名
	public static final String qinduyun_private_bucket = "vssystem";

	/**
	 * 上传图片到七牛云
	 * 
	 * @param file
	 * @return
	 */
	public AjaxJson uploadImg(MultipartFile file) {
		AjaxJson ajaxJson = new AjaxJson();
		Configuration cfg = new Configuration(Zone.zone0());
		String json = null;
		UploadManager uploadManager = new UploadManager(cfg);
		Auth auth = Auth.create(qinduyun_public_key, qinduyun_private_key);
		String upToken = auth.uploadToken(qinduyun_private_bucket);
		String fileName = UUID.randomUUID() + file.getOriginalFilename();
		String url = "http://cdn.yhsjtop.com/";
		try {
			Response response = uploadManager.put(file.getBytes(), fileName, upToken);
			if (response.statusCode == 200) {
				String rotationgoodsImg = url + fileName;
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				System.out.println("=========>>>>>>>>>>>>>>>>>>>>>>>>存贮到七牛云上的key值：" + putRet.key);
				System.out.println(putRet.hash);
				System.out.println("文件上传成功");
				json = rotationgoodsImg;
				ajaxJson.setMsg("上传成功");
				ajaxJson.setData(json);
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setMsg("上传失败");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		return null;
	}
    
	@RequestMapping(value = "/saveFeedBack", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveFeedBack(@RequestParam("file") MultipartFile[] file, FrontFeedbackDO feedbackDO,
			HttpServletRequest request) {
		AjaxJson ajaxJson = new AjaxJson();
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		if (feedbackDO.getCoontent() == null || StringUtils.isEmpty(feedbackDO.getCoontent())) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("必须填写相关内容");
			return ajaxJson;
		}
		FrontUserDO wFrontUser = frontUserDao.get(Integer.parseInt(session.getAttribute("userId").toString()));
		feedbackDO.setType(XssHttpServletRequestWrapper.replaceXSS(feedbackDO.getType()));
		feedbackDO.setCoontent(XssHttpServletRequestWrapper.replaceXSS(feedbackDO.getCoontent()));
		feedbackDO.setUserId(wFrontUser.getUserId());
		feedbackDO.setUserTel(wFrontUser.getUserTel());
		System.out.println("===========" + file.length);
		List<String> list = new ArrayList<>();
		if (file.length > 0) {
			for (MultipartFile multipartFile : file) {
				System.out.println("=========" + multipartFile.getOriginalFilename());
				AjaxJson data = uploadImg(multipartFile);
				if (data.isSuccess()) {
					list.add((String) data.getData());
				} else {
					ajaxJson.setMsg("失败");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
		}
		System.out.println("==============集合" + JSONObject.toJSONString(list));
		if (list.size() == 3) {
			feedbackDO.setPhoto1(list.get(0));
			feedbackDO.setPhoto2(list.get(1));
			feedbackDO.setPhoto3(list.get(2));
		}
		if (list.size() == 2) {
			feedbackDO.setPhoto1(list.get(0));
			feedbackDO.setPhoto2(list.get(1));
		}
		if (list.size() == 1) {
			feedbackDO.setPhoto1(list.get(0));
		}
		try {
			if (frontFeedbackDao.save(feedbackDO) > 0) {
				ajaxJson.setSuccess(true);
				ajaxJson.setMsg("提交成功");
				ajaxJson.setMsg("操作成功");
				return ajaxJson;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("信息填写有误");
			return ajaxJson;
		}
		return ajaxJson;
	}

	/***
	 * 用户申请提币 这里要涉及到汇率换算
	 * @param num
	 * @param sendAddress
	 * @param passWord
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendUsdtCoin", method = RequestMethod.POST)
	public AjaxJson sendUsdtCoin(BigDecimal num, String sendAddress, String passWord,
								 @SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		return usdtChargeServiceImpl.inserSendLog(num, sendAddress, userId, passWord, getType());
	}
    @GetMapping("/tibiView")
	 public String tibiView(@SessionAttribute("userId") Integer userId,Model model) {
    	if (userId==null) {
			return "login";
		}
    	WalletManagerDO walletManagerDO=walletManagerDao.getUserIdWallet(userId);
    	model.addAttribute("walletManagerDO", walletManagerDO);
		return "/tibi";
		
	}
	/**
	 * 用户提币记录
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/tibiOrder")
	public String tibiOrder(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "/login";
		}
		List<Map<String, Object>> maps = frontTiBiLogDao.findTiBiLogByUserId(userId);
		if (maps.size() > 0) {
			model.addAttribute("maps", maps);
		}
		return "/record_tibi";
	}

	// 转账模板
	@GetMapping("/transferAccountsUserView")
	public String transferAccountsUserView(@SessionAttribute("userId") String userId, Model model) {
		if (userId==null) {
			return "login";
		}
		Map<String, Object> map=m9UserMapper.findUserByuserId(userId);
		model.addAttribute("user", map);
		return "/Interturn";
	}

	/**
	 * 用户转账，type=1 usdt 2 ut 3 vs
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@PostMapping("/transferAccountsUser")
	@RenegadeAuthSign
	public AjaxJson transferAccountsUser(@RequestParam Map<String, Object> map,
			@SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setMsg("登陆超时请重新登录");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		return usdtChargeServiceImpl.transferAccountsUser(map, userId);
	}

	// 转账二维码
	@GetMapping("/tanferQurode")
	public String tanferQurode(Model model) {
		return "/cash_code";
	}

}
