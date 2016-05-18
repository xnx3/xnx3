package com.xnx3.j2ee.module.pingxx.bean;

/**
 * webhooks接受的内容，转化为Event，但官方的Event没有解析到Charge对象。故而增加
 * @author 管雷鸣
 */
public class Event extends com.pingplusplus.model.Event {
	
	/**
	 * 支付成功
	 */
	public final static String TYPE_CHARGE_SUCCEEDED = "charge.succeeded";
	/**
	 * 退款成功
	 */
	public final static String TYPE_REFUND_SUCCEEDED = "refund.succeeded";
	
	
	private SmallCharge smallCharge;

	public SmallCharge getSmallCharge() {
		return smallCharge;
	}

	public void setSmallCharge(SmallCharge smallCharge) {
		this.smallCharge = smallCharge;
	}

	
}
