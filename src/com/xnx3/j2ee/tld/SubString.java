package com.xnx3.j2ee.tld;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 字符串截取
 * @author 管雷鸣
 */
public class SubString extends TagSupport {
	private final static String DEFAULT_MORE="...";	//如果末尾更多字符没有传递过来，会使用这个
	private String text;	//原本的字符串
	private int maxLength;	//显示的最大长度
	private String more;	//若超过最大长度，裁减的字符串会加上此输出。若不填默认使用 ...
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}

	@Override
	public int doEndTag() throws JspException {
		String content = "";
		
		if(more == null){
			more = DEFAULT_MORE;
		}
		
		if(text != null){
			if(text.length()>maxLength){
				content = text.substring(0, maxLength)+more;
			}
		}

		JspWriter writer = pageContext.getOut();
		try {
			writer.print(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	
}
