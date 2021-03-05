package com.xnx3.mail;
import java.security.GeneralSecurityException;
import java.util.Date;  
import java.util.Properties;  
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;  
import javax.mail.Authenticator;  
import javax.mail.PasswordAuthentication;  
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMultipart;
import javax.mail.Transport;  

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 邮件发送
 * <br><b>需导入</b> 
 * <br/>mail.jar
 * <br/>commons-configuration-1.7.jar
 * <br/>commons-collections-3.2.1.jar
 * <br/>commons-io-1.3.2.jar
 * <br/>commons-lang-2.5.jar
 * <br/>commons-logging-1.2.jar
 * @author 管雷鸣
 */
public class MailUtil {  
	private Properties properties;  
	public boolean debug=false;	//调试日志
	public final String BR = "\n";	//内容里的换行符
	
//	private String host;	//mail.smtp.host
	private String username;	//登录用户名
	private String password;	//登录密码
	private String mailSmtpPort = "25";	//发送邮件的端口，默认是25
	
	private FailureInterface failureInterface;	//邮件发送失败，进入try catch，会执行的方法
	
	public MailUtil(String host, String username, String password) {
		properties = new Properties();
		//设置邮件服务器  
		properties.put("mail.smtp.host", host);  
		//验证  
		properties.put("mail.smtp.auth", "true");  
		
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 设置失败的捕获。当邮件发送失败时，比如对方邮箱不存在，发件箱密码不对等，都可以通过此进行捕获
	 * @param failureInterface new一个对象，实现这个接口
	 */
	public void setFailureInterface(FailureInterface failureInterface) {
		this.failureInterface = failureInterface;
	}


	public void setSSLSmtpPort(String mailSmtpPort){
		this.mailSmtpPort = mailSmtpPort;
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		sf.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", sf);
		properties.put("mail.smtp.port", mailSmtpPort);
	}
	
//	static{
//		//阿里云服务器安全考虑禁用了25端口，所以判断如果是阿里云的，则用80端口
//		if(mailSmtpPort != null && mailSmtpPort.length() > 0){
//			properties.put("mail.smtp.port", mailSmtpPort);
//		}
//	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	/**
	 * 是否开启邮件发送的日志打印
	 * @param debug true：开启
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * 阿里云服务器安全考虑禁用了25端口，所以判断如果是阿里云的，则用80端口。默认无需设置，使用25端口
	 * @param mailSmtpPort 端口号
	 */
	public void setMailSmtpPort(String mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}


	/**
	 * 发送Txt纯文字邮件
	 * @param targetMail  发送至的邮箱账号
	 * @param title  邮件标题
	 * @param content 邮件发送的内容
	 */
	public void sendMail(String targetMail,String title,String content) {  
		Transport trans = null;
		MimeMessage mailMessage = null;
		try {  
			//根据属性新建一个邮件会话  
			Session mailSession = Session.getInstance(properties,  
			new Authenticator() {  
				public PasswordAuthentication getPasswordAuthentication() {  
					  return new PasswordAuthentication(username,password);  
				  }
			});  
			mailSession.setDebug(debug);  
			//建立消息对象  
			mailMessage = new MimeMessage(mailSession);  
			//发件人  
			mailMessage.setFrom(new InternetAddress(username));  	//xnx3_cs@163.com
			//收件人  
			mailMessage.setRecipient(MimeMessage.RecipientType.TO,  
			new InternetAddress(targetMail));  
			//主题  
			mailMessage.setSubject(title);  
			//内容  
			mailMessage.setText(content);  
			//发信时间  
			mailMessage.setSentDate(new Date());  
			//存储信息  
			mailMessage.saveChanges();  
			
			//  
			trans = mailSession.getTransport("smtp");  
			//发送
			trans.send(mailMessage);  
		} catch (Exception e) {  
			if(failureInterface != null){
				failureInterface.executeFailure(username, targetMail, mailMessage, e);
			}
		} finally {  
			try {
				trans.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}  
	}  
	
	/**
	 * 发送HTML格式邮件
	 * @param targetMail  发送至的邮箱账号
	 * @param title  邮件标题
	 * @param content 邮件发送的HTML内容，直接写html即可，无需html、body等
	 */
	public void sendHtmlMail(String targetMail,String title,String content) {  
		sendHtmlMail(targetMail, title, content, null);
	}
	
	/**
	 * 发送HTML格式邮件
	 * @param targetMail  发送至的邮箱账号
	 * @param title  邮件标题
	 * @param content 邮件发送的HTML内容，直接写html即可，无需html、body等
	 */
	public void sendHtmlMail(String targetMail,String title,String content, String replayTo) {  
		Transport trans = null;
		try {  
			//根据属性新建一个邮件会话  
			Session mailSession = Session.getInstance(properties,  
			new Authenticator() {  
				public PasswordAuthentication getPasswordAuthentication() {  
					return new PasswordAuthentication(username,password);  
				}
			});  
			mailSession.setDebug(debug);  
			//建立消息对象  
			MimeMessage mailMessage = new MimeMessage(mailSession);  
			//发件人  
			mailMessage.setFrom(new InternetAddress(username));  	//xnx3_cs@163.com
			//收件人  
			mailMessage.setRecipient(MimeMessage.RecipientType.TO,  
			new InternetAddress(targetMail));  
			//主题  
			mailMessage.setSubject(title);  
			
			if(replayTo != null && replayTo.length() > 3){
				Address[] add = {new InternetAddress(replayTo)};
				mailMessage.setReplyTo(add);
			}
			
			//内容  
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
	        Multipart mainPart = new MimeMultipart();
	        // 创建一个包含HTML内容的MimeBodyPart
	        BodyPart html = new MimeBodyPart();
	        // 设置HTML内容
	        html.setContent(content, "text/html; charset=utf-8");
	        mainPart.addBodyPart(html);
	        // 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			//发信时间  
			mailMessage.setSentDate(new Date());  
			//存储信息  
			mailMessage.saveChanges();  
			//  
			trans = mailSession.getTransport("smtp");  
			//发送  
			trans.send(mailMessage);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {
				trans.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}  
	}  
	
}