package com.xnx3.net;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import org.csource_.common.MyException;
import org.csource_.common.NameValuePair;
import org.csource_.fastdfs.ClientGlobal;
import org.csource_.fastdfs.FileInfo;
import org.csource_.fastdfs.ServerInfo;
import org.csource_.fastdfs.StorageClient;
import org.csource_.fastdfs.StorageServer;
import org.csource_.fastdfs.TrackerClient;
import org.csource_.fastdfs.TrackerGroup;
import org.csource_.fastdfs.TrackerServer;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;
import com.xnx3.bean.UploadBean;

/**
 * FastDFS快速使用
 * <pre>
		//加入配置文件 src/xnx3Config.xml  ，配置其FastDFS节点的参数。
 		
 		FastDFSUtil client=new FastDFSUtil();
		client.open();
		//上传
		UploadBean upload = client.upload("/jar_file/mysql-connector-java-3.0.17-ga-bin.jar");
		//下载
		boolean result=client.download("M00/07/65/CgAA-lVRp9-AYn-mAAPCLZlIvsE337.txt", "/Users/apple/Desktop/t/1.jar");
		client.close();
 * </pre>
 * <br/>需
 * <br/>fastdfs.jar
 * <br/>commons-configuration-1.7.jar
 * <br/>commons-collections-3.2.1.jar
 * <br/>commons-io-1.3.2.jar
 * <br/>commons-lang-2.5.jar
 * <br/>commons-logging-1.2.jar
 * @author 管雷鸣
 */
public class FastDFSUtil{
	TrackerClient tracker = null;
	TrackerServer trackerServer = null;
	StorageClient client =null;
	StorageServer storageServer = null;
	ServerInfo[] servers = null;
	NameValuePair[] meta_list = null;
	
	static{
		ClientGlobal.g_connect_timeout = Lang.stringToInt(ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.connect_timeout"), ClientGlobal.DEFAULT_CONNECT_TIMEOUT);
  		if (ClientGlobal.g_connect_timeout < 0){
  			ClientGlobal.g_connect_timeout = ClientGlobal.DEFAULT_CONNECT_TIMEOUT;
  		}
  		ClientGlobal.g_connect_timeout *= 1000; //millisecond
  		
  		ClientGlobal.g_network_timeout = Lang.stringToInt(ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.network_timeout"), ClientGlobal.DEFAULT_NETWORK_TIMEOUT);
  		if (ClientGlobal.g_network_timeout < 0){
  			ClientGlobal.g_network_timeout = ClientGlobal.DEFAULT_NETWORK_TIMEOUT;
  		}
  		ClientGlobal.g_network_timeout *= 1000; //millisecond

  		ClientGlobal.g_charset = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.charset");
  		if (ClientGlobal.g_charset == null || ClientGlobal.g_charset.length() == 0){
  			ClientGlobal.g_charset = "ISO8859-1";
  		}
  		
  		String d = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.debug");
  		if(d != null){
  			ClientGlobal.debug = d.equals("true");
  		}else{
  			ClientGlobal.debug = false;
  		}
  		ClientGlobal.groupName = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.default_group_name");
  		if (ClientGlobal.groupName == null || ClientGlobal.groupName.length() == 0){
  			ClientGlobal.g_charset = "group1";
  		}
  		ClientGlobal.defaultSuffix = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.default_fileSuffix");
  		if (ClientGlobal.defaultSuffix == null || ClientGlobal.defaultSuffix.length() == 0){
  			ClientGlobal.defaultSuffix = "xnx3";
  		}
  		
  		List<String> list= ConfigManagerUtil.getSingleton("xnx3Config.xml").getList("FastDFS.tracker_servers.tracker_server");
  		String[] szTrackerServers = new String[list.size()];
		String[] parts = new String[list.size()];
  		for (int i = 0; i < list.size(); i++) {
  			szTrackerServers[i]=list.get(i);
		}
  		
  		InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
  		for (int i=0; i<szTrackerServers.length; i++){
  			parts = szTrackerServers[i].split("\\:", 2);
  			if (parts.length != 2){
  				try {
					throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
				} catch (MyException e) {
					e.printStackTrace();
				}
  			}
  			
  			tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
  		}
  		ClientGlobal.g_tracker_group = new TrackerGroup(tracker_servers);
  		
  		ClientGlobal.g_tracker_http_port = Lang.stringToInt(ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.tracker_http_port"), 80);
  		String ast = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.anti_steal_token");
  		if(ast != null){
  			ClientGlobal.g_anti_steal_token = ast.equals("true");
  		}else{
  			ClientGlobal.g_anti_steal_token = false;
  		}
  		if (ClientGlobal.g_anti_steal_token){
  			ClientGlobal.g_secret_key = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("FastDFS.secret_key");
  		}
	}

	/**
	 * 打开服务
	 */
	public void open(){
		String group_name;
		tracker = new TrackerClient();
		try {
			trackerServer = tracker.getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		client= new StorageClient(trackerServer, storageServer);
		
		meta_list = new NameValuePair[4];
		meta_list[0] = new NameValuePair("width", "800");
		meta_list[1] = new NameValuePair("heigth", "600");
		meta_list[2] = new NameValuePair("bgcolor", "#FFFFFF");
		meta_list[3] = new NameValuePair("author", "Mike");
		
		group_name = null;
		StorageServer[] storageServers = null;
		try {
			storageServers = tracker
					.getStoreStorages(trackerServer, group_name);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (storageServers == null) {
			debug("get store storage servers fail, error code: " + tracker.getErrorCode());
		} else {
			debug("store storage servers count: " + storageServers.length);
			for (int k = 0; k < storageServers.length; k++) {
				debug((k + 1)
						+ ". "
						+ storageServers[k].getInetSocketAddress().getAddress()
								.getHostAddress() + ":"
						+ storageServers[k].getInetSocketAddress().getPort());
			}
		}
	}
	
	/**
	 * 停止，关闭Socket
	 */
	public void close(){
		try {
			if(storageServer!=null){
				storageServer.close();
			}
			if(trackerServer!=null){
				trackerServer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 文件上传
	 * @param local_filename 要上传的本地文件绝对路径
	 * <ul>
	 * 	<li>使用之前需先开启，调用 {@link #open()}
	 * </ul>
	 * @return {@link UploadBean}
	 * 		<ul>
	 * 			<li>{@link UploadBean#isSuccess()}:判断是否上传成功
	 * 		</ul>
	 */
	public UploadBean upload(String local_filename) {
		String[] results=null;
		try {
			String suffix = com.xnx3.Lang.findFileSuffix(local_filename);
			if(suffix==null){
				suffix=ClientGlobal.defaultSuffix;
			}
			results = client.upload_file(local_filename, suffix, meta_list);
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}

		return trimUploadBean(results);
	}
	

	/**
	 * 文件上传
	 * 	<ul>
	 * 		<li>使用之前需先开启，调用 {@link #open()}
	 * 	</ul>
	 * @param file_buff byte[]
	 * @param file_ext_name 文件后缀名,不包含"."
	 * @return {@link UploadBean}
	 * 			<ul>
	 * 				<li>{@link UploadBean#isSuccess()}:判断是否上传成功
	 * 			</ul>
	 * @see StorageClient#upload_file(byte[], String, NameValuePair[])
	 */
	public UploadBean upload(byte[] file_buff,String file_ext_name) {
		String[] results=null;
		try {
			 results = client.upload_file(file_buff, file_ext_name, meta_list);
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}

		return trimUploadBean(results);
	}
	
	/**
	 * 文件上传返回对象统一封装
	 * @param results
	 * @return UploadBean
	 */
	private UploadBean trimUploadBean(String results[]){
		UploadBean uploadBean=new UploadBean();
		if (results == null) {
			debug("upload file fail, error code: " + client.getErrorCode());
			uploadBean.setErrorInfo("upload file fail, error code: " + client.getErrorCode());
		} else {
			String group_name = results[0];
			String remote_filename = results[1];
			
			try {
				FileInfo fileInfo = client.get_file_info(group_name,remote_filename);
				uploadBean.setCrc32((int)fileInfo.getCrc32());
				uploadBean.setCreateTimestamp(fileInfo.getCreateTimestamp());
				uploadBean.setErrorInfo("");
				uploadBean.setFileSize(fileInfo.getFileSize());
				uploadBean.setGroupName(group_name);
				uploadBean.setRemoteFileName(remote_filename);
				uploadBean.setSourceIpAddr(fileInfo.getSourceIpAddr());
				uploadBean.setSuccess(true);
			} catch (IOException | MyException e) {
				e.printStackTrace();
			}
		}
		return uploadBean;
	}
	
	/**
	 * download file
	 * @param remoteFileName such as : M00/07/65/CgAA-lVRoGGAaCboAAAADv4ZzcQ734.txt
	 * @param localFileName such as : /Users/apple/Desktop/t/1.txt
	 * @return true:成功；false:失败
	 * @see StorageClient#download_file(String, String, String)
	 */
	public boolean download(String remoteFileName,String localFileName){
		try {
			int result=client.download_file(ClientGlobal.groupName, remoteFileName, localFileName);
			
			if(result==0){
				return true;
			}else{
				debug("download file failure ! error code:"+result);
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (MyException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * download file
	 * @param remoteFileName such as : M00/07/65/CgAA-lVRoGGAaCboAAAADv4ZzcQ734.txt
	 * @return byte[] , 若是返回null则为失败
	 * @see StorageClient#download_file(String, String)
	 */
	public byte[] download(String remoteFileName){
		byte[] result=null;
		try {
			result=client.download_file(ClientGlobal.groupName, remoteFileName);
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (MyException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除文件
	 * @param remoteFileName filename on storage server
	 * @return boolean
	 * @see StorageClient#delete_file(String, String)
	 */
	public boolean delete(String remoteFileName){
		boolean result=false;
		try {
			int r=client.delete_file(ClientGlobal.groupName, remoteFileName);
			if(r==0){
				result=true;
			}else{
				debug("delete file failure ! error code : "+r);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 查看文件信息
	 * @param remoteFileName filename on storage server
	 * @return FileInfo object for success, return null for fail
	 * @see StorageClient#get_file_info(String, String)
	 */
	public FileInfo getFileInfo(String remoteFileName){
		FileInfo fileInfo=null;
		try {
			fileInfo=client.get_file_info(ClientGlobal.groupName, remoteFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return fileInfo;
	}
	
	/**
	 * Console log
	 * @param text
	 */
	private void debug(String text){
		if(ClientGlobal.debug){
			System.out.println(text);
		}
	}
	
	public static void main(String[] args) {
 		FastDFSUtil client=new FastDFSUtil();
		client.open();
		
		//上传
		UploadBean upload = client.upload("/jar_file/mysql-connector-java-3.0.17-ga-bin.jar");
		
		//下载
		boolean result=client.download("M00/07/65/CgAA-lVRp9-AYn-mAAPCLZlIvsE337.txt", "/Users/apple/Desktop/1.jar");
		
		client.close();
	}
}
