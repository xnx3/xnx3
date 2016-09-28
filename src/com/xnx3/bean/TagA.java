package com.xnx3.bean;

/**
 * html的A标签
 * @author 管雷鸣
 *
 */
public class TagA {
	private String href = "";	//链接地址
	private int pageNumber;	//页码
	private String title = "";	//显示的标题文字
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	@Override
	public String toString() {
		return "TagA [href=" + href + ", pageNumber=" + pageNumber + ", title="
				+ title + "]";
	}
}
