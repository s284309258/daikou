package com.renegade.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.renegade.annotations.RenegadeAuth;
import com.renegade.annotations.RenegadeAuthSign;
import com.renegade.config.ApplicationContextRegister;
import com.renegade.config.HttpServletUtils;
import com.renegade.config.StringUtil;
import com.renegade.constant.SysSecurityKeyConstant;
import com.renegade.dao.M9UserMapper;
import com.renegade.util.R;
import com.renegade.util.common.SignUtil;
import com.renegade.util.pub.AdvFunUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Copyright 2018
 * @author Renegade丶四叶草 All right reserved
 * @Created 2019年7月5日
 * @version 1.0
 * @email 291312408@qq.com
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private final static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("agent信息================" + request.getHeader("user-agent"));
		System.out.println("agent信息========URL========" + request.getRequestURL().toString());
		response.setCharacterEncoding("UTF-8");
		// 如果不是验证方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		PrintWriter outRite = null;
		JSONObject ajaxJson = new JSONObject();
		//校验文件格式
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
			List<MultipartFile> fileSet = new LinkedList<>();
			for (Map.Entry<String, List<MultipartFile>> temp : multiFileMap.entrySet()) {
				fileSet = temp.getValue();
			}
			AdvFunUtil advFunUtil = ApplicationContextRegister.getBean(AdvFunUtil.class);
			logger.debug("---------------------" + advFunUtil);
			for (MultipartFile temp : fileSet) {
				if (!temp.isEmpty()) {
					if (!advFunUtil.checkPicFile(temp.getOriginalFilename())) {
						if (HttpServletUtils.jsAjax(request)) {
							ajaxJson.put("success", false);
							ajaxJson.put("msg", "图片格式有误");
							outRite = response.getWriter();
							outRite.append(ajaxJson.toString());
							return false;
						}
						response.sendRedirect("/407");
						return false;
					}
				}
			}
		}
		// 如果被@RenegadeAuthSign注解，那么就是不用判定验证,是否存在封号
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		if (method.getAnnotation(RenegadeAuth.class) != null && method.getAnnotation(RenegadeAuthSign.class) == null) {
			return true;
		}
		// 校验签名
		if (method.getAnnotation(RenegadeAuthSign.class) != null) {
			Map<String, Object> params = new HashMap<>();
			Set keySet = request.getParameterMap().entrySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
				Map.Entry object = (Map.Entry) iterator.next();
				String key = (String) object.getKey();
				Object values = object.getValue();
				// 只有一个元素的字符串数组
				String[] value = new String[1];
				String valueStr = "";
				// 转换为字符串数组
				if (values instanceof String[]) {
					value = (String[]) values;
				} else {
					value[0] = values.toString();
				}
				for (int k = 0; k < value.length; k++) {
					valueStr = (k == value.length - 1 ? valueStr + value[k] : valueStr + value[k] + ",");
				}
				params.put(key, valueStr);
			}
			// 验签
			R check = SignUtil.checkSign(params, SysSecurityKeyConstant.md5Key_web);
			if (!check.get("code").toString().equals("0")) {
//			    判断是否为ajax请求
				if (HttpServletUtils.jsAjax(request)) {
					ajaxJson.put("success", false);
					ajaxJson.put("msg", "提交参数异常");
					outRite = response.getWriter();
					outRite.append(ajaxJson.toString());
					return false;
				}
				response.sendRedirect("/407");
				return false;
			}
			// 校验时间戳是否有效
			String timeStamp = StringUtil.getMapValue(params, "timeStamp");
			if (StringUtils.isBlank(timeStamp)) {
				if (HttpServletUtils.jsAjax(request)) {
					ajaxJson.put("success", false);
					ajaxJson.put("msg", "请求失效");
					outRite = response.getWriter();
					outRite.append(ajaxJson.toString());
					return false;
				}
				response.sendRedirect("/406");
				return false;
			}
			Long timeSystem = new Date().getTime();
			logger.debug("========时间=======>>>>{}", timeSystem);
			Long long1 = timeSystem - Long.parseLong(timeStamp);
			logger.debug("========时间=======>>>>{}", long1);
			if (long1 >= 4900L || long1 <= -4900L) {
//			    判断是否为ajax请求
				if (HttpServletUtils.jsAjax(request)) {
					ajaxJson.put("success", false);
					ajaxJson.put("msg", "请求失效");
					outRite = response.getWriter();
					outRite.append(ajaxJson.toString());
					return false;
				}
				response.sendRedirect("/406");
				return false;
			}
		}
		if (method.getAnnotation(RenegadeAuth.class) == null) {
			String userId = request.getSession().getAttribute("userId").toString();
			if (userId == null) {
				if (HttpServletUtils.jsAjax(request)) {
					ajaxJson.put("success", false);
					ajaxJson.put("msg", "登录已经超时，请重新登录");
					outRite = response.getWriter();
					outRite.append(ajaxJson.toString());
					return false;
				}
				response.sendRedirect("/406");
				return false;
			}
			M9UserMapper frontUserDao = ApplicationContextRegister.getBean(M9UserMapper.class);
			Map<String, Object> frontUserDO = frontUserDao.findUserByuserId(userId);
			if ("2".equals(frontUserDO.get("user_state").toString())) {// 冻结，直接封号ajka
//			    判断是否为ajax请求
				if (HttpServletUtils.jsAjax(request)) {
					ajaxJson.put("success", false);
					ajaxJson.put("msg", "该账号已经被冻结，不可以进行任何操作!");
					outRite = response.getWriter();
					outRite.append(ajaxJson.toString());
					return false;
				}
				response.sendRedirect("/405");
				return false;
			}
			return true;
		}
		return true;

	}
}
