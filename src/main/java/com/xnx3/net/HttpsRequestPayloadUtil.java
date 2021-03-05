package com.xnx3.net;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;  
import com.xnx3.net.https.SSLClient;

/**
 * https request payload
 * <br/>需jar包：
 * <br/>commons-codec-1.6.jar
 * <br/>commons-logging-1.1.1.jar
 * <br/>fluent-hc-4.2.1.jar
 * <br/>httpclient-4.2.1.jar
 * <br/>httpclient-cache-4.2.1.jar
 * <br/>httpcore-4.2.1.jar
 * <br/>httpmime-4.2.1.jar
 * @author 管雷鸣
 */
public class HttpsRequestPayloadUtil {  
	final static Charset charsetObj = Charset.forName("utf-8");  
//	private String cookies = "";
	public Map<String, String> cookies;
	
	public HttpsRequestPayloadUtil() {
		this.cookies = new HashMap<String, String>();
	}
	
	/**
	 * post请求
	 * @param url 要请求的url地址
	 * @param param 请求的参数，会拼接成JSON格式。适用于只有一级结构的json
	 * @return 响应的源码。若失败，返回null
	 */
	public String postByMapParam(String url, Map<String, String> param){
		return post(url, param, null);
	}
	
	/**
	 * post请求
	 * @param url 要请求的url地址
	 * @param paramString 请求的参数。request payload的内容
	 * @return 响应的源码。若失败，返回null
	 */
	public String postByStringParam(String url, String paramString){
		return post(url, null, paramString);
	}
	
	/**
	 * post请求
	 * @param url 要请求的url地址
	 * @param param 请求的参数，会拼接成JSON格式 此处跟 下面 paramString只需传入一个即可
	 * @param paramString 请求的参数。若param传入空，则此处生效。request payload的内容。若 param不为空，则不使用此处
	 * @return 响应的源码。若失败，返回null
	 */
    public String post(String url, Map<String, String> param, String paramString){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{
        	httpClient = new SSLClient();  
        	httpPost = new HttpPost(url);  
//        	httpClient.getState().addCookies(cookies); 
//        	httpClient.getParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH) 
//            httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
//            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            
        	String content = "";
        	if(param != null){
            	StringBuffer p = new StringBuffer();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    p.append("\""+entry.getKey()+"\":\""+entry.getValue()+"\",");
                }
                content = ("{"+p.toString()+"}").replaceAll(",}", "}");
            }else if(paramString != null){
            	content = paramString;
            }
        	
        	StringBuffer cookie = new StringBuffer();
        	  for (Map.Entry<String, String> entry : this.cookies.entrySet()) {
        		  cookie.append(entry.getKey()+"="+entry.getValue()+";");
        	  }
            httpPost.setHeader("Set-Cookie", cookie.toString());
            httpPost.setHeader("Cookie", cookie.toString());
           if(content != null && content.length() > 0){
        	   // 设置请求的数据    
               StringEntity reqEntity = new StringEntity(content);
               reqEntity.setContentType("application/json;charset=UTF-8");    
               httpPost.setEntity(reqEntity);    
           }
//            httpPost.setHeader("Cookie", value);
//            httpPost.setHeader("Cookie", "");
            
        	HttpResponse httpresponse = httpClient.execute(httpPost); 
        	
        	org.apache.http.Header[] headers = httpresponse.getHeaders("Set-Cookie");
        	StringBuffer sb = new StringBuffer();
        	for (int i = 0; i < headers.length; i++) {
//        		sb.append(headers[i].getName()+"="+headers[i].getValue());
//        		String cookie = getCookie(headers[i].getValue());
//        		if(sb.length() > 0){
//        			sb.append("; ");
//        		}
//        		sb.append(cookie);
        		setCookie(headers[i].getValue());
			}
        	
        	HttpEntity entity = httpresponse.getEntity();  
        	String body = EntityUtils.toString(entity);   
            return body; 
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return null;
    }  
    
    /**
     * 设置Cookies，将当前请求的cookie缓存，自动将旧的替换为新的，若无替换的，保留，不多删除
     * @param cookies 传入如THIRD_STATE_CART=; domain=.walgreens.com; expires=Tue, 27-Feb-2018 06:51:04 +00:00; path=/; HttpOnly
     * @return
     */
    public void setCookie(String cookies){
//    	String cookies = "THIRD_STATE_CART=; domain=.walgreens.com; expires=Tue, 27-Feb-2018 06:51:04 +00:00; path=/; HttpOnly";
    	String[] ca = cookies.split(";");
    	String result = "";
    	for (int i = 0; i < ca.length; i++) {
			if(ca[i] != null && ca[i].indexOf("=") > 0){
				String[] nv = ca[i].split("=");
				String name = nv[0].trim();
				String value = "";
				if(nv.length == 2){
					value = nv[1].trim();
				}
				if(!(name.equalsIgnoreCase("domain") || name.equalsIgnoreCase("path") || name.equalsIgnoreCase("expires"))){
					this.cookies.put(name, value);
					return;
				}
			}
		}
    }
    
}  