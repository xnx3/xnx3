package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.xnx3.Lang;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;

/**
 * 颜色识别
 * @author 管雷鸣
 *
 */
public class Color {
	private ActiveXComponent activeDm=null;
	Log log;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public Color(ActiveBean activeBean) {
		this.activeDm=activeBean.getDm();
		this.log=new Log();
	}
	
	/**
	 * 获取指定点的颜色
	 * @param x x坐标
	 * @param y y坐标
	 * @return 字符串形式16禁制，如 FFFFFF
	 */
	public String getColor(int x,int y){
		String color="FFFFFF";
		try {
			color=activeDm.invoke("GetColor",x,y).getString();
		} catch (Exception e) {
			log.debug(this,"getColor","获取指定点颜色异常捕获:"+e.getMessage());
		}
		return color;
	}
	
	/**
	 * 判断某点颜色是否跟指定的相同或相似
	 * @param x X坐标
	 * @param y Y坐标
	 * @param color 颜色字符串,可以支持偏色,多色,例如 "ffffff-202020|000000-000000" 这个表示白色偏色为202020,和黑色偏色为000000.颜色最多支持10种颜色组合.
	 * @param similarity 相似度(0.1-1.0)
     * @return 若颜色匹配，此点是这颜色，则返回true
	 */
	public boolean findColor(int x,int y,String color,float similarity){
		boolean xnx3_result=false;
		try {
			Variant[] var=new Variant[4];
			var[0]=new Variant(x);
			var[1]=new Variant(y);
			var[2]=new Variant(color);
			var[3]=new Variant(similarity);
			
			int cmpColor=activeDm.invoke("CmpColor",var).getInt();
			var=null;

			if(cmpColor==0){
				xnx3_result=true;
			}
			cmpColor=0;
		} catch (Exception e) {
			log.debug(this,"findColor","获取颜色异常捕获:"+e.getMessage());
		}
		return xnx3_result;
	}
	
	/**
	 * 范围区域内查询某颜色是否存在
	 * @param xStart 起始点x坐标，区域的左上X坐标
	 * @param yStart 起始点y坐标，区域的左上Y坐标
	 * @param xEnd 结束点x坐标，区域的右下X坐标
	 * @param yEnd 结束点y坐标，区域的右下Y坐标
	 * @param color 颜色 格式为"RRGGBB-DRDGDB",比如"123456-000000|aabbcc-202020"
	 * @param sim 相似度,取值范围0.1-1.0
	 * @param dir 查找方向
	 * 	   <ul> 
	 * 		 <li>0: 从左到右,从上到下 
     *       <li>1: 从左到右,从下到上 
     *       <li>2: 从右到左,从上到下 
     *       <li>3: 从右到左,从下到上 
     *       <li>4：从中心往外查找
     *       <li>5: 从上到下,从左到右 
     *       <li>6: 从上到下,从右到左
     *       <li>7: 从下到上,从左到右
     *       <li>8: 从下到上,从右到左
     *    </ul>
	 * @return array 找到的图片坐标数组 
	 * 			<ul>	
	 * 				<li>若是成功返回坐在坐标int[0]：X坐标，int[1]：Y坐标
	 * 				<li>若是失败，则都是-1
	 * 			</ul>
	 */
	public int[] findColor(int xStart,int yStart,int xEnd,int yEnd,String color,double sim,int dir){
		int[] xnx3_result=new int[2];
		try {
			Variant[] var=new Variant[7];
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(color);
			var[5]=new Variant(sim);
			var[6]=new Variant(dir);
			
			String findColor=activeDm.invoke("FindColorE",var).getString();
			String[] findColorArray=findColor.split("\\|");
			xnx3_result[0]=Lang.Integer_(findColorArray[0], -1);
			xnx3_result[1]=Lang.Integer_(findColorArray[1], -1);
			var=null;
			findColorArray=null;
			findColor=null;
		} catch (Exception e) {
			log.debug(this,"findColor","获取颜色异常捕获:"+e.getMessage());
		}
		return xnx3_result;
	}
	
	/**
	 * 判断2个16进制数据的差是否在指定范围内
	 * @param color1 颜色值，如 FFFFFF
	 * @param color2 
	 * @param param 两者差距的颜色值，如 303030
	 * @return boolean
	 */
	public boolean compareColor(String color1,String color2,String param){
		boolean xnx3_result=false;
		if(color1==null||color1.length()!=6||color2==null||color2.length()!=6||param==null||param.length()!=6){
			//false
			log.debug(this,"compareColor","颜色值传入不正确，请传入6位16进制颜色值");
		}else{
			try {
				int color1_12=Lang.Integer_(color1.substring(0, 2), 0, 16);
				int color1_34=Lang.Integer_(color1.substring(2, 4), 0, 16);
				int color1_56=Lang.Integer_(color1.substring(4, 6), 0, 16);

				int color2_12=Lang.Integer_(color2.substring(0, 2), 9, 16);
				int color2_34=Lang.Integer_(color2.substring(2, 4), 9, 16);
				int color2_56=Lang.Integer_(color2.substring(4, 6), 9, 16);
				
				int param_12=Lang.Integer_(param.substring(0, 2), 5, 16);
				int param_34=Lang.Integer_(param.substring(2, 4), 5, 16);
				int param_56=Lang.Integer_(param.substring(4, 6), 5, 16);
				
				if(Math.abs(color1_12-color2_12)<param_12&&Math.abs(color1_34-color2_34)<param_34&&Math.abs(color1_56-color2_56)<param_56){
					xnx3_result=true;
				}else{
					xnx3_result=false;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.debug(this,"compareColor","16进制判断异常捕获:"+e.getMessage());
			}
		}
		
		return xnx3_result;
	}
}
