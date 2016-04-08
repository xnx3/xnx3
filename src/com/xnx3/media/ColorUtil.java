package com.xnx3.media;

import java.awt.Color;


/**
 * 颜色相关操作
 * @author 管雷鸣
 *
 */
public class ColorUtil {
	
	/** 
	 * 
     * Color对象转换成字符串 
     * @param color Color对象 
     * @return 16进制颜色字符串 
     */  
    public static String toHexFromColor(Color color){  
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
        su.append("0xFF");  
        su.append(r);  
        su.append(g);  
        su.append(b);  
        //0xFF0000FF  
        return su.toString();  
    }
    
    /** 
     * 字符串转换成Color对象 
     * @param colorStr 16进制颜色字符串 
     * @return Color对象 
     */ 
    public static Color hexToColor(String colorStr){  
        colorStr = colorStr.substring(4);  
        Color color = new Color(Integer.parseInt(colorStr, 16)) ;  
        return color;  
    }  
    
    /**
     * {@link Color}转换为十六进制颜色
     * @param color {@link Color}
     * @return 十六进制颜色，如 FFFFFF
     */
    public static String colorToHex(Color color){
    	return RgbToHex(color.getRed(), color.getGreen(), color.getBlue());
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
	
}
