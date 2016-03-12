import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import com.xnx3.microsoft.SystemUtil;

/**
 * 界面-托盘DEMO
 * 【当前class目录下放上icon.png作为托盘图标】
 * 调试时右键托盘菜单会乱码，不用在意，创建运行jar文件后会恢复正常
 * @author 管雷鸣
 *
 */
public class TrayDemo {
	static TrayIcon tray;
	
	public static void main(String[] args) {
		
		/********创建界面********/
		JFrame jframe=new JFrame("演示界面及托盘创建");
		jframe.setBounds(100, 100, 300, 300);
		jframe.setVisible(true);
		JButton button=new JButton("弹出托盘提示");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tray.displayMessage("管雷鸣提示您", "点击按钮了", MessageType.INFO);
			}
		});
		jframe.add(button);
		
		
		/************创建托盘************/
		//添加右键弹出按钮
		PopupMenu popupMenu=new PopupMenu();
		java.awt.MenuItem menuItemExit=new java.awt.MenuItem("Exit");	//退出按钮
		java.awt.MenuItem menuItemAbout=new java.awt.MenuItem("About");	//关于按钮
		menuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				com.xnx3.Lang.showMessageDialog("管爷出品<br/>必属精品");
			}
		});
		popupMenu.add(menuItemExit);
		popupMenu.add(menuItemAbout);
		
		//创建托盘，可以对tray创建监听事件
		tray=SystemUtil.createTray(TrayDemo.class.getResource("/icon.png"), "显示文字", popupMenu);
		
		//创建托盘完毕后，拿到的tray对象可以进行在创建的托盘上弹出文字提示
		tray.displayMessage("标题", "托盘创建完毕", MessageType.INFO);
		
	}
}
