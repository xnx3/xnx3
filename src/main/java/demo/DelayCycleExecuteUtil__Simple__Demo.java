package demo;

import com.xnx3.DateUtil;
import com.xnx3.DelayCycleExecuteUtil;
import com.xnx3.interfaces.DelayCycleExecute;

/**
 * 类似于支付宝、微信支付，支付成功后的对方服务器异步回调，指定时间执行
 * <br/>比如，第一次立即执行，若执行失败，第二次间隔2秒后再次执行，若执行成功，跳出；若执行失败，继续等待5秒后继续执行...
 * <br/>需Jar包：
 * <br/>	xnx3-*.jar	可从 https://github.com/xnx3/xnx3 下载       
 * @author 管雷鸣
 */
public class DelayCycleExecuteUtil__Simple__Demo {
	
	public static void main(String[] args) {
		DelayCycleExecuteUtil dct = new DelayCycleExecuteUtil(new DelayCycleExecute() {
			public void success() {
				System.out.println("执行成功");
			}
			public void failure() {
				System.out.println("执行失败");
			}
			public boolean executeProcedures(int i) {
				System.out.println("当前第"+i+"次执行，当前时间："+DateUtil.currentDate("yyyy-MM-dd HH:mm:ss"));
				if(i==3){
					//假设在执行第三次的时候，执行成功了，返回true。 （true:执行成功，跳出循环，不再执行）
					return true;
				}
				return false;
			}
		});
		
		/**
		 * 设置每次执行的延迟时间，单位：毫秒。
		 * 如果执行 {@link DelayCycleExecute#executeProcedures(int)} 失败，则延迟指定时间后，继续执行。直到成功为止，才退出循环
		 * 如设定数组为：{0,2000,6000,20000}
		 * 则第一次执行时，立即执行，不延迟；
		 * 若失败，则延迟2秒后再次执行；
		 * 若还失败，则延迟6秒后再次执行；
		 * （若成功，退出循环，不再向下执行。同时运行 {@link DelayCycleExecute#success()} 方法）
		 * 若还失败，则延迟20秒后再次执行；
		 * 若还失败，此此时数组已到末尾，结束线程。同时会运行 {@link DelayCycleExecute#failure()} 方法
		 * 
		 * 若不设置，默认为：
		 * 		{0,3000,10000,60000,600000,6000000}
		 * @param sleepArray 设定的延迟数组。
		 */
		dct.setSleepArray(new int[]{0,1000,2000,2000,1000,2000});
		
		//开启此线程。本身是一个多线程 Thread
		dct.start();
	}
	
}
