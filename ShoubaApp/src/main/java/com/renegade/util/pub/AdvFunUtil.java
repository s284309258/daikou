package com.renegade.util.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdvFunUtil {

	
	@Autowired
	private SuffixConfig suffixConfig;
	
/*	*//**
	 * 通过token获取App登录用户信息
	 * 
	 * @param token
	 * @return
	 *//*
	public FrontUserDO getAdvUserInfoDOByToken(String token) {
		int userId = Integer.parseInt(token.split("\\|")[0]);
		return this.frontUserDao.get(userId);
	};
*/
	/**
	 * 校验图片格式后缀
	 * @param fileName
	 * @return
	 */
	public boolean checkPicFile(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (suffixConfig.getPicSuffix().contains(suffix.trim().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 校验视频格式后缀
	 * @param fileName
	 * @return
	 */
	public boolean checkVidFile(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (suffixConfig.getVidSuffix().contains(suffix.trim().toLowerCase())) {
			return true;
		}
		return false;
	}

}
