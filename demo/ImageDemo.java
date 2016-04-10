
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;

import com.xnx3.UI;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 图片搜索Demo， {@link Robot} 使用示例
 * @author 管雷鸣
 *
 */
public class ImageDemo extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageDemo frame = new ImageDemo();
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
	public ImageDemo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton button = new JButton("资源库中两张图片搜索");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageSearchBySource();
			}
		});
		
		JButton btnNewButton = new JButton("搜索当前屏幕上的图片");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageSearchByScreen();
			}
		});
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		
		JButton btnNewButton_1 = new JButton("<html>当前屏幕上搜索\n<br/>直到搜索到目标图片\n<br/>出现后，鼠标右键单击");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageSearchAndMouseRightClick();
				
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
							.addComponent(button, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
							.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addGap(103))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	public JTextArea getTextArea() {
		return textArea;
	}
	
	
	

	/**
	 * 两个在本地的图片对比
	 */
	public void imageSearchBySource(){
		Robot robot = new Robot();
		robot.setSourcePath(ImageDemo.class);
		List<CoordBean> list = robot.imageSearch(robot.getResourceImage("source.png"), robot.getResourceImage("search.png"), Robot.SIM_ACCURATE);
		addLog("imageSearchBySource搜索到了"+list.size()+"个图片");
		for (int i = 0; i < list.size(); i++) {
			CoordBean coord = list.get(i);
			addLog("第"+(i+1)+"个图片坐标："+coord.getX()+","+coord.getY());
		}
	}
	
	/**
	 * 在当前屏幕上搜索指定的图片
	 */
	public void imageSearchByScreen(){
		Robot robot = new Robot();
		robot.setSourcePath(ImageDemo.class);
		List<CoordBean> list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, "search.png", Robot.SIM_ACCURATE);
		addLog("imageSearchByScreen搜索到了"+list.size()+"个图片");
		
		for (int i = 0; i < list.size(); i++) {
			CoordBean coord = list.get(i);
			addLog("第"+(i+1)+"个图片坐标："+coord.getX()+","+coord.getY());
		}
	}
	
	/**
	 * 屏幕上搜索指定的图，直到图出现后，右键点击图片
	 */
	public void imageSearchAndMouseRightClick(){
		Robot robot = new Robot();
		robot.setSourcePath(ImageDemo.class);
		robot.imageDelaySearch(0, 0, 800, 600, robot.getResourceImage("search.png"), Robot.SIM_ACCURATE, 10000);
		List<CoordBean> list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, "search.png", Robot.SIM_ACCURATE);
		System.out.println("搜索到了"+list.size()+"个图片");
		addLog("imageSearchByScreen搜索到了"+list.size()+"个图片");
		
		for (int i = 0; i < list.size(); i++) {
			CoordBean coord = list.get(i);
			addLog("第"+(i+1)+"个图片坐标："+coord.getX()+","+coord.getY());
		}
	}
	
	
	/**
	 * 日志纪录
	 * @param str
	 */
	public void addLog(String str){
		System.out.println(str);
		this.textArea.setText(str+"\n"+this.textArea.getText());
	}
}
