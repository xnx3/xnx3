package com.xnx3.net.ossbean;

/**
 * aliyun OSS 上传成功后，返回值
 * @author 管雷鸣
 */
public class PutResult {
	private String fileName;	//上传成功后的文件名，如 "xnx3.jar"
	private String path;		//上传成功后的路径，如 "/jar/file/xnx3.jar"
	private String url;		//文件上传成功后，外网访问的url
	
	public PutResult() {
	}
	
	/**
	 * 这是OSS上传成功后的返回值
	 * @param fileName 上传成功后的文件名，如 "xnx3.jar"
	 * @param path 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 * @param url 文件上传成功后，外网访问的url
	 */
	public PutResult(String fileName,String path,String url) {
		this.fileName = fileName;
		this.path = path;
		this.url = url;
	}
	
	/**
	 * 上传成功后的文件名，如 "xnx3.jar"
	 * @return 上传成功后的文件名，如 "xnx3.jar"
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 上传成功后的文件名，如 "xnx3.jar"
	 * @param fileName 上传成功后的文件名，如 "xnx3.jar"
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 * @return
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 * @param path 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 文件上传成功后，外网访问的url 
	 * @return url 文件上传成功后，外网访问的url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 文件上传成功后，外网访问的url
	 * @param url 文件上传成功后，外网访问的url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "PutResult [fileName=" + fileName + ", path=" + path + ", url="
				+ url + "]";
	}
	
	
}
