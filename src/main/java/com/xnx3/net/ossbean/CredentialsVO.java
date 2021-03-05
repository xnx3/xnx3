package com.xnx3.net.ossbean;

import com.xnx3.BaseVO;

/**
 * 阿里云 临时账户 STS 返回值
 * @author 管雷鸣
 */
public class CredentialsVO extends BaseVO {
	private String expiration;
	private String accessKeyId;
	private String accessKeySecret;
	private String securityToken;
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getSecurityToken() {
		return securityToken;
	}
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
	
	@Override
	public String toString() {
		return "CredentialsVO [expiration=" + expiration + ", accessKeyId="
				+ accessKeyId + ", accessKeySecret=" + accessKeySecret
				+ ", securityToken=" + securityToken + ", getResult()="
				+ getResult() + ", getInfo()=" + getInfo() + "]";
	}
}
