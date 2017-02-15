package com.xnx3.net;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.xnx3.BaseVO;

/**
 * 阿里云短信发送
 * 需Jar包：
 * <br/>aliyun-java-sdk-core-2.4.3.jar
 * <br/>aliyun-java-sdk-sms-3.0.0-rc1.jar
 * @author 管雷鸣
 * @see https://help.aliyun.com/document_detail/44364.html
 */
public class AliyunSMSUtil {
	public String regionId;
	public String accessKeyId;
	public String accessKeySecret;
	
	/*短信具体每条发送的相关参数*/
	public String signName;
	public String templateCode;
	public String paramString;
	public String RecNum;
	
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
	 * 设置发送短信的参数。设置完毕后可以直接通过 {@link #send()}发送
	 * <br/>等同于直接调用 {@link #send(String, String, String, String)}进行发送
	 * 	<pre>sms.send("网市场","SMS_40000000","{\"code\":\"123456\"}","18711111111");</pre>
	 * @param signName 控制台创建的签名名称（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Sign
	 * @param templateCode 控制台创建的模板CODE（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Template
	 * @param paramString 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。 例如:短信模板为：“接受短信验证码${no}”,此参数传递{“no”:”123456”}，用户将接收到[短信签名]接受短信验证码123456，传入的字符串为JSON格式
	 * @param RecNum 目标手机号，多个手机号可以逗号分隔
	 */
	public void setSendParam(String signName,String templateCode,String paramString, String RecNum){
		
	}
	
	/**
	 * 发送短信
	 * <br/><b>前提是已经设置了要发送短信的相关参数 {@link #setSendParam(String, String, String, String)}</b>
	 * <br/>等同于直接调用 {@link #send(String, String, String, String)}进行发送
	 * @return {@link BaseVO} 
	 * 			<ul>
	 * 				<li>若成功，返回 {@link BaseVO#SUCCESS}，此时可以用 {@link BaseVO#getInfo()} 拿到其requestId</li>
	 * 				<li>若失败，返回 {@link BaseVO#FAILURE}，此时可以用 {@link BaseVO#getInfo()} 拿到其错误原因(catch的抛出的异常名字)</li>
	 * 			</ul>
	 */
	public BaseVO send() {
		return null;
	}
	
	/**
	 * 发送短信，如
	 * 	<pre>sms.send("网市场","SMS_40000000","{\"code\":\"123456\"}","18711111111");</pre>
	 * @param signName 控制台创建的签名名称（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Sign
	 * @param templateCode 控制台创建的模板CODE（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Template
	 * @param paramString 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。 例如:短信模板为：“接受短信验证码${no}”,此参数传递{“no”:”123456”}，用户将接收到[短信签名]接受短信验证码123456，传入的字符串为JSON格式
	 * @param RecNum 目标手机号，多个手机号可以逗号分隔
	 * @return {@link BaseVO} 
	 * 			<ul>
	 * 				<li>若成功，返回 {@link BaseVO#SUCCESS}，此时可以用 {@link BaseVO#getInfo()} 拿到其requestId</li>
	 * 				<li>若失败，返回 {@link BaseVO#FAILURE}，此时可以用 {@link BaseVO#getInfo()} 拿到其错误原因(catch的抛出的异常名字)</li>
	 * 			</ul>
	 */
	public BaseVO send(String signName,String templateCode,String paramString, String RecNum) {
		BaseVO vo = new BaseVO();
        IClientProfile profile = DefaultProfile.getProfile(this.regionId,this.accessKeyId,this.accessKeySecret);
        try {
			DefaultProfile.addEndpoint(this.regionId, this.regionId, "Sms",  "sms.aliyuncs.com");
		} catch (ClientException e1) {
			e1.printStackTrace();
			vo.setBaseVO(BaseVO.FAILURE, e1.getMessage());
			return vo;
		}
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendSmsRequest request = new SingleSendSmsRequest();
        try {
            request.setSignName(signName);	//控制台创建的签名名称
            request.setTemplateCode(templateCode);//控制台创建的模板CODE
            request.setParamString(paramString);	//json格式的变量
            request.setRecNum(RecNum);			//发送至的手机号
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            vo.setBaseVO(BaseVO.SUCCESS, httpResponse.getRequestId());
			return vo;
        } catch (ServerException e) {
            e.printStackTrace();
            vo.setBaseVO(BaseVO.FAILURE, e.getMessage());
			return vo;
        }catch (ClientException e) {
            e.printStackTrace();
            vo.setBaseVO(BaseVO.FAILURE, e.getMessage());
			return vo;
        }
    }
	
	public static void main(String[] args) {
		AliyunSMSUtil sms = new AliyunSMSUtil("cn-hongkong", "...", "...");
		sms.send("网市场","SMS_44315000","{\"code\":\"12345s6\"}","17076012262");
	}
}
