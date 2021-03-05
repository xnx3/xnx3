package com.xnx3;

/**
 * 类 相关的操作工具
 * @author 管雷鸣
 *
 */
public class ClassUtil{
	
	/**
	 * 判断某个class是否存在
	 * @param packageName class的包名，传入如 com.xnx3.wangmarket.plugin.learnExample.Plugin
	 * @return true:class存在，  false:class不存在
	 */
	public static boolean classExist(String packageName){
		try{
			Class.forName(packageName);
			return true;
		}catch(ClassNotFoundException e){
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(classExist("com.xnx3.Languages"));
	}
}