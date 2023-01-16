package com.dalcho.adme.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    public static final String[] GET_WHITE_LIST = {
            "/tenSeconds/list"
    };

    public static final String[] USER_ENABLE = {
            "/tenSeconds/videos",
            "/tenSeconds/video/**",
            "/registry/**",
            "/comment**"
    };

    public static final String[] VIEW_LIST = {
            "/static/**",
            "/js/**",
            "/favicon.ico/**",
            "/user/**",
            "/taste",
            "/tenSeconds",
            "/adme",
            "/"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin().disable();
        httpSecurity.csrf()
                .disable() // rest api 에서는 csrf 공격으로부터 안전하고 매번 api 요청으로부터 csrf 토큰을 받지 않아도 되어 disable로 설정
                .sessionManagement() // Rest Api 기반 애플리케이션 동작 방식 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않아 STATELESS 로 설정
                .and()// 클릭재킹 공격으로부터 보안 설정
                .headers()
                .frameOptions()
                .disable()
                .and() // 접근설정
                .authorizeRequests() // 요청에 의한 보안검사 시작
                .antMatchers(HttpMethod.GET, GET_WHITE_LIST).permitAll() // GET 요청 허용
                .antMatchers(VIEW_LIST).permitAll()
                .antMatchers(USER_ENABLE).hasAnyRole("USER","ADMIN") // USER 접근 가능
                .and() // 로그아웃 처리
                .logout()
                .logoutUrl("/user/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/user/login") // 로그아웃 처리 후 이동할 URL
                .deleteCookies("TokenCookie") // 쿠키삭제
                .permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증과정에서의 예외처리
                .and() // JWT Token 필터를 id/password 인증 필터 이전에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
