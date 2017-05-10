package com.xnx3.net;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;
import com.xnx3.media.ImageUtil;
import com.xnx3.net.ossbean.PutResult;

/**
 * aliyun OSS
 * <pre>
 * 		//加入配置文件 src/xnx3Config.xml  ，配置其aliyunOSS节点的参数。
 * </pre>
 * <br><b>需导入</b> 
 * <br/>aliyun-sdk-oss-2.2.3.jar
 * <br/>commons-codec-1.9.jar
 * <br/>commons-logging-1.2.jar
 * <br/>hamcrest-core-1.1.jar
 * <br/>httpclient-4.4.1.jar
 * <br/>httpcore-4.4.1.jar
 * <br/>jdom-1.1.jar
 * @author 管雷鸣
 */
public class OSSUtil {
	public static String endpoint = "";
	public static String accessKeyId = "";
	public static String accessKeySecret = "";
	public static String bucketName = "";
	public static String roleArn = "";
	
    /**
     * 处理过的OSS外网域名,如 http://xnx3.oss-cn-qingdao.aliyuncs.com/
     * <br/>(文件上传成功时会加上此域名拼接出文件的访问完整URL。位于Bucket概览－OSS域名)
     */
    public static String url = ""; 
    /**
     * STS使用
     * <br/>目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
     */
    public static String region_cn_hangzhou = "cn-hangzhou";
    /**
     * 当前 STS API 版本
     */
    public static final String sta_api_version = "2015-04-01";
	
    private static OSSClient ossClient;
    
	static{
		endpoint = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.endpoint");
		accessKeyId = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.accessKeyId");
		accessKeySecret = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.accessKeySecret");
		bucketName = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.bucketName");
		roleArn = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.roleArn");
		
		url = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.url");
		if(url == null || url.length() == 0){
			url = bucketName+"."+endpoint;
		}
		//判断url前面是否加了http://
		if(url.indexOf("://") == -1){
			url = "http://"+url;
		}
		//末尾加/
		url = url+"/";
	}
	
	/**
	 * 获取 OSSClient 对象
	 * @return {@link OSSClient}
	 */
	public static OSSClient getOSSClient(){
		if(ossClient == null){
			System.out.println("create OSSCLient");
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
	 * @return {@link PutResult} 若失败，返回null
	 */
	public static PutResult put(String filePath,String fileName,InputStream inputStream){
		String fileSuffix=com.xnx3.Lang.subString(fileName, ".", null, 3);	//获得文件后缀，以便重命名
        String name=Lang.uuid()+"."+fileSuffix;
        String path = filePath+name;
        PutObjectResult pr = getOSSClient().putObject(bucketName, path, inputStream);
		return new PutResult(name, path,url+path);
	}
	
	/**
	 * 删除文件
	 * @param filePath 文件所在OSS的绝对路径，如 "jar/file/xnx3.jpg"
	 */
	public static void deleteObject(String filePath){
		getOSSClient().deleteObject(bucketName, filePath);
	}
	
	/**
	 * 上传文件。上传后的文件名固定
	 * @param path 上传到哪里，包含上传后的文件名，如"image/head/123.jpg"
	 * @param inputStream 文件
	 * @return {@link PutResult}
	 */
	public static PutResult put(String path,InputStream inputStream){
		PutObjectResult pr = getOSSClient().putObject(bucketName, path, inputStream);
		String name = Lang.subString(path, "/", null, 3);
		return new PutResult(name, path,url+path);
	}
	
	/**
	 * 上传本地文件
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param localPath 本地要上传的文件的绝对路径，如 "/jar_file/iw.jar"
	 * @return {@link PutResult} 若失败，返回null
	 */
	public static PutResult put(String filePath, String localPath){
		File file = new File(localPath);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return put(filePath, localPath, input);
	}
	
	/**
	 * 上传图片，将网上的图片复制到OSS里 如果获取不到后缀，默认用 jpg
	 * @param filePath 上传图片的OSS地址，如 image/124  后面会自动拼接上图片的后缀名，上传成功后为image/124.png
	 * @param imageUrl 网上图片的地址
	 * @return {@link PutResult}
	 */
	public static PutResult putImageByUrl(String filePath, String imageUrl){
		if(imageUrl == null){
			return null;
		}
		String suffix = Lang.findFileSuffix(imageUrl);	//取图片后缀名
		BufferedImage bufferedImage = ImageUtil.getBufferedImageByUrl(imageUrl);
		if(suffix == null){
			suffix = "jpg";
		}
		
    	return OSSUtil.put(filePath+"."+suffix, ImageUtil.bufferedImageToInputStream(bufferedImage, suffix));
	}
	
	/**
	 * STS 授权给第三方上传，获得临时访问凭证
	 * @param roleSessionName 临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
	 * 							<br/>注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
	 * 							<br/>具体规则请参考API文档中的格式要求
	 * 							<br/>如：alice-001
	 * @param policy RAM和STS授权策略，详细参考 <a href="https://help.aliyun.com/document_detail/31867.html">https://help.aliyun.com/document_detail/31867.html</a>
	 * 			<pre>
	 * 				{
	 * 					"Version": "1",
	 * 					"Statement": [
	 * 						{
	 * 							"Action": [
	 * 								"oss:PutObject", 
	 * 								"oss:GetObject"
	 * 							], 
	 * 							"Resource": [
	 * 								"acs:oss:*:*:*"
	 * 							], 
	 * 							"Effect": "Allow",
	 * 							"Condition": {
	 * 								"IpAddress": {
	 * 									"acs:SourceIp": "192.168.0.*"	//指定ip网段,支持*通配
	 * 								}
	 * 							}
	 * 						}
	 * 					]
	 * 				}
	 * 			</pre>
	 * @return 成功，返回 {@link Credentials} ，失败返回null
	 */
	public static Credentials createSTS(String roleSessionName,String policy){
		String accessKeyId = OSSUtil.accessKeyId;
	    String accessKeySecret = OSSUtil.accessKeySecret;
	    // AssumeRole API 请求参数: RoleArn, RoleSessionName, Policy, and DurationSeconds
	    // RoleArn 需要在 RAM 控制台上获取
//	    String roleArn = "acs:ram::1080155601964967:role/aliyunosstokengeneratorrole";
	    // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
	    // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
	    // 具体规则请参考API文档中的格式要求
//		String roleSessionName = "alice-001";
	    // 如何定制你的policy?
//		    String policy = "{\n" +
//		            "    \"Version\": \"1\", \n" +
//		            "    \"Statement\": [\n" +
//		            "        {\n" +
//		            "            \"Action\": [\n" +
//		            "                \"oss:GetBucket\", \n" +
//		            "                \"oss:GetObject\" \n" +
//		            "            ], \n" +
//		            "            \"Resource\": [\n" +
//		            "                \"acs:oss:*:*:*\"\n" +
//		            "            ], \n" +
//		            "            \"Effect\": \"Allow\"\n" +
//		            "        }\n" +
//		            "    ]\n" +
//		            "}";
	    // 此处必须为 HTTPS
	    ProtocolType protocolType = ProtocolType.HTTPS;
	    try {
	    	AssumeRoleResponse response = assumeRole(accessKeyId, accessKeySecret,roleArn, roleSessionName, policy, protocolType);
	    	Credentials credentials = response.getCredentials();
	    	return credentials;
	    } catch (ClientException e) {
	    	e.printStackTrace();
	    	System.out.println("Failed to get a token.");
	    	System.out.println("Error code: " + e.getErrCode());
	    	System.out.println("Error message: " + e.getErrMsg());
	    }
	    return null;
	}
	static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,String roleArn, String roleSessionName, String policy,ProtocolType protocolType) throws ClientException {
		try {
			// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
			IClientProfile profile = DefaultProfile.getProfile(region_cn_hangzhou, accessKeyId, accessKeySecret);
			DefaultAcsClient client = new DefaultAcsClient(profile);
			// 创建一个 AssumeRoleRequest 并设置请求参数
			final AssumeRoleRequest request = new AssumeRoleRequest();
			request.setVersion(sta_api_version);
			request.setMethod(MethodType.POST);
			request.setProtocol(protocolType);
			request.setRoleArn(roleArn);
			request.setRoleSessionName(roleSessionName);
			request.setPolicy(policy);
			// 发起请求，并得到response
			final AssumeRoleResponse response = client.getAcsResponse(request);
			return response;
		} catch (ClientException e) {
			throw e;
		}
	}
	
	/**
	 * 以字符串创建文件
	 * @param path 上传后的文件所在OSS的目录＋文件名，如 "jar/file/xnx3.html"
	 * @param text 文件内容
	 * @param encode 文件编码，如：UTF-8 
	 * @return {@link PutResult} 若失败，返回null
	 */
	public static PutResult putStringFile(String path, String text, String encode){
		try {
			return put(path, new ByteArrayInputStream(text.getBytes(encode)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 以字符串创建文件，创建的文件编码为UTF-8
	 * @param path 上传后的文件所在OSS的目录＋文件名，如 "jar/file/xnx3.html"
	 * @param text 文件内容
	 * @param encode 文件编码，如：UTF-8 
	 * @return {@link PutResult} 若失败，返回null
	 */
	public static PutResult putStringFile(String path, String text){
		return putStringFile(path, text, "UTF-8");
	}
	
	/**
	 * 查看某个路径下的文件所占用的资源的大小
	 * @param filePath 要查看文件的路径，如 file/image/
	 * @return 单位：B
	 */
	public static long getFolderSize(String filePath){
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		listObjectsRequest.setPrefix(filePath); 
		listObjectsRequest.setMaxKeys(1000);
		
		boolean have = true;		//是否有下一页
		String nextMarker = null;
		int size = 0;		//总字节大小，单位：B
		while(have){
			if(nextMarker != null){
				listObjectsRequest.setMarker(nextMarker);
			}
			ObjectListing listO = OSSUtil.getOSSClient().listObjects(listObjectsRequest);
			
		    for (OSSObjectSummary objectSummary : listO.getObjectSummaries()) {
		        size += objectSummary.getSize();  
		    }
		    
		    have = listO.isTruncated();
		    nextMarker = listO.getNextMarker();
		}
		return size;
	}
	
	/**
	 * 获取 指定目录下的所有文件对象
	 * @param filePath 要查看文件的路径，如 file/image/
	 * @return {@link List}
	 */
	public static List<OSSObjectSummary> getFolderObjectList(String filePath){
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		listObjectsRequest.setPrefix(filePath); 
		listObjectsRequest.setMaxKeys(1000);
		List<OSSObjectSummary> list = new ArrayList<OSSObjectSummary>();
		
		boolean have = true;		//是否有下一页
		String nextMarker = null;
		while(have){
			if(nextMarker != null){
				listObjectsRequest.setMarker(nextMarker);
			}
			ObjectListing listO = OSSUtil.getOSSClient().listObjects(listObjectsRequest);
			
		    for (OSSObjectSummary objectSummary : listO.getObjectSummaries()) {
		    	list.add(objectSummary);
		    }
		    
		    have = listO.isTruncated();
		    nextMarker = listO.getNextMarker();
		}
		return list;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		putStringFile("test.txt", "test");
	}
}
