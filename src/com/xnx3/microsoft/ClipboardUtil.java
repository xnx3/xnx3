package com.xnx3.microsoft;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * windows剪切板操作
 * @author 管雷鸣
 *
 */
public class ClipboardUtil {
	
	/**
	 * 设置剪切板文本内容
	 * @param content
	 */
	public static void setText(String content){ 
		//设置为static是为了直接使用，不用new一个该类的实例即可直接使用,即定义的: 类名.方法名
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
	public static void setImage(String filePath){
		Image img = Toolkit.getDefaultToolkit().getImage(filePath);
		setImage(img);  //给剪切板设置图片型内容
	}
	
	/**
	 * 设置剪贴板图片内容
	 * @param image
	 */
	public static void setImage(Image image) { 
		ImageSelection imgSel = new ImageSelection(image); 
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null); 
	} 

	private static class ImageSelection implements Transferable { 
	 	private Image image; 
		public ImageSelection(Image image) {this.image = image;} 
		  
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
	
}
