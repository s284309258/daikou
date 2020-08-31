package com.renegade.util.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Url编码解码工具类
 * @author Administrator
 *
 */
public class UrlCodeUtil {
	
	public static String getUrlEncode(String str)
    {
        if (str == null || "".equals(str))
        {
            return "";
        }
        try
        {
            str = URLEncoder.encode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static String getUrlDecode(String str)
    {
        if (str == null || "".equals(str))
        {
            return "";
        }
        try
        {
            str = URLDecoder.decode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return str;
    }

}
