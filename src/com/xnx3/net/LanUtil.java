package com.xnx3.net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 局域网通信
 * @author 管雷鸣
 */
public class LanUtil {
	
	/**
	 * 判断这个局域网IP是否是可以访问的
	 * @param ip (192.168.0.101)
	 * @return boolean 
	 * 				<ul>
	 * 					<li>true:可以访问
	 * 				    <li>false:访问不通
	 * 				</ul>
	 */
	public boolean isUsedIPAddress(String ip) {                            
		synchronized (this) {                                   
			// 判断这个 IP 地址在当前局域网中是否是可连接的 IP                                   
			Process process = null;                                   
			BufferedReader bufReader = null;                                   
			String bufReadLineString = null;                                   
			try {
				process = Runtime.getRuntime().exec("ping " + ip + " -w 100 -n 1");
				bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));                                          
				for (int i = 0; i < 6 && bufReader != null; i++) {
					bufReader.readLine();
				}
				bufReadLineString = bufReader.readLine();
				if (bufReadLineString == null) {
					process.destroy();
					return false;
				}
				if (bufReadLineString.indexOf("timed out") > 0||bufReadLineString.length() < 17||bufReadLineString.indexOf("invalid") > 0) {
					process.destroy();
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			process.destroy();
			return true;
		}
	}
}
