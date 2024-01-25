package com.dalcho.adme.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://www.admee.site")
                .allowedHeaders("Authorization", "Content-Type")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
