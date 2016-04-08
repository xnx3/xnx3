package com.xnx3.robot.support;

/**
 * RGB颜色相关
 * @author 管雷鸣
 *
 */
public class RGBBean {
	private int r;
	private int g;
	private int b;
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	@Override
	public String toString() {
		return "RGBBean [r=" + r + ", g=" + g + ", b=" + b + "]";
	}
	
	
}
