package com.example.feedprocessor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectoryConfiguration {
    
    @Value("${feed.processor.inputDirectory:input}")
    private String inputDirectory;
    
    @Value("${feed.processor.processedDirectory:processed}")
    private String processedDirectory;
    
    @Value("${feed.processor.errorDirectory:error}")
    private String errorDirectory;
    
    public String getInputDirectory() {
        return inputDirectory;
    }
    
    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }
    
    public String getProcessedDirectory() {
        return processedDirectory;
    }
    
    public void setProcessedDirectory(String processedDirectory) {
        this.processedDirectory = processedDirectory;
    }
    
    public String getErrorDirectory() {
        return errorDirectory;
    }
    
    public void setErrorDirectory(String errorDirectory) {
        this.errorDirectory = errorDirectory;
    }
}
