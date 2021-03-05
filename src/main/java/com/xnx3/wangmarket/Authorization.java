package com.xnx3.wangmarket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.net.AuthHttpUtil;
import com.xnx3.net.HttpResponse;
import net.sf.json.JSONObject;

/**
 * @author 管雷鸣
 *
 */
public class Authorization {
	public static Boolean copyright = false;	//是否显示版权信息。true显示网市场版权信息 
	public static String auth_id;	//授权码
	private static String domain;	//泛解析的二级域名
	private static int version;		//当前系统的版本号，格式如 4001000
	private static int softType = 1;	//当前系统类型，1:网市场云建站(默认)  2:网市场云商城  。。。
	
	private static int sleeptime;	//延迟等待的时间，单位毫秒 
	
	private static int gainNumber = 0;	//获取次数，也就是从云端请求授权信息的次数
	
	private static Thread thread;	//授权检测线程。每间隔1小时检测一次
	/**
	 * 设置当前网市场系统的泛解析主域名
	 * @param setDomain
	 */
	public static void setDomain(String setDomain){
		if(setDomain == null || setDomain.equals("")){
			Authorization.domain = "null";
		}else{
			Authorization.domain = setDomain;
		}
	}
	/**
	 * 设置当前网市场系统的版本号
	 * @param version 格式如 4009000
	 */
	public static void setVersion(Integer version){
		if(version == null){
			Authorization.version = 0;
			return;
		}
		Authorization.version = version;
	}
	
	public static void setSoftType(int softType) {
		Authorization.softType = softType;
	}
	/**
	 * 执行授权验证。 只需要调用一次即可。当然调用多次也无效。
	 * 需要提前设置好 domain、version 、 softType
	 */
	public static void auth(){
		if(thread == null){
			//会先从wangMarketConfig.xml中取，如果没有配置，再从  application.properties 中取
			ConfigManagerUtil c = ConfigManagerUtil.getSingleton("wangMarketConfig.xml");
			auth_id = c.getValue("authorize");
			if(auth_id == null || auth_id.equals("")){
				//再去读application.properties配置文件中的授权码
				String className = "com.xnx3.j2ee.util.ApplicationPropertiesUtil";
				try {
					Class<?> cla = Class.forName(className);
					Object invoke = null;
					try {
						invoke = cla.newInstance();
						//运用newInstance()来生成这个新获取方法的实例  
						Method m = cla.getMethod("getProperty",new Class[]{String.class});
						//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
						Object o = m.invoke(invoke, new Object[]{"authorize"});
						if(o != null && !o.equals("null")){
							auth_id = o.toString();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
			}
			if(auth_id != null && auth_id.trim().length() == 64){
				//是64位授权码，可以判断是否是真的授权，每间隔半小时进行一次
				sleeptime = 1000 * 60 * 30;
			}else{
				//授权码都没有，肯定未授权用户
				sleeptime = 1000 * 60 * 60 * 24;	//一天请求一次
			}
			
			
			//创建授权线程
			thread = new Thread(new Runnable() {
				public void run() {
					//给30秒延迟时间，给启动项目时间
					try {
						Thread.sleep(30*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					while(true){
						AuthHttpUtil http = new AuthHttpUtil();
						Map<String, String> params = new HashMap<String, String>();
						params.put("auth", auth_id);
						params.put("domain", domain);
						params.put("softType", softType+"");
						params.put("version", Authorization.version+"");
						
						HttpResponse hr = null;
						try {
							hr = http.post("http://cloud.wscso.com/auth", params);
						} catch (Exception e) {
							System.out.println("authorization service exception, but does not affect the system, you can still feel free to use !");
							//如果异常，那估计就是联网不大行了，退出 ，并按照已授权来处理
							copyright = false;	//不显示版权
							return;
						}
						
						if(hr.getCode() == 200){
							//正常，响应 200
							JSONObject json = JSONObject.fromObject(hr.getContent().trim());
							if(json.get("result") != null){
								String result = json.getString("result");
								if(result.equals("1")){
									//授权
									copyright = false;	//不显示版权
								}else{
									//非授权
									copyright = true;	//显示版权
								}
							}else{
								//异常
								//判断是否是第一次运行，如果是软件刚开启时，出现联网异常，那么就认为是已授权的好了
								if(gainNumber == 0){
									copyright = false;	//不显示版权
								}
								
								//如果不是第一次运行了，那就还是按照上次所取到的状态来就好
								gainNumber++;
							}
							
						}else{
							//非正常，响应码不是200，那么就忽略好了,并且以已经授权来处理
							copyright = false;	//不显示版权
						}
						
						//延迟。没间隔一段时间就会请求验证授权是否有效
						try {
							Thread.sleep(sleeptime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
	
	public static void main(String[] args) {
		
	}
	
	static{
		auth();
	}
}
