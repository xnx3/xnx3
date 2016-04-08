package com.xnx3;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 操作跟系统交互、以及调用相关
 * @author 管雷鸣
 */
public class SystemUtil {
	
	/**
	 * 调用当前系统的默认浏览器打开网页
	 * @param url 要打开网页的url
	 */
	public static void openUrl(String url){
		java.net.URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Java运行时环境规范版本,如： 1.6 、1.7
	 * @return 数值 获取失败或转换失败返回 0 ，若成功，返回如： 1.7
	 */
	public static float getJavaSpecificationVersion(){
		String v = System.getProperty("java.specification.version");
		float xnx3_result = 0f;
		if(v == null || v.equals("")){
			xnx3_result = 0;
		}else{
			xnx3_result = Lang.stringToFloat(v, 0);
		}
		return xnx3_result;
	}
	
	/**
	 * 获取当前项目路径，用户的当前工作目录，如当前项目名为xnx3，则会获得其绝对路径 "E:\MyEclipseWork\xnx3"
	 * @return 项目路径
	 */
	public static String getCurrentDir(){
		return System.getProperty("user.dir");
	}
	
	/**
	 * 获取当前Java运行所依赖的Jre的路径所在，绝对路径
	 * @return 如：D:\Program Files\MyEclipse2014\binary\com.sun.java.jdk7.win32.x86_64_1.7.0.u45\jre
	 */
	public static String getJrePath(){
		return System.getProperty("java.home");
	}
	
	
	public static void main(String[] args) {
		System.out.println(getJavaSpecificationVersion());
	}
}
