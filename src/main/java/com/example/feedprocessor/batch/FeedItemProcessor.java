package com.example.feedprocessor.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Map;

public class FeedItemProcessor implements ItemProcessor<Map<String, String>, Map<String, String>> {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedItemProcessor.class);
    
    private final String idColumn;
    
    public FeedItemProcessor(String idColumn) {
        this.idColumn = idColumn;
    }
    
    @Override
    public Map<String, String> process(Map<String, String> item) throws Exception {
        if (item == null || item.isEmpty()) {
            logger.warn("Skipping empty or null record");
            return null;
        }
        
        String idValue = item.get(idColumn);
        if (idValue == null || idValue.trim().isEmpty()) {
            logger.warn("Skipping record with missing or empty ID column '{}': {}", idColumn, item);
            return null;
        }
        
        logger.debug("Processing record with ID: {}", idValue);
        return item;
    }
}
