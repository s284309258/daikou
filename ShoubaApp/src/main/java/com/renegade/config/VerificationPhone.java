package com.renegade.config;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class VerificationPhone {
	
	public static boolean verificationPhone(String phone){
		boolean flag = false;
		if(StringUtils.isEmpty(phone)){
			return flag;
		} else {
			if(phone.length()!=11){
				return flag;
			} else {
				if(Pattern.matches("^[1][0-9][0-9]{9}$", phone)){
					flag = true ;
				} else {
					flag = false;
				}
			}
		}
		return flag;
	}	
	
	
	/**
	 * 判断密码是否是6-20位且是数字与字母组合
	 * @param pass
	 * @return
	 */
	public static boolean verificationPass(String pass){
		 String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";	
		 return pass.matches(regex);   
		
	}
	
	public static boolean verificationPayPass(String pass){
		String regex = "\\d{6}";//6位数字
		boolean flag = false;
		if(pass.matches(regex)){
//			System.out.println("是6位字符");
			//哎，找不到好的正则匹配，硬编码判断吧,剔除几种简单的支付密码就OK了吧，以后又好的方法了在来修改
			switch (pass) {
			case "123456":
				flag = false;
				break;
			case "111111":
				flag = false;
				break;
			case "222222":
				flag = false;
				break;
			case "333333":
				flag = false;
				break;
			case "444444":
				flag = false;
				break;
			case "555555":
				flag = false;
				break;
			case "666666":
				flag = false;
				break;
			case "777777":
				flag = false;
				break;
			case "888888":
				flag = false;
				break;
			case "999999":
				flag = false;
				break;
			case "000000":
				flag = false;
				break;
			default:
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public static void main(String[] args) {
		System.out.println(verificationPayPass("123456"));
		short a = 128;
		byte b= (byte)a;
		System.out.println(a+":"+b);
	}
	
}
