package com.dalcho.adme.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig {
    private final AuthenticationFailureHandler customFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //SecurityFilterChain 를 빈으로 등록하는 방식
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                // image 폴더를 login 없이 허용
                .antMatchers("/img/**").permitAll()

                // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()

                // js 폴더를 login 없이 허용
                .antMatchers("/js/**").permitAll()

                // 회원 관리 URL 전부를 login 없이 허용
                .antMatchers("/user/**").permitAll()

                // h2-console URL 을 login 없이 허용
                .antMatchers("/h2-console/**").permitAll()

                // 로그인 하기 전 기본 페이지 로그인 없이 허용
                .antMatchers("/").permitAll() // adme 페이지

                .antMatchers("/taste").permitAll() // 공감공간 페이지

                // 그 외 모든 요청은 인증과정 필요
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/user/login")
//                .failureUrl("/user/login/error")
                .failureHandler(customFailureHandler)
                .defaultSuccessUrl("/adme")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .permitAll();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}
