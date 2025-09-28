package com.example.feedprocessor.batch;

import com.example.feedprocessor.entity.ProcessingReport;
import com.example.feedprocessor.repository.ProcessingReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class FeedProcessingListener implements JobExecutionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedProcessingListener.class);
    
    private final ProcessingReportRepository reportRepository;
    private final String feedName;
    private final String fileName;
    private final String tableName;
    
    private ProcessingReport report;
    private final AtomicLong totalRecords = new AtomicLong(0);
    private final AtomicLong insertedRecords = new AtomicLong(0);
    private final AtomicLong updatedRecords = new AtomicLong(0);
    private final AtomicLong failedRecords = new AtomicLong(0);
    
    public FeedProcessingListener(ProcessingReportRepository reportRepository, 
                                String feedName, String fileName, String tableName) {
        this.reportRepository = reportRepository;
        this.feedName = feedName;
        this.fileName = fileName;
        this.tableName = tableName;
    }
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Starting processing for feed: {}, file: {}", feedName, fileName);
        report = new ProcessingReport(feedName, fileName, tableName);
        report = reportRepository.save(report);
    }
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        report.setProcessingEndTime(LocalDateTime.now());
        report.setTotalRecords(totalRecords.get());
        report.setInsertedRecords(insertedRecords.get());
        report.setUpdatedRecords(updatedRecords.get());
        report.setFailedRecords(failedRecords.get());
        
        if (jobExecution.getStatus().isUnsuccessful()) {
            report.setStatus(ProcessingReport.ProcessingStatus.FAILED);
            report.setErrorMessage(jobExecution.getAllFailureExceptions().toString());
        } else if (failedRecords.get() > 0) {
            report.setStatus(ProcessingReport.ProcessingStatus.PARTIAL_SUCCESS);
        } else {
            report.setStatus(ProcessingReport.ProcessingStatus.SUCCESS);
        }
        
        reportRepository.save(report);
        
        logger.info("Completed processing for feed: {}. Total: {}, Inserted: {}, Updated: {}, Failed: {}", 
                   feedName, totalRecords.get(), insertedRecords.get(), updatedRecords.get(), failedRecords.get());
    }
    
    public void incrementTotalRecords() {
        totalRecords.incrementAndGet();
    }
    
    public void incrementInsertedRecords() {
        insertedRecords.incrementAndGet();
    }
    
    public void incrementInsertedRecords(int count) {
        insertedRecords.addAndGet(count);
    }
    
    public void incrementUpdatedRecords() {
        updatedRecords.incrementAndGet();
    }
    
    public void incrementUpdatedRecords(int count) {
        updatedRecords.addAndGet(count);
    }
    
    public void incrementFailedRecords() {
        failedRecords.incrementAndGet();
    }
    
    public void incrementFailedRecords(int count) {
        failedRecords.addAndGet(count);
    }
}
