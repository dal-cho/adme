package com.dalcho.adme.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@ServerEndpoint("/chatbot")
public class ChatBotServiceImpl extends TextWebSocketHandler {
    private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());

    @OnOpen
    public void open(Session newUser) throws IOException {
        sessionUsers.add(newUser);
    }


    @OnMessage // 사용자로부터 메시지를 받았을 때, 실행된다.
    public void onMessage(Session receiveSession, String msg) throws IOException {
        for (int i = 0; i < sessionUsers.size(); i++) {
            if (!receiveSession.getId().equals(sessionUsers.get(i).getId())) {
                try {
                    sessionUsers.get(i).getBasicRemote().sendText("$" + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sessionUsers.get(i).getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    @OnClose
    public void onClose(Session nowUser, CloseReason closeReason) {
        sessionUsers.remove(nowUser);
    }

    //에러 발생시
    @OnError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
    }

    @Override//웹소켓 클라이언트가 언결을 직접 끊거나 서버에서 타임아웃이 발생해서 연결을 끊을때 호출된다.
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }

}
