package com.renegade.util;

import java.util.regex.Pattern;

public class Regex {
	
	/**
	 * ������ʽ�����׽��У�飨������0��
	 * @param str
	 * @return
	 */
	public static boolean isVaildTradeNum(String tardeNum) {  
		if("".equals(tardeNum)||null==tardeNum) {
			return false;
		}else {
			Pattern pattern = Pattern.compile("^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$");  
			return pattern.matcher(tardeNum).matches();  
		}
	}
	
	
	/**
	 * ������ʽ����������Ϊ0~10������
	 * @param num
	 * @return
	 */
	public static boolean isVaildNum(String num) {  
		if("".equals(num)||null==num) {
			return false;
		}else {
			Pattern pattern = Pattern.compile("^(?:0|[1-9]?|10)$");  
			return pattern.matcher(num).matches();  
		}
	}
	
	
	
	/**
	 * ����У��
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email){
		if("".equals(email)||null==email) {
			return false;
		}else {
			Pattern pattern = Pattern.compile("^\\w+@[a-zA-Z0-9]{2,10}(?:\\.[a-z]{2,4}){1,3}$");  
			return pattern.matcher(email).matches();  
		}
	}
	
	
	/**
	 * �ֻ�����У��
	 * @param tel
	 * @return
	 */
	public static boolean isValidTel(String tel){
		if("".equals(tel)||null==tel) {
			return false;
		}else {
			Pattern pattern = Pattern.compile("^1(3|4|5|6|7|8|9)\\d{9}");  
			return pattern.matcher(tel).matches();  
		}
	}
	
	
	/**
	 * 6λ����У��
	 * @param num
	 * @return
	 */
	public static boolean isValidSixNum(String num){
		if("".equals(num)||null==num) {
			return false;
		}else {
			Pattern pattern = Pattern.compile("^\\d{6}$");  
			return pattern.matcher(num).matches();  
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(isVaildTradeNum("0"));
	}

}
