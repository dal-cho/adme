package com.dalcho.adme.controller.chat;

import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import com.dalcho.adme.service.Impl.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final SimpMessagingTemplate template;
	private final ChatServiceImpl chatService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService redisService;

	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		template.convertAndSend("/topic/public/" + chatMessage.getRoomId(), chatMessage);
	}

	@MessageMapping("/chat/addUser")
	public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		String token = headerAccessor.getFirstNativeHeader("Authorization");
		User user= jwtTokenProvider.getUserFromToken(token);
		log.info("[chat] addUser token 검사: " + user.getNickname());
		chatMessage.setSender(user.getNickname());
		chatMessage.setType(ChatMessage.MessageType.JOIN);
		redisService.addRedis(chatMessage);
		chatService.connectUser("Connect", chatMessage.getRoomId(), chatMessage);
		template.convertAndSend("/topic/public/" + chatMessage.getRoomId(), chatMessage);
	}
}
