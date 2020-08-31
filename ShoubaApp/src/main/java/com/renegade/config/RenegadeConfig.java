package com.renegade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="renegade")
public class RenegadeConfig {
	//上传路径
	private String uploadPath;
	//上午交易时间开始
	private String startTimeAm;
	//上午交易时间结束
	private String endTimeAm;
	//下午交易时间开始
	private String startTimePm;
	//下午交易时间结束
	private String endTimePm;

	public String getStartTimeAm() {
		return startTimeAm;
	}

	public void setStartTimeAm(String startTimeAm) {
		this.startTimeAm = startTimeAm;
	}

	public String getEndTimeAm() {
		return endTimeAm;
	}

	public void setEndTimeAm(String endTimeAm) {
		this.endTimeAm = endTimeAm;
	}

	public String getStartTimePm() {
		return startTimePm;
	}

	public void setStartTimePm(String startTimePm) {
		this.startTimePm = startTimePm;
	}

	public String getEndTimePm() {
		return endTimePm;
	}

	public void setEndTimePm(String endTimePm) {
		this.endTimePm = endTimePm;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
}
