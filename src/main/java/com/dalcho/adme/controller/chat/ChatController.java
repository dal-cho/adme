package com.dalcho.adme.controller.chat;

import com.dalcho.adme.config.RedisPublisher;
import com.dalcho.adme.config.RedisSubscriber;
import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatMessage.MessageType;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import com.dalcho.adme.service.Impl.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final SimpMessagingTemplate template;
	private final ChatServiceImpl chatService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService redisService;

	private final RedisMessageListenerContainer redisMessageListener;
	private final RedisPublisher redisPublisher;
	private final RedisSubscriber redisSubscriber;
	private Map<String, ChannelTopic> channels;

	@PostConstruct
	public void init(){
		channels = new HashMap<>();
	}
	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		ChannelTopic channel = channels.get(chatMessage.getRoomId());
		redisPublisher.publish(channel, chatMessage);
	}

	@MessageMapping("/chat/addUser")
	public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = (String) headerAccessor.getHeader("simpSessionId");
		String token = headerAccessor.getFirstNativeHeader("Authorization");
		redisService.addSession(sessionId, token);
		User user= jwtTokenProvider.getUserFromToken(token);
		String roomId = chatMessage.getRoomId();
		ChannelTopic channel = new ChannelTopic("/topic/public/" + roomId);
		redisMessageListener.addMessageListener(redisSubscriber, channel);
		channels.put(roomId, channel);
		log.info("[chat] addUser token 검사: " + user.getNickname());
		chatMessage.setSender(user.getNickname());
		chatMessage.setType(MessageType.JOIN);
		chatMessage.setAuth(user.getRole().name());
		redisService.addRedis(chatMessage);
		chatService.connectUser("Connect", roomId, chatMessage);
		template.convertAndSend("/topic/public/" + roomId, chatMessage);
	}
}
