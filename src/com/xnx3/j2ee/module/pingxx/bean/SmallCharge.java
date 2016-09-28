package com.xnx3.j2ee.module.pingxx.bean;

import com.pingplusplus.model.Charge;

/**
 *  {@link Charge}的简化，主要是webhooks返回值接受到后转换为smallCharge
 * @author 管雷鸣
 */
public class SmallCharge {
	
	/**
	 * 支付宝手机支付
	 */
	public final static String CHANNEL_ALIPAY="alipay";	
	/**
	 * 支付宝手机网页支付
	 */
	public final static String CHANNEL_ALIPAY_WAP="alipay_wap";	
	/**
	 * 支付宝扫码支付
	 */
	public final static String CHANNEL_ALIPAY_QR="alipay_qr";	
	/**
	 * 支付宝 PC 网页支付
	 */
	public final static String CHANNEL_ALIPAY_PC_DIRECT="alipay_pc_direct";	
	/**
	 * 银联全渠道支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
	 */
	public final static String CHANNEL_UPACP="upacp";	
	/**
	 * 银联全渠道手机网页支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
	 */
	public final static String CHANNEL_UPACP_WAP="upacp_wap";	
	/**
	 * 银联 PC 网页支付
	 */
	public final static String CHANNEL_UPACP_PC="upacp_pc";	
	/**
	 * 银联企业网银支付
	 */
	public final static String CHANNEL_CP_B2B="cp_b2b";	
	/**
	 * 微信支付
	 */
	public final static String CHANNEL_WX="wx";	
	/**
	 * 微信公众账号支付
	 */
	public final static String CHANNEL_WX_PUB="wx_pub";	
	
	/**
	 * 微信公众账号扫码支付
	 */
	public final static String CHANNEL_WX_PUB_QR="wx_pub_qr";	
	/**
	 * Apple Pay
	 */
	public final static String CHANNEL_APPLEPAY_UPACP="applepay_upacp";	
	/**
	 * 百度钱包移动快捷支付
	 */
	public final static String CHANNEL_BFB="bfb";	
	/**
	 * 百度钱包手机网页支付
	 */
	public final static String CHANNEL_BFB_WAP="bfb_wap";	
	/**
	 * 易宝手机网页支付
	 */
	public final static String CHANNEL_YEEPAY_WAP="yeepay_wap";	
	/**
	 * 京东手机网页支付
	 */
	public final static String CHANNEL_JDPAY_WAP="jdpay_wap";	
	/**
	 * 应用内快捷支付（银联）
	 */
	public final static String CHANNEL_CNP_U="cnp_u";	
	/**
	 * 应用内快捷支付（外卡）
	 */
	public final static String CHANNEL_CNP_F="cnp_f";	
	/**
	 * 分期乐支付
	 */
	public final static String CHANNEL_FQLPAY_WAP="fqlpay_wap";	
	/**
	 * 量化派支付
	 */
	public final static String CHANNEL_QGBC_WAP="qgbc_wap";	
	
	
    String channel;	//支付使用的第三方支付渠道
    String orderNo;	//商户订单号，适配每个渠道对此参数的要求，必须在商户系统内唯一。(alipay: 1-64 位， wx: 2-32 位，bfb: 1-20 位，upacp: 8-40 位，yeepay_wap:1-50 位，jdpay_wap:1-30 位，cnp_u:8-20 位，cnp_f:8-20 位，推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
    String clientIp;	//发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1。
    Integer amount;	//订单总金额, 单位为对应币种的最小货币单位，例如：人民币为分（如订单总金额为 1 元，此处请填 100）。
    
    /**
     * 支付使用的第三方支付渠道,如 {@link SmallCharge#CHANNEL_ALIPAY}
     * @return channel
     */
	public String getChannel() {
		return channel;
	}
	/**
	 * 支付使用的第三方支付渠道,如 {@link SmallCharge#CHANNEL_ALIPAY}
	 * @param channel 如{@link SmallCharge#CHANNEL_ALIPAY}
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * 商户订单号，适配每个渠道对此参数的要求，必须在商户系统内唯一。(alipay: 1-64 位， wx: 2-32 位，bfb: 1-20 位，upacp: 8-40 位，yeepay_wap:1-50 位，jdpay_wap:1-30 位，cnp_u:8-20 位，cnp_f:8-20 位，推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
	 * @return 订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 商户订单号，适配每个渠道对此参数的要求，必须在商户系统内唯一。(alipay: 1-64 位， wx: 2-32 位，bfb: 1-20 位，upacp: 8-40 位，yeepay_wap:1-50 位，jdpay_wap:1-30 位，cnp_u:8-20 位，cnp_f:8-20 位，推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
	 * @param orderNo 订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1。
	 * @return  IP 地址
	 */
	public String getClientIp() {
		return clientIp;
	}
	/**
	 * 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1。
	 * @param clientIp 客户端的 IP 地址
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	/**
	 * 订单总金额, 单位为对应币种的最小货币单位，例如：人民币为分（如订单总金额为 1 元，此处请填 100）。
	 * @return 订单总金额
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * 订单总金额, 单位为对应币种的最小货币单位，例如：人民币为分（如订单总金额为 1 元，此处请填 100）。
	 * @param amount 订单总金额
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "SmallCharge [channel=" + channel + ", orderNo=" + orderNo
				+ ", clientIp=" + clientIp + ", amount=" + amount + "]";
	}
	
}
