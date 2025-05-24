package com.idld.notificationservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sse/notifications")
public class SseNotificationController {

    private final List<SseEmitter> globalEmitters = new ArrayList<>();


    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();


    @GetMapping(value = "/global", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToGlobalNotifications() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // No timeout
        globalEmitters.add(emitter);


        emitter.onCompletion(() -> globalEmitters.remove(emitter));
        emitter.onTimeout(() -> globalEmitters.remove(emitter));

        return emitter;
    }


    @GetMapping(value = "/user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToUserNotifications(@PathVariable Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // No timeout
        userEmitters.put(userId, emitter);

        // Remove the emitter when the client disconnects
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));

        return emitter;
    }


    public void sendGlobalNotification(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        globalEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });


        globalEmitters.removeAll(deadEmitters);
    }


    public void sendUserNotification(Long userId, String message) {
        SseEmitter emitter = userEmitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
                userEmitters.remove(userId); // Remove the emitter if the connection is dead
            }
        }
    }
}
