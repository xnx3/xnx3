package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;

/**
 * 图片识别
 * @author 管雷鸣
 *
 */
public class FindPic {
	private Log log;
	
	private ActiveXComponent active=null;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public FindPic(ActiveBean activeBean) {
		this.active=activeBean.getDm();
		log=new Log();
	}

	/**
	 * 查找划定区域内指定的图片是否存在
	 * <li>查找指定区域内的图片,位图必须是24位色格式,支持透明色,当图像上下左右4个顶点的颜色一样时,则这个颜色将作为透明色处理.
	 * @return 存在，返回true
	 * @see FindPic#findPic(int, int, int, int, String, String, double, int)
	 */
	public boolean findPicExist(int xStart,int yStart,int xEnd,int yEnd,String pic,String deviationColor,double sim,int order){
		boolean xnx3_result=false;
		
		int result[]=findPic(xStart, yStart, xEnd, yEnd, pic, deviationColor, sim, order);
		if(result[0]!=-1){
			xnx3_result=true;
		}
		
		return xnx3_result;
	}
	
	/**
	 * 查找图片，返回找到的所有图片，限制1500个以内
	 * @param xStart 区域的左上X坐标
	 * @param yStart 区域的左上Y坐标
	 * @param xEnd 区域的右下X坐标
	 * @param yEnd 区域的右下Y坐标
	 * @param pic 图像集，多个用|分割，24位色bmp格式,且边框为同一种颜色,比如"test.bmp|test2.bmp|test3.bmp"
	 * @param deviationColor 颜色偏差，比如"203040" 表示RGB的色偏分别是20 30 40 (这里是16进制表示)
	 * @param sim 相似度,取值范围0.1-1.0 , 一般情况下0.9即可<li>越大，查找的越精准，误差越小，速度越快
	 * @param order <li>0: 从左到右,从上到下 
	 * 				<li>1: 从左到右,从下到上 
	 * 				<li>2: 从右到左,从上到下 
	 * 				<li>3: 从右到左, 从下到上
	 * @return 返回找到的所有图片，格式如："id,x,y|id,x,y..|id,x,y"
	 * 		<br/>比如"0,100,20|2,30,40" 表示找到了两个,第一个,对应的图片是图像序号为0的图片,坐标是(100,20),第二个是序号为2的图片,坐标(30,40)
	 * 		<li>出错返回null
	 */
	public String findPics(int xStart,int yStart,int xEnd,int yEnd,String pic,String deviationColor,double sim,int order){
		String result=null;
		Variant[] var=new Variant[8];
		try {
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(pic);
			var[5]=new Variant(deviationColor);
			var[6]=new Variant(sim);
			var[7]=new Variant(order);
			result=active.invoke("FindPicEx",var).getString();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this,"findPicExPositions","查找图片异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}

		return result;
	}
	
	/**
	 * 查找图片，返回找到的第一个图片的坐标
	 * @param xStart 区域的左上X坐标
	 * @param yStart 区域的左上Y坐标
	 * @param xEnd 区域的右下X坐标
	 * @param yEnd 区域的右下Y坐标
	 * @param pic 图像集，多个用|分割，24位色bmp格式,且边框为同一种颜色,比如"test.bmp|test2.bmp|test3.bmp"
	 * @param deviationColor 颜色偏差，比如"203040" 表示RGB的色偏分别是20 30 40 (这里是16进制表示)
	 * @param sim 相似度,取值范围0.1-1.0 , 一般情况下0.9即可<li>越大，查找的越精准，误差越小，速度越快
	 * @param order <li>0: 从左到右,从上到下 
	 * 				<li>1: 从左到右,从下到上 
	 * 				<li>2: 从右到左,从上到下 
	 * 				<li>3: 从右到左, 从下到上
	 * @return 返回找到的图片，返回int[]   <li>int[0]:是否找到，没找到返回-1  <li>int[1]:找到的图像的x坐标  <li>int[2]:找到的图像的y坐标
	 */
	public int[] findPic(int xStart,int yStart,int xEnd,int yEnd,String pic,String deviationColor,double sim,int order){
		int[] xnx3_result=new int[3];
		//初始化，避免走catch
		xnx3_result[0]=-1;
		xnx3_result[1]=-1;
		xnx3_result[2]=-1;
		try {
			Variant[] var=new Variant[8];
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(pic);
			var[5]=new Variant(deviationColor);
			var[6]=new Variant(sim);
			var[7]=new Variant(order);
			String findPicE=active.invoke("FindPicE",var).getString();
			var=null;
			String[] getPic=findPicE.split("\\|");
			xnx3_result[0]=Integer.parseInt(getPic[0]);
			xnx3_result[1]=Integer.parseInt(getPic[1]);
			xnx3_result[2]=Integer.parseInt(getPic[2]);
			findPicE=null;
			getPic=null;
		} catch (Exception e) {
			log.debug(this,"findPic","查找图片异常捕获:"+e.getMessage());
		}

		return xnx3_result;
	}
	
	/**
	 * 判断指定的区域，在指定的时间内(秒),图像数据是否一直不变.(卡屏)
	 * <li>若是出现变化，会立即中断计时，返回false
	 * <li>若未出现变化，则会阻塞 time 的时间
	 * @param xStart 区域的左上X坐标
	 * @param yStart 区域的左上Y坐标
	 * @param xEnd 区域的右下X坐标
	 * @param yEnd 区域的右下Y坐标
	 * @param time 需要等待的时间,单位是秒
	 * @return true:是一直不变
	 */
	public boolean isDisplayDead(int xStart,int yStart,int xEnd,int yEnd,int time){
		Variant[] var=new Variant[5];
		int result=0;
		try {
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(time);
			result=this.active.invoke("IsDisplayDead",var).getInt();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this, "isDisplayDead", "异常捕获:"+e.getMessage());
		}finally{
			var=null;
		}
		
		return result==1; 
	}
}