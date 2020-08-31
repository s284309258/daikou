package com.renegade.util;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class UpLoadQinDuYun {
    public static R upload( MultipartFile file,String fileName){
    	 Configuration cfg = new Configuration(Zone.zone0());
	    	//...其他参数参考类注释
	    	UploadManager uploadManager = new UploadManager(cfg);
	    	String key = null;
	    	Auth auth = Auth.create(Constant.qinduyun_public_key,Constant.qinduyun_private_key);
	    	String upToken = auth.uploadToken(Constant.qinduyun_private_bucket);
	    	try {
	    		 Response response = uploadManager.put(file.getBytes(),fileName, upToken);
	    		 if(response.statusCode==200){
	    			 DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
	 				System.out.println("=========>>>>>>>>>>>>>>>>>>>>>>>>存贮到七牛云上的key值："+putRet.key);
	 				System.out.println(putRet.hash);
	 				System.out.println("文件上传成功");
	 				return R.ok();
	    		 }else{
	    			 return R.error();
	    		 }
			} catch (Exception e) {
				e.printStackTrace();
				return R.error();
			}
	    	
    }

}
