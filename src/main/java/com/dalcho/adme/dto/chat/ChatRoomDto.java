package com.dalcho.adme.dto.chat;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.LastMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatRoomDto implements Serializable {
    private String roomId;
    private String nickname;
    private LastMessage lastMessage;

    public ChatRoomDto() {
    }

    public static ChatRoomDto create() {
        ChatRoomDto room = new ChatRoomDto();
        room.roomId = UUID.randomUUID().toString();
        return room;
    }

    public static ChatRoomDto of(User user, LastMessage lastMessage) {
        return ChatRoomDto.builder()
                .nickname(user.getNickname())
                .lastMessage(lastMessage)
                .build();
    }
}
