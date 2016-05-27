package com.xnx3.robot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import com.xnx3.Lang;
import com.xnx3.SystemUtil;
import com.xnx3.UI;
import com.xnx3.robot.support.CoordBean;
import com.xnx3.robot.support.RGBBean;
import com.xnx3.robot.support.RgbImageComparerBean;

/**
 * 操作，模拟操作，模拟点击鼠标、按键盘、找图、输入文本等
 * @author 管雷鸣
 */
public class Robot{
	/**
	 * 当前屏幕的宽度。
	 */
	public static int screenWidth;
	/**
	 * 当前屏幕的高度。
	 */
	public static int screenHeight;	

	/**
	 * 图片搜索的缓存，搜索图片时某个图片使用后会将其加入内存，以后再次使用此图会先从程序内存找，内存没有才会从硬盘加载。
	 */
	public static Map<String, BufferedImage> bufferedImageList;
	/**
	 * 搜索模糊度参数：非常精确，精确无误
	 */
	public final static int SIM_ACCURATE_VERY = 0;
	/**
	 * 搜索模糊度参数：精确
	 */
	public final static int SIM_ACCURATE = 31;
	/**
	 * 搜索模糊度参数：模糊
	 */
	public final static int SIM_BLUR = 61;
	/**
	 * 搜索模糊度参数：非常模糊
	 */
	public final static int SIM_BLUR_VERY = 81;
	
	private Class useClass;	//使用这个工具的class，根据这个来加载图片等资源文件
	
	java.awt.Robot robot;
	
	static{
		bufferedImageList = new HashMap<String, BufferedImage>();
		screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	/**
	 * @see Robot
	 */
	public Robot() {
		float v = SystemUtil.getJavaSpecificationVersion();
		if(v>0 && v<1.7){
			UI.showMessageDialog("请使用 JDK 1.7 及以上版本进行开发。推荐使用JDK1.7");
		}
		
		try {
			robot = new java.awt.Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置资源文件路径，加载的资源文件都是在这个资源文件。
	 * <br/><b> 注意，像是{@link #getBufferImage(String)}这些获取资源文件的方法使用前必须先执行此方法进行设置资源文件所在路径 </b>
	 * @param c 传入使用此Robot类的类，用来确定资源文件所在。资源文件都放置于传入的类当前文件夹下的res文件夹中
	 */
	public void setSourcePath(Class c){
		this.useClass = c;
	}
	
	/**
	 * 将字符型转换为按键码，可直接使用 {@link #press(int)}调用
	 * @param key 字符型文字，包含 0～9 a~z .
	 * @return 按键码
	 */
	public int StringToKey(String key){
		switch (key) {
		case "a":
			return KeyEvent.VK_A;
		case "b":
			return KeyEvent.VK_B;
		case "c":
			return KeyEvent.VK_C;
		case "d":
			return KeyEvent.VK_D;
		case "e":
			return KeyEvent.VK_E;
		case "f":
			return KeyEvent.VK_F;
		case "g":
			return KeyEvent.VK_G;
		case "h":
			return KeyEvent.VK_H;
		case "i":
			return KeyEvent.VK_I;
		case "j":
			return KeyEvent.VK_J;
		case "k":
			return KeyEvent.VK_K;
		case "l":
			return KeyEvent.VK_L;
		case "m":
			return KeyEvent.VK_M;
		case "n":
			return KeyEvent.VK_N;
		case "o":
			return KeyEvent.VK_O;
		case "p":
			return KeyEvent.VK_P;
		case "q":
			return KeyEvent.VK_Q;
		case "r":
			return KeyEvent.VK_R;
		case "s":
			return KeyEvent.VK_S;
		case "t":
			return KeyEvent.VK_T;
		case "u":
			return KeyEvent.VK_U;
		case "v":
			return KeyEvent.VK_V;
		case "w":
			return KeyEvent.VK_W;
		case "x":
			return KeyEvent.VK_X;
		case "y":
			return KeyEvent.VK_Y;
		case "z":
			return KeyEvent.VK_Z;
		case "0":
			return KeyEvent.VK_0;
		case "1":
			return KeyEvent.VK_1;
		case "2":
			return KeyEvent.VK_2;
		case "3":
			return KeyEvent.VK_3;
		case "4":
			return KeyEvent.VK_4;
		case "5":
			return KeyEvent.VK_5;
		case "6":
			return KeyEvent.VK_6;
		case "7":
			return KeyEvent.VK_7;
		case "8":
			return KeyEvent.VK_8;
		case "9":
			return KeyEvent.VK_9;
		case ".":
			return KeyEvent.VK_PERIOD;
		default:
			break;
		}
		
		return 0;
	}
	
	/**
	 * 按键
	 * @param key 如：{@link KeyEvent.VK_A}
	 */
	public void press(int key){
		robot.keyPress(key);
		robot.delay(5);
		robot.keyRelease(key);
	}
	
	/**
	 * 组合键，如SHIFT+1
	 * @param key1 如：{@link KeyEvent#VK_SHIFT}
	 * @param key2 如：{@link KeyEvent#VK_1}
	 */
	public void press(int key1,int key2){
		robot.keyPress(key1);
		robot.delay(5);
		robot.keyPress(key2);
		robot.keyRelease(key1);
		robot.keyRelease(key2);
	}
	
	/**
	 * 获取 {@link java.awt.Robot}对象
	 * @return
	 */
	public java.awt.Robot getRobot(){
		return robot;
	}
	
	/**
	 * 向当前窗口输入文本
	 * @param text 不支持汉字，仅支持：a~z 0~9 !@#$%^&*()_:
	 */
	public void sendString(String text){
		for (int i = 0; i < text.length(); i++) {
			String s = text.substring(i, i+1);
			int key = StringToKey(s);
			if(key == 0){
				switch (s) {
					case "!":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_1);
						break;
					case "@":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_2);
						break;
					case "#":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_3);
						break;
					case "$":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_4);
						break;	
					case "%":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_5);
						break;
					case "^":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_6);
						break;	
					case "&":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_7);
						break;	
					case "*":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_8);
						break;
					case "(":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_9);
						break;
					case ")":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_0);
						break;
					case "_":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS);
						break;	
					case ":":
						press(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON);
						break;
						
					default:
						break;
				}
			}else{
				press(key);
			}
		}
	}
	
	/**
	 * 当前屏幕上鼠标左键点击
	 * @param x 屏幕的X坐标
	 * @param y 屏幕的Y坐标
	 */
	public void mouseLeftClick(int x,int y){
		mouseClick(x, y, InputEvent.BUTTON1_MASK);
	}
	
	/**
	 * 当前屏幕上鼠标右键点击
	 * @param x 屏幕的X坐标
	 * @param y 屏幕的Y坐标
	 */
	public void mouseRightClick(int x,int y){
		mouseClick(x, y, InputEvent.BUTTON3_MASK);
	}
	
	
	/**
	 * 延迟等待
	 * @param ms 毫秒
	 */
	public void delay(int ms){
		robot.delay(ms);
	}
	
	/**
	 * 鼠标点击
	 * @param x 点击的当前屏幕的x坐标
	 * @param y 点击的当前屏幕的y坐标
	 * @param buttons 何种方式点击，是左还是右，同 {@link java.awt.Robot#mousePress(int)}的参数
	 */
	public void mouseClick(int x, int y, int buttons){
		robot.mouseMove(x, y);
		robot.delay(20);
		robot.mousePress(buttons);
		robot.delay(20);
        robot.mouseRelease(buttons);
	}
	
	/**
	 * 鼠标滚轮
	 * @param wheelAmt 滚轮次数（滚轮方式系统设置不同，以下仅供参考，可自行测试）
	 * 			<ul>
	 * 				<li>Mac:正数为向上滚轮，负数为向下滚轮
	 * 				<li>Windows:正数为向下滚轮，负数为向上滚轮
	 * 			</ul>
	 */
	public void mouseWheel(int wheelAmt){
		robot.mouseWheel(wheelAmt);
	}
	
	/**
	 * 等待，直到此区域图片跟指定图片匹配
	 * @param xStart 当前屏幕要对比的区域的开始X坐标
	 * @param yStart 当前屏幕要对比的区域的开始Y坐标
	 * @param xEnd 当前屏幕要对比的区域的结束X坐标
	 * @param yEnd 当前屏幕要对比的区域的结束Y坐标
	 * @param image 指定寻找的目标图片
	 * @param sim 模糊值，建议使用{@link Handle#SIM_ACCURATE} 、{@link Handle#SIM_BLUR}。传入值说明：
	 * 				<ul> 
	 * 					<li>{@link Handle#SIM_ACCURATE_VERY} 精确无误，无任何误差，
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_ACCURATE} 精确，极小的误差(RGB误差30/255)
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR} 模糊，有误差(RGB误差60/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度慢，搜索方式：纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR_VERY} 非常模糊搜索，误差大，速度很慢(RGB误差100/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度最慢！搜索方式：全搜索
	 * 				</ul>
	 * @param maxDelay 最长的等待时间，单位为毫秒。如果超过这个时间图片还没出现，返回false
	 * @return 出现为true，未出现或者超时则为false
	 */
	public boolean imageDelaySearch(int xStart,int yStart,int xEnd,int yEnd,BufferedImage image,int sim,int maxDelay){
		int time = 0;
		while(time<maxDelay){
			List<CoordBean> list = imageSearch(xStart, yStart, xEnd, yEnd, image,sim);
			if(list.size()>0){
				return true;
			}else{
				delay(200);
				time+=200;
			}
		}
		return false;
	}

	/**
	 * 截取当前屏幕指定区域坐标的图像，将其保存到本地，保存为png格式图片
	 * @param xStart 截图指定区域的开始X坐标
	 * @param yStart 截图指定区域的开始Y坐标
	 * @param xEnd 截图指定区域的结束X坐标
	 * @param yEnd 截图指定区域的结束Y坐标
	 * @param filePath 截图保存的路径，如： /Users/apple/Desktop/xnx3.png
	 */
	public void screenCapture(int xStart, int yStart, int xEnd, int yEnd, String filePath){
        BufferedImage image = robot.createScreenCapture(new Rectangle(xStart, yStart, xEnd-xStart,yEnd-yStart));
        saveScreenCapture(image, filePath);
	}
	
	/**
	 * 全屏截图
	 * @return {@link BufferedImage}
	 */
	public BufferedImage screenCapture(){
		return robot.createScreenCapture(new Rectangle(0, 0, screenWidth,screenHeight));
	}

	/**
	 * 全屏截图，保存为本地文件
	 * @param filePath 截图保存的路径，如： /Users/apple/Desktop/xnx3.png
	 * @return {@link BufferedImage}
	 */
	public void screenCapture(String filePath){
		BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, screenWidth,screenHeight));
		saveScreenCapture(image, filePath);
	}
	
	/**
	 * 将 {@link BufferedImage} 保存为本地图像文件
	 * @param image 要保存的图像
	 * @param filePath 保存至本地的文件路径，如： /Users/apple/Desktop/xnx3.png
	 */
	public void saveScreenCapture(BufferedImage image,String filePath){
		try {
			ImageIO.write(image, "png", new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 根据文件名返回要使用的资源文件。（使用过一次后以后每次使用都会直接从内存取）
	 * 	<ul>
	 * 		<li> 需先调用 {@link #setSourcePath(Class)} 方法设置资源文件路径。这个只执行一次便可。读取的资源文件都是在当前类文件下的res文件夹下
	 * 	</ul>
	 * @param fileName 资源文件的文件名，如： xnx3.png  
	 * 			<ul>
	 * 				<li>xnx3.png 这个文件是在{@link #setSourcePath(Class)}传入的类的当前目录，有res文件夹，xnx3.png在res文件夹内
	 * 			</ul>
	 * @return 若没找到、失败，返回null
	 */
	public BufferedImage getResourceImage(String fileName){
		BufferedImage localImg = bufferedImageList.get(fileName);
		if(localImg != null){
			return localImg;
		}
		
		if(useClass == null){
			UI.showMessageDialog("请先执行方法 robot.setResourcePath(Class) 设置资源路径");
			return null;
		}
		
		try {
			java.io.InputStream inputStream = useClass.getResourceAsStream("res/"+fileName);
			if(inputStream == null){
//				System.out.println("要加载的图片不存在！");
			}else{
				localImg = ImageIO.read(inputStream);
			}
		} catch (FileNotFoundException ee) {
			ee.printStackTrace();
		} catch (IOException eee) {
			eee.printStackTrace();
		}
		if(localImg == null){
			return null;
		}else{
			return localImg;
		}
	}
	
	/**
	 * 将图片改变成像素数据，同时获取搜索图片时的图片相关参数
	 * @param bufferedImage 要转换的成像素数据的图片
	 * @return {@link RgbImageComparerBean}
	 */
	public RgbImageComparerBean getPX(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int minx = bufferedImage.getMinX();
		int miny = bufferedImage.getMinY();
		
		RgbImageComparerBean rgb = new RgbImageComparerBean();
		int colorArray[][] = new int[width][height];
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				colorArray[i][j] = bufferedImage.getRGB(i, j);
			}
		}
		rgb.setColorArray(colorArray);
		return rgb;
	}
	
	/**
	 * 当前屏幕指定区域，与指定的图片比较其相似度，如：
	 * 	<pre>
	 * 		Robot robot = new Robot();
	 *		robot.setSourcePath(当前类.class);
	 *		List<CoordBean> list = robot.imageSearch(0, 0, 100, 200, "xnx3.png", Robot.SIM_ACCURATE);
	 *  </pre>
	 * <li>使用此方法前必须先设置：{{@link #setSourcePath(Class)}，不然找不到传入的图片在何处。robot创建后只需设置一次便可
	 * <li>图片搜索顺序为 由上向下，由左向右
	 * @param xStart 当前屏幕要对比的区域的开始X坐标
	 * @param yStart 当前屏幕要对比的区域的开始Y坐标
	 * @param xEnd 当前屏幕要对比的区域的结束X坐标
	 * @param yEnd 当前屏幕要对比的区域的结束Y坐标
	 * @param searchImageName 要搜索的目标图片文件名字，如果多个，则用英文输入法下的符号 | 隔开，如：a.png|b.png|c.png
	 * @param sim 模糊值，值： 
	 * 				<ul>
	 * 					<li>{@link Handle#SIM_ACCURATE_VERY} 精确无误，无任何误差，
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_ACCURATE} 精确，极小的误差(RGB误差30/255)
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR} 模糊，有误差(RGB误差60/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度慢，搜索方式：纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR_VERY} 非常模糊，很大误差(RGB误差81/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度最慢，搜索方式：全搜索
	 * 				</ul>
	 * 					<br/>
	 * 					<hr/>
	 * 					<i style="margin-left:30px;">
	 * 						在2300*1100的图片中搜索50*50的图像所在，所耗时分别为:<br/>
	 * 						<ul>
	 * 							<li>{@link Handle#SIM_ACCURATE_VERY}	:	440ms
	 * 							<li>{@link Handle#SIM_ACCURATE}		:	454ms
	 * 							<li>{@link Handle#SIM_BLUR}			:	1267ms
	 * 							<li>{@link Handle#SIM_BLUR_VERY}		:	37518ms
	 * 						</ul>
	 * 					</i>
	 * @return {@link List} 将所有找到的图像位置的左上坐标返回，如果搜索不到，list.size()为0
	 */
	public List<CoordBean> imageSearch(int xStart,int yStart,int xEnd,int yEnd,String searchImageName,int sim){
		String[] names = searchImageName.split("\\|");
		List<CoordBean> list = new ArrayList<CoordBean>();
		for (int i = 0; i < names.length; i++) {
			List<CoordBean> l = imageSearch(xStart, yStart, xEnd, yEnd, getResourceImage(names[i]), sim);
			Lang.listAppend(list, l);
		}
		
		return list;
	}
	
	/**
	 * 当前屏幕上搜索图片
	 * <li>图片搜索顺序为 由上向下，由左向右
	 * @param sourceImage 原图，要在这个图上进行搜索目标图，使用 {@link #getResourceImage(String)}传入资源图片
	 * @param searchImage 要搜索的目标图片，使用 {@link #getResourceImage(String)}传入要搜索的目标资源文件
	 * @param sim 模糊值，值： 
	 * 				<ul>
	 * 					<li>{@link Handle#SIM_ACCURATE_VERY} 精确无误，无任何误差，
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_ACCURATE} 精确，极小的误差(RGB误差30/255)
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR} 模糊，有误差(RGB误差60/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度慢，搜索方式：纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR_VERY} 非常模糊，很大误差(RGB误差81/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度最慢，搜索方式：全搜索
	 * 				</ul>
	 * 					<br/>
	 * 					<hr/>
	 * 					<i style="margin-left:30px;">
	 * 						在2300*1100的图片中搜索50*50的图像所在，所耗时分别为:<br/>
	 * 						<ul>
	 * 							<li>{@link Handle#SIM_ACCURATE_VERY}	:	440ms
	 * 							<li>{@link Handle#SIM_ACCURATE}		:	454ms
	 * 							<li>{@link Handle#SIM_BLUR}			:	1267ms
	 * 							<li>{@link Handle#SIM_BLUR_VERY}		:	37518ms
	 * 						</ul>
	 * 					</i>
	 * @return {@link List} 将所有找到的图像位置的左上坐标返回，如果搜索不到，list.size()为0
	 */
	public List<CoordBean> imageSearch(BufferedImage sourceImage,BufferedImage searchImage,int sim){
		List<CoordBean> list = new ArrayList<CoordBean>();
		
		RgbImageComparerBean pxSource = getPX(sourceImage);
		RgbImageComparerBean pxSearch = getPX(searchImage);
		
		int[][] px = pxSource.getColorArray();	//原图的像素点
		int[][] pxS = pxSearch.getColorArray();	//要搜索的目标图的像素点
		int pxSXMax = pxSearch.getImgWidth()-1;	//要搜索的图的像素点的数组最大x下标
		int pxSYMax = pxSearch.getImgHeight()-1;	//要搜索的图的像素点的数组最大y下标
		int xSearchEnd = pxSource.getImgWidth()-pxSearch.getImgWidth();	//可搜索的x坐标的原图像素数组下标+1
		int ySearchEnd = pxSource.getImgHeight()-pxSearch.getImgHeight();	//可搜索的y坐标的原图像素数组下标+1
		//要搜索的图片的纵横中心点的像素大小，非坐标
		int contentSearchX = 1; 
		int contentSearchY = 1; 
		
		//根据sim计算最小像素匹配率
		double pxPercent = 0.99f;
		if(sim>0){
			//RGB的模糊率／4为最小的像素匹配率，大于这个匹配率，为图像识别成功
			pxPercent = ((double)sim/255)/4;
		}
		
		for (int x = 0; x < xSearchEnd; x++) {
			for (int y = 0; y < ySearchEnd; y++) {
				
				boolean contrast = false;	//对比，是否通过
				
				//如果使用的精确搜索（SIM_ACCURATE、SIM_ACCURATE_VERY），则匹配图像的四个角的点跟中心点
				if(sim < 32){
					//首先比较图片四个角的四个点，如果四个点比较通过，则进行下一轮比较
					if(pxCompare(px[x][y], pxS[0][0], sim)){
						//要搜索的图左上坐标在原图匹配成功
						int pxX = x+pxSearch.getImgWidth()-1;	//原图要搜索的，定位搜索图右上坐标x下标
						if(pxCompare(px[pxX][y], pxS[pxSXMax][0], sim)){
							//要搜索的图右上坐标在原图匹配成功
							int pxY = y+pxSearch.getImgHeight()-1;	//原图要搜索的，定位搜索图右上坐标x下标
							if(pxCompare(px[x][pxY], pxS[0][pxSYMax], sim)){
								//要搜索的图左下坐标在原图匹配成功
								if(pxCompare(px[pxX][pxY], pxS[pxSXMax][pxSYMax], sim)){
									//要搜索的图右下坐标在原图匹配成功
									
									//进行要搜索的图片的中心点比较
									//计算中心点坐标
									if(pxSXMax>2){
										contentSearchX = (int) Math.ceil(pxSXMax/2);
									}
									if(pxSYMax>2){
										contentSearchY = (int) Math.ceil(pxSYMax/2);
									}
									if(pxCompare(px[x+contentSearchX][y+contentSearchY], pxS[contentSearchX][contentSearchY], sim)){
										//要搜索的图的中心点坐标在原图上匹配成功
										contrast = true;
									}
								}
							}
						}
					}
				}else{
					//两个模糊搜索不搜索四角＋中心点
					contrast = true;
				}
				
				//模糊搜索的大模糊不进行纵横十字搜索
				if(sim < 62){
					//如果图片四角点对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之横向像素条比较
					if(contrast){
						int yes = 0;
						
						//计算以搜索图纵向中心，X横向像素条
						int ySour = y+contentSearchY;
						for (int i = 0; i < pxSearch.getImgWidth(); i++) {
							if(pxCompare(px[x+i][ySour], pxS[i][contentSearchY], sim)){
								yes++;
							}
						}
						if((yes/pxSearch.getImgWidth())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}
					
					//如果以上对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之纵向像素条比较
					if(contrast){
						int yes = 0;
						
						//计算以搜索图横向为中心，Y纵向像素条
						int xSour = x+contentSearchX;
						for (int i = 0; i < pxSearch.getImgHeight(); i++) {
							if(pxCompare(px[xSour][y+i], pxS[contentSearchX][i], sim)){
								yes++;
							}
						}
						
						if((yes/pxSearch.getImgHeight())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}
				}else{
					//大模糊搜索不进行纵横十字搜索
					contrast = true;
				}
				
				//进行整个目标图片的像素扫描
				if(contrast){
					int yes = 0;
					for (int xS = 0; xS < pxSearch.getImgWidth(); xS++) {
						for (int yS = 0; yS < pxSearch.getImgHeight(); yS++) {
							if(pxCompare(px[x+xS][y+yS], pxS[xS][yS], sim)){
								yes++;
							}
						}
					}
					if((yes/pxSearch.getPxCount())>pxPercent){
						CoordBean coord = new CoordBean();
						coord.setX(x);
						coord.setY(y);
						list.add(coord);
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 当前屏幕指定区域，与指定的图片比较其相似度
	 * <li>图片搜索顺序为 由上向下，由左向右
	 * @param xStart 当前屏幕要对比的区域的开始X坐标
	 * @param yStart 当前屏幕要对比的区域的开始Y坐标
	 * @param xEnd 当前屏幕要对比的区域的结束X坐标
	 * @param yEnd 当前屏幕要对比的区域的结束Y坐标
	 * @param searchImage 要搜索的目标图片，使用 {@link #getResourceImage(String)}传入要搜索的目标资源文件
	 * @param sim 模糊值，值： 
	 * 				<ul>
	 * 					<li>{@link Handle#SIM_ACCURATE_VERY} 精确无误，无任何误差，
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_ACCURATE} 精确，极小的误差(RGB误差30/255)
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度快，搜索方式：四角中心点－－纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR} 模糊，有误差(RGB误差60/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度慢，搜索方式：纵横十字搜索－－全搜索
	 * 					<li>{@link Handle#SIM_BLUR_VERY} 非常模糊，很大误差(RGB误差81/255)：
	 * 									<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 									速度最慢，搜索方式：全搜索
	 * 				</ul>
	 * 					<br/>
	 * 					<hr/>
	 * 					<i style="margin-left:30px;">
	 * 						在2300*1100的图片中搜索50*50的图像所在，所耗时分别为:<br/>
	 * 						<ul>
	 * 							<li>{@link Handle#SIM_ACCURATE_VERY}	:	440ms
	 * 							<li>{@link Handle#SIM_ACCURATE}		:	454ms
	 * 							<li>{@link Handle#SIM_BLUR}			:	1267ms
	 * 							<li>{@link Handle#SIM_BLUR_VERY}		:	37518ms
	 * 						</ul>
	 * 					</i>
	 * @see #imageSearch(BufferedImage, BufferedImage, int)
	 * @return {@link List} 若 list.size()>0 则搜索到了数值，若＝0则没有搜索到图片
	 */
	public List<CoordBean> imageSearch(int xStart,int yStart,int xEnd,int yEnd,BufferedImage searchImage,int sim){
		BufferedImage cutImage = robot.createScreenCapture(new Rectangle(xStart, yStart, xEnd-xStart,yEnd-yStart)); 
		List<CoordBean> list = imageSearch(cutImage, searchImage,SIM_ACCURATE);
		List<CoordBean> l = new ArrayList<CoordBean>(); 
		for (int i = 0; i < list.size(); i++) {
			CoordBean c = list.get(i);
			c.setX(xStart+c.getX());
			c.setY(yStart+c.getY());
			l.add(c);
		}
		
		return l;
	}

	/**
	 * 两像素点的颜色值比较，是否匹配
	 * @param pxSource 像素1，原像素，十进制的颜色值
	 * @param pxSearch 像素2，要搜索对比的像素，十进制的颜色值
	 * @param sim 模糊值，如： 
	 * 				<ul>
	 * 					<li>非常精确，无误差：{@link Handle#SIM_ACCURATE_VERY}
	 * 					<li>精确，极小的误差：{@link Handle#SIM_ACCURATE}
	 * 					<li>模糊，有误差，模糊搜索：{@link Handle#SIM_BLUR}
	 * 					<li>非常模糊，误差很大：{@link Handle#SIM_BLUR_VERY}
	 * 				</ul>
	 */
	public boolean pxCompare(int pxSource,int pxSearch,int sim){
		if(sim == SIM_ACCURATE_VERY){
			return pxSearch==pxSource;
		}else{
			RGBBean sourceRgb = intToRgb(pxSource);
			RGBBean searchRgb = intToRgb(pxSearch);
			if(Math.abs(sourceRgb.getR()-searchRgb.getR())<sim && Math.abs(sourceRgb.getG()-searchRgb.getG())<sim && Math.abs(sourceRgb.getB()-searchRgb.getB())<sim){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 将颜色值int型转换为RGB类型，三原色数值单独分开
	 * @param value 十进制的图像颜色，FFFFFF颜色转成10进制便是这个传入值
	 * @return {@link RGBBean}
	 */
	public RGBBean intToRgb(int value){
		RGBBean rgb = new RGBBean();		//searchRGB
		rgb.setR((value & 0xff0000) >> 16);
		rgb.setG((value & 0xff00) >> 8);
		rgb.setB((value & 0xff));
		return rgb;
	}
	
	/**
	 * 获取屏幕上某点颜色
	 * @param x 获取的点在屏幕的x坐标
	 * @param y 获取的点在屏幕的y坐标
	 * @return {@link Color}
	 */
	public Color getPixelColor(int x, int y){
		return robot.getPixelColor(x, y);
	}
	
	public static void main(String[] args) {
		Robot robot = new Robot();
		
		/*****图片搜索示例*****/
//		robot.setSourcePath(Robot.class);
//		List<CoordBean> list = robot.imageSearch(robot.getResourceImage("source.png"),robot.getResourceImage("search.png") , robot.SIM_ACCURATE);
//		System.out.println("共找到了"+list.size()+"个图片");
//		for (int i = 0; i < list.size(); i++) {
//			CoordBean coord = list.get(i);
//			System.out.println("第"+(i+1)+"个图片："+coord.getX()+","+coord.getY());
//		}
		
		/***模拟鼠标点击***/
		robot.mouseRightClick(499, 400);
	}
	
}

