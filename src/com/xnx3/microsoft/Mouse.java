package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;

/**
 * 模拟鼠标操作
 * @author 管雷鸣
 *
 */
public class Mouse {
	private ActiveXComponent active=null;
	private Log log;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public Mouse(ActiveBean activeBean) {
		this.active=activeBean.getDm();
		log=new Log();
	}
	
	/**
	 * 鼠标移动至某个坐标点点击鼠标
	 * @param x 点击的X坐标
	 * @param y 点击的Y坐标
	 * @param click <li>左键为true
	 * 				<li>右键为false
	 */
	public boolean mouseClick(int x,int y,boolean click){
		
		try {
			int move=active.invoke("MoveTo",x,y).getInt();
			new Sleep().sleep(300);	//避免卡屏
			
			int clickResult=0;
			if(click){
				clickResult=active.invoke("LeftClick").getInt();
			}else{
				clickResult=active.invoke("RightClick").getInt();
			}
			if(move==1&&clickResult==1){
				return true;
			}else{
				return false;
			}
			
		} catch (Exception e) {
			log.debug(this,"mouseClick","鼠标移动至某个坐标点点击鼠标异常捕获:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 鼠标点击
	 * @param leftClick <li>左键点击传入true
	 * 					<li>右键点击传入false
	 */
	public void mouseClick(boolean leftClick){
		try {
			if(leftClick){
				active.invoke("LeftClick");
			}else{
				active.invoke("rightClick");
			}
		} catch (Exception e) {
			log.debug(this,"mouseClick","鼠标点击异常捕获:"+e.getMessage());
		}
	}
	
	/**
	 * 鼠标移动到指定点
	 */
	public void mouseMoveTo(int x,int y){
		try {
			active.invoke("MoveTo",x,y);
		} catch (Exception e) {
			log.debug(this,"mouseMoveTo","鼠标移动捕获异常:"+e.getMessage());
		}
	}
	
	/**
	 * 按住鼠标左键
	 */
	public void leftDown(){
		try {
			active.invoke("LeftDown");
		} catch (Exception e) {
			log.debug(this,"leftDown","鼠标左键按住捕获异常:"+e.getMessage());
		}
	}
	
	/**
	 * 弹起鼠标左键
	 */
	public void leftUp(){
		try {
			active.invoke("LeftUp");
		} catch (Exception e) {
			log.debug(this, "leftUp","鼠标左键弹起捕获异常:"+e.getMessage());
		}
	}
	
	/**
	 * 鼠标向下滚
	 */
	public void wheelDown(){
		try {
			active.invoke("WheelDown");
		} catch (Exception e) {
			log.debug(this, "wheelDown","鼠标向下滚异常:"+e.getMessage());
		}
	}
	
	/**
	 * 鼠标向上滚
	 */
	public void wheelUp(){
		try {
			active.invoke("WheelUp");
		} catch (Exception e) {
			log.debug(this, "wheelUp","鼠标向上滚异常:"+e.getMessage());
		}
	}
	
}
