package com.dalcho.adme.controller.chat;

import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatMessage.MessageType;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import com.dalcho.adme.service.Impl.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketEventListener {
    private final ChatServiceImpl chatService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtProvider;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.disconnect.exchange}")
    private String disconnectExchange;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info(" Received a new web socket connection  ");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = (String) headerAccessor.getHeader("simpSessionId");
        String token = redisService.getSession(sessionId);
        User user = jwtProvider.getUserFromToken(token);
        String nickname = user.getNickname();
        userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
        String role = user.getRole().name();
        String roomId = redisService.getRoomId(nickname);

        if (roomId.startsWith("aaaa")) {
            log.info("[랜덤 채팅] disconnected chat");
        } else {
            log.info("[고객센터] disconnected chat - {} 의 roomId : {}", nickname, roomId);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(nickname);
            chatMessage.setRoomId(roomId);
            chatMessage.setAuth(role);
            chatService.countUser("Disconnect", roomId, chatMessage);
            if (role.equals("ADMIN")) {
                redisService.deleteRoomId(nickname);
            }
            rabbitTemplate.convertAndSend(disconnectExchange, "room." + roomId, chatMessage);
        }
        redisService.deleteSession(sessionId);
    }
}