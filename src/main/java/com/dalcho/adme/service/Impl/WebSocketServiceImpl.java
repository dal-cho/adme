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
//websocket 핸들러를 구현하기 위해서 기본적으로 TextWebSocketHandler를 상속
public class WebSocketServiceImpl extends TextWebSocketHandler implements WebSocketService {
    private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());

    //private static final List<Session> session = new ArrayList<Session>();

    public int userCount() {
        return sessionUsers.size();
    }


    @OnOpen // 사용자가 페이지에 접속할 때 실행되는 @OnOpen메서드에서 세션 리스트에 담아준다.
    public void open(Session newUser) throws IOException {
        //session.add(newUser);
        sessionUsers.add(newUser);
        System.out.println("현재 접속자 수 : " + sessionUsers.size());
        userCount();
    }     // 사용자가 증가할 때마다 세션의 getId()는 1씩 증가하며 문자열 형태로 지정된다.


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
        System.out.println("remove : " + nowUser);
        System.out.println("sessionUsers.size() = " + sessionUsers.size());
        log.info(String.format("Session %s closed because of %s", nowUser.getId(), closeReason));
    }

    //에러 발생시
    @OnError
    public void onError(Session session, Throwable e) {
        log.info("문제 세션 : " + session);
        e.printStackTrace();
    }

    @Override//웹소켓 클라이언트가 언결을 직접 끊거나 서버에서 타임아웃이 발생해서 연결을 끊을때 호출된다.
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
}


