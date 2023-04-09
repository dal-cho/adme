package com.dalcho.adme.dto.chat;

import com.dalcho.adme.domain.Chat;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ChatRoomDto {
	private String roomId; // 채팅방 아이디
	private String roomName; // 채팅방 이름(사용자가 설정한 이름)
	private String nickname;
	private Integer adminChat;
	private Integer userChat;
	private String message;

	public ChatRoomDto() {
	}

	public static ChatRoomDto create(String name) {
		ChatRoomDto room = new ChatRoomDto();
		room.roomId = UUID.randomUUID().toString();
		room.roomName = name;
		return room;
	}

	public static ChatRoomDto of (Chat chat, List<String> list){
		return ChatRoomDto.builder()
				.roomId(chat.getRoomId())
				.nickname(chat.getNickname())
				.adminChat(Integer.valueOf(list.get(0)))
				.userChat(Integer.valueOf(list.get(1)))
				.message(list.get(2))
				.build();
	}
}