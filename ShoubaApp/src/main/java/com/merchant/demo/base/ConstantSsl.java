package com.merchant.demo.base;

/**
 * 描述：通讯类常量
 * @author Payeco
 *
 */
public class ConstantSsl {
	
	
	/**
	 * 字符集常量
	 * UTF8
	 * GBK
	 * 
	 *
	 */
	public static enum CHARSET{
		UTF8("UTF8"),GBK("GBK"),ISO_8859_1("ISO-8859-1");
		private String charset;

		private CHARSET(String charset){
			this.charset = charset;
		}

		public String value(){
			return charset;
		}
	}


}
