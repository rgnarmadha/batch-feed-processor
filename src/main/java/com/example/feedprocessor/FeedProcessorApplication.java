package com.example.feedprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeedProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedProcessorApplication.class, args);
    }
}
