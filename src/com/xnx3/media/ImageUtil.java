package com.xnx3.media;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


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
	
}
