package org.springframework.base.system.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.base.system.entity.MonitorConfig;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/MailUtil.class */
public class MailUtil {
    public static Map<String, Object> mailSend(MonitorConfig monitorConfig) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        Transport transport = null;
        try {
            try {
                try {
                    try {
                        try {
                            Properties props = new Properties();
                            props.setProperty("mail.smtp.auth", "true");
                            props.setProperty("mail.transport.protocol", "smtp");
                            props.setProperty("mail.smtp.host", monitorConfig.getSmtpServer());
                            if (monitorConfig.getSmtpSsl().equals("0")) {
                                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                                sf.setTrustAllHosts(true);
                                props.put("mail.smtp.ssl.enable", "true");
                                props.put("mail.smtp.ssl.socketFactory", sf);
                            }
                            Session session = Session.getInstance(props);
                            session.setDebug(false);
                            transport = session.getTransport();
                            transport.connect(monitorConfig.getSmtpServer(), monitorConfig.getSendEmail(), monitorConfig.getSendPassword());
                            MimeMessage message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(monitorConfig.getSendEmail()));
                            message.setRecipients(Message.RecipientType.TO, monitorConfig.getReceiveEmail());
                            message.setRecipients(Message.RecipientType.CC, monitorConfig.getCopyEmail());
                            message.setSubject(monitorConfig.getTitle(), "UTF-8");
                            message.setContent(monitorConfig.getContent(), "text/html;charset=utf-8");
                            message.setSentDate(new Date());
                            message.saveChanges();
                            transport.sendMessage(message, message.getAllRecipients());
                            mess = "邮件发送成功";
                            status = "success";
                            try {
                                transport.close();
                            } catch (Exception e) {
                                LogUtil.e("邮件发送执行失败", e);
                            }
                        } catch (Exception e2) {
                            LogUtil.e("邮件发送失败", e2);
                            mess = "邮件发送失败," + e2.getMessage();
                            status = "fail";
                            try {
                                transport.close();
                            } catch (Exception e3) {
                                LogUtil.e("邮件发送执行失败", e3);
                            }
                        }
                    } catch (MessagingException e4) {
                        LogUtil.e("邮件发送失败", e4);
                        mess = "邮件发送失败," + e4.getMessage();
                        status = "fail";
                        try {
                            transport.close();
                        } catch (Exception e5) {
                            LogUtil.e("邮件发送执行失败", e5);
                        }
                    }
                } catch (AuthenticationFailedException e6) {
                    LogUtil.e("邮件发送失败", e6);
                    mess = "邮箱登录失败，请确认用户名密码是否错误," + e6.getMessage();
                    status = "fail";
                    try {
                        transport.close();
                    } catch (Exception e7) {
                        LogUtil.e("邮件发送执行失败", e7);
                    }
                }
            } catch (Throwable th) {
                try {
                    transport.close();
                } catch (Exception e8) {
                    LogUtil.e("邮件发送执行失败", e8);
                }
                throw th;
            }
        } catch (GeneralSecurityException e9) {
            LogUtil.e("邮件发送失败", e9);
            mess = "邮件发送失败," + e9.getMessage();
            status = "fail";
            try {
                transport.close();
            } catch (Exception e10) {
                LogUtil.e("邮件发送执行失败", e10);
            }
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
