import javax.swing.JFrame;
import com.xnx3.UI;

/**
 * 使用外观包美化界面
 * @author 管雷鸣
 */
public class UITest {
	public static void main(String[] args) {
		
		//设置界面使用外观包,在界面创建之前使用
		new UI().UseLookAndFeelBySubstance();
		
		//正常Swing界面
		JFrame jframe=new JFrame("java.xnx3.com");
		jframe.setBounds(100,100, 300, 200);
		jframe.setVisible(true);
	}
}
