package com.dalcho.adme.controller.chat;

import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.dto.LastMessage;
import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatRoomDto;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final ChatServiceImpl chatService;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();
    private final JwtTokenProvider jwtTokenProvider;
    // 모든 채팅방 목록 반환(관리자)
    @GetMapping("/rooms")
    public List<ChatRoomDto> room() {
        return chatService.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoomDto createRoom(@RequestBody String nickname) {
        return chatService.createRoom(nickname);
    }

    // 채팅방 기록 갖고오기
    @GetMapping("/room/enter/{roomId}/{roomName}")
    public Object readFile(@PathVariable String roomId, @PathVariable String roomName) {
        return chatService.readFile(roomId);
    }

    // 채팅방 기록 저장하기
    @PostMapping("/room/enter/{roomId}/{roomName}")
    public void saveFile(@PathVariable String roomId, @PathVariable String roomName, @RequestBody ChatMessage chatMessage){
        chatService.saveFile(chatMessage);
    }
    @GetMapping ("/room/enter/{roomId}")
    LastMessage lastLine(@PathVariable String roomId) {
        return chatService.lastLine(roomId);
    }

    @GetMapping("/find-nickname/{token}")
    public String findNickname(@PathVariable String token){
        return jwtTokenProvider.getNickname(token);
    }

    @GetMapping("/room/subscribe")
    public SseEmitter subscribe(String id) throws IOException {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        CLIENTS.put(id, emitter);
        emitter.send(SseEmitter.event().name("connect") // 해당 이벤트의 이름 지정
                .data("connected!")); // 503 에러 방지를 위한 더미 데이터
        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        return emitter;
    }

    @GetMapping("/room/publish")
    @Async // 비동기
    public void publish(String sender, String roomId) {
        log.info("[SSE] - sender : " + sender);
        Set<String> deadIds = new HashSet<>();
        CLIENTS.forEach((id, emitter) -> {
            try {
                ChatMessage chatMessage = chatService.chatAlarm(sender, roomId);
                System.out.println("[SSE] chatMessage : " + chatMessage);
                emitter.send(chatMessage, MediaType.APPLICATION_JSON);
                System.out.println("[SSE] send 완료");
            } catch (Exception e) {
                log.error("[error]  " + e);
                deadIds.add(id);
                log.warn("disconnected id : {}", id);
            }
            deadIds.forEach(CLIENTS::remove);
        });
    }
}