package com.xnx3;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作跟系统交互、以及调用相关
 * @author 管雷鸣
 */
public class SystemUtil {
	
	/**
	 * 调用当前系统的默认浏览器打开网页
	 * @param url 要打开网页的url
	 */
	public static void openUrl(String url){
		java.net.URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Java运行时环境规范版本,如： 1.6 、1.7
	 * @return 数值 获取失败或转换失败返回 0 ，若成功，返回如： 1.7
	 */
	public static float getJavaSpecificationVersion(){
		String v = System.getProperty("java.specification.version");
		float xnx3_result = 0f;
		if(v == null || v.equals("")){
			xnx3_result = 0;
		}else{
			xnx3_result = Lang.stringToFloat(v, 0);
		}
		return xnx3_result;
	}
	
	/**
	 * 获取当前项目路径，用户的当前工作目录，如当前项目名为xnx3，则会获得其绝对路径 "E:\MyEclipseWork\xnx3"
	 * @return 项目路径
	 */
	public static String getCurrentDir(){
		return System.getProperty("user.dir");
	}
	
	/**
	 * 获取当前Java运行所依赖的Jre的路径所在，绝对路径
	 * @return 如：D:\Program Files\MyEclipse2014\binary\com.sun.java.jdk7.win32.x86_64_1.7.0.u45\jre
	 */
	public static String getJrePath(){
		return System.getProperty("java.home");
	}

	/**
	 * 设置剪切板文本内容
	 * @param content 内容
	 */
	public static void setClipboardText(String content){ 
		String vc = content.trim();
		StringSelection ss = new StringSelection(vc);
		java.awt.datatransfer.Clipboard sysClb=null;
		sysClb = Toolkit.getDefaultToolkit().getSystemClipboard();
		sysClb.setContents(ss,null);
	}
	
	/**
	 * 设置剪切板图片内容
	 * @param filePath 图片文件所在路径，如：E:\\MyEclipseWork\\refreshTaobao\\logScreen\\a.png 格式限制gif|png|jpg
	 */
	public static void setClipboardImage(String filePath){
		Image img = Toolkit.getDefaultToolkit().getImage(filePath);
		setClipboardImage(img);  //给剪切板设置图片型内容
	}
	
	/**
	 * 设置剪贴板图片内容
	 * @param image 图片
	 */
	public static void setClipboardImage(Image image) { 
		ImageSelection imgSel = new ImageSelection(image); 
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null); 
	} 
	
	/**
	 * 剪切版相关操作使用
	 * @author 管雷鸣
	 */
	private static class ImageSelection implements Transferable { 
	 	private Image image; 
		public ImageSelection(Image image) {
			this.image = image;
		} 
		  
		public DataFlavor[] getTransferDataFlavors() { 
			return new DataFlavor[]{DataFlavor.imageFlavor}; 
		} 
		public boolean isDataFlavorSupported(DataFlavor flavor) { 
			return DataFlavor.imageFlavor.equals(flavor); 
		}
		  
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException { 
			if (!DataFlavor.imageFlavor.equals(flavor)) {throw new UnsupportedFlavorException(flavor);} 
			return image; 
		}
	}
	
	/**
	 * 打开本地的文件夹
	 * @param filePath 路径，如 /Users/apple/Desktop/119/
	 */
	public static void openLocalFolder(String filePath){
		try {
			java.awt.Desktop.getDesktop().open(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
