package com.example.feedprocessor.repository;

import com.example.feedprocessor.entity.ProcessingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProcessingReportRepository extends JpaRepository<ProcessingReport, Long> {
    
    List<ProcessingReport> findByFeedNameOrderByProcessingStartTimeDesc(String feedName);
    
    List<ProcessingReport> findByStatusOrderByProcessingStartTimeDesc(ProcessingReport.ProcessingStatus status);
    
    @Query("SELECT pr FROM ProcessingReport pr WHERE pr.processingStartTime >= :startTime ORDER BY pr.processingStartTime DESC")
    List<ProcessingReport> findRecentReports(LocalDateTime startTime);
    
    @Query("SELECT pr.feedName, COUNT(pr), SUM(pr.totalRecords), SUM(pr.insertedRecords), SUM(pr.updatedRecords), SUM(pr.failedRecords) " +
           "FROM ProcessingReport pr WHERE pr.processingStartTime >= :startTime GROUP BY pr.feedName")
    List<Object[]> getProcessingSummary(LocalDateTime startTime);
}
