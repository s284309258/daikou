package com.renegade.util.pub;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

import com.renegade.config.AjaxJson;
public class CheckObjUtil {
	/**
	 * 验证对象
	 * 
	 * @param o
	 * @throws AppException
	 * @throws Exception
	 */
	public static AjaxJson checkReqObj(Object o,AjaxJson ajaxJson) {
		ajaxJson.setSuccess(true);
		if (o == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("输入参数对象为空");
		}
		try {
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field field : fields) {
				CheckObj checkObj = field.getAnnotation(CheckObj.class);
				if (checkObj != null) {
					field.setAccessible(true);
					Object param = field.get(o);
					field.setAccessible(false);
					boolean status = validRequired(checkObj, param);
					if(!status){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("交易失败：" + checkObj.fieldName() + "不能为空");
						return ajaxJson;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxJson;
	}

	/**
	 * 验证必输
	 * 
	 * @param checkObj
	 * @param param
	 * @throws AppException
	 */
	private static boolean validRequired(CheckObj checkObj, Object param) {
		if (checkObj.required() && null != param && StringUtils.isNotBlank(param.toString())) {
			return true;
		}
		return false;
	}
}
