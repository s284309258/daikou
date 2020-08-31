package com.renegade.config;

import java.util.Random;

public class Utils {
	
	/**
	 * 邀请码长度，8位
	 */
	private static int len=8;
	
	private static int random = 1000;
	
	/**
	 * 提货订单号
	 * @return
	 */
	public  static String getTiHuoOrderId(){
		String tiHuo = "TH";
		long ms = System.currentTimeMillis();
		String orderId = tiHuo+ms+random;
		random = random+1;
		return orderId;
	}
	
	/**
	 * 邀请码
	 * @return
	 */
	public static String getInvitationCode(){
		String val = "";
        Random random = new Random();
 
        //参数length，表示生成几位随机数
        for (int i = 0; i < len; i++) {
      //随机数由0-9，a-z,A-Z组成，数字占10个，字母占52个，数字、字母占比1:5（标准的应该是10:52）
        //random.nextInt(6) 0-5中6个数取一个
//            String charOrNum = (random.nextInt(6)+6) % 6 >=1 ? "char" : "num";
            String charOrNum = (random.nextInt(6)+6) % 6 >=1 ? "num" : "char";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母，输出比例为1:1
//                int temp = random.nextInt(2) % 2 == 0 ? 97 : 65;
                //char（65）-char(90) 为大写字母A-Z；char(97)-char(122)为小写字母a-z
//                val += (char) (random.nextInt(26) + temp);
                val += (char) (random.nextInt(26) + 65);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(9)+1);
            }
        }
        return val;

	}
	
	public static void main(String[] args) {
		System.out.println("====="+String.valueOf((char)97));
		for(int i=0;i<10;i++){
			System.out.println(getInvitationCode().toUpperCase());
		}
		
	}
	
}
