package com.idld.notificationservice.Consumer;



import com.idld.notificationservice.Repository.NotificationRepository;

import com.idld.notificationservice.controller.SseNotificationController;
import com.idld.notificationservice.entities.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.idld.resultatservice.entities.GradeNotificationEvent;

import java.time.LocalDateTime;

@Service
public class NotificationConsumer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);
    private final NotificationRepository notificationRepository;
    private final SseNotificationController sseNotificationController;

    public NotificationConsumer(NotificationRepository notificationRepository, SseNotificationController sseNotificationController) {
        this.notificationRepository = notificationRepository;
        this.sseNotificationController = sseNotificationController;
    }


    @KafkaListener(topics = "grades-notifications", groupId = "grades-notifications-group", containerFactory = "gradeKafkaListenerContainerFactory")
    public void handleNewGradeEvent(GradeNotificationEvent event){

        logger.info("Received new grade event: userId={}, grade={}, courseId={}, courseName={}",
                event.getUserId(), event.getGrade(), event.getCourseId(), event.getCourseName());

        Long userId = event.getUserId();
        double grade = event.getGrade();
        Long courseId = event.getCourseId();
        String courseName = event.getCourseName();

        String message = "Vous avez obtenu la note de " + grade + " dans le cours " + courseName + " (ID : " + courseId + ").";

        Notification notification = new Notification();
        notification.setUserId(userId); // Extract user ID from the message
        notification.setTitle("New Grade Posted");
        notification.setMessage(message);
        notification.setType("WEBSOCKET");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);



        sseNotificationController.sendUserNotification(userId, message);




    }


    @KafkaListener(topics = "course-notifications", groupId = "course-notifications-group", containerFactory = "courseKafkaListenerContainerFactory")
    public void handleCourseNotification(String message) {
        logger.info("Received new message: message={}", message);
        // Send the notification to all users via SSE
        sseNotificationController.sendGlobalNotification(message);
    }
}
