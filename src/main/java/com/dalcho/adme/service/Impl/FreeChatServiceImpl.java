package com.dalcho.adme.service.Impl;

import com.dalcho.adme.service.WebSocketService;
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
@ServerEndpoint("/websocket")
public class FreeChatServiceImpl extends TextWebSocketHandler implements WebSocketService {
    private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());

    public int userCount() {
        return sessionUsers.size();
    }

    @OnOpen
    public void open(Session newUser) throws IOException {
        sessionUsers.add(newUser);
        log.info("현재 접속자 수 : " + sessionUsers.size());
        userCount();
    }


    @OnMessage
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
        log.info("sessionUsers.size() = " + sessionUsers.size());
        log.info(String.format("Session %s closed because of %s", nowUser.getId(), closeReason));
    }

    //에러 발생시
    @OnError
    public void onError(Session session, Throwable e) {
        log.info("문제 세션 : " + session);
        e.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }
}
