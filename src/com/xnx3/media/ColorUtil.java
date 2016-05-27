package com.xnx3.media;

import java.awt.Color;

/**
 * 颜色相关操作
 * @author 管雷鸣
 *
 */
public class ColorUtil {
	
    public static void main(String[] args) {
    	System.out.println(hexToInt("F1F1F1"));
		System.out.println(intToHex(-921103));
		System.out.println(intToColor(-921102));
		System.out.println(colorToHex(new Color(340)));
	}
	
    /**
     * {@link Color}转换为十六进制颜色
     * @param color {@link Color}
     * @return 十六进制颜色，如 FFFFFF
     */
    public static String colorToHex(Color color){  
        String r,g,b;  
        StringBuilder su = new StringBuilder();  
        r = Integer.toHexString(color.getRed());  
        g = Integer.toHexString(color.getGreen());  
        b = Integer.toHexString(color.getBlue());  
        r = r.length() == 1 ? "0" + r : r;  
        g = g.length() ==1 ? "0" +g : g;  
        b = b.length() == 1 ? "0" + b : b;  
        r = r.toUpperCase();  
        g = g.toUpperCase();  
        b = b.toUpperCase();  
//        su.append("0xFF");  
        su.append(r);  
        su.append(g);  
        su.append(b);  
        //0xFF0000FF  
        return su.toString();  
    }
    
    /** 
     * 十六进制字符串转换成Color对象 
     * @param colorStr 16进制颜色字符串 ,如 FFFFFF
     * @return Color对象 
     */ 
    public static Color hexToColor(String colorStr){  
        return intToColor(hexToInt(colorStr));
    }  
    
    /**
     * 将十六进制颜色转换为10进制int
     * @param hex 十六进制颜色
     * @return 10进制int
     */
    public static int hexToInt(String hex){
    	return Integer.valueOf(hex,16);
    }
    
    /** 
     * 通过RGB颜色得到十六进制的颜色 
     * @param r 0-255 
     * @param g 0-255 
     * @param b 0-255 
     * @return 255,0,253则返回FF00FD 
     */ 
    public static String RgbToHex(int r,int g,int b){ 
        return vali(getHexNum(r))+vali(getHexNum(g))+vali(getHexNum(b)); 
    } 
    
    /**
     * 将十进制rgb颜色转换为十六进制颜色字符串
     * @param rgb 十进制rgb颜色
     * @return 十六进制颜色字符串
     */
    public static String intToHex(int rgb){
    	Color color = intToColor(rgb);
    	return RgbToHex(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    private static String vali(String s){ 
        if (s.length()<2) { 
            s="0"+s; 
        } 
        return s; 
    } 
    private static String getHexNum(int num){ 
        int result=num/16; 
        int mod=num%16; 
        StringBuilder s=new StringBuilder(); 
        hexHelp(result, mod, s); 
        return s.toString(); 
    } 
    private static void hexHelp(int result,int mod,StringBuilder s){ 
        char[] H={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
        if (result>0) { 
            hexHelp(result/16, result%16, s); 
        } 
        s.append(H[mod]); 
    } 
	
	/**
	 * 将颜色值int型转换为RGB类型，三原色数值单独分开
	 * @param value 十进制的图像颜色，FFFFFF颜色转成10进制便是这个传入值
	 * @return {@link RGBBean}
	 */
	public static Color intToColor(int value){
		Color color = new Color(value);
		return color;
	}
	
	
	
}
