package com.renegade.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 获取主机ip地址
 * @author 16616
 *
 */
public class IpUtil
{
    public static final String STAT_S = "S";
    
    public static final String STAT_F = "F";
    
    public static final String LOCAL_HOST = "localhost";
    
    public static String LOCAL_IP;
    
    public static String EQUAL = "=";
    public static String PIPE = "=";
    
    /*static
    {
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println(LOCAL_IP = addr.getHostAddress().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
    
    /**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 
	 * 可是，如果�?�过了多级反向代理的话，X-Forwarded-For的�?�并不止�?个，而是�?串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第�?个非unknown的有效IP字符串�??
	 * 
	 * 如：X-Forwarded-For�?192.168.1.110, 192.168.1.120, 192.168.1.130,
	 * 192.168.1.100
	 * 
	 * 用户真实IP为： 192.168.1.110
	 * 
	 * @param
	 * @return
	 */
	public static String getIpAddress()
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
