package com.xnx3.net;
import java.util.Date;  
import java.util.Properties;  
import javax.mail.MessagingException;
import javax.mail.Session;  
import javax.mail.Authenticator;  
import javax.mail.PasswordAuthentication;  
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.InternetAddress;  
import javax.mail.Transport;  
import com.xnx3.ConfigManagerUtil;

/**
 * 邮件发送
 * <br/>使用示例
 * <pre>
 * 		////加入配置文件 src/xnx3Config.xml  ，配置其mail节点的参数。
 * 		MailUtil.sendMail("123456@qq.com", "这是标题", "这是内容");
 * </pre>
 * <br><b>需导入</b> 
 * <br/><i>mail.jar</i>
 * <br/><i>commons-configuration-1.7.jar</i>
 * <br/><i>commons-collections-3.2.1.jar</i>
 * <br/><i>commons-io-1.3.2.jar</i>
 * <br/><i>commons-lang-2.5.jar</i>
 * <br/><i>commons-logging-1.2.jar</i>
 * @author 管雷鸣
 */
public class MailUtil {  
	private static Properties properties;  
	private static boolean debug=true;	//调试日志
	
	private static String host;	//mail.smtp.host
	private static String username;	//登录用户名
	private static String password;	//登录密码
	
	static{
		host = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.host");
		username = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.username");
		password = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.password");
		String d = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.debug");
		if(d != null){
			debug = d.equals("true");
		}
		System.out.println(debug);
		properties = new Properties();  
		//设置邮件服务器  
		properties.put("mail.smtp.host", host);  
		//验证  
		properties.put("mail.smtp.auth", "true");  
	}
	
	/**
	 * 发送邮件
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
	
	public static void main(String[] args) {
		MailUtil.sendMail("123456@qq.com", "这是标题", "这是内容");
	}

}