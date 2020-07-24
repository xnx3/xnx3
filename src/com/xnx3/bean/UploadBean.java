package com.xnx3.bean;

import java.util.Date;
import com.xnx3.net.FastDFSUtil;

/**
 * FastDFS文件上传的返回对象
 * 原本是继承 extends org.csource_.fastdfs.FileInfo  ，但是类扫描时失败，所以去掉继承了
 * @author 管雷鸣
 * @see FastDFSUtil#upload(String)
 */
public class UploadBean{
	private boolean isSuccess;	//成功：true
	private String groupName;
	private String remoteFileName;
	private String errorInfo;	//若是失败，调用错误信息
	private String consumeTime;	//执行耗时
	
	private long fileSize;
	private Date createTimestamp;
	private int crc32;
	private String sourceIpAddr;
	
	public UploadBean(long file_size, int create_timestamp, int crc32,
			String source_ip_addr) {
//		super(file_size, create_timestamp, crc32, source_ip_addr);
		this.fileSize = file_size;
		this.createTimestamp = new Date();	//要把int转为data类型，待转换
		this.crc32 = crc32;
		this.sourceIpAddr = source_ip_addr;
		this.isSuccess=true;
	}
	
	public UploadBean() {
		this.isSuccess=false;
	}
	
	//获取组名
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * 获取远程文件路径
	 * @return 路径 如：
	 */
	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}
	
	/**
	 * 执行结果是否成功
	 * @return 
	 * 		<ul>
	 * 			<li>true:成功
	 * 			<li>false:失败
	 * 		</ul>
	 * @see UploadBean#getErrorInfo()
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	/**
	 * 若是失败，调用错误信息
	 * @return 错误提示信息
	 * @see #isSuccess()
	 */
	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	
	/**
	 * 执行耗时总长
	 * @return String文字说明
	 */
	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}
	
	
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}
	
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public int getCrc32() {
		return crc32;
	}

	public void setCrc32(int crc32) {
		this.crc32 = crc32;
	}

	public String getSourceIpAddr() {
		return sourceIpAddr;
	}

	public void setSourceIpAddr(String sourceIpAddr) {
		this.sourceIpAddr = sourceIpAddr;
	}

	@Override
	public String toString() {
		return "UploadBean [isSuccess=" + isSuccess + ", groupName=" + groupName + ", remoteFileName=" + remoteFileName
				+ ", errorInfo=" + errorInfo + ", consumeTime=" + consumeTime + ", fileSize=" + fileSize
				+ ", createTimestamp=" + createTimestamp + ", crc32=" + crc32 + ", sourceIpAddr=" + sourceIpAddr + "]";
	}

}
