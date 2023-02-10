package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Socket;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatRoomDto;
import com.dalcho.adme.dto.chat.ChatRoomMap;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.notfound.FileNotFoundException;
import com.dalcho.adme.exception.notfound.ChatRoomNotFoundException;
import com.dalcho.adme.repository.ChatRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl {
	private final ChatRepository chatRepository;
	@Value("${spring.servlet.multipart.location}")
	private String chatUploadLocation;

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

	// 삭제 후 재 접속 막기
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

	//채팅방 하나 불러오기
	public ChatRoomDto roomOne(String nickname) throws CustomException {
		Socket socket = chatRepository.findByNickname(nickname).orElseThrow(ChatRoomNotFoundException::new);
		return ChatRoomDto.of(socket);
	}

	public void deleteRoom(String roomId) {
		Timer t = new Timer(true);
		TimerTask task = new MyTimeTask(chatRepository, roomId, chatUploadLocation);
		t.schedule(task, 300000);
		log.info("5분뒤에 삭제 됩니다.");
	}

	// 파일 저장
	public void saveFile(ChatMessage chatMessage) {
		JSONObject json = new JSONObject();
		json.put("roomId", chatMessage.getRoomId());
		json.put("type", chatMessage.getType().toString());
		json.put("sender", chatMessage.getSender());
		json.put("message", chatMessage.getMessage());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json1 = gson.toJson(json);
		try {
			FileWriter file = new FileWriter(chatUploadLocation + "/" + chatMessage.getRoomId() + ".txt", true);
			File file1 = new File(chatUploadLocation + "/" + chatMessage.getRoomId() + ".txt");
			if (file1.exists() && file1.length() == 0) {
				file.write(json1);
			} else {
				file.write("," + json1);
			}
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object readFile(String roomId) {
		try {
			String str = Files.readString(Paths.get(chatUploadLocation + "/" + roomId + ".txt"));
			JSONParser parser = new JSONParser();
			Object obj = parser.parse("[" + str + "]");
			return obj;
		} catch (NoSuchFileException e) {
			throw new FileNotFoundException();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}