package com.dalcho.adme.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
	public enum MessageType {
		JOIN, TALK, LEAVE
	}

	private MessageType type; // message type

	private String sender; // message 보내는 사람

	private String message; // 내용(message)

}