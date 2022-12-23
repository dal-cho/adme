package com.dalcho.adme.config.security;

import org.springframework.beans.factory.annotation.Autowired;
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
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

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
                .antMatchers("/sign-api/sign-in", "/sign-api/sign-up", "/sign-api/exception").permitAll() // 권한 허용 URL 설정
                .antMatchers("/v2/api-docs", "/swagger-resources", "/swagger*/**", "/swagger-ui.html", "/webjars/**", "/swagger/**").permitAll() // 권한 허용 URL 설정
                .antMatchers("/favicon.ico/**","/tenseconds/**", "/signOn", "/test").permitAll()
                .antMatchers(HttpMethod.GET, "/tenseconds/**").permitAll() // tenseconds 로 시작하는 GET 요청 허용
                .antMatchers("**exception**", "/sign-api/exception").permitAll() // 'exception' 단어가 들어간 경로는 모두 허용
                .antMatchers("/static/**","/js/**").permitAll() // 접근 허용
//                    .antMatchers("/test").authenticated() // 이증된 사용자만 접근 가능
                .antMatchers("/tenseconds/video","/sign-api/cookie","/ten/test").hasAnyRole("USER","ADMIN") // USER 접근 가능
                .anyRequest().hasRole("ADMIN") // 기타 요청은 인증 권한을 가진 사용자에게 허용
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
