package com.renegade.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 根据返回编码和中英文type获取返回值
 * @author Administrator
 *
 */
//@Configuration
public class PropertyUtil {
	
	 private static Properties props_CH;
	 private static Properties props_EN;
	 
	 

	 @PostConstruct
	 public void loadProps() {
		 System.out.println("开始加载properties文件内容.......");
		 props_CH = new Properties();
		 props_EN = new Properties(); 
		 InputStream in_CH = null;
		 InputStream in_EN = null;
		 try {
			 in_CH = PropertyUtil.class.getClassLoader().getResourceAsStream("config/china.properties");
			 in_EN = PropertyUtil.class.getClassLoader().getResourceAsStream("config/english.properties");
			 props_CH.load(in_CH);
			 props_EN.load(in_EN);
		} catch (FileNotFoundException e) {
			System.out.println(".properties文件未找到");
		} catch(IOException e){
			System.out.println("出现IOException");
		} finally {
			try {
                if(null != in_CH) {
                	in_CH.close();
                }
                if(null != in_EN){
                	in_EN.close();
                }
            } catch (IOException e) {
                System.out.println(".properties文件流关闭出现异常");
            }
		}
		
		 System.out.println("加载properties文件内容完成...........");
	 }
	 
	 /**
	  * 根据指定的key，获取返回值
	  * @param key
	  * @return
	  */
	 public static String getProperty(String key,String type){
	      if("EN".equals(type)){
	    	  return props_EN.getProperty(key);
	      } else {
	    	  return props_CH.getProperty(key);
	      }  
	 }
	 
}
