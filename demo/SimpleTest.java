import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.SystemUtil;
/**
 * 简单使用DEMO
 * @author 管雷鸣
 *
 */
public class SimpleTest {
	
	public static void main(String[] args) {

		//所有辅助的，模拟进行某种操作(键盘、鼠标、..)要先创建此类,在new Com()时，会自动检测运行环境是否符合、部署、注册Dll
		Com com=new Com();
		
		//返回创建Com()的结果，如果自检过程中发现异常，创建Com失败，则调用此会返回false
		if(com.isCreateSuccess()){
			
			/*
			 * 这里是执行的主体了~~~~~写你想要做的事吧
			 */
			SystemUtil systemUtil=new SystemUtil(com.getActiveXComponent());
			systemUtil.beep(2000, 1000);	//蜂鸣器发音
			System.out.println("系统开机到现在运行的时间是："+systemUtil.getSystemRunTime()/1000+"秒");
			
		}else{
			System.out.println("创建Com对象失败");
		}
		
		//用完后一定要记得释放，释放内存
		com.unbind();
	}
}
