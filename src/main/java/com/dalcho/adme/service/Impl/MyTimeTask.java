package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Socket;
import com.dalcho.adme.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor
public class MyTimeTask extends TimerTask {
	private final ChatRepository chatRepository;
	private String roomId;
	public MyTimeTask(ChatRepository chatRepository, String roomId){
		this.chatRepository = chatRepository;
		this.roomId = roomId;
	}
	@Override
	public void run() {
		Socket socket = chatRepository.findByRoomId(roomId).orElseThrow(NullPointerException :: new);
		chatRepository.delete(socket);
		log.info("5분이 지나 삭제 되었습니다." + socket);
	}
}
