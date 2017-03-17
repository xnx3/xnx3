package com.xnx3.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 模拟HTTPS请求工具类
 * @author 管雷鸣
 */
public class HttpsUtil {
	private String encode; 	//默认编码格式
    private String cookies="";	//每次请求都用自动发送此cookies,请求完毕后自动更新此cookies
	
    public HttpsUtil() {
    	this.encode = Charset.defaultCharset().name(); 
	}

    /**
     * 设置好编码类型，若不设置则默认是Java虚拟机当前的文件编码
     * @param encode 使用时首先会自动获取请求地址的编码，获取编码失败时才会使用此处的编码<br/> {@link HttpUtil#UTF8} {@link HttpUtil#GBK}
     */
    public HttpsUtil(String encode) { 
        this.encode = encode; 
    } 
   
    /**
     * 设置默认的响应字符集，若不设置默认是UTF-8编码
     * @param encode 字符编码 ，默认使用UTF-8，传入参数如{@link HttpUtil#GBK}
     */ 
    public void setEncode(String encode) { 
        this.encode = encode; 
    } 
    
    /**
     * 获取上次请求完成后获得的Cookies
     * @return cookies
     */
    public String getCookies() {
		return cookies;
	}
    
    /**
     * 设置请求时会附带传递的cookies
     * @param cookies {@link #getCookies()}获取到的值
     */
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public static void main(String[] args) {
		HttpsUtil h = new HttpsUtil();
		System.out.println(h.get("https://www.baidu.com"));
		new HttpsUtil().get("https://www.baidu.com").getContent();
	}
	
	
    /**
     * GET方式打开网址，返回源代码
     * @param url 请求url
     * @param headers header头
     * @return {@link HttpResponse}
     */
    public HttpResponse get(String url,Map<String, String> headers){
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
     * @return {@link HttpResponse}
     */
    public HttpResponse get(String url){
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
     * @param parameters 传递参数集合，会解析为 "key=value&key=value"
     * @param headers header头
     * @return {@link HttpResponse}
     */
    public HttpResponse post(String url,Map<String, String> parameters,Map<String, String> headers){
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
     * @param parameters 传递参数集合，会解析为 "key=value&key=value"
     * @return {@link HttpResponse}
     */
    public HttpResponse post(String url,Map<String, String> parameters){
    	try {
			return send(url, HttpUtil.mapToQueryString(parameters), new HashMap<String, String>());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    
    /**
     * 获取网页源代码
     * @param url 请求的url
     * @param post POST要提交的数据。可为null，为不提交数据。若有POST数据，格式可为 "a=1&b=2"
     * @param headers header头
     * @return {@link HttpResponse}
     * @throws Exception
     */
    public HttpResponse send(String url,String post,Map<String, String> headers) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Cookie", this.cookies);
        
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
		this.cookies=conn.getHeaderField("Set-Cookie");
		
		return makeContent(url, conn,sb.toString());
    }
    

    /**
     * 得到响应对象
     * @param urlConnection
     * @param content 网页内容
     * @return 响应对象
     * @throws IOException
     */ 
    private HttpResponse makeContent(String urlString, HttpURLConnection urlConnection, String content) throws IOException { 
        HttpResponse httpResponser = new HttpResponse(); 
        try { 
            httpResponser.contentCollection = new Vector<String>(); 
            String ecod = urlConnection.getContentEncoding(); 
            if (ecod == null) 
                ecod = this.encode; 
            httpResponser.urlString = urlString; 
            this.cookies=urlConnection.getHeaderField("Set-Cookie");
            httpResponser.cookie=this.cookies;
            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort(); 
            httpResponser.file = urlConnection.getURL().getFile(); 
            httpResponser.host = urlConnection.getURL().getHost(); 
            httpResponser.path = urlConnection.getURL().getPath(); 
            httpResponser.port = urlConnection.getURL().getPort(); 
            httpResponser.protocol = urlConnection.getURL().getProtocol(); 
            httpResponser.query = urlConnection.getURL().getQuery(); 
            httpResponser.ref = urlConnection.getURL().getRef(); 
            httpResponser.userInfo = urlConnection.getURL().getUserInfo(); 
            httpResponser.content = content;
            httpResponser.contentEncoding = ecod; 
            httpResponser.code = urlConnection.getResponseCode(); 
            httpResponser.message = urlConnection.getResponseMessage(); 
            httpResponser.contentType = urlConnection.getContentType(); 
            httpResponser.method = urlConnection.getRequestMethod(); 
            httpResponser.connectTimeout = urlConnection.getConnectTimeout(); 
            httpResponser.readTimeout = urlConnection.getReadTimeout(); 
            httpResponser.headerFields = urlConnection.getHeaderFields();
            return httpResponser; 
        } catch (IOException e) { 
            throw e; 
        } finally { 
            if (urlConnection != null) 
                urlConnection.disconnect(); 
        } 
    } 
    
    public static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
