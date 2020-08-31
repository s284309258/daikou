package com.renegade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * md5加密类
 * @author fengwenjie
 *
 */ 
public class MD5 {
	
	public static Logger loggerInfo = LoggerFactory.getLogger(MD5.class);
	public static String strToMd5(String param) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			loggerInfo.info("md5  实例化成功");
		} catch (NoSuchAlgorithmException e) {
			loggerInfo.error("md5 实例化失败,exception : "+e);
		}
		md5.update(param.getBytes());
        byte b[] = md5.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        String sing = buf.toString();
		return sing;
	}
	
	
	public static void main(String[] args) {
//		PropertyConfigurator.configure("D:\\FengWenJieWorkspaces\\bsvc.user\\src\\main\\resources\\log4j.properties");//加载.properties文件 
		String  a = "123456";
		String str = strToMd5(strToMd5(a));
		System.out.println(str);
		loggerInfo.error(str);
	}
}
