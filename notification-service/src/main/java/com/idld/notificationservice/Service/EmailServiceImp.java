package com.idld.notificationservice.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailServiceInterface {

    private final JavaMailSender emailSender;

    public EmailServiceImp(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    @Override
    public void sendEmail(String to, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("sifeddine.hebbaj12@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message);
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String message, String filePath) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("your_email@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message);
        helper.addAttachment("attachment", new java.io.File(filePath));  // Add attachment here
        emailSender.send(mimeMessage);
    }
}
