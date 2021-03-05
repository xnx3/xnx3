package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;

/**
 * 键盘模拟操作
 * @author 管雷鸣
 *
 */
public class Press {
	private ActiveXComponent active=null;
	private Log log;
	
	/**
	 * enter确定键
	 */
	public final static int ENTER=13;
	public final static int TAB=9;
	public final static int SHIFT=16;
	public final static int CAPSLOCK=20;
	public final static int CTRL=17;
	
	/**
	 * win键，点击触发开始菜单的键
	 */
	public final static int WIN=91;
	public final static int ALT=18;
	
	/**
	 * 空格键
	 */
	public final static int SPACE=32;
	
	/**
	 * Backspace删除键
	 */
	public final static int BACKSPACE=8;
	
	/**
	 * 上分页
	 */
	public final static int PAGEUP=33;
	
	/**
	 * 下分页
	 */
	public final static int PAGEDOWN=34;
	public final static int HOME=36;
	
	/**
	 * 键盘右侧HOME下的END键
	 */
	public final static int END=35;	
	
	/**
	 * 键盘右侧，方向键上方的Insert键
	 */
	public final static int INSERT=45;
	
	/**
	 * 键盘右侧，方向键上方的Delete键
	 */
	public final static int DELETE=46;
	public final static int F1=112;
	public final static int F2=113;
	public final static int F3=114;
	public final static int F4=115;
	public final static int F5=116;
	public final static int F6=117;
	public final static int F7=118;
	public final static int F8=119;
	public final static int F9=120;
	public final static int F10=121;
	public final static int F11=122;
	public final static int F12=123;
	
	/**
	 * 数字键，主键盘字母上面的数字0
	 */
	public final static int NUM_0=48;

	/**
	 * 数字键，主键盘字母上面的数字1
	 */
	public final static int NUM_1=59;
	
	/**
	 * 数字键，主键盘字母上面的数字2
	 */
	public final static int NUM_2=50;
	
	/**
	 * 数字键，主键盘字母上面的数字3
	 */
	public final static int NUM_3=51;
	
	/**
	 * 数字键，主键盘字母上面的数字4
	 */
	public final static int NUM_4=52;
	
	/**
	 * 数字键，主键盘字母上面的数字5
	 */
	public final static int NUM_5=53;
	
	/**
	 * 数字键，主键盘字母上面的数字6
	 */
	public final static int NUM_6=54;
	
	/**
	 * 数字键，主键盘字母上面的数字7
	 */
	public final static int NUM_7=55;
	
	/**
	 * 数字键，主键盘字母上面的数字8
	 */
	public final static int NUM_8=56;
	
	/**
	 * 数字键，主键盘字母上面的数字9
	 */
	public final static int NUM_9=57;
	
	/**
	 * 下划线，位于主键盘区域890后面的那个_
	 */
	public final static int UNDERLINE=189;
	
	/**
	 * 等号，位于主键盘区域的890_后面的那个=
	 */
	public final static int QUEAL=187;
	
	public final static int A=65;
	public final static int B=66;
	public final static int C=67;
	public final static int D=68;
	public final static int E=69;
	public final static int F=70;
	public final static int G=71;
	public final static int H=72;
	public final static int I=73;
	public final static int J=74;
	public final static int K=75;
	public final static int L=76;
	public final static int M=77;
	public final static int N=78;
	public final static int O=79;
	public final static int P=80;
	public final static int Q=81;
	public final static int R=82;
	public final static int S=83;
	public final static int T=84;
	public final static int U=85;
	public final static int V=86;
	public final static int W=87;
	public final static int X=88;
	public final static int Y=89;
	public final static int Z=90;
	
	/**
	 * 逗号
	 */
	public final static int COMMA=188;
	
	/**
	 * 句号
	 */
	public final static int FULLSTOP=190;
	
	/**
	 * 问号
	 */
	public final static int QUESTION=191;
	
	/**
	 * 分号
	 */
	public final static int SEMICOLON=186;
	
	/**
	 * 引号，主键盘字母跟Enter中间那个
	 */
	public final static int QUOTATIONMARK=222;
	
	/**
	 * 分隔号，主键盘Enter左侧的那个键
	 */
	public final static int SEPARATRIX=220;
	
	/**
	 * 大括号，左侧的大括号，p键右边那个
	 */
	public final static int BRACE_LEFT=219;
	
	/**
	 * 大括号，右侧的大括号，p键右边第二个结束的右括号
	 */
	public final static int BRACE_RIGHT=221;
	
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public Press(ActiveBean activeBean) {
		this.active=activeBean.getDm();
	}
	
	public Press(){
		
	}
	
	/**
	 * 按下指定的键一定的时间后放开
	 * @param keyCode 如 {@link Press#A}
	 * @param sleep 延迟时间，单位毫秒
	 */
	public void keyPressTime(int keyCode,int sleep){
		try {
			active.invoke("KeyDown",keyCode);
			
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			active.invoke("KeyUp",keyCode);
		} catch (Exception e) {
			log.debug(this,"keyPressTime","按下指定键一段时间异常捕获:"+e.getMessage());
		}
	}
	
	/**
	 * 组合按键,按住一个键，再按一个键(释放，非按住不放)
	 * @param downKey 要按住的键  如 {@link Press#A}
	 * @param secondKey 第二个按的键  如 {@link Press#A}
	 */
	public void groupPress(int downKey,int secondKey){
		Sleep sleep=new Sleep();
		keyDown(downKey);
		sleep.sleep(200);
		keyPress(secondKey);
		sleep.sleep(100);
		keyUp(downKey);
		sleep=null;
	}
	
	/**
	 * 按下某键
	 * @param keyCode 传入按键码  如 {@link Press#A}
	 */
	public void keyPress(int keyCode){
		try {
			active.invoke("KeyPress",keyCode);
		} catch (Exception e) {
			log.debug(this,"keyPress","按键异常捕获"+e.getMessage());
		}
	}
	
	
	/**
	 * 按住某键(按下某个键位并不弹起)搭配keyUp使用
	 * @param keyCode 传入按键码  如 {@link Press#A}
	 */
	public void keyDown(int keyCode){
		try {
			active.invoke("KeyDown",keyCode);
		} catch (Exception e) {
			log.debug(this,"keyDown","按键按住异常捕获"+e.getMessage());
		}
	}
	
	/**
	 * 按住某键(按下某个键位并不弹起)搭配keyDown使用
	 * @param keyCode 传入按键码  如 {@link Press#A}
	 */
	public void keyUp(int keyCode){
		try {
			active.invoke("KeyUp",keyCode);
		} catch (Exception e) {
			log.debug(this,"keyUp","按键弹起异常捕获"+e.getMessage());
		}
	}
	
	/**
	 * 将java.awt.event.KeyEvent.getKeyCode转换为模拟按键用的按键码
	 * <li>无需ActiveXComponent对象即可
	 * @param keyCode java.awt.event.KeyEvent
	 * @return 模拟按键用的按键码
	 */
	public int keyCodeTransform(int keyCode){
		switch(keyCode){
			case 45:		//-
				keyCode=189;
				break;
			case 61:		//=
				keyCode=187;
				break;
			case 10:		//Enter
				keyCode=13;
				break;
			case 91:		//[
				keyCode=219;
				break;
			case 93:		//]
				keyCode=221;
				break;
			case 92:		// \
				keyCode=220;
				break;
			case 59:		// ;
				keyCode=186;
				break;
			case 44:		// ,
				keyCode=188;
				break;
			case 46:		// .
				keyCode=190;
				break;
			case 47:		// /
				keyCode=191;
				break;
		}
		return keyCode;
	}
	
	/**
	 * 将某些不能输出显示的按键为正常文字可供用户看到
	 * <li>无需ActiveXComponent对象即可使用
	 * @param keyCode 传入按键码,传入的为模拟按键的按键码
	 * @return 返回键名，如Enter、BackSpace、空格、上……等，若没有则返回NULL
	 */
	public String keyCodeName(int keyCode){
		String result=null;
		switch(keyCode){
			case 27:	
				result="Esc";
				break;
			case 112:		
				result="F1";
				break;
			case 113:		
				result="F2";
				break;
			case 114:		
				result="F3";
				break;
			case 115:		
				result="F4";
				break;
			case 116:		
				result="F5";
				break;
			case 117:		
				result="F6";
				break;
			case 118:		
				result="F7";
				break;
			case 119:		
				result="F8";
				break;
			case 120:		
				result="F10";
				break;
			case 121:		
				result="F11";
				break;
			case 122:		
				result="F12";
				break;
			case 145:		
				result="Scroll Lock";
				break;
			case 8:		
				result="BackSpace";
				break;
			case 20:		
				result="Caps Lock";
				break;
			case 16:		
				result="Shift";
				break;
			case 17:		
				result="Ctrl";
				break;
			case 18:		
				result="Alt";
				break;
			case 32:		
				result="空格";
				break;
			case 38:	
				result="上";
				break;
			case 40:	
				result="下";
				break;
			case 37:	
				result="左";
				break;
			case 39:	
				result="右";
				break;
			case 13:	
				result="Enter";
				break;
		}
		return result;
	}
}
