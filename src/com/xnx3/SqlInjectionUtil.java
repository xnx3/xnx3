package com.xnx3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql 防注入
 * @author 管雷鸣
 */
public class SqlInjectionUtil {
	/**
	 * 防止SQL注入的关键字
	 */
	private final static String[] INJECT_KEYWORD = {"sitename", "net user", "xp_cmdshell", "like'", "and", "exec", "execute", "insert", "create", "drop", "table", "from", "grant", "use", "group_concat", "column_name", "information_schema.columns", "table_schema", "union", "where", "select", "delete", "update", "order", "by", "count", "chr", "mid", "master", "truncate", "char", "declare", "or", "like"};
	private final static String[] INJECT_SPECIAL_CHARACTER = {"'", ";", "--", ",", "%", "#","*","+"};
	/**
	 * 防止SQL注入的关键字对应的全角字符
	 */
	private final static String[] KEYWORD_FULL_STR = {"ｓｉｔｅｎａｍｅ", "ｎｅｔ　ｕｓｅｒ", "ｘｐ＿ｃｍｄｓｈｅｌｌ", "ｌｉｋｅ＇", "ａｎｄ", "ｅｘｅｃ", "ｅｘｅｃｕｔｅ", "ｉｎｓｅｒｔ", "ｃｒｅａｔｅ", "ｄｒｏｐ", "ｔａｂｌｅ", "ｆｒｏｍ", "ｇｒａｎｔ", "ｕｓｅ", "ｇｒｏｕｐ＿ｃｏｎｃａｔ", "ｃｏｌｕｍｎ＿ｎａｍｅ", "ｉｎｆｏｒｍａｔｉｏｎ＿ｓｃｈｅｍａ．ｃｏｌｕｍｎｓ", "ｔａｂｌｅ＿ｓｃｈｅｍａ", "ｕｎｉｏｎ", "ｗｈｅｒｅ", "ｓｅｌｅｃｔ", "ｄｅｌｅｔｅ", "ｕｐｄａｔｅ", "ｏｒｄｅｒ", "ｂｙ", "ｃｏｕｎｔ", "ｃｈｒ", "ｍｉｄ", "ｍａｓｔｅｒ", "ｔｒｕｎｃａｔｅ", "ｃｈａｒ", "ｄｅｃｌａｒｅ", "ｏｒ", "ｌｉｋｅ"};
	private final static String[] SPECIAL_CHARACTER_FULL_STR = {"＇", "；", "－－", "，", "％", "＃", "＊","＋"};

	public static void main(String[] args) {
		String text = " user ' hjor and net user ds andka";
		
		System.out.println(filter(text));
	}

	/**
	 * 过滤字符串的值，防止被sql注入
	 * @param value 要过滤的字符串
	 * @return 将有危险的字符，替换为其全角字符，将安全的字符串返回
	 * @author 管雷鸣
	 */
	public static String filter(String value){
		if(value == null){
			return null;
		}
		//原始的，未进行全部转化为小写的字符串，每次找到风险关键词后，替换完毕每一个都会更新到此处。
		String originalValue = value;	
		//全部变为小写之后的字符串
		String lowerValue = value.toLowerCase();
		
		//检查关键字
		for (int i = 0; i < INJECT_KEYWORD.length; i++) {
			Pattern p = Pattern.compile("\\b"+INJECT_KEYWORD[i]+"\\b");
	        Matcher m = p.matcher(lowerValue);
	        while (m.find()) {
	        	//发现了风险关键词，进行字符串重组
				originalValue = originalValue.substring(0, m.start()) + KEYWORD_FULL_STR[i] + originalValue.substring(m.end(), originalValue.length());
	        }
		}
		
		//检查特殊字符
		for (int i = 0; i < INJECT_SPECIAL_CHARACTER.length; i++) {
			//统一转为小写后进行判断
			int index = originalValue.toLowerCase().indexOf(INJECT_SPECIAL_CHARACTER[i]);
			if(index != -1){
				//发现了风险关键词，进行字符串重组
				originalValue = originalValue.substring(0, index) + SPECIAL_CHARACTER_FULL_STR[i] + originalValue.substring(index+INJECT_KEYWORD[i].length(), originalValue.length());
			}
		}
		
		return originalValue;
	}
	
	
}
