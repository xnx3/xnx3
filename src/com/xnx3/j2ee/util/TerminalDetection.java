package com.xnx3.j2ee.util;

import javax.servlet.http.HttpServletRequest;
import eu.bitwalker.useragentutils.UserAgent;


/**
 * 客户端的终端检测
 * <br/>需 UserAgentUtils-1.20.jar
 * @author 管雷鸣
 */
public class TerminalDetection {
	//PC操作系统
	public static final String[] PC_SYSTEMS = {"Mac OS X","Windows 10","Windows 8.1","Windows 7","Windows Vista","Windows 2000","Windows XP","Android (Google TV)","Ubuntu"};
	
	/** 
     * 检测是PC端还是手机端访问，先判断PC端，若系统是 Windows或者Max OSX或者Ubuntu，则返回false，其他的都返回true
     * @param request {@link HttpServletRequest}
     * @return true:移动设备接入，false:pc端接入 
     */  
    public static boolean checkMobileOrPc(HttpServletRequest request){    
    	String userAgents = request.getHeader("User-Agent").toLowerCase();
    	boolean isMobile = true;
    	if(userAgents == null){
    		return true;
    	}
    	
    	UserAgent userAgent = UserAgent.parseUserAgentString(userAgents); 
    	if(userAgent == null){
    		return true;
    	}
    	String systemName = userAgent.getOperatingSystem().getName();
    	if(systemName == null){
    		return true;
    	}
    	for(int i=0;i<PC_SYSTEMS.length;i++){
    		if(systemName.equals(PC_SYSTEMS[i])){
    			return false;
    		}
    	}
    	
        return isMobile;  
    }
    

	/** 
     * 检测是PC端还是手机端访问，先判断PC端，若系统是 Windows或者Max OSX或者Ubuntu，则返回false，其他的都返回true
     * @param userAgent 浏览器的 user-agent 字符串
     * @return true:移动设备接入，false:pc端接入 
     */  
    public static boolean checkMobileOrPc(String userAgent){    
    	String userAgents = userAgent.toLowerCase();
    	boolean isMobile = true;
    	if(userAgents == null){
    		return true;
    	}
    	
    	UserAgent ua = UserAgent.parseUserAgentString(userAgents); 
    	if(ua == null){
    		return true;
    	}
    	String systemName = ua.getOperatingSystem().getName();
    	if(systemName == null){
    		return true;
    	}
    	for(int i=0;i<PC_SYSTEMS.length;i++){
    		if(systemName.equals(PC_SYSTEMS[i])){
    			return false;
    		}
    	}
    	
        return isMobile;  
    }
    
    
    public static void main(String[] args) {
    	String ua = "Mozilla/5.0 (Linux; Android 5.1; vivo X6D Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.3.1260(0x26060339) NetType/4G Language/zh_CN";
		System.out.println(checkMobileOrPc(ua));
	}
	
}
