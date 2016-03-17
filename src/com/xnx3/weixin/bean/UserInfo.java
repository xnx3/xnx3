package com.xnx3.weixin.bean;

/**
 * 获取到的微信的用户信息
 * @author 管雷鸣
 *
 */
public class UserInfo {
	//用户是否订阅该公众号标识，值为false时，代表此用户没有关注该公众号，拉取不到以下其余信息。
	private boolean subscribe;
	//用户的标识，对当前公众号唯一
	private String openid;
	//用户关注时间，为Linux时间戳,10位。如果用户曾多次关注，则取最后关注时间
	private int subscribeTime;
	// 昵称
	private String nickname;
	// 用户的性别（1是男性，2是女性，0是未知）
	private int sex;
	// 用户所在国家
	private String country;
	// 用户所在省份
	private String province;
	// 用户所在城市
	private String city;
	// 用户的语言，简体中文为zh_CN
	private String language;
	// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	private String headImgUrl;
	
	/**
	 * 用户的标识，对当前公众号唯一
	 * @return openid
	 */
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * 用户是否订阅该公众号标识，值为false时，代表此用户没有关注该公众号，拉取不到其余信息。
	 * @return subscribe
	 */
	public boolean isSubscribe() {
		return subscribe;
	}
	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}
	/**
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	 * @return subscribeTime
	 */
	public int getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(int subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	/**
	 * 用户的昵称
	 * @return nickname
	 */
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 * @return sex
	 */
	public int getSex() {
		return sex;
	}
	
	/**
	 * 用户的性别
	 * @return <li>男
	 * 			<li>女
	 * 			<li>未知
	 */
	public String getSexInfo(){
		if(sex == 1){
			return "男";
		}else if (sex == 2) {
			return "女";
		}else{
			return "未知";
		}
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	/**
	 * 用户所在国家
	 * @return country
	 */
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * 用户所在省份
	 * @return province
	 */
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	/**
	 * 用户所在城市
	 * @return city
	 */
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 用户的语言，简体中文为zh_CN
	 * @return language
	 */
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	 * @return headImgUrl
	 */
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	@Override
	public String toString() {
		return "UserInfo [subscribe=" + subscribe + ", openid=" + openid
				+ ", subscribeTime=" + subscribeTime + ", nickname=" + nickname
				+ ", sex=" + sex + ", country=" + country + ", province="
				+ province + ", city=" + city + ", language=" + language
				+ ", headImgUrl=" + headImgUrl + "]";
	}
	
}
