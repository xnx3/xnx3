package com.xnx3.net;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import com.xnx3.bean.ShellBean;
import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 远程Linux服务器SSH链接控制
 * <pre>
 *		SSHUtil ssh = new SSHUtil("10.0.0.251", "guanleiming", "password"); 
 *		ssh.open();
 *		System.out.println(ssh.exeCommand("cd /alidata;pwd;ls").getOutString());
 *		System.out.println(ssh.exeCommand("cd /;pwd").getOutString());
 *		ssh.close();
 * </pre>
 * @author 管雷鸣
 *
 */
public class SSHUtil {
    private Connection conn; 
    private String ip;	 //远程机器IP 
    private String username; //用户名 
    private String password; //密码 
    private String charset = Charset.defaultCharset().toString(); 
    
    private static final int TIME_OUT = 1000 * 5 * 60; 
    
    public SSHUtil(String ip,String username,String password) {
		this.ip=ip;
		this.username=username;
		this.password=password;
	}
    
    /**
     * 登陆
     * @return boolean
     * @throws IOException
     */
    public boolean open(){ 
        conn = new Connection(ip); 
        try {
			conn.connect();
			return conn.authenticateWithPassword(this.username, this.password); 
		} catch (IOException e) {
			e.printStackTrace();
		}
        return false;
    } 
	
    /**
     * 执行脚本
     * <br/>需提前执行login()
     * @param cmds
     * @return {@link ShellBean}
     * @throws Exception
     */
    public ShellBean exeCommand(String cmd) throws Exception { 
    	ShellBean shellBean =new ShellBean();
        InputStream stdOut = null; 
        InputStream stdErr = null; 
        try { 
            Session session = conn.openSession(); 
            session.execCommand(cmd); 
            
            stdOut = new StreamGobbler(session.getStdout()); 
            shellBean.setOutString(processStream(stdOut, charset)); 
            
            stdErr = new StreamGobbler(session.getStderr()); 
            shellBean.setErrorString(processStream(stdErr, charset));
            
            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT); 
            
            shellBean.setExitStatus(session.getExitStatus()); 
        } finally { 
            if(stdOut!=null){
            	stdOut.close();	
            }
            if(stdErr!=null){
            	stdErr.close();	
            }
        } 
        return shellBean; 
    }
    
    /**
     * 关闭Connect
     */
    public void close(){
    	 if (conn != null) { 
             conn.close(); 
         }
    }
    
    private String processStream(InputStream in, String charset) throws Exception { 
        byte[] buf = new byte[1024]; 
        StringBuilder sb = new StringBuilder(); 
        while (in.read(buf) != -1) { 
            sb.append(new String(buf, charset)); 
        } 
        return sb.toString(); 
    } 
    
    public static void main(String args[]) throws Exception { 
    	SSHUtil ssh = new SSHUtil("192.168.199.251", "root", "elinli@2015"); 
    	ssh.open();
        System.out.println(ssh.exeCommand("cd /alidata;pwd;ls").getOutString());
        System.out.println(ssh.exeCommand("cd /;pwd").getOutString());
        ssh.close();
    } 
}
