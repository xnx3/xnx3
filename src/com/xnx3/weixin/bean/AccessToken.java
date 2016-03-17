package com.xnx3.weixin.bean;

/**
 * 公众平台的API调用所需的普通access_token
 * @author 管雷鸣
 */
public class AccessToken {
	//获取到的凭证
	private String access_token;
	
	//凭证有效时间，单位：秒
	private int expires_in;
	
	//当前access_token获取的时间，Linux时间戳
	private int gainTime;	
	
	/**
	 * 获取到的凭证
	 * @return access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * 凭证有效时间，单位：秒
	 * @return expires_in
	 */
	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * 当前access_token获取的时间，Linux时间戳
	 * @return gainTime
	 */
	public int getGainTime() {
		return gainTime;
	}

	public void setGainTime(int gainTime) {
		this.gainTime = gainTime;
	}
	
	
}
