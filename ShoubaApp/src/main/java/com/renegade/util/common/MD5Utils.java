package com.renegade.util.common;

import java.security.MessageDigest;

public class MD5Utils {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    
    
    public static void main(String[] args) {
		System.out.println(MD5Encode("content=123456&message_type=00&op_type=add&send_type=00&system_type=circle&token=7|523WI1ZJLMNWKKPLI680DGE3P9SGZXBG&user_addr=深圳&key=JntKVUavicBvDNmtRNHgKB8AZauN2hWw"));
		System.out.println(MD5Encode("content=123456&message_type=00&op_type=add&send_type=00&system_type=circle&token=7|523WI1ZJLMNWKKPLI680DGE3P9SGZXBG&user_addr=深圳&key=JntKVUavicBvDNmtRNHgKB8AZauN2hWw"));
	}

    
    
    public static String getSign(String string, String posKey) {
        String result = string;
        // key是双方约定的加密秘钥
        result += "&key=" + posKey;
        System.out.println("加签字符串====="+result);
        result = MD5Utils.MD5Encode(result).toUpperCase();
        return result;
    }
}
