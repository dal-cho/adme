package com.dalcho.adme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class AdmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmeApplication.class, args);
    }

}
