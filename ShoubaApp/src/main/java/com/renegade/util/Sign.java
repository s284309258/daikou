package com.renegade.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;



/** 
*  @Copyright    2018 
*        @author Renegadeؼ�o�g  
*                    All right reserved
*      @Created   2018��9��27�� 
*   @version  1.0
* @email 291312408@qq.com
*/
public class Sign {
	/**
	 * ΢��֧�������㷨
	 * @param characterEncoding
	 * @param paramerters
	 * @param key
	 * @return
	 */
      @SuppressWarnings("unckecked")
      public static String createSign(String characterEncoding,
    		  SortedMap<Object, Object>paramerters,String key){
    	  StringBuffer sBuffer=new StringBuffer();
    	  Set eSet=paramerters.entrySet();
    	  //��ǿforѭ���򻯵ĵ�������
    	/*  for (Object entry : eSet) {
 			 Map.Entry entry2=( Map.Entry) entry;
 			 String k=(String) entry2.getKey();
 			 Object v= entry2.getValue();
 		}*/
    	  Iterator iterator=eSet.iterator();
    	 while (iterator.hasNext()) {
			Map.Entry  entry=(Map.Entry)iterator.next();
			String k=(String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sBuffer.append(k + "=" + v + "&");
			}
		}
    	 
    	 sBuffer.append("key="+key);
    	 //����MD5����
    	 String sign =MD5Util.MD5Encode(sBuffer.toString(), characterEncoding).toUpperCase();// �������ڽ�Сд�ַ�ת��Ϊ��д��
		return sign;
    	  
      }
}

