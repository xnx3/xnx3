package com.xnx3.bean;

import com.jcraft.jsch.SftpATTRS;

/**
 * 文件相关
 * @author 管雷鸣
 *
 */
public class FileBean {
	private boolean Dir; //是否是目录
	private String fileName; // 文件、目录名字
	private String filePath;	//文件所在路径，如: /root
	private SftpATTRS attrs;	//属性
	
	
	/**
	 * 属性
	 * @return {@link SftpATTRS}
	 */
	public SftpATTRS getAttrs() {
		return attrs;
	}

	public void setAttrs(SftpATTRS attrs) {
		this.attrs = attrs;
	}

	/**
	 * 文件所在路径，如: /root
	 * @return 文件路径
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * 文件所在路径，如: /root
	 * @param filePath 当前所在路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public boolean isDir() {
		return Dir;
	}
	public void setDir(boolean dir) {
		Dir = dir;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "FileBean [Dir=" + Dir + ", fileName=" + fileName + "]";
	}
	
	
}
