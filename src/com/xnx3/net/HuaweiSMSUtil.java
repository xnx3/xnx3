package com.xnx3.net;

//如果JDK版本低于1.8,请使用三方库提供Base64类
//import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import com.xnx3.BaseVO;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//如果JDK版本是1.8,可使用原生Base64类
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 华为云短信发送.
 * 短信开通：  http://huawei.leimingyun.com/
 * @author 管雷鸣
 *
 */
public class HuaweiSMSUtil {
	private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";	//无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
	private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";	//无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
	private static final String url = "https://rtcsms.cn-north-1.myhuaweicloud.com:10743/sms/batchSendSms/v1"; //APP接入地址+接口访问URI
	private String appKey;		//短信应用的appKey
	private String appSecret;	//短信应用的appSecret
	private String sender;		//国内短信签名通道号或国际/港澳台短信通道号
	private String signature;	//签名
	
	/**
	 * 华为云短信发送
	 * 短信开通：  http://huawei.leimingyun.com/
	 * @param appKey 短信应用的appKey
	 * @param appSecret 短信应用的appSecret
	 * @param sender 国内短信签名通道号或国际/港澳台短信通道号
	 * @param signature 短信签名
	 */
	public HuaweiSMSUtil(String appKey, String appSecret, String sender, String signature) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.sender = sender;
		this.signature = signature;
	}
	
	/**
	 * 发送短信
	 * @param phone 要发送的手机号。可以传入 +8618788888888 ，也可以不带+86，接口里面会自动加上
	 * @param templateParas 模板变量。
	 * 	选填,使用无变量模板时请赋空值 String templateParas = "";
     * 单变量模板示例:模板内容为"您的验证码是${1}"时,templateParas可填写为"[\"369751\"]"
     * 双变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
     * 模板中的每个变量都必须赋值，且取值不能为空
     * 查看更多模板和变量规范:产品介绍>模板和变量规范
     * @param templateId 模板ID
     * @return {@link BaseVO} result = 1 则是发送成功， =0则是失败，用getInfo() 获取失败原因
	 */
	public BaseVO send(String phone, String templateId, String templateParas){
		if(phone.indexOf("+") != 0){
			phone = "+86"+phone;
		}
		
		//选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
		String statusCallBack = "";
		
		
		//请求Body,不携带签名名称时,signature请填null
		String body = buildRequestBody(sender, phone, templateId, templateParas, statusCallBack, signature);
	      if (null == body || body.isEmpty()) {
	          return BaseVO.failure("body is null.");
	      }

	      //请求Headers中的X-WSSE参数值
	      String wsseHeader = buildWsseHeader(appKey, appSecret);
	      if (null == wsseHeader || wsseHeader.isEmpty()) {
	          return BaseVO.failure("wsse header is null.");
	      }

	      //如果JDK版本低于1.8,可使用如下代码
	      //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
	      //CloseableHttpClient client = HttpClients.custom()
	      //        .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
	      //            @Override
	      //            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	      //                return true;
	      //            }
	      //        }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

	      //如果JDK版本是1.8,可使用如下代码
	      //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
	      CloseableHttpClient client = null;
		try {
			client = HttpClients.custom()
			              .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
			                      (x509CertChain, authType) -> true).build())
			              .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
			              .build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		}

		HttpResponse response;
		try {
			response = client.execute(RequestBuilder.create("POST")//请求方法POST
			              .setUri(url)
			              .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
			              .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
			              .addHeader("X-WSSE", wsseHeader)
			              .setEntity(new StringEntity(body)).build());
			String responseContent = EntityUtils.toString(response.getEntity());
			if(responseContent != null){
				JSONObject json = JSONObject.fromObject(responseContent);
				if(json.get("code") != null && json.getString("code").equals("000000")){
					return BaseVO.success("success");
				}
			}
			return BaseVO.failure(responseContent);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		}
	}
	

 
  public static void main(String[] args) throws Exception{
	  String appKey = "3RwC21LkS2xxxxxxxxxx";
      String appSecret = "IWLr67lg4TshegBTlxxxxxxxxxxxx";
      String sender = "10690400999000000000"; //国内短信签名通道号或国际/港澳台短信通道号
      String signature = "华为云短信测试"; //签名名称
      HuaweiSMSUtil sms = new HuaweiSMSUtil(appKey, appSecret, sender, signature);
	  sms.send("17000000000", "58972990fb1b4b16abf312d991a00e00", "[\"369751\"]");
	  
  }

  /**
   * 构造请求Body体
   * @param sender
   * @param receiver
   * @param templateId
   * @param templateParas
   * @param statusCallbackUrl
   * @param signature | 签名名称,使用国内短信通用模板时填写
   * @return
   */
  static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                 String statusCallbackUrl, String signature) {
      if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
              || templateId.isEmpty()) {
          System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
          return null;
      }
      List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

      keyValues.add(new BasicNameValuePair("from", sender));
      keyValues.add(new BasicNameValuePair("to", receiver));
      keyValues.add(new BasicNameValuePair("templateId", templateId));
      if (null != templateParas && !templateParas.isEmpty()) {
          keyValues.add(new BasicNameValuePair("templateParas", templateParas));
      }
      if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
          keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
      }
      if (null != signature && !signature.isEmpty()) {
          keyValues.add(new BasicNameValuePair("signature", signature));
      }

      return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
  }

  /**
   * 构造X-WSSE参数值
   * @param appKey
   * @param appSecret
   * @return
   */
  static String buildWsseHeader(String appKey, String appSecret) {
      if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
          System.out.println("buildWsseHeader(): appKey or appSecret is null.");
          return null;
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      String time = sdf.format(new Date()); //Created
      String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

      byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
      String hexDigest = Hex.encodeHexString(passwordDigest);

      //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
      String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
      //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
      //String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8"))); //PasswordDigest
      //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
      //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");

      return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
  }
}