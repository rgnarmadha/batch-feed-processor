package com.example.feedprocessor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feed_column_configs")
public class FeedColumnConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_config_id", nullable = false)
    private FeedConfig feedConfig;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "nullable", nullable = false)
    private boolean nullable = true;
    
    @Column(name = "primary_key", nullable = false)
    private boolean primaryKey = false;
    
    @Column(name = "unique_constraint", nullable = false)
    private boolean unique = false;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
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
    
    public FeedConfig getFeedConfig() { return feedConfig; }
    public void setFeedConfig(FeedConfig feedConfig) { this.feedConfig = feedConfig; }
    
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
