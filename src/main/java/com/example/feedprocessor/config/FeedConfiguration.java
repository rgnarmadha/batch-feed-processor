package com.example.feedprocessor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

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
        private SchemaConfig schema;
        private String delimiter = ",";
        private String quoteChar = "\"";
        private String escapeChar = "\\";
        private boolean skipHeaderRecord = true;

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
        
        public SchemaConfig getSchema() { return schema; }
        public void setSchema(SchemaConfig schema) { this.schema = schema; }
        
        public String getDelimiter() { return delimiter; }
        public void setDelimiter(String delimiter) { this.delimiter = delimiter; }
        
        public String getQuoteChar() { return quoteChar; }
        public void setQuoteChar(String quoteChar) { this.quoteChar = quoteChar; }
        
        public String getEscapeChar() { return escapeChar; }
        public void setEscapeChar(String escapeChar) { this.escapeChar = escapeChar; }
        
        public boolean isSkipHeaderRecord() { return skipHeaderRecord; }
        public void setSkipHeaderRecord(boolean skipHeaderRecord) { this.skipHeaderRecord = skipHeaderRecord; }
    }
    
    public static class SchemaConfig {
        private List<ColumnConfig> columns;
        
        public List<ColumnConfig> getColumns() { return columns; }
        public void setColumns(List<ColumnConfig> columns) { this.columns = columns; }
    }
    
    public static class ColumnConfig {
        private String name;
        private String type;
        private boolean nullable = true;
        private boolean primaryKey = false;
        private boolean unique = false;
        private String defaultValue;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public boolean isNullable() { return nullable; }
        public void setNullable(boolean nullable) { this.nullable = nullable; }
        
        public boolean isPrimaryKey() { return primaryKey; }
        public void setPrimaryKey(boolean primaryKey) { this.primaryKey = primaryKey; }
        
        public boolean isUnique() { return unique; }
        public void setUnique(boolean unique) { this.unique = unique; }
        
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    }

    public String getInputDirectory() { 
        System.out.println("DEBUG: getInputDirectory() called, returning: " + inputDirectory);
        return inputDirectory; 
    }
    public void setInputDirectory(String inputDirectory) { 
        System.out.println("DEBUG: setInputDirectory() called with: " + inputDirectory);
        this.inputDirectory = inputDirectory; 
    }
    
    public String getProcessedDirectory() { 
        System.out.println("DEBUG: getProcessedDirectory() called, returning: " + processedDirectory);
        return processedDirectory; 
    }
    public void setProcessedDirectory(String processedDirectory) { 
        System.out.println("DEBUG: setProcessedDirectory() called with: " + processedDirectory);
        this.processedDirectory = processedDirectory; 
    }
    
    public String getErrorDirectory() { 
        System.out.println("DEBUG: getErrorDirectory() called, returning: " + errorDirectory);
        return errorDirectory; 
    }
    public void setErrorDirectory(String errorDirectory) { 
        System.out.println("DEBUG: setErrorDirectory() called with: " + errorDirectory);
        this.errorDirectory = errorDirectory; 
    }
    
    public List<FeedConfig> getFeeds() { return feeds; }
    public void setFeeds(List<FeedConfig> feeds) { this.feeds = feeds; }
}
