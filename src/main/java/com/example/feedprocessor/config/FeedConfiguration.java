package com.example.feedprocessor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "feed.processor")
public class FeedConfiguration {
    
    private String inputDirectory;
    private String processedDirectory;
    private String errorDirectory;
    private List<FeedConfig> feeds;

    public static class FeedConfig {
        private String name;
        private String filePattern;
        private String tableName;
        private String idColumn;
        private boolean enabled = true;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getFilePattern() { return filePattern; }
        public void setFilePattern(String filePattern) { this.filePattern = filePattern; }
        
        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }
        
        public String getIdColumn() { return idColumn; }
        public void setIdColumn(String idColumn) { this.idColumn = idColumn; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public String getInputDirectory() { return inputDirectory; }
    public void setInputDirectory(String inputDirectory) { this.inputDirectory = inputDirectory; }
    
    public String getProcessedDirectory() { return processedDirectory; }
    public void setProcessedDirectory(String processedDirectory) { this.processedDirectory = processedDirectory; }
    
    public String getErrorDirectory() { return errorDirectory; }
    public void setErrorDirectory(String errorDirectory) { this.errorDirectory = errorDirectory; }
    
    public List<FeedConfig> getFeeds() { return feeds; }
    public void setFeeds(List<FeedConfig> feeds) { this.feeds = feeds; }
}
