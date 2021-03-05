package com.xnx3.bean;

import com.jacob.activeX.ActiveXComponent;

/**
 * ActiveXComponent对象集，Windows平台下模拟键盘鼠标使用
 * @author 管雷鸣
 */
public class ActiveBean {
	
	private ActiveXComponent dm;	//dm.dll
	private ActiveXComponent plugin365;	//Plug365New.dll
	
	public ActiveXComponent getDm() {
		return dm;
	}
	public void setDm(ActiveXComponent dm) {
		this.dm = dm;
	}
	public ActiveXComponent getPlugin365() {
		return plugin365;
	}
	public void setPlugin365(ActiveXComponent plugin365) {
		this.plugin365 = plugin365;
	}
	
	
}
