package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Socket;
import com.dalcho.adme.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor
public class MyTimeTask extends TimerTask {
	private final ChatRepository chatRepository;
	private String roomId;
	@Value("${spring.servlet.multipart.location}")
	private String chatUploadLocation;

	public MyTimeTask(ChatRepository chatRepository, String roomId, String chatUploadLocation) {
		this.chatRepository = chatRepository;
		this.roomId = roomId;
		this.chatUploadLocation = chatUploadLocation;
	}

	@Override
	public void run() {
		Socket socket = chatRepository.findByRoomId(roomId).orElseThrow(NullPointerException::new);
		chatRepository.delete(socket);
		File file = new File(chatUploadLocation + "/" + roomId + ".txt");
		if (file.exists()){
			boolean result = file.delete();
			if (result) {
				log.info("파일이 삭제되었습니다.");
			} else   {
				log.info("파일이 삭제되지 않았습니다.");
			}
		} else{
			log.info("파일을 찾을 수 없습니다.");
		}

		log.info("5분이 지나 채팅방이 삭제 되었습니다." + socket);
	}
}