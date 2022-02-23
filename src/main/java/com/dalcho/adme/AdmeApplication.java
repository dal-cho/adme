package com.dalcho.adme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Timestamped에서 null x
@SpringBootApplication
public class AdmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmeApplication.class, args);
    }

}
