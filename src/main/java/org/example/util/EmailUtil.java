package org.example.util;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

@Slf4j
public class EmailUtil {
    private EmailUtil() {
    }

    private static final String EMAIL = "example1226custom@gmail.com";
    private static final String APP_PASSWORD = "xyemufskzmfcnlkk";

    private static Session getSession() {
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, APP_PASSWORD);
            }
        });
    }

    private static MimeMessage prepareMessage(Session session, String toEmail, String subject, String body) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);
        } catch (MessagingException e) {
            log.error("Error prepareMessage method : MessagingException occurred.", e);
        }
        return message;
    }

    public static Boolean sendEmail(String toEmail, String subject, String body) {
        Session session = getSession();
        session.setDebug(true);
        try {
            MimeMessage message = prepareMessage(session, toEmail, subject, body);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            log.error("Error send email : MessagingException occurred.", e);
        } catch (Exception e) {
            log.error("Error send email : An unexpected error occurred.", e);
        }
        return false;
    }

    public static void sendEmail(String toEmail, String subject, String body, String filePath) {
        Session session = getSession();
        session.setDebug(true);

        try {
            MimeMessage message = prepareMessage(session, toEmail, subject, body);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(filePath).getName());
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Error send email : MessagingException occurred.", e);
        } catch (Exception e) {
            log.error("Error send email : An unexpected error occurred.", e);
        }
    }
}
