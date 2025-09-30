package com.example.feedprocessor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "feed_configs")
public class FeedConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "file_pattern", nullable = false)
    private String filePattern;
    
    @Column(name = "table_name", nullable = false)
    private String tableName;
    
    @Column(name = "id_column", nullable = false)
    private String idColumn;
    
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
    
    @Column(name = "delimiter", nullable = false)
    private String delimiter = ",";
    
    @Column(name = "quote_char", nullable = false)
    private String quoteChar = "\"";
    
    @Column(name = "escape_char", nullable = false)
    private String escapeChar = "\\";
    
    @Column(name = "skip_header_record", nullable = false)
    private boolean skipHeaderRecord = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "feedConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeedColumnConfig> columns;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
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
    
    public String getDelimiter() { return delimiter; }
    public void setDelimiter(String delimiter) { this.delimiter = delimiter; }
    
    public String getQuoteChar() { return quoteChar; }
    public void setQuoteChar(String quoteChar) { this.quoteChar = quoteChar; }
    
    public String getEscapeChar() { return escapeChar; }
    public void setEscapeChar(String escapeChar) { this.escapeChar = escapeChar; }
    
    public boolean isSkipHeaderRecord() { return skipHeaderRecord; }
    public void setSkipHeaderRecord(boolean skipHeaderRecord) { this.skipHeaderRecord = skipHeaderRecord; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<FeedColumnConfig> getColumns() { return columns; }
    public void setColumns(List<FeedColumnConfig> columns) { this.columns = columns; }
}
