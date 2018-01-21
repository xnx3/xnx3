package com.xnx3.net;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.xnx3.BaseVO;

/**
 * 阿里云短信发送
 * 需Jar包：
 * <br/>aliyun-java-sdk-core-2.4.3.jar
 * <br/>aliyun-java-sdk-dysmsapi-1.0.0.jar
 * @author 管雷鸣
 * @see https://help.aliyun.com/document_detail/44364.html
 */
public class AliyunSMSUtil {
	public String regionId;
	public String accessKeyId;
	public String accessKeySecret;
	
	static final String product = "Dysmsapi";	//产品名称:云通信短信API产品,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";	//产品域名,开发者无需替换
	
	/**
	 * 阿里云短信发送配置参数初始化
	 * @param regionId 机房信息，如
	 * 			<ul>
	 * 				<li>cn-hangzhou</li>
	 * 				<li>cn-qingdao</li>
	 * 				<li>cn-hongkong</li>
	 * 			</ul>
	 * @param accessKeyId Access Key Id ， 参见 https://ak-console.aliyun.com/?spm=#/accesskey
	 * @param accessKeySecret Access Key Secret， 参见 https://ak-console.aliyun.com/?spm=#/accesskey
	 */
	public AliyunSMSUtil(String regionId, String accessKeyId, String accessKeySecret) {
		this.regionId = regionId;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
	}
	

    /**
	 * 发送短信，如
	 * 	<pre>sms.send("网市场","SMS_40000000","{\"code\":\"123456\"}","18711111111");</pre>
	 * @param signName 控制台创建的签名名称（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Sign
	 * @param templateCode 控制台创建的模板CODE（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Template
	 * @param paramString 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。 例如:短信模板为：“接受短信验证码${no}”,此参数传递{“no”:”123456”}，用户将接收到[短信签名]接受短信验证码123456，传入的字符串为JSON格式
	 * @param phone 接收短信的手机号
	 * @return {@link BaseVO} 
	 * 			<ul>
	 * 				<li>若成功，返回 {@link BaseVO#SUCCESS}，此时可以用 {@link BaseVO#getInfo()} 拿到其requestId</li>
	 * 				<li>若失败，返回 {@link BaseVO#FAILURE}，此时可以用 {@link BaseVO#getInfo()} 拿到其错误原因(catch的抛出的异常名字)</li>
	 * 			</ul>
	 */
    public BaseVO send(String signName,String templateCode,String templateParamString, String phone) {
    	BaseVO vo = new BaseVO();
    	
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        try {
			DefaultProfile.addEndpoint(regionId, regionId, product, domain);
		} catch (ClientException e1) {
			e1.printStackTrace();
			vo.setBaseVO(BaseVO.FAILURE, e1.getMessage());
			return vo;
		}
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParamString);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        try {
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			vo.setInfo(sendSmsResponse.getRequestId());
		} catch (ServerException e) {
			vo.setBaseVO(BaseVO.FAILURE, e.getMessage());
			e.printStackTrace();
		} catch (ClientException e) {
			vo.setBaseVO(BaseVO.FAILURE, e.getMessage());
			e.printStackTrace();
		}
        
        return vo;
    }
	
	public static void main(String[] args) {
		AliyunSMSUtil sms = new AliyunSMSUtil("cn-hongkong", "...", "...");
		sms.send("网市场","SMS_1234566","{\"code\":\"12345s6\"}","18788888888");
	}
}
