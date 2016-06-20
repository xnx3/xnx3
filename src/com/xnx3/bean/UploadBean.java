package com.xnx3.bean;

import com.xnx3.net.FastDFSUtil;

/**
 * FastDFS文件上传的返回对象
 * @author 管雷鸣
 * @see FastDFSUtil#upload(String)
 */
public class UploadBean extends org.csource_.fastdfs.FileInfo {
	private boolean isSuccess;	//成功：true
	private String groupName;
	private String remoteFileName;
	private String errorInfo;	//若是失败，调用错误信息
	private String consumeTime;	//执行耗时
	
	
	public UploadBean(long file_size, int create_timestamp, int crc32,
			String source_ip_addr) {
		super(file_size, create_timestamp, crc32, source_ip_addr);
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

	@Override
	public String toString() {
		return "UploadBean [isSuccess=" + isSuccess + ", groupName="
				+ groupName + ", remoteFileName=" + remoteFileName
				+ ", errorInfo=" + errorInfo + ", source_ip_addr="
				+ source_ip_addr + ", file_size=" + file_size
				+ ", create_timestamp=" + create_timestamp + ", crc32=" + crc32
				+ ", getGroupName()=" + getGroupName()
				+ ", getRemoteFileName()=" + getRemoteFileName()
				+ ", isSuccess()=" + isSuccess() + ", getErrorInfo()="
				+ getErrorInfo() + ", getSourceIpAddr()=" + getSourceIpAddr()
				+ ", getFileSize()=" + getFileSize()
				+ ", getCreateTimestamp()=" + getCreateTimestamp()
				+ ", getCrc32()=" + getCrc32() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}
	
	

}
