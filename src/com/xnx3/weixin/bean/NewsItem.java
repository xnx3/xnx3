package com.xnx3.weixin.bean;

/**
 * 微信自动回复图文消息列表，每一条图文，便是一个 NewsItem
 * @author 管雷鸣
 *
 */
public class NewsItem {
	private String title;
	private String description;
	private String picUrl;
	private String url;
	
	public String getTitle() {
		return title;
	}
	/**
	 * @param title 图文消息标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	/**
	 * 
	 * @param description 图文消息描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	/**
	 * @param picUrl 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url 点击图文消息跳转链接
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 获取 xml格式的数据。如 <item>....</item>
	 * @return
	 */
	public String getXMLString(){
		return "<item>"
				+ "<Title><![CDATA["+this.title+"]]></Title>"
				+ "<Description><![CDATA["+this.description+"]]></Description>"
				+ "<PicUrl><![CDATA["+this.picUrl+"]]></PicUrl>"
				+ "<Url><![CDATA["+this.url+"]]></Url>"
				+ "</item>";
	}
	
}
