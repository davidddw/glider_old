package com.yunshan.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class EmailUtil {
	private static String host = "smtp.exmail.qq.com"; // smtp服务器
	private static String user = "stats20@yunshan.net.cn"; // 用户名
	private static String pwd = "yunshan3302"; // 密码

	private static String from = "stats20@yunshan.net.cn"; // 发件人地址
	private static String to = "dawei@yunshan.net.cn"; // 收件人地址
	private static String subject = "[故事里]邮件测试"; // 邮件标题

	public static void send(String content) {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pwd);
			}
		};
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(false);
		MimeMessage message = new MimeMessage(session);
		try {
			String nick = "";
			try {
				nick = javax.mail.internet.MimeUtility.encodeText("故事里");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			message.setFrom(new InternetAddress(nick + " <" + from + ">"));
			// 加载收件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// 加载标题
			message.setSubject(subject);
			message.setSentDate(new Date());
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			// 给BodyPart对象设置内容和格式/编码方式
			contentPart.setContent(content, "text/html;charset=utf-8");
			// 发送纯文本
			// contentPart.setText(content);
			multipart.addBodyPart(contentPart);
			// 添加附件
			// BodyPart messageBodyPart = new MimeBodyPart();
			// DataSource source = new FileDataSource(affix);
			// 添加附件的内容
			// messageBodyPart.setDataHandler(new DataHandler(source));
			// 添加附件的标题
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			// messageBodyPart.setFileName("=?GBK?B?"+
			// enc.encode(affixName.getBytes()) + "?=");
			// multipart.addBodyPart(messageBodyPart);

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();
			// 发送邮件
			Transport transport = session.getTransport("smtp");
			// 连接服务器的邮箱
			transport.connect(host, user, pwd);
			// 把邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<!DOCTYPE html>");
		sBuffer.append("<html>");
		sBuffer.append("<head>");
		sBuffer.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
		sBuffer.append("<meta http-equiv='X-UA-Compatible' content='IE=edge'>");
		sBuffer.append("<title>故事里|记录,讲述,分享我们的故事</title>");
		sBuffer.append("</head>");
		sBuffer.append("<body>");
		sBuffer.append("<table><tr><td>");
		sBuffer.append("<div style='width:500px;height:400px;margin:0 auto;background:#EF4A3A;color:#FFF;font-size:18px;'>你好，这里是【故事里】系统测试邮件</div> ");
		sBuffer.append("</td></tr></table>");
		sBuffer.append("</body>");
		sBuffer.append("</html>");
		send(sBuffer.toString());
	}
}
