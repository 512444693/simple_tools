package com.zm.mix.mail_notify;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by zhangmin on 2015/8/7.
 */
public class MyMail{
    String host = "smtp.163.com";
    String user = "information_notify@163.com";
    String password = "zhangmin";
    public void setHost(String host) {
        this.host = host;
    }
    public void setAccount(String user, String password) {
        this.user = user;
        this.password = password;
    }
    public void MyMail(){

    }
    public void send(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host); // 指定SMTP服务器
        props.put("mail.smtp.auth", "true"); // 指定是否需要SMTP验证
        try {
            Session mailSession = Session.getDefaultInstance(props);
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(user)); // 发件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 收件人
            message.setSubject(subject); // 邮件主题
            message.setText(content); // 邮件内容
            message.saveChanges();
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        MyMail mm = new MyMail();
        //mm.setHost("smtp.163.com"); // 指定要使用的邮件服务器
        //mm.setAccount("information_notify@163.com", "zhangmin"); // 指定帐号和密码

        mm.send("512444693@qq.com", "标题", "HelloWold!");
    }
}