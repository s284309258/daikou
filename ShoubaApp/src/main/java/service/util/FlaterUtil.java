/**
 *@author: chenyoulong 
 *@Title:FlaterUtil.java
 *@date 2020-5-27 ????5:12:38 
 *@Description:TODO
 */
package service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *@ClassName:FlaterUtil
 *@author: chenyoulong  Email: chen.youlong@payeco.com
 *@date :2020-5-27 ????5:12:38
 *@Description:TODO  flater ???
 */
public class FlaterUtil {
	/**
	 * ???base64?????????????
	 * @Title: inFlaterFromBase64 
	 * @Description: TODO 
	 * @param base64Content
	 * @param charset
	 * @throws Exception 
	 * @return String
	 */
	public static String inFlaterFromBase64(String base64Content,String charset) throws Exception{
		byte[] fileArray = inflater(Base64.decode(base64Content));
		return new String(fileArray,charset);
	}

	
	/**
	 * ?????.
	 * 
	 * @param inputByte
	 *            byte[]?????????????
	 * @return ????????????
	 * @throws IOException
	 */
	public static byte[] inflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (Exception ex) {
			System.err.println("Data format error!\n");
			ex.printStackTrace();
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}
	
	
	/**
	 * ???????????base64?????
	 * @Title: deflaterFromString 
	 * @Description: TODO 
	 * @param orgStr
	 * @param charset
	 * @return 
	 * @return String
	 * @throws IOException 
	 */
	public static String deflaterFromString(String orgStr,String charset) throws IOException{
		return Base64.encode(deflater(orgStr.getBytes(charset)));
	}
	
	/**
	 * ???.
	 * 
	 * @param inputByte
	 *            ??????????byte[]????
	 * @return ??????????
	 * @throws IOException
	 */
	public static byte[] deflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.deflate(result);
				o.write(result, 0, compressedDataLength);
			}
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}
	
	
	public static void main(String[] args) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("1?????????????????????|3423|???????|asfs").append("\r\n");
		buf.append("2????????鷢??|3423|???????|asfs").append("\r\n");
		buf.append("3?????????????????????|3423|???????|asfs").append("\r\n");
		buf.append("4?????????????????????|3423|???????|asfs").append("\r\n");
		
		System.out.println("????"+buf.toString());
		
		String base64Str = deflaterFromString(buf.toString(), "UTF-8");
		System.out.println("?????base64???????"+base64Str);
		
		String orgContent = inFlaterFromBase64(base64Str, "UTF-8");
		System.out.println("?????????"+orgContent);
		
	}
}
