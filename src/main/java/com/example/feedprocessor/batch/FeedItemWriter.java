package com.example.feedprocessor.batch;

import com.example.feedprocessor.service.DynamicTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedItemWriter implements ItemWriter<Map<String, String>> {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedItemWriter.class);
    
    private final DynamicTableService dynamicTableService;
    private final String tableName;
    private final String idColumn;
    private final FeedProcessingListener processingListener;
    
    public FeedItemWriter(DynamicTableService dynamicTableService, String tableName, 
                         String idColumn, FeedProcessingListener processingListener) {
        this.dynamicTableService = dynamicTableService;
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.processingListener = processingListener;
    }
    
    @Override
    public void write(Chunk<? extends Map<String, String>> chunk) throws Exception {
        logger.info("Processing chunk of {} records for table: {}", chunk.size(), tableName);
        
        List<Map<String, String>> insertsToProcess = new ArrayList<>();
        List<Map<String, String>> updatesToProcess = new ArrayList<>();
        
        for (Map<String, String> record : chunk) {
            try {
                String idValue = record.get(idColumn);
                
                if (idValue != null && dynamicTableService.recordExists(tableName, idColumn, idValue)) {
                    updatesToProcess.add(record);
                } else {
                    insertsToProcess.add(record);
                }
            } catch (Exception e) {
                processingListener.incrementFailedRecords();
                logger.error("Error processing record: {}", record, e);
                throw e;
            }
        }
        
        if (!insertsToProcess.isEmpty()) {
            try {
                dynamicTableService.insertRecordsBatch(tableName, insertsToProcess);
                processingListener.incrementInsertedRecords(insertsToProcess.size());
                logger.info("Batch inserted {} records", insertsToProcess.size());
            } catch (Exception e) {
                processingListener.incrementFailedRecords(insertsToProcess.size());
                logger.error("Error batch inserting records", e);
                throw e;
            }
        }
        
        for (Map<String, String> record : updatesToProcess) {
            try {
                String idValue = record.get(idColumn);
                dynamicTableService.updateRecord(tableName, idColumn, idValue, record);
                processingListener.incrementUpdatedRecords();
                logger.debug("Updated record with ID: {}", idValue);
            } catch (Exception e) {
                processingListener.incrementFailedRecords();
                logger.error("Error updating record: {}", record, e);
                throw e;
            }
        }
        
        logger.info("Completed chunk processing: {} inserts, {} updates", 
                   insertsToProcess.size(), updatesToProcess.size());
    }
}
