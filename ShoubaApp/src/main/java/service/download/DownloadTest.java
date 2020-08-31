/**
 *@author: chenyoulong 
 *@Title:DownloadTest.java
 *@date 2014-9-2 下午9:21:00 
 *@Description:TODO
 */
package service.download;

import service.encrypt.MD5;
import service.util.SslConnection;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;



/**
 *@ClassName:DownloadTest
 *@author: chenyoulong  Email: chen.youlong@payeco.com
 *@date :2014-9-2 下午9:21:00
 *@Description:TODO 
 */
public class DownloadTest {
	private static String msg_type = "200003"; //下载对账单类型 100004-代付对账单；  200003-代收清算（当天清算记录）； 200004-代收成功 ；
	private static String url = "https://testagent.payeco.com:9444/download"; //对账单下载地址
	private static String merchant_no = "002020000008";//系统商户编号
	private static String merchant_key = "77D58CF7761943B8";//商户秘钥
	private static String trans_date = "20160716"; //下载对账单日期 yyyymmdd
	private static String filePath = "D:\\Test\\代收对账单.txt"; //下载对账单存放路径 
	public static void main(String[] args) {
		testDownloadCheckingFile(); //下载对账文件
	}
	
	
	public static void testDownloadCheckingFile(){
		HttpURLConnection conn = null;
		FileOutputStream fos = null ;
	    
		try {
			
			
			String org_mac=msg_type +" " + merchant_no +" " + trans_date+" " +merchant_key;
			System.out.println("MAC源码："+org_mac);
			MD5 md5 = new MD5();
			String MAC = md5.getMD5ofStr(org_mac);
			
//			String url = "https://agent.payeco.com/download?MSG_TYPE="+msg_type+"&MERCHANT_NO="+merchant_no+"&TRANS_DATE="+trans_date+"&MAC="+MAC;
			String requse_url = url+"?MSG_TYPE="+msg_type+"&MERCHANT_NO="+merchant_no+"&TRANS_DATE="+trans_date+"&MAC="+MAC;
			System.out.println("URL:"+requse_url);
			
			//SSL
			SslConnection ssl = new SslConnection();
			conn = ssl.openConnection(requse_url); 
			conn.setRequestMethod("POST");
			conn.setReadTimeout(60000);
			conn.setConnectTimeout(60000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			InputStream instream = conn.getInputStream();	
			fos = new FileOutputStream(filePath);
			byte[] buf = new byte[1024];
			
			int len = 0;
			
			while((len = instream.read(buf)) != -1){
				fos.write(buf, 0, len);
			}
			fos.flush();
			fos.close();	
			System.out.println("下载成功，已存放到："+filePath);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("下载失败！");
		}finally{
			if(conn != null)
				conn.disconnect();
			
		}
	}
	
	
}
