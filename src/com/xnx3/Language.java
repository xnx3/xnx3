package com.xnx3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xnx3.ConfigManagerUtil;
import com.xnx3.j2ee.util.CookieUtil;

/**
 * 语言包。
 * <br/>多种语言，如中文、英语、日语等
 * <br/>应用中需要选择切换到自己的语言的，可以使用
 * @author 管雷鸣
 */
public class Language {
	/*
	 * 当用户选择使用哪种语言后，会在某个地方缓存下来这个语种的名字，这个缓存的名字统一都是叫这个。比如，session缓存、cookie缓存，cookie的名字便是调用这个的
	 */
	public final static String cacheLanguagePackageName = "language_default";	
	
	/**
	 * 用户可自由切换当前语言，此处为语言库配置的(language.xml)，默认使用哪种语言包。（默认设想的是根据IP智能判断，若是在中国则使用简体中文尚未做ip地域匹配）
	 * chinese:简体中文
	 * english:英语
	 */
	public static String language_default="chinese";
	
	/**
	 * language.get("chinese").get("collect_notCollectOneself")
	 */
	public static Map<String, Map<String, String>> languageMap; 

	static{
		language_default = ConfigManagerUtil.getSingleton("language.xml").getValue("defaultLanguage");
		loadLanguagePackage();
	}
	
	/**
	 * 设置当前用户使用的语言包语种，是哪种，如：chinese、english
	 * @param languagePackageName 要设置的语种，如：chinese、english
	 * @return true:设置成功；   false:设置失败，语言包中没有这个语种
	 */
//	public boolean setLanguagePackageName(String languagePackageName) {
//		//判断 language.xml 中是否有这个语言包
//		if(languageMap.get(languagePackageName) != null){
//			this.languagePackageName = languagePackageName;
//			return true;
//		}else{
//			return false;
//		}
//	}

	public static void main(String[] args) {

		for (Map.Entry<String, Map<String, String>> entry : languageMap.entrySet()) {
			System.out.println(entry.getKey()+" ---->  "+entry.getValue().size());
			
			for (Map.Entry<String, String> e : entry.getValue().entrySet()) {
				System.out.println("   "+e.getKey());
			}
			
		}
	}

	/**
	 * 从language.xml中获取语言包的名字
	 * 
	 */
	private static void loadLanguagePackage(){
		languageMap = new HashMap<String, Map<String,String>>();
		
		ConfigManagerUtil config = ConfigManagerUtil.getSingleton("language.xml");
		Iterator englishIt = config.getFileConfiguration().getKeys();
		Map<String, String> languageMap = new HashMap<String, String>();
		while(englishIt.hasNext()){
			String key = (String) englishIt.next();
			if(key.indexOf(".")>-1){
				String[] l = key.split("\\.");
				languageMap.put(l[0], l[0]);
			}
		}
		
		Iterator<Map.Entry<String, String>> it = languageMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			loadLanguagePackage(entry.getKey());
		}
	}
	

	/**
	 * 从language.xml加载语言包
	 * @param languageName 语言名，如chinese、english等
	 */
	private static void loadLanguagePackage(String languageName){
		ConfigManagerUtil config = ConfigManagerUtil.getSingleton("language.xml");
		Iterator englishIt = config.getFileConfiguration().getKeys(languageName);
		Map<String, String> englishMap = new HashMap<String, String>();
		while(englishIt.hasNext()){
			String key = (String) englishIt.next();
			String value = config.getValue(key);
			englishMap.put(key.replace(languageName+".", ""), value);
		}
		languageMap.put(languageName, englishMap);
	}
	
	/**
	 * 获取语言包language.xml中的描述文字显示出来。如果没有找到返回空字符""
	 * @param packageName 要显示的描述文字是使用的哪个语言包的，这里是语言包的名字，如 chinese 、 english。若传入null，则使用默认的语言包
	 * @param key 要调用文字的代码
	 * @return 显示出来的文字
	 */
	public static String show(String packageName, String key){
		if(packageName == null){
			packageName = language_default;
		}
		if(key == null){
			return "";
		}
		String value = languageMap.get(packageName).get(key);
		if(value == null){
			value = "";
		}
		return value;
	}
	
	/**
	 * 获取当前多语言配置中，有多少种语言包、或有多少语种、有多少种语言
	 * <br/>可用于，给用户选择使用哪种语种时，调取当前的所有语种
	 * @return 语种的List列表，如 chinese,english
	 */
	public static List<String> getLanguagePackageList(){
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, Map<String, String>> entry : languageMap.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}
	
	/**
	 * 判断当前多语言配置(language.xml)中，是否有叫这个名字的语言包，若有，返回true
	 * <br/>此方法多用在用户设置语言包后，缓存或保存时，判断一下用户设置的这个语言包是否真的存在
	 * @param packageName 语言包的名字，语种的名字，如chinese、english
	 * @return true：有这个语言包
	 */
	public static boolean isHaveLanguagePackageName(String packageName){
		//判断 language.xml 中是否有这个语言包
		if(languageMap.get(packageName) != null){
			return true;
		}else{
			return false;
		}
	}
}
