package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Socket;
import com.dalcho.adme.dto.socket.ChatRoomDto;
import com.dalcho.adme.dto.socket.ChatRoomMap;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.notfound.SocketNotFoundException;
import com.dalcho.adme.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl {
	private final ChatRepository chatRepository;

	//채팅방 불러오기
	public List<ChatRoomDto> findAllRoom() {
		List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
		List<Socket> all = chatRepository.findAll();
		try {
			for (int i = 0; i < all.size(); i++) {
				chatRoomDtos.add(ChatRoomDto.of(all.get(i)));
			}
		} catch (NullPointerException e) {
			throw new RuntimeException("data 없음! ");
		}
		return chatRoomDtos;
	}

	//채팅방 하나 불러오기
	public boolean getRoomInfo(String roomId) {
		return chatRepository.existsByRoomId(roomId);
	}

	//채팅방 생성
	public ChatRoomDto createRoom(String nickname) {
		ChatRoomDto chatRoom = new ChatRoomDto();
		if (!chatRepository.existsByNickname(nickname)) {
			chatRoom = ChatRoomDto.create(nickname);
			ChatRoomMap.getInstance().getChatRooms().put(chatRoom.getRoomId(), chatRoom);
			Socket socket = new Socket(chatRoom.getRoomId(), nickname);
			log.info("Service socket :  " + socket);
			chatRepository.save(socket);
			return chatRoom;
		} else {
			Optional<Socket> byNickname = chatRepository.findByNickname(nickname);
			return ChatRoomDto.of(byNickname.get());
		}
	}

	public ChatRoomDto roomOne(String nickname) throws CustomException {
		Socket socket = chatRepository.findByNickname(nickname).orElseThrow(SocketNotFoundException::new);
		return ChatRoomDto.of(socket);
	}

	public void deleteRoom(String roomId) {
		Timer t = new Timer(true);
		TimerTask task = new MyTimeTask(chatRepository, roomId);
		t.schedule(task, 300000);
		log.info("5분뒤에 삭제 됩니다.");
	}
}