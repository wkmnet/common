/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.plugin
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-11
 * Time : 上午10:26
 * ---------------------------------
 */
package org.wukm.mtool.plugin;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.plugin
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-7-11
 * Time : 上午10:26
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class MailerPlugin implements IPlugin{

    //日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean start() {

        Prop prop = PropKit.use("mail.properties");
        Prop keyFile = PropKit.use("system.properties");
        Prop key = PropKit.use(new File(keyFile.get("cloud.key.file")));
        // 收件人电子邮箱
        String to = prop.get("to","wukunmeng@vmovier.com");

        // 发件人电子邮箱
        final String from = prop.get("from","wukunmeng@vmovier.com");

        // 指定发送邮件的主机为 localhost
        String host = prop.get("host","smtp.exmail.qq.com");

        final String password = key.get("password","");

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        //验证
        properties.put("mail.smtp.auth", "true");

        // 获取默认的 Session 对象。
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password); //发件人邮件用户名、密码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象。
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头字段
            message.setSubject("服务器启动通知");

            // 发送 HTML 消息, 可以插入html标签
            message.setContent("<h1>你好,监控服务已经启动,请知晓!</h1>",
                    "text/html;charset=utf-8");
            // 发送消息
//            Transport.send(message);
            logger.info("Sent message successfully....");
            return true;
        }catch (MessagingException mex) {
            logger.info("Sent message MessagingException:" + mex.getMessage(),mex);
        }
        return false;
    }

    @Override
    public boolean stop() {
        return true;
    }
}
