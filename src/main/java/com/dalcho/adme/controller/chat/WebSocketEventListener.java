package com.dalcho.adme.controller.chat;

import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatMessage.MessageType;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import com.dalcho.adme.service.Impl.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations template;
    private final ChatServiceImpl chatService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection  ");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println();
        System.out.println();
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("event : " + event);
        System.out.println("headerAccessor :   " + headerAccessor);
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) headerAccessor.getHeader("simpUser");
        System.out.println("token : " + token);

        String nickname = token.getName();
        userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
        String role = token.getAuthorities().toString().replace("[", "").replace("]", "");
        String roomId = redisService.getRedis(nickname);
        if (roomId.startsWith("aaaa")) {
            log.info("[랜덤 채팅] disconnected chat");
        } else {
            log.info("[고객센터] disconnected chat - {} 의 roomId : {}", nickname, redisService.getRedis(nickname));
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(nickname);
            chatMessage.setRoomId(roomId);
            chatService.connectUser("Disconnect", roomId, chatMessage);
            if (role.equals("ADMIN")) {
                redisService.deleteRedis(nickname);
            }
            template.convertAndSend("/topic/public/" + roomId, chatMessage);
        }
    }
}