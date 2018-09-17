package com.junl.frame.tools;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 
 * @author xus
 * @date 2016年6月20日 上午9:57:21
 * @description TODO
 */
public class SendEmail {

	public void sendEmail(String recipients, String sender, String username, String password, int random) {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

		// 设定mail server
		senderImpl.setHost("smtp.163.com");
		// 建立邮件消息,发送简单邮件和html邮件的区别
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		// 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
		// multipart模式 为true时发送附件 可以设置html格式
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
			// 设置收件人
			messageHelper.setTo(recipients);
			// 寄件人
			messageHelper.setFrom(sender);
			// 邮箱标题
			messageHelper.setSubject("jdoc管理系统密码找回！");
			// true 表示启动HTML格式的邮件
			messageHelper.setText("<html><head></head><body><h1>你好：你的修改密码的邮箱验证码是:" + random + "</h1></body></html>",
					true);
			// 附件
			/*
			 * FileSystemResource file = new FileSystemResource( new
			 * File("E:/sb.zip")); // 这里的方法调用和插入图片是不同的。
			 * messageHelper.addAttachment("test.zip", file);
			 */

			senderImpl.setUsername(username); // 根据自己的情况,设置username
			senderImpl.setPassword(password); // 根据自己的情况, 设置password
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
			prop.put("mail.smtp.timeout", "25000");
			senderImpl.setJavaMailProperties(prop);
			// 发送邮件
			senderImpl.send(mailMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
