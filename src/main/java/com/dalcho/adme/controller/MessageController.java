package com.dalcho.adme.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

@Controller
@ServerEndpoint("/websocket")
public class MessageController extends Socket {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private static final List<Session> session = new ArrayList<Session>();


    @GetMapping("/chat")
    public String index() {
        return "chat";
    }


    @OnOpen // 사용자가 페이지에 접속할 때 실행되는 @OnOpen메서드에서 세션 리스트에 담아준다.
    public void open(Session newUser) {
        System.out.println("connected");
        session.add(newUser);
        System.out.println(newUser.getId());
    }     // 사용자가 증가할 때마다 세션의 getId()는 1씩 증가하며 문자열 형태로 지정된다.


    @OnMessage // 사용자로부터 메시지를 받았을 때, 실행된다.
    public void getMsg(String msg) {
        for (int i = 0; i < session.size(); i++) {
                try {
                    session.get(i).getBasicRemote().sendText(msg); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    //에러 발생시
    @OnError
    public void onError(Session session, Throwable e) {
        logger.info("문제 세션 : "+ session);
        System.out.println(e.toString());
    }
}
