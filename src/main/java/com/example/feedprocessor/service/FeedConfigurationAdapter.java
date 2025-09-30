package com.example.feedprocessor.service;

import com.example.feedprocessor.config.FeedConfiguration;
import com.example.feedprocessor.entity.FeedConfig;
import com.example.feedprocessor.entity.FeedColumnConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedConfigurationAdapter {
    
    @Autowired
    private DatabaseFeedConfigurationService databaseFeedConfigurationService;
    
    public List<FeedConfiguration.FeedConfig> getAllEnabledFeeds() {
        try {
            List<FeedConfig> dbFeeds = databaseFeedConfigurationService.getAllEnabledFeeds();
            System.out.println("DEBUG: Retrieved " + dbFeeds.size() + " enabled feeds from database");
            return dbFeeds.stream()
                    .map(this::convertToFeedConfig)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve enabled feeds: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public FeedConfiguration.FeedConfig getFeedByName(String name) {
        return databaseFeedConfigurationService.getFeedByName(name)
                .map(this::convertToFeedConfig)
                .orElse(null);
    }
    
    private FeedConfiguration.FeedConfig convertToFeedConfig(FeedConfig dbFeed) {
        FeedConfiguration.FeedConfig feedConfig = new FeedConfiguration.FeedConfig();
        
        feedConfig.setName(dbFeed.getName());
        feedConfig.setFilePattern(dbFeed.getFilePattern());
        feedConfig.setTableName(dbFeed.getTableName());
        feedConfig.setIdColumn(dbFeed.getIdColumn());
        feedConfig.setEnabled(dbFeed.isEnabled());
        feedConfig.setDelimiter(dbFeed.getDelimiter());
        feedConfig.setQuoteChar(dbFeed.getQuoteChar());
        feedConfig.setEscapeChar(dbFeed.getEscapeChar());
        feedConfig.setSkipHeaderRecord(dbFeed.isSkipHeaderRecord());
        
        if (dbFeed.getColumns() != null && !dbFeed.getColumns().isEmpty()) {
            FeedConfiguration.SchemaConfig schemaConfig = new FeedConfiguration.SchemaConfig();
            List<FeedConfiguration.ColumnConfig> columnConfigs = dbFeed.getColumns().stream()
                    .map(this::convertToColumnConfig)
                    .collect(Collectors.toList());
            schemaConfig.setColumns(columnConfigs);
            feedConfig.setSchema(schemaConfig);
        }
        
        return feedConfig;
    }
    
    private FeedConfiguration.ColumnConfig convertToColumnConfig(FeedColumnConfig dbColumn) {
        FeedConfiguration.ColumnConfig columnConfig = new FeedConfiguration.ColumnConfig();
        
        columnConfig.setName(dbColumn.getName());
        columnConfig.setType(dbColumn.getType());
        columnConfig.setNullable(dbColumn.isNullable());
        columnConfig.setPrimaryKey(dbColumn.isPrimaryKey());
        columnConfig.setUnique(dbColumn.isUnique());
        columnConfig.setDefaultValue(dbColumn.getDefaultValue());
        
        return columnConfig;
    }
}
