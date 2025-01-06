package com.betterfb;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

/**
 * Sends an email using the specified SMTP host and authentication settings.
 *
 * @param to the recipient's email address
 * @param subject the subject of the email
 * @param body the body content of the email
 * @throws MessagingException if an error occurs during the sending process
 */

    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        String from = "betterfb.support@op.pl";
        String host = "smtp.poczta.onet.pl";
        String appPassword = "H1PD-BVEJ-MU71-M2ZQ";
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(from, appPassword);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
