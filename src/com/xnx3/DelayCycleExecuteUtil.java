package com.xnx3;

import com.xnx3.interfaces.DelayCycleExecute;


/**
 * 延迟循环执行。可指定每次循环的等待时间
 * <br/>类似于支付宝、微信支付，支付成功后的对方服务器异步回调，指定时间执行
 * @author 管雷鸣
 */
public class DelayCycleExecuteUtil extends Thread {
	DelayCycleExecute dce;
	/*
	 * 每次请求的延迟，直到成功为止
	 * 第一次请求：0秒，即时，立即执行。不延迟
	 * 第二次请求：延迟3秒后执行
	 * 第三次请求：10秒
	 * 第四次请求：1分钟
	 * 第五次请求：10分钟
	 * 第六次请求：100分钟
	 */
	private int[] sleeps= {0,3000,10000,60000,600000,6000000};
	
	public DelayCycleExecuteUtil(DelayCycleExecute dce) {
		this.dce = dce;
	}
	
	/**
	 * 设置每次执行的延迟时间，单位：毫秒。
	 * <br/>如果执行 {@link DelayCycleExecute#executeProcedures(int)} 失败，则延迟指定时间后，继续执行。直到成功为止，才退出循环
	 * <br/><br/>如设定数组为：new int[]{0,2000,6000,20000}
	 * <br/>则第一次执行时，立即执行，不延迟；
	 * <br/>若失败，则延迟2秒后再次执行；
	 * <br/>若还失败，则延迟6秒后再次执行；
	 * <br/>（若成功，退出循环，不再向下执行。同时运行 {@link DelayCycleExecute#success()} 方法）
	 * <br/>若还失败，则延迟20秒后再次执行；
	 * <br/>若还失败，此此时数组已到末尾，结束线程。同时会运行 {@link DelayCycleExecute#failure()} 方法
	 * <br/><br/>若不设置，默认为：
	 * <br/>new int[]{0,3000,10000,60000,600000,6000000}
	 * @param sleepArray 设定的延迟数组。
	 */
	public void setSleepArray(int[] sleepArray){
		this.sleeps = sleepArray;
	}
	
	@Override
	public void run() {
		int i = 0;
		boolean result = false;
		int countNum = sleeps.length;
		while(result == false && ++i <= countNum){
			try {
				Thread.sleep(sleeps[i-1]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			result = dce.executeProcedures(i);
		}
		
		if(result){
			//成功
			dce.success();
		}else{
			//失败
			dce.failure();
		}
	}
	
}
