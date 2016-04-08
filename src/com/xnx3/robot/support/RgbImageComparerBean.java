package com.xnx3.robot.support;

/**
 * RGB 相关，图片相似度计算时使用
 * @author 管雷鸣
 *
 */
public class RgbImageComparerBean {
	
	/******颜色值数组，第一纬度为x坐标，第二纬度为y坐标******/
	private int colorArray[][];
	
	/****图片的宽高****/
	private int imgWidth;		
	private int imgHeight;
	
	//图片的像素总数
	private int pxCount;

	public int[][] getColorArray() {
		return colorArray;
	}

	public void setColorArray(int[][] colorArray) {
		this.colorArray = colorArray;
		this.imgWidth = this.colorArray.length;
		this.imgHeight = this.colorArray[0].length;		
		this.pxCount = this.imgWidth*this.imgHeight;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public int getPxCount() {
		return pxCount;
	}

	
}
