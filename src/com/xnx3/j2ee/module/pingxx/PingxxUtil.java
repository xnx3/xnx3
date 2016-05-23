package com.xnx3.j2ee.module.pingxx;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.module.pingxx.bean.SmallCharge;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.spec.X509EncodedKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.security.KeyFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Ping＋＋
 * <br/>需配置 xnx3Config.xml的PingPlusPlus节点
 * <br><b>需导入</b> 
 * <br/><i>commons-codec-1.10.jar</i>
 * <br/><i>gson-2.6.2.jar</i>
 * <br/><i>pingpp-java-2.1.7.jar</i>
 * <br/><i>json-lib-2.4-jdk15.jar</i>
 * @author 管雷鸣
 */
public class PingxxUtil {
    /**
     * ping＋＋ 管理平台对应的应用 ID，会自动从systemConfig.xml中读出来
     */
    public static String appId = "";
    
    /**
     * ping＋＋的公钥，会自动从systemConfig.xml中读出来
     */
    public static String publicKey = "";
    
    private static boolean debug = true;

    static {
    	Pingpp.apiKey = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("pingxx.apiKey");
    	appId = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("pingxx.appId");
        Pingpp.privateKey=ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("pingxx.privateKey");
        publicKey=ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("pingxx.publicKey");
        debug =ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("pingxx.debug").equals("true");
    }

    public static void main(String[] args) {
    	
    	/****测试创建Charge*****/
////    	Charge charge = createCharge(12, "标题啊", "描述啊", "3423425", SmallCharge.CHANNEL_ALIPAY, "129.12.12.12");
//    	System.out.println(charge.toString());
    	System.out.println(generateOrderNo());
    	
//    	/*******测试webhooks异步回调接受******/
//		//签名
//    	String sign = "l4EUlkWgJD0cUNRshtGhrV/qQ6tXhnCANTDR5D3iHyo0F2WqhubbTUJEzp8Ym00TTIM37lv1mfVozpHF811Vy2ZXwnqPLrLl9SyWMnRML2OOYDpD0XrStHMyE774yf6HyaFD8fcmwlOOFeY26NyfCx3cmbLWHT+me/ZnKtA1N5eZmKkteUIMSgk8jhBHA4RT2nSapKiVbMqueBXrKmtekvuUHwRmuqHmi0ee/7uesIqWfhtGXRbwQvygIo+Mx2OV7qAvl0IyENipZUa+TTJ05GZDM/s6goPkH9pcp/hQ/lUTbIMU42jZJO30W6zCoDsZKUjTo6+Quz1XVW1lK/KuSw==";
//		//模拟ping＋＋ 通过webhooks发送过来的json数据,实际web应用使用 webhooks(request, response)
//		String dataString = "{\"id\":\"evt_ugB6x3K43D16wXCcqbplWAJo\",\"created\":1440407501,\"livemode\":false,\"type\":\"charge.succeeded\",\"data\":{\"object\":{\"id\":\"ch_Xsr7u35O3m1Gw4ed2ODmi4Lw\",\"object\":\"charge\",\"created\":1440407501,\"livemode\":true,\"paid\":true,\"refunded\":false,\"app\":\"app_urj1WLzvzfTK0OuL\",\"channel\":\"upacp\",\"order_no\":\"123456789\",\"client_ip\":\"127.0.0.1\",\"amount\":100,\"amount_settle\":0,\"currency\":\"cny\",\"subject\":\"Your Subject\",\"body\":\"Your Body\",\"extra\":{},\"time_paid\":1440407501,\"time_expire\":1440407501,\"time_settle\":null,\"transaction_no\":\"1224524301201505066067849274\",\"refunds\":{\"object\":\"list\",\"url\":\"/v1/charges/ch_Xsr7u35O3m1Gw4ed2ODmi4Lw/refunds\",\"has_more\":false,\"data\":[]},\"amount_refunded\":0,\"failure_code\":null,\"failure_msg\":null,\"metadata\":{},\"credential\":{},\"description\":null}},\"object\":\"event\",\"pending_webhooks\":0,\"request\":\"iar_qH4y1KbTy5eLGm1uHSTS00s\"}";
//		com.xnx3.j2ee.module.pingxx.bean.Event event = getEventByContent(dataString, sign);
//		if(event!=null){
//			if(event.getType().equals(com.xnx3.j2ee.module.pingxx.bean.Event.TYPE_CHARGE_SUCCEEDED)){
//				System.out.println("支付成功的回调，支付成功的订单号："+event.getSmallCharge().getOrderNo());
//			}else if (event.getType().equals(com.xnx3.j2ee.module.pingxx.bean.Event.TYPE_REFUND_SUCCEEDED)) {
//				System.out.println("退款成功的回调，退款成功的订单号："+event.getSmallCharge().getOrderNo());
//			}
//		}
    	
    	String a = "12345";
    	System.out.println(a.substring(0, 2));
    }
    
    /**
     * 生成12位全数字的订单号，10位时间戳＋2位随机数
     * @return
     */
    public static String generateOrderNo(){
    	Random random = new Random();
		return random.nextInt(10)+""+random.nextInt(10)+DateUtil.timeForUnix10();
    }
    
    /**
     * 将 webhooks接收到的json数据传入，获得 {@link com.xnx3.j2ee.module.pingxx.bean.Event}
     * @param dataString  webhooks接收到的json数据
     * @param sign header的签名
     * @return 成功，返回 {@link com.xnx3.j2ee.module.pingxx.bean.Event},失败返回null
     */
    private static com.xnx3.j2ee.module.pingxx.bean.Event getEventByContent(String dataString,String sign){
    	try {
			if(verifyData(dataString, sign, getPubKey())){
				Event ev = Webhooks.eventParse(dataString);
				com.xnx3.j2ee.module.pingxx.bean.Event event = new com.xnx3.j2ee.module.pingxx.bean.Event();
				event.setCreated(ev.getCreated());
				event.setData(ev.getData());
				event.setId(ev.getId());
				event.setLivemode(ev.getLivemode());
				event.setPendingWebhooks(event.getPendingWebhooks());
				event.setType(ev.getType());
				event.setSmallCharge(getSmallChargeByEvent(ev));
				return event;
			}else{
				log("webhook sign verify failure ！require data :"+dataString);
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将webhoos接收到的参数转化，得到 {@link SmallCharge}
     * @param event webhoos接收到的参数转化为的 {@link Event}
     * @return {@link SmallCharge}
     */
    public static SmallCharge getSmallChargeByEvent(Event event){
    	JSONObject j = JSONObject.fromObject(event.getData().getObject());
    	
    	SmallCharge sc = new SmallCharge();
    	sc.setOrderNo(j.getString("orderNo"));
    	sc.setChannel(j.getString("channel"));
    	sc.setClientIp(j.getString("clientIp"));
    	sc.setAmount(j.getInt("amount"));
    	return sc;
    }
    
    /**
     * 创建 Charge
     * <p/>
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
     *
     * @param amount   金额，单位分
     * @param subject  商品的标题，32字节
     * @param body     商品的描述信息，128个字节
     * @param orderNo  商户订单号，(alipay: 1-64 位， wx: 1-32 位，bfb: 1-20 位，upacp: 8-40 位，yeepay_wap:1-50 位，jdpay_wap:1-30 位，推荐使用 8-20 位，要求数字或字母，不允许特殊字符)
     * @param channel  支付使用的第三方支付渠道：
     * 				<ul>
     *                 <li>{@link SmallCharge#CHANNEL_ALIPAY}:支付宝手机支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_ALIPAY_WAP}:支付宝手机网页支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_ALIPAY_QR}:支付宝扫码支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_APPLEPAY_UPACP}:Apple Pay</li>
     *                 <li>{@link SmallCharge#CHANNEL_BFB}:百度钱包移动快捷支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_BFB_WAP}:百度钱包手机网页支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_UPACP}:银联全渠道支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）</li>
     *                 <li>{@link SmallCharge#CHANNEL_UPACP_WAP}:银联全渠道手机网页支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）</li>
     *                 <li>{@link SmallCharge#CHANNEL_UPACP_PC}:银联 PC 网页支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_WX}:微信支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_WX_PUB}:微信公众账号支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_WX_PUB_QR}:微信公众账号扫码支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_YEEPAY_WAP}:易宝手机网页支付</li>
     *                 <li>{@link SmallCharge#CHANNEL_JDPAY_WAP}:京东手机网页支付</li>
     *              </ul>
     * @param clientIp 发起支付请求终端的 IP 地址
     * @return 若成功，返回 {@link Charge}，若失败，返回null
     */
    public static Charge createCharge(Integer amount, String subject, String body, String orderNo, String channel, String clientIp) {
		if(body == null || body.length()==0){
			body=subject;
		}
		if(subject.length()>16){
			subject = subject.substring(0, 16);
		}
		
		if(body.length()>64){
			body = body.substring(0, 64);
		}
    	
    	Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);
        chargeMap.put("channel", channel);
        chargeMap.put("client_ip", clientIp);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeMap.put("app", app);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
            return charge;
        } catch (PingppException e) {
            e.printStackTrace();
            log("PingplusUtil charge Exception !!!"+e.getMessage());
            return null;
        }
    }
    
    /**
     * ping++异步回调传回的参数，直接调用此方法，拿到值
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return 若成功，返回 {@link com.xnx3.j2ee.module.pingxx.bean.Event} 若失败，返回null
     * @throws IOException
     */
    public static com.xnx3.j2ee.module.pingxx.bean.Event webhooks(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	request.setCharacterEncoding("UTF8");
        // 获得 http body 内容
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String string;
        while ((string = reader.readLine()) != null) {
            buffer.append(string);
        }
        reader.close();
        
        String sign = request.getHeader("X-Pingplusplus-Signature");	//签名
        String dataString = buffer.toString();
        com.xnx3.j2ee.module.pingxx.bean.Event event = getEventByContent(dataString, sign);
    	return event;
    }
    
    /**
	 * 获得公钥
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPubKey() throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
		// generate public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pk = keyFactory.generatePublic(spec);
		return pk;
	}

	/**
	 * 验证签名
	 * @param dataString
	 * @param signatureString
	 * @param publicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
		if(signatureString != null){
			byte[] signatureBytes = Base64.decodeBase64(signatureString);
	        Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initVerify(publicKey);
			signature.update(dataString.getBytes("UTF-8"));
			return signature.verify(signatureBytes);
		}else{
			return false;
		}
	}
    
	/**
	 * 日志打印
	 * @param content 日志内容
	 */
	private static void log(String content){
		if(debug){
			System.out.println("pingxx:"+content);
		}
	}
}
