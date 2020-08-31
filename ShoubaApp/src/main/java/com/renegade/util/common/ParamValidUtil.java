package com.renegade.util.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.renegade.config.AjaxJson;

/**
 * 接口调取商的参数校验工具类
 * @author Administrator
 *
 */
public class ParamValidUtil {
		
	/**
	 * 必备参数校验
	 * @param map
	 * @param paramArra
	 * @param ajaxJson 
	 * @return
	 */
    public static void checkMustParam(Map<String, Object> map,String[] paramArra, AjaxJson ajaxJson) {
    	for(int i=0;i<paramArra.length;i++) {
    		if(!map.containsKey(paramArra[i])||StringUtils.isEmpty(map.get(paramArra[i]).toString())) {
    			ajaxJson.setSuccess(false);
    			ajaxJson.setMsg("参数有误");
    			return;
    		}
    	}
    	ajaxJson.setSuccess(true);
    	return;
    }
    
    
    /**
     * 特殊参数校验
     * @param map
     * @param ajaxJson 
     * @param string
     * @param merchantPayRefundParamType
     * @return
     */
    public static void checkSpecifyParam(Map<String, Object> map, String paraName,
			String[] paramValueArra, AjaxJson ajaxJson) {
		boolean flag=false;
		for(int i=0;i<paramValueArra.length;i++) {
			if(paramValueArra[i].equals(StringUtil.getMapValue(map, paraName))) {
				flag=true;
				break;
			}
		}
		if(flag==false) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("参数"+paraName+"无效，请仔细查阅接口文档");
		}else {
			ajaxJson.setSuccess(true);
		}
		return;
	}
    
    
    /**
     * 交易数据处理
     * @param map
     * @param string
     */
    public static void dealTransData(Map<String, Object> map, String objectStr) {
    	if(!map.containsKey(objectStr)) {
    		map.put(objectStr, "0");
    	}
	}
    
    
    public static void main(String[] args) {
		Map<String, Object> map=new HashMap<>();
		map.put("payerName", "黄容");
		map.put("payerTelephone", "黄容");
		map.put("payerAcctNo", "黄容");
		map.put("orderAmount", "黄容");
		map.put("payerIdNo", "黄容");
		map.put("orderId", "黄容");
	}


	



	
}
