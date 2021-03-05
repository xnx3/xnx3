package com.xnx3.bean;
/**
 * Shell相关
 * @author 管雷鸣
 *
 */
public class ShellBean {
	
	private int exitStatus;
	private String outString;	//响应成功返回输出的字符串
	private String errorString;	//响应出错的信息
	
	public ShellBean() {
		this.exitStatus=-1;
	}
	
	
	public String getErrorString() {
		return errorString;
	}


	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}


	/**
	 * 返回 －1 ，则是失败，若是返回>-1，则是成功
	 * @return 返回 －1 ，则是失败，若是返回>-1，则是成功
	 */
	public int getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(int exitStatus) {
		this.exitStatus = exitStatus;
	}
	public String getOutString() {
		return outString;
	}
	public void setOutString(String outString) {
		this.outString = outString;
	}
	
	
}
