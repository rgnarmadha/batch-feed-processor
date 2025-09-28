package com.example.feedprocessor.controller;

import com.example.feedprocessor.entity.ProcessingReport;
import com.example.feedprocessor.repository.ProcessingReportRepository;
import com.example.feedprocessor.service.FeedProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/feed-processing")
public class FeedProcessingController {
    
    @Autowired
    private FeedProcessingService feedProcessingService;
    
    @Autowired
    private ProcessingReportRepository reportRepository;
    
    @PostMapping("/trigger")
    public ResponseEntity<String> triggerProcessing() {
        try {
            feedProcessingService.processAllFeeds();
            return ResponseEntity.ok("Feed processing triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error triggering feed processing: " + e.getMessage());
        }
    }
    
    @GetMapping("/reports")
    public ResponseEntity<List<ProcessingReport>> getReports(
            @RequestParam(required = false) String feedName,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "24") int hoursBack) {
        
        LocalDateTime startTime = LocalDateTime.now().minusHours(hoursBack);
        
        List<ProcessingReport> reports;
        if (feedName != null) {
            reports = reportRepository.findByFeedNameOrderByProcessingStartTimeDesc(feedName);
        } else if (status != null) {
            ProcessingReport.ProcessingStatus statusEnum = ProcessingReport.ProcessingStatus.valueOf(status.toUpperCase());
            reports = reportRepository.findByStatusOrderByProcessingStartTimeDesc(statusEnum);
        } else {
            reports = reportRepository.findRecentReports(startTime);
        }
        
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/reports/summary")
    public ResponseEntity<List<Object[]>> getProcessingSummary(
            @RequestParam(defaultValue = "24") int hoursBack) {
        
        LocalDateTime startTime = LocalDateTime.now().minusHours(hoursBack);
        List<Object[]> summary = reportRepository.getProcessingSummary(startTime);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Feed Processing Service is running");
    }
}
