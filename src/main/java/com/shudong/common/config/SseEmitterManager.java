package com.shudong.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitterManager {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long userId, SseEmitter emitter) {
        SseEmitter oldEmitter = emitters.put(userId, emitter);
        if (oldEmitter != null) {
            oldEmitter.complete();
            log.info("用户 {} 旧SSE连接已关闭", userId);
        }
        log.info("用户 {} SSE连接已注册", userId);
    }

    public void removeEmitter(Long userId) {
        emitters.remove(userId);
        log.info("用户 {} SSE连接已移除", userId);
    }

    public void sendToUser(Long userId, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) {
            return;
        }
        try {
            emitter.send(SseEmitter.event().name("notification").data(data));
        } catch (Exception e) {
            log.warn("向用户 {} 推送SSE失败，移除连接: {}", userId, e.getMessage());
            emitters.remove(userId);
            emitter.complete();
        }
    }
}