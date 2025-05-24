package com.idld.notificationservice.Service;

import jakarta.mail.MessagingException;

public interface EmailServiceInterface {
    void sendEmail(String to, String subject, String message) throws MessagingException;
    void sendEmailWithAttachment(String to, String subject, String message, String filePath) throws MessagingException;
}
