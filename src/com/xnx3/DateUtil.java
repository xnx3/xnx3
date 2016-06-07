package com.xnx3;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		return long13To10(timeForUnix13());
	}
	
	/**
	 * 将String类型时间转换为Date对象
	 * @param time 要转换的时间，如2016-02-18 00:00:11
	 * @param format 要转换的String的时间格式，如：yyyy-MM-dd HH:mm:ss
	 * @return Date对象 若失败，返回null
	 */
	public static Date StringToDate(String time , String format){
		SimpleDateFormat sFormat =  new SimpleDateFormat(format);  
		Date date = null;
		try {
			date = sFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;  
	}
	
	/**
	 * 将String类型时间转换为10位的linux时间戳
	 * @param time 要转换的时间，如2016-02-18 00:00:11
	 * @param format 要转换的String的时间格式，如：yyyy-MM-dd HH:mm:ss
	 * @return 10位Linux时间戳
	 * @throws ParseException 
	 */
	public static int StringToInt(String time , String format){
		long d = StringToDate(time, format).getTime();
		if(d == 0){
			return 0;
		}else{
			return (int)Math.ceil(d/1000);
		}
	}
	
	
	/**
	 * 获取当前传入时间的当天凌晨时间，如 2016-03-19 00:00:00
	 * @param date {@link java.util.Date}
	 * @return {@link java.util.Date}
	 */
	public static Date weeHours(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
	    int minute = cal.get(Calendar.MINUTE);
	    int second = cal.get(Calendar.SECOND);
	    //时分秒（毫秒数）
	    long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
	    //凌晨00:00:00
	    cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);

	    return cal.getTime();
	}
	
	/**
	 * 获取当前传入时间的当天午夜时间，如 2016-03-19 23:59:59
	 * @param {@link java.util.Date}
	 * @return {@link java.util.Date}
	 */
	public static Date midnight(Date date){
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
		cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
		return cal.getTime();
	}
	
	/**
	 * {@link Date}转为String类型，变成当前显示的文字时间
	 * @param date {@link java.util.Date}
	 * @param format 生成的格式化时间，如 yyyy-MM-dd HH:mm:ss
	 * @return 文字时间
	 */
	public static String dateToString(Date date,String format){
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return format2.format(date);
	}
	
	/**
	 * 将 {@link Date} 转化为 10位的时间戳
	 * @param date {@link Date}
	 * @return 10位的时间戳
	 */
	public static int dateToInt10(Date date){
		return long13To10(date.getTime());
	}
	
	/**
	 * 将13位Linux时间戳转换为10位时间戳
	 * @param time 13位Linux时间戳
	 * @return 10位Linux时间戳
	 */
	public static int long13To10(long time){
		return Lang.stringToInt((time+"").substring(0, 10), 0);
	}
	
}
