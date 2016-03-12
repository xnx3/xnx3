package com.xnx3.microsoft;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;
import com.xnx3.file.FileUtil;
import com.xnx3.media.TTSUtil;

/**
 * TTS语音功能，将TXT文本转化为语音
 * 已废弃，建议使用 {@link TTSUtil#speak(String, int)}
 * <pre>
 * 		Com com=new Com();
 *		Tts tts=new Tts(com.getActiveXComponent());
 *		tts.speak("测试一下");
 * </pre>
 * @author 管雷鸣
 * @deprecated
 */
public class Tts {
	private ActiveBean activeBean;
	private Log log;
	private int playId;	//当前或上次播放的声音id(file.play返回的id)
	private File file;
	
	/**
	 * 播放的临时mp3文件存放位置
	 */
	public static String ttsPath="C:\\xnx3_tts.mp3";
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public Tts(ActiveBean activeBean) {
		this.activeBean=activeBean;
		file=new File(activeBean);
		log=new Log();
	}
	
	/**
	 * 输入文字以及发音速度，女声读出
	 * 已废弃，建议使用 {@link TTSUtil#speak(String, int)}
	 * <li>需联网。
	 * <li>会阻塞当前线程
	 * <li>组合函数，首先通过 {@link FileUtil#downFile(String, String)} 将服务器声音文件下载到本地 {@link #ttsPath} ,然后使用 {@link File#play(String)} 进行声音播放
	 * @param text 要读的文字，建议80个汉字以内，
	 * 				<li><i>标点符号注意：</i>逗号、句号、分号等常用符号没问题，其他特殊符号自行测试。有的会终止阅读甚至报错！
	 * @param mode 阅读的快慢，越小发音越慢，参数范围1-5
	 * @return 成功|失败
	 * @deprecated
	 */
	public boolean speak(String text,int mode){
		boolean xnx3_result=false;
		
		try {
			//URL编码，防止乱码导致不发音
			text=URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//先下载文件
		String down=FileUtil.downFile("http://tts.baidu.com/text2audio?lan=zh&pid=101&ie=UTF-8&text="+text+"&spd="+mode, ttsPath);
		if(down==null){
			int play=file.play(ttsPath);
			if(play!=0){
				xnx3_result=true;
				this.playId=play;
			}
		}else{
			log.debug(this, "speak", down);
		}
		
		return xnx3_result;
	}
	
	/**
	 * 输入文字，以女声读出
	 * 已废弃，建议使用 {@link TTSUtil#speak(String)}
	 * <li>需联网。
	 * <li>会阻塞当前线程
	 * <li>组合函数，首先通过 {@link FileUtil#downFile(String, String)} 将服务器声音文件下载到本地 {@link #ttsPath} ,然后使用 {@link File#play(String)} 进行声音播放
	 * @param text 要读的文字，建议80个汉字以内，
	 * 				<li><i>标点符号注意：</i>逗号、句号、分号等常用符号没问题，其他特殊符号自行测试。有的会终止阅读甚至报错！
	 * @return 成功|失败
	 * @see #speak(String, int)
	 * @deprecated
	 */
	public boolean speak(String text){
		return speak(text, 3);
	}
}
