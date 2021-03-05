package com.xnx3.j2ee.util;

import javax.servlet.http.HttpServletRequest;

import com.xnx3.Lang;

/**
 * IP相关操作
 * @author 管雷鸣
 *
 */
public class IpUtil {
	
	/**
	 * 获取IP地址，只会返回一个IP
	 * @param request
	 * @return <li>成功：返回一个ip
	 * 			<li>失败：返回null
	 */
	public static String getIpAddress(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    
	    //如果有多个ip，拿最前面的
	    if(ip.indexOf(",")>0){
	    	ip=Lang.subString(ip, null, ",", 2);
	    }
	    
	    if(ip.equals("0:0:0:0:0:0:0:1")){
	    	ip = "127.0.0.1";
	    }
	    return ip;  
	}   
	
}
