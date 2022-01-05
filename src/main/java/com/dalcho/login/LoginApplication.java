package com.dalcho.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Timestamped에서 null x
@SpringBootApplication
public class LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

}
