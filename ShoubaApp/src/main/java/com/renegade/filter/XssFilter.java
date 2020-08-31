package com.renegade.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
  
/** 
 * 拦截防止xss注入
 * 通过Jsoup过滤请求参数内的特定字符
 * @author yangwk 
 */  
//@Component  //如果添加了@Component或@Configuration，又添加了@WebFilter（），那么会初始化两次Filter，并且会过滤所有路径+自己指定的路径 ，便会出现对没有指定的URL也会进行过滤
//@WebFilter(filterName = "myFilter", urlPatterns = "/upload/*")
public class XssFilter implements Filter {  
	private static Logger logger = LoggerFactory.getLogger(XssFilter.class);

	/**
	 * 是否过滤富文本内容
	 */
	private static boolean IS_INCLUDE_RICH_TEXT = false;
	
	public List<String> excludes = new ArrayList<>();
  
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {
    	if(logger.isDebugEnabled()){
  		}
  		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
  		if(handleExcludeURL(req, resp)){
  			//用来放行，如果执行这个方法之后，后序还有
  			//过滤器，那么就会执行过滤器，若果没有就会去执行目标资源。可以理解成，如果有多个过滤器会链成一条线，一个接一个的执行。
  			filterChain.doFilter(request, response);
			return;
		}
  		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,IS_INCLUDE_RICH_TEXT);
  		filterChain.doFilter(xssRequest, response);
    }
    
    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

		if (excludes == null || excludes.isEmpty()) {
			return false;
		}

		String url = request.getServletPath();
		for (String pattern : excludes) {
			Pattern p = Pattern.compile("^" + pattern);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if(logger.isDebugEnabled()){
			logger.debug("xss filter init~~~~~~~~~~~~");
		}
		String isIncludeRichText = filterConfig.getInitParameter("isIncludeRichText");
		if(StringUtils.isNotBlank(isIncludeRichText)){
			IS_INCLUDE_RICH_TEXT = BooleanUtils.toBoolean(isIncludeRichText);
		}
		
		String temp = filterConfig.getInitParameter("excludes");
		if (temp != null) {
			String[] url = temp.split(",");
			for (int i = 0; url != null && i < url.length; i++) {
				excludes.add(url[i]);
			}
		}
	}

	@Override
	public void destroy() {}  
  
}  
