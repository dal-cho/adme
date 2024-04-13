package com.dalcho.adme.controller.chat;

import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatMessage.MessageType;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import com.dalcho.adme.service.Impl.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final ChatServiceImpl chatService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService redisService;
	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.connect.exchange}")
	private String connectExchange;

	@Value("${rabbitmq.send.exchange}")
	private String sendExchange;

	@MessageMapping("chat.sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		String roomId = chatMessage.getRoomId();
		rabbitTemplate.convertAndSend(sendExchange, "room." + roomId, chatMessage);
	}

	@MessageMapping("chat.addUser")
	public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = (String) headerAccessor.getHeader("simpSessionId");
		String token = headerAccessor.getFirstNativeHeader("Authorization");
		redisService.addSession(sessionId, token);

		User user = jwtTokenProvider.getUserFromToken(token);
		String roomId = chatMessage.getRoomId();
		log.info("[chat] addUser token 검사: " + user.getNickname());

		chatMessage.setSender(user.getNickname());
		chatMessage.setType(MessageType.JOIN);
		chatMessage.setAuth(user.getRole().name());

		redisService.addRoomId(chatMessage);
		redisService.addAuth(chatMessage);
		chatService.countUser("Connect", roomId, chatMessage);
		rabbitTemplate.convertAndSend(connectExchange, "room." + roomId, chatMessage);
	}
}
