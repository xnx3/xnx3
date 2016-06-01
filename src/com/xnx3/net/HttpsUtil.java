package com.xnx3.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpsUtil {
	
	public static void main(String[] args) {
		String url = "https://120.52.121.75:8443/QuerySummary";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Host", "120.52.121.75:8443");
		headers.put("Accept", "*/*");
		headers.put("Accept-Language", "zh-Hans-CN;q=1");
		headers.put("Cookie", "44EDF024-CD2E-45B0-86EC-6BD8328027DB");
		headers.put("Connection", "keep-alive");
		headers.put("User-Agent", "Mozilla/5.0 (Ios;9.3.2;iPhone;iPhone);Version/1.3.7;ISN_GSXT");

		Map<String, String> param  = new HashMap<String, String>();
		param.put("AreaCode", 10000+"");
		param.put("Limit", "50");
		param.put("Page", "1");
		param.put("Q", "青岛国大期货经纪有限公司");
		
	}
	
	
	private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
	
    /**
     * GET方式打开网址，返回源代码
     * @param url 请求url
     * @param headers header头
     * @return 网页源代码。若出错，返回null
     */
    public static String get(String url,Map<String, String> headers){
    	try {
			return send(url, null, headers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * GET方式获取网页源代码
     * @param url 请求url
     * @return 网页源代码。若出错，返回null
     */
    public static String get(String url){
    	try {
			return send(url, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * POST方式获取网页源代码
     * @param url 请求url
     * @param parameters 传递参数集合，会解析为 key=value&key=value
     * @param headers header头
     * @return 网页源代码。若出错，返回null
     */
    public String post(String url,Map<String, String> parameters,Map<String, String> headers){
    	try {
			return send(url, HttpUtil.mapToQueryString(parameters), headers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * POST获取网页源代码
     * @param url 请求url
     * @param parameters 传递参数集合，会解析为 key=value&key=value
     * @return 网页源代码。若出错，返回null
     */
    public String post(String url,Map<String, String> parameters){
    	try {
			return send(url, HttpUtil.mapToQueryString(parameters), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    
    /**
     * 获取网页源代码
     * @param url 请求的url
     * @param post POST要提交的数据。可为null，为不提交数据
     * @param headers header头
     * @return 网页源代码
     * @throws Exception
     */
    public static String send(String url,String post,Map<String, String> headers) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if(post != null && post.length()>0){
        	headers.put("Content-Length", post.length()+"");
        }
        
        if(headers != null){
        	for (Map.Entry<String, String> entry : headers.entrySet()) {  
            	conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		if(post != null && post.length()>0){
			PrintWriter writer = new PrintWriter(conn.getOutputStream());
			writer.print(post);
			writer.flush();
			writer.close();
		}

		String line;
		BufferedReader bufferedReader;
		StringBuilder sb = new StringBuilder();
		InputStreamReader streamReader = null;
		
		try {
			streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
		} catch (IOException e) {
			streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");
		} finally {
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
			}
		}
		return sb.toString();
    }
}
