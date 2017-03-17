package com.xnx3.net;

import java.util.Map;

import com.xnx3.ConfigManagerUtil;

import push.AndroidNotification;
import push.PushClient;
import push.UmengNotification;
import push.android.AndroidUnicast;
import push.ios.IOSUnicast;

/**
 * 友盟推送
 * <br/>文档：http://dev.umeng.com/push/android/api-doc
 * <pre>
 * 		//加入配置文件 src/xnx3Config.xml  ，配置其umengPush节点的参数。
 * </pre>
 * <br/><b/>需</b>
 * <br/>commons-configuration-1.7.jar
 * <br/>commons-collections-3.2.1.jar
 * <br/>commons-io-1.3.2.jar
 * <br/>commons-lang-2.5.jar
 * <br/>commons-logging-1.2.jar
 * <br/>umpush.jar
 * @author 管雷鸣
 */
public class UMPushUtil {
	public static String appKey_android;
	public static String appMasterSecret_android;
	public static String appKey_ios;
	public static String appMasterSecret_ios;
	
	static{
		appKey_android = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("umengPush.android.appKey");
		appMasterSecret_android = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("umengPush.android.appMasterSecret");
		appKey_ios = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("umengPush.ios.appKey");
		appMasterSecret_ios = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("umengPush.ios.appMasterSecret");
	}
	
	public static void main(String[] args) {
//		try {
//			AndroidUnicast au = UMPushUtil.createAndroidUnicast();
//			au.setTitle("测试啊");
//			au.setText("哈哈哈哈哈哈哈");
//			au.setDeviceToken("test1111111111111111111111111111111111111111");
//			au.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//			au.setTicker("通知栏标题");
//			new PushClient().send(au);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		unicast("2b56c5f7d28d357777a7de655628cef614591a8c9952f015b2584777d7d7f0eb", "aa", "title", "woshitext", null);
	}
	
	/**
	 * Android 创建针对某台设备单个推送对象
	 * @return {@link AndroidUnicast}
	 * @throws Exception
	 */
	public static AndroidUnicast createAndroidUnicast() throws Exception{
		return new AndroidUnicast(appKey_android, appMasterSecret_android);
	}
	
	/**
	 * IOS 创建针对某台设备单个推送对象
	 * @return {@link IOSUnicast}
	 * @throws Exception
	 */
	public static IOSUnicast createIOSUnicast() throws Exception{
		return new IOSUnicast(appKey_ios, appMasterSecret_ios);
	}
	
	/**
	 * 通过device_token 进行单个设备的通知推送。默认productionMode为true，为正式环境
	 * @param deviceToken 根据此长度决定是发送Android/IOS推送
	 * @param ticker Android有效，通知栏提示文字
	 * @param title Android有效，通知标题
	 * @param text Android、IOS共用，通知内容
	 * @param map 用户自定义key-value。只对"通知"。若不使用可传入null
	 * @see #unicast(String, String, String, String, Map, boolean)
	 */
	public static boolean unicast(String deviceToken,String ticker,String title,String text,Map<String, String> map){
		return unicast(deviceToken, ticker, title, text, map, true);
	}
	
	/**
	 * 通过device_token 进行单个设备的通知推送
	 * @param deviceToken 根据此长度决定是发送Android/IOS推送
	 * @param ticker Android有效，通知栏提示文字
	 * @param title Android有效，通知标题
	 * @param text Android、IOS共用，通知内容
	 * @param map 用户自定义key-value。只对"通知"。若不使用可传入null
	 * @param productionMode 是否是正式环境，若唯true，则是正式环境，false为测试环境
	 */
	public static boolean unicast(String deviceToken,String ticker,String title,String text,Map<String, String> map, boolean productionMode){
		if(deviceToken.length() == 44){
			//Android
			try {
				AndroidUnicast unicast = createAndroidUnicast();
				unicast.setDeviceToken(deviceToken);
				unicast.setTicker(ticker);
				unicast.setTitle(title);
				unicast.setText(text);
				unicast.goAppAfterOpen();
				unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
				// TODO Set 'production_mode' to 'false' if it's a test device. 
				// For how to register a test device, please see the developer doc.
				if(productionMode){
					unicast.setProductionMode();
				}else{
					unicast.setTestMode();
				}
				// Set customized fields
//				unicast.setExtraField("test", "helloworld");
//				unicast.setCustomField("");
				//添加自定义key-value
				if(map != null){
					for (Map.Entry<String, String> entry : map.entrySet()) {
						unicast.setExtraField(entry.getKey(), entry.getValue());
					}
				}
				
				return send(unicast);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(deviceToken.length() == 64){
			//IOS
			try {
				IOSUnicast unicast = createIOSUnicast();
				// TODO Set your device token
				unicast.setDeviceToken(deviceToken);
				unicast.setAlert(text);
				unicast.setBadge( 0);
				unicast.setSound("default");
//				unicast.
				// TODO set 'production_mode' to 'true' if your app is under production mode
				if(productionMode){
					//正式环境
					unicast.setProductionMode();
				}else{
					//测试环境
					unicast.setTestMode();
				}
//				unicast.setTestMode();	
				// Set customized fields
				//添加自定义key-value
				if(map != null){
					for (Map.Entry<String, String> entry : map.entrySet()) {
						unicast.setCustomizedField(entry.getKey(), entry.getValue());
					}
				}
				return send(unicast);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("device_token 不合法："+deviceToken);
		}
		return false;
	}
	
	/**
	 * 进行消息/通知推送
	 * @param msg 要推送的消息对象
	 * @return 
	 * @throws Exception 
	 */
	public static boolean send(UmengNotification msg) throws Exception{
		PushClient client = new PushClient();
		return client.send(msg);
	}
	
}
