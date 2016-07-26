package com.xnx3.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件操作
 * @author 管雷鸣
 *
 */
public class FileUtil {
	public final static String UTF8="UTF-8";
	public final static String GBK="GBK";
	
	static final String ENCODE="UTF-8";	//默认文件编码UTF-8
	
	/**
	 * 读文件，返回文件文本信息，默认编码UTF-8
	 * @param path 文件路径 C:\xnx3.txt
	 * @return String 读取的文件文本信息
	 */
	public static String read(String path){
		return read(path,ENCODE);
	}
	
	/**
	 * 读文件，返回文件文本信息
	 * @param path 文件路径 C:\xnx3.txt
	 * @param encode 文件编码.如 FileUtil.GBK
	 * @return String 返回的文件文本信息
	 */
	public static String read(String path,String encode){
		StringBuffer xnx3_content=new StringBuffer();
		try{
			File file=new File(path);
			BufferedReader xnx3_reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			String date=null;
			while((date=xnx3_reader.readLine())!=null){
				xnx3_content.append(date+"\n");
			}
			xnx3_reader.close();
		}catch (Exception e) {
		}
		
		return xnx3_content.toString();
	}
	
	/**
	 * 读文件，返回文件内容
	 * @param file
	 * @param encode 编码，如FileUtil.GBK
	 * @return String 读取的文件文本信息
	 */
	public static String read(File file,String encode){
		StringBuffer xnx3_content=new StringBuffer();
		try{
			BufferedReader xnx3_reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			String date=null;
			while((date=xnx3_reader.readLine())!=null){
				xnx3_content.append(date);
			}
			xnx3_reader.close();
		}catch (Exception e) {
		}
		
		return xnx3_content.toString();
	}
	
	
	/**
	 * 写文件
	 * @param path 传入要保存至的路径————如D:\\a.txt
	 * @param xnx3_content 传入要保存的内容
	 * @return 成功|失败
	 */
	public static boolean write(String path,String xnx3_content){
		try {
			FileWriter fw=new FileWriter(path);
			java.io.PrintWriter pw=new java.io.PrintWriter(fw);
			pw.print(xnx3_content);
			pw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * 写文件
	 * @param path 传入要保存至的路径————如D:\\a.txt
	 * @param xnx3_content 传入要保存的内容
	 * @param encode 写出文件的编码
	 * 				<li>{@link FileUtil#UTF8}
	 * 				<li>{@link FileUtil#GBK}
	 * @return 成功|失败
	 * @throws IOException 
	 */
	public static void write(String path,String xnx3_content,String encode) throws IOException{
        FileOutputStream fos = new FileOutputStream(path); 
        OutputStreamWriter osw = new OutputStreamWriter(fos, encode); 
        osw.write(xnx3_content); 
        osw.flush(); 
	}
	
	/**
	 * 写文件
	 * @param file 传入要保存至的路径————如D:\\a.txt
	 * @param xnx3_content 传入要保存的内容
	 * @return boolean
	 */
	public static boolean write(File file,String xnx3_content){
		try {
			java.io.PrintWriter pw=new java.io.PrintWriter(file);
			pw.print(xnx3_content);
			pw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * InputStream转为文件并保存，为jar包内的资源导出而写
	 * <pre>
	 * 	FileUtil.inputStreamToFile(getClass().getResourceAsStream("dm.dll"), "C:\\dm.dll");
	 * </pre>
	 * @param inputStream 输入流
	 * @param targetFilePath 要保存的文件路径
	 */
	public static void inputStreamToFile(InputStream inputStream, String targetFilePath) {
		File file = new File(targetFilePath);
		OutputStream os = null;

		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  复制文件
	 *  <pre>copyFile("E:\\a.txt", "E:\\aa.txt");</pre>
	 * @param sourceFile 源文件，要复制的文件所在路径
	 * @param targetFile 复制到那个地方
	 */
    public static void copyFile(String sourceFile, String targetFile){
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        }catch (Exception e) {
			e.printStackTrace();
		}finally {
			 // 关闭流
            if (inBuff != null)
				try {
					inBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            if (outBuff != null)
				try {
					outBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
        }
    }

	
	/**
	 * 删除单个文件，java操作
	 * @param fileName 文件名，包含路径。如E:\\a\\b.txt
	 * @return boolean true：删除成功
	 */
	public static boolean deleteFile(String fileName){
		boolean xnx3_result=false;
		
		java.io.File f=new java.io.File(fileName);
		if(f.isFile()&&f.exists()){
			f.delete();
			xnx3_result=true;
		}
		
		return xnx3_result;
	}
	
	
	/**
	 * 传入绝对路径，判断该文件是否存在
	 * @param filePath 文件的绝对路径，如 "C:\\WINDOWS\\system32\\msvcr100.dll"
	 * @return Boolean true:存在
	 */
    public static boolean exists(String filePath){
    	java.io.File f = new java.io.File(filePath);
    	return f.exists();
    }


	/**
	 * 通过网址获得文件长度
	 * @param url 文件的链接地址
	 * @return 文件长度(Hander里的Content-Length)
	 * 			<li>失败返回-1
	 */
	public static long getFileSize(String url) {
		int nFileLength = -1;
		try {
			URL xnx3_url = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) xnx3_url
					.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "Internet Explorer");

			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				System.err.println("Error Code : " + responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nFileLength;
	}

	/**
	 * 从互联网下载文件
	 * <li>下载过程会阻塞当前线程
	 * <li>若文件存在，会先删除存在的文件，再下载
	 * @param downUrl 下载的目标文件网址 如 "http://www.xnx3.com/down/java/j2se_util.zip"
	 * @param savePath 下载的文件保存路径。如 "C:\\test\\j2se_util.zip"
	 * @return 返回下载出现的异常
	 * 			<li>若返回null，则为下载成功，下载完毕，没有出现异常
	 * 			<li>若返回具体字符串，则出现了异常，被try捕获到了，返回e.getMessage()异常信息
	 * @throws IOException 
	 */
	public void downFiles(String downUrl,String savePath) throws IOException{
		//判断文件是否已存在，若存在，则先删除
		if(exists(savePath)){
			FileUtil.deleteFile(savePath);
		}

		int nStartPos = 0;
		int nRead = 0;
		URL url = new URL(downUrl);
		// 打开连接
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();
		// 获得文件长度
		long nEndPos = getFileSize(downUrl);
		
		RandomAccessFile oSavedFile = new RandomAccessFile(savePath, "rw");
		httpConnection
				.setRequestProperty("User-Agent", "Internet Explorer");
		String sProperty = "bytes=" + nStartPos + "-";
		// 告诉服务器book.rar这个文件从nStartPos字节开始传
		httpConnection.setRequestProperty("RANGE", sProperty);
		InputStream input = httpConnection.getInputStream();
		byte[] b = new byte[1024];
		// 读取网络文件,写入指定的文件中
		while ((nRead = input.read(b, 0, 1024)) > 0 && nStartPos < nEndPos) {
			oSavedFile.write(b, 0, nRead);
			nStartPos += nRead;
		}
			
		httpConnection.disconnect();
		oSavedFile.close();
	}
	
	/**
	 * 从互联网下载文件
	 * <li>建议使用 {@link #downFiles(String, String)}
	 * <li>下载过程会阻塞当前线程
	 * <li>若文件存在，会先删除存在的文件，再下载
	 * @param downUrl 下载的目标文件网址 如 "http://www.xnx3.com/down/java/j2se_util.zip"
	 * @param savePath 下载的文件保存路径。如 "C:\\test\\j2se_util.zip"
	 * @return 返回下载出现的异常
	 * 			<li>若返回null，则为下载成功，下载完毕，没有出现异常
	 * 			<li>若返回具体字符串，则出现了异常，被try捕获到了，返回e.getMessage()异常信息
	 */
	@Deprecated
	public static String downFile(String downUrl,String savePath){
		String result = null;
		try {
			new FileUtil().downFiles(downUrl, savePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * 将 {@link BufferedReader} 转换为 {@link String}
	 * @param br {@link BufferedReader}
	 * @return String 若失败，返回 ""
	 */
	public static String BufferedReaderToString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

    public static void main(String[] args) throws IOException {
//		downFileaa("http://www.xnx3.com/down/java/j2se_util.zip", "/music/a.zip");
    	downFile("http://www.xnx3.com/down/java/j2se_util.zip", "/music/a.zip");
	}
}
