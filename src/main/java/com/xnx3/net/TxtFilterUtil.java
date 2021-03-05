package com.xnx3.net;

import java.util.ArrayList;
import java.util.List;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20160621.TextKeywordFilterRequest;
import com.aliyuncs.green.model.v20160621.TextKeywordFilterResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 文本合法性过滤，使用的阿里绿网
 * @author 管雷鸣
 * <br><b>需导入阿里云相关jar包</b> 
 * <br/>aliyun-java-sdk-core-2.4.3.jar
 * <br/>aliyun-java-sdk-green-1.4.0.jar
 */
public class TxtFilterUtil {
	 IClientProfile profile;
	 IAcsClient client;

	 /**
	  * 文本合法性过滤
	  * @param regionId 哪个区，如 cn-hangzhou 、 cn-qingdao
	  * @param accessKeyId 阿里云的 AccessKeys 查看
	  * @param accessKeySecret
	  */
	public TxtFilterUtil(String regionId, String accessKeyId, String accessKeySecret) {
		//请替换成你自己的accessKeyId、accessKeySecret
        profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        client = new DefaultAcsClient(profile);
	}
	
	/**
	 * 文本合法性过滤
	 * @param text 要检测的字符串
	 * @return 若是跟阿里绿网连接等出问题，会返回 null
	 */
	public TextKeywordFilterResponse filter(String text){
		TextKeywordFilterRequest textKeywordFilterRequest = new TextKeywordFilterRequest();
        //设置待检测文本
        textKeywordFilterRequest.setText(text);
        
        int i = 0;
        while(i++ < 3){
        	try {
    			TextKeywordFilterResponse textKeywordFilterResponse = client.getAcsResponse(textKeywordFilterRequest);
    			return textKeywordFilterResponse;
    		} catch (ServerException e) {
    			e.printStackTrace();
    		} catch (ClientException e) {
    			e.printStackTrace();
    		}
        }
        
        return null;
	}
	
	/**
	 * 判断该字符串中是否有违法字符（若检测时出现问题，如阿里云联网超时等，会返回false）
	 * @param text 要检测的字符串
	 * @return 检测结果。
	 * 		<ul>
	 * 			<li>true：有违法的字符存在</li>
	 * 			<li>false：没有有违法的字符存在</li>
	 * 		</ul>
	 */
	public boolean filterIsIllegal(String text){
		TextKeywordFilterResponse tk = filter(text);
		if(tk != null && "Success".equals(tk.getCode()) && tk.getHit()){
			return tk.getHit();
		}else{
			return false;
		}
	}
	
	/**
	 * 判断该字符串中是否有违法字符，若有，则通过List返回。若没有，则返回空的List
	 * <br/>（若检测时出现问题，如阿里云联网超时等，也会返回空的List）
	 * @param text text 要检测的字符串
	 * @return 检测结果。
	 */
	public List<TextKeywordFilterResponse.KeywordResult> filterGainList(String text){
		TextKeywordFilterResponse tk = filter(text);
		if(tk != null && "Success".equals(tk.getCode()) && tk.getHit()){
			return tk.getKeywordResults();
		}else{
			return new ArrayList<TextKeywordFilterResponse.KeywordResult>();
		}
	}
}
