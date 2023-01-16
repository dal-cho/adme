//package com.dalcho.adme.service.Impl;
//
//import com.dalcho.adme.service.WebSocketService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//@ServerEndpoint("/websocket")
////websocket 핸들러를 구현하기 위해서 기본적으로 TextWebSocketHandler를 상속
//public class WebSocketServiceImpl extends TextWebSocketHandler implements WebSocketService {
//    private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());
//
//    //private static final List<Session> session = new ArrayList<Session>();
//
//    public int userCount() {
//        return sessionUsers.size();
//    }
//
//
//    @OnOpen // 사용자가 페이지에 접속할 때 실행되는 @OnOpen메서드에서 세션 리스트에 담아준다.
//    public void open(Session newUser) throws IOException {
//        //session.add(newUser);
//        sessionUsers.add(newUser);
//        System.out.println("현재 접속자 수 : " + sessionUsers.size());
//        userCount();
//    }     // 사용자가 증가할 때마다 세션의 getId()는 1씩 증가하며 문자열 형태로 지정된다.
//
//
//    @OnMessage // 사용자로부터 메시지를 받았을 때, 실행된다.
//    public void onMessage(Session receiveSession, String msg) throws IOException {
//        for (int i = 0; i < sessionUsers.size(); i++) {
//            if (!receiveSession.getId().equals(sessionUsers.get(i).getId())) {
//                try {
//                    sessionUsers.get(i).getBasicRemote().sendText("$" + msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    sessionUsers.get(i).getBasicRemote().sendText(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
//}