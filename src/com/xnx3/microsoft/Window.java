package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;

/**
 * 窗口操作
 * @author 管雷鸣
 *
 */
public class Window {
	private ActiveXComponent active=null;
	private Log log;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public Window(ActiveBean activeBean){
		this.active=activeBean.getDm();
		log=new Log();
	}
	
	
	/**
	 * 移动窗口到屏幕指定坐标点
	 * @param hwnd 移动窗口的句柄
	 * @return 成功:true
	 */
	public boolean moveWindow(int hwnd,int x, int y){
		boolean xnx3_result=false;
		Variant[] var=new Variant[3];
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(x);
			var[2]=new Variant(y);
			int getResult=active.invoke("MoveWindow",var).getInt();
			if(getResult==1){		//成功
				xnx3_result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this,"MoveWindow","移动窗口时异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return xnx3_result;
	}
	
	/**
	 * 设置窗口状态
	 * @param  hwnd 操作的窗口句柄
	 * @param type
	 *  <li>0 : 关闭指定窗口
	 *	<li>1 : 激活指定窗口
	 *	<li>2 : 最小化指定窗口,但不激活
	 *	<li>3 : 最小化指定窗口,并释放内存,但同时也会激活窗口.
	 *	<li>4 : 最大化指定窗口,同时激活窗口.
	 *	<li>5 : 恢复指定窗口 ,但不激活
	 *	<li>6 : 隐藏指定窗口
	 *	<li>7 : 显示指定窗口
	 *	<li>8 : 置顶指定窗口
	 *	<li>9 : 取消置顶指定窗口
	 *	<li>10 : 禁止指定窗口
	 *	<li>11 : 取消禁止指定窗口
	 *	<li>12 : 恢复并激活指定窗口
	 *	<li>13 : 强制结束窗口所在进程.
	 */
	public boolean setWindowState(int hwnd,int type){
		boolean xnx3_result=false;
		try {
			int getResult=this.active.invoke("SetWindowState",hwnd,type).getInt();
			if(getResult==1){
				xnx3_result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this,"setWindowState","设置窗口状态setWindowState异常捕获:"+e.getMessage());
		}
		return xnx3_result;
	}
	
	/**
	 * 结束窗口所在进程
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowDestroy(int hwnd){
		return setWindowState(hwnd, 13);
	}
	
	/**
	 * 取消置顶指定窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowTopDestroy(int hwnd){
		return setWindowState(hwnd, 9);
	}
	
	
	/**
	 * 置顶指定窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowTop(int hwnd){
		return setWindowState(hwnd, 8);
	}
	

	/**
	 * 显示指定窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowShow(int hwnd){
		return setWindowState(hwnd, 7);
	}
	
	
	/**
	 * 隐藏指定窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowHidden(int hwnd){
		return setWindowState(hwnd, 6);
	}
	
	
	/**
	 * 最大化指定窗口,同时激活窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowMaximizeAndActivate(int hwnd){
		return setWindowState(hwnd, 4);
	}
	
	
	/**
	 * 激活指定的窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败 
	 */
	public boolean setWindowActivate(int hwnd){
		return setWindowState(hwnd, 1);
	}
	
	/**
	 * 最小化指定窗口,并释放内存,但同时也会激活窗口
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowMinimizeAndActivate(int hwnd){
		return setWindowState(hwnd, 2);
	}
	
	/**
	 * 最小化指定窗口,但不激活
	 * @param hwnd 操作的目标窗口句柄
	 * @see Window#setWindowState(int, int)
	 * @return <li>true:成功
	 * 			<li>false:失败
	 */
	public boolean setWindowMinimize(int hwnd){
		return setWindowState(hwnd, 2);
	}
	
	
	/**
	 * 设置指定窗口的大小
	 * @param hwnd 窗口句柄
	 * @param width 要设置成的宽度
	 * @param height 要设置成的高度
	 * @return 设置成功返回true
	 */
	public boolean setWindowSize(int hwnd,int width,int height){
		Variant[] var=new Variant[3];
		int result=0;
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(width);
			var[2]=new Variant(height);
			result=this.active.invoke("SetClientSize",var).getInt();
		} catch (Exception e) {
			log.debug(this, "setWindowSize", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		return result==1; 
	}
	
	/**
	 * 获取鼠标所指向的窗口句柄
	 * @return int <li>0：失败
	 * 			   <li> 大于0:成功
	 */
	public int getMousePointWindowHwnd(){
		int result=0;
		try {
			result=this.active.invoke("GetMousePointWindow").getInt();
		} catch (Exception e) {
			log.debug(this, "getMousePointWindowHwnd", "异常捕获:"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据窗口句柄获取窗口标题
	 * @param hwnd 窗口句柄
	 * @return String 窗口名字
	 */
	public String getWindowTitle(int hwnd){
		String xnx3_result=null;
		if(hwnd<1){
			xnx3_result="窗口句柄不可为空";
		}
		try {
			xnx3_result=this.active.invoke("GetWindowTitle",hwnd).getString();
		} catch (Exception e) {
			xnx3_result="获取异常:"+e.getMessage();
			log.debug(this, "getWindowTitle", "异常捕获:"+e.getMessage());
		}
		
		return xnx3_result;
	}
	
	/**
	 * 设置窗口的标题
	 * @param hwnd 指定的窗口句柄
	 * @param title 新标题
	 * @return boolean true:成功
	 */
	public boolean setWindowTitle(int hwnd,String title){
		Variant[] var=new Variant[2];
		int result=0;
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(title);
			result=this.active.invoke("SetWindowText",var).getInt();
		} catch (Exception e) {
			log.debug(this, "SetWindowText", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	

	/**
	 * 设置窗口的透明度
	 * @param hwnd 指定的窗口句柄
	 * @param trans 透明度取值(0-255) 越小透明度越大 0为完全透明(不可见) 255为完全显示(不透明)
	 * @return boolean true:成功
	 */
	public boolean setWindowTransparent(int hwnd,int trans){
		Variant[] var=new Variant[2];
		int result=0;
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(trans);
			result=this.active.invoke("setWindowTransparent",var).getInt();
		} catch (Exception e) {
			log.debug(this, "setWindowTransparent", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	
	/**
	 * 向指定窗口发送粘贴命令. 把剪贴板的内容发送到目标窗口
	 * @param hwnd 指定的窗口句柄
	 * @return boolean true:成功
	 */
	public boolean sendPaste(int hwnd){
		Variant[] var=new Variant[1];
		int result=0;
		try {
			var[0]=new Variant(hwnd);
			result=this.active.invoke("SendPaste",var).getInt();
		} catch (Exception e) {
			log.debug(this, "setWindowTransparent", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	
	/**
	 * 判断窗口是否存在 
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:存在
	 */
	public boolean windowIsExist(int hwnd){
		return windowState(hwnd, 0);
	}
	

	/**
	 * 判断窗口是否处于激活
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:激活
	 */
	public boolean windowIsActivate(int hwnd){
		return windowState(hwnd, 1);
	}
	
	/**
	 * 判断窗口是否可见
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:可见
	 */
	public boolean windowIsVisible(int hwnd){
		return windowState(hwnd, 2);
	}
	
	/**
	 * 判断窗口是否最小化
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:最小化
	 */
	public boolean windowIsMinimize(int hwnd){
		return windowState(hwnd, 3);
	}
	
	
	/**
	 * 判断窗口是否最大化
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:最大化
	 */
	public boolean windowIsMaximize(int hwnd){
		return windowState(hwnd, 4);
	}
	
	/**
	 * 判断窗口是否置顶
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:置顶
	 */
	public boolean windowIsTop(int hwnd){
		return windowState(hwnd, 5);
	}
	
	/**
	 * 判断窗口是否无响应
	 * @param hwnd 指定的窗口句柄
	 * @see Window#windowState(int, int)
	 * @return boolean true:无响应
	 */
	public boolean windowIsNoResponse(int hwnd){
		return windowState(hwnd, 6);
	}
	
	
	/**
	 * 判断窗口的状态,获得一些窗口的属性,可直接使用  可直接使用 {@link Window#windowIsExist(int)} 、 {@link Window#windowIsVisible(int)} ...
	 * @param hwnd 指定的窗口句柄
	 * @param type 查询的状态:<li>0 : 判断窗口是否存在
	 * 						<li>1 : 判断窗口是否处于激活
	 * 						<li>2 : 判断窗口是否可见
	 * 						<li>3 : 判断窗口是否最小化
	 * 						<li>4 : 判断窗口是否最大化
	 * 						<li>5 : 判断窗口是否置顶
	 * 						<li>6 : 判断窗口是否无响应
	 * @return boolean true:是
	 */
	public boolean windowState(int hwnd,int type){
		Variant[] var=new Variant[2];
		int result=0;
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(type);
			result=this.active.invoke("GetWindowState",var).getInt();
		} catch (Exception e) {
			log.debug(this, "windowState", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
	
	
	/**
	 * 获取窗口的类名
	 * @param hwnd 指定的窗口句柄
	 * @return 窗口的类名
	 */
	public String getWindowClass(int hwnd){
		Variant[] var=new Variant[1];
		String result=null;
		try {
			var[0]=new Variant(hwnd);
			result=this.active.invoke("GetWindowClass",var).getString();
		} catch (Exception e) {
			log.debug(this, "getWindowClass", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result; 
	}
	
	/**
	 * 获取指定窗口所在的进程的exe文件全路径
	 * @param hwnd 指定的窗口句柄
	 * @return 返回字符串表示的是exe全路径名，如： D:\Program Files (x86)\Tencent\QQ\bin\QQ.exe
	 */
	public String getWindowProcessPath(int hwnd){
		Variant[] var=new Variant[1];
		String result=null;
		try {
			var[0]=new Variant(hwnd);
			result=this.active.invoke("GetWindowProcessPath",var).getString();
		} catch (Exception e) {
			log.debug(this, "getWindowProcessPath", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result; 
	}
	
	/**
	 * 发送给指定窗口字符串消息（输入到指定窗口）
	 * @param hwnd 窗口句柄
	 * @param content 发送的内容
	 * @return 成功返回true
	 */
	public boolean sendString(int hwnd,String content){
		Variant[] var=new Variant[2];
		int result=0;
		try {
			var[0]=new Variant(hwnd);
			var[1]=new Variant(content);
			result=this.active.invoke("SendString",var).getInt();
		} catch (Exception e) {
			log.debug(this, "sendString", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		return result==1; 
	}
	
	/**
	 * 查找符合类名或者标题名的顶层可见窗口,如果指定了parentHwnd,则在parentHwnd的第一层子窗口中查找.
	 * @param parentHwnd 父窗口句柄，如果为0，则匹配所有顶层窗口
	 * @param className 窗口类名，如果为空，则匹配所有. 这里的匹配是模糊匹配.
	 * @param title 窗口标题,如果为空，则匹配所有. 这里的匹配是模糊匹配.
	 * @return 找到的窗口句柄，0为没有找到
	 */
	public int findWindow(int parentHwnd,String className,String title){
		Variant[] var=new Variant[3];
		int result=0;
		if(className==null){
			className="";
		}
		if(title==null){
			title="";
		}
		try {
			var[0]=new Variant(parentHwnd);
			var[1]=new Variant(className);
			var[2]=new Variant(title);
			result=this.active.invoke("FindWindowEx",var).getInt();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this, "findChildWindow", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		return result; 
	}
	

	/**
	 * 获取窗口在屏幕上的位置,窗口可被遮挡，不可最小化
	 * @param hwnd 目标窗口句柄
	 * @return int[] 参数介绍：
	 * 			<li>[0]:调用C函数执行情况。 0失败；1成功
	 * 			<li>[1]:返回窗口左上角X坐标
	 * 			<li>[2]:返回窗口左上角Y坐标
	 * 			<li>[3]:返回窗口右下角X坐标
	 * 			<li>[4]:返回窗口右下角Y坐标
	 */
	public int[] getWindowRect(int hwnd){
		int result[]={0,0,0,0,0};
		
		Variant[] var=new Variant[5];
		try{
			var[0]=new Variant(hwnd);
			var[1]=new Variant(0,true);
			var[2]=new Variant(0,true);
			var[3]=new Variant(0,true);
			var[4]=new Variant(0,true);
			result[0]=active.invoke("GetWindowRect",var).getInt();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			result[1]=var[1].getInt();
			result[2]=var[2].getInt();
			result[3]=var[3].getInt();
			result[4]=var[4].getInt();
			
			var=null;
		}
		
		return result;
	}
	
}
