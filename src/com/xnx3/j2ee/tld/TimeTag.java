package com.xnx3.j2ee.tld;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;

/**
 * 时间转换标签，将linux转换为文字描述的时间
 * @author 管雷鸣
 *
 */
public class TimeTag extends TagSupport {

	private static String FORMAT_DEFAULT="yyyy-MM-dd hh:mm:ss";	//如果format没有传递过来，会使用这个默认的时间戳
	private long linuxTime;	//Linux时间戳，10位或者13位
	private String format;	//转换格式 ,若不填，默认为yyyy-MM-dd hh:mm:ss
	
	public String getFormat() {
		return format;
	}

	/**
	 * yyyy-MM-dd hh:mm:ss
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	public long getLinuxTime() {
		return linuxTime;
	}

	public void setLinuxTime(long linuxTime) {
		this.linuxTime = linuxTime;
	}

	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		String date="";
		DateUtil dateUtil = new DateUtil();
		try {
			date = dateUtil.dateFormat(getLinuxTime(), getFormat());
		} catch (NotReturnValueException e1) {
			date = e1.getMessage();
		}
		
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(date);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return super.doEndTag();
	}
	
}
