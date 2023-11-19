package com.dalcho.adme.config;

import com.dalcho.adme.config.security.JwtTokenProvider;
import com.dalcho.adme.service.Impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailServiceImpl userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            log.info("websocket 연결 - jwt token 검증");
            String accessToken = headerAccessor.getFirstNativeHeader("Authorization");
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getNickname(accessToken));
            if (userDetails != null) {
                log.info("[preSend] 인증 확인");
            } else {
                throw new BadCredentialsException("Invalid JWT token");
            }
        }
        return message;
    }
}