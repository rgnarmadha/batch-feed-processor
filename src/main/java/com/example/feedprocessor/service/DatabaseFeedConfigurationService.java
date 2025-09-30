package com.example.feedprocessor.service;

import com.example.feedprocessor.entity.FeedConfig;
import com.example.feedprocessor.entity.FeedColumnConfig;
import com.example.feedprocessor.repository.FeedConfigRepository;
import com.example.feedprocessor.repository.FeedColumnConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DatabaseFeedConfigurationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseFeedConfigurationService.class);
    
    @Autowired
    private FeedConfigRepository feedConfigRepository;
    
    @Autowired
    private FeedColumnConfigRepository feedColumnConfigRepository;
    
    public List<FeedConfig> getAllEnabledFeeds() {
        return feedConfigRepository.findEnabledFeedsWithColumns();
    }
    
    public Optional<FeedConfig> getFeedByName(String name) {
        return feedConfigRepository.findByNameWithColumns(name);
    }
    
    public FeedConfig saveFeedConfig(FeedConfig feedConfig) {
        logger.info("Saving feed configuration: {}", feedConfig.getName());
        return feedConfigRepository.save(feedConfig);
    }
    
    public void deleteFeedConfig(Long id) {
        logger.info("Deleting feed configuration with id: {}", id);
        feedConfigRepository.deleteById(id);
    }
    
    public List<FeedColumnConfig> getColumnConfigsForFeed(String feedName) {
        return feedColumnConfigRepository.findByFeedConfigName(feedName);
    }
    
    public FeedColumnConfig saveColumnConfig(FeedColumnConfig columnConfig) {
        logger.info("Saving column configuration: {} for feed: {}", 
                   columnConfig.getName(), columnConfig.getFeedConfig().getName());
        return feedColumnConfigRepository.save(columnConfig);
    }
    
    public void deleteColumnConfig(Long id) {
        logger.info("Deleting column configuration with id: {}", id);
        feedColumnConfigRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean feedExists(String name) {
        return feedConfigRepository.findByName(name).isPresent();
    }
    
    @Transactional(readOnly = true)
    public long getTotalFeedCount() {
        return feedConfigRepository.count();
    }
    
    @Transactional(readOnly = true)
    public long getEnabledFeedCount() {
        return feedConfigRepository.findByEnabledTrue().size();
    }
}
