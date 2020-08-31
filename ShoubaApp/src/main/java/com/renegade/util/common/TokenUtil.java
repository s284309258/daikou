package com.renegade.util.common;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.renegade.config.AjaxJson;

public class TokenUtil {
	
	/**
	 * token值校验
	 * @param map
	 * @param ajaxJson
	 */
	public static void checkTokenMap(Map<String, Object> map, AjaxJson ajaxJson) {
		//token值校验
		String token = StringUtil.getMapValue(map, "token");
		if(StringUtils.isEmpty(token)) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("token信息无效");
			return;
		}
		//用户ID
		String id = map.get("token").toString().split("\\|")[0];
		map.put("sys_user_id", id);
		ajaxJson.setSuccess(true);
	}

}
