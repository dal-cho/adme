package com.dalcho.adme.controller.chat;

import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
	private final SimpMessagingTemplate template;
	private final ChatServiceImpl chatService;

	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		template.convertAndSend("/topic/public/" + chatMessage.getRoomId(), chatMessage);
	}

	@MessageMapping("/chat/addUser")
	public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());
		chatService.connectUser("Connect", chatMessage.getRoomId(), chatMessage);
		template.convertAndSend("/topic/public/" + chatMessage.getRoomId(), chatMessage);
	}
}
