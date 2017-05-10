package com.xnx3.media;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import com.xnx3.media.ImageUtil;

/**
 * 验证码相关，为数字、英文，其中不含0、O、1、I等容易混淆的字符
 * @author 管雷鸣
 */
public class CaptchaUtil {
	private int width = 90;	//生成图片的宽度
	private int height = 20;	//生成图片的高度
	private int codeCount = 4;	//图片上显示验证码的个数，1到5之间
	private int xx = 15;
	private int fontHeight = 21;	//验证码文字的高度,文字本身的高
	private int codeY = 16;
	public char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' , 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
	private Font font;	//验证码的字体
	private String[] code;	//当前的验证码
	private Color backgroundColor;	//图片背景颜色
	Random random;
	
	/**
	 * 验证码相关，为数字、英文，其中不含0、O、1、I等容易混淆的字符
	 */
	public CaptchaUtil() {
		backgroundColor = Color.WHITE;
		code = new String[codeCount];
		random = new Random();
		font = new Font("Fixedsys", Font.BOLD, fontHeight);
		generateCode();
	}
	
	/**
	 * 生成图片的宽度
	 * @return 单位：像素 px
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 生成图片的宽度
	 * @param width 宽度，单位：像素 px
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 生成图片的高度
	 * @return 单位：像素 px
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 生成图片的高度
	 * @param height 高度，单位：像素 px
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 图片上显示验证码的个数，1到5之间
	 * @return 1～5
	 */
	public int getCodeCount() {
		return codeCount;
	}

	/**
	 * 图片上显示验证码的个数，1到5之间
	 * @param codeCount 验证码个数，限制1～5
	 */
	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
		
		//重新生成验证码
		code = new String[codeCount];
		generateCode();
	}

	/**
	 * 验证码文字的高度,文字本身的高
	 * @return 单位：像素 px
	 */
	public int getFontHeight() {
		return fontHeight;
	}

	/**
	 * 验证码文字的高度,文字本身的高
	 * @param fontHeight 文字本身的高度，单位：像素 px
	 */
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	/**
	 * 验证码的字体
	 * @return 当前使用的字体
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * 验证码的字体，默认使用：
	 * <pre>Font font = new Font("Fixedsys", Font.BOLD, fontHeight);</pre>
	 * @param font 字体
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * 获取当前的验证码
	 * @return 验证码
	 */
	public String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < code.length; i++) {
			sb.append(code[i]);
		}
		return sb.toString();
	}
	
	/**
	 * 设置当前的验证码，如果想让验证码显示汉字等，可自己传入要显示的字符串
	 * @param code 绘制到图片上的验证码
	 */
	public void setCode(String[] code) {
		this.code = code;
	}
	
	/**
	 * 获取验证码图片的背景颜色
	 * @return Color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * 设置验证码图片的背景颜色
	 * <br/>不设置默认为白色 {@link Color#WHITE}
	 * @param backgroundColor
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * 生成验证码（刷新验证码）
	 */
	private void generateCode(){
		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
		// 得到随机产生的验证码数字。
			code[i] = String.valueOf(codeSequence[random.nextInt(31)]);
		}
	}
	
	/**
	 * 创建（生成）验证码图片
	 */
	public BufferedImage createImage(){
		//定义图像buffer
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics gd = bufferedImage.getGraphics();
		//创建一个随机数生成器类
		
		// 将图像填充为白色
		gd.setColor(backgroundColor);
		gd.fillRect(0, 0, width, height);
		
		// 设置字体。
		gd.setFont(font);

		// 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 15; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}
		
		//randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < code.length; i++) {
			//产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			//用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(red, green, blue));
			gd.drawString(code[i], (i + 1) * xx, codeY);

			//将产生的四个随机数组合在一起。
			randomCode.append(code);
		}
		return bufferedImage;
	}
	
	public static void main(String[] args) {
		CaptchaUtil cap = new CaptchaUtil();
		cap.setCodeCount(4);		//验证码个数为4个字符
		cap.setBackgroundColor(Color.BLACK); 	//图片背景为黑色
		BufferedImage bufferedImage = cap.createImage();
		ImageUtil.saveToLocalhost(bufferedImage, "jpg", "/images/cs.jpg");	//保存到本地，能看到生成的验证码图
		System.out.println(cap.getCode());	//获取当前验证码
	}
	
}
