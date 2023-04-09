package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Chat;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatRoomDto;
import com.dalcho.adme.dto.chat.ChatRoomMap;
import com.dalcho.adme.exception.notfound.FileNotFoundException;
import com.dalcho.adme.repository.ChatRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl {
	private final ChatRepository chatRepository;
	private Map<String, Integer> connectUsers;
	private Map<String, Integer> adminChat;
	private Map<String, Integer> userChat;
	@Value("${spring.servlet.multipart.location}")
	private String chatUploadLocation;

	@PostConstruct
	private void setUp() {
		this.connectUsers = new HashMap<>();
		this.adminChat = new HashMap<>();
		this.userChat = new HashMap<>();
	}

	public void connectUser(String status, String roomId, ChatMessage chatMessage) {
		if (Objects.equals(status, "Connect")){
			connectUsers.putIfAbsent(roomId, 0);
			int num = connectUsers.get(roomId);
			connectUsers.put(roomId, (num+1));
			saveFile(chatMessage);
		} else if (Objects.equals(status, "Disconnect")) {
			int num = connectUsers.get(roomId);
			connectUsers.put(roomId, (num-1));
		}
		log.info("현재 인원 : " + connectUsers.get(roomId));
	}

	//채팅방 불러오기
	public List<ChatRoomDto> findAllRoom() {
		List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
		List<Chat> all = chatRepository.findAll();
		try {
			for (int i = 0; i < all.size(); i++) {
				chatRoomDtos.add(ChatRoomDto.of(all.get(i), lastLine(all.get(i).getRoomId())));
			}
		} catch (NullPointerException e) {
			throw new RuntimeException("data 없음! ");
		}
		return chatRoomDtos;
	}

	//채팅방 생성
	public ChatRoomDto createRoom(String nickname) {
		ChatRoomDto chatRoom = new ChatRoomDto();
		if (!chatRepository.existsByNickname(nickname)) {
			chatRoom = ChatRoomDto.create(nickname);
			ChatRoomMap.getInstance().getChatRooms().put(chatRoom.getRoomId(), chatRoom);
			Chat chat = new Chat(chatRoom.getRoomId(), nickname);
			log.info("Service socket :  " + chat);
			chatRepository.save(chat);
			return chatRoom;
		} else {
			Optional<Chat> findChat = chatRepository.findByNickname(nickname);
			return ChatRoomDto.of(findChat.get(), lastLine(findChat.get().getRoomId()));
		}
	}

	// 파일 저장
	public void saveFile(ChatMessage chatMessage) {
		if (connectUsers.get(chatMessage.getRoomId())!=0){
			if ((chatMessage.getType().toString()).equals("JOIN")){
				reset(chatMessage.getSender(), chatMessage.getRoomId());
			} else {
				countChat(chatMessage.getSender(), chatMessage.getRoomId());
			}
		}
		JSONObject json = new JSONObject();
		json.put("roomId", chatMessage.getRoomId());
		json.put("type", chatMessage.getType().toString());
		json.put("sender", chatMessage.getSender());
		json.put("message", chatMessage.getMessage());
		json.put("adminChat", adminChat.get(chatMessage.getRoomId()));
		json.put("userChat", userChat.get(chatMessage.getRoomId()));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json1 = gson.toJson(json);
		try {
			FileWriter file = new FileWriter(chatUploadLocation + "/" + chatMessage.getRoomId() + ".txt", true);
			File file1 = new File(chatUploadLocation + "/" + chatMessage.getRoomId() + ".txt");
			if (file1.exists() && file1.length() == 0) {
				file.write(json1);
				chatAlarm(chatMessage.getSender(), chatMessage.getRoomId());
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

	public void reset(String sender, String roomId){
		if (sender.equals("admin")){
			adminChat.putIfAbsent(roomId, 0);
			userChat.putIfAbsent(roomId, 0);
			adminChat.put(roomId,0);
		} else {
			userChat.putIfAbsent(roomId, 0);
			adminChat.putIfAbsent(roomId, 0);
			userChat.put(roomId,0);
		}
	}

	public void countChat(String sender, String roomId){
		if(sender.equals("admin")) {
			userChat.putIfAbsent(roomId, 0);
			int num = userChat.get(roomId);
			userChat.put(roomId, num+1);
			adminChat.put(roomId,0);
		}
		else {
			adminChat.putIfAbsent(roomId, 0);
			int num = adminChat.get(roomId);
			adminChat.put(roomId, num+1);
			userChat.put(roomId,0);
		}
	}
	public List lastLine(String roomId) {
		File file1 = new File(chatUploadLocation + "/" + roomId + ".txt");
		try{
			ReversedLinesFileReader reader
					= new ReversedLinesFileReader(file1, Charset.forName("UTF-8"));

			List<String> lines = reader.readLines(7);

			List<String> chat = new ArrayList<>();
			chat.add(lines.get(6).substring(15, lines.get(6).length()-1)); // adminChat
			chat.add(lines.get(4).substring(14, lines.get(4).length()-1)); // userChat
			chat.add(lines.get(2).substring(14, lines.get(2).length()-2)); // message
			return chat;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public ChatMessage chatAlarm(String sender, String roomId){
		ChatMessage chatMessage = new ChatMessage();
		if (Objects.equals(sender, "admin") && connectUsers.get(roomId) ==1){
			chatMessage.setRoomId(roomId);
			chatMessage.setSender(sender);
			chatMessage.setMessage("고객센터에 문의한 글에 답글이 달렸습니다.");
		} else if (!Objects.equals(sender, "admin") && connectUsers.get(roomId) == 1) {
			chatMessage.setRoomId(roomId);
			chatMessage.setSender(sender);
			chatMessage.setMessage(sender + " 님이 답을 기다리고 있습니다.");

		}
		return chatMessage;
	}
}