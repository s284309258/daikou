package com.renegade.controller;

import com.renegade.annotations.RenegadeAuth;
import com.renegade.dao.FrontUserDao;
import com.renegade.domain.FrontUserDO;
import com.renegade.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private ParamService paramServiceImpl;

	@GetMapping({ "/", "" })
	@RenegadeAuth
	String welcome(Model model) {
		return "redirect:/login";
	}

	/**
	 * 登录页面
	 * 
	 * @return
	 */
	@GetMapping("/login")
	@RenegadeAuth
	public String login(String lan, HttpServletRequest req) {
		HttpSession session = req.getSession();
		return "/login";
	}

	/***
	 * 底部首页菜单
	 * 
	 * @return
	 */
	@GetMapping("/index")
	@RenegadeAuth
	public String index(){
		return "/index";
	}

    /***
     * 底部导航栏
     * @return
     */
    @GetMapping("/nav")
	@RenegadeAuth
    public String nav(){
        return "/nav";
    }

	/***
	 * 底部订单菜单
	 * 
	 * @return
	 */
	@GetMapping("/Order")
	@RenegadeAuth
	public String Order(){
		return "/Order";
	}

	/***
	 * 底部我的菜单
	 * 
	 * @return
	 */
	@GetMapping("/self")
	@RenegadeAuth
	public String self(){
		return "/self";
	}

	/***
	 * 我的充币按钮
	 * 
	 * @return
	 */
	@GetMapping("/charging")
	@RenegadeAuth
	public String charging(){
		return "charging";
	}

	/***
	 * 系统设置按钮
	 * 
	 * @return
	 */
	@GetMapping("/Safety")
    @RenegadeAuth
	public String Safety(){
		return "Safety";
	}

	/***
	 * 系统设置按钮
	 * 
	 * @return
	 */
	@GetMapping("/mingxi")
	@RenegadeAuth
	public String mingxi(){
		return "mingxi";
	}

	/**
	 * 退出页面
	 * 
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("userId",null);
		return "/login";
	}

	/**
	 * 修改登陆密码
	 *
	 * @return
	 */
	@GetMapping("/set_pwd")
	@RenegadeAuth
	public String set_pwd(){
		return "set_pwd";
	}

	/**
	 * 个人中心
	 *
	 * @return
	 */
	@GetMapping("/set_my")
	@RenegadeAuth
	public String set_my(){
		return "set_my";
	}

	/**
	 * 修改交易密码
	 *
	 * @return
	 */
	@GetMapping("/set_pwd1")
	@RenegadeAuth
	public String set_pwd1(){
		return "set_pwd1";
	}

	/**
	 * 修改昵称
	 *
	 * @return
	 */
	@GetMapping("/set_name")
	@RenegadeAuth
	public String set_name(){
		return "set_name";
	}

	/**
	 * 修改头像
	 *
	 * @return
	 */
	@GetMapping("/set_photo")
	@RenegadeAuth
	public String set_photo(){
		return "set_photo";
	}

	/**
	 * 交易地址
	 *
	 * @return
	 */
	@GetMapping("/address1")
	@RenegadeAuth
	public String address(){
		return "address";
	}

	/**
	 * 注册页面第一步
	 * 
	 * @return
	 */
	@GetMapping("/register")
	@RenegadeAuth
	public String register(String inviteCode, Model model) {
		if (inviteCode != null) {
			model.addAttribute("inviteCode", inviteCode);
		}
		return "/register";
	}

	/**
	 * 注册页面第二步
	 * 
	 * @return
	 */
	@GetMapping("/registerTwo")
	@RenegadeAuth
	public String registerTwo(String userName, String identityNo, String pid, Model mode) {
		mode.addAttribute("userName", userName);
		mode.addAttribute("identityNo", identityNo);
		return "/register_1";
	}

	@GetMapping("/find_pwd")
	@RenegadeAuth
	public String find_pwd() {
		return "/find_pwd";
	}

	@GetMapping("/setupView")
	public String setupView() {
		return "/setup";

	}

	@GetMapping("/edit_pay_pwd")
	public String edit_pay_pwd(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "/login";
		}
		FrontUserDO frontUserDO = frontUserDao.get(userId);
		model.addAttribute("frontUserDO", frontUserDO);
		return "/edit_pay_pwd";

	}

	@GetMapping("/invite")
	@RenegadeAuth
	public String invite(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "/login";
		}
		FrontUserDO frontUserDO = frontUserDao.get(userId);
		model.addAttribute("frontUserDO", frontUserDO);
		return "/invite";

	}


	@GetMapping("/tibi")
	public String tibi(@SessionAttribute("userId") Integer userId, Model model) {
		if (userId == null) {
			return "/login";
		}
		FrontUserDO frontUserDO = frontUserDao.get(userId);
		model.addAttribute("frontUserDO", frontUserDO);
		return "/tibi";
	}

	/***
	 * 中英文渲染
	 * 
	 * @param req
	 * @param lan
	 * @param model
	 * @return
	 */
	@GetMapping("/loginLanguage")
	@RenegadeAuth
	public String loginLanguage(HttpServletRequest req, String lan, Model model) {
		HttpSession session = req.getSession();
		if ("EN".equals(lan) || "en".equals(lan)) {
			session.setAttribute("type", "EN");
			model.addAttribute("type", "EN");
		} else {
			session.setAttribute("type", "CN");
			model.addAttribute("type", "CN");
		}
		return "/login";
	}

	@GetMapping("/405")
	public String error() {
		return "/error/405";
	}

	@GetMapping("/406")
	public String error6() {
		return "/error/406";
	}

	@GetMapping("/407")
	public String error7() {
		return "/error/407";
	}
	/**
	 * 忘记面貌
	 * @return
	 */
	@GetMapping("/frogetPassView")
	@RenegadeAuth
    public String frogetPassView() {
		return "/Retrieve";
		
	}
}
