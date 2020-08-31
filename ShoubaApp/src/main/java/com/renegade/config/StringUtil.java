package com.renegade.config;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

/**
 * 超详细的字符串工具类
 * @author Administrator
 *
 */
public class StringUtil {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private static BigDecimal bigdecimal_0 = new BigDecimal(0);
	
	
	/**
	 * 对map里面参数处理，有返回该值，无返回""
	 * @param map
	 * @param paramName
	 * @return
	 */
	public static String getMapValue(Map<String, Object> map,String paramName) {
		if(map!=null&&map.containsKey(paramName)&&map.get(paramName)!=null) {
			return map.get(paramName).toString();
		}else {
			return "";
		}
	}
	
	
	/**
	 * 生成纯数字的6位验证码
	 * @return
	 */
	public static String getCode() {
		Long time = System.currentTimeMillis();
		String strTime = String.valueOf(time);
		String code = strTime.substring(strTime.length() - 6);
		return code;
	}
	
	
	/**
	 * 生成字母数字混合的随机数
	 * @return
	 */
	public static String genRandomNum(){
		int  maxNum = 36;
		int i;
		int count = 0;
		char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
		    'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
		    'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };	  
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while(count < 6){
			i = Math.abs(r.nextInt(maxNum));   
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count ++;
			}
		}
		return pwd.toString();
	}
	
	
	/**
	 * 判断某个字符串中是否包含另外一个字符串
	 * @param fatherString
	 * @param sonString
	 * @return
	 */
	public static boolean containString(String fatherString,String sonString){
		if(fatherString.indexOf(sonString)!=-1){  
			System.out.println("包含"); 
			return true;
		}else{ 
			System.out.println("不包含"); 
			return false;
		} 
	}
	
	
	/**
	 * 生成随机ID
	 * @return
	 */
	public static String getDateTimeAndRandomForID(){
		int random = (int)((Math.random()*9+1)*100000);
		String id = format.format(new Date())+random;
		return id;
	}
	
	/**
	 * 生成随机邀请码
	 * @return
	 */
	public static String getInviteCodeRandom(int num){
		String randomcode = "";
		// 用字符数组的方式随机
		String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] m = model.toCharArray();
		for (int j = 0; j < num; j++) {
			char c = m[(int) (Math.random() * 36)];
			randomcode = randomcode + c;
		}
		return randomcode;
	}
	
	/**
	 * 生成Token参数
	 * @return
	 */
	public static String getTokenRandom(Long userId){
		String randomcode = "";
		// 用字符数组的方式随机
		String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] m = model.toCharArray();
		for (int j = 0; j < 32; j++) {
			char c = m[(int) (Math.random() * 36)];
			randomcode = randomcode + c;
		}
		return userId+"|"+randomcode;
	}
	
	
	/**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     * 
     * @param string
     * @param str1
     * @param str2
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {
 
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
 
        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }
    
    
    /**
     * 截取某字符串中固定字符串之后的
     * @param str
     * @param objStr
     * @return
     */
    public static String getAfterStr(String str, String objStr) {
    	if(StringUtils.isEmpty(str)||StringUtils.isEmpty(objStr)) {
    		return null;
    	}
    	String result = StringUtils.substringAfter(str, objStr);
    	if(StringUtils.isEmpty(result)) {
    		return null;
    	}
        return result;
    }

    
    /**
     * 字符串小写全部转为大写
     * @param str
     * @return
     */
    public static String ToUpper(String str) {
    	return str.toUpperCase();
    }
    
    
    
    public static String getLastStrAfter(String str) {
    	int one = str.lastIndexOf(".");
    	return "."+str.substring((one+1),str.length());
    }
    
    
    
    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     * @author zxcvbnmzb
     * @param testStr 要+1的字符串
     * @return +1后的字符串
     * @exception NumberFormatException
     */
     public static String addOne(String testStr){
         String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
         String numStr = strs[strs.length-1];//取出最后一组数字
         if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
             int n = numStr.length();//取出字符串的长度
             Long num = Long.parseLong(numStr)+1;//将该数字加一
             String added = String.valueOf(num);
             n = Math.min(n, added.length());
             //拼接字符串
             return testStr.subSequence(0, testStr.length()-n)+added;
         }else{
             throw new NumberFormatException();
         }
     }
     
     /**
      * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
      * @author zxcvbnmzb
      * @param testStr 要+1的字符串
      * @return +1后的字符串
      * @exception NumberFormatException
      */
      public static String addOneForTen(String testStr){
          String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
          String numStr = strs[strs.length-1];//取出最后一组数字
          int n = 0;
          if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
              if(numStr.length()>10){//超过十位按十位自增
             	 n = 10;
             	 numStr = testStr.substring(testStr.length()-n,testStr.length());
              }else{
             	 n = numStr.length();//取出字符串的长度
              }
              Long num = Long.parseLong(numStr)+1;//将该数字加一
              String added = String.valueOf(num);
              n = Math.min(n, added.length());
              //拼接字符串
              return testStr.subSequence(0, testStr.length()-n)+added;
          }else{
              throw new NumberFormatException();
          }
      }
    


	/**
	 * 计算权重
	 * @param userInfoMap
	 * @param userInfoCalcuate
	 * @param userInfoRate 
	 * @return
	 */
	public static BigDecimal calcuateRate(Map<String, Object> map, String[] calcuateArray, BigDecimal totalRate) {
		int hasNum=0;
		//keySet获取map集合key的集合  然后在遍历key即可
		for(String key:map.keySet()){
			//key对应的value值
			if(map.get(key)!=null&&map.get(key)!="") {
				String value = map.get(key).toString();
				for(int j=0;j<calcuateArray.length;j++) {
					if(key.equals(calcuateArray[j])&&!StringUtils.isEmpty(value)) {
						hasNum++;
					}
				}
			}
		}
		BigDecimal resultRate=new BigDecimal(hasNum).divide(new BigDecimal(calcuateArray.length), BigDecimal.ROUND_HALF_DOWN, 2).multiply(totalRate).setScale(2, BigDecimal.ROUND_DOWN);
		return resultRate;
	}
	
	
	/**
	 * 去掉两个指定字符串之间的所有字符串（包括这两个字符串）
	 * @param body
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String subRangeString(String body,String str1,String str2) {
        while (true) {
            int index1 = body.indexOf(str1);
            if (index1 != -1) {
                int index2 = body.indexOf(str2, index1);
                if (index2 != -1) {
                    String str3 = body.substring(0, index1) + body.substring(index2 +    str2.length(), body.length());       
                    body = str3;
                }else {
                    return body;
                }
            }else {
                return body;
            }
        }
    }

	
	/**
	 * 计算总的收益数量
	 * @param mapList
	 * @param string
	 * @return
	 */
	public static BigDecimal getCalcuateTotalNum(List<Map<String, Object>> mapList, String param) {
		BigDecimal totalNum = BigDecimal.ZERO;
		for(int i=0;i<mapList.size();i++) {
			totalNum=totalNum.add(new BigDecimal(mapList.get(i).get(param).toString()));
		}
		return totalNum;
	}


	/**
	 * 
	 * @param inviteFriendList
	 * @param string
	 * @return
	 */
	public static String getStringContact(List<Map<String, Object>> mapList, String param) {
		String str="";
		for(int i=0;i<mapList.size();i++) {
			if(i<mapList.size()-1) {
				str=str+mapList.get(i).get(param).toString()+",";
			}else {
				str=str+mapList.get(i).get(param).toString();
			}
		}
		return str;
	}
	
	/**
	 * 判断数值是否大于零
	 * @param num
	 * @return
	 */
	public static boolean isValidLargeBigDecimal0(String num){
		try{
			if(StringUtils.isEmpty(num)){
				return false;
			}else{
				if(new BigDecimal(num).compareTo(bigdecimal_0)>0){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 判断ETH地址是否规范
	 * @param address
	 * @return
	 */
	public static boolean isValidETHAddress(String address){
		try{
			if("0x".equals(address.substring(0,2))&&address.length()==42){
				return true;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static void main(String[] args) {
//		List<Map<String, Object>> list=new ArrayList<>();
//		for(int i=0;i<10;i++) {
//			Map<String, Object> map=new HashMap<>();
//			map.put("id", i);
//			list.add(map);
//		}
//		System.out.println(getStringContact(list, "id"));
		System.out.println(getDateTimeAndRandomForID());
	}
	
}
