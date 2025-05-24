package com.idld.notificationservice.Service;


import com.idld.notificationservice.Mapper.EmailMapper;
import com.idld.notificationservice.Repository.EmailRepository;
import com.idld.notificationservice.dtos.ResponseEmailDto;
import com.idld.notificationservice.entities.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements NotificationServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailServiceInterface emailService;

    @Autowired
    private EmailMapper emailMapper;

    @Override
    public Email sendEmail(String recipient, String subject, String message) {
        try {
            Email email = new Email(recipient, message, subject, "SENT", LocalDateTime.now());
            emailRepository.save(email);
            emailService.sendEmail(recipient, subject, message);
            return email;
        } catch (Exception e) {
            logger.error("Error while sending notification to " + recipient, e);
        }
        return null;
    }

    @Override
    public void sendEmails(String recipients, String subject, String message) {
        String[] recipientArray = recipients.split(" ");
        for (String recipient : recipientArray) {
            try {
                sendEmail(recipient, subject, message);
            } catch (Exception e) {
                logger.error("Failed to send notification to: " + recipient, e);
            }
        }
    }


    @Override
    public List<Email> getAllEmails() {

        return emailRepository.findAll();
    }
    @Override
    public ResponseEmailDto getEmailById(Long id) {
        Optional<Email> notification = emailRepository.findById(id);
        return notification.map(email -> emailMapper.toDto(email)).orElse(null);
    }
}
