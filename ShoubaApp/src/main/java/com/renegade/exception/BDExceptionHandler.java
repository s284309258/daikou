package com.renegade.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.renegade.config.AjaxJson;
import com.renegade.config.HttpServletUtils;

/**
 * 异常处理器
 */
@RestControllerAdvice
//@ControllerAdvice //这个注解是指这个类是处理其他controller抛出的异常 如果包含json数据的异常那么就加上上面的
//控制器必须要跑出异常这边才起作用
public class BDExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
//
//    /**
//     * 自定义异常
//     */
//    @ExceptionHandler(BDException.class)
//    public R handleBDException(BDException e) {
//        logger.error(e.getMessage(), e);
//        R r = new R();
//        r.put("code", e.getCode());
//        r.put("msg", e.getMessage());
//        return r;
//    }
//
//    @ExceptionHandler(DuplicateKeyException.class)
//    public R handleDuplicateKeyException(DuplicateKeyException e) {
//        logger.error(e.getMessage(), e);
//        return R.error("数据库中已存在该记录");
//    }
//
//    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
//    public R noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
//        logger.error(e.getMessage(), e);
//        return R.error(404, "没找找到页面");
//    }

	@ExceptionHandler(BDException.class)
//    @ExceptionHandler(AuthorizationException.class)//shiro
	public Object handleAuthorizationException(BDException e, HttpServletRequest request) {
		logger.error("唉！还是习惯打出中文虚浮,自定义异常====="+e.getMessage()+"==========你们说呢："+e);
		if (HttpServletUtils.jsAjax(request)) {
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg(e.getMessage());
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
//		return new ModelAndView("error/403");
		return new ModelAndView("login");
	}

	@ExceptionHandler({ Exception.class })
	public Object handleException(Exception e, HttpServletRequest request) {
		logger.error("唉！还是习惯打出中文虚浮，哦豁，四叶草又得调试了！====="+e.getMessage()+"==========你们说呢："+e);
		e.printStackTrace();
		if (HttpServletUtils.jsAjax(request)) {
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("系统异常");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		return new ModelAndView("login");
	}
}
