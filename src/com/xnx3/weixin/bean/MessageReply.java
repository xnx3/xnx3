package com.xnx3.weixin.bean;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.xnx3.DateUtil;

/**
 * 被动回复用户消息
 * <br/>使用如：
 * <pre>
 * 		
 * </pre>
 * @author 管雷鸣
 */
public class MessageReply {
	private String toUserName;
	private String fromUserName;
	
	/**
	 * 初始化新建一个被动回复消息
	 * @param toUserName 接收方帐号（收到的OpenID），是向哪个用户发送信息,也就是接收到微信消息时 {@link MessageReceive#getFromUserName()}
	 * @param fromUserName 开发者微信号，发送者微信号，也就是接收到微信消息时 {@link MessageReceive#getToUserName()}
	 */
	public MessageReply(String toUserName, String fromUserName) {
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
	}

	/**
	 * 向微信服务器进行文字回复（MsgType=text）的XML格式字符串，可以将此直接回复微信服务器即可达到回复效果。
	 * <br/>此项即向微信服务器自动回复xml
	 * @param response {@link HttpServletResponse}响应，输出返回值给微信服务器。
	 * @param content 微信自动回复用户的内容
	 */
	public void replyText(HttpServletResponse response, String content){
		String text = "<xml>"
				+ "<ToUserName><![CDATA["+this.toUserName+"]]></ToUserName>"
				+ "<FromUserName><![CDATA["+this.fromUserName+"]]></FromUserName>"
				+ "<CreateTime>"+DateUtil.timeForUnix10()+"</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA["+content+"]]></Content>"
				+ "</xml>";
		reply(response, text);
	}
	
	public void reply(HttpServletResponse response, String text){
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {
	    	out = response.getWriter();  
	    	out.append(text);
	    } catch (IOException e) {  
	    	e.printStackTrace();  
	    } finally {
	    	if (out != null) {  
	    		out.close();  
	    	}  
	    }
	}
}
