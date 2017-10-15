package com.xnx3.media;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.xnx3.net.HttpUtil;
import com.xnx3.net.OSSUtil;


public class ImageUtil {
	
	/**
	 * 替换 {@link BufferedImage}中，制定颜色进行替换，比如将图片中所有红色的像素点替换为黑色
	 * @param bufferedImage 要替换的图像
	 * @param oldHex 要替换的像素点十六进制颜色，如FFFFFF
	 * @param newHex 替换成的新颜色，像素点十六进制颜色，如FFFFFF
	 * @return 新的图像{@link BufferedImage}
	 */
	public static BufferedImage replaceColor(BufferedImage bufferedImage, String oldHex,String newHex){
		int newC = ColorUtil.hexToInt(newHex);
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		BufferedImage newImage = new BufferedImage(width,height,bufferedImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(bufferedImage, 0,0,width,height,null);
        g.dispose();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(ColorUtil.intToHex(newImage.getRGB(x, y)).equals(oldHex)){
					System.out.println("-->"+newImage.getRGB(x, y));
					newImage.setRGB(x, y, newC);
					System.out.println(x+","+y);
				}
			}
		}
		
		return newImage;
	}
	
	 /**
     * 对图片进行放大
     * @param originalImage 原始图片
     * @param times 放大倍数
     * @return
     */
    public static BufferedImage bigImage(BufferedImage  originalImage, Integer times){
        int width = originalImage.getWidth()*times;
        int height = originalImage.getHeight()*times;
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0,0,width,height,null);
        g.dispose();
        return newImage;
    }
    
    /**
     * 等比例缩放
     * <br/>判断图像的宽度，若是宽度大于传入的值，则进行等比例压缩到指定宽高。若是图片小于指定的值，则不处理
     * @param inputStream 原图
     * @param maxWidth 缩放后的宽度。若大于这个宽度才会进行等比例缩放。否则不进行处理。传入0则不处理，忽略
     * @param suffix 图片的后缀名，如png、jpg
     * @return 处理好的
     */
    public static InputStream proportionZoom(InputStream inputStream,int maxWidth,String suffix){
    	if(inputStream == null){
    		return null;
    	}
    	if(maxWidth == 0 || maxWidth < 0){
    		return inputStream;
    	}
    	
    	try {
			BufferedImage bi = ImageIO.read(inputStream);
			BufferedImage b = proportionZoom(bi, maxWidth);
			ByteArrayOutputStream os = new ByteArrayOutputStream();  
			ImageIO.write(b, suffix, os);  
			InputStream is = new ByteArrayInputStream(os.toByteArray()); 
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 等比例缩放
     * <br/>判断图像的宽度，若是宽度大于传入的值，则进行等比例压缩到指定宽高。若是图片小于指定的值，则不处理
     * @param bufferedImage 原图
     * @param maxWidth 缩放后的宽度。若大于这个宽度才会进行等比例缩放。否则不进行处理
     * @return 处理好的
     */
    public static BufferedImage proportionZoom(BufferedImage bufferedImage,int maxWidth){
    	if(maxWidth == 0 || maxWidth < 0){
    		return bufferedImage;
    	}
    	if(bufferedImage == null){
    		return null;
    	}
    	
    	//原始图像的宽度
    	int originalWidth = bufferedImage.getWidth();	
    	if(maxWidth < originalWidth){
    		//原始图像的高度
    		int originalHeight = bufferedImage.getHeight();	
			//计算出等比例缩放后，新图片的高度
			int height = (int)((originalHeight*maxWidth)/originalWidth);
			
			BufferedImage newImage = new BufferedImage(maxWidth,height,bufferedImage.getType());
			Graphics g = newImage.getGraphics();
			g.drawImage(bufferedImage, 0,0,maxWidth,height,null);
			g.dispose();
			return newImage;
    	}else{
		   return bufferedImage;
	   }
    }
    
   
    /**
     * 通过一个图片所在的URL超链接，读取图片的 BufferedImage
     */
    public static BufferedImage getBufferedImageByUrl(String imageUrl){
		URL url = null;
		try {
			url = new URL(imageUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return bufferedImage;
    }
    
    /**
     * 将 {@link InputStream} 转化为 {@link BufferedImage}
     * @param is {@link InputStream}
     * @return {@link BufferedImage}
     */
    public static BufferedImage inputStreamToBufferedImage(InputStream is){
    	if(is == null){
    		return null;
    	}
    	try {
			return ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将 {@link InputStream} 转换为 {@link Byte}[]
     * @param is  {@link InputStream} 
     * @return {@link Byte}[]
     */
    public static byte[] inputStreamToByte(InputStream is)  {  
    	if(is == null){
    		return null;
    	}
    	
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[100];  
        int rc = 0;  
        try {
			while ((rc = is.read(buff, 0, 100)) > 0) {  
			    swapStream.write(buff, 0, rc);  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
        byte[] in2b = swapStream.toByteArray();  
        return in2b;  
    }  
    
    /** 
     * 获取图片的类型。如果是 gif、jpg、png、bmp 以外的类型则返回null。 
     * @param imageBytes 图片字节数组。 
     * @return 图片类型。 
     * @throws java.io.IOException IO异常。 
     */  
    public static String getImageType(byte[] imageBytes) {  
    	if(imageBytes == null){
    		return null;
    	}
        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);  
        ImageInputStream imageInput = null;
        String type = null;  
		try {
			imageInput = ImageIO.createImageInputStream(input);
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInput);  
	        if (iterator.hasNext()) {  
	            ImageReader reader = iterator.next();  
	            type = reader.getFormatName().toUpperCase();  
	        }  
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (imageInput != null) {  
	            try {
					imageInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
	        }
		}
        
        return type;  
    }  
    
    /**
     * 将 {@link BufferedImage} 转换为 {@link InputStream}
     * @param bufferedImage 要转换的 {@link BufferedImage} 
     * @param imageFileSuffix 图片的后缀名，如 gif 、png 、 jpg 、 bmp
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage bufferedImage, String imageFileSuffix){
    	if(bufferedImage == null){
    		return null;
    	}
    	ByteArrayOutputStream os = new ByteArrayOutputStream();  
    	try {
			ImageIO.write(bufferedImage, imageFileSuffix, os);
		} catch (IOException e) {
			e.printStackTrace();
		}  
    	InputStream is = new ByteArrayInputStream(os.toByteArray());  
    	return is;
    }
    
    /**
     * 将 {@link BufferedImage} 转换为 {@link Byte}[]
     * @param bufferedImage 要转换的 {@link BufferedImage}
     * @param imageSuffix 图片的后缀名，如 gif 、png 、 jpg 、 bmp
     * @return {@link Byte}[] ，若是出错或没有，返回null
     */
    public static byte[] bufferedImageToByte(BufferedImage bufferedImage, String imageSuffix){
    	if(bufferedImage == null){
    		return null;
    	}
        ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, imageSuffix, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(out == null){
			return null;
		}else{
			return out.toByteArray();
		}
    }
    
    /**
     * 加载本地图像，变为 {@link BufferedImage}
     * @param path 图像所在路径，如 ： /images/ceshi.png
     * @return {@link BufferedImage} 若为空或图像不存在，返回null
     */
    public static BufferedImage loadLocalhostImage(String path){
    	if(path == null){
    		return null;
    	}
    	try {
			return ImageIO.read(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将 {@link BufferedImage} 保存到本地
     * @param bufferedImage 要保存的内存图像
     * @param formatName 如 jpg、 png、gif 
     * @param path 本地路径，如 /images/xnx3/ceshiaaaa.png
     */
    public static void saveToLocalhost(BufferedImage bufferedImage, String formatName,String path){
    	File f=new File(path);
    	try {
			ImageIO.write(bufferedImage,formatName,f);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 后缀格式转换，读入 png、jpg、gif格式图片的 {@link BufferedImage} ，返回新生成的内存图像，可另行保存为png、jpg、gif等格式
     * @param bufferedImage
     * @return 新创建的 {@link BufferedImage}
     */
    public static BufferedImage formatConversion(BufferedImage bufferedImage){
    	 Image image = bufferedImage.getScaledInstance(bufferedImage.getWidth(),bufferedImage.getHeight(), Image.SCALE_DEFAULT);
         BufferedImage tag = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
         Graphics g = tag.getGraphics();
         g.drawImage(image, 0, 0, null); // 绘制新图
         g.dispose();
         return tag;
    }
    
    public static void main(String[] args) throws IOException {
    	BufferedImage bufferedImage = loadLocalhostImage("/images/xnx3/qrcode_for_gh_674025ffa56e_430.jpg");
        BufferedImage tag = formatConversion(bufferedImage);
		saveToLocalhost(tag, "png", "/images/xnx3/ceshi.png");
	}
    
	
}
