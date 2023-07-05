package com.dalcho.adme.config.security;

import com.dalcho.adme.oauth2.CustomOAuthService;
import com.dalcho.adme.oauth2.OAuth2SuccessHandler;
import com.dalcho.adme.oauth2.Oauth2FailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuthService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final Oauth2FailureHandler failureHandler;

    public static final String[] GET_WHITE_LIST = {
            "/tenSeconds/list"
    };

    public static final String[] USER_ENABLE = {
            "/tenSeconds/videos",
            "/tenSeconds/video/**",
            "/registry/**",
            "/comment/**",
            "/chat"
    };

    public static final String[] VIEW_LIST = {
            "/static/**",
            "/js/**",
            "/favicon.ico/**",
            "/user/**",
            "/taste",
            "/tenSeconds",
            "/",
            "/oauth2/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, GET_WHITE_LIST).authenticated()
                .antMatchers(VIEW_LIST).permitAll()
                .antMatchers(USER_ENABLE).hasAnyRole("USER", "ADMIN")
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/login")
                .deleteCookies("TokenCookie")
                .permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .oauth2Login().loginPage("/user/login") // 소셜 로그인 페이지 설정
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));

        configuration.addAllowedHeader("*");
        //configuration.setAllowedHeaders(Arrays.asList("X-Custom-Header", "Content-Type"));

        //configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST"));

//        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
