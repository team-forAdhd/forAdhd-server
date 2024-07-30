package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.NotificationService;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.util.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class NotificationController {
    private final NotificationService notificationService;
    private final SseEmitters sseEmitters;

    @GetMapping("/sse")
    public SseEmitter streamSseMvc(@AuthUserId String userId) {
        SseEmitter emitter = new SseEmitter(0L);
        sseEmitters.addEmitter(userId, emitter);
        emitter.onCompletion(() -> sseEmitters.removeEmitter(userId, emitter));
        emitter.onTimeout(() -> sseEmitters.removeEmitter(userId, emitter));
        emitter.onError((e) -> sseEmitters.removeEmitter(userId, emitter));
        return emitter;
    }

    @PostMapping("/mark-as-read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
