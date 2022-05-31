package com.dalcho.adme.controller;

import com.dalcho.adme.service.WebSocketService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WebSocketController  {
    private final WebSocketService webSocketService;

    @GetMapping("/websocket/count") // 접속자 수 띄우기
    public int userCount() {
        return webSocketService.userCount();
    }


}
