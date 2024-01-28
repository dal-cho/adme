package com.dalcho.adme.service.Impl;

import com.dalcho.adme.dto.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    
    public void addRedis(ChatMessage chatMessage) {
        log.info("[addRedis의 KEY] : " + chatMessage.getSender());
        long expireTimeInSeconds = 24 * 60 * 60;
        long creationTimeInMillis = System.currentTimeMillis();
        long remainingTimeInSeconds = expireTimeInSeconds - ((System.currentTimeMillis() - creationTimeInMillis) / 1000);
        redisTemplate.opsForValue().set(chatMessage.getSender(), chatMessage.getRoomId(), remainingTimeInSeconds, TimeUnit.SECONDS);
    }

    public String getRedis(String nickname) {
        return redisTemplate.opsForValue().get(nickname);
    }

    public void deleteRedis(String nickname) {
        redisTemplate.delete(nickname);
    }

    public void addToken(String email, String accessToken) {
        long expireTimeInSeconds = 24 * 60 * 60;
        long creationTimeInMillis = System.currentTimeMillis();
        long remainingTimeInSeconds = expireTimeInSeconds - ((System.currentTimeMillis() - creationTimeInMillis) / 1000);
        redisTemplate.opsForValue().set(email, accessToken, remainingTimeInSeconds, TimeUnit.SECONDS);
    }

    public void addSession(String sessionId, String token) {
        long expireTimeInSeconds = 24 * 60 * 60;
        long creationTimeInMillis = System.currentTimeMillis();
        long remainingTimeInSeconds = expireTimeInSeconds - ((System.currentTimeMillis() - creationTimeInMillis) / 1000);
        redisTemplate.opsForValue().set(sessionId, token, remainingTimeInSeconds, TimeUnit.SECONDS);
    }

    public String getSession(String sessionId) {
        return redisTemplate.opsForValue().get(sessionId);
    }
}