
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

/**
 * 定义系统级热键
 * 	 优点: 定义的快捷键在窗口没有焦点时也能执行.
 * 	 缺点: 限制操作系统win,没有取消注册事件前默认事件被覆盖
 * 		如:KEY_ERROR_DEMO 注册的是ctrl+a,那么全选功能被覆盖,
 * 			要到取消注册时才能使用全选,尽量绕过系统快捷键.
 * @author QQ群418768360 脱线的苦力怕
 */
public class SysHotKey implements HotkeyListener {
    private static final int KEY_SHOW_JFRAME = 88;
    private static final int KEY_EXIT_JFRAME = 89;
    private static final int KEY_ERROR_DEMO = 90;
    static JFrame frame = null;
 
    public void onHotKey(int key) {
        switch (key) {
            case KEY_SHOW_JFRAME:
            	if(frame.getState() != Frame.ICONIFIED){
            		frame.setExtendedState(Frame.ICONIFIED);
            		frame.setVisible(false);
            	}else{
            		frame.setExtendedState(Frame.NORMAL);
            		frame.setVisible(true);
            	}
            	String fromState = (frame.getState()==0)?"最大化":"最小化";
            	System.out.println("失去焦点,的快捷键注册win+a\t现在窗口"+fromState);
            	break;
            case KEY_EXIT_JFRAME:
            	System.out.println("关闭注册,关闭程序win+s");
            	destroy();
            	break;
            case KEY_ERROR_DEMO:
            	System.out.println("我是错误示范,我占用了ctrl+a全选功能");
            	break;
        }

    }
    /**
     * 解除注册
     * */
    void destroy() {
        JIntellitype.getInstance().unregisterHotKey(KEY_SHOW_JFRAME);
        JIntellitype.getInstance().unregisterHotKey(KEY_EXIT_JFRAME);
        JIntellitype.getInstance().unregisterHotKey(KEY_ERROR_DEMO);
        
        System.exit(0);
    }
    /**
     * 开启注册
     * */
    void initHotkey() {
        JIntellitype.getInstance().registerHotKey(KEY_SHOW_JFRAME, JIntellitype.MOD_WIN ,(int) 'A');
        JIntellitype.getInstance().registerHotKey(KEY_EXIT_JFRAME, JIntellitype.MOD_WIN ,(int) 'S');
        JIntellitype.getInstance().registerHotKey(KEY_ERROR_DEMO, JIntellitype.MOD_CONTROL ,(int) 'A');
        JIntellitype.getInstance().addHotKeyListener(this);
    }

    public static void main(String[] args) {
        SysHotKey key = new SysHotKey();
        key.initHotkey();

        frame = new JFrame();
        frame.getContentPane().add(new JLabel("Just a test.")); 
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        Dimension di =  Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(di.width/4 , di.height/4); 
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }
}