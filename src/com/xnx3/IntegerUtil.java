package com.xnx3;

import java.util.Random;

/**
 * Int相关
 * @author 管雷鸣
 */
public class IntegerUtil {
	
	/**
	 * 生成一个随机数，值在 minValue 与 maxValue 之间(包含)
	 * @param minValue 生成的随机数的最小值
	 * @param maxValue 生成的随机数的最大值
	 * @return 生成的随机数
	 */
	public static int random(int minValue, int maxValue){
		int max = maxValue + 1;
		if(minValue >= max){
			//若传入的最小值比最大值还大，那么直接返回最小值
			return minValue;
		}
		
		Random random = new Random();
		int i = 0;
		while(!(i != 0 && i >= minValue)){
			i = random.nextInt(max);
		}
		
		return i;
	}
	
}
