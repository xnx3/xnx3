package com.xnx3;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.swing.JOptionPane;

/**
 * 常用方法集合
 * @author 管雷鸣
 */
public class Lang {
	/**
	 * 当前版本号
	 */
	public final static float version=2.1f;
	
	/**
	 * confirm弹出提示框-选择了是
	 * @deprecated
	 */
	public final static int CONFIRM_YES=0;
	
	/**
	 * confirm弹出提示框-选择了否
	 * @deprecated
	 */
	public final static int CONFIRM_NO=1;
	
	/**
	 * confirm弹出提示框-选择了取消
	 * @deprecated
	 */
	public final static int CONFIRM_CENCEL=2;
	
	/**
	 * 字符型转换为整数型
	 * @param param 待转换的字符串
	 * @param defaultValue 异常后的返回值，默认值
	 * @return 整数
	 */
	public static int stringToInt(String param,int defaultValue){
		int xnx3_result=0;
		
		//首先判断字符串不能为空
		if(param==null||param.equalsIgnoreCase("null")){
			xnx3_result=defaultValue;
		}else{
			try {
				xnx3_result=Integer.parseInt(param);
			} catch (Exception e) {
				xnx3_result=defaultValue;
			}
		}
		
		return xnx3_result;
	}
	
	
	
	/**
	 * 字符型转换为整数型
	 * @see Lang#stringToInt(String, int)
	 * @deprecated
	 */
	public static int Integer_(String param,int defaultValue){
		return stringToInt(param, defaultValue);
	}
	

	/**
	 * 字符串转换为各个进制的整数型
	 * @param param  要转换的
	 * @param defaultValue  转换出错异常时返回的值
	 * @param radix  进制。16进制转换则为16
	 * @return  返回int
	 */
	public static int stringToInt(String param,int defaultValue,int radix){
		int xnx3_result=0;
		
		//首先判断字符串不能为空
		if(param==null||param.equalsIgnoreCase("null")){
			xnx3_result=defaultValue;
		}else{
			try {
				xnx3_result=Integer.parseInt(param, radix);
			} catch (Exception e) {
				xnx3_result=defaultValue;
			}
		}
		
		return xnx3_result;
	}
	
	/**
	 * 字符串转换为long
	 * @param param  要转换的字符串
	 * @param defaultValue  转换出错异常时返回的值
	 * @return  返回long
	 */
	public static long stringToLong(String param,long defaultValue){
		long xnx3_result=0;
		
		//首先判断字符串不能为空
		if(param==null||param.equalsIgnoreCase("null")){
			xnx3_result = defaultValue;
		}else{
			try {
				xnx3_result=Long.parseLong(param);
			} catch (Exception e) {
				xnx3_result=defaultValue;
			}
		}
		
		return xnx3_result;
	}
	
	/**
	 * 字符串转换为各个进制的整数型
	 * @see Lang#stringToInt(String, int, int)
	 * @deprecated
	 */
	public static int Integer_(String param,int defaultValue,int radix){
		return stringToInt(param, defaultValue, radix);
	}
	
	/**
	 * 字符型转换为Float型
	 * @param param 待转换的字符串
	 * @param defaultValue 异常后的返回值
	 * @return float
	 */
	public static float stringToFloat(String param,float defaultValue){
		float xnx3_result=0.0f;
		
		//首先判断字符串不能为空
		if(param==null||param.equalsIgnoreCase("null")){
			xnx3_result=defaultValue;
		}else{
			try {
				xnx3_result=Float.parseFloat(param);
			} catch (Exception e) {
				xnx3_result=defaultValue;
			}
		}
		
		return xnx3_result;
	}
	
	/**
	 * 字符型转换为Float型
	 * @see Lang#stringToFloat(String, float)
	 * @deprecated
	 */
	public static float Float_(String param,float defaultValue){
		return stringToFloat(param, defaultValue);
	}
	
	/**
	 * 分割一个整数,将其平均分成多少份，返回其中一份的数值。只四舍五入精确到整数
	 * @param value 要分割的目标值
	 * @param grades 要将value分割成几份
	 * @return 每份是多少
	 */
	public static int splitInt(int value,int grades){
		return Math.round(value/grades);
	}
	

    /**
     * 将输入流转为字节数组
     * @param inStream
     * @return byte[]
     * @throws Exception
     */ 
    public static byte[] streamToByte(InputStream inStream)throws Exception{ 
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024]; 
        int len = 0; 
        while( (len = inStream.read(buffer)) !=-1 ){ 
            outSteam.write(buffer, 0, len); 
        } 
        outSteam.close(); 
        inStream.close(); 
        return outSteam.toByteArray(); 
    } 
    
    /**
     * 将输入流转为字符串
     * @param inStream
     * @return String
     * @throws Exception
     */ 
    public static String streamToString(InputStream inStream)throws Exception{ 
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024]; 
        int len = 0; 
        while( (len = inStream.read(buffer)) !=-1 ){ 
            outSteam.write(buffer, 0, len); 
        } 
        outSteam.close(); 
        inStream.close(); 
        return new String(outSteam.toByteArray(),"UTF-8"); 
    } 
    

    /**
     * UTF-8格式汉字转换为%E4%BD%A0形式
     * <br/>以废弃，使用 StringUtil.stringToUrl
     * @param content
     * @return String
     * @deprecated 
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
     * <br/>以废弃，使用  StringUtil
     * @param content
     * @return String
     * @deprecated
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
	 * 弹出提示框，
	 * @param message 要显示的信息
	 * &lt;br/&gt;为换行
	 * @see UI#showMessageDialog(String)
	 * @deprecated
	 */
	public static void showMessageDialog(String message){
		JOptionPane.showMessageDialog(null, message.replaceAll("<br/>", "\n").replaceAll("&amp;lt;br/&amp;gt;", "\n"));
	}
	
	/**
	 * 弹出选择、确认框 
	 * &lt;br/&gt;表示换行
	 * @param message 要显示的信息
	 * @return int {@link Lang#CONFIRM_YES} {@link Lang#CONFIRM_NO} {@link Lang#CONFIRM_CENCEL}
	 * @see UI#showConfirmDialog(String)
	 * @deprecated
	 */
	public static int showConfirmDialog(String message){
		return JOptionPane.showConfirmDialog(null,message.replaceAll("<br/>", "\n").replaceAll("&amp;lt;br/&amp;gt;", "\n"));
	}
	
	/**
	 * 获取当前项目路径，如当前项目名为j2se_util，则会获得其绝对路径 "E:\MyEclipseWork\j2se_util"
	 * @return 项目路径
	 * @deprecated
	 * @see SystemUtil#getCurrentDir()
	 */
	public static String getCurrentDir(){
		return SystemUtil.getCurrentDir();
	}
	
	/**
	 * 获取Java运行时环境规范版本,如： 1.6 、1.7
	 * @return 项目路径
	 */
	public static String getJavaSpecificationVersion(){
		return System.getProperty("user.dir");
	}
	
	/**
	 * 获取当前Java运行所依赖的Jre的路径所在，绝对路径
	 * @return 如：D:\Program Files\MyEclipse2014\binary\com.sun.java.jdk7.win32.x86_64_1.7.0.u45\jre
	 * @deprecated
	 * @see SystemUtil#getJrePath()
	 */
	public static String getCurrentJrePath(){
		return System.getProperty("java.home");
	}
	
    /**
     * 获取当前系统名称
     * @return 当前系统名，例如： windows xp
     */
    public static String getSystemName(){
        Properties sysProperty = System.getProperties();
        //系统名称
        String systemName=sysProperty.getProperty("os.name");
        return systemName;
    }
	
    /**
     * 返回当前时间
     * @return 格式如：2015-01-05 13:12:15
     */
	public static String time() {
		Calendar Cld = Calendar.getInstance();
		int YY = Cld.get(Calendar.YEAR);
		int MM = Cld.get(Calendar.MONTH)+1;
		int DD = Cld.get(Calendar.DATE);
		int HH = Cld.get(Calendar.HOUR_OF_DAY);
		int mm = Cld.get(Calendar.MINUTE);
		int SS = Cld.get(Calendar.SECOND);
		// 微秒 int MI = Cld.get(Calendar.MILLISECOND);

		String xnx3_time = time_triansform(YY) + "-" + time_triansform(MM)
				+ "-" + time_triansform(DD) + " " + time_triansform(HH) + ":"
				+ time_triansform(mm) + ":" + time_triansform(SS);

		return xnx3_time;
	}
	private static String time_triansform(int xnx3_int) {
		String xnx3_temporary;
		if (xnx3_int < 10) {
			xnx3_temporary = "0" + xnx3_int;
		} else {
			xnx3_temporary = "" + xnx3_int;
		}
		return xnx3_temporary;
	}
    
	/**
	 * 从给定的字符串中截取想要的指定字符
	 * <br/><b>注意，请使用 StringUtil.subString  </b>
	 * @param sourceString 源字符串，要切割的字符串
	 * @param startString 匹配的开始点字符
	 * 				<li>若为null或者""表示从头开始匹配
	 * 				<li>若是没找到开始点字符串，默认为从最开始匹配
	 * @param endString 匹配的结束点字符
	 * 				<li>若为null或者""表示匹配到末尾
	 * 				<li>若是没找到结束点字符串，默认为匹配到最末尾
	 * @param matchType 此项是针对结尾的匹配,可传入：
	 * 				<li>1:开始匹配找到的第一个，结束匹配找到的最后一个。
	 * 				<li>2:开始匹配找到的第一个，结束匹配：找到的开始位置往后的第一个。
	 * 				<li>3.开始匹配找到的最后一个，结束匹配找到的最后一个。
	 * 				<li>4:开始匹配找到的最后一个，结束匹配：找到的开始位置往后的第一个。
	 * @return 截取的字符串,若是传入了但是没找到开始或者结束字符则返回null
	 * @deprecated
	 */
	public static String subString(String sourceString,String startString,String endString,int matchType){
		return StringUtil.subString(sourceString, startString, endString, matchType);
	}
	

	/**
	 * 从给定的字符串中截取想要的指定字符。
	 * 		<li>注意：开始跟结束的两个字符串必须是这段字符中是唯一存在的(此为匹配开始字符串第一个到结束字符串最后一个)
	 * <br/><b>注意，请使用 StringUtil.subString  </b>
	 * @param sourceString 源字符串，要切割的字符串 
	 * @param startString 匹配的开始点字符
	 * 				<li>若为null或者""表示从头开始匹配
	 * 				<li>若是没找到开始点字符串，默认为从最开始匹配
	 * @param endString 匹配的结束点字符
	 * 				<li>若为null或者""表示匹配到末尾
	 * 				<li>若是没找到结束点字符串，默认为匹配到最末尾
	 * @see Lang#subString(String, String, String, int)
	 * @return 截取的字符串,若是传入了但是没找到开始或者结束字符则返回null
	 * @deprecated
	 */
	public static String subString(String sourceString,String startString,String endString){
		return StringUtil.subString(sourceString, startString, endString, 1);
	}
	
	/**
	 * float四舍五入，保留指定位的小数返回
	 * @param value float原型
	 * @param num 要保留几位小数
	 * @return float
	 */
	public static float floatRound(float value,int num){
		BigDecimal   b   =   new   BigDecimal(value);  
		float   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();  
		return f1;
	}
	

	
	/**
	 * 传入一个文件名、或者文件的路径，获取其文件的后缀名
	 * @param text 如: xnx3.jar  或者 /Users/apple/xnx3.jar
	 * @return 后缀，如txt、jar......如果没有获得到，则返回null
	 */
	public static String findFileSuffix(String text){
		int point = text.lastIndexOf(".");
		if(point>0){
			String suffix=subString(text, ".", null, 3);
			if(suffix.length()>6){
				return null;
			}else{
				return suffix;
			}
		}else{
			return null;
		}
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
			return stringToInt(result, 0, 16);
		}
	}
	
	
	/**
	 * 传入字节数剧，返回大小的描述信息，小数点后保留两位。
	 * <li>如传入：1234435742，返回：1.15 GB
	 * @param size 传入字节
	 * @return 如:1.15 GB
	 */
	public static String fileSizeToInfo(double size){
		int GB=1073741824; //1024*1024*1024;
		int MB=1048576;	//1024*1024		
		int KB=1024;
		
		if(size>GB){
			return ((double)Math.round((size/GB)*100)/100)+" GB";
		}
		
		if(size>MB){
			return ((double)Math.round((size/MB)*100)/100)+" MB";
		}
		
		if(size>KB){
			return ((double)Math.round((size/KB)*100)/100)+" KB";
		}
		
		return (int)size+"Byte";
	}
	
	/**
	 * 两个同样数据类型的List合并,要求两个list包含的数据类型要相同
	 * @param list 合并后组合起来的结果list
	 * @param appendList 要合并入的list的另一个集合
	 * @return 成功或失败
	 */
	public static boolean listAppend(List list,List appendList){
		if(appendList == null){
			return true;
		}
		
		for (int i = 0; i < appendList.size(); i++) {
			list.add(appendList.get(i));
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		List<String> l = new ArrayList<String>();
		l.add("1");
		
		List<String> l2 = new ArrayList<String>();
		l2.add("2");
		
		for (int i = 0; i <l2.size(); i++) {
			System.out.println(l2.get(i));
		}
	}
	
	/**
	 * 返回 UUID.randomUUID() ，替换掉其中的－
	 * @return uuid
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
