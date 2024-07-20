package com.project.foradhd.global.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitters {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public synchronized void addEmitter(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public synchronized void removeEmitter(String userId, SseEmitter emitter) {
        emitters.remove(userId, emitter);
    }

    public void sendNotification(String userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                removeEmitter(userId, emitter);
            }
        }
    }
}
