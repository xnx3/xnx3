package com.xnx3.net;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.xnx3.Log;
import com.xnx3.bean.FileBean;

/**
 * <b>Linux下的SFTP操作类</b>
 * <br/>依赖Jar 包：jsch-0.1.53.jar
 * <pre>
 *		SFTPUtil sftp= new SFTPUtil();
 *		sftp.setHost("10.0.0.251");
 *		sftp.setUsername("root");
 *		sftp.setPassword("guanleiming");
 *		sftp.connect();
 *       
 *		try {
 *			//下载文件
 *			sftp.getSftp().get("/root/install.log", "/Users/apple/Desktop/");
 *
 *			//上传文件
 *			sftp.getSftp().put("/Users/apple/Desktop/installaaa.log", "/root");
 *		} catch (SftpException e) {
 *			e.printStackTrace();
 *		}
 *       
 *		sftp.disconnect();
 * 
 * </pre>
 * 
 * @author 管雷鸣
 */
public class SFTPUtil {
    
    private String host = "127.0.0.1";
    private String username="root";
    private String password="管雷鸣";
    private int port = 22;
    private ChannelSftp sftp = null;
    
    private Log log;
    
    public SFTPUtil() {
		this.log=new Log();
	}
    
    /**
     * connect server via sftp
     */
    public void connect() {
        try {
            if(sftp != null){
                log.debug(this,"connect","sftp is not null");
            }
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            log.debug(this,"connect","Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            log.debug(this,"connect","Session connected,Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.debug(this,"connect","Connected to " + host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Disconnect with server
     */
    public void disconnect() {
        if(this.sftp != null){
            if(this.sftp.isConnected()){
                this.sftp.disconnect();
            }else if(this.sftp.isClosed()){
            	 log.debug(this,"disconnect","sftp closed");
            }
        }

    }
    
    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the sftp
     */
    public ChannelSftp getSftp() {
        return sftp;
    }

    /**
     * @param sftp the sftp to set
     */
    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }
    
    /**
     * 
     * @param dir 服务器远程路径
     * @return 若成功，返回List，失败，返回null
     */
    public List<FileBean> list(String dir) {  
        try {  
        	Vector ls = sftp.ls(dir);
            return _buildFiles(ls,dir);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
    
    private List<FileBean> _buildFiles(Vector ls,String dir) throws Exception {  
        if (ls != null && ls.size() >= 0) {  
            List<FileBean> list = new ArrayList<FileBean>();
            for (int i = 0; i < ls.size(); i++) {  
                LsEntry f = (LsEntry) ls.get(i);  
                String nm = f.getFilename();  
                
                if (nm.equals(".") || nm.equals(".."))  
                    continue;  
                SftpATTRS attr = f.getAttrs();  
                FileBean fileBean=new FileBean();
                if (attr.isDir()) {  
                    fileBean.setDir(true);
                } else {  
                    fileBean.setDir(false);
                }  
                fileBean.setAttrs(attr);
                fileBean.setFilePath(dir);
                fileBean.setFileName(nm);
                list.add(fileBean);  
            }  
            return list;  
        }  
        return null;  
    }  
}
