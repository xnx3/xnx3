package com.xnx3.mail;

import javax.mail.internet.MimeMessage;

/**
 * 邮件发送失败，执行的东西
 * @author 管雷鸣
 *
 */
public interface FailureInterface {
	
	/**
	 * 发送邮件失败，进行什么动作
	 * @param sendEmail 发信邮箱的账号、登陆用户名
	 * @param targetEmail 要发送到那个邮箱
	 * @param mailMessage 发送邮件的 {@link MimeMessage}
	 * @param e 发送失败后，捕获的 {@link Exception}
	 */
	public void executeFailure(String sendEmail, String targetEmail, MimeMessage mailMessage, Exception e);
	
}
