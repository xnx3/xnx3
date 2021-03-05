package com.xnx3.j2ee.util;

import javax.servlet.http.Cookie;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
/**
 * Cookie常用操作工具类
 * @author 管雷鸣
 *
 */
public class CookieUtil {  
    private HttpServletRequest request;  
    private HttpServletResponse response;  
    private int age;//Cookie的有效期
    
    /**
     * 
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param age cookie有效期
     * 				<li>0 ：不记录cookie
     * 				<li>-1：会话级cookie，关闭浏览器失效
     * 				<li>>0：过期时间，单位：秒
     */
    public CookieUtil(HttpServletRequest request,HttpServletResponse response,int age) {  
        this.request = request;  
        this.response = response;  
        this.age=age;  
    }  
    
    /**
     * cookie有效时间一年
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    public CookieUtil(HttpServletRequest request,HttpServletResponse response) {
		this.request = request;
		this.response = response;  
		this.age = 60 * 60 * 24 * 365 ;	//过期时间：一年 
	}
    
    /**
     * 增加cookie
     * @param name cookie的名字
     * @param value cookie的值
     */
    public void addCookie(String name, String value) {  
        Cookie cookies = new Cookie(name, value);  
        cookies.setPath("/");  
        cookies.setMaxAge(age);  
        response.addCookie(cookies);  
    }
    
    /**
     * 获取cookie的值
     * @param cookieName 要取的cookie名字
     * @return cookie的值
     * 			<li>若取不到这个名字的cookie，则返回null
     */
    public String getCookieValue(String cookieName) {  
        if (cookieName != null) {  
            Cookie cookie = getCookie(cookieName);  
            if(cookie!=null){  
                return cookie.getValue();  
            }  
        }  
        return null;  
    }  

    /**
     * 获取cookie
     * @param cookieName cookie的名字
     * @return {@link Cookie}
     * 			<li>若没有这个名字的cookie，则返回null
     */
    public Cookie getCookie(String cookieName){  
        Cookie[] cookies = request.getCookies();  
        Cookie cookie = null;  
        try {  
            if (cookies != null && cookies.length > 0) {  
                for (int i = 0; i < cookies.length; i++) {  
                    cookie = cookies[i];  
                    if (cookie.getName().equals(cookieName)) {  
                        return cookie;  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    /**
     * 删除cookie
     * @param cookieName
     * @return
     */
    public void deleteCookie(String cookieName) {  
        if (cookieName != null) {  
            Cookie cookie = getCookie(cookieName);  
            if(cookie!=null){  
            	//如果0，立即删除  
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);  
            }  
        }  
    }  

}  