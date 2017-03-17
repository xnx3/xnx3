package com.xnx3.interfaces;

import com.xnx3.DelayCycleExecuteUtil;

/**
 * 服务于 {@link DelayCycleExecuteUtil} ，用于定时运行某段命令使用
 * @author 管雷鸣
 */
public interface DelayCycleExecute {
	/**
	 * 要执行的程序
	 * @param i 当前第几次执行。最大值取决于{@link DelayCycleExecuteUtil#setSleepArray(int[])}的长度
	 * @return boolean <ul>
	 * 						<li>true:执行成功，跳出循环，不再执行</li>
	 * 						<li>false:执行失败，继续等待下一次执行</li>
	 * 					</ul>
	 */
	public boolean executeProcedures(int i);
	
	/**
	 * 要执行的程序 {@link #executeProcedures()} 执行成功后，会执行此方法
	 */
	public void success();
	
	/**
	 * 要执行的程序 {@link #executeProcedures()} 执行完指定的次数后( {@link DelayCycleExecuteUtil#setSleepArray(int[])} )，如果依然没有执行成功过，则会执行此方法
	 */
	public void failure();
}