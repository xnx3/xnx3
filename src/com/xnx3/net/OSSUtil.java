package com.xnx3.net;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.aliyun.oss.OSSClient;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;
import com.xnx3.net.ossbean.PutResult;

/**
 * aliyun OSS
 * <br><b>需导入</b> 
 * <br/><i>aliyun-sdk-oss-2.2.3.jar</i>
 * <br/><i>commons-codec-1.9.jar</i>
 * <br/><i>commons-logging-1.2.jar</i>
 * <br/><i>hamcrest-core-1.1.jar</i>
 * <br/><i>httpclient-4.4.1.jar</i>
 * <br/><i>httpcore-4.4.1.jar</i>
 * <br/><i>jdom-1.1.jar</i>
 * @author 管雷鸣
 */
public class OSSUtil {
	private static String endpoint = "";
    private static String accessKeyId = "";
    private static String accessKeySecret = "";
    private static String bucketName = "";
	
    private static OSSClient ossClient;
    
	static{
		endpoint = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.endpoint");
		accessKeyId = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.accessKeyId");
		accessKeySecret = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.accessKeySecret");
		bucketName = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.bucketName");
	}
	
	/**
	 * 获取 OSSClient 对象
	 * @return {@link OSSClient}
	 */
	public static OSSClient getOSSClient(){
		if(ossClient == null){
			ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		}
		return ossClient;
	}
	
	/**
	 * 创建文件夹
	 * @param folderName 要创建的文件夹名字，如要创建xnx3文件夹，则传入"xnx3/"。也可以传入"x/n/" 代表建立x文件夹同时其下再建立n文件夹
	 */
	public static void createFolder(String folderName){
		//既然是目录，那就是以/结束，判断此是否是以／结束的，若不是，末尾自动加上
		if(folderName.lastIndexOf("/")<(folderName.length()-1)){
			folderName+="/";
		}
		
		getOSSClient().putObject(bucketName, folderName, new ByteArrayInputStream(new byte[0]));
	}
	
	/**
	 * 上传文件
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param fileName 上传的文件名，如“xnx3.jar”；主要拿里面的后缀名。也可以直接传入文件的后缀名如“.jar”
	 * @param inputStream {@link InputStream}
	 * @return {@link PutResult}
	 */
	public static PutResult put(String filePath,String fileName,InputStream inputStream){
		String fileSuffix=com.xnx3.Lang.subString(fileName, ".", null, 3);	//获得文件后缀，以便重命名
        String name=Lang.uuid()+"."+fileSuffix;
        String path = filePath+name;
		getOSSClient().putObject(bucketName, path, inputStream);
		
		return new PutResult(name, path);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("/jar_file/iw.jar");
		InputStream input = new FileInputStream(file);
		PutResult p = put("jar/file/", ".jar", input);
		System.out.println(p);
	}
}
