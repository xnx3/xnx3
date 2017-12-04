package com.xnx3.net;
import java.util.Date;  
import java.util.Properties;  

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

import com.xnx3.ConfigManagerUtil;

/**
 * 邮件发送
 * <br/>使用示例
 * <pre>
 * 		//加入配置文件 src/xnx3Config.xml  ，配置其mail节点的参数。
 * 		MailUtil.sendMail("123456@qq.com", "这是标题", "这是内容");
 * </pre>
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
	private static Properties properties;  
	private static boolean debug=true;	//调试日志
	public static final String BR = "\n";	//内容里的换行符
	
	private static String host;	//mail.smtp.host
	private static String username;	//登录用户名
	private static String password;	//登录密码
	
	static{
		host = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.host");
		username = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.username");
		password = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.password");
		String d = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.debug");
		String mailSmtpPort = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.mailSmtpPort");
		
		if(d != null){
			debug = d.equals("true");
		}
		properties = new Properties();  
		//设置邮件服务器  
		properties.put("mail.smtp.host", host);  
		//验证  
		properties.put("mail.smtp.auth", "true");  
		
		//阿里云服务器安全考虑禁用了25端口，所以判断如果是阿里云的，则用80端口
		if(mailSmtpPort != null && mailSmtpPort.length() > 0){
			properties.put("mail.smtp.port", mailSmtpPort);
		}
	}
	
	/**
	 * 发送Txt纯文字邮件
	 * @param targetMail  发送至的邮箱账号
	 * @param title  邮件标题
	 * @param content 邮件发送的内容
	 */
	public static void sendMail(String targetMail,String title,String content) {  
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
			e.printStackTrace();  
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
	public static void sendHtmlMail(String targetMail,String title,String content) {  
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
	
	public static void main(String[] args) {
		MailUtil.sendHtmlMail("123456@qq.com", "这是标题", "这是内容");
	}

}