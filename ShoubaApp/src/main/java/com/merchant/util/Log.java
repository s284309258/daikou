package com.merchant.util;

/**
 * 日志打印类； 
 * 商户可以根据项目需求进行日志记录方式的修改。
 */
public class Log {
	//日志打印标志，默认开启
	private static boolean bLogFlag = true;  
	
	public static void setLogFlag(boolean bfalg) {
		bLogFlag = bfalg;
	}
	
	/**
	 * 描述：日志打印函数；商户可以根据自己的使用情况，修改日志的打印方式（如调整为log4j）
	 * @param str ： 打印内容
	 */
	public static void println(String str) {
		if(bLogFlag){
			System.out.println(str);
		}
	}
}
