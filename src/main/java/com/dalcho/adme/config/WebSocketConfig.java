package com.dalcho.adme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 컨테이너 등록
@EnableWebSocketMessageBroker // 웹소켓 서버 사용 설정
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    // connection을 맺을때 CORS 허용
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
        // endpoint는 양 사용자 간 웹소켓 핸드 셰이크를 위해 지정
    }
}
