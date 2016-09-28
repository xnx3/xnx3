package com.xnx3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import com.swetake.util.Qrcode;

/**
 * 二维码操作
 * <br/>需 Qrcode_swetake.jar
 * @author 管雷鸣
 */
public class QRCodeUtil {
	
	/**
	 * 生成一个二维码，格式是jpg的
	 * @param content 二维码的内容
	 * @return {@link BufferedImage}
	 * @throws IOException
	 */
	public static BufferedImage createQRCoder(String content) {  
		int size = 90;
		Qrcode testQrcode =new Qrcode();  
        testQrcode.setQrcodeErrorCorrect('M');  
        testQrcode.setQrcodeEncodeMode('B');  
        testQrcode.setQrcodeVersion(7);  
        String testString = content;  
        byte[] d = null;
		try {
			d = testString.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_BINARY);  
        Graphics2D g = bi.createGraphics();  
        g.setBackground(Color.WHITE);  
        g.clearRect(0, 0, size, size);  
        g.setColor(Color.BLACK);  
          
        // 限制最大字节数为119  
        if (d.length>0 && d.length <120){  
            boolean[][] s = testQrcode.calQrcode(d);  
            for (int i=0;i<s.length;i++){  
                for (int j=0;j<s.length;j++){  
                    if (s[j][i]) {  
                        g.fillRect(j*2,i*2,2,2);  
                    }  
                }  
            }  
        }  
        g.dispose();  
        bi.flush();  
        return bi;
    }  
	
	public static void main(String[] args) throws IOException {
		BufferedImage bi = createQRCoder("http://saizhuo.wang.market");
		System.out.println(bi);
		try {
			ImageIO.write(bi, "jpg", new File("/images/test.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
