package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Socket;
import com.dalcho.adme.dto.socket.ChatRoomDto;
import com.dalcho.adme.dto.socket.ChatRoomMap;
import com.dalcho.adme.repository.SocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl {
	private final SocketRepository socketRepository;

	//채팅방 불러오기
	public List<ChatRoomDto> findAllRoom() {
		List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
		List<Socket> all = socketRepository.findAll();
		try {
			for(int i=0; i<all.size(); i++){
				chatRoomDtos.add(ChatRoomDto.of(all.get(i)));
			}
		} catch (NullPointerException e) {
			throw new RuntimeException("data 없음! ") ;
		}
		return chatRoomDtos;
	}

	//채팅방 하나 불러오기
	public boolean getRoomInfo(String roomId) {
		return socketRepository.existsByRoomId(roomId);
	}

	//채팅방 생성
	public ChatRoomDto createRoom(String nickname) {
		ChatRoomDto chatRoom = new ChatRoomDto();
		if (!socketRepository.existsByNickname(nickname)) {
			chatRoom = ChatRoomDto.create(nickname);
			ChatRoomMap.getInstance().getChatRooms().put(chatRoom.getRoomId(), chatRoom);
			Socket socket = new Socket(chatRoom.getRoomId(), nickname);
			log.info("Service socket :  " + socket);
			socketRepository.save(socket);
			return chatRoom;
		}
		else{
			Optional<Socket> byNickname = socketRepository.findByNickname(nickname);
			return ChatRoomDto.of(byNickname.get());
		}
	}

	public ChatRoomDto roomOne(String nickname){
		Optional<Socket> byNickname = socketRepository.findByNickname(nickname);
		return ChatRoomDto.of(byNickname.get());
	}

	public void deleteRoom(String roomId){
		Socket socket = socketRepository.findByRoomId(roomId).orElseThrow(NullPointerException :: new);
		socketRepository.delete(socket);
	}
}
