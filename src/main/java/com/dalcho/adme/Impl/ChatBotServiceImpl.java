package com.dalcho.adme.Impl;

import com.google.gson.Gson;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    private Gson gson = new Gson(); // JSON을 파싱하는 클래스

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
        ChatMessage message = gson.fromJson(msg, ChatMessage.class);
        // State가 0이라면 초기 접속
        if (message.getState() == 0) {
            // 세션 만들기
            try {
                // 파일로부터 채팅 내용을 읽어와서 보내기
                receiveSession.getBasicRemote().sendText(readFile());
            } catch (Throwable e) {
                // 에러가 발생할 경우.
                e.printStackTrace();
            }
            // State가 1이라면 일반 메시지
        } else if (message.getState() == 1) {
            // 세션 확인 하기
            if (receiveSession != null) {
                // 파일에 저장하기
                saveFile(message.getId(), message.getValue());
            }
        }
    }

    // 채팅 내용을 파일로 부터 읽어온다.
    private String readFile() {
        File file = new File("C:\\Users\\cjera\\Desktop\\달코\\chat\\chat.txt");
        // 파일 있는지 검사
        if (!file.exists()) {
            return "";
        }
        // 파일을 읽어온다.
        try (FileInputStream stream = new FileInputStream(file)) {
            return new String(stream.readAllBytes());
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    // 파일를 저장하는 함수
    private void saveFile(String id, String message) {
        // 메시지 내용
        String msg = id + ":  " + message + "\n";
        // 파일을 저장한다.
        try (FileOutputStream stream = new FileOutputStream("C:\\Users\\cjera\\Desktop\\달코\\chat\\chat.txt", true)) {
            stream.write(msg.getBytes("UTF-8"));
        } catch (Throwable e) {
            e.printStackTrace();
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

    class ChatMessage {
        // id
        private String id;
        // state
        private int state;
        // 내용
        private String value;

        // getter, setter
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
