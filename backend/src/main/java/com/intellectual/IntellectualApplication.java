package com.intellectual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IntellectualApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntellectualApplication.class, args);
    }
}
