package com.renegade.util.common;
 
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
 
 
 
public class HttpClient {
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();
	private static boolean checkTimeout = false;
	private static HttpClient instance = null;
 
	private HttpClient() {
		// test
	}
 
	public static HttpClient getInstance() {
		if (instance == null) {
			instance = new HttpClient();
		}
		return instance;
	}
 
	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 */
	public static String sendHttpPost(String httpUrl) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		return sendHttpPost(httpPost);
	}
 
	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param params
	 *            参数(格式:key1=value1&key2=value2)
	 */
	public static String sendHttpPost(String httpUrl, String params) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
 
	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param maps
	 *            参数
	 */
	public static String sendHttpPost(String httpUrl, Map<String, Object> maps) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key).toString()));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(nameValuePairs);
		return sendHttpPost(httpPost);
	}
 
	/**
	 * 发送Post请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			if (checkTimeout)
				httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			System.out.println(httpPost);
			System.out.println(entity);
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
 
	/**
	 * 发送 get请求
	 * 
	 * @param httpUrl
	 */
	public static String sendHttpGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpGet(httpGet);
	}
 
	/**
	 * 发送 get请求Https
	 * 
	 * @param httpUrl
	 */
	public static String sendHttpsGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpsGet(httpGet);
	}
 
	/**
	 * 发送Get请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			if (checkTimeout)
				httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
 
	/**
	 * 发送Get请求Https
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpsGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			if (checkTimeout)
				httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			// Ulog.error(e);
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
 
	public static String sendHttpsPost(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			// Ulog.error(e);
		}
		try {
			return sendHttpsPost(httpPost);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
 
	private static String sendHttpsPost(HttpPost httpPost) throws Exception {
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();
		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslcontext))
				.build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		HttpClients.custom().setConnectionManager(connManager);
		// 创建自定义的httpclient对象
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		String body = "";
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, "UTF-8");
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();
		return body;
	}
 
	/**
	 * 绕过验证
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLContext createIgnoreVerifySSL() throws Exception {
		SSLContext sc = SSLContext.getInstance("SSL");
		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString)
					throws CertificateException {
			}
 
			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString)
					throws CertificateException {
			}
 
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
 
	/**
	 * 是否校验超时时间
	 * 
	 * @return true：校验<br>
	 *         false：不校验(默认)
	 */
	public boolean isCheckTimeout() {
		return checkTimeout;
	}
 
	/**
	 * 设置是否校验超时时间
	 * 
	 * @param checkTimeout
	 *            true：校验<br>
	 *            false：不校验(默认)
	 */
	public void setCheckTimeout(boolean checkTimeout) {
		this.checkTimeout = checkTimeout;
	}
}
