package com.xnx3.weixin.bean;

/**
 * 接收消息，用户通过微信公众号发送消息，有服务器接收
 * @author 管雷鸣
 */
public class MessageReceive {
	private String receiveBody;	//微信服务器给我们发送的消息体
	
	private String toUserName;	//开发者微信号
	private String fromUserName;	//发送方帐号（一个OpenID）
	private Integer createTime;	//消息创建时间 （整型）10位Linux时间戳
	private String msgType;		//类型，如text、image
	private String msgId;			//消息id，64位整型
	
	/*文本消息， msgType=text */
	private String content;		//文本消息内容
	
	/*图片消息， msgType=image */
	private String picUrl;		//图片链接（由系统生成）
	private String mediaId;	//图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
	
	/*语音消息， msgType=voice */
	private String format;	//语音格式，如amr，speex等
//	private String mediaId;	//语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	
	/*视频消息， msgType=video  或者小视频 shortvideo */
	private String thumbMediaId;	//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	
	/*链接消息， msgType=link */
	private String title;	//消息标题
	private String description;	//消息描述
	private String url;	//消息链接
	
	/*关注、取消关注*/
	private String event;
	
	/**
	 * 消息类型，link，超链接
	 */
	public final static String MSGTYPE_LINK = "link";
	/**
	 * 消息类型，text，文本消息
	 */
	public final static String MSGTYPE_TEXT = "text";
	/**
	 * 消息类型，image,图片消息
	 */
	public final static String MSGTYPE_IMAGE = "image";
	/**
	 * 消息类型，voice，语音消息
	 */
	public final static String MSGTYPE_VIOCE = "voice";
	/**
	 * 消息类型，vide，视频消息
	 */
	public final static String MSGTYPE_VIDEO = "video";
	/**
	 * 消息类型，shortvideo，小视频
	 */
	public final static String MSGTYPE_SHORT_VIDEO = "shortvideo";
	/**
	 * 消息类型，location，地理位置消息
	 */
	public final static String MSGTYPE_LOCATION = "location";
	/**
	 * 消息类型，event,关注/取消关注事件
	 */
	public final static String MSGTYPE_EVENT = "event";
	
	/**
	 * 事件类型，subscribe(订阅)
	 */
	public final static String EVENT_SUBSCRIBE = "subscribe";
	/**
	 * 事件类型，unsubscribe(取消订阅)
	 */
	public final static String EVENT_UNSUBSCRIBE = "unsubscribe";
	
	
	/**
	 * 开发者（接收方）微信号，所有类型消息都有
	 */
	public String getToUserName() {
		return toUserName;
	}
	
	/**
	 * 开发者（接收方）微信号，所有类型消息都有
	 * @param toUserName
	 */
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	
	/**
	 * 发送方帐号（一个OpenID），所有类型消息都有
	 */
	public String getFromUserName() {
		return fromUserName;
	}
	
	/**
	 * 发送方帐号（一个OpenID），所有类型消息都有
	 */
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	
	/**
	 * 消息创建时间 （整型）10位Linux时间戳，所有类型消息都有
	 */
	public int getCreateTime() {
		return createTime;
	}
	/**
	 * 消息创建时间 （整型）10位Linux时间戳，所有类型消息都有
	 */
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	/**
	 * 消息类型，如text、image，所有类型消息都有，可通过 {@link #MSGTYPE_TEXT}来判断类型是否是某一种
	 */
	public String getMsgType() {
		return msgType;
	}
	/**
	 * 消息类型，如text、image，所有类型消息都有，可通过 {@link #MSGTYPE_TEXT}来设置类型
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	/**
	 * 消息id，64位整型，所有类型消息都有
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * 消息id，64位整型，所有类型消息都有
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * 文本消息内容，只限 {@link #MSGTYPE_TEXT}文本消息类型才会有这个值
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 文本消息内容，只限 {@link #MSGTYPE_TEXT}文本消息类型才会有这个值
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 图片链接（由系统生成）,只限 {@link #MSGTYPE_IMAGE}图片消息类型才会有这个值
	 */
	public String getPicUrl() {
		return picUrl;
	}
	/**
	 * 图片链接（由系统生成）,只限 {@link #MSGTYPE_IMAGE}图片消息类型才会有这个值
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 返回值的是否订阅，可用 {@value #EVENT_SUBSCRIBE} {@value #EVENT_UNSUBSCRIBE} 来进行判断
	 */
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	/**
	 * 微信服务器给我们发送的消息体
	 */
	public String getReceiveBody() {
		return receiveBody;
	}

	/**
	 * 微信服务器给我们发送的消息体
	 */
	public void setReceiveBody(String receiveBody) {
		this.receiveBody = receiveBody;
	}

	@Override
	public String toString() {
		return "MessageReceive [toUserName=" + toUserName + ", fromUserName="
				+ fromUserName + ", createTime=" + createTime + ", msgType="
				+ msgType + ", msgId=" + msgId + ", content=" + content
				+ ", picUrl=" + picUrl + ", mediaId=" + mediaId + ", format="
				+ format + ", thumbMediaId=" + thumbMediaId + ", title="
				+ title + ", description=" + description + ", url=" + url
				+ ", event=" + event + "]";
	}
	
}
