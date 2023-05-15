package com.dalcho.adme.service.Impl;

import com.dalcho.adme.dto.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final StringRedisTemplate redisTemplate;

	@Cacheable(key = "#chatMessage.sender", value = "roomId", unless = "#chatMessage.roomId == null")
	public void addRedis(ChatMessage chatMessage){
		long expireTimeInSeconds = 24 * 60 * 60;
		long creationTimeInMillis = System.currentTimeMillis();
		long remainingTimeInSeconds = expireTimeInSeconds - ((System.currentTimeMillis() - creationTimeInMillis) / 1000);
		redisTemplate.opsForValue().set(chatMessage.getSender(), chatMessage.getRoomId(), remainingTimeInSeconds, TimeUnit.SECONDS);
	}

	@Cacheable(value = "roomId", key = "#nickname")
	public String getRedis(String nickname){
		return redisTemplate.opsForValue().get(nickname);
	}

	@Cacheable(key = "#email", value = "accessToken", unless = "#result == null || #accessToken == null")
	public void addToken(String email, String accessToken){
		long expireTimeInSeconds = 24 * 60 * 60;
		long creationTimeInMillis = System.currentTimeMillis();
		long remainingTimeInSeconds = expireTimeInSeconds - ((System.currentTimeMillis() - creationTimeInMillis) / 1000);
		redisTemplate.opsForValue().set(email, accessToken, remainingTimeInSeconds, TimeUnit.SECONDS);
	}
	@Cacheable(value = "accessToken")
	public String getToken(String email){
		return redisTemplate.opsForValue().get(email);
	}
}