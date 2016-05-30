package com.xnx3.net.ossbean;

/**
 * aliyun OSS 上传成功后，返回值
 * @author 管雷鸣
 */
public class PutResult {
	private String fileName;	//上传成功后的文件名，如 "xnx3.jar"
	private String path;		//上传成功后的路径，如 "/jar/file/xnx3.jar"
	
	public PutResult() {
	}
	
	/**
	 * 这是OSS上传成功后的返回值
	 * @param fileName 上传成功后的文件名，如 "xnx3.jar"
	 * @param path 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 */
	public PutResult(String fileName,String path) {
		this.fileName = fileName;
		this.path = path;
	}
	
	/**
	 * 上传成功后的文件名，如 "xnx3.jar"
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 上传成功后的文件名，如 "xnx3.jar"
	 * @param fileName
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
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "PutResult [fileName=" + fileName + ", path=" + path + "]";
	}
	
}
