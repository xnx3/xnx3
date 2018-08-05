package com.xnx3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class StringUtil {
	private static final String X36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
	/**
	 * 0-9 、 a-z 一共36个字符
	 */
	public static final char[] AZ09CHAR_36 = {'0','1','2','3','4','5','6','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
    /**
     * 26个字母
     */
    public static final char[] AZCHAR_26 = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	/**
	 * UTF8编码，16进制区间
	 */
	public static Object[][] UTF8UNICODE={
						{0x0000,0x007F,"C0控制符及基本拉丁文",},
						{0x0080,0x00FF,"C1控制符及拉丁文补充-1"},
						{0x0100,0x017F,"拉丁文扩展-A"},
						{0x0180,0x024F,"拉丁文扩展-B"},
						{0x0250,0x02AF,"国际音标扩展"},
						{0x02B0,0x02FF,"空白修饰字母"},
						{0x0300,0x036F,"结合用读音符号"},
						{0x0370,0x03FF,"希腊文及科普特文"},
						{0x0400,0x04FF,"西里尔字母"},
						{0x0500,0x052F,"西里尔字母补充"},
						{0x0530,0x058F,"亚美尼亚语"},
						{0x0590,0x05FF,"希伯来文"},
						{0x0600,0x06FF,"阿拉伯文"},
						{0x0700,0x074F,"叙利亚文"},
						{0x0750,0x077F,"阿拉伯文补充"},
						{0x0780,0x07BF,"马尔代夫语"},
						{0x07C0,0x07FF,"西非書面語言"},
						{0x0800,0x085F,"阿维斯塔语及巴列维语"},
						{0x0860,0x087F,"Mandaic"},
						{0x0880,0x08AF,"撒马利亚语"},
						{0x0900,0x097F,"天城文书"},
						{0x0980,0x09FF,"孟加拉语"},
						{0x0A00,0x0A7F,"锡克教文"},
						{0x0A80,0x0AFF,"古吉拉特文"},
						{0x0B00,0x0B7F,"奥里亚文"},
						{0x0B80,0x0BFF,"泰米尔文"},
						{0x0C00,0x0C7F,"泰卢固文"},
						{0x0C80,0x0CFF,"卡纳达文"},
						{0x0D00,0x0D7F,"德拉维族语"},
						{0x0D80,0x0DFF,"僧伽罗语"},
						{0x0E00,0x0E7F,"泰文"},
						{0x0E80,0x0EFF,"老挝文"},
						{0x0F00,0x0FFF,"藏文"},
						{0x1000,0x109F,"缅甸语"},
						{0x10A0,0x10FF,"格鲁吉亚语"},
						{0x1100,0x11FF,"朝鲜文"},
						{0x1200,0x137F,"埃塞俄比亚语"},
						{0x1380,0x139F,"埃塞俄比亚语补充"},
						{0x13A0,0x13FF,"切罗基语"},
						{0x1400,0x167F,"统一加拿大土著语音节"},
						{0x1680,0x169F,"欧甘字母"},
						{0x16A0,0x16FF,"如尼文"},
						{0x1700,0x171F,"塔加拉语"},
						{0x1720,0x173F,"Hanunóo"},
						{0x1740,0x175F,"Buhid"},
						{0x1760,0x177F,"Tagbanwa"},
						{0x1780,0x17FF,"高棉语"},
						{0x1800,0x18AF,"蒙古文"},
						{0x18B0,0x18FF,"Cham"},
						{0x1900,0x194F,"Limbu"},
						{0x1950,0x197F,"德宏泰语"},
						{0x1980,0x19DF,"新傣仂语"},
						{0x19E0,0x19FF,"高棉语记号"},
						{0x1A00,0x1A1F,"Buginese"},
						{0x1A20,0x1A5F,"Batak"},
						{0x1A80,0x1AEF,"Lanna"},
						{0x1B00,0x1B7F,"巴厘语"},
						{0x1B80,0x1BB0,"巽他语"},
						{0x1BC0,0x1BFF,"Pahawh Hmong"},
						{0x1C00,0x1C4F,"雷布查语"},
						{0x1C50,0x1C7F,"Ol Chiki"},
						{0x1C80,0x1CDF,"曼尼普尔语"},
						{0x1D00,0x1D7F,"语音学扩展"},
						{0x1D80,0x1DBF,"语音学扩展补充"},
						{0x1DC0,0x1DFF,"结合用读音符号补充"},
						{0x1E00,0x1EFF,"拉丁文扩充附加"},
						{0x1F00,0x1FFF,"希腊语扩充"},
						{0x2000,0x206F,"常用标点"},
						{0x2070,0x209F,"上标及下标"},
						{0x20A0,0x20CF,"货币符号"},
						{0x20D0,0x20FF,"组合用记号"},
						{0x2100,0x214F,"字母式符号"},
						{0x2150,0x218F,"数字形式"},
						{0x2190,0x21FF,"箭头"},
						{0x2200,0x22FF,"数学运算符"},
						{0x2300,0x23FF,"杂项工业符号"},
						{0x2400,0x243F,"控制图片"},
						{0x2440,0x245F,"光学识别符"},
						{0x2460,0x24FF,"封闭式字母数字"},
						{0x2500,0x257F,"制表符"},
						{0x2580,0x259F,"方块元素"},
						{0x25A0,0x25FF,"几何图形"},
						{0x2600,0x26FF,"杂项符号"},
						{0x2700,0x27BF,"印刷符号"},
						{0x27C0,0x27EF,"杂项数学符号-A"},
						{0x27F0,0x27FF,"追加箭头-A"},
						{0x2800,0x28FF,"盲文点字模型"},
						{0x2900,0x297F,"追加箭头-B"},
						{0x2980,0x29FF,"杂项数学符号-B"},
						{0x2A00,0x2AFF,"追加数学运算符"},
						{0x2B00,0x2BFF,"杂项符号和箭头"},
						{0x2C00,0x2C5F,"格拉哥里字母"},
						{0x2C60,0x2C7F,"拉丁文扩展-C"},
						{0x2C80,0x2CFF,"古埃及语"},
						{0x2D00,0x2D2F,"格鲁吉亚语补充"},
						{0x2D30,0x2D7F,"提非纳文"},
						{0x2D80,0x2DDF,"埃塞俄比亚语扩展"},
						{0x2E00,0x2E7F,"追加标点"},
						{0x2E80,0x2EFF,"CJK 部首补充"},
						{0x2F00,0x2FDF,"康熙字典部首"},
						{0x2FF0,0x2FFF,"表意文字描述符"},
						{0x3000,0x303F,"CJK 符号和标点"},
						{0x3040,0x309F,"日文平假名"},
						{0x30A0,0x30FF,"日文片假名"},
						{0x3100,0x312F,"注音字母"},
						{0x3130,0x318F,"朝鲜文兼容字母"},
						{0x3190,0x319F,"象形字注释标志"},
						{0x31A0,0x31BF,"注音字母扩展"},
						{0x31C0,0x31EF,"CJK 笔画"},
						{0x31F0,0x31FF,"日文片假名语音扩展"},
						{0x3200,0x32FF,"封闭式 CJK 文字和月份"},
						{0x3300,0x33FF,"CJK 兼容"},
						{0x3400,0x4DBF,"CJK 统一表意符号扩展 A"},
						{0x4DC0,0x4DFF,"易经六十四卦符号	"},
						{0x4E00,0x9FBF,"CJK 统一表意符号"},
						{0xA000,0xA48F,"彝文音节"},
						{0xA490,0xA4CF,"彝文字根"},
						{0xA500,0xA61F,"Vai"},
						{0xA660,0xA6FF,"统一加拿大土著语音节补充"},
						{0xA700,0xA71F,"声调修饰字母"},
						{0xA720,0xA7FF,"拉丁文扩展-D"},
						{0xA800,0xA82F,"Syloti Nagri"},
						{0xA840,0xA87F,"八思巴字"},
						{0xA880,0xA8DF,"Saurashtra"},
						{0xA900,0xA97F,"爪哇语"},
						{0xA980,0xA9DF,"Chakma"},
						{0xAA00,0xAA3F,"Varang Kshiti"},
						{0xAA40,0xAA6F,"Sorang Sompeng"},
						{0xAA80,0xAADF,"Newari"},
						{0xAB00,0xAB5F,"越南傣语"},
						{0xAB80,0xABA0,"Kayah Li"},
						{0xAC00,0xD7AF,"朝鲜文音节"},
						{0xD800,0xDBFF,"High-half zone of UTF-16"},
						{0xDC00,0xDFFF,"Low-half zone of UTF-16"},
						{0xE000,0xF8FF,"自行使用區域"},
						{0xF900,0xFAFF,"CJK 兼容象形文字"},
						{0xFB00,0xFB4F,"字母表達形式"},
						{0xFB50,0xFDFF,"阿拉伯表達形式A"},
						{0xFE00,0xFE0F,"变量选择符"},
						{0xFE10,0xFE1F,"竖排形式"},
						{0xFE20,0xFE2F,"组合用半符号"},
						{0xFE30,0xFE4F,"CJK 兼容形式"},
						{0xFE50,0xFE6F,"小型变体形式"},
						{0xFE70,0xFEFF,"阿拉伯表達形式B"},
						{0xFF00,0xFFEF,"半型及全型形式"},
						{0xFFF0,0xFFFF,"特殊"}
					};
	

	/**
	 * 将 十进制 的int数，转化为 0-9 + a-z 的36进制字符串，从而所见其长度
	 * @param num 要转换的十进制数
	 * @return 转换好的36进制字符串
	 */
	public static String intTo36(int num) {
        StringBuffer sBuffer = new StringBuffer();
        if(num == 0) {
            sBuffer.append("0");
        }
        while(num > 0) {
            sBuffer.append(AZ09CHAR_36[num % 36]);
            num = num / 36;
        }
        return sBuffer.reverse().toString();
    }

    public static int _36ToInt(String string) {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < X36.length(); i++) {
            map.put(X36.charAt(i), i);
        }
        int size = string.length();
        int num = 0;
        for(int i = 0; i<size; i++) {
            String char2str = String.valueOf(string.charAt(i)).toUpperCase();
            num = (int) (map.get(char2str.charAt(0)) * Math.pow(36, size - i - 1) + num);
        }
        return num;
    }

	
	/**
	 * 字符串转UTF8编码(16进制如\u7ba1\u96f7\u9e23)
	 * <br/>建议使用 StringToUtf8
	 * @param text 要转成utf8的文字
	 * @return 16进制编码，如\u7ba1\u96f7\u9e23
	 * @deprecated
	 */
	public static String Utf8ToString(String text){
		return StringToUtf8(text);
	}
	
	/**
	 * 字符串转UTF8编码字符串
	 * @param text 要转化你的字符串，如“管雷鸣”
	 * @return UTF8编码字符串，如“\u7ba1\u96f7\u9e23”
	 */
	public static String StringToUtf8(String text){
		if(text == null){
			return null;
		}
		
        StringBuffer output = new StringBuffer();
        for (int j = 0; j < text.length(); j++){
        	String s=Integer.toString(text.charAt(j), 16);
        	while(s.length()<4){
        		s="0"+s;
        	}
            output.append("\\u" +s);
        }
        return output.toString();
	}

	/**
	 * 将UTF-8编码的字符串转换为正常字符串
	 * @param text UTF-8的字符串，如  \u7ba1\u96f7\u9e23
	 * @return 正常字符，如 管雷鸣
	 */
	public static String utf8ToString(String text){
//        String str = null;  
        if(text == null){
        	return null;
        }
        
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");  
	    Matcher matcher = pattern.matcher(text);  
	    char ch;  
	    while (matcher.find()) {  
	        //group 6728  
	        String group = matcher.group(2);  
	        //ch:'木' 26408  
	        ch = (char) Integer.parseInt(group, 16);  
	        //group1 \u6728  
	        String group1 = matcher.group(1);  
	        text = text.replace(group1, ch + "");  
	    }
	    return text;  
	}
	
	/**
	 * 获取制定的utf-8文字编码是哪国什么语言，中文、英语、阿拉伯语、.....
	 * @param text 要检测的UTF8编码，可传入：
	 * 				<li>16进制字符串，如 "\u7ba1"
	 * 				<li>单个字符,如 "管"
	 *				<li>16进制编码,如 "7ba1"
	 * @return 哪国语言，若是返回null，则出错失败
	 */
	public static String getStringLanguage(String text){
		String result=null;
		int param=0;
		if(text.length()==1){
			param=Utf8ToInt(text);
		}else{
			param=Lang.stringToInt(text, 0, 16);
		}
		
		if(param==0){
			return null;
		}

		for (int i = 0; i < UTF8UNICODE.length&&result==null; i++) {
			if(param>=(int)UTF8UNICODE[i][0]&&param<=(int)UTF8UNICODE[i][1]){
				result=UTF8UNICODE[i][2]+"";
			}
		}
		if(result==null){
			result="没有发现此文字对应的语言";
		}
		
		return result;
	}
	

	/**
	 * 字符转UTF8的16进制编码，只支持单个文字转换!若多个只转换第一个
	 * @param text 要转成utf8的文字
	 * @return 16进制编码，如7ba1 ，若是返回0则是失败
	 */
	public static int Utf8ToInt(String text){
		String result=Integer.toString(text.charAt(0), 16);
		if(result.length()==0){
			return 0;
		}else{
			return Lang.stringToInt(result, 0, 16);
		}
	}
	
	/**
	 * 过滤HTML标签，返回文本内容
	 * @param text 要过滤的字符串
	 * @return 过滤掉HTML标签的内容
	 */
	public static String filterHtmlTag(String text){
		if(text == null){
			return null;
		}
		return text.replaceAll("<[.[^<]]*>","");
	}
	
	/**
	 * split 根据指定的字符分割字符串为List输出
	 * @param content 要分隔的目标字符串
	 * @param regex 分隔符，split的传入值
	 * @return 分割好的List
	 */
	public static List<String> split(String content,String regex){
		String[] sa = content.split(regex);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < sa.length; i++) {
			if(sa[i] != null && sa[i].length()>0){
				list.add(sa[i]);
			}
		}
		return list;
	}
	
	/**
	 * 再字符串的某个位置，插入一个新的字符串
	 * <br/>比如原始字符串为 abcd ，在位置1插入，插入一个@符，则会变为 a@bcd
	 * @param sourceString 原始字符串，要将插入的字符串插入到这里
	 * @param insertString 要插入的字符串。若为null或者空字符穿""，则不做任何改动sourceString原样返回
	 * @param place 要插入的字符串，要插入到原始字符串sourceString的位置，其之前有多少个字符，使用indexOf所获得
	 * 				<ul>
	 * 					<li>若传入 -1 ，则不做任何改动sourceString原样返回</li>
	 * 				</ul>
	 */
	public static String insert(String sourceString, String insertString, int place){
		if(insertString == null || insertString.length() == 0 || place == -1){
			return sourceString;
		}
		String start = sourceString.substring(0, place);
		String end = sourceString.substring(place, sourceString.length());
		
		String newString = start+insertString+end;
		return newString;
	}
	

	/**
	 * 对一个原始字符串内，指定两个字符串中间的区域文字进行替换操作，将中间的这段文字替换为另一段
	 * <br/>如:要将字符串1234567890内的3456789替换为abc，最终得到字符串12abc0,则这样写
	 * <pre>subStringReplace("1234567890","34","789","abc");</pre>
	 * @param text 原始字符串
	 * @param startString 要替换的这段区域的文字的开始字符串，替换后会包含这段一块被替换
	 * @param endString 要替换的这段区域的文字的结束，替换后会包含这段一块被替换
	 * @param replaceNewString 这是要替换成新的字符串
	 * @return 返回替换好的。若是替换时，找不到替换的
	 */
	public static String subStringReplace(String text, String startString, String endString, String replaceNewString){
		int start = text.indexOf(startString);
		if(start == -1){
			return text;
		}
		int end = text.substring(start, text.length()).indexOf(endString);
		if(end == -1){
			return text;
		}
		end = end+start;
		
		//如果开始、结束两个字符串都找到了，才进行替换操作
		String stringStart = text.substring(0, start);
		String stringEnd = text.substring(end+endString.length(), text.length());
		return stringStart+replaceNewString+stringEnd;
	}
	
    /**
     * 生成随机长度的英文（a－z，26个英文字母）
     * @param length 生成字符串的长度
     * @return 字符串
     */
    public static String getRandomAZ(int length){
    	//字符长度
    	final int  maxNum = 26;
    	int i;  //生成的随机数
    	int count = 0; //生成的密码的长度
    	
    	StringBuffer pwd = new StringBuffer("");
    	Random r = new Random();
    	while(count < length){
    		//生成随机数，取绝对值，防止生成负数，
    		i = Math.abs(r.nextInt(maxNum));  //生成的数最大为26-1
    		if (i >= 0 && i < AZCHAR_26.length) {
    			pwd.append(AZCHAR_26[i]);
    			count ++;
    		}
    	}
    	return pwd.toString();
    }

    /**
     * 生成随机长度的英文+数字（0-9 10个数字、a－z，26个英文字母）
     * @param length 生成字符串的长度
     * @return 字符串
     */
    public static String getRandom09AZ(int length){
    	//字符长度
    	final int  maxNum = 36;
    	int i;  //生成的随机数
    	int count = 0; //生成的密码的长度
    	
    	StringBuffer pwd = new StringBuffer("");
    	Random r = new Random();
    	while(count < length){
    		//生成随机数，取绝对值，防止生成负数，
    		i = Math.abs(r.nextInt(maxNum));  //生成的数最大为26-1
    		if (i >= 0 && i < AZ09CHAR_36.length) {
    			pwd.append(AZ09CHAR_36 [i]);
    			count ++;
    		}
    	}
    	return pwd.toString();
    }

    
	/**
	 * 过滤XSS攻击有关的字符。将其转化为无效标签。过滤script、frame、;、等
	 * @param text 要过滤的原始字符
	 * @return 生成的无XSS的安全字符
	 */
	public static String filterXss(String text){
		if(text == null){
			return null;
		}
		
		text = text.replaceAll(";", "；");
		
		//过滤，忽略大小写
		String[] filterTagArray = {"script","frame "};
		for (int i = 0; i < filterTagArray.length; i++) {
			Pattern p = Pattern.compile(filterTagArray[i], Pattern.CASE_INSENSITIVE);  
	        Matcher m = p.matcher(text);  
	        text = m.replaceAll("xss_"+filterTagArray[i]);  
		}
		
		text = text.replaceAll("<", "&lt;");
		text = text.replaceAll(">", "&gt;");
		
		text = text.replaceAll("\\(", "&#40;");
		text = text.replaceAll("\\)", "&#41;");
		
		text = text.replaceAll("'", "&#39;");
		text = text.replaceAll("eval\\((.*)\\)", "");
		text = text.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        
		return text;
	}
	
	/**
	 * 将输入流 {@link InputStream} 转化为 {@link String}
	 * @param in 要转化的 输入流 {@link InputStream}
	 * @param encode 编码，如 UTF-8
	 * @return 转换好的String字符串
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream in, String encode) throws IOException{
		if(in == null){
			return null;
		}
		return IOUtils.toString(in, encode);
	}
	
	/**
	 * 将 {@link String} 字符串转化为 {@link InputStream}输入流
	 * @param text 要转换的字符串
	 * @param encode 编码，如UTF-8
	 * @return 转换好的输入流
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream stringToInputStream(String text, String encode) throws UnsupportedEncodingException{
		if(text == null){
			return null;
		}
		return new ByteArrayInputStream(text.getBytes(encode));  
	}
	

	/**
	 * 将当前字符串内的空格、换行、tab缩进等空白符号移除
	 * <br/>若传入null，会原样返回
	 * @param str 要移除空白符号的字符串
	 * @return 移除空白字符后的字符串
	 */
	public static String removeBlank(String str) {
        String newStr = null;
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            newStr = m.replaceAll("");
        }
        return newStr;
    }
	
	/**
	 * 两个String字符串比较是否相等。可一个为null、一个不为null进行对比
	 * @param S1 要比较的第一个字符串
	 * @param S2 要比较的第二个字符串
	 * @return true:相等
	 */
	public static boolean StringEqual(String S1, String S2){
		return StringEqual(S1, S2, false);
	}
	
	/**
	 * 两个String字符串比较是否相等。可一个为null、一个不为null进行对比
	 * @param S1 要比较的第一个字符串
	 * @param S2 要比较的第二个字符串
	 * @param removeBlank 是否移除空格、换行、tab缩进等空白符号，true为自动将两个字符串的空符移除掉后在进行比较
	 * @return true:相等
	 */
	public static boolean StringEqual(String S1, String S2, boolean removeBlank){
		return StringEqual(S1, S2, removeBlank , false);
	}
	
	/**
	 * 两个String字符串比较是否相等。可一个为null、一个不为null进行对比
	 * @param S1 要比较的第一个字符串
	 * @param S2 要比较的第二个字符串
	 * @param removeBlank 是否移除空格、换行、tab缩进等空白符号，true为自动将两个字符串的空符移除掉后在进行比较
	 * @param ignoreNull 忽略空，若为true，即 null 跟 空字符串"" 视为相等的。若是字符串为"null"，也会自动视为空字符串
	 * @return true:相等
	 */
	public static boolean StringEqual(String S1, String S2, boolean removeBlank, boolean ignoreNull){
		if(ignoreNull){
			if(S1 == null || S1.equals("null")){
				S1 = "";
			}
			if(S2 == null || S2.equals("null")){
				S2 = "";
			}
		}else{
			//先进行为空判断
			if(S1 == null && S2 != null){
				return false;
			}
			if(S1 != null && S2 == null){
				return false;
			}
			if(S1 == null && S2 == null){
				return true;
			}
		}
		
		//若都不是为空，有字符，在对字符进行判断
		if(removeBlank){
			if(S1.length() > 0){
				S1 = removeBlank(S1);
			}
			if(S2.length() > 0){
				S2 = removeBlank(S2);
			}
		}
		
		if(S1.equals(S2)){
			return true;
		}else{
			return false;
		}
	}
	

	/**
	 * 字符串中
	 * <br/>1.首先，若首字母是大写，则转小写
	 * <br/>2.然后将字符串中出现的大写字母转为下划线加小写
	 * <br/>（相当于JPA实体类，会自动将数据表的名字转换成数据库名一般）
	 * @param s 要转换的字符串
	 */
	public static String firstCharToLowerCase(String s){
		if(s==null || s.equals("")){    
			return "";    
        }
		
		//首字母转小写
		if(Character.isLowerCase(s.charAt(0))){
		}else{
			s = (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}

		//中间大写字母，专为下划线+小写字母
		Pattern p=Pattern.compile("[A-Z]");    
        StringBuilder builder=new StringBuilder(s);    
        Matcher mc=p.matcher(s);    
        int i=0;    
        while (mc.find()) {  
            builder.replace(mc.start()+i, mc.end()+i, "_"+mc.group().toLowerCase());    
            i++;    
        }    
        if('_' == builder.charAt(0)){    
            builder.deleteCharAt(0);    
        }
        return builder.toString();    
	}
	
	/**
	 * 对某个字符串进行位移操作，利用位移来进行简单的加密、解密
	 * @param text 要加密或者解密的字符串
	 * @param shiftNum 位移的值
	 * @return 加密或者解密的字符串
	 */
	public static String encrypt(String text, int shiftNum){
		char[] array = text.toCharArray();// 获取字符数组  
		for (int i = 0; i < array.length; i++) {// 遍历字符数组  
            array[i] = (char) (array[i] ^ 1);// 对每个数组元素进行异或运算  
        }
		return new String(array);
	}
	
	 /**
     * UTF-8格式汉字转换为%E4%BD%A0形式
     * @param content 要转换的字符串内容
     * @return String 转换好的字符串
     */
    public static String stringToUrl(String content) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将%E4%BD%A0转换为UTF-8格式汉字 
     * @param content 要转换的内容
     * @return String 转换好的内容
     */
    public static String urlToString(String content) {
        StringBuffer sbuf = new StringBuffer();
        int l = content.length();
        int ch = -1;
        int b, sumb = 0;
        for (int i = 0, more = -1; i < l; i++) {
            /* Get next byte b from URL segment s */
            switch (ch = content.charAt(i)) {
            case '%':
                ch = content.charAt(++i);
                int hb = (Character.isDigit((char) ch) ? ch - '0'
                        : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                ch = content.charAt(++i);
                int lb = (Character.isDigit((char) ch) ? ch - '0'
                        : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                b = (hb << 4) | lb;
                break;
            case '+':
                b = ' ';
                break;
            default:
                b = ch;
            }
            /* Decode byte b as UTF-8, sumb collects incomplete chars */
            if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)   
                sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb   
                if (--more == 0)
                    sbuf.append((char) sumb); // Add char to sbuf   
            } else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)   
                sbuf.append((char) b); // Store in sbuf   
            } else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)   
                sumb = b & 0x1f;
                more = 1; // Expect 1 more byte   
            } else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)   
                sumb = b & 0x0f;
                more = 2; // Expect 2 more bytes   
            } else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)   
                sumb = b & 0x07;
                more = 3; // Expect 3 more bytes   
            } else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)   
                sumb = b & 0x03;
                more = 4; // Expect 4 more bytes   
            } else /*if ((b & 0xfe) == 0xfc)*/{ // 1111110x (yields 1 bit)   
                sumb = b & 0x01;
                more = 5; // Expect 5 more bytes   
            }
            /* We don't test if the UTF-8 encoding is well-formed */
        }
        return sbuf.toString();
    }
	
    /**
	 * 正则替换
	 * @param text 操作的内容源，主体
	 * @param regex 替换掉的
	 * @param replacement 替换成新的，取而代之的
	 * @return 替换好的内容
	 */
	public static String replaceAll(String text, String regex, String replacement){
		String s[] = {"?","(",")"}; 
		for (int i = 0; i < s.length; i++) {
			regex = regex.replaceAll("\\"+s[i], "\\\\"+s[i]);
		}
		text = text.replaceAll(regex, replacement);
		
		return text;
	}
	

	
	/**
	 * 过滤英文特殊字符 `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~
	 * @param text 要过滤得原始字符串
	 * @return 过滤完后得字符串
	 */
	public static String filterEnglishSpecialSymbol(String text){
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(text);
		return m.replaceAll("").trim();
	}
	
	/**
	 * 过滤中文特殊字符 ！@#￥%……&*（）——+|{}【】‘；：”“’。，、？
	 * @param text 要过滤得原始字符串
	 * @return 过滤完后得字符串
	 */
	public static String filterChineseSpecialSymbol(String text){
		String regEx="[！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(text);
		return m.replaceAll("").trim();
	}
	
	public static void main(String[] args) {
		System.out.println(intTo36(90));
	}
}
