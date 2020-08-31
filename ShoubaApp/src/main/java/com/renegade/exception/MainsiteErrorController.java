package com.renegade.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renegade.config.AjaxJson;
import com.renegade.util.R;

/**
 * web错误全局配置
 * 
 * <p>
 * Title: MainsiteErrorController
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author NIC丶四叶草^Renegade
 * 
 * @date 2019年5月31日
 */
@RestController
public class MainsiteErrorController implements ErrorController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	// 默认错误衍射路由为"/error"路径
	private static final String ERROR_PATH = "/error";

	@Autowired
	ErrorAttributes errorAttributes;

	@RequestMapping(value = { ERROR_PATH }, produces = { "text/html" })
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		int code = response.getStatus();
		System.out.println("状态码==四叶草==原生视图==========" + code);
		if (404 == code) {
			return new ModelAndView("login");
		} else if (403 == code) {
			return new ModelAndView("login");
		} else if (401 == code) {
			return new ModelAndView("login");
		} else if (400 == code) {
			return new ModelAndView("login");
		} else if (500 == code) {
			return new ModelAndView("error/500");
		} else {
			return new ModelAndView("login");
		}

	}

	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public AjaxJson handleError(HttpServletRequest request, HttpServletResponse response) {
		int code = response.getStatus();
		System.out.println("状态码==四叶草==原生ajax==========" + code);
		AjaxJson ajaxJson = new AjaxJson();
		if (code != 200) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("errorApp500");
			return ajaxJson;
		}
		ajaxJson.setSuccess(false);
		ajaxJson.setMsg("errorApp");
		return ajaxJson;
//        if (404 == code) {
//            return R.error(404, "未找到资源");
//        } else if (403 == code) {
//            return R.error(403, "没有访问权限");
//        } else if (401 == code) {
//            return R.error(403, "登录过期");
//        } else {
//            return R.error(500, "服务器错误");
//        }
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return ERROR_PATH;
	}
}