package com.dalcho.adme.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        // servletRequest 에서 token 추출
        String token = jwtTokenProvider.resolveToken(servletRequest);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("[dofilterInternal] token 값 유효성 체크 시작");

        // UserDetails 값 유무
        try {
            if (token != null && auth != null) {
                log.info("[doFilterInternal] login 완료");
            } else if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("[doFilterInternal] token 값 유효성 체크 완료");
            } else if (auth != null) {
                log.info(" [ oauth ] login ");
            } else {
                log.info("[doFilterInternal] 유효한 JWT 토큰이 없습니다, uri: {}", servletRequest.getRequestURL());
            }
        } catch (AuthenticationException | NullPointerException e) {
            log.info("doFilterInternal");
        } finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}