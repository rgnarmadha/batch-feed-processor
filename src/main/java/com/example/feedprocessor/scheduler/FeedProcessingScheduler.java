package com.example.feedprocessor.scheduler;

import com.example.feedprocessor.service.FeedProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class FeedProcessingScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedProcessingScheduler.class);
    
    @Autowired
    private FeedProcessingService feedProcessingService;
    
    @Scheduled(cron = "${scheduling.cron:0 0 2 * * ?}")
    public void processFeeds() {
        logger.info("Scheduled feed processing started");
        try {
            feedProcessingService.processAllFeeds();
            logger.info("Scheduled feed processing completed successfully");
        } catch (Exception e) {
            logger.error("Error during scheduled feed processing: {}", e.getMessage(), e);
        }
    }
}
