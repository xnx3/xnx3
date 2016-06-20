package com.xnx3.microsoft;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.xnx3.Log;
import com.xnx3.bean.ActiveBean;
import com.xnx3.file.FileUtil;
import com.xnx3.media.MP3Play;

/**
 * 文件操作，包含文件基本操作、截图等
 * @author 管雷鸣
 */
public class File extends FileUtil{
	private Log log;
	private Sleep sleep;
	private ActiveXComponent active=null;
	
	/**
	 * @param activeBean 传入操作的目标对象 {@link Com#getActiveXComponent()}
	 */
	public File(ActiveBean activeBean) {
		this.active=activeBean.getDm();
		log=new Log();
		sleep=new Sleep();
	}
	
	/**
	 * 播放声音,需定义播放文件（另开一线程播放。）
	 * @param playFile 声音文件所在路径
	 * @param playTime 声音播放时间
	 * @deprecated
	 */
	public void play(final String playFile,final int playTime){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int play=active.invoke("Play",playFile).getInt();
					sleep.sleep(playTime);
					if(play==0){
						log.debug("com.xnx3.automate.File","play","声音播放失败");
					}else{
						active.invoke("Stop",play);
					}
				} catch (Exception e) {
					log.debug("com.xnx3.automate.File","play","声音播放异常捕获"+e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 播放某个MP3/WAV文件
	 * <br/>建议使用 {@link MP3Play}
	 * @param playFile MP3/WAV文件的所在路径,如 "C:\\test\\a.mp3"
	 * @return 播放的结果
	 * 				<ul>
	 * 					<li>0:播放失败
	 * 					<li>不为0:播放成功，返回当前播放的id标识
	 * 				</ul>
	 */
	public int play(String playFile){
		int xnx3_result=0;
		try {
			xnx3_result=active.invoke("Play",playFile).getInt();
			if(xnx3_result==0){
				log.debug("com.xnx3.automate.File","play","声音播放失败");
			}else{
				active.invoke("Stop",xnx3_result);
			}
		} catch (Exception e) {
			log.debug(this,"play","声音播放异常捕获"+e.getMessage());
		}
		return xnx3_result;
	}
	
	/**
	 * 截图保存，保存为png格式
	 * @param xStart 区域的左上X坐标
	 * @param yStart 区域的左上Y坐标
	 * @param xEnd 区域的右下X坐标
	 * @param yEnd 区域的右下Y坐标
	 * @param fileName 保存的文件名
	 * 		<li>只有文件名，如 "xnx3.png" 保存的地方为 {@link Com#setResourcePath(String)} 中设置的目录
	 * 		<li>也可以指定全路径名，如"c:\\xunxian\\xnx3.png"
	 */
	public void screenImage(int xStart,int yStart,int xEnd,int yEnd, String fileName){
		try {
			int enableDisplayDebug=active.invoke("EnableDisplayDebug",1).getInt();
			if(enableDisplayDebug==0){
				log.debug(this,"screenImage","EnableDisplayDebug-截图开启失败");
			}
			
			Variant[] var=new Variant[5];
			var[0]=new Variant(xStart);
			var[1]=new Variant(yStart);
			var[2]=new Variant(xEnd);
			var[3]=new Variant(yEnd);
			var[4]=new Variant(fileName);
			int capTurePre=active.invoke("CapturePng",var).getInt();
			var=null;
			
			if(capTurePre==0){
				log.debug(this,"screenImage","截图失败");
			}
			
			active.invoke("EnableDisplayDebug",0);
		} catch (Exception e) {
			log.debug(this,"screenImage","截图异常捕获:"+e.getMessage());
		}
	}
	
}
