package com.dalcho.adme.dto;

import com.dalcho.adme.dto.chat.ChatMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastMessage {
    private String roomId;
    private int adminChat;
    private int userChat;
    private String message;
    private String day;
    private String time;

    public static LastMessage of(ChatMessage chatMessage, int adminChat, int userChat, String day, String time){
        return LastMessage.builder()
                .roomId(chatMessage.getRoomId())
                .adminChat(adminChat)
                .userChat(userChat)
                .message(chatMessage.getMessage())
                .day(day)
                .time(time)
                .build();
    }
}
