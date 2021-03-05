package com.xnx3.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络相关
 * @author 管雷鸣
 *
 */
public class NetUtil {
	
	/**
	 * 获取本机当前局域网的ip地址
	 * @return 局域网ip地址，如 192.168.1.123 。 若未获取到，则返回 null
	 */
	public static String getLANIp(){
		InetAddress ia;
		try {
			ia = InetAddress.getLocalHost();
			return ia.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}  
	}
}
