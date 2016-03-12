package com.xnx3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xnx3.exception.NotReturnValueException;

/**
 * 日期工具类
 * @author 管雷鸣
 *
 */
public class DateUtil {
	/**
	 * 如果format没有传递过来，会使用这个默认的时间戳
	 */
	public final static String FORMAT_DEFAULT="yyyy-MM-dd hh:mm:ss";
	
	/**
	 * 返回当前13位的Unix时间戳
	 * @see Date
	 * @return 13位Unix时间戳
	 */
	public static long timeForUnix13(){
		Date date = new Date();
		long time = date.getTime();
		
		return time;
	}
	
	/**
	 * 将Linux时间戳变为文字描述的时间
	 * @param linuxTime Linux时间戳，10位或者13位
	 * @param format 转换格式 ,若不填，默认为yyyy-MM-dd hh:mm:ss {@link #FORMAT_DEFAULT}
	 * @return 转换后的日期。如 2016-01-18 11:11:11
	 * @throws NotReturnValueException
	 */
	public String dateFormat(long linuxTime,String format) throws NotReturnValueException{
		int linuxTimeLength=(linuxTime+"").length();
		if(linuxTime==0||!(linuxTimeLength==10||linuxTimeLength==13)){
			throw new NotReturnValueException("传入的linux时间戳长度错误！当前传入的时间戳："+linuxTime+",请传入10或者13位的时间戳");
		}else{
			if(format==null||format.length()==0){
				format=FORMAT_DEFAULT;
			}
			
			if(linuxTimeLength==10){
				linuxTime=linuxTime*1000;
			}
			return new SimpleDateFormat(format).format(new java.util.Date(linuxTime));
		}
	}
	
	/**
	 * 将Linux时间戳变为文字描述的时间
	 * {@link #dateFormat(long, String)}
	 * @param linuxTime Linux时间戳，10位或者13位
	 * @return 转换后的日期。如 2016-01-18 11:11:11
	 * @throws NotReturnValueException
	 */
	public String dateFormat(long linuxTime) throws NotReturnValueException{
		return dateFormat(linuxTime, FORMAT_DEFAULT);
	}
	
	/**
	 * 返回当前10位数的Unix时间戳
	 * @return Unix时间戳，失败返回0
	 */
	public static int timeForUnix10(){
		return Lang.stringToInt((timeForUnix13()+"").substring(0, 10), 0);
	}
	
	/**
	 * 将String类型时间转换为Date对象
	 * @param time 要转换的时间，如2016-02-18 00:00:11
	 * @param format 要转换的String的时间格式，如：yyyy-MM-dd HH:mm:ss
	 * @return Date对象
	 * @throws ParseException 
	 */
	public Date StringToDate(String time , String format) throws ParseException{
		SimpleDateFormat sFormat =  new SimpleDateFormat(format);  
		Date date = sFormat.parse(time);
		return date;  
	}
	
	/**
	 * 将String类型时间转换为10位的linux时间戳
	 * @param time 要转换的时间，如2016-02-18 00:00:11
	 * @param format 要转换的String的时间格式，如：yyyy-MM-dd HH:mm:ss
	 * @return 10位Linux时间戳
	 * @throws ParseException 
	 */
	public int StringToInt(String time , String format) throws ParseException{
		long d = StringToDate(time, format).getTime();
		return (int)Math.ceil(d/1000);
	}
	
	
}
