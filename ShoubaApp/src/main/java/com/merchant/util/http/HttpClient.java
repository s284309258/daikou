package com.merchant.util.http;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.merchant.demo.base.ConstantSsl.CHARSET;
import com.merchant.util.http.ssl.TLSSocketFactory;

/**
 * 描述：Http通讯类
 * @author Payeco
 *
 */
public class HttpClient {
	public static String REQUEST_METHOD_GET = "GET";
	public static String REQUEST_METHOD_POST = "POST";
	public static String REQUEST_COOKIE = "Cookie";
	public static String SET_COOKIE = "Set-Cookie";
	
	protected int status;
	protected String cookies;
	protected Proxy proxy;
	
	public HttpClient(){
		
	}

	public HttpClient(Proxy proxy){
		this.proxy = proxy;
	}
	
	
	/**
	 * 发送Post请求,超时时间默认为300秒
	 * @param postURL			请求地址
	 * @param requestBody		请求参数
	 * @param sendCharset		发送字节编码格式
	 * @param readCharset		读取字节编码格式
	 * @return
	 */
	public  String send(String postURL, String requestBody, String sendCharset, String readCharset) {
		return send(postURL, requestBody, sendCharset, readCharset, 300, 300, REQUEST_METHOD_POST);
	}
	
	/**
	 * 发送指定方式(Get/Post)请求,超时时间默认为300秒
	 * @param postURL			请求地址
	 * @param requestBody		请求参数
	 * @param sendCharset		发送字节编码格式
	 * @param readCharset		读取字节编码格式
	 * @param RequestMethod		GET或POST
	 * @return
	 */
	public  String send(String postURL, String requestBody, String sendCharset, String readCharset, String RequestMethod) {
		return send(postURL, requestBody, sendCharset, readCharset, 300, 300, RequestMethod);
	}
	
	/**
	 * 发送Post请求
	 * @param postURL  			访问地址
	 * @param requestBody  		paramName1=paramValue1&paramName2=paramValue2
	 * @param sendCharset  		发送字符编码
	 * @param readCharset  		返回字符编码
	 * @param connectTimeout  	连接主机的超时时间 单位:秒
	 * @param readTimeout 		从主机读取数据的超时时间 单位:秒
	 * @return 通讯返回
	 */
	public  String send(String url, String requestBody, String sendCharset, String readCharset,int connectTimeout,int readTimeout) {
		return send(url, requestBody, sendCharset, readCharset, connectTimeout, readTimeout,REQUEST_METHOD_POST);
	}
	
	/**
	 * 发送指定方式(Get/Post)请求
	 * @param postURL  			访问地址
	 * @param requestBody  		paramName1=paramValue1&paramName2=paramValue2
	 * @param sendCharset  		发送字符编码
	 * @param readCharset  		返回字符编码
	 * @param connectTimeout  	连接主机的超时时间 单位:秒
	 * @param readTimeout 		从主机读取数据的超时时间 单位:秒
	 * @param RequestMethod 	GET或POST
	 * @return 通讯返回
	 */
	public  String send(String url, String requestBody, String sendCharset, String readCharset,int connectTimeout,int readTimeout,String RequestMethod) {
		try {
			return connection(url, requestBody, sendCharset, readCharset, connectTimeout, readTimeout, RequestMethod, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("发送请求[" + url + "]失败，" + ex.getMessage());
			return null;
		} 
	}
	
	/**
	 * 发送指定方式(Get/Post)请求
	 * @param postURL			访问地址
	 * @param requestBody  		paramName1=paramValue1&paramName2=paramValue2
	 * @param sendCharset  		发送字符编码
	 * @param readCharset  		返回字符编码
	 * @param connectTimeout  	连接主机的超时时间 单位:秒
	 * @param readTimeout 		从主机读取数据的超时时间 单位:秒
	 * @param RequestMethod 	GET或POST
	 * @return
	 * @throws Exception
	 */
	public String connection(String postURL, String requestBody, String sendCharset, String readCharset, int connectTimeout, int readTimeout, String RequestMethod)throws Exception {
		return connection(postURL, requestBody, sendCharset, readCharset, connectTimeout, readTimeout, RequestMethod, null);
	}
	
	/**
	 * 发送指定方式(Get/Post)请求
	 * @param postURL			访问地址
	 * @param requestBody  		paramName1=paramValue1&paramName2=paramValue2
	 * @param sendCharset  		发送字符编码
	 * @param readCharset  		返回字符编码
	 * @param connectTimeout  	连接主机的超时时间 单位:秒
	 * @param readTimeout 		从主机读取数据的超时时间 单位:秒
	 * @param RequestMethod 	GET或POST
	 * @param reqHead			请求头信息
	 * @return
	 * @throws Exception
	 */
	public String connection(String postURL, String requestBody, String sendCharset, String readCharset,int connectTimeout,int readTimeout,String RequestMethod, Map<String,String> reqHead)throws Exception {
		if(REQUEST_METHOD_POST.equals(RequestMethod)){
			return postConnection(postURL,requestBody,sendCharset,readCharset,connectTimeout,readTimeout,reqHead);
		}else if(REQUEST_METHOD_GET.equals(RequestMethod)){
			return getConnection(postURL,requestBody,sendCharset,readCharset,connectTimeout,readTimeout,reqHead);
		}else{
			return "";
		}
	}
	
	private HttpURLConnection createHttpURLConnection(String url) throws IOException{
		URL postUrl = new URL(url);
		if (!url.contains("https:")) {
			if(proxy != null){
				return (HttpURLConnection) postUrl.openConnection(proxy);
			}else{
				return (HttpURLConnection) postUrl.openConnection();
			}
		} else {
			HttpsURLConnection httpsConn = null;
			if(proxy != null){
				httpsConn = (HttpsURLConnection) postUrl.openConnection(proxy);
			}else{
				httpsConn = (HttpsURLConnection) postUrl.openConnection();
			}
			httpsConn.setSSLSocketFactory(new TLSSocketFactory());

			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					//System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
					return true;
				}
			};
			httpsConn.setHostnameVerifier(hv);
			
			return  (HttpURLConnection) httpsConn;
		}
	}
	
	public String getConnection(String url, String requestBody, String sendCharset, String readCharset, int connectTimeout, int readTimeout, Map<String, String> reqHead)throws Exception {
		HttpURLConnection httpConn = null;
		try {
			httpConn = createHttpURLConnection(url);
			
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + sendCharset);
			if (null != cookies){
                httpConn.setRequestProperty(REQUEST_COOKIE, cookies);
            }
			
			if (reqHead != null && reqHead.size() > 0) {
                for(String key : reqHead.keySet()){
                	httpConn.setRequestProperty(key, reqHead.get(key));
                }
            }
			
			// 连接主机的超时时间（单位：毫秒）
			httpConn.setConnectTimeout(1000 * connectTimeout);
			// 从主机读取数据的超时时间（单位：毫秒） 
			httpConn.setReadTimeout(1000 * readTimeout);
			// 连接,从postUrl.openConnection()至此的配置必须要在 connect之前完成,要注意的是connection.getOutputStream会隐含的进行 connect。
			httpConn.connect();
		
			int status = httpConn.getResponseCode();
			setStatus(status);
			if (status != HttpURLConnection.HTTP_OK) {
				System.out.println("发送请求失败,状态码：[" + status + "] 返回信息：" + httpConn.getResponseMessage());
				return null;
			}
			//保留cookie
            fetchCookie(httpConn.getHeaderFields());
            
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), readCharset));
			StringBuffer responseSb = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseSb.append(line.trim());
			}
			reader.close();
			return responseSb.toString().trim();
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
	}
	
	public String postConnection(String postURL, String requestBody, String sendCharset, String readCharset,int connectTimeout,int readTimeout, Map<String,String> reqHead)throws Exception {
		HttpURLConnection httpConn = null;
		try {
			httpConn = createHttpURLConnection(postURL);
			
			// 设置是否向httpUrlConnection输出,因为这个是post请求,参数要放在http正文内,因此需要设为true, 默认情况下是false; 
			httpConn.setDoOutput(true);
			// 设置是否从httpUrlConnection读入,默认情况下是true; 
			httpConn.setDoInput(true);
			// 设定请求的方法为"POST",默认是GET 
			httpConn.setRequestMethod("POST");
			// Post 请求不能使用缓存 
			httpConn.setUseCaches(false);
			//进行跳转
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + sendCharset);
			if (null != cookies){
			    httpConn.setRequestProperty(REQUEST_COOKIE, cookies);
			}
			
			if (reqHead != null && reqHead.size() > 0) {
                for(String key : reqHead.keySet()){
                	httpConn.setRequestProperty(key, reqHead.get(key));
                }
            }
			
			// 连接主机的超时时间（单位：毫秒）
			httpConn.setConnectTimeout(1000 * connectTimeout);
			// 从主机读取数据的超时时间（单位：毫秒） 
			httpConn.setReadTimeout(1000 * readTimeout);
			// 连接,从postUrl.openConnection()至此的配置必须要在 connect之前完成,要注意的是connection.getOutputStream会隐含的进行 connect。
			httpConn.connect();
			DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());
			out.write(requestBody.getBytes(sendCharset));
			out.flush();
			out.close();
			int status = httpConn.getResponseCode();
			setStatus(status);
			if (status != HttpURLConnection.HTTP_OK) {
				System.out.println("发送请求失败,状态码：[" + status + "] 返回信息：" + httpConn.getResponseMessage());
				return null;
			}
			//保留cookie
			fetchCookie(httpConn.getHeaderFields());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), readCharset));
			StringBuffer responseSb = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseSb.append(line.trim());
			}
			reader.close();
			return responseSb.toString().trim();
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	/**
	 * 访问返回值有换行
	 * @param postURL  访问地址
     * @param requestBody  paramName1=paramValue1&paramName2=paramValue2
     * @param sendCharset  发送字符编码
     * @param readCharset  返回字符编码
	 * @return
	 */
    public String sendNotTrim(String postURL, String requestBody, String sendCharset, String readCharset) {
    	// 连接主机的超时时间 单位:秒
    	int connectTimeout = 300;
    	// 从主机读取数据的超时时间 单位:秒
        int readTimeout = 300;
        HttpURLConnection httpConn = null;
        try {
        	httpConn = createHttpURLConnection(postURL);
            
            //设置是否向httpUrlConnection输出,因为这个是post请求,参数要放在http正文内,因此需要设为true, 默认情况下是false; 
            httpConn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入,默认情况下是true; 
            httpConn.setDoInput(true);
            // 设定请求的方法为"POST",默认是GET 
            httpConn.setRequestMethod("POST");
            // Post 请求不能使用缓存 
            httpConn.setUseCaches(false);
            //进行跳转
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + sendCharset);
            if (null != cookies){
                httpConn.setRequestProperty(REQUEST_COOKIE, cookies);
            }
            
            //连接主机的超时时间（单位：毫秒）
            httpConn.setConnectTimeout(1000 * connectTimeout);
            //从主机读取数据的超时时间（单位：毫秒） 
            httpConn.setReadTimeout(1000 * readTimeout);
            // 连接,从postUrl.openConnection()至此的配置必须要在 connect之前完成,
            // 要注意的是connection.getOutputStream会隐含的进行 connect。
            httpConn.connect();
            DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());
            out.write(requestBody.getBytes(sendCharset));
            out.flush();
            out.close();
            int status = httpConn.getResponseCode();
            setStatus(status);
            if (status != HttpURLConnection.HTTP_OK) {
                System.out.println("发送请求失败,状态码：[" + status + "] 返回信息："  + httpConn.getResponseMessage());
                return null;
            }
            //保留cookie
            fetchCookie(httpConn.getHeaderFields());
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), readCharset));
            StringBuffer responseSb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseSb.append(line + "\n");
            }
            reader.close();
            return responseSb.toString().trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("发送请求[" + postURL + "]失败," + ex.getMessage());
            return null;
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
    }
    
    /**
	 * 用流的方法向http服务器发送数据
	 * @param urlTarget 目的url
	 * @param data 需要发送的字符串数据
	 * @param encoding 字符集
	 * @return
	 * @throws Exception
	 */
	public String sendStream(String urlTarget, String data, CHARSET encoding) throws Exception{
		URL url = new URL(urlTarget);
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Content-Type", "binary/octet-stream");
		if (null != cookies){
            conn.setRequestProperty(REQUEST_COOKIE, cookies);
        }
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), encoding.value());

		out.write(data);
		out.flush();
		out.close();
		
		//保留cookie
		fetchCookie(conn.getHeaderFields());
		
		InputStream in = conn.getInputStream();
		byte[] bs = read(in);
		if (bs == null){
		    return null;
		}
		return new String(bs, encoding.value());
	}
	
	public static byte[] read(InputStream in){
		int BUFFER_SIZE = 4096;
		byte[] recvBytes = null;
		//定义用于接收服务器返回的数据的缓冲区
		byte[] readBuffer = new byte[BUFFER_SIZE];
		//定义一个用于存储所有服务器发送过来的数据
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int readInt = 0;
		try{
			while ((readInt = in.read(readBuffer)) >= 0) {
				if (readInt == 0){
					continue;
				}
				if (readInt <= BUFFER_SIZE) {
					//将此数据存入byte流中
					bos.write(readBuffer, 0, readInt);
				}
			}
		}catch (Throwable e){
			e.printStackTrace();
		}

		//如果byte流中存有数据
		if (bos.size() > 0) {
			//建立一个普通字节数组存取缓冲区的数据
			recvBytes = bos.toByteArray();
		}

		return recvBytes;
	}
    
	
	/**
	 * 循环获取Cookie信息放到属性中
	 * @param headerFields
	 */
	protected void fetchCookie(Map<String,List<String>> headerFields){
	    Set<String> set = headerFields.keySet();
	    for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
	        String key = (String) iterator.next();
	        if (SET_COOKIE.equalsIgnoreCase(key)) {
	            List<String> list = headerFields.get(key);
	            StringBuilder builder = new StringBuilder();
	            for (String str : list) {
	                builder.append(str).toString();
	            }
	            cookies=builder.toString();
	        }
	    }
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
}
