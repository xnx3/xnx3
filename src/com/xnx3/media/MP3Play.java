package com.xnx3.media;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.xnx3.file.FileUtil;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * 播放本地MP3文件
 * @author 管雷鸣
 *
 */
public class MP3Play {
    private Player player;
	
    /**
     * @param filePath	MP3文件的绝对路径，如 E:/xnx3/test.mp3
     * @throws JavaLayerException 
     * @throws FileNotFoundException 
     */
    public MP3Play(String filePath) throws JavaLayerException, FileNotFoundException {
    	if(FileUtil.exists(filePath)){
    		BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filePath));
            player = new Player(buffer);
    	}else{
    		throw new FileNotFoundException(filePath+" is not exists");
    	}
    }
    
    /**
     * 开始播放
     * @throws JavaLayerException
     */
    public void play() throws JavaLayerException{
    	player.play();
    }
 
    public static void main(String[] args) throws FileNotFoundException, JavaLayerException{
        new MP3Play("/music/asd.mp3").play();
    }
}
