package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Chat;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatRoomDto;
import com.dalcho.adme.dto.chat.ChatRoomMap;
import com.dalcho.adme.exception.notfound.FileNotFoundException;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.ChatRepository;
import com.dalcho.adme.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl {
	private final ChatRepository chatRepository;
	private Map<String, Integer> connectUsers;
	private Map<String, Integer> adminChat;
	private Map<String, Integer> userChat;
	private final RedisTemplate<String, ChatRoomDto> redisTemplate;
	@Value("${chat.file}")
	private String chatUploadLocation;
	private final UserRepository userRepository;
	private final Object lock = new Object();

	@PostConstruct
	private void setUp() {
		this.connectUsers = new HashMap<>();
		this.adminChat = new HashMap<>();
		this.userChat = new HashMap<>();
	}

	public void connectUser(String status, String roomId, ChatMessage chatMessage) {
		int num = 0;
		synchronized (lock) {
			if (Objects.equals(status, "Connect")) {
				connectUsers.putIfAbsent(roomId, 0);
				num = connectUsers.get(roomId);
				connectUsers.put(roomId, (num + 1));
				saveFile(chatMessage);
			} else if (Objects.equals(status, "Disconnect")) {
				num = connectUsers.get(roomId);
				connectUsers.put(roomId, (num - 1));
			}
			log.info("현재 인원 : " + connectUsers.get(roomId));
		}
	}

	//채팅방 불러오기
	public List<ChatRoomDto> findAllRoom(){
		List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
		List<Chat> all = chatRepository.findAll();
		try {
			for (int i = 0; i < all.size(); i++) {
				User user = userRepository.findById(all.get(i).getUser().getId()).orElseThrow(UserNotFoundException::new);
				chatRoomDtos.add(ChatRoomDto.of(all.get(i).getRoomId(), user.getNickname(), user, lastLine(all.get(i).getRoomId())));
			}
		} catch (NullPointerException e) {
			log.info(" [현재 채팅방 db 없음!] " + e);
		}
		return chatRoomDtos;
	}

	//채팅방 생성
	@Cacheable(key = "#nickname", value = "createRoom", unless = "#result == null", cacheManager = "cacheManager")
	public ChatRoomDto createRoom(String nickname) {
		long startTime = System.currentTimeMillis();
		User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
		ChatRoomDto chatRoom = new ChatRoomDto();
		long stopTime;
		if (!chatRepository.existsByUserId(user.getId())) {
			log.info("[createRoom] roomId 값이 없음");
			chatRoom = ChatRoomDto.create(nickname);
			ChatRoomMap.getInstance().getChatRooms().put(chatRoom.getRoomId(), chatRoom);
			Chat chat = new Chat(chatRoom.getRoomId(), user);
			chatRepository.save(chat);
			stopTime = System.currentTimeMillis();
			log.info("readFile : " + (stopTime - startTime) + " 초");
			return chatRoom;
		} else {
			log.info("[createRoom] roomId 값은 있지만 cache 적용 안됨");
			Optional<Chat> findChat = chatRepository.findByUserId(user.getId());
			chatRoom.setRoomId(findChat.get().getRoomId());
			if (!isChatFileExists(findChat.get().getRoomId())) {
				return null;
			}
			List<String> lastLine = lastLine(findChat.get().getRoomId());
			if (lastLine == null || lastLine.isEmpty()) {
				return null;
			}
			stopTime = System.currentTimeMillis();
			log.info("readFile : " + (stopTime - startTime) + " 초");
			return ChatRoomDto.of(findChat.get().getRoomId(), nickname, user, lastLine);
		}
	}

	private boolean isChatFileExists(String roomId) {
		String filePath = chatUploadLocation + "/" + roomId + ".txt";
		File file = new File(filePath);
		return file.exists();
	}

	// 파일 저장
	public void saveFile(ChatMessage chatMessage) {
		if (connectUsers.get(chatMessage.getRoomId()) != 0) {
			if (chatMessage.getType() == ChatMessage.MessageType.JOIN) {
				reset(chatMessage.getSender(), chatMessage.getRoomId());
			} else {
				countChat(chatMessage.getSender(), chatMessage.getRoomId());
			}
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("roomId", chatMessage.getRoomId());
		if (chatMessage.getType().toString().equals("JOIN")){
			jsonObject.addProperty("type", "JOINED");
		}else {
			jsonObject.addProperty("type", chatMessage.getType().toString());
		}
		jsonObject.addProperty("sender", chatMessage.getSender());
		jsonObject.addProperty("message", chatMessage.getMessage());
		jsonObject.addProperty("adminChat", adminChat.get(chatMessage.getRoomId()));
		jsonObject.addProperty("userChat", userChat.get(chatMessage.getRoomId()));

		Gson gson = new Gson();
		String json = gson.toJson(jsonObject);

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(chatUploadLocation + "/" + chatMessage.getRoomId() + ".txt", true)))){
			if (new File(chatUploadLocation + "/" + chatMessage.getRoomId() + ".txt").length() == 0) {
				out.println(json);
				chatAlarm(chatMessage.getSender(), chatMessage.getRoomId());
			} else {
				out.println("," + json);
			}
		} catch (IOException e) {
			log.error("[error] " + e);
		}
	}

	public Object readFile(String roomId) {
		long startTime = System.currentTimeMillis();
		String filePath = chatUploadLocation + "/" + roomId + ".txt";
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error("[error] " + e);
				return null;
			}
		}
		try {
			List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
			String jsonString = "[" + String.join(",", lines) + "]";
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonString);
			long stopTime = System.currentTimeMillis();
			log.info("readFile : " + (stopTime - startTime) + " 초");
			return obj;
		} catch (NoSuchFileException e) {
			throw new FileNotFoundException();
		} catch (IOException | ParseException e) {
			log.error("[error] " + e);
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
	public List<String> lastLine(String roomId) {
		String filePath = chatUploadLocation + "/" + roomId + ".txt";
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				return Collections.emptyList();
			} catch (IOException e) {
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {

			long fileLength = file.length();
			if (fileLength <= 0) {
				return Collections.emptyList();
			}
			randomAccessFile.seek(fileLength);
			long pointer = fileLength - 2;
			while (pointer > 0) {
				randomAccessFile.seek(pointer);
				char c = (char) randomAccessFile.read();
				if (c == '\n') {
					break;
				}
				pointer--;
			}
			randomAccessFile.seek(pointer + 1);
			String line = randomAccessFile.readLine();
			if (line == null || line.trim().isEmpty()) {
				return Collections.emptyList();
			}
			if (line.startsWith(",")) {
				line = line.substring(1);
			}

			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(line).getAsJsonObject();
			int adminChat = json.get("adminChat").getAsInt();
			int userChat = json.get("userChat").getAsInt();
			String message = json.get("message").getAsString().trim();
			String messages = new String(message.getBytes("iso-8859-1"), "utf-8");

			List<String> chat = new ArrayList<>();
			chat.add(Integer.toString(adminChat));
			chat.add(Integer.toString(userChat));
			chat.add(messages);
			return chat;
		} catch (IOException | JsonSyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
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