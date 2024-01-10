package com.dalcho.adme.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final List<HandlerInterceptor> interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptors.forEach(registry::addInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.addAllowedOriginPattern("https://www.admee.site");
        config.setAllowCredentials(true);

        registry.addMapping("/**")
                .combine(config);
    }
}
