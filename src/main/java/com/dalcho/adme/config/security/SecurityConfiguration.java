package com.dalcho.adme.config.security;

import com.dalcho.adme.domain.UserRole;
import com.dalcho.adme.oauth2.CustomOAuthService;
import com.dalcho.adme.oauth2.OAuth2SuccessHandler;
import com.dalcho.adme.oauth2.Oauth2FailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuthService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final Oauth2FailureHandler failureHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public static final String[] VIEW_LIST = {
            "/static/**",
            "/css/**",
            "/js/**",
            "/favicon.ico/**",
            "/user/**",
            "/taste",
            "/tenSeconds",
            "/",
            "/sign-up",
            "/templates/**"
    };

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().configurationSource(corsConfigurationSource());
    http.csrf().disable() // rest api 에서는 csrf 공격으로부터 안전하고 매번 api 요청으로부터 csrf 토큰을 받지 않아도 되어 disable로 설정
            .sessionManagement() // Rest Api 기반 애플리케이션 동작 방식 설정
            .and()
            .headers()
            .frameOptions()
            .disable();

    http.authorizeRequests()
            //.antMatchers("/admin-chat.html").hasAuthority("ADMIN")
            .regexMatchers(".*admin-chat.*").hasAuthority(UserRole.ADMIN.name())
            .antMatchers("/rooms").hasAuthority(UserRole.ADMIN.name())
            .antMatchers("/sign-up").permitAll()
            .antMatchers("/sign-in").permitAll()
            .antMatchers("/health").permitAll()
            .antMatchers(VIEW_LIST).permitAll()
            .anyRequest().authenticated();

    http.oauth2Login().loginPage("/templates/login.html")
            .and()
            .logout().logoutSuccessUrl("/templates/login.html")
            .deleteCookies("TokenCookie");

    http.oauth2Login()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(successHandler)
            .failureHandler(failureHandler);

    http.formLogin()
            .loginPage("/user/login")
            .defaultSuccessUrl("/chat")
            .and()
            .logout()
            .logoutUrl("/user/logout") // 로그아웃 처리 URL
            .logoutSuccessUrl("/user/login"); // 로그아웃 처리 후 이동할 URL
            //.permitAll();

    http.exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("https://www.admee.site","https://api.admee.site","wss://api.admee.site"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
