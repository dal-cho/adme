package com.dalcho.adme.controller.chat;

import com.dalcho.adme.dto.chat.ChatMessage;
import com.dalcho.adme.dto.chat.ChatRoomDto;
import com.dalcho.adme.service.Impl.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final ChatServiceImpl chatService;

    // 모든 채팅방 목록 반환(관리자)
    @GetMapping("/rooms")
    public List<ChatRoomDto> room() {
        return chatService.findAllRoom();
    }

    // 본인 채팅방(일반 유저)
    @GetMapping("/room/one/{nickname}")
    public ChatRoomDto roomOne(@PathVariable String nickname) {
        return chatService.roomOne(nickname);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoomDto createRoom(@RequestBody String nickname) {
        return chatService.createRoom(nickname);
    }

    // 완료된 채팅방 삭제하기
    @DeleteMapping("/room/one/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        chatService.deleteRoom(roomId);
    }

    // 삭제 후 채팅방 재 접속 막기
    @GetMapping("/room/{roomId}")
    public boolean getRoomInfo(@PathVariable String roomId) {
        return chatService.getRoomInfo(roomId);
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
}