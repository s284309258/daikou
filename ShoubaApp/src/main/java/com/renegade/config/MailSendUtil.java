package com.renegade.config;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * 邮件发送
 * @author zenghuayo
 *
 */
public class MailSendUtil {
    private static final String HOST = "smtp.163.com";
    private static final Integer PORT = 25;
    private static final String USERNAME = "18512601138@163.com";
    private static final String PASSWORD = "601138";
    private static final String systemName = "SLA";
    
    private static JavaMailSenderImpl mailSender = createMailSender();
    
    
    public static void main(String[] args)  {
    	//sendRegisteCode("zenghuayo@qq.com", "123456");
    	sendBusCode("291312408@qq.com", "123456");
//    	sendRegisteCode("291312408@qq.com", "123456");
    	
    	
	}
    

    
    public static String sendRegisteCode(String to, String code){
    	String html="你正在注册，你的验证码为:<br/><strong>"+code+"</strong><br/>请勿向任何人泄露您收到的验证码。<br/><br/><br/>"
    	+ "You are registering, your verification code is:<br/><strong>"+code+"</strong><br/>Do not reveal the verification code you received to anyone.";
    	try {
			sendHtmlMail(to, "帐号注册验证码", html);
			return "success";
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
    }
    
    public static String sendBusCode(String to, String code){
    	String html="为确保是您本人操作，请在邮件验证码输入框输入下方验证码:<br/><strong>"+code+"</strong><br/>请勿向任何人泄露您收到的验证码。<br/><br/><br/>"
    	+ "To ensure that you are working, please enter the verification code below in the email verification code input box:<br/><strong>"+code+"</strong><br/>Do not reveal the verification code you received to anyone.";
    	try {
			sendHtmlMail(to, "帐号操作验证码", html);
			return "success";
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new RRException("发送邮件失败");
			return "error";
		}
    }
    
    public static String send(String to, String subject, String html){
    	try {
			sendHtmlMail(to, subject, html);
			return "success";
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new RRException("发送邮件失败");
			return "error";
		}
    }
    
    
    /**
     * BUG通知
     * @param subject 标题
     * @param parameter 参数
     * @param result 结果
     * @param javaExc java异常msg
     */
    public static String sendBug(String subject, String parameter,String result,String javaExc){
    	try {
			sendHtmlMail("zenghuayo@qq.com", subject, "接口参数：<br/>"+parameter+"<br/>接口返回：<br/>"+result+"<br/>java代码异常："+javaExc);
			return "success";
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new RRException("发送邮件失败");
			return "error";
		}
    }
    
    
    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendHtmlMail(String to, String subject, String html) throws UnsupportedEncodingException, MessagingException {
    	//SimpleMailMessage 
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(mailSender.getUsername(), systemName);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }
    
    
    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "25000");
        p.setProperty("mail.smtp.auth", "true");
        sender.setJavaMailProperties(p);
        
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置, 
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.port", "465");
        return sender;
    }
    
    
}

