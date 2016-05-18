package com.xnx3;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel;
import org.jvnet.substance.utils.SubstanceConstants.ImageWatermarkKind;
import org.jvnet.substance.watermark.SubstanceImageWatermark;
import com.xnx3.file.FileUtil;


/**
 * UI界面方面
 * @author 管雷鸣
 *
 */
public class UI {
	/**
	 * confirm弹出提示框-选择了是
	 */
	public final static int CONFIRM_YES=0;
	
	/**
	 * confirm弹出提示框-选择了否
	 */
	public final static int CONFIRM_NO=1;
	
	/**
	 * confirm弹出提示框-选择了取消
	 */
	public final static int CONFIRM_CENCEL=2;

	
	/**
	 * 鼠标跟随提示信息的显示界面
	 */
	static JFrame jframeMessageForMouse;
	static JLabel jlabelMessageForMouse;
	static{
		jframeMessageForMouse=new JFrame();
		jframeMessageForMouse.setUndecorated(true);
		jframeMessageForMouse.setType(Type.POPUP);
		jframeMessageForMouse.setAlwaysOnTop(true);
		JPanel jp = new JPanel();
		jp.setBorder(new LineBorder(java.awt.Color.RED, 1, true));
		jframeMessageForMouse.setContentPane(jp);
		jp.setLayout(new CardLayout(0, 0));
		
		jlabelMessageForMouse = new JLabel("New label");
		jlabelMessageForMouse.setVerticalAlignment(SwingConstants.TOP);
		jlabelMessageForMouse.setHorizontalAlignment(SwingConstants.LEFT);
		jlabelMessageForMouse.setHorizontalTextPosition(SwingConstants.LEFT);
		jlabelMessageForMouse.setAutoscrolls(true);
		jlabelMessageForMouse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jframeMessageForMouse.setVisible(false);
			}
		});
		jlabelMessageForMouse.setBorder(new EmptyBorder(10, 10, 10, 10));
		jp.add(jlabelMessageForMouse, "name_24011468992203");
	}
	

	/**
	 * 使用第三方外观包,同时设置背景图。 {@link UI#UseLookAndFeelBySubstance()}
	 * <li>需导入substance.jar
	 * <li>设置主题 SubstanceLookAndFeel.setCurrentTheme(new SubstanceCremeTheme());
	 * <li>设置按钮外观 SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
	 * <li>设置边框 SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
	 * <li>设置渐变渲染 SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());  
	 * <li>设置标题 SubstanceLookAndFeel.setCurrentTitlePainter(new MatteHeaderPainter()); 
	 * <li>设置水印 SubstanceOfficeBlue2007LookAndFeel.setCurrentWatermark(new SubstanceMarbleVeinWatermark());
	 * @param watermarkBackgroundImage 水印背景图片 ，传入如：MainEntry.class.getResourceAsStream("res/bg.jpg") 使用当前目录下res内的bg.jpg作为水印图
	 * @param watermarkOpacity 水印图片的透明度，取值范围0.1~1之间，越接近1越真实，数字越小越模糊
	 * @return SubstanceOfficeBlue2007LookAndFeel外观包操作对象，可继续扩展
	 */
	public SubstanceOfficeBlue2007LookAndFeel UseLookAndFeelBySubstance(final InputStream watermarkBackgroundImage,final float watermarkOpacity){
		SubstanceOfficeBlue2007LookAndFeel substance = null;
		try {
			//外观设置
			substance=new SubstanceOfficeBlue2007LookAndFeel();
			UIManager.setLookAndFeel(substance);
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			if(watermarkBackgroundImage!=null){
				class mySkin extends BusinessBlackSteelSkin{
					public mySkin(){
						super();
						//新建一个图片水印，路径可以自己该，使用自己喜欢的图片来做应用程序的水印图片、  
						SubstanceImageWatermark watermark =  new SubstanceImageWatermark(watermarkBackgroundImage);
						watermark.setOpacity(watermarkOpacity);
				        watermark.setKind(ImageWatermarkKind.APP_CENTER);
				        this.watermark=watermark;
					}
				}
				SubstanceLookAndFeel.setSkin(new mySkin());
			}
		} catch (Exception e) {
			new Log().debug("com.xnx3.UI", "UseLookAndFeelBySubstance()", e.getMessage());
		}
		
		return substance;
	}
	

	/**
	 * 使用第三方外观包
	 * <br/>需在Jframe创建之前调用
	 * <li>需导入substance.jar
	 * <li>设置主题 SubstanceLookAndFeel.setCurrentTheme(new SubstanceCremeTheme());
	 * <li>设置按钮外观 SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
	 * <li>设置边框 SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
	 * <li>设置渐变渲染 SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());  
	 * <li>设置标题 SubstanceLookAndFeel.setCurrentTitlePainter(new MatteHeaderPainter()); 
	 * <li>设置水印 SubstanceOfficeBlue2007LookAndFeel.setCurrentWatermark(new SubstanceMarbleVeinWatermark());
	 * @return SubstanceOfficeBlue2007LookAndFeel 外观包操作对象，可继续扩展
	 * @see UI#UseLookAndFeelBySubstance(InputStream, float)
	 */
	public SubstanceOfficeBlue2007LookAndFeel UseLookAndFeelBySubstance(){
		return UseLookAndFeelBySubstance(null, 0);
	}
	

	/**
	 * 在当前屏幕上显示一段信息提示，配合{@link UI#hiddenMessageForMouse()} 使用
	 * @param x 信息显示框的左上角在屏幕的x坐标
	 * @param y 信息显示框的左上角在屏幕的y坐标
	 * @param width 显示的提示框宽度
	 * @param height 显示的提示框高度
	 * @param html 显示的文字，支持html格式
	 * @return 显示文字的组件JLabel，可对此组件进行调整
	 * @see UI#showMessageForMouse(MouseEvent, int, int, String)
	 */
	public static JLabel showMessageForMouse(int x,int y,int width,int height,String html){
		jlabelMessageForMouse.setText("<html>"+html);
		jframeMessageForMouse.setBounds(x, y, width, height);
		jframeMessageForMouse.setVisible(true);
		
		return jlabelMessageForMouse;
	}
	
	
	/**
	 * 隐藏鼠标跟随的信息提示 配合 {@link UI#showMessageForMouse(MouseEvent, int, int, String)} 使用
	 */
	public static void hiddenMessageForMouse(){
		jframeMessageForMouse.setVisible(false);
	}
	
	/**
	 * 显示鼠标跟随的信息提示，配合{@link UI#hiddenMessageForMouse()} 使用
	 * @param mouseEvent 添加鼠标监听后，传入鼠标的监听对象 java.awt.event.MouseEvent
	 * @param width 显示的提示框宽度
	 * @param height 显示的提示框高度
	 * @param html 显示的文字，支持html格式
	 * @return 显示文字的组件JLabel，可对此组件进行调整
	 * @see UI#showMessageForMouse(int, int, int, int, String)
	 */
	public static JLabel showMessageForMouse(MouseEvent mouseEvent,int width,int height,String html){
		int x=0;
		int y=0;
		if(mouseEvent!=null){
			x=mouseEvent.getXOnScreen();
			y=mouseEvent.getYOnScreen();
		}
		
		return showMessageForMouse(x, y, width, height, html);
	}
	

	/**
	 * 弹出提示框，
	 * @param message 要显示的信息，支持HTML
	 */
	public static void showMessageDialog(String message){
		JOptionPane.showMessageDialog(null, "<html>"+message);
	}
	
	/**
	 * 弹出选择、确认框 
	 * @param message 要显示的信息 ，支持HTML
	 * @return int {@link #CONFIRM_YES} {@link #CONFIRM_NO} {@link #CONFIRM_CENCEL}
	 */
	public static int showConfirmDialog(String message){
		return JOptionPane.showConfirmDialog(null,"<html>"+message);
	}
	

	/** 
     * 创建此程序的系统托盘，本程序的托盘创建、以及使用返回的对象进行托盘弹出信息提示等
     * <li>MyEclipse编辑时一般菜单会出现中文乱码，不用在意。打包出去后会正常。
     * <li>简单使用：<pre>SystemUtil.createTray(MyClassName.class.getResource("res/icon.png"), "显示文字", null)</pre>
     * <li>高级使用：<a href="http://www.xnx3.com/doc/j2se_util/20150307/131.html">http://www.xnx3.com/doc/j2se_util/20150307/131.html</a>
     * @param imageIcon 托盘的图标所在路径。若图片过大会不显示！建议尺寸15*15的png图片。 如 <pre>new ImageIcon(MyClassName.class.getResource("res/icon.png"))</pre> 其中trayico.png在当前的目录下的res文件夹内，MyClassName为当前生成次托盘的类名
     * @param title 鼠标放到托盘图标上时显示的文字
     * @param popupMenu 托盘右键点击后弹出的菜单项。若不想有菜单项或者不需要，可传入null
     * @return {@link TrayIcon} 对象。若是返回null，则创建失败
     */  
    public static TrayIcon createTray(ImageIcon imageIcon,String title,PopupMenu popupMenu) {  
    	TrayIcon trayIcon=null;
    	SystemTray sysTray= SystemTray.getSystemTray();// 获得当前操作系统的托盘对象  
    	
        trayIcon=new TrayIcon(imageIcon.getImage(), title, popupMenu);
        
        try {
			sysTray.add(trayIcon);		//将托盘添加到操作系统的托盘  
			return trayIcon;
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
    }  
    
    /**
     * 读取文件内容，通过用户自己打开文件选择框的方式
     * @param encode 以什么编码读取文件，如：{@link FileUtil#UTF8}
     * @return 文件的内容，若用户没有打开或者打开失败，返回null
     */
    public static String readFileByJFileChooser(String encode){
    	String xnx3_content = null;
    	JFileChooser jfc=new JFileChooser(".");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result=jfc.showOpenDialog(jfc);
		if(result==0){	//用户选择了打开
			try{
				File file=jfc.getSelectedFile();
				FileUtil f = new FileUtil();
				xnx3_content = f.read(file, encode);
				file=null;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		jfc=null;
		return xnx3_content;
    }
    
}
