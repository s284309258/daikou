package com.renegade.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * 图片处理工具类
 * @author Administrator
 *
 */
public class ImgUtil {
	
	//public static final String ImageFile = "D:\\images\\signImage\\";
	
	//签名保存路径
	//public static final String SIGNFILE_PATH = "D:/home/java/upload/";
	
	public static final String SIGNFILE_PATH = "/home/java/upload/";
	
	
	/**
	 * 对字节数组字符串进行Base64解码并生成图片返回图片图片路径
	 * 生成签名图片并且返回签名图片的路径
	 * @param imageData：签名数据源：base64位字符串
	 * @return
	 */
	public static String GenerateImage(String imageData,String dir) {
		String str = "";
		// 文件存储路径
		String filepath = SIGNFILE_PATH;
		// 图像数据为空  
		if (imageData == null) {
			return null;  
		}
		// 对字节数组字符串进行Base64解码并生成图片
		BASE64Decoder decoder = new BASE64Decoder();  
		OutputStream out=null;
		try {  
			imageData=imageData.replace(" ", "+");
			// Base64解码  
			byte[] bytes = decoder.decodeBuffer(imageData);  
			for (int i = 0; i < bytes.length; ++i) {  
				if (bytes[i] < 0) {// 调整异常数据  
					bytes[i] += 256;  
				}  
			}  
			//文件绝对路径
			filepath = filepath+dir+"/";
			//如果文件夹不存在则创建    
			File directory =new File(filepath); 
			if(!directory.exists()&&!directory.isDirectory()){       
			    directory.mkdir();    
			} 
			//文件相对路径
			String fileName1=System.currentTimeMillis()+(int)(Math.random()*100)+".png";
			// 文件绝对路径
			String fileName = filepath + fileName1;
			out = new FileOutputStream(fileName);                                        
			// 生成png图片  
			out.write(bytes);  
			out.flush();
			return dir+"/"+ fileName1;
		} catch (Exception e) {  
			e.printStackTrace();
			return null;  
		}finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}  
	
	
	
	/**
	 * 获取图片Base64编码
	 * @param imgFile：图片绝对路径
	 * @return
	 */
	public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		imgFile=SIGNFILE_PATH+imgFile;
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
}

