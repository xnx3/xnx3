package com.xnx3.microsoft;

import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.xnx3.Lang;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;
import com.xnx3.swing.TrayUtil;

/**
 * Windows系统的一些常用操作
 * @author 管雷鸣
 */
public class SystemUtil extends Lang {
	private ActiveXComponent activeDm;
	private ActiveXComponent activePlugin365;
	private Log log;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public SystemUtil(ActiveBean activeBean) {
		this.activeDm=activeBean.getDm();
		this.activePlugin365=activeBean.getPlugin365();
		this.log=new Log();
	}
	
	/**
	 * 蜂鸣器，进行发音蜂鸣
	 * @param frequency 频率，50到10000之间便可，数值越大越尖越刺耳，一般情况500就可
	 * @param time 蜂鸣时间，单位为毫秒
	 * @return true:成功
	 */
	public boolean beep(int frequency,int time){
		Variant[] var=new Variant[2];
		int result=0;
		try {
			var[0]=new Variant(frequency);
			var[1]=new Variant(time);
			result=this.activeDm.invoke("Beep",var).getInt();
		} catch (Exception e) {
			log.debug(this, "beep", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	
	/**
	 * 退出系统(注销 重启 关机) 可使用：  {@link SystemUtil#exitSystemByRestart()} {@link SystemUtil#exitSystemByLogout()} {@link SystemUtil#exitSystemByShutdown()}
	 * @param type 类型: <li>0 : 注销系统
	 * 					<li>1 : 关机
	 * 					<li>2 : 重新启动
	 * 
	 * @return true:执行成功
	 */
	public boolean exitSystem(int type){
		Variant[] var=new Variant[1];
		int result=0;
		try {
			var[0]=new Variant(type);
			result=this.activeDm.invoke("ExitOs",var).getInt();
		} catch (Exception e) {
			log.debug(this, "exitSystem", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	
	/**
	 * 系统注销系统
	 * @see SystemUtil#exitSystem(int)
	 * @return true:执行成功
	 */
	public boolean exitSystemByLogout(){
		return exitSystem(0);
	}
	
	/**
	 * 系统重新启动
	 * @see SystemUtil#exitSystem(int)
	 * @return true:执行成功
	 */
	public boolean exitSystemByRestart(){
		return exitSystem(2);
	}
	
	/**
	 * 系统关机
	 * @see SystemUtil#exitSystem(int)
	 * @return true:执行成功
	 */
	public boolean exitSystemByShutdown(){
		return exitSystem(1);
	}
	
	/**
	 * 得到系统的路径，可直接使用： {@link SystemUtil#getDirForProcess()}  {@link SystemUtil#getDirForSystem32()}  {@link SystemUtil#getDirForTemp()}  {@link SystemUtil#getDirForWindow()} 
	 * @param type 类型，获取哪种<li>0 : 获取当前路径
	 * 							<li>1 : 获取系统路径(system32路径)
	 * 							<li>2 : 获取windows路径(windows所在路径)
	 * 							<li>3 : 获取临时目录路径(temp)
	 * 							<li>4 : 获取当前进程(exe)所在的路径
	 * @return 返回路径 ，失败返回null
	 */
	public String getDir(int type){
		Variant[] var=new Variant[1];
		String result=null;
		try {
			var[0]=new Variant(type);
			result=this.activeDm.invoke("GetDir",var).getString();
		} catch (Exception e) {
			log.debug(this, "getDir", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result; 
	}
	
	/**
	 * 获取临时目录路径(temp)
	 * @see SystemUtil#getDir(int)
	 * @return 返回如： "C:\Users\ADMINI~1\AppData\Local\Temp\"
	 */
	public String getDirForTemp(){
		return getDir(3);
	}

	/**
	 * 获取当前进程(exe)所在的路径,也就是jre虚拟机的bin路径(java.exe)
	 * @see SystemUtil#getDir(int)
	 * @return 返回如： "E:\仙人辅助\jre7\bin"
	 */
	public String getDirForProcess(){
		return getDir(4);
	}
	
	/**
	 * 获取本机的硬盘序列号.支持ide scsi硬盘. 要求调用进程必须有管理员权限. 否则返回null
	 * @return 硬盘序列号，无管理员权限或者失败返回null
	 */
	public String getDiskSerial(){
		String result=null;
		try {
			result=this.activeDm.invoke("GetDiskSerial").getString();
			if(result==null||result.length()==0){
				result=null;
				log.debug(this, "getDiskSerial", "获取本机的硬盘序列号必须要有管理员权限！");
			}
		} catch (Exception e) {
			log.debug(this, "getDiskSerial", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取屏幕的色深(16、32...)
	 * @return 返回系统颜色深度.(16或者32等)
	 */
	public int getScreenDepth(){
		int result=0;
		try {
			result=this.activeDm.invoke("GetScreenDepth").getInt();
		} catch (Exception e) {
			log.debug(this, "getScreenDepth", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}


	/**
	 * 获取屏幕的高度. 如1024*768这里的768
	 * @return 高度
	 */
	public int getScreenHeight(){
		int result=0;
		try {
			result=this.activeDm.invoke("GetScreenHeight").getInt();
		} catch (Exception e) {
			log.debug(this, "getScreenHeight", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}

	/**
	 * 获取屏幕的宽度. 如1024*768这里的1024
	 * @return 宽度
	 */
	public int getScreenWidth(){
		int result=0;
		try {
			result=this.activeDm.invoke("GetScreenWidth").getInt();
		} catch (Exception e) {
			log.debug(this, "getScreenWidth", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}

	/**
	 * 获取当前系统从开机到现在所经历过的时间，单位是毫秒
	 * @return 单位是毫秒
	 */
	public int getSystemRunTime(){
		int result=0;
		try {
			result=this.activeDm.invoke("GetTime").getInt();
		} catch (Exception e) {
			log.debug(this, "getSystemRunTime", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}

	
	/**
	 * 获取windows路径(windows所在路径)
	 * @see SystemUtil#getDir(int)
	 * @return 返回如： "C:\Windows"
	 */
	public String getDirForWindow(){
		return getDir(2);
	}

	/**
	 * 获取系统路径(system32路径)
	 * @see SystemUtil#getDir(int)
	 * @return 返回如： "C:\Windows\system32"
	 */
	public String getDirForSystem32(){
		return getDir(1);
	}
	
	/**
	 * 设置系统的分辨率、系统色深
	 * @param width 屏幕分辨率宽度
	 * @param height 屏幕分辨率高度
	 * @param depth 系统色深
	 * @return true:成功
	 */
	public boolean setScreen(int width,int height,int depth){
		Variant[] var=new Variant[3];
		int result=0;
		try {
			var[0]=new Variant(width);
			var[1]=new Variant(height);
			var[2]=new Variant(depth);
			result=this.activeDm.invoke("SetScreen",var).getInt();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this, "setScreen", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	
	/**
	 * 设置系统的色深(16、32...)
	 * @param depth 16、32...
	 * @return true:成功
	 */
	public boolean setScreenDepth(int depth){
		return setScreen(getScreenWidth(), getScreenHeight(), depth);
	}
	
	/**
	 * 设置屏幕分辨率
	 * @param width 分辨率的宽度，如1024*768的1024
	 * @param height 分辨率的高度，如1024*768的768
	 * @return true:成功
	 */
	public boolean setScreenWidth(int width,int height){
		return setScreen(width, height, getScreenDepth());
	}
	
	/**
	 * 检测当前系统是否有开启UAC(用户账户控制).
	 * <li>注: 只有WIN7 VISTA WIN2008以及以上系统才有UAC设置
	 * @return <li>true:已开启UAC
	 * 			<li>false:未开启UAC
	 */
	public boolean getUAC(){
		int result=0;
		try {
			result=this.activeDm.invoke("CheckUAC").getInt();
		} catch (Exception e) {
			log.debug(this, "getUAC", "异常捕获:"+e.getMessage());
		}
		
		return result==1;
	}
	
	/**
	 * 设置当前系统的UAC(用户账户控制).
	 * <li>注: 只有WIN7 VISTA WIN2008以及以上系统才有UAC设置. 关闭UAC以后，必须重启系统才会生效.
	 * <br/>如果关闭了UAC，那么默认启动所有应用程序都是管理员权限，就不会再发生绑定失败这样的尴尬情况了.
	 * @param use <li>true:开启
	 * 				<li>false:关闭
	 * @return true:成功
	 */
	public boolean setUAC(boolean use){
		int result=0;
		
		int uac=0;
		if(use){
			uac=1;
		}
		try {
			result=this.activeDm.invoke("SetUAC",uac).getInt();
		} catch (Exception e) {
			log.debug(this, "setUAC", "异常捕获:"+e.getMessage());
		}
		
		return result==1;
	}
	
	
	/**
	 * 获取当前程序的pid（任务管理器里的pid）
	 * @return 成功，则返回该pid，若是失败、出错异常，则返回-1
	 */
    public static int getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format: "pid@hostname"
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1;
        }
    }
    
	/**
	 * 开始-运行,运行cmd命令
	 * <li>注意，此函数会阻塞当前线程，如，notepad打开一个记事本，会阻塞，直到打开的记事本关闭为止才会释放。
	 * @param command 要运行的cmd命令，如  ping www.xnx3.com
	 * @return 执行的结果，若是出错则返回出错的原因
	 */
    public static String cmd(String command){
        Process process;
		try {
			process = Runtime.getRuntime().exec( "cmd.exe /c "+command);
			InputStream inputStream = process.getInputStream(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); 

			// 取得输出流
			StringBuffer str=new StringBuffer();
			String line = ""; 
			while ((line = reader.readLine()) != null) { 
				str.append(line+"\n");
			}
			
			reader.close(); 
			process.destroy();
			
			return str.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
    /**
     * 注册dll文件进系统，注册成功时会有类似js的alert弹出框提示成功
     * @param dllFilePath dll文件的绝对路径，如 C:\\xnx3.dll（如果有目录，会注册失败，建议将dll直接放到C盘根目录进行注册）
     */
    public static void registerDll(String dllFilePath){
    	try {
			Runtime.getRuntime().exec("cmd.exe /c regsvr32 "+dllFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 调用本地默认的浏览器打开指定网址
     * @param url 打开的网址
     */
    public static void openUrl(String url){
    	try {
			Runtime.getRuntime().exec( "cmd.exe /c start iexplore " + url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
    /**
     * 获取计算机名，在我的电脑-右键-属性，那里显示的计算机名
     * @return String，若没有获取到返回null
     */
    public static String getComputerName(){
    	try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 运行某个文件，如同鼠标双击运行
     * @param path 文件所在的绝对路径，如：C:/t.mp3
     */
    public static void run(String path){
		try {
			Runtime.getRuntime().exec("cmd  /c  start  "  +  path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
    
    /**
     * 枚举系统当前运行的进程列表，效果同任务管理器
     * @return 进程列表 效果如：<a href="http://www.xnx3.com/doc/j2se_util/20150302/130.html">http://www.xnx3.com/doc/j2se_util/20150302/130.html</a>
     * 					<li>若失败返回null
     */
	public String enumProcess(){
		String result=null;
		try {
			result=this.activePlugin365.invoke("EnumProcess").getString();
		} catch (Exception e) {
			log.debug(this, "enumProcess", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 得到进程优先级
	 * @param hwnd 窗口句柄
	 * @return  <li>0 : 获取失败
	 * 			<li>1 : 低
	 * 			<li>2 : 低于标准
	 * 			<li>3 : 标准
	 * 			<li>4 : 高于标准
	 * 			<li>5 : 高
	 * 			<li>6 : 实时
	 */
	public int getProcessPriority(int hwnd){
		int result=0;
		try {
			result=this.activePlugin365.invoke("GetProcessPriority",hwnd).getInt();
		} catch (Exception e) {
			log.debug(this, "getProcessPriority", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 设置进程优先级
	 * @param hwnd 窗口句柄
	 * @param mode <li>1 : 低
	 * 			<li>2 : 低于标准
	 * 			<li>3 : 标准
	 * 			<li>4 : 高于标准
	 * 			<li>5 : 高
	 * 			<li>6 : 实时
	 * @return 成功|失败
	 */
	public boolean setProcessPriority(int hwnd,int mode){
		boolean result=true;
		try {
			this.activePlugin365.invoke("setProcessPriority",hwnd,mode);
		} catch (Exception e) {
			result=false;
			log.debug(this, "setProcessPriority", "异常捕获:"+e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 将某个窗口装载到托盘(创建托盘，只是生成一个托盘，鼠标放上显示title文字，托盘图标为运行程序的图标,也就是java的图标)
	 * @param hwnd 窗口句柄
	 * @param title 鼠标放上显示的文字
	 * @return 成功|失败
	 */
	public boolean addTray(int hwnd,String title){
		boolean result=true;
		
		Variant[] var=new Variant[2];
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(title);
			this.activePlugin365.invoke("AddTray",var);
		} catch (Exception e) {
			result=false;
			e.printStackTrace();
			log.debug(this, "addTray", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result;
	}
	
	/**
	 * 弹出托盘气泡
	 * <li>要有托盘才能弹出气泡，可使用 {@link #addTray(int, String)} 创建一个托盘
	 * @param hwnd 窗口句柄
	 * @param title 气泡标题
	 * @param message 气泡信息的内容
	 * @param mode 气泡显示模式，分为以下：
	 * 				<li>0 : 没有图标
	 * 				<li>1 : 信息图标
	 * 				<li>2 : 警告图标
	 * 				<li>3 : 错误图标
	 * 				<li>4 : 程序图标
	 * @return 成功|失败
	 */
	public boolean showTipTray(int hwnd,String title,String message,int mode){
		boolean result=true;
		
		Variant[] var=new Variant[4];
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(message);
			var[2]=new Variant(title);
			var[3]=new Variant(mode);
			this.activePlugin365.invoke("TipTray",var);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this, "showTipTray", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result;
	}
	
	/**
	 * 弹出一个没有图标的托盘气泡
	 * <li>要有托盘才能弹出气泡，可使用 {@link #addTray(int, String)} 创建一个托盘
	 * @param hwnd 窗口句柄
	 * @param title 气泡标题
	 * @param message 气泡信息的内容
	 * @return 成功|失败
	 * @see #showTipTray(int, String, String, int)
	 */
	public boolean showTipTrayForNoIcon(int hwnd,String title,String message){
		return showTipTray(hwnd, title, message, 0);
	}
	

	/**
	 * 弹出一个带有信息提示图标的托盘气泡
	 * <li>要有托盘才能弹出气泡，可使用 {@link #addTray(int, String)} 创建一个托盘
	 * @param hwnd 窗口句柄
	 * @param title 气泡标题
	 * @param message 气泡信息的内容
	 * @return 成功|失败
	 * @see #showTipTray(int, String, String, int)
	 */
	public boolean showTipTrayForInfo(int hwnd,String title,String message){
		return showTipTray(hwnd, title, message, 1);
	}
	
	/**
	 * 弹出一个带有警告提示图标的托盘气泡
	 * <li>要有托盘才能弹出气泡，可使用 {@link #addTray(int, String)} 创建一个托盘
	 * @param hwnd 窗口句柄
	 * @param title 气泡标题
	 * @param message 气泡信息的内容
	 * @return 成功|失败
	 * @see #showTipTray(int, String, String, int)
	 */
	public boolean showTipTrayForWarn(int hwnd,String title,String message){
		return showTipTray(hwnd, title, message, 2);
	}
	
	/**
	 * 弹出一个带有错误提示图标的托盘气泡
	 * <li>要有托盘才能弹出气泡，可使用 {@link #addTray(int, String)} 创建一个托盘
	 * @param hwnd 窗口句柄
	 * @param title 气泡标题
	 * @param message 气泡信息的内容
	 * @return 成功|失败
	 * @see #showTipTray(int, String, String, int)
	 */
	public boolean showTipTrayForError(int hwnd,String title,String message){
		return showTipTray(hwnd, title, message, 3);
	}
	
	/**
	 * 弹出一个带有当前程序图标的托盘气泡
	 * <li>要有托盘才能弹出气泡，可使用 {@link #addTray(int, String)} 创建一个托盘
	 * @param hwnd 窗口句柄
	 * @param title 气泡标题
	 * @param message 气泡信息的内容
	 * @return 成功|失败
	 * @see #showTipTray(int, String, String, int)
	 */
	public boolean showTipTrayForProcedure(int hwnd,String title,String message){
		return showTipTray(hwnd, title, message, 4);
	}
	

	/**
	 * 卸载已装载的托盘
	 * @param hwnd 窗口句柄
	 * @return 成功|失败
	 */
	public boolean delTray(int hwnd){
		boolean result=true;
		
		Variant[] var=new Variant[1];
		try {
			var[0]=new Variant(hwnd);
			this.activePlugin365.invoke("DelTray",var);
		} catch (Exception e) {
			result=false;
			e.printStackTrace();
			log.debug(this, "delTray", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result;
	}
	

	/** 
     * 创建此程序的系统托盘，本程序的托盘创建、以及使用返回的对象进行托盘弹出信息提示等
     * <li>MyEclipse编辑时一般菜单会出现中文乱码，不用在意。打包出去后会正常。
     * <li>简单使用：<pre>SystemUtil.createTray(MyClassName.class.getResource("/res/icon.png"), "显示文字", null)</pre>
     * <li>高级使用：<a href="http://www.xnx3.com/doc/j2se_util/20150307/131.html">http://www.xnx3.com/doc/j2se_util/20150307/131.html</a>
     * @param imageURL 托盘的图标所在路径。若图片过大会不显示！ 如 <pre>MyClassName.class.getResource("/res/icon.png")</pre> 其中trayico.png在当前的目录下的res文件夹内，MyClassName为当前生成次托盘的类名
     * @param title 鼠标放到托盘图标上时显示的文字
     * @param popupMenu 托盘右键点击后弹出的菜单项。若不想有菜单项或者不需要，可传入null
     * @return {@link TrayIcon} 对象。若是返回null，则创建失败
     * @see UI#createTray(ImageIcon, String, PopupMenu)
     * @deprecated
     */  
    public static TrayIcon createTray(java.net.URL imageURL,String title,PopupMenu popupMenu) {  
    	ImageIcon icon = new ImageIcon(imageURL);
    	return TrayUtil.createTray(icon, title, popupMenu);
    }  
    
}
