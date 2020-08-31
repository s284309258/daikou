package com.renegade.controller;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.renegade.config.AjaxJson;
import com.renegade.config.StringUtil;
import com.renegade.dao.ApplyRecordDao;
import com.renegade.dao.FrontNoticeDao;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.GoodsCategoryDao;
import com.renegade.dao.HangSellOrderDao;
import com.renegade.dao.SpotGoodsDao;
import com.renegade.dao.SpotOrderDao;
import com.renegade.domain.ApplyRecordDO;
import com.renegade.domain.FileDO;
import com.renegade.domain.FrontFeedbackDO;
import com.renegade.domain.FrontNoticeDO;
import com.renegade.domain.GoodsCategoryDO;
import com.renegade.domain.SpotGoodsDO;
import com.renegade.domain.SpotOrder;
import com.renegade.filter.XssHttpServletRequestWrapper;
import com.renegade.service.ApplyRecordService;
import com.renegade.service.ParamService;
import com.renegade.service.SpotOrderSerivce;
import com.renegade.service.StoreManagerService;
import com.renegade.service.TaskService;
import com.renegade.service.impl.SpotOrderSerivceImpl;
import com.renegade.service.impl.StoreManagerServiceImpl;
import com.renegade.service.impl.TaskServiceImpl;
import com.renegade.util.FileType;
import com.renegade.util.FileUtil;
import com.renegade.util.R;
import com.renegade.util.UpLoadQinDuYun;

import javassist.expr.NewArray;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年6月14日
 * @version 1.0
 * @email 291312408@qq.com
 */
@Controller
public class StoreManagerController {
	@Autowired
	private ApplyRecordDao applyRecordDao;
	@Autowired
	private StoreManagerService storeManagerServiceImpl;
	@Autowired
	private GoodsCategoryDao goodsCategoryDao;
	@Autowired
	private SpotGoodsDao spotGoodsDao;
	@Autowired
	private SpotOrderSerivce spotOrderSerivceImpl;
	@Autowired
	private SpotOrderDao spotOrderDao;
	@Autowired
	private FrontNoticeDao frontNoticeDao;
	@Autowired
	private HangSellOrderDao hangSellOrderDao;
	@Autowired
	private ParamService paramServiceImpl;
	@Autowired
	private FrontUserDao frontUserDao;

	// 申请加盟，只能消费usdt或者ut
	@GetMapping("/participateparticipateView")
	public String participateparticipateView(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "/login";
		}
		return "/business_entry_1";
	}

	// 加盟步骤
	@ResponseBody
	@PostMapping("/participateDetail")
	public AjaxJson participateDetail(@RequestParam("file") MultipartFile[] file, ApplyRecordDO applyRecordDO,
			@SessionAttribute("userId") Integer userId) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("登录超时，请重新登录");
			return ajaxJson;
		}

		try {
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
			if (list.size() != 2) {
				ajaxJson.setSuccess(false);
				ajaxJson.setData("请上传身份证图像");
				return ajaxJson;
			}

			applyRecordDO.setIdcardPicture1(list.get(0));
			applyRecordDO.setIdcardPicture2(list.get(1));
			applyRecordDO.setUserId(userId);
			applyRecordDO.setStoreName(XssHttpServletRequestWrapper.replaceXSS(applyRecordDO.getStoreName()));
			applyRecordDO.setStoreMajor(XssHttpServletRequestWrapper.replaceXSS(applyRecordDO.getStoreMajor()));
			return storeManagerServiceImpl.participateDetail(applyRecordDO);

		} catch (Exception e) { // TODO: handle exception e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("信息填写有误");
			return ajaxJson;
		}

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

	

	// 我的 商品管理
	@GetMapping("/goodsManager")
	public String goodsManager(Model model, @SessionAttribute("userId") Integer userId, Integer flag) {
		if (userId == null) {
			return "/login";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("goodsUserId", userId);
		List<SpotGoodsDO> list2 = spotGoodsDao.list(map);
		model.addAttribute("listGoods", list2);
		if (flag == 1) {
			model.addAttribute("type", 1);
		} else if (flag == 2) {
			model.addAttribute("type", 2);
		} else if (flag == 3) {
			model.addAttribute("type", 3);
		}
		return "/business_manage";
	}

	// 添加商品视图
	@GetMapping("/addSotreGoodsView")
	public String addSotreGoodsView(Model model, @SessionAttribute("userId") Integer userId) {
		if (userId == null) {
			return "/login";
		}
		Map<String, Object> map = new HashMap<>();
		List<GoodsCategoryDO> list = goodsCategoryDao.list(map);
		// 从参数表中查询商品价格
		BigDecimal goodsOriginPrice = new BigDecimal(paramServiceImpl.findValue("goodsOriginPrice").getParamValue());
		BigDecimal goodsPrice = new BigDecimal(paramServiceImpl.findValue("goodsPrice").getParamValue());
		model.addAttribute("listCategory", list);
		model.addAttribute("goodsOriginPrice", goodsOriginPrice);
		model.addAttribute("goodsPrice", goodsPrice);
		return "/business_addgoods";
	}

	private final static Logger logger = LoggerFactory.getLogger(StoreManagerController.class);

	// 添加action
	@ResponseBody
	@PostMapping("/addSocreGoods")
	public AjaxJson addSocreGoods(@SessionAttribute("userId") Integer userId, SpotGoodsDO spotGoodsDO,
			@RequestParam("file1") MultipartFile file1, @RequestParam("uploadFile") MultipartFile[] uploadFile,
			HttpServletRequest request) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		if (file1 == null || uploadFile.length <= 0) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("图片不能为空");
			return ajaxJson;
		}
		if (uploadFile.length>5) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("图片最多上传五张");
			return ajaxJson;
		}
		System.out.println("===有限的======" + file1.getOriginalFilename() + "===个数==========" + uploadFile.length);
		AjaxJson data1 = uploadImg(file1);
		String data2 = "";
		for (MultipartFile multipartFile : uploadFile) {
			System.out.println("=======测试走一波==" + multipartFile.getOriginalFilename());
			AjaxJson data = uploadImg(multipartFile);
			if (data.isSuccess()) {
				data2 = data2 + (String) data.getData()+',';
			} else {
				ajaxJson.setMsg("失败");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		}
		data2=data2.substring(0,data2.lastIndexOf(","));
		if (data1.isSuccess()) {
			spotGoodsDO.setGoodsUserId(userId);
			spotGoodsDO.setGoodsDesc(data1.getData().toString());
			spotGoodsDO.setGoodsPicture(data2);
			return spotOrderSerivceImpl.addSocreGoods(spotGoodsDO);
		} else {
			ajaxJson.setMsg("失败");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}

	}

	// 编辑商品 视图
	@GetMapping("/edictScoreGoodsView/{goodsId}")
	public String edictScoreGoodsView(Model model, @SessionAttribute("userId") Integer userId,
			@PathVariable("goodsId") Integer goodsId) {
		if (userId == null) {
			return "/login";
		}
		Map<String, Object> map = new HashMap<>();
		List<GoodsCategoryDO> list = goodsCategoryDao.list(map);// 商品类型
		model.addAttribute("listCategory", list);
		// 商品类型
		map.put("goodsUserId", userId);
		map.put("goodsId", goodsId);
		SpotGoodsDO spotOrder = spotGoodsDao.listTypeGoods(map);
		// 查询类型名称
		String name = goodsCategoryDao.findNameById(Integer.parseInt(spotOrder.getGoodsCategory()));
		model.addAttribute("spotOrder", spotOrder);
		model.addAttribute("name", name);
		return "/business_editgoods";
	}

	// 编辑商品
	@ResponseBody
	@PostMapping("/edictScoreGoods")
	public AjaxJson edictScoreGoods(@SessionAttribute("userId") Integer userId, SpotGoodsDO spotGoodsDO,@RequestParam("file1") MultipartFile file1, @RequestParam("uploadFile") MultipartFile[] uploadFile,
			HttpServletRequest request) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			// ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		if (file1 == null || uploadFile.length <= 0) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("图片不能为空");
			return ajaxJson;
		}
		System.out.println("===有限的======" + file1.getOriginalFilename() + "===个数==========" + uploadFile.length);
		if (uploadFile.length>5) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("图片最多上传五张");
			return ajaxJson;
		}
		if(file1.getSize()!=0) {
			AjaxJson data1 = uploadImg(file1);
			if (data1.isSuccess()) {
				spotGoodsDO.setGoodsDesc(data1.getData().toString());
			}
		}
		if(uploadFile.length!=0) {
			String data2 = "";
			for (MultipartFile multipartFile : uploadFile) {
				System.out.println("=======测试走一波==" + multipartFile.getOriginalFilename());
				AjaxJson data = uploadImg(multipartFile);
				if (data.isSuccess()) {
					data2 = data2 + (String) data.getData()+',';
				} else {
					ajaxJson.setMsg("失败");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			data2=data2.substring(0,data2.lastIndexOf(","));
		}
		spotGoodsDO.setGoodsUserId(userId);
		spotGoodsDO.setGoodsUserId(userId);
		String goodsName = XssHttpServletRequestWrapper.replaceXSS(spotGoodsDO.getGoodsName());
		spotGoodsDO.setGoodsName(goodsName);
		if (spotGoodsDao.update(spotGoodsDO) > 0) {
			ajaxJson.setSuccess(true);
			ajaxJson.setData("修改成功");
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		ajaxJson.setMsg("编辑失败");
		return ajaxJson;
	} 

	// 上传图片单独接口，必须这样，
	@PostMapping("/uploadJpg")
	public AjaxJson uploadJpg(@RequestParam("file") MultipartFile file) {
		AjaxJson ajaxJson = new AjaxJson();
		System.out.println("=========" + file.getOriginalFilename());
		AjaxJson data = uploadImg(file);
		if (data.isSuccess()) {
			logger.debug("data======={}", data);
			return data;
		} else {
			ajaxJson.setMsg("失败");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
	}

	@ResponseBody
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String fileName = file.getOriginalFilename();
		fileName = FileUtil.renameToUUID(fileName);
		String url = "http://cdn.yhsjtop.com/";
		String filepath = url + fileName;
		// FileUtil.uploadFile(file.getBytes(), bootdoConfig.getUploadPath(), fileName);
		FileDO bannerFile = new FileDO(FileType.fileType(fileName), filepath, new Date());
		try {
			UpLoadQinDuYun.upload(file, fileName);
			R.ok().put("fileName", bannerFile.getUrl());
		} catch (Exception e) {
			return R.error();
		}
		System.out.println("=================>>>>>>>>>>>" + bannerFile.getUrl());
		return R.ok().put("fileName", bannerFile.getUrl());

	}

	// 上架 type=1 type=2下架
	@ResponseBody
	@PostMapping("/updatePutawayStatus")
	public AjaxJson updatePutawayStatus(@SessionAttribute("userId") Integer userId, Integer goodsId, String type) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		if (type.equals("1")) {
			if (spotGoodsDao.updatePutWay(goodsId) > 0) {
				ajaxJson.setSuccess(true);
				// ajaxJson.setData("loginTimeOut");
				return ajaxJson;
			}
			ajaxJson.setSuccess(false);
			ajaxJson.setData("操作失败");
			return ajaxJson;
		}
		if (spotGoodsDao.updatePullWay(goodsId) > 0) {
			ajaxJson.setSuccess(true);
			// ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		ajaxJson.setData("操作失败");
		return ajaxJson;
	}

	// 我的订单商家(全部）
	@GetMapping("/myOrder")
	public String myOrder(Model model, @SessionAttribute("userId") Integer userId, Integer type, Integer flag) {
		// AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			// ajaxJson.setSuccess(false);
			// ajaxJson.setData("loginTimeOut");
			// return ajaxJson;
			return "/login";
		}
		// if (type == 1) {// 用户端
		// List<Map<String, Object>> spotOrders =
		// spotOrderDao.findeliverByUserIdOrder(userId, null);
		// ajaxJson.setData(spotOrders);
		// model.addAttribute("spotOrders",spotOrders);
		// } else {// 商家端
		List<Map<String, Object>> spotOrders = spotOrderDao.findeliverByUserIdOrder(null, userId);
		model.addAttribute("spotOrders", spotOrders);
		// ajaxJson.setData(spotOrders);
		// }
		// ajaxJson.setSuccess(true);
		// return ajaxJson;
		if (flag == 1) {
			model.addAttribute("type", 1);
		} else if (flag == 2) {
			model.addAttribute("type", 2);
		} else if (flag == 3) {
			model.addAttribute("type", 3);
		} else if (flag == 4) {
			model.addAttribute("type", 4);
		}

		return "/business_order";
	}

	// 我的订单用户(全部）
	@GetMapping("/myOrderUser")
	public String myOrderUser(Model model, @SessionAttribute("userId") Integer userId, Integer flag) {
		// AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			return "/login";
		}
		List<Map<String, Object>> spotOrders = spotOrderDao.findeliverByUserIdOrder(userId, null);
		model.addAttribute("spotOrders", spotOrders);
		if (flag == 1) {
			model.addAttribute("type", 1);
		} else if (flag == 2) {
			model.addAttribute("type", 2);
		} else if (flag == 3) {
			model.addAttribute("type", 3);
		} else if (flag == 0) {
			model.addAttribute("type", 0);
		} else if (flag == 4) {
			model.addAttribute("type", 4);
		}
		return "/order";
	}

	// 根据商品主键查询订单信息
	@GetMapping("/goodsDetailInfo/{orderNo}")
	public String goodsDetailInfo(Model model, @SessionAttribute("userId") Integer userId,
			@PathVariable("orderNo") String deliveryOrderId) {
		List<Map<String, Object>> spotOrders = spotOrderDao.findeliverBydeliveryOrderId(deliveryOrderId, null, userId);
		model.addAttribute("spotOrders", spotOrders);
		return "/business_order_detail";
	}

	// 确认收货
	@ResponseBody
	@PostMapping("/confirmOrder")
	public AjaxJson confirmOrder(@SessionAttribute("userId") Integer userId, String orderNo) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		return storeManagerServiceImpl.confirmOrder(orderNo, userId);

	}

	// 确认发货
	@ResponseBody
	@PostMapping("/confirmDeliveryOrder")
	public AjaxJson confirmDeliveryOrder(@SessionAttribute("userId") Integer userId,
			@RequestParam Map<String, Object> map) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setData("loginTimeOut");
			return ajaxJson;
		}
		if (StringUtils.isBlank(StringUtil.getMapValue(map, "courierName"))
				|| StringUtils.isBlank(StringUtil.getMapValue(map, "courierNo"))) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("物流相关信息必填");
			return ajaxJson;
		}
		if (spotOrderDao.updateDeliverGoods(map) > 0) {
			ajaxJson.setSuccess(true);
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		ajaxJson.setMsg("发货失败");
		return ajaxJson;
	}

	// 挂售订单
	@GetMapping("/consignmensSaleOrder")
	public String consignmensSaleOrder(@SessionAttribute("userId") Integer userId, Model model) {
		AjaxJson ajaxJson = new AjaxJson();
		if (userId == null) {
			return "/login";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("hangSellUser", userId);
		List<Map<String, Object>> maps = hangSellOrderDao.listHangSellOrder(map);
		model.addAttribute("maps", maps);
		// 挂售中
		map.put("hangSellStatus", "0");
		List<Map<String, Object>> maps0 = hangSellOrderDao.listHangSellOrder(map);
		model.addAttribute("maps0", maps0);
		// 挂售成功
		map.put("hangSellStatus", "1");
		List<Map<String, Object>> maps1 = hangSellOrderDao.listHangSellOrder(map);
		model.addAttribute("maps1", maps1);
		return "/consignmensSaleOrder";
	}
}
