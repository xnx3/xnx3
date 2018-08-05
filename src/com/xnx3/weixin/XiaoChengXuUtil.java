package com.xnx3.weixin;

import net.sf.json.JSONObject;

import com.xnx3.BaseVO;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;
import com.xnx3.weixin.vo.Jscode2sessionResultVO;

/**
 * 微信小程序
 * @author 管雷鸣
 */
public class XiaoChengXuUtil {
	private static String appId; 	//appId 小程序的 app_id
	private static String appSecret;	//appSecret 小程序的 app_secret
	
	/**
	 * 初始化
	 * @param appId 小程序的 app_id
	 * @param appSecret 小程序的 app_secret
	 */
	public XiaoChengXuUtil(String appId, String appSecret) {
		XiaoChengXuUtil.appId = appId;
		XiaoChengXuUtil.appSecret = appSecret;
	}
	
	/**
	 * https://api.weixin.qq.com/sns/jscode2session 根据code ，获取 openid、session_key 、 unionid
	 * @param code 微信小程序端，通过js获取到的code，也就是 wx.login 获取到的 code 
	 * @return {@link Jscode2sessionResultVO} 微信jscode2session接口所返回的结果。
	 * 	<ul>
	 * 		<li>result : {@link Jscode2sessionResultVO}.FAILURE 调用失败，通过 .getInfo() 返回失败结果</li>
	 * 		<li>result : {@link Jscode2sessionResultVO}.SUCCESS 调用成功。即可正常获得 openid、session_key 等</li>
	 * 	</ul>
	 */
	public static Jscode2sessionResultVO jscode2session(String code){
		Jscode2sessionResultVO vo = new Jscode2sessionResultVO();
		
		HttpsUtil https = new HttpsUtil();
		HttpResponse hr = https.get("https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code");
		if(hr.getCode() - 200 == 0 && hr.getContent() != null && hr.getContent().indexOf("session_key") > -1){
			JSONObject json = JSONObject.fromObject(hr.getContent());
			vo.setOpenid(json.get("openid") == null? "":json.getString("openid"));
			vo.setUnionid(json.get("unionid") == null ? "":json.getString("unionid"));
			vo.setSessionKey(json.get("session_key") == null? "":json.getString("session_key"));
		}else{
			vo.setBaseVO(BaseVO.FAILURE, hr.getContent());
		}
		return vo;
	}
	 
	public static void main(String[] args) {
		
		
	}
	
}
