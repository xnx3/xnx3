package com.xnx3.media;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;


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
     * @param maxWidth 缩放后的宽度。若大于这个宽度才会进行等比例缩放。否则不进行处理
     * @param suffix 图片的后缀名，如png、jpg
     * @return 处理好的
     */
    public static InputStream proportionZoom(InputStream inputStream,int maxWidth,String suffix){
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
    
    public static void main(String[] args) throws IOException {
    	File file=new File("/images/1347256202745.jpg");
    	InputStream is=new FileInputStream(file);
    	BufferedImage bi=ImageIO.read(is);
    	System.out.println(bi.getWidth());
    	
    	BufferedImage b = proportionZoom(bi, 50);
    	System.out.println(b.getWidth());
    	
    	ImageIO.write(b, "png", new File("/images/1347256202745s.jpg"));
	}
    

    /**
     * @param im            原始图像
     * @param resizeTimes   倍数,比如0.5就是缩小一半,0.98等等double类型
     * @return              返回处理后的图像
     */
    public BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
        /*原始图像的宽度和高度*/
        int width = im.getWidth();
        int height = im.getHeight();
 
        /*调整后的图片的宽度和高度*/
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);
 
        /*新生成结果图片*/
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
 
        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return result;
    }
	
}
