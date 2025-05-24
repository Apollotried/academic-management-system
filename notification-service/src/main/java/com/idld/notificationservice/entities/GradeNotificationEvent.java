package com.idld.notificationservice.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeNotificationEvent {
    private Long userId;
    private double grade;
    private Long courseId;
    private String courseName;
}