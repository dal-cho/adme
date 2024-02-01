package com.dalcho.adme.config;

import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatMessage.MessageType;
import com.dalcho.adme.dto.chat.ChatRoomDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, ChatRoomDto> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = redisTemplate.getStringSerializer().deserialize(message.getBody());

            String channel = new String(message.getChannel());
            log.info("Received message from channel: " + channel);

            ChatMessage chatMessage = objectMapper.readValue(msg, ChatMessage.class);
            log.info("chatMessage : " + chatMessage);

            try (Jedis jedis = new Jedis("localhost")) {
                Map<String, String> stringStringMap = jedis.pubsubNumSub(channel);
                log.info("구독자 수: " + stringStringMap.get(channel));
            }

            if (chatMessage.getType().equals(MessageType.TALK)) {
                messagingTemplate.convertAndSend(channel, chatMessage);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
