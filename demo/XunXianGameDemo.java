import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.Window; 

/**
 * 寻仙游戏模拟操作DEMO
 * 如果你没开寻仙游戏的话，不会有什么效果，代码仅供参考
 * @author 管雷鸣
 *
 */
public class XunXianGameDemo {
	public static void main(String[] args) {
		
		//所有辅助的，模拟进行某种操作(键盘、鼠标、..)要先创建此类,在new Com()时，会自动检测运行环境是否符合、部署、注册Dll
		Com com=new Com();
		
		//返回创建Com()的结果，如果自检过程中发现异常，创建Com失败，则调用此会返回false
		if(!com.isCreateSuccess()){
			return;
		}
		
		//创建window窗口操作对象
		Window window=new Window(com.getActiveXComponent());
		
		//查找当前运行的程序中标题包含“寻仙 -”三个字的窗口句柄，
		int hwnd=window.findWindow(0, null, "寻仙 -");		
		//如果找到了寻仙这个游戏窗口了，确定是有这个程序存在，那么可以继续以下操作
		if(hwnd>0){
			
			/*
			 * 对找到的寻仙这个窗口进行绑定，绑定完毕后，那么所有的鼠标、键盘、图色等操作就是都是对这个窗口（寻仙游戏）操作的，完全后台的~~，窗口可以被遮挡、点击葫芦隐藏，但是不可以最小化
			 * 寻仙的就是这种模式绑定，只需要传入窗口句柄就可
			 * 其他游戏的请自行组合测试绑定模式,使用： com.bind(hwnd, display, mouse, key, mode) 自行测试
			 */
			if(com.bind(hwnd)){
				
				/**
				 * 绑定完毕，对游戏的操作全在这里
				 */
				Press press=new Press(com.getActiveXComponent());	//创建模拟按键对象
				Mouse mouse=new Mouse(com.getActiveXComponent());	//创建模拟鼠标对象
				Sleep sleep=new Sleep();							//创建延迟等待对象
				
				/***************以下来模拟按wasd四个方向键转三圈，然后结束软件退出运行***************/
				int i=0;
				while(i++<3){
					press.keyPressTime(Press.W, 300);	//模拟按下W键0.3秒钟
					press.keyPressTime(Press.A, 300);
					press.keyPressTime(Press.S, 300);
					press.keyPressTime(Press.D, 300);
				}
				
				System.out.println("执行完毕");
				
			}else{
				System.out.println("窗口绑定失败");
			}
		}else{
			System.out.println("没有发现寻仙窗口");
		}
		
		//用完后一定要记得释放，释放内存，无论是否绑定了窗口、绑定是否成功，都可以直接调用此函数释放
		com.unbind();
	}
}