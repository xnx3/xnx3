package com.xnx3.weixin;

import net.sf.json.JSONObject;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.weixin.bean.AccessToken;
import com.xnx3.weixin.bean.UserInfo;

/**
 * 微信基本操作
 * @author 管雷鸣
 * <br><b>需导入</b> 
 * <br/><i>commons-configuration-1.7.jar</i>
 * <br/><i>commons-collections-3.2.1.jar</i>
 * <br/><i>commons-io-1.3.2.jar</i>
 * <br/><i>commons-lang-2.5.jar</i>
 * <br/><i>commons-logging-1.2.jar</i>
 * <br/><i>commons-beanutils-1.8.0.jar</i>
 * <br/><i>ezmorph-1.0.6.jar</i>
 * <br/><i>json-lib-2.4-jdk15.jar</i>
 */
public class WeiXinUtil {
	private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";	//获取普通access_token的url
	private final static String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";	//获取用户个人信息的url
	private final static String OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";	//网页授权跳转的url
	private final static String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";	//网页授权，获取access_token
	private final static String OAUTH2_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";	//网页授权，获取用户信息
	private final static int ACCESS_TOKEN_DELAY_TIME = 10;	//access_token获取向前延迟10秒，expires_in-10
	private static boolean debug;	//调试日志是否打印
	private static AccessToken accessToken;	//持久化access_token数据
	private static String AppId;	//AppID(应用ID)
	private static String AppSecret;	//AppSecret(应用密钥)
	
	static{
		AppId = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("weixin.AppId");
		AppSecret = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("weixin.AppSecret");
		debug = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("weixin.debug").equals("true");
	}
	
	/**
	 * 获取最新的普通access_token
	 * @return AccessToken 若返回null，则获取access_token失败
	 */
	public static AccessToken getAccessToken(){
		boolean refreshToken = false;	//需重新刷新获取token，默认是不需要
		
		if(accessToken == null){
			accessToken = new AccessToken();
			refreshToken = true;
		}
		
		//是否过时，需要重新获取token
		if(DateUtil.timeForUnix10()>accessToken.getGainTime()+accessToken.getExpires_in()+ACCESS_TOKEN_DELAY_TIME){
			refreshToken = true;
		}
		
		//避免一次可能网络中断，连续获取三次，减小误差
		boolean success = !refreshToken;
		int i = 0;
		for (; i < 3 && !success ; i++) {
			success = refreshAccessToken();
		}
		
		if(!success){
			debug("连续获取"+i+"次access_token，均失败！" );
			return null;
		}else{
			return accessToken;
		}
	}
	
	
	
	/**
	 * 通过openId，获取用户的信息
	 * @param openId 普通用户的标识，对当前公众号唯一
	 * @return UserInfo	<li>若返回null，则获取失败
	 * 					<li>若不为null，先判断其subscribe，若为true，已关注，则可以取到其他的信息
	 */
	public static UserInfo getUserInfo(String openId){
		HttpUtil httpUtil = new HttpUtil();
		UserInfo userInfo = null;
		HttpResponse httpResponse = httpUtil.get(USER_INFO_URL.replace("ACCESS_TOKEN", getAccessToken().getAccess_token()).replace("OPENID", openId));
		JSONObject json = JSONObject.fromObject(httpResponse.getContent());
		if(json.get("errcode") != null){
			userInfo = new UserInfo();
			userInfo.setSubscribe(json.getString("subscribe").equals("1"));
			if(userInfo.isSubscribe()){
				userInfo.setCity(json.getString("city"));
				userInfo.setCountry(json.getString("country"));
				userInfo.setHeadImgUrl(json.getString("headimgurl"));
				userInfo.setLanguage(json.getString("language"));
				userInfo.setNickname(json.getString("nickname"));
				userInfo.setOpenid(json.getString("openid"));
				userInfo.setProvince(json.getString("province"));
				userInfo.setSex(json.getInt("sex"));
				userInfo.setSubscribeTime(json.getInt("subscribe_time"));
			}
		}else{
			debug("获取用户信息失败！用户openid:"+openId+"，微信回执："+httpResponse.getContent());
		}
		
		return userInfo;
	}
	
	/**
	 * 刷新重新获取access_token
	 * @return 获取成功|失败
	 */
	private static boolean refreshAccessToken(){
		HttpUtil httpUtil = new HttpUtil();
		HttpResponse httpResponse = httpUtil.get(ACCESS_TOKEN_URL.replace("APPID", AppId).replace("APPSECRET", AppSecret));
		JSONObject json = JSONObject.fromObject(httpResponse.getContent());
		if(json.get("errcode") == null){
			//没有出错，获取access_token成功
			accessToken.setAccess_token(json.getString("access_token"));
			accessToken.setExpires_in(json.getInt("expires_in"));
			return true;
		}else{
			debug("获取access_token失败！返回值："+httpResponse.getContent());
			return false;
		}
	}
	
	/**
	 * 获取网页授权的URL跳转地址
	 * @param redirectUri 授权后重定向的回调链接地址，无需URL转码，原始url
	 * @param scope 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
	 * @param state 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 * @return url地址
	 */
	public static String getOauth2Url(String redirectUri,String scope,String state){
		return OAUTH2_URL.replace("APPID", AppId).replace("REDIRECT_URI", Lang.stringToUrl(redirectUri)).replace("SCOPE", scope).replace("STATE", state);
	}
	
	/**
	 * 获取网页授权的URL跳转地址，弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
	 * @param redirectUri 授权后重定向的回调链接地址，无需URL转码，原始url
	 * @return url地址
	 */
	public static String getOauth2SimpleUrl(String redirectUri){
		return getOauth2Url(redirectUri, "snsapi_userinfo", "STATE");
	}
	
	/**
	 * 获取网页授权的URL跳转地址，不会出现授权页面，只能拿到用户openid
	 * @param redirectUri 授权后重定向的回调链接地址，无需URL转码，原始url
	 * @return url地址
	 */
	public static String getOauth2ExpertUrl(String redirectUri){
		return getOauth2Url(redirectUri, "snsapi_base", "STATE");
	}
	
	/**
	 * 获取网页授权，获取用户的openid
	 * @param code 如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE，授权成功会get方式传过来
	 * @return 用户openid 若为null，则获取失败
	 */
	public static String getOauth2OpenId(String code){
		HttpUtil httpUtil = new HttpUtil();
		UserInfo userInfo = null;
		HttpResponse httpResponse = httpUtil.get(OAUTH2_ACCESS_TOKEN_URL.replace("APPID", AppId).replace("SECRET", AppSecret).replace("CODE", code));
		JSONObject json = JSONObject.fromObject(httpResponse.getContent());
		if(json.get("errcode") == null){
			//没有出错，获取网页access_token成功
			return json.getString("openid");
		}else{
			debug("获取网页授权access_token失败！返回值："+httpResponse.getContent());
		}
		
		return null;
	}
	
	/**
	 * 网页授权获取用户的个人信息
	 * @param code 如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE，授权成功会get方式传过来
	 * @return	<li>若成功，返回{@link UserInfo} (无 subscribeTime 项)
	 * 			<li>若失败，返回null
	 */
	public static UserInfo getOauth2UserInfo(String code){
		HttpUtil httpUtil = new HttpUtil();
		HttpResponse httpResponse = httpUtil.get(OAUTH2_ACCESS_TOKEN_URL.replace("APPID", AppId).replace("SECRET", AppSecret).replace("CODE", code));
		JSONObject json = JSONObject.fromObject(httpResponse.getContent());
		if(json.get("errcode") == null){
			//没有出错，获取网页access_token成功
			HttpResponse res = httpUtil.get(OAUTH2_USER_INFO_URL.replace("ACCESS_TOKEN", json.getString("access_token")).replace("OPENID", json.getString("openid")));
			JSONObject j = JSONObject.fromObject(res.getContent());
			if(j.get("errcode") == null){
				UserInfo userInfo = new UserInfo();
				userInfo.setCity(j.getString("city"));
				userInfo.setOpenid(j.getString("openid"));
				userInfo.setNickname(j.getString("nickname"));
				userInfo.setSex(j.getInt("sex"));
				userInfo.setProvince(j.getString("province"));
				userInfo.setCountry(j.getString("country"));
				userInfo.setHeadImgUrl(j.getString("headimgurl"));
				userInfo.setLanguage("zh_CN");
				return userInfo;
			}else{
				debug("获取网页授权用户信息失败！返回值："+res.getContent());
			}
		}else{
			debug("获取网页授权access_token失败！返回值："+httpResponse.getContent());
		}
		
		return null;
	}
	
	/**
	 * 调试日志打印
	 * @param message 日志内容
	 */
	private static void debug(String message){
		if(debug){
			System.out.println("WeiXinUtil:"+message);
		}
	}

}
