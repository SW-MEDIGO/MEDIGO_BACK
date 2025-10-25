package com.example.oswc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ★ SecurityConfig를 강제 포함
@org.springframework.context.annotation.Import(com.example.oswc.config.SecurityConfig.class)
@SpringBootApplication
public class OswcApplication {
    public static void main(String[] args) {
        SpringApplication.run(OswcApplication.class, args);
    }
}
