package com.dalcho.adme.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


//websocket 핸들러를 구현하기 위해서 기본적으로 TextWebSocketHandler를 상속
public interface WebSocketService  {

    int userCount();
}

