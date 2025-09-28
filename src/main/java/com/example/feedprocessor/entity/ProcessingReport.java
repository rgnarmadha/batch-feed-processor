package com.example.feedprocessor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "processing_reports")
public class ProcessingReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "feed_name", nullable = false)
    private String feedName;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "table_name", nullable = false)
    private String tableName;
    
    @Column(name = "total_records")
    private Long totalRecords;
    
    @Column(name = "inserted_records")
    private Long insertedRecords;
    
    @Column(name = "updated_records")
    private Long updatedRecords;
    
    @Column(name = "failed_records")
    private Long failedRecords;
    
    @Column(name = "processing_start_time")
    private LocalDateTime processingStartTime;
    
    @Column(name = "processing_end_time")
    private LocalDateTime processingEndTime;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;
    
    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    public enum ProcessingStatus {
        SUCCESS, FAILED, PARTIAL_SUCCESS
    }

    public ProcessingReport() {}

    public ProcessingReport(String feedName, String fileName, String tableName) {
        this.feedName = feedName;
        this.fileName = fileName;
        this.tableName = tableName;
        this.processingStartTime = LocalDateTime.now();
        this.totalRecords = 0L;
        this.insertedRecords = 0L;
        this.updatedRecords = 0L;
        this.failedRecords = 0L;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFeedName() { return feedName; }
    public void setFeedName(String feedName) { this.feedName = feedName; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    
    public Long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Long totalRecords) { this.totalRecords = totalRecords; }
    
    public Long getInsertedRecords() { return insertedRecords; }
    public void setInsertedRecords(Long insertedRecords) { this.insertedRecords = insertedRecords; }
    
    public Long getUpdatedRecords() { return updatedRecords; }
    public void setUpdatedRecords(Long updatedRecords) { this.updatedRecords = updatedRecords; }
    
    public Long getFailedRecords() { return failedRecords; }
    public void setFailedRecords(Long failedRecords) { this.failedRecords = failedRecords; }
    
    public LocalDateTime getProcessingStartTime() { return processingStartTime; }
    public void setProcessingStartTime(LocalDateTime processingStartTime) { this.processingStartTime = processingStartTime; }
    
    public LocalDateTime getProcessingEndTime() { return processingEndTime; }
    public void setProcessingEndTime(LocalDateTime processingEndTime) { this.processingEndTime = processingEndTime; }
    
    public ProcessingStatus getStatus() { return status; }
    public void setStatus(ProcessingStatus status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
