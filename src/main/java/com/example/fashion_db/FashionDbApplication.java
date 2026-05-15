package com.example.fashion_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FashionDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(FashionDbApplication.class, args);
    }

}
