package com.xnx3.robot;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import com.xnx3.Lang;
import com.xnx3.SystemUtil;
import com.xnx3.UI;
import com.xnx3.media.ColorUtil;
import com.xnx3.robot.support.CoordBean;
import com.xnx3.robot.support.RGBBean;

import java.io.File;
import java.util.List;

import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.theme.SubstanceCremeTheme;

import java.awt.Font;

/**
 * 仙人辅助
 * @author 管雷鸣
 *
 */
public class ScreenCapture extends JFrame {
	private BufferedImage bufferedImage;
	public Robot robot;
	
	private JPanel contentPane;

    private JPanel imagePanel;
    private JLabel imageLabel;
    private JTextField positionTextview;
    
    //鼠标所勾选的截取区域纪录
    private int mouseSelectXStart = 0;
    private int mouseSelectYStart = 0;
    private int mouseSelectXEnd = 0;
    private int mouseSelectYEnd = 0;
    
    //当前鼠标所在的坐标
    private int currentMouseX;
    private int currentMouseY;
    private JLabel bigImageLabel;
    private JPanel bigImagePanel;
    
    public final static int bigMultiple_mouseView = 20;	//图像鼠标查看的放大倍数
    public static int bigMultiple_mainView = 1;	//主查看区域的放大倍数
    private JSlider imageSizeSlider;
    
    /***鼠标选择图像截图区域的截图，鼠标按下时赋予值，放开时重回－1***/
    private int sX = -1;	//选中的开始点的x坐标
	private int sY = -1;	//选中的开始点的y坐标
	private int eX = -1;	//选中的结束点的x坐标
	private int eY = -1;	//选中的结束点的x坐标
	private JTextField pxColorTextField;
	private JLabel selectImageSizeLabel;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new UI().UseLookAndFeelBySubstance(ScreenCapture.class.getResourceAsStream("res/screenCaptureJframeBackground.jpg"), 0.2f);
//					new UI().UseLookAndFeelBySubstance();
					SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
					SubstanceLookAndFeel.setCurrentTheme(new SubstanceCremeTheme());
					ScreenCapture frame = new ScreenCapture();
					frame.getImageLabel().setIcon(new ImageIcon(ScreenCapture.class.getResource("/com/xnx3/robot/res/mainImagePanelBackGround.png")));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ScreenCapture() {
		robot = new Robot();
		setTitle("仙人辅助 v"+Lang.version+"   管雷鸣");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 669, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		imagePanel = new JPanel();
		imagePanel.setOpaque(false);
		imagePanel.setBackground(Color.BLACK);
		
		JPanel panel_1 = new JPanel();
		panel_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				keyMouseHotspot(e.getKeyCode());
			}
		});
		
		JPanel panel_3 = new JPanel();
		
		imageSizeSlider = new JSlider();
		imageSizeSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imageSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				bigMultiple_mainView = imageSizeSlider.getValue();
				updateImagePanel(bufferedImage);
			}
		});
		imageSizeSlider.setValue(1);
		imageSizeSlider.setMinimum(1);
		imageSizeSlider.setMaximum(50);
		
		JButton button_4 = new JButton("－");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				smallImageButton();
			}
		});
		
		JButton button_5 = new JButton("＋");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bigImageButton();
			}
		});
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(imageSizeSlider, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button_5, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
						.addComponent(imageSizeSlider, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
						.addComponent(button_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
						.addComponent(button_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
						.addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
		);
		imagePanel.setLayout(new BorderLayout(0, 0));
		
		imageLabel = new JLabel("");
		imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		imageLabel.setOpaque(true);
		imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				setPxColorTextField(e);
				showBigImageLabel(e);
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(bufferedImage != null){
					drawRect(e);
					setPxColorTextField(e);
					showBigImageLabel(e);
				}
			}
		});
		imageLabel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
//				keyMouseHotspot(e.getKeyCode());
			}
		});
		imageLabel.setVerticalTextPosition(SwingConstants.TOP);
		imageLabel.setVerticalAlignment(SwingConstants.TOP);
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(bufferedImage == null){
					return;
				}
				BufferedImage image = bigImage(bufferedImage, 1);
				updateImagePanel(image);
				if(e.getX() <= image.getWidth()*bigMultiple_mainView && e.getY() <= image.getHeight()*bigMultiple_mainView){
					sX = e.getX()/bigMultiple_mainView;
					sY = e.getY()/bigMultiple_mainView;
				}else{
					sX = -1;
					sY = -1;
				}
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(bufferedImage == null){
					return;
				}
				
				drawRect(e);
				
				sX = -1;
				sY = -1;
				eX = -1;
				eY = -1;
			}
		});
		imageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		imageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		imageLabel.setBackground(Color.BLACK);
		imagePanel.add(imageLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192)), "\u56FE\u7247\u64CD\u4F5C\u533A\u57DF", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(null);
		
		JLabel lblQq = new JLabel("<html>\n作者：管雷鸣   QQ交流群:418768360<br/>\n纯 Java 模拟鼠标点击、键盘按键、找图找色，加入<a href=\"http://github.com/xnx3/xnx3\" target=\"_black\">xnx3.jar</a>即可实现跨平台辅助开发");
		lblQq.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SystemUtil.openUrl("http://github.com/xnx3/xnx3");
			}
		});
		lblQq.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblQq.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton button_1 = new JButton("保存到本地");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveImage(bufferedImage);
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblQq, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
							.addContainerGap())
						.addComponent(panel_2, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(7, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(button_1, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblQq, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JButton button = new JButton("区域截图");
		button.setToolTipText("在屏幕上用鼠标划出指定的区域截图");
		
		JButton button_2 = new JButton("全屏截图");
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button_2, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(4)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_2, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
						.addComponent(button, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
					.addGap(3))
		);
		panel_4.setLayout(gl_panel_4);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fullScreenCutImage();
			}
		});
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cutImage();
			}
		});
		
		bigImagePanel = new JPanel();
		bigImagePanel.setOpaque(false);
		
		positionTextview = new JTextField();
		positionTextview.setToolTipText("鼠标在左侧图片区划出矩形选择区块时，会在当前显示出所划矩形位于图片的： 开始点x坐标，开始点y坐标，结束点x坐标，结束点y坐标");
		positionTextview.setText("xStart,yStart,xEnd,yEnd");
		positionTextview.setColumns(10);
		
		pxColorTextField = new JTextField();
		pxColorTextField.setToolTipText("像素点颜色（16进制颜色）｜当前鼠标所在点位于图片上的x坐标,y坐标");
		pxColorTextField.setText("像素点颜色｜x坐标,y坐标");
		pxColorTextField.setColumns(10);
		
		selectImageSizeLabel = new JLabel("宽＊高");
		selectImageSizeLabel.setToolTipText("鼠标在左侧图片区域中所划矩形的宽＊高");
		selectImageSizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton button_3 = new JButton("裁剪");
		button_3.setToolTipText("会将方框覆盖的一像素一并裁剪");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jianqie();
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(bigImagePanel, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(pxColorTextField)
								.addComponent(positionTextview, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
								.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 59, Short.MAX_VALUE)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(selectImageSizeLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(bigImagePanel, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addGap(20)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(pxColorTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectImageSizeLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(2)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(button_3, 0, 0, Short.MAX_VALUE)
						.addComponent(positionTextview, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		
		bigImageLabel = new JLabel("");
		bigImageLabel.setToolTipText("右侧图片区域鼠标跟随放大显示。放到效果固定为20倍");
		bigImageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		bigImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_bigImagePanel = new GroupLayout(bigImagePanel);
		gl_bigImagePanel.setHorizontalGroup(
			gl_bigImagePanel.createParallelGroup(Alignment.LEADING)
				.addComponent(bigImageLabel, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
		);
		gl_bigImagePanel.setVerticalGroup(
			gl_bigImagePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bigImagePanel.createSequentialGroup()
					.addComponent(bigImageLabel, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		bigImagePanel.setLayout(gl_bigImagePanel);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void drawRect(MouseEvent e){
		BufferedImage image = bigImage(bufferedImage, 1);
		eX = e.getX()/bigMultiple_mainView;
		eY = e.getY()/bigMultiple_mainView;
		if(eX < image.getWidth() && eY < image.getHeight() && sX >-1 && sY >-1){
			if(eX-sX<1 || eY-sY<1){
				//并没有选择有效区域
			}else{
				mouseSelectXStart = sX;
				mouseSelectYStart = sY;
				mouseSelectXEnd = eX;
				mouseSelectYEnd = eY;
				positionTextview.setText(mouseSelectXStart+","+mouseSelectYStart+","+mouseSelectXEnd+","+mouseSelectYEnd);
				
				//得到Graphics2D 对象
//				BufferedImage b1 = bufferedImage.getSubimage(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
//				BufferedImage b = bigImage(b1, bigMultiple_mainView);
	    		Graphics2D g2d=(Graphics2D)image.getGraphics();
	    		//设置颜色和画笔粗细
	    		g2d.setColor(Color.RED);
	    		g2d.setStroke(new BasicStroke(1));
	    		g2d.drawRect(sX, sY, (eX-sX), (eY-sY));
	    		//绘制坐标点
//	    		g2d.drawString(mouseSelectXStart+","+mouseSelectYStart, mouseSelectXStart, mouseSelectYStart+12);
//	    		g2d.drawString(mouseSelectXEnd+","+mouseSelectYEnd, mouseSelectXEnd-60, mouseSelectYEnd-2);
	    		updateImagePanel(image);
	    		selectImageSizeLabel.setText((eX-sX)+","+(eY-sY));
			}
		}
	}
	
	/**
	 * 缩小图片按钮
	 */
	public void smallImageButton(){
		if(bigMultiple_mainView > 1){
			bigMultiple_mainView--;
		}
		
		imageSizeSlider.setValue(bigMultiple_mainView);
		updateImagePanel(bufferedImage);
	}
	
	/**
	 * 放大图片按钮
	 */
	public void bigImageButton(){
		if(bufferedImage == null){
			UI.showMessageDialog("编辑区没有图像，请先截取图像");
			return;
		}
		bigMultiple_mainView++;
		imageSizeSlider.setValue(bigMultiple_mainView);
		updateImagePanel(bufferedImage);
	}
	
	/**
	 * 键盘移动图像区域的热点矩形
	 * @param keyCode 如{@link KeyEvent#VK_UP}
	 */
	public void keyMouseHotspot(int keyCode){
		boolean update = false;	//重新绘制更新UI
		
		switch (keyCode) {
		case KeyEvent.VK_UP:
			if(mouseSelectYStart>0){
				mouseSelectYStart--;
				mouseSelectYEnd--;
				update = true;
			}
			break;
		case KeyEvent.VK_DOWN:
			if(bufferedImage.getHeight()-mouseSelectYEnd > 0){
				mouseSelectYStart++;
				mouseSelectYEnd++;
				update = true;
			}
			break;
		case KeyEvent.VK_LEFT:
			if(mouseSelectXStart > 0){
				mouseSelectXStart--;
				mouseSelectXEnd--;
				update = true;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(bufferedImage.getWidth()-mouseSelectXEnd > 0){
				mouseSelectXStart++;
				mouseSelectXEnd++;
				update = true;
			}
			break;

		default:
			break;
		}
		
		if(update){
			updateImagePanel(bufferedImage);
		}
	}
	
	//保存PNG格式的过滤器
    private class PNGfilter extends javax.swing.filechooser.FileFilter{
        public boolean accept(File file){
            if(file.toString().toLowerCase().endsWith(".png") || file.isDirectory()){
                return true;
            }else{
            	return false;
            }
        }
        public String getDescription(){
            return "*.PNG(PNG图像)";
        }
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
     * 设置鼠标移动时的实时像素点的颜色信息
     * @param color
     */
    private void setPxColorTextField(MouseEvent e){
    	if(bufferedImage == null){
    		return;
    	}
    	
    	int x = (int) Math.floor(e.getX()/bigMultiple_mainView);
    	int y = (int) Math.floor(e.getY()/bigMultiple_mainView);
    	if(x>=0 && x<bufferedImage.getWidth() && y>=0 && y<bufferedImage.getHeight()){
    		RGBBean rgbBean = robot.intToRgb(bufferedImage.getRGB(x, y));
    		pxColorTextField.setText(ColorUtil.RgbToHex(rgbBean.getR(), rgbBean.getG(), rgbBean.getB())+"｜"+x+","+y);
    	}
    }
    
    /**
     * 根据鼠标的移动，实时显示在右侧的放大小窗口
     * @param e
     */
    private void showBigImageLabel(MouseEvent e){
    	if(bufferedImage == null){
    		return;
    	}
    	
    	/****原图上截取放大图像使用***/
    	int x = -1;
    	int y = -1;
    	int width = 10;
    	int height = 10;
    	
    	/*** 小图显示的当前坐标所在点十字定位 ***/
    	int bigX = 0;
    	int bigY = 0;
    	
    	/****不放大，原始的坐标 sourceX ,sourceY*****/
    	int sX = (int) Math.floor(e.getX()/bigMultiple_mainView);
    	int sY = (int) Math.floor(e.getY()/bigMultiple_mainView);
    	
    	//首先判断鼠标是在图像内移动的,并且最大处留出1像素
    	if(sX>=0 && sX<bufferedImage.getWidth() && sY>=0 && sY<bufferedImage.getHeight()){
    		/****判断开始坐标点****/
        	if(sX>=0 && sX<=5){
        		x = 0;
        		bigX = sX;
        	}else if(sX>5 && bufferedImage.getWidth()-sX>10) {
    			x = sX-5;
    			bigX = 5;
    		}else{
    			x = bufferedImage.getWidth()-10;
    			bigX = width - (bufferedImage.getWidth()-sX);
    		}
        	
        	if(sY>=0 && sY<=5){
        		y = 0;
        		bigY = sY;
        	}else if(sY>5 && bufferedImage.getHeight()-sY>10) {
    			y = sY-5;
    			bigY = 5;
    		}else{
    			y = bufferedImage.getHeight()-10;
    			bigY = height - (bufferedImage.getHeight()-sY);
    		}
        		
        	//判断截取显示的宽度跟高度
//        	if(bufferedImage.getWidth()-x<10){
//        		width = bufferedImage.getWidth() - x;
//        	}
//        	if(bufferedImage.getHeight()-y<10){
//        		height = bufferedImage.getHeight() - y;
//        	}
        	
            //如果编辑区的图像宽或者高有一个小于10的，那么放大区的图片是没法显示的，故而不让其显示到放大区
        	if(bufferedImage.getWidth()<width || bufferedImage.getHeight()<height){
        		return;
        	}

        	//得到Graphics2D 对象
    		BufferedImage b1 = bufferedImage.getSubimage(x, y, width, height);
    		BufferedImage b = bigImage(b1, bigMultiple_mouseView);
    		//重新绘制一个bufferedImage，像素点组合
    		Graphics2D g2d=(Graphics2D)b.getGraphics();
    		//设置颜色和画笔粗细
    		g2d.setColor(Color.RED);
    		g2d.setStroke(new BasicStroke(1));
    		
    		//画纵横十字定位
    		g2d.drawLine(bigX*bigMultiple_mouseView,0, bigX*bigMultiple_mouseView, b.getHeight());	//纵向左 
    		g2d.drawLine((bigX+1)*bigMultiple_mouseView-1,0, (bigX+1)*bigMultiple_mouseView-1, b.getHeight());	//纵向右
    		
    		g2d.drawLine(0,bigY*bigMultiple_mouseView-1, b.getWidth(), bigY*bigMultiple_mouseView-1);	//横向上
    		g2d.drawLine(0,(bigY+1)*bigMultiple_mouseView-1, b.getWidth(), (bigY+1)*bigMultiple_mouseView-1);	//横向下
    		
            bigImageLabel.setIcon(new ImageIcon(b));
        	
    	}
    }
    
    /**
    *公用的处理保存图片的方法
    */
    public void saveImage(BufferedImage image){
        try{
            if(image==null){
                JOptionPane.showMessageDialog(this
                  , "图片不能为空!!", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser jfc=new JFileChooser(".");
            jfc.addChoosableFileFilter(new PNGfilter());
            int i=jfc.showSaveDialog(this);
            if(i==JFileChooser.APPROVE_OPTION){
                File file=jfc.getSelectedFile();
                String about="PNG";
                String ext=file.toString().toLowerCase();
                javax.swing.filechooser.FileFilter ff=jfc.getFileFilter();
                if(ff instanceof PNGfilter){
                    if(!ext.endsWith(".png")){
                        String ns=ext+".png";
                        file=new File(ns);
                        about="PNG";
                    }
                }
                if(ImageIO.write(image,about,file)){
                    UI.showMessageDialog("保存成功");
                } else{
                	UI.showMessageDialog("保存失败");
                }
            }
        } catch(Exception exe){
            exe.printStackTrace();
        }
    }
	
    /**
     * 全屏截图
     */
    private void fullScreenCutImage(){
    	this.setVisible(false);
    	setImageSizeInit();
        robot.delay(500);	//等待500毫秒让主窗口完全隐藏
    	bufferedImage=robot.screenCapture();
    	this.setVisible(true);
    	updateImagePanel(bufferedImage);
    }
    
    /**
     * 图像裁剪功能，对大图像选择的区域进行裁剪
     */
    private void jianqie(){
    	if(mouseSelectXEnd-mouseSelectXStart<=0 || mouseSelectYEnd-mouseSelectYStart<=0){
    		UI.showMessageDialog("请先用鼠标在左侧图像区域中画出要裁剪的图像");
    	}else{
    		bufferedImage = bufferedImage.getSubimage(mouseSelectXStart, mouseSelectYStart, mouseSelectXEnd-mouseSelectXStart+1, mouseSelectYEnd-mouseSelectYStart+1);
    		updateImagePanel(bufferedImage);
    	}
    }
    
	//截图
    private void cutImage(){
        try{
            this.setVisible(false);
            setImageSizeInit();
            robot.delay(200);	//等待200毫秒让主窗口完全隐藏
            Toolkit tk=Toolkit.getDefaultToolkit(); // AWT组件的抽象父类（java.awt）
            Dimension di=tk.getScreenSize();
            Rectangle rec=new Rectangle(0,0,di.width,di.height);
            BufferedImage bi=robot.getRobot().createScreenCapture(rec);
            JFrame jf=new JFrame();
            Temp temp=new Temp(jf,bi,di.width,di.height); // 自定义的Temp类的对象
            jf.getContentPane().add(temp,BorderLayout.CENTER);
            jf.setUndecorated(true);
            jf.setSize(di);
            jf.setVisible(true);
            jf.setAlwaysOnTop(true);
        } catch(Exception exe){
            exe.printStackTrace();
        }
    }
    
    /**
     * 刷新当前图像显示到面板中
     * @param image
     */
    private void updateImagePanel(BufferedImage image){
        this.setVisible(true);
        if(image!=null){
//        	bufferedImageWidth = bufferedImage.getWidth();
//        	bufferedImageHeight = bufferedImage.getHeight();
        	
        	imagePanel.removeAll();
        	
        	if(bigMultiple_mainView == 1){
        		imageLabel.setIcon(new ImageIcon(image));
    		}else{
    			BufferedImage b = bigImage(image, bigMultiple_mainView);
    			imageLabel.setIcon(new ImageIcon(b));
    		}
            
            imagePanel.add(new JScrollPane(imageLabel),BorderLayout.CENTER);
             
            SwingUtilities.updateComponentTreeUI(imagePanel); // 调整LookAndFeel（javax.swing）
        }
    }
    
    private class PicJLabel extends JLabel{
    	
    	@Override
    	public void paint(Graphics g) {
    		super.paint(g);
    		g.drawImage(bufferedImage, 0, 0,this);
//    		Graphics2D g2 = (Graphics2D) g;
//    		g2.setColor(Color.red);
//    		g2.drawRect(x,y,h,w);//x,y起点坐标，h高，w宽
    		g.setColor(Color.RED);
    		g.drawRect(3, 3, 30, 30);
    	}

    }
    
  //一个内部类,它表示一个面板,一个可以被放进tabpane的面板
    //也有自己的一套处理保存和复制的方法
    private class PicPanel extends JPanel implements ActionListener{
        JButton save,copy,close;//表示保存,复制,关闭的按钮
        BufferedImage get;//得到的图片
        public PicPanel(BufferedImage get){
            super(new BorderLayout());
            this.get=get;
            initPanel();
        }
        private void initPanel(){
            save=new JButton("保存(S)");
            copy=new JButton("复制到剪帖板(C)");
            close=new JButton("关闭(X)");
            save.setMnemonic('S');
            copy.setMnemonic('C');
            close.setMnemonic('X');
            JPanel buttonPanel=new JPanel();
            buttonPanel.add(copy);
            buttonPanel.add(save);
            buttonPanel.add(close);
            JLabel icon=new JLabel(new ImageIcon(get));
            this.add(new JScrollPane(icon),BorderLayout.CENTER);
            this.add(buttonPanel,BorderLayout.SOUTH);
            save.addActionListener(this);
            copy.addActionListener(this);
            close.addActionListener(this);
        }
        public void actionPerformed(ActionEvent e) {
            Object source=e.getSource();
            if(source==save){
//                doSave(get);
            }else if(source==copy){
//                doCopy(get);
            }else if(source==close){
                get=null;
//                doClose(this);
            }
        }
    }
    
  //一个临时类，用于显示当前的屏幕图像
    class Temp extends JPanel implements MouseListener, MouseMotionListener {

    	private BufferedImage bi;
    	private int width, height;
    	private int startX, startY, endX, endY, tempX, tempY;
    	private JFrame jf;
    	private Rectangle select = new Rectangle(0, 0, 0, 0);// 表示选中的区域
    	private Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);// 表示一般情况下的鼠标状态（十字线）
    	private States current = States.DEFAULT;// 表示当前的编辑状态
    	private Rectangle[] rec;// 表示八个编辑点的区域
    	// 下面四个常量,分别表示谁是被选中的那条线上的端点
    	public static final int START_X = 1;
    	public static final int START_Y = 2;
    	public static final int END_X = 3;
    	public static final int END_Y = 4;
    	private int currentX, currentY;// 当前被选中的X和Y,只有这两个需要改变
    	private Point p = new Point();// 当前鼠标移的地点
    	private boolean showTip = true;// 是否显示提示.如果鼠标左键一按,则提示就不再显示了

    	public Temp(JFrame jf, BufferedImage bi, int width, int height) {
    		this.jf = jf;
    		this.bi = bi;
    		this.width = width;
    		this.height = height;
    		this.addMouseListener(this);
    		this.addMouseMotionListener(this);
    		initRecs();
    	}

    	private void initRecs() {
    		rec = new Rectangle[8];
    		for (int i = 0; i < rec.length; i++) {
    			rec[i] = new Rectangle();
    		}
    	}

    	public void paintComponent(Graphics g) {
    		g.drawImage(bi, 0, 0, width, height, this);
    		g.setColor(Color.RED);
    		g.drawLine(startX, startY, endX, startY);
    		g.drawLine(startX, endY, endX, endY);
    		g.drawLine(startX, startY, startX, endY);
    		g.drawLine(endX, startY, endX, endY);
    		int x = startX < endX ? startX : endX;
    		int y = startY < endY ? startY : endY;
    		select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY
    				- startY));
    		int x1 = (startX + endX) / 2;
    		int y1 = (startY + endY) / 2;
    		g.fillRect(x1 - 2, startY - 2, 5, 5);
    		g.fillRect(x1 - 2, endY - 2, 5, 5);
    		g.fillRect(startX - 2, y1 - 2, 5, 5);
    		g.fillRect(endX - 2, y1 - 2, 5, 5);
    		g.fillRect(startX - 2, startY - 2, 5, 5);
    		g.fillRect(startX - 2, endY - 2, 5, 5);
    		g.fillRect(endX - 2, startY - 2, 5, 5);
    		g.fillRect(endX - 2, endY - 2, 5, 5);
    		rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
    		rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
    		rec[2] = new Rectangle((startX > endX ? startX : endX) - 5, y - 5, 10,
    				10);
    		rec[3] = new Rectangle((startX > endX ? startX : endX) - 5, y1 - 5, 10,
    				10);
    		rec[4] = new Rectangle((startX > endX ? startX : endX) - 5,
    				(startY > endY ? startY : endY) - 5, 10, 10);
    		rec[5] = new Rectangle(x1 - 5, (startY > endY ? startY : endY) - 5, 10,
    				10);
    		rec[6] = new Rectangle(x - 5, (startY > endY ? startY : endY) - 5, 10,
    				10);
    		rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);
    		if (showTip) {
//    			g.setColor(Color.CYAN);
//    			g.fillRect(p.x, p.y, 170, 40);
    			g.setColor(Color.RED);
//    			g.drawRect(p.x, p.y, 170, 40);
//    			g.setColor(Color.BLACK);
    			g.drawString("按住鼠标左键拖动截图", p.x, p.y + 15);
    			g.drawString("双击截图区域完成截图", p.x, p.y + 30);
    		}
    	}

    	// 根据东南西北等八个方向决定选中的要修改的X和Y的座标
    	private void initSelect(States state) {
    		switch (state) {
    		case DEFAULT:
    			currentX = 0;
    			currentY = 0;
    			break;
    		case EAST:
    			currentX = (endX > startX ? END_X : START_X);
    			currentY = 0;
    			break;
    		case WEST:
    			currentX = (endX > startX ? START_X : END_X);
    			currentY = 0;
    			break;
    		case NORTH:
    			currentX = 0;
    			currentY = (startY > endY ? END_Y : START_Y);
    			break;
    		case SOUTH:
    			currentX = 0;
    			currentY = (startY > endY ? START_Y : END_Y);
    			break;
    		case NORTH_EAST:
    			currentY = (startY > endY ? END_Y : START_Y);
    			currentX = (endX > startX ? END_X : START_X);
    			break;
    		case NORTH_WEST:
    			currentY = (startY > endY ? END_Y : START_Y);
    			currentX = (endX > startX ? START_X : END_X);
    			break;
    		case SOUTH_EAST:
    			currentY = (startY > endY ? START_Y : END_Y);
    			currentX = (endX > startX ? END_X : START_X);
    			break;
    		case SOUTH_WEST:
    			currentY = (startY > endY ? START_Y : END_Y);
    			currentX = (endX > startX ? START_X : END_X);
    			break;
    		default:
    			currentX = 0;
    			currentY = 0;
    			break;
    		}
    	}

    	public void mouseMoved(MouseEvent me) {
    		doMouseMoved(me);
    		initSelect(current); // current：当前状态（state）
    		if (showTip) {
    			p = me.getPoint();
    			repaint();
    		}
    	}

    	// 特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的区域
    	private void doMouseMoved(MouseEvent me) {
    		if (select.contains(me.getPoint())) {
    			this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
    			current = States.MOVE;
    		} else {
    			States[] st = States.values();
    			for (int i = 0; i < rec.length; i++) {
    				if (rec[i].contains(me.getPoint())) {
    					current = st[i];
    					this.setCursor(st[i].getCursor());
    					return;
    				}
    			}
    			this.setCursor(cs);
    			current = States.DEFAULT;
    		}
    	}

    	public void mouseExited(MouseEvent me) {
    	}

    	public void mouseEntered(MouseEvent me) {
    	}

    	public void mouseDragged(MouseEvent me) {
    		int x = me.getX();
    		int y = me.getY();
    		// 分别处理一系列的（光标）状态（枚举值）
    		if (current == States.MOVE) {
    			startX += (x - tempX);
    			startY += (y - tempY);
    			endX += (x - tempX);
    			endY += (y - tempY);
    			tempX = x;
    			tempY = y;
    		} else if (current == States.EAST || current == States.WEST) {
    			if (currentX == START_X) {
    				startX += (x - tempX);
    				tempX = x;
    			} else {
    				endX += (x - tempX);
    				tempX = x;
    			}
    		} else if (current == States.NORTH || current == States.SOUTH) {
    			if (currentY == START_Y) {
    				startY += (y - tempY);
    				tempY = y;
    			} else {
    				endY += (y - tempY);
    				tempY = y;
    			}
    		} else if (current == States.NORTH_EAST || current == States.NORTH_EAST
    				|| current == States.SOUTH_EAST || current == States.SOUTH_WEST) {
    			if (currentY == START_Y) {
    				startY += (y - tempY);
    				tempY = y;
    			} else {
    				endY += (y - tempY);
    				tempY = y;
    			}
    			if (currentX == START_X) {
    				startX += (x - tempX);
    				tempX = x;
    			} else {
    				endX += (x - tempX);
    				tempX = x;
    			}
    		} else {
    			startX = tempX;
    			startY = tempY;
    			endX = me.getX();
    			endY = me.getY();
    		}
    		this.repaint();
    	}

    	public void mousePressed(MouseEvent me) {
    		showTip = false;
    		tempX = me.getX();
    		tempY = me.getY();
    	}

    	public void mouseReleased(MouseEvent me) {
    		if (me.isPopupTrigger()) { // 右键
    			if (current == States.MOVE) {
    				showTip = true;
    				p = me.getPoint();
    				startX = 0;
    				startY = 0;
    				endX = 0;
    				endY = 0;
    				repaint();
    			} else { // 普通情况
    				jf.dispose();
    				updateImagePanel(bufferedImage);
    			}
    		}
    	}

    	public void mouseClicked(MouseEvent me) {
    		if (me.getClickCount() == 2) {
    			// Rectangle rec=new
    			// Rectangle(startX,startY,Math.abs(endX-startX),Math.abs(endY-startY));
    			Point p = me.getPoint();
    			if (select.contains(p)) {
    				if (select.x + select.width < this.getWidth()
    						&& select.y + select.height < this.getHeight()) {
    					bufferedImage = bi.getSubimage(select.x, select.y, select.width,
    							select.height);
    					jf.dispose();
    					updateImagePanel(bufferedImage);
    				} else {
    					int wid = select.width, het = select.height;
    					if (select.x + select.width >= this.getWidth()) {
    						wid = this.getWidth() - select.x;
    					}
    					if (select.y + select.height >= this.getHeight()) {
    						het = this.getHeight() - select.y;
    					}
    					bufferedImage = bi.getSubimage(select.x, select.y, wid, het);
    					jf.dispose();
    					updateImagePanel(bufferedImage);
    				}
    			}
    		}
    	}
    }

    //一些表示状态的枚举
    enum States{
      NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)),//表示西北角
      NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)),
      NORTH_EAST(new Cursor(Cursor.NE_RESIZE_CURSOR)),
      EAST(new Cursor(Cursor.E_RESIZE_CURSOR)),
      SOUTH_EAST(new Cursor(Cursor.SE_RESIZE_CURSOR)),
      SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
      SOUTH_WEST(new Cursor(Cursor.SW_RESIZE_CURSOR)),
      WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
      MOVE(new Cursor(Cursor.MOVE_CURSOR)),
      DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR));
      
      private Cursor cs;
      
      States(Cursor cs){
          this.cs=cs;
      }
      
      public Cursor getCursor(){
          return cs;
      }
    }
	public JPanel getImagePanel() {
		return imagePanel;
	}
	public JLabel getImageLabel() {
		return imageLabel;
	}
	public JTextField getPositionTextview() {
		return positionTextview;
	}
	public JLabel getBigImageLabel() {
		return bigImageLabel;
	}
	public JPanel getBigImagePanel() {
		return bigImagePanel;
	}
	public JSlider getImageSizeSlider() {
		return imageSizeSlider;
	}
	
	/**
	 * 初始化图像放大倍数，还原为1
	 */
	private void setImageSizeInit(){
		imageSizeSlider.setValue(1);
		bigMultiple_mainView = 1;
	}
	public JTextField getPxColorTextField() {
		return pxColorTextField;
	}
	public JLabel getSelectImageSizeLabel() {
		return selectImageSizeLabel;
	}
}

