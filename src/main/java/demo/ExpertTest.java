package demo;

import com.xnx3.Lang;
import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.File;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.SystemUtil;
import com.xnx3.microsoft.Window;

/**
 * 前台辅助模拟操作案例
 * @author 管雷鸣
 *
 */
public class ExpertTest {
	
	public static void main(String[] args) {

		//所有辅助的，模拟进行某种操作(键盘、鼠标、..)要先创建此类,在new Com()时，会自动检测运行环境是否符合、部署、注册Dll
		Com com=new Com();
		
		//返回创建Com()的结果，如果自检过程中发现异常，创建Com失败，则调用此会返回false
		if(com.isCreateSuccess()){
			
			/*
			 * 这里是执行的主体了~~~~~写你想要做的事吧
			 */
			Sleep sleep=new Sleep();	//延迟等待类
			Window window=new Window(com.getActiveXComponent());	//窗口操作类
			SystemUtil systemUtil=new SystemUtil(com.getActiveXComponent());	//系统操作类
			
			new Thread(new Runnable() {
				public void run() {
					//打开记事本,此函数会阻塞当前线程，直到打开的关闭为止。故而须另开辟一个线程执行此函数
					SystemUtil.cmd("notepad");	
				}
			}).start();
			
			//延迟等待1秒，以便记事本线程打开
			sleep.sleep(1000);
			
			//获取鼠标所指向的窗口句柄
			int hwnd=window.findWindow(0, null, "记事本");
			if(hwnd>0){	
				//如果找到了记事本这个窗口了，确定是有这个程序存在，那么可以继续以下操作,因为没有试用Com.bind所以操作的只是前台我们看到的，模拟我们的操作
				
				Mouse mouse=new Mouse(com.getActiveXComponent());	//鼠标模拟操作类
				Press press=new Press(com.getActiveXComponent());	//键盘模拟操作类
				Color color=new Color(com.getActiveXComponent());	//颜色相关的取色、判断类
				File file=new File(com.getActiveXComponent());		//文件相关的操作类，如截图等
				
				window.setWindowSize(hwnd, 400, 400);	//设置记事本窗口的大小
				window.setWindowTransparent(hwnd, 200);	//设置窗口的透明度
				window.setWindowActivate(hwnd);	//激活窗口
				
				//模拟按键
				press.keyPress(Press.H);
				press.keyPress(Press.I);
				
				//延迟等待2秒钟
				sleep.sleep(2000);
				
				if(Lang.showConfirmDialog("要销毁这个记事本程序吗？")==Lang.CONFIRM_YES){
					//根据窗口句柄，来关闭这个窗口
					window.setWindowDestroy(hwnd);
				}else{
					window.setWindowTitle(hwnd, "你自己看着办吧！");	//改记事本的标题
				}
				
			}else{
				Lang.showMessageDialog("没有发现记事本窗口，难道记事本没有打开？<br/>还是被你关闭了?");
			}
			
			systemUtil.beep(2000, 1000);	//蜂鸣器发音
			
		}else{
			System.out.println("创建Com对象失败");
		}
		
		//用完后一定要记得释放，释放内存
		com.unbind();
	}
}
