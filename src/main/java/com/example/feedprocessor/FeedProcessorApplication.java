package com.example.feedprocessor;

import com.example.feedprocessor.config.FeedConfiguration;
import com.example.feedprocessor.config.DirectoryConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(FeedConfiguration.class)
public class FeedProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedProcessorApplication.class, args);
    }
}
