package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;

/**
 * 文字识别 , 须提前设置点阵字库 {@link Com#setDict(int, String)}
 * @author 管雷鸣
 *
 */
public class FindStr {
	private ActiveXComponent active=null;
	private Log log;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public FindStr(ActiveBean activeBean) {
		this.active=activeBean.getDm();
	}
	
	/**
	 * 在屏幕范围(x1,y1,x2,y2)内,查找string(可以是任意个字符串的组合),并返回找到的符合的坐标位置
	 * 		<li>须提前设置好点阵字库 {@link Com#setDict(int, String)}
	 * @param xStart 区域的左上X坐标
	 * @param yStart 区域的左上Y坐标
	 * @param xEnd 区域的右下X坐标
	 * @param yEnd 区域的右下Y坐标
	 * @param findString 待查找的字符串, 可以是字符串组合，比如"长安|洛阳|大雁塔",中间用"|"来分割字符串
	 * @param colorFormat 颜色格式串，如  "FFFFFF-101010|555555-102030"
	 * @param sim 相似度,取值范围0.1-1.0 , 一般情况下0.9即可。越大，查找的越精准，误差越小，速度越快
	 * @param useDict 字库的序号，以此来设定使用哪个字库.   {@link Com#setDict(int, String)} 便是此设置的int序号
	 * @return int[3]   <li>int[0]:是否查找到，若是没有则返回-1  
	 * 					<li>int[1]查找到的文字的X的值，没有返回-1   
	 * 					<li>int[2]查找到的文字的Y值，没有返回-1
	 */
	public int[] findStrE(int xStart, int yStart, int xEnd,int yEnd, String findString ,String colorFormat,double sim,int useDict ){
		int[] xnx3_result=new int[3];
		//初始化赋值，避免走catch
		xnx3_result[0]=-1;
		xnx3_result[1]=-1;
		xnx3_result[2]=-1;
		try{
			active.invoke("UseDict",useDict);
			
			Variant[] var=new Variant[7];
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(findString);
			var[5]=new Variant(colorFormat);
			var[6]=new Variant(sim);
			String findStrE=active.invoke("FindStrE",var).getString();
			var=null;
			String[] findStrEArray=findStrE.split("\\|");
			xnx3_result[0]=Integer.parseInt(findStrEArray[0]);
			xnx3_result[1]=Integer.parseInt(findStrEArray[1]);
			xnx3_result[2]=Integer.parseInt(findStrEArray[2]);
			findStrE=null;
			findStrEArray=null;
		}catch (Exception e) {
			e.printStackTrace();
			log.debug(this,"findStrE","查找文字异常捕获:"+e.getMessage());
		}
		
		return xnx3_result;
	}
	
	/**
	 * 指定区域内读取文字
	 * 		<li>须提前设置好点阵字库 {@link Com#setDict(int, String)}
	 * @param xStart 区域的左上X坐标
	 * @param yStart 区域的左上Y坐标
	 * @param xEnd 区域的右下X坐标
	 * @param yEnd 区域的右下Y坐标
	 * @param colorFormat 颜色格式串
	 * 			<li>RGB单色识别:"9f2e3f-000000"
	 * 			<li>RGB单色差色识别:"9f2e3f-030303"
	 * 			<li>RGB多色识别(最多支持10种,每种颜色用"|"分割):"9f2e3f-030303|2d3f2f-000000|3f9e4d-100000"
	 * 			<li>HSV多色识别(最多支持10种,每种颜色用"|"分割):"20.30.40-0.0.0|30.40.50-0.0.0"
	 * 			<li>背景色识别:"b@ffffff-000000"
	 * @param lineBreak 换行符，为空或者null则不使用换行符分割。读取的文字每行换行时会加上此字符串作为换行分割
	 * @param sim 相似度,取值范围0.1-1.0 , 一般情况下0.9即可。越大，查找的越精准，误差越小，速度越快
	 * @param useDict 字库的序号，以此来设定使用哪个字库.   {@link Com#setDict(int, String)} 便是此设置的int序号
	 * @return String 若是没找到，返回null
	 */
	public String readStr(int xStart,int yStart,int xEnd,int yEnd,String colorFormat,String lineBreak, double sim,int useDict){
		String xnx3_result="";
		try {
			active.invoke("UseDict",useDict);
			Variant[] var=new Variant[6];
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(colorFormat);
			var[5]=new Variant(sim);
			xnx3_result=active.invoke("Ocr",var).getString();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this,"readStr","读取文字异常捕获:"+e.getMessage());
		}
		
		if(xnx3_result.length()==0){
			xnx3_result=null;
		}
		
		return xnx3_result;
	}
}
