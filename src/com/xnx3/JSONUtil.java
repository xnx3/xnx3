package com.xnx3;

import net.sf.json.JSONObject;

/**
 * json 工具类
 * @author 管雷鸣
 *
 */
public class JSONUtil {
	
	/**
	 * 从 JSONObject 对象中取一个字符串。操作的是 json-lib-2.4-jdk15.jar
	 * @param json 要取数据的json对象
	 * @param key 要取的字符串在json中的key
	 * @return 若有值，能正常取到，返回其值。若取不到，或者json中本来就没有这个key，那么返回空字符串""
	 */
	public static String getJsonString(JSONObject json, String key){
		if(json.get(key) != null){
			return json.getString(key);
		}else{
			return "";
		}
	}
	
	/**
	 * 从 JSONObject 对象中取一个int 数值。操作的是 json-lib-2.4-jdk15.jar
	 * @param json 要取数据的json对象
	 * @param key 要取的int值在json中的key
	 * @return 若有值，能正常取到，返回其值。若取不到，或者json中本来就没有这个key，那么返回0
	 */
	public static int getJsonInt(JSONObject json, String key){
		String s = getJsonString(json, key);
		if(s.length() > 0){
			return Lang.stringToInt(s, 0);
		}else{
			return 0;
		}
	}
	
}
