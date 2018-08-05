package com.xnx3.weixin.vo;

import com.xnx3.BaseVO;

/**
 * 根据 code，获取 session_key、openid、unionid，这里是其接口的返回值
 * @author 管雷鸣
 *
 */
public class Jscode2sessionResultVO extends BaseVO {
	
	private String sessionKey;
	private String openid;
	private String unionid;
	
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	/**
	 * 只有绑定了微信开放平台后，才会有这个返回值
	 * @return
	 */
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	public String toString() {
		return "Jscode2sessionResultVO [sessionKey=" + sessionKey + ", openid="
				+ openid + ", unionid=" + unionid + ", getResult()="
				+ getResult() + ", getInfo()=" + getInfo() + "]";
	}
	
	
}
