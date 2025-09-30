package com.example.feedprocessor.batch;

import com.example.feedprocessor.config.FeedConfiguration;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedItemReader implements ItemReader<Map<String, String>> {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedItemReader.class);
    
    private final String filePath;
    private final FeedConfiguration.FeedConfig feedConfig;
    private CSVReader csvReader;
    private String[] headers;
    private boolean initialized = false;
    
    public FeedItemReader(String filePath, FeedConfiguration.FeedConfig feedConfig) {
        this.filePath = filePath;
        this.feedConfig = feedConfig;
    }
    
    @Override
    public Map<String, String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!initialized) {
            initialize();
        }
        
        String[] line = csvReader.readNext();
        if (line == null) {
            return null;
        }
        
        Map<String, String> record = new HashMap<>();
        for (int i = 0; i < headers.length && i < line.length; i++) {
            record.put(headers[i], line[i] != null ? line[i].trim() : "");
        }
        
        return record;
    }
    
    private void initialize() throws IOException, CsvException {
        logger.info("Initializing CSV reader for file: {} with delimiter: '{}'", filePath, feedConfig.getDelimiter());
        
        char delimiter = feedConfig.getDelimiter().charAt(0);
        char quoteChar = feedConfig.getQuoteChar().charAt(0);
        char escapeChar = feedConfig.getEscapeChar().charAt(0);
        
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(delimiter)
                .withQuoteChar(quoteChar)
                .withEscapeChar(escapeChar)
                .build();
        
        FileReader fileReader = new FileReader(filePath);
        csvReader = new CSVReaderBuilder(fileReader)
                .withCSVParser(parser)
                .withSkipLines(feedConfig.isSkipHeaderRecord() ? 0 : 1)
                .build();
        
        headers = csvReader.readNext();
        
        if (headers == null || headers.length == 0) {
            throw new IllegalArgumentException("CSV file has no headers: " + filePath);
        }
        
        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].trim();
        }
        
        logger.info("Found {} columns in file: {}", headers.length, String.join(", ", headers));
        initialized = true;
    }
    
    public String[] getHeaders() {
        return headers;
    }
    
    public void close() {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException e) {
                logger.error("Error closing CSV reader: {}", e.getMessage());
            }
        }
    }
}
