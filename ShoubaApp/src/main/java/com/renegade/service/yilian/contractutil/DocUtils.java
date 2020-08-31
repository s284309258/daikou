package com.renegade.service.yilian.contractutil;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DocUtils {

	private static final String ENCODE = "UTF-8";
	
	/*//合同文件存储路径
	public static final String CONTRACT_PATH = "D:/home/java/upload/contract/";
	//模板存储路径
	public static final String TEMPLATE_PATH = "D:/home/java/resources/templates/";
	//对应的返回相对路径
	private static final String RELATIVE_CONTRACT_PATH = "upload/contract/";*/
	
	
	//合同文件存储路径
	public static final String CONTRACT_PATH = "/home/java/upload/contract/";
	//模板存储路径
	public static final String TEMPLATE_PATH = "/home/java/resources/templates/";
	//对应的返回相对路径
	private static final String RELATIVE_CONTRACT_PATH = "upload/contract/";
	

	public static void main(String[] args) throws Exception {
		String tmplName = "electronicSign.ftl";
		// 获取数据
		Map<String, Object> dataMap = getData();
		System.out.println(createDoc(tmplName,dataMap));
	}
	
	
	/**
	 * 创建合同，doc合同和pdf合同
	 * @param templateName
	 * @param dataMap
	 * @return
	 */
	public static String createDoc(String templateName, Map<String, Object> dataMap){
		Configuration configuration = null;
		FileTemplateLoader templateLoader = null;
		File outFile = null;
		Writer out = null;
		Template t = null;
		String contractDocName = "";
		String contractPdfName = "";
		try{
			//加载合同模板
			configuration = new Configuration();
			templateLoader = new FileTemplateLoader(new File(TEMPLATE_PATH));
			configuration.setTemplateLoader(templateLoader);
			//合同名称
			String contractName = UUIDUtils.generateUUID()+(int)(Math.random()*100);
			//doc合同名称
			contractDocName = contractName+".doc";
			outFile = new File(CONTRACT_PATH + contractDocName);
			// 如果输出目标文件夹不存在，则创建
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			// 将模板和数据模型合并生成文件
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), ENCODE));
			t = configuration.getTemplate(templateName, ENCODE);
			t.process(dataMap, out);
			out.flush();
			//转换成的pdf合同名称
			contractPdfName = contractName+".pdf";
			//将word转换成pdf并返回pdf文件的路径名称
//			docToPDF(CONTRACT_PATH+contractDocName, CONTRACT_PATH+contractPdfName);
			return RELATIVE_CONTRACT_PATH+contractPdfName;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(out != null){
				try {
					out.close();
					//DeleteFileUtil.delete(CONTRACT_PATH+contractDocName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private static Map<String, Object> getData() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name", "张三");
		// 图片
		dataMap.put("img", ImagUtils.GetImageStr("D:/home/java/upload/signImage/0c6aa19dd8c640e58ac9b49bc42d894f.png"));
		return dataMap;
	}
	
	
	/**
	 * 处理PDF转码水印
	 * @return
	 */
//	private static boolean getLicense() {
//		boolean result = false;
//		try {
//			InputStream is = DocUtils.class.getResourceAsStream("license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
//			License aposeLic = new License();
//			aposeLic.setLicense(is);
//			result = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	
	/**
	 * Doc转PDF
	 * @param inPath word文件
	 * @param outPath pdf文件
	 * @return 
	 */
//	private static boolean docToPDF(String contractDocName, String contractPdfName) {
//		FileOutputStream os = null;
//		File file = null;
//		Document doc = null;
//		// 验证License 若不验证则转化出的pdf文档会有水印产生
//		if (!getLicense()) {
//			return false;
//		}
//		try {
//			long old = System.currentTimeMillis();
//			//新建一个空白pdf文档
//			file = new File(contractPdfName);
//			os = new FileOutputStream(file);
//			//contractDocPath是将要被转化的word文档
//			doc = new Document(contractDocName);
//			// 全面支持DOC, DOCX, OOXML, RTF HTML,
//			// OpenDocument, PDF,
//			// EPUB, XPS, SWF 相互转换
//			doc.save(os, SaveFormat.PDF);
//			long now = System.currentTimeMillis();
//			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
//			os.flush();
//			//返回合同名称和路径
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			if (os != null) {
//				try {
//					os.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}

}
