package com.xnx3.media;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * 文字转语音工具类
 * 依赖 jl1.0.1.jar
 * @author 管雷鸣
 *
 */
public class TTSUtil {
	private Player player;

	/**
	 * 输入文字以及发音速度，女声读出
	 * <li>需联网。
	 * <li>会阻塞当前线程
	 * @param text 要读的文字，建议80个汉字以内，
	 * 				<li><i>标点符号注意：</i>逗号、句号、分号等常用符号没问题，其他特殊符号自行测试。有的会终止阅读甚至报错！
	 * @param mode 阅读的快慢，越小发音越慢，参数范围1-5
	 */
	public void speak(String text,int mode){
		URL url = null;
		try {
			url = new URL("http://tts.baidu.com/text2audio?lan=zh&pid=101&ie=UTF-8&text="+text+"&spd="+mode);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// 打开连接
		HttpURLConnection httpConnection = null;
		try {
			httpConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
		String sProperty = "bytes=" + 0 + "-";
		// 告诉服务器book.rar这个文件从nStartPos字节开始传
		httpConnection.setRequestProperty("RANGE", sProperty);
		InputStream input = null;
		try {
			input = httpConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        try {
			player = new Player(input);
			player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		
		httpConnection.disconnect();
	}
	
	/**
	 * 输入文字，以女声读出
	 * @param text 要发音的文字，建议一句话以内，不要太长
	 * @see #speak(String, int)
	 */
	public void speak(String text){
		speak(text, 4);
	}
	
	/**
	 * 输入文字，以女声读出。此单独开启一个线程提供阅读，不阻碍当前线程执行。需注意可能同时多个发音重叠问题。
	 * @param text 要发音的文字，建议一句话以内，不要太长
	 * @see #speak(String, int)
	 */
	public static void speakByThread(final String text){
		new Thread(new Runnable() {
			@Override
			public void run() {
				new TTSUtil().speak(text);
			}
		}).start();
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException, JavaLayerException {
		new TTSUtil().speak("哈哈哈哈哈上");
		TTSUtil.speakByThread("管雷鸣");
	}
	
}
