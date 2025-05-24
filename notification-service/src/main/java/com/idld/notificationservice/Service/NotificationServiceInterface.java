package com.idld.notificationservice.Service;

import com.idld.notificationservice.dtos.ResponseEmailDto;
import com.idld.notificationservice.entities.Email;

import java.util.List;

public interface NotificationServiceInterface {
    void sendEmails(String recipients, String subject, String message);
    Email sendEmail(String recipient, String subject, String message);
    List<Email> getAllEmails();
    public ResponseEmailDto getEmailById(Long id);
}
