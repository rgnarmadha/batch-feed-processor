package com.example.feedprocessor.config;

import com.example.feedprocessor.batch.*;
import com.example.feedprocessor.config.FeedConfiguration;
import com.example.feedprocessor.repository.ProcessingReportRepository;
import com.example.feedprocessor.service.DynamicTableService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class BatchConfiguration {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private DynamicTableService dynamicTableService;
    
    @Autowired
    private ProcessingReportRepository reportRepository;
    
    public Job createFeedProcessingJob(String jobName, String filePath, String tableName, 
                                     String idColumn, String feedName, String fileName, 
                                     FeedConfiguration.FeedConfig feedConfig) {
        
        FeedProcessingListener listener = new FeedProcessingListener(reportRepository, feedName, fileName, tableName);
        
        return new JobBuilder(jobName, jobRepository)
                .listener(listener)
                .start(createFeedProcessingStep(filePath, tableName, idColumn, listener, feedConfig))
                .build();
    }
    
    private Step createFeedProcessingStep(String filePath, String tableName, String idColumn, 
                                        FeedProcessingListener listener, FeedConfiguration.FeedConfig feedConfig) {
        
        FeedItemReader reader = new FeedItemReader(filePath, feedConfig);
        FeedItemProcessor processor = new FeedItemProcessor(idColumn);
        FeedItemWriter writer = new FeedItemWriter(dynamicTableService, tableName, idColumn, listener);
        
        return new StepBuilder("feedProcessingStep", jobRepository)
                .<Map<String, String>, Map<String, String>>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(100)
                .skip(Exception.class)
                .build();
    }
}
