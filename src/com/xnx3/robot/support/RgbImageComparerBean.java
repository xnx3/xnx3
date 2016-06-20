package com.xnx3.robot.support;

/**
 * RGB 相关，图片相似度计算时使用
 * @author 管雷鸣
 */
public class RgbImageComparerBean {
	
	/******颜色值数组，第一纬度为x坐标，第二纬度为y坐标******/
	private int colorArray[][];
	
	/***是否忽略此点，若为true，则不纳入比较的像素行列。***/
	private boolean ignorePx[][];
	
	/****图片的宽高****/
	private int imgWidth;		
	private int imgHeight;
	
	//图片的像素总数
	private int pxCount;

	/**
	 * 获取图像的二维数组组成
	 * @return 图像的二维数组
	 */
	public int[][] getColorArray() {
		return colorArray;
	}
	
	/**
	 * 要对比的像素总数,会自动筛选掉不对比的颜色
	 * @param pxCount pxCount
	 */
	public void setPxCount(int pxCount) {
		this.pxCount = pxCount;
	}

	/**
	 * 设置颜色二维数组
	 * @param colorArray 颜色二维数组，一维为x轴，二维为y轴
	 */
	public void setColorArray(int[][] colorArray) {
		this.colorArray = colorArray;
		this.imgWidth = this.colorArray.length;
		this.imgHeight = this.colorArray[0].length;		
		this.pxCount = this.imgWidth*this.imgHeight;
	}
	
	/**
	 * 是否忽略此点，若为true，则不纳入像素比较行列。
	 * @return 具体x，y坐标的那个像素点
	 */
	public boolean[][] getIgnorePx() {
		return ignorePx;
	}

	/**
	 * 是否忽略此点，若为true，则不纳入像素比较行列。
	 * @param ignorePx 具体x，y坐标的那个像素点
	 */
	public void setIgnorePx(boolean[][] ignorePx) {
		this.ignorePx = ignorePx;
	}

	/**
	 * 获取图像的宽度
	 * @return 图像宽度
	 */
	public int getImgWidth() {
		return imgWidth;
	}

	/**
	 * 获取图像的高度
	 * @return 图像高度
	 */
	public int getImgHeight() {
		return imgHeight;
	}
	
	/**
	 * 获取图像里像素的总数
	 * @return 图像里像素的总数
	 */
	public int getPxCount() {
		return pxCount;
	}

	
}
