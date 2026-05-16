package gui;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "delcompany2@gmail.com";
    private static final String APP_PASSWORD = "jnqb nuas fqyj qjjv";

    public static void sendPasswordChangedEmail(String toEmail, String username, String newPassword) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset Successfully");

        message.setText(
                "Hello " + username + ",\n\n" +
                "Your password has been changed successfully.\n\n" +
                "Your new password is: " + newPassword + "\n\n" +
                "Delivery Company"
        );

        Transport.send(message);
    }
}