package com.renegade.controller;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.renegade.annotations.RenegadeAuth;
import com.renegade.annotations.RenegadeAuthSign;
import com.renegade.annotations.RenegadeLimit;
import com.renegade.config.AjaxJson;
import com.renegade.config.RegexUtil;
import com.renegade.constant.RedisNameConstants;
import com.renegade.domain.ParamDO;
import com.renegade.service.FrontUserService;
import com.renegade.service.M9UserService;
import com.renegade.service.ParamService;
import com.renegade.service.VerifyRecordService;
import com.renegade.util.DateUtil;
import com.renegade.util.common.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/m9/user")
public class M9UserController {

	@Autowired
	private M9UserService m9UserService;

	@Autowired
	private ParamService paramService;

	@Autowired
	private VerifyRecordService verifyRecordService;
	@Autowired
	private FrontUserService frontUserServiceImpl;
	
	@Autowired
	private ParamService paramServiceImpl;

	@PostMapping("/register")
	@RenegadeAuth
	@RenegadeAuthSign
	@ResponseBody
	public AjaxJson register(@RequestParam Map<String, Object> map) throws Exception {
		return m9UserService.register(map);
	}

	@PostMapping("/login")
	@RenegadeAuth
	@RenegadeAuthSign
	@RenegadeLimit(RenegadeLimitType = RenegadeLimit.RenegadeLimitType.RESUBMIT, count = 1, periodTime = 30, prefix = RedisNameConstants.t_renegae_limit, key = "limitingRengadeUserLogin")
	@ResponseBody
	public AjaxJson login(HttpSession session, @RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = m9UserService.login(map);
		if (ajaxJson.isSuccess()) {
			session.setAttribute("userId", ajaxJson.getData().toString());
		}
		return ajaxJson;
	}

	/***
	 * 灯火详情
	 * @param requests
	 * @param model
	 * @return
	 */
	@GetMapping("/lamp")
	@RenegadeAuth
	public String lamp(HttpServletRequest requests,Model model) throws Exception{
		HttpSession session = requests.getSession();
		String zone = requests.getParameter("zone");
		if (session.getAttribute("userId") == null) {
			return "/login";
		}

		Map<String,Object> map = new HashMap<>();

		String userId = session.getAttribute("userId").toString();
		Map<String, Object> user_param_map = getUserInfo(userId);

		if("4".equals(zone)){
			map.put("light_red",user_param_map.get("light4_red"));
			map.put("light_blue",user_param_map.get("light4_blue"));
			map.put("light_green",user_param_map.get("light4_green"));
		}else if("40".equals(zone)){
			map.put("light_red",user_param_map.get("light40_red"));
			map.put("light_blue",user_param_map.get("light40_blue"));
			map.put("light_green",user_param_map.get("light40_green"));
		}
		model.addAttribute("user", map);
		model.addAttribute("zone",zone);
		return "lamp";
	}

	/***
	 * 推荐人详情
	 * @param requests
	 * @param model
	 * @return
	 */
	@GetMapping("/Invitate")
	@RenegadeAuth
	public String Invitate(HttpServletRequest requests, Model model) throws Exception{

		return "Invitate";
	}

	/***
	 * 灯火详情-明细
	 * 灯火明细，红灯+1，绿-1，蓝灯+1 等
	 * @param requests
	 * @param map
	 * @return
	 */
	@PostMapping("/select_flowing_record_light")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson select_flowing_record_light(HttpServletRequest requests, @RequestParam Map<String, Object> map) throws Exception{
		AjaxJson ajaxJson = new AjaxJson();
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}

		String userId = session.getAttribute("userId").toString();
		map.put("user_id",userId);
		return m9UserService.select_flowing_record_light(map);
	}

	/***
	 * 排单币-我的-财务明细
	 * 排单币明细，挂求购单，充币，账户激活 等
	 * @param requests
	 * @param map
	 * @return
	 */
	@PostMapping("/select_flowing_record")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson select_flowing_record(HttpServletRequest requests, @RequestParam Map<String, Object> map) throws Exception{
		AjaxJson ajaxJson = new AjaxJson();
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}

		String userId = session.getAttribute("userId").toString();
		map.put("user_id",userId);
		return m9UserService.select_flowing_record(map);

	}


	/***
	 * 兑换灯火
	 * @param requests
	 * @param map
	 * @return
	 */
	@PostMapping("/exchange_light")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson exchange_light(HttpServletRequest requests, @RequestParam Map<String, Object> map) throws Exception{
		HttpSession session = requests.getSession();
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("兑换成功");
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}

		String userId = session.getAttribute("userId").toString();
		String quantity = requests.getParameter("quantity");

		return m9UserService.exchange_light(quantity,userId,map);

	}



	// 登录成功跳转页面
	// 个人中心
	@GetMapping("/index")
	@RenegadeAuth
	public String index(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		System.out.println("===" + session);
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		String userId = session.getAttribute("userId").toString();

		//界面显示是否已签到true为可以签到,false为今日已签到，不能签到
		String sign_date = DateUtil.get_format4_today();
		String qdjf = "0";
		boolean issign = true;
		List<Map<String,Object>> list = m9UserService.select_sign_record(userId,sign_date);
		if(list.size()>0){
			issign = false;
			Map<String,Object> map = list.get(0);
			qdjf = map.get("prize").toString();
		}

		model.addAttribute("user", getUserInfo(userId));
		model.addAttribute("wx", paramServiceImpl.findValue("wx1").getParamValue());
		model.addAttribute("sign",issign);
		model.addAttribute("qdjf",qdjf);
		return "/index";
	}

	@GetMapping("/sign_recode")
	@RenegadeAuth
	public String sign_recode(HttpServletRequest requests, Model model){
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));
		return "/sign_recode";
	}

	@GetMapping("/self")
	@RenegadeAuth
	public String self(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));
		return "/self";
	}

	@GetMapping("/transaction")
	@RenegadeAuth
	public String transaction(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		String userId = session.getAttribute("userId").toString();
		String zone = requests.getParameter("zone");
		model.addAttribute("zone",zone);
		model.addAttribute("user", getUserInfo(userId));
		if("4".equals(zone)){
			ParamDO param = paramServiceImpl.findValue("m9_lineup_count4");
			model.addAttribute("lineup_count",param.getParamValue());
		}else if("40".equals(zone)){
			ParamDO param = paramServiceImpl.findValue("m9_lineup_count40");
			model.addAttribute("lineup_count",param.getParamValue());
		}

		return "/transaction";
	}

	@GetMapping("/setLoginPwd")
	@RenegadeAuth
	public String setLoginPwd(HttpServletRequest requests) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		return "/set_pwd";
	}

	@PostMapping("/updateUser")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson updateUser(HttpSession session, @RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("修改成功");
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}
		String userId = session.getAttribute("userId").toString();
		map.put("id", userId);
		int cnt = m9UserService.updateUser(map);
		if (cnt < 1) {
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("修改失败");
		}
		return ajaxJson;
	}

	@PostMapping("/uploadFile")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson uploadFile(HttpServletRequest requests,@RequestParam("browerfile")  MultipartFile browerfile,
			@RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("修改成功");

		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}
//		String file = ((StandardMultipartHttpServletRequest.StandardMultipartFile) browerfile).filename;
		if (browerfile.isEmpty()) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("图片不能为空");
			return ajaxJson;
		}

		AjaxJson data1 = FileUpload.uploadImg(browerfile);

		// 上传图片成功
		if (data1.isSuccess()) {
			map.put("user_poto", data1.getData());
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("上传图片失败");
			return ajaxJson;
		}

		String userId = session.getAttribute("userId").toString();
		map.put("id", userId);
		m9UserService.updateUser(map);

		return ajaxJson;
	}

	@PostMapping("/addCoin")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson addCoin(HttpServletRequest requests,@RequestParam("browerfile") MultipartFile browerfile,
			@RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("充值成功,等待审核");

		String change_num = requests.getParameter("change_num");
		System.out.println("change_num==" + change_num);
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}

		if (browerfile.isEmpty()) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("图片不能为空");
			return ajaxJson;
		}
		// 数量校验
		if (!RegexUtil.isValidNumFloat4S(String.valueOf(map.get("change_num")))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请输入正确的数量");
			return ajaxJson;
		}

//        Map<String,Object> map = new HashMap<>();
//        map.put("change_num",wChangeLogDO.getChange_num());
//        map.put("change_no",wChangeLogDO.getChange_no());
		// 上传图片到七牛云
		AjaxJson data1 = FileUpload.uploadImg(browerfile);
		// 上传图片成功
		if (data1.isSuccess()) {
			map.put("change_picture", data1.getData());
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("上传图片失败");
			return ajaxJson;
		}

		String userId = session.getAttribute("userId").toString();
		map.put("change_userid", userId);
		ParamDO pdo = paramService.findValue("m9_eos_adress");
		String eos_address = pdo.getParamValue();
		map.put("change_addres", eos_address);
		int cnt = m9UserService.insert_change_log(map);
		if (cnt < 1) {
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("充值失败");
		}

		return ajaxJson;
	}

	@PostMapping("/sendCode")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson sendCode(HttpServletRequest requests, @RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}
		String userId = session.getAttribute("userId").toString();
		Map<String, Object> usermap = new HashMap<>();
		usermap.put("id", userId);
		List<Map<String, Object>> list = m9UserService.selectUserList(usermap);
		Map<String, Object> umap = list.get(0);
		String busType = map.get("busType")==null?"updatePass":map.get("busType").toString();
		ajaxJson = verifyRecordService.addVerifyRecord(umap.get("telphone").toString(), busType, "1");
		return ajaxJson;
	}

	@PostMapping("/updateLoginPwd")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson updateLoginPwd(HttpServletRequest requests, @RequestParam Map<String, Object> map)
			throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("修改成功");

		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}


//		if("D41D8CD98F00B204E9800998ECF8427E".equals(map.get("login_pwd"))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("登陆密码不能为空");
//			return ajaxJson;
//		}
//
//		if("D41D8CD98F00B204E9800998ECF8427E".equals(map.get("login_pwd1"))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("登陆密码不能为空");
//			return ajaxJson;
//		}
//
//		if(StringUtils.isEmpty(String.valueOf(map.get("code")))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("验证码不能为空");
//			return ajaxJson;
//		}
//
//		if(!map.get("login_pwd").equals(map.get("login_pwd1"))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("登陆密码不一致");
//			return ajaxJson;
//		}

		Map<String, Object> mapself = new HashMap<>();
		String userId = session.getAttribute("userId").toString();
		map.put("type_pwd", "login");
		map.put("id", userId);
		mapself.put("id", userId);
		List<Map<String, Object>> list = m9UserService.selectUserList(mapself);
		Map<String, Object> umap = list.get(0);
		map.put("telphone", umap.get("telphone").toString());
		return m9UserService.updateUserPwd(map);
	}

	@PostMapping("/updatePayPwd")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson updatePayPwd(HttpServletRequest requests, @RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("修改成功");

		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}

//		if(StringUtils.isEmpty(String.valueOf(map.get("login_pwd")))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("支付密码不能为空");
//			return ajaxJson;
//		}
//
//		if(StringUtils.isEmpty(String.valueOf(map.get("login_pwd1")))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("支付密码不能为空");
//			return ajaxJson;
//		}
//
//		if(StringUtils.isEmpty(String.valueOf(map.get("code")))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("验证码不能为空");
//			return ajaxJson;
//		}
//
//		if(!map.get("login_pwd").equals(map.get("login_pwd1"))){
//			ajaxJson.setSuccess(false);
//			ajaxJson.setMsg("支付密码不一致");
//			return ajaxJson;
//		}


		Map<String, Object> mapself = new HashMap<>();
		String userId = session.getAttribute("userId").toString();
		map.put("type_pwd", "pay");
		map.put("id", userId);
		mapself.put("id", userId);
		List<Map<String, Object>> list = m9UserService.selectUserList(mapself);
		Map<String, Object> umap = list.get(0);
		map.put("telphone", umap.get("telphone").toString());
		return m9UserService.updateUserPwd(map);
	}

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

	/***
	 * 排单币明细查询
	 * 
	 * @return
	 */
	@RequestMapping("/mingxi")
	@RenegadeAuth
	public String coinDetail(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		String userId = session.getAttribute("userId").toString();

		Map<String, Object> map = new HashMap<>();
		map.put("user_id", userId);
//		List<Map<String, Object>> list = m9UserService.select_flowing_record(map);
//		model.addAttribute("lineupOrders", list);
		return "mingxi";
	}

	@GetMapping("/address")
	@RenegadeAuth
	public String address(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}

		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));

		return "/address";
	}

	@GetMapping("/add_address")
	@RenegadeAuth
	public String add_address(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}

		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));
		return "add_address";
	}

	@PostMapping("/updateAddress")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson updateAddress(HttpServletRequest requests, @RequestParam Map<String, Object> map) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("地址修改成功");

		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}
		String userId = session.getAttribute("userId").toString();
		map.put("id", session.getAttribute("userId").toString());
		ajaxJson = m9UserService.updateAddress(map);

		return ajaxJson;
	}

	/**
	 * 个人中心，修改昵称、头像
	 * 
	 * @return
	 */
	@GetMapping("/set_my")
	@RenegadeAuth
	public String set_my(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}

		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));

		return "/set_my";
	}

	/**
	 * 个人中心，修改昵称
	 * 
	 * @return
	 */
	@GetMapping("/set_name")
	@RenegadeAuth
	public String set_name(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}

		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));

		return "/set_name";
	}

	/**
	 * 个人中心，修改头像
	 * 
	 * @return
	 */
	@GetMapping("/set_photo")
	@RenegadeAuth
	public String set_photo(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}

		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));

		return "/set_photo";
	}

	/**
	 * 我的-充币页面
	 * 
	 * @return
	 */
	@GetMapping("/charging")
	@RenegadeAuth
	public String charging(HttpServletRequest requests, Model model) throws Exception {
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			return "/login";
		}
		String userId = session.getAttribute("userId").toString();
		model.addAttribute("user", getUserInfo(userId));
		ParamDO pdo = paramService.findValue("m9_eos_adress");
		ParamDO pdo1 = paramService.findValue("eos_address_label");
		String eos_address = pdo.getParamValue();
		String eos_address_label = pdo1.getParamValue();
		model.addAttribute("m9_eos_adress", eos_address);
		model.addAttribute("eos_address_label", eos_address_label);
		return "/charging";
	}

	private Map<String, Object> getUserInfo(String userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", userId);
		List<Map<String, Object>> list = m9UserService.selectUserList(map);
		Map<String, Object> userMap = list.get(0);
		return userMap;
	}

	/**
	 * 忘记登陆密码
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("/frogetLoginPassWordM9")
	@RenegadeAuthSign
	@RenegadeAuth
	public AjaxJson frogetLoginPassWord(@RequestParam Map<String, Object> map)  throws Exception{
		return frontUserServiceImpl.frogetLoginPassWord(map);
	}

	@PostMapping("/insert_sign_record")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson insert_sign_record(HttpServletRequest requests){
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("签到成功");
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}
		String userId = session.getAttribute("userId").toString();
		return m9UserService.insert_sign_record(userId);
	}

	@PostMapping("/select_sign_record")
	@RenegadeAuth
	@ResponseBody
	public AjaxJson select_sign_record(HttpServletRequest requests){
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("查询成功");
		HttpSession session = requests.getSession();
		if (session.getAttribute("userId") == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("登录超时,请重新登陆");
			return ajaxJson;
		}
		String userId = session.getAttribute("userId").toString();
		String lastId = requests.getParameter("lastId");

		return m9UserService.select_sign_record(userId,Integer.parseInt(lastId));
	}
}
