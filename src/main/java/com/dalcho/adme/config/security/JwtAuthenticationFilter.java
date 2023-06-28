package com.dalcho.adme.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public void doFilterInternal(
//            HttpServletRequest servletRequest,
//            HttpServletResponse servletResponse,
//            FilterChain filterChain) throws ServletException, IOException {
//        // servletRequest 에서 token 추출
//        String token = jwtTokenProvider.resolveToken(servletRequest);
//
//        log.info("[dofilterInternal] token 값 유효성 체크 시작");
//        // token 이 유효하다면 Authentication 객체를 생성해서 SecurityContextHolder 에 추가
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("[doFilterInternal] token 값 유효성 체크 완료");
//        }
//        else {
//            log.info("[doFilterInternal] 유효한 JWT 토큰이 없습니다, uri: {}", servletRequest.getRequestURL());
//        }
//
//        // 요청 정보가 매칭되지 않을경우 동작
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final String BEARER = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        request.setAttribute("existsToken", true); // 토큰 존재 여부 초기화

        if (isEmptyToken(token)) request.setAttribute("existsToken", false); // 토큰이 없는 경우 false로 변경

        if (token == null || !token.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = parseBearer(token);

        if (jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isEmptyToken(String token) {
        return token == null || "".equals(token);
    }

    private String parseBearer(String token) {
        return token.substring(BEARER.length()); //"Bearer "를 제외한 나머지
    }
}