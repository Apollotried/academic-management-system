package com.idld.notificationservice.controller;


import com.idld.notificationservice.Service.NotificationServiceInterface;
import com.idld.notificationservice.entities.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api/notificationsEmail")
public class EmailController  {


    @Autowired
    private NotificationServiceInterface notificationService;

    @PostMapping("/send")
    public void sendNotification(@RequestBody Email email) {
        try {
            String subject = email.getObject() != null ? email.getObject() : "Mise à jour importante";
            if (email.getRecipient().contains(" ")) {
                notificationService.sendEmails(email.getRecipient(), subject, email.getMessage());
            } else {
                notificationService.sendEmail(email.getRecipient(), subject, email.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending notification(s)", e);
        }
    }

    @GetMapping("/all")
    public List<Email> getAllNotifications() {

        return notificationService.getAllEmails();
    }

}