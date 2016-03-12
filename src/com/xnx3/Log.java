package com.xnx3;

/**
 * 日志输出调试
 * @author 管雷鸣
 *
 */
public class Log {
	static boolean DEBUG=true;
	
	/**
	 * 调试日志输出
	 * @param object 非static的传入this
	 * @param method 那个类那个方法，如 debug 后面不加()，只是方法名字
	 * @param message 日志信息
	 * @see #debug(String, String, String)
	 */
	public void debug(Object object, String method,String message){
		debug(object.getClass().getName(), method, message);
	}
	
	/**
	 * 调试日志输出
	 * @param className 当前class名字，加包名全名，如 com.xnx3.Log
	 * @param method 那个类那个方法，如 debug 后面不加()，只是方法名字
	 * @param message 日志信息
	 */
	public void debug(String className,String method,String message){
		if(DEBUG){
			System.out.println("DEBUG : "+className+"类"+method+"()方法  "+message);
		}
	}
}