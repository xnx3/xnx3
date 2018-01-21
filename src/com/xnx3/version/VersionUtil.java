package com.xnx3.version;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;

/**
 * 版本更新相关的工具类
 * @author 管雷鸣
 */
public class VersionUtil {
	
	/**
	 * 云端版本对比，判断当前程序是否跟服务器上url配置的版本号一致。是否需要进行升级操作
	 * @param url 服务器进行版本检测判断的url地址，以拉取最新的版本号进行判断。该url的源代码如：
	 * 				<pre>2.0|https://github.com/xnx3/iw</pre>
	 * 				版本号|更新查看的url地址
	 * @param currentVersion 传入当前程序的版本号，跟上面url配置的版本号对比，判断是否已经有最新版本了
	 * @return {@link VersionVO#isFindNewVersion()}:true 则是有新版本，发现了新版本
	 */
	public static VersionVO cloudContrast(String url, String currentVersion){
		VersionVO vo = new VersionVO();
		
		HttpUtil http = new HttpUtil();
		HttpResponse hr = http.get(url);
		String text = hr.getContent();
		if(text != null){
			String s[] = text.split("\\|");
			if(s.length > 1){
				if(!s[0].equals(currentVersion)){
					//有新版本，进行提示
					vo.setNewVersion(s[0]);
					vo.setPreviewUrl(s[1]);
					vo.setFindNewVersion(true);
					return vo;
				}
			}
		}
		
		return vo;
	}
	
	
	
}
