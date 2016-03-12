package com.xnx3.microsoft;

/**
 * 延迟等待
 * @author 管雷鸣
 */
public class Sleep {
	
	/**
	 * 延迟等待，使用的Thread.sleep()
	 * @param millis 延迟等待的时间，单位：毫秒
	 */
	public void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
