package com.example.feedprocessor.service;

import com.example.feedprocessor.batch.FeedItemReader;
import com.example.feedprocessor.config.BatchConfiguration;
import com.example.feedprocessor.config.FeedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.feedprocessor.config.DirectoryConfiguration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class FeedProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedProcessingService.class);
    
    @Autowired
    private DirectoryConfiguration directoryConfiguration;
    
    @Autowired
    private FeedConfigurationAdapter feedConfigurationAdapter;
    
    @Autowired
    private BatchConfiguration batchConfiguration;
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private DynamicTableService dynamicTableService;
    
    public void processAllFeeds() {
        logger.info("Starting feed processing for all configured feeds");
        
        try {
            createDirectoriesIfNotExist();
            
            System.out.println("DEBUG: About to call feedConfigurationAdapter.getAllEnabledFeeds()");
            List<FeedConfiguration.FeedConfig> enabledFeeds = feedConfigurationAdapter.getAllEnabledFeeds();
            System.out.println("DEBUG: Retrieved " + enabledFeeds.size() + " enabled feeds");
            
            for (FeedConfiguration.FeedConfig feedConfig : enabledFeeds) {
                if (feedConfig.isEnabled()) {
                    processFeed(feedConfig);
                } else {
                    logger.info("Skipping disabled feed: {}", feedConfig.getName());
                }
            }
            
            logger.info("Completed feed processing for all feeds");
        } catch (Exception e) {
            logger.error("Error during feed processing: {}", e.getMessage(), e);
            System.err.println("ERROR in processAllFeeds: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private void processFeed(FeedConfiguration.FeedConfig feedConfig) {
        try {
            logger.info("Processing feed: {}", feedConfig.getName());
            
            List<File> files = findMatchingFiles(feedConfig.getFilePattern());
            
            if (files.isEmpty()) {
                logger.info("No files found for feed: {} with pattern: {}", 
                           feedConfig.getName(), feedConfig.getFilePattern());
                return;
            }
            
            for (File file : files) {
                processFile(file, feedConfig);
            }
            
        } catch (Exception e) {
            logger.error("Error processing feed {}: {}", feedConfig.getName(), e.getMessage(), e);
        }
    }
    
    private void processFile(File file, FeedConfiguration.FeedConfig feedConfig) {
        try {
            logger.info("Processing file: {} for feed: {}", file.getName(), feedConfig.getName());
            
            FeedItemReader reader = new FeedItemReader(file.getAbsolutePath(), feedConfig);
            reader.read();
            String[] headers = reader.getHeaders();
            reader.close();
            
            if (headers == null || headers.length == 0) {
                logger.error("No headers found in file: {}", file.getName());
                moveFileToError(file);
                return;
            }
            
            dynamicTableService.createTableIfNotExists(
                feedConfig.getTableName(), 
                Arrays.asList(headers), 
                feedConfig.getIdColumn(),
                feedConfig.getSchema()
            );
            
            String jobName = feedConfig.getName() + "_" + System.currentTimeMillis();
            Job job = batchConfiguration.createFeedProcessingJob(
                jobName,
                file.getAbsolutePath(),
                feedConfig.getTableName(),
                feedConfig.getIdColumn(),
                feedConfig.getName(),
                file.getName(),
                feedConfig
            );
            
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("feedName", feedConfig.getName())
                    .addString("fileName", file.getName())
                    .addString("tableName", feedConfig.getTableName())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            
            jobLauncher.run(job, jobParameters);
            
            moveFileToProcessed(file);
            logger.info("Successfully processed file: {}", file.getName());
            
        } catch (Exception e) {
            logger.error("Error processing file {}: {}", file.getName(), e.getMessage(), e);
            moveFileToError(file);
        }
    }
    
    private List<File> findMatchingFiles(String pattern) {
        File inputDir = new File(directoryConfiguration.getInputDirectory());
        
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            logger.warn("Input directory does not exist: {}", directoryConfiguration.getInputDirectory());
            return Arrays.asList();
        }
        
        Pattern filePattern = Pattern.compile(pattern.replace("*", ".*"));
        
        File[] matchingFiles = inputDir.listFiles((dir, name) -> 
            filePattern.matcher(name).matches() && name.toLowerCase().endsWith(".csv"));
        
        return matchingFiles != null ? Arrays.asList(matchingFiles) : Arrays.asList();
    }
    
    private void createDirectoriesIfNotExist() {
        logger.info("Creating directories if they don't exist");
        logger.info("inputDirectory = {}", directoryConfiguration.getInputDirectory());
        logger.info("processedDirectory = {}", directoryConfiguration.getProcessedDirectory());
        logger.info("errorDirectory = {}", directoryConfiguration.getErrorDirectory());
        
        createDirectoryIfNotExist(directoryConfiguration.getInputDirectory());
        createDirectoryIfNotExist(directoryConfiguration.getProcessedDirectory());
        createDirectoryIfNotExist(directoryConfiguration.getErrorDirectory());
    }
    
    private void createDirectoryIfNotExist(String directory) {
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("Created directory: {}", directory);
            } catch (Exception e) {
                logger.error("Error creating directory {}: {}", directory, e.getMessage());
            }
        }
    }
    
    private void moveFileToProcessed(File file) {
        moveFile(file, directoryConfiguration.getProcessedDirectory());
    }
    
    private void moveFileToError(File file) {
        moveFile(file, directoryConfiguration.getErrorDirectory());
    }
    
    private void moveFile(File file, String targetDirectory) {
        try {
            Path sourcePath = file.toPath();
            Path targetPath = Paths.get(targetDirectory, file.getName());
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Moved file {} to {}", file.getName(), targetDirectory);
        } catch (Exception e) {
            logger.error("Error moving file {} to {}: {}", file.getName(), targetDirectory, e.getMessage());
        }
    }
}
