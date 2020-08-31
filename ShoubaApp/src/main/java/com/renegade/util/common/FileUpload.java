package com.renegade.util.common;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.renegade.config.AjaxJson;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;


public class FileUpload {
	
	public static final String realPath = "/home/java/upload/";
	
//	public static final String realPath = "D:/home/java/upload/";
	
	//多文件上传
	public static String uploadItemPictire(MultipartFile[] files,HttpServletRequest request,String dir) {
		String str = "";
		// 得到服务器路径
		String filepath = request.getSession().getServletContext().getRealPath("upload/"+dir)+"/";
		System.out.println("服务器路径filepath==============="+filepath);
		File directory =new File(filepath); 
		//如果文件夹不存在则创建    
		if(!directory.exists()&&!directory.isDirectory()){       
		    System.out.println("//不存在");  
		    directory .mkdir();    
		} 
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile multipartFile = files[i];
				// 文件相对路径
				String fileName1 = System.currentTimeMillis() + multipartFile.getOriginalFilename();
				// 文件绝对路径
				String fileName = filepath + fileName1;
				File file = new File(fileName);
				try {
					multipartFile.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("上传失败");
				}
				str = str + "upload/"+dir+"/"+ fileName1;
				if (i != (files.length - 1)) {
					str = str + ",";
				}
			}
		}
		return str;
	}

		
	/**
	 * 单文件上传
	 * @param uploadFile
	 * @param request
	 * @param dir
	 * @return
	 */
	public static String uploadItemPictire(MultipartFile uploadFile, HttpServletRequest request) {
		String str = "";
		// 得到服务器路径
		String filepath = request.getSession().getServletContext().getRealPath("upload");
		System.out.println("服务器路径filepath==============="+filepath);
		File directory =new File(filepath); 
		//如果文件夹不存在则创建    
		if(!directory.exists()&&!directory.isDirectory()){       
			System.out.println("//不存在");  
			directory .mkdir();    
		} 
		if (!uploadFile.isEmpty()) {
			// 文件相对路径
			String fileName1 = System.currentTimeMillis() + uploadFile.getOriginalFilename();
			// 文件绝对路径
			String fileName = filepath +"\\"+ fileName1;
			File file = new File(fileName);
			try {
				uploadFile.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("上传失败");
			}
			str = str + "upload/"+ fileName1;
		}
		return str;
	}
	
	
	/**
	 * 上传文件（可带灵活文件夹）
	 * @param uploadFile
	 * @param request
	 * @return
	 */
	public static String uploadFile1(MultipartFile uploadFile, HttpServletRequest request,String dir) {
		String str = "";
		// 得到服务器路径
		String filepath = request.getSession().getServletContext().getRealPath("upload/"+dir)+"/";
		System.out.println("服务器路径filepath==============="+filepath);
		File directory =new File(filepath); 
		//如果文件夹不存在则创建    
		if(!directory.exists()&&!directory.isDirectory()){       
		    System.out.println("//不存在");  
		    directory .mkdir();    
		} 
		if (!uploadFile.isEmpty()) {
			// 文件相对路径
			String fileName1 = System.currentTimeMillis() + uploadFile.getOriginalFilename();
			// 文件绝对路径
			String fileName = filepath + fileName1;
			File file = new File(fileName);
			try {
				uploadFile.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("上传失败");
			}
			str = str + "upload/"+dir+"/"+ fileName1;
		}
		return str;
	}
	
	
	/**
	 * 上传到服务器上的路径（单个文件夹）
	 * @param uploadFile
	 * @param request
	 * @param dir
	 * @return
	 */
	public static String uploadFile(MultipartFile uploadFile, String dir)throws Exception {
		//得到上传路径
		String filepath = realPath+dir+"/";
		System.out.println("服务器路径filepath==============="+filepath);
		File directory =new File(filepath); 
		//如果文件夹不存在则创建    
		if(!directory.exists()&&!directory.isDirectory()){       
			System.out.println("文件夹不存在");
		    directory.mkdir();    
		    CreateMultilayerFile(filepath);
		} 
		if (!uploadFile.isEmpty()) {
			// 文件相对路径
			String fileName1 = System.currentTimeMillis() + StringUtil.getDateTimeAndRandomForID()+StringUtil.getLastStrAfter(uploadFile.getOriginalFilename());
			// 文件绝对路径
			String fileName = filepath + fileName1;
			File file = new File(fileName);
			try {
				uploadFile.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("上传失败");
				throw e;
			}
			return "upload/"+dir+"/"+ fileName1;
		}
		return null;
	}
	

	/**
     * 创建多个文件夹
     * */
	private static boolean CreateMultilayerFile(String dir) {
		try {
			File dirPath = new File(dir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
		} catch (Exception e) {
			System.out.println("创建多层目录操作出错: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 图片上传秘钥
	public static final String qinduyun_public_key = "yF0b1F4ED38qfQP-igOcaZPuulcfUVr4o6Sontce";
	// 图片上传公钥
	public static final String qinduyun_private_key = "YOM7jGDa-hRY1Mdg0YF1Id74Op_SeBNDkgaMRRPa";
	// 图片创建名
	public static final String qinduyun_private_bucket = "zfqg";

	public static final String qiniu_domain = "http://cdn.szbypos.com/";

	/**
	 * 上传图片到七牛云
	 *
	 * @param file
	 * @return
	 */
	public static AjaxJson uploadImg(MultipartFile file) {
		AjaxJson ajaxJson = new AjaxJson();
		Configuration cfg = new Configuration(Zone.zone2());
		String json = null;
		UploadManager uploadManager = new UploadManager(cfg);
		Auth auth = Auth.create(qinduyun_public_key, qinduyun_private_key);
		String upToken = auth.uploadToken(qinduyun_private_bucket);
		String fileName = UUID.randomUUID() + file.getOriginalFilename();
		try {
			Response response = uploadManager.put(file.getBytes(), fileName, upToken);
			if (response.statusCode == 200) {
				String rotationgoodsImg = qiniu_domain + fileName;
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				System.out.println("=========>>>>>>>>>>>>>>>>>>>>>>>>存贮到七牛云上的key值：" + putRet.key);
				System.out.println(putRet.hash);
				System.out.println("文件上传成功");
				json = rotationgoodsImg;
				ajaxJson.setMsg("上传成功");
				ajaxJson.setData(json);
				ajaxJson.setSuccess(true);
				return ajaxJson;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setMsg("上传失败");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		return null;
	}

    /**
     * 创建多层目录
     * */
    public static void main(String[] args) {
        System.out.println(FileUpload.CreateMultilayerFile("D:/test1/test1/test1/"));

    }

}
