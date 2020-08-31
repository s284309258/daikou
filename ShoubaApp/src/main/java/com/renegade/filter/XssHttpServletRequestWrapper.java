package com.renegade.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

import com.renegade.util.xss.JsoupUtil;


/** 
 * <code>{@link XssHttpServletRequestWrapper}</code>
 * @author win7
 */  
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
    HttpServletRequest orgRequest = null;  
    private boolean isIncludeRichText = false;
  
    public XssHttpServletRequestWrapper(HttpServletRequest request, boolean isIncludeRichText) {  
        super(request);  
        orgRequest = request;
        this.isIncludeRichText = isIncludeRichText;
    }  
  
    /** 
    * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/> 
    * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/> 
    * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖 
    */  
    @Override  
    public String getParameter(String name) {
        Boolean flag = ("content".equals(name) || name.endsWith("WithHtml"));
        if( flag && !isIncludeRichText){
            return super.getParameter(name);
        }
        name = JsoupUtil.clean(name);
        String value = super.getParameter(name);  
        if (StringUtils.isNotBlank(value)) {
            value = JsoupUtil.clean(value);  
        }
        return value;  
    }  
    
    @Override
    public String[] getParameterValues(String name) {
    	String[] arr = super.getParameterValues(name);
    	if(arr != null){
    		for (int i=0;i<arr.length;i++) {
    			arr[i] = JsoupUtil.clean(arr[i]);
    		}
    	}
    	return arr;
    }
    
  
    /** 
    * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/> 
    * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/> 
    * getHeaderNames 也可能需要覆盖 
    */  
    @Override  
    public String getHeader(String name) {  
        name = JsoupUtil.clean(name);
        String value = super.getHeader(name);  
        if (StringUtils.isNotBlank(value)) {  
            value = JsoupUtil.clean(value); 
        }  
        return value;  
    }  
  
    /** 
    * 获取最原始的request 
    * 
    * @return 
    */  
    public HttpServletRequest getOrgRequest() {  
        return orgRequest;  
    }  
  
    /** 
    * 获取最原始的request的静态方法 
    * 
    * @return 
    */  
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {  
        if (req instanceof XssHttpServletRequestWrapper) {  
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();  
        }  
  
        return req;  
    }


    /**
     * 去除待带script、src的语句，转义替换后的value值,只能过滤GET提交方式，不能过滤Post提交方式,Post提交方式还需要在业务类型中写
     */
    public static String replaceXSS(String value) {
        if (value != null) {
            try{
                value = value.replace("+","%2B");   //'+' replace to '%2B'
                value = URLDecoder.decode(value, "utf-8");
            }catch(UnsupportedEncodingException e){
            }catch(IllegalArgumentException e){
            }

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");//如果有sricpt脚本(即xss攻击)，置换

            // Avoid anything in a src='...' type of e?xpression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid eval(...) e?xpressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid e?xpression(...) e?xpressions
            scriptPattern = Pattern.compile("e?xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid javascript:... e?xpressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid alert:... e?xpressions
            scriptPattern = Pattern.compile("alert", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= e?xpressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return filter(value);
    }

    /**
     * 过滤特殊字符
     */
    public static String filter(String value) {
        if (value == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(value.length());
        for (int i=0; i<value.length(); ++i) {
            switch (value.charAt(i)) {
                case '<':
                    result.append("<");
                    break;
                case '>':
                    result.append(">");
                    break;
                case '"':
                    result.append('"');
                    break;
                case '\'':
                    result.append("'");
                    break;
                case '%':
                    result.append("%");
                    break;
                case ';':
                    result.append(";");
                    break;
                case '(':
                    result.append("(");
                    break;
                case ')':
                    result.append(")");
                    break;
                case '&':
                    result.append("&");
                    break;
                case '+':
                    result.append("+");
                    break;
                default:
                    result.append(value.charAt(i));
                    break;
            }
        }
        return result.toString();
    }

	
   

   
}  
