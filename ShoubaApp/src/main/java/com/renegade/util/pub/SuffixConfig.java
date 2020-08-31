package com.renegade.util.pub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:quartz.properties")
public class SuffixConfig {

	@Value("${check.picture.suffix}")
	public String picSuffix; // 图片格式后缀

	@Value("${check.video.suffix}")
	public String vidSuffix; // 视频格式后缀

	public String getPicSuffix() {
		return picSuffix;
	}

	public void setPicSuffix(String picSuffix) {
		this.picSuffix = picSuffix;
	}

	public String getVidSuffix() {
		return vidSuffix;
	}

	public void setVidSuffix(String vidSuffix) {
		this.vidSuffix = vidSuffix;
	}
	
}
